// 
// Decompiled by Procyon v0.6.0
// 

package me.amkgre.bettercraft.client.mods.teamspeak.event;

import me.amkgre.bettercraft.client.mods.teamspeak.api.ServerTab;
import me.amkgre.bettercraft.client.mods.teamspeak.api.Client;
import me.amkgre.bettercraft.client.mods.teamspeak.api.TalkStatus;

public class TalkStatusChangeEvent extends Event
{
    public final TalkStatus status;
    public final boolean receivedWhisper;
    public final Client client;
    
    public TalkStatusChangeEvent(final ServerTab tab, final TalkStatus status, final boolean receivedWhisper, final Client client) {
        super(tab);
        this.status = status;
        this.receivedWhisper = receivedWhisper;
        this.client = client;
    }
}
