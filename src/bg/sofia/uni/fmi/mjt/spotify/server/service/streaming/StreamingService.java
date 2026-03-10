package bg.sofia.uni.fmi.mjt.spotify.server.service.streaming;

import bg.sofia.uni.fmi.mjt.spotify.common.exception.network.MissingClientChannelException;
import bg.sofia.uni.fmi.mjt.spotify.common.exception.song.AlreadyStreamingException;
import bg.sofia.uni.fmi.mjt.spotify.common.exception.song.NoSongStreamingException;
import bg.sofia.uni.fmi.mjt.spotify.common.exception.song.NoSuchSongException;
import bg.sofia.uni.fmi.mjt.spotify.common.exception.validation.BadSongTitleException;
import bg.sofia.uni.fmi.mjt.spotify.common.exception.validation.BadTopNException;
import bg.sofia.uni.fmi.mjt.spotify.server.service.Service;

import java.nio.channels.SocketChannel;
import java.util.List;

public interface StreamingService extends Service {
    void startStreaming(SocketChannel clientChannel, String songName)
            throws BadSongTitleException, NoSuchSongException, MissingClientChannelException,
            NoSongStreamingException, AlreadyStreamingException;

    void stopStreaming(SocketChannel clientChannel) throws MissingClientChannelException, NoSongStreamingException;

    List<String> topNSongs(int n) throws BadTopNException;

    void stopAllStreams();
}
