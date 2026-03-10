package bg.sofia.uni.fmi.mjt.spotify.server.repository;

import bg.sofia.uni.fmi.mjt.spotify.server.model.Playlist;

import java.util.List;

public record PlaylistStorage(List<Playlist> playlistList) {
}
