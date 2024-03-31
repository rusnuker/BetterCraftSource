// 
// Decompiled by Procyon v0.6.0
// 

package io.netty.handler.codec.http2;

import io.netty.util.AsciiString;
import io.netty.handler.codec.Headers;
import io.netty.handler.codec.http.HttpHeaderNames;
import io.netty.handler.codec.compression.ZlibCodecFactory;
import io.netty.channel.ChannelHandler;
import io.netty.handler.codec.compression.ZlibWrapper;
import io.netty.handler.codec.http.HttpHeaderValues;
import io.netty.util.concurrent.Promise;
import io.netty.util.concurrent.PromiseCombiner;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelPromise;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.embedded.EmbeddedChannel;

public class CompressorHttp2ConnectionEncoder extends DecoratingHttp2ConnectionEncoder
{
    public static final int DEFAULT_COMPRESSION_LEVEL = 6;
    public static final int DEFAULT_WINDOW_BITS = 15;
    public static final int DEFAULT_MEM_LEVEL = 8;
    private final int compressionLevel;
    private final int windowBits;
    private final int memLevel;
    private final Http2Connection.PropertyKey propertyKey;
    
    public CompressorHttp2ConnectionEncoder(final Http2ConnectionEncoder delegate) {
        this(delegate, 6, 15, 8);
    }
    
    public CompressorHttp2ConnectionEncoder(final Http2ConnectionEncoder delegate, final int compressionLevel, final int windowBits, final int memLevel) {
        super(delegate);
        if (compressionLevel < 0 || compressionLevel > 9) {
            throw new IllegalArgumentException("compressionLevel: " + compressionLevel + " (expected: 0-9)");
        }
        if (windowBits < 9 || windowBits > 15) {
            throw new IllegalArgumentException("windowBits: " + windowBits + " (expected: 9-15)");
        }
        if (memLevel < 1 || memLevel > 9) {
            throw new IllegalArgumentException("memLevel: " + memLevel + " (expected: 1-9)");
        }
        this.compressionLevel = compressionLevel;
        this.windowBits = windowBits;
        this.memLevel = memLevel;
        this.propertyKey = this.connection().newKey();
        this.connection().addListener(new Http2ConnectionAdapter() {
            @Override
            public void onStreamRemoved(final Http2Stream stream) {
                final EmbeddedChannel compressor = stream.getProperty(CompressorHttp2ConnectionEncoder.this.propertyKey);
                if (compressor != null) {
                    CompressorHttp2ConnectionEncoder.this.cleanup(stream, compressor);
                }
            }
        });
    }
    
    @Override
    public ChannelFuture writeData(final ChannelHandlerContext ctx, final int streamId, final ByteBuf data, int padding, final boolean endOfStream, final ChannelPromise promise) {
        final Http2Stream stream = this.connection().stream(streamId);
        final EmbeddedChannel channel = (stream == null) ? null : stream.getProperty(this.propertyKey);
        if (channel == null) {
            return super.writeData(ctx, streamId, data, padding, endOfStream, promise);
        }
        try {
            channel.writeOutbound(data);
            ByteBuf buf = nextReadableBuf(channel);
            if (buf == null) {
                if (endOfStream) {
                    if (channel.finish()) {
                        buf = nextReadableBuf(channel);
                    }
                    return super.writeData(ctx, streamId, (buf == null) ? Unpooled.EMPTY_BUFFER : buf, padding, true, promise);
                }
                promise.setSuccess();
                return promise;
            }
            else {
                final PromiseCombiner combiner = new PromiseCombiner();
                while (true) {
                    ByteBuf nextBuf = nextReadableBuf(channel);
                    boolean compressedEndOfStream = nextBuf == null && endOfStream;
                    if (compressedEndOfStream && channel.finish()) {
                        nextBuf = nextReadableBuf(channel);
                        compressedEndOfStream = (nextBuf == null);
                    }
                    final ChannelPromise bufPromise = ctx.newPromise();
                    combiner.add(bufPromise);
                    super.writeData(ctx, streamId, buf, padding, compressedEndOfStream, bufPromise);
                    if (nextBuf == null) {
                        break;
                    }
                    padding = 0;
                    buf = nextBuf;
                }
                combiner.finish(promise);
            }
        }
        catch (final Throwable cause) {
            promise.tryFailure(cause);
        }
        finally {
            if (endOfStream) {
                this.cleanup(stream, channel);
            }
        }
        return promise;
    }
    
