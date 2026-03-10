package bg.sofia.uni.fmi.mjt.spotify.server.validation;

import bg.sofia.uni.fmi.mjt.spotify.common.exception.validation.BadSongTitleException;

final class SongTitleValidator {
    private static final String NULL_SONG_TITLE_MESSAGE = "Song title must not be null!";
    private static final String BLANK_SONG_TITLE_MESSAGE = "Song title must not be blank!";

    private SongTitleValidator() {
    }

    public static void isValidSongTitle(String title) throws BadSongTitleException {
        if (title == null) {
            throw new BadSongTitleException(NULL_SONG_TITLE_MESSAGE);
        }

        if (title.isBlank()) {
            throw new BadSongTitleException(BLANK_SONG_TITLE_MESSAGE);
        }
    }
}
