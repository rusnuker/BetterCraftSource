// 
// Decompiled by Procyon v0.6.0
// 

package me.amkgre.bettercraft.client.mods.teamspeak.event;

import me.amkgre.bettercraft.client.mods.teamspeak.api.ServerTab;
import me.amkgre.bettercraft.client.mods.teamspeak.api.Client;
import me.amkgre.bettercraft.client.mods.teamspeak.api.Channel;

public class ChannelCreatedEvent extends Event
{
    public final Channel channel;
    public final Client invoker;
    
    public ChannelCreatedEvent(final ServerTab tab, final Channel channel, final Client invoker) {
        super(tab);
        this.channel = channel;
        this.invoker = invoker;
    }
}
