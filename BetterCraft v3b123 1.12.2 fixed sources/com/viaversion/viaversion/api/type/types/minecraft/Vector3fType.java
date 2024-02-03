// 
// Decompiled by Procyon v0.6.0
// 

package com.viaversion.viaversion.api.type.types.minecraft;

import io.netty.buffer.ByteBuf;
import com.viaversion.viaversion.api.minecraft.Vector3f;
import com.viaversion.viaversion.api.type.Type;

public class Vector3fType extends Type<Vector3f>
{
    public Vector3fType() {
        super(Vector3f.class);
    }
    
    @Override
    public Vector3f read(final ByteBuf buffer) throws Exception {
        final float x = buffer.readFloat();
        final float y = buffer.readFloat();
        final float z = buffer.readFloat();
        return new Vector3f(x, y, z);
    }
    
    @Override
    public void write(final ByteBuf buffer, final Vector3f object) throws Exception {
        buffer.writeFloat(object.x());
        buffer.writeFloat(object.y());
        buffer.writeFloat(object.z());
    }
}
