// 
// Decompiled by Procyon v0.6.0
// 

package org.apache.logging.log4j.core.net;

import javax.net.ssl.SSLSocket;
import java.net.UnknownHostException;
import org.apache.logging.log4j.core.util.Closer;
import org.apache.logging.log4j.Level;
import java.io.ByteArrayOutputStream;
import org.apache.logging.log4j.Logger;
import java.io.IOException;
import javax.net.ssl.SSLSocketFactory;
import java.net.SocketAddress;
import java.net.InetSocketAddress;
import org.apache.logging.log4j.core.appender.ManagerFactory;
import org.apache.logging.log4j.core.appender.OutputStreamManager;
import org.apache.logging.log4j.util.Strings;
import java.io.Serializable;
import org.apache.logging.log4j.core.Layout;
import java.net.InetAddress;
import java.net.Socket;
import java.io.OutputStream;
import org.apache.logging.log4j.core.net.ssl.SslConfiguration;

public class SslSocketManager extends TcpSocketManager
{
    public static final int DEFAULT_PORT = 6514;
    private static final SslSocketManagerFactory FACTORY;
    private final SslConfiguration sslConfig;
    
    @Deprecated
    public SslSocketManager(final String name, final OutputStream os, final Socket sock, final SslConfiguration sslConfig, final InetAddress inetAddress, final String host, final int port, final int connectTimeoutMillis, final int delay, final boolean immediateFail, final Layout<? extends Serializable> layout, final int bufferSize) {
        super(name, os, sock, inetAddress, host, port, connectTimeoutMillis, delay, immediateFail, layout, bufferSize, null);
        this.sslConfig = sslConfig;
    }
    
    public SslSocketManager(final String name, final OutputStream os, final Socket sock, final SslConfiguration sslConfig, final InetAddress inetAddress, final String host, final int port, final int connectTimeoutMillis, final int delay, final boolean immediateFail, final Layout<? extends Serializable> layout, final int bufferSize, final SocketOptions socketOptions) {
        super(name, os, sock, inetAddress, host, port, connectTimeoutMillis, delay, immediateFail, layout, bufferSize, socketOptions);
        this.sslConfig = sslConfig;
    }
    
    @Deprecated
    public static SslSocketManager getSocketManager(final SslConfiguration sslConfig, final String host, final int port, final int connectTimeoutMillis, final int reconnectDelayMillis, final boolean immediateFail, final Layout<? extends Serializable> layout, final int bufferSize) {
        return getSocketManager(sslConfig, host, port, connectTimeoutMillis, reconnectDelayMillis, immediateFail, layout, bufferSize, null);
    }
    
    public static SslSocketManager getSocketManager(final SslConfiguration sslConfig, final String host, int port, final int connectTimeoutMillis, int reconnectDelayMillis, final boolean immediateFail, final Layout<? extends Serializable> layout, final int bufferSize, final SocketOptions socketOptions) {
        if (Strings.isEmpty(host)) {
            throw new IllegalArgumentException("A host name is required");
        }
        if (port <= 0) {
            port = 6514;
        }
        if (reconnectDelayMillis == 0) {
            reconnectDelayMillis = 30000;
        }
        return (SslSocketManager)OutputStreamManager.getManager("TLS:" + host + ':' + port, new SslFactoryData(sslConfig, host, port, connectTimeoutMillis, reconnectDelayMillis, immediateFail, layout, bufferSize, socketOptions), SslSocketManager.FACTORY);
    }
    
    @Override
    protected Socket createSocket(final String host, final int port) throws IOException {
        final SSLSocketFactory socketFactory = createSslSocketFactory(this.sslConfig);
        final InetSocketAddress address = new InetSocketAddress(host, port);
        final Socket newSocket = socketFactory.createSocket();
        newSocket.connect(address, this.getConnectTimeoutMillis());
        return newSocket;
    }
    
    private static SSLSocketFactory createSslSocketFactory(final SslConfiguration sslConf) {
        SSLSocketFactory socketFactory;
        if (sslConf != null) {
            socketFactory = sslConf.getSslSocketFactory();
        }
        else {
            socketFactory = (SSLSocketFactory)SSLSocketFactory.getDefault();
        }
        return socketFactory;
    }
    
    static {
        FACTORY = new SslSocketManagerFactory();
    }
    
    private static class SslFactoryData
    {
        protected SslConfiguration sslConfiguration;
        private final String host;
        private final int port;
        private final int connectTimeoutMillis;
        private final int delayMillis;
        private final boolean immediateFail;
        private final Layout<? extends Serializable> layout;
        private final int bufferSize;
        private final SocketOptions socketOptions;
        
        public SslFactoryData(final SslConfiguration sslConfiguration, final String host, final int port, final int connectTimeoutMillis, final int delayMillis, final boolean immediateFail, final Layout<? extends Serializable> layout, final int bufferSize, final SocketOptions socketOptions) {
            this.host = host;
            this.port = port;
            this.connectTimeoutMillis = connectTimeoutMillis;
            this.delayMillis = delayMillis;
            this.immediateFail = immediateFail;
            this.layout = layout;
            this.sslConfiguration = sslConfiguration;
            this.bufferSize = bufferSize;
            this.socketOptions = socketOptions;
        }
    }
    
    private static class SslSocketManagerFactory implements ManagerFactory<SslSocketManager, SslFactoryData>
    {
        @Override
        public SslSocketManager createManager(final String name, final SslFactoryData data) {
            InetAddress inetAddress = null;
            OutputStream os = null;
            Socket socket = null;
            try {
                inetAddress = this.resolveAddress(data.host);
                socket = this.createSocket(data);
                os = socket.getOutputStream();
                this.checkDelay(data.delayMillis, os);
            }
            catch (final IOException e) {
                SslSocketManager.LOGGER.error("SslSocketManager ({})", name, e);
                os = new ByteArrayOutputStream();
            }
            catch (final TlsSocketManagerFactoryException e2) {
                SslSocketManager.LOGGER.catching(Level.DEBUG, e2);
                Closer.closeSilently(socket);
                return null;
            }
            return new SslSocketManager(name, os, socket, data.sslConfiguration, inetAddress, data.host, data.port, data.connectTimeoutMillis, data.delayMillis, data.immediateFail, data.layout, data.bufferSize, data.socketOptions);
        }
        
        private InetAddress resolveAddress(final String hostName) throws TlsSocketManagerFactoryException {
            InetAddress address;
            try {
                address = InetAddress.getByName(hostName);
            }
            catch (final UnknownHostException ex) {
                SslSocketManager.LOGGER.error("Could not find address of {}", hostName, ex);
                throw new TlsSocketManagerFactoryException();
            }
            return address;
        }
        
        private void checkDelay(final int delay, final OutputStream os) throws TlsSocketManagerFactoryException {
            if (delay == 0 && os == null) {
                throw new TlsSocketManagerFactoryException();
            }
        }
        
        private Socket createSocket(final SslFactoryData data) throws IOException {
            final SSLSocketFactory socketFactory = createSslSocketFactory(data.sslConfiguration);
            final SSLSocket socket = (SSLSocket)socketFactory.createSocket();
            final SocketOptions socketOptions = data.socketOptions;
            if (socketOptions != null) {
                socketOptions.apply(socket);
            }
            socket.connect(new InetSocketAddress(data.host, data.port), data.connectTimeoutMillis);
            if (socketOptions != null) {
                socketOptions.apply(socket);
            }
            return socket;
        }
        
        private static class TlsSocketManagerFactoryException extends Exception
        {
            private static final long serialVersionUID = 1L;
        }
    }
}
