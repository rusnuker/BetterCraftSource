// 
// Decompiled by Procyon v0.6.0
// 

package io.netty.handler.codec;

import io.netty.channel.DefaultAddressedEnvelope;
import java.util.List;
import io.netty.channel.ChannelHandlerContext;
import io.netty.util.internal.ObjectUtil;
import io.netty.buffer.ByteBuf;
import io.netty.channel.socket.DatagramPacket;

public class DatagramPacketDecoder extends MessageToMessageDecoder<DatagramPacket>
{
    private final MessageToMessageDecoder<ByteBuf> decoder;
    
    public DatagramPacketDecoder(final MessageToMessageDecoder<ByteBuf> decoder) {
        this.decoder = ObjectUtil.checkNotNull(decoder, "decoder");
    }
    
    @Override
    public boolean acceptInboundMessage(final Object msg) throws Exception {
        return msg instanceof DatagramPacket && this.decoder.acceptInboundMessage(((DefaultAddressedEnvelope<Object, A>)msg).content());
    }
    
    @Override
    protected void decode(final ChannelHandlerContext ctx, final DatagramPacket msg, final List<Object> out) throws Exception {
        this.decoder.decode(ctx, ((DefaultAddressedEnvelope<ByteBuf, A>)msg).content(), out);
    }
    
    @Override
    public void channelRegistered(final ChannelHandlerContext ctx) throws Exception {
        this.decoder.channelRegistered(ctx);
    }
    
    @Override
    public void channelUnregistered(final ChannelHandlerContext ctx) throws Exception {
        this.decoder.channelUnregistered(ctx);
    }
    
    @Override
    public void channelActive(final ChannelHandlerContext ctx) throws Exception {
        this.decoder.channelActive(ctx);
    }
    
    @Override
    public void channelInactive(final ChannelHandlerContext ctx) throws Exception {
        this.decoder.channelInactive(ctx);
    }
    
    @Override
    public void channelReadComplete(final ChannelHandlerContext ctx) throws Exception {
        this.decoder.channelReadComplete(ctx);
    }
    
    @Override
    public void userEventTriggered(final ChannelHandlerContext ctx, final Object evt) throws Exception {
        this.decoder.userEventTriggered(ctx, evt);
    }
    
    @Override
    public void channelWritabilityChanged(final ChannelHandlerContext ctx) throws Exception {
        this.decoder.channelWritabilityChanged(ctx);
    }
    
    @Override
    public void exceptionCaught(final ChannelHandlerContext ctx, final Throwable cause) throws Exception {
        this.decoder.exceptionCaught(ctx, cause);
    }
    
    @Override
    public void handlerAdded(final ChannelHandlerContext ctx) throws Exception {
        this.decoder.handlerAdded(ctx);
    }
    
    @Override
    public void handlerRemoved(final ChannelHandlerContext ctx) throws Exception {
        this.decoder.handlerRemoved(ctx);
    }
    
    @Override
    public boolean isSharable() {
        return this.decoder.isSharable();
    }
}
