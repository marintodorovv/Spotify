package bg.sofia.uni.fmi.mjt.spotify.server.service.streaming;

import bg.sofia.uni.fmi.mjt.spotify.common.exception.SpotifyException;
import bg.sofia.uni.fmi.mjt.spotify.common.exception.network.MissingClientChannelException;
import bg.sofia.uni.fmi.mjt.spotify.common.exception.song.AlreadyStreamingException;
import bg.sofia.uni.fmi.mjt.spotify.common.exception.song.NoSongStreamingException;
import bg.sofia.uni.fmi.mjt.spotify.common.exception.song.NoSuchSongException;
import bg.sofia.uni.fmi.mjt.spotify.common.exception.validation.BadSongTitleException;
import bg.sofia.uni.fmi.mjt.spotify.common.exception.validation.BadTopNException;
import bg.sofia.uni.fmi.mjt.spotify.server.model.Song;
import bg.sofia.uni.fmi.mjt.spotify.server.repository.SongRepository;
import bg.sofia.uni.fmi.mjt.spotify.server.validation.SongValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.nio.channels.SocketChannel;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class StreamingServiceImplTest {
    private SongRepository songRepository;
    private SongValidator songValidator;
    private StreamingServiceImpl streamingService;
    private SocketChannel clientChannel;

    private static final String SONGS_DIR = "/songs";

    @BeforeEach
    void setUp() {
        songRepository = mock(SongRepository.class);
        songValidator = mock(SongValidator.class);
        clientChannel = mock(SocketChannel.class);

        streamingService = new StreamingServiceImpl(songRepository, SONGS_DIR, songValidator);
    }

    @Test
    void testStartStreamingNullChannel() {
        assertThrows(MissingClientChannelException.class,
                () -> streamingService.startStreaming(null, "Star67"));
    }

    @Test
    void testStartStreamingAlreadyStreaming() throws SpotifyException {
        Song song = new Song("1", "Drake", "Star67", "gods_plan.wav");
        when(songRepository.find("Star67")).thenReturn(Optional.of(song));

        streamingService.startStreaming(clientChannel, "Star67");

        assertThrows(AlreadyStreamingException.class,
                () -> streamingService.startStreaming(clientChannel, "Star67"));
    }

    @Test
    void testStartStreamingNoSuchSong() throws SpotifyException {
        when(songRepository.find("Star67")).thenReturn(Optional.empty());

        assertThrows(NoSuchSongException.class,
                () -> streamingService.startStreaming(clientChannel, "Star67"));
    }

    @Test
    void testStartStreamingCallsValidator() throws SpotifyException {
        doThrow(new BadSongTitleException("invalid")).when(songValidator).validatePlaySong("Star67");

        assertThrows(BadSongTitleException.class,
                () -> streamingService.startStreaming(clientChannel, "Star67"));
    }

    @Test
    void testStopStreamingNullChannel() {
        assertThrows(MissingClientChannelException.class,
                () -> streamingService.stopStreaming(null));
    }

    @Test
    void testStopStreamingNotStreaming() {
        assertThrows(NoSongStreamingException.class,
                () -> streamingService.stopStreaming(clientChannel));
    }

    @Test
    void testTopNSongsSuccess() throws SpotifyException {
        Song song1 = new Song("1", "Drake", "Song1", "s1.wav");
        Song song2 = new Song("2", "Drake", "Song2", "s2.wav");
        SocketChannel channel2 = mock(SocketChannel.class);

        when(songRepository.find("Song1")).thenReturn(Optional.of(song1));
        when(songRepository.find("Song2")).thenReturn(Optional.of(song2));

        streamingService.startStreaming(clientChannel, "Song1");
        streamingService.startStreaming(channel2, "Song2");

        List<String> topSongs = streamingService.topNSongs(2);

        assertEquals(2, topSongs.size());
    }

    @Test
    void testTopNSongsCallsValidator() throws SpotifyException {
        doThrow(new BadTopNException("invalid")).when(songValidator).validateTopNSongs(-1);

        assertThrows(BadTopNException.class, () -> streamingService.topNSongs(-1));
    }

    @Test
    void testStopAllStreams() throws SpotifyException {
        Song song = new Song("1", "Drake", "Star67", "gods_plan.wav");
        when(songRepository.find("Star67")).thenReturn(Optional.of(song));

        streamingService.startStreaming(clientChannel, "Star67");
        streamingService.stopAllStreams();

        List<String> topSongs = streamingService.topNSongs(1);
        assertEquals(0, topSongs.size());
    }
}
