// 
// Decompiled by Procyon v0.6.0
// 

package io.netty.channel;

import io.netty.util.internal.logging.InternalLoggerFactory;
import io.netty.util.ReferenceCounted;
import io.netty.util.IllegalReferenceCountException;
import java.nio.channels.WritableByteChannel;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.channels.FileChannel;
import java.io.File;
import io.netty.util.internal.logging.InternalLogger;
import io.netty.util.AbstractReferenceCounted;

public class DefaultFileRegion extends AbstractReferenceCounted implements FileRegion
{
    private static final InternalLogger logger;
    private final File f;
    private final long position;
    private final long count;
    private long transferred;
    private FileChannel file;
    
    public DefaultFileRegion(final FileChannel file, final long position, final long count) {
        if (file == null) {
            throw new NullPointerException("file");
        }
        if (position < 0L) {
            throw new IllegalArgumentException("position must be >= 0 but was " + position);
        }
        if (count < 0L) {
            throw new IllegalArgumentException("count must be >= 0 but was " + count);
        }
        this.file = file;
        this.position = position;
        this.count = count;
        this.f = null;
    }
    
    public DefaultFileRegion(final File f, final long position, final long count) {
        if (f == null) {
            throw new NullPointerException("f");
        }
        if (position < 0L) {
            throw new IllegalArgumentException("position must be >= 0 but was " + position);
        }
        if (count < 0L) {
            throw new IllegalArgumentException("count must be >= 0 but was " + count);
        }
        this.position = position;
        this.count = count;
        this.f = f;
    }
    
    public boolean isOpen() {
        return this.file != null;
    }
    
    public void open() throws IOException {
        if (!this.isOpen() && this.refCnt() > 0) {
            this.file = new RandomAccessFile(this.f, "r").getChannel();
        }
    }
    
    @Override
    public long position() {
        return this.position;
    }
    
    @Override
    public long count() {
        return this.count;
    }
    
    @Deprecated
    @Override
    public long transfered() {
        return this.transferred;
    }
    
    @Override
    public long transferred() {
        return this.transferred;
    }
    
    @Override
    public long transferTo(final WritableByteChannel target, final long position) throws IOException {
        final long count = this.count - position;
        if (count < 0L || position < 0L) {
            throw new IllegalArgumentException("position out of range: " + position + " (expected: 0 - " + (this.count - 1L) + ')');
        }
        if (count == 0L) {
            return 0L;
        }
        if (this.refCnt() == 0) {
            throw new IllegalReferenceCountException(0);
        }
        this.open();
        final long written = this.file.transferTo(this.position + position, count, target);
        if (written > 0L) {
            this.transferred += written;
        }
        return written;
    }
    
    @Override
    protected void deallocate() {
        final FileChannel file = this.file;
        if (file == null) {
            return;
        }
        this.file = null;
        try {
            file.close();
        }
        catch (final IOException e) {
            if (DefaultFileRegion.logger.isWarnEnabled()) {
                DefaultFileRegion.logger.warn("Failed to close a file.", e);
            }
        }
    }
    
    @Override
    public FileRegion retain() {
        super.retain();
        return this;
    }
    
    @Override
    public FileRegion retain(final int increment) {
        super.retain(increment);
        return this;
    }
    
    @Override
    public FileRegion touch() {
        return this;
    }
    
    @Override
    public FileRegion touch(final Object hint) {
        return this;
    }
    
    static {
        logger = InternalLoggerFactory.getInstance(DefaultFileRegion.class);
    }
}
