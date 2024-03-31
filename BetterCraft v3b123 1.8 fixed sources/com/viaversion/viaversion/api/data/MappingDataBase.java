// 
// Decompiled by Procyon v0.6.0
// 

package com.viaversion.viaversion.api.data;

import java.util.logging.Logger;
import java.util.Iterator;
import com.viaversion.viaversion.libs.opennbt.tag.builtin.IntArrayTag;
import com.viaversion.viaversion.libs.opennbt.tag.builtin.Tag;
import java.util.ArrayList;
import java.util.EnumMap;
import com.viaversion.viaversion.libs.opennbt.tag.builtin.CompoundTag;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import com.viaversion.viaversion.libs.opennbt.tag.builtin.ListTag;
import com.viaversion.viaversion.api.Via;
import com.viaversion.viaversion.api.minecraft.TagData;
import java.util.List;
import com.viaversion.viaversion.api.minecraft.RegistryType;
import java.util.Map;

public class MappingDataBase implements MappingData
{
    protected final String unmappedVersion;
    protected final String mappedVersion;
    protected BiMappings itemMappings;
    protected FullMappings argumentTypeMappings;
    protected FullMappings entityMappings;
    protected ParticleMappings particleMappings;
    protected Mappings blockMappings;
    protected Mappings blockStateMappings;
    protected Mappings blockEntityMappings;
    protected Mappings soundMappings;
    protected Mappings statisticsMappings;
    protected Mappings enchantmentMappings;
    protected Mappings paintingMappings;
    protected Mappings menuMappings;
    protected Map<RegistryType, List<TagData>> tags;
    
    public MappingDataBase(final String unmappedVersion, final String mappedVersion) {
        this.unmappedVersion = unmappedVersion;
        this.mappedVersion = mappedVersion;
    }
    
    @Override
    public void load() {
        if (Via.getManager().isDebug()) {
            this.getLogger().info("Loading " + this.unmappedVersion + " -> " + this.mappedVersion + " mappings...");
        }
        final CompoundTag data = this.readNBTFile("mappings-" + this.unmappedVersion + "to" + this.mappedVersion + ".nbt");
        this.blockMappings = this.loadMappings(data, "blocks");
        this.blockStateMappings = this.loadMappings(data, "blockstates");
        this.blockEntityMappings = this.loadMappings(data, "blockentities");
        this.soundMappings = this.loadMappings(data, "sounds");
        this.statisticsMappings = this.loadMappings(data, "statistics");
        this.menuMappings = this.loadMappings(data, "menus");
        this.enchantmentMappings = this.loadMappings(data, "enchantments");
        this.paintingMappings = this.loadMappings(data, "paintings");
        this.itemMappings = this.loadBiMappings(data, "items");
        final CompoundTag unmappedIdentifierData = MappingDataLoader.loadNBT("identifiers-" + this.unmappedVersion + ".nbt", true);
        final CompoundTag mappedIdentifierData = MappingDataLoader.loadNBT("identifiers-" + this.mappedVersion + ".nbt", true);
        if (unmappedIdentifierData != null && mappedIdentifierData != null) {
            this.entityMappings = this.loadFullMappings(data, unmappedIdentifierData, mappedIdentifierData, "entities");
            this.argumentTypeMappings = this.loadFullMappings(data, unmappedIdentifierData, mappedIdentifierData, "argumenttypes");
            final ListTag unmappedParticles = unmappedIdentifierData.get("particles");
            final ListTag mappedParticles = mappedIdentifierData.get("particles");
            if (unmappedParticles != null && mappedParticles != null) {
                Mappings particleMappings = this.loadMappings(data, "particles");
                if (particleMappings == null) {
                    particleMappings = new IdentityMappings(unmappedParticles.size(), mappedParticles.size());
                }
                final List<String> identifiers = unmappedParticles.getValue().stream().map(t -> t.getValue()).collect((Collector<? super Object, ?, List<String>>)Collectors.toList());
                final List<String> mappedIdentifiers = mappedParticles.getValue().stream().map(t -> t.getValue()).collect((Collector<? super Object, ?, List<String>>)Collectors.toList());
                this.particleMappings = new ParticleMappings(identifiers, mappedIdentifiers, particleMappings);
            }
        }
        final CompoundTag tagsTag = data.get("tags");
        if (tagsTag != null) {
            this.tags = new EnumMap<RegistryType, List<TagData>>(RegistryType.class);
            this.loadTags(RegistryType.ITEM, tagsTag);
            this.loadTags(RegistryType.BLOCK, tagsTag);
        }
        this.loadExtras(data);
    }
    
