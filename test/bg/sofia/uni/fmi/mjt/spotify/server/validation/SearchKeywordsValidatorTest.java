package bg.sofia.uni.fmi.mjt.spotify.server.validation;

import bg.sofia.uni.fmi.mjt.spotify.common.exception.validation.BadSearchKeywordsException;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class SearchKeywordsValidatorTest {
    @Test
    void testValidKeywords() {
        assertDoesNotThrow(() -> SearchKeywordsValidator.areValidKeywords(List.of("a")));
    }

    @Test
    void testNullKeywords() {
        assertThrows(BadSearchKeywordsException.class, () -> SearchKeywordsValidator.areValidKeywords(null));
    }

    @Test
    void testEmptyKeywords() {
        assertThrows(BadSearchKeywordsException.class, () -> SearchKeywordsValidator.areValidKeywords(List.of()));
    }

    @Test
    void testBlankKeyword() {
        assertThrows(BadSearchKeywordsException.class, () -> SearchKeywordsValidator.areValidKeywords(List.of("  ")));
    }
}
