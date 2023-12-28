// 
// Decompiled by Procyon v0.6.0
// 

package com.viaversion.viaversion.api.type.types.version;

import com.viaversion.viaversion.api.type.types.minecraft.Particle1_13Type;
import com.viaversion.viaversion.api.type.types.minecraft.MetaListType;
import com.viaversion.viaversion.api.type.types.Particle;
import com.viaversion.viaversion.api.minecraft.chunks.ChunkSection;
import java.util.List;
import com.viaversion.viaversion.api.minecraft.metadata.Metadata;
import com.viaversion.viaversion.api.type.Type;

public class Types1_13
{
    public static final Type<Metadata> METADATA;
    public static final Type<List<Metadata>> METADATA_LIST;
    public static final Type<ChunkSection> CHUNK_SECTION;
    public static final Type<Particle> PARTICLE;
    
    static {
        METADATA = new Metadata1_13Type();
        METADATA_LIST = new MetaListType(Types1_13.METADATA);
        CHUNK_SECTION = new ChunkSectionType1_13();
        PARTICLE = new Particle1_13Type();
    }
}
