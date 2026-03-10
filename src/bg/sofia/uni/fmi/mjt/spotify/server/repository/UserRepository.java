package bg.sofia.uni.fmi.mjt.spotify.server.repository;

import bg.sofia.uni.fmi.mjt.spotify.server.model.User;

import java.util.Optional;

public interface UserRepository extends Repository {
    void addUser(User user);

    Optional<User> findUserByEmail(String email);
}
