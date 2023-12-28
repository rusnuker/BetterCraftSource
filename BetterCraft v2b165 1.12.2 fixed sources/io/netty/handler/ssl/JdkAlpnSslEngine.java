// 
// Decompiled by Procyon v0.6.0
// 

package io.netty.handler.ssl;

import javax.net.ssl.SSLException;
import javax.net.ssl.SSLHandshakeException;
import java.util.List;
import org.eclipse.jetty.alpn.ALPN;
import java.util.Set;
import java.util.Collection;
import java.util.LinkedHashSet;
import io.netty.util.internal.ObjectUtil;
import javax.net.ssl.SSLEngine;
import io.netty.util.internal.PlatformDependent;

final class JdkAlpnSslEngine extends JdkSslEngine
{
    private static boolean available;
    
    static boolean isAvailable() {
        updateAvailability();
        return JdkAlpnSslEngine.available;
    }
    
    private static void updateAvailability() {
        if (JdkAlpnSslEngine.available || PlatformDependent.javaVersion() > 8) {
            return;
        }
        try {
            Class.forName("sun.security.ssl.ALPNExtension", true, null);
            JdkAlpnSslEngine.available = true;
        }
        catch (final Exception ex) {}
    }
    
    JdkAlpnSslEngine(final SSLEngine engine, final JdkApplicationProtocolNegotiator applicationNegotiator, final boolean server) {
        super(engine);
        ObjectUtil.checkNotNull(applicationNegotiator, "applicationNegotiator");
        if (server) {
            final JdkApplicationProtocolNegotiator.ProtocolSelector protocolSelector = ObjectUtil.checkNotNull(applicationNegotiator.protocolSelectorFactory().newSelector(this, new LinkedHashSet<String>(applicationNegotiator.protocols())), "protocolSelector");
            ALPN.put(engine, (ALPN.Provider)new ALPN.ServerProvider() {
                public String select(final List<String> protocols) throws SSLException {
                    try {
                        return protocolSelector.select(protocols);
                    }
                    catch (final SSLHandshakeException e) {
                        throw e;
                    }
                    catch (final Throwable t) {
                        final SSLHandshakeException e2 = new SSLHandshakeException(t.getMessage());
                        e2.initCause(t);
                        throw e2;
                    }
                }
                
                public void unsupported() {
                    protocolSelector.unsupported();
                }
            });
        }
        else {
            final JdkApplicationProtocolNegotiator.ProtocolSelectionListener protocolListener = ObjectUtil.checkNotNull(applicationNegotiator.protocolListenerFactory().newListener(this, applicationNegotiator.protocols()), "protocolListener");
            ALPN.put(engine, (ALPN.Provider)new ALPN.ClientProvider() {
                public List<String> protocols() {
                    return applicationNegotiator.protocols();
                }
                
                public void selected(final String protocol) throws SSLException {
                    try {
                        protocolListener.selected(protocol);
                    }
                    catch (final SSLHandshakeException e) {
                        throw e;
                    }
                    catch (final Throwable t) {
                        final SSLHandshakeException e2 = new SSLHandshakeException(t.getMessage());
                        e2.initCause(t);
                        throw e2;
                    }
                }
                
                public void unsupported() {
                    protocolListener.unsupported();
                }
            });
        }
    }
    
    @Override
    public void closeInbound() throws SSLException {
        ALPN.remove(this.getWrappedEngine());
        super.closeInbound();
    }
    
    @Override
    public void closeOutbound() {
        ALPN.remove(this.getWrappedEngine());
        super.closeOutbound();
    }
}
