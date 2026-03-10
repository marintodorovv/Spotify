package bg.sofia.uni.fmi.mjt.spotify.common.exception;

public class SpotifyRuntimeException extends RuntimeException {
    public SpotifyRuntimeException(String message) {
        super(message);
    }

    public SpotifyRuntimeException(String message, Throwable throwable) {
        super(message, throwable);
    }
}
