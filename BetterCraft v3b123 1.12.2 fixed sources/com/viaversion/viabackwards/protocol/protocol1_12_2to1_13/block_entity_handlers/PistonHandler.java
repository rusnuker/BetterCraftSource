// 
// Decompiled by Procyon v0.6.0
// 

package com.viaversion.viabackwards.protocol.protocol1_12_2to1_13.block_entity_handlers;

import com.viaversion.viaversion.libs.opennbt.tag.builtin.Tag;
import java.util.StringJoiner;
import com.viaversion.viaversion.libs.opennbt.tag.builtin.IntTag;
import com.viaversion.viaversion.libs.opennbt.tag.builtin.CompoundTag;
import com.viaversion.viaversion.api.connection.UserConnection;
import com.viaversion.viabackwards.protocol.protocol1_12_2to1_13.Protocol1_12_2To1_13;
import java.util.Iterator;
import com.viaversion.viaversion.libs.opennbt.tag.builtin.StringTag;
import com.viaversion.viaversion.api.data.MappingDataLoader;
import com.viaversion.viaversion.libs.opennbt.tag.builtin.ListTag;
import com.viaversion.viaversion.protocols.protocol1_13to1_12_2.blockconnections.ConnectionData;
import com.viaversion.viaversion.api.Via;
import java.util.HashMap;
import java.util.Map;
import com.viaversion.viabackwards.protocol.protocol1_12_2to1_13.providers.BackwardsBlockEntityProvider;

public class PistonHandler implements BackwardsBlockEntityProvider.BackwardsBlockEntityHandler
{
    private final Map<String, Integer> pistonIds;
    
    public PistonHandler() {
        this.pistonIds = new HashMap<String, Integer>();
        if (Via.getConfig().isServersideBlockConnections()) {
            final Map<String, Integer> keyToId = ConnectionData.getKeyToId();
            for (final Map.Entry<String, Integer> entry : keyToId.entrySet()) {
                if (!entry.getKey().contains("piston")) {
                    continue;
                }
                this.addEntries(entry.getKey(), entry.getValue());
            }
        }
        else {
            final ListTag blockStates = MappingDataLoader.loadNBT("blockstates-1.13.nbt").get("blockstates");
            for (int id = 0; id < blockStates.size(); ++id) {
                final StringTag state = blockStates.get(id);
                final String key = state.getValue();
                if (key.contains("piston")) {
                    this.addEntries(key, id);
                }
            }
        }
    }
    
    private void addEntries(String data, int id) {
        id = Protocol1_12_2To1_13.MAPPINGS.getNewBlockStateId(id);
        this.pistonIds.put(data, id);
        final String substring = data.substring(10);
        if (!substring.startsWith("piston") && !substring.startsWith("sticky_piston")) {
            return;
        }
        final String[] split = data.substring(0, data.length() - 1).split("\\[");
        final String[] properties = split[1].split(",");
        data = split[0] + "[" + properties[1] + "," + properties[0] + "]";
        this.pistonIds.put(data, id);
    }
    
    @Override
    public CompoundTag transform(final UserConnection user, final int blockId, final CompoundTag tag) {
        final CompoundTag blockState = tag.get("blockState");
        if (blockState == null) {
            return tag;
        }
        final String dataFromTag = this.getDataFromTag(blockState);
        if (dataFromTag == null) {
            return tag;
        }
        final Integer id = this.pistonIds.get(dataFromTag);
        if (id == null) {
            return tag;
        }
        tag.put("blockId", new IntTag(id >> 4));
        tag.put("blockData", new IntTag(id & 0xF));
        return tag;
    }
    
    private String getDataFromTag(final CompoundTag tag) {
        final StringTag name = tag.get("Name");
        if (name == null) {
            return null;
        }
        final CompoundTag properties = tag.get("Properties");
        if (properties == null) {
            return name.getValue();
        }
        final StringJoiner joiner = new StringJoiner(",", name.getValue() + "[", "]");
        for (final Map.Entry<String, Tag> entry : properties) {
            if (!(entry.getValue() instanceof StringTag)) {
                continue;
            }
            joiner.add(entry.getKey() + "=" + entry.getValue().getValue());
        }
        return joiner.toString();
    }
}
