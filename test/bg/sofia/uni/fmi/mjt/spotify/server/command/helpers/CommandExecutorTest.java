package bg.sofia.uni.fmi.mjt.spotify.server.command.helpers;

import bg.sofia.uni.fmi.mjt.spotify.common.exception.authentication.UserAlreadyRegisteredException;
import bg.sofia.uni.fmi.mjt.spotify.common.exception.authentication.UserDoesNotExistsException;
import bg.sofia.uni.fmi.mjt.spotify.common.exception.authentication.WrongPasswordException;
import bg.sofia.uni.fmi.mjt.spotify.common.exception.network.MissingClientChannelException;
import bg.sofia.uni.fmi.mjt.spotify.common.exception.playlist.NoSuchPlaylistException;
import bg.sofia.uni.fmi.mjt.spotify.common.exception.playlist.PlaylistAlreadyExistsException;
import bg.sofia.uni.fmi.mjt.spotify.common.exception.playlist.SongAlreadyInPlaylistException;
import bg.sofia.uni.fmi.mjt.spotify.common.exception.song.AlreadyStreamingException;
import bg.sofia.uni.fmi.mjt.spotify.common.exception.song.NoSongStreamingException;
import bg.sofia.uni.fmi.mjt.spotify.common.exception.song.NoSuchSongException;
import bg.sofia.uni.fmi.mjt.spotify.common.exception.validation.BadEmailFormatException;
import bg.sofia.uni.fmi.mjt.spotify.common.exception.validation.BadPasswordFormatException;
import bg.sofia.uni.fmi.mjt.spotify.common.exception.validation.BadPlaylistNameFormatException;
import bg.sofia.uni.fmi.mjt.spotify.common.exception.validation.BadSearchKeywordsException;
import bg.sofia.uni.fmi.mjt.spotify.common.exception.validation.BadSongTitleException;
import bg.sofia.uni.fmi.mjt.spotify.common.exception.validation.BadTopNException;
import bg.sofia.uni.fmi.mjt.spotify.server.command.Command;
import bg.sofia.uni.fmi.mjt.spotify.server.context.UserContext;
import bg.sofia.uni.fmi.mjt.spotify.server.reponse.CommandResponse;
import bg.sofia.uni.fmi.mjt.spotify.server.request.CommandRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.nio.channels.SocketChannel;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class CommandExecutorTest {
    private CommandRegistry commandRegistry;
    private CommandExecutor commandExecutor;
    private UserContext userContext;
    private Command mockCommand;
    private SocketChannel socketChannel;

    @BeforeEach
    void setUp() {
        commandRegistry = mock(CommandRegistry.class);
        commandExecutor = new CommandExecutor(commandRegistry);
        userContext = new UserContext();
        mockCommand = mock(Command.class);
        socketChannel = mock(SocketChannel.class);
    }

    @Test
    void testExecuteSuccess()
            throws BadPlaylistNameFormatException, AlreadyStreamingException, BadSongTitleException,
            NoSuchSongException, UserDoesNotExistsException, MissingClientChannelException, BadTopNException,
            BadPasswordFormatException, BadSearchKeywordsException, NoSongStreamingException, WrongPasswordException,
            BadEmailFormatException, PlaylistAlreadyExistsException, NoSuchPlaylistException,
            SongAlreadyInPlaylistException, UserAlreadyRegisteredException {
        CommandRequest request = new CommandRequest("test", Map.of(), socketChannel);
        when(commandRegistry.getCommand("test")).thenReturn(mockCommand);
        when(mockCommand.execute(request, userContext)).thenReturn(CommandResponse.ok("Success"));

        CommandResponse response = commandExecutor.execute(request, userContext);

        assertTrue(response.success());
    }

    @Test
    void testExecuteCommandNotFound() {
        CommandRequest request = new CommandRequest("", Map.of(), socketChannel);

        when(commandRegistry.getCommand("")).thenReturn(null);

        CommandResponse response = commandExecutor.execute(request, userContext);

        assertFalse(response.success());
    }

    @Test
    void testExecuteSpotifyException() throws BadPlaylistNameFormatException, AlreadyStreamingException,
            BadSongTitleException, NoSuchSongException, UserDoesNotExistsException, MissingClientChannelException,
            BadTopNException, BadPasswordFormatException, BadSearchKeywordsException, NoSongStreamingException,
            WrongPasswordException, BadEmailFormatException, PlaylistAlreadyExistsException, NoSuchPlaylistException,
            SongAlreadyInPlaylistException, UserAlreadyRegisteredException {
        CommandRequest request = new CommandRequest("test", Map.of(), socketChannel);

        when(commandRegistry.getCommand("test")).thenReturn(mockCommand);
        when(mockCommand.execute(request, userContext)).thenThrow(new RuntimeException(""));

        CommandResponse response = commandExecutor.execute(request, userContext);

        assertFalse(response.success());
    }
}
