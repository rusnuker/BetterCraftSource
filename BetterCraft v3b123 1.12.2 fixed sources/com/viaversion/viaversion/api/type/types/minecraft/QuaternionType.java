// 
// Decompiled by Procyon v0.6.0
// 

package com.viaversion.viaversion.api.type.types.minecraft;

import io.netty.buffer.ByteBuf;
import com.viaversion.viaversion.api.minecraft.Quaternion;
import com.viaversion.viaversion.api.type.Type;

public class QuaternionType extends Type<Quaternion>
{
    public QuaternionType() {
        super(Quaternion.class);
    }
    
    @Override
    public Quaternion read(final ByteBuf buffer) throws Exception {
        final float x = buffer.readFloat();
        final float y = buffer.readFloat();
        final float z = buffer.readFloat();
        final float w = buffer.readFloat();
        return new Quaternion(x, y, z, w);
    }
    
    @Override
    public void write(final ByteBuf buffer, final Quaternion object) throws Exception {
        buffer.writeFloat(object.x());
        buffer.writeFloat(object.y());
        buffer.writeFloat(object.z());
        buffer.writeFloat(object.w());
    }
}
