package bg.sofia.uni.fmi.mjt.spotify.server.validation;

import bg.sofia.uni.fmi.mjt.spotify.common.exception.validation.BadPlaylistNameFormatException;

final class PlaylistNameValidator {
    private static final String PLAYLIST_NAME_TOO_LONG_MESSAGE = "Playlist name must not be longer than 25 characters!";
    private static final String PLAYLIST_NAME_BLANK_MESSAGE = "Playlist name must not be blank!";
    private static final String NULL_PLAYLIST_NAME_MESSAGE = "Playlist name must not be null!";

    private static final int PLAYLIST_NAME_MAX_LENGTH = 25;

    private PlaylistNameValidator() {
    }

    public static void isValidPlaylistName(String playlistName) throws BadPlaylistNameFormatException {
        if (playlistName == null) {
            throw new BadPlaylistNameFormatException(NULL_PLAYLIST_NAME_MESSAGE);
        }

        if (playlistName.isBlank()) {
            throw new BadPlaylistNameFormatException(PLAYLIST_NAME_BLANK_MESSAGE);
        }

        if (playlistName.length() > PLAYLIST_NAME_MAX_LENGTH) {
            throw new BadPlaylistNameFormatException(PLAYLIST_NAME_TOO_LONG_MESSAGE);
        }
    }
}
