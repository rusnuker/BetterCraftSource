// 
// Decompiled by Procyon v0.6.0
// 

package io.netty.handler.ssl;

import io.netty.util.concurrent.DefaultPromise;
import io.netty.util.internal.ThrowableUtil;
import io.netty.util.internal.logging.InternalLoggerFactory;
import io.netty.channel.ChannelFutureListener;
import java.util.concurrent.ScheduledFuture;
import io.netty.util.concurrent.FutureListener;
import io.netty.util.concurrent.EventExecutor;
import io.netty.util.concurrent.GenericFutureListener;
import io.netty.channel.ChannelPromiseNotifier;
import java.util.Iterator;
import java.util.concurrent.CountDownLatch;
import java.util.ArrayList;
import io.netty.buffer.ByteBufUtil;
import java.util.List;
import java.nio.channels.DatagramChannel;
import java.nio.channels.SocketChannel;
import java.io.IOException;
import io.netty.buffer.CompositeByteBuf;
import io.netty.buffer.ByteBufAllocator;
import javax.net.ssl.SSLEngineResult;
import io.netty.buffer.Unpooled;
import io.netty.util.internal.PlatformDependent;
import io.netty.handler.codec.UnsupportedMessageTypeException;
import io.netty.buffer.ByteBuf;
import java.net.SocketAddress;
import io.netty.channel.ChannelException;
import io.netty.channel.ChannelPromise;
import io.netty.channel.ChannelFuture;
import io.netty.util.concurrent.Future;
import javax.net.ssl.SSLSession;
import java.util.concurrent.TimeUnit;
import io.netty.util.concurrent.ImmediateExecutor;
import io.netty.channel.Channel;
import io.netty.util.concurrent.Promise;
import io.netty.channel.PendingWriteQueue;
import java.nio.ByteBuffer;
import java.util.concurrent.Executor;
import javax.net.ssl.SSLEngine;
import io.netty.channel.ChannelHandlerContext;
import java.nio.channels.ClosedChannelException;
import javax.net.ssl.SSLException;
import java.util.regex.Pattern;
import io.netty.util.internal.logging.InternalLogger;
import io.netty.channel.ChannelOutboundHandler;
import io.netty.handler.codec.ByteToMessageDecoder;

public class SslHandler extends ByteToMessageDecoder implements ChannelOutboundHandler
{
    private static final InternalLogger logger;
    private static final Pattern IGNORABLE_CLASS_IN_STACK;
    private static final Pattern IGNORABLE_ERROR_MESSAGE;
    private static final SSLException SSLENGINE_CLOSED;
    private static final SSLException HANDSHAKE_TIMED_OUT;
    private static final ClosedChannelException CHANNEL_CLOSED;
    private volatile ChannelHandlerContext ctx;
    private final SSLEngine engine;
    private final SslEngineType engineType;
    private final int maxPacketBufferSize;
    private final Executor delegatedTaskExecutor;
    private final ByteBuffer[] singleBuffer;
    private final boolean startTls;
    private boolean sentFirstMessage;
    private boolean flushedBeforeHandshake;
    private boolean readDuringHandshake;
    private PendingWriteQueue pendingUnencryptedWrites;
    private Promise<Channel> handshakePromise;
    private final LazyChannelPromise sslClosePromise;
    private boolean needsFlush;
    private boolean outboundClosed;
    private int packetLength;
    private boolean firedChannelRead;
    private volatile long handshakeTimeoutMillis;
    private volatile long closeNotifyFlushTimeoutMillis;
    private volatile long closeNotifyReadTimeoutMillis;
    
    public SslHandler(final SSLEngine engine) {
        this(engine, false);
    }
    
    public SslHandler(final SSLEngine engine, final boolean startTls) {
        this(engine, startTls, ImmediateExecutor.INSTANCE);
    }
    
    @Deprecated
    public SslHandler(final SSLEngine engine, final Executor delegatedTaskExecutor) {
        this(engine, false, delegatedTaskExecutor);
    }
    
    @Deprecated
    public SslHandler(final SSLEngine engine, final boolean startTls, final Executor delegatedTaskExecutor) {
        this.singleBuffer = new ByteBuffer[1];
        this.handshakePromise = new LazyChannelPromise();
        this.sslClosePromise = new LazyChannelPromise();
        this.handshakeTimeoutMillis = 10000L;
        this.closeNotifyFlushTimeoutMillis = 3000L;
        if (engine == null) {
            throw new NullPointerException("engine");
        }
        if (delegatedTaskExecutor == null) {
            throw new NullPointerException("delegatedTaskExecutor");
        }
        this.engine = engine;
        this.engineType = SslEngineType.forEngine(engine);
        this.delegatedTaskExecutor = delegatedTaskExecutor;
        this.startTls = startTls;
        this.maxPacketBufferSize = engine.getSession().getPacketBufferSize();
        this.setCumulator(this.engineType.cumulator);
    }
    
    public long getHandshakeTimeoutMillis() {
        return this.handshakeTimeoutMillis;
    }
    
    public void setHandshakeTimeout(final long handshakeTimeout, final TimeUnit unit) {
        if (unit == null) {
            throw new NullPointerException("unit");
        }
        this.setHandshakeTimeoutMillis(unit.toMillis(handshakeTimeout));
    }
    
