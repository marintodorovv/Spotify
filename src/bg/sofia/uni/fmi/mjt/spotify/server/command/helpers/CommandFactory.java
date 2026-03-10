package bg.sofia.uni.fmi.mjt.spotify.server.command.helpers;

import bg.sofia.uni.fmi.mjt.spotify.common.CommandType;
import bg.sofia.uni.fmi.mjt.spotify.server.command.AddSongToPlaylistCommand;
import bg.sofia.uni.fmi.mjt.spotify.server.command.CreatePlaylistCommand;
import bg.sofia.uni.fmi.mjt.spotify.server.command.DisconnectCommand;
import bg.sofia.uni.fmi.mjt.spotify.server.command.LoginCommand;
import bg.sofia.uni.fmi.mjt.spotify.server.command.PlaySongCommand;
import bg.sofia.uni.fmi.mjt.spotify.server.command.RegisterCommand;
import bg.sofia.uni.fmi.mjt.spotify.server.command.SearchCommand;
import bg.sofia.uni.fmi.mjt.spotify.server.command.ShowPlaylistCommand;
import bg.sofia.uni.fmi.mjt.spotify.server.command.StopPlayingSongCommand;
import bg.sofia.uni.fmi.mjt.spotify.server.command.TopSongsCommand;
import bg.sofia.uni.fmi.mjt.spotify.server.service.PlaylistService;
import bg.sofia.uni.fmi.mjt.spotify.server.service.SongService;
import bg.sofia.uni.fmi.mjt.spotify.server.service.UserService;
import bg.sofia.uni.fmi.mjt.spotify.server.service.streaming.StreamingService;

public final class CommandFactory {
    private CommandFactory() {
    }

    public static CommandExecutor createExecutor(UserService userService, PlaylistService playlistService,
                                                 SongService songService, StreamingService streamingService) {
        CommandRegistry registry = new CommandRegistry();

        registry.register(CommandType.REGISTER, new RegisterCommand(userService));
        registry.register(CommandType.LOGIN, new LoginCommand(userService));
        registry.register(CommandType.DISCONNECT, new DisconnectCommand(streamingService));

        registry.register(CommandType.SEARCH, new SearchCommand(songService));
        registry.register(CommandType.TOP, new TopSongsCommand(streamingService));

        registry.register(CommandType.CREATE_PLAYLIST, new CreatePlaylistCommand(playlistService));
        registry.register(CommandType.ADD_SONG_TO, new AddSongToPlaylistCommand(playlistService));
        registry.register(CommandType.SHOW_PLAYLIST, new ShowPlaylistCommand(playlistService));

        registry.register(CommandType.PLAY, new PlaySongCommand(streamingService));
        registry.register(CommandType.STOP, new StopPlayingSongCommand(streamingService));

        return new CommandExecutor(registry);
    }
}
