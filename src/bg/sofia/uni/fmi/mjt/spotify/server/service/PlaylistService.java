package bg.sofia.uni.fmi.mjt.spotify.server.service;

import bg.sofia.uni.fmi.mjt.spotify.common.exception.playlist.NoSuchPlaylistException;
import bg.sofia.uni.fmi.mjt.spotify.common.exception.playlist.PlaylistAlreadyExistsException;
import bg.sofia.uni.fmi.mjt.spotify.common.exception.playlist.SongAlreadyInPlaylistException;
import bg.sofia.uni.fmi.mjt.spotify.common.exception.song.NoSuchSongException;
import bg.sofia.uni.fmi.mjt.spotify.common.exception.validation.BadEmailFormatException;
import bg.sofia.uni.fmi.mjt.spotify.common.exception.validation.BadPlaylistNameFormatException;
import bg.sofia.uni.fmi.mjt.spotify.common.exception.validation.BadSongTitleException;
import bg.sofia.uni.fmi.mjt.spotify.server.model.Playlist;

import java.util.List;

public interface PlaylistService extends Service {
    void createPlaylist(String playlistName, String ownerEmail)
            throws BadPlaylistNameFormatException, BadEmailFormatException, PlaylistAlreadyExistsException;

    void addSongToPlaylist(String playlistName, String songTitle, String ownerEmail)
            throws BadPlaylistNameFormatException, BadEmailFormatException, BadSongTitleException,
            NoSuchSongException, NoSuchPlaylistException, SongAlreadyInPlaylistException;

    List<String> getAllSongsFromPlaylist(String playlistName, String ownerEmail)
            throws BadPlaylistNameFormatException, BadEmailFormatException, NoSuchPlaylistException;

    List<Playlist> getAllPlaylistsForUser(String ownerEmail)
            throws BadEmailFormatException;
}
