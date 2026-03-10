package bg.sofia.uni.fmi.mjt.spotify.server.repository;

import bg.sofia.uni.fmi.mjt.spotify.common.exception.repository.RepositoryException;
import bg.sofia.uni.fmi.mjt.spotify.server.model.User;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public final class JsonUserRepository extends BaseRepository implements UserRepository {
    private static final String UNEXPECTED_ERROR_WHILE_WRITING_USERS_MESSAGE = "Encountered an error while " +
            "writing users to disk!";
    private static final String UNEXPECTED_ERROR_WHILE_READING_USERS_MESSAGE = "Encountered an error while " +
            "loading users from disk!";

    private final Path filePath;
    private final Gson gson;
    private final Map<String, User> userMap;

    public JsonUserRepository(String filePath, Gson gson) {
        this.filePath = Path.of(filePath);
        this.gson = gson;
        userMap = new ConcurrentHashMap<>();

        ensureFileExists(this.filePath);
        loadUsersFromFile();
    }

    public JsonUserRepository(String filePath) {
        this(filePath, new GsonBuilder().create());
    }

    private void loadUsersFromFile() {
        userMap.clear();
        try (BufferedReader reader = Files.newBufferedReader(filePath)) {
            UserStorage storage = gson.fromJson(reader, UserStorage.class);
            if (storage != null && storage.userList() != null) {
                for (User user : storage.userList()) {
                    userMap.put(user.email(), user);
                }
            }
        } catch (IOException e) {
            throw new RepositoryException(UNEXPECTED_ERROR_WHILE_READING_USERS_MESSAGE, e);
        }
    }

    private synchronized void syncFile() {
        try (BufferedWriter writer = Files.newBufferedWriter(filePath)) {
            UserStorage storage = new UserStorage(new ArrayList<>(userMap.values()));
            gson.toJson(storage, writer);
        } catch (IOException e) {
            throw new RepositoryException(UNEXPECTED_ERROR_WHILE_WRITING_USERS_MESSAGE, e);
        }
    }

    @Override
    public synchronized void addUser(User user) {
        userMap.putIfAbsent(user.email(), user);

        syncFile();
    }

    @Override
    public Optional<User> findUserByEmail(String email) {
        return Optional.ofNullable(userMap.get(email));
    }
}
