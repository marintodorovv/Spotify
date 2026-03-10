package bg.sofia.uni.fmi.mjt.spotify.server.context;

import bg.sofia.uni.fmi.mjt.spotify.server.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UserContextTest {
    private UserContext userContext;
    private User testUser;

    @BeforeEach
    void setUp() {
        userContext = new UserContext();
        testUser = new User("test@abv.bg", "password");
    }

    @Test
    void testLoginMakesUserLogged() {
        userContext.login(testUser);

        assertTrue(userContext.isLogged());
    }

    @Test
    void testLogoutClearsCurrentUser() {
        userContext.login(testUser);
        userContext.logout();

        assertNull(userContext.getCurrentUser());
    }

    @Test
    void testLogoutMakesUserNotLogged() {
        userContext.login(testUser);
        userContext.logout();

        assertFalse(userContext.isLogged());
    }
}
