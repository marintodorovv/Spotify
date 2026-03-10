package bg.sofia.uni.fmi.mjt.spotify.client.streaming;

import bg.sofia.uni.fmi.mjt.spotify.common.AudioFormatDTO;
import bg.sofia.uni.fmi.mjt.spotify.common.exception.song.BadEncodingStringException;

import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import java.io.ByteArrayInputStream;
import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.UncheckedIOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.Arrays;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public final class AudioReceiveThread extends Thread {
    private static final String PLAYBACK_STOPPED_PER_REQUEST_MESSAGE = "PLAYBACK STOPPED PER REQUEST!";
    private static final String UNEXPECTED_END_OF_FILE_MESSAGE = "UNEXPECTED END OF FILE!";
    private static final String UNKNOWN_MESSAGE_TYPE_MESSAGE = "UNKNOWN MESSAGE TYPE!";
    private static final String SERVER_ERROR_MESSAGE = "SERVER ENCOUNTERED AN ERROR: ";
    private static final String RECEIVED_END_MESSAGE_FROM_SERVER_MESSAGE = "RECEIVED END MESSAGE!";
    private static final String AUDIO_LINE_STARTED_MESSAGE = "STARTED AUDIO LINE!";
    private static final String UNSUPPORTED_AUDIO_LINE_MESSAGE = "Audio line not supported";
    private static final String RECEIVED_AUDIO_FORMAT_DTO_MESSAGE = "GOT DTO FOR AUDIO FORMAT!";
    private static final String RECEIVED_WRONG_AUDIO_FORMAT_MESSAGE = "WRONG AUDIO FORMAT MESSAGE: EXPECTED 1, " +
            "RECEIVED: ";
    private static final String STOPPED_THREAD_MESSAGE = "AUDIORECEIVETHREAD STOPPED!";
    private static final String AUDIO_PLAYBACK_FAILED_MESSAGE = "AUDIO PLAYBACK FAILED!";
    private static final String STARTED_THREAD_MESSAGE = "STARTED AUDIORECEIVETHREAD!";
    private static final String FAILED_TO_CONFIGURE_LOGGER_MESSAGE = "Failed to configure logger";

    private static final String THREAD_NAME = "AudioReceiveThread";

    private static final String RECEIVE_THREAD_LOGS_PATH = "client_logs.log";

    private static final int BUFFER_SIZE = 4;
    private static final int MESSAGE_TYPE_AUDIO_FORMAT = 1;
    private static final int MESSAGE_TYPE_AUDIO_DATA = 2;
    private static final int MESSAGE_TYPE_END = 3;
    private static final int MESSAGE_TYPE_ERROR = 4;

    private static final int MINIMUM_VALID_CHUNK_SIZE = 0;
    private static final int OFFSET = 0;
    private static final int MESSAGE_TYPE_BUFFER_SIZE = 1;
    private static final int INVALID_BYTES = -1;


    private final SocketChannel serverChannel;
    private volatile boolean shouldStop = false;

    private SourceDataLine dataLine;

    private static final Logger LOGGER = Logger.getLogger(AudioReceiveThread.class.getName());

    static {
        configureLogger();
    }

    public AudioReceiveThread(SocketChannel serverChannel) throws IOException {
        this.serverChannel = serverChannel;
        serverChannel.configureBlocking(true);

        this.setDaemon(true);
        this.setName(THREAD_NAME);
    }

    private static void configureLogger() {
        try {
            FileHandler fileHandler = new FileHandler(RECEIVE_THREAD_LOGS_PATH, true);
            fileHandler.setFormatter(new SimpleFormatter());
            LOGGER.addHandler(fileHandler);
            LOGGER.setUseParentHandlers(false);
        } catch (IOException e) {
            throw new UncheckedIOException(FAILED_TO_CONFIGURE_LOGGER_MESSAGE, e);
        }
    }

    @Override
    public void run() {
        LOGGER.info(STARTED_THREAD_MESSAGE);

        try {
            AudioFormat audioFormat = receiveAudioFormat();
            if (audioFormat == null) {
                return;
            }

            dataLine = createAndStartDataLine(audioFormat);

            receiveAndPlayAudio();

            if (dataLine != null) {
                dataLine.drain();
                dataLine.stop();
                dataLine.close();
            }
        } catch (Exception e) {
            LOGGER.severe(AUDIO_PLAYBACK_FAILED_MESSAGE + Arrays.toString(e.getStackTrace()));
        } finally {
            closeDataline();
            LOGGER.info(STOPPED_THREAD_MESSAGE);
        }
    }

    private void closeDataline() {
        if (dataLine != null) {
            dataLine.drain();
            dataLine.stop();
            dataLine.close();
        }
    }

    private AudioFormat receiveAudioFormat() throws IOException, BadEncodingStringException, ClassNotFoundException {
        ByteBuffer typeBuffer = ByteBuffer.allocate(MESSAGE_TYPE_BUFFER_SIZE);
        readFully(typeBuffer);
        typeBuffer.flip();
        byte messageType = typeBuffer.get();

        if (messageType != MESSAGE_TYPE_AUDIO_FORMAT) {
            LOGGER.severe(RECEIVED_WRONG_AUDIO_FORMAT_MESSAGE + messageType);
            return null;
        }

        ByteBuffer sizeBuffer = ByteBuffer.allocate(BUFFER_SIZE);
        readFully(sizeBuffer);
        sizeBuffer.flip();

        byte[] dtoBytes = new byte[sizeBuffer.getInt()];
        ByteBuffer dtoBuffer = ByteBuffer.wrap(dtoBytes);
        readFully(dtoBuffer);

        try (ByteArrayInputStream bais = new ByteArrayInputStream(dtoBytes);
             ObjectInputStream ois = new ObjectInputStream(bais)) {
            AudioFormatDTO dto = (AudioFormatDTO)ois.readObject();

            LOGGER.info(RECEIVED_AUDIO_FORMAT_DTO_MESSAGE);

            return dto.toAudioFormat();
        }
    }

    private SourceDataLine createAndStartDataLine(AudioFormat format)
            throws LineUnavailableException {
        DataLine.Info info = new DataLine.Info(SourceDataLine.class, format);

        if (!AudioSystem.isLineSupported(info)) {
            LOGGER.severe(UNSUPPORTED_AUDIO_LINE_MESSAGE);
            throw new LineUnavailableException(UNSUPPORTED_AUDIO_LINE_MESSAGE);
        }

        SourceDataLine line = (SourceDataLine)AudioSystem.getLine(info);
        line.open(format);
        line.start();

        LOGGER.info(AUDIO_LINE_STARTED_MESSAGE);

        return line;
    }

    private void receiveAndPlayAudio() throws IOException {
        ByteBuffer typeBuffer = ByteBuffer.allocate(MESSAGE_TYPE_BUFFER_SIZE);
        ByteBuffer sizeBuffer = ByteBuffer.allocate(BUFFER_SIZE);
        while (!shouldStop) {
            typeBuffer.clear();
            readFully(typeBuffer);
            typeBuffer.flip();
            byte messageType = typeBuffer.get();

            sizeBuffer.clear();
            readFully(sizeBuffer);
            sizeBuffer.flip();
            int chunkSize = sizeBuffer.getInt();

            if (handleMessageType(messageType, chunkSize)) {
                break;
            }
        }
    }

    private boolean handleMessageType(byte messageType, int chunkSize) throws IOException {
        if (messageType == MESSAGE_TYPE_AUDIO_DATA) {
            if (chunkSize > MINIMUM_VALID_CHUNK_SIZE) {
                byte[] audioData = new byte[chunkSize];
                ByteBuffer dataBuffer = ByteBuffer.wrap(audioData);
                readFully(dataBuffer);

                dataLine.write(audioData, OFFSET, chunkSize);
            }

        } else if (messageType == MESSAGE_TYPE_END) {
            LOGGER.info(RECEIVED_END_MESSAGE_FROM_SERVER_MESSAGE);
            return true;
        } else if (messageType == MESSAGE_TYPE_ERROR) {
            if (chunkSize > MINIMUM_VALID_CHUNK_SIZE) {
                byte[] errorData = new byte[chunkSize];
                ByteBuffer errorBuffer = ByteBuffer.wrap(errorData);
                readFully(errorBuffer);

                String errorMessage = new String(errorData);
                LOGGER.severe(SERVER_ERROR_MESSAGE + errorMessage);
            }
            return true;

        } else {
            LOGGER.warning(UNKNOWN_MESSAGE_TYPE_MESSAGE);
            return true;
        }
        return false;
    }

    private void readFully(ByteBuffer buffer) throws IOException {
        while (buffer.hasRemaining()) {
            int bytesRead = serverChannel.read(buffer);
            if (bytesRead == INVALID_BYTES) {
                throw new EOFException(UNEXPECTED_END_OF_FILE_MESSAGE);
            }
        }
    }

    public void stopPlayback() {
        shouldStop = true;
        this.interrupt();

        if (dataLine != null && dataLine.isOpen()) {
            dataLine.stop();
            dataLine.flush();
        }

        LOGGER.info(PLAYBACK_STOPPED_PER_REQUEST_MESSAGE);
    }
}
