package bg.sofia.uni.fmi.mjt.spotify.server.request;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CommandRequestTest {
    @Test
    void testParseNullMessage() {
        CommandRequest req = CommandRequest.parse(null, null);

        assertEquals("", req.command());
    }

    @Test
    void testParseLoginCommand() {
        CommandRequest req = CommandRequest.parse("login a@b.c pass", null);

        assertEquals("login", req.command());
    }

    @Test
    void testParseLoginEmail() {
        CommandRequest req = CommandRequest.parse("login a@b.c pass", null);

        assertEquals("a@b.c", req.getArgument(CommandArgument.EMAIL));
    }

    @Test
    void testParseSearchKeywords() {
        CommandRequest req = CommandRequest.parse("search drake future", null);

        assertEquals("drake future", req.getArgument(CommandArgument.KEYWORDS));
    }

    @Test
    void testParseTopNumber() {
        CommandRequest req = CommandRequest.parse("top 10", null);

        assertEquals("10", req.getArgument(CommandArgument.NUMBER));
    }

    @Test
    void testParseCreatePlaylistName() {
        CommandRequest req = CommandRequest.parse("create-playlist my playlist", null);

        assertEquals("my playlist", req.getArgument(CommandArgument.PLAYLIST_NAME));
    }

    @Test
    void testParsePlaySongTitle() {
        CommandRequest req = CommandRequest.parse("play song.wav", null);

        assertEquals("song.wav", req.getArgument(CommandArgument.SONG_TITLE));
    }

    @Test
    void testParseAddSongToPlaylistName() {
        CommandRequest req = CommandRequest.parse("add-song-to playlist song", null);

        assertEquals("playlist", req.getArgument(CommandArgument.PLAYLIST_NAME));
    }

    @Test
    void testParseAddSongToSongTitle() {
        CommandRequest req = CommandRequest.parse("add-song-to playlist song", null);

        assertEquals("song", req.getArgument(CommandArgument.SONG_TITLE));
    }

    @Test
    void testParseCaseInsensitive() {
        CommandRequest req = CommandRequest.parse("LOGIN a@b.c pass", null);

        assertEquals("login", req.command());
    }

    @Test
    void testParseCommandOnly() {
        CommandRequest req = CommandRequest.parse("disconnect", null);

        assertEquals("disconnect", req.command());
    }
}
