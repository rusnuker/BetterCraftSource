// 
// Decompiled by Procyon v0.6.0
// 

package net.labymod.addons.resourcepacks24.loader.model;

import java.beans.ConstructorProperties;

public class MCPack
{
    public int pack_format;
    public String description;
    
    @ConstructorProperties({ "pack_format", "description" })
    public MCPack(final int pack_format, final String description) {
        this.pack_format = pack_format;
        this.description = description;
    }
}
