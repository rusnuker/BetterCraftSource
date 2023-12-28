// 
// Decompiled by Procyon v0.6.0
// 

package io.netty.handler.codec.http;

import io.netty.buffer.ByteBuf;
import io.netty.handler.codec.CodecException;
import io.netty.util.ReferenceCountUtil;
import java.util.List;
import io.netty.channel.embedded.EmbeddedChannel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;

public abstract class HttpContentDecoder extends MessageToMessageDecoder<HttpObject>
{
    static final String IDENTITY;
    protected ChannelHandlerContext ctx;
    private EmbeddedChannel decoder;
    private boolean continueResponse;
    
    @Override
    protected void decode(final ChannelHandlerContext ctx, final HttpObject msg, final List<Object> out) throws Exception {
        if (msg instanceof HttpResponse && ((HttpResponse)msg).status().code() == 100) {
            if (!(msg instanceof LastHttpContent)) {
                this.continueResponse = true;
            }
            out.add(ReferenceCountUtil.retain(msg));
            return;
        }
        if (this.continueResponse) {
            if (msg instanceof LastHttpContent) {
                this.continueResponse = false;
            }
            out.add(ReferenceCountUtil.retain(msg));
            return;
        }
        if (msg instanceof HttpMessage) {
            this.cleanup();
            final HttpMessage message = (HttpMessage)msg;
            final HttpHeaders headers = message.headers();
            String contentEncoding = headers.get(HttpHeaderNames.CONTENT_ENCODING);
            if (contentEncoding != null) {
                contentEncoding = contentEncoding.trim();
            }
            else {
                contentEncoding = HttpContentDecoder.IDENTITY;
            }
            this.decoder = this.newContentDecoder(contentEncoding);
            if (this.decoder == null) {
                if (message instanceof HttpContent) {
                    ((HttpContent)message).retain();
                }
                out.add(message);
                return;
            }
            if (headers.contains(HttpHeaderNames.CONTENT_LENGTH)) {
                headers.remove(HttpHeaderNames.CONTENT_LENGTH);
                headers.set(HttpHeaderNames.TRANSFER_ENCODING, HttpHeaderValues.CHUNKED);
            }
            final CharSequence targetContentEncoding = this.getTargetContentEncoding(contentEncoding);
            if (HttpHeaderValues.IDENTITY.contentEquals(targetContentEncoding)) {
                headers.remove(HttpHeaderNames.CONTENT_ENCODING);
            }
            else {
                headers.set(HttpHeaderNames.CONTENT_ENCODING, targetContentEncoding);
            }
            if (message instanceof HttpContent) {
                HttpMessage copy;
                if (message instanceof HttpRequest) {
                    final HttpRequest r = (HttpRequest)message;
                    copy = new DefaultHttpRequest(r.protocolVersion(), r.method(), r.uri());
                }
                else {
                    if (!(message instanceof HttpResponse)) {
                        throw new CodecException("Object of class " + message.getClass().getName() + " is not a HttpRequest or HttpResponse");
                    }
                    final HttpResponse r2 = (HttpResponse)message;
                    copy = new DefaultHttpResponse(r2.protocolVersion(), r2.status());
                }
                copy.headers().set(message.headers());
                copy.setDecoderResult(message.decoderResult());
                out.add(copy);
            }
            else {
                out.add(message);
            }
        }
        if (msg instanceof HttpContent) {
            final HttpContent c = (HttpContent)msg;
            if (this.decoder == null) {
                out.add(c.retain());
            }
            else {
                this.decodeContent(c, out);
            }
        }
    }
    
    private void decodeContent(final HttpContent c, final List<Object> out) {
        final ByteBuf content = c.content();
        this.decode(content, out);
        if (c instanceof LastHttpContent) {
            this.finishDecode(out);
            final LastHttpContent last = (LastHttpContent)c;
            final HttpHeaders headers = last.trailingHeaders();
            if (headers.isEmpty()) {
                out.add(LastHttpContent.EMPTY_LAST_CONTENT);
            }
            else {
                out.add(new ComposedLastHttpContent(headers));
            }
        }
    }
    
    protected abstract EmbeddedChannel newContentDecoder(final String p0) throws Exception;
    
    protected String getTargetContentEncoding(final String contentEncoding) throws Exception {
        return HttpContentDecoder.IDENTITY;
    }
    
    @Override
    public void handlerRemoved(final ChannelHandlerContext ctx) throws Exception {
        this.cleanup();
        super.handlerRemoved(ctx);
    }
    
    @Override
    public void channelInactive(final ChannelHandlerContext ctx) throws Exception {
        this.cleanup();
        super.channelInactive(ctx);
    }
    
    @Override
    public void handlerAdded(final ChannelHandlerContext ctx) throws Exception {
        super.handlerAdded(this.ctx = ctx);
    }
    
    private void cleanup() {
        if (this.decoder != null) {
            if (this.decoder.finish()) {
                while (true) {
                    final ByteBuf buf = this.decoder.readInbound();
                    if (buf == null) {
                        break;
                    }
                    buf.release();
                }
            }
            this.decoder = null;
        }
    }
    
    private void decode(final ByteBuf in, final List<Object> out) {
        this.decoder.writeInbound(in.retain());
        this.fetchDecoderOutput(out);
    }
    
    private void finishDecode(final List<Object> out) {
        if (this.decoder.finish()) {
            this.fetchDecoderOutput(out);
        }
        this.decoder = null;
    }
    
    private void fetchDecoderOutput(final List<Object> out) {
        while (true) {
            final ByteBuf buf = this.decoder.readInbound();
            if (buf == null) {
                break;
            }
            if (!buf.isReadable()) {
                buf.release();
            }
            else {
                out.add(new DefaultHttpContent(buf));
            }
        }
    }
    
    static {
        IDENTITY = HttpHeaderValues.IDENTITY.toString();
    }
}
