// 
// Decompiled by Procyon v0.6.0
// 

package io.netty.handler.ssl;

import java.util.List;
import io.netty.util.internal.ObjectUtil;

public final class OpenSslDefaultApplicationProtocolNegotiator implements OpenSslApplicationProtocolNegotiator
{
    private final ApplicationProtocolConfig config;
    
    public OpenSslDefaultApplicationProtocolNegotiator(final ApplicationProtocolConfig config) {
        this.config = ObjectUtil.checkNotNull(config, "config");
    }
    
    @Override
    public List<String> protocols() {
        return this.config.supportedProtocols();
    }
    
    @Override
    public ApplicationProtocolConfig.Protocol protocol() {
        return this.config.protocol();
    }
    
    @Override
    public ApplicationProtocolConfig.SelectorFailureBehavior selectorFailureBehavior() {
        return this.config.selectorFailureBehavior();
    }
    
    @Override
    public ApplicationProtocolConfig.SelectedListenerFailureBehavior selectedListenerFailureBehavior() {
        return this.config.selectedListenerFailureBehavior();
    }
}
