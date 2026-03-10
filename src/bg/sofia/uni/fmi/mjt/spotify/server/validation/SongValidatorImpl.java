package bg.sofia.uni.fmi.mjt.spotify.server.validation;

import bg.sofia.uni.fmi.mjt.spotify.common.exception.validation.BadSearchKeywordsException;
import bg.sofia.uni.fmi.mjt.spotify.common.exception.validation.BadSongTitleException;
import bg.sofia.uni.fmi.mjt.spotify.common.exception.validation.BadTopNException;

import java.util.List;

public final class SongValidatorImpl implements SongValidator {
    @Override
    public void validateSearch(List<String> keywords) throws BadSearchKeywordsException {
        SearchKeywordsValidator.areValidKeywords(keywords);
    }

    @Override
    public void validateTopNSongs(int n) throws BadTopNException {
        TopNValidator.validateTopN(n);
    }

    @Override
    public void validatePlaySong(String songTitle) throws BadSongTitleException {
        SongTitleValidator.isValidSongTitle(songTitle);
    }
}
