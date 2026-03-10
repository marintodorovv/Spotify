package bg.sofia.uni.fmi.mjt.spotify.common.exception.song;

public class AlreadyStreamingException extends SongException {
    public AlreadyStreamingException(String message) {
        super(message);
    }

    public AlreadyStreamingException(String message, Throwable throwable) {
        super(message, throwable);
    }
}
