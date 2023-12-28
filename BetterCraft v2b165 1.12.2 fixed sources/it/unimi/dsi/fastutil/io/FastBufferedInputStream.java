// 
// Decompiled by Procyon v0.6.0
// 

package it.unimi.dsi.fastutil.io;

import it.unimi.dsi.fastutil.bytes.ByteArrays;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.nio.channels.FileChannel;
import java.io.InputStream;
import java.util.EnumSet;

public class FastBufferedInputStream extends MeasurableInputStream implements RepositionableStream
{
    public static final int DEFAULT_BUFFER_SIZE = 8192;
    public static final EnumSet<LineTerminator> ALL_TERMINATORS;
    protected InputStream is;
    protected byte[] buffer;
    protected int pos;
    protected long readBytes;
    protected int avail;
    private FileChannel fileChannel;
    private RepositionableStream repositionableStream;
    private MeasurableStream measurableStream;
    
    private static int ensureBufferSize(final int bufferSize) {
        if (bufferSize <= 0) {
            throw new IllegalArgumentException("Illegal buffer size: " + bufferSize);
        }
        return bufferSize;
    }
    
    public FastBufferedInputStream(final InputStream is, final byte[] buffer) {
        this.is = is;
        ensureBufferSize(buffer.length);
        this.buffer = buffer;
        if (is instanceof RepositionableStream) {
            this.repositionableStream = (RepositionableStream)is;
        }
        if (is instanceof MeasurableStream) {
            this.measurableStream = (MeasurableStream)is;
        }
        if (this.repositionableStream == null) {
            try {
                this.fileChannel = (FileChannel)is.getClass().getMethod("getChannel", (Class<?>[])new Class[0]).invoke(is, new Object[0]);
            }
            catch (final IllegalAccessException ex) {}
            catch (final IllegalArgumentException ex2) {}
            catch (final NoSuchMethodException ex3) {}
            catch (final InvocationTargetException ex4) {}
            catch (final ClassCastException ex5) {}
        }
    }
    
    public FastBufferedInputStream(final InputStream is, final int bufferSize) {
        this(is, new byte[ensureBufferSize(bufferSize)]);
    }
    
    public FastBufferedInputStream(final InputStream is) {
        this(is, 8192);
    }
    
    protected boolean noMoreCharacters() throws IOException {
        if (this.avail == 0) {
            this.avail = this.is.read(this.buffer);
            if (this.avail <= 0) {
                this.avail = 0;
                return true;
            }
            this.pos = 0;
        }
        return false;
    }
    
    @Override
    public int read() throws IOException {
        if (this.noMoreCharacters()) {
            return -1;
        }
        --this.avail;
        ++this.readBytes;
        return this.buffer[this.pos++] & 0xFF;
    }
    
    @Override
    public int read(final byte[] b, final int offset, final int length) throws IOException {
        if (length <= this.avail) {
            System.arraycopy(this.buffer, this.pos, b, offset, length);
            this.pos += length;
            this.avail -= length;
            this.readBytes += length;
            return length;
        }
        final int head = this.avail;
        System.arraycopy(this.buffer, this.pos, b, offset, head);
        final int n = 0;
        this.avail = n;
        this.pos = n;
        this.readBytes += head;
        if (length > this.buffer.length) {
            final int result = this.is.read(b, offset + head, length - head);
            if (result > 0) {
                this.readBytes += result;
            }
            return (result < 0) ? ((head == 0) ? -1 : head) : (result + head);
        }
        if (this.noMoreCharacters()) {
            return (head == 0) ? -1 : head;
        }
        final int toRead = Math.min(length - head, this.avail);
        this.readBytes += toRead;
        System.arraycopy(this.buffer, 0, b, offset + head, toRead);
        this.pos = toRead;
        this.avail -= toRead;
        return toRead + head;
    }
    
    public int readLine(final byte[] array) throws IOException {
        return this.readLine(array, 0, array.length, FastBufferedInputStream.ALL_TERMINATORS);
    }
    
    public int readLine(final byte[] array, final EnumSet<LineTerminator> terminators) throws IOException {
        return this.readLine(array, 0, array.length, terminators);
    }
    
    public int readLine(final byte[] array, final int off, final int len) throws IOException {
        return this.readLine(array, off, len, FastBufferedInputStream.ALL_TERMINATORS);
    }
    
