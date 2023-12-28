// 
// Decompiled by Procyon v0.6.0
// 

package com.viaversion.viaversion.api.type.types.minecraft;

import io.netty.buffer.ByteBuf;
import com.viaversion.viaversion.api.minecraft.metadata.Metadata;
import com.viaversion.viaversion.api.type.Type;

public abstract class ModernMetaListType extends AbstractMetaListType
{
    @Override
    protected void writeEnd(final Type<Metadata> type, final ByteBuf buffer) throws Exception {
        type.write(buffer, null);
    }
}
