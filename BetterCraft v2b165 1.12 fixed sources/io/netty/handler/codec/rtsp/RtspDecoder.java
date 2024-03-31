// 
// Decompiled by Procyon v0.6.0
// 

package io.netty.handler.codec.rtsp;

import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.DefaultFullHttpRequest;
import io.netty.handler.codec.http.DefaultHttpRequest;
import io.netty.handler.codec.http.DefaultHttpResponse;
import io.netty.handler.codec.http.HttpMessage;
import java.util.regex.Pattern;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpObjectDecoder;

public class RtspDecoder extends HttpObjectDecoder
{
    private static final HttpResponseStatus UNKNOWN_STATUS;
    private boolean isDecodingRequest;
    private static final Pattern versionPattern;
    public static final int DEFAULT_MAX_INITIAL_LINE_LENGTH = 4096;
    public static final int DEFAULT_MAX_HEADER_SIZE = 8192;
    public static final int DEFAULT_MAX_CONTENT_LENGTH = 8192;
    
    public RtspDecoder() {
        this(4096, 8192, 8192);
    }
    
    public RtspDecoder(final int maxInitialLineLength, final int maxHeaderSize, final int maxContentLength) {
        super(maxInitialLineLength, maxHeaderSize, maxContentLength * 2, false);
    }
    
    public RtspDecoder(final int maxInitialLineLength, final int maxHeaderSize, final int maxContentLength, final boolean validateHeaders) {
        super(maxInitialLineLength, maxHeaderSize, maxContentLength * 2, false, validateHeaders);
    }
    
    @Override
    protected HttpMessage createMessage(final String[] initialLine) throws Exception {
        if (RtspDecoder.versionPattern.matcher(initialLine[0]).matches()) {
            this.isDecodingRequest = false;
            return new DefaultHttpResponse(RtspVersions.valueOf(initialLine[0]), new HttpResponseStatus(Integer.parseInt(initialLine[1]), initialLine[2]), this.validateHeaders);
        }
        this.isDecodingRequest = true;
        return new DefaultHttpRequest(RtspVersions.valueOf(initialLine[2]), RtspMethods.valueOf(initialLine[0]), initialLine[1], this.validateHeaders);
    }
    
    @Override
    protected boolean isContentAlwaysEmpty(final HttpMessage msg) {
        return super.isContentAlwaysEmpty(msg) || !msg.headers().contains(RtspHeaderNames.CONTENT_LENGTH);
    }
    
    @Override
    protected HttpMessage createInvalidMessage() {
        if (this.isDecodingRequest) {
            return new DefaultFullHttpRequest(RtspVersions.RTSP_1_0, RtspMethods.OPTIONS, "/bad-request", this.validateHeaders);
        }
        return new DefaultFullHttpResponse(RtspVersions.RTSP_1_0, RtspDecoder.UNKNOWN_STATUS, this.validateHeaders);
    }
    
    @Override
    protected boolean isDecodingRequest() {
        return this.isDecodingRequest;
    }
    
    static {
        UNKNOWN_STATUS = new HttpResponseStatus(999, "Unknown");
        versionPattern = Pattern.compile("RTSP/\\d\\.\\d");
    }
}
