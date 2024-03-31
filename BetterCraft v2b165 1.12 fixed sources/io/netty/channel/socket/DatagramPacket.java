// 
// Decompiled by Procyon v0.6.0
// 

package io.netty.channel.socket;

import io.netty.util.ReferenceCounted;
import io.netty.channel.AddressedEnvelope;
import io.netty.buffer.ByteBufHolder;
import java.net.InetSocketAddress;
import io.netty.buffer.ByteBuf;
import io.netty.channel.DefaultAddressedEnvelope;

public final class DatagramPacket extends DefaultAddressedEnvelope<ByteBuf, InetSocketAddress> implements ByteBufHolder
{
    public DatagramPacket(final ByteBuf data, final InetSocketAddress recipient) {
        super(data, recipient);
    }
    
    public DatagramPacket(final ByteBuf data, final InetSocketAddress recipient, final InetSocketAddress sender) {
        super(data, recipient, sender);
    }
    
    @Override
    public DatagramPacket copy() {
        return this.replace(((DefaultAddressedEnvelope<ByteBuf, A>)this).content().copy());
    }
    
    @Override
    public DatagramPacket duplicate() {
        return this.replace(((DefaultAddressedEnvelope<ByteBuf, A>)this).content().duplicate());
    }
    
    @Override
    public DatagramPacket retainedDuplicate() {
        return this.replace(((DefaultAddressedEnvelope<ByteBuf, A>)this).content().retainedDuplicate());
    }
    
    @Override
    public DatagramPacket replace(final ByteBuf content) {
        return new DatagramPacket(content, ((DefaultAddressedEnvelope<M, InetSocketAddress>)this).recipient(), ((DefaultAddressedEnvelope<M, InetSocketAddress>)this).sender());
    }
    
    @Override
    public DatagramPacket retain() {
        super.retain();
        return this;
    }
    
    @Override
    public DatagramPacket retain(final int increment) {
        super.retain(increment);
        return this;
    }
    
    @Override
    public DatagramPacket touch() {
        super.touch();
        return this;
    }
    
    @Override
    public DatagramPacket touch(final Object hint) {
        super.touch(hint);
        return this;
    }
}