    public void setHandshakeTimeoutMillis(final long handshakeTimeoutMillis) {
        if (handshakeTimeoutMillis < 0L) {
            throw new IllegalArgumentException("handshakeTimeoutMillis: " + handshakeTimeoutMillis + " (expected: >= 0)");
        }
        this.handshakeTimeoutMillis = handshakeTimeoutMillis;
    }
    
    @Deprecated
    public long getCloseNotifyTimeoutMillis() {
        return this.getCloseNotifyFlushTimeoutMillis();
    }
    
    @Deprecated
    public void setCloseNotifyTimeout(final long closeNotifyTimeout, final TimeUnit unit) {
        this.setCloseNotifyFlushTimeout(closeNotifyTimeout, unit);
    }
    
    @Deprecated
    public void setCloseNotifyTimeoutMillis(final long closeNotifyFlushTimeoutMillis) {
        this.setCloseNotifyFlushTimeoutMillis(closeNotifyFlushTimeoutMillis);
    }
    
    public final long getCloseNotifyFlushTimeoutMillis() {
        return this.closeNotifyFlushTimeoutMillis;
    }
    
    public final void setCloseNotifyFlushTimeout(final long closeNotifyFlushTimeout, final TimeUnit unit) {
        this.setCloseNotifyFlushTimeoutMillis(unit.toMillis(closeNotifyFlushTimeout));
    }
    
    public final void setCloseNotifyFlushTimeoutMillis(final long closeNotifyFlushTimeoutMillis) {
        if (closeNotifyFlushTimeoutMillis < 0L) {
            throw new IllegalArgumentException("closeNotifyFlushTimeoutMillis: " + closeNotifyFlushTimeoutMillis + " (expected: >= 0)");
        }
        this.closeNotifyFlushTimeoutMillis = closeNotifyFlushTimeoutMillis;
    }
    
    public final long getCloseNotifyReadTimeoutMillis() {
        return this.closeNotifyReadTimeoutMillis;
    }
    
    public final void setCloseNotifyReadTimeout(final long closeNotifyReadTimeout, final TimeUnit unit) {
        this.setCloseNotifyReadTimeoutMillis(unit.toMillis(closeNotifyReadTimeout));
    }
    
    public final void setCloseNotifyReadTimeoutMillis(final long closeNotifyReadTimeoutMillis) {
        if (closeNotifyReadTimeoutMillis < 0L) {
            throw new IllegalArgumentException("closeNotifyReadTimeoutMillis: " + closeNotifyReadTimeoutMillis + " (expected: >= 0)");
        }
        this.closeNotifyReadTimeoutMillis = closeNotifyReadTimeoutMillis;
    }
    
    public SSLEngine engine() {
        return this.engine;
    }
    
    public String applicationProtocol() {
        final SSLSession sess = this.engine().getSession();
        if (!(sess instanceof ApplicationProtocolAccessor)) {
            return null;
        }
        return ((ApplicationProtocolAccessor)sess).getApplicationProtocol();
    }
    
    public Future<Channel> handshakeFuture() {
        return this.handshakePromise;
    }
    
    @Deprecated
    public ChannelFuture close() {
        return this.close(this.ctx.newPromise());
    }
    
    @Deprecated
    public ChannelFuture close(final ChannelPromise promise) {
        final ChannelHandlerContext ctx = this.ctx;
        ctx.executor().execute(new Runnable() {
            @Override
            public void run() {
                SslHandler.this.outboundClosed = true;
                SslHandler.this.engine.closeOutbound();
                try {
                    SslHandler.this.flush(ctx, promise);
                }
                catch (final Exception e) {
                    if (!promise.tryFailure(e)) {
                        SslHandler.logger.warn("{} flush() raised a masked exception.", ctx.channel(), e);
                    }
                }
            }
        });
        return promise;
    }
    
    public Future<Channel> sslCloseFuture() {
        return this.sslClosePromise;
    }
    
    public void handlerRemoved0(final ChannelHandlerContext ctx) throws Exception {
        if (!this.pendingUnencryptedWrites.isEmpty()) {
            this.pendingUnencryptedWrites.removeAndFailAll(new ChannelException("Pending write on removal of SslHandler"));
        }
        if (this.engine instanceof ReferenceCountedOpenSslEngine) {
            ((ReferenceCountedOpenSslEngine)this.engine).release();
        }
    }
    
    @Override
    public void bind(final ChannelHandlerContext ctx, final SocketAddress localAddress, final ChannelPromise promise) throws Exception {
        ctx.bind(localAddress, promise);
    }
    
    @Override
    public void connect(final ChannelHandlerContext ctx, final SocketAddress remoteAddress, final SocketAddress localAddress, final ChannelPromise promise) throws Exception {
        ctx.connect(remoteAddress, localAddress, promise);
    }
    
    @Override
    public void deregister(final ChannelHandlerContext ctx, final ChannelPromise promise) throws Exception {
        ctx.deregister(promise);
    }
    
    @Override
    public void disconnect(final ChannelHandlerContext ctx, final ChannelPromise promise) throws Exception {
        this.closeOutboundAndChannel(ctx, promise, true);
    }
    
    @Override
    public void close(final ChannelHandlerContext ctx, final ChannelPromise promise) throws Exception {
        this.closeOutboundAndChannel(ctx, promise, false);
    }
    
    @Override
    public void read(final ChannelHandlerContext ctx) throws Exception {
        if (!this.handshakePromise.isDone()) {
            this.readDuringHandshake = true;
        }
        ctx.read();
    }
    
