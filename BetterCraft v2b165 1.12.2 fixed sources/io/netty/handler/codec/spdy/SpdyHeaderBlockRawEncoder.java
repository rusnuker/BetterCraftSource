// 
// Decompiled by Procyon v0.6.0
// 

package io.netty.handler.codec.spdy;

import io.netty.handler.codec.Headers;
import java.util.Iterator;
import java.util.Set;
import io.netty.buffer.ByteBufUtil;
import io.netty.buffer.Unpooled;
import io.netty.buffer.ByteBufAllocator;
import io.netty.buffer.ByteBuf;

public class SpdyHeaderBlockRawEncoder extends SpdyHeaderBlockEncoder
{
    private final int version;
    
    public SpdyHeaderBlockRawEncoder(final SpdyVersion version) {
        if (version == null) {
            throw new NullPointerException("version");
        }
        this.version = version.getVersion();
    }
    
    private static void setLengthField(final ByteBuf buffer, final int writerIndex, final int length) {
        buffer.setInt(writerIndex, length);
    }
    
    private static void writeLengthField(final ByteBuf buffer, final int length) {
        buffer.writeInt(length);
    }
    
    public ByteBuf encode(final ByteBufAllocator alloc, final SpdyHeadersFrame frame) throws Exception {
        final Set<CharSequence> names = ((Headers<CharSequence, V, T>)frame.headers()).names();
        final int numHeaders = names.size();
        if (numHeaders == 0) {
            return Unpooled.EMPTY_BUFFER;
        }
        if (numHeaders > 65535) {
            throw new IllegalArgumentException("header block contains too many headers");
        }
        final ByteBuf headerBlock = alloc.heapBuffer();
        writeLengthField(headerBlock, numHeaders);
        for (final CharSequence name : names) {
            writeLengthField(headerBlock, name.length());
            ByteBufUtil.writeAscii(headerBlock, name);
            final int savedIndex = headerBlock.writerIndex();
            int valueLength = 0;
            writeLengthField(headerBlock, valueLength);
            for (final CharSequence value : ((Headers<CharSequence, CharSequence, T>)frame.headers()).getAll(name)) {
                final int length = value.length();
                if (length > 0) {
                    ByteBufUtil.writeAscii(headerBlock, value);
                    headerBlock.writeByte(0);
                    valueLength += length + 1;
                }
            }
            if (valueLength != 0) {
                --valueLength;
            }
            if (valueLength > 65535) {
                throw new IllegalArgumentException("header exceeds allowable length: " + (Object)name);
            }
            if (valueLength <= 0) {
                continue;
            }
            setLengthField(headerBlock, savedIndex, valueLength);
            headerBlock.writerIndex(headerBlock.writerIndex() - 1);
        }
        return headerBlock;
    }
    
    @Override
    void end() {
    }
}
