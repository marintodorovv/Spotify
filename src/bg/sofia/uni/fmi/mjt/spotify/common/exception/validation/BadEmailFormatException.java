package bg.sofia.uni.fmi.mjt.spotify.common.exception.validation;

public class BadEmailFormatException extends ValidationException {
    public BadEmailFormatException(String message) {
        super(message);
    }

    public BadEmailFormatException(String message, Throwable throwable) {
        super(message, throwable);
    }
}
