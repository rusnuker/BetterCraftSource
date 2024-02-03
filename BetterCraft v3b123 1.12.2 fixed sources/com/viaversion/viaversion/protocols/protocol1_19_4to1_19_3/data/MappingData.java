// 
// Decompiled by Procyon v0.6.0
// 

package com.viaversion.viaversion.protocols.protocol1_19_4to1_19_3.data;

import java.io.IOException;
import com.viaversion.viaversion.api.minecraft.nbt.BinaryTagIO;
import com.viaversion.viaversion.api.data.MappingDataLoader;
import com.viaversion.viaversion.libs.opennbt.tag.builtin.CompoundTag;
import com.viaversion.viaversion.api.data.MappingDataBase;

public final class MappingData extends MappingDataBase
{
    private CompoundTag damageTypesRegistry;
    
    public MappingData() {
        super("1.19.3", "1.19.4");
    }
    
    @Override
    protected void loadExtras(final CompoundTag data) {
        try {
            this.damageTypesRegistry = BinaryTagIO.readInputStream(MappingDataLoader.getResource("damage-types-1.19.4.nbt"));
        }
        catch (final IOException e) {
            throw new RuntimeException(e);
        }
    }
    
    public CompoundTag damageTypesRegistry() {
        return this.damageTypesRegistry.clone();
    }
}
