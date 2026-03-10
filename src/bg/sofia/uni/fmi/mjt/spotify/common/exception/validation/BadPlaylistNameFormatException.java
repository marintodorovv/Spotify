package bg.sofia.uni.fmi.mjt.spotify.common.exception.validation;

public class BadPlaylistNameFormatException extends ValidationException {
    public BadPlaylistNameFormatException(String message) {
        super(message);
    }

    public BadPlaylistNameFormatException(String message, Throwable throwable) {
        super(message, throwable);
    }
}
