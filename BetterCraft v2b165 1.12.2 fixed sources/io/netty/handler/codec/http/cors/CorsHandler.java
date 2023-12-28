// 
// Decompiled by Procyon v0.6.0
// 

package io.netty.handler.codec.http.cors;

import io.netty.util.internal.logging.InternalLoggerFactory;
import io.netty.channel.ChannelFuture;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;
import io.netty.channel.ChannelFutureListener;
import io.netty.handler.codec.http.HttpMessage;
import io.netty.handler.codec.http.HttpUtil;
import io.netty.channel.ChannelPromise;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.HttpMethod;
import io.netty.handler.codec.http.HttpHeaderNames;
import io.netty.handler.codec.http.HttpResponse;
import io.netty.util.ReferenceCountUtil;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.channel.ChannelHandlerContext;
import io.netty.util.internal.ObjectUtil;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.util.internal.logging.InternalLogger;
import io.netty.channel.ChannelDuplexHandler;

public class CorsHandler extends ChannelDuplexHandler
{
    private static final InternalLogger logger;
    private static final String ANY_ORIGIN = "*";
    private static final String NULL_ORIGIN = "null";
    private final CorsConfig config;
    private HttpRequest request;
    
    public CorsHandler(final CorsConfig config) {
        this.config = ObjectUtil.checkNotNull(config, "config");
    }
    
    @Override
    public void channelRead(final ChannelHandlerContext ctx, final Object msg) throws Exception {
        if (this.config.isCorsSupportEnabled() && msg instanceof HttpRequest) {
            this.request = (HttpRequest)msg;
            if (isPreflightRequest(this.request)) {
                this.handlePreflight(ctx, this.request);
                return;
            }
            if (this.config.isShortCircuit() && !this.validateOrigin()) {
                forbidden(ctx, this.request);
                return;
            }
        }
        ctx.fireChannelRead(msg);
    }
    
    private void handlePreflight(final ChannelHandlerContext ctx, final HttpRequest request) {
        final HttpResponse response = new DefaultFullHttpResponse(request.protocolVersion(), HttpResponseStatus.OK, true, true);
        if (this.setOrigin(response)) {
            this.setAllowMethods(response);
            this.setAllowHeaders(response);
            this.setAllowCredentials(response);
            this.setMaxAge(response);
            this.setPreflightHeaders(response);
        }
        ReferenceCountUtil.release(request);
        respond(ctx, request, response);
    }
    
    private void setPreflightHeaders(final HttpResponse response) {
        response.headers().add(this.config.preflightResponseHeaders());
    }
    
    private boolean setOrigin(final HttpResponse response) {
        final String origin = this.request.headers().get(HttpHeaderNames.ORIGIN);
        if (origin != null) {
            if ("null".equals(origin) && this.config.isNullOriginAllowed()) {
                setNullOrigin(response);
                return true;
            }
            if (this.config.isAnyOriginSupported()) {
                if (this.config.isCredentialsAllowed()) {
                    this.echoRequestOrigin(response);
                    setVaryHeader(response);
                }
                else {
                    setAnyOrigin(response);
                }
                return true;
            }
            if (this.config.origins().contains(origin)) {
                setOrigin(response, origin);
                setVaryHeader(response);
                return true;
            }
            CorsHandler.logger.debug("Request origin [{}]] was not among the configured origins [{}]", origin, this.config.origins());
        }
        return false;
    }
    
    private boolean validateOrigin() {
        if (this.config.isAnyOriginSupported()) {
            return true;
        }
        final String origin = this.request.headers().get(HttpHeaderNames.ORIGIN);
        return origin == null || ("null".equals(origin) && this.config.isNullOriginAllowed()) || this.config.origins().contains(origin);
    }
    
    private void echoRequestOrigin(final HttpResponse response) {
        setOrigin(response, this.request.headers().get(HttpHeaderNames.ORIGIN));
    }
    
    private static void setVaryHeader(final HttpResponse response) {
        response.headers().set(HttpHeaderNames.VARY, HttpHeaderNames.ORIGIN);
    }
    
    private static void setAnyOrigin(final HttpResponse response) {
        setOrigin(response, "*");
    }
    
    private static void setNullOrigin(final HttpResponse response) {
        setOrigin(response, "null");
    }
    
    private static void setOrigin(final HttpResponse response, final String origin) {
        response.headers().set(HttpHeaderNames.ACCESS_CONTROL_ALLOW_ORIGIN, origin);
    }
    
    private void setAllowCredentials(final HttpResponse response) {
        if (this.config.isCredentialsAllowed() && !response.headers().get(HttpHeaderNames.ACCESS_CONTROL_ALLOW_ORIGIN).equals("*")) {
            response.headers().set(HttpHeaderNames.ACCESS_CONTROL_ALLOW_CREDENTIALS, "true");
        }
    }
    
    private static boolean isPreflightRequest(final HttpRequest request) {
        final HttpHeaders headers = request.headers();
        return request.method().equals(HttpMethod.OPTIONS) && headers.contains(HttpHeaderNames.ORIGIN) && headers.contains(HttpHeaderNames.ACCESS_CONTROL_REQUEST_METHOD);
    }
    
    private void setExposeHeaders(final HttpResponse response) {
        if (!this.config.exposedHeaders().isEmpty()) {
            response.headers().set(HttpHeaderNames.ACCESS_CONTROL_EXPOSE_HEADERS, this.config.exposedHeaders());
        }
    }
    
    private void setAllowMethods(final HttpResponse response) {
        response.headers().set(HttpHeaderNames.ACCESS_CONTROL_ALLOW_METHODS, this.config.allowedRequestMethods());
    }
    
    private void setAllowHeaders(final HttpResponse response) {
        response.headers().set(HttpHeaderNames.ACCESS_CONTROL_ALLOW_HEADERS, this.config.allowedRequestHeaders());
    }
    
    private void setMaxAge(final HttpResponse response) {
        response.headers().set(HttpHeaderNames.ACCESS_CONTROL_MAX_AGE, this.config.maxAge());
    }
    
    @Override
    public void write(final ChannelHandlerContext ctx, final Object msg, final ChannelPromise promise) throws Exception {
        if (this.config.isCorsSupportEnabled() && msg instanceof HttpResponse) {
            final HttpResponse response = (HttpResponse)msg;
            if (this.setOrigin(response)) {
                this.setAllowCredentials(response);
                this.setExposeHeaders(response);
            }
        }
        ctx.writeAndFlush(msg, promise);
    }
    
    private static void forbidden(final ChannelHandlerContext ctx, final HttpRequest request) {
        ReferenceCountUtil.release(request);
        respond(ctx, request, new DefaultFullHttpResponse(request.protocolVersion(), HttpResponseStatus.FORBIDDEN));
    }
    
    private static void respond(final ChannelHandlerContext ctx, final HttpRequest request, final HttpResponse response) {
        final boolean keepAlive = HttpUtil.isKeepAlive(request);
        HttpUtil.setKeepAlive(response, keepAlive);
        final ChannelFuture future = ctx.writeAndFlush(response);
        if (!keepAlive) {
            future.addListener((GenericFutureListener<? extends Future<? super Void>>)ChannelFutureListener.CLOSE);
        }
    }
    
    static {
        logger = InternalLoggerFactory.getInstance(CorsHandler.class);
    }
}
