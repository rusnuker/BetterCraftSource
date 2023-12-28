// 
// Decompiled by Procyon v0.6.0
// 

package io.netty.handler.codec.http.multipart;

import io.netty.handler.codec.DecoderException;
import io.netty.handler.codec.http.HttpContent;
import java.util.List;
import io.netty.util.internal.StringUtil;
import io.netty.handler.codec.http.HttpHeaderValues;
import io.netty.handler.codec.http.HttpHeaderNames;
import java.nio.charset.Charset;
import io.netty.handler.codec.http.HttpConstants;
import io.netty.handler.codec.http.HttpRequest;

public class HttpPostRequestDecoder implements InterfaceHttpPostRequestDecoder
{
    static final int DEFAULT_DISCARD_THRESHOLD = 10485760;
    private final InterfaceHttpPostRequestDecoder decoder;
    
    public HttpPostRequestDecoder(final HttpRequest request) {
        this(new DefaultHttpDataFactory(16384L), request, HttpConstants.DEFAULT_CHARSET);
    }
    
    public HttpPostRequestDecoder(final HttpDataFactory factory, final HttpRequest request) {
        this(factory, request, HttpConstants.DEFAULT_CHARSET);
    }
    
    public HttpPostRequestDecoder(final HttpDataFactory factory, final HttpRequest request, final Charset charset) {
        if (factory == null) {
            throw new NullPointerException("factory");
        }
        if (request == null) {
            throw new NullPointerException("request");
        }
        if (charset == null) {
            throw new NullPointerException("charset");
        }
        if (isMultipart(request)) {
            this.decoder = new HttpPostMultipartRequestDecoder(factory, request, charset);
        }
        else {
            this.decoder = new HttpPostStandardRequestDecoder(factory, request, charset);
        }
    }
    
    public static boolean isMultipart(final HttpRequest request) {
        return request.headers().contains(HttpHeaderNames.CONTENT_TYPE) && getMultipartDataBoundary(request.headers().get(HttpHeaderNames.CONTENT_TYPE)) != null;
    }
    
    protected static String[] getMultipartDataBoundary(final String contentType) {
        final String[] headerContentType = splitHeaderContentType(contentType);
        if (!headerContentType[0].toLowerCase().startsWith(HttpHeaderValues.MULTIPART_FORM_DATA.toString())) {
            return null;
        }
        int mrank;
        int crank;
        if (headerContentType[1].toLowerCase().startsWith(HttpHeaderValues.BOUNDARY.toString())) {
            mrank = 1;
            crank = 2;
        }
        else {
            if (!headerContentType[2].toLowerCase().startsWith(HttpHeaderValues.BOUNDARY.toString())) {
                return null;
            }
            mrank = 2;
            crank = 1;
        }
        String boundary = StringUtil.substringAfter(headerContentType[mrank], '=');
        if (boundary == null) {
            throw new ErrorDataDecoderException("Needs a boundary value");
        }
        if (boundary.charAt(0) == '\"') {
            final String bound = boundary.trim();
            final int index = bound.length() - 1;
            if (bound.charAt(index) == '\"') {
                boundary = bound.substring(1, index);
            }
        }
        if (headerContentType[crank].toLowerCase().startsWith(HttpHeaderValues.CHARSET.toString())) {
            final String charset = StringUtil.substringAfter(headerContentType[crank], '=');
            if (charset != null) {
                return new String[] { "--" + boundary, charset };
            }
        }
        return new String[] { "--" + boundary };
    }
    
    @Override
    public boolean isMultipart() {
        return this.decoder.isMultipart();
    }
    
    @Override
    public void setDiscardThreshold(final int discardThreshold) {
        this.decoder.setDiscardThreshold(discardThreshold);
    }
    
    @Override
    public int getDiscardThreshold() {
        return this.decoder.getDiscardThreshold();
    }
    
    @Override
    public List<InterfaceHttpData> getBodyHttpDatas() {
        return this.decoder.getBodyHttpDatas();
    }
    
