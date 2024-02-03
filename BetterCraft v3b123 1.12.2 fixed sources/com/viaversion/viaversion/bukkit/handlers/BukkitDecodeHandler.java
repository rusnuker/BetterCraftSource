// 
// Decompiled by Procyon v0.6.0
// 

package com.viaversion.viaversion.bukkit.handlers;

import io.netty.channel.ChannelPipeline;
import com.viaversion.viaversion.exception.InformativeException;
import com.viaversion.viaversion.bukkit.util.NMSUtil;
import com.viaversion.viaversion.util.PipelineUtil;
import com.viaversion.viaversion.exception.CancelCodecException;
import java.util.function.Function;
import com.viaversion.viaversion.exception.CancelDecoderException;
import java.util.List;
import io.netty.channel.ChannelHandlerContext;
import com.viaversion.viaversion.api.connection.UserConnection;
import io.netty.channel.ChannelHandler;
import io.netty.buffer.ByteBuf;
import io.netty.handler.codec.MessageToMessageDecoder;

@ChannelHandler.Sharable
public final class BukkitDecodeHandler extends MessageToMessageDecoder<ByteBuf>
{
    private final UserConnection connection;
    
    public BukkitDecodeHandler(final UserConnection connection) {
        this.connection = connection;
    }
    
    @Override
    protected void decode(final ChannelHandlerContext ctx, final ByteBuf bytebuf, final List<Object> out) throws Exception {
        if (!this.connection.checkServerboundPacket()) {
            throw CancelDecoderException.generate(null);
        }
        if (!this.connection.shouldTransformPacket()) {
            out.add(bytebuf.retain());
            return;
        }
        final ByteBuf transformedBuf = ctx.alloc().buffer().writeBytes(bytebuf);
        try {
            this.connection.transformIncoming(transformedBuf, (Function<Throwable, Exception>)CancelDecoderException::generate);
            out.add(transformedBuf.retain());
        }
        finally {
            transformedBuf.release();
        }
    }
    
    @Override
    public void exceptionCaught(final ChannelHandlerContext ctx, final Throwable cause) throws Exception {
        if (PipelineUtil.containsCause(cause, CancelCodecException.class)) {
            return;
        }
        super.exceptionCaught(ctx, cause);
        if (NMSUtil.isDebugPropertySet()) {
            return;
        }
        final InformativeException exception = PipelineUtil.getCause(cause, InformativeException.class);
        if (exception != null && exception.shouldBePrinted()) {
            cause.printStackTrace();
            exception.setShouldBePrinted(false);
        }
    }
    
    @Override
    public void userEventTriggered(final ChannelHandlerContext ctx, final Object event) throws Exception {
        if (BukkitChannelInitializer.COMPRESSION_ENABLED_EVENT == null || event != BukkitChannelInitializer.COMPRESSION_ENABLED_EVENT) {
            super.userEventTriggered(ctx, event);
            return;
        }
        final ChannelPipeline pipeline = ctx.pipeline();
        pipeline.addAfter("compress", "via-encoder", pipeline.remove("via-encoder"));
        pipeline.addAfter("decompress", "via-decoder", pipeline.remove("via-decoder"));
        super.userEventTriggered(ctx, event);
    }
}
