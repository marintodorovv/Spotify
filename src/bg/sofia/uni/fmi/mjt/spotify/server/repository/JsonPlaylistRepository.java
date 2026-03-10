package bg.sofia.uni.fmi.mjt.spotify.server.repository;

import bg.sofia.uni.fmi.mjt.spotify.common.exception.repository.RepositoryException;
import bg.sofia.uni.fmi.mjt.spotify.server.model.Playlist;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public final class JsonPlaylistRepository extends BaseRepository implements PlaylistRepository {
    private static final String UNEXPECTED_ERROR_WHILE_SYNCING_PLAYLISTS_MESSAGE = "Encountered an error while " +
            "writing playlists to disk!";
    private static final String UNEXPECTED_ERROR_WHILE_LOADING_PLAYLISTS_MESSAGE = "Encountered an error while " +
            "loading playlists!";

    private final Path filePath;
    private final Gson gson;
    private final Map<String, Playlist> playlistMap;

    public JsonPlaylistRepository(String filePath, Gson gson) {
        this.filePath = Path.of(filePath);
        this.gson = gson;
        playlistMap = new ConcurrentHashMap<>();

        ensureFileExists(this.filePath);
        loadPlaylistsFromFile();
    }

    public JsonPlaylistRepository(String filePath) {
        this(filePath, new GsonBuilder().create());
    }

    private void loadPlaylistsFromFile() {
        playlistMap.clear();

        try (BufferedReader reader = Files.newBufferedReader(filePath)) {
            PlaylistStorage storage = gson.fromJson(reader, PlaylistStorage.class);

            if (storage != null && storage.playlistList() != null) {
                for (Playlist playlist : storage.playlistList()) {
                    playlistMap.put(playlist.playlistIdentifier(), playlist);
                }
            }
        } catch (IOException e) {
            throw new RepositoryException(UNEXPECTED_ERROR_WHILE_LOADING_PLAYLISTS_MESSAGE, e);
        }
    }

    private synchronized void syncFile() {
        try (BufferedWriter writer = Files.newBufferedWriter(filePath)) {
            PlaylistStorage storage = new PlaylistStorage(new ArrayList<>(playlistMap.values()));
            gson.toJson(storage, writer);
        } catch (IOException e) {
            throw new RepositoryException(UNEXPECTED_ERROR_WHILE_SYNCING_PLAYLISTS_MESSAGE, e);
        }
    }

    @Override
    public synchronized void createPlaylist(String ownerEmail, String playlistName) {
        Playlist playlist = new Playlist(playlistName, ownerEmail, new ArrayList<>());

        playlistMap.put(playlist.playlistIdentifier(), playlist);

        syncFile();
    }

    @Override
    public synchronized void addSongToPlaylist(String ownerEmail, String playlistName, String songName) {
        Playlist playlist = playlistMap.get(Playlist.generatePlaylistIdentifier(ownerEmail, playlistName));

        List<String> updatedSongList = new ArrayList<>(playlist.songIdentifiersList());

        updatedSongList.add(songName);

        playlistMap.put(playlist.playlistIdentifier(), new Playlist(playlistName, ownerEmail, updatedSongList));

        syncFile();
    }

    @Override
    public List<String> getSongsFromPlaylist(String ownerEmail, String playlistName) {
        Playlist playlist = playlistMap.get(Playlist.generatePlaylistIdentifier(ownerEmail, playlistName));

        return List.copyOf(playlist.songIdentifiersList());
    }

    @Override
    public List<Playlist> getAllPlaylistsForUser(String ownerEmail) {
        return playlistMap.values().stream().filter(p -> p.ownerEmail().equals(ownerEmail)).toList();
    }

    @Override
    public boolean playlistExists(String ownerEmail, String playlistName) {
        return playlistMap.containsKey(Playlist.generatePlaylistIdentifier(ownerEmail, playlistName));
    }

    @Override
    public boolean playlistContainsSong(String ownerEmail, String playlistName, String songName) {
        Playlist playlist = playlistMap.get(Playlist.generatePlaylistIdentifier(ownerEmail, playlistName));

        for (String song : playlist.songIdentifiersList()) {
            if (song.equals(songName)) {
                return true;
            }
        }

        return false;
    }
}
