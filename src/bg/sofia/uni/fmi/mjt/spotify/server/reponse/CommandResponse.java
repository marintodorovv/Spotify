package bg.sofia.uni.fmi.mjt.spotify.server.reponse;

import com.google.gson.Gson;

import java.util.List;

public record CommandResponse(boolean success, List<String> response) {
    private static final Gson GSON = new Gson();

    public static CommandResponse ok(String message) {
        return new CommandResponse(true, List.of(message));
    }

    public static CommandResponse error(String message) {
        return new CommandResponse(false, List.of(message));
    }

    public static CommandResponse data(List<String> data) {
        return new CommandResponse(true, data);
    }

    @Override
    public String toString() {
        return GSON.toJson(this);
    }
}
