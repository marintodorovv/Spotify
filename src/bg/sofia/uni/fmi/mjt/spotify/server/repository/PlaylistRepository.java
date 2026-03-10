package bg.sofia.uni.fmi.mjt.spotify.server.repository;

import bg.sofia.uni.fmi.mjt.spotify.server.model.Playlist;

import java.util.List;

public interface PlaylistRepository extends Repository {
    void createPlaylist(String ownerEmail, String playlistName);

    void addSongToPlaylist(String ownerEmail, String playlistName, String songName);

    List<String> getSongsFromPlaylist(String ownerEmail, String playlistName);

    List<Playlist> getAllPlaylistsForUser(String ownerEmail);

    boolean playlistExists(String ownerEmail, String playlistName);

    boolean playlistContainsSong(String ownerEmail, String playlistName, String songName);
}
