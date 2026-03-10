package bg.sofia.uni.fmi.mjt.spotify.server.command.helpers;

import bg.sofia.uni.fmi.mjt.spotify.common.CommandType;
import bg.sofia.uni.fmi.mjt.spotify.server.command.Command;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

class CommandRegistryTest {
    private CommandRegistry commandRegistry;
    private Command mockCommand;

    @BeforeEach
    void setUp() {
        commandRegistry = new CommandRegistry();
        mockCommand = mock(Command.class);
    }

    @Test
    void testRegisterAndGet() {
        commandRegistry.register(CommandType.REGISTER, mockCommand);

        assertEquals(mockCommand, commandRegistry.getCommand("register"));
    }

    @Test
    void testGetCommandNotFound() {
        assertNull(commandRegistry.getCommand(""));
    }

    @Test
    void testGetCommandAfterRegister() {
        commandRegistry.register(CommandType.LOGIN, mockCommand);

        assertEquals(mockCommand, commandRegistry.getCommand("login"));
    }
}
