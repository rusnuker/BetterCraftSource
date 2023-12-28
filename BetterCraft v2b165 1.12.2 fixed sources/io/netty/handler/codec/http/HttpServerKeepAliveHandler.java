// 
// Decompiled by Procyon v0.6.0
// 

package io.netty.handler.codec.http;

import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelPromise;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelDuplexHandler;

public class HttpServerKeepAliveHandler extends ChannelDuplexHandler
{
    private static final String MULTIPART_PREFIX = "multipart";
    private boolean persistentConnection;
    private int pendingResponses;
    
    public HttpServerKeepAliveHandler() {
        this.persistentConnection = true;
    }
    
    @Override
    public void channelRead(final ChannelHandlerContext ctx, final Object msg) throws Exception {
        if (msg instanceof HttpRequest) {
            final HttpRequest request = (HttpRequest)msg;
            if (this.persistentConnection) {
                ++this.pendingResponses;
                this.persistentConnection = HttpUtil.isKeepAlive(request);
            }
        }
        super.channelRead(ctx, msg);
    }
    
    @Override
    public void write(final ChannelHandlerContext ctx, final Object msg, final ChannelPromise promise) throws Exception {
        if (msg instanceof HttpResponse) {
            final HttpResponse response = (HttpResponse)msg;
            this.trackResponse(response);
            if (!HttpUtil.isKeepAlive(response) || !isSelfDefinedMessageLength(response)) {
                this.pendingResponses = 0;
                this.persistentConnection = false;
            }
            if (!this.shouldKeepAlive()) {
                HttpUtil.setKeepAlive(response, false);
            }
        }
        if (msg instanceof LastHttpContent && !this.shouldKeepAlive()) {
            promise.addListener((GenericFutureListener<? extends Future<? super Void>>)ChannelFutureListener.CLOSE);
        }
        super.write(ctx, msg, promise);
    }
    
    private void trackResponse(final HttpResponse response) {
        if (!isInformational(response)) {
            --this.pendingResponses;
        }
    }
    
    private boolean shouldKeepAlive() {
        return this.pendingResponses != 0 || this.persistentConnection;
    }
    
    private static boolean isSelfDefinedMessageLength(final HttpResponse response) {
        return HttpUtil.isContentLengthSet(response) || HttpUtil.isTransferEncodingChunked(response) || isMultipart(response) || isInformational(response);
    }
    
    private static boolean isInformational(final HttpResponse response) {
        return response.status().codeClass() == HttpStatusClass.INFORMATIONAL;
    }
    
    private static boolean isMultipart(final HttpResponse response) {
        final String contentType = response.headers().get(HttpHeaderNames.CONTENT_TYPE);
        return contentType != null && contentType.regionMatches(true, 0, "multipart", 0, "multipart".length());
    }
}
