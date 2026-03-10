package bg.sofia.uni.fmi.mjt.spotify.common.exception.network;

public class MissingClientChannelException extends NetworkException {
    public MissingClientChannelException(String message) {
        super(message);
    }

    public MissingClientChannelException(String message, Throwable throwable) {
        super(message, throwable);
    }
}
