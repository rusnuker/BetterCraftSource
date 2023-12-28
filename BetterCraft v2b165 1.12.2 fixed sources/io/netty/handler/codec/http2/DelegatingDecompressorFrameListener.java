// 
// Decompiled by Procyon v0.6.0
// 

package io.netty.handler.codec.http2;

import io.netty.util.AsciiString;
import io.netty.handler.codec.Headers;
import io.netty.util.internal.ObjectUtil;
import io.netty.handler.codec.http.HttpHeaderNames;
import io.netty.handler.codec.compression.ZlibCodecFactory;
import io.netty.handler.codec.compression.ZlibWrapper;
import io.netty.channel.ChannelHandler;
import io.netty.handler.codec.http.HttpHeaderValues;
import io.netty.channel.embedded.EmbeddedChannel;
import io.netty.buffer.Unpooled;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;

public class DelegatingDecompressorFrameListener extends Http2FrameListenerDecorator
{
    private final Http2Connection connection;
    private final boolean strict;
    private boolean flowControllerInitialized;
    private final Http2Connection.PropertyKey propertyKey;
    
    public DelegatingDecompressorFrameListener(final Http2Connection connection, final Http2FrameListener listener) {
        this(connection, listener, true);
    }
    
    public DelegatingDecompressorFrameListener(final Http2Connection connection, final Http2FrameListener listener, final boolean strict) {
        super(listener);
        this.connection = connection;
        this.strict = strict;
        this.propertyKey = connection.newKey();
        connection.addListener(new Http2ConnectionAdapter() {
            @Override
            public void onStreamRemoved(final Http2Stream stream) {
                final Http2Decompressor decompressor = DelegatingDecompressorFrameListener.this.decompressor(stream);
                if (decompressor != null) {
                    cleanup(decompressor);
                }
            }
        });
    }
    
    @Override
    public int onDataRead(final ChannelHandlerContext ctx, final int streamId, final ByteBuf data, int padding, final boolean endOfStream) throws Http2Exception {
        final Http2Stream stream = this.connection.stream(streamId);
        final Http2Decompressor decompressor = this.decompressor(stream);
        if (decompressor == null) {
            return this.listener.onDataRead(ctx, streamId, data, padding, endOfStream);
        }
        final EmbeddedChannel channel = decompressor.decompressor();
        final int compressedBytes = data.readableBytes() + padding;
        decompressor.incrementCompressedBytes(compressedBytes);
        try {
            channel.writeInbound(data.retain());
            ByteBuf buf = nextReadableBuf(channel);
            if (buf == null && endOfStream && channel.finish()) {
                buf = nextReadableBuf(channel);
            }
            if (buf == null) {
                if (endOfStream) {
                    this.listener.onDataRead(ctx, streamId, Unpooled.EMPTY_BUFFER, padding, true);
                }
                decompressor.incrementDecompressedBytes(compressedBytes);
                return compressedBytes;
            }
            try {
                final Http2LocalFlowController flowController = this.connection.local().flowController();
                decompressor.incrementDecompressedBytes(padding);
                while (true) {
                    ByteBuf nextBuf = nextReadableBuf(channel);
                    boolean decompressedEndOfStream = nextBuf == null && endOfStream;
                    if (decompressedEndOfStream && channel.finish()) {
                        nextBuf = nextReadableBuf(channel);
                        decompressedEndOfStream = (nextBuf == null);
                    }
                    decompressor.incrementDecompressedBytes(buf.readableBytes());
                    flowController.consumeBytes(stream, this.listener.onDataRead(ctx, streamId, buf, padding, decompressedEndOfStream));
                    if (nextBuf == null) {
                        break;
                    }
                    padding = 0;
                    buf.release();
                    buf = nextBuf;
                }
                return 0;
            }
            finally {
                buf.release();
            }
        }
        catch (final Http2Exception e) {
            throw e;
        }
        catch (final Throwable t) {
            throw Http2Exception.streamError(stream.id(), Http2Error.INTERNAL_ERROR, t, "Decompressor error detected while delegating data read on streamId %d", stream.id());
        }
    }
    
    @Override
    public void onHeadersRead(final ChannelHandlerContext ctx, final int streamId, final Http2Headers headers, final int padding, final boolean endStream) throws Http2Exception {
        this.initDecompressor(ctx, streamId, headers, endStream);
        this.listener.onHeadersRead(ctx, streamId, headers, padding, endStream);
    }
    
