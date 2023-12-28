// 
// Decompiled by Procyon v0.6.0
// 

package io.netty.handler.ssl;

import javax.net.ssl.SSLEngine;
import java.util.Collections;
import java.util.List;

final class JdkDefaultApplicationProtocolNegotiator implements JdkApplicationProtocolNegotiator
{
    public static final JdkDefaultApplicationProtocolNegotiator INSTANCE;
    private static final SslEngineWrapperFactory DEFAULT_SSL_ENGINE_WRAPPER_FACTORY;
    
    private JdkDefaultApplicationProtocolNegotiator() {
    }
    
    @Override
    public SslEngineWrapperFactory wrapperFactory() {
        return JdkDefaultApplicationProtocolNegotiator.DEFAULT_SSL_ENGINE_WRAPPER_FACTORY;
    }
    
    @Override
    public ProtocolSelectorFactory protocolSelectorFactory() {
        throw new UnsupportedOperationException("Application protocol negotiation unsupported");
    }
    
    @Override
    public ProtocolSelectionListenerFactory protocolListenerFactory() {
        throw new UnsupportedOperationException("Application protocol negotiation unsupported");
    }
    
    @Override
    public List<String> protocols() {
        return Collections.emptyList();
    }
    
    static {
        INSTANCE = new JdkDefaultApplicationProtocolNegotiator();
        DEFAULT_SSL_ENGINE_WRAPPER_FACTORY = new SslEngineWrapperFactory() {
            @Override
            public SSLEngine wrapSslEngine(final SSLEngine engine, final JdkApplicationProtocolNegotiator applicationNegotiator, final boolean isServer) {
                return engine;
            }
        };
    }
}
