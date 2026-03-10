package bg.sofia.uni.fmi.mjt.spotify.server.validation;

import bg.sofia.uni.fmi.mjt.spotify.common.exception.validation.BadPasswordFormatException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PasswordValidatorTest {
    @Test
    void testValidPassword() {
        assertDoesNotThrow(() -> PasswordValidator.isValidPassword("bmwbmw"));
    }

    @Test
    void testNullPassword() {
        assertThrows(BadPasswordFormatException.class, () -> PasswordValidator.isValidPassword(null));
    }

    @Test
    void testBlankPassword() {
        assertThrows(BadPasswordFormatException.class, () -> PasswordValidator.isValidPassword("  "));
    }

    @Test
    void testPasswordWithSpaces() {
        assertThrows(BadPasswordFormatException.class, () -> PasswordValidator.isValidPassword("bmw bmw"));
    }

    @Test
    void testPasswordTooShort() {
        assertThrows(BadPasswordFormatException.class, () -> PasswordValidator.isValidPassword("bmw"));
    }
}
