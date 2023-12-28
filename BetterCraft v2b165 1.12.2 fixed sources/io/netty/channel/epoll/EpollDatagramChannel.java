// 
// Decompiled by Procyon v0.6.0
// 

package io.netty.channel.epoll;

import io.netty.channel.unix.DatagramSocketAddress;
import io.netty.buffer.ByteBufAllocator;
import io.netty.channel.ChannelPipeline;
import java.util.ArrayList;
import java.util.List;
import io.netty.channel.socket.DatagramChannelConfig;
import io.netty.channel.AbstractChannel;
import io.netty.channel.ChannelConfig;
import io.netty.util.internal.StringUtil;
import io.netty.channel.DefaultAddressedEnvelope;
import io.netty.util.internal.PlatformDependent;
import io.netty.channel.socket.DatagramPacket;
import java.nio.ByteBuffer;
import io.netty.buffer.CompositeByteBuf;
import java.nio.channels.NotYetConnectedException;
import io.netty.buffer.ByteBuf;
import io.netty.channel.AddressedEnvelope;
import java.io.IOException;
import io.netty.channel.ChannelOutboundBuffer;
import java.net.SocketAddress;
import java.net.SocketException;
import java.net.NetworkInterface;
import io.netty.channel.ChannelPromise;
import io.netty.channel.ChannelFuture;
import java.net.InetAddress;
import io.netty.channel.Channel;
import io.netty.channel.unix.FileDescriptor;
import io.netty.channel.unix.Socket;
import java.net.InetSocketAddress;
import io.netty.channel.ChannelMetadata;
import io.netty.channel.socket.DatagramChannel;

public final class EpollDatagramChannel extends AbstractEpollChannel implements DatagramChannel
{
    private static final ChannelMetadata METADATA;
    private static final String EXPECTED_TYPES;
    private volatile InetSocketAddress local;
    private volatile InetSocketAddress remote;
    private volatile boolean connected;
    private final EpollDatagramChannelConfig config;
    
    public EpollDatagramChannel() {
        super(Socket.newSocketDgram(), Native.EPOLLIN);
        this.config = new EpollDatagramChannelConfig(this);
    }
    
    @Deprecated
    public EpollDatagramChannel(final FileDescriptor fd) {
        this(new Socket(fd.intValue()));
    }
    
    public EpollDatagramChannel(final Socket fd) {
        super(null, fd, Native.EPOLLIN, true);
        this.local = fd.localAddress();
        this.config = new EpollDatagramChannelConfig(this);
    }
    
    @Override
    public InetSocketAddress remoteAddress() {
        return (InetSocketAddress)super.remoteAddress();
    }
    
    @Override
    public InetSocketAddress localAddress() {
        return (InetSocketAddress)super.localAddress();
    }
    
    @Override
    public ChannelMetadata metadata() {
        return EpollDatagramChannel.METADATA;
    }
    
    @Override
    public boolean isActive() {
        return this.fd().isOpen() && ((this.config.getActiveOnOpen() && this.isRegistered()) || this.active);
    }
    
    @Override
    public boolean isConnected() {
        return this.connected;
    }
    
    @Override
    public ChannelFuture joinGroup(final InetAddress multicastAddress) {
        return this.joinGroup(multicastAddress, this.newPromise());
    }
    
    @Override
    public ChannelFuture joinGroup(final InetAddress multicastAddress, final ChannelPromise promise) {
        try {
            return this.joinGroup(multicastAddress, NetworkInterface.getByInetAddress(this.localAddress().getAddress()), null, promise);
        }
        catch (final SocketException e) {
            promise.setFailure((Throwable)e);
            return promise;
        }
    }
    
    @Override
    public ChannelFuture joinGroup(final InetSocketAddress multicastAddress, final NetworkInterface networkInterface) {
        return this.joinGroup(multicastAddress, networkInterface, this.newPromise());
    }
    
    @Override
    public ChannelFuture joinGroup(final InetSocketAddress multicastAddress, final NetworkInterface networkInterface, final ChannelPromise promise) {
        return this.joinGroup(multicastAddress.getAddress(), networkInterface, null, promise);
    }
    
    @Override
    public ChannelFuture joinGroup(final InetAddress multicastAddress, final NetworkInterface networkInterface, final InetAddress source) {
        return this.joinGroup(multicastAddress, networkInterface, source, this.newPromise());
    }
    
    @Override
    public ChannelFuture joinGroup(final InetAddress multicastAddress, final NetworkInterface networkInterface, final InetAddress source, final ChannelPromise promise) {
        if (multicastAddress == null) {
            throw new NullPointerException("multicastAddress");
        }
        if (networkInterface == null) {
            throw new NullPointerException("networkInterface");
        }
        promise.setFailure((Throwable)new UnsupportedOperationException("Multicast not supported"));
        return promise;
    }
    
