// 
// Decompiled by Procyon v0.6.0
// 

package net.jpountz.lz4;

import net.jpountz.util.SafeUtils;
import java.io.IOException;
import net.jpountz.xxhash.XXHashFactory;
import java.io.OutputStream;
import java.util.zip.Checksum;
import java.io.FilterOutputStream;

public class LZ4BlockOutputStream extends FilterOutputStream
{
    static final byte[] MAGIC;
    static final int MAGIC_LENGTH;
    static final int HEADER_LENGTH;
    static final int COMPRESSION_LEVEL_BASE = 10;
    static final int MIN_BLOCK_SIZE = 64;
    static final int MAX_BLOCK_SIZE = 33554432;
    static final int COMPRESSION_METHOD_RAW = 16;
    static final int COMPRESSION_METHOD_LZ4 = 32;
    static final int DEFAULT_SEED = -1756908916;
    private final int blockSize;
    private final int compressionLevel;
    private final LZ4Compressor compressor;
    private final Checksum checksum;
    private final byte[] buffer;
    private final byte[] compressedBuffer;
    private final boolean syncFlush;
    private boolean finished;
    private int o;
    
    private static int compressionLevel(final int blockSize) {
        if (blockSize < 64) {
            throw new IllegalArgumentException("blockSize must be >= 64, got " + blockSize);
        }
        if (blockSize > 33554432) {
            throw new IllegalArgumentException("blockSize must be <= 33554432, got " + blockSize);
        }
        int compressionLevel = 32 - Integer.numberOfLeadingZeros(blockSize - 1);
        assert 1 << compressionLevel >= blockSize;
        assert blockSize * 2 > 1 << compressionLevel;
        compressionLevel = Math.max(0, compressionLevel - 10);
        assert compressionLevel >= 0 && compressionLevel <= 15;
        return compressionLevel;
    }
    
    public LZ4BlockOutputStream(final OutputStream out, final int blockSize, final LZ4Compressor compressor, final Checksum checksum, final boolean syncFlush) {
        super(out);
        this.blockSize = blockSize;
        this.compressor = compressor;
        this.checksum = checksum;
        this.compressionLevel = compressionLevel(blockSize);
        this.buffer = new byte[blockSize];
        final int compressedBlockSize = LZ4BlockOutputStream.HEADER_LENGTH + compressor.maxCompressedLength(blockSize);
        this.compressedBuffer = new byte[compressedBlockSize];
        this.syncFlush = syncFlush;
        this.o = 0;
        this.finished = false;
        System.arraycopy(LZ4BlockOutputStream.MAGIC, 0, this.compressedBuffer, 0, LZ4BlockOutputStream.MAGIC_LENGTH);
    }
    
    public LZ4BlockOutputStream(final OutputStream out, final int blockSize, final LZ4Compressor compressor) {
        this(out, blockSize, compressor, XXHashFactory.fastestInstance().newStreamingHash32(-1756908916).asChecksum(), false);
    }
    
    public LZ4BlockOutputStream(final OutputStream out, final int blockSize) {
        this(out, blockSize, LZ4Factory.fastestInstance().fastCompressor());
    }
    
    public LZ4BlockOutputStream(final OutputStream out) {
        this(out, 65536);
    }
    
    private void ensureNotFinished() {
        if (this.finished) {
            throw new IllegalStateException("This stream is already closed");
        }
    }
    
    @Override
    public void write(final int b) throws IOException {
        this.ensureNotFinished();
        if (this.o == this.blockSize) {
            this.flushBufferedData();
        }
        this.buffer[this.o++] = (byte)b;
    }
    
    @Override
    public void write(final byte[] b, int off, int len) throws IOException {
        SafeUtils.checkRange(b, off, len);
        this.ensureNotFinished();
        while (this.o + len > this.blockSize) {
            final int l = this.blockSize - this.o;
            System.arraycopy(b, off, this.buffer, this.o, this.blockSize - this.o);
            this.o = this.blockSize;
            this.flushBufferedData();
            off += l;
            len -= l;
        }
        System.arraycopy(b, off, this.buffer, this.o, len);
        this.o += len;
    }
    
