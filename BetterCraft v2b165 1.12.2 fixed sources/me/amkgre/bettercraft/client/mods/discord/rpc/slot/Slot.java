// 
// Decompiled by Procyon v0.6.0
// 

package me.amkgre.bettercraft.client.mods.discord.rpc.slot;

public class Slot<V>
{
    private final V object;
    private boolean selected;
    
    public Slot(final V object) {
        this.object = object;
        this.selected = false;
    }
    
    public V getObject() {
        return this.object;
    }
    
    public boolean isSelected() {
        return this.selected;
    }
    
    public void setSelected(final boolean value) {
        this.selected = value;
    }
}
