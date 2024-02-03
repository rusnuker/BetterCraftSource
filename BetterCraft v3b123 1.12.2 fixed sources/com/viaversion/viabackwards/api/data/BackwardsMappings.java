// 
// Decompiled by Procyon v0.6.0
// 

package com.viaversion.viabackwards.api.data;

import java.util.logging.Logger;
import com.viaversion.viabackwards.ViaBackwards;
import com.viaversion.viaversion.util.Key;
import com.viaversion.viaversion.api.data.MappingData;
import com.viaversion.viaversion.api.data.Mappings;
import com.viaversion.viaversion.api.Via;
import com.viaversion.viaversion.api.data.BiMappings;
import java.util.Iterator;
import java.util.HashMap;
import com.viaversion.viaversion.libs.opennbt.tag.builtin.NumberTag;
import com.viaversion.viaversion.libs.opennbt.tag.builtin.StringTag;
import com.viaversion.viaversion.libs.opennbt.tag.builtin.Tag;
import com.viaversion.viaversion.libs.fastutil.ints.Int2ObjectOpenHashMap;
import com.viaversion.viaversion.libs.opennbt.tag.builtin.CompoundTag;
import com.google.common.base.Preconditions;
import com.viaversion.viabackwards.api.BackwardsProtocol;
import java.util.Map;
import com.viaversion.viaversion.libs.fastutil.ints.Int2ObjectMap;
import com.viaversion.viaversion.api.protocol.Protocol;
import com.viaversion.viaversion.api.data.MappingDataBase;

public class BackwardsMappings extends MappingDataBase
{
    private final Class<? extends Protocol<?, ?, ?, ?>> vvProtocolClass;
    protected Int2ObjectMap<MappedItem> backwardsItemMappings;
    private Map<String, String> backwardsSoundMappings;
    private Map<String, String> entityNames;
    
    public BackwardsMappings(final String unmappedVersion, final String mappedVersion) {
        this(unmappedVersion, mappedVersion, null);
    }
    
    public BackwardsMappings(final String unmappedVersion, final String mappedVersion, final Class<? extends Protocol<?, ?, ?, ?>> vvProtocolClass) {
        super(unmappedVersion, mappedVersion);
        Preconditions.checkArgument(vvProtocolClass == null || !vvProtocolClass.isAssignableFrom(BackwardsProtocol.class));
        this.vvProtocolClass = vvProtocolClass;
    }
    
    @Override
    protected void loadExtras(final CompoundTag data) {
        final CompoundTag itemNames = data.get("itemnames");
        if (itemNames != null) {
            Preconditions.checkNotNull(this.itemMappings);
            this.backwardsItemMappings = new Int2ObjectOpenHashMap<MappedItem>(itemNames.size());
            final CompoundTag extraItemData = data.get("itemdata");
            for (final Map.Entry<String, Tag> entry : itemNames.entrySet()) {
                final StringTag name = entry.getValue();
                final int id = Integer.parseInt(entry.getKey());
                Integer customModelData = null;
                if (extraItemData != null && extraItemData.contains(entry.getKey())) {
                    final CompoundTag entryTag = extraItemData.get(entry.getKey());
                    final NumberTag customModelDataTag = entryTag.get("custom_model_data");
                    customModelData = ((customModelDataTag != null) ? Integer.valueOf(customModelDataTag.asInt()) : null);
                }
                this.backwardsItemMappings.put(id, new MappedItem(this.getNewItemId(id), name.getValue(), customModelData));
            }
        }
        final CompoundTag entityNames = data.get("entitynames");
        if (entityNames != null) {
            this.entityNames = new HashMap<String, String>(entityNames.size());
            for (final Map.Entry<String, Tag> entry : entityNames.entrySet()) {
                final StringTag mappedTag = entry.getValue();
                this.entityNames.put(entry.getKey(), mappedTag.getValue());
            }
        }
        final CompoundTag soundNames = data.get("soundnames");
        if (soundNames != null) {
            this.backwardsSoundMappings = new HashMap<String, String>(soundNames.size());
            for (final Map.Entry<String, Tag> entry2 : soundNames.entrySet()) {
                final StringTag mappedTag2 = entry2.getValue();
                this.backwardsSoundMappings.put(entry2.getKey(), mappedTag2.getValue());
            }
        }
    }
    
    @Override
    protected BiMappings loadBiMappings(final CompoundTag data, final String key) {
        if (key.equals("items") && this.vvProtocolClass != null) {
            final Mappings mappings = super.loadMappings(data, key);
            final MappingData mappingData = ((Protocol)Via.getManager().getProtocolManager().getProtocol(this.vvProtocolClass)).getMappingData();
            if (mappingData != null && mappingData.getItemMappings() != null) {
                return ItemMappings.of(mappings, mappingData.getItemMappings());
            }
        }
        return super.loadBiMappings(data, key);
    }
    
    @Override
    public int getNewItemId(final int id) {
        return this.itemMappings.getNewId(id);
    }
    
    @Override
    public int getNewBlockId(final int id) {
        return this.blockMappings.getNewId(id);
    }
    
    @Override
    public int getOldItemId(final int id) {
        return this.checkValidity(id, this.itemMappings.inverse().getNewId(id), "item");
    }
    
    public MappedItem getMappedItem(final int id) {
        return (this.backwardsItemMappings != null) ? this.backwardsItemMappings.get(id) : null;
    }
    
    public String getMappedNamedSound(final String id) {
        if (this.backwardsSoundMappings == null) {
            return null;
        }
        return this.backwardsSoundMappings.get(Key.stripMinecraftNamespace(id));
    }
    
    public String mappedEntityName(final String entityName) {
        if (this.entityNames == null) {
            ViaBackwards.getPlatform().getLogger().severe("No entity mappings found when requesting them for " + entityName);
            new Exception().printStackTrace();
            return null;
        }
        return this.entityNames.get(entityName);
    }
    
    public Int2ObjectMap<MappedItem> getBackwardsItemMappings() {
        return this.backwardsItemMappings;
    }
    
    public Map<String, String> getBackwardsSoundMappings() {
        return this.backwardsSoundMappings;
    }
    
    public Class<? extends Protocol<?, ?, ?, ?>> getViaVersionProtocolClass() {
        return this.vvProtocolClass;
    }
    
    @Override
    protected Logger getLogger() {
        return ViaBackwards.getPlatform().getLogger();
    }
    
    @Override
    protected CompoundTag readNBTFile(final String name) {
        return VBMappingDataLoader.loadNBTFromDir(name);
    }
}
