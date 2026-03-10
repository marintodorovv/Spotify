package bg.sofia.uni.fmi.mjt.spotify.server.model;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PlaylistTest {
    @Test
    void testPlaylistName() {
        List<String> songs = List.of("a", "b", "c");
        Playlist playlist = new Playlist("a", "user@abv.bg", songs);

        assertEquals("a", playlist.name());
    }

    @Test
    void testPlaylistSongListSize() {
        List<String> songs = List.of("a", "b", "cc");
        Playlist playlist = new Playlist("a", "user@abv.bg", songs);

        assertEquals(3, playlist.songIdentifiersList().size());
    }

    @Test
    void testPlaylistEmptySongList() {
        List<String> songs = List.of();
        Playlist playlist = new Playlist("a", "user@abv.bg", songs);

        assertTrue(playlist.songIdentifiersList().isEmpty());
    }

    @Test
    void testPlaylistIdentifier() {
        List<String> songs = List.of("a");
        Playlist playlist = new Playlist("a", "user@abv.bg", songs);

        assertEquals("user@abv.bg%a", playlist.playlistIdentifier());
    }

    @Test
    void testGeneratePlaylistIdentifier() {
        String identifier = Playlist.generatePlaylistIdentifier("user@abv.bg", "a");

        assertEquals("user@abv.bg%a", identifier);
    }

    @Test
    void testEqualsDiffName() {
        Playlist playlist1 = new Playlist("1", "user@abv.bg", List.of());
        Playlist playlist2 = new Playlist("2", "user@abv.bg", List.of());

        assertNotEquals(playlist1, playlist2);
    }

    @Test
    void testEqualsNull() {
        Playlist playlist2 = new Playlist("a", "user2@abv.bg", List.of());

        assertNotEquals(playlist2, null);
    }

    @Test
    void testHashCodeSame() {
        Playlist playlist1 = new Playlist("a", "user1@abv.bg", List.of());
        Playlist playlist2 = new Playlist("a", "user1@abv.bg", List.of());

        assertEquals(playlist1.hashCode(), playlist2.hashCode());
    }
}
