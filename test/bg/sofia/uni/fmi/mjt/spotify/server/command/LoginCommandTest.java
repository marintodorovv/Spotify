package bg.sofia.uni.fmi.mjt.spotify.server.command;

import bg.sofia.uni.fmi.mjt.spotify.common.exception.SpotifyException;
import bg.sofia.uni.fmi.mjt.spotify.server.context.UserContext;
import bg.sofia.uni.fmi.mjt.spotify.server.model.User;
import bg.sofia.uni.fmi.mjt.spotify.server.reponse.CommandResponse;
import bg.sofia.uni.fmi.mjt.spotify.server.request.CommandArgument;
import bg.sofia.uni.fmi.mjt.spotify.server.request.CommandRequest;
import bg.sofia.uni.fmi.mjt.spotify.server.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class LoginCommandTest {
    private UserService userService;
    private LoginCommand command;
    private UserContext userContext;
    private CommandRequest request;

    @BeforeEach
    void setUp() {
        userService = mock(UserService.class);
        command = new LoginCommand(userService);
        userContext = new UserContext();
        request = mock(CommandRequest.class);
    }

    @Test
    void testExecuteSuccess() throws SpotifyException {
        User user = new User("test@abv.bg", "password");
        when(request.getArgument(CommandArgument.EMAIL)).thenReturn("test@abv.bg");
        when(request.getArgument(CommandArgument.PASSWORD)).thenReturn("password");
        when(userService.authenticate("test@abv.bg", "password")).thenReturn(user);

        CommandResponse response = command.execute(request, userContext);

        assertTrue(response.success());
    }

    @Test
    void testExecuteLogsInUser() throws SpotifyException {
        User user = new User("test@abv.bg", "password");
        when(request.getArgument(CommandArgument.EMAIL)).thenReturn("test@abv.bg");
        when(request.getArgument(CommandArgument.PASSWORD)).thenReturn("password");
        when(userService.authenticate("test@abv.bg", "password")).thenReturn(user);

        command.execute(request, userContext);

        assertTrue(userContext.isLogged());
    }

    @Test
    void testExecuteAlreadyLoggedIn() throws SpotifyException {
        User user = new User("test@abv.bg", "password");
        userContext.login(user);

        CommandResponse response = command.execute(request, userContext);

        assertFalse(response.success());
    }
}
