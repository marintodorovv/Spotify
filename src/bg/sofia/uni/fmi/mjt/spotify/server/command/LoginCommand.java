package bg.sofia.uni.fmi.mjt.spotify.server.command;

import bg.sofia.uni.fmi.mjt.spotify.common.exception.authentication.UserDoesNotExistsException;
import bg.sofia.uni.fmi.mjt.spotify.common.exception.authentication.WrongPasswordException;
import bg.sofia.uni.fmi.mjt.spotify.common.exception.validation.BadEmailFormatException;
import bg.sofia.uni.fmi.mjt.spotify.common.exception.validation.BadPasswordFormatException;
import bg.sofia.uni.fmi.mjt.spotify.server.context.UserContext;
import bg.sofia.uni.fmi.mjt.spotify.server.model.User;
import bg.sofia.uni.fmi.mjt.spotify.server.reponse.CommandResponse;
import bg.sofia.uni.fmi.mjt.spotify.server.request.CommandArgument;
import bg.sofia.uni.fmi.mjt.spotify.server.request.CommandRequest;
import bg.sofia.uni.fmi.mjt.spotify.server.service.UserService;

public final class LoginCommand implements Command {
    private static final String SUCCESSFUL_LOG_IN_MESSAGE = "User logged in successfully!";
    private static final String USER_ALREADY_LOGGED_IN_MESSAGE = "This user is already logged in!";

    private final UserService userService;

    public LoginCommand(UserService userService) {
        this.userService = userService;
    }

    @Override
    public CommandResponse execute(CommandRequest request, UserContext userContext)
            throws BadEmailFormatException, UserDoesNotExistsException, BadPasswordFormatException,
            WrongPasswordException {
        if (userContext.isLogged()) {
            return CommandResponse.error(USER_ALREADY_LOGGED_IN_MESSAGE);
        }

        String email = request.getArgument(CommandArgument.EMAIL);
        String password = request.getArgument(CommandArgument.PASSWORD);

        User user = userService.authenticate(email, password);
        userContext.login(user);

        return CommandResponse.ok(SUCCESSFUL_LOG_IN_MESSAGE);
    }
}