    @Override
    public ChannelFuture leaveGroup(final InetAddress multicastAddress) {
        return this.leaveGroup(multicastAddress, this.newPromise());
    }
    
    @Override
    public ChannelFuture leaveGroup(final InetAddress multicastAddress, final ChannelPromise promise) {
        try {
            return this.leaveGroup(multicastAddress, NetworkInterface.getByInetAddress(this.localAddress().getAddress()), null, promise);
        }
        catch (final SocketException e) {
            promise.setFailure((Throwable)e);
            return promise;
        }
    }
    
    @Override
    public ChannelFuture leaveGroup(final InetSocketAddress multicastAddress, final NetworkInterface networkInterface) {
        return this.leaveGroup(multicastAddress, networkInterface, this.newPromise());
    }
    
    @Override
    public ChannelFuture leaveGroup(final InetSocketAddress multicastAddress, final NetworkInterface networkInterface, final ChannelPromise promise) {
        return this.leaveGroup(multicastAddress.getAddress(), networkInterface, null, promise);
    }
    
    @Override
    public ChannelFuture leaveGroup(final InetAddress multicastAddress, final NetworkInterface networkInterface, final InetAddress source) {
        return this.leaveGroup(multicastAddress, networkInterface, source, this.newPromise());
    }
    
    @Override
    public ChannelFuture leaveGroup(final InetAddress multicastAddress, final NetworkInterface networkInterface, final InetAddress source, final ChannelPromise promise) {
        if (multicastAddress == null) {
            throw new NullPointerException("multicastAddress");
        }
        if (networkInterface == null) {
            throw new NullPointerException("networkInterface");
        }
        promise.setFailure((Throwable)new UnsupportedOperationException("Multicast not supported"));
        return promise;
    }
    
    @Override
    public ChannelFuture block(final InetAddress multicastAddress, final NetworkInterface networkInterface, final InetAddress sourceToBlock) {
        return this.block(multicastAddress, networkInterface, sourceToBlock, this.newPromise());
    }
    
    @Override
    public ChannelFuture block(final InetAddress multicastAddress, final NetworkInterface networkInterface, final InetAddress sourceToBlock, final ChannelPromise promise) {
        if (multicastAddress == null) {
            throw new NullPointerException("multicastAddress");
        }
        if (sourceToBlock == null) {
            throw new NullPointerException("sourceToBlock");
        }
        if (networkInterface == null) {
            throw new NullPointerException("networkInterface");
        }
        promise.setFailure((Throwable)new UnsupportedOperationException("Multicast not supported"));
        return promise;
    }
    
    @Override
    public ChannelFuture block(final InetAddress multicastAddress, final InetAddress sourceToBlock) {
        return this.block(multicastAddress, sourceToBlock, this.newPromise());
    }
    
    @Override
    public ChannelFuture block(final InetAddress multicastAddress, final InetAddress sourceToBlock, final ChannelPromise promise) {
        try {
            return this.block(multicastAddress, NetworkInterface.getByInetAddress(this.localAddress().getAddress()), sourceToBlock, promise);
        }
        catch (final Throwable e) {
            promise.setFailure(e);
            return promise;
        }
    }
    
    @Override
    protected AbstractEpollUnsafe newUnsafe() {
        return new EpollDatagramChannelUnsafe();
    }
    
    @Override
    protected InetSocketAddress localAddress0() {
        return this.local;
    }
    
    @Override
    protected InetSocketAddress remoteAddress0() {
        return this.remote;
    }
    
    @Override
    protected void doBind(final SocketAddress localAddress) throws Exception {
        final InetSocketAddress addr = (InetSocketAddress)localAddress;
        AbstractEpollChannel.checkResolvable(addr);
        this.fd().bind(addr);
        this.local = this.fd().localAddress();
        this.active = true;
    }
    
    @Override
    protected void doWrite(final ChannelOutboundBuffer in) throws Exception {
        while (true) {
            final Object msg = in.current();
            if (msg == null) {
                this.clearFlag(Native.EPOLLOUT);
                break;
            }
            try {
                if (Native.IS_SUPPORTING_SENDMMSG && in.size() > 1) {
                    final NativeDatagramPacketArray array = NativeDatagramPacketArray.getInstance(in);
                    int cnt = array.count();
                    if (cnt >= 1) {
                        int offset = 0;
                        final NativeDatagramPacketArray.NativeDatagramPacket[] packets = array.packets();
                        while (cnt > 0) {
                            final int send = Native.sendmmsg(this.fd().intValue(), packets, offset, cnt);
                            if (send == 0) {
                                this.setFlag(Native.EPOLLOUT);
                                return;
                            }
                            for (int i = 0; i < send; ++i) {
                                in.remove();
                            }
                            cnt -= send;
                            offset += send;
                        }
                        continue;
                    }
                }
                boolean done = false;
                for (int j = this.config().getWriteSpinCount() - 1; j >= 0; --j) {
                    if (this.doWriteMessage(msg)) {
                        done = true;
                        break;
                    }
                }
                if (!done) {
                    this.setFlag(Native.EPOLLOUT);
                    break;
                }
                in.remove();
            }
            catch (final IOException e) {
                in.remove(e);
            }
        }
    }
    
