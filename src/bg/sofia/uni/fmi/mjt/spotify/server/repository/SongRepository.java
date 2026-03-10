package bg.sofia.uni.fmi.mjt.spotify.server.repository;

import bg.sofia.uni.fmi.mjt.spotify.server.model.Song;

import java.util.List;
import java.util.Optional;

public interface SongRepository extends Repository {
    Optional<Song> find(String id);

    List<Song> search(List<String> keywords);
}
