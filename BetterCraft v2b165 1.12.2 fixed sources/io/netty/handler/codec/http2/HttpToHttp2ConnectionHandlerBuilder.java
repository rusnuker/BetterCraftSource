// 
// Decompiled by Procyon v0.6.0
// 

package io.netty.handler.codec.http2;

public final class HttpToHttp2ConnectionHandlerBuilder extends AbstractHttp2ConnectionHandlerBuilder<HttpToHttp2ConnectionHandler, HttpToHttp2ConnectionHandlerBuilder>
{
    public HttpToHttp2ConnectionHandlerBuilder validateHeaders(final boolean validateHeaders) {
        return super.validateHeaders(validateHeaders);
    }
    
    public HttpToHttp2ConnectionHandlerBuilder initialSettings(final Http2Settings settings) {
        return super.initialSettings(settings);
    }
    
    public HttpToHttp2ConnectionHandlerBuilder frameListener(final Http2FrameListener frameListener) {
        return super.frameListener(frameListener);
    }
    
    public HttpToHttp2ConnectionHandlerBuilder gracefulShutdownTimeoutMillis(final long gracefulShutdownTimeoutMillis) {
        return super.gracefulShutdownTimeoutMillis(gracefulShutdownTimeoutMillis);
    }
    
    public HttpToHttp2ConnectionHandlerBuilder server(final boolean isServer) {
        return super.server(isServer);
    }
    
    public HttpToHttp2ConnectionHandlerBuilder connection(final Http2Connection connection) {
        return super.connection(connection);
    }
    
    public HttpToHttp2ConnectionHandlerBuilder codec(final Http2ConnectionDecoder decoder, final Http2ConnectionEncoder encoder) {
        return super.codec(decoder, encoder);
    }
    
    public HttpToHttp2ConnectionHandlerBuilder frameLogger(final Http2FrameLogger frameLogger) {
        return super.frameLogger(frameLogger);
    }
    
    public HttpToHttp2ConnectionHandlerBuilder encoderEnforceMaxConcurrentStreams(final boolean encoderEnforceMaxConcurrentStreams) {
        return super.encoderEnforceMaxConcurrentStreams(encoderEnforceMaxConcurrentStreams);
    }
    
    public HttpToHttp2ConnectionHandlerBuilder headerSensitivityDetector(final Http2HeadersEncoder.SensitivityDetector headerSensitivityDetector) {
        return super.headerSensitivityDetector(headerSensitivityDetector);
    }
    
    public HttpToHttp2ConnectionHandler build() {
        return super.build();
    }
    
    @Override
    protected HttpToHttp2ConnectionHandler build(final Http2ConnectionDecoder decoder, final Http2ConnectionEncoder encoder, final Http2Settings initialSettings) {
        return new HttpToHttp2ConnectionHandler(decoder, encoder, initialSettings, this.isValidateHeaders());
    }
}
