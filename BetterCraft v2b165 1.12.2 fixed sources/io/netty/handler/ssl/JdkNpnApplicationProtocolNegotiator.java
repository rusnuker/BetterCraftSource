// 
// Decompiled by Procyon v0.6.0
// 

package io.netty.handler.ssl;

import javax.net.ssl.SSLEngine;
import java.util.List;

public final class JdkNpnApplicationProtocolNegotiator extends JdkBaseApplicationProtocolNegotiator
{
    private static final JdkApplicationProtocolNegotiator.SslEngineWrapperFactory NPN_WRAPPER;
    
    public JdkNpnApplicationProtocolNegotiator(final Iterable<String> protocols) {
        this(false, protocols);
    }
    
    public JdkNpnApplicationProtocolNegotiator(final String... protocols) {
        this(false, protocols);
    }
    
    public JdkNpnApplicationProtocolNegotiator(final boolean failIfNoCommonProtocols, final Iterable<String> protocols) {
        this(failIfNoCommonProtocols, failIfNoCommonProtocols, protocols);
    }
    
    public JdkNpnApplicationProtocolNegotiator(final boolean failIfNoCommonProtocols, final String... protocols) {
        this(failIfNoCommonProtocols, failIfNoCommonProtocols, protocols);
    }
    
    public JdkNpnApplicationProtocolNegotiator(final boolean clientFailIfNoCommonProtocols, final boolean serverFailIfNoCommonProtocols, final Iterable<String> protocols) {
        this(clientFailIfNoCommonProtocols ? JdkNpnApplicationProtocolNegotiator.FAIL_SELECTOR_FACTORY : JdkNpnApplicationProtocolNegotiator.NO_FAIL_SELECTOR_FACTORY, serverFailIfNoCommonProtocols ? JdkNpnApplicationProtocolNegotiator.FAIL_SELECTION_LISTENER_FACTORY : JdkNpnApplicationProtocolNegotiator.NO_FAIL_SELECTION_LISTENER_FACTORY, protocols);
    }
    
    public JdkNpnApplicationProtocolNegotiator(final boolean clientFailIfNoCommonProtocols, final boolean serverFailIfNoCommonProtocols, final String... protocols) {
        this(clientFailIfNoCommonProtocols ? JdkNpnApplicationProtocolNegotiator.FAIL_SELECTOR_FACTORY : JdkNpnApplicationProtocolNegotiator.NO_FAIL_SELECTOR_FACTORY, serverFailIfNoCommonProtocols ? JdkNpnApplicationProtocolNegotiator.FAIL_SELECTION_LISTENER_FACTORY : JdkNpnApplicationProtocolNegotiator.NO_FAIL_SELECTION_LISTENER_FACTORY, protocols);
    }
    
    public JdkNpnApplicationProtocolNegotiator(final JdkApplicationProtocolNegotiator.ProtocolSelectorFactory selectorFactory, final JdkApplicationProtocolNegotiator.ProtocolSelectionListenerFactory listenerFactory, final Iterable<String> protocols) {
        super(JdkNpnApplicationProtocolNegotiator.NPN_WRAPPER, selectorFactory, listenerFactory, protocols);
    }
    
    public JdkNpnApplicationProtocolNegotiator(final JdkApplicationProtocolNegotiator.ProtocolSelectorFactory selectorFactory, final JdkApplicationProtocolNegotiator.ProtocolSelectionListenerFactory listenerFactory, final String... protocols) {
        super(JdkNpnApplicationProtocolNegotiator.NPN_WRAPPER, selectorFactory, listenerFactory, protocols);
    }
    
    static {
        NPN_WRAPPER = new JdkApplicationProtocolNegotiator.SslEngineWrapperFactory() {
            {
                if (!JdkNpnSslEngine.isAvailable()) {
                    throw new RuntimeException("NPN unsupported. Is your classpatch configured correctly? See https://wiki.eclipse.org/Jetty/Feature/NPN");
                }
            }
            
            @Override
            public SSLEngine wrapSslEngine(final SSLEngine engine, final JdkApplicationProtocolNegotiator applicationNegotiator, final boolean isServer) {
                return new JdkNpnSslEngine(engine, applicationNegotiator, isServer);
            }
        };
    }
}
