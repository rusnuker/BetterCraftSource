// 
// Decompiled by Procyon v0.6.0
// 

package io.netty.handler.codec.compression;

import java.util.List;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import com.ning.compress.lzf.util.ChunkDecoderFactory;
import com.ning.compress.BufferRecycler;
import com.ning.compress.lzf.ChunkDecoder;
import io.netty.handler.codec.ByteToMessageDecoder;

public class LzfDecoder extends ByteToMessageDecoder
{
    private State currentState;
    private static final short MAGIC_NUMBER = 23126;
    private ChunkDecoder decoder;
    private BufferRecycler recycler;
    private int chunkLength;
    private int originalLength;
    private boolean isCompressed;
    
    public LzfDecoder() {
        this(false);
    }
    
    public LzfDecoder(final boolean safeInstance) {
        this.currentState = State.INIT_BLOCK;
        this.decoder = (safeInstance ? ChunkDecoderFactory.safeInstance() : ChunkDecoderFactory.optimalInstance());
        this.recycler = BufferRecycler.instance();
    }
    
    @Override
    protected void decode(final ChannelHandlerContext ctx, final ByteBuf in, final List<Object> out) throws Exception {
        try {
            switch (this.currentState) {
                case INIT_BLOCK: {
                    if (in.readableBytes() < 5) {
                        break;
                    }
                    final int magic = in.readUnsignedShort();
                    if (magic != 23126) {
                        throw new DecompressionException("unexpected block identifier");
                    }
                    final int type = in.readByte();
                    switch (type) {
                        case 0: {
                            this.isCompressed = false;
                            this.currentState = State.DECOMPRESS_DATA;
                            break;
                        }
                        case 1: {
                            this.isCompressed = true;
                            this.currentState = State.INIT_ORIGINAL_LENGTH;
                            break;
                        }
                        default: {
                            throw new DecompressionException(String.format("unknown type of chunk: %d (expected: %d or %d)", type, 0, 1));
                        }
                    }
                    this.chunkLength = in.readUnsignedShort();
                    if (type != 1) {
                        break;
                    }
                }
                case INIT_ORIGINAL_LENGTH: {
                    if (in.readableBytes() < 2) {
                        break;
                    }
                    this.originalLength = in.readUnsignedShort();
                    this.currentState = State.DECOMPRESS_DATA;
                }
                case DECOMPRESS_DATA: {
                    final int chunkLength = this.chunkLength;
                    if (in.readableBytes() < chunkLength) {
                        break;
                    }
                    final int originalLength = this.originalLength;
                    if (this.isCompressed) {
                        final int idx = in.readerIndex();
                        byte[] inputArray;
                        int inPos;
                        if (in.hasArray()) {
                            inputArray = in.array();
                            inPos = in.arrayOffset() + idx;
                        }
                        else {
                            inputArray = this.recycler.allocInputBuffer(chunkLength);
                            in.getBytes(idx, inputArray, 0, chunkLength);
                            inPos = 0;
                        }
                        final ByteBuf uncompressed = ctx.alloc().heapBuffer(originalLength, originalLength);
                        final byte[] outputArray = uncompressed.array();
                        final int outPos = uncompressed.arrayOffset() + uncompressed.writerIndex();
                        boolean success = false;
                        try {
                            this.decoder.decodeChunk(inputArray, inPos, outputArray, outPos, outPos + originalLength);
                            uncompressed.writerIndex(uncompressed.writerIndex() + originalLength);
                            out.add(uncompressed);
                            in.skipBytes(chunkLength);
                            success = true;
                        }
                        finally {
                            if (!success) {
                                uncompressed.release();
                            }
                        }
                        if (!in.hasArray()) {
                            this.recycler.releaseInputBuffer(inputArray);
                        }
                    }
                    else if (chunkLength > 0) {
                        out.add(in.readRetainedSlice(chunkLength));
                    }
                    this.currentState = State.INIT_BLOCK;
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
            this.decoder = null;
            this.recycler = null;
            throw e;
        }
    }
    
    private enum State
    {
        INIT_BLOCK, 
        INIT_ORIGINAL_LENGTH, 
        DECOMPRESS_DATA, 
        CORRUPTED;
    }
}
