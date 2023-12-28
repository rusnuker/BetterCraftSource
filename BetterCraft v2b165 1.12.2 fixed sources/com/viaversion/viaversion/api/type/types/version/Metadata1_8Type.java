// 
// Decompiled by Procyon v0.6.0
// 

package com.viaversion.viaversion.api.type.types.version;

import com.viaversion.viaversion.api.minecraft.metadata.MetaType;
import com.viaversion.viaversion.api.minecraft.metadata.types.MetaType1_8;
import com.viaversion.viaversion.api.minecraft.metadata.Metadata;
import io.netty.buffer.ByteBuf;
import com.viaversion.viaversion.api.type.types.minecraft.MetaTypeTemplate;

public class Metadata1_8Type extends MetaTypeTemplate
{
    @Override
    public Metadata read(final ByteBuf buffer) throws Exception {
        final byte item = buffer.readByte();
        if (item == 127) {
            return null;
        }
        final int typeID = (item & 0xE0) >> 5;
        final MetaType1_8 type = MetaType1_8.byId(typeID);
        final int id = item & 0x1F;
        return new Metadata(id, type, type.type().read(buffer));
    }
    
    @Override
    public void write(final ByteBuf buffer, final Metadata meta) throws Exception {
        final byte item = (byte)(meta.metaType().typeId() << 5 | (meta.id() & 0x1F));
        buffer.writeByte(item);
        meta.metaType().type().write(buffer, meta.getValue());
    }
}