    @Override
    public void write(final ChannelHandlerContext ctx, final Object msg, final ChannelPromise promise) throws Exception {
        if (!(msg instanceof ByteBuf)) {
            promise.setFailure((Throwable)new UnsupportedMessageTypeException(msg, (Class<?>[])new Class[] { ByteBuf.class }));
            return;
        }
        this.pendingUnencryptedWrites.add(msg, promise);
    }
    
    @Override
    public void flush(final ChannelHandlerContext ctx) throws Exception {
        if (this.startTls && !this.sentFirstMessage) {
            this.sentFirstMessage = true;
            this.pendingUnencryptedWrites.removeAndWriteAll();
            this.forceFlush(ctx);
            return;
        }
        try {
            this.wrapAndFlush(ctx);
        }
        catch (final Throwable cause) {
            this.setHandshakeFailure(ctx, cause);
            PlatformDependent.throwException(cause);
        }
    }
    
    private void wrapAndFlush(final ChannelHandlerContext ctx) throws SSLException {
        if (this.pendingUnencryptedWrites.isEmpty()) {
            this.pendingUnencryptedWrites.add(Unpooled.EMPTY_BUFFER, ctx.newPromise());
        }
        if (!this.handshakePromise.isDone()) {
            this.flushedBeforeHandshake = true;
        }
        try {
            this.wrap(ctx, false);
        }
        finally {
            this.forceFlush(ctx);
        }
    }
    
    private void wrap(final ChannelHandlerContext ctx, final boolean inUnwrap) throws SSLException {
        ByteBuf out = null;
        ChannelPromise promise = null;
        final ByteBufAllocator alloc = ctx.alloc();
        boolean needUnwrap = false;
        try {
            while (!ctx.isRemoved()) {
                final Object msg = this.pendingUnencryptedWrites.current();
                if (msg == null) {
                    break;
                }
                final ByteBuf buf = (ByteBuf)msg;
                if (out == null) {
                    out = this.allocateOutNetBuf(ctx, buf.readableBytes(), buf.nioBufferCount());
                }
                final SSLEngineResult result = this.wrap(alloc, this.engine, buf, out);
                if (result.getStatus() == SSLEngineResult.Status.CLOSED) {
                    this.pendingUnencryptedWrites.removeAndFailAll(SslHandler.SSLENGINE_CLOSED);
                    return;
                }
                if (!buf.isReadable()) {
                    promise = this.pendingUnencryptedWrites.remove();
                }
                else {
                    promise = null;
                }
                switch (result.getHandshakeStatus()) {
                    case NEED_TASK: {
                        this.runDelegatedTasks();
                        continue;
                    }
                    case FINISHED: {
                        this.setHandshakeSuccess();
                    }
                    case NOT_HANDSHAKING: {
                        this.setHandshakeSuccessIfStillHandshaking();
                    }
                    case NEED_WRAP: {
                        this.finishWrap(ctx, out, promise, inUnwrap, false);
                        promise = null;
                        out = null;
                        continue;
                    }
                    case NEED_UNWRAP: {
                        needUnwrap = true;
                        return;
                    }
                    default: {
                        throw new IllegalStateException("Unknown handshake status: " + result.getHandshakeStatus());
                    }
                }
            }
        }
        finally {
            this.finishWrap(ctx, out, promise, inUnwrap, needUnwrap);
        }
    }
    
    private void finishWrap(final ChannelHandlerContext ctx, ByteBuf out, final ChannelPromise promise, final boolean inUnwrap, final boolean needUnwrap) {
        if (out == null) {
            out = Unpooled.EMPTY_BUFFER;
        }
        else if (!out.isReadable()) {
            out.release();
            out = Unpooled.EMPTY_BUFFER;
        }
        if (promise != null) {
            ctx.write(out, promise);
        }
        else {
            ctx.write(out);
        }
        if (inUnwrap) {
            this.needsFlush = true;
        }
        if (needUnwrap) {
            this.readIfNeeded(ctx);
        }
    }
    
    private boolean wrapNonAppData(final ChannelHandlerContext ctx, final boolean inUnwrap) throws SSLException {
        ByteBuf out = null;
        final ByteBufAllocator alloc = ctx.alloc();
        try {
            while (!ctx.isRemoved()) {
                if (out == null) {
                    out = this.allocateOutNetBuf(ctx, 2048, 1);
                }
                final SSLEngineResult result = this.wrap(alloc, this.engine, Unpooled.EMPTY_BUFFER, out);
                if (result.bytesProduced() > 0) {
                    ctx.write(out);
                    if (inUnwrap) {
                        this.needsFlush = true;
                    }
                    out = null;
                }
                switch (result.getHandshakeStatus()) {
                    case FINISHED: {
                        this.setHandshakeSuccess();
                        return false;
                    }
                    case NEED_TASK: {
                        this.runDelegatedTasks();
                        break;
                    }
                    case NEED_UNWRAP: {
                        if (!inUnwrap) {
                            this.unwrapNonAppData(ctx);
                            break;
                        }
                        break;
                    }
                    case NEED_WRAP: {
                        break;
                    }
                    case NOT_HANDSHAKING: {
                        this.setHandshakeSuccessIfStillHandshaking();
                        if (!inUnwrap) {
                            this.unwrapNonAppData(ctx);
                        }
                        return true;
                    }
                    default: {
                        throw new IllegalStateException("Unknown handshake status: " + result.getHandshakeStatus());
                    }
                }
                if (result.bytesProduced() == 0) {
                    break;
                }
                if (result.bytesConsumed() == 0 && result.getHandshakeStatus() == SSLEngineResult.HandshakeStatus.NOT_HANDSHAKING) {
                    break;
                }
            }
        }
        finally {
            if (out != null) {
                out.release();
            }
        }
        return false;
    }
    
