// 
// Decompiled by Procyon v0.6.0
// 

package com.viaversion.viaversion.api.type.types.version;

import com.viaversion.viaversion.api.type.types.minecraft.Particle1_14Type;
import com.viaversion.viaversion.api.type.types.minecraft.MetaListType;
import com.viaversion.viaversion.api.type.types.Particle;
import java.util.List;
import com.viaversion.viaversion.api.minecraft.metadata.Metadata;
import com.viaversion.viaversion.api.type.Type;

public final class Types1_14
{
    public static final Type<Metadata> METADATA;
    public static final Type<List<Metadata>> METADATA_LIST;
    public static final Type<Particle> PARTICLE;
    
    static {
        METADATA = new Metadata1_14Type();
        METADATA_LIST = new MetaListType(Types1_14.METADATA);
        PARTICLE = new Particle1_14Type();
    }
}
