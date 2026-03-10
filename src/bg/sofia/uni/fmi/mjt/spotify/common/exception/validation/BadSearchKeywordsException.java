package bg.sofia.uni.fmi.mjt.spotify.common.exception.validation;

public class BadSearchKeywordsException extends ValidationException {
    public BadSearchKeywordsException(String message) {
        super(message);
    }

    public BadSearchKeywordsException(String message, Throwable throwable) {
        super(message, throwable);
    }
}
