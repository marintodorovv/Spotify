package bg.sofia.uni.fmi.mjt.spotify.common.exception.authentication;

public class UserNotLoggedInException extends AuthenticationException {
    public UserNotLoggedInException(String message) {
        super(message);
    }

    public UserNotLoggedInException(String message, Throwable throwable) {
        super(message, throwable);
    }
}
