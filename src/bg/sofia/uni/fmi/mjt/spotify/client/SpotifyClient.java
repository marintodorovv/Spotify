package bg.sofia.uni.fmi.mjt.spotify.client;

import bg.sofia.uni.fmi.mjt.spotify.client.model.CommandResponseModel;
import bg.sofia.uni.fmi.mjt.spotify.client.streaming.AudioReceiveThread;
import bg.sofia.uni.fmi.mjt.spotify.common.CommandType;
import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UncheckedIOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public final class SpotifyClient {
    private static final String FAILED_TO_CONFIGURE_LOGGER_MESSAGE = "Failed to configure logger";
    private static final String FAILED_TO_LOG_IN_SOCKET_MESSAGE = "Failed to log in socket! :";


    private static final int SERVER_PORT = 6260;
    private static final String HOST = "localhost";

    private static final String CLIENT_LOGS_PATH = "client_logs.log";

    private static final String LOGIN_COMMAND = "login ";
    private static final String SPACE =  " ";
    private static final String SPACE_REGEX = " ";

    private static final char CARRIAGE_RETURN = '\r';
    private static final char NEW_LINE = '\n';
    private static final int ZERO = 0;
    private static final int MINUS_ONE = -1;
    private static final int LOGIN_COMMAND_PARTS = 3;

    private static String savedEmail = null;
    private static String savedPassword = null;

    private static final Gson GSON;

    private static final Logger LOGGER = Logger.getLogger(SpotifyClient.class.getName());

    private static AudioReceiveThread songReceiveThread;

    private static SocketChannel audioChannel;

    static {
        //Stoyan Velev said i would never use this
        GSON = new Gson();
        configureLogger();
    }

    private static void configureLogger() {
        try {
            FileHandler fileHandler = new FileHandler(CLIENT_LOGS_PATH, true);
            fileHandler.setFormatter(new SimpleFormatter());
            LOGGER.addHandler(fileHandler);
            LOGGER.setUseParentHandlers(false);
        } catch (IOException e) {
            throw new UncheckedIOException(FAILED_TO_CONFIGURE_LOGGER_MESSAGE, e);
        }
    }

    private static void initializeConnection(SocketChannel socketChannel) throws IOException {
        socketChannel.connect(new InetSocketAddress(HOST, SERVER_PORT));
        System.out.println("Successfully connected to Spotify Server!");

        songReceiveThread = null;
        audioChannel = null;
    }

    private static SocketChannel createAudioChannel() throws IOException {
        SocketChannel channel = SocketChannel.open();
        channel.connect(new InetSocketAddress(HOST, SERVER_PORT));

        PrintWriter writer = new PrintWriter(
                Channels.newWriter(channel, StandardCharsets.UTF_8), true
        );
        BufferedReader reader = new BufferedReader(
                Channels.newReader(channel, StandardCharsets.UTF_8)
        );

        writer.println(LOGIN_COMMAND + savedEmail + SPACE + savedPassword);
        String loginResponse = reader.readLine();

        CommandResponseModel response = GSON.fromJson(loginResponse, CommandResponseModel.class);
        if (!response.success()) {
            System.err.println(FAILED_TO_LOG_IN_SOCKET_MESSAGE + response.getFormattedResponse());
            channel.close();
            return null;
        }
        return channel;
    }

    private static void handleLogin(PrintWriter writer, String command, BufferedReader reader) throws IOException {
        String[] parts = command.split(SPACE_REGEX);
        if (parts.length >= LOGIN_COMMAND_PARTS) {
            savedEmail = parts[1];
            savedPassword = parts[2];
        }

        writer.println(command);
        String responseJson = reader.readLine();
        CommandResponseModel response = GSON.fromJson(responseJson, CommandResponseModel.class);

        if (!response.success()) {
            savedEmail = null;
            savedPassword = null;
            System.out.println("Command failed: " + response.getFormattedResponse());
        } else {
            System.out.println("Command succeeded: " + response.getFormattedResponse());
        }
    }

    private static String readLineFromChannel(SocketChannel channel) throws IOException {
        ByteBuffer buffer = ByteBuffer.allocate(1);
        StringBuilder line = new StringBuilder();

        while (true) {
            buffer.clear();
            int bytesRead = channel.read(buffer);

            if (bytesRead == MINUS_ONE) {
                return null;
            }

            if (bytesRead == ZERO) {
                continue;
            }

            buffer.flip();
            char c = (char)buffer.get();

            if (c == NEW_LINE) {
                break;
            }

            if (c != CARRIAGE_RETURN) {
                line.append(c);
            }
        }
        return line.toString();
    }

    private static void handlePlaySong(PrintWriter writer, String command, BufferedReader reader,
                                       SocketChannel socketChannel) throws IOException {
        if (savedEmail == null || savedPassword == null) {
            System.out.println("You must be logged in to do this!");
            return;
        }

        String responseJson = handleAudioChannel(command);
        if (responseJson == null) {
            return;
        }

        CommandResponseModel response = GSON.fromJson(responseJson, CommandResponseModel.class);

        if (!response.success()) {
            System.out.println("Playing song failed: " + response.getFormattedResponse());
            audioChannel.close();
            audioChannel = null;
            return;
        }

        songReceiveThread = new AudioReceiveThread(audioChannel);
        songReceiveThread.start();

        System.out.println("Playing song succeeded: " + response.getFormattedResponse());
    }

    private static String handleAudioChannel(String command) throws IOException {
        audioChannel = createAudioChannel();
        if (audioChannel == null) {
            System.out.println("Failed to establish connection, please try again later!");
            return null;
        }

        PrintWriter audioWriter = new PrintWriter(
                Channels.newWriter(audioChannel, StandardCharsets.UTF_8), true);
        audioWriter.println(command);

        String responseJson = readLineFromChannel(audioChannel);

        if (responseJson == null) {
            System.out.println("No response from server!");
            audioChannel.close();
            audioChannel = null;
            return null;
        }
        return responseJson;
    }

    private static void handleStopSong(PrintWriter writer, String command) throws InterruptedException, IOException {
        if (songReceiveThread != null && songReceiveThread.isAlive()) {

            songReceiveThread.stopPlayback();
            songReceiveThread.join();

            if (audioChannel != null && audioChannel.isOpen()) {
                audioChannel.close();
                audioChannel = null;
            }

            System.out.println("Song stopped!");
        } else {
            System.out.println("You are not playing a song!");
        }
    }

    private static boolean handleCommand(PrintWriter writer, String command, BufferedReader reader) throws IOException {
        writer.println(command);

        if (command.equals(CommandType.DISCONNECT.getCommandName())) {
            return false;
        }

        String responseJson = reader.readLine();

        CommandResponseModel response = GSON.fromJson(responseJson, CommandResponseModel.class);

        if (!response.success()) {
            System.out.println("Command failed: " + response.getFormattedResponse());
        } else {
            System.out.println("Command succeeded: " + response.getFormattedResponse());
        }
        return true;
    }

    private static void communicationLoop(Scanner scanner, PrintWriter writer, BufferedReader reader,
                                          SocketChannel socketChannel)
            throws IOException, InterruptedException {
        displayCommandOptions();

        while (true) {
            System.out.print("Input command: ");

            String command = scanner.nextLine();

            if (processClientSide(command)) {
                continue;
            }

            if (command.startsWith(CommandType.LOGIN.getCommandName())) {
                handleLogin(writer, command, reader);
            } else if (command.startsWith(CommandType.PLAY.getCommandName())) {
                handlePlaySong(writer, command, reader, socketChannel);
            } else if (command.startsWith(CommandType.STOP.getCommandName())) {
                handleStopSong(writer, command);
            } else {
                if (!handleCommand(writer, command, reader)) {
                    System.out.println("BYE!");
                    break;
                }
            }
        }
    }

    private static boolean processClientSide(String command) {
        if (command.isBlank()) {
            return true;
        }

        if (command.equals(CommandType.HELP.getCommandName())) {
            displayCommandOptions();
            return true;
        }

        if (command.startsWith(CommandType.PLAY.getCommandName())
                && songReceiveThread != null && songReceiveThread.isAlive()) {
            System.out.println("Stop playing current song, before playing another!");
            return true;
        }
        return false;
    }

    static void main(String[] args) {
        printSpotifyLogo();

        try (SocketChannel socketChannel = SocketChannel.open();
             BufferedReader reader = new BufferedReader(Channels.newReader(socketChannel, StandardCharsets.UTF_8));
             PrintWriter writer =
                     new PrintWriter(Channels.newWriter(socketChannel, StandardCharsets.UTF_8), true);
             Scanner scanner = new Scanner(System.in)) {

            initializeConnection(socketChannel);

            communicationLoop(scanner, writer, reader, socketChannel);

            if (songReceiveThread != null && songReceiveThread.isAlive()) {
                songReceiveThread.stopPlayback();
                songReceiveThread.join();
            }
            if (audioChannel != null && audioChannel.isOpen()) {
                audioChannel.close();
            }

        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "SpotifyClient encountered a network error!", e);
        } catch (InterruptedException e) {
            LOGGER.log(Level.SEVERE, "The SpotifyClient thread was unexpectedly interrupted!", e);
        }
    }

    private static void printSpotifyLogo() {
        System.out.println("|.........................................|");
        System.out.println("|                                         |");
        System.out.println("|                 SPOTIFY                 |");
        System.out.println("|                                         |");
        System.out.println("|.........................................|");
    }

    private static void displayCommandOptions() {
        System.out.println("|.........................................|");
        System.out.println("|                                         |");
        System.out.println("|            SPOTIFY COMMANDS             |");
        System.out.println("|                                         |");
        System.out.println("|.........................................|");
        System.out.println(System.lineSeparator());

        System.out.println("To register -> register <email> <password>");
        System.out.println("To login -> login <email> <password>");
        System.out.println("To search for a song -> search <keywords>");
        System.out.println("To see top streamed songs right now -> top <number>");
        System.out.println("To create a playlist -> create-playlist <name>");
        System.out.println("To add a song to a playlist -> add-song-to <playlist> <song>");
        System.out.println("To show all songs in a playlist -> show-playlist <name>");
        System.out.println("To play a song -> play <song name>");
        System.out.println("To stop playing a song -> stop");
        System.out.println("To disconnect -> disconnect");
        System.out.println("To see this menu again -> help");
    }
}
