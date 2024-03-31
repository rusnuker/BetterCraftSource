// 
// Decompiled by Procyon v0.6.0
// 

package io.netty.handler.codec;

import io.netty.channel.ChannelPromise;
import java.net.SocketAddress;
import io.netty.channel.socket.DatagramPacket;
import io.netty.buffer.ByteBuf;
import io.netty.util.internal.StringUtil;
import java.util.List;
import io.netty.channel.ChannelHandlerContext;
import io.netty.util.internal.ObjectUtil;
import java.net.InetSocketAddress;
import io.netty.channel.AddressedEnvelope;

public class DatagramPacketEncoder<M> extends MessageToMessageEncoder<AddressedEnvelope<M, InetSocketAddress>>
{
    private final MessageToMessageEncoder<? super M> encoder;
    
    public DatagramPacketEncoder(final MessageToMessageEncoder<? super M> encoder) {
        this.encoder = ObjectUtil.checkNotNull(encoder, "encoder");
    }
    
    @Override
    public boolean acceptOutboundMessage(final Object msg) throws Exception {
        if (super.acceptOutboundMessage(msg)) {
            final AddressedEnvelope envelope = (AddressedEnvelope)msg;
            return this.encoder.acceptOutboundMessage(envelope.content()) && envelope.sender() instanceof InetSocketAddress && envelope.recipient() instanceof InetSocketAddress;
        }
        return false;
    }
    
    @Override
    protected void encode(final ChannelHandlerContext ctx, final AddressedEnvelope<M, InetSocketAddress> msg, final List<Object> out) throws Exception {
        assert out.isEmpty();
        this.encoder.encode(ctx, msg.content(), out);
        if (out.size() != 1) {
            throw new EncoderException(StringUtil.simpleClassName(this.encoder) + " must produce only one message.");
        }
        final Object content = out.get(0);
        if (content instanceof ByteBuf) {
            out.set(0, new DatagramPacket((ByteBuf)content, msg.recipient(), msg.sender()));
            return;
        }
        throw new EncoderException(StringUtil.simpleClassName(this.encoder) + " must produce only ByteBuf.");
    }
    
    @Override
    public void bind(final ChannelHandlerContext ctx, final SocketAddress localAddress, final ChannelPromise promise) throws Exception {
        this.encoder.bind(ctx, localAddress, promise);
    }
    
    @Override
    public void connect(final ChannelHandlerContext ctx, final SocketAddress remoteAddress, final SocketAddress localAddress, final ChannelPromise promise) throws Exception {
        this.encoder.connect(ctx, remoteAddress, localAddress, promise);
    }
    
    @Override
    public void disconnect(final ChannelHandlerContext ctx, final ChannelPromise promise) throws Exception {
        this.encoder.disconnect(ctx, promise);
    }
    
    @Override
    public void close(final ChannelHandlerContext ctx, final ChannelPromise promise) throws Exception {
        this.encoder.close(ctx, promise);
    }
    
    @Override
    public void deregister(final ChannelHandlerContext ctx, final ChannelPromise promise) throws Exception {
        this.encoder.deregister(ctx, promise);
    }
    
    @Override
    public void read(final ChannelHandlerContext ctx) throws Exception {
        this.encoder.read(ctx);
    }
    
    @Override
    public void flush(final ChannelHandlerContext ctx) throws Exception {
        this.encoder.flush(ctx);
    }
    
    @Override
    public void handlerAdded(final ChannelHandlerContext ctx) throws Exception {
        this.encoder.handlerAdded(ctx);
    }
    
    @Override
    public void handlerRemoved(final ChannelHandlerContext ctx) throws Exception {
        this.encoder.handlerRemoved(ctx);
    }
    
    @Override
    public void exceptionCaught(final ChannelHandlerContext ctx, final Throwable cause) throws Exception {
        this.encoder.exceptionCaught(ctx, cause);
    }
    
    @Override
    public boolean isSharable() {
        return this.encoder.isSharable();
    }
}
