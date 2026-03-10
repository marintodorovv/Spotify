package bg.sofia.uni.fmi.mjt.spotify.server.validation;

import bg.sofia.uni.fmi.mjt.spotify.common.exception.validation.BadSongTitleException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SongTitleValidatorTest {
    @Test
    void testValidTitle() {
        assertDoesNotThrow(() -> SongTitleValidator.isValidSongTitle("abc"));
    }

    @Test
    void testNullTitle() {
        assertThrows(BadSongTitleException.class, () -> SongTitleValidator.isValidSongTitle(null));
    }

    @Test
    void testBlankTitle() {
        assertThrows(BadSongTitleException.class, () -> SongTitleValidator.isValidSongTitle("  "));
    }
}
