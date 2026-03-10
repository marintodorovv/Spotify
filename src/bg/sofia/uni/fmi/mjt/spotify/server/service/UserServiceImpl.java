package bg.sofia.uni.fmi.mjt.spotify.server.service;

import bg.sofia.uni.fmi.mjt.spotify.common.exception.authentication.UserAlreadyRegisteredException;
import bg.sofia.uni.fmi.mjt.spotify.common.exception.authentication.UserDoesNotExistsException;
import bg.sofia.uni.fmi.mjt.spotify.common.exception.authentication.WrongPasswordException;
import bg.sofia.uni.fmi.mjt.spotify.common.exception.validation.BadEmailFormatException;
import bg.sofia.uni.fmi.mjt.spotify.common.exception.validation.BadPasswordFormatException;
import bg.sofia.uni.fmi.mjt.spotify.server.model.User;
import bg.sofia.uni.fmi.mjt.spotify.server.repository.UserRepository;
import bg.sofia.uni.fmi.mjt.spotify.server.security.PasswordHasher;
import bg.sofia.uni.fmi.mjt.spotify.server.validation.UserValidator;
import bg.sofia.uni.fmi.mjt.spotify.server.validation.UserValidatorImpl;

import java.util.Optional;

public final class UserServiceImpl implements UserService {
    private static final String WRONG_PASSWORD_MESSAGE = "Wrong password!";
    private static final String NO_SUCH_USER_MESSAGE = "No user with this email exists!";
    private static final String USER_ALREADY_REGISTERED_MESSAGE = "This user is already registered!";

    private final UserRepository userRepository;
    private final UserValidator userValidator;

    public UserServiceImpl(UserRepository usersRepository, UserValidator userValidator) {
        this.userRepository = usersRepository;
        this.userValidator = userValidator;
    }

    public UserServiceImpl(UserRepository usersRepository) {
        this(usersRepository, new UserValidatorImpl());
    }

    @Override
    public void register(String email, String password) throws BadEmailFormatException, BadPasswordFormatException,
            UserAlreadyRegisteredException {
        userValidator.validateRegister(email, password);

        if (userRepository.findUserByEmail(email).isPresent()) {
            throw new UserAlreadyRegisteredException(USER_ALREADY_REGISTERED_MESSAGE);
        }

        userRepository.addUser(new User(email, PasswordHasher.hashPassword(password)));
    }

    @Override
    public User authenticate(String email, String password) throws BadEmailFormatException,
            BadPasswordFormatException, UserDoesNotExistsException, WrongPasswordException {
        userValidator.validateAuthenticate(email, password);

        Optional<User> user = userRepository.findUserByEmail(email);
        if (user.isEmpty()) {
            throw new UserDoesNotExistsException(NO_SUCH_USER_MESSAGE);
        }

        if (!PasswordHasher.matches(password, user.get().password())) {
            throw new WrongPasswordException(WRONG_PASSWORD_MESSAGE);
        }

        return user.get();
    }
}
