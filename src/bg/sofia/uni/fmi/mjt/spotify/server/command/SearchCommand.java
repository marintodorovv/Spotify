package bg.sofia.uni.fmi.mjt.spotify.server.command;

import bg.sofia.uni.fmi.mjt.spotify.common.exception.validation.BadSearchKeywordsException;
import bg.sofia.uni.fmi.mjt.spotify.server.context.UserContext;
import bg.sofia.uni.fmi.mjt.spotify.server.model.Song;
import bg.sofia.uni.fmi.mjt.spotify.server.reponse.CommandResponse;
import bg.sofia.uni.fmi.mjt.spotify.server.request.CommandArgument;
import bg.sofia.uni.fmi.mjt.spotify.server.request.CommandRequest;
import bg.sofia.uni.fmi.mjt.spotify.server.service.SongService;

import java.util.Arrays;
import java.util.List;

public final class SearchCommand implements Command {
    private static final String NO_SONGS_FIT_THE_CURRENT_SEARCH_KEYWORDS_MESSAGE = "No songs fit the current search " +
            "keywords!";
    private static final String NO_SEARCH_KEYWORDS_PROVIDED_MESSAGE = "Please provide search keywords!";
    private static final String USER_NOT_LOGGED_IN_MESSAGE = "You must log in to do this!";
    private static final String SPACE = " ";

    private final SongService songService;

    public SearchCommand(SongService songService) {
        this.songService = songService;
    }

    @Override
    public CommandResponse execute(CommandRequest request, UserContext userContext) throws BadSearchKeywordsException {
        if (!userContext.isLogged()) {
            return CommandResponse.error(USER_NOT_LOGGED_IN_MESSAGE);
        }

        String words = request.getArgument(CommandArgument.KEYWORDS);

        if (words == null || words.isBlank()) {
            return CommandResponse.error(NO_SEARCH_KEYWORDS_PROVIDED_MESSAGE);
        }

        List<String> keywords = Arrays.stream(words.split(SPACE)).toList();

        List<Song> results = songService.search(keywords);

        if (results.isEmpty()) {
            return CommandResponse.ok(NO_SONGS_FIT_THE_CURRENT_SEARCH_KEYWORDS_MESSAGE);
        }

        return CommandResponse.data(results.stream().map(Song::toString).toList());
    }
}