    private SSLEngineResult wrap(final ByteBufAllocator alloc, final SSLEngine engine, final ByteBuf in, final ByteBuf out) throws SSLException {
        ByteBuf newDirectIn = null;
        try {
            final int readerIndex = in.readerIndex();
            final int readableBytes = in.readableBytes();
            ByteBuffer[] in2;
            if (in.isDirect() || !this.engineType.wantsDirectBuffer) {
                if (!(in instanceof CompositeByteBuf) && in.nioBufferCount() == 1) {
                    in2 = this.singleBuffer;
                    in2[0] = in.internalNioBuffer(readerIndex, readableBytes);
                }
                else {
                    in2 = in.nioBuffers();
                }
            }
            else {
                newDirectIn = alloc.directBuffer(readableBytes);
                newDirectIn.writeBytes(in, readerIndex, readableBytes);
                in2 = this.singleBuffer;
                in2[0] = newDirectIn.internalNioBuffer(newDirectIn.readerIndex(), readableBytes);
            }
            while (true) {
                final ByteBuffer out2 = out.nioBuffer(out.writerIndex(), out.writableBytes());
                final SSLEngineResult result = engine.wrap(in2, out2);
                in.skipBytes(result.bytesConsumed());
                out.writerIndex(out.writerIndex() + result.bytesProduced());
                switch (result.getStatus()) {
                    case BUFFER_OVERFLOW: {
                        out.ensureWritable(this.maxPacketBufferSize);
                        continue;
                    }
                    default: {
                        return result;
                    }
                }
            }
        }
        finally {
            this.singleBuffer[0] = null;
            if (newDirectIn != null) {
                newDirectIn.release();
            }
        }
    }
    
    @Override
    public void channelInactive(final ChannelHandlerContext ctx) throws Exception {
        this.setHandshakeFailure(ctx, SslHandler.CHANNEL_CLOSED, !this.outboundClosed);
        this.notifyClosePromise(SslHandler.CHANNEL_CLOSED);
        super.channelInactive(ctx);
    }
    
    @Override
    public void exceptionCaught(final ChannelHandlerContext ctx, final Throwable cause) throws Exception {
        if (this.ignoreException(cause)) {
            if (SslHandler.logger.isDebugEnabled()) {
                SslHandler.logger.debug("{} Swallowing a harmless 'connection reset by peer / broken pipe' error that occurred while writing close_notify in response to the peer's close_notify", ctx.channel(), cause);
            }
            if (ctx.channel().isActive()) {
                ctx.close();
            }
        }
        else {
            ctx.fireExceptionCaught(cause);
        }
    }
    
    private boolean ignoreException(final Throwable t) {
        if (!(t instanceof SSLException) && t instanceof IOException && this.sslClosePromise.isDone()) {
            final String message = t.getMessage();
            if (message != null && SslHandler.IGNORABLE_ERROR_MESSAGE.matcher(message).matches()) {
                return true;
            }
            final StackTraceElement[] stackTrace;
            final StackTraceElement[] elements = stackTrace = t.getStackTrace();
            for (final StackTraceElement element : stackTrace) {
                final String classname = element.getClassName();
                final String methodname = element.getMethodName();
                if (!classname.startsWith("io.netty.")) {
                    if ("read".equals(methodname)) {
                        if (SslHandler.IGNORABLE_CLASS_IN_STACK.matcher(classname).matches()) {
                            return true;
                        }
                        try {
                            final Class<?> clazz = PlatformDependent.getClassLoader(this.getClass()).loadClass(classname);
                            if (SocketChannel.class.isAssignableFrom(clazz) || DatagramChannel.class.isAssignableFrom(clazz)) {
                                return true;
                            }
                            if (PlatformDependent.javaVersion() >= 7 && "com.sun.nio.sctp.SctpChannel".equals(clazz.getSuperclass().getName())) {
                                return true;
                            }
                        }
                        catch (final Throwable cause) {
                            SslHandler.logger.debug("Unexpected exception while loading class {} classname {}", this.getClass(), classname, cause);
                        }
                    }
                }
            }
        }
        return false;
    }
    
    public static boolean isEncrypted(final ByteBuf buffer) {
        if (buffer.readableBytes() < 5) {
            throw new IllegalArgumentException("buffer must have at least 5 readable bytes");
        }
        return SslUtils.getEncryptedPacketLength(buffer, buffer.readerIndex()) != -2;
    }
    
