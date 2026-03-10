package bg.sofia.uni.fmi.mjt.spotify.server.command;

import bg.sofia.uni.fmi.mjt.spotify.common.exception.SpotifyException;
import bg.sofia.uni.fmi.mjt.spotify.server.context.UserContext;
import bg.sofia.uni.fmi.mjt.spotify.server.model.Song;
import bg.sofia.uni.fmi.mjt.spotify.server.model.User;
import bg.sofia.uni.fmi.mjt.spotify.server.reponse.CommandResponse;
import bg.sofia.uni.fmi.mjt.spotify.server.request.CommandArgument;
import bg.sofia.uni.fmi.mjt.spotify.server.request.CommandRequest;
import bg.sofia.uni.fmi.mjt.spotify.server.service.SongService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class SearchCommandTest {
    private SongService songService;
    private SearchCommand command;
    private UserContext userContext;
    private CommandRequest request;

    @BeforeEach
    void setUp() {
        songService = mock(SongService.class);
        command = new SearchCommand(songService);
        userContext = new UserContext();
        request = mock(CommandRequest.class);
    }

    @Test
    void testExecuteSuccess() throws SpotifyException {
        User user = new User("test@abv.bg", "password");
        userContext.login(user);
        when(request.getArgument(CommandArgument.KEYWORDS)).thenReturn("drake");
        List<Song> songs = List.of(new Song("asdasf", "Drake", "Star67", "Star67.wav"));
        when(songService.search(anyList())).thenReturn(songs);

        CommandResponse response = command.execute(request, userContext);

        assertTrue(response.success());
    }

    @Test
    void testExecuteNotLoggedIn() throws SpotifyException {
        when(request.getArgument(CommandArgument.KEYWORDS)).thenReturn("drake");

        CommandResponse response = command.execute(request, userContext);

        assertFalse(response.success());
    }

    @Test
    void testExecuteBlankKeywords() throws SpotifyException {
        User user = new User("test@abv.bg", "password");
        userContext.login(user);
        when(request.getArgument(CommandArgument.KEYWORDS)).thenReturn("   ");

        CommandResponse response = command.execute(request, userContext);

        assertFalse(response.success());
    }
}
