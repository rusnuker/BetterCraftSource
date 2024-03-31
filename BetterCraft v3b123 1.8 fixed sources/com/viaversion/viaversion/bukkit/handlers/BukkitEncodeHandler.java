// 
// Decompiled by Procyon v0.6.0
// 

package com.viaversion.viaversion.bukkit.handlers;

import com.viaversion.viaversion.exception.InformativeException;
import com.viaversion.viaversion.bukkit.util.NMSUtil;
import com.viaversion.viaversion.exception.CancelCodecException;
import io.netty.handler.codec.MessageToByteEncoder;
import io.netty.channel.ChannelPipeline;
import com.viaversion.viaversion.util.PipelineUtil;
import io.netty.handler.codec.ByteToMessageDecoder;
import java.util.function.Function;
import com.viaversion.viaversion.exception.CancelEncoderException;
import java.util.List;
import io.netty.channel.ChannelHandlerContext;
import com.viaversion.viaversion.api.connection.UserConnection;
import io.netty.channel.ChannelHandler;
import io.netty.buffer.ByteBuf;
import io.netty.handler.codec.MessageToMessageEncoder;

@ChannelHandler.Sharable
public final class BukkitEncodeHandler extends MessageToMessageEncoder<ByteBuf>
{
    private final UserConnection connection;
    private boolean handledCompression;
    
    public BukkitEncodeHandler(final UserConnection connection) {
        this.handledCompression = (BukkitChannelInitializer.COMPRESSION_ENABLED_EVENT != null);
        this.connection = connection;
    }
    
    @Override
    protected void encode(final ChannelHandlerContext ctx, final ByteBuf bytebuf, final List<Object> out) throws Exception {
        if (!this.connection.checkClientboundPacket()) {
            throw CancelEncoderException.generate(null);
        }
        if (!this.connection.shouldTransformPacket()) {
            out.add(bytebuf.retain());
            return;
        }
        final ByteBuf transformedBuf = ctx.alloc().buffer().writeBytes(bytebuf);
        try {
            final boolean needsCompression = !this.handledCompression && this.handleCompressionOrder(ctx, transformedBuf);
            this.connection.transformClientbound(transformedBuf, (Function<Throwable, Exception>)CancelEncoderException::generate);
            if (needsCompression) {
                this.recompress(ctx, transformedBuf);
            }
            out.add(transformedBuf.retain());
        }
        finally {
            transformedBuf.release();
        }
    }
    
    private boolean handleCompressionOrder(final ChannelHandlerContext ctx, final ByteBuf buf) throws Exception {
        final ChannelPipeline pipeline = ctx.pipeline();
        final List<String> names = pipeline.names();
        final int compressorIndex = names.indexOf("compress");
        if (compressorIndex == -1) {
            return false;
        }
        this.handledCompression = true;
        if (compressorIndex > names.indexOf("via-encoder")) {
            final ByteBuf decompressed = PipelineUtil.callDecode((ByteToMessageDecoder)pipeline.get("decompress"), ctx, buf).get(0);
            try {
                buf.clear().writeBytes(decompressed);
            }
            finally {
                decompressed.release();
            }
            pipeline.addAfter("compress", "via-encoder", pipeline.remove("via-encoder"));
            pipeline.addAfter("decompress", "via-decoder", pipeline.remove("via-decoder"));
            return true;
        }
        return false;
    }
    
    private void recompress(final ChannelHandlerContext ctx, final ByteBuf buf) throws Exception {
        final ByteBuf compressed = ctx.alloc().buffer();
        try {
            PipelineUtil.callEncode((MessageToByteEncoder)ctx.pipeline().get("compress"), ctx, buf, compressed);
            buf.clear().writeBytes(compressed);
        }
        finally {
            compressed.release();
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
    
    public UserConnection connection() {
        return this.connection;
    }
}
