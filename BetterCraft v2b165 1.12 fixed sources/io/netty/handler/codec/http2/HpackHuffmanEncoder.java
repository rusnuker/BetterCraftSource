// 
// Decompiled by Procyon v0.6.0
// 

package io.netty.handler.codec.http2;

import io.netty.util.internal.PlatformDependent;
import io.netty.util.ByteProcessor;
import io.netty.util.AsciiString;
import io.netty.util.internal.ObjectUtil;
import io.netty.buffer.ByteBuf;

final class HpackHuffmanEncoder
{
    private final int[] codes;
    private final byte[] lengths;
    private final EncodedLengthProcessor encodedLengthProcessor;
    private final EncodeProcessor encodeProcessor;
    
    HpackHuffmanEncoder() {
        this(HpackUtil.HUFFMAN_CODES, HpackUtil.HUFFMAN_CODE_LENGTHS);
    }
    
    private HpackHuffmanEncoder(final int[] codes, final byte[] lengths) {
        this.encodedLengthProcessor = new EncodedLengthProcessor();
        this.encodeProcessor = new EncodeProcessor();
        this.codes = codes;
        this.lengths = lengths;
    }
    
    public void encode(final ByteBuf out, final CharSequence data) {
        ObjectUtil.checkNotNull(out, "out");
        if (data instanceof AsciiString) {
            final AsciiString string = (AsciiString)data;
            try {
                this.encodeProcessor.out = out;
                string.forEachByte(this.encodeProcessor);
            }
            catch (final Exception e) {
                PlatformDependent.throwException(e);
            }
            finally {
                this.encodeProcessor.end();
            }
        }
        else {
            this.encodeSlowPath(out, data);
        }
    }
    
    private void encodeSlowPath(final ByteBuf out, final CharSequence data) {
        long current = 0L;
        int n = 0;
        for (int i = 0; i < data.length(); ++i) {
            final int b = data.charAt(i) & '\u00ff';
            final int code = this.codes[b];
            final int nbits = this.lengths[b];
            current <<= nbits;
            current |= code;
            n += nbits;
            while (n >= 8) {
                n -= 8;
                out.writeByte((int)(current >> n));
            }
        }
        if (n > 0) {
            current <<= 8 - n;
            current |= 255 >>> n;
            out.writeByte((int)current);
        }
    }
    
    int getEncodedLength(final CharSequence data) {
        if (data instanceof AsciiString) {
            final AsciiString string = (AsciiString)data;
            try {
                this.encodedLengthProcessor.reset();
                string.forEachByte(this.encodedLengthProcessor);
                return this.encodedLengthProcessor.length();
            }
            catch (final Exception e) {
                PlatformDependent.throwException(e);
                return -1;
            }
        }
        return this.getEncodedLengthSlowPath(data);
    }
    
    private int getEncodedLengthSlowPath(final CharSequence data) {
        long len = 0L;
        for (int i = 0; i < data.length(); ++i) {
            len += this.lengths[data.charAt(i) & '\u00ff'];
        }
        return (int)(len + 7L >> 3);
    }
    
    private final class EncodeProcessor implements ByteProcessor
    {
        ByteBuf out;
        private long current;
        private int n;
        
        @Override
        public boolean process(final byte value) {
            final int b = value & 0xFF;
            final int nbits = HpackHuffmanEncoder.this.lengths[b];
            this.current <<= nbits;
            this.current |= HpackHuffmanEncoder.this.codes[b];
            this.n += nbits;
            while (this.n >= 8) {
                this.n -= 8;
                this.out.writeByte((int)(this.current >> this.n));
            }
            return true;
        }
        
        void end() {
            try {
                if (this.n > 0) {
                    this.current <<= 8 - this.n;
                    this.current |= 255 >>> this.n;
                    this.out.writeByte((int)this.current);
                }
            }
            finally {
                this.out = null;
                this.current = 0L;
                this.n = 0;
            }
        }
    }
    
    private final class EncodedLengthProcessor implements ByteProcessor
    {
        private long len;
        
        @Override
        public boolean process(final byte value) {
            this.len += HpackHuffmanEncoder.this.lengths[value & 0xFF];
            return true;
        }
        
        void reset() {
            this.len = 0L;
        }
        
        int length() {
            return (int)(this.len + 7L >> 3);
        }
    }
}
