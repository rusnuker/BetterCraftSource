// 
// Decompiled by Procyon v0.6.0
// 

package io.netty.handler.codec.http.multipart;

import io.netty.buffer.ByteBufHolder;
import io.netty.util.ReferenceCounted;
import java.io.InputStream;
import java.io.File;
import io.netty.buffer.ByteBuf;
import java.io.IOException;
import java.nio.charset.Charset;
import io.netty.handler.codec.http.HttpConstants;

public class MixedAttribute implements Attribute
{
    private Attribute attribute;
    private final long limitSize;
    private long maxSize;
    
    public MixedAttribute(final String name, final long limitSize) {
        this(name, limitSize, HttpConstants.DEFAULT_CHARSET);
    }
    
    public MixedAttribute(final String name, final long definedSize, final long limitSize) {
        this(name, definedSize, limitSize, HttpConstants.DEFAULT_CHARSET);
    }
    
    public MixedAttribute(final String name, final long limitSize, final Charset charset) {
        this.maxSize = -1L;
        this.limitSize = limitSize;
        this.attribute = new MemoryAttribute(name, charset);
    }
    
    public MixedAttribute(final String name, final long definedSize, final long limitSize, final Charset charset) {
        this.maxSize = -1L;
        this.limitSize = limitSize;
        this.attribute = new MemoryAttribute(name, definedSize, charset);
    }
    
    public MixedAttribute(final String name, final String value, final long limitSize) {
        this(name, value, limitSize, HttpConstants.DEFAULT_CHARSET);
    }
    
    public MixedAttribute(final String name, final String value, final long limitSize, final Charset charset) {
        this.maxSize = -1L;
        this.limitSize = limitSize;
        if (value.length() > this.limitSize) {
            try {
                this.attribute = new DiskAttribute(name, value, charset);
            }
            catch (final IOException e) {
                try {
                    this.attribute = new MemoryAttribute(name, value, charset);
                }
                catch (final IOException ignore) {
                    throw new IllegalArgumentException(e);
                }
            }
        }
        else {
            try {
                this.attribute = new MemoryAttribute(name, value, charset);
            }
            catch (final IOException e) {
                throw new IllegalArgumentException(e);
            }
        }
    }
    
    @Override
    public long getMaxSize() {
        return this.maxSize;
    }
    
    @Override
    public void setMaxSize(final long maxSize) {
        this.maxSize = maxSize;
        this.attribute.setMaxSize(maxSize);
    }
    
    @Override
    public void checkSize(final long newSize) throws IOException {
        if (this.maxSize >= 0L && newSize > this.maxSize) {
            throw new IOException("Size exceed allowed maximum capacity");
        }
    }
    
    @Override
    public void addContent(final ByteBuf buffer, final boolean last) throws IOException {
        if (this.attribute instanceof MemoryAttribute) {
            this.checkSize(this.attribute.length() + buffer.readableBytes());
            if (this.attribute.length() + buffer.readableBytes() > this.limitSize) {
                final DiskAttribute diskAttribute = new DiskAttribute(this.attribute.getName(), this.attribute.definedLength());
                diskAttribute.setMaxSize(this.maxSize);
                if (((MemoryAttribute)this.attribute).getByteBuf() != null) {
                    diskAttribute.addContent(((MemoryAttribute)this.attribute).getByteBuf(), false);
                }
                this.attribute = diskAttribute;
            }
        }
        this.attribute.addContent(buffer, last);
    }
    
    @Override
    public void delete() {
        this.attribute.delete();
    }
    
    @Override
    public byte[] get() throws IOException {
        return this.attribute.get();
    }
    
    @Override
    public ByteBuf getByteBuf() throws IOException {
        return this.attribute.getByteBuf();
    }
    
    @Override
    public Charset getCharset() {
        return this.attribute.getCharset();
    }
    
    @Override
    public String getString() throws IOException {
        return this.attribute.getString();
    }
    
    @Override
    public String getString(final Charset encoding) throws IOException {
        return this.attribute.getString(encoding);
    }
    
