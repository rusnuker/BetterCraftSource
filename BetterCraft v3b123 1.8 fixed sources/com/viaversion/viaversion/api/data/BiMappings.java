// 
// Decompiled by Procyon v0.6.0
// 

package com.viaversion.viaversion.api.data;

public interface BiMappings extends Mappings
{
    BiMappings inverse();
    
    default BiMappings of(final Mappings mappings) {
        return of(mappings, mappings.inverse());
    }
    
    default BiMappings of(final Mappings mappings, final Mappings inverse) {
        return new BiMappingsBase(mappings, inverse);
    }
}
