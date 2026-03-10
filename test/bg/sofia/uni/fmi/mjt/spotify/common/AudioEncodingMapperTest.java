package bg.sofia.uni.fmi.mjt.spotify.common;

import bg.sofia.uni.fmi.mjt.spotify.common.exception.song.BadEncodingStringException;
import org.junit.jupiter.api.Test;

import javax.sound.sampled.AudioFormat;

import static org.junit.jupiter.api.Assertions.*;

class AudioEncodingMapperTest {
    @Test
    void testFromStringAlaw() throws BadEncodingStringException {
        assertEquals(AudioFormat.Encoding.ALAW, AudioEncodingMapper.fromString("ALAW"));
    }

    @Test
    void testFromStringPcmFloat() throws BadEncodingStringException {
        assertEquals(AudioFormat.Encoding.PCM_FLOAT, AudioEncodingMapper.fromString("PCM_FLOAT"));
    }

    @Test
    void testFromStringPcmSigned() throws BadEncodingStringException {
        assertEquals(AudioFormat.Encoding.PCM_SIGNED, AudioEncodingMapper.fromString("PCM_SIGNED"));
    }

    @Test
    void testFromStringPcmUnsigned() throws BadEncodingStringException {
        assertEquals(AudioFormat.Encoding.PCM_UNSIGNED, AudioEncodingMapper.fromString("PCM_UNSIGNED"));
    }

    @Test
    void testFromStringUlaw() throws BadEncodingStringException {
        assertEquals(AudioFormat.Encoding.ULAW, AudioEncodingMapper.fromString("ULAW"));
    }

    @Test
    void testFromStringLowercase() throws BadEncodingStringException {
        assertEquals(AudioFormat.Encoding.ALAW, AudioEncodingMapper.fromString("alaw"));
    }

    @Test
    void testFromStringCustomEncoding() throws BadEncodingStringException {
        assertEquals("CUSTOM", AudioEncodingMapper.fromString("CUSTOM").toString());
    }

    @Test
    void testFromStringNull() {
        assertThrows(BadEncodingStringException.class,
                () -> AudioEncodingMapper.fromString(null));
    }

    @Test
    void testFromStringBlank() {
        assertThrows(BadEncodingStringException.class,
                () -> AudioEncodingMapper.fromString(""));
    }
}
