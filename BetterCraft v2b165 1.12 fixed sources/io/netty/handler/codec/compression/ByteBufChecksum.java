// 
// Decompiled by Procyon v0.6.0
// 

package io.netty.handler.codec.compression;

import io.netty.buffer.ByteBuf;
import java.util.zip.CRC32;
import java.util.zip.Adler32;
import io.netty.util.internal.ObjectUtil;
import java.nio.ByteBuffer;
import io.netty.util.internal.PlatformDependent;
import io.netty.util.ByteProcessor;
import java.lang.reflect.Method;
import java.util.zip.Checksum;

abstract class ByteBufChecksum implements Checksum
{
    private static final Method ADLER32_UPDATE_METHOD;
    private static final Method CRC32_UPDATE_METHOD;
    private final ByteProcessor updateProcessor;
    
    ByteBufChecksum() {
        this.updateProcessor = new ByteProcessor() {
            @Override
            public boolean process(final byte value) throws Exception {
                ByteBufChecksum.this.update(value);
                return true;
            }
        };
    }
    
    private static Method updateByteBuffer(final Checksum checksum) {
        if (PlatformDependent.javaVersion() >= 8) {
            try {
                final Method method = checksum.getClass().getDeclaredMethod("update", ByteBuffer.class);
                method.invoke(method, ByteBuffer.allocate(1));
                return method;
            }
            catch (final Throwable ignore) {
                return null;
            }
        }
        return null;
    }
    
    static ByteBufChecksum wrapChecksum(final Checksum checksum) {
        ObjectUtil.checkNotNull(checksum, "checksum");
        if (checksum instanceof Adler32 && ByteBufChecksum.ADLER32_UPDATE_METHOD != null) {
            return new ReflectiveByteBufChecksum(checksum, ByteBufChecksum.ADLER32_UPDATE_METHOD);
        }
        if (checksum instanceof CRC32 && ByteBufChecksum.CRC32_UPDATE_METHOD != null) {
            return new ReflectiveByteBufChecksum(checksum, ByteBufChecksum.CRC32_UPDATE_METHOD);
        }
        return new SlowByteBufChecksum(checksum);
    }
    
    public void update(final ByteBuf b, final int off, final int len) {
        if (b.hasArray()) {
            this.update(b.array(), b.arrayOffset() + off, len);
        }
        else {
            b.forEachByte(off, len, this.updateProcessor);
        }
    }
    
    static {
        ADLER32_UPDATE_METHOD = updateByteBuffer(new Adler32());
        CRC32_UPDATE_METHOD = updateByteBuffer(new CRC32());
    }
    
    private static final class ReflectiveByteBufChecksum extends SlowByteBufChecksum
    {
        private final Method method;
        
        ReflectiveByteBufChecksum(final Checksum checksum, final Method method) {
            super(checksum);
            this.method = method;
        }
        
        @Override
        public void update(final ByteBuf b, final int off, final int len) {
            if (b.hasArray()) {
                this.update(b.array(), b.arrayOffset() + off, len);
            }
            else {
                try {
                    this.method.invoke(this.checksum, CompressionUtil.safeNioBuffer(b));
                }
                catch (final Throwable cause) {
                    throw new Error();
                }
            }
        }
    }
    
    private static class SlowByteBufChecksum extends ByteBufChecksum
    {
        protected final Checksum checksum;
        
        SlowByteBufChecksum(final Checksum checksum) {
            this.checksum = checksum;
        }
        
        @Override
        public void update(final int b) {
            this.checksum.update(b);
        }
        
        @Override
        public void update(final byte[] b, final int off, final int len) {
            this.checksum.update(b, off, len);
        }
        
        @Override
        public long getValue() {
            return this.checksum.getValue();
        }
        
        @Override
        public void reset() {
            this.checksum.reset();
        }
    }
}
