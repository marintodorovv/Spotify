package bg.sofia.uni.fmi.mjt.spotify.server.command.helpers;

import bg.sofia.uni.fmi.mjt.spotify.common.CommandType;
import bg.sofia.uni.fmi.mjt.spotify.server.command.Command;

import java.util.HashMap;
import java.util.Map;

final class CommandRegistry {
    private final Map<CommandType, Command> commandMap;

    CommandRegistry() {
        this.commandMap = new HashMap<>();
    }

    void register(CommandType type, Command command) {
        commandMap.put(type, command);
    }

    Command getCommand(String name) {
        CommandType type = CommandType.fromString(name);

        if (type == null) {
            return null;
        }

        return commandMap.get(type);
    }
}
