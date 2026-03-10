package bg.sofia.uni.fmi.mjt.spotify.server.validation;

import bg.sofia.uni.fmi.mjt.spotify.common.exception.validation.BadEmailFormatException;

final class EmailValidator {
    private static final char AT = '@';
    private static final String TWO_DOTS = "..";
    private static final String SPACE = " ";
    private static final char UNDERSCORE = '_';
    private static final char DOT = '.';

    private static final String EMAIL_DOMAIN_PART_CONTAINS_INVALID_CHARACTERS_MESSAGE = "Email domain part must only " +
            "contain letters, digits and dots!";
    private static final String EMAIL_DOMAIN_PART_DOES_NOT_CONTAIN_A_DOT_MESSAGE = "Email domain part must contain " +
            "a dot!";
    private static final String EMAIL_DOMAIN_PART_MUST_NOT_BE_BLANK_MESSAGE = "Email domain part must not be blank!";
    private static final String EMAIL_LOCAL_PART_CONTAINS_INVALID_CHARACTERS_MESSAGE = "Email local part must only " +
            "contain letters, digits, underscores and dots!";
    private static final String EMAIL_LOCAL_PART_MUST_NOT_BE_BLANK_MESSAGE = "Email local part must not be blank!";
    private static final String EMAIL_CONTAINS_TWO_DOTS_MESSAGE = "Email must not contain two dots!";
    private static final String EMAIL_CONTAINS_MORE_THAN_ONE_AT_MESSAGE = "Email must contain only one @!";
    private static final String EMAIL_DOES_NOT_CONTAIN_AT_MESSAGE = "Email must contain @!";
    private static final String EMAIL_IS_BLANK_MESSAGE = "Email must not be blank!";
    private static final String EMAIL_CONTAINS_SPACES_MESSAGE = "Email must not contain spaces!";
    private static final String EMAIL_IS_NULL_MESSAGE = "Email must not be null!";

    private EmailValidator() {
    }

    public static void isValidEmail(String email) throws BadEmailFormatException {
        if (email == null) {
            throw new BadEmailFormatException(EMAIL_IS_NULL_MESSAGE);
        }

        if (email.isBlank()) {
            throw new BadEmailFormatException(EMAIL_IS_BLANK_MESSAGE);
        }

        if (email.contains(SPACE)) {
            throw new BadEmailFormatException(EMAIL_CONTAINS_SPACES_MESSAGE);
        }

        int indexOfAt = email.indexOf(AT);

        if (indexOfAt <= 0 || indexOfAt == email.length() - 1) {
            throw new BadEmailFormatException(EMAIL_DOES_NOT_CONTAIN_AT_MESSAGE);
        }

        if (countAt(email) != 1) {
            throw new BadEmailFormatException(EMAIL_CONTAINS_MORE_THAN_ONE_AT_MESSAGE);
        }

        if (email.contains(TWO_DOTS)) {
            throw new BadEmailFormatException(EMAIL_CONTAINS_TWO_DOTS_MESSAGE);
        }

        isValidLocalPart(email.substring(0, indexOfAt));
        isValidDomainPart(email.substring(indexOfAt + 1));
    }

    private static long countAt(String email) {
        return email.chars().filter(ch -> ch == AT).count();
    }

    private static void isValidLocalPart(String localPart) throws BadEmailFormatException {
        if (localPart.isBlank()) {
            throw new BadEmailFormatException(EMAIL_LOCAL_PART_MUST_NOT_BE_BLANK_MESSAGE);
        }

        for (char c : localPart.toCharArray()) {
            if (!Character.isLetterOrDigit(c) && c != UNDERSCORE && c != DOT) {
                throw new BadEmailFormatException(EMAIL_LOCAL_PART_CONTAINS_INVALID_CHARACTERS_MESSAGE);
            }
        }
    }

    private static void isValidDomainPart(String domainPart) throws BadEmailFormatException {
        if (domainPart.isBlank()) {
            throw new BadEmailFormatException(EMAIL_DOMAIN_PART_MUST_NOT_BE_BLANK_MESSAGE);
        }

        int dotIndex = domainPart.lastIndexOf(DOT);
        if (dotIndex <= 0 || dotIndex == domainPart.length() - 1) {
            throw new BadEmailFormatException(EMAIL_DOMAIN_PART_DOES_NOT_CONTAIN_A_DOT_MESSAGE);
        }

        for (char c : domainPart.toCharArray()) {
            if (!Character.isLetterOrDigit(c) && c != DOT) {
                throw new BadEmailFormatException(EMAIL_DOMAIN_PART_CONTAINS_INVALID_CHARACTERS_MESSAGE);
            }
        }
    }
}
