package bg.sofia.uni.fmi.mjt.spotify.server.validation;

import bg.sofia.uni.fmi.mjt.spotify.common.exception.validation.BadEmailFormatException;
import bg.sofia.uni.fmi.mjt.spotify.common.exception.validation.BadPasswordFormatException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UserValidatorImplTest {
    private final UserValidator validator = new UserValidatorImpl();

    @Test
    void testValidateRegisterValid() {
        assertDoesNotThrow(() -> validator.validateRegister("a@b.c", "pass12"));
    }

    @Test
    void testValidateRegisterInvalidEmail() {
        assertThrows(BadEmailFormatException.class, () -> validator.validateRegister("", "pass12"));
    }

    @Test
    void testValidateRegisterInvalidPassword() {
        assertThrows(BadPasswordFormatException.class, () -> validator.validateRegister("a@b.c", ""));
    }

    @Test
    void testValidateAuthenticateValid() {
        assertDoesNotThrow(() -> validator.validateAuthenticate("a@b.c", "pass12"));
    }

    @Test
    void testValidateAuthenticateInvalidEmail() {
        assertThrows(BadEmailFormatException.class, () -> validator.validateAuthenticate("", "pass12"));
    }
}
