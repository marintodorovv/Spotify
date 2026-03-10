package bg.sofia.uni.fmi.mjt.spotify.server.command;

import bg.sofia.uni.fmi.mjt.spotify.common.exception.playlist.NoSuchPlaylistException;
import bg.sofia.uni.fmi.mjt.spotify.common.exception.playlist.SongAlreadyInPlaylistException;
import bg.sofia.uni.fmi.mjt.spotify.common.exception.song.NoSuchSongException;
import bg.sofia.uni.fmi.mjt.spotify.common.exception.validation.BadEmailFormatException;
import bg.sofia.uni.fmi.mjt.spotify.common.exception.validation.BadPlaylistNameFormatException;
import bg.sofia.uni.fmi.mjt.spotify.common.exception.validation.BadSongTitleException;
import bg.sofia.uni.fmi.mjt.spotify.server.context.UserContext;
import bg.sofia.uni.fmi.mjt.spotify.server.reponse.CommandResponse;
import bg.sofia.uni.fmi.mjt.spotify.server.request.CommandArgument;
import bg.sofia.uni.fmi.mjt.spotify.server.request.CommandRequest;
import bg.sofia.uni.fmi.mjt.spotify.server.service.PlaylistService;

public final class AddSongToPlaylistCommand implements Command {
    private static final String SONG_ADDED_TO_PLAYLIST_MESSAGE = "Song added to playlist successfully!";
    private static final String USER_NOT_LOGGED_IN_MESSAGE = "You must be logged in to do this!";

    private final PlaylistService playlistService;

    public AddSongToPlaylistCommand(PlaylistService playlistService) {
        this.playlistService = playlistService;
    }

    @Override
    public CommandResponse execute(CommandRequest request, UserContext userContext)
            throws BadPlaylistNameFormatException, BadEmailFormatException, BadSongTitleException,
            NoSuchSongException, NoSuchPlaylistException, SongAlreadyInPlaylistException {
        if (!userContext.isLogged()) {
            return CommandResponse.error(USER_NOT_LOGGED_IN_MESSAGE);
        }

        String playlistName = request.getArgument(CommandArgument.PLAYLIST_NAME);
        String songTitle = request.getArgument(CommandArgument.SONG_TITLE);

        String ownerEmail = userContext.getCurrentUser().email();

        playlistService.addSongToPlaylist(playlistName, songTitle, ownerEmail);

        return CommandResponse.ok(SONG_ADDED_TO_PLAYLIST_MESSAGE);
    }
}
