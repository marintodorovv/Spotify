package bg.sofia.uni.fmi.mjt.spotify.common.exception.authentication;

public class UserDoesNotExistsException extends AuthenticationException {
    public UserDoesNotExistsException(String message) {
        super(message);
    }

    public UserDoesNotExistsException(String message, Throwable throwable) {
        super(message, throwable);
    }
}
