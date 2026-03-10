package bg.sofia.uni.fmi.mjt.spotify.server.service;

import bg.sofia.uni.fmi.mjt.spotify.common.exception.validation.BadSearchKeywordsException;
import bg.sofia.uni.fmi.mjt.spotify.server.model.Song;
import bg.sofia.uni.fmi.mjt.spotify.server.repository.SongRepository;
import bg.sofia.uni.fmi.mjt.spotify.server.validation.SongValidator;
import bg.sofia.uni.fmi.mjt.spotify.server.validation.SongValidatorImpl;

import java.util.List;

public final class SongServiceImpl implements SongService {
    private final SongRepository songRepository;
    private final SongValidator songValidator;

    public SongServiceImpl(SongRepository songRepository, SongValidator songValidator) {
        this.songRepository = songRepository;
        this.songValidator = songValidator;
    }

    public SongServiceImpl(SongRepository songRepository) {
        this(songRepository, new SongValidatorImpl());
    }

    @Override
    public List<Song> search(List<String> keywords) throws BadSearchKeywordsException {
        songValidator.validateSearch(keywords);

        return songRepository.search(keywords);
    }
}
