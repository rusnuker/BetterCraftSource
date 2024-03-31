// 
// Decompiled by Procyon v0.6.0
// 

package com.viaversion.viaversion.api.type.types.minecraft;

import io.netty.buffer.ByteBuf;
import com.viaversion.viaversion.api.minecraft.Position;
import com.viaversion.viaversion.api.type.Type;

public class PositionType extends Type<Position>
{
    public PositionType() {
        super(Position.class);
    }
    
    @Override
    public Position read(final ByteBuf buffer) {
        final long val = buffer.readLong();
        final long x = val >> 38;
        final long y = val >> 26 & 0xFFFL;
        final long z = val << 38 >> 38;
        return new Position((int)x, (short)y, (int)z);
    }
    
    @Override
    public void write(final ByteBuf buffer, final Position object) {
        buffer.writeLong(((long)object.getX() & 0x3FFFFFFL) << 38 | ((long)object.getY() & 0xFFFL) << 26 | (long)(object.getZ() & 0x3FFFFFF));
    }
}
