// 
// Decompiled by Procyon v0.6.0
// 

package io.netty.channel.epoll;

import io.netty.channel.DefaultAddressedEnvelope;
import java.net.InetAddress;
import io.netty.channel.unix.NativeInetAddress;
import java.net.Inet6Address;
import java.net.InetSocketAddress;
import io.netty.buffer.ByteBuf;
import io.netty.channel.socket.DatagramPacket;
import io.netty.util.concurrent.FastThreadLocal;
import io.netty.channel.ChannelOutboundBuffer;

final class NativeDatagramPacketArray implements ChannelOutboundBuffer.MessageProcessor
{
    private static final FastThreadLocal<NativeDatagramPacketArray> ARRAY;
    private final NativeDatagramPacket[] packets;
    private int count;
    
    private NativeDatagramPacketArray() {
        this.packets = new NativeDatagramPacket[Native.UIO_MAX_IOV];
        for (int i = 0; i < this.packets.length; ++i) {
            this.packets[i] = new NativeDatagramPacket();
        }
    }
    
    boolean add(final DatagramPacket packet) {
        if (this.count == this.packets.length) {
            return false;
        }
        final ByteBuf content = ((DefaultAddressedEnvelope<ByteBuf, A>)packet).content();
        final int len = content.readableBytes();
        if (len == 0) {
            return true;
        }
        final NativeDatagramPacket p = this.packets[this.count];
        final InetSocketAddress recipient = ((DefaultAddressedEnvelope<M, InetSocketAddress>)packet).recipient();
        if (!p.init(content, recipient)) {
            return false;
        }
        ++this.count;
        return true;
    }
    
    @Override
    public boolean processMessage(final Object msg) throws Exception {
        return msg instanceof DatagramPacket && this.add((DatagramPacket)msg);
    }
    
    int count() {
        return this.count;
    }
    
    NativeDatagramPacket[] packets() {
        return this.packets;
    }
    
    static NativeDatagramPacketArray getInstance(final ChannelOutboundBuffer buffer) throws Exception {
        final NativeDatagramPacketArray array = NativeDatagramPacketArray.ARRAY.get();
        array.count = 0;
        buffer.forEachFlushedMessage(array);
        return array;
    }
    
    static {
        ARRAY = new FastThreadLocal<NativeDatagramPacketArray>() {
            @Override
            protected NativeDatagramPacketArray initialValue() throws Exception {
                return new NativeDatagramPacketArray(null);
            }
            
            @Override
            protected void onRemoval(final NativeDatagramPacketArray value) throws Exception {
                final NativeDatagramPacket[] access$100;
                final NativeDatagramPacket[] packetsArray = access$100 = value.packets;
                for (final NativeDatagramPacket datagramPacket : access$100) {
                    datagramPacket.release();
                }
            }
        };
    }
    
    static final class NativeDatagramPacket
    {
        private final IovArray array;
        private long memoryAddress;
        private int count;
        private byte[] addr;
        private int scopeId;
        private int port;
        
        NativeDatagramPacket() {
            this.array = new IovArray();
        }
        
        private void release() {
            this.array.release();
        }
        
        private boolean init(final ByteBuf buf, final InetSocketAddress recipient) {
            this.array.clear();
            if (!this.array.add(buf)) {
                return false;
            }
            this.memoryAddress = this.array.memoryAddress(0);
            this.count = this.array.count();
            final InetAddress address = recipient.getAddress();
            if (address instanceof Inet6Address) {
                this.addr = address.getAddress();
                this.scopeId = ((Inet6Address)address).getScopeId();
            }
            else {
                this.addr = NativeInetAddress.ipv4MappedIpv6Address(address.getAddress());
                this.scopeId = 0;
            }
            this.port = recipient.getPort();
            return true;
        }
    }
}
