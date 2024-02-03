// 
// Decompiled by Procyon v0.6.0
// 

package net.labymod.user.sticker.data;

import java.beans.ConstructorProperties;

public class Sticker
{
    private short id;
    private String name;
    private String[] tags;
    
    public short getId() {
        return this.id;
    }
    
    public String getName() {
        return this.name;
    }
    
    public String[] getTags() {
        return this.tags;
    }
    
    @ConstructorProperties({ "id", "name", "tags" })
    public Sticker(final short id, final String name, final String[] tags) {
        this.id = id;
        this.name = name;
        this.tags = tags;
    }
}
