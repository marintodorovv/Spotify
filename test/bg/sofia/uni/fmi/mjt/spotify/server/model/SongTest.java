package bg.sofia.uni.fmi.mjt.spotify.server.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SongTest {
    @Test
    void testSongId() {
        Song song = new Song("1", "Drake", "Star67", "Drake - Star67.wav");

        assertEquals("1", song.id());
    }

    @Test
    void testSongToString() {
        Song song = new Song("1", "Drake", "Star67", "Drake - Star67.wav");

        assertEquals("Drake - Star67", song.toString());
    }
}
