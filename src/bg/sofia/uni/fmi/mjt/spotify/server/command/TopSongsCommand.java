package bg.sofia.uni.fmi.mjt.spotify.server.command;

import bg.sofia.uni.fmi.mjt.spotify.common.exception.validation.BadTopNException;
import bg.sofia.uni.fmi.mjt.spotify.server.context.UserContext;
import bg.sofia.uni.fmi.mjt.spotify.server.reponse.CommandResponse;
import bg.sofia.uni.fmi.mjt.spotify.server.request.CommandArgument;
import bg.sofia.uni.fmi.mjt.spotify.server.request.CommandRequest;
import bg.sofia.uni.fmi.mjt.spotify.server.service.streaming.StreamingService;

import java.util.List;

public final class TopSongsCommand implements Command {
    private static final String NO_SONGS_ARE_CURRENTLY_BEING_STREAMED_MESSAGE =
            "No songs are currently being streamed!";
    private static final String USER_NOT_LOGGED_IN_MESSAGE = "You need to be logged in to do this!";

    private final StreamingService streamingService;

    public TopSongsCommand(StreamingService streamingService) {
        this.streamingService = streamingService;
    }

    @Override
    public CommandResponse execute(CommandRequest request, UserContext userContext) throws BadTopNException {
        if (!userContext.isLogged()) {
            return CommandResponse.error(USER_NOT_LOGGED_IN_MESSAGE);
        }

        int n = Integer.parseInt(request.getArgument(CommandArgument.NUMBER));

        List<String> top = streamingService.topNSongs(n);

        if (top.isEmpty()) {
            return CommandResponse.ok(NO_SONGS_ARE_CURRENTLY_BEING_STREAMED_MESSAGE);
        }

        return CommandResponse.data(top);
    }
}
