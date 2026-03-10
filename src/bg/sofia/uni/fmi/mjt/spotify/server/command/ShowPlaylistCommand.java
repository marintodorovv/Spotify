package bg.sofia.uni.fmi.mjt.spotify.server.command;

import bg.sofia.uni.fmi.mjt.spotify.common.exception.playlist.NoSuchPlaylistException;
import bg.sofia.uni.fmi.mjt.spotify.common.exception.validation.BadEmailFormatException;
import bg.sofia.uni.fmi.mjt.spotify.common.exception.validation.BadPlaylistNameFormatException;
import bg.sofia.uni.fmi.mjt.spotify.server.context.UserContext;
import bg.sofia.uni.fmi.mjt.spotify.server.reponse.CommandResponse;
import bg.sofia.uni.fmi.mjt.spotify.server.request.CommandArgument;
import bg.sofia.uni.fmi.mjt.spotify.server.request.CommandRequest;
import bg.sofia.uni.fmi.mjt.spotify.server.service.PlaylistService;

import java.util.List;

public final class ShowPlaylistCommand implements Command {
    private static final String EMPTY_PLAYLIST_MESSAGE = "This playlist has no songs!";
    private static final String USER_NOT_LOGGED_IN_MESSAGE = "You must be logged in to do this!";

    private final PlaylistService playlistService;

    public ShowPlaylistCommand(PlaylistService playlistService) {
        this.playlistService = playlistService;
    }

    @Override
    public CommandResponse execute(CommandRequest request, UserContext userContext)
            throws BadPlaylistNameFormatException, BadEmailFormatException, NoSuchPlaylistException {
        if (!userContext.isLogged()) {
            return CommandResponse.error(USER_NOT_LOGGED_IN_MESSAGE);
        }

        String playlistName = request.getArgument(CommandArgument.PLAYLIST_NAME);

        String ownerEmail = userContext.getCurrentUser().email();

        List<String> songs = playlistService.getAllSongsFromPlaylist(playlistName, ownerEmail);

        if (songs.isEmpty()) {
            return CommandResponse.ok(EMPTY_PLAYLIST_MESSAGE);
        }

        return CommandResponse.data(songs);
    }
}
