// 
// Decompiled by Procyon v0.6.0
// 

package me.amkgre.bettercraft.client.mods.teamspeak.api;

import java.util.List;

public interface Chat
{
    MessageTargetMode getType();
    
    List<? extends Message> getMessages();
    
    void sendMessage(final String p0);
}