    @Override
    public void onHeadersRead(final ChannelHandlerContext ctx, final int streamId, final Http2Headers headers, final int streamDependency, final short weight, final boolean exclusive, final int padding, final boolean endStream) throws Http2Exception {
        this.initDecompressor(ctx, streamId, headers, endStream);
        this.listener.onHeadersRead(ctx, streamId, headers, streamDependency, weight, exclusive, padding, endStream);
    }
    
    protected EmbeddedChannel newContentDecompressor(final ChannelHandlerContext ctx, final CharSequence contentEncoding) throws Http2Exception {
        if (HttpHeaderValues.GZIP.contentEqualsIgnoreCase(contentEncoding) || HttpHeaderValues.X_GZIP.contentEqualsIgnoreCase(contentEncoding)) {
            return new EmbeddedChannel(ctx.channel().id(), ctx.channel().metadata().hasDisconnect(), ctx.channel().config(), new ChannelHandler[] { ZlibCodecFactory.newZlibDecoder(ZlibWrapper.GZIP) });
        }
        if (HttpHeaderValues.DEFLATE.contentEqualsIgnoreCase(contentEncoding) || HttpHeaderValues.X_DEFLATE.contentEqualsIgnoreCase(contentEncoding)) {
            final ZlibWrapper wrapper = this.strict ? ZlibWrapper.ZLIB : ZlibWrapper.ZLIB_OR_NONE;
            return new EmbeddedChannel(ctx.channel().id(), ctx.channel().metadata().hasDisconnect(), ctx.channel().config(), new ChannelHandler[] { ZlibCodecFactory.newZlibDecoder(wrapper) });
        }
        return null;
    }
    
    protected CharSequence getTargetContentEncoding(final CharSequence contentEncoding) throws Http2Exception {
        return HttpHeaderValues.IDENTITY;
    }
    
    private void initDecompressor(final ChannelHandlerContext ctx, final int streamId, final Http2Headers headers, final boolean endOfStream) throws Http2Exception {
        final Http2Stream stream = this.connection.stream(streamId);
        if (stream == null) {
            return;
        }
        Http2Decompressor decompressor = this.decompressor(stream);
        if (decompressor == null && !endOfStream) {
            CharSequence contentEncoding = ((Headers<AsciiString, CharSequence, T>)headers).get(HttpHeaderNames.CONTENT_ENCODING);
            if (contentEncoding == null) {
                contentEncoding = HttpHeaderValues.IDENTITY;
            }
            final EmbeddedChannel channel = this.newContentDecompressor(ctx, contentEncoding);
            if (channel != null) {
                decompressor = new Http2Decompressor(channel);
                stream.setProperty(this.propertyKey, decompressor);
                final CharSequence targetContentEncoding = this.getTargetContentEncoding(contentEncoding);
                if (HttpHeaderValues.IDENTITY.contentEqualsIgnoreCase(targetContentEncoding)) {
                    ((Headers<AsciiString, V, T>)headers).remove(HttpHeaderNames.CONTENT_ENCODING);
                }
                else {
                    ((Headers<AsciiString, CharSequence, Headers>)headers).set(HttpHeaderNames.CONTENT_ENCODING, targetContentEncoding);
                }
            }
        }
        if (decompressor != null) {
            ((Headers<AsciiString, V, T>)headers).remove(HttpHeaderNames.CONTENT_LENGTH);
            if (!this.flowControllerInitialized) {
                this.flowControllerInitialized = true;
                this.connection.local().flowController(new ConsumedBytesConverter(this.connection.local().flowController()));
            }
        }
    }
    
    Http2Decompressor decompressor(final Http2Stream stream) {
        return (stream == null) ? null : stream.getProperty(this.propertyKey);
    }
    
    private static void cleanup(final Http2Decompressor decompressor) {
        decompressor.decompressor().finishAndReleaseAll();
    }
    
    private static ByteBuf nextReadableBuf(final EmbeddedChannel decompressor) {
        while (true) {
            final ByteBuf buf = decompressor.readInbound();
            if (buf == null) {
                return null;
            }
            if (buf.isReadable()) {
                return buf;
            }
            buf.release();
        }
    }
    
