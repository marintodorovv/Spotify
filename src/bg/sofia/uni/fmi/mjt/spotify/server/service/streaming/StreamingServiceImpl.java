package bg.sofia.uni.fmi.mjt.spotify.server.service.streaming;

import bg.sofia.uni.fmi.mjt.spotify.common.exception.network.MissingClientChannelException;
import bg.sofia.uni.fmi.mjt.spotify.common.exception.song.AlreadyStreamingException;
import bg.sofia.uni.fmi.mjt.spotify.common.exception.song.NoSongStreamingException;
import bg.sofia.uni.fmi.mjt.spotify.common.exception.song.NoSuchSongException;
import bg.sofia.uni.fmi.mjt.spotify.common.exception.validation.BadSongTitleException;
import bg.sofia.uni.fmi.mjt.spotify.common.exception.validation.BadTopNException;
import bg.sofia.uni.fmi.mjt.spotify.server.repository.SongRepository;
import bg.sofia.uni.fmi.mjt.spotify.server.streaming.AudioStreamThread;
import bg.sofia.uni.fmi.mjt.spotify.server.validation.SongValidator;
import bg.sofia.uni.fmi.mjt.spotify.server.validation.SongValidatorImpl;

import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public final class StreamingServiceImpl implements StreamingService {
    private static final String NOT_STREAMING_MESSAGE = "You are not currently streaming a song!";
    private static final String UNEXPECTED_NETWORK_ERROR_PLEASE_TRY_AGAIN_MESSAGE =
            "Unexpected network error, please try again!";
    private static final String NO_SUCH_SONG_EXISTS_MESSAGE = "No such song exists!";
    private static final String ALREADY_STREAMING_A_SONG_MESSAGE = "You are already streaming a song!";

    private static final String DASH = "/";
    private static final String SPACER = " : ";

    private final SongRepository songRepository;
    private final SongValidator songValidator;

    private final String songsDirectory;
    private final Map<SocketChannel, AudioStreamThread> activeStreams;
    private final Map<String, AtomicInteger> currentlyPlaying;
    //Atomic int is probably unnecessary as ive switched to compute, but ill leave it in

    public StreamingServiceImpl(SongRepository songRepository, String songsDirectory, SongValidator songValidator) {
        this.songRepository = songRepository;
        this.songsDirectory = songsDirectory;
        this.songValidator = songValidator;
        this.activeStreams = new ConcurrentHashMap<>();
        this.currentlyPlaying = new ConcurrentHashMap<>();
    }

    public StreamingServiceImpl(SongRepository songRepository, String songsDirectory) {
        this(songRepository, songsDirectory, new SongValidatorImpl());
    }

    @Override
    public void startStreaming(SocketChannel clientChannel, String songName)
            throws BadSongTitleException, NoSuchSongException, MissingClientChannelException,
            AlreadyStreamingException {
        if (clientChannel == null) {
            throw new MissingClientChannelException(UNEXPECTED_NETWORK_ERROR_PLEASE_TRY_AGAIN_MESSAGE);
        }

        if (isStreaming(clientChannel)) {
            throw new AlreadyStreamingException(ALREADY_STREAMING_A_SONG_MESSAGE);
        }

        songValidator.validatePlaySong(songName);

        if (songRepository.find(songName).isEmpty()) {
            throw new NoSuchSongException(NO_SUCH_SONG_EXISTS_MESSAGE);
        }

        String songPath = songsDirectory + DASH + songRepository.find(songName).get().fileName();

        AudioStreamThread streamingThread = new AudioStreamThread(clientChannel, songPath, songName);
        activeStreams.put(clientChannel, streamingThread);
        incrementCurrentlyPlaying(songName);
        streamingThread.start();
    }

    @Override
    public void stopStreaming(SocketChannel clientChannel)
            throws MissingClientChannelException, NoSongStreamingException {
        if (clientChannel == null) {
            throw new MissingClientChannelException(UNEXPECTED_NETWORK_ERROR_PLEASE_TRY_AGAIN_MESSAGE);
        }

        //CURSED RACE CONDITION!
        if (!isStreaming(clientChannel)) {
            throw new NoSongStreamingException(NOT_STREAMING_MESSAGE);
        }

        AudioStreamThread stream = activeStreams.remove(clientChannel);

        if (stream != null) {
            stream.stopStreaming();
            decrementCurrentlyPlaying(stream.getSongTitle());
        }
    }

    @Override
    public List<String> topNSongs(int n) throws BadTopNException {
        songValidator.validateTopNSongs(n);

        List<String> topSongs = new ArrayList<>();

        List<Map.Entry<String, AtomicInteger>> entries = new ArrayList<>(currentlyPlaying.entrySet());

        entries.sort((a, b) -> b.getValue().get() - a.getValue().get());

        int count = 0;
        for (Map.Entry<String, AtomicInteger> entry : entries) {
            if (count >= n) {
                break;
            }
            topSongs.add(entry.getKey() + SPACER +  entry.getValue());
            count++;
        }

        return topSongs;
    }

    @Override
    public void stopAllStreams() {
        for (AudioStreamThread stream : activeStreams.values()) {
            stream.stopStreaming();
        }
        activeStreams.clear();
        currentlyPlaying.clear();
    }

    private boolean isStreaming(SocketChannel clientChannel) {
        AudioStreamThread stream = activeStreams.get(clientChannel);
//        return (stream != null && stream.isAlive());
        return stream != null; //RACE CONDITION
    }

    private void incrementCurrentlyPlaying(String songName) {
        currentlyPlaying.compute(songName, (key, count) -> {
            if (count == null) {
                return new AtomicInteger(1);
            }
            count.incrementAndGet();
            return count;
        });
    }

    private void decrementCurrentlyPlaying(String songName) {
        currentlyPlaying.computeIfPresent(songName, (key, count) -> {
            if (count.decrementAndGet() <= 0) {
                return null;
            }
            return count;
        });
    }
}
