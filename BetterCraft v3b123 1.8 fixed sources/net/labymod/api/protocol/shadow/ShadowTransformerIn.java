// 
// Decompiled by Procyon v0.6.0
// 

package net.labymod.api.protocol.shadow;

import java.beans.ConstructorProperties;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class ShadowTransformerIn extends ChannelInboundHandlerAdapter
{
    private ShadowProtocol shadow;
    
    @Override
    public void channelRead(final ChannelHandlerContext ctx, final Object msg) throws Exception {
        if (!this.shadow.isShadowSupported()) {
            super.channelRead(ctx, msg);
            return;
        }
        this.shadow.increaseCounter();
        super.channelRead(ctx, msg);
    }
    
    @ConstructorProperties({ "shadow" })
    public ShadowTransformerIn(final ShadowProtocol shadow) {
        this.shadow = shadow;
    }
}
