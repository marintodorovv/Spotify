package bg.sofia.uni.fmi.mjt.spotify.server.repository;

import bg.sofia.uni.fmi.mjt.spotify.server.model.Song;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class FileSongRepositoryTest {
    private FileSongRepository songRepository;
    private Path testDirectory;

    @BeforeEach
    void setUp() throws IOException {
        testDirectory = Files.createTempDirectory("test-songs");
        songRepository = new FileSongRepository(testDirectory.toString());
    }

    @AfterEach
    void tearDown() throws IOException {
        Files.walk(testDirectory)
                .sorted(Comparator.reverseOrder())
                .forEach(path -> {
                    try {
                        Files.delete(path);
                    } catch (IOException _) {}
                });
    }

    @Test
    void testFindSuccess() throws IOException {
        Files.createFile(testDirectory.resolve("Drake - Star67.wav"));
        songRepository = new FileSongRepository(testDirectory.toString());

        Optional<Song> result = songRepository.find("Drake - Star67");

        assertEquals("Drake", result.get().author());
    }

    @Test
    void testFindNotFound() {
        Optional<Song> result = songRepository.find("");

        assertTrue(result.isEmpty());
    }

    @Test
    void testSearchSuccess() throws IOException {
        Files.createFile(testDirectory.resolve("Drake - Star67.wav"));
        Files.createFile(testDirectory.resolve("Future - Solo.wav"));
        songRepository = new FileSongRepository(testDirectory.toString());

        List<Song> result = songRepository.search(List.of("drake"));

        assertEquals(1, result.size());
    }

    @Test
    void testSearchNullKeywords() {
        List<Song> result = songRepository.search(null);

        assertEquals(0, result.size());
    }
}
