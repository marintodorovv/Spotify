package bg.sofia.uni.fmi.mjt.spotify.server.service;

import bg.sofia.uni.fmi.mjt.spotify.common.exception.validation.BadSearchKeywordsException;
import bg.sofia.uni.fmi.mjt.spotify.server.model.Song;

import java.util.List;

public interface SongService extends Service {
    List<Song> search(List<String> keywords) throws BadSearchKeywordsException;
}
