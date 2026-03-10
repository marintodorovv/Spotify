package bg.sofia.uni.fmi.mjt.spotify.server.service;

import bg.sofia.uni.fmi.mjt.spotify.common.exception.authentication.UserAlreadyRegisteredException;
import bg.sofia.uni.fmi.mjt.spotify.common.exception.authentication.UserDoesNotExistsException;
import bg.sofia.uni.fmi.mjt.spotify.common.exception.authentication.WrongPasswordException;
import bg.sofia.uni.fmi.mjt.spotify.common.exception.validation.BadEmailFormatException;
import bg.sofia.uni.fmi.mjt.spotify.common.exception.validation.BadPasswordFormatException;
import bg.sofia.uni.fmi.mjt.spotify.server.model.User;

public interface UserService extends Service {
    void register(String email, String password)
            throws BadEmailFormatException, BadPasswordFormatException, UserAlreadyRegisteredException;

    User authenticate(String email, String password)
            throws BadEmailFormatException, BadPasswordFormatException,
            UserDoesNotExistsException, WrongPasswordException;
}
