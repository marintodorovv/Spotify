package bg.sofia.uni.fmi.mjt.spotify.server.validation;

import bg.sofia.uni.fmi.mjt.spotify.common.exception.validation.BadSearchKeywordsException;
import bg.sofia.uni.fmi.mjt.spotify.common.exception.validation.BadSongTitleException;
import bg.sofia.uni.fmi.mjt.spotify.common.exception.validation.BadTopNException;

import java.util.List;

public interface SongValidator extends Validator {
    void validateSearch(List<String> keywords) throws BadSearchKeywordsException;

    void validateTopNSongs(int n) throws BadTopNException;

    void validatePlaySong(String songTitle) throws BadSongTitleException;
}