    private boolean doWriteMessage(final Object msg) throws Exception {
        ByteBuf data;
        InetSocketAddress remoteAddress;
        if (msg instanceof AddressedEnvelope) {
            final AddressedEnvelope<ByteBuf, InetSocketAddress> envelope = (AddressedEnvelope<ByteBuf, InetSocketAddress>)msg;
            data = envelope.content();
            remoteAddress = envelope.recipient();
        }
        else {
            data = (ByteBuf)msg;
            remoteAddress = null;
        }
        final int dataLen = data.readableBytes();
        if (dataLen == 0) {
            return true;
        }
        if (remoteAddress == null) {
            remoteAddress = this.remote;
            if (remoteAddress == null) {
                throw new NotYetConnectedException();
            }
        }
        int writtenBytes;
        if (data.hasMemoryAddress()) {
            final long memoryAddress = data.memoryAddress();
            writtenBytes = this.fd().sendToAddress(memoryAddress, data.readerIndex(), data.writerIndex(), remoteAddress.getAddress(), remoteAddress.getPort());
        }
        else if (data instanceof CompositeByteBuf) {
            final IovArray array = ((EpollEventLoop)this.eventLoop()).cleanArray();
            array.add(data);
            final int cnt = array.count();
            assert cnt != 0;
            writtenBytes = this.fd().sendToAddresses(array.memoryAddress(0), cnt, remoteAddress.getAddress(), remoteAddress.getPort());
        }
        else {
            final ByteBuffer nioData = data.internalNioBuffer(data.readerIndex(), data.readableBytes());
            writtenBytes = this.fd().sendTo(nioData, nioData.position(), nioData.limit(), remoteAddress.getAddress(), remoteAddress.getPort());
        }
        return writtenBytes > 0;
    }
    
    @Override
    protected Object filterOutboundMessage(final Object msg) {
        if (msg instanceof DatagramPacket) {
            final DatagramPacket packet = (DatagramPacket)msg;
            final ByteBuf content = ((DefaultAddressedEnvelope<ByteBuf, A>)packet).content();
            if (content.hasMemoryAddress()) {
                return msg;
            }
            if (content.isDirect() && content instanceof CompositeByteBuf) {
                final CompositeByteBuf comp = (CompositeByteBuf)content;
                if (comp.isDirect() && comp.nioBufferCount() <= Native.IOV_MAX) {
                    return msg;
                }
            }
            return new DatagramPacket(this.newDirectBuffer(packet, content), ((DefaultAddressedEnvelope<M, InetSocketAddress>)packet).recipient());
        }
        else {
            if (msg instanceof ByteBuf) {
                ByteBuf buf = (ByteBuf)msg;
                if (!buf.hasMemoryAddress() && (PlatformDependent.hasUnsafe() || !buf.isDirect())) {
                    if (buf instanceof CompositeByteBuf) {
                        final CompositeByteBuf comp2 = (CompositeByteBuf)buf;
                        if (!comp2.isDirect() || comp2.nioBufferCount() > Native.IOV_MAX) {
                            buf = this.newDirectBuffer(buf);
                            assert buf.hasMemoryAddress();
                        }
                    }
                    else {
                        buf = this.newDirectBuffer(buf);
                        assert buf.hasMemoryAddress();
                    }
                }
                return buf;
            }
            if (msg instanceof AddressedEnvelope) {
                final AddressedEnvelope<Object, SocketAddress> e = (AddressedEnvelope<Object, SocketAddress>)msg;
                if (e.content() instanceof ByteBuf && (e.recipient() == null || e.recipient() instanceof InetSocketAddress)) {
                    final ByteBuf content = e.content();
                    if (content.hasMemoryAddress()) {
                        return e;
                    }
                    if (content instanceof CompositeByteBuf) {
                        final CompositeByteBuf comp = (CompositeByteBuf)content;
                        if (comp.isDirect() && comp.nioBufferCount() <= Native.IOV_MAX) {
                            return e;
                        }
                    }
                    return new DefaultAddressedEnvelope(this.newDirectBuffer(e, content), e.recipient());
                }
            }
            throw new UnsupportedOperationException("unsupported message type: " + StringUtil.simpleClassName(msg) + EpollDatagramChannel.EXPECTED_TYPES);
        }
    }
    
