// 
// Decompiled by Procyon v0.6.0
// 

package net.jpountz.lz4;

import java.util.Locale;
import java.nio.ByteOrder;
import java.io.IOException;
import net.jpountz.xxhash.XXHashFactory;
import java.io.InputStream;
import java.nio.ByteBuffer;
import net.jpountz.xxhash.XXHash32;
import java.io.FilterInputStream;

public class LZ4FrameInputStream extends FilterInputStream
{
    static final String PREMATURE_EOS = "Stream ended prematurely";
    static final String NOT_SUPPORTED = "Stream unsupported";
    static final String BLOCK_HASH_MISMATCH = "Block checksum mismatch";
    static final String DESCRIPTOR_HASH_MISMATCH = "Stream frame descriptor corrupted";
    static final int MAGIC_SKIPPABLE_BASE = 407710288;
    private final LZ4SafeDecompressor decompressor;
    private final XXHash32 checksum;
    private final byte[] headerArray;
    private final ByteBuffer headerBuffer;
    private final boolean readSingleFrame;
    private byte[] compressedBuffer;
    private ByteBuffer buffer;
    private byte[] rawBuffer;
    private int maxBlockSize;
    private long expectedContentSize;
    private long totalContentSize;
    private LZ4FrameOutputStream.FrameInfo frameInfo;
    private final ByteBuffer readNumberBuff;
    
    public LZ4FrameInputStream(final InputStream in) throws IOException {
        this(in, LZ4Factory.fastestInstance().safeDecompressor(), XXHashFactory.fastestInstance().hash32());
    }
    
    public LZ4FrameInputStream(final InputStream in, final boolean readSingleFrame) throws IOException {
        this(in, LZ4Factory.fastestInstance().safeDecompressor(), XXHashFactory.fastestInstance().hash32(), readSingleFrame);
    }
    
    public LZ4FrameInputStream(final InputStream in, final LZ4SafeDecompressor decompressor, final XXHash32 checksum) throws IOException {
        this(in, decompressor, checksum, false);
    }
    
    public LZ4FrameInputStream(final InputStream in, final LZ4SafeDecompressor decompressor, final XXHash32 checksum, final boolean readSingleFrame) throws IOException {
        super(in);
        this.headerArray = new byte[15];
        this.headerBuffer = ByteBuffer.wrap(this.headerArray).order(ByteOrder.LITTLE_ENDIAN);
        this.buffer = null;
        this.rawBuffer = null;
        this.maxBlockSize = -1;
        this.expectedContentSize = -1L;
        this.totalContentSize = 0L;
        this.frameInfo = null;
        this.readNumberBuff = ByteBuffer.allocate(8).order(ByteOrder.LITTLE_ENDIAN);
        this.decompressor = decompressor;
        this.checksum = checksum;
        this.readSingleFrame = readSingleFrame;
        this.nextFrameInfo();
    }
    
    private boolean nextFrameInfo() throws IOException {
        while (true) {
            int size = 0;
            do {
                final int mySize = this.in.read(this.readNumberBuff.array(), size, 4 - size);
                if (mySize < 0) {
                    return false;
                }
                size += mySize;
            } while (size < 4);
            final int magic = this.readNumberBuff.getInt(0);
            if (magic == 407708164) {
                this.readHeader();
                return true;
            }
            if (magic >>> 4 != 25481893) {
                throw new IOException("Stream unsupported");
            }
            this.skippableFrame();
        }
    }
    
    private void skippableFrame() throws IOException {
        int skipSize = this.readInt(this.in);
        final byte[] skipBuffer = new byte[1024];
        while (skipSize > 0) {
            final int mySize = this.in.read(skipBuffer, 0, Math.min(skipSize, skipBuffer.length));
            if (mySize < 0) {
                throw new IOException("Stream ended prematurely");
            }
            skipSize -= mySize;
        }
    }
    
    private void readHeader() throws IOException {
        this.headerBuffer.rewind();
        final int flgRead = this.in.read();
        if (flgRead < 0) {
            throw new IOException("Stream ended prematurely");
        }
        final int bdRead = this.in.read();
        if (bdRead < 0) {
            throw new IOException("Stream ended prematurely");
        }
        final byte flgByte = (byte)(flgRead & 0xFF);
        final LZ4FrameOutputStream.FLG flg = LZ4FrameOutputStream.FLG.fromByte(flgByte);
        this.headerBuffer.put(flgByte);
        final byte bdByte = (byte)(bdRead & 0xFF);
        final LZ4FrameOutputStream.BD bd = LZ4FrameOutputStream.BD.fromByte(bdByte);
        this.headerBuffer.put(bdByte);
        this.frameInfo = new LZ4FrameOutputStream.FrameInfo(flg, bd);
        if (flg.isEnabled(LZ4FrameOutputStream.FLG.Bits.CONTENT_SIZE)) {
            this.expectedContentSize = this.readLong(this.in);
            this.headerBuffer.putLong(this.expectedContentSize);
        }
        this.totalContentSize = 0L;
        final byte hash = (byte)(this.checksum.hash(this.headerArray, 0, this.headerBuffer.position(), 0) >> 8 & 0xFF);
        final int expectedHash = this.in.read();
        if (expectedHash < 0) {
            throw new IOException("Stream ended prematurely");
        }
        if (hash != (byte)(expectedHash & 0xFF)) {
            throw new IOException("Stream frame descriptor corrupted");
        }
        this.maxBlockSize = this.frameInfo.getBD().getBlockMaximumSize();
        this.compressedBuffer = new byte[this.maxBlockSize];
        this.rawBuffer = new byte[this.maxBlockSize];
        (this.buffer = ByteBuffer.wrap(this.rawBuffer)).limit(0);
    }
    
