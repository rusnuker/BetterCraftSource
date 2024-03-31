// 
// Decompiled by Procyon v0.6.0
// 

package it.unimi.dsi.fastutil.io;

import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;

public class FastMultiByteArrayInputStream extends MeasurableInputStream implements RepositionableStream
{
    public static final int SLICE_BITS = 10;
    public static final int SLICE_SIZE = 1024;
    public static final int SLICE_MASK = 1023;
    public byte[][] array;
    public byte[] current;
    public long length;
    private long position;
    
    public FastMultiByteArrayInputStream(final MeasurableInputStream is) throws IOException {
        this(is, is.length());
    }
    
    public FastMultiByteArrayInputStream(final InputStream is, long size) throws IOException {
        this.length = size;
        this.array = new byte[(int)((size + 1024L - 1L) / 1024L) + 1][];
        for (int i = 0; i < this.array.length - 1; ++i) {
            this.array[i] = new byte[(size >= 1024L) ? 1024 : ((int)size)];
            if (BinIO.loadBytes(is, this.array[i]) != this.array[i].length) {
                throw new EOFException();
            }
            size -= this.array[i].length;
        }
        this.current = this.array[0];
    }
    
    public FastMultiByteArrayInputStream(final FastMultiByteArrayInputStream is) {
        this.array = is.array;
        this.length = is.length;
        this.current = this.array[0];
    }
    
    public FastMultiByteArrayInputStream(final byte[] array) {
        if (array.length == 0) {
            this.array = new byte[1][];
        }
        else {
            (this.array = new byte[2][])[0] = array;
            this.length = array.length;
            this.current = array;
        }
    }
    
    @Override
    public int available() {
        return (int)Math.min(2147483647L, this.length - this.position);
    }
    
    @Override
    public long skip(long n) {
        if (n > this.length - this.position) {
            n = this.length - this.position;
        }
        this.position += n;
        this.updateCurrent();
        return n;
    }
    
    @Override
    public int read() {
        if (this.length == this.position) {
            return -1;
        }
        final int disp = (int)(this.position++ & 0x3FFL);
        if (disp == 0) {
            this.updateCurrent();
        }
        return this.current[disp] & 0xFF;
    }
    
    @Override
    public int read(final byte[] b, int offset, final int length) {
        final long remaining = this.length - this.position;
        if (remaining == 0L) {
            return (length == 0) ? 0 : -1;
        }
        final int m;
        int n = m = (int)Math.min(length, remaining);
        while (true) {
            final int disp = (int)(this.position & 0x3FFL);
            if (disp == 0) {
                this.updateCurrent();
            }
            final int res = Math.min(n, this.current.length - disp);
            System.arraycopy(this.current, disp, b, offset, res);
            n -= res;
            this.position += res;
            if (n == 0) {
                break;
            }
            offset += res;
        }
        return m;
    }
    
    private void updateCurrent() {
        this.current = this.array[(int)(this.position >>> 10)];
    }
    
    @Override
    public long position() {
        return this.position;
    }
    
    @Override
    public void position(final long newPosition) {
        this.position = Math.min(newPosition, this.length);
        this.updateCurrent();
    }
    
    @Override
    public long length() throws IOException {
        return this.length;
    }
    
    @Override
    public void close() {
    }
    
    @Override
    public boolean markSupported() {
        return false;
    }
    
    @Override
    public void mark(final int dummy) {
        throw new UnsupportedOperationException();
    }
    
    @Override
    public void reset() {
        throw new UnsupportedOperationException();
    }
}
