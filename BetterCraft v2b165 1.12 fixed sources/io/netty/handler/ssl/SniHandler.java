// 
// Decompiled by Procyon v0.6.0
// 

package io.netty.handler.ssl;

import io.netty.util.concurrent.Promise;
import io.netty.util.internal.logging.InternalLoggerFactory;
import io.netty.channel.ChannelPromise;
import java.net.SocketAddress;
import io.netty.util.ReferenceCountUtil;
import io.netty.channel.ChannelHandler;
import io.netty.util.concurrent.GenericFutureListener;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.FutureListener;
import io.netty.handler.codec.DecoderException;
import io.netty.util.internal.PlatformDependent;
import java.util.Locale;
import java.net.IDN;
import io.netty.util.CharsetUtil;
import io.netty.buffer.ByteBufUtil;
import java.util.List;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.util.internal.ObjectUtil;
import io.netty.util.DomainNameMapping;
import io.netty.util.Mapping;
import io.netty.util.AsyncMapping;
import io.netty.util.internal.logging.InternalLogger;
import io.netty.channel.ChannelOutboundHandler;
import io.netty.handler.codec.ByteToMessageDecoder;

public class SniHandler extends ByteToMessageDecoder implements ChannelOutboundHandler
{
    private static final int MAX_SSL_RECORDS = 4;
    private static final InternalLogger logger;
    private static final Selection EMPTY_SELECTION;
    protected final AsyncMapping<String, SslContext> mapping;
    private boolean handshakeFailed;
    private boolean suppressRead;
    private boolean readPending;
    private volatile Selection selection;
    
    public SniHandler(final Mapping<? super String, ? extends SslContext> mapping) {
        this(new AsyncMappingAdapter((Mapping)mapping));
    }
    
    public SniHandler(final DomainNameMapping<? extends SslContext> mapping) {
        this((Mapping<? super String, ? extends SslContext>)mapping);
    }
    
    public SniHandler(final AsyncMapping<? super String, ? extends SslContext> mapping) {
        this.selection = SniHandler.EMPTY_SELECTION;
        this.mapping = ObjectUtil.checkNotNull(mapping, "mapping");
    }
    
    public String hostname() {
        return this.selection.hostname;
    }
    
    public SslContext sslContext() {
        return this.selection.context;
    }
    
    @Override
    protected void decode(final ChannelHandlerContext ctx, final ByteBuf in, final List<Object> out) throws Exception {
        if (!this.suppressRead && !this.handshakeFailed) {
            final int writerIndex = in.writerIndex();
            try {
                int i = 0;
                while (i < 4) {
                    final int readerIndex = in.readerIndex();
                    final int readableBytes = writerIndex - readerIndex;
                    if (readableBytes < 5) {
                        return;
                    }
                    final int command = in.getUnsignedByte(readerIndex);
                    switch (command) {
                        case 20:
                        case 21: {
                            final int len = SslUtils.getEncryptedPacketLength(in, readerIndex);
                            if (len == -2) {
                                this.handshakeFailed = true;
                                final NotSslRecordException e = new NotSslRecordException("not an SSL/TLS record: " + ByteBufUtil.hexDump(in));
                                in.skipBytes(in.readableBytes());
                                SslUtils.notifyHandshakeFailure(ctx, e);
                                throw e;
                            }
                            if (len == -1 || writerIndex - readerIndex - 5 < len) {
                                return;
                            }
                            in.skipBytes(len);
                            ++i;
                            continue;
                        }
                        case 22: {
                            final int majorVersion = in.getUnsignedByte(readerIndex + 1);
                            if (majorVersion != 3) {
                                break;
                            }
                            final int packetLength = in.getUnsignedShort(readerIndex + 3) + 5;
                            if (readableBytes < packetLength) {
                                return;
                            }
                            final int endOffset = readerIndex + packetLength;
                            int offset = readerIndex + 43;
                            if (endOffset - offset < 6) {
                                break;
                            }
                            final int sessionIdLength = in.getUnsignedByte(offset);
                            offset += sessionIdLength + 1;
                            final int cipherSuitesLength = in.getUnsignedShort(offset);
                            offset += cipherSuitesLength + 2;
                            final int compressionMethodLength = in.getUnsignedByte(offset);
                            offset += compressionMethodLength + 1;
                            final int extensionsLength = in.getUnsignedShort(offset);
                            offset += 2;
                            final int extensionsLimit = offset + extensionsLength;
                            if (extensionsLimit > endOffset) {
                                break;
                            }
                            while (extensionsLimit - offset >= 4) {
                                final int extensionType = in.getUnsignedShort(offset);
                                offset += 2;
                                final int extensionLength = in.getUnsignedShort(offset);
                                offset += 2;
                                if (extensionsLimit - offset < extensionLength) {
                                    break;
                                }
                                if (extensionType == 0) {
                                    offset += 2;
                                    if (extensionsLimit - offset < 3) {
                                        break;
                                    }
                                    final int serverNameType = in.getUnsignedByte(offset);
                                    ++offset;
                                    if (serverNameType != 0) {
                                        break;
                                    }
                                    final int serverNameLength = in.getUnsignedShort(offset);
                                    offset += 2;
                                    if (extensionsLimit - offset < serverNameLength) {
                                        break;
                                    }
                                    final String hostname = in.toString(offset, serverNameLength, CharsetUtil.UTF_8);
                                    try {
                                        this.select(ctx, IDN.toASCII(hostname, 1).toLowerCase(Locale.US));
                                    }
                                    catch (final Throwable t) {
                                        PlatformDependent.throwException(t);
                                    }
                                    return;
                                }
                                else {
                                    offset += extensionLength;
                                }
                            }
                            break;
                        }
                    }
                    break;
                }
            }
            catch (final Throwable e2) {
                if (SniHandler.logger.isDebugEnabled()) {
                    SniHandler.logger.debug("Unexpected client hello packet: " + ByteBufUtil.hexDump(in), e2);
                }
            }
            this.select(ctx, null);
        }
    }
    
