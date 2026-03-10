package bg.sofia.uni.fmi.mjt.spotify.server.validation;

import bg.sofia.uni.fmi.mjt.spotify.common.exception.validation.BadPasswordFormatException;

final class PasswordValidator {
    private static final String SPACE = " ";
    private static final String NULL_PASSWORD_MESSAGE = "Password must not be null!";
    private static final String PASSWORD_CONTAINS_SPACES_MESSAGE = "Password must not contain spaces!";
    private static final String BLANK_PASSWORD_MESSAGE = "Password must not be blank!";
    private static final String PASSWORD_TOO_SHORT_MESSAGE = "Password must be at least six characters!";

    private static final int MIN_PASSWORD_LENGTH = 6;

    private PasswordValidator() {
    }

    public static void isValidPassword(String password) throws BadPasswordFormatException {
        if (password == null) {
            throw new BadPasswordFormatException(NULL_PASSWORD_MESSAGE);
        }

        if (password.isBlank()) {
            throw new BadPasswordFormatException(BLANK_PASSWORD_MESSAGE);
        }

        if (password.contains(SPACE)) {
            throw new BadPasswordFormatException(PASSWORD_CONTAINS_SPACES_MESSAGE);
        }

        if (password.length() < MIN_PASSWORD_LENGTH) {
            throw new BadPasswordFormatException(PASSWORD_TOO_SHORT_MESSAGE);
        }
    }
}
