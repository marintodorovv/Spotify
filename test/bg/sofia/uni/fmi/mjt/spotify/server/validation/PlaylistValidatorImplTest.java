package bg.sofia.uni.fmi.mjt.spotify.server.validation;

import bg.sofia.uni.fmi.mjt.spotify.common.exception.validation.BadEmailFormatException;
import bg.sofia.uni.fmi.mjt.spotify.common.exception.validation.BadPlaylistNameFormatException;
import bg.sofia.uni.fmi.mjt.spotify.common.exception.validation.BadSongTitleException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PlaylistValidatorImplTest {
    private final PlaylistValidator validator = new PlaylistValidatorImpl();

    @Test
    void testValidateCreateValid() {
        assertDoesNotThrow(() -> validator.validateCreate("a@b.c", "a"));
    }

    @Test
    void testValidateCreateInvalidEmail() {
        assertThrows(BadEmailFormatException.class, () -> validator.validateCreate("", "a"));
    }

    @Test
    void testValidateCreateInvalidName() {
        assertThrows(BadPlaylistNameFormatException.class, () -> validator.validateCreate("a@b.c", ""));
    }

    @Test
    void testValidateAddSongValid() {
        assertDoesNotThrow(() -> validator.validateAddSong("a@b.c", "a", "a"));
    }

    @Test
    void testValidateAddSongInvalidEmail() {
        assertThrows(BadEmailFormatException.class, () -> validator.validateAddSong("invalid", "a", "a"));
    }

    @Test
    void testValidateAddSongInvalidName() {
        assertThrows(BadPlaylistNameFormatException.class, () -> validator.validateAddSong("a@b.c", "", "a"));
    }

    @Test
    void testValidateAddSongInvalidTitle() {
        assertThrows(BadSongTitleException.class, () -> validator.validateAddSong("a@b.c", "a", null));
    }

    @Test
    void testValidateGetAllSongsInvalidEmail() {
        assertThrows(BadEmailFormatException.class, () -> validator.validateGetAllSongs("invalid", "a"));
    }

    @Test
    void testValidateGetAllPlaylistsValid() {
        assertDoesNotThrow(() -> validator.validateGetAllPlaylistsForUser("a@b.c"));
    }
}
