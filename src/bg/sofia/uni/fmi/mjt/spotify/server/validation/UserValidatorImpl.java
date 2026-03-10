package bg.sofia.uni.fmi.mjt.spotify.server.validation;

import bg.sofia.uni.fmi.mjt.spotify.common.exception.validation.BadEmailFormatException;
import bg.sofia.uni.fmi.mjt.spotify.common.exception.validation.BadPasswordFormatException;

public final class UserValidatorImpl implements UserValidator {
    @Override
    public void validateRegister(String email, String password) throws BadEmailFormatException,
            BadPasswordFormatException {
        EmailValidator.isValidEmail(email);
        PasswordValidator.isValidPassword(password);
    }

    @Override
    public void validateAuthenticate(String email, String password) throws BadEmailFormatException,
            BadPasswordFormatException {
        EmailValidator.isValidEmail(email);
        PasswordValidator.isValidPassword(password);
    }
}
