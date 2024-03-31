// 
// Decompiled by Procyon v0.6.0
// 

package me.amkgre.bettercraft.client.mods.teamspeak.impl;

import me.amkgre.bettercraft.client.mods.teamspeak.util.Callback;
import me.amkgre.bettercraft.client.mods.teamspeak.request.Request;
import me.amkgre.bettercraft.client.mods.teamspeak.response.TeamSpeakCommandResponse;
import me.amkgre.bettercraft.client.mods.teamspeak.util.EmptyCallback;
import me.amkgre.bettercraft.client.mods.teamspeak.request.ClientUpdateRequest;
import me.amkgre.bettercraft.client.mods.teamspeak.net.TeamSpeakNetworkManager;
import me.amkgre.bettercraft.client.mods.teamspeak.api.OwnClient;

public class OwnClientImpl extends ClientImpl implements OwnClient
{
    public OwnClientImpl(final TeamSpeakNetworkManager networkManager, final int id, final int databaseId, final String uniqueId, final String nickName, final ChannelImpl channel) {
        super(networkManager, id, databaseId, uniqueId, nickName, channel);
    }
    
    @Override
    public void setNickName(final String nickname) {
        this.networkManager.sendRequest(new ClientUpdateRequest(ClientUpdateRequest.Ident.NICKNAME, nickname), new EmptyCallback<TeamSpeakCommandResponse>());
    }
    
    @Override
    public void setAway(final boolean away) {
        this.networkManager.sendRequest(new ClientUpdateRequest(ClientUpdateRequest.Ident.AWAY, away), new EmptyCallback<TeamSpeakCommandResponse>());
    }
    
    @Override
    public void setAwayMessage(final String awayMessage) {
        this.networkManager.sendRequest(new ClientUpdateRequest(ClientUpdateRequest.Ident.AWAY_MESSAGE, awayMessage), new EmptyCallback<TeamSpeakCommandResponse>());
    }
    
    @Override
    public void setInputMuted(final boolean muted) {
        this.networkManager.sendRequest(new ClientUpdateRequest(ClientUpdateRequest.Ident.INPUT_MUTED, muted), new EmptyCallback<TeamSpeakCommandResponse>());
    }
    
    @Override
    public void setOutputMuted(final boolean muted) {
        this.networkManager.sendRequest(new ClientUpdateRequest(ClientUpdateRequest.Ident.OUTPUT_MUTED, muted), new EmptyCallback<TeamSpeakCommandResponse>());
    }
    
    @Override
    public void setInputDeactivated(final boolean deactivated) {
        this.networkManager.sendRequest(new ClientUpdateRequest(ClientUpdateRequest.Ident.INPUT_DEACTIVATED, deactivated), new EmptyCallback<TeamSpeakCommandResponse>());
    }
    
    @Override
    public void poke(final String message) {
        throw new UnsupportedOperationException("Cannot poke yourself!");
    }
    
    @Override
    public void kickFromChannel(final String reason) {
        throw new UnsupportedOperationException("Cannot kick yourself!");
    }
    
    @Override
    public void kickFromServer(final String reason) {
        throw new UnsupportedOperationException("Cannot kick yourself!");
    }
    
    @Override
    public void banFromServer(final String reason, final int time) {
        throw new UnsupportedOperationException("Cannot ban yourself!");
    }
    
    @Override
    public void mute() {
        throw new UnsupportedOperationException("Cannot mute yourself!");
    }
    
    @Override
    public void unMute() {
        throw new UnsupportedOperationException("Cannot un-mute yourself!");
    }
}
