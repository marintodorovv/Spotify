package bg.sofia.uni.fmi.mjt.spotify.common.exception.validation;

public class BadSongTitleException  extends ValidationException {
    public BadSongTitleException(String message) {
        super(message);
    }

    public BadSongTitleException(String message, Throwable throwable) {
        super(message, throwable);
    }
}
