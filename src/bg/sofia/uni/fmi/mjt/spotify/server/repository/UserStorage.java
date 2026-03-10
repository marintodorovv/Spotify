package bg.sofia.uni.fmi.mjt.spotify.server.repository;

import bg.sofia.uni.fmi.mjt.spotify.server.model.User;

import java.util.List;

public record UserStorage(List<User> userList) {
}
