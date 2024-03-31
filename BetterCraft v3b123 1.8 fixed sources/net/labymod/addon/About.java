// 
// Decompiled by Procyon v0.6.0
// 

package net.labymod.addon;

import java.util.UUID;

public class About
{
    public boolean deleted;
    public boolean loaded;
    public UUID uuid;
    public String name;
    
    public About(final UUID uuid, final String name) {
        this.uuid = uuid;
        this.name = name;
    }
}
