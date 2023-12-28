// 
// Decompiled by Procyon v0.6.0
// 

package io.netty.handler.codec.http2;

import io.netty.buffer.ByteBuf;
import io.netty.util.internal.ObjectUtil;

public class DefaultHttp2HeadersDecoder implements Http2HeadersDecoder, Configuration
{
    private static final float HEADERS_COUNT_WEIGHT_NEW = 0.2f;
    private static final float HEADERS_COUNT_WEIGHT_HISTORICAL = 0.8f;
    private final HpackDecoder hpackDecoder;
    private final boolean validateHeaders;
    private float headerArraySizeAccumulator;
    
    public DefaultHttp2HeadersDecoder() {
        this(true);
    }
    
    public DefaultHttp2HeadersDecoder(final boolean validateHeaders) {
        this(validateHeaders, 8192L);
    }
    
    public DefaultHttp2HeadersDecoder(final boolean validateHeaders, final long maxHeaderListSize) {
        this(validateHeaders, maxHeaderListSize, 32);
    }
    
    public DefaultHttp2HeadersDecoder(final boolean validateHeaders, final long maxHeaderListSize, final int initialHuffmanDecodeCapacity) {
        this(validateHeaders, new HpackDecoder(maxHeaderListSize, initialHuffmanDecodeCapacity));
    }
    
    DefaultHttp2HeadersDecoder(final boolean validateHeaders, final HpackDecoder hpackDecoder) {
        this.headerArraySizeAccumulator = 8.0f;
        this.hpackDecoder = ObjectUtil.checkNotNull(hpackDecoder, "hpackDecoder");
        this.validateHeaders = validateHeaders;
    }
    
    @Override
    public void maxHeaderTableSize(final long max) throws Http2Exception {
        this.hpackDecoder.setMaxHeaderTableSize(max);
    }
    
    @Override
    public long maxHeaderTableSize() {
        return this.hpackDecoder.getMaxHeaderTableSize();
    }
    
    @Override
    public void maxHeaderListSize(final long max, final long goAwayMax) throws Http2Exception {
        this.hpackDecoder.setMaxHeaderListSize(max, goAwayMax);
    }
    
    @Override
    public long maxHeaderListSize() {
        return this.hpackDecoder.getMaxHeaderListSize();
    }
    
    @Override
    public long maxHeaderListSizeGoAway() {
        return this.hpackDecoder.getMaxHeaderListSizeGoAway();
    }
    
    @Override
    public Configuration configuration() {
        return this;
    }
    
    @Override
    public Http2Headers decodeHeaders(final int streamId, final ByteBuf headerBlock) throws Http2Exception {
        try {
            final Http2Headers headers = new DefaultHttp2Headers(this.validateHeaders, (int)this.headerArraySizeAccumulator);
            this.hpackDecoder.decode(streamId, headerBlock, headers);
            this.headerArraySizeAccumulator = 0.2f * headers.size() + 0.8f * this.headerArraySizeAccumulator;
            return headers;
        }
        catch (final Http2Exception e) {
            throw e;
        }
        catch (final Throwable e2) {
            throw Http2Exception.connectionError(Http2Error.COMPRESSION_ERROR, e2, e2.getMessage(), new Object[0]);
        }
    }
}
