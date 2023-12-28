// 
// Decompiled by Procyon v0.6.0
// 

package io.netty.channel;

import io.netty.buffer.ByteBufAllocator;
import java.net.SocketAddress;
import io.netty.util.AttributeMap;

public interface Channel extends AttributeMap, ChannelOutboundInvoker, Comparable<Channel>
{
    ChannelId id();
    
    EventLoop eventLoop();
    
    Channel parent();
    
    ChannelConfig config();
    
    boolean isOpen();
    
    boolean isRegistered();
    
    boolean isActive();
    
    ChannelMetadata metadata();
    
    SocketAddress localAddress();
    
    SocketAddress remoteAddress();
    
    ChannelFuture closeFuture();
    
    boolean isWritable();
    
    long bytesBeforeUnwritable();
    
    long bytesBeforeWritable();
    
    Unsafe unsafe();
    
    ChannelPipeline pipeline();
    
    ByteBufAllocator alloc();
    
    Channel read();
    
    Channel flush();
    
    public interface Unsafe
    {
        RecvByteBufAllocator.Handle recvBufAllocHandle();
        
        SocketAddress localAddress();
        
        SocketAddress remoteAddress();
        
        void register(final EventLoop p0, final ChannelPromise p1);
        
        void bind(final SocketAddress p0, final ChannelPromise p1);
        
        void connect(final SocketAddress p0, final SocketAddress p1, final ChannelPromise p2);
        
        void disconnect(final ChannelPromise p0);
        
        void close(final ChannelPromise p0);
        
        void closeForcibly();
        
        void deregister(final ChannelPromise p0);
        
        void beginRead();
        
        void write(final Object p0, final ChannelPromise p1);
        
        void flush();
        
        ChannelPromise voidPromise();
        
        ChannelOutboundBuffer outboundBuffer();
    }
}
