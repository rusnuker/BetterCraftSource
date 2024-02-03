// 
// Decompiled by Procyon v0.6.0
// 

package net.labymod.api.events;

import net.minecraft.network.PacketBuffer;

public interface PluginMessageEvent
{
    void receiveMessage(final String p0, final PacketBuffer p1);
}
