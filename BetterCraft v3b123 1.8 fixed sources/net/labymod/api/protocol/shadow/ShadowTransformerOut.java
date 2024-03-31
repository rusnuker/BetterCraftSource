// 
// Decompiled by Procyon v0.6.0
// 

package net.labymod.api.protocol.shadow;

import java.beans.ConstructorProperties;
import net.labymod.core.LabyModCore;
import io.netty.channel.ChannelPromise;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelOutboundHandlerAdapter;

public class ShadowTransformerOut extends ChannelOutboundHandlerAdapter
{
    private ShadowProtocol shadow;
    
    @Override
    public void write(final ChannelHandlerContext ctx, final Object msg, final ChannelPromise promise) throws Exception {
        if (!this.shadow.isShadowSupported()) {
            super.write(ctx, msg, promise);
            return;
        }
        if (LabyModCore.getCoreAdapter().getProtocolAdapter().handleOutgoingPacket(msg, this.shadow)) {
            return;
        }
        super.write(ctx, msg, promise);
    }
    
    @ConstructorProperties({ "shadow" })
    public ShadowTransformerOut(final ShadowProtocol shadow) {
        this.shadow = shadow;
    }
}
