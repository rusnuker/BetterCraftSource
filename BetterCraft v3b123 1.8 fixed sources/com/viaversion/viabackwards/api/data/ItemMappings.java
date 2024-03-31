// 
// Decompiled by Procyon v0.6.0
// 

package com.viaversion.viabackwards.api.data;

import com.viaversion.viaversion.api.data.Mappings;
import com.viaversion.viaversion.api.data.BiMappingsBase;

public final class ItemMappings extends BiMappingsBase
{
    private ItemMappings(final Mappings mappings, final Mappings inverse) {
        super(mappings, inverse);
    }
    
    public static ItemMappings of(final Mappings mappings, final Mappings inverse) {
        return new ItemMappings(mappings, inverse);
    }
    
    @Override
    public void setNewId(final int id, final int mappedId) {
        this.mappings.setNewId(id, mappedId);
    }
}
