// 
// Decompiled by Procyon v0.6.0
// 

package com.viaversion.viaversion.api.data;

import com.viaversion.viaversion.api.minecraft.TagData;
import java.util.List;
import com.viaversion.viaversion.api.minecraft.RegistryType;

public interface MappingData
{
    void load();
    
    int getNewBlockStateId(final int p0);
    
    int getNewBlockId(final int p0);
    
    int getNewItemId(final int p0);
    
    int getOldItemId(final int p0);
    
    int getNewParticleId(final int p0);
    
    List<TagData> getTags(final RegistryType p0);
    
    BiMappings getItemMappings();
    
    ParticleMappings getParticleMappings();
    
    Mappings getBlockMappings();
    
    Mappings getBlockEntityMappings();
    
    Mappings getBlockStateMappings();
    
    Mappings getSoundMappings();
    
    Mappings getStatisticsMappings();
    
    Mappings getMenuMappings();
    
    Mappings getEnchantmentMappings();
    
    FullMappings getEntityMappings();
    
    FullMappings getArgumentTypeMappings();
    
    Mappings getPaintingMappings();
}
