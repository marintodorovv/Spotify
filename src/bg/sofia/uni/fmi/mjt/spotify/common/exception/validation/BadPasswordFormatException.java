package bg.sofia.uni.fmi.mjt.spotify.common.exception.validation;

public class BadPasswordFormatException extends ValidationException {
    public BadPasswordFormatException(String message) {
        super(message);
    }

    public BadPasswordFormatException(String message, Throwable throwable) {
        super(message, throwable);
    }
}
