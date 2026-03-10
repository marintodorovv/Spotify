package bg.sofia.uni.fmi.mjt.spotify.server.command;

import bg.sofia.uni.fmi.mjt.spotify.common.exception.network.MissingClientChannelException;
import bg.sofia.uni.fmi.mjt.spotify.common.exception.song.AlreadyStreamingException;
import bg.sofia.uni.fmi.mjt.spotify.common.exception.song.NoSongStreamingException;
import bg.sofia.uni.fmi.mjt.spotify.common.exception.song.NoSuchSongException;
import bg.sofia.uni.fmi.mjt.spotify.common.exception.validation.BadSongTitleException;
import bg.sofia.uni.fmi.mjt.spotify.server.context.UserContext;
import bg.sofia.uni.fmi.mjt.spotify.server.reponse.CommandResponse;
import bg.sofia.uni.fmi.mjt.spotify.server.request.CommandArgument;
import bg.sofia.uni.fmi.mjt.spotify.server.request.CommandRequest;
import bg.sofia.uni.fmi.mjt.spotify.server.service.streaming.StreamingService;

import java.nio.channels.SocketChannel;

public final class PlaySongCommand implements Command {
    private static final String USER_NOT_LOGGED_IN_MESSAGE = "You must be logged in to play songs!";
    private static final String SONG_NOT_FOUND_MESSAGE = "Song not found!";
    private static final String STARTING_PLAYBACK_MESSAGE = "Starting playback of: ";

    private final StreamingService streamingService;

    public PlaySongCommand(StreamingService streamingService) {
        this.streamingService = streamingService;
    }

    @Override
    public CommandResponse execute(CommandRequest request, UserContext userContext)
            throws BadSongTitleException, NoSuchSongException, MissingClientChannelException,
            NoSongStreamingException, AlreadyStreamingException {
        if (!userContext.isLogged()) {
            return CommandResponse.error(USER_NOT_LOGGED_IN_MESSAGE);
        }

        String songName = request.getArgument(CommandArgument.SONG_TITLE);

        SocketChannel clientChannel = request.clientChannel();

        streamingService.startStreaming(clientChannel, songName);

        return CommandResponse.ok(STARTING_PLAYBACK_MESSAGE + songName);
    }
}
