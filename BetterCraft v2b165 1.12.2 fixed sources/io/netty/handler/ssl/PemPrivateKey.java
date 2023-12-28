// 
// Decompiled by Procyon v0.6.0
// 

package io.netty.handler.ssl;

import io.netty.util.CharsetUtil;
import io.netty.buffer.ByteBufHolder;
import io.netty.util.ReferenceCounted;
import io.netty.util.IllegalReferenceCountException;
import io.netty.util.internal.ObjectUtil;
import io.netty.buffer.Unpooled;
import io.netty.buffer.ByteBufAllocator;
import io.netty.buffer.ByteBuf;
import java.security.PrivateKey;
import io.netty.util.AbstractReferenceCounted;

public final class PemPrivateKey extends AbstractReferenceCounted implements PrivateKey, PemEncoded
{
    private static final byte[] BEGIN_PRIVATE_KEY;
    private static final byte[] END_PRIVATE_KEY;
    private static final String PKCS8_FORMAT = "PKCS#8";
    private final ByteBuf content;
    
    static PemEncoded toPEM(final ByteBufAllocator allocator, final boolean useDirect, final PrivateKey key) {
        if (key instanceof PemEncoded) {
            return ((PemEncoded)key).retain();
        }
        final ByteBuf encoded = Unpooled.wrappedBuffer(key.getEncoded());
        try {
            final ByteBuf base64 = SslUtils.toBase64(allocator, encoded);
            try {
                final int size = PemPrivateKey.BEGIN_PRIVATE_KEY.length + base64.readableBytes() + PemPrivateKey.END_PRIVATE_KEY.length;
                boolean success = false;
                final ByteBuf pem = useDirect ? allocator.directBuffer(size) : allocator.buffer(size);
                try {
                    pem.writeBytes(PemPrivateKey.BEGIN_PRIVATE_KEY);
                    pem.writeBytes(base64);
                    pem.writeBytes(PemPrivateKey.END_PRIVATE_KEY);
                    final PemValue value = new PemValue(pem, true);
                    success = true;
                    return value;
                }
                finally {
                    if (!success) {
                        SslUtils.zerooutAndRelease(pem);
                    }
                }
            }
            finally {
                SslUtils.zerooutAndRelease(base64);
            }
        }
        finally {
            SslUtils.zerooutAndRelease(encoded);
        }
    }
    
    public static PemPrivateKey valueOf(final byte[] key) {
        return valueOf(Unpooled.wrappedBuffer(key));
    }
    
    public static PemPrivateKey valueOf(final ByteBuf key) {
        return new PemPrivateKey(key);
    }
    
    private PemPrivateKey(final ByteBuf content) {
        this.content = ObjectUtil.checkNotNull(content, "content");
    }
    
    @Override
    public boolean isSensitive() {
        return true;
    }
    
    @Override
    public ByteBuf content() {
        final int count = this.refCnt();
        if (count <= 0) {
            throw new IllegalReferenceCountException(count);
        }
        return this.content;
    }
    
    @Override
    public PemPrivateKey copy() {
        return this.replace(this.content.copy());
    }
    
    @Override
    public PemPrivateKey duplicate() {
        return this.replace(this.content.duplicate());
    }
    
    @Override
    public PemPrivateKey retainedDuplicate() {
        return this.replace(this.content.retainedDuplicate());
    }
    
    @Override
    public PemPrivateKey replace(final ByteBuf content) {
        return new PemPrivateKey(content);
    }
    
    @Override
    public PemPrivateKey touch() {
        this.content.touch();
        return this;
    }
    
    @Override
    public PemPrivateKey touch(final Object hint) {
        this.content.touch(hint);
        return this;
    }
    
    @Override
    public PemPrivateKey retain() {
        return (PemPrivateKey)super.retain();
    }
    
    @Override
    public PemPrivateKey retain(final int increment) {
        return (PemPrivateKey)super.retain(increment);
    }
    
    @Override
    protected void deallocate() {
        SslUtils.zerooutAndRelease(this.content);
    }
    
    @Override
    public byte[] getEncoded() {
        throw new UnsupportedOperationException();
    }
    
    @Override
    public String getAlgorithm() {
        throw new UnsupportedOperationException();
    }
    
    @Override
    public String getFormat() {
        return "PKCS#8";
    }
    
    @Override
    public void destroy() {
        this.release(this.refCnt());
    }
    
    @Override
    public boolean isDestroyed() {
        return this.refCnt() == 0;
    }
    
    static {
        BEGIN_PRIVATE_KEY = "-----BEGIN PRIVATE KEY-----\n".getBytes(CharsetUtil.US_ASCII);
        END_PRIVATE_KEY = "\n-----END PRIVATE KEY-----\n".getBytes(CharsetUtil.US_ASCII);
    }
}
