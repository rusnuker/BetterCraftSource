// 
// Decompiled by Procyon v0.6.0
// 

package it.unimi.dsi.fastutil.io;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.nio.channels.FileChannel;
import java.io.OutputStream;

public class FastBufferedOutputStream extends MeasurableOutputStream implements RepositionableStream
{
    private static final boolean ASSERTS = false;
    public static final int DEFAULT_BUFFER_SIZE = 8192;
    protected byte[] buffer;
    protected int pos;
    protected int avail;
    protected OutputStream os;
    private FileChannel fileChannel;
    private RepositionableStream repositionableStream;
    private MeasurableStream measurableStream;
    
    private static int ensureBufferSize(final int bufferSize) {
        if (bufferSize <= 0) {
            throw new IllegalArgumentException("Illegal buffer size: " + bufferSize);
        }
        return bufferSize;
    }
    
    public FastBufferedOutputStream(final OutputStream os, final byte[] buffer) {
        this.os = os;
        ensureBufferSize(buffer.length);
        this.buffer = buffer;
        this.avail = buffer.length;
        if (os instanceof RepositionableStream) {
            this.repositionableStream = (RepositionableStream)os;
        }
        if (os instanceof MeasurableStream) {
            this.measurableStream = (MeasurableStream)os;
        }
        if (this.repositionableStream == null) {
            try {
                this.fileChannel = (FileChannel)os.getClass().getMethod("getChannel", (Class<?>[])new Class[0]).invoke(os, new Object[0]);
            }
            catch (final IllegalAccessException ex) {}
            catch (final IllegalArgumentException ex2) {}
            catch (final NoSuchMethodException ex3) {}
            catch (final InvocationTargetException ex4) {}
            catch (final ClassCastException ex5) {}
        }
    }
    
    public FastBufferedOutputStream(final OutputStream os, final int bufferSize) {
        this(os, new byte[ensureBufferSize(bufferSize)]);
    }
    
    public FastBufferedOutputStream(final OutputStream os) {
        this(os, 8192);
    }
    
    private void dumpBuffer(final boolean ifFull) throws IOException {
        if (!ifFull || this.avail == 0) {
            this.os.write(this.buffer, 0, this.pos);
            this.pos = 0;
            this.avail = this.buffer.length;
        }
    }
    
    @Override
    public void write(final int b) throws IOException {
        --this.avail;
        this.buffer[this.pos++] = (byte)b;
        this.dumpBuffer(true);
    }
    
    @Override
    public void write(final byte[] b, final int offset, final int length) throws IOException {
        if (length >= this.buffer.length) {
            this.dumpBuffer(false);
            this.os.write(b, offset, length);
            return;
        }
        if (length <= this.avail) {
            System.arraycopy(b, offset, this.buffer, this.pos, length);
            this.pos += length;
            this.avail -= length;
            this.dumpBuffer(true);
            return;
        }
        this.dumpBuffer(false);
        System.arraycopy(b, offset, this.buffer, 0, length);
        this.pos = length;
        this.avail -= length;
    }
    
    @Override
    public void flush() throws IOException {
        this.dumpBuffer(false);
        this.os.flush();
    }
    
    @Override
    public void close() throws IOException {
        if (this.os == null) {
            return;
        }
        this.flush();
        if (this.os != System.out) {
            this.os.close();
        }
        this.os = null;
        this.buffer = null;
    }
    
    @Override
    public long position() throws IOException {
        if (this.repositionableStream != null) {
            return this.repositionableStream.position() + this.pos;
        }
        if (this.measurableStream != null) {
            return this.measurableStream.position() + this.pos;
        }
        if (this.fileChannel != null) {
            return this.fileChannel.position() + this.pos;
        }
        throw new UnsupportedOperationException("position() can only be called if the underlying byte stream implements the MeasurableStream or RepositionableStream interface or if the getChannel() method of the underlying byte stream exists and returns a FileChannel");
    }
    
    @Override
    public void position(final long newPosition) throws IOException {
        this.flush();
        if (this.repositionableStream != null) {
            this.repositionableStream.position(newPosition);
        }
        else {
            if (this.fileChannel == null) {
                throw new UnsupportedOperationException("position() can only be called if the underlying byte stream implements the RepositionableStream interface or if the getChannel() method of the underlying byte stream exists and returns a FileChannel");
            }
            this.fileChannel.position(newPosition);
        }
    }
    
    @Override
    public long length() throws IOException {
        this.flush();
        if (this.measurableStream != null) {
            return this.measurableStream.length();
        }
        if (this.fileChannel != null) {
            return this.fileChannel.size();
        }
        throw new UnsupportedOperationException();
    }
}
