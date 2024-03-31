// 
// Decompiled by Procyon v0.6.0
// 

package io.netty.handler.codec.http;

import io.netty.util.AsciiString;
import io.netty.util.CharsetUtil;
import io.netty.buffer.ByteBufUtil;
import io.netty.buffer.ByteBuf;

public class HttpRequestEncoder extends HttpObjectEncoder<HttpRequest>
{
    private static final char SLASH = '/';
    private static final char QUESTION_MARK = '?';
    
    @Override
    public boolean acceptOutboundMessage(final Object msg) throws Exception {
        return super.acceptOutboundMessage(msg) && !(msg instanceof HttpResponse);
    }
    
    @Override
    protected void encodeInitialLine(final ByteBuf buf, final HttpRequest request) throws Exception {
        final AsciiString method = request.method().asciiName();
        ByteBufUtil.copy(method, method.arrayOffset(), buf, method.length());
        buf.writeByte(32);
        String uri = request.uri();
        if (uri.isEmpty()) {
            uri += '/';
        }
        else {
            final int start = uri.indexOf("://");
            if (start != -1 && uri.charAt(0) != '/') {
                final int startIndex = start + 3;
                final int index = uri.indexOf(63, startIndex);
                if (index == -1) {
                    if (uri.lastIndexOf(47) <= startIndex) {
                        uri += '/';
                    }
                }
                else if (uri.lastIndexOf(47, index) <= startIndex) {
                    final int len = uri.length();
                    final StringBuilder sb = new StringBuilder(len + 1);
                    sb.append(uri, 0, index).append('/').append(uri, index, len);
                    uri = sb.toString();
                }
            }
        }
        buf.writeBytes(uri.getBytes(CharsetUtil.UTF_8));
        buf.writeByte(32);
        request.protocolVersion().encode(buf);
        buf.writeBytes(HttpRequestEncoder.CRLF);
    }
}
