// 
// Decompiled by Procyon v0.6.0
// 

package net.jpountz.lz4;

import java.io.EOFException;
import net.jpountz.util.SafeUtils;
import java.io.IOException;
import net.jpountz.xxhash.XXHashFactory;
import java.io.InputStream;
import java.util.zip.Checksum;
import java.io.FilterInputStream;

public class LZ4BlockInputStream extends FilterInputStream
{
    private final LZ4FastDecompressor decompressor;
    private final Checksum checksum;
    private final boolean stopOnEmptyBlock;
    private byte[] buffer;
    private byte[] compressedBuffer;
    private int originalLen;
    private int o;
    private boolean finished;
    
    public LZ4BlockInputStream(final InputStream in, final LZ4FastDecompressor decompressor, final Checksum checksum, final boolean stopOnEmptyBlock) {
        super(in);
        this.decompressor = decompressor;
        this.checksum = checksum;
        this.stopOnEmptyBlock = stopOnEmptyBlock;
        this.buffer = new byte[0];
        this.compressedBuffer = new byte[LZ4BlockOutputStream.HEADER_LENGTH];
        final int n = 0;
        this.originalLen = n;
        this.o = n;
        this.finished = false;
    }
    
    public LZ4BlockInputStream(final InputStream in, final LZ4FastDecompressor decompressor, final Checksum checksum) {
        this(in, decompressor, checksum, true);
    }
    
    public LZ4BlockInputStream(final InputStream in, final LZ4FastDecompressor decompressor) {
        this(in, decompressor, XXHashFactory.fastestInstance().newStreamingHash32(-1756908916).asChecksum(), true);
    }
    
    public LZ4BlockInputStream(final InputStream in, final boolean stopOnEmptyBlock) {
        this(in, LZ4Factory.fastestInstance().fastDecompressor(), XXHashFactory.fastestInstance().newStreamingHash32(-1756908916).asChecksum(), stopOnEmptyBlock);
    }
    
    public LZ4BlockInputStream(final InputStream in) {
        this(in, LZ4Factory.fastestInstance().fastDecompressor());
    }
    
    @Override
    public int available() throws IOException {
        return this.originalLen - this.o;
    }
    
    @Override
    public int read() throws IOException {
        if (this.finished) {
            return -1;
        }
        if (this.o == this.originalLen) {
            this.refill();
        }
        if (this.finished) {
            return -1;
        }
        return this.buffer[this.o++] & 0xFF;
    }
    
    @Override
    public int read(final byte[] b, final int off, int len) throws IOException {
        SafeUtils.checkRange(b, off, len);
        if (this.finished) {
            return -1;
        }
        if (this.o == this.originalLen) {
            this.refill();
        }
        if (this.finished) {
            return -1;
        }
        len = Math.min(len, this.originalLen - this.o);
        System.arraycopy(this.buffer, this.o, b, off, len);
        this.o += len;
        return len;
    }
    
    @Override
    public int read(final byte[] b) throws IOException {
        return this.read(b, 0, b.length);
    }
    
    @Override
    public long skip(final long n) throws IOException {
        if (n <= 0L || this.finished) {
            return 0L;
        }
        if (this.o == this.originalLen) {
            this.refill();
        }
        if (this.finished) {
            return 0L;
        }
        final int skipped = (int)Math.min(n, this.originalLen - this.o);
        this.o += skipped;
        return skipped;
    }
    
