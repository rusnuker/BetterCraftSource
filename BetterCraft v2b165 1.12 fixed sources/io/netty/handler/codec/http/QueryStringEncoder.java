// 
// Decompiled by Procyon v0.6.0
// 

package io.netty.handler.codec.http;

import java.io.UnsupportedEncodingException;
import java.nio.charset.UnsupportedCharsetException;
import java.net.URLEncoder;
import java.net.URISyntaxException;
import java.net.URI;
import io.netty.util.internal.ObjectUtil;
import java.util.ArrayList;
import java.util.List;
import java.nio.charset.Charset;
import java.util.regex.Pattern;

public class QueryStringEncoder
{
    private static final Pattern PATTERN;
    private final Charset charset;
    private final String uri;
    private final List<Param> params;
    
    public QueryStringEncoder(final String uri) {
        this(uri, HttpConstants.DEFAULT_CHARSET);
    }
    
    public QueryStringEncoder(final String uri, final Charset charset) {
        this.params = new ArrayList<Param>();
        this.uri = ObjectUtil.checkNotNull(uri, "uri");
        this.charset = ObjectUtil.checkNotNull(charset, "charset");
    }
    
    public void addParam(final String name, final String value) {
        ObjectUtil.checkNotNull(name, "name");
        this.params.add(new Param(name, value));
    }
    
    public URI toUri() throws URISyntaxException {
        return new URI(this.toString());
    }
    
    @Override
    public String toString() {
        if (this.params.isEmpty()) {
            return this.uri;
        }
        final StringBuilder sb = new StringBuilder(this.uri).append('?');
        for (int i = 0; i < this.params.size(); ++i) {
            final Param param = this.params.get(i);
            sb.append(encodeComponent(param.name, this.charset));
            if (param.value != null) {
                sb.append('=');
                sb.append(encodeComponent(param.value, this.charset));
            }
            if (i != this.params.size() - 1) {
                sb.append('&');
            }
        }
        return sb.toString();
    }
    
    private static String encodeComponent(final String s, final Charset charset) {
        try {
            return URLEncoder.encode(s, QueryStringEncoder.PATTERN.matcher(charset.name()).replaceAll("%20"));
        }
        catch (final UnsupportedEncodingException ignored) {
            throw new UnsupportedCharsetException(charset.name());
        }
    }
    
    static {
        PATTERN = Pattern.compile("+", 16);
    }
    
    private static final class Param
    {
        final String name;
        final String value;
        
        Param(final String name, final String value) {
            this.value = value;
            this.name = name;
        }
    }
}
