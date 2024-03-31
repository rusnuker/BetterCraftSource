// 
// Decompiled by Procyon v0.6.0
// 

package io.netty.channel.unix;

import io.netty.util.internal.ThrowableUtil;
import io.netty.channel.ChannelException;
import io.netty.util.CharsetUtil;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.net.Inet6Address;
import java.net.InetAddress;
import java.nio.ByteBuffer;
import java.io.IOException;
import java.nio.channels.ClosedChannelException;

public final class Socket extends FileDescriptor
{
    private static final ClosedChannelException SHUTDOWN_CLOSED_CHANNEL_EXCEPTION;
    private static final ClosedChannelException SEND_TO_CLOSED_CHANNEL_EXCEPTION;
    private static final ClosedChannelException SEND_TO_ADDRESS_CLOSED_CHANNEL_EXCEPTION;
    private static final ClosedChannelException SEND_TO_ADDRESSES_CLOSED_CHANNEL_EXCEPTION;
    private static final Errors.NativeIoException SEND_TO_CONNECTION_RESET_EXCEPTION;
    private static final Errors.NativeIoException SEND_TO_ADDRESS_CONNECTION_RESET_EXCEPTION;
    private static final Errors.NativeIoException CONNECTION_RESET_EXCEPTION_SENDMSG;
    private static final Errors.NativeIoException CONNECTION_RESET_SHUTDOWN_EXCEPTION;
    private static final Errors.NativeConnectException FINISH_CONNECT_REFUSED_EXCEPTION;
    private static final Errors.NativeConnectException CONNECT_REFUSED_EXCEPTION;
    
    public Socket(final int fd) {
        super(fd);
    }
    
    public void shutdown() throws IOException {
        this.shutdown(true, true);
    }
    
    public void shutdown(final boolean read, final boolean write) throws IOException {
        while (true) {
            final int oldState = this.state;
            if (FileDescriptor.isClosed(oldState)) {
                throw new ClosedChannelException();
            }
            int newState = oldState;
            if (read && !FileDescriptor.isInputShutdown(newState)) {
                newState = FileDescriptor.inputShutdown(newState);
            }
            if (write && !FileDescriptor.isOutputShutdown(newState)) {
                newState = FileDescriptor.outputShutdown(newState);
            }
            if (newState == oldState) {
                return;
            }
            if (this.casState(oldState, newState)) {
                final int res = shutdown(this.fd, read, write);
                if (res < 0) {
                    Errors.ioResult("shutdown", res, Socket.CONNECTION_RESET_SHUTDOWN_EXCEPTION, Socket.SHUTDOWN_CLOSED_CHANNEL_EXCEPTION);
                }
            }
        }
    }
    
    public boolean isShutdown() {
        final int state = this.state;
        return FileDescriptor.isInputShutdown(state) && FileDescriptor.isOutputShutdown(state);
    }
    
    public boolean isInputShutdown() {
        return FileDescriptor.isInputShutdown(this.state);
    }
    
    public boolean isOutputShutdown() {
        return FileDescriptor.isOutputShutdown(this.state);
    }
    
    public int sendTo(final ByteBuffer buf, final int pos, final int limit, final InetAddress addr, final int port) throws IOException {
        byte[] address;
        int scopeId;
        if (addr instanceof Inet6Address) {
            address = addr.getAddress();
            scopeId = ((Inet6Address)addr).getScopeId();
        }
        else {
            scopeId = 0;
            address = NativeInetAddress.ipv4MappedIpv6Address(addr.getAddress());
        }
        final int res = sendTo(this.fd, buf, pos, limit, address, scopeId, port);
        if (res >= 0) {
            return res;
        }
        return Errors.ioResult("sendTo", res, Socket.SEND_TO_CONNECTION_RESET_EXCEPTION, Socket.SEND_TO_CLOSED_CHANNEL_EXCEPTION);
    }
    
    public int sendToAddress(final long memoryAddress, final int pos, final int limit, final InetAddress addr, final int port) throws IOException {
        byte[] address;
        int scopeId;
        if (addr instanceof Inet6Address) {
            address = addr.getAddress();
            scopeId = ((Inet6Address)addr).getScopeId();
        }
        else {
            scopeId = 0;
            address = NativeInetAddress.ipv4MappedIpv6Address(addr.getAddress());
        }
        final int res = sendToAddress(this.fd, memoryAddress, pos, limit, address, scopeId, port);
        if (res >= 0) {
            return res;
        }
        return Errors.ioResult("sendToAddress", res, Socket.SEND_TO_ADDRESS_CONNECTION_RESET_EXCEPTION, Socket.SEND_TO_ADDRESS_CLOSED_CHANNEL_EXCEPTION);
    }
    
