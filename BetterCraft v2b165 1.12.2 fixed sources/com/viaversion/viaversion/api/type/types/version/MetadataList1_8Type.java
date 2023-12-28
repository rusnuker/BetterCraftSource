// 
// Decompiled by Procyon v0.6.0
// 

package com.viaversion.viaversion.api.type.types.version;

import io.netty.buffer.ByteBuf;
import com.viaversion.viaversion.api.minecraft.metadata.Metadata;
import com.viaversion.viaversion.api.type.Type;
import com.viaversion.viaversion.api.type.types.minecraft.AbstractMetaListType;

public class MetadataList1_8Type extends AbstractMetaListType
{
    @Override
    protected Type<Metadata> getType() {
        return Types1_8.METADATA;
    }
    
    @Override
    protected void writeEnd(final Type<Metadata> type, final ByteBuf buffer) throws Exception {
        buffer.writeByte(127);
    }
}
