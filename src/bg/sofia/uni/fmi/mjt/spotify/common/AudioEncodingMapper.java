package bg.sofia.uni.fmi.mjt.spotify.common;

import bg.sofia.uni.fmi.mjt.spotify.common.exception.song.BadEncodingStringException;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioFormat.Encoding;

public final class AudioEncodingMapper {
    private static final String BLANK_ENCODING_STRING_MESSAGE = "Encoding must not be blank!";
    private static final String NULL_ENCODING_STRING_MESSAGE = "Encoding must not be null!";

    private AudioEncodingMapper() {
    }

    public static AudioFormat.Encoding fromString(String encodingStr) throws BadEncodingStringException {
        if (encodingStr == null) {
            throw new BadEncodingStringException(NULL_ENCODING_STRING_MESSAGE);
        }

        if (encodingStr.isBlank()) {
            throw new BadEncodingStringException(BLANK_ENCODING_STRING_MESSAGE);
        }

        return switch (encodingStr.toUpperCase()) {
            case "ALAW" -> Encoding.ALAW;
            case "PCM_FLOAT" -> Encoding.PCM_FLOAT;
            case "PCM_SIGNED" -> Encoding.PCM_SIGNED;
            case "PCM_UNSIGNED" -> Encoding.PCM_UNSIGNED;
            case "ULAW" -> Encoding.ULAW;
            default -> new Encoding(encodingStr);
        };
    }
}
