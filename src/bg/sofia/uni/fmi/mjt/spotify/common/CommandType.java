package bg.sofia.uni.fmi.mjt.spotify.common;

public enum CommandType {
    REGISTER("register"),
    LOGIN("login"),
    DISCONNECT("disconnect"),
    SEARCH("search"),
    TOP("top"),
    CREATE_PLAYLIST("create-playlist"),
    ADD_SONG_TO("add-song-to"),
    SHOW_PLAYLIST("show-playlist"),
    PLAY("play"),
    STOP("stop"),
    HELP("help");

    private final String commandName;

    CommandType(String commandName) {
        this.commandName = commandName;
    }

    public String getCommandName() {
        return commandName;
    }

    public static CommandType fromString(String text) {
        for (CommandType type : CommandType.values()) {
            if (type.commandName.equalsIgnoreCase(text)) {
                return type;
            }
        }
        return null;
    }
}
