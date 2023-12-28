// 
// Decompiled by Procyon v0.6.0
// 

package io.netty.handler.codec.http2;

import io.netty.util.internal.ObjectUtil;
import io.netty.util.internal.ThrowableUtil;
import io.netty.util.ByteProcessor;
import io.netty.util.AsciiString;
import io.netty.buffer.ByteBuf;

final class HpackHuffmanDecoder
{
    private static final Http2Exception EOS_DECODED;
    private static final Http2Exception INVALID_PADDING;
    private static final Node ROOT;
    private final DecoderProcessor processor;
    
    HpackHuffmanDecoder(final int initialCapacity) {
        this.processor = new DecoderProcessor(initialCapacity);
    }
    
    public AsciiString decode(final ByteBuf buf, final int length) throws Http2Exception {
        this.processor.reset();
        buf.forEachByte(buf.readerIndex(), length, this.processor);
        buf.skipBytes(length);
        return this.processor.end();
    }
    
    private static Node buildTree(final int[] codes, final byte[] lengths) {
        final Node root = new Node();
        for (int i = 0; i < codes.length; ++i) {
            insert(root, i, codes[i], lengths[i]);
        }
        return root;
    }
    
    private static void insert(final Node root, final int symbol, final int code, byte length) {
        Node current = root;
        while (length > 8) {
            if (current.isTerminal()) {
                throw new IllegalStateException("invalid Huffman code: prefix not unique");
            }
            length -= 8;
            final int i = code >>> length & 0xFF;
            if (current.children[i] == null) {
                current.children[i] = new Node();
            }
            current = current.children[i];
        }
        final Node terminal = new Node(symbol, length);
        final int shift = 8 - length;
        for (int start = code << shift & 0xFF, end = 1 << shift, j = start; j < start + end; ++j) {
            current.children[j] = terminal;
        }
    }
    
    static {
        EOS_DECODED = ThrowableUtil.unknownStackTrace(Http2Exception.connectionError(Http2Error.COMPRESSION_ERROR, "HPACK - EOS Decoded", new Object[0]), HpackHuffmanDecoder.class, "decode(..)");
        INVALID_PADDING = ThrowableUtil.unknownStackTrace(Http2Exception.connectionError(Http2Error.COMPRESSION_ERROR, "HPACK - Invalid Padding", new Object[0]), HpackHuffmanDecoder.class, "decode(..)");
        ROOT = buildTree(HpackUtil.HUFFMAN_CODES, HpackUtil.HUFFMAN_CODE_LENGTHS);
    }
    
    private static final class Node
    {
        private final int symbol;
        private final int bits;
        private final Node[] children;
        
        Node() {
            this.symbol = 0;
            this.bits = 8;
            this.children = new Node[256];
        }
        
        Node(final int symbol, final int bits) {
            assert bits > 0 && bits <= 8;
            this.symbol = symbol;
            this.bits = bits;
            this.children = null;
        }
        
        private boolean isTerminal() {
            return this.children == null;
        }
    }
    
    private static final class DecoderProcessor implements ByteProcessor
    {
        private final int initialCapacity;
        private byte[] bytes;
        private int index;
        private Node node;
        private int current;
        private int currentBits;
        private int symbolBits;
        
        DecoderProcessor(final int initialCapacity) {
            this.initialCapacity = ObjectUtil.checkPositive(initialCapacity, "initialCapacity");
        }
        
        void reset() {
            this.node = HpackHuffmanDecoder.ROOT;
            this.current = 0;
            this.currentBits = 0;
            this.symbolBits = 0;
            this.bytes = new byte[this.initialCapacity];
            this.index = 0;
        }
        
        @Override
        public boolean process(final byte value) throws Http2Exception {
            this.current = (this.current << 8 | (value & 0xFF));
            this.currentBits += 8;
            this.symbolBits += 8;
            do {
                this.node = this.node.children[this.current >>> this.currentBits - 8 & 0xFF];
                this.currentBits -= this.node.bits;
                if (this.node.isTerminal()) {
                    if (this.node.symbol == 256) {
                        throw HpackHuffmanDecoder.EOS_DECODED;
                    }
                    this.append(this.node.symbol);
                    this.node = HpackHuffmanDecoder.ROOT;
                    this.symbolBits = this.currentBits;
                }
            } while (this.currentBits >= 8);
            return true;
        }
        
        AsciiString end() throws Http2Exception {
            while (this.currentBits > 0) {
                this.node = this.node.children[this.current << 8 - this.currentBits & 0xFF];
                if (!this.node.isTerminal() || this.node.bits > this.currentBits) {
                    break;
                }
                if (this.node.symbol == 256) {
                    throw HpackHuffmanDecoder.EOS_DECODED;
                }
                this.currentBits -= this.node.bits;
                this.append(this.node.symbol);
                this.node = HpackHuffmanDecoder.ROOT;
                this.symbolBits = this.currentBits;
            }
            final int mask = (1 << this.symbolBits) - 1;
            if (this.symbolBits > 7 || (this.current & mask) != mask) {
                throw HpackHuffmanDecoder.INVALID_PADDING;
            }
            return new AsciiString(this.bytes, 0, this.index, false);
        }
        
        private void append(final int i) {
            try {
                this.bytes[this.index] = (byte)i;
            }
            catch (final IndexOutOfBoundsException ignore) {
                final byte[] newBytes = new byte[this.bytes.length + this.initialCapacity];
                System.arraycopy(this.bytes, 0, newBytes, 0, this.bytes.length);
                (this.bytes = newBytes)[this.index] = (byte)i;
            }
            ++this.index;
        }
    }
}