    @Override
    public EpollDatagramChannelConfig config() {
        return this.config;
    }
    
    @Override
    protected void doDisconnect() throws Exception {
        this.connected = false;
    }
    
    static {
        METADATA = new ChannelMetadata(true);
        EXPECTED_TYPES = " (expected: " + StringUtil.simpleClassName(DatagramPacket.class) + ", " + StringUtil.simpleClassName(AddressedEnvelope.class) + '<' + StringUtil.simpleClassName(ByteBuf.class) + ", " + StringUtil.simpleClassName(InetSocketAddress.class) + ">, " + StringUtil.simpleClassName(ByteBuf.class) + ')';
    }
    
    final class EpollDatagramChannelUnsafe extends AbstractEpollUnsafe
    {
        private final List<Object> readBuf;
        
        EpollDatagramChannelUnsafe() {
            this.readBuf = new ArrayList<Object>();
        }
        
        @Override
        public void connect(final SocketAddress remote, final SocketAddress local, final ChannelPromise channelPromise) {
            boolean success = false;
            try {
                try {
                    final boolean wasActive = EpollDatagramChannel.this.isActive();
                    final InetSocketAddress remoteAddress = (InetSocketAddress)remote;
                    if (local != null) {
                        final InetSocketAddress localAddress = (InetSocketAddress)local;
                        EpollDatagramChannel.this.doBind(localAddress);
                    }
                    AbstractEpollChannel.checkResolvable(remoteAddress);
                    EpollDatagramChannel.this.remote = remoteAddress;
                    EpollDatagramChannel.this.local = EpollDatagramChannel.this.fd().localAddress();
                    success = true;
                    channelPromise.trySuccess();
                    if (!wasActive && EpollDatagramChannel.this.isActive()) {
                        EpollDatagramChannel.this.pipeline().fireChannelActive();
                    }
                }
                finally {
                    if (!success) {
                        EpollDatagramChannel.this.doClose();
                    }
                    else {
                        EpollDatagramChannel.this.connected = true;
                    }
                }
            }
            catch (final Throwable cause) {
                channelPromise.tryFailure(cause);
            }
        }
        
        @Override
        void epollInReady() {
            assert EpollDatagramChannel.this.eventLoop().inEventLoop();
            final DatagramChannelConfig config = EpollDatagramChannel.this.config();
            if (EpollDatagramChannel.this.shouldBreakEpollInReady(config)) {
                this.clearEpollIn0();
                return;
            }
            final EpollRecvByteAllocatorHandle allocHandle = this.recvBufAllocHandle();
            allocHandle.edgeTriggered(EpollDatagramChannel.this.isFlagSet(Native.EPOLLET));
            final ChannelPipeline pipeline = EpollDatagramChannel.this.pipeline();
            final ByteBufAllocator allocator = config.getAllocator();
            allocHandle.reset(config);
            this.epollInBefore();
            Throwable exception = null;
            try {
                ByteBuf data = null;
                try {
                    do {
                        data = allocHandle.allocate(allocator);
                        allocHandle.attemptedBytesRead(data.writableBytes());
                        DatagramSocketAddress remoteAddress;
                        if (data.hasMemoryAddress()) {
                            remoteAddress = EpollDatagramChannel.this.fd().recvFromAddress(data.memoryAddress(), data.writerIndex(), data.capacity());
                        }
                        else {
                            final ByteBuffer nioData = data.internalNioBuffer(data.writerIndex(), data.writableBytes());
                            remoteAddress = EpollDatagramChannel.this.fd().recvFrom(nioData, nioData.position(), nioData.limit());
                        }
                        if (remoteAddress == null) {
                            allocHandle.lastBytesRead(-1);
                            data.release();
                            data = null;
                            break;
                        }
                        allocHandle.incMessagesRead(1);
                        allocHandle.lastBytesRead(remoteAddress.receivedAmount());
                        data.writerIndex(data.writerIndex() + allocHandle.lastBytesRead());
                        this.readBuf.add(new DatagramPacket(data, (InetSocketAddress)this.localAddress(), remoteAddress));
                        data = null;
                    } while (allocHandle.continueReading());
                }
                catch (final Throwable t) {
                    if (data != null) {
                        data.release();
                    }
                    exception = t;
                }
                for (int size = this.readBuf.size(), i = 0; i < size; ++i) {
                    this.readPending = false;
                    pipeline.fireChannelRead(this.readBuf.get(i));
                }
                this.readBuf.clear();
                allocHandle.readComplete();
                pipeline.fireChannelReadComplete();
                if (exception != null) {
                    pipeline.fireExceptionCaught(exception);
                }
            }
            finally {
                this.epollInFinally(config);
            }
        }
    }
}
