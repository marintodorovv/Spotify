package bg.sofia.uni.fmi.mjt.spotify.server.command.helpers;

import bg.sofia.uni.fmi.mjt.spotify.common.exception.SpotifyException;
import bg.sofia.uni.fmi.mjt.spotify.server.command.Command;
import bg.sofia.uni.fmi.mjt.spotify.server.context.UserContext;
import bg.sofia.uni.fmi.mjt.spotify.server.reponse.CommandResponse;
import bg.sofia.uni.fmi.mjt.spotify.server.request.CommandRequest;

import java.util.logging.Level;
import java.util.logging.Logger;

public final class CommandExecutor {
    private static final String UNEXPECTED_ERROR_MESSAGE = "UNEXPECTED ERROR!";
    private static final String ENCOUNTERED_AN_ERROR_WHILE_EXECUTING_COMMAND_MESSAGE = "Encountered an error while " +
            "executing command!";
    private static final String NO_SUCH_COMMAND_EXISTS_MESSAGE = "No such command exists!";

    private static final Logger LOGGER = Logger.getLogger("SpotifyLogger");
    private final CommandRegistry commandRegistry;

    CommandExecutor(CommandRegistry commandRegistry) {
        this.commandRegistry = commandRegistry;
    }

    public CommandResponse execute(CommandRequest request, UserContext userContext) {
        Command command = commandRegistry.getCommand(request.command());

        if (command == null) {
            return CommandResponse.error(NO_SUCH_COMMAND_EXISTS_MESSAGE);
        }

        try {
            return command.execute(request, userContext);
        } catch (SpotifyException e) {
            LOGGER.log(Level.WARNING, ENCOUNTERED_AN_ERROR_WHILE_EXECUTING_COMMAND_MESSAGE, e);
            return CommandResponse.error(e.getMessage());
        } catch (Exception e) {
            //STOP SERVER FROM CRASHING ON RUNTIME EXCEPTION
            LOGGER.log(Level.SEVERE, UNEXPECTED_ERROR_MESSAGE, e);
            return CommandResponse.error(UNEXPECTED_ERROR_MESSAGE);
        }
    }
}
