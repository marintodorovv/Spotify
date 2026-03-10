package bg.sofia.uni.fmi.mjt.spotify.common.exception.security;

import bg.sofia.uni.fmi.mjt.spotify.common.exception.SpotifyRuntimeException;

public class SecurityException extends SpotifyRuntimeException {
    public SecurityException(String message) {
        super(message);
    }

    public SecurityException(String message, Throwable throwable) {
        super(message, throwable);
    }
}