    protected CompoundTag readNBTFile(final String name) {
        return MappingDataLoader.loadNBT(name);
    }
    
    protected Mappings loadMappings(final CompoundTag data, final String key) {
        return MappingDataLoader.loadMappings(data, key);
    }
    
    protected FullMappings loadFullMappings(final CompoundTag data, final CompoundTag unmappedIdentifiers, final CompoundTag mappedIdentifiers, final String key) {
        return MappingDataLoader.loadFullMappings(data, unmappedIdentifiers, mappedIdentifiers, key);
    }
    
    protected BiMappings loadBiMappings(final CompoundTag data, final String key) {
        final Mappings mappings = this.loadMappings(data, key);
        return (mappings != null) ? BiMappings.of(mappings) : null;
    }
    
    private void loadTags(final RegistryType type, final CompoundTag data) {
        final CompoundTag tag = data.get(type.resourceLocation());
        if (tag == null) {
            return;
        }
        final List<TagData> tagsList = new ArrayList<TagData>(this.tags.size());
        for (final Map.Entry<String, Tag> entry : tag.entrySet()) {
            final IntArrayTag entries = entry.getValue();
            tagsList.add(new TagData(entry.getKey(), entries.getValue()));
        }
        this.tags.put(type, tagsList);
    }
    
    @Override
    public int getNewBlockStateId(final int id) {
        return this.checkValidity(id, this.blockStateMappings.getNewId(id), "blockstate");
    }
    
    @Override
    public int getNewBlockId(final int id) {
        return this.checkValidity(id, this.blockMappings.getNewId(id), "block");
    }
    
    @Override
    public int getNewItemId(final int id) {
        return this.checkValidity(id, this.itemMappings.getNewId(id), "item");
    }
    
    @Override
    public int getOldItemId(final int id) {
        return this.itemMappings.inverse().getNewIdOrDefault(id, 1);
    }
    
    @Override
    public int getNewParticleId(final int id) {
        return this.checkValidity(id, this.particleMappings.getNewId(id), "particles");
    }
    
    @Override
    public List<TagData> getTags(final RegistryType type) {
        return (this.tags != null) ? this.tags.get(type) : null;
    }
    
    @Override
    public BiMappings getItemMappings() {
        return this.itemMappings;
    }
    
    @Override
    public ParticleMappings getParticleMappings() {
        return this.particleMappings;
    }
    
    @Override
    public Mappings getBlockMappings() {
        return this.blockMappings;
    }
    
    @Override
    public Mappings getBlockEntityMappings() {
        return this.blockEntityMappings;
    }
    
    @Override
    public Mappings getBlockStateMappings() {
        return this.blockStateMappings;
    }
    
    @Override
    public Mappings getSoundMappings() {
        return this.soundMappings;
    }
    
    @Override
    public Mappings getStatisticsMappings() {
        return this.statisticsMappings;
    }
    
    @Override
    public Mappings getMenuMappings() {
        return this.menuMappings;
    }
    
    @Override
    public Mappings getEnchantmentMappings() {
        return this.enchantmentMappings;
    }
    
    @Override
    public FullMappings getEntityMappings() {
        return this.entityMappings;
    }
    
    @Override
    public FullMappings getArgumentTypeMappings() {
        return this.argumentTypeMappings;
    }
    
    @Override
    public Mappings getPaintingMappings() {
        return this.paintingMappings;
    }
    
    protected Logger getLogger() {
        return Via.getPlatform().getLogger();
    }
    
    protected int checkValidity(final int id, final int mappedId, final String type) {
        if (mappedId == -1) {
            if (!Via.getConfig().isSuppressConversionWarnings()) {
                this.getLogger().warning(String.format("Missing %s %s for %s %s %d", this.mappedVersion, type, this.unmappedVersion, type, id));
            }
            return 0;
        }
        return mappedId;
    }
    
    protected void loadExtras(final CompoundTag data) {
    }
}
