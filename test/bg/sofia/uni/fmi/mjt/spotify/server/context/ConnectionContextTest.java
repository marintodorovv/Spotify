package bg.sofia.uni.fmi.mjt.spotify.server.context;

import bg.sofia.uni.fmi.mjt.spotify.server.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ConnectionContextTest {
    private ConnectionContext connectionContext;
    private static final int BUFFER_SIZE = 1024;

    @BeforeEach
    void setUp() {
        connectionContext = new ConnectionContext(BUFFER_SIZE);
    }

    @Test
    void testContextPersistsLogin() {
        UserContext context = connectionContext.getContext();
        User user = new User("test@abv.bg", "password");
        context.login(user);

        assertTrue(connectionContext.getContext().isLogged());
    }

    @Test
    void testContextPersistsUser() {
        UserContext context = connectionContext.getContext();
        User user = new User("test@abv.bg", "password");
        context.login(user);

        assertEquals(user, connectionContext.getContext().getCurrentUser());
    }
}
