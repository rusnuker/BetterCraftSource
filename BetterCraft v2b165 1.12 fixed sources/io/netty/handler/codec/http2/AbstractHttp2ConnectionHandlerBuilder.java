// 
// Decompiled by Procyon v0.6.0
// 

package io.netty.handler.codec.http2;

import java.util.concurrent.TimeUnit;
import io.netty.util.internal.ObjectUtil;

public abstract class AbstractHttp2ConnectionHandlerBuilder<T extends Http2ConnectionHandler, B extends AbstractHttp2ConnectionHandlerBuilder<T, B>>
{
    private static final long DEFAULT_GRACEFUL_SHUTDOWN_TIMEOUT_MILLIS;
    private static final Http2HeadersEncoder.SensitivityDetector DEFAULT_HEADER_SENSITIVITY_DETECTOR;
    private Http2Settings initialSettings;
    private Http2FrameListener frameListener;
    private long gracefulShutdownTimeoutMillis;
    private Boolean isServer;
    private Integer maxReservedStreams;
    private Http2Connection connection;
    private Http2ConnectionDecoder decoder;
    private Http2ConnectionEncoder encoder;
    private Boolean validateHeaders;
    private Http2FrameLogger frameLogger;
    private Http2HeadersEncoder.SensitivityDetector headerSensitivityDetector;
    private Boolean encoderEnforceMaxConcurrentStreams;
    private Boolean encoderIgnoreMaxHeaderListSize;
    
    public AbstractHttp2ConnectionHandlerBuilder() {
        this.initialSettings = new Http2Settings().maxHeaderListSize(8192L);
        this.gracefulShutdownTimeoutMillis = AbstractHttp2ConnectionHandlerBuilder.DEFAULT_GRACEFUL_SHUTDOWN_TIMEOUT_MILLIS;
    }
    
    protected Http2Settings initialSettings() {
        return this.initialSettings;
    }
    
    protected B initialSettings(final Http2Settings settings) {
        this.initialSettings = ObjectUtil.checkNotNull(settings, "settings");
        return this.self();
    }
    
    protected Http2FrameListener frameListener() {
        return this.frameListener;
    }
    
    protected B frameListener(final Http2FrameListener frameListener) {
        this.frameListener = ObjectUtil.checkNotNull(frameListener, "frameListener");
        return this.self();
    }
    
    protected long gracefulShutdownTimeoutMillis() {
        return this.gracefulShutdownTimeoutMillis;
    }
    
    protected B gracefulShutdownTimeoutMillis(final long gracefulShutdownTimeoutMillis) {
        this.gracefulShutdownTimeoutMillis = gracefulShutdownTimeoutMillis;
        return this.self();
    }
    
    protected boolean isServer() {
        return this.isServer == null || this.isServer;
    }
    
    protected B server(final boolean isServer) {
        enforceConstraint("server", "connection", this.connection);
        enforceConstraint("server", "codec", this.decoder);
        enforceConstraint("server", "codec", this.encoder);
        this.isServer = isServer;
        return this.self();
    }
    
    protected int maxReservedStreams() {
        return (this.maxReservedStreams != null) ? this.maxReservedStreams : 100;
    }
    
    protected B maxReservedStreams(final int maxReservedStreams) {
        enforceConstraint("server", "connection", this.connection);
        enforceConstraint("server", "codec", this.decoder);
        enforceConstraint("server", "codec", this.encoder);
        this.maxReservedStreams = ObjectUtil.checkPositiveOrZero(maxReservedStreams, "maxReservedStreams");
        return this.self();
    }
    
    protected Http2Connection connection() {
        return this.connection;
    }
    
    protected B connection(final Http2Connection connection) {
        enforceConstraint("connection", "maxReservedStreams", this.maxReservedStreams);
        enforceConstraint("connection", "server", this.isServer);
        enforceConstraint("connection", "codec", this.decoder);
        enforceConstraint("connection", "codec", this.encoder);
        this.connection = ObjectUtil.checkNotNull(connection, "connection");
        return this.self();
    }
    
    protected Http2ConnectionDecoder decoder() {
        return this.decoder;
    }
    
    protected Http2ConnectionEncoder encoder() {
        return this.encoder;
    }
    
    protected B codec(final Http2ConnectionDecoder decoder, final Http2ConnectionEncoder encoder) {
        enforceConstraint("codec", "server", this.isServer);
        enforceConstraint("codec", "maxReservedStreams", this.maxReservedStreams);
        enforceConstraint("codec", "connection", this.connection);
        enforceConstraint("codec", "frameLogger", this.frameLogger);
        enforceConstraint("codec", "validateHeaders", this.validateHeaders);
        enforceConstraint("codec", "headerSensitivityDetector", this.headerSensitivityDetector);
        enforceConstraint("codec", "encoderEnforceMaxConcurrentStreams", this.encoderEnforceMaxConcurrentStreams);
        ObjectUtil.checkNotNull(decoder, "decoder");
        ObjectUtil.checkNotNull(encoder, "encoder");
        if (decoder.connection() != encoder.connection()) {
            throw new IllegalArgumentException("The specified encoder and decoder have different connections.");
        }
        this.decoder = decoder;
        this.encoder = encoder;
        return this.self();
    }
    
    protected boolean isValidateHeaders() {
        return this.validateHeaders == null || this.validateHeaders;
    }
    
    protected B validateHeaders(final boolean validateHeaders) {
        this.enforceNonCodecConstraints("validateHeaders");
        this.validateHeaders = validateHeaders;
        return this.self();
    }
    
