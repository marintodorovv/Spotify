package bg.sofia.uni.fmi.mjt.spotify.server.reponse;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CommandResponseTest {
    @Test
    void testOkResponseSuccess() {
        CommandResponse response = CommandResponse.ok("Success");

        assertTrue(response.success());
    }

    @Test
    void testErrorResponseNotSuccess() {
        CommandResponse response = CommandResponse.error("Error occurred");

        assertFalse(response.success());
    }

    @Test
    void testDataResponseHasCorrectSize() {
        List<String> data = List.of("item1", "item2", "item3");
        CommandResponse response = CommandResponse.data(data);

        assertEquals(3, response.response().size());
    }

    @Test
    void testToStringContainsSuccess() {
        CommandResponse response = CommandResponse.ok("");

        assertTrue(response.toString().contains("\"success\":true"));
    }
}
