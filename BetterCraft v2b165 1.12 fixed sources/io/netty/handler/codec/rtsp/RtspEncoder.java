// 
// Decompiled by Procyon v0.6.0
// 

package io.netty.handler.codec.rtsp;

import io.netty.handler.codec.UnsupportedMessageTypeException;
import io.netty.util.internal.StringUtil;
import io.netty.util.CharsetUtil;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.buffer.ByteBuf;
import io.netty.handler.codec.http.HttpResponse;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.HttpMessage;
import io.netty.handler.codec.http.HttpObjectEncoder;

public class RtspEncoder extends HttpObjectEncoder<HttpMessage>
{
    private static final byte[] CRLF;
    
    @Override
    public boolean acceptOutboundMessage(final Object msg) throws Exception {
        return super.acceptOutboundMessage(msg) && (msg instanceof HttpRequest || msg instanceof HttpResponse);
    }
    
    @Override
    protected void encodeInitialLine(final ByteBuf buf, final HttpMessage message) throws Exception {
        if (message instanceof HttpRequest) {
            final HttpRequest request = (HttpRequest)message;
            HttpHeaders.encodeAscii(request.method().toString(), buf);
            buf.writeByte(32);
            buf.writeBytes(request.uri().getBytes(CharsetUtil.UTF_8));
            buf.writeByte(32);
            HttpHeaders.encodeAscii(request.protocolVersion().toString(), buf);
            buf.writeBytes(RtspEncoder.CRLF);
        }
        else {
            if (!(message instanceof HttpResponse)) {
                throw new UnsupportedMessageTypeException("Unsupported type " + StringUtil.simpleClassName(message));
            }
            final HttpResponse response = (HttpResponse)message;
            HttpHeaders.encodeAscii(response.protocolVersion().toString(), buf);
            buf.writeByte(32);
            buf.writeBytes(String.valueOf(response.status().code()).getBytes(CharsetUtil.US_ASCII));
            buf.writeByte(32);
            HttpHeaders.encodeAscii(String.valueOf(response.status().reasonPhrase()), buf);
            buf.writeBytes(RtspEncoder.CRLF);
        }
    }
    
    static {
        CRLF = new byte[] { 13, 10 };
    }
}
