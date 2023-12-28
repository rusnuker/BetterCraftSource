// 
// Decompiled by Procyon v0.6.0
// 

package io.netty.handler.codec.compression;

import java.nio.ByteBuffer;
import io.netty.buffer.ByteBuf;

final class CompressionUtil
{
    private CompressionUtil() {
    }
    
    static void checkChecksum(final ByteBufChecksum checksum, final ByteBuf uncompressed, final int currentChecksum) {
        checksum.reset();
        checksum.update(uncompressed, uncompressed.readerIndex(), uncompressed.readableBytes());
        final int checksumResult = (int)checksum.getValue();
        if (checksumResult != currentChecksum) {
            throw new DecompressionException(String.format("stream corrupted: mismatching checksum: %d (expected: %d)", checksumResult, currentChecksum));
        }
    }
    
    static ByteBuffer safeNioBuffer(final ByteBuf buffer) {
        return (buffer.nioBufferCount() == 1) ? buffer.internalNioBuffer(buffer.readerIndex(), buffer.readableBytes()) : buffer.nioBuffer();
    }
}
