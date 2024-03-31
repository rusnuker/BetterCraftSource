// 
// Decompiled by Procyon v0.6.0
// 

package viamcp.vialoadingbase.netty;

import viamcp.vialoadingbase.netty.handler.VLBViaEncodeHandler;
import viamcp.vialoadingbase.netty.handler.VLBViaDecodeHandler;
import viamcp.vialoadingbase.netty.event.CompressionReorderEvent;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import com.viaversion.viaversion.api.connection.UserConnection;
import io.netty.channel.ChannelInboundHandlerAdapter;

public abstract class VLBPipeline extends ChannelInboundHandlerAdapter
{
    public static final String VIA_DECODER_HANDLER_NAME = "via-decoder";
    public static final String VIA_ENCODER_HANDLER_NAME = "via-encoder";
    private final UserConnection user;
    
    public VLBPipeline(final UserConnection user) {
        this.user = user;
    }
    
    @Override
    public void handlerAdded(final ChannelHandlerContext ctx) throws Exception {
        super.handlerAdded(ctx);
        ctx.pipeline().addBefore(this.getDecoderHandlerName(), "via-decoder", this.createVLBViaDecodeHandler());
        ctx.pipeline().addBefore(this.getEncoderHandlerName(), "via-encoder", this.createVLBViaEncodeHandler());
    }
    
    @Override
    public void userEventTriggered(final ChannelHandlerContext ctx, final Object evt) throws Exception {
        super.userEventTriggered(ctx, evt);
        if (evt instanceof CompressionReorderEvent) {
            final int decoderIndex = ctx.pipeline().names().indexOf(this.getDecompressionHandlerName());
            if (decoderIndex == -1) {
                return;
            }
            if (decoderIndex > ctx.pipeline().names().indexOf("via-decoder")) {
                final ChannelHandler decoder = ctx.pipeline().get("via-decoder");
                final ChannelHandler encoder = ctx.pipeline().get("via-encoder");
                ctx.pipeline().remove(decoder);
                ctx.pipeline().remove(encoder);
                ctx.pipeline().addAfter(this.getDecompressionHandlerName(), "via-decoder", decoder);
                ctx.pipeline().addAfter(this.getCompressionHandlerName(), "via-encoder", encoder);
            }
        }
    }
    
    public VLBViaDecodeHandler createVLBViaDecodeHandler() {
        return new VLBViaDecodeHandler(this.user);
    }
    
    public VLBViaEncodeHandler createVLBViaEncodeHandler() {
        return new VLBViaEncodeHandler(this.user);
    }
    
    public abstract String getDecoderHandlerName();
    
    public abstract String getEncoderHandlerName();
    
    public abstract String getDecompressionHandlerName();
    
    public abstract String getCompressionHandlerName();
    
    public UserConnection getUser() {
        return this.user;
    }
}
