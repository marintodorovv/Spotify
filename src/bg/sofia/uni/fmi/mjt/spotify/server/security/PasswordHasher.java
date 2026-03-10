package bg.sofia.uni.fmi.mjt.spotify.server.security;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;

public final class PasswordHasher {
    private static final String UNEXPECTED_ERROR_WHILE_COMPARING_PASSWORDS_MESSAGE = "Encountered an error while " +
            "checking passwords for match!";
    private static final String UNEXPECTED_ERROR_WHILE_HASHING_PASSWORD_MESSAGE = "Encountered an error while " +
            "hashing password!";
    private static final int STORED_PASSWORD_PARTS = 2;


    private static final String ALGORITHM = "SHA-256";
    private static final int SALT_LENGTH = 32;

    private static final String DOUBLE_DOT = ":";

    private static final int SALT_INDEX = 0;
    private static final int HASHED_PASSWORD_INDEX = 1;

    private PasswordHasher() {
    }

    public static String hashPassword(String password) {
        try {
            SecureRandom rnd = new SecureRandom();
            byte[] salt = new byte[SALT_LENGTH];
            rnd.nextBytes(salt);

            MessageDigest md = MessageDigest.getInstance(ALGORITHM);
            md.update(salt);

            String hashed = Base64.getEncoder().encodeToString(md.digest(password.getBytes(StandardCharsets.UTF_8)));

            return Base64.getEncoder().encodeToString(salt) + DOUBLE_DOT + hashed;
        } catch (NoSuchAlgorithmException e) {
            throw new SecurityException(UNEXPECTED_ERROR_WHILE_HASHING_PASSWORD_MESSAGE, e);
        }
    }

    public static boolean matches(String firstPassword, String hashedPassword) {
        try {
            String[] parts = hashedPassword.split(DOUBLE_DOT);

            if (parts.length != STORED_PASSWORD_PARTS) {
                return false;
            }

            byte[] salt = Base64.getDecoder().decode(parts[SALT_INDEX]);

            MessageDigest md = MessageDigest.getInstance(ALGORITHM);
            md.update(salt);

            String newHashed = Base64.getEncoder().encodeToString(
                    md.digest(firstPassword.getBytes(StandardCharsets.UTF_8)));

            return newHashed.equals(parts[HASHED_PASSWORD_INDEX]);
        } catch (NoSuchAlgorithmException e) {
            throw new SecurityException(UNEXPECTED_ERROR_WHILE_COMPARING_PASSWORDS_MESSAGE, e);
        }
    }
}