    @Override
    protected void decode(final ChannelHandlerContext ctx, final ByteBuf in, final List<Object> out) throws SSLException {
        final int startOffset = in.readerIndex();
        final int endOffset = in.writerIndex();
        int offset = startOffset;
        int totalLength = 0;
        if (this.packetLength > 0) {
            if (endOffset - startOffset < this.packetLength) {
                return;
            }
            offset += this.packetLength;
            totalLength = this.packetLength;
            this.packetLength = 0;
        }
        boolean nonSslRecord = false;
        while (totalLength < 16474) {
            final int readableBytes = endOffset - offset;
            if (readableBytes < 5) {
                break;
            }
            final int packetLength = SslUtils.getEncryptedPacketLength(in, offset);
            if (packetLength == -2) {
                nonSslRecord = true;
                break;
            }
            assert packetLength > 0;
            if (packetLength > readableBytes) {
                this.packetLength = packetLength;
                break;
            }
            final int newTotalLength = totalLength + packetLength;
            if (newTotalLength > 16474) {
                break;
            }
            offset += packetLength;
            totalLength = newTotalLength;
        }
        if (totalLength > 0) {
            in.skipBytes(totalLength);
            try {
                this.firedChannelRead = (this.unwrap(ctx, in, startOffset, totalLength) || this.firedChannelRead);
            }
            catch (final Throwable cause) {
                try {
                    this.wrapAndFlush(ctx);
                }
                catch (final SSLException ex) {
                    SslHandler.logger.debug("SSLException during trying to call SSLEngine.wrap(...) because of an previous SSLException, ignoring...", ex);
                }
                finally {
                    this.setHandshakeFailure(ctx, cause);
                }
                PlatformDependent.throwException(cause);
            }
        }
        if (nonSslRecord) {
            final NotSslRecordException e = new NotSslRecordException("not an SSL/TLS record: " + ByteBufUtil.hexDump(in));
            in.skipBytes(in.readableBytes());
            this.setHandshakeFailure(ctx, e);
            throw e;
        }
    }
    
    @Override
    public void channelReadComplete(final ChannelHandlerContext ctx) throws Exception {
        this.discardSomeReadBytes();
        this.flushIfNeeded(ctx);
        this.readIfNeeded(ctx);
        this.firedChannelRead = false;
        ctx.fireChannelReadComplete();
    }
    
    private void readIfNeeded(final ChannelHandlerContext ctx) {
        if (!ctx.channel().config().isAutoRead() && (!this.firedChannelRead || !this.handshakePromise.isDone())) {
            ctx.read();
        }
    }
    
    private void flushIfNeeded(final ChannelHandlerContext ctx) {
        if (this.needsFlush) {
            this.forceFlush(ctx);
        }
    }
    
    private void unwrapNonAppData(final ChannelHandlerContext ctx) throws SSLException {
        this.unwrap(ctx, Unpooled.EMPTY_BUFFER, 0, 0);
    }
    
    private boolean unwrap(final ChannelHandlerContext ctx, final ByteBuf packet, int offset, int length) throws SSLException {
        boolean decoded = false;
        boolean wrapLater = false;
        boolean notifyClosure = false;
        ByteBuf decodeOut = this.allocate(ctx, length);
        try {
        Label_0394:
            while (!ctx.isRemoved()) {
                final SSLEngineResult result = this.engineType.unwrap(this, packet, offset, length, decodeOut);
                final SSLEngineResult.Status status = result.getStatus();
                final SSLEngineResult.HandshakeStatus handshakeStatus = result.getHandshakeStatus();
                final int produced = result.bytesProduced();
                final int consumed = result.bytesConsumed();
                offset += consumed;
                length -= consumed;
                switch (status) {
                    case BUFFER_OVERFLOW: {
                        final int readableBytes = decodeOut.readableBytes();
                        int bufferSize = this.engine.getSession().getApplicationBufferSize() - readableBytes;
                        if (readableBytes > 0) {
                            decoded = true;
                            ctx.fireChannelRead((Object)decodeOut);
                            decodeOut = null;
                            if (bufferSize <= 0) {
                                bufferSize = this.engine.getSession().getApplicationBufferSize();
                            }
                        }
                        else {
                            decodeOut.release();
                            decodeOut = null;
                        }
                        decodeOut = this.allocate(ctx, bufferSize);
                        continue;
                    }
                    case CLOSED: {
                        notifyClosure = true;
                        break;
                    }
                }
                switch (handshakeStatus) {
                    case NEED_UNWRAP: {
                        break;
                    }
                    case NEED_WRAP: {
                        if (this.wrapNonAppData(ctx, true) && length == 0) {
                            break Label_0394;
                        }
                        break;
                    }
                    case NEED_TASK: {
                        this.runDelegatedTasks();
                        break;
                    }
                    case FINISHED: {
                        this.setHandshakeSuccess();
                        wrapLater = true;
                        break;
                    }
                    case NOT_HANDSHAKING: {
                        if (this.setHandshakeSuccessIfStillHandshaking()) {
                            wrapLater = true;
                            continue;
                        }
                        if (this.flushedBeforeHandshake) {
                            this.flushedBeforeHandshake = false;
                            wrapLater = true;
                        }
                        if (length == 0) {
                            break Label_0394;
                        }
                        break;
                    }
                    default: {
                        throw new IllegalStateException("unknown handshake status: " + handshakeStatus);
                    }
                }
                if (status == SSLEngineResult.Status.BUFFER_UNDERFLOW || (consumed == 0 && produced == 0)) {
                    if (handshakeStatus == SSLEngineResult.HandshakeStatus.NEED_UNWRAP) {
                        this.readIfNeeded(ctx);
                        break;
                    }
                    break;
                }
            }
            if (wrapLater) {
                this.wrap(ctx, true);
            }
            if (notifyClosure) {
                this.notifyClosePromise(null);
            }
        }
        finally {
            if (decodeOut != null) {
                if (decodeOut.isReadable()) {
                    decoded = true;
                    ctx.fireChannelRead((Object)decodeOut);
                }
                else {
                    decodeOut.release();
                }
            }
        }
        return decoded;
    }
    
