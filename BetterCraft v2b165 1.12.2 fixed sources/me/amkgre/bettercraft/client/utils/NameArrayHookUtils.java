// 
// Decompiled by Procyon v0.6.0
// 

package me.amkgre.bettercraft.client.utils;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class NameArrayHookUtils
{
    @SerializedName("name")
    @Expose
    private String name;
    public boolean e;
    
    public NameArrayHookUtils() {
        this.e = false;
    }
    
    public final synchronized String getName() {
        return this.name;
    }
    
    public void setE(final boolean e) {
        this.e = e;
    }
    
    public final synchronized void setName(final String name) {
        this.name = name;
    }
}
