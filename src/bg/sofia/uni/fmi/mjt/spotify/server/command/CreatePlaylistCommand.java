package bg.sofia.uni.fmi.mjt.spotify.server.command;

import bg.sofia.uni.fmi.mjt.spotify.common.exception.playlist.PlaylistAlreadyExistsException;
import bg.sofia.uni.fmi.mjt.spotify.common.exception.validation.BadEmailFormatException;
import bg.sofia.uni.fmi.mjt.spotify.common.exception.validation.BadPlaylistNameFormatException;
import bg.sofia.uni.fmi.mjt.spotify.server.context.UserContext;
import bg.sofia.uni.fmi.mjt.spotify.server.reponse.CommandResponse;
import bg.sofia.uni.fmi.mjt.spotify.server.request.CommandArgument;
import bg.sofia.uni.fmi.mjt.spotify.server.request.CommandRequest;
import bg.sofia.uni.fmi.mjt.spotify.server.service.PlaylistService;

public final class CreatePlaylistCommand implements Command {
    private static final String PLAYLIST_CREATED_SUCCESSFULLY_MESSAGE = "Playlist was created successfully!";
    private static final String USER_NOT_LOGGED_IN_MESSAGE = "You must log in to do this!";

    private final PlaylistService playlistService;

    public CreatePlaylistCommand(PlaylistService playlistService) {
        this.playlistService = playlistService;
    }

    @Override
    public CommandResponse execute(CommandRequest request, UserContext userContext)
            throws BadPlaylistNameFormatException, BadEmailFormatException, PlaylistAlreadyExistsException {
        if (!userContext.isLogged()) {
            return CommandResponse.error(USER_NOT_LOGGED_IN_MESSAGE);
        }

        String name = request.getArgument(CommandArgument.PLAYLIST_NAME);

        playlistService.createPlaylist(name, userContext.getCurrentUser().email());

        return CommandResponse.ok(PLAYLIST_CREATED_SUCCESSFULLY_MESSAGE);
    }
}
