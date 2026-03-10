package bg.sofia.uni.fmi.mjt.spotify.server.command;

import bg.sofia.uni.fmi.mjt.spotify.common.exception.authentication.UserAlreadyRegisteredException;
import bg.sofia.uni.fmi.mjt.spotify.common.exception.validation.BadEmailFormatException;
import bg.sofia.uni.fmi.mjt.spotify.common.exception.validation.BadPasswordFormatException;
import bg.sofia.uni.fmi.mjt.spotify.server.context.UserContext;
import bg.sofia.uni.fmi.mjt.spotify.server.reponse.CommandResponse;
import bg.sofia.uni.fmi.mjt.spotify.server.request.CommandArgument;
import bg.sofia.uni.fmi.mjt.spotify.server.request.CommandRequest;
import bg.sofia.uni.fmi.mjt.spotify.server.service.UserService;

public final class RegisterCommand implements Command {
    private static final String SUCCESSFUL_REGISTER_MESSAGE = "User registered successfully!";
    private static final String USER_ALREADY_LOGGED_IN_MESSAGE = "Log out to do this!";

    private final UserService userService;

    public RegisterCommand(UserService userService) {
        this.userService = userService;
    }

    @Override
    public CommandResponse execute(CommandRequest request, UserContext userContext)
            throws BadEmailFormatException, BadPasswordFormatException, UserAlreadyRegisteredException {
        if (userContext.isLogged()) {
            return CommandResponse.error(USER_ALREADY_LOGGED_IN_MESSAGE);
        }

        String email = request.getArgument(CommandArgument.EMAIL);
        String password = request.getArgument(CommandArgument.PASSWORD);

        userService.register(email, password);

        return CommandResponse.ok(SUCCESSFUL_REGISTER_MESSAGE);
    }
}
