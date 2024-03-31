// 
// Decompiled by Procyon v0.6.0
// 

package me.amkgre.bettercraft.client.mods.teamspeak.event;

import me.amkgre.bettercraft.client.mods.teamspeak.api.ServerTab;
import me.amkgre.bettercraft.client.mods.teamspeak.api.Client;

public class ClientPokeEvent extends Event
{
    public final Client invoker;
    public final String message;
    
    protected ClientPokeEvent(final ServerTab tab, final Client invoker, final String message) {
        super(tab);
        this.invoker = invoker;
        this.message = message;
    }
}
