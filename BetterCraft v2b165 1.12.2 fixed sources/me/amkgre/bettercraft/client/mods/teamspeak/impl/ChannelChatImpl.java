// 
// Decompiled by Procyon v0.6.0
// 

package me.amkgre.bettercraft.client.mods.teamspeak.impl;

import me.amkgre.bettercraft.client.mods.teamspeak.util.Callback;
import me.amkgre.bettercraft.client.mods.teamspeak.request.Request;
import me.amkgre.bettercraft.client.mods.teamspeak.response.TeamSpeakCommandResponse;
import me.amkgre.bettercraft.client.mods.teamspeak.util.EmptyCallback;
import me.amkgre.bettercraft.client.mods.teamspeak.request.SendTextMessageRequest;
import me.amkgre.bettercraft.client.mods.teamspeak.api.MessageTargetMode;
import me.amkgre.bettercraft.client.mods.teamspeak.net.TeamSpeakNetworkManager;

public class ChannelChatImpl extends ChatImpl
{
    public ChannelChatImpl(final TeamSpeakNetworkManager networkManager) {
        super(networkManager, MessageTargetMode.CHANNEL);
    }
    
    @Override
    public void sendMessage(final String message) {
        this.networkManager.sendRequest(new SendTextMessageRequest(MessageTargetMode.CHANNEL, message), new EmptyCallback<TeamSpeakCommandResponse>());
    }
}
