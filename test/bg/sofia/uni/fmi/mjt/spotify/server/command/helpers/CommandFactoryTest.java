package bg.sofia.uni.fmi.mjt.spotify.server.command.helpers;

import bg.sofia.uni.fmi.mjt.spotify.server.service.PlaylistService;
import bg.sofia.uni.fmi.mjt.spotify.server.service.SongService;
import bg.sofia.uni.fmi.mjt.spotify.server.service.UserService;
import bg.sofia.uni.fmi.mjt.spotify.server.service.streaming.StreamingService;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

class CommandFactoryTest {
    @Test
    void testCreateExecutor() {
        UserService userService = mock(UserService.class);
        PlaylistService playlistService = mock(PlaylistService.class);
        SongService songService = mock(SongService.class);
        StreamingService streamingService = mock(StreamingService.class);

        CommandExecutor executor = CommandFactory.createExecutor(
                userService, playlistService, songService, streamingService);

        assertNotNull(executor);
    }
}
