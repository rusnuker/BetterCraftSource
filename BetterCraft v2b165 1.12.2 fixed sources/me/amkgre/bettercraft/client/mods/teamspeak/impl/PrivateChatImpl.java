// 
// Decompiled by Procyon v0.6.0
// 

package me.amkgre.bettercraft.client.mods.teamspeak.impl;

import me.amkgre.bettercraft.client.mods.teamspeak.api.Client;
import me.amkgre.bettercraft.client.mods.teamspeak.util.Callback;
import me.amkgre.bettercraft.client.mods.teamspeak.request.Request;
import me.amkgre.bettercraft.client.mods.teamspeak.response.TeamSpeakCommandResponse;
import me.amkgre.bettercraft.client.mods.teamspeak.util.EmptyCallback;
import me.amkgre.bettercraft.client.mods.teamspeak.request.SendTextMessageRequest;
import me.amkgre.bettercraft.client.mods.teamspeak.api.MessageTargetMode;
import me.amkgre.bettercraft.client.mods.teamspeak.net.TeamSpeakNetworkManager;
import me.amkgre.bettercraft.client.mods.teamspeak.api.PrivateChat;

public class PrivateChatImpl extends ChatImpl implements PrivateChat
{
    private final ClientImpl client;
    
    public PrivateChatImpl(final TeamSpeakNetworkManager networkManager, final ClientImpl client) {
        super(networkManager, MessageTargetMode.CLIENT);
        this.client = client;
    }
    
    @Override
    public ClientImpl getClient() {
        return this.client;
    }
    
    @Override
    public void sendMessage(final String message) {
        this.networkManager.sendRequest(new SendTextMessageRequest(MessageTargetMode.CLIENT, this.client.getId(), message), new EmptyCallback<TeamSpeakCommandResponse>());
    }
}
