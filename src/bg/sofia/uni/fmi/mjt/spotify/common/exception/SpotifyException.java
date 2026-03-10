package bg.sofia.uni.fmi.mjt.spotify.common.exception;

public class SpotifyException extends Exception {
    public SpotifyException(String message) {
        super(message);
    }

    public SpotifyException(String message, Throwable throwable) {
        super(message, throwable);
    }
}
