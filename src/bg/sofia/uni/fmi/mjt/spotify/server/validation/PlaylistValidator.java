package bg.sofia.uni.fmi.mjt.spotify.server.validation;

import bg.sofia.uni.fmi.mjt.spotify.common.exception.validation.BadEmailFormatException;
import bg.sofia.uni.fmi.mjt.spotify.common.exception.validation.BadPlaylistNameFormatException;
import bg.sofia.uni.fmi.mjt.spotify.common.exception.validation.BadSongTitleException;

public interface PlaylistValidator extends Validator {
    void validateCreate(String ownerEmail, String playlistName)
            throws BadEmailFormatException, BadPlaylistNameFormatException;

    void validateAddSong(String ownerEmail, String playlistName, String songTitle)
            throws BadEmailFormatException, BadPlaylistNameFormatException, BadSongTitleException;

    void validateGetAllSongs(String ownerEmail, String playlistName)
            throws BadEmailFormatException, BadPlaylistNameFormatException;

    void validateGetAllPlaylistsForUser(String ownerEmail)
            throws BadEmailFormatException;
}