    private final class ConsumedBytesConverter implements Http2LocalFlowController
    {
        private final Http2LocalFlowController flowController;
        
        ConsumedBytesConverter(final Http2LocalFlowController flowController) {
            this.flowController = ObjectUtil.checkNotNull(flowController, "flowController");
        }
        
        @Override
        public Http2LocalFlowController frameWriter(final Http2FrameWriter frameWriter) {
            return this.flowController.frameWriter(frameWriter);
        }
        
        @Override
        public void channelHandlerContext(final ChannelHandlerContext ctx) throws Http2Exception {
            this.flowController.channelHandlerContext(ctx);
        }
        
        @Override
        public void initialWindowSize(final int newWindowSize) throws Http2Exception {
            this.flowController.initialWindowSize(newWindowSize);
        }
        
        @Override
        public int initialWindowSize() {
            return this.flowController.initialWindowSize();
        }
        
        @Override
        public int windowSize(final Http2Stream stream) {
            return this.flowController.windowSize(stream);
        }
        
        @Override
        public void incrementWindowSize(final Http2Stream stream, final int delta) throws Http2Exception {
            this.flowController.incrementWindowSize(stream, delta);
        }
        
        @Override
        public void receiveFlowControlledFrame(final Http2Stream stream, final ByteBuf data, final int padding, final boolean endOfStream) throws Http2Exception {
            this.flowController.receiveFlowControlledFrame(stream, data, padding, endOfStream);
        }
        
        @Override
        public boolean consumeBytes(final Http2Stream stream, int numBytes) throws Http2Exception {
            final Http2Decompressor decompressor = DelegatingDecompressorFrameListener.this.decompressor(stream);
            if (decompressor != null) {
                numBytes = decompressor.consumeBytes(stream.id(), numBytes);
            }
            try {
                return this.flowController.consumeBytes(stream, numBytes);
            }
            catch (final Http2Exception e) {
                throw e;
            }
            catch (final Throwable t) {
                throw Http2Exception.streamError(stream.id(), Http2Error.INTERNAL_ERROR, t, "Error while returning bytes to flow control window", new Object[0]);
            }
        }
        
        @Override
        public int unconsumedBytes(final Http2Stream stream) {
            return this.flowController.unconsumedBytes(stream);
        }
        
        @Override
        public int initialWindowSize(final Http2Stream stream) {
            return this.flowController.initialWindowSize(stream);
        }
    }
    
    private static final class Http2Decompressor
    {
        private final EmbeddedChannel decompressor;
        private int compressed;
        private int decompressed;
        
        Http2Decompressor(final EmbeddedChannel decompressor) {
            this.decompressor = decompressor;
        }
        
        EmbeddedChannel decompressor() {
            return this.decompressor;
        }
        
        void incrementCompressedBytes(final int delta) {
            assert delta >= 0;
            this.compressed += delta;
        }
        
        void incrementDecompressedBytes(final int delta) {
            assert delta >= 0;
            this.decompressed += delta;
        }
        
        int consumeBytes(final int streamId, final int decompressedBytes) throws Http2Exception {
            if (decompressedBytes < 0) {
                throw new IllegalArgumentException("decompressedBytes must not be negative: " + decompressedBytes);
            }
            if (this.decompressed - decompressedBytes < 0) {
                throw Http2Exception.streamError(streamId, Http2Error.INTERNAL_ERROR, "Attempting to return too many bytes for stream %d. decompressed: %d decompressedBytes: %d", streamId, this.decompressed, decompressedBytes);
            }
            final double consumedRatio = decompressedBytes / (double)this.decompressed;
            final int consumedCompressed = Math.min(this.compressed, (int)Math.ceil(this.compressed * consumedRatio));
            if (this.compressed - consumedCompressed < 0) {
                throw Http2Exception.streamError(streamId, Http2Error.INTERNAL_ERROR, "overflow when converting decompressed bytes to compressed bytes for stream %d.decompressedBytes: %d decompressed: %d compressed: %d consumedCompressed: %d", streamId, decompressedBytes, this.decompressed, this.compressed, consumedCompressed);
            }
            this.decompressed -= decompressedBytes;
            this.compressed -= consumedCompressed;
            return consumedCompressed;
        }
    }
}
