package bg.sofia.uni.fmi.mjt.spotify.common.exception.network;

import bg.sofia.uni.fmi.mjt.spotify.common.exception.SpotifyException;

public class NetworkException extends SpotifyException {
    public NetworkException(String message) {
        super(message);
    }

    public NetworkException(String message, Throwable throwable) {
        super(message, throwable);
    }
}
