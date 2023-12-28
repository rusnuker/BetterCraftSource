// 
// Decompiled by Procyon v0.6.0
// 

package com.viaversion.viaversion.api.type.types.minecraft;

import java.util.Iterator;
import java.util.ArrayList;
import java.util.List;
import io.netty.buffer.ByteBuf;
import com.viaversion.viaversion.api.minecraft.metadata.Metadata;
import com.viaversion.viaversion.api.type.Type;

public abstract class AbstractMetaListType extends MetaListTypeTemplate
{
    protected abstract Type<Metadata> getType();
    
    @Override
    public List<Metadata> read(final ByteBuf buffer) throws Exception {
        final Type<Metadata> type = this.getType();
        final List<Metadata> list = new ArrayList<Metadata>();
        Metadata meta;
        do {
            meta = type.read(buffer);
            if (meta != null) {
                list.add(meta);
            }
        } while (meta != null);
        return list;
    }
    
    @Override
    public void write(final ByteBuf buffer, final List<Metadata> object) throws Exception {
        final Type<Metadata> type = this.getType();
        for (final Metadata metadata : object) {
            type.write(buffer, metadata);
        }
        this.writeEnd(type, buffer);
    }
    
    protected abstract void writeEnd(final Type<Metadata> p0, final ByteBuf p1) throws Exception;
}
