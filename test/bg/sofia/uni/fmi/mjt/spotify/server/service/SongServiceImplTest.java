package bg.sofia.uni.fmi.mjt.spotify.server.service;

import bg.sofia.uni.fmi.mjt.spotify.common.exception.SpotifyException;
import bg.sofia.uni.fmi.mjt.spotify.common.exception.validation.BadSearchKeywordsException;
import bg.sofia.uni.fmi.mjt.spotify.server.model.Song;
import bg.sofia.uni.fmi.mjt.spotify.server.repository.SongRepository;
import bg.sofia.uni.fmi.mjt.spotify.server.validation.SongValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.mock;

class SongServiceImplTest {
    private SongRepository songRepository;
    private SongValidator songValidator;
    private SongServiceImpl songService;

    @BeforeEach
    void setUp() {
        songRepository = mock(SongRepository.class);
        songValidator = mock(SongValidator.class);

        songService = new SongServiceImpl(songRepository, songValidator);
    }

    @Test
    void testSearchSuccess() throws SpotifyException {
        List<Song> songs = List.of(new Song("1", "Drake", "Star67", "a.wav"));
        when(songRepository.search(List.of("drake"))).thenReturn(songs);

        List<Song> result = songService.search(List.of("drake"));

        assertEquals(1, result.size());
    }

    @Test
    void testSearchCallsValidator() throws SpotifyException {
        List<Song> songs = List.of();
        when(songRepository.search(List.of("drake"))).thenReturn(songs);

        songService.search(List.of("drake"));

        verify(songValidator).validateSearch(List.of("drake"));
    }

    @Test
    void testSearchInvalidKeywords() throws SpotifyException {
        doThrow(new BadSearchKeywordsException("invalid")).when(songValidator).validateSearch(null);

        assertThrows(BadSearchKeywordsException.class, () -> songService.search(null));
    }
}
