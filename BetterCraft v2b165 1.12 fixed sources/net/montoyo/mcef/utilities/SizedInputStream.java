// 
// Decompiled by Procyon v0.6.0
// 

package net.montoyo.mcef.utilities;

import java.io.IOException;
import java.io.InputStream;

public class SizedInputStream extends InputStream
{
    private InputStream source;
    private long length;
    private long lenCnt;
    
    public SizedInputStream(final InputStream is, final long len) {
        this.source = is;
        this.length = len;
        this.lenCnt = 0L;
    }
    
    public long getContentLength() {
        return this.length;
    }
    
    @Override
    public int read() throws IOException {
        final int data = this.source.read();
        if (data >= 0) {
            ++this.lenCnt;
        }
        return data;
    }
    
    @Override
    public int read(final byte[] data) throws IOException {
        final int ret = this.source.read(data);
        if (ret > 0) {
            this.lenCnt += ret;
        }
        return ret;
    }
    
    @Override
    public int read(final byte[] data, final int off, final int len) throws IOException {
        final int ret = this.source.read(data, off, len);
        if (ret > 0) {
            this.lenCnt += ret;
        }
        return ret;
    }
    
    @Override
    public long skip(final long s) throws IOException {
        final long ret = this.source.skip(s);
        if (ret > 0L) {
            this.lenCnt += ret;
        }
        return ret;
    }
    
    @Override
    public int available() throws IOException {
        return this.source.available();
    }
    
    @Override
    public void close() throws IOException {
        this.source.close();
    }
    
    @Override
    public synchronized void mark(final int limit) {
        this.source.mark(limit);
    }
    
    @Override
    public synchronized void reset() throws IOException {
        this.source.reset();
    }
    
    public long resetLengthCounter() {
        final long cpy = this.lenCnt;
        this.lenCnt = 0L;
        return cpy;
    }
    
    @Override
    public boolean markSupported() {
        return this.source.markSupported();
    }
}
