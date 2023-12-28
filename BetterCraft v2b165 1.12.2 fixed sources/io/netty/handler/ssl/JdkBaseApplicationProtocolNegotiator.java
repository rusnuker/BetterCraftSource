// 
// Decompiled by Procyon v0.6.0
// 

package io.netty.handler.ssl;

import javax.net.ssl.SSLHandshakeException;
import java.util.Iterator;
import java.util.Set;
import javax.net.ssl.SSLEngine;
import java.util.Collections;
import io.netty.util.internal.ObjectUtil;
import java.util.List;

class JdkBaseApplicationProtocolNegotiator implements JdkApplicationProtocolNegotiator
{
    private final List<String> protocols;
    private final ProtocolSelectorFactory selectorFactory;
    private final ProtocolSelectionListenerFactory listenerFactory;
    private final SslEngineWrapperFactory wrapperFactory;
    static final ProtocolSelectorFactory FAIL_SELECTOR_FACTORY;
    static final ProtocolSelectorFactory NO_FAIL_SELECTOR_FACTORY;
    static final ProtocolSelectionListenerFactory FAIL_SELECTION_LISTENER_FACTORY;
    static final ProtocolSelectionListenerFactory NO_FAIL_SELECTION_LISTENER_FACTORY;
    
    protected JdkBaseApplicationProtocolNegotiator(final SslEngineWrapperFactory wrapperFactory, final ProtocolSelectorFactory selectorFactory, final ProtocolSelectionListenerFactory listenerFactory, final Iterable<String> protocols) {
        this(wrapperFactory, selectorFactory, listenerFactory, ApplicationProtocolUtil.toList(protocols));
    }
    
    protected JdkBaseApplicationProtocolNegotiator(final SslEngineWrapperFactory wrapperFactory, final ProtocolSelectorFactory selectorFactory, final ProtocolSelectionListenerFactory listenerFactory, final String... protocols) {
        this(wrapperFactory, selectorFactory, listenerFactory, ApplicationProtocolUtil.toList(protocols));
    }
    
    private JdkBaseApplicationProtocolNegotiator(final SslEngineWrapperFactory wrapperFactory, final ProtocolSelectorFactory selectorFactory, final ProtocolSelectionListenerFactory listenerFactory, final List<String> protocols) {
        this.wrapperFactory = ObjectUtil.checkNotNull(wrapperFactory, "wrapperFactory");
        this.selectorFactory = ObjectUtil.checkNotNull(selectorFactory, "selectorFactory");
        this.listenerFactory = ObjectUtil.checkNotNull(listenerFactory, "listenerFactory");
        this.protocols = Collections.unmodifiableList((List<? extends String>)ObjectUtil.checkNotNull((List<? extends T>)protocols, "protocols"));
    }
    
    @Override
    public List<String> protocols() {
        return this.protocols;
    }
    
    @Override
    public ProtocolSelectorFactory protocolSelectorFactory() {
        return this.selectorFactory;
    }
    
    @Override
    public ProtocolSelectionListenerFactory protocolListenerFactory() {
        return this.listenerFactory;
    }
    
    @Override
    public SslEngineWrapperFactory wrapperFactory() {
        return this.wrapperFactory;
    }
    
    static {
        FAIL_SELECTOR_FACTORY = new ProtocolSelectorFactory() {
            @Override
            public ProtocolSelector newSelector(final SSLEngine engine, final Set<String> supportedProtocols) {
                return new FailProtocolSelector((JdkSslEngine)engine, supportedProtocols);
            }
        };
        NO_FAIL_SELECTOR_FACTORY = new ProtocolSelectorFactory() {
            @Override
            public ProtocolSelector newSelector(final SSLEngine engine, final Set<String> supportedProtocols) {
                return new NoFailProtocolSelector((JdkSslEngine)engine, supportedProtocols);
            }
        };
        FAIL_SELECTION_LISTENER_FACTORY = new ProtocolSelectionListenerFactory() {
            @Override
            public ProtocolSelectionListener newListener(final SSLEngine engine, final List<String> supportedProtocols) {
                return new FailProtocolSelectionListener((JdkSslEngine)engine, supportedProtocols);
            }
        };
        NO_FAIL_SELECTION_LISTENER_FACTORY = new ProtocolSelectionListenerFactory() {
            @Override
            public ProtocolSelectionListener newListener(final SSLEngine engine, final List<String> supportedProtocols) {
                return new NoFailProtocolSelectionListener((JdkSslEngine)engine, supportedProtocols);
            }
        };
    }
    
    protected static class NoFailProtocolSelector implements ProtocolSelector
    {
        private final JdkSslEngine jettyWrapper;
        private final Set<String> supportedProtocols;
        
        public NoFailProtocolSelector(final JdkSslEngine jettyWrapper, final Set<String> supportedProtocols) {
            this.jettyWrapper = jettyWrapper;
            this.supportedProtocols = supportedProtocols;
        }
        
        @Override
        public void unsupported() {
            this.jettyWrapper.getSession().setApplicationProtocol(null);
        }
        
        @Override
        public String select(final List<String> protocols) throws Exception {
            for (final String p : this.supportedProtocols) {
                if (protocols.contains(p)) {
                    this.jettyWrapper.getSession().setApplicationProtocol(p);
                    return p;
                }
            }
            return this.noSelectMatchFound();
        }
        
        public String noSelectMatchFound() throws Exception {
            this.jettyWrapper.getSession().setApplicationProtocol(null);
            return null;
        }
    }
    
    protected static final class FailProtocolSelector extends NoFailProtocolSelector
    {
        public FailProtocolSelector(final JdkSslEngine jettyWrapper, final Set<String> supportedProtocols) {
            super(jettyWrapper, supportedProtocols);
        }
        
        @Override
        public String noSelectMatchFound() throws Exception {
            throw new SSLHandshakeException("Selected protocol is not supported");
        }
    }
    
    protected static class NoFailProtocolSelectionListener implements ProtocolSelectionListener
    {
        private final JdkSslEngine jettyWrapper;
        private final List<String> supportedProtocols;
        
        public NoFailProtocolSelectionListener(final JdkSslEngine jettyWrapper, final List<String> supportedProtocols) {
            this.jettyWrapper = jettyWrapper;
            this.supportedProtocols = supportedProtocols;
        }
        
        @Override
        public void unsupported() {
            this.jettyWrapper.getSession().setApplicationProtocol(null);
        }
        
        @Override
        public void selected(final String protocol) throws Exception {
            if (this.supportedProtocols.contains(protocol)) {
                this.jettyWrapper.getSession().setApplicationProtocol(protocol);
            }
            else {
                this.noSelectedMatchFound(protocol);
            }
        }
        
        public void noSelectedMatchFound(final String protocol) throws Exception {
        }
    }
    
    protected static final class FailProtocolSelectionListener extends NoFailProtocolSelectionListener
    {
        public FailProtocolSelectionListener(final JdkSslEngine jettyWrapper, final List<String> supportedProtocols) {
            super(jettyWrapper, supportedProtocols);
        }
        
        @Override
        public void noSelectedMatchFound(final String protocol) throws Exception {
            throw new SSLHandshakeException("No compatible protocols found");
        }
    }
}
