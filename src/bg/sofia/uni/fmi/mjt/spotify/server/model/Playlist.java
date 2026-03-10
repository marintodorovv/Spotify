package bg.sofia.uni.fmi.mjt.spotify.server.model;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

public record Playlist(String name,
                       String ownerEmail,
                       List<String> songIdentifiersList) implements Serializable {
    public Playlist {
        songIdentifiersList = List.copyOf(songIdentifiersList);
    }

    public String playlistIdentifier() {
        return ownerEmail + "%" + name;
    }

    public static String generatePlaylistIdentifier(String ownerEmail, String playlistName) {
        return ownerEmail + "%" + playlistName;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Playlist playlist = (Playlist)o;
        return Objects.equals(playlistIdentifier(), playlist.playlistIdentifier());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(playlistIdentifier());
    }
}
