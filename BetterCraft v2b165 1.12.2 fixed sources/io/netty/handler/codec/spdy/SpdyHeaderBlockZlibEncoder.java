// 
// Decompiled by Procyon v0.6.0
// 

package io.netty.handler.codec.spdy;

import io.netty.buffer.Unpooled;
import io.netty.buffer.ByteBufAllocator;
import io.netty.buffer.ByteBuf;
import java.util.zip.Deflater;

class SpdyHeaderBlockZlibEncoder extends SpdyHeaderBlockRawEncoder
{
    private final Deflater compressor;
    private boolean finished;
    
    SpdyHeaderBlockZlibEncoder(final SpdyVersion spdyVersion, final int compressionLevel) {
        super(spdyVersion);
        if (compressionLevel < 0 || compressionLevel > 9) {
            throw new IllegalArgumentException("compressionLevel: " + compressionLevel + " (expected: 0-9)");
        }
        (this.compressor = new Deflater(compressionLevel)).setDictionary(SpdyCodecUtil.SPDY_DICT);
    }
    
    private int setInput(final ByteBuf decompressed) {
        final int len = decompressed.readableBytes();
        if (decompressed.hasArray()) {
            this.compressor.setInput(decompressed.array(), decompressed.arrayOffset() + decompressed.readerIndex(), len);
        }
        else {
            final byte[] in = new byte[len];
            decompressed.getBytes(decompressed.readerIndex(), in);
            this.compressor.setInput(in, 0, in.length);
        }
        return len;
    }
    
    private ByteBuf encode(final ByteBufAllocator alloc, final int len) {
        final ByteBuf compressed = alloc.heapBuffer(len);
        boolean release = true;
        try {
            while (this.compressInto(compressed)) {
                compressed.ensureWritable(compressed.capacity() << 1);
            }
            release = false;
            return compressed;
        }
        finally {
            if (release) {
                compressed.release();
            }
        }
    }
    
    private boolean compressInto(final ByteBuf compressed) {
        final byte[] out = compressed.array();
        final int off = compressed.arrayOffset() + compressed.writerIndex();
        final int toWrite = compressed.writableBytes();
        final int numBytes = this.compressor.deflate(out, off, toWrite, 2);
        compressed.writerIndex(compressed.writerIndex() + numBytes);
        return numBytes == toWrite;
    }
    
    @Override
    public ByteBuf encode(final ByteBufAllocator alloc, final SpdyHeadersFrame frame) throws Exception {
        if (frame == null) {
            throw new IllegalArgumentException("frame");
        }
        if (this.finished) {
            return Unpooled.EMPTY_BUFFER;
        }
        final ByteBuf decompressed = super.encode(alloc, frame);
        try {
            if (!decompressed.isReadable()) {
                return Unpooled.EMPTY_BUFFER;
            }
            final int len = this.setInput(decompressed);
            return this.encode(alloc, len);
        }
        finally {
            decompressed.release();
        }
    }
    
    public void end() {
        if (this.finished) {
            return;
        }
        this.finished = true;
        this.compressor.end();
        super.end();
    }
}