    public int sendToAddresses(final long memoryAddress, final int length, final InetAddress addr, final int port) throws IOException {
        byte[] address;
        int scopeId;
        if (addr instanceof Inet6Address) {
            address = addr.getAddress();
            scopeId = ((Inet6Address)addr).getScopeId();
        }
        else {
            scopeId = 0;
            address = NativeInetAddress.ipv4MappedIpv6Address(addr.getAddress());
        }
        final int res = sendToAddresses(this.fd, memoryAddress, length, address, scopeId, port);
        if (res >= 0) {
            return res;
        }
        return Errors.ioResult("sendToAddresses", res, Socket.CONNECTION_RESET_EXCEPTION_SENDMSG, Socket.SEND_TO_ADDRESSES_CLOSED_CHANNEL_EXCEPTION);
    }
    
    public DatagramSocketAddress recvFrom(final ByteBuffer buf, final int pos, final int limit) throws IOException {
        return recvFrom(this.fd, buf, pos, limit);
    }
    
    public DatagramSocketAddress recvFromAddress(final long memoryAddress, final int pos, final int limit) throws IOException {
        return recvFromAddress(this.fd, memoryAddress, pos, limit);
    }
    
    public boolean connect(final SocketAddress socketAddress) throws IOException {
        int res;
        if (socketAddress instanceof InetSocketAddress) {
            final InetSocketAddress inetSocketAddress = (InetSocketAddress)socketAddress;
            final NativeInetAddress address = NativeInetAddress.newInstance(inetSocketAddress.getAddress());
            res = connect(this.fd, address.address, address.scopeId, inetSocketAddress.getPort());
        }
        else {
            if (!(socketAddress instanceof DomainSocketAddress)) {
                throw new Error("Unexpected SocketAddress implementation " + socketAddress);
            }
            final DomainSocketAddress unixDomainSocketAddress = (DomainSocketAddress)socketAddress;
            res = connectDomainSocket(this.fd, unixDomainSocketAddress.path().getBytes(CharsetUtil.UTF_8));
        }
        if (res < 0) {
            if (res == Errors.ERRNO_EINPROGRESS_NEGATIVE) {
                return false;
            }
            Errors.throwConnectException("connect", Socket.CONNECT_REFUSED_EXCEPTION, res);
        }
        return true;
    }
    
    public boolean finishConnect() throws IOException {
        final int res = finishConnect(this.fd);
        if (res < 0) {
            if (res == Errors.ERRNO_EINPROGRESS_NEGATIVE) {
                return false;
            }
            Errors.throwConnectException("finishConnect", Socket.FINISH_CONNECT_REFUSED_EXCEPTION, res);
        }
        return true;
    }
    
    public void bind(final SocketAddress socketAddress) throws IOException {
        if (socketAddress instanceof InetSocketAddress) {
            final InetSocketAddress addr = (InetSocketAddress)socketAddress;
            final NativeInetAddress address = NativeInetAddress.newInstance(addr.getAddress());
            final int res = bind(this.fd, address.address, address.scopeId, addr.getPort());
            if (res < 0) {
                throw Errors.newIOException("bind", res);
            }
        }
        else {
            if (!(socketAddress instanceof DomainSocketAddress)) {
                throw new Error("Unexpected SocketAddress implementation " + socketAddress);
            }
            final DomainSocketAddress addr2 = (DomainSocketAddress)socketAddress;
            final int res2 = bindDomainSocket(this.fd, addr2.path().getBytes(CharsetUtil.UTF_8));
            if (res2 < 0) {
                throw Errors.newIOException("bind", res2);
            }
        }
    }
    
    public void listen(final int backlog) throws IOException {
        final int res = listen(this.fd, backlog);
        if (res < 0) {
            throw Errors.newIOException("listen", res);
        }
    }
    
    public int accept(final byte[] addr) throws IOException {
        final int res = accept(this.fd, addr);
        if (res >= 0) {
            return res;
        }
        if (res == Errors.ERRNO_EAGAIN_NEGATIVE || res == Errors.ERRNO_EWOULDBLOCK_NEGATIVE) {
            return -1;
        }
        throw Errors.newIOException("accept", res);
    }
    
