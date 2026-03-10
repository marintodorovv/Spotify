package bg.sofia.uni.fmi.mjt.spotify.server.validation;

import bg.sofia.uni.fmi.mjt.spotify.common.exception.validation.BadSearchKeywordsException;

import java.util.List;

final class SearchKeywordsValidator {
    private static final String NULL_SEARCH_KEYWORDS_MESSAGE = "Search keywords must not be null!";
    private static final String EMPTY_SEARCH_KEYWORDS_MESSAGE = "Search keywords must not be empty!";
    private static final String BLANK_SEARCH_KEYWORD_MESSAGE = "Search keyword must not be blank!";

    private SearchKeywordsValidator() {
    }

    public static void areValidKeywords(List<String> keywords) throws BadSearchKeywordsException {
        if (keywords == null) {
            throw new BadSearchKeywordsException(NULL_SEARCH_KEYWORDS_MESSAGE);
        }

        if (keywords.isEmpty()) {
            throw new BadSearchKeywordsException(EMPTY_SEARCH_KEYWORDS_MESSAGE);
        }

        for (String keyword : keywords) {
            if (keyword.trim().isBlank()) {
                throw new BadSearchKeywordsException(BLANK_SEARCH_KEYWORD_MESSAGE);
            }
        }
    }
}
