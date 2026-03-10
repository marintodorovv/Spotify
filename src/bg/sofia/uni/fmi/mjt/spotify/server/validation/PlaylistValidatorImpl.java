package bg.sofia.uni.fmi.mjt.spotify.server.validation;

import bg.sofia.uni.fmi.mjt.spotify.common.exception.validation.BadEmailFormatException;
import bg.sofia.uni.fmi.mjt.spotify.common.exception.validation.BadPlaylistNameFormatException;
import bg.sofia.uni.fmi.mjt.spotify.common.exception.validation.BadSongTitleException;

public final class PlaylistValidatorImpl implements PlaylistValidator {
    @Override
    public void validateCreate(String ownerEmail, String playlistName) throws BadEmailFormatException,
            BadPlaylistNameFormatException {
        EmailValidator.isValidEmail(ownerEmail);
        PlaylistNameValidator.isValidPlaylistName(playlistName);
    }

    @Override
    public void validateAddSong(String ownerEmail, String playlistName, String songTitle)
            throws BadEmailFormatException, BadPlaylistNameFormatException, BadSongTitleException {
        EmailValidator.isValidEmail(ownerEmail);
        PlaylistNameValidator.isValidPlaylistName(playlistName);
        SongTitleValidator.isValidSongTitle(songTitle);
    }

    @Override
    public void validateGetAllSongs(String ownerEmail, String playlistName) throws BadEmailFormatException,
            BadPlaylistNameFormatException {
        EmailValidator.isValidEmail(ownerEmail);
        PlaylistNameValidator.isValidPlaylistName(playlistName);
    }

    @Override
    public void validateGetAllPlaylistsForUser(String ownerEmail) throws BadEmailFormatException {
        EmailValidator.isValidEmail(ownerEmail);
    }
}
