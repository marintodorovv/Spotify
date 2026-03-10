package bg.sofia.uni.fmi.mjt.spotify.server.context;

import bg.sofia.uni.fmi.mjt.spotify.server.model.User;

public final class UserContext {
    private User currentUser;

    public void login(User user) {
        this.currentUser = user;
    }

    public void logout() {
        currentUser = null;
    }

    public User getCurrentUser() {
        return currentUser;
    }

    public boolean isLogged() {
        return currentUser != null;
    }
}
