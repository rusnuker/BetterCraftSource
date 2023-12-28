// 
// Decompiled by Procyon v0.6.0
// 

package io.netty.handler.ssl;

import javax.net.ssl.SSLEngine;
import java.util.List;

public final class JdkAlpnApplicationProtocolNegotiator extends JdkBaseApplicationProtocolNegotiator
{
    private static final JdkApplicationProtocolNegotiator.SslEngineWrapperFactory ALPN_WRAPPER;
    
    public JdkAlpnApplicationProtocolNegotiator(final Iterable<String> protocols) {
        this(false, protocols);
    }
    
    public JdkAlpnApplicationProtocolNegotiator(final String... protocols) {
        this(false, protocols);
    }
    
    public JdkAlpnApplicationProtocolNegotiator(final boolean failIfNoCommonProtocols, final Iterable<String> protocols) {
        this(failIfNoCommonProtocols, failIfNoCommonProtocols, protocols);
    }
    
    public JdkAlpnApplicationProtocolNegotiator(final boolean failIfNoCommonProtocols, final String... protocols) {
        this(failIfNoCommonProtocols, failIfNoCommonProtocols, protocols);
    }
    
    public JdkAlpnApplicationProtocolNegotiator(final boolean clientFailIfNoCommonProtocols, final boolean serverFailIfNoCommonProtocols, final Iterable<String> protocols) {
        this(serverFailIfNoCommonProtocols ? JdkAlpnApplicationProtocolNegotiator.FAIL_SELECTOR_FACTORY : JdkAlpnApplicationProtocolNegotiator.NO_FAIL_SELECTOR_FACTORY, clientFailIfNoCommonProtocols ? JdkAlpnApplicationProtocolNegotiator.FAIL_SELECTION_LISTENER_FACTORY : JdkAlpnApplicationProtocolNegotiator.NO_FAIL_SELECTION_LISTENER_FACTORY, protocols);
    }
    
    public JdkAlpnApplicationProtocolNegotiator(final boolean clientFailIfNoCommonProtocols, final boolean serverFailIfNoCommonProtocols, final String... protocols) {
        this(serverFailIfNoCommonProtocols ? JdkAlpnApplicationProtocolNegotiator.FAIL_SELECTOR_FACTORY : JdkAlpnApplicationProtocolNegotiator.NO_FAIL_SELECTOR_FACTORY, clientFailIfNoCommonProtocols ? JdkAlpnApplicationProtocolNegotiator.FAIL_SELECTION_LISTENER_FACTORY : JdkAlpnApplicationProtocolNegotiator.NO_FAIL_SELECTION_LISTENER_FACTORY, protocols);
    }
    
    public JdkAlpnApplicationProtocolNegotiator(final JdkApplicationProtocolNegotiator.ProtocolSelectorFactory selectorFactory, final JdkApplicationProtocolNegotiator.ProtocolSelectionListenerFactory listenerFactory, final Iterable<String> protocols) {
        super(JdkAlpnApplicationProtocolNegotiator.ALPN_WRAPPER, selectorFactory, listenerFactory, protocols);
    }
    
    public JdkAlpnApplicationProtocolNegotiator(final JdkApplicationProtocolNegotiator.ProtocolSelectorFactory selectorFactory, final JdkApplicationProtocolNegotiator.ProtocolSelectionListenerFactory listenerFactory, final String... protocols) {
        super(JdkAlpnApplicationProtocolNegotiator.ALPN_WRAPPER, selectorFactory, listenerFactory, protocols);
    }
    
    static {
        ALPN_WRAPPER = new JdkApplicationProtocolNegotiator.SslEngineWrapperFactory() {
            {
                if (!JdkAlpnSslEngine.isAvailable()) {
                    throw new RuntimeException("ALPN unsupported. Is your classpatch configured correctly? See http://www.eclipse.org/jetty/documentation/current/alpn-chapter.html#alpn-starting");
                }
            }
            
            @Override
            public SSLEngine wrapSslEngine(final SSLEngine engine, final JdkApplicationProtocolNegotiator applicationNegotiator, final boolean isServer) {
                return new JdkAlpnSslEngine(engine, applicationNegotiator, isServer);
            }
        };
    }
}