    protected Http2FrameLogger frameLogger() {
        return this.frameLogger;
    }
    
    protected B frameLogger(final Http2FrameLogger frameLogger) {
        this.enforceNonCodecConstraints("frameLogger");
        this.frameLogger = ObjectUtil.checkNotNull(frameLogger, "frameLogger");
        return this.self();
    }
    
    protected boolean encoderEnforceMaxConcurrentStreams() {
        return this.encoderEnforceMaxConcurrentStreams != null && this.encoderEnforceMaxConcurrentStreams;
    }
    
    protected B encoderEnforceMaxConcurrentStreams(final boolean encoderEnforceMaxConcurrentStreams) {
        this.enforceNonCodecConstraints("encoderEnforceMaxConcurrentStreams");
        this.encoderEnforceMaxConcurrentStreams = encoderEnforceMaxConcurrentStreams;
        return this.self();
    }
    
    protected Http2HeadersEncoder.SensitivityDetector headerSensitivityDetector() {
        return (this.headerSensitivityDetector != null) ? this.headerSensitivityDetector : AbstractHttp2ConnectionHandlerBuilder.DEFAULT_HEADER_SENSITIVITY_DETECTOR;
    }
    
    protected B headerSensitivityDetector(final Http2HeadersEncoder.SensitivityDetector headerSensitivityDetector) {
        this.enforceNonCodecConstraints("headerSensitivityDetector");
        this.headerSensitivityDetector = ObjectUtil.checkNotNull(headerSensitivityDetector, "headerSensitivityDetector");
        return this.self();
    }
    
    protected B encoderIgnoreMaxHeaderListSize(final boolean ignoreMaxHeaderListSize) {
        this.enforceNonCodecConstraints("encoderIgnoreMaxHeaderListSize");
        this.encoderIgnoreMaxHeaderListSize = ignoreMaxHeaderListSize;
        return this.self();
    }
    
    protected T build() {
        if (this.encoder == null) {
            Http2Connection connection = this.connection;
            if (connection == null) {
                connection = new DefaultHttp2Connection(this.isServer(), this.maxReservedStreams());
            }
            return this.buildFromConnection(connection);
        }
        assert this.decoder != null;
        return this.buildFromCodec(this.decoder, this.encoder);
    }
    
    private T buildFromConnection(final Http2Connection connection) {
        final Long maxHeaderListSize = this.initialSettings.maxHeaderListSize();
        Http2FrameReader reader = new DefaultHttp2FrameReader((maxHeaderListSize == null) ? new DefaultHttp2HeadersDecoder(this.isValidateHeaders()) : new DefaultHttp2HeadersDecoder(this.isValidateHeaders(), maxHeaderListSize));
        Http2FrameWriter writer = (this.encoderIgnoreMaxHeaderListSize == null) ? new DefaultHttp2FrameWriter(this.headerSensitivityDetector()) : new DefaultHttp2FrameWriter(this.headerSensitivityDetector(), this.encoderIgnoreMaxHeaderListSize);
        if (this.frameLogger != null) {
            reader = new Http2InboundFrameLogger(reader, this.frameLogger);
            writer = new Http2OutboundFrameLogger(writer, this.frameLogger);
        }
        Http2ConnectionEncoder encoder = new DefaultHttp2ConnectionEncoder(connection, writer);
        final boolean encoderEnforceMaxConcurrentStreams = this.encoderEnforceMaxConcurrentStreams();
        if (encoderEnforceMaxConcurrentStreams) {
            if (connection.isServer()) {
                encoder.close();
                reader.close();
                throw new IllegalArgumentException("encoderEnforceMaxConcurrentStreams: " + encoderEnforceMaxConcurrentStreams + " not supported for server");
            }
            encoder = new StreamBufferingEncoder(encoder);
        }
        final Http2ConnectionDecoder decoder = new DefaultHttp2ConnectionDecoder(connection, encoder, reader);
        return this.buildFromCodec(decoder, encoder);
    }
    
    private T buildFromCodec(final Http2ConnectionDecoder decoder, final Http2ConnectionEncoder encoder) {
        T handler;
        try {
            handler = this.build(decoder, encoder, this.initialSettings);
        }
        catch (final Throwable t) {
            encoder.close();
            decoder.close();
            throw new IllegalStateException("failed to build a Http2ConnectionHandler", t);
        }
        handler.gracefulShutdownTimeoutMillis(this.gracefulShutdownTimeoutMillis);
        if (handler.decoder().frameListener() == null) {
            handler.decoder().frameListener(this.frameListener);
        }
        return handler;
    }
    
    protected abstract T build(final Http2ConnectionDecoder p0, final Http2ConnectionEncoder p1, final Http2Settings p2) throws Exception;
    
    protected final B self() {
        return (B)this;
    }
    
    private void enforceNonCodecConstraints(final String rejectee) {
        enforceConstraint(rejectee, "server/connection", this.decoder);
        enforceConstraint(rejectee, "server/connection", this.encoder);
    }
    
    private static void enforceConstraint(final String methodName, final String rejectorName, final Object value) {
        if (value != null) {
            throw new IllegalStateException(methodName + "() cannot be called because " + rejectorName + "() has been called already.");
        }
    }
    
    static {
        DEFAULT_GRACEFUL_SHUTDOWN_TIMEOUT_MILLIS = TimeUnit.MILLISECONDS.convert(30L, TimeUnit.SECONDS);
        DEFAULT_HEADER_SENSITIVITY_DETECTOR = Http2HeadersEncoder.NEVER_SENSITIVE;
    }
}
