// 
// Decompiled by Procyon v0.6.0
// 

package me.amkgre.bettercraft.client.modules;

import net.minecraft.client.Minecraft;

public class Module
{
    public String name;
    public String displayname;
    private Type type;
    private boolean toggled;
    private int keyBind;
    public static boolean colormode;
    public boolean visible;
    public Minecraft mc;
    
    static {
        Module.colormode = false;
    }
    
    public Module(final String name, final String displayname, final int keyBind, final Type type) {
        this.mc = Minecraft.getMinecraft();
        this.name = name;
        this.displayname = displayname;
        this.type = type;
        this.keyBind = keyBind;
        this.visible = true;
    }
    
    public String getName() {
        return this.name;
    }
    
    public void setName(final String name) {
        this.name = name;
    }
    
    public String getDisplayname() {
        return this.displayname;
    }
    
    public void setDisplayname(final String displayname) {
        this.displayname = displayname;
    }
    
    public Type gettype() {
        return this.type;
    }
    
    public void settype(final Type type) {
        this.type = type;
    }
    
    public boolean istype(final Type type) {
        return this.type == type;
    }
    
    public int getKeyBind() {
        return this.keyBind;
    }
    
    public void setKeyBind(final int keyBind) {
        this.keyBind = keyBind;
    }
    
    public boolean isEnabled() {
        return this.toggled;
    }
    
    public void toggle() {
        if (this.toggled) {
            this.toggled = false;
            this.onDisable();
        }
        else {
            this.toggled = true;
            this.onEnable();
        }
    }
    
    public void onEnable() {
    }
    
    public void onDisable() {
    }
    
    public enum Type
    {
        OTHER("OTHER", 0);
        
        private Type(final String s, final int n) {
        }
    }
}
