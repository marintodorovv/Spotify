package bg.sofia.uni.fmi.mjt.spotify.server.repository;

import bg.sofia.uni.fmi.mjt.spotify.common.exception.repository.RepositoryException;
import bg.sofia.uni.fmi.mjt.spotify.server.model.Song;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public final class FileSongRepository extends BaseRepository implements SongRepository {
    private static final String UNEXPECTED_ERROR_WHILE_LOADING_SONGS_MESSAGE = "Encountered an error while " +
            "loading songs!";
    private static final int FILE_EXTENSION_LENGTH = 4;
    private static final int OFFSET_FROM_DASH = 3;
    private static final String DASH = " - ";

    private static final String EXTENSION = "*.wav";

    private final Path filePath;
    private final Map<String, Song> songMap;

    public FileSongRepository(String filePath) {
        this.filePath = Path.of(filePath);
        songMap = new ConcurrentHashMap<>();
        ensureFileExists(this.filePath);
        loadSongsFromFile();
    }

    private void loadSongsFromFile() {
        songMap.clear();
        try (DirectoryStream<Path> stream = Files.newDirectoryStream(filePath, EXTENSION)) {
            for (Path file : stream) {
                String fileName = file.getFileName().toString();

                if (fileName.contains(DASH)) {
                    int dashIndex = fileName.indexOf(DASH);
                    String author = fileName.substring(0, dashIndex).trim();
                    String title = fileName.substring(dashIndex + OFFSET_FROM_DASH, fileName.length() -
                            FILE_EXTENSION_LENGTH).trim();

                    String id = fileName.substring(0, fileName.length() - FILE_EXTENSION_LENGTH);

                    songMap.put(id, new Song(id, author, title, fileName));
                }
            }
        } catch (IOException e) {
            throw new RepositoryException(UNEXPECTED_ERROR_WHILE_LOADING_SONGS_MESSAGE, e);
        }
    }

    @Override
    public Optional<Song> find(String id) {
        return Optional.ofNullable(songMap.get(id));
    }

    @Override
    public List<Song> search(List<String> keywords) {
        if (keywords == null || keywords.isEmpty()) {
            return List.of();
        }

        List<Song> matches = new ArrayList<>();

        for (Song song : songMap.values()) {
            for (String keyword : keywords) {
                if (song.id().toLowerCase().contains(keyword.toLowerCase())) {
                    matches.add(song);
                    break;
                }
            }
        }

        return matches;
    }
}
