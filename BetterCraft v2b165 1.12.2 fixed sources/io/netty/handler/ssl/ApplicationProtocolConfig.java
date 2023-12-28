// 
// Decompiled by Procyon v0.6.0
// 

package io.netty.handler.ssl;

import java.util.Collections;
import io.netty.util.internal.ObjectUtil;
import java.util.List;

public final class ApplicationProtocolConfig
{
    public static final ApplicationProtocolConfig DISABLED;
    private final List<String> supportedProtocols;
    private final Protocol protocol;
    private final SelectorFailureBehavior selectorBehavior;
    private final SelectedListenerFailureBehavior selectedBehavior;
    
    public ApplicationProtocolConfig(final Protocol protocol, final SelectorFailureBehavior selectorBehavior, final SelectedListenerFailureBehavior selectedBehavior, final Iterable<String> supportedProtocols) {
        this(protocol, selectorBehavior, selectedBehavior, ApplicationProtocolUtil.toList(supportedProtocols));
    }
    
    public ApplicationProtocolConfig(final Protocol protocol, final SelectorFailureBehavior selectorBehavior, final SelectedListenerFailureBehavior selectedBehavior, final String... supportedProtocols) {
        this(protocol, selectorBehavior, selectedBehavior, ApplicationProtocolUtil.toList(supportedProtocols));
    }
    
    private ApplicationProtocolConfig(final Protocol protocol, final SelectorFailureBehavior selectorBehavior, final SelectedListenerFailureBehavior selectedBehavior, final List<String> supportedProtocols) {
        this.supportedProtocols = Collections.unmodifiableList((List<? extends String>)ObjectUtil.checkNotNull((List<? extends T>)supportedProtocols, "supportedProtocols"));
        this.protocol = ObjectUtil.checkNotNull(protocol, "protocol");
        this.selectorBehavior = ObjectUtil.checkNotNull(selectorBehavior, "selectorBehavior");
        this.selectedBehavior = ObjectUtil.checkNotNull(selectedBehavior, "selectedBehavior");
        if (protocol == Protocol.NONE) {
            throw new IllegalArgumentException("protocol (" + Protocol.NONE + ") must not be " + Protocol.NONE + '.');
        }
        if (supportedProtocols.isEmpty()) {
            throw new IllegalArgumentException("supportedProtocols must be not empty");
        }
    }
    
    private ApplicationProtocolConfig() {
        this.supportedProtocols = Collections.emptyList();
        this.protocol = Protocol.NONE;
        this.selectorBehavior = SelectorFailureBehavior.CHOOSE_MY_LAST_PROTOCOL;
        this.selectedBehavior = SelectedListenerFailureBehavior.ACCEPT;
    }
    
    public List<String> supportedProtocols() {
        return this.supportedProtocols;
    }
    
    public Protocol protocol() {
        return this.protocol;
    }
    
    public SelectorFailureBehavior selectorFailureBehavior() {
        return this.selectorBehavior;
    }
    
    public SelectedListenerFailureBehavior selectedListenerFailureBehavior() {
        return this.selectedBehavior;
    }
    
    static {
        DISABLED = new ApplicationProtocolConfig();
    }
    
    public enum Protocol
    {
        NONE, 
        NPN, 
        ALPN, 
        NPN_AND_ALPN;
    }
    
    public enum SelectorFailureBehavior
    {
        FATAL_ALERT, 
        NO_ADVERTISE, 
        CHOOSE_MY_LAST_PROTOCOL;
    }
    
    public enum SelectedListenerFailureBehavior
    {
        ACCEPT, 
        FATAL_ALERT, 
        CHOOSE_MY_LAST_PROTOCOL;
    }
}
