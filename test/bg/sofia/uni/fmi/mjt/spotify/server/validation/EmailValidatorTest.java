package bg.sofia.uni.fmi.mjt.spotify.server.validation;

import bg.sofia.uni.fmi.mjt.spotify.common.exception.validation.BadEmailFormatException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EmailValidatorTest {
    @Test
    void testValidEmail() {
        assertDoesNotThrow(() -> EmailValidator.isValidEmail("a@b.c"));
    }

    @Test
    void testNullEmail() {
        assertThrows(BadEmailFormatException.class, () -> EmailValidator.isValidEmail(null));
    }

    @Test
    void testBlankEmail() {
        assertThrows(BadEmailFormatException.class, () -> EmailValidator.isValidEmail("  "));
    }

    @Test
    void testEmailWithSpaces() {
        assertThrows(BadEmailFormatException.class, () -> EmailValidator.isValidEmail("a @b.c"));
    }

    @Test
    void testEmailNoAt() {
        assertThrows(BadEmailFormatException.class, () -> EmailValidator.isValidEmail("ab.c"));
    }

    @Test
    void testEmailAtStart() {
        assertThrows(BadEmailFormatException.class, () -> EmailValidator.isValidEmail("@b.c"));
    }

    @Test
    void testEmailTwoDots() {
        assertThrows(BadEmailFormatException.class, () -> EmailValidator.isValidEmail("a..b@c.d"));
    }

    @Test
    void testLocalPartInvalidChar() {
        assertThrows(BadEmailFormatException.class, () -> EmailValidator.isValidEmail("a#@b.c"));
    }

    @Test
    void testDomainNoDot() {
        assertThrows(BadEmailFormatException.class, () -> EmailValidator.isValidEmail("a@b"));
    }

    @Test
    void testDomainDotAtEnd() {
        assertThrows(BadEmailFormatException.class, () -> EmailValidator.isValidEmail("a@b."));
    }

    @Test
    void testDomainInvalidChar() {
        assertThrows(BadEmailFormatException.class, () -> EmailValidator.isValidEmail("a@b_.c"));
    }
}
