// 
// Decompiled by Procyon v0.6.0
// 

package io.netty.handler.ssl;

import io.netty.handler.codec.base64.Base64;
import io.netty.handler.codec.base64.Base64Dialect;
import io.netty.buffer.ByteBufAllocator;
import io.netty.channel.ChannelHandlerContext;
import java.nio.ByteBuffer;
import io.netty.buffer.ByteBuf;

final class SslUtils
{
    static final int SSL_CONTENT_TYPE_CHANGE_CIPHER_SPEC = 20;
    static final int SSL_CONTENT_TYPE_ALERT = 21;
    static final int SSL_CONTENT_TYPE_HANDSHAKE = 22;
    static final int SSL_CONTENT_TYPE_APPLICATION_DATA = 23;
    static final int SSL_RECORD_HEADER_LENGTH = 5;
    static final int NOT_ENOUGH_DATA = -1;
    static final int NOT_ENCRYPTED = -2;
    
    static int getEncryptedPacketLength(final ByteBuf buffer, final int offset) {
        int packetLength = 0;
        boolean tls = false;
        switch (buffer.getUnsignedByte(offset)) {
            case 20:
            case 21:
            case 22:
            case 23: {
                tls = true;
                break;
            }
            default: {
                tls = false;
                break;
            }
        }
        if (tls) {
            final int majorVersion = buffer.getUnsignedByte(offset + 1);
            if (majorVersion == 3) {
                packetLength = buffer.getUnsignedShort(offset + 3) + 5;
                if (packetLength <= 5) {
                    tls = false;
                }
            }
            else {
                tls = false;
            }
        }
        if (!tls) {
            final int headerLength = ((buffer.getUnsignedByte(offset) & 0x80) != 0x0) ? 2 : 3;
            final int majorVersion2 = buffer.getUnsignedByte(offset + headerLength + 1);
            if (majorVersion2 != 2 && majorVersion2 != 3) {
                return -2;
            }
            if (headerLength == 2) {
                packetLength = (buffer.getShort(offset) & 0x7FFF) + 2;
            }
            else {
                packetLength = (buffer.getShort(offset) & 0x3FFF) + 3;
            }
            if (packetLength <= headerLength) {
                return -1;
            }
        }
        return packetLength;
    }
    
    private static short unsignedByte(final byte b) {
        return (short)(b & 0xFF);
    }
    
    private static int unsignedShort(final short s) {
        return s & 0xFFFF;
    }
    
    static int getEncryptedPacketLength(final ByteBuffer[] buffers, int offset) {
        ByteBuffer buffer = buffers[offset];
        if (buffer.remaining() >= 5) {
            return getEncryptedPacketLength(buffer);
        }
        final ByteBuffer tmp = ByteBuffer.allocate(5);
        do {
            buffer = buffers[offset++].duplicate();
            if (buffer.remaining() > tmp.remaining()) {
                buffer.limit(buffer.position() + tmp.remaining());
            }
            tmp.put(buffer);
        } while (tmp.hasRemaining());
        tmp.flip();
        return getEncryptedPacketLength(tmp);
    }
    
    private static int getEncryptedPacketLength(final ByteBuffer buffer) {
        int packetLength = 0;
        final int pos = buffer.position();
        boolean tls = false;
        switch (unsignedByte(buffer.get(pos))) {
            case 20:
            case 21:
            case 22:
            case 23: {
                tls = true;
                break;
            }
            default: {
                tls = false;
                break;
            }
        }
        if (tls) {
            final int majorVersion = unsignedByte(buffer.get(pos + 1));
            if (majorVersion == 3) {
                packetLength = unsignedShort(buffer.getShort(pos + 3)) + 5;
                if (packetLength <= 5) {
                    tls = false;
                }
            }
            else {
                tls = false;
            }
        }
        if (!tls) {
            final int headerLength = ((unsignedByte(buffer.get(pos)) & 0x80) != 0x0) ? 2 : 3;
            final int majorVersion2 = unsignedByte(buffer.get(pos + headerLength + 1));
            if (majorVersion2 != 2 && majorVersion2 != 3) {
                return -2;
            }
            if (headerLength == 2) {
                packetLength = (buffer.getShort(pos) & 0x7FFF) + 2;
            }
            else {
                packetLength = (buffer.getShort(pos) & 0x3FFF) + 3;
            }
            if (packetLength <= headerLength) {
                return -1;
            }
        }
        return packetLength;
    }
    
    static void notifyHandshakeFailure(final ChannelHandlerContext ctx, final Throwable cause) {
        ctx.flush();
        ctx.fireUserEventTriggered((Object)new SslHandshakeCompletionEvent(cause));
        ctx.close();
    }
    
    static void zeroout(final ByteBuf buffer) {
        if (!buffer.isReadOnly()) {
            buffer.setZero(0, buffer.capacity());
        }
    }
    
    static void zerooutAndRelease(final ByteBuf buffer) {
        zeroout(buffer);
        buffer.release();
    }
    
    static ByteBuf toBase64(final ByteBufAllocator allocator, final ByteBuf src) {
        final ByteBuf dst = Base64.encode(src, src.readerIndex(), src.readableBytes(), true, Base64Dialect.STANDARD, allocator);
        src.readerIndex(src.writerIndex());
        return dst;
    }
    
    private SslUtils() {
    }
}