    @Override
    public boolean isCompleted() {
        return this.attribute.isCompleted();
    }
    
    @Override
    public boolean isInMemory() {
        return this.attribute.isInMemory();
    }
    
    @Override
    public long length() {
        return this.attribute.length();
    }
    
    @Override
    public long definedLength() {
        return this.attribute.definedLength();
    }
    
    @Override
    public boolean renameTo(final File dest) throws IOException {
        return this.attribute.renameTo(dest);
    }
    
    @Override
    public void setCharset(final Charset charset) {
        this.attribute.setCharset(charset);
    }
    
    @Override
    public void setContent(final ByteBuf buffer) throws IOException {
        this.checkSize(buffer.readableBytes());
        if (buffer.readableBytes() > this.limitSize && this.attribute instanceof MemoryAttribute) {
            (this.attribute = new DiskAttribute(this.attribute.getName(), this.attribute.definedLength())).setMaxSize(this.maxSize);
        }
        this.attribute.setContent(buffer);
    }
    
    @Override
    public void setContent(final File file) throws IOException {
        this.checkSize(file.length());
        if (file.length() > this.limitSize && this.attribute instanceof MemoryAttribute) {
            (this.attribute = new DiskAttribute(this.attribute.getName(), this.attribute.definedLength())).setMaxSize(this.maxSize);
        }
        this.attribute.setContent(file);
    }
    
    @Override
    public void setContent(final InputStream inputStream) throws IOException {
        if (this.attribute instanceof MemoryAttribute) {
            (this.attribute = new DiskAttribute(this.attribute.getName(), this.attribute.definedLength())).setMaxSize(this.maxSize);
        }
        this.attribute.setContent(inputStream);
    }
    
    @Override
    public InterfaceHttpData.HttpDataType getHttpDataType() {
        return this.attribute.getHttpDataType();
    }
    
    @Override
    public String getName() {
        return this.attribute.getName();
    }
    
    @Override
    public int hashCode() {
        return this.attribute.hashCode();
    }
    
    @Override
    public boolean equals(final Object obj) {
        return this.attribute.equals(obj);
    }
    
    @Override
    public int compareTo(final InterfaceHttpData o) {
        return this.attribute.compareTo(o);
    }
    
    @Override
    public String toString() {
        return "Mixed: " + this.attribute;
    }
    
    @Override
    public String getValue() throws IOException {
        return this.attribute.getValue();
    }
    
    @Override
    public void setValue(final String value) throws IOException {
        if (value != null) {
            this.checkSize(value.getBytes().length);
        }
        this.attribute.setValue(value);
    }
    
    @Override
    public ByteBuf getChunk(final int length) throws IOException {
        return this.attribute.getChunk(length);
    }
    
    @Override
    public File getFile() throws IOException {
        return this.attribute.getFile();
    }
    
    @Override
    public Attribute copy() {
        return this.attribute.copy();
    }
    
    @Override
    public Attribute duplicate() {
        return this.attribute.duplicate();
    }
    
    @Override
    public Attribute retainedDuplicate() {
        return this.attribute.retainedDuplicate();
    }
    
    @Override
    public Attribute replace(final ByteBuf content) {
        return this.attribute.replace(content);
    }
    
    @Override
    public ByteBuf content() {
        return this.attribute.content();
    }
    
    @Override
    public int refCnt() {
        return this.attribute.refCnt();
    }
    
    @Override
    public Attribute retain() {
        this.attribute.retain();
        return this;
    }
    
    @Override
    public Attribute retain(final int increment) {
        this.attribute.retain(increment);
        return this;
    }
    
    @Override
    public Attribute touch() {
        this.attribute.touch();
        return this;
    }
    
    @Override
    public Attribute touch(final Object hint) {
        this.attribute.touch(hint);
        return this;
    }
    
    @Override
    public boolean release() {
        return this.attribute.release();
    }
    
    @Override
    public boolean release(final int decrement) {
        return this.attribute.release(decrement);
    }
}
