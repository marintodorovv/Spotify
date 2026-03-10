package bg.sofia.uni.fmi.mjt.spotify.server.context;

import java.nio.ByteBuffer;

public final class ConnectionContext {
    private final ByteBuffer buffer;
    private final UserContext context;

    public ConnectionContext(int bufferSize) {
        buffer = ByteBuffer.allocate(bufferSize);
        context = new UserContext();
    }

    public ByteBuffer getBuffer() {
        return buffer;
    }

    public UserContext getContext() {
        return context;
    }
}
