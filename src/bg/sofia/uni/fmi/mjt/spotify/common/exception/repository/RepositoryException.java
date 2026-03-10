package bg.sofia.uni.fmi.mjt.spotify.common.exception.repository;

import bg.sofia.uni.fmi.mjt.spotify.common.exception.SpotifyRuntimeException;

public class RepositoryException extends SpotifyRuntimeException {
    public RepositoryException(String message) {
        super(message);
    }

    public RepositoryException(String message, Throwable throwable) {
        super(message, throwable);
    }
}
