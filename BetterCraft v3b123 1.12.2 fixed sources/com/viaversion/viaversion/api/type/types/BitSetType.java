// 
// Decompiled by Procyon v0.6.0
// 

package com.viaversion.viaversion.api.type.types;

import java.util.Arrays;
import com.google.common.base.Preconditions;
import io.netty.buffer.ByteBuf;
import java.util.BitSet;
import com.viaversion.viaversion.api.type.Type;

public class BitSetType extends Type<BitSet>
{
    private final int length;
    private final int bytesLength;
    
    public BitSetType(final int length) {
        super(BitSet.class);
        this.length = length;
        this.bytesLength = -Math.floorDiv(-length, 8);
    }
    
    @Override
    public BitSet read(final ByteBuf buffer) {
        final byte[] bytes = new byte[this.bytesLength];
        buffer.readBytes(bytes);
        return BitSet.valueOf(bytes);
    }
    
    @Override
    public void write(final ByteBuf buffer, final BitSet object) {
        Preconditions.checkArgument(object.length() <= this.length, (Object)("BitSet of length " + object.length() + " larger than max length " + this.length));
        final byte[] bytes = object.toByteArray();
        buffer.writeBytes(Arrays.copyOf(bytes, this.bytesLength));
    }
}
