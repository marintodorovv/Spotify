package bg.sofia.uni.fmi.mjt.spotify.server.validation;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import bg.sofia.uni.fmi.mjt.spotify.common.exception.validation.BadPlaylistNameFormatException;

class PlaylistNameValidatorTest {
    @Test
    void testValidName() {
        assertDoesNotThrow(() -> PlaylistNameValidator.isValidPlaylistName("zapalqnko"));
    }

    @Test
    void testNullName() {
        assertThrows(BadPlaylistNameFormatException.class, () -> PlaylistNameValidator.isValidPlaylistName(null));
    }

    @Test
    void testBlankName() {
        assertThrows(BadPlaylistNameFormatException.class, () -> PlaylistNameValidator.isValidPlaylistName("  "));
    }

    @Test
    void testNameTooLong() {
        assertThrows(BadPlaylistNameFormatException.class, () ->
                PlaylistNameValidator.isValidPlaylistName("zapalqnkozapalqnkozapalqnkozapalqnkozapalqnkozapalqnko"));
    }
}
