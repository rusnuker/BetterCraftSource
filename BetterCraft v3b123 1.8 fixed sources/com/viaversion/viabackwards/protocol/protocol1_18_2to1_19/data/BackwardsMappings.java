// 
// Decompiled by Procyon v0.6.0
// 

package com.viaversion.viabackwards.protocol.protocol1_18_2to1_19.data;

import java.util.Iterator;
import java.io.IOException;
import com.viaversion.viaversion.libs.opennbt.tag.builtin.NumberTag;
import com.viaversion.viaversion.libs.opennbt.tag.builtin.Tag;
import com.viaversion.viaversion.api.minecraft.nbt.BinaryTagIO;
import com.viaversion.viabackwards.api.data.VBMappingDataLoader;
import com.viaversion.viaversion.libs.opennbt.tag.builtin.ListTag;
import com.viaversion.viaversion.libs.fastutil.ints.Int2ObjectOpenHashMap;
import com.viaversion.viaversion.api.protocol.Protocol;
import com.viaversion.viaversion.protocols.protocol1_19to1_18_2.Protocol1_19To1_18_2;
import com.viaversion.viaversion.libs.opennbt.tag.builtin.CompoundTag;
import com.viaversion.viaversion.libs.fastutil.ints.Int2ObjectMap;

public final class BackwardsMappings extends com.viaversion.viabackwards.api.data.BackwardsMappings
{
    private final Int2ObjectMap<CompoundTag> defaultChatTypes;
    
    public BackwardsMappings() {
        super("1.19", "1.18", Protocol1_19To1_18_2.class);
        this.defaultChatTypes = new Int2ObjectOpenHashMap<CompoundTag>();
    }
    
    @Override
    protected void loadExtras(final CompoundTag data) {
        super.loadExtras(data);
        try {
            final ListTag chatTypes = BinaryTagIO.readInputStream(VBMappingDataLoader.getResource("chat-types-1.19.1.nbt")).get("values");
            for (final Tag chatType : chatTypes) {
                final CompoundTag chatTypeCompound = (CompoundTag)chatType;
                final NumberTag idTag = chatTypeCompound.get("id");
                this.defaultChatTypes.put(idTag.asInt(), chatTypeCompound);
            }
        }
        catch (final IOException e) {
            e.printStackTrace();
        }
    }
    
    public CompoundTag chatType(final int id) {
        return this.defaultChatTypes.get(id);
    }
}