    public InetSocketAddress remoteAddress() {
        final byte[] addr = remoteAddress(this.fd);
        if (addr == null) {
            return null;
        }
        return NativeInetAddress.address(addr, 0, addr.length);
    }
    
    public InetSocketAddress localAddress() {
        final byte[] addr = localAddress(this.fd);
        if (addr == null) {
            return null;
        }
        return NativeInetAddress.address(addr, 0, addr.length);
    }
    
    public int getReceiveBufferSize() throws IOException {
        return getReceiveBufferSize(this.fd);
    }
    
    public int getSendBufferSize() throws IOException {
        return getSendBufferSize(this.fd);
    }
    
    public boolean isKeepAlive() throws IOException {
        return isKeepAlive(this.fd) != 0;
    }
    
    public boolean isTcpNoDelay() throws IOException {
        return isTcpNoDelay(this.fd) != 0;
    }
    
    public boolean isTcpCork() throws IOException {
        return isTcpCork(this.fd) != 0;
    }
    
    public int getSoLinger() throws IOException {
        return getSoLinger(this.fd);
    }
    
    public int getTcpDeferAccept() throws IOException {
        return getTcpDeferAccept(this.fd);
    }
    
    public boolean isTcpQuickAck() throws IOException {
        return isTcpQuickAck(this.fd) != 0;
    }
    
    public int getSoError() throws IOException {
        return getSoError(this.fd);
    }
    
    public PeerCredentials getPeerCredentials() throws IOException {
        return getPeerCredentials(this.fd);
    }
    
    public void setKeepAlive(final boolean keepAlive) throws IOException {
        setKeepAlive(this.fd, keepAlive ? 1 : 0);
    }
    
    public void setReceiveBufferSize(final int receiveBufferSize) throws IOException {
        setReceiveBufferSize(this.fd, receiveBufferSize);
    }
    
    public void setSendBufferSize(final int sendBufferSize) throws IOException {
        setSendBufferSize(this.fd, sendBufferSize);
    }
    
    public void setTcpNoDelay(final boolean tcpNoDelay) throws IOException {
        setTcpNoDelay(this.fd, tcpNoDelay ? 1 : 0);
    }
    
    public void setTcpCork(final boolean tcpCork) throws IOException {
        setTcpCork(this.fd, tcpCork ? 1 : 0);
    }
    
    public void setSoLinger(final int soLinger) throws IOException {
        setSoLinger(this.fd, soLinger);
    }
    
    public void setTcpDeferAccept(final int deferAccept) throws IOException {
        setTcpDeferAccept(this.fd, deferAccept);
    }
    
    public void setTcpQuickAck(final boolean quickAck) throws IOException {
        setTcpQuickAck(this.fd, quickAck ? 1 : 0);
    }
    
    @Override
    public String toString() {
        return "Socket{fd=" + this.fd + '}';
    }
    
    public static Socket newSocketStream() {
        final int res = newSocketStreamFd();
        if (res < 0) {
            throw new ChannelException(Errors.newIOException("newSocketStream", res));
        }
        return new Socket(res);
    }
    
    public static Socket newSocketDgram() {
        final int res = newSocketDgramFd();
        if (res < 0) {
            throw new ChannelException(Errors.newIOException("newSocketDgram", res));
        }
        return new Socket(res);
    }
    
    public static Socket newSocketDomain() {
        final int res = newSocketDomainFd();
        if (res < 0) {
            throw new ChannelException(Errors.newIOException("newSocketDomain", res));
        }
        return new Socket(res);
    }
    
    private static native int shutdown(final int p0, final boolean p1, final boolean p2);
    
    private static native int connect(final int p0, final byte[] p1, final int p2, final int p3);
    
    private static native int connectDomainSocket(final int p0, final byte[] p1);
    
    private static native int finishConnect(final int p0);
    
    private static native int bind(final int p0, final byte[] p1, final int p2, final int p3);
    
    private static native int bindDomainSocket(final int p0, final byte[] p1);
    
    private static native int listen(final int p0, final int p1);
    
    private static native int accept(final int p0, final byte[] p1);
    
    private static native byte[] remoteAddress(final int p0);
    
    private static native byte[] localAddress(final int p0);
    
    private static native int sendTo(final int p0, final ByteBuffer p1, final int p2, final int p3, final byte[] p4, final int p5, final int p6);
    