    private long readLong(final InputStream stream) throws IOException {
        int offset = 0;
        do {
            final int mySize = stream.read(this.readNumberBuff.array(), offset, 8 - offset);
            if (mySize < 0) {
                throw new IOException("Stream ended prematurely");
            }
            offset += mySize;
        } while (offset < 8);
        return this.readNumberBuff.getLong(0);
    }
    
    private int readInt(final InputStream stream) throws IOException {
        int offset = 0;
        do {
            final int mySize = stream.read(this.readNumberBuff.array(), offset, 4 - offset);
            if (mySize < 0) {
                throw new IOException("Stream ended prematurely");
            }
            offset += mySize;
        } while (offset < 4);
        return this.readNumberBuff.getInt(0);
    }
    
    private void readBlock() throws IOException {
        int blockSize = this.readInt(this.in);
        final boolean compressed = (blockSize & Integer.MIN_VALUE) == 0x0;
        blockSize &= Integer.MAX_VALUE;
        if (blockSize == 0) {
            if (this.frameInfo.isEnabled(LZ4FrameOutputStream.FLG.Bits.CONTENT_CHECKSUM)) {
                final int contentChecksum = this.readInt(this.in);
                if (contentChecksum != this.frameInfo.currentStreamHash()) {
                    throw new IOException("Content checksum mismatch");
                }
            }
            if (this.frameInfo.isEnabled(LZ4FrameOutputStream.FLG.Bits.CONTENT_SIZE) && this.expectedContentSize != this.totalContentSize) {
                throw new IOException("Size check mismatch");
            }
            this.frameInfo.finish();
        }
        else {
            byte[] tmpBuffer;
            if (compressed) {
                tmpBuffer = this.compressedBuffer;
            }
            else {
                tmpBuffer = this.rawBuffer;
            }
            if (blockSize > this.maxBlockSize) {
                throw new IOException(String.format(Locale.ROOT, "Block size %s exceeded max: %s", blockSize, this.maxBlockSize));
            }
            int lastRead;
            for (int offset = 0; offset < blockSize; offset += lastRead) {
                lastRead = this.in.read(tmpBuffer, offset, blockSize - offset);
                if (lastRead < 0) {
                    throw new IOException("Stream ended prematurely");
                }
            }
            if (this.frameInfo.isEnabled(LZ4FrameOutputStream.FLG.Bits.BLOCK_CHECKSUM)) {
                final int hashCheck = this.readInt(this.in);
                if (hashCheck != this.checksum.hash(tmpBuffer, 0, blockSize, 0)) {
                    throw new IOException("Block checksum mismatch");
                }
            }
            int currentBufferSize = 0;
            Label_0322: {
                if (compressed) {
                    try {
                        currentBufferSize = this.decompressor.decompress(tmpBuffer, 0, blockSize, this.rawBuffer, 0, this.rawBuffer.length);
                        break Label_0322;
                    }
                    catch (final LZ4Exception e) {
                        throw new IOException(e);
                    }
                }
                currentBufferSize = blockSize;
            }
            if (this.frameInfo.isEnabled(LZ4FrameOutputStream.FLG.Bits.CONTENT_CHECKSUM)) {
                this.frameInfo.updateStreamHash(this.rawBuffer, 0, currentBufferSize);
            }
            this.totalContentSize += currentBufferSize;
            this.buffer.limit(currentBufferSize);
            this.buffer.rewind();
        }
    }
    
    @Override
    public int read() throws IOException {
        while (this.buffer.remaining() == 0) {
            if (this.frameInfo.isFinished()) {
                if (this.readSingleFrame) {
                    return -1;
                }
                if (!this.nextFrameInfo()) {
                    return -1;
                }
            }
            this.readBlock();
        }
        return this.buffer.get() & 0xFF;
    }
    
    @Override
    public int read(final byte[] b, final int off, int len) throws IOException {
        if (off < 0 || len < 0 || off + len > b.length) {
            throw new IndexOutOfBoundsException();
        }
        while (this.buffer.remaining() == 0) {
            if (this.frameInfo.isFinished()) {
                if (this.readSingleFrame) {
                    return -1;
                }
                if (!this.nextFrameInfo()) {
                    return -1;
                }
            }
            this.readBlock();
        }
        len = Math.min(len, this.buffer.remaining());
        this.buffer.get(b, off, len);
        return len;
    }
    
    @Override
    public long skip(long n) throws IOException {
        if (n <= 0L) {
            return 0L;
        }
        while (this.buffer.remaining() == 0) {
            if (this.frameInfo.isFinished()) {
                if (this.readSingleFrame) {
                    return 0L;
                }
                if (!this.nextFrameInfo()) {
                    return 0L;
                }
            }
            this.readBlock();
        }
        n = Math.min(n, this.buffer.remaining());
        this.buffer.position(this.buffer.position() + (int)n);
        return n;
    }
    
    @Override
    public int available() throws IOException {
        return this.buffer.remaining();
    }
    
    @Override
    public void close() throws IOException {
        super.close();
    }
    
    @Override
    public synchronized void mark(final int readlimit) {
        throw new UnsupportedOperationException("mark not supported");
    }
    
    @Override
    public synchronized void reset() throws IOException {
        throw new UnsupportedOperationException("reset not supported");
    }
    
    @Override
    public boolean markSupported() {
        return false;
    }
    
    public long getExpectedContentSize() {
        if (!this.readSingleFrame) {
            throw new UnsupportedOperationException("Operation not permitted when multiple frames can be read");
        }
        return this.expectedContentSize;
    }
    
    public boolean isExpectedContentSizeDefined() {
        return this.readSingleFrame && this.expectedContentSize >= 0L;
    }
}