    private static ByteBuffer toByteBuffer(final ByteBuf out, final int index, final int len) {
        return (out.nioBufferCount() == 1) ? out.internalNioBuffer(index, len) : out.nioBuffer(index, len);
    }
    
    private void runDelegatedTasks() {
        if (this.delegatedTaskExecutor == ImmediateExecutor.INSTANCE) {
            while (true) {
                final Runnable task = this.engine.getDelegatedTask();
                if (task == null) {
                    break;
                }
                task.run();
            }
        }
        else {
            final List<Runnable> tasks = new ArrayList<Runnable>(2);
            while (true) {
                final Runnable task2 = this.engine.getDelegatedTask();
                if (task2 == null) {
                    break;
                }
                tasks.add(task2);
            }
            if (tasks.isEmpty()) {
                return;
            }
            final CountDownLatch latch = new CountDownLatch(1);
            this.delegatedTaskExecutor.execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        for (final Runnable task : tasks) {
                            task.run();
                        }
                    }
                    catch (final Exception e) {
                        SslHandler.this.ctx.fireExceptionCaught((Throwable)e);
                    }
                    finally {
                        latch.countDown();
                    }
                }
            });
            boolean interrupted = false;
            while (latch.getCount() != 0L) {
                try {
                    latch.await();
                }
                catch (final InterruptedException e) {
                    interrupted = true;
                }
            }
            if (interrupted) {
                Thread.currentThread().interrupt();
            }
        }
    }
    
    private boolean setHandshakeSuccessIfStillHandshaking() {
        if (!this.handshakePromise.isDone()) {
            this.setHandshakeSuccess();
            return true;
        }
        return false;
    }
    
    private void setHandshakeSuccess() {
        this.handshakePromise.trySuccess(this.ctx.channel());
        if (SslHandler.logger.isDebugEnabled()) {
            SslHandler.logger.debug("{} HANDSHAKEN: {}", this.ctx.channel(), this.engine.getSession().getCipherSuite());
        }
        this.ctx.fireUserEventTriggered((Object)SslHandshakeCompletionEvent.SUCCESS);
        if (this.readDuringHandshake && !this.ctx.channel().config().isAutoRead()) {
            this.readDuringHandshake = false;
            this.ctx.read();
        }
    }
    
    private void setHandshakeFailure(final ChannelHandlerContext ctx, final Throwable cause) {
        this.setHandshakeFailure(ctx, cause, true);
    }
    
    private void setHandshakeFailure(final ChannelHandlerContext ctx, final Throwable cause, final boolean closeInbound) {
        try {
            this.engine.closeOutbound();
            if (closeInbound) {
                try {
                    this.engine.closeInbound();
                }
                catch (final SSLException e) {
                    final String msg = e.getMessage();
                    if (msg == null || !msg.contains("possible truncation attack")) {
                        SslHandler.logger.debug("{} SSLEngine.closeInbound() raised an exception.", ctx.channel(), e);
                    }
                }
            }
            this.notifyHandshakeFailure(cause);
        }
        finally {
            this.pendingUnencryptedWrites.removeAndFailAll(cause);
        }
    }
    
    private void notifyHandshakeFailure(final Throwable cause) {
        if (this.handshakePromise.tryFailure(cause)) {
            SslUtils.notifyHandshakeFailure(this.ctx, cause);
        }
    }
    
    private void notifyClosePromise(final Throwable cause) {
        if (cause == null) {
            if (this.sslClosePromise.trySuccess(this.ctx.channel())) {
                this.ctx.fireUserEventTriggered((Object)SslCloseCompletionEvent.SUCCESS);
            }
        }
        else if (this.sslClosePromise.tryFailure(cause)) {
            this.ctx.fireUserEventTriggered((Object)new SslCloseCompletionEvent(cause));
        }
    }
    
    private void closeOutboundAndChannel(final ChannelHandlerContext ctx, final ChannelPromise promise, final boolean disconnect) throws Exception {
        if (!ctx.channel().isActive()) {
            if (disconnect) {
                ctx.disconnect(promise);
            }
            else {
                ctx.close(promise);
            }
            return;
        }
        this.outboundClosed = true;
        this.engine.closeOutbound();
        final ChannelPromise closeNotifyPromise = ctx.newPromise();
        try {
            this.flush(ctx, closeNotifyPromise);
        }
        finally {
            this.safeClose(ctx, closeNotifyPromise, ctx.newPromise().addListener((GenericFutureListener<? extends Future<? super Void>>)new ChannelPromiseNotifier(false, new ChannelPromise[] { promise })));
        }
    }
    
    private void flush(final ChannelHandlerContext ctx, final ChannelPromise promise) throws Exception {
        this.pendingUnencryptedWrites.add(Unpooled.EMPTY_BUFFER, promise);
        this.flush(ctx);
    }
    
    @Override
    public void handlerAdded(final ChannelHandlerContext ctx) throws Exception {
        this.ctx = ctx;
        this.pendingUnencryptedWrites = new PendingWriteQueue(ctx);
        if (ctx.channel().isActive() && this.engine.getUseClientMode()) {
            this.handshake(null);
        }
    }
    
    public Future<Channel> renegotiate() {
        final ChannelHandlerContext ctx = this.ctx;
        if (ctx == null) {
            throw new IllegalStateException();
        }
        return this.renegotiate(ctx.executor().newPromise());
    }
    
    public Future<Channel> renegotiate(final Promise<Channel> promise) {
        if (promise == null) {
            throw new NullPointerException("promise");
        }
        final ChannelHandlerContext ctx = this.ctx;
        if (ctx == null) {
            throw new IllegalStateException();
        }
        final EventExecutor executor = ctx.executor();
        if (!executor.inEventLoop()) {
            executor.execute(new Runnable() {
                @Override
                public void run() {
                    SslHandler.this.handshake(promise);
                }
            });
            return promise;
        }
        this.handshake(promise);
        return promise;
    }
    
    private void handshake(final Promise<Channel> newHandshakePromise) {
        Promise<Channel> p;
        if (newHandshakePromise != null) {
            final Promise<Channel> oldHandshakePromise = this.handshakePromise;
            if (!oldHandshakePromise.isDone()) {
                oldHandshakePromise.addListener((GenericFutureListener<? extends Future<? super Channel>>)new FutureListener<Channel>() {
                    @Override
                    public void operationComplete(final Future<Channel> future) throws Exception {
                        if (future.isSuccess()) {
                            newHandshakePromise.setSuccess(future.getNow());
                        }
                        else {
                            newHandshakePromise.setFailure(future.cause());
                        }
                    }
                });
                return;
            }
            p = newHandshakePromise;
            this.handshakePromise = newHandshakePromise;
        }
        else {
            if (this.engine.getHandshakeStatus() != SSLEngineResult.HandshakeStatus.NOT_HANDSHAKING) {
                return;
            }
            p = this.handshakePromise;
            assert !p.isDone();
        }
        final ChannelHandlerContext ctx = this.ctx;
        try {
            this.engine.beginHandshake();
            this.wrapNonAppData(ctx, false);
        }
        catch (final Throwable e) {
            this.setHandshakeFailure(ctx, e);
        }
        finally {
            this.forceFlush(ctx);
        }
        final long handshakeTimeoutMillis = this.handshakeTimeoutMillis;
        if (handshakeTimeoutMillis <= 0L || p.isDone()) {
            return;
        }
        final ScheduledFuture<?> timeoutFuture = ctx.executor().schedule((Runnable)new Runnable() {
            @Override
            public void run() {
                if (p.isDone()) {
                    return;
                }
                SslHandler.this.notifyHandshakeFailure(SslHandler.HANDSHAKE_TIMED_OUT);
            }
        }, handshakeTimeoutMillis, TimeUnit.MILLISECONDS);
        p.addListener((GenericFutureListener<? extends Future<? super Channel>>)new FutureListener<Channel>() {
            @Override
            public void operationComplete(final Future<Channel> f) throws Exception {
                timeoutFuture.cancel(false);
            }
        });
    }
    
    private void forceFlush(final ChannelHandlerContext ctx) {
        this.needsFlush = false;
        ctx.flush();
    }
    
    @Override
    public void channelActive(final ChannelHandlerContext ctx) throws Exception {
        if (!this.startTls && this.engine.getUseClientMode()) {
            this.handshake(null);
        }
        ctx.fireChannelActive();
    }
    
    private void safeClose(final ChannelHandlerContext ctx, final ChannelFuture flushFuture, final ChannelPromise promise) {
        if (!ctx.channel().isActive()) {
            ctx.close(promise);
            return;
        }
        ScheduledFuture<?> timeoutFuture;
        if (!flushFuture.isDone()) {
            final long closeNotifyTimeout = this.closeNotifyFlushTimeoutMillis;
            if (closeNotifyTimeout > 0L) {
                timeoutFuture = ctx.executor().schedule((Runnable)new Runnable() {
                    @Override
                    public void run() {
                        if (!flushFuture.isDone()) {
                            SslHandler.logger.warn("{} Last write attempt timed out; force-closing the connection.", ctx.channel());
                            addCloseListener(ctx.close(ctx.newPromise()), promise);
                        }
                    }
                }, closeNotifyTimeout, TimeUnit.MILLISECONDS);
            }
            else {
                timeoutFuture = null;
            }
        }
        else {
            timeoutFuture = null;
        }
        flushFuture.addListener((GenericFutureListener<? extends Future<? super Void>>)new ChannelFutureListener() {
            @Override
            public void operationComplete(final ChannelFuture f) throws Exception {
                if (timeoutFuture != null) {
                    timeoutFuture.cancel(false);
                }
                final long closeNotifyReadTimeout = SslHandler.this.closeNotifyReadTimeoutMillis;
                if (closeNotifyReadTimeout <= 0L) {
                    addCloseListener(ctx.close(ctx.newPromise()), promise);
                }
                else {
                    ScheduledFuture<?> closeNotifyReadTimeoutFuture;
                    if (!SslHandler.this.sslClosePromise.isDone()) {
                        closeNotifyReadTimeoutFuture = ctx.executor().schedule((Runnable)new Runnable() {
                            @Override
                            public void run() {
                                if (!SslHandler.this.sslClosePromise.isDone()) {
                                    SslHandler.logger.debug("{} did not receive close_notify in {}ms; force-closing the connection.", ctx.channel(), closeNotifyReadTimeout);
                                    addCloseListener(ctx.close(ctx.newPromise()), promise);
                                }
                            }
                        }, closeNotifyReadTimeout, TimeUnit.MILLISECONDS);
                    }
                    else {
                        closeNotifyReadTimeoutFuture = null;
                    }
                    SslHandler.this.sslClosePromise.addListener(new FutureListener<Channel>() {
                        @Override
                        public void operationComplete(final Future<Channel> future) throws Exception {
                            if (closeNotifyReadTimeoutFuture != null) {
                                closeNotifyReadTimeoutFuture.cancel(false);
                            }
                            addCloseListener(ctx.close(ctx.newPromise()), promise);
                        }
                    });
                }
            }
        });
    }
    
    private static void addCloseListener(final ChannelFuture future, final ChannelPromise promise) {
        future.addListener((GenericFutureListener<? extends Future<? super Void>>)new ChannelPromiseNotifier(false, new ChannelPromise[] { promise }));
    }
    
    private ByteBuf allocate(final ChannelHandlerContext ctx, final int capacity) {
        final ByteBufAllocator alloc = ctx.alloc();
        if (this.engineType.wantsDirectBuffer) {
            return alloc.directBuffer(capacity);
        }
        return alloc.buffer(capacity);
    }
    
    private ByteBuf allocateOutNetBuf(final ChannelHandlerContext ctx, final int pendingBytes, final int numComponents) {
        return this.allocate(ctx, this.engineType.calculateOutNetBufSize(this, pendingBytes, numComponents));
    }
    
    static {
        logger = InternalLoggerFactory.getInstance(SslHandler.class);
        IGNORABLE_CLASS_IN_STACK = Pattern.compile("^.*(?:Socket|Datagram|Sctp|Udt)Channel.*$");
        IGNORABLE_ERROR_MESSAGE = Pattern.compile("^.*(?:connection.*(?:reset|closed|abort|broken)|broken.*pipe).*$", 2);
        SSLENGINE_CLOSED = ThrowableUtil.unknownStackTrace(new SSLException("SSLEngine closed already"), SslHandler.class, "wrap(...)");
        HANDSHAKE_TIMED_OUT = ThrowableUtil.unknownStackTrace(new SSLException("handshake timed out"), SslHandler.class, "handshake(...)");
        CHANNEL_CLOSED = ThrowableUtil.unknownStackTrace(new ClosedChannelException(), SslHandler.class, "channelInactive(...)");
    }
    
    private enum SslEngineType
    {
        TCNATIVE(true, ByteToMessageDecoder.COMPOSITE_CUMULATOR) {
            @Override
            SSLEngineResult unwrap(final SslHandler handler, final ByteBuf in, final int readerIndex, final int len, final ByteBuf out) throws SSLException {
                final int nioBufferCount = in.nioBufferCount();
                final int writerIndex = out.writerIndex();
                SSLEngineResult result;
                if (nioBufferCount > 1) {
                    final ReferenceCountedOpenSslEngine opensslEngine = (ReferenceCountedOpenSslEngine)handler.engine;
                    try {
                        handler.singleBuffer[0] = toByteBuffer(out, writerIndex, out.writableBytes());
                        result = opensslEngine.unwrap(in.nioBuffers(readerIndex, len), handler.singleBuffer);
                    }
                    finally {
                        handler.singleBuffer[0] = null;
                    }
                }
                else {
                    result = handler.engine.unwrap(toByteBuffer(in, readerIndex, len), toByteBuffer(out, writerIndex, out.writableBytes()));
                }
                out.writerIndex(writerIndex + result.bytesProduced());
                return result;
            }
            
            @Override
            int calculateOutNetBufSize(final SslHandler handler, final int pendingBytes, final int numComponents) {
                return ReferenceCountedOpenSslEngine.calculateOutNetBufSize(pendingBytes, numComponents);
            }
        }, 
        JDK(false, ByteToMessageDecoder.MERGE_CUMULATOR) {
            @Override
            SSLEngineResult unwrap(final SslHandler handler, final ByteBuf in, final int readerIndex, final int len, final ByteBuf out) throws SSLException {
                final int writerIndex = out.writerIndex();
                final SSLEngineResult result = handler.engine.unwrap(toByteBuffer(in, readerIndex, len), toByteBuffer(out, writerIndex, out.writableBytes()));
                out.writerIndex(writerIndex + result.bytesProduced());
                return result;
            }
            
            @Override
            int calculateOutNetBufSize(final SslHandler handler, final int pendingBytes, final int numComponents) {
                return handler.maxPacketBufferSize;
            }
        };
        
        final boolean wantsDirectBuffer;
        final Cumulator cumulator;
        
        static SslEngineType forEngine(final SSLEngine engine) {
            return (engine instanceof ReferenceCountedOpenSslEngine) ? SslEngineType.TCNATIVE : SslEngineType.JDK;
        }
        
        private SslEngineType(final boolean wantsDirectBuffer, final Cumulator cumulator) {
            this.wantsDirectBuffer = wantsDirectBuffer;
            this.cumulator = cumulator;
        }
        
        abstract SSLEngineResult unwrap(final SslHandler p0, final ByteBuf p1, final int p2, final int p3, final ByteBuf p4) throws SSLException;
        
        abstract int calculateOutNetBufSize(final SslHandler p0, final int p1, final int p2);
    }
    
    private final class LazyChannelPromise extends DefaultPromise<Channel>
    {
        @Override
        protected EventExecutor executor() {
            if (SslHandler.this.ctx == null) {
                throw new IllegalStateException();
            }
            return SslHandler.this.ctx.executor();
        }
        
        @Override
        protected void checkDeadLock() {
            if (SslHandler.this.ctx == null) {
                return;
            }
            super.checkDeadLock();
        }
    }
}
