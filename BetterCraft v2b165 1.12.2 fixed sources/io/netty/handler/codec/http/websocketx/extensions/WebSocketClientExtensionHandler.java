// 
// Decompiled by Procyon v0.6.0
// 

package io.netty.handler.codec.http.websocketx.extensions;

import io.netty.channel.ChannelHandler;
import io.netty.handler.codec.CodecException;
import java.util.ArrayList;
import io.netty.handler.codec.http.HttpResponse;
import java.util.Iterator;
import io.netty.handler.codec.http.HttpHeaderNames;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.channel.ChannelPromise;
import io.netty.channel.ChannelHandlerContext;
import java.util.Arrays;
import java.util.List;
import io.netty.channel.ChannelDuplexHandler;

public class WebSocketClientExtensionHandler extends ChannelDuplexHandler
{
    private final List<WebSocketClientExtensionHandshaker> extensionHandshakers;
    
    public WebSocketClientExtensionHandler(final WebSocketClientExtensionHandshaker... extensionHandshakers) {
        if (extensionHandshakers == null) {
            throw new NullPointerException("extensionHandshakers");
        }
        if (extensionHandshakers.length == 0) {
            throw new IllegalArgumentException("extensionHandshakers must contains at least one handshaker");
        }
        this.extensionHandshakers = Arrays.asList(extensionHandshakers);
    }
    
    @Override
    public void write(final ChannelHandlerContext ctx, final Object msg, final ChannelPromise promise) throws Exception {
        if (msg instanceof HttpRequest && WebSocketExtensionUtil.isWebsocketUpgrade(((HttpRequest)msg).headers())) {
            final HttpRequest request = (HttpRequest)msg;
            String headerValue = request.headers().getAsString(HttpHeaderNames.SEC_WEBSOCKET_EXTENSIONS);
            for (final WebSocketClientExtensionHandshaker extentionHandshaker : this.extensionHandshakers) {
                final WebSocketExtensionData extensionData = extentionHandshaker.newRequestData();
                headerValue = WebSocketExtensionUtil.appendExtension(headerValue, extensionData.name(), extensionData.parameters());
            }
            request.headers().set(HttpHeaderNames.SEC_WEBSOCKET_EXTENSIONS, headerValue);
        }
        super.write(ctx, msg, promise);
    }
    
    @Override
    public void channelRead(final ChannelHandlerContext ctx, final Object msg) throws Exception {
        if (msg instanceof HttpResponse) {
            final HttpResponse response = (HttpResponse)msg;
            if (WebSocketExtensionUtil.isWebsocketUpgrade(response.headers())) {
                final String extensionsHeader = response.headers().getAsString(HttpHeaderNames.SEC_WEBSOCKET_EXTENSIONS);
                if (extensionsHeader != null) {
                    final List<WebSocketExtensionData> extensions = WebSocketExtensionUtil.extractExtensions(extensionsHeader);
                    final List<WebSocketClientExtension> validExtensions = new ArrayList<WebSocketClientExtension>(extensions.size());
                    int rsv = 0;
                    for (final WebSocketExtensionData extensionData : extensions) {
                        Iterator<WebSocketClientExtensionHandshaker> extensionHandshakersIterator;
                        WebSocketClientExtension validExtension;
                        WebSocketClientExtensionHandshaker extensionHandshaker;
                        for (extensionHandshakersIterator = this.extensionHandshakers.iterator(), validExtension = null; validExtension == null && extensionHandshakersIterator.hasNext(); validExtension = extensionHandshaker.handshakeExtension(extensionData)) {
                            extensionHandshaker = extensionHandshakersIterator.next();
                        }
                        if (validExtension == null || (validExtension.rsv() & rsv) != 0x0) {
                            throw new CodecException("invalid WebSocket Extension handhshake for \"" + extensionsHeader + "\"");
                        }
                        rsv |= validExtension.rsv();
                        validExtensions.add(validExtension);
                    }
                    for (final WebSocketClientExtension validExtension2 : validExtensions) {
                        final WebSocketExtensionDecoder decoder = validExtension2.newExtensionDecoder();
                        final WebSocketExtensionEncoder encoder = validExtension2.newExtensionEncoder();
                        ctx.pipeline().addAfter(ctx.name(), decoder.getClass().getName(), decoder);
                        ctx.pipeline().addAfter(ctx.name(), encoder.getClass().getName(), encoder);
                    }
                }
                ctx.pipeline().remove(ctx.name());
            }
        }
        super.channelRead(ctx, msg);
    }
}
