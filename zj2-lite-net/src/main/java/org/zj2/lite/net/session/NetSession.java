package org.zj2.lite.net.session;

import java.io.Closeable;

public interface NetSession extends Closeable {
    String sessionId();

    NetSessionGroup sessionGroup();

    @Override
    void close();

    Object rawConnect();

    void send(Object msg);
}
