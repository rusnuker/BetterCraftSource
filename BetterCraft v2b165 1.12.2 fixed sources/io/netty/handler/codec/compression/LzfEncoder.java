// 
// Decompiled by Procyon v0.6.0
// 

package io.netty.handler.codec.compression;

import com.ning.compress.lzf.LZFEncoder;
import io.netty.channel.ChannelHandlerContext;
import com.ning.compress.lzf.util.ChunkEncoderFactory;
import com.ning.compress.BufferRecycler;
import com.ning.compress.lzf.ChunkEncoder;
import io.netty.buffer.ByteBuf;
import io.netty.handler.codec.MessageToByteEncoder;

public class LzfEncoder extends MessageToByteEncoder<ByteBuf>
{
    private static final int MIN_BLOCK_TO_COMPRESS = 16;
    private final ChunkEncoder encoder;
    private final BufferRecycler recycler;
    
    public LzfEncoder() {
        this(false, 65535);
    }
    
    public LzfEncoder(final boolean safeInstance) {
        this(safeInstance, 65535);
    }
    
    public LzfEncoder(final int totalLength) {
        this(false, totalLength);
    }
    
    public LzfEncoder(final boolean safeInstance, final int totalLength) {
        super(false);
        if (totalLength < 16 || totalLength > 65535) {
            throw new IllegalArgumentException("totalLength: " + totalLength + " (expected: " + 16 + '-' + 65535 + ')');
        }
        this.encoder = (safeInstance ? ChunkEncoderFactory.safeNonAllocatingInstance(totalLength) : ChunkEncoderFactory.optimalNonAllocatingInstance(totalLength));
        this.recycler = BufferRecycler.instance();
    }
    
    @Override
    protected void encode(final ChannelHandlerContext ctx, final ByteBuf in, final ByteBuf out) throws Exception {
        final int length = in.readableBytes();
        final int idx = in.readerIndex();
        byte[] input;
        int inputPtr;
        if (in.hasArray()) {
            input = in.array();
            inputPtr = in.arrayOffset() + idx;
        }
        else {
            input = this.recycler.allocInputBuffer(length);
            in.getBytes(idx, input, 0, length);
            inputPtr = 0;
        }
        final int maxOutputLength = LZFEncoder.estimateMaxWorkspaceSize(length);
        out.ensureWritable(maxOutputLength);
        final byte[] output = out.array();
        final int outputPtr = out.arrayOffset() + out.writerIndex();
        final int outputLength = LZFEncoder.appendEncoded(this.encoder, input, inputPtr, length, output, outputPtr) - outputPtr;
        out.writerIndex(out.writerIndex() + outputLength);
        in.skipBytes(length);
        if (!in.hasArray()) {
            this.recycler.releaseInputBuffer(input);
        }
    }
}
