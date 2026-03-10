package bg.sofia.uni.fmi.mjt.spotify.common.exception.playlist;

public class PlaylistAlreadyExistsException extends PlaylistException {
    public PlaylistAlreadyExistsException(String message) {
        super(message);
    }

    public PlaylistAlreadyExistsException(String message, Throwable throwable) {
        super(message, throwable);
    }
}