    private void refill() throws IOException {
        if (!this.tryReadFully(this.compressedBuffer, LZ4BlockOutputStream.HEADER_LENGTH)) {
            if (!this.stopOnEmptyBlock) {
                this.finished = true;
                return;
            }
            throw new EOFException("Stream ended prematurely");
        }
        else {
            for (int i = 0; i < LZ4BlockOutputStream.MAGIC_LENGTH; ++i) {
                if (this.compressedBuffer[i] != LZ4BlockOutputStream.MAGIC[i]) {
                    throw new IOException("Stream is corrupted");
                }
            }
            final int token = this.compressedBuffer[LZ4BlockOutputStream.MAGIC_LENGTH] & 0xFF;
            final int compressionMethod = token & 0xF0;
            final int compressionLevel = 10 + (token & 0xF);
            if (compressionMethod != 16 && compressionMethod != 32) {
                throw new IOException("Stream is corrupted");
            }
            final int compressedLen = SafeUtils.readIntLE(this.compressedBuffer, LZ4BlockOutputStream.MAGIC_LENGTH + 1);
            this.originalLen = SafeUtils.readIntLE(this.compressedBuffer, LZ4BlockOutputStream.MAGIC_LENGTH + 5);
            final int check = SafeUtils.readIntLE(this.compressedBuffer, LZ4BlockOutputStream.MAGIC_LENGTH + 9);
            assert LZ4BlockOutputStream.HEADER_LENGTH == LZ4BlockOutputStream.MAGIC_LENGTH + 13;
            if (this.originalLen > 1 << compressionLevel || this.originalLen < 0 || compressedLen < 0 || (this.originalLen == 0 && compressedLen != 0) || (this.originalLen != 0 && compressedLen == 0) || (compressionMethod == 16 && this.originalLen != compressedLen)) {
                throw new IOException("Stream is corrupted");
            }
            if (this.originalLen == 0 && compressedLen == 0) {
                if (check != 0) {
                    throw new IOException("Stream is corrupted");
                }
                if (!this.stopOnEmptyBlock) {
                    this.refill();
                }
                else {
                    this.finished = true;
                }
            }
            else {
                if (this.buffer.length < this.originalLen) {
                    this.buffer = new byte[Math.max(this.originalLen, this.buffer.length * 3 / 2)];
                }
                Label_0500: {
                    switch (compressionMethod) {
                        case 16: {
                            this.readFully(this.buffer, this.originalLen);
                            break Label_0500;
                        }
                        case 32: {
                            if (this.compressedBuffer.length < compressedLen) {
                                this.compressedBuffer = new byte[Math.max(compressedLen, this.compressedBuffer.length * 3 / 2)];
                            }
                            this.readFully(this.compressedBuffer, compressedLen);
                            try {
                                final int compressedLen2 = this.decompressor.decompress(this.compressedBuffer, 0, this.buffer, 0, this.originalLen);
                                if (compressedLen != compressedLen2) {
                                    throw new IOException("Stream is corrupted");
                                }
                                break Label_0500;
                            }
                            catch (final LZ4Exception e) {
                                throw new IOException("Stream is corrupted", e);
                            }
                            break;
                        }
                    }
                    throw new AssertionError();
                }
                this.checksum.reset();
                this.checksum.update(this.buffer, 0, this.originalLen);
                if ((int)this.checksum.getValue() != check) {
                    throw new IOException("Stream is corrupted");
                }
                this.o = 0;
            }
        }
    }
    
    private boolean tryReadFully(final byte[] b, final int len) throws IOException {
        int read;
        int r;
        for (read = 0; read < len; read += r) {
            r = this.in.read(b, read, len - read);
            if (r < 0) {
                return false;
            }
        }
        assert len == read;
        return true;
    }
    
    private void readFully(final byte[] b, final int len) throws IOException {
        if (!this.tryReadFully(b, len)) {
            throw new EOFException("Stream ended prematurely");
        }
    }
    
    @Override
    public boolean markSupported() {
        return false;
    }
    
    @Override
    public void mark(final int readlimit) {
    }
    
    @Override
    public void reset() throws IOException {
        throw new IOException("mark/reset not supported");
    }
    
    @Override
    public String toString() {
        return this.getClass().getSimpleName() + "(in=" + this.in + ", decompressor=" + this.decompressor + ", checksum=" + this.checksum + ")";
    }
}
