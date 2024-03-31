// 
// Decompiled by Procyon v0.6.0
// 

package io.netty.handler.codec.http;

import io.netty.buffer.ByteBufUtil;
import io.netty.util.AsciiString;
import io.netty.buffer.ByteBuf;

final class HttpHeadersEncoder
{
    private HttpHeadersEncoder() {
    }
    
    public static void encoderHeader(final CharSequence name, final CharSequence value, final ByteBuf buf) throws Exception {
        final int nameLen = name.length();
        final int valueLen = value.length();
        final int entryLen = nameLen + valueLen + 4;
        buf.ensureWritable(entryLen);
        int offset = buf.writerIndex();
        writeAscii(buf, offset, name, nameLen);
        offset += nameLen;
        buf.setByte(offset++, 58);
        buf.setByte(offset++, 32);
        writeAscii(buf, offset, value, valueLen);
        offset += valueLen;
        buf.setByte(offset++, 13);
        buf.setByte(offset++, 10);
        buf.writerIndex(offset);
    }
    
    private static void writeAscii(final ByteBuf buf, final int offset, final CharSequence value, final int valueLen) {
        if (value instanceof AsciiString) {
            ByteBufUtil.copy((AsciiString)value, 0, buf, offset, valueLen);
        }
        else {
            writeCharSequence(buf, offset, value, valueLen);
        }
    }
    
    private static void writeCharSequence(final ByteBuf buf, int offset, final CharSequence value, final int valueLen) {
        for (int i = 0; i < valueLen; ++i) {
            buf.setByte(offset++, AsciiString.c2b(value.charAt(i)));
        }
    }
}
