// 
// Decompiled by Procyon v0.6.0
// 

package io.netty.channel.epoll;

import io.netty.util.internal.ThrowableUtil;
import io.netty.util.internal.NativeLibraryLoader;
import io.netty.util.internal.PlatformDependent;
import java.util.Locale;
import io.netty.util.internal.SystemPropertyUtil;
import io.netty.channel.unix.NativeInetAddress;
import java.net.InetAddress;
import io.netty.channel.DefaultFileRegion;
import java.io.IOException;
import io.netty.channel.unix.FileDescriptor;
import java.nio.channels.ClosedChannelException;
import io.netty.channel.unix.Errors;

public final class Native
{
    public static final int EPOLLIN;
    public static final int EPOLLOUT;
    public static final int EPOLLRDHUP;
    public static final int EPOLLET;
    public static final int EPOLLERR;
    public static final int IOV_MAX;
    public static final int UIO_MAX_IOV;
    public static final boolean IS_SUPPORTING_SENDMMSG;
    public static final boolean IS_SUPPORTING_TCP_FASTOPEN;
    public static final long SSIZE_MAX;
    public static final int TCP_MD5SIG_MAXKEYLEN;
    public static final String KERNEL_VERSION;
    private static final Errors.NativeIoException SENDFILE_CONNECTION_RESET_EXCEPTION;
    private static final Errors.NativeIoException SENDMMSG_CONNECTION_RESET_EXCEPTION;
    private static final Errors.NativeIoException SPLICE_CONNECTION_RESET_EXCEPTION;
    private static final ClosedChannelException SENDFILE_CLOSED_CHANNEL_EXCEPTION;
    private static final ClosedChannelException SENDMMSG_CLOSED_CHANNEL_EXCEPTION;
    private static final ClosedChannelException SPLICE_CLOSED_CHANNEL_EXCEPTION;
    
    public static FileDescriptor newEventFd() {
        return new FileDescriptor(eventFd());
    }
    
    private static native int eventFd();
    
    public static native void eventFdWrite(final int p0, final long p1);
    
    public static native void eventFdRead(final int p0);
    
    public static FileDescriptor newEpollCreate() {
        return new FileDescriptor(epollCreate());
    }
    
    private static native int epollCreate();
    
    public static int epollWait(final int efd, final EpollEventArray events, final int timeout) throws IOException {
        final int ready = epollWait0(efd, events.memoryAddress(), events.length(), timeout);
        if (ready < 0) {
            throw Errors.newIOException("epoll_wait", ready);
        }
        return ready;
    }
    
    private static native int epollWait0(final int p0, final long p1, final int p2, final int p3);
    
    public static void epollCtlAdd(final int efd, final int fd, final int flags) throws IOException {
        final int res = epollCtlAdd0(efd, fd, flags);
        if (res < 0) {
            throw Errors.newIOException("epoll_ctl", res);
        }
    }
    
    private static native int epollCtlAdd0(final int p0, final int p1, final int p2);
    
    public static void epollCtlMod(final int efd, final int fd, final int flags) throws IOException {
        final int res = epollCtlMod0(efd, fd, flags);
        if (res < 0) {
            throw Errors.newIOException("epoll_ctl", res);
        }
    }
    
    private static native int epollCtlMod0(final int p0, final int p1, final int p2);
    
    public static void epollCtlDel(final int efd, final int fd) throws IOException {
        final int res = epollCtlDel0(efd, fd);
        if (res < 0) {
            throw Errors.newIOException("epoll_ctl", res);
        }
    }
    
    private static native int epollCtlDel0(final int p0, final int p1);
    
    public static int splice(final int fd, final long offIn, final int fdOut, final long offOut, final long len) throws IOException {
        final int res = splice0(fd, offIn, fdOut, offOut, len);
        if (res >= 0) {
            return res;
        }
        return Errors.ioResult("splice", res, Native.SPLICE_CONNECTION_RESET_EXCEPTION, Native.SPLICE_CLOSED_CHANNEL_EXCEPTION);
    }
    
