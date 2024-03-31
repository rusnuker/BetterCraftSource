// 
// Decompiled by Procyon v0.6.0
// 

package io.netty.handler.codec.http2;

public final class Http2ConnectionHandlerBuilder extends AbstractHttp2ConnectionHandlerBuilder<Http2ConnectionHandler, Http2ConnectionHandlerBuilder>
{
    public Http2ConnectionHandlerBuilder validateHeaders(final boolean validateHeaders) {
        return super.validateHeaders(validateHeaders);
    }
    
    public Http2ConnectionHandlerBuilder initialSettings(final Http2Settings settings) {
        return super.initialSettings(settings);
    }
    
    public Http2ConnectionHandlerBuilder frameListener(final Http2FrameListener frameListener) {
        return super.frameListener(frameListener);
    }
    
    public Http2ConnectionHandlerBuilder gracefulShutdownTimeoutMillis(final long gracefulShutdownTimeoutMillis) {
        return super.gracefulShutdownTimeoutMillis(gracefulShutdownTimeoutMillis);
    }
    
    public Http2ConnectionHandlerBuilder server(final boolean isServer) {
        return super.server(isServer);
    }
    
    public Http2ConnectionHandlerBuilder connection(final Http2Connection connection) {
        return super.connection(connection);
    }
    
    public Http2ConnectionHandlerBuilder maxReservedStreams(final int maxReservedStreams) {
        return super.maxReservedStreams(maxReservedStreams);
    }
    
    public Http2ConnectionHandlerBuilder codec(final Http2ConnectionDecoder decoder, final Http2ConnectionEncoder encoder) {
        return super.codec(decoder, encoder);
    }
    
    public Http2ConnectionHandlerBuilder frameLogger(final Http2FrameLogger frameLogger) {
        return super.frameLogger(frameLogger);
    }
    
    public Http2ConnectionHandlerBuilder encoderEnforceMaxConcurrentStreams(final boolean encoderEnforceMaxConcurrentStreams) {
        return super.encoderEnforceMaxConcurrentStreams(encoderEnforceMaxConcurrentStreams);
    }
    
    public Http2ConnectionHandlerBuilder encoderIgnoreMaxHeaderListSize(final boolean encoderIgnoreMaxHeaderListSize) {
        return super.encoderIgnoreMaxHeaderListSize(encoderIgnoreMaxHeaderListSize);
    }
    
    public Http2ConnectionHandlerBuilder headerSensitivityDetector(final Http2HeadersEncoder.SensitivityDetector headerSensitivityDetector) {
        return super.headerSensitivityDetector(headerSensitivityDetector);
    }
    
    public Http2ConnectionHandler build() {
        return super.build();
    }
    
    @Override
    protected Http2ConnectionHandler build(final Http2ConnectionDecoder decoder, final Http2ConnectionEncoder encoder, final Http2Settings initialSettings) {
        return new Http2ConnectionHandler(decoder, encoder, initialSettings);
    }
}
