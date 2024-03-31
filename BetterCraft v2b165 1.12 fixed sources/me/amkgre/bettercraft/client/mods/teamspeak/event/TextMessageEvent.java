// 
// Decompiled by Procyon v0.6.0
// 

package me.amkgre.bettercraft.client.mods.teamspeak.event;

import me.amkgre.bettercraft.client.mods.teamspeak.api.ServerTab;
import me.amkgre.bettercraft.client.mods.teamspeak.api.Client;
import me.amkgre.bettercraft.client.mods.teamspeak.api.MessageTargetMode;

public class TextMessageEvent extends Event
{
    public final MessageTargetMode targetMode;
    public final String msg;
    public final int target;
    public final Client invoker;
    
    public TextMessageEvent(final ServerTab tab, final MessageTargetMode targetMode, final String msg, final int target, final Client invoker) {
        super(tab);
        this.targetMode = targetMode;
        this.msg = msg;
        this.target = target;
        this.invoker = invoker;
    }
}
