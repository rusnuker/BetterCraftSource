// 
// Decompiled by Procyon v0.6.0
// 

package io.netty.handler.codec.http2;

import io.netty.handler.codec.http.HttpContent;
import io.netty.handler.codec.http.HttpResponse;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.LastHttpContent;
import io.netty.util.ReferenceCountUtil;
import io.netty.handler.codec.http.DefaultHttpContent;
import io.netty.handler.codec.http.HttpHeaderValues;
import io.netty.handler.codec.http.HttpHeaderNames;
import io.netty.handler.codec.http.HttpMessage;
import io.netty.handler.codec.http.HttpUtil;
import io.netty.handler.codec.http.HttpVersion;
import io.netty.handler.codec.http.DefaultLastHttpContent;
import io.netty.buffer.Unpooled;
import java.util.List;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.HttpObject;
import io.netty.handler.codec.MessageToMessageCodec;

public class Http2ServerDowngrader extends MessageToMessageCodec<Http2StreamFrame, HttpObject>
{
    private final boolean validateHeaders;
    
    public Http2ServerDowngrader(final boolean validateHeaders) {
        this.validateHeaders = validateHeaders;
    }
    
    public Http2ServerDowngrader() {
        this(true);
    }
    
    @Override
    public boolean acceptInboundMessage(final Object msg) throws Exception {
        return msg instanceof Http2HeadersFrame || msg instanceof Http2DataFrame;
    }
    
    @Override
    protected void decode(final ChannelHandlerContext ctx, final Http2StreamFrame frame, final List<Object> out) throws Exception {
        if (frame instanceof Http2HeadersFrame) {
            final int id = 0;
            final Http2HeadersFrame headersFrame = (Http2HeadersFrame)frame;
            final Http2Headers headers = headersFrame.headers();
            if (headersFrame.isEndStream()) {
                if (headers.method() == null) {
                    final LastHttpContent last = new DefaultLastHttpContent(Unpooled.EMPTY_BUFFER, this.validateHeaders);
                    HttpConversionUtil.addHttp2ToHttpHeaders(id, headers, last.trailingHeaders(), HttpVersion.HTTP_1_1, true, true);
                    out.add(last);
                }
                else {
                    final FullHttpRequest full = HttpConversionUtil.toFullHttpRequest(id, headers, ctx.alloc(), this.validateHeaders);
                    out.add(full);
                }
            }
            else {
                final HttpRequest req = HttpConversionUtil.toHttpRequest(id, headersFrame.headers(), this.validateHeaders);
                if (!HttpUtil.isContentLengthSet(req)) {
                    req.headers().add(HttpHeaderNames.TRANSFER_ENCODING, HttpHeaderValues.CHUNKED);
                }
                out.add(req);
            }
        }
        else if (frame instanceof Http2DataFrame) {
            final Http2DataFrame dataFrame = (Http2DataFrame)frame;
            if (dataFrame.isEndStream()) {
                out.add(new DefaultLastHttpContent(dataFrame.content(), this.validateHeaders));
            }
            else {
                out.add(new DefaultHttpContent(dataFrame.content()));
            }
        }
        ReferenceCountUtil.retain(frame);
    }
    
    private void encodeLastContent(final LastHttpContent last, final List<Object> out) {
        final boolean needFiller = !(last instanceof FullHttpResponse) && last.trailingHeaders().isEmpty();
        if (last.content().isReadable() || needFiller) {
            out.add(new DefaultHttp2DataFrame(last.content(), last.trailingHeaders().isEmpty()));
        }
        if (!last.trailingHeaders().isEmpty()) {
            final Http2Headers headers = HttpConversionUtil.toHttp2Headers(last.trailingHeaders(), this.validateHeaders);
            out.add(new DefaultHttp2HeadersFrame(headers, true));
        }
    }
    
    @Override
    protected void encode(final ChannelHandlerContext ctx, final HttpObject obj, final List<Object> out) throws Exception {
        if (obj instanceof HttpResponse) {
            final Http2Headers headers = HttpConversionUtil.toHttp2Headers((HttpMessage)obj, this.validateHeaders);
            boolean noMoreFrames = false;
            if (obj instanceof FullHttpResponse) {
                final FullHttpResponse full = (FullHttpResponse)obj;
                noMoreFrames = (!full.content().isReadable() && full.trailingHeaders().isEmpty());
            }
            out.add(new DefaultHttp2HeadersFrame(headers, noMoreFrames));
        }
        if (obj instanceof LastHttpContent) {
            final LastHttpContent last = (LastHttpContent)obj;
            this.encodeLastContent(last, out);
        }
        else if (obj instanceof HttpContent) {
            final HttpContent cont = (HttpContent)obj;
            out.add(new DefaultHttp2DataFrame(cont.content(), false));
        }
        ReferenceCountUtil.retain(obj);
    }
}
