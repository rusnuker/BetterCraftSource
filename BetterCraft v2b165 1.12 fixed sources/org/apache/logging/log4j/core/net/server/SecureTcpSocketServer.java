// 
// Decompiled by Procyon v0.6.0
// 

package org.apache.logging.log4j.core.net.server;

import java.io.IOException;
import org.apache.logging.log4j.core.net.ssl.SslConfiguration;
import java.io.InputStream;

public class SecureTcpSocketServer<T extends InputStream> extends TcpSocketServer<T>
{
    public SecureTcpSocketServer(final int port, final LogEventBridge<T> logEventInput, final SslConfiguration sslConfig) throws IOException {
        super(port, logEventInput, sslConfig.getSslServerSocketFactory().createServerSocket(port));
    }
}
