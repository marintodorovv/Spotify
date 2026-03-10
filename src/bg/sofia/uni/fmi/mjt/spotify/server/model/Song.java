package bg.sofia.uni.fmi.mjt.spotify.server.model;

import java.io.Serializable;

public record Song(String id,
                   String author,
                   String title,
                   String fileName) implements Serializable {
    @Override
    public String toString() {
        return author + " - " + title;
    }
}
