package bg.sofia.uni.fmi.mjt.spotify.common;

import bg.sofia.uni.fmi.mjt.spotify.common.exception.song.BadEncodingStringException;
import org.junit.jupiter.api.Test;

import javax.sound.sampled.AudioFormat;

import static org.junit.jupiter.api.Assertions.*;

class AudioFormatDTOTest {
    @Test
    void testToDTO() {
        AudioFormat format = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, 44100, 16, 2, 4, 44100, true);

        AudioFormatDTO dto = AudioFormatDTO.toDTO(format);

        assertEquals("PCM_SIGNED", dto.encoding());
    }

    @Test
    void testToAudioFormat() throws BadEncodingStringException {
        AudioFormatDTO dto = new AudioFormatDTO(true, 2, "PCM_SIGNED", 44100, 4, 44100, 16);

        AudioFormat format = dto.toAudioFormat();

        assertEquals(AudioFormat.Encoding.PCM_SIGNED, format.getEncoding());
    }

    @Test
    void testToAudioFormatInvalidEncoding() {
        AudioFormatDTO dto = new AudioFormatDTO(true, 2, null, 44100, 4, 44100, 16);

        assertThrows(BadEncodingStringException.class, dto::toAudioFormat);
    }
}
