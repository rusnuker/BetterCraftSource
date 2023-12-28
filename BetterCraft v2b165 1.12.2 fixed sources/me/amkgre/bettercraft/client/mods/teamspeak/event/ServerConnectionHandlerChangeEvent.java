// 
// Decompiled by Procyon v0.6.0
// 

package me.amkgre.bettercraft.client.mods.teamspeak.event;

import me.amkgre.bettercraft.client.mods.teamspeak.api.ServerTab;

public class ServerConnectionHandlerChangeEvent extends Event
{
    public final ServerTab previousTab;
    
    public ServerConnectionHandlerChangeEvent(final ServerTab tab, final ServerTab previousTab) {
        super(tab);
        this.previousTab = previousTab;
    }
}
