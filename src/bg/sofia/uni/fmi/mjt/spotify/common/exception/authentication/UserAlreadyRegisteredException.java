package bg.sofia.uni.fmi.mjt.spotify.common.exception.authentication;

public class UserAlreadyRegisteredException extends AuthenticationException {
    public UserAlreadyRegisteredException(String message) {
        super(message);
    }

    public UserAlreadyRegisteredException(String message, Throwable throwable) {
        super(message, throwable);
    }
}
