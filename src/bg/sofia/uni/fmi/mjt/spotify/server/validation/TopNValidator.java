package bg.sofia.uni.fmi.mjt.spotify.server.validation;

//This is overkill, but ive already done classes for every different type of validation, so might as well do this one

import bg.sofia.uni.fmi.mjt.spotify.common.exception.validation.BadTopNException;

final class TopNValidator {
    private static final String ZERO_N_MESSAGE = "N must not be zero!";
    private static final String NEGATIVE_N_MESSAGE = "N must not be negative!";

    private TopNValidator() {
    }

    public static void validateTopN(int n) throws BadTopNException {
        if (n < 0) {
            throw new BadTopNException(NEGATIVE_N_MESSAGE);
        }

        if (n == 0) {
            throw new BadTopNException(ZERO_N_MESSAGE);
        }
    }
}
