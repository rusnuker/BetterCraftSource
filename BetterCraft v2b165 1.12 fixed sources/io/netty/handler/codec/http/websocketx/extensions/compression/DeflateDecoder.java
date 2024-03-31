// 
// Decompiled by Procyon v0.6.0
// 

package io.netty.handler.codec.http.websocketx.extensions.compression;

import io.netty.buffer.CompositeByteBuf;
import io.netty.handler.codec.http.websocketx.ContinuationWebSocketFrame;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.handler.codec.compression.ZlibCodecFactory;
import io.netty.handler.codec.compression.ZlibWrapper;
import io.netty.channel.ChannelHandler;
import io.netty.handler.codec.CodecException;
import io.netty.handler.codec.http.websocketx.BinaryWebSocketFrame;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import java.util.List;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.websocketx.WebSocketFrame;
import io.netty.channel.embedded.EmbeddedChannel;
import io.netty.handler.codec.http.websocketx.extensions.WebSocketExtensionDecoder;

abstract class DeflateDecoder extends WebSocketExtensionDecoder
{
    static final byte[] FRAME_TAIL;
    private final boolean noContext;
    private EmbeddedChannel decoder;
    
    public DeflateDecoder(final boolean noContext) {
        this.noContext = noContext;
    }
    
    protected abstract boolean appendFrameTail(final WebSocketFrame p0);
    
    protected abstract int newRsv(final WebSocketFrame p0);
    
    @Override
    protected void decode(final ChannelHandlerContext ctx, final WebSocketFrame msg, final List<Object> out) throws Exception {
        if (this.decoder == null) {
            if (!(msg instanceof TextWebSocketFrame) && !(msg instanceof BinaryWebSocketFrame)) {
                throw new CodecException("unexpected initial frame type: " + msg.getClass().getName());
            }
            this.decoder = new EmbeddedChannel(new ChannelHandler[] { ZlibCodecFactory.newZlibDecoder(ZlibWrapper.NONE) });
        }
        final boolean readable = msg.content().isReadable();
        this.decoder.writeInbound(msg.content().retain());
        if (this.appendFrameTail(msg)) {
            this.decoder.writeInbound(Unpooled.wrappedBuffer(DeflateDecoder.FRAME_TAIL));
        }
        final CompositeByteBuf compositeUncompressedContent = ctx.alloc().compositeBuffer();
        while (true) {
            final ByteBuf partUncompressedContent = this.decoder.readInbound();
            if (partUncompressedContent == null) {
                break;
            }
            if (!partUncompressedContent.isReadable()) {
                partUncompressedContent.release();
            }
            else {
                compositeUncompressedContent.addComponent(true, partUncompressedContent);
            }
        }
        if (readable && compositeUncompressedContent.numComponents() <= 0) {
            compositeUncompressedContent.release();
            throw new CodecException("cannot read uncompressed buffer");
        }
        if (msg.isFinalFragment() && this.noContext) {
            this.cleanup();
        }
        WebSocketFrame outMsg;
        if (msg instanceof TextWebSocketFrame) {
            outMsg = new TextWebSocketFrame(msg.isFinalFragment(), this.newRsv(msg), compositeUncompressedContent);
        }
        else if (msg instanceof BinaryWebSocketFrame) {
            outMsg = new BinaryWebSocketFrame(msg.isFinalFragment(), this.newRsv(msg), compositeUncompressedContent);
        }
        else {
            if (!(msg instanceof ContinuationWebSocketFrame)) {
                throw new CodecException("unexpected frame type: " + msg.getClass().getName());
            }
            outMsg = new ContinuationWebSocketFrame(msg.isFinalFragment(), this.newRsv(msg), compositeUncompressedContent);
        }
        out.add(outMsg);
    }
    
    @Override
    public void handlerRemoved(final ChannelHandlerContext ctx) throws Exception {
        this.cleanup();
        super.handlerRemoved(ctx);
    }
    
    @Override
    public void channelInactive(final ChannelHandlerContext ctx) throws Exception {
        this.cleanup();
        super.channelInactive(ctx);
    }
    
    private void cleanup() {
        if (this.decoder != null) {
            if (this.decoder.finish()) {
                while (true) {
                    final ByteBuf buf = this.decoder.readOutbound();
                    if (buf == null) {
                        break;
                    }
                    buf.release();
                }
            }
            this.decoder = null;
        }
    }
    
    static {
        FRAME_TAIL = new byte[] { 0, 0, -1, -1 };
    }
}
