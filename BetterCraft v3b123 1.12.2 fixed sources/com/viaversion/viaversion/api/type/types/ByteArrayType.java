// 
// Decompiled by Procyon v0.6.0
// 

package com.viaversion.viaversion.api.type.types;

import com.viaversion.viaversion.api.type.OptionalType;
import com.google.common.base.Preconditions;
import io.netty.buffer.ByteBuf;
import com.viaversion.viaversion.api.type.Type;

public class ByteArrayType extends Type<byte[]>
{
    private final int length;
    
    public ByteArrayType(final int length) {
        super(byte[].class);
        this.length = length;
    }
    
    public ByteArrayType() {
        super(byte[].class);
        this.length = -1;
    }
    
    @Override
    public void write(final ByteBuf buffer, final byte[] object) throws Exception {
        if (this.length != -1) {
            Preconditions.checkArgument(this.length == object.length, (Object)"Length does not match expected length");
        }
        else {
            Type.VAR_INT.writePrimitive(buffer, object.length);
        }
        buffer.writeBytes(object);
    }
    
    @Override
    public byte[] read(final ByteBuf buffer) throws Exception {
        final int length = (this.length == -1) ? Type.VAR_INT.readPrimitive(buffer) : this.length;
        Preconditions.checkArgument(buffer.isReadable(length), (Object)"Length is fewer than readable bytes");
        final byte[] array = new byte[length];
        buffer.readBytes(array);
        return array;
    }
    
    public static final class OptionalByteArrayType extends OptionalType<byte[]>
    {
        public OptionalByteArrayType() {
            super(Type.BYTE_ARRAY_PRIMITIVE);
        }
        
        public OptionalByteArrayType(final int length) {
            super(new ByteArrayType(length));
        }
    }
}
