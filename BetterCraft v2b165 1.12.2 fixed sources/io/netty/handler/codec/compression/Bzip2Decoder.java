// 
// Decompiled by Procyon v0.6.0
// 

package io.netty.handler.codec.compression;

import java.util.List;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

public class Bzip2Decoder extends ByteToMessageDecoder
{
    private State currentState;
    private final Bzip2BitReader reader;
    private Bzip2BlockDecompressor blockDecompressor;
    private Bzip2HuffmanStageDecoder huffmanStageDecoder;
    private int blockSize;
    private int blockCRC;
    private int streamCRC;
    
    public Bzip2Decoder() {
        this.currentState = State.INIT;
        this.reader = new Bzip2BitReader();
    }
    
    @Override
    protected void decode(final ChannelHandlerContext ctx, final ByteBuf in, final List<Object> out) throws Exception {
        if (!in.isReadable()) {
            return;
        }
        final Bzip2BitReader reader = this.reader;
        reader.setByteBuf(in);
        while (true) {
            switch (this.currentState) {
                case INIT: {
                    if (in.readableBytes() < 4) {
                        return;
                    }
                    final int magicNumber = in.readUnsignedMedium();
                    if (magicNumber != 4348520) {
                        throw new DecompressionException("Unexpected stream identifier contents. Mismatched bzip2 protocol version?");
                    }
                    final int blockSize = in.readByte() - 48;
                    if (blockSize < 1 || blockSize > 9) {
                        throw new DecompressionException("block size is invalid");
                    }
                    this.blockSize = blockSize * 100000;
                    this.streamCRC = 0;
                    this.currentState = State.INIT_BLOCK;
                }
                case INIT_BLOCK: {
                    if (!reader.hasReadableBytes(10)) {
                        return;
                    }
                    final int magic1 = reader.readBits(24);
                    final int magic2 = reader.readBits(24);
                    if (magic1 == 1536581 && magic2 == 3690640) {
                        final int storedCombinedCRC = reader.readInt();
                        if (storedCombinedCRC != this.streamCRC) {
                            throw new DecompressionException("stream CRC error");
                        }
                        this.currentState = State.EOF;
                        continue;
                    }
                    else {
                        if (magic1 != 3227993 || magic2 != 2511705) {
                            throw new DecompressionException("bad block header");
                        }
                        this.blockCRC = reader.readInt();
                        this.currentState = State.INIT_BLOCK_PARAMS;
                    }
                    break;
                }
                case INIT_BLOCK_PARAMS: {
                    if (!reader.hasReadableBits(25)) {
                        return;
                    }
                    final boolean blockRandomised = reader.readBoolean();
                    final int bwtStartPointer = reader.readBits(24);
                    this.blockDecompressor = new Bzip2BlockDecompressor(this.blockSize, this.blockCRC, blockRandomised, bwtStartPointer, reader);
                    this.currentState = State.RECEIVE_HUFFMAN_USED_MAP;
                }
                case RECEIVE_HUFFMAN_USED_MAP: {
                    if (!reader.hasReadableBits(16)) {
                        return;
                    }
                    this.blockDecompressor.huffmanInUse16 = reader.readBits(16);
                    this.currentState = State.RECEIVE_HUFFMAN_USED_BITMAPS;
                }
                case RECEIVE_HUFFMAN_USED_BITMAPS: {
                    final Bzip2BlockDecompressor blockDecompressor = this.blockDecompressor;
                    final int inUse16 = blockDecompressor.huffmanInUse16;
                    final int bitNumber = Integer.bitCount(inUse16);
                    final byte[] huffmanSymbolMap = blockDecompressor.huffmanSymbolMap;
                    if (!reader.hasReadableBits(bitNumber * 16 + 3)) {
                        return;
                    }
                    int huffmanSymbolCount = 0;
                    if (bitNumber > 0) {
                        for (int i = 0; i < 16; ++i) {
                            if ((inUse16 & 32768 >>> i) != 0x0) {
                                for (int j = 0, k = i << 4; j < 16; ++j, ++k) {
                                    if (reader.readBoolean()) {
                                        huffmanSymbolMap[huffmanSymbolCount++] = (byte)k;
                                    }
                                }
                            }
                        }
                    }
                    blockDecompressor.huffmanEndOfBlockSymbol = huffmanSymbolCount + 1;
                    final int totalTables = reader.readBits(3);
                    if (totalTables < 2 || totalTables > 6) {
                        throw new DecompressionException("incorrect huffman groups number");
                    }
                    final int alphaSize = huffmanSymbolCount + 2;
                    if (alphaSize > 258) {
                        throw new DecompressionException("incorrect alphabet size");
                    }
                    this.huffmanStageDecoder = new Bzip2HuffmanStageDecoder(reader, totalTables, alphaSize);
                    this.currentState = State.RECEIVE_SELECTORS_NUMBER;
                }
                case RECEIVE_SELECTORS_NUMBER: {
                    if (!reader.hasReadableBits(15)) {
                        return;
                    }
                    final int totalSelectors = reader.readBits(15);
                    if (totalSelectors < 1 || totalSelectors > 18002) {
                        throw new DecompressionException("incorrect selectors number");
                    }
                    this.huffmanStageDecoder.selectors = new byte[totalSelectors];
                    this.currentState = State.RECEIVE_SELECTORS;
                }
                case RECEIVE_SELECTORS: {
                    final Bzip2HuffmanStageDecoder huffmanStageDecoder = this.huffmanStageDecoder;
                    final byte[] selectors = huffmanStageDecoder.selectors;
                    final int totalSelectors = selectors.length;
                    final Bzip2MoveToFrontTable tableMtf = huffmanStageDecoder.tableMTF;
                    for (int currSelector = huffmanStageDecoder.currentSelector; currSelector < totalSelectors; ++currSelector) {
                        if (!reader.hasReadableBits(6)) {
                            huffmanStageDecoder.currentSelector = currSelector;
                            return;
                        }
                        int index = 0;
                        while (reader.readBoolean()) {
                            ++index;
                        }
                        selectors[currSelector] = tableMtf.indexToFront(index);
                    }
                    this.currentState = State.RECEIVE_HUFFMAN_LENGTH;
                }
                case RECEIVE_HUFFMAN_LENGTH: {
                    final Bzip2HuffmanStageDecoder huffmanStageDecoder = this.huffmanStageDecoder;
                    final int totalTables = huffmanStageDecoder.totalTables;
                    final byte[][] codeLength = huffmanStageDecoder.tableCodeLengths;
                    final int alphaSize = huffmanStageDecoder.alphabetSize;
                    int currLength = huffmanStageDecoder.currentLength;
                    int currAlpha = 0;
                    boolean modifyLength = huffmanStageDecoder.modifyLength;
                    boolean saveStateAndReturn = false;
                    int currGroup = 0;
                Label_0970:
                    for (currGroup = huffmanStageDecoder.currentGroup; currGroup < totalTables; ++currGroup) {
                        if (!reader.hasReadableBits(5)) {
                            saveStateAndReturn = true;
                            break;
                        }
                        if (currLength < 0) {
                            currLength = reader.readBits(5);
                        }
                        for (currAlpha = huffmanStageDecoder.currentAlpha; currAlpha < alphaSize; ++currAlpha) {
                            if (!reader.isReadable()) {
                                saveStateAndReturn = true;
                                break Label_0970;
                            }
                            while (modifyLength || reader.readBoolean()) {
                                if (!reader.isReadable()) {
                                    modifyLength = true;
                                    saveStateAndReturn = true;
                                    break Label_0970;
                                }
                                currLength += (reader.readBoolean() ? -1 : 1);
                                modifyLength = false;
                                if (!reader.isReadable()) {
                                    saveStateAndReturn = true;
                                    break Label_0970;
                                }
                            }
                            codeLength[currGroup][currAlpha] = (byte)currLength;
                        }
                        currLength = -1;
                        final Bzip2HuffmanStageDecoder bzip2HuffmanStageDecoder = huffmanStageDecoder;
                        final int currentAlpha = 0;
                        bzip2HuffmanStageDecoder.currentAlpha = currentAlpha;
                        currAlpha = currentAlpha;
                        modifyLength = false;
                    }
                    if (saveStateAndReturn) {
                        huffmanStageDecoder.currentGroup = currGroup;
                        huffmanStageDecoder.currentLength = currLength;
                        huffmanStageDecoder.currentAlpha = currAlpha;
                        huffmanStageDecoder.modifyLength = modifyLength;
                        return;
                    }
                    huffmanStageDecoder.createHuffmanDecodingTables();
                    this.currentState = State.DECODE_HUFFMAN_DATA;
                }
                case DECODE_HUFFMAN_DATA: {
                    final Bzip2BlockDecompressor blockDecompressor = this.blockDecompressor;
                    final int oldReaderIndex = in.readerIndex();
                    final boolean decoded = blockDecompressor.decodeHuffmanData(this.huffmanStageDecoder);
                    if (!decoded) {
                        return;
                    }
                    if (in.readerIndex() == oldReaderIndex && in.isReadable()) {
                        reader.refill();
                    }
                    final int blockLength = blockDecompressor.blockLength();
                    final ByteBuf uncompressed = ctx.alloc().buffer(blockLength);
                    boolean success = false;
                    try {
                        int uncByte;
                        while ((uncByte = blockDecompressor.read()) >= 0) {
                            uncompressed.writeByte(uncByte);
                        }
                        final int currentBlockCRC = blockDecompressor.checkCRC();
                        this.streamCRC = ((this.streamCRC << 1 | this.streamCRC >>> 31) ^ currentBlockCRC);
                        out.add(uncompressed);
                        success = true;
                    }
                    finally {
                        if (!success) {
                            uncompressed.release();
                        }
                    }
                    this.currentState = State.INIT_BLOCK;
                    continue;
                }
                case EOF: {
                    in.skipBytes(in.readableBytes());
                    return;
                }
                default: {
                    throw new IllegalStateException();
                }
            }
        }
    }
    
    public boolean isClosed() {
        return this.currentState == State.EOF;
    }
    
    private enum State
    {
        INIT, 
        INIT_BLOCK, 
        INIT_BLOCK_PARAMS, 
        RECEIVE_HUFFMAN_USED_MAP, 
        RECEIVE_HUFFMAN_USED_BITMAPS, 
        RECEIVE_SELECTORS_NUMBER, 
        RECEIVE_SELECTORS, 
        RECEIVE_HUFFMAN_LENGTH, 
        DECODE_HUFFMAN_DATA, 
        EOF;
    }
}
