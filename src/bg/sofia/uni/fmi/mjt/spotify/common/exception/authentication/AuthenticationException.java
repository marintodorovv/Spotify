package bg.sofia.uni.fmi.mjt.spotify.common.exception.authentication;

import bg.sofia.uni.fmi.mjt.spotify.common.exception.SpotifyException;

public class AuthenticationException extends SpotifyException {
    public AuthenticationException(String message) {
        super(message);
    }

    public AuthenticationException(String message, Throwable throwable) {
        super(message, throwable);
    }
}
