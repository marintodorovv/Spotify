package bg.sofia.uni.fmi.mjt.spotify.common.exception.song;

public class NoSongStreamingException extends SongException {
    public NoSongStreamingException(String message) {
        super(message);
    }

    public NoSongStreamingException(String message, Throwable throwable) {
        super(message, throwable);
    }
}
