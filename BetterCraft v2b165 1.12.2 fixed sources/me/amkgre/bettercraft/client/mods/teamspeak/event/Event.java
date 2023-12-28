// 
// Decompiled by Procyon v0.6.0
// 

package me.amkgre.bettercraft.client.mods.teamspeak.event;

import me.amkgre.bettercraft.client.mods.teamspeak.api.ServerTab;

public abstract class Event
{
    public final ServerTab tab;
    
    protected Event(final ServerTab tab) {
        this.tab = tab;
    }
}