    private static native int splice0(final int p0, final long p1, final int p2, final long p3, final long p4);
    
    public static long sendfile(final int dest, final DefaultFileRegion src, final long baseOffset, final long offset, final long length) throws IOException {
        src.open();
        final long res = sendfile0(dest, src, baseOffset, offset, length);
        if (res >= 0L) {
            return res;
        }
        return Errors.ioResult("sendfile", (int)res, Native.SENDFILE_CONNECTION_RESET_EXCEPTION, Native.SENDFILE_CLOSED_CHANNEL_EXCEPTION);
    }
    
    private static native long sendfile0(final int p0, final DefaultFileRegion p1, final long p2, final long p3, final long p4) throws IOException;
    
    public static int sendmmsg(final int fd, final NativeDatagramPacketArray.NativeDatagramPacket[] msgs, final int offset, final int len) throws IOException {
        final int res = sendmmsg0(fd, msgs, offset, len);
        if (res >= 0) {
            return res;
        }
        return Errors.ioResult("sendmmsg", res, Native.SENDMMSG_CONNECTION_RESET_EXCEPTION, Native.SENDMMSG_CLOSED_CHANNEL_EXCEPTION);
    }
    
    private static native int sendmmsg0(final int p0, final NativeDatagramPacketArray.NativeDatagramPacket[] p1, final int p2, final int p3);
    
    public static int recvFd(final int fd) throws IOException {
        final int res = recvFd0(fd);
        if (res > 0) {
            return res;
        }
        if (res == 0) {
            return -1;
        }
        if (res == Errors.ERRNO_EAGAIN_NEGATIVE || res == Errors.ERRNO_EWOULDBLOCK_NEGATIVE) {
            return 0;
        }
        throw Errors.newIOException("recvFd", res);
    }
    
    private static native int recvFd0(final int p0);
    
    public static int sendFd(final int socketFd, final int fd) throws IOException {
        final int res = sendFd0(socketFd, fd);
        if (res >= 0) {
            return res;
        }
        if (res == Errors.ERRNO_EAGAIN_NEGATIVE || res == Errors.ERRNO_EWOULDBLOCK_NEGATIVE) {
            return -1;
        }
        throw Errors.newIOException("sendFd", res);
    }
    
    private static native int sendFd0(final int p0, final int p1);
    
    public static native int isReuseAddress(final int p0) throws IOException;
    
    public static native int isReusePort(final int p0) throws IOException;
    
    public static native int getTcpNotSentLowAt(final int p0) throws IOException;
    
    public static native int getTrafficClass(final int p0) throws IOException;
    
    public static native int isBroadcast(final int p0) throws IOException;
    
    public static native int getTcpKeepIdle(final int p0) throws IOException;
    
    public static native int getTcpKeepIntvl(final int p0) throws IOException;
    
    public static native int getTcpKeepCnt(final int p0) throws IOException;
    
    public static native int getTcpUserTimeout(final int p0) throws IOException;
    
    public static native int isIpFreeBind(final int p0) throws IOException;
    
    public static native void setReuseAddress(final int p0, final int p1) throws IOException;
    
    public static native void setReusePort(final int p0, final int p1) throws IOException;
    
    public static native void setTcpFastopen(final int p0, final int p1) throws IOException;
    
    public static native void setTcpNotSentLowAt(final int p0, final int p1) throws IOException;
    
    public static native void setTrafficClass(final int p0, final int p1) throws IOException;
    
    public static native void setBroadcast(final int p0, final int p1) throws IOException;
    
    public static native void setTcpKeepIdle(final int p0, final int p1) throws IOException;
    
    public static native void setTcpKeepIntvl(final int p0, final int p1) throws IOException;
    
    public static native void setTcpKeepCnt(final int p0, final int p1) throws IOException;
    
