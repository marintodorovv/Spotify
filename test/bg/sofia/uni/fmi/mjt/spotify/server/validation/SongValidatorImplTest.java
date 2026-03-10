package bg.sofia.uni.fmi.mjt.spotify.server.validation;

import bg.sofia.uni.fmi.mjt.spotify.common.exception.validation.BadSearchKeywordsException;
import bg.sofia.uni.fmi.mjt.spotify.common.exception.validation.BadSongTitleException;
import bg.sofia.uni.fmi.mjt.spotify.common.exception.validation.BadTopNException;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class SongValidatorImplTest {
    private final SongValidator validator = new SongValidatorImpl();

    @Test
    void testValidateSearchValid() {
        assertDoesNotThrow(() -> validator.validateSearch(List.of("a")));
    }

    @Test
    void testValidateSearchNull() {
        assertThrows(BadSearchKeywordsException.class, () -> validator.validateSearch(null));
    }

    @Test
    void testValidateTopNValid() {
        assertDoesNotThrow(() -> validator.validateTopNSongs(1));
    }

    @Test
    void testValidateTopNZero() {
        assertThrows(BadTopNException.class, () -> validator.validateTopNSongs(0));
    }

    @Test
    void testValidateTopNNegative() {
        assertThrows(BadTopNException.class, () -> validator.validateTopNSongs(-1));
    }

    @Test
    void testValidatePlaySongValid() {
        assertDoesNotThrow(() -> validator.validatePlaySong("a"));
    }

    @Test
    void testValidatePlaySongNull() {
        assertThrows(BadSongTitleException.class, () -> validator.validatePlaySong(null));
    }
}
