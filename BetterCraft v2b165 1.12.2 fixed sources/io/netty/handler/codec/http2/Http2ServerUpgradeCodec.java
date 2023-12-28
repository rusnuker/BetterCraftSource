// 
// Decompiled by Procyon v0.6.0
// 

package io.netty.handler.codec.http2;

import java.util.Collections;
import io.netty.util.internal.logging.InternalLoggerFactory;
import io.netty.buffer.ByteBuf;
import io.netty.handler.codec.base64.Base64;
import io.netty.handler.codec.base64.Base64Dialect;
import io.netty.buffer.ByteBufUtil;
import io.netty.util.CharsetUtil;
import java.nio.CharBuffer;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.channel.ChannelHandlerContext;
import java.util.Collection;
import io.netty.util.internal.ObjectUtil;
import io.netty.channel.ChannelHandler;
import java.util.List;
import io.netty.util.internal.logging.InternalLogger;
import io.netty.handler.codec.http.HttpServerUpgradeHandler;

public class Http2ServerUpgradeCodec implements HttpServerUpgradeHandler.UpgradeCodec
{
    private static final InternalLogger logger;
    private static final List<CharSequence> REQUIRED_UPGRADE_HEADERS;
    private final String handlerName;
    private final Http2ConnectionHandler connectionHandler;
    private final ChannelHandler upgradeToHandler;
    private final Http2FrameReader frameReader;
    
    public Http2ServerUpgradeCodec(final Http2ConnectionHandler connectionHandler) {
        this(null, connectionHandler);
    }
    
    public Http2ServerUpgradeCodec(final Http2Codec http2Codec) {
        this(null, http2Codec);
    }
    
    public Http2ServerUpgradeCodec(final String handlerName, final Http2ConnectionHandler connectionHandler) {
        this(handlerName, connectionHandler, connectionHandler);
    }
    
    public Http2ServerUpgradeCodec(final String handlerName, final Http2Codec http2Codec) {
        this(handlerName, http2Codec.frameCodec().connectionHandler(), http2Codec);
    }
    
    Http2ServerUpgradeCodec(final String handlerName, final Http2ConnectionHandler connectionHandler, final ChannelHandler upgradeToHandler) {
        this.handlerName = handlerName;
        this.connectionHandler = ObjectUtil.checkNotNull(connectionHandler, "connectionHandler");
        this.upgradeToHandler = ObjectUtil.checkNotNull(upgradeToHandler, "upgradeToHandler");
        this.frameReader = new DefaultHttp2FrameReader();
    }
    
    @Override
    public Collection<CharSequence> requiredUpgradeHeaders() {
        return Http2ServerUpgradeCodec.REQUIRED_UPGRADE_HEADERS;
    }
    
    @Override
    public boolean prepareUpgradeResponse(final ChannelHandlerContext ctx, final FullHttpRequest upgradeRequest, final HttpHeaders headers) {
        try {
            final List<String> upgradeHeaders = upgradeRequest.headers().getAll(Http2CodecUtil.HTTP_UPGRADE_SETTINGS_HEADER);
            if (upgradeHeaders.isEmpty() || upgradeHeaders.size() > 1) {
                throw new IllegalArgumentException("There must be 1 and only 1 " + (Object)Http2CodecUtil.HTTP_UPGRADE_SETTINGS_HEADER + " header.");
            }
            final Http2Settings settings = this.decodeSettingsHeader(ctx, upgradeHeaders.get(0));
            this.connectionHandler.onHttpServerUpgrade(settings);
            return true;
        }
        catch (final Throwable cause) {
            Http2ServerUpgradeCodec.logger.info("Error during upgrade to HTTP/2", cause);
            return false;
        }
    }
    
    @Override
    public void upgradeTo(final ChannelHandlerContext ctx, final FullHttpRequest upgradeRequest) {
        ctx.pipeline().addAfter(ctx.name(), this.handlerName, this.upgradeToHandler);
    }
    
    private Http2Settings decodeSettingsHeader(final ChannelHandlerContext ctx, final CharSequence settingsHeader) throws Http2Exception {
        final ByteBuf header = ByteBufUtil.encodeString(ctx.alloc(), CharBuffer.wrap(settingsHeader), CharsetUtil.UTF_8);
        try {
            final ByteBuf payload = Base64.decode(header, Base64Dialect.URL_SAFE);
            final ByteBuf frame = createSettingsFrame(ctx, payload);
            return this.decodeSettings(ctx, frame);
        }
        finally {
            header.release();
        }
    }
    
    private Http2Settings decodeSettings(final ChannelHandlerContext ctx, final ByteBuf frame) throws Http2Exception {
        try {
            final Http2Settings decodedSettings = new Http2Settings();
            this.frameReader.readFrame(ctx, frame, new Http2FrameAdapter() {
                @Override
                public void onSettingsRead(final ChannelHandlerContext ctx, final Http2Settings settings) {
                    decodedSettings.copyFrom(settings);
                }
            });
            return decodedSettings;
        }
        finally {
            frame.release();
        }
    }
    
    private static ByteBuf createSettingsFrame(final ChannelHandlerContext ctx, final ByteBuf payload) {
        final ByteBuf frame = ctx.alloc().buffer(9 + payload.readableBytes());
        Http2CodecUtil.writeFrameHeader(frame, payload.readableBytes(), (byte)4, new Http2Flags(), 0);
        frame.writeBytes(payload);
        payload.release();
        return frame;
    }
    
    static {
        logger = InternalLoggerFactory.getInstance(Http2ServerUpgradeCodec.class);
        REQUIRED_UPGRADE_HEADERS = Collections.singletonList(Http2CodecUtil.HTTP_UPGRADE_SETTINGS_HEADER);
    }
}
