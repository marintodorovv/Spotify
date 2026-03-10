package bg.sofia.uni.fmi.mjt.spotify.server.validation;

import bg.sofia.uni.fmi.mjt.spotify.common.exception.validation.BadEmailFormatException;
import bg.sofia.uni.fmi.mjt.spotify.common.exception.validation.BadPasswordFormatException;

public interface UserValidator extends Validator {
    void validateRegister(String email, String password)
            throws BadEmailFormatException, BadPasswordFormatException;

    void validateAuthenticate(String email, String password)
            throws BadEmailFormatException, BadPasswordFormatException;
}
