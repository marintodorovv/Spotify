package bg.sofia.uni.fmi.mjt.spotify.server.security;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PasswordHasherTest {
    @Test
    void testHashPasswordContainsColon() {
        String hashed = PasswordHasher.hashPassword("password");

        assertTrue(hashed.contains(":"));
    }

    @Test
    void testHashPasswordDifferentSalts() {
        String hash1 = PasswordHasher.hashPassword("password");
        String hash2 = PasswordHasher.hashPassword("password");

        assertNotEquals(hash1, hash2);
    }

    @Test
    void testMatchesSamePassword() {
        String hashed = PasswordHasher.hashPassword("password");

        assertTrue(PasswordHasher.matches("password", hashed));
    }

    @Test
    void testMatchesDifferentPassword() {
        String hashed = PasswordHasher.hashPassword("password");

        assertFalse(PasswordHasher.matches("wrong", hashed));
    }

    @Test
    void testMatchesInvalidFormat() {
        boolean result = PasswordHasher.matches("password", "invalid");

        assertFalse(result);
    }
}
