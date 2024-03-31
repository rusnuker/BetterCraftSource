// 
// Decompiled by Procyon v0.6.0
// 

package io.netty.handler.codec.compression;

import io.netty.util.internal.EmptyArrays;
import java.util.List;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import java.util.zip.Adler32;
import java.util.zip.Checksum;
import io.netty.handler.codec.ByteToMessageDecoder;

public class FastLzFrameDecoder extends ByteToMessageDecoder
{
    private State currentState;
    private final Checksum checksum;
    private int chunkLength;
    private int originalLength;
    private boolean isCompressed;
    private boolean hasChecksum;
    private int currentChecksum;
    
    public FastLzFrameDecoder() {
        this(false);
    }
    
    public FastLzFrameDecoder(final boolean validateChecksums) {
        this(validateChecksums ? new Adler32() : null);
    }
    
    public FastLzFrameDecoder(final Checksum checksum) {
        this.currentState = State.INIT_BLOCK;
        this.checksum = checksum;
    }
    
    @Override
    protected void decode(final ChannelHandlerContext ctx, final ByteBuf in, final List<Object> out) throws Exception {
        try {
            switch (this.currentState) {
                case INIT_BLOCK: {
                    if (in.readableBytes() < 4) {
                        break;
                    }
                    final int magic = in.readUnsignedMedium();
                    if (magic != 4607066) {
                        throw new DecompressionException("unexpected block identifier");
                    }
                    final byte options = in.readByte();
                    this.isCompressed = ((options & 0x1) == 0x1);
                    this.hasChecksum = ((options & 0x10) == 0x10);
                    this.currentState = State.INIT_BLOCK_PARAMS;
                }
                case INIT_BLOCK_PARAMS: {
                    if (in.readableBytes() < 2 + (this.isCompressed ? 2 : 0) + (this.hasChecksum ? 4 : 0)) {
                        break;
                    }
                    this.currentChecksum = (this.hasChecksum ? in.readInt() : 0);
                    this.chunkLength = in.readUnsignedShort();
                    this.originalLength = (this.isCompressed ? in.readUnsignedShort() : this.chunkLength);
                    this.currentState = State.DECOMPRESS_DATA;
                }
                case DECOMPRESS_DATA: {
                    final int chunkLength = this.chunkLength;
                    if (in.readableBytes() < chunkLength) {
                        break;
                    }
                    final int idx = in.readerIndex();
                    final int originalLength = this.originalLength;
                    ByteBuf uncompressed;
                    byte[] output;
                    int outputPtr;
                    if (originalLength != 0) {
                        uncompressed = ctx.alloc().heapBuffer(originalLength, originalLength);
                        output = uncompressed.array();
                        outputPtr = uncompressed.arrayOffset() + uncompressed.writerIndex();
                    }
                    else {
                        uncompressed = null;
                        output = EmptyArrays.EMPTY_BYTES;
                        outputPtr = 0;
                    }
                    boolean success = false;
                    try {
                        if (this.isCompressed) {
                            byte[] input;
                            int inputPtr;
                            if (in.hasArray()) {
                                input = in.array();
                                inputPtr = in.arrayOffset() + idx;
                            }
                            else {
                                input = new byte[chunkLength];
                                in.getBytes(idx, input);
                                inputPtr = 0;
                            }
                            final int decompressedBytes = FastLz.decompress(input, inputPtr, chunkLength, output, outputPtr, originalLength);
                            if (originalLength != decompressedBytes) {
                                throw new DecompressionException(String.format("stream corrupted: originalLength(%d) and actual length(%d) mismatch", originalLength, decompressedBytes));
                            }
                        }
                        else {
                            in.getBytes(idx, output, outputPtr, chunkLength);
                        }
                        final Checksum checksum = this.checksum;
                        if (this.hasChecksum && checksum != null) {
                            checksum.reset();
                            checksum.update(output, outputPtr, originalLength);
                            final int checksumResult = (int)checksum.getValue();
                            if (checksumResult != this.currentChecksum) {
                                throw new DecompressionException(String.format("stream corrupted: mismatching checksum: %d (expected: %d)", checksumResult, this.currentChecksum));
                            }
                        }
                        if (uncompressed != null) {
                            uncompressed.writerIndex(uncompressed.writerIndex() + originalLength);
                            out.add(uncompressed);
                        }
                        in.skipBytes(chunkLength);
                        this.currentState = State.INIT_BLOCK;
                        success = true;
                    }
                    finally {
                        if (!success) {
                            uncompressed.release();
                        }
                    }
                    break;
                }
                case CORRUPTED: {
                    in.skipBytes(in.readableBytes());
                    break;
                }
                default: {
                    throw new IllegalStateException();
                }
            }
        }
        catch (final Exception e) {
            this.currentState = State.CORRUPTED;
            throw e;
        }
    }
    
    private enum State
    {
        INIT_BLOCK, 
        INIT_BLOCK_PARAMS, 
        DECOMPRESS_DATA, 
        CORRUPTED;
    }
}
