package bg.sofia.uni.fmi.mjt.spotify.server.command;

import bg.sofia.uni.fmi.mjt.spotify.common.exception.network.MissingClientChannelException;
import bg.sofia.uni.fmi.mjt.spotify.common.exception.song.NoSongStreamingException;
import bg.sofia.uni.fmi.mjt.spotify.server.context.UserContext;
import bg.sofia.uni.fmi.mjt.spotify.server.reponse.CommandResponse;
import bg.sofia.uni.fmi.mjt.spotify.server.request.CommandRequest;
import bg.sofia.uni.fmi.mjt.spotify.server.service.streaming.StreamingService;

public final class DisconnectCommand implements Command {
    private static final String SUCCESSFULLY_DISCONNECTED_MESSAGE = "Successfully disconnected!";
    private static final String USER_NOT_LOGGED_IN_MESSAGE = "You need to be logged in to do this!";

    private final StreamingService streamingService;

    public DisconnectCommand(StreamingService streamingService) {
        this.streamingService = streamingService;
    }

    @Override
    public CommandResponse execute(CommandRequest request, UserContext userContext)
            throws MissingClientChannelException, NoSongStreamingException {
        if (!userContext.isLogged()) {
            return CommandResponse.error(USER_NOT_LOGGED_IN_MESSAGE);
        }

        streamingService.stopStreaming(request.clientChannel());

        userContext.logout();
        return CommandResponse.ok(SUCCESSFULLY_DISCONNECTED_MESSAGE);
    }
}
