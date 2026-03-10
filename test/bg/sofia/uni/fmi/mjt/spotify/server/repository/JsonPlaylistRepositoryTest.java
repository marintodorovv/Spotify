package bg.sofia.uni.fmi.mjt.spotify.server.repository;

import bg.sofia.uni.fmi.mjt.spotify.server.model.Playlist;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class JsonPlaylistRepositoryTest {
    private JsonPlaylistRepository playlistRepository;
    private Path testFilePath;
    private Gson GSON;

    @BeforeEach
    void setUp() throws IOException {
        testFilePath = Files.createTempFile("test-playlists", ".json");
        GSON = new GsonBuilder().create();
        playlistRepository = new JsonPlaylistRepository(testFilePath.toString(), GSON);
    }

    @AfterEach
    void tearDown() throws IOException {
        Files.deleteIfExists(testFilePath);
    }

    @Test
    void testCreatePlaylist() {
        playlistRepository.createPlaylist("user@abv.bg", "My Playlist");

        assertTrue(playlistRepository.playlistExists("user@abv.bg", "My Playlist"));
    }

    @Test
    void testAddSongToPlaylist() {
        playlistRepository.createPlaylist("user@abv.bg", "Playlist");
        playlistRepository.addSongToPlaylist("user@abv.bg", "Playlist", "Song1");

        List<String> songs = playlistRepository.getSongsFromPlaylist("user@abv.bg", "Playlist");

        assertEquals("Song1", songs.getFirst());
    }

    @Test
    void testGetAllPlaylistsForUser() {
        playlistRepository.createPlaylist("user@abv.bg", "Playlist1");
        playlistRepository.createPlaylist("user@abv.bg", "Playlist2");
        playlistRepository.createPlaylist("other@abv.bg", "Playlist3");

        List<Playlist> playlists = playlistRepository.getAllPlaylistsForUser("user@abv.bg");

        assertEquals(2, playlists.size());
    }

    @Test
    void testPlaylistContainsSong() {
        playlistRepository.createPlaylist("user@abv.bg", "My Playlist");
        playlistRepository.addSongToPlaylist("user@abv.bg", "My Playlist", "Song1");

        assertTrue(playlistRepository.playlistContainsSong("user@abv.bg", "My Playlist", "Song1"));
    }
}
