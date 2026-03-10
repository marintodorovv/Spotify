package bg.sofia.uni.fmi.mjt.spotify.common.exception.validation;

import bg.sofia.uni.fmi.mjt.spotify.common.exception.SpotifyException;

public class ValidationException extends SpotifyException {
    public ValidationException(String message) {
        super(message);
    }

    public ValidationException(String message, Throwable throwable) {
        super(message, throwable);
    }
}
