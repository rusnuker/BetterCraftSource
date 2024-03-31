// 
// Decompiled by Procyon v0.6.0
// 

package com.mcf.davidee.nbtedit.nbt;

import net.minecraft.nbt.NBTBase;

public class NamedNBT
{
    protected String name;
    protected NBTBase nbt;
    
    public NamedNBT(final NBTBase nbt) {
        this("", nbt);
    }
    
    public NamedNBT(final String name, final NBTBase nbt) {
        this.name = name;
        this.nbt = nbt;
    }
    
    public String getName() {
        return this.name;
    }
    
    public void setName(final String name) {
        this.name = name;
    }
    
    public NBTBase getNBT() {
        return this.nbt;
    }
    
    public void setNBT(final NBTBase nbt) {
        this.nbt = nbt;
    }
    
    public NamedNBT copy() {
        return new NamedNBT(this.name, this.nbt.copy());
    }
}
