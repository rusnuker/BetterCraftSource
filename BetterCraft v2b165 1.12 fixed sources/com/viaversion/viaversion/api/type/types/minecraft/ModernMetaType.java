// 
// Decompiled by Procyon v0.6.0
// 

package com.viaversion.viaversion.api.type.types.minecraft;

import com.viaversion.viaversion.api.minecraft.metadata.MetaType;
import com.viaversion.viaversion.api.minecraft.metadata.Metadata;
import io.netty.buffer.ByteBuf;

public abstract class ModernMetaType extends MetaTypeTemplate
{
    @Override
    public Metadata read(final ByteBuf buffer) throws Exception {
        final short index = buffer.readUnsignedByte();
        if (index == 255) {
            return null;
        }
        final MetaType type = this.getType(buffer.readByte());
        return new Metadata(index, type, type.type().read(buffer));
    }
    
    protected abstract MetaType getType(final int p0);
    
    @Override
    public void write(final ByteBuf buffer, final Metadata object) throws Exception {
        if (object == null) {
            buffer.writeByte(255);
        }
        else {
            buffer.writeByte(object.id());
            final MetaType type = object.metaType();
            buffer.writeByte(type.typeId());
            type.type().write(buffer, object.getValue());
        }
    }
}
