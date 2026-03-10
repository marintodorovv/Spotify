package bg.sofia.uni.fmi.mjt.spotify.common.exception.playlist;

import bg.sofia.uni.fmi.mjt.spotify.common.exception.SpotifyException;

public class PlaylistException extends SpotifyException {
    public PlaylistException(String message) {
        super(message);
    }

    public PlaylistException(String message, Throwable throwable) {
        super(message, throwable);
    }
}
