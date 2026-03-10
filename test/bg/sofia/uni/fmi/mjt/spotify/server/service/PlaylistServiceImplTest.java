package bg.sofia.uni.fmi.mjt.spotify.server.service;

import bg.sofia.uni.fmi.mjt.spotify.common.exception.SpotifyException;
import bg.sofia.uni.fmi.mjt.spotify.common.exception.playlist.NoSuchPlaylistException;
import bg.sofia.uni.fmi.mjt.spotify.common.exception.playlist.PlaylistAlreadyExistsException;
import bg.sofia.uni.fmi.mjt.spotify.common.exception.playlist.SongAlreadyInPlaylistException;
import bg.sofia.uni.fmi.mjt.spotify.common.exception.song.NoSuchSongException;
import bg.sofia.uni.fmi.mjt.spotify.common.exception.validation.BadEmailFormatException;
import bg.sofia.uni.fmi.mjt.spotify.server.model.Playlist;
import bg.sofia.uni.fmi.mjt.spotify.server.model.Song;
import bg.sofia.uni.fmi.mjt.spotify.server.repository.PlaylistRepository;
import bg.sofia.uni.fmi.mjt.spotify.server.repository.SongRepository;
import bg.sofia.uni.fmi.mjt.spotify.server.validation.PlaylistValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PlaylistServiceImplTest {
    private PlaylistRepository playlistRepository;
    private SongRepository songRepository;
    private PlaylistValidator playlistValidator;
    private PlaylistServiceImpl playlistService;

    @BeforeEach
    void setUp() {
        songRepository = mock(SongRepository.class);
        playlistValidator = mock(PlaylistValidator.class);
        playlistRepository = mock(PlaylistRepository.class);

        playlistService = new PlaylistServiceImpl(playlistRepository, songRepository, playlistValidator);
    }

    @Test
    void testCreatePlaylistSuccess() throws SpotifyException {
        when(playlistRepository.playlistExists("a@b.c", "playlist")).thenReturn(false);

        assertDoesNotThrow(() -> playlistService.createPlaylist("playlist", "a@b.c"));
    }

    @Test
    void testCreatePlaylistCallsValidator() throws SpotifyException {
        when(playlistRepository.playlistExists("a@b.c", "playlist")).thenReturn(false);

        playlistService.createPlaylist("playlist", "a@b.c");

        verify(playlistValidator).validateCreate("a@b.c", "playlist");
    }

    @Test
    void testCreatePlaylistAlreadyExists() {
        when(playlistRepository.playlistExists("a@b.c", "playlist")).thenReturn(true);

        assertThrows(PlaylistAlreadyExistsException.class, () -> playlistService.createPlaylist("playlist", "a@b.c"));
    }

    @Test
    void testCreatePlaylistInvalidEmail() throws SpotifyException {
        doThrow(new BadEmailFormatException("invalid")).when(playlistValidator).validateCreate("invalid", "playlist");

        assertThrows(BadEmailFormatException.class, () -> playlistService.createPlaylist("playlist", "invalid"));
    }

    @Test
    void testAddSongSuccess() throws SpotifyException {
        Song song = new Song("1", "Drake", "Star67", "a.wav");
        when(songRepository.find("song")).thenReturn(Optional.of(song));
        when(playlistRepository.playlistExists("a@b.c", "playlist")).thenReturn(true);
        when(playlistRepository.playlistContainsSong("a@b.c", "playlist", "song")).thenReturn(false);

        assertDoesNotThrow(() -> playlistService.addSongToPlaylist("playlist", "song", "a@b.c"));
    }

    @Test
    void testAddSongNoSuchSong() {
        when(songRepository.find("song")).thenReturn(Optional.empty());

        assertThrows(NoSuchSongException.class, () -> playlistService.addSongToPlaylist("playlist", "song", "a@b.c"));
    }

    @Test
    void testAddSongNoSuchPlaylist() {
        Song song = new Song("1", "Drake", "Star67", "a.wav");
        when(songRepository.find("song")).thenReturn(Optional.of(song));
        when(playlistRepository.playlistExists("a@b.c", "playlist")).thenReturn(false);

        assertThrows(NoSuchPlaylistException.class, () -> playlistService.addSongToPlaylist("playlist", "song", "a@b.c"));
    }

    @Test
    void testAddSongAlreadyInPlaylist() {
        Song song = new Song("1", "Drake", "Star67", "a.wav");
        when(songRepository.find("song")).thenReturn(Optional.of(song));
        when(playlistRepository.playlistExists("a@b.c", "playlist")).thenReturn(true);
        when(playlistRepository.playlistContainsSong("a@b.c", "playlist", "song")).thenReturn(true);

        assertThrows(SongAlreadyInPlaylistException.class, () -> playlistService.addSongToPlaylist("playlist", "song", "a@b.c"));
    }

    @Test
    void testGetAllSongsSuccess() throws SpotifyException {
        when(playlistRepository.playlistExists("a@b.c", "playlist")).thenReturn(true);
        when(playlistRepository.getSongsFromPlaylist("a@b.c", "playlist")).thenReturn(List.of("song1", "song2"));

        List<String> result = playlistService.getAllSongsFromPlaylist("playlist", "a@b.c");

        assertEquals(2, result.size());
    }

    @Test
    void testGetAllSongsNoSuchPlaylist() {
        when(playlistRepository.playlistExists("a@b.c", "playlist")).thenReturn(false);

        assertThrows(NoSuchPlaylistException.class, () -> playlistService.getAllSongsFromPlaylist("playlist", "a@b.c"));
    }

    @Test
    void testGetAllPlaylistsSuccess() throws SpotifyException {
        List<Playlist> playlists = List.of(new Playlist("playlist", "a@b.c", List.of()));
        when(playlistRepository.getAllPlaylistsForUser("a@b.c")).thenReturn(playlists);

        List<Playlist> result = playlistService.getAllPlaylistsForUser("a@b.c");

        assertEquals(1, result.size());
    }

    @Test
    void testGetAllPlaylistsCallsValidator() throws SpotifyException {
        when(playlistRepository.getAllPlaylistsForUser("a@b.c")).thenReturn(List.of());

        playlistService.getAllPlaylistsForUser("a@b.c");

        verify(playlistValidator).validateGetAllPlaylistsForUser("a@b.c");
    }
}