    private void select(final ChannelHandlerContext ctx, final String hostname) throws Exception {
        final Future<SslContext> future = this.lookup(ctx, hostname);
        if (future.isDone()) {
            if (!future.isSuccess()) {
                throw new DecoderException("failed to get the SslContext for " + hostname, future.cause());
            }
            this.onSslContext(ctx, hostname, future.getNow());
        }
        else {
            this.suppressRead = true;
            future.addListener(new FutureListener<SslContext>() {
                @Override
                public void operationComplete(final Future<SslContext> future) throws Exception {
                    try {
                        SniHandler.this.suppressRead = false;
                        if (future.isSuccess()) {
                            try {
                                SniHandler.this.onSslContext(ctx, hostname, future.getNow());
                            }
                            catch (final Throwable cause) {
                                ctx.fireExceptionCaught((Throwable)new DecoderException(cause));
                            }
                        }
                        else {
                            ctx.fireExceptionCaught((Throwable)new DecoderException("failed to get the SslContext for " + hostname, future.cause()));
                        }
                    }
                    finally {
                        if (SniHandler.this.readPending) {
                            SniHandler.this.readPending = false;
                            ctx.read();
                        }
                    }
                }
            });
        }
    }
    
    protected Future<SslContext> lookup(final ChannelHandlerContext ctx, final String hostname) throws Exception {
        return this.mapping.map(hostname, ctx.executor().newPromise());
    }
    
    private void onSslContext(final ChannelHandlerContext ctx, final String hostname, final SslContext sslContext) {
        this.selection = new Selection(sslContext, hostname);
        try {
            this.replaceHandler(ctx, hostname, sslContext);
        }
        catch (final Throwable cause) {
            this.selection = SniHandler.EMPTY_SELECTION;
            PlatformDependent.throwException(cause);
        }
    }
    
    protected void replaceHandler(final ChannelHandlerContext ctx, final String hostname, final SslContext sslContext) throws Exception {
        SslHandler sslHandler = null;
        try {
            sslHandler = sslContext.newHandler(ctx.alloc());
            ctx.pipeline().replace(this, SslHandler.class.getName(), sslHandler);
            sslHandler = null;
        }
        finally {
            if (sslHandler != null) {
                ReferenceCountUtil.safeRelease(sslHandler.engine());
            }
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
    public void disconnect(final ChannelHandlerContext ctx, final ChannelPromise promise) throws Exception {
        ctx.disconnect(promise);
    }
    
    @Override
    public void close(final ChannelHandlerContext ctx, final ChannelPromise promise) throws Exception {
        ctx.close(promise);
    }
    
    @Override
    public void deregister(final ChannelHandlerContext ctx, final ChannelPromise promise) throws Exception {
        ctx.deregister(promise);
    }
    
    @Override
    public void read(final ChannelHandlerContext ctx) throws Exception {
        if (this.suppressRead) {
            this.readPending = true;
        }
        else {
            ctx.read();
        }
    }
    
    @Override
    public void write(final ChannelHandlerContext ctx, final Object msg, final ChannelPromise promise) throws Exception {
        ctx.write(msg, promise);
    }
    
    @Override
    public void flush(final ChannelHandlerContext ctx) throws Exception {
        ctx.flush();
    }
    
    static {
        logger = InternalLoggerFactory.getInstance(SniHandler.class);
        EMPTY_SELECTION = new Selection(null, null);
    }
    
    private static final class AsyncMappingAdapter implements AsyncMapping<String, SslContext>
    {
        private final Mapping<? super String, ? extends SslContext> mapping;
        
        private AsyncMappingAdapter(final Mapping<? super String, ? extends SslContext> mapping) {
            this.mapping = ObjectUtil.checkNotNull(mapping, "mapping");
        }
        
        @Override
        public Future<SslContext> map(final String input, final Promise<SslContext> promise) {
            SslContext context;
            try {
                context = (SslContext)this.mapping.map(input);
            }
            catch (final Throwable cause) {
                return promise.setFailure(cause);
            }
            return promise.setSuccess(context);
        }
    }
    
    private static final class Selection
    {
        final SslContext context;
        final String hostname;
        
        Selection(final SslContext context, final String hostname) {
            this.context = context;
            this.hostname = hostname;
        }
    }
}
