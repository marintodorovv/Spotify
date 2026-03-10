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

import java.nio.channels.SocketChannel;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class PlaySongCommandTest {
    private StreamingService streamingService;
    private PlaySongCommand command;
    private UserContext userContext;
    private CommandRequest request;
    private SocketChannel clientChannel;

    @BeforeEach
    void setUp() {
        streamingService = mock(StreamingService.class);
        command = new PlaySongCommand(streamingService);
        userContext = new UserContext();
        request = mock(CommandRequest.class);
        clientChannel = mock(SocketChannel.class);
    }

    @Test
    void testExecuteSuccess() throws SpotifyException {
        User user = new User("test@abv.bg", "password");
        userContext.login(user);
        when(request.getArgument(CommandArgument.SONG_TITLE)).thenReturn("Star67");
        when(request.clientChannel()).thenReturn(clientChannel);

        CommandResponse response = command.execute(request, userContext);

        assertTrue(response.success());
    }

    @Test
    void testExecuteNotLoggedIn() throws SpotifyException {
        when(request.getArgument(CommandArgument.SONG_TITLE)).thenReturn("Star67");
        when(request.clientChannel()).thenReturn(clientChannel);

        CommandResponse response = command.execute(request, userContext);

        assertFalse(response.success());
    }
}
