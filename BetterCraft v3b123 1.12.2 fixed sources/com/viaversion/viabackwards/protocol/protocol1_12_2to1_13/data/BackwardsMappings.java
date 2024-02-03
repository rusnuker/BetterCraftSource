// 
// Decompiled by Procyon v0.6.0
// 

package com.viaversion.viabackwards.protocol.protocol1_12_2to1_13.data;

import java.util.Iterator;
import com.viaversion.viaversion.protocols.protocol1_13to1_12_2.data.StatisticMappings;
import com.viaversion.viaversion.libs.opennbt.tag.builtin.CompoundTag;
import java.util.HashMap;
import com.viaversion.viaversion.libs.fastutil.ints.Int2ObjectOpenHashMap;
import com.viaversion.viaversion.api.protocol.Protocol;
import com.viaversion.viaversion.protocols.protocol1_13to1_12_2.Protocol1_13To1_12_2;
import java.util.Map;
import com.viaversion.viaversion.libs.fastutil.ints.Int2ObjectMap;

public class BackwardsMappings extends com.viaversion.viabackwards.api.data.BackwardsMappings
{
    private final Int2ObjectMap<String> statisticMappings;
    private final Map<String, String> translateMappings;
    
    public BackwardsMappings() {
        super("1.13", "1.12", Protocol1_13To1_12_2.class);
        this.statisticMappings = new Int2ObjectOpenHashMap<String>();
        this.translateMappings = new HashMap<String, String>();
    }
    
    public void loadExtras(final CompoundTag data) {
        super.loadExtras(data);
        for (final Map.Entry<String, Integer> entry : StatisticMappings.CUSTOM_STATS.entrySet()) {
            this.statisticMappings.put((int)entry.getValue(), entry.getKey());
        }
        for (final Map.Entry<String, String> entry2 : Protocol1_13To1_12_2.MAPPINGS.getTranslateMapping().entrySet()) {
            this.translateMappings.put(entry2.getValue(), entry2.getKey());
        }
    }
    
    @Override
    public int getNewBlockStateId(int id) {
        if (id >= 5635 && id <= 5650) {
            if (id < 5639) {
                id += 4;
            }
            else if (id < 5643) {
                id -= 4;
            }
            else if (id < 5647) {
                id += 4;
            }
            else {
                id -= 4;
            }
        }
        final int mappedId = super.getNewBlockStateId(id);
        switch (mappedId) {
            case 1595:
            case 1596:
            case 1597: {
                return 1584;
            }
            case 1611:
            case 1612:
            case 1613: {
                return 1600;
            }
            default: {
                return mappedId;
            }
        }
    }
    
    @Override
    protected int checkValidity(final int id, final int mappedId, final String type) {
        return mappedId;
    }
    
    public Int2ObjectMap<String> getStatisticMappings() {
        return this.statisticMappings;
    }
    
    public Map<String, String> getTranslateMappings() {
        return this.translateMappings;
    }
}
