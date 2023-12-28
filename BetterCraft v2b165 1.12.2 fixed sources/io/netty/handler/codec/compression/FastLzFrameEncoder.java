// 
// Decompiled by Procyon v0.6.0
// 

package io.netty.handler.codec.compression;

import io.netty.channel.ChannelHandlerContext;
import java.util.zip.Adler32;
import java.util.zip.Checksum;
import io.netty.buffer.ByteBuf;
import io.netty.handler.codec.MessageToByteEncoder;

public class FastLzFrameEncoder extends MessageToByteEncoder<ByteBuf>
{
    private final int level;
    private final Checksum checksum;
    
    public FastLzFrameEncoder() {
        this(0, null);
    }
    
    public FastLzFrameEncoder(final int level) {
        this(level, null);
    }
    
    public FastLzFrameEncoder(final boolean validateChecksums) {
        this(0, validateChecksums ? new Adler32() : null);
    }
    
    public FastLzFrameEncoder(final int level, final Checksum checksum) {
        super(false);
        if (level != 0 && level != 1 && level != 2) {
            throw new IllegalArgumentException(String.format("level: %d (expected: %d or %d or %d)", level, 0, 1, 2));
        }
        this.level = level;
        this.checksum = checksum;
    }
    
    @Override
    protected void encode(final ChannelHandlerContext ctx, final ByteBuf in, final ByteBuf out) throws Exception {
        final Checksum checksum = this.checksum;
        while (in.isReadable()) {
            final int idx = in.readerIndex();
            final int length = Math.min(in.readableBytes(), 65535);
            final int outputIdx = out.writerIndex();
            out.setMedium(outputIdx, 4607066);
            int outputOffset = outputIdx + 4 + ((checksum != null) ? 4 : 0);
            byte blockType;
            int chunkLength;
            if (length < 32) {
                blockType = 0;
                out.ensureWritable(outputOffset + 2 + length);
                final byte[] output = out.array();
                final int outputPtr = out.arrayOffset() + outputOffset + 2;
                if (checksum != null) {
                    byte[] input;
                    int inputPtr;
                    if (in.hasArray()) {
                        input = in.array();
                        inputPtr = in.arrayOffset() + idx;
                    }
                    else {
                        input = new byte[length];
                        in.getBytes(idx, input);
                        inputPtr = 0;
                    }
                    checksum.reset();
                    checksum.update(input, inputPtr, length);
                    out.setInt(outputIdx + 4, (int)checksum.getValue());
                    System.arraycopy(input, inputPtr, output, outputPtr, length);
                }
                else {
                    in.getBytes(idx, output, outputPtr, length);
                }
                chunkLength = length;
            }
            else {
                byte[] input2;
                int inputPtr2;
                if (in.hasArray()) {
                    input2 = in.array();
                    inputPtr2 = in.arrayOffset() + idx;
                }
                else {
                    input2 = new byte[length];
                    in.getBytes(idx, input2);
                    inputPtr2 = 0;
                }
                if (checksum != null) {
                    checksum.reset();
                    checksum.update(input2, inputPtr2, length);
                    out.setInt(outputIdx + 4, (int)checksum.getValue());
                }
                final int maxOutputLength = FastLz.calculateOutputBufferLength(length);
                out.ensureWritable(outputOffset + 4 + maxOutputLength);
                final byte[] output2 = out.array();
                final int outputPtr2 = out.arrayOffset() + outputOffset + 4;
                final int compressedLength = FastLz.compress(input2, inputPtr2, length, output2, outputPtr2, this.level);
                if (compressedLength < length) {
                    blockType = 1;
                    chunkLength = compressedLength;
                    out.setShort(outputOffset, chunkLength);
                    outputOffset += 2;
                }
                else {
                    blockType = 0;
                    System.arraycopy(input2, inputPtr2, output2, outputPtr2 - 2, length);
                    chunkLength = length;
                }
            }
            out.setShort(outputOffset, length);
            out.setByte(outputIdx + 3, blockType | ((checksum != null) ? 16 : 0));
            out.writerIndex(outputOffset + 2 + chunkLength);
            in.skipBytes(length);
        }
    }
}