    @Override
    public ChannelFuture writeHeaders(final ChannelHandlerContext ctx, final int streamId, final Http2Headers headers, final int padding, final boolean endStream, final ChannelPromise promise) {
        try {
            final EmbeddedChannel compressor = this.newCompressor(ctx, headers, endStream);
            final ChannelFuture future = super.writeHeaders(ctx, streamId, headers, padding, endStream, promise);
            this.bindCompressorToStream(compressor, streamId);
            return future;
        }
        catch (final Throwable e) {
            promise.tryFailure(e);
            return promise;
        }
    }
    
    @Override
    public ChannelFuture writeHeaders(final ChannelHandlerContext ctx, final int streamId, final Http2Headers headers, final int streamDependency, final short weight, final boolean exclusive, final int padding, final boolean endOfStream, final ChannelPromise promise) {
        try {
            final EmbeddedChannel compressor = this.newCompressor(ctx, headers, endOfStream);
            final ChannelFuture future = super.writeHeaders(ctx, streamId, headers, streamDependency, weight, exclusive, padding, endOfStream, promise);
            this.bindCompressorToStream(compressor, streamId);
            return future;
        }
        catch (final Throwable e) {
            promise.tryFailure(e);
            return promise;
        }
    }
    
    protected EmbeddedChannel newContentCompressor(final ChannelHandlerContext ctx, final CharSequence contentEncoding) throws Http2Exception {
        if (HttpHeaderValues.GZIP.contentEqualsIgnoreCase(contentEncoding) || HttpHeaderValues.X_GZIP.contentEqualsIgnoreCase(contentEncoding)) {
            return this.newCompressionChannel(ctx, ZlibWrapper.GZIP);
        }
        if (HttpHeaderValues.DEFLATE.contentEqualsIgnoreCase(contentEncoding) || HttpHeaderValues.X_DEFLATE.contentEqualsIgnoreCase(contentEncoding)) {
            return this.newCompressionChannel(ctx, ZlibWrapper.ZLIB);
        }
        return null;
    }
    
    protected CharSequence getTargetContentEncoding(final CharSequence contentEncoding) throws Http2Exception {
        return contentEncoding;
    }
    
    private EmbeddedChannel newCompressionChannel(final ChannelHandlerContext ctx, final ZlibWrapper wrapper) {
        return new EmbeddedChannel(ctx.channel().id(), ctx.channel().metadata().hasDisconnect(), ctx.channel().config(), new ChannelHandler[] { ZlibCodecFactory.newZlibEncoder(wrapper, this.compressionLevel, this.windowBits, this.memLevel) });
    }
    
    private EmbeddedChannel newCompressor(final ChannelHandlerContext ctx, final Http2Headers headers, final boolean endOfStream) throws Http2Exception {
        if (endOfStream) {
            return null;
        }
        CharSequence encoding = ((Headers<AsciiString, CharSequence, T>)headers).get(HttpHeaderNames.CONTENT_ENCODING);
        if (encoding == null) {
            encoding = HttpHeaderValues.IDENTITY;
        }
        final EmbeddedChannel compressor = this.newContentCompressor(ctx, encoding);
        if (compressor != null) {
            final CharSequence targetContentEncoding = this.getTargetContentEncoding(encoding);
            if (HttpHeaderValues.IDENTITY.contentEqualsIgnoreCase(targetContentEncoding)) {
                ((Headers<AsciiString, V, T>)headers).remove(HttpHeaderNames.CONTENT_ENCODING);
            }
            else {
                ((Headers<AsciiString, CharSequence, Headers>)headers).set(HttpHeaderNames.CONTENT_ENCODING, targetContentEncoding);
            }
            ((Headers<AsciiString, V, T>)headers).remove(HttpHeaderNames.CONTENT_LENGTH);
        }
        return compressor;
    }
    
    private void bindCompressorToStream(final EmbeddedChannel compressor, final int streamId) {
        if (compressor != null) {
            final Http2Stream stream = this.connection().stream(streamId);
            if (stream != null) {
                stream.setProperty(this.propertyKey, compressor);
            }
        }
    }
    
    void cleanup(final Http2Stream stream, final EmbeddedChannel compressor) {
        if (compressor.finish()) {
            while (true) {
                final ByteBuf buf = compressor.readOutbound();
                if (buf == null) {
                    break;
                }
                buf.release();
            }
        }
        stream.removeProperty(this.propertyKey);
    }
    
    private static ByteBuf nextReadableBuf(final EmbeddedChannel compressor) {
        while (true) {
            final ByteBuf buf = compressor.readOutbound();
            if (buf == null) {
                return null;
            }
            if (buf.isReadable()) {
                return buf;
            }
            buf.release();
        }
    }
}
