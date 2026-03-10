package bg.sofia.uni.fmi.mjt.spotify.common.exception.authentication;

public class WrongPasswordException extends AuthenticationException {
    public WrongPasswordException(String message) {
        super(message);
    }

    public WrongPasswordException(String message, Throwable throwable) {
        super(message, throwable);
    }
}
