// 
// Decompiled by Procyon v0.6.0
// 

package com.viaversion.viaversion.api.minecraft;

public class VillagerData
{
    private final int type;
    private final int profession;
    private final int level;
    
    public VillagerData(final int type, final int profession, final int level) {
        this.type = type;
        this.profession = profession;
        this.level = level;
    }
    
    public int getType() {
        return this.type;
    }
    
    public int getProfession() {
        return this.profession;
    }
    
    public int getLevel() {
        return this.level;
    }
}
