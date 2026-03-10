package bg.sofia.uni.fmi.mjt.spotify.server.request;

import java.nio.channels.SocketChannel;
import java.util.EnumMap;
import java.util.Map;

public record CommandRequest(String command, Map<CommandArgument, String> arguments, SocketChannel clientChannel) {
    private static final String EMPTY_STRING = "";
    private static final String REGEX_FOR_SPACE = " ";

    private static final int MAX_PARTS = 2;
    private static final int COMMAND_PART_INDEX = 0;
    private static final int ARGUMENT_PART_INDEX = 1;

    public String getArgument(CommandArgument argument) {
        return arguments.get(argument);
    }

    public SocketChannel getClientChannel() {
        return clientChannel;
    }

    public static CommandRequest parse(String clientMessage, SocketChannel clientChannel) {
        if (clientMessage == null || clientMessage.isBlank()) {
            return new CommandRequest(EMPTY_STRING, Map.of(), clientChannel);
        }

        String[] parts = clientMessage.trim().split(REGEX_FOR_SPACE, MAX_PARTS);
        String cmdName = parts[COMMAND_PART_INDEX].toLowerCase();
        String remainder = parts.length > ARGUMENT_PART_INDEX ? parts[ARGUMENT_PART_INDEX] : EMPTY_STRING;

        Map<CommandArgument, String> args = new EnumMap<>(CommandArgument.class);

        switch (cmdName) {
            case "register", "login" -> splitArgs(remainder, args, CommandArgument.EMAIL, CommandArgument.PASSWORD);
            case "search" -> args.put(CommandArgument.KEYWORDS, remainder);
            case "top" -> args.put(CommandArgument.NUMBER, remainder);
            case "create-playlist", "show-playlist" -> args.put(CommandArgument.PLAYLIST_NAME, remainder);
            case "play" -> args.put(CommandArgument.SONG_TITLE, remainder);
            case "add-song-to" -> splitArgs(remainder, args, CommandArgument.PLAYLIST_NAME, CommandArgument.SONG_TITLE);
        }

        return new CommandRequest(cmdName, args, clientChannel);
    }

    //cool elipsis
    private static void splitArgs(String input, Map<CommandArgument, String> args, CommandArgument... keys) {
        String[] tokens = input.split(REGEX_FOR_SPACE, keys.length);
        for (int i = COMMAND_PART_INDEX; i < tokens.length; i++) {
            args.put(keys[i], tokens[i]);
        }
    }
}
