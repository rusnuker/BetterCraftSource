// 
// Decompiled by Procyon v0.6.0
// 

package io.netty.handler.codec.http.multipart;

import io.netty.buffer.ByteBufHolder;
import io.netty.util.ReferenceCounted;
import io.netty.channel.ChannelException;
import io.netty.buffer.ByteBuf;
import java.io.IOException;
import io.netty.handler.codec.http.HttpConstants;
import java.nio.charset.Charset;
import java.util.regex.Pattern;
import io.netty.util.AbstractReferenceCounted;

public abstract class AbstractHttpData extends AbstractReferenceCounted implements HttpData
{
    private static final Pattern STRIP_PATTERN;
    private static final Pattern REPLACE_PATTERN;
    private final String name;
    protected long definedSize;
    protected long size;
    private Charset charset;
    private boolean completed;
    private long maxSize;
    
    protected AbstractHttpData(String name, final Charset charset, final long size) {
        this.charset = HttpConstants.DEFAULT_CHARSET;
        this.maxSize = -1L;
        if (name == null) {
            throw new NullPointerException("name");
        }
        name = AbstractHttpData.REPLACE_PATTERN.matcher(name).replaceAll(" ");
        name = AbstractHttpData.STRIP_PATTERN.matcher(name).replaceAll("");
        if (name.isEmpty()) {
            throw new IllegalArgumentException("empty name");
        }
        this.name = name;
        if (charset != null) {
            this.setCharset(charset);
        }
        this.definedSize = size;
    }
    
    @Override
    public long getMaxSize() {
        return this.maxSize;
    }
    
    @Override
    public void setMaxSize(final long maxSize) {
        this.maxSize = maxSize;
    }
    
    @Override
    public void checkSize(final long newSize) throws IOException {
        if (this.maxSize >= 0L && newSize > this.maxSize) {
            throw new IOException("Size exceed allowed maximum capacity");
        }
    }
    
    @Override
    public String getName() {
        return this.name;
    }
    
    @Override
    public boolean isCompleted() {
        return this.completed;
    }
    
    protected void setCompleted() {
        this.completed = true;
    }
    
    @Override
    public Charset getCharset() {
        return this.charset;
    }
    
    @Override
    public void setCharset(final Charset charset) {
        if (charset == null) {
            throw new NullPointerException("charset");
        }
        this.charset = charset;
    }
    
    @Override
    public long length() {
        return this.size;
    }
    
    @Override
    public long definedLength() {
        return this.definedSize;
    }
    
    @Override
    public ByteBuf content() {
        try {
            return this.getByteBuf();
        }
        catch (final IOException e) {
            throw new ChannelException(e);
        }
    }
    
    @Override
    protected void deallocate() {
        this.delete();
    }
    
    @Override
    public HttpData retain() {
        super.retain();
        return this;
    }
    
    @Override
    public HttpData retain(final int increment) {
        super.retain(increment);
        return this;
    }
    
    @Override
    public abstract HttpData touch();
    
    @Override
    public abstract HttpData touch(final Object p0);
    
    static {
        STRIP_PATTERN = Pattern.compile("(?:^\\s+|\\s+$|\\n)");
        REPLACE_PATTERN = Pattern.compile("[\\r\\t]");
    }
}
