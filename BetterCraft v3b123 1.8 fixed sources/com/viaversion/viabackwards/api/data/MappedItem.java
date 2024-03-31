// 
// Decompiled by Procyon v0.6.0
// 

package com.viaversion.viabackwards.api.data;

import com.viaversion.viaversion.protocols.protocol1_13to1_12_2.ChatRewriter;

public class MappedItem
{
    private final int id;
    private final String jsonName;
    private final Integer customModelData;
    
    public MappedItem(final int id, final String name) {
        this(id, name, null);
    }
    
    public MappedItem(final int id, final String name, final Integer customModelData) {
        this.id = id;
        this.jsonName = ChatRewriter.legacyTextToJsonString("§f" + name, true);
        this.customModelData = customModelData;
    }
    
    public int getId() {
        return this.id;
    }
    
    public String getJsonName() {
        return this.jsonName;
    }
    
    public Integer customModelData() {
        return this.customModelData;
    }
}