    @Override
    public List<InterfaceHttpData> getBodyHttpDatas(final String name) {
        return this.decoder.getBodyHttpDatas(name);
    }
    
    @Override
    public InterfaceHttpData getBodyHttpData(final String name) {
        return this.decoder.getBodyHttpData(name);
    }
    
    @Override
    public InterfaceHttpPostRequestDecoder offer(final HttpContent content) {
        return this.decoder.offer(content);
    }
    
    @Override
    public boolean hasNext() {
        return this.decoder.hasNext();
    }
    
    @Override
    public InterfaceHttpData next() {
        return this.decoder.next();
    }
    
    @Override
    public InterfaceHttpData currentPartialHttpData() {
        return this.decoder.currentPartialHttpData();
    }
    
    @Override
    public void destroy() {
        this.decoder.destroy();
    }
    
    @Override
    public void cleanFiles() {
        this.decoder.cleanFiles();
    }
    
    @Override
    public void removeHttpDataFromClean(final InterfaceHttpData data) {
        this.decoder.removeHttpDataFromClean(data);
    }
    
    private static String[] splitHeaderContentType(final String sb) {
        final int aStart = HttpPostBodyUtil.findNonWhitespace(sb, 0);
        int aEnd = sb.indexOf(59);
        if (aEnd == -1) {
            return new String[] { sb, "", "" };
        }
        final int bStart = HttpPostBodyUtil.findNonWhitespace(sb, aEnd + 1);
        if (sb.charAt(aEnd - 1) == ' ') {
            --aEnd;
        }
        int bEnd = sb.indexOf(59, bStart);
        if (bEnd == -1) {
            bEnd = HttpPostBodyUtil.findEndOfString(sb);
            return new String[] { sb.substring(aStart, aEnd), sb.substring(bStart, bEnd), "" };
        }
        final int cStart = HttpPostBodyUtil.findNonWhitespace(sb, bEnd + 1);
        if (sb.charAt(bEnd - 1) == ' ') {
            --bEnd;
        }
        final int cEnd = HttpPostBodyUtil.findEndOfString(sb);
        return new String[] { sb.substring(aStart, aEnd), sb.substring(bStart, bEnd), sb.substring(cStart, cEnd) };
    }
    
    protected enum MultiPartStatus
    {
        NOTSTARTED, 
        PREAMBLE, 
        HEADERDELIMITER, 
        DISPOSITION, 
        FIELD, 
        FILEUPLOAD, 
        MIXEDPREAMBLE, 
        MIXEDDELIMITER, 
        MIXEDDISPOSITION, 
        MIXEDFILEUPLOAD, 
        MIXEDCLOSEDELIMITER, 
        CLOSEDELIMITER, 
        PREEPILOGUE, 
        EPILOGUE;
    }
    
    public static class NotEnoughDataDecoderException extends DecoderException
    {
        private static final long serialVersionUID = -7846841864603865638L;
        
        public NotEnoughDataDecoderException() {
        }
        
        public NotEnoughDataDecoderException(final String msg) {
            super(msg);
        }
        
        public NotEnoughDataDecoderException(final Throwable cause) {
            super(cause);
        }
        
        public NotEnoughDataDecoderException(final String msg, final Throwable cause) {
            super(msg, cause);
        }
    }
    
    public static class EndOfDataDecoderException extends DecoderException
    {
        private static final long serialVersionUID = 1336267941020800769L;
    }
    
    public static class ErrorDataDecoderException extends DecoderException
    {
        private static final long serialVersionUID = 5020247425493164465L;
        
        public ErrorDataDecoderException() {
        }
        
        public ErrorDataDecoderException(final String msg) {
            super(msg);
        }
        
        public ErrorDataDecoderException(final Throwable cause) {
            super(cause);
        }
        
        public ErrorDataDecoderException(final String msg, final Throwable cause) {
            super(msg, cause);
        }
    }
}
