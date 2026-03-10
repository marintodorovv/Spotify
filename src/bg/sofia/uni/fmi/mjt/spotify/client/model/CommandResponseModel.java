package bg.sofia.uni.fmi.mjt.spotify.client.model;

import java.util.List;

public record CommandResponseModel(boolean success,
                                   List<String> response) {

    private static final String EMPTY_STRING = "";

    public String getFormattedResponse() {
        if (response == null || response.isEmpty()) {
            return EMPTY_STRING;
        }

        StringBuilder sb = new StringBuilder();
        sb.append(response.getFirst()).append(System.lineSeparator());

        for (int i = 1; i < response.size(); i++) {
            sb.append(response.get(i)).append(System.lineSeparator());
        }
        return sb.toString();
    }

    @Override
    public String toString() {
        return getFormattedResponse();
    }
}
