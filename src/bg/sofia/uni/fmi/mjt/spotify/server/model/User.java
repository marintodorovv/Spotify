package bg.sofia.uni.fmi.mjt.spotify.server.model;

import java.io.Serializable;
import java.util.Objects;

public record User(String email,
                   String password) implements Serializable {
    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        User user = (User)o;
        return Objects.equals(email, user.email);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(email);
    }
}
