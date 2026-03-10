package bg.sofia.uni.fmi.mjt.spotify.server.command;

import bg.sofia.uni.fmi.mjt.spotify.common.exception.SpotifyException;
import bg.sofia.uni.fmi.mjt.spotify.server.context.UserContext;
import bg.sofia.uni.fmi.mjt.spotify.server.model.User;
import bg.sofia.uni.fmi.mjt.spotify.server.reponse.CommandResponse;
import bg.sofia.uni.fmi.mjt.spotify.server.request.CommandRequest;
import bg.sofia.uni.fmi.mjt.spotify.server.service.streaming.StreamingService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.nio.channels.SocketChannel;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class DisconnectCommandTest {
    private StreamingService streamingService;
    private DisconnectCommand command;
    private UserContext userContext;
    private CommandRequest request;
    private SocketChannel clientChannel;

    @BeforeEach
    void setUp() {
        streamingService = mock(StreamingService.class);
        command = new DisconnectCommand(streamingService);
        userContext = new UserContext();
        request = mock(CommandRequest.class);
        clientChannel = mock(SocketChannel.class);
    }

    @Test
    void testExecuteSuccess() throws SpotifyException {
        User user = new User("test@abv.bg", "password");
        userContext.login(user);
        when(request.clientChannel()).thenReturn(clientChannel);

        CommandResponse response = command.execute(request, userContext);

        assertTrue(response.success());
    }

    @Test
    void testExecuteLogsOutUser() throws SpotifyException {
        User user = new User("test@abv.bg", "password");
        userContext.login(user);
        when(request.clientChannel()).thenReturn(clientChannel);

        command.execute(request, userContext);

        assertFalse(userContext.isLogged());
    }

    @Test
    void testExecuteNotLoggedIn() throws SpotifyException {
        when(request.clientChannel()).thenReturn(clientChannel);

        CommandResponse response = command.execute(request, userContext);

        assertFalse(response.success());
    }
}
