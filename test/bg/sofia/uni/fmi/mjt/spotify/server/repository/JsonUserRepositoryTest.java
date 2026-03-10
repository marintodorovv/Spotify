package bg.sofia.uni.fmi.mjt.spotify.server.repository;

import bg.sofia.uni.fmi.mjt.spotify.server.model.User;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class JsonUserRepositoryTest {
    private JsonUserRepository userRepository;
    private Path testFilePath;
    private Gson GSON;

    @BeforeEach
    void setUp() throws IOException {
        testFilePath = Files.createTempFile("test-users", ".json");
        GSON = new GsonBuilder().create();
        userRepository = new JsonUserRepository(testFilePath.toString(), GSON);
    }

    @AfterEach
    void tearDown() throws IOException {
        Files.deleteIfExists(testFilePath);
    }

    @Test
    void testAddUser() {
        User user = new User("user@abv.bg", "password");
        userRepository.addUser(user);

        Optional<User> result = userRepository.findUserByEmail("user@abv.bg");

        assertEquals("user@abv.bg", result.get().email());
    }

    @Test
    void testFindUserByEmailNotFound() {
        Optional<User> result = userRepository.findUserByEmail("nonexistent@abv.bg");

        assertTrue(result.isEmpty());
    }

    @Test
    void testAddUserDoesNotOverwrite() {
        User user1 = new User("user@abv.bg", "password1");
        User user2 = new User("user@abv.bg", "password2");

        userRepository.addUser(user1);
        userRepository.addUser(user2);

        Optional<User> result = userRepository.findUserByEmail("user@abv.bg");

        assertEquals("password1", result.get().password());
    }
}
