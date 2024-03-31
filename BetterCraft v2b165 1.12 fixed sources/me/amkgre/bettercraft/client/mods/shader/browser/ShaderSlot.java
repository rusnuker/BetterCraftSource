// 
// Decompiled by Procyon v0.6.0
// 

package me.amkgre.bettercraft.client.mods.shader.browser;

public class ShaderSlot<V>
{
    private final V object;
    private boolean isSelected;
    
    public ShaderSlot(final V object, final boolean selected) {
        this.object = object;
        this.isSelected = selected;
    }
    
    public boolean isSelected() {
        return this.isSelected;
    }
    
    public void setSelected(final boolean value) {
        this.isSelected = value;
    }
    
    public V getObject() {
        return this.object;
    }
}