    @Override
    public void write(final byte[] b) throws IOException {
        this.ensureNotFinished();
        this.write(b, 0, b.length);
    }
    
    @Override
    public void close() throws IOException {
        if (!this.finished) {
            this.finish();
        }
        if (this.out != null) {
            this.out.close();
            this.out = null;
        }
    }
    
    private void flushBufferedData() throws IOException {
        if (this.o == 0) {
            return;
        }
        this.checksum.reset();
        this.checksum.update(this.buffer, 0, this.o);
        final int check = (int)this.checksum.getValue();
        int compressedLength = this.compressor.compress(this.buffer, 0, this.o, this.compressedBuffer, LZ4BlockOutputStream.HEADER_LENGTH);
        int compressMethod;
        if (compressedLength >= this.o) {
            compressMethod = 16;
            compressedLength = this.o;
            System.arraycopy(this.buffer, 0, this.compressedBuffer, LZ4BlockOutputStream.HEADER_LENGTH, this.o);
        }
        else {
            compressMethod = 32;
        }
        this.compressedBuffer[LZ4BlockOutputStream.MAGIC_LENGTH] = (byte)(compressMethod | this.compressionLevel);
        writeIntLE(compressedLength, this.compressedBuffer, LZ4BlockOutputStream.MAGIC_LENGTH + 1);
        writeIntLE(this.o, this.compressedBuffer, LZ4BlockOutputStream.MAGIC_LENGTH + 5);
        writeIntLE(check, this.compressedBuffer, LZ4BlockOutputStream.MAGIC_LENGTH + 9);
        assert LZ4BlockOutputStream.MAGIC_LENGTH + 13 == LZ4BlockOutputStream.HEADER_LENGTH;
        this.out.write(this.compressedBuffer, 0, LZ4BlockOutputStream.HEADER_LENGTH + compressedLength);
        this.o = 0;
    }
    
    @Override
    public void flush() throws IOException {
        if (this.out != null) {
            if (this.syncFlush) {
                this.flushBufferedData();
            }
            this.out.flush();
        }
    }
    
    public void finish() throws IOException {
        this.ensureNotFinished();
        this.flushBufferedData();
        this.compressedBuffer[LZ4BlockOutputStream.MAGIC_LENGTH] = (byte)(0x10 | this.compressionLevel);
        writeIntLE(0, this.compressedBuffer, LZ4BlockOutputStream.MAGIC_LENGTH + 1);
        writeIntLE(0, this.compressedBuffer, LZ4BlockOutputStream.MAGIC_LENGTH + 5);
        writeIntLE(0, this.compressedBuffer, LZ4BlockOutputStream.MAGIC_LENGTH + 9);
        assert LZ4BlockOutputStream.MAGIC_LENGTH + 13 == LZ4BlockOutputStream.HEADER_LENGTH;
        this.out.write(this.compressedBuffer, 0, LZ4BlockOutputStream.HEADER_LENGTH);
        this.finished = true;
        this.out.flush();
    }
    
    private static void writeIntLE(final int i, final byte[] buf, int off) {
        buf[off++] = (byte)i;
        buf[off++] = (byte)(i >>> 8);
        buf[off++] = (byte)(i >>> 16);
        buf[off++] = (byte)(i >>> 24);
    }
    
    @Override
    public String toString() {
        return this.getClass().getSimpleName() + "(out=" + this.out + ", blockSize=" + this.blockSize + ", compressor=" + this.compressor + ", checksum=" + this.checksum + ")";
    }
    
    static {
        MAGIC = new byte[] { 76, 90, 52, 66, 108, 111, 99, 107 };
        MAGIC_LENGTH = LZ4BlockOutputStream.MAGIC.length;
        HEADER_LENGTH = LZ4BlockOutputStream.MAGIC_LENGTH + 1 + 4 + 4 + 4;
    }
}
