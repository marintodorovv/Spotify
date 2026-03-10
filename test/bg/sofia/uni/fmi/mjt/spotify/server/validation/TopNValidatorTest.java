package bg.sofia.uni.fmi.mjt.spotify.server.validation;

import bg.sofia.uni.fmi.mjt.spotify.common.exception.validation.BadTopNException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TopNValidatorTest {
    @Test
    void testValidN() {
        assertDoesNotThrow(() -> TopNValidator.validateTopN(1));
    }

    @Test
    void testZeroN() {
        assertThrows(BadTopNException.class, () -> TopNValidator.validateTopN(0));
    }

    @Test
    void testNegativeN() {
        assertThrows(BadTopNException.class, () -> TopNValidator.validateTopN(-1));
    }
}
