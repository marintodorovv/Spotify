package bg.sofia.uni.fmi.mjt.spotify.common.exception.validation;

public class BadTopNException extends ValidationException {
    public BadTopNException(String message) {
        super(message);
    }

    public BadTopNException(String message, Throwable throwable) {
        super(message, throwable);
    }
}
