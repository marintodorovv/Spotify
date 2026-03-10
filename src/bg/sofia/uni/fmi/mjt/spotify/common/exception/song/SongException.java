package bg.sofia.uni.fmi.mjt.spotify.common.exception.song;

import bg.sofia.uni.fmi.mjt.spotify.common.exception.SpotifyException;

public class SongException extends SpotifyException {
    public SongException(String message) {
        super(message);
    }

    public SongException(String message, Throwable throwable) {
        super(message, throwable);
    }
}
