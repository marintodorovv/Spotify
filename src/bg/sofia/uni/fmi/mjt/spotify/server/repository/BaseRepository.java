package bg.sofia.uni.fmi.mjt.spotify.server.repository;

import bg.sofia.uni.fmi.mjt.spotify.common.exception.repository.RepositoryException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public abstract sealed class BaseRepository
        permits FileSongRepository, JsonPlaylistRepository, JsonUserRepository {

    public static final String UNEXPECTED_ERROR_WHILE_CHECKING_FILE_EXISTANCE_MESSAGE = "Encountered an error while " +
            "checking if file exists on disk!";

    protected void ensureFileExists(Path filePath) {
        try {
            if (Files.notExists(filePath)) {
                if (filePath.getParent() != null) {
                    Files.createDirectories(filePath.getParent());
                }
                Files.createFile(filePath);
            }
        } catch (IOException e) {
            throw new RepositoryException(UNEXPECTED_ERROR_WHILE_CHECKING_FILE_EXISTANCE_MESSAGE, e);
        }
    }
}
