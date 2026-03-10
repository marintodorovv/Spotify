package bg.sofia.uni.fmi.mjt.spotify.server;

import bg.sofia.uni.fmi.mjt.spotify.common.exception.network.MissingClientChannelException;
import bg.sofia.uni.fmi.mjt.spotify.common.exception.song.NoSongStreamingException;
import bg.sofia.uni.fmi.mjt.spotify.server.command.helpers.CommandExecutor;
import bg.sofia.uni.fmi.mjt.spotify.server.command.helpers.CommandFactory;
import bg.sofia.uni.fmi.mjt.spotify.server.context.ConnectionContext;
import bg.sofia.uni.fmi.mjt.spotify.server.context.UserContext;
import bg.sofia.uni.fmi.mjt.spotify.server.reponse.CommandResponse;
import bg.sofia.uni.fmi.mjt.spotify.server.repository.FileSongRepository;
import bg.sofia.uni.fmi.mjt.spotify.server.repository.JsonPlaylistRepository;
import bg.sofia.uni.fmi.mjt.spotify.server.repository.JsonUserRepository;
import bg.sofia.uni.fmi.mjt.spotify.server.repository.PlaylistRepository;
import bg.sofia.uni.fmi.mjt.spotify.server.repository.SongRepository;
import bg.sofia.uni.fmi.mjt.spotify.server.repository.UserRepository;
import bg.sofia.uni.fmi.mjt.spotify.server.request.CommandRequest;
import bg.sofia.uni.fmi.mjt.spotify.server.service.PlaylistService;
import bg.sofia.uni.fmi.mjt.spotify.server.service.PlaylistServiceImpl;
import bg.sofia.uni.fmi.mjt.spotify.server.service.SongService;
import bg.sofia.uni.fmi.mjt.spotify.server.service.SongServiceImpl;
import bg.sofia.uni.fmi.mjt.spotify.server.service.UserService;
import bg.sofia.uni.fmi.mjt.spotify.server.service.UserServiceImpl;
import bg.sofia.uni.fmi.mjt.spotify.server.service.streaming.StreamingService;
import bg.sofia.uni.fmi.mjt.spotify.server.service.streaming.StreamingServiceImpl;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;
import java.util.Set;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public final class SpotifyServer {
    private static final int SERVER_PORT = 6260;
    private static final String HOST = "localhost";

    private static final int BUFFER_SIZE = 1024;

    private static final String SERVER_STARTED_MESSAGE = "STARTED SERVER!";
    private static final String SERVER_INITIALIZED_MESSAGE = "INITIALIZED SERVER!";
    private static final String COMMAND_EXECUTOR_INITIALIZED_MESSAGE = "INITIALIZED COMMAND EXECUTOR!";
    private static final String SERVICES_INITIALIZED_MESSAGE = "INITIALIZED SERVICES!";
    private static final String REPOSITORIES_INITIALIZED_MESSAGE = "INITIALIZED REPOSITORIES!";
    private static final String CLIENT_DISCONNECTED_FORCEFULLY_MESSAGE
            = "SWALLOWED EXCEPTION: CLIENT DISCONNECTED FORCEFULLY!";
    private static final String ACCEPTING_CONNECTION_MESSAGE = "ACCEPTING CONNECTION!";
    private static final String CONFIGURING_SERVER_SOCKET_MESSAGE = "CONFIGURING SERVER SOCKET!";
    private static final String FAILED_TO_CONFIGURE_LOGGER_MESSAGE = "Failed to configure logger";
    private static final String FAILED_TO_START_SERVER_MESSAGE = "Failed to start server!";
    private static final String CALLING_SERVER_WORKING_LOOP_MESSAGE = "CALLING SERVER WORKING LOOP!";
    private static final String HANDLING_READ_MESSAGE = "HANDLING READ FROM CLIENT!";
    private static final String ENCOUNTERED_UNKNOWN_ERROR_WHILE_HANDLING_CLIENT_DISCONNECT_MESSAGE =
            "ENCOUNTERED UNKNOWN ERROR WHILE HANDLING CLIENT DISCONNECT: ";
    private static final String WRITING_TO_CLIENT_MESSAGE = "WRITING TO CLIENT!";
    private static final String CLIENT_CLEAN_UP_FAILED_DUE_TO_MISSING_CHANNEL_MESSAGE =
            "CLIENT CLEAN UP FAILED DUE TO MISSING CHANNEL!";

    private static final String SONGS_DIRECTORY = "data";
    private static final String PLAYLISTS_JSON_DIRECTORY = "data/playlists.json";
    private static final String USERS_JSON_DIRECTORY = "data/users.json";
    private static final String SPOTIFY_SERVER_LOGS_PATH = "spotify_server_logs.log";
    private static final String CLIENT_CLEAN_UP_UNNEEDED_MESSAGE = "CLIENT CLEAN UP UNNEEDED!";

    private final int port;

    private final CommandExecutor commandExecutor;
    private final StreamingService streamingService;

    private static final Logger LOGGER = Logger.getLogger(SpotifyServer.class.getName());

    private boolean isServerWorking;
    private Selector selector;

    static {
        //Stoyan Velev said i would never use this
        configureLogger();
    }

    public SpotifyServer(int port, CommandExecutor commandExecutor, StreamingService streamingService) {
        this.port = port;
        this.commandExecutor = commandExecutor;
        this.streamingService = streamingService;
    }

    public void start() {
        try (ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
             Selector selector = Selector.open()) {

            this.selector = selector;

            configureServerSocket(serverSocketChannel);

            isServerWorking = true;

            LOGGER.info(CALLING_SERVER_WORKING_LOOP_MESSAGE);

            serverWorkingLoop(selector);
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, FAILED_TO_START_SERVER_MESSAGE, e);
            throw new UncheckedIOException(FAILED_TO_START_SERVER_MESSAGE, e);
        }
    }

    private static void configureLogger() {
        try {
            FileHandler fileHandler = new FileHandler(SPOTIFY_SERVER_LOGS_PATH, true);
            fileHandler.setFormatter(new SimpleFormatter());
            LOGGER.addHandler(fileHandler);
            LOGGER.setUseParentHandlers(true);
        } catch (IOException e) {
            throw new UncheckedIOException(FAILED_TO_CONFIGURE_LOGGER_MESSAGE, e);
        }
    }

    private void serverWorkingLoop(Selector selector) throws IOException {
        while (isServerWorking) {
            int readyChannels = selector.select();

            if (readyChannels == 0) {
                continue;
            }

            Set<SelectionKey> selectedKeys = selector.selectedKeys();
            Iterator<SelectionKey> keyIterator = selectedKeys.iterator();

            while (keyIterator.hasNext()) {
                SelectionKey key = keyIterator.next();
                keyIterator.remove();

                if (!key.isValid()) {
                    continue;
                }

                if (key.isAcceptable()) {
                    acceptConnection(key);
                } else if (key.isReadable()) {
                    handleRead(key);
                }
            }
        }
    }

    private void configureServerSocket(ServerSocketChannel serverChannel) throws IOException {
        LOGGER.info(CONFIGURING_SERVER_SOCKET_MESSAGE);

        serverChannel.bind(new InetSocketAddress(HOST, port));
        serverChannel.configureBlocking(false);
        serverChannel.register(selector, SelectionKey.OP_ACCEPT);
    }

    public void stop() {

        isServerWorking = false;

        streamingService.stopAllStreams();

        try {
            if (selector != null && selector.isOpen()) {
                selector.close();
            }
        } catch (IOException e) {
            //No logging here as the filehandler is shut down at this point and will just throw exception
        }
    }

    private void acceptConnection(SelectionKey key) throws IOException {
        LOGGER.info(ACCEPTING_CONNECTION_MESSAGE);

        ServerSocketChannel serverChannel = (ServerSocketChannel)key.channel();
        SocketChannel clientChannel = serverChannel.accept();

        if (clientChannel == null) {
            return;
        }

        clientChannel.configureBlocking(false);

        ConnectionContext context = new ConnectionContext(BUFFER_SIZE);
        clientChannel.register(selector, SelectionKey.OP_READ, context);
    }

    private void handleRead(SelectionKey key) {
        LOGGER.info(HANDLING_READ_MESSAGE);
        SocketChannel clientChannel = (SocketChannel) key.channel();
        ConnectionContext connectionContext = (ConnectionContext) key.attachment();
        ByteBuffer buffer = connectionContext.getBuffer();
        UserContext userContext = connectionContext.getContext();

        try {
            handleBuffer(key, buffer, clientChannel, userContext);
        } catch (IOException e) {
            handleClientDisconnect(key, clientChannel);
        }
    }

    private void handleBuffer(SelectionKey key, ByteBuffer buffer, SocketChannel clientChannel,
                              UserContext userContext) throws IOException {
        buffer.clear();
        int readBytes = clientChannel.read(buffer);
        if (readBytes < 0) {
            try {
                streamingService.stopStreaming(clientChannel);
            } catch (MissingClientChannelException e) {
                LOGGER.info(CLIENT_CLEAN_UP_FAILED_DUE_TO_MISSING_CHANNEL_MESSAGE);
            } catch (NoSongStreamingException e) {
                LOGGER.info(CLIENT_CLEAN_UP_UNNEEDED_MESSAGE);
            }

            clientChannel.close();
            key.cancel();
            return;
        }

        if (readBytes == 0) {
            return;
        }

        buffer.flip();
        byte[] clientInputBytes = new byte[buffer.remaining()];
        buffer.get(clientInputBytes);
        String clientInput = new String(clientInputBytes, StandardCharsets.UTF_8);

        CommandRequest request = CommandRequest.parse(clientInput, clientChannel);
        CommandResponse response = commandExecutor.execute(request, userContext);
        writeClientOutput(clientChannel, response.toString(), buffer);
    }

    private void handleClientDisconnect(SelectionKey key, SocketChannel clientChannel) {
        LOGGER.info(CLIENT_DISCONNECTED_FORCEFULLY_MESSAGE);

        try {
            streamingService.stopStreaming(clientChannel);
        } catch (MissingClientChannelException e) {
            LOGGER.info(CLIENT_CLEAN_UP_FAILED_DUE_TO_MISSING_CHANNEL_MESSAGE);
        } catch (NoSongStreamingException e) {
            LOGGER.info(CLIENT_CLEAN_UP_UNNEEDED_MESSAGE);
        }

        key.cancel();
        try {
            clientChannel.close();
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, ENCOUNTERED_UNKNOWN_ERROR_WHILE_HANDLING_CLIENT_DISCONNECT_MESSAGE, e);
        }
    }

    private void writeClientOutput(SocketChannel clientChannel, String output, ByteBuffer buffer) throws IOException {
        LOGGER.info(WRITING_TO_CLIENT_MESSAGE);
        String terminatedOutput = output + System.lineSeparator();

        buffer.clear();
        buffer.put(terminatedOutput.getBytes(StandardCharsets.UTF_8));
        buffer.flip();

        while (buffer.hasRemaining()) {
            clientChannel.write(buffer);
        }
    }

    static void main(String[] args) {
        UserRepository userRepository = new JsonUserRepository(USERS_JSON_DIRECTORY);
        PlaylistRepository playlistRepository = new JsonPlaylistRepository(PLAYLISTS_JSON_DIRECTORY);
        SongRepository songRepository = new FileSongRepository(SONGS_DIRECTORY);

        LOGGER.info(REPOSITORIES_INITIALIZED_MESSAGE);

        UserService userService = new UserServiceImpl(userRepository);
        PlaylistService playlistService = new PlaylistServiceImpl(playlistRepository, songRepository);
        SongService songService = new SongServiceImpl(songRepository);
        StreamingService streamingService = new StreamingServiceImpl(songRepository, SONGS_DIRECTORY);

        LOGGER.info(SERVICES_INITIALIZED_MESSAGE);

        CommandExecutor commandExecutor = CommandFactory.createExecutor(
                userService, playlistService, songService, streamingService);

        LOGGER.info(COMMAND_EXECUTOR_INITIALIZED_MESSAGE);

        SpotifyServer server = new SpotifyServer(SERVER_PORT, commandExecutor, streamingService);

        LOGGER.info(SERVER_INITIALIZED_MESSAGE);

        //Hook for graceful stop
        Runtime.getRuntime().addShutdownHook(new Thread(server::stop));

        server.start();

        LOGGER.info(SERVER_STARTED_MESSAGE);
    }
}
