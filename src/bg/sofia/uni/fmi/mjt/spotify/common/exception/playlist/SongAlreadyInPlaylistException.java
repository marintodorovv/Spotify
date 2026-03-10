package bg.sofia.uni.fmi.mjt.spotify.common.exception.playlist;

public class SongAlreadyInPlaylistException extends PlaylistException {
    public SongAlreadyInPlaylistException(String message) {
        super(message);
    }

    public SongAlreadyInPlaylistException(String message, Throwable throwable) {
        super(message, throwable);
    }
}
