package bg.sofia.uni.fmi.mjt.spotify.server.command;

import bg.sofia.uni.fmi.mjt.spotify.common.exception.network.MissingClientChannelException;
import bg.sofia.uni.fmi.mjt.spotify.common.exception.song.NoSongStreamingException;
import bg.sofia.uni.fmi.mjt.spotify.server.context.UserContext;
import bg.sofia.uni.fmi.mjt.spotify.server.reponse.CommandResponse;
import bg.sofia.uni.fmi.mjt.spotify.server.request.CommandRequest;
import bg.sofia.uni.fmi.mjt.spotify.server.service.streaming.StreamingService;

public final class StopPlayingSongCommand implements Command {
    private static final String INTERNAL_ERROR_NO_CLIENT_CHANNEL_AVAILABLE_MESSAGE =
            "Encountered an error, please try again!";
    private static final String USER_NOT_LOGGED_IN_MESSAGE = "You must be logged in to stop playback!";
    private static final String PLAYBACK_STOPPED_MESSAGE = "Playback stopped.";
    private static final String NO_SONG_PLAYING_MESSAGE = "No song is currently playing.";

    private final StreamingService streamingService;

    public StopPlayingSongCommand(StreamingService streamingService) {
        this.streamingService = streamingService;
    }

    @Override
    public CommandResponse execute(CommandRequest request, UserContext userContext)
            throws MissingClientChannelException, NoSongStreamingException {
        if (!userContext.isLogged()) {
            return CommandResponse.error(USER_NOT_LOGGED_IN_MESSAGE);
        }

        streamingService.stopStreaming(request.getClientChannel());

        return CommandResponse.ok(PLAYBACK_STOPPED_MESSAGE);
    }
}
