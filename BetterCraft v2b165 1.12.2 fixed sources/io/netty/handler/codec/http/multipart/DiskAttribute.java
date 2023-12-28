// 
// Decompiled by Procyon v0.6.0
// 

package io.netty.handler.codec.http.multipart;

import io.netty.buffer.ByteBufHolder;
import io.netty.util.ReferenceCounted;
import io.netty.channel.ChannelException;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import java.io.IOException;
import java.nio.charset.Charset;
import io.netty.handler.codec.http.HttpConstants;

public class DiskAttribute extends AbstractDiskHttpData implements Attribute
{
    public static String baseDirectory;
    public static boolean deleteOnExitTemporaryFile;
    public static final String prefix = "Attr_";
    public static final String postfix = ".att";
    
    public DiskAttribute(final String name) {
        this(name, HttpConstants.DEFAULT_CHARSET);
    }
    
    public DiskAttribute(final String name, final long definedSize) {
        this(name, definedSize, HttpConstants.DEFAULT_CHARSET);
    }
    
    public DiskAttribute(final String name, final Charset charset) {
        super(name, charset, 0L);
    }
    
    public DiskAttribute(final String name, final long definedSize, final Charset charset) {
        super(name, charset, definedSize);
    }
    
    public DiskAttribute(final String name, final String value) throws IOException {
        this(name, value, HttpConstants.DEFAULT_CHARSET);
    }
    
    public DiskAttribute(final String name, final String value, final Charset charset) throws IOException {
        super(name, charset, 0L);
        this.setValue(value);
    }
    
    @Override
    public InterfaceHttpData.HttpDataType getHttpDataType() {
        return InterfaceHttpData.HttpDataType.Attribute;
    }
    
    @Override
    public String getValue() throws IOException {
        final byte[] bytes = this.get();
        return new String(bytes, this.getCharset());
    }
    
    @Override
    public void setValue(final String value) throws IOException {
        if (value == null) {
            throw new NullPointerException("value");
        }
        final byte[] bytes = value.getBytes(this.getCharset());
        this.checkSize(bytes.length);
        final ByteBuf buffer = Unpooled.wrappedBuffer(bytes);
        if (this.definedSize > 0L) {
            this.definedSize = buffer.readableBytes();
        }
        this.setContent(buffer);
    }
    
    @Override
    public void addContent(final ByteBuf buffer, final boolean last) throws IOException {
        final long newDefinedSize = this.size + buffer.readableBytes();
        this.checkSize(newDefinedSize);
        if (this.definedSize > 0L && this.definedSize < newDefinedSize) {
            this.definedSize = newDefinedSize;
        }
        super.addContent(buffer, last);
    }
    
    @Override
    public int hashCode() {
        return this.getName().hashCode();
    }
    
    @Override
    public boolean equals(final Object o) {
        if (!(o instanceof Attribute)) {
            return false;
        }
        final Attribute attribute = (Attribute)o;
        return this.getName().equalsIgnoreCase(attribute.getName());
    }
    
    @Override
    public int compareTo(final InterfaceHttpData o) {
        if (!(o instanceof Attribute)) {
            throw new ClassCastException("Cannot compare " + this.getHttpDataType() + " with " + o.getHttpDataType());
        }
        return this.compareTo((Attribute)o);
    }
    
    public int compareTo(final Attribute o) {
        return this.getName().compareToIgnoreCase(o.getName());
    }
    
    @Override
    public String toString() {
        try {
            return this.getName() + '=' + this.getValue();
        }
        catch (final IOException e) {
            return this.getName() + '=' + e;
        }
    }
    
    @Override
    protected boolean deleteOnExit() {
        return DiskAttribute.deleteOnExitTemporaryFile;
    }
    
    @Override
    protected String getBaseDirectory() {
        return DiskAttribute.baseDirectory;
    }
    
    @Override
    protected String getDiskFilename() {
        return this.getName() + ".att";
    }
    
    @Override
    protected String getPostfix() {
        return ".att";
    }
    
    @Override
    protected String getPrefix() {
        return "Attr_";
    }
    
    @Override
    public Attribute copy() {
        final ByteBuf content = this.content();
        return this.replace((content != null) ? content.copy() : null);
    }
    
    @Override
    public Attribute duplicate() {
        final ByteBuf content = this.content();
        return this.replace((content != null) ? content.duplicate() : null);
    }
    
    @Override
    public Attribute retainedDuplicate() {
        ByteBuf content = this.content();
        if (content != null) {
            content = content.retainedDuplicate();
            boolean success = false;
            try {
                final Attribute duplicate = this.replace(content);
                success = true;
                return duplicate;
            }
            finally {
                if (!success) {
                    content.release();
                }
            }
        }
        return this.replace(null);
    }
    
    @Override
    public Attribute replace(final ByteBuf content) {
        final DiskAttribute attr = new DiskAttribute(this.getName());
        attr.setCharset(this.getCharset());
        if (content != null) {
            try {
                attr.setContent(content);
            }
            catch (final IOException e) {
                throw new ChannelException(e);
            }
        }
        return attr;
    }
    
    @Override
    public Attribute retain(final int increment) {
        super.retain(increment);
        return this;
    }
    
    @Override
    public Attribute retain() {
        super.retain();
        return this;
    }
    
    @Override
    public Attribute touch() {
        super.touch();
        return this;
    }
    
    @Override
    public Attribute touch(final Object hint) {
        super.touch(hint);
        return this;
    }
    
    static {
        DiskAttribute.deleteOnExitTemporaryFile = true;
    }
}
