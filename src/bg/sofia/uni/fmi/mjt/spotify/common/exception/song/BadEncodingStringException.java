package bg.sofia.uni.fmi.mjt.spotify.common.exception.song;

public class BadEncodingStringException extends SongException {
    public BadEncodingStringException(String message) {
        super(message);
    }

    public BadEncodingStringException(String message, Throwable throwable) {
        super(message, throwable);
    }
}