    public int readLine(final byte[] array, final int off, final int len, final EnumSet<LineTerminator> terminators) throws IOException {
        ByteArrays.ensureOffsetLength(array, off, len);
        if (len == 0) {
            return 0;
        }
        if (this.noMoreCharacters()) {
            return -1;
        }
        int k = 0;
        int remaining = len;
        int read = 0;
        while (true) {
            int i;
            for (i = 0; i < this.avail && i < remaining && (k = this.buffer[this.pos + i]) != 10 && k != 13; ++i) {}
            System.arraycopy(this.buffer, this.pos, array, off + read, i);
            this.pos += i;
            this.avail -= i;
            read += i;
            remaining -= i;
            if (remaining == 0) {
                this.readBytes += read;
                return read;
            }
            if (this.avail > 0) {
                if (k == 10) {
                    ++this.pos;
                    --this.avail;
                    if (terminators.contains(LineTerminator.LF)) {
                        this.readBytes += read + 1;
                        return read;
                    }
                    array[off + read++] = 10;
                    --remaining;
                }
                else {
                    if (k != 13) {
                        continue;
                    }
                    ++this.pos;
                    --this.avail;
                    if (terminators.contains(LineTerminator.CR_LF)) {
                        if (this.avail > 0) {
                            if (this.buffer[this.pos] == 10) {
                                ++this.pos;
                                --this.avail;
                                this.readBytes += read + 2;
                                return read;
                            }
                        }
                        else {
                            if (this.noMoreCharacters()) {
                                if (!terminators.contains(LineTerminator.CR)) {
                                    array[off + read++] = 13;
                                    --remaining;
                                    this.readBytes += read;
                                }
                                else {
                                    this.readBytes += read + 1;
                                }
                                return read;
                            }
                            if (this.buffer[0] == 10) {
                                ++this.pos;
                                --this.avail;
                                this.readBytes += read + 2;
                                return read;
                            }
                        }
                    }
                    if (terminators.contains(LineTerminator.CR)) {
                        this.readBytes += read + 1;
                        return read;
                    }
                    array[off + read++] = 13;
                    --remaining;
                }
            }
            else {
                if (this.noMoreCharacters()) {
                    this.readBytes += read;
                    return read;
                }
                continue;
            }
        }
    }
    
    @Override
    public void position(final long newPosition) throws IOException {
        final long position = this.readBytes;
        if (newPosition <= position + this.avail && newPosition >= position - this.pos) {
            this.pos += (int)(newPosition - position);
            this.avail -= (int)(newPosition - position);
            this.readBytes = newPosition;
            return;
        }
        if (this.repositionableStream != null) {
            this.repositionableStream.position(newPosition);
        }
        else {
            if (this.fileChannel == null) {
                throw new UnsupportedOperationException("position() can only be called if the underlying byte stream implements the RepositionableStream interface or if the getChannel() method of the underlying byte stream exists and returns a FileChannel");
            }
            this.fileChannel.position(newPosition);
        }
        this.readBytes = newPosition;
        final int n = 0;
        this.pos = n;
        this.avail = n;
    }
    
    @Override
    public long position() throws IOException {
        return this.readBytes;
    }
    
    @Override
    public long length() throws IOException {
        if (this.measurableStream != null) {
            return this.measurableStream.length();
        }
        if (this.fileChannel != null) {
            return this.fileChannel.size();
        }
        throw new UnsupportedOperationException();
    }
    
    private long skipByReading(final long n) throws IOException {
        long toSkip;
        int len;
        for (toSkip = n; toSkip > 0L; toSkip -= len) {
            len = this.is.read(this.buffer, 0, (int)Math.min(this.buffer.length, toSkip));
            if (len <= 0) {
                break;
            }
        }
        return n - toSkip;
    }
    
    @Override
    public long skip(final long n) throws IOException {
        if (n <= this.avail) {
            final int m = (int)n;
            this.pos += m;
            this.avail -= m;
            this.readBytes += n;
            return n;
        }
        long toSkip = n - this.avail;
        long result = 0L;
        this.avail = 0;
        while (toSkip != 0L && (result = ((this.is == System.in) ? this.skipByReading(toSkip) : this.is.skip(toSkip))) < toSkip) {
            if (result == 0L) {
                if (this.is.read() == -1) {
                    break;
                }
                --toSkip;
            }
            else {
                toSkip -= result;
            }
        }
        final long t = n - (toSkip - result);
        this.readBytes += t;
        return t;
    }
    
    @Override
    public int available() throws IOException {
        return (int)Math.min(this.is.available() + (long)this.avail, 2147483647L);
    }
    
    @Override
    public void close() throws IOException {
        if (this.is == null) {
            return;
        }
        if (this.is != System.in) {
            this.is.close();
        }
        this.is = null;
        this.buffer = null;
    }
    
    public void flush() {
        if (this.is == null) {
            return;
        }
        this.readBytes += this.avail;
        final int n = 0;
        this.pos = n;
        this.avail = n;
    }
    
    @Deprecated
    @Override
    public void reset() {
        this.flush();
    }
    
    static {
        ALL_TERMINATORS = EnumSet.allOf(LineTerminator.class);
    }
    
    public enum LineTerminator
    {
        CR, 
        LF, 
        CR_LF;
    }
}
