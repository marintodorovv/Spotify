package bg.sofia.uni.fmi.mjt.spotify.server.service;

import bg.sofia.uni.fmi.mjt.spotify.common.exception.SpotifyException;
import bg.sofia.uni.fmi.mjt.spotify.common.exception.authentication.UserAlreadyRegisteredException;
import bg.sofia.uni.fmi.mjt.spotify.common.exception.authentication.UserDoesNotExistsException;
import bg.sofia.uni.fmi.mjt.spotify.common.exception.validation.BadEmailFormatException;
import bg.sofia.uni.fmi.mjt.spotify.server.model.User;
import bg.sofia.uni.fmi.mjt.spotify.server.repository.UserRepository;
import bg.sofia.uni.fmi.mjt.spotify.server.validation.UserValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceImplTest {
    private UserRepository userRepository;
    private UserValidator userValidator;
    private UserServiceImpl userService;

    @BeforeEach
    void setUp() {
        userRepository = mock(UserRepository.class);
        userValidator = mock(UserValidator.class);

        userService = new UserServiceImpl(userRepository, userValidator);
    }

    @Test
    void testRegisterSuccess() throws SpotifyException {
        when(userRepository.findUserByEmail("a@b.c")).thenReturn(Optional.empty());

        assertDoesNotThrow(() -> userService.register("a@b.c", "pass12"));
    }

    @Test
    void testRegisterCallsValidator() throws SpotifyException {
        when(userRepository.findUserByEmail("a@b.c")).thenReturn(Optional.empty());

        userService.register("a@b.c", "pass12");

        verify(userValidator).validateRegister("a@b.c", "pass12");
    }

    @Test
    void testRegisterUserExists() {
        when(userRepository.findUserByEmail("a@b.c")).thenReturn(Optional.of(new User("a@b.c", "hash")));

        assertThrows(UserAlreadyRegisteredException.class, () -> userService.register("a@b.c", "pass12"));
    }

    @Test
    void testRegisterInvalidEmail() throws SpotifyException {
        doThrow(new BadEmailFormatException("invalid")).when(userValidator).validateRegister("invalid", "pass12");

        assertThrows(BadEmailFormatException.class, () -> userService.register("invalid", "pass12"));
    }

    @Test
    void testAuthenticateUserNotFound() {
        when(userRepository.findUserByEmail("a@b.c")).thenReturn(Optional.empty());

        assertThrows(UserDoesNotExistsException.class, () -> userService.authenticate("a@b.c", "pass12"));
    }

    @Test
    void testAuthenticateInvalidEmail() throws SpotifyException {
        doThrow(new BadEmailFormatException("invalid")).when(userValidator).validateAuthenticate("invalid", "pass12");

        assertThrows(BadEmailFormatException.class, () -> userService.authenticate("invalid", "pass12"));
    }
}
