package bg.sofia.uni.fmi.mjt.spotify.server.command;

import bg.sofia.uni.fmi.mjt.spotify.common.exception.SpotifyException;
import bg.sofia.uni.fmi.mjt.spotify.server.context.UserContext;
import bg.sofia.uni.fmi.mjt.spotify.server.model.User;
import bg.sofia.uni.fmi.mjt.spotify.server.reponse.CommandResponse;
import bg.sofia.uni.fmi.mjt.spotify.server.request.CommandArgument;
import bg.sofia.uni.fmi.mjt.spotify.server.request.CommandRequest;
import bg.sofia.uni.fmi.mjt.spotify.server.service.streaming.StreamingService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class TopSongsCommandTest {
    private StreamingService streamingService;
    private TopSongsCommand command;
    private UserContext userContext;
    private CommandRequest request;

    @BeforeEach
    void setUp() {
        streamingService = mock(StreamingService.class);
        command = new TopSongsCommand(streamingService);
        userContext = new UserContext();
        request = mock(CommandRequest.class);
    }

    @Test
    void testExecuteSuccess() throws SpotifyException {
        User user = new User("test@abv.bg", "password");
        userContext.login(user);
        when(request.getArgument(CommandArgument.NUMBER)).thenReturn("5");
        when(streamingService.topNSongs(5)).thenReturn(List.of("Star67"));

        CommandResponse response = command.execute(request, userContext);

        assertTrue(response.success());
    }

    @Test
    void testExecuteNotLoggedIn() throws SpotifyException {
        when(request.getArgument(CommandArgument.NUMBER)).thenReturn("5");

        CommandResponse response = command.execute(request, userContext);

        assertFalse(response.success());
    }

    @Test
    void testExecuteNoSongsStreaming() throws SpotifyException {
        User user = new User("test@abbv.bg", "password");
        userContext.login(user);
        when(request.getArgument(CommandArgument.NUMBER)).thenReturn("5");
        when(streamingService.topNSongs(5)).thenReturn(List.of());

        CommandResponse response = command.execute(request, userContext);

        assertTrue(response.success());
    }
}