    public static native void setTcpUserTimeout(final int p0, final int p1) throws IOException;
    
    public static native void setIpFreeBind(final int p0, final int p1) throws IOException;
    
    public static void tcpInfo(final int fd, final EpollTcpInfo info) throws IOException {
        tcpInfo0(fd, info.info);
    }
    
    private static native void tcpInfo0(final int p0, final int[] p1) throws IOException;
    
    public static void setTcpMd5Sig(final int fd, final InetAddress address, final byte[] key) throws IOException {
        final NativeInetAddress a = NativeInetAddress.newInstance(address);
        setTcpMd5Sig0(fd, a.address(), a.scopeId(), key);
    }
    
    private static native void setTcpMd5Sig0(final int p0, final byte[] p1, final int p2, final byte[] p3) throws IOException;
    
    public static native int sizeofEpollEvent();
    
    public static native int offsetofEpollData();
    
    private Native() {
    }
    
    private static void loadNativeLibrary() {
        final String name = SystemPropertyUtil.get("os.name").toLowerCase(Locale.UK).trim();
        if (!name.startsWith("linux")) {
            throw new IllegalStateException("Only supported on Linux");
        }
        NativeLibraryLoader.load(SystemPropertyUtil.get("io.netty.packagePrefix", "").replace('.', '-') + "netty-transport-native-epoll", PlatformDependent.getClassLoader(Native.class));
    }
    
    static {
        try {
            offsetofEpollData();
        }
        catch (final UnsatisfiedLinkError ignore) {
            loadNativeLibrary();
        }
        EPOLLIN = NativeStaticallyReferencedJniMethods.epollin();
        EPOLLOUT = NativeStaticallyReferencedJniMethods.epollout();
        EPOLLRDHUP = NativeStaticallyReferencedJniMethods.epollrdhup();
        EPOLLET = NativeStaticallyReferencedJniMethods.epollet();
        EPOLLERR = NativeStaticallyReferencedJniMethods.epollerr();
        IOV_MAX = NativeStaticallyReferencedJniMethods.iovMax();
        UIO_MAX_IOV = NativeStaticallyReferencedJniMethods.uioMaxIov();
        IS_SUPPORTING_SENDMMSG = NativeStaticallyReferencedJniMethods.isSupportingSendmmsg();
        IS_SUPPORTING_TCP_FASTOPEN = NativeStaticallyReferencedJniMethods.isSupportingTcpFastopen();
        SSIZE_MAX = NativeStaticallyReferencedJniMethods.ssizeMax();
        TCP_MD5SIG_MAXKEYLEN = NativeStaticallyReferencedJniMethods.tcpMd5SigMaxKeyLen();
        KERNEL_VERSION = NativeStaticallyReferencedJniMethods.kernelVersion();
        SENDFILE_CLOSED_CHANNEL_EXCEPTION = ThrowableUtil.unknownStackTrace(new ClosedChannelException(), Native.class, "sendfile(...)");
        SENDMMSG_CLOSED_CHANNEL_EXCEPTION = ThrowableUtil.unknownStackTrace(new ClosedChannelException(), Native.class, "sendmmsg(...)");
        SPLICE_CLOSED_CHANNEL_EXCEPTION = ThrowableUtil.unknownStackTrace(new ClosedChannelException(), Native.class, "splice(...)");
        SENDFILE_CONNECTION_RESET_EXCEPTION = Errors.newConnectionResetException("syscall:sendfile(...)", Errors.ERRNO_EPIPE_NEGATIVE);
        SENDMMSG_CONNECTION_RESET_EXCEPTION = Errors.newConnectionResetException("syscall:sendmmsg(...)", Errors.ERRNO_EPIPE_NEGATIVE);
        SPLICE_CONNECTION_RESET_EXCEPTION = Errors.newConnectionResetException("syscall:splice(...)", Errors.ERRNO_EPIPE_NEGATIVE);
    }
}
