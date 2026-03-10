package bg.sofia.uni.fmi.mjt.spotify.client.model;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CommandResponseModelTest {
    @Test
    void testGetFormattedResponseSingleLine() {
        CommandResponseModel model = new CommandResponseModel(true, List.of("Message"));

        assertTrue(model.getFormattedResponse().contains("Message"));
    }

    @Test
    void testGetFormattedResponseMultipleLines() {
        CommandResponseModel model = new CommandResponseModel(true, List.of("Line1", "Line2"));

        assertTrue(model.getFormattedResponse().contains("Line1"));
    }

    @Test
    void testGetFormattedResponseEmpty() {
        CommandResponseModel model = new CommandResponseModel(true, List.of());

        assertEquals("", model.getFormattedResponse());
    }

    @Test
    void testGetFormattedResponseNull() {
        CommandResponseModel model = new CommandResponseModel(true, null);

        assertEquals("", model.getFormattedResponse());
    }

    @Test
    void testToString() {
        CommandResponseModel model = new CommandResponseModel(true, List.of("Test"));

        assertTrue(model.toString().contains("Test"));
    }
}
