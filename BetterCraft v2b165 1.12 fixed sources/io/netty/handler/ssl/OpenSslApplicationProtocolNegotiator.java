// 
// Decompiled by Procyon v0.6.0
// 

package io.netty.handler.ssl;

public interface OpenSslApplicationProtocolNegotiator extends ApplicationProtocolNegotiator
{
    ApplicationProtocolConfig.Protocol protocol();
    
    ApplicationProtocolConfig.SelectorFailureBehavior selectorFailureBehavior();
    
    ApplicationProtocolConfig.SelectedListenerFailureBehavior selectedListenerFailureBehavior();
}