    private static native int sendToAddress(final int p0, final long p1, final int p2, final int p3, final byte[] p4, final int p5, final int p6);
    
    private static native int sendToAddresses(final int p0, final long p1, final int p2, final byte[] p3, final int p4, final int p5);
    
    private static native DatagramSocketAddress recvFrom(final int p0, final ByteBuffer p1, final int p2, final int p3) throws IOException;
    
    private static native DatagramSocketAddress recvFromAddress(final int p0, final long p1, final int p2, final int p3) throws IOException;
    
    private static native int newSocketStreamFd();
    
    private static native int newSocketDgramFd();
    
    private static native int newSocketDomainFd();
    
    private static native int getReceiveBufferSize(final int p0) throws IOException;
    
    private static native int getSendBufferSize(final int p0) throws IOException;
    
    private static native int isKeepAlive(final int p0) throws IOException;
    
    private static native int isTcpNoDelay(final int p0) throws IOException;
    
    private static native int isTcpCork(final int p0) throws IOException;
    
    private static native int getSoLinger(final int p0) throws IOException;
    
    private static native int getSoError(final int p0) throws IOException;
    
    private static native int getTcpDeferAccept(final int p0) throws IOException;
    
    private static native int isTcpQuickAck(final int p0) throws IOException;
    
    private static native PeerCredentials getPeerCredentials(final int p0) throws IOException;
    
    private static native void setKeepAlive(final int p0, final int p1) throws IOException;
    
    private static native void setReceiveBufferSize(final int p0, final int p1) throws IOException;
    
    private static native void setSendBufferSize(final int p0, final int p1) throws IOException;
    
    private static native void setTcpNoDelay(final int p0, final int p1) throws IOException;
    
    private static native void setTcpCork(final int p0, final int p1) throws IOException;
    
    private static native void setSoLinger(final int p0, final int p1) throws IOException;
    
    private static native void setTcpDeferAccept(final int p0, final int p1) throws IOException;
    
    private static native void setTcpQuickAck(final int p0, final int p1) throws IOException;
    
    static {
        SHUTDOWN_CLOSED_CHANNEL_EXCEPTION = ThrowableUtil.unknownStackTrace(new ClosedChannelException(), Socket.class, "shutdown(..)");
        SEND_TO_CLOSED_CHANNEL_EXCEPTION = ThrowableUtil.unknownStackTrace(new ClosedChannelException(), Socket.class, "sendTo(..)");
        SEND_TO_ADDRESS_CLOSED_CHANNEL_EXCEPTION = ThrowableUtil.unknownStackTrace(new ClosedChannelException(), Socket.class, "sendToAddress(..)");
        SEND_TO_ADDRESSES_CLOSED_CHANNEL_EXCEPTION = ThrowableUtil.unknownStackTrace(new ClosedChannelException(), Socket.class, "sendToAddresses(..)");
        SEND_TO_CONNECTION_RESET_EXCEPTION = ThrowableUtil.unknownStackTrace(Errors.newConnectionResetException("syscall:sendto", Errors.ERRNO_EPIPE_NEGATIVE), Socket.class, "sendTo(..)");
        SEND_TO_ADDRESS_CONNECTION_RESET_EXCEPTION = ThrowableUtil.unknownStackTrace(Errors.newConnectionResetException("syscall:sendto", Errors.ERRNO_EPIPE_NEGATIVE), Socket.class, "sendToAddress");
        CONNECTION_RESET_EXCEPTION_SENDMSG = ThrowableUtil.unknownStackTrace(Errors.newConnectionResetException("syscall:sendmsg", Errors.ERRNO_EPIPE_NEGATIVE), Socket.class, "sendToAddresses(..)");
        CONNECTION_RESET_SHUTDOWN_EXCEPTION = ThrowableUtil.unknownStackTrace(Errors.newConnectionResetException("syscall:shutdown", Errors.ERRNO_ECONNRESET_NEGATIVE), Socket.class, "shutdown");
        FINISH_CONNECT_REFUSED_EXCEPTION = ThrowableUtil.unknownStackTrace(new Errors.NativeConnectException("syscall:getsockopt", Errors.ERROR_ECONNREFUSED_NEGATIVE), Socket.class, "finishConnect(..)");
        CONNECT_REFUSED_EXCEPTION = ThrowableUtil.unknownStackTrace(new Errors.NativeConnectException("syscall:connect", Errors.ERROR_ECONNREFUSED_NEGATIVE), Socket.class, "connect(..)");
    }
}
