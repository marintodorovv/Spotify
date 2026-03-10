package bg.sofia.uni.fmi.mjt.spotify.common;

import bg.sofia.uni.fmi.mjt.spotify.common.exception.song.BadEncodingStringException;

import javax.sound.sampled.AudioFormat;
import java.io.Serializable;

public record AudioFormatDTO(boolean bigEndian,
                             int channels,
                             String encoding,
                             float frameRate,
                             int frameSize,
                             float sampleRate,
                             int sampleSizeInBits) implements Serializable {
    public static AudioFormatDTO toDTO(AudioFormat format) {
        return new AudioFormatDTO(
                format.isBigEndian(),
                format.getChannels(),
                format.getEncoding().toString(),
                format.getFrameRate(),
                format.getFrameSize(),
                format.getSampleRate(),
                format.getSampleSizeInBits()
        );
    }

    public AudioFormat toAudioFormat() throws BadEncodingStringException {
        return new AudioFormat(
                AudioEncodingMapper.fromString(encoding),
                sampleRate,
                sampleSizeInBits,
                channels,
                frameSize,
                frameRate,
                bigEndian
        );
    }
}
