package bg.sofia.uni.fmi.mjt.spotify.server.streaming;

import bg.sofia.uni.fmi.mjt.spotify.common.AudioFormatDTO;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.UncheckedIOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public final class AudioStreamThread extends Thread {
    private static final String FAILED_TO_CONFIGURE_LOGGER_MESSAGE = "Failed to configure logger";
    private static final String UNEXPECTED_ERROR_WHILE_SENDING_ERROR = "Encountered an error while sending error!";
    private static final String UNEXPECTED_ERROR_WHILE_STREAMING = "Encountered an error while streaming!";
    private static final String AUDIO_STREAM_THREAD_FINISHED_STREAMING_MESSAGE =
            "AudioStreamThread finished streaming!";
    private static final String AUDIO_STREAM_THREAD_STARTED_STREAMING_MESSAGE = "AudioStreamThread started streaming!";
    private static final String SONG_IS_CURRENTLY_UNAVAILABLE_FOR_STREAMING_MESSAGE =
            "This song is currently unavailable for streaming!";
    private static final String NO_SUCH_SONG_EXISTS_MESSAGE = "NO SUCH SONG EXISTS!";

    private static final String STREAM_THREAD_NAME = "AudioStreamThread - ";
    private static final String DASH = " - ";

    private static final String STREAM_THREAD_LOGS_PATH = "audio_stream_thread_logs.log";
    private static final int SERVER_WAIT = 500;

    private final SocketChannel clientChannel;
    private final String songFilePath;
    private final String songTitle;

    private volatile boolean shouldStop = false;

    private static final int BUFFER_SIZE = 4096;

    private static final Logger LOGGER = Logger.getLogger(AudioStreamThread.class.getName());

    private static final int MESSAGE_TYPE_AUDIO_FORMAT = 1;
    private static final int MESSAGE_TYPE_AUDIO_DATA = 2;
    private static final int MESSAGE_TYPE_END = 3;
    private static final int MESSAGE_TYPE_ERROR = 4;

    private static final int INVALID_BYTES = -1;
    private static final int MESSAGE_TYPE_BUFFER_SIZE = 5;
    private static final int ZERO = 0;

    static {
        //Stoyan Velev said i would never use this
        configureLogger();
    }

    public AudioStreamThread(SocketChannel clientChannel, String songFilePath, String songTitle) {
        this.clientChannel = clientChannel;
        this.songFilePath = songFilePath;
        this.songTitle = songTitle;

        this.setDaemon(true);
        this.setName(STREAM_THREAD_NAME + songFilePath + DASH + clientChannel.toString());
    }

    //Prevent a race condition here, quite ugly and not a complete fix as client can still take longer
    //than 500ms, but this is the best i can do
    //maybe can be properly fixed by having the client send something when ready, but no time
    @Override
    public void run() {
        try {
            File songFile = new File(songFilePath);
            if (!songFile.exists()) {
                LOGGER.severe(NO_SUCH_SONG_EXISTS_MESSAGE);
                sendError(SONG_IS_CURRENTLY_UNAVAILABLE_FOR_STREAMING_MESSAGE);
                return;
            }

            try (AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(songFile)) {
                AudioFormat audioFormat = audioInputStream.getFormat();
                Thread.sleep(SERVER_WAIT);
                sendAudioFormat(audioFormat);

                LOGGER.info(AUDIO_STREAM_THREAD_STARTED_STREAMING_MESSAGE);

                streamAudioData(audioInputStream);
            }

            LOGGER.info(AUDIO_STREAM_THREAD_FINISHED_STREAMING_MESSAGE);

        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, UNEXPECTED_ERROR_WHILE_STREAMING, e);
            try {
                sendError(UNEXPECTED_ERROR_WHILE_STREAMING);
            } catch (IOException ex) {
                LOGGER.log(Level.SEVERE, UNEXPECTED_ERROR_WHILE_SENDING_ERROR, e);
            }
        }
    }

    private static void configureLogger() {
        try {
            FileHandler fileHandler = new FileHandler(STREAM_THREAD_LOGS_PATH, true);
            fileHandler.setFormatter(new SimpleFormatter());
            LOGGER.addHandler(fileHandler);
            LOGGER.setUseParentHandlers(true);
        } catch (IOException e) {
            throw new UncheckedIOException(FAILED_TO_CONFIGURE_LOGGER_MESSAGE, e);
        }
    }

    private void sendAudioFormat(AudioFormat format) throws IOException {
        AudioFormatDTO dto = AudioFormatDTO.toDTO(format);

        try (ByteArrayOutputStream baos = new ByteArrayOutputStream();
             ObjectOutputStream oos = new ObjectOutputStream(baos)) {
            oos.writeObject(dto);
            oos.flush();
            byte[] dtoBytes = baos.toByteArray();

            ByteBuffer buffer = ByteBuffer.allocate(MESSAGE_TYPE_BUFFER_SIZE + dtoBytes.length);
            buffer.put((byte)MESSAGE_TYPE_AUDIO_FORMAT);
            buffer.putInt(dtoBytes.length);
            buffer.put(dtoBytes);
            buffer.flip();

            while (buffer.hasRemaining()) {
                clientChannel.write(buffer);
            }
        }
    }

    private void streamAudioData(AudioInputStream audioInputStream) throws IOException {
        byte[] audioBuffer = new byte[BUFFER_SIZE];
        int bytesRead;

        while (!shouldStop && (bytesRead = audioInputStream.read(audioBuffer)) != INVALID_BYTES) {
            ByteBuffer headerBuffer = ByteBuffer.allocate(MESSAGE_TYPE_BUFFER_SIZE);
            headerBuffer.put((byte)MESSAGE_TYPE_AUDIO_DATA);
            headerBuffer.putInt(bytesRead);
            headerBuffer.flip();

            while (headerBuffer.hasRemaining()) {
                clientChannel.write(headerBuffer);
            }

            ByteBuffer dataBuffer = ByteBuffer.wrap(audioBuffer, ZERO, bytesRead);
            while (dataBuffer.hasRemaining()) {
                clientChannel.write(dataBuffer);
            }
        }

        if (!shouldStop) {
            sendEndOfStream();
        }
    }

    private void sendEndOfStream() throws IOException {
        ByteBuffer buffer = ByteBuffer.allocate(MESSAGE_TYPE_BUFFER_SIZE);
        buffer.put((byte)MESSAGE_TYPE_END);
        buffer.putInt(ZERO);
        buffer.flip();

        while (buffer.hasRemaining()) {
            clientChannel.write(buffer);
        }
    }

    private void sendError(String errorMessage) throws IOException {
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream();
             DataOutputStream dos = new DataOutputStream(baos)) {
            dos.writeByte(MESSAGE_TYPE_ERROR);
            dos.writeInt(errorMessage.length());
            dos.writeBytes(errorMessage);

            byte[] errorData = baos.toByteArray();
            ByteBuffer buffer = ByteBuffer.wrap(errorData);

            while (buffer.hasRemaining()) {
                clientChannel.write(buffer);
            }
        }
    }

    public void stopStreaming() {
        shouldStop = true;
    }

    public String getSongTitle() {
        return songTitle;
    }
}
