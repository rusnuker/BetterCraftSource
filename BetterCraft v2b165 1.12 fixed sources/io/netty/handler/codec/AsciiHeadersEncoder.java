// 
// Decompiled by Procyon v0.6.0
// 

package io.netty.handler.codec;

import io.netty.buffer.ByteBufUtil;
import io.netty.util.AsciiString;
import java.util.Map;
import io.netty.buffer.ByteBuf;

public final class AsciiHeadersEncoder
{
    private final ByteBuf buf;
    private final SeparatorType separatorType;
    private final NewlineType newlineType;
    
    public AsciiHeadersEncoder(final ByteBuf buf) {
        this(buf, SeparatorType.COLON_SPACE, NewlineType.CRLF);
    }
    
    public AsciiHeadersEncoder(final ByteBuf buf, final SeparatorType separatorType, final NewlineType newlineType) {
        if (buf == null) {
            throw new NullPointerException("buf");
        }
        if (separatorType == null) {
            throw new NullPointerException("separatorType");
        }
        if (newlineType == null) {
            throw new NullPointerException("newlineType");
        }
        this.buf = buf;
        this.separatorType = separatorType;
        this.newlineType = newlineType;
    }
    
    public void encode(final Map.Entry<CharSequence, CharSequence> entry) {
        final CharSequence name = entry.getKey();
        final CharSequence value = entry.getValue();
        final ByteBuf buf = this.buf;
        final int nameLen = name.length();
        final int valueLen = value.length();
        final int entryLen = nameLen + valueLen + 4;
        int offset = buf.writerIndex();
        buf.ensureWritable(entryLen);
        writeAscii(buf, offset, name, nameLen);
        offset += nameLen;
        switch (this.separatorType) {
            case COLON: {
                buf.setByte(offset++, 58);
                break;
            }
            case COLON_SPACE: {
                buf.setByte(offset++, 58);
                buf.setByte(offset++, 32);
                break;
            }
            default: {
                throw new Error();
            }
        }
        writeAscii(buf, offset, value, valueLen);
        offset += valueLen;
        switch (this.newlineType) {
            case LF: {
                buf.setByte(offset++, 10);
                break;
            }
            case CRLF: {
                buf.setByte(offset++, 13);
                buf.setByte(offset++, 10);
                break;
            }
            default: {
                throw new Error();
            }
        }
        buf.writerIndex(offset);
    }
    
    private static void writeAscii(final ByteBuf buf, final int offset, final CharSequence value, final int valueLen) {
        if (value instanceof AsciiString) {
            writeAsciiString(buf, offset, (AsciiString)value, valueLen);
        }
        else {
            writeCharSequence(buf, offset, value, valueLen);
        }
    }
    
    private static void writeAsciiString(final ByteBuf buf, final int offset, final AsciiString value, final int valueLen) {
        ByteBufUtil.copy(value, 0, buf, offset, valueLen);
    }
    
    private static void writeCharSequence(final ByteBuf buf, int offset, final CharSequence value, final int valueLen) {
        for (int i = 0; i < valueLen; ++i) {
            buf.setByte(offset++, c2b(value.charAt(i)));
        }
    }
    
    private static int c2b(final char ch) {
        return (ch < '\u0100') ? ((byte)ch) : 63;
    }
    
    public enum SeparatorType
    {
        COLON, 
        COLON_SPACE;
    }
    
    public enum NewlineType
    {
        LF, 
        CRLF;
    }
}
