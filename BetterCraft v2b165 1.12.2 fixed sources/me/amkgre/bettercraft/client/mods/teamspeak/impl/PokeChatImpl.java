// 
// Decompiled by Procyon v0.6.0
// 

package me.amkgre.bettercraft.client.mods.teamspeak.impl;

import me.amkgre.bettercraft.client.mods.teamspeak.api.MessageTargetMode;
import me.amkgre.bettercraft.client.mods.teamspeak.net.TeamSpeakNetworkManager;

public class PokeChatImpl extends ChatImpl
{
    public PokeChatImpl(final TeamSpeakNetworkManager networkManager) {
        super(networkManager, MessageTargetMode.POKE);
    }
    
    public void reset() {
        this.messages.clear();
    }
    
    @Override
    public void sendMessage(final String message) {
    }
}
