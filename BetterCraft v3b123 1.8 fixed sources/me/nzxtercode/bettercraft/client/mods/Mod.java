// 
// Decompiled by Procyon v0.6.0
// 

package me.nzxtercode.bettercraft.client.mods;

import net.lenni0451.eventapi.manager.EventManager;
import me.nzxtercode.bettercraft.client.BetterCraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.Minecraft;

public abstract class Mod
{
    protected final Minecraft mc;
    protected final FontRenderer font;
    protected final BetterCraft client;
    private boolean isEnabled;
    private String name;
    private ModType type;
    
    public Mod() {
        this.mc = Minecraft.getMinecraft();
        this.isEnabled = true;
        this.font = this.mc.fontRendererObj;
        this.client = BetterCraft.getInstance();
        this.setEnabled(this.isEnabled);
    }
    
    public void setEnabled(final boolean isEnabled) {
        this.isEnabled = isEnabled;
        if (isEnabled) {
            EventManager.register((Object)this);
        }
        else {
            EventManager.unregister(this);
        }
    }
    
    public void setEnabledIngame(final boolean isEnabled) {
        this.isEnabled = isEnabled;
    }
    
    public boolean isEnabled() {
        return this.isEnabled;
    }
    
    public String getName() {
        return this.name;
    }
    
    public void setName(final String name) {
        this.name = name;
    }
    
    public ModType getType() {
        return this.type;
    }
    
    public void setType(final ModType type) {
        this.type = type;
    }
}
