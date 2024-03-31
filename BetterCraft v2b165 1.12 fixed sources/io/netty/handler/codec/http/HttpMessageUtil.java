// 
// Decompiled by Procyon v0.6.0
// 

package io.netty.handler.codec.http;

import java.util.Iterator;
import java.util.Map;
import io.netty.util.internal.StringUtil;

final class HttpMessageUtil
{
    static StringBuilder appendRequest(final StringBuilder buf, final HttpRequest req) {
        appendCommon(buf, req);
        appendInitialLine(buf, req);
        appendHeaders(buf, req.headers());
        removeLastNewLine(buf);
        return buf;
    }
    
    static StringBuilder appendResponse(final StringBuilder buf, final HttpResponse res) {
        appendCommon(buf, res);
        appendInitialLine(buf, res);
        appendHeaders(buf, res.headers());
        removeLastNewLine(buf);
        return buf;
    }
    
    private static void appendCommon(final StringBuilder buf, final HttpMessage msg) {
        buf.append(StringUtil.simpleClassName(msg));
        buf.append("(decodeResult: ");
        buf.append(msg.decoderResult());
        buf.append(", version: ");
        buf.append(msg.protocolVersion());
        buf.append(')');
        buf.append(StringUtil.NEWLINE);
    }
    
    static StringBuilder appendFullRequest(final StringBuilder buf, final FullHttpRequest req) {
        appendFullCommon(buf, req);
        appendInitialLine(buf, req);
        appendHeaders(buf, req.headers());
        appendHeaders(buf, req.trailingHeaders());
        removeLastNewLine(buf);
        return buf;
    }
    
    static StringBuilder appendFullResponse(final StringBuilder buf, final FullHttpResponse res) {
        appendFullCommon(buf, res);
        appendInitialLine(buf, res);
        appendHeaders(buf, res.headers());
        appendHeaders(buf, res.trailingHeaders());
        removeLastNewLine(buf);
        return buf;
    }
    
    private static void appendFullCommon(final StringBuilder buf, final FullHttpMessage msg) {
        buf.append(StringUtil.simpleClassName(msg));
        buf.append("(decodeResult: ");
        buf.append(msg.decoderResult());
        buf.append(", version: ");
        buf.append(msg.protocolVersion());
        buf.append(", content: ");
        buf.append(msg.content());
        buf.append(')');
        buf.append(StringUtil.NEWLINE);
    }
    
    private static void appendInitialLine(final StringBuilder buf, final HttpRequest req) {
        buf.append(req.method());
        buf.append(' ');
        buf.append(req.uri());
        buf.append(' ');
        buf.append(req.protocolVersion());
        buf.append(StringUtil.NEWLINE);
    }
    
    private static void appendInitialLine(final StringBuilder buf, final HttpResponse res) {
        buf.append(res.protocolVersion());
        buf.append(' ');
        buf.append(res.status());
        buf.append(StringUtil.NEWLINE);
    }
    
    private static void appendHeaders(final StringBuilder buf, final HttpHeaders headers) {
        for (final Map.Entry<String, String> e : headers) {
            buf.append(e.getKey());
            buf.append(": ");
            buf.append(e.getValue());
            buf.append(StringUtil.NEWLINE);
        }
    }
    
    private static void removeLastNewLine(final StringBuilder buf) {
        buf.setLength(buf.length() - StringUtil.NEWLINE.length());
    }
    
    private HttpMessageUtil() {
    }
}
