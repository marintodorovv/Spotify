package bg.sofia.uni.fmi.mjt.spotify.common.exception.playlist;

public class NoSuchPlaylistException extends PlaylistException {
    public NoSuchPlaylistException(String message) {
        super(message);
    }

    public NoSuchPlaylistException(String message, Throwable throwable) {
        super(message, throwable);
    }
}
