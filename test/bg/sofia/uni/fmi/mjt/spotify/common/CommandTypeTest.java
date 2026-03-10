package bg.sofia.uni.fmi.mjt.spotify.common;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CommandTypeTest {
    @Test
    void testGetCommandName() {
        assertEquals("register", CommandType.REGISTER.getCommandName());
    }

    @Test
    void testFromStringRegister() {
        assertEquals(CommandType.REGISTER, CommandType.fromString("register"));
    }

    @Test
    void testFromStringLogin() {
        assertEquals(CommandType.LOGIN, CommandType.fromString("login"));
    }

    @Test
    void testFromStringDisconnect() {
        assertEquals(CommandType.DISCONNECT, CommandType.fromString("disconnect"));
    }

    @Test
    void testFromStringSearch() {
        assertEquals(CommandType.SEARCH, CommandType.fromString("search"));
    }

    @Test
    void testFromStringTop() {
        assertEquals(CommandType.TOP, CommandType.fromString("top"));
    }

    @Test
    void testFromStringCreatePlaylist() {
        assertEquals(CommandType.CREATE_PLAYLIST, CommandType.fromString("create-playlist"));
    }

    @Test
    void testFromStringAddSongTo() {
        assertEquals(CommandType.ADD_SONG_TO, CommandType.fromString("add-song-to"));
    }

    @Test
    void testFromStringShowPlaylist() {
        assertEquals(CommandType.SHOW_PLAYLIST, CommandType.fromString("show-playlist"));
    }

    @Test
    void testFromStringPlay() {
        assertEquals(CommandType.PLAY, CommandType.fromString("play"));
    }

    @Test
    void testFromStringCaseInsensitive() {
        assertEquals(CommandType.REGISTER, CommandType.fromString("REGISTER"));
    }

    @Test
    void testFromStringInvalid() {
        assertNull(CommandType.fromString("invalid"));
    }
}
