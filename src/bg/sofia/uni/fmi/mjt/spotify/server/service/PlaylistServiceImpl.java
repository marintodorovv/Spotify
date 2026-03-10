package bg.sofia.uni.fmi.mjt.spotify.server.service;

import bg.sofia.uni.fmi.mjt.spotify.common.exception.playlist.NoSuchPlaylistException;
import bg.sofia.uni.fmi.mjt.spotify.common.exception.playlist.PlaylistAlreadyExistsException;
import bg.sofia.uni.fmi.mjt.spotify.common.exception.playlist.SongAlreadyInPlaylistException;
import bg.sofia.uni.fmi.mjt.spotify.common.exception.song.NoSuchSongException;
import bg.sofia.uni.fmi.mjt.spotify.common.exception.validation.BadEmailFormatException;
import bg.sofia.uni.fmi.mjt.spotify.common.exception.validation.BadPlaylistNameFormatException;
import bg.sofia.uni.fmi.mjt.spotify.common.exception.validation.BadSongTitleException;
import bg.sofia.uni.fmi.mjt.spotify.server.model.Playlist;
import bg.sofia.uni.fmi.mjt.spotify.server.repository.PlaylistRepository;
import bg.sofia.uni.fmi.mjt.spotify.server.repository.SongRepository;
import bg.sofia.uni.fmi.mjt.spotify.server.validation.PlaylistValidator;
import bg.sofia.uni.fmi.mjt.spotify.server.validation.PlaylistValidatorImpl;

import java.util.List;

public final class PlaylistServiceImpl implements PlaylistService {
    private static final String NO_SUCH_PLAYLIST_MESSAGE = "No such playlist exists!";
    private static final String NO_SUCH_SONG_MESSAGE = "No such song exists!";
    private static final String PLAYLIST_ALREADY_EXISTS_MESSAGE = "This playlist already exists!";
    private static final String PLAYLIST_ALREADY_CONTAINS_THIS_SONG_MESSAGE = "This playlist already " +
            "contains this song!";

    private final PlaylistRepository playlistRepository;
    private final SongRepository songRepository;
    private final PlaylistValidator playlistValidator;

    public PlaylistServiceImpl(PlaylistRepository playlistRepository, SongRepository songRepository,
                               PlaylistValidator playlistValidator) {
        this.playlistRepository = playlistRepository;
        this.songRepository = songRepository;
        this.playlistValidator = playlistValidator;
    }

    public PlaylistServiceImpl(PlaylistRepository playlistRepository, SongRepository songRepository) {
        this(playlistRepository, songRepository, new PlaylistValidatorImpl());
    }

    @Override
    public void createPlaylist(String playlistName, String ownerEmail) throws BadPlaylistNameFormatException,
            BadEmailFormatException, PlaylistAlreadyExistsException {
        playlistValidator.validateCreate(ownerEmail, playlistName);

        if (playlistRepository.playlistExists(ownerEmail, playlistName)) {
            throw new PlaylistAlreadyExistsException(PLAYLIST_ALREADY_EXISTS_MESSAGE);
        }

        playlistRepository.createPlaylist(ownerEmail, playlistName);
    }

    @Override
    public void addSongToPlaylist(String playlistName, String songTitle, String ownerEmail)
            throws BadPlaylistNameFormatException, BadEmailFormatException, BadSongTitleException,
            NoSuchSongException, NoSuchPlaylistException, SongAlreadyInPlaylistException {
        playlistValidator.validateAddSong(ownerEmail, playlistName, songTitle);

        if (songRepository.find(songTitle).isEmpty()) {
            throw new NoSuchSongException(NO_SUCH_SONG_MESSAGE);
        }

        if (!playlistRepository.playlistExists(ownerEmail, playlistName)) {
            throw new NoSuchPlaylistException(NO_SUCH_PLAYLIST_MESSAGE);
        }

        if (playlistRepository.playlistContainsSong(ownerEmail, playlistName, songTitle)) {
            throw new SongAlreadyInPlaylistException(PLAYLIST_ALREADY_CONTAINS_THIS_SONG_MESSAGE);
        }

        playlistRepository.addSongToPlaylist(ownerEmail, playlistName, songTitle);
    }

    @Override
    public List<String> getAllSongsFromPlaylist(String playlistName, String ownerEmail)
            throws BadPlaylistNameFormatException, BadEmailFormatException, NoSuchPlaylistException {
        playlistValidator.validateGetAllSongs(ownerEmail, playlistName);

        if (!playlistRepository.playlistExists(ownerEmail, playlistName)) {
            throw new NoSuchPlaylistException(NO_SUCH_PLAYLIST_MESSAGE);
        }

        return playlistRepository.getSongsFromPlaylist(ownerEmail, playlistName);
    }

    @Override
    public List<Playlist> getAllPlaylistsForUser(String ownerEmail) throws BadEmailFormatException {
        playlistValidator.validateGetAllPlaylistsForUser(ownerEmail);

        return playlistRepository.getAllPlaylistsForUser(ownerEmail);
    }
}
