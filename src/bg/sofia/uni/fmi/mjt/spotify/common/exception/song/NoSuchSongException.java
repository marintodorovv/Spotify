package bg.sofia.uni.fmi.mjt.spotify.common.exception.song;

public class NoSuchSongException extends SongException {
    public NoSuchSongException(String message) {
        super(message);
    }

    public NoSuchSongException(String message, Throwable throwable) {
        super(message, throwable);
    }
}
