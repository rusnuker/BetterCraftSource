// 
// Decompiled by Procyon v0.6.0
// 

package io.netty.handler.ssl;

import java.util.Set;
import java.util.List;
import javax.net.ssl.SSLEngine;

public interface JdkApplicationProtocolNegotiator extends ApplicationProtocolNegotiator
{
    SslEngineWrapperFactory wrapperFactory();
    
    ProtocolSelectorFactory protocolSelectorFactory();
    
    ProtocolSelectionListenerFactory protocolListenerFactory();
    
    public interface ProtocolSelectionListenerFactory
    {
        ProtocolSelectionListener newListener(final SSLEngine p0, final List<String> p1);
    }
    
    public interface ProtocolSelectionListener
    {
        void unsupported();
        
        void selected(final String p0) throws Exception;
    }
    
    public interface ProtocolSelectorFactory
    {
        ProtocolSelector newSelector(final SSLEngine p0, final Set<String> p1);
    }
    
    public interface ProtocolSelector
    {
        void unsupported();
        
        String select(final List<String> p0) throws Exception;
    }
    
    public interface SslEngineWrapperFactory
    {
        SSLEngine wrapSslEngine(final SSLEngine p0, final JdkApplicationProtocolNegotiator p1, final boolean p2);
    }
}
