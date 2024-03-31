// 
// Decompiled by Procyon v0.6.0
// 

package com.viaversion.viaversion.api.minecraft.blockentity;

import com.viaversion.viaversion.libs.opennbt.tag.builtin.CompoundTag;

public interface BlockEntity
{
    default byte pack(final int sectionX, final int sectionZ) {
        return (byte)((sectionX & 0xF) << 4 | (sectionZ & 0xF));
    }
    
    default byte sectionX() {
        return (byte)(this.packedXZ() >> 4 & 0xF);
    }
    
    default byte sectionZ() {
        return (byte)(this.packedXZ() & 0xF);
    }
    
    byte packedXZ();
    
    short y();
    
    int typeId();
    
    CompoundTag tag();
    
    BlockEntity withTypeId(final int p0);
}
