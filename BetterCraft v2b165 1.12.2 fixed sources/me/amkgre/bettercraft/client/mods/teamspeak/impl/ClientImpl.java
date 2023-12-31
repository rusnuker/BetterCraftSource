// 
// Decompiled by Procyon v0.6.0
// 

package me.amkgre.bettercraft.client.mods.teamspeak.impl;

import java.util.Locale;
import com.google.common.collect.ComparisonChain;
import me.amkgre.bettercraft.client.mods.teamspeak.request.ClientUnMuteRequest;
import me.amkgre.bettercraft.client.mods.teamspeak.request.ClientMuteRequest;
import me.amkgre.bettercraft.client.mods.teamspeak.request.BanClientRequest;
import me.amkgre.bettercraft.client.mods.teamspeak.request.ClientKickRequest;
import me.amkgre.bettercraft.client.mods.teamspeak.request.ClientPokeRequest;
import me.amkgre.bettercraft.client.mods.teamspeak.request.SetClientChannelGroupRequest;
import me.amkgre.bettercraft.client.mods.teamspeak.request.ServerGroupDelClientRequest;
import me.amkgre.bettercraft.client.mods.teamspeak.request.ServerGroupAddClientRequest;
import me.amkgre.bettercraft.client.mods.teamspeak.TeamSpeakException;
import me.amkgre.bettercraft.client.mods.teamspeak.util.EmptyCallback;
import me.amkgre.bettercraft.client.mods.teamspeak.request.ClientMoveRequest;
import me.amkgre.bettercraft.client.mods.teamspeak.request.Request;
import java.util.Map;
import me.amkgre.bettercraft.client.mods.teamspeak.response.TeamSpeakCommandResponse;
import me.amkgre.bettercraft.client.mods.teamspeak.util.Callback;
import me.amkgre.bettercraft.client.mods.teamspeak.request.HashPasswordRequest;
import com.google.common.base.Strings;
import me.amkgre.bettercraft.client.mods.teamspeak.api.Channel;
import me.amkgre.bettercraft.client.mods.teamspeak.util.ImageManager;
import java.awt.image.BufferedImage;
import java.util.Iterator;
import me.amkgre.bettercraft.client.mods.teamspeak.api.Group;
import java.util.Collections;
import me.amkgre.bettercraft.client.mods.teamspeak.util.PropertyMap;
import java.util.ArrayList;
import java.util.List;
import me.amkgre.bettercraft.client.mods.teamspeak.api.ClientType;
import me.amkgre.bettercraft.client.mods.teamspeak.net.TeamSpeakNetworkManager;
import me.amkgre.bettercraft.client.mods.teamspeak.api.Client;

public class ClientImpl implements Client
{
    protected final TeamSpeakNetworkManager networkManager;
    private final int id;
    private final int databaseId;
    private final String uniqueId;
    private String nickName;
    private ClientType type;
    private ChannelImpl channel;
    private boolean talking;
    private boolean whispering;
    private boolean inputMuted;
    private boolean outputMuted;
    private boolean inputHardware;
    private boolean outputHardware;
    private int talkPower;
    private boolean talker;
    private boolean prioritySpeaker;
    private boolean recording;
    private boolean channelCommander;
    private boolean muted;
    private boolean away;
    private String awayMessage;
    private final List<GroupImpl> serverGroups;
    private GroupImpl channelGroup;
    private int iconId;
    
    public ClientImpl(final TeamSpeakNetworkManager networkManager, final int id, final int databaseId, final String uniqueId, final String nickName, final ChannelImpl channel) {
        this.serverGroups = new ArrayList<GroupImpl>();
        this.networkManager = networkManager;
        this.id = id;
        this.databaseId = databaseId;
        this.uniqueId = uniqueId;
        this.nickName = nickName;
        this.channel = channel;
    }
    
    public void updateProperties(final PropertyMap propertyMap) {
        this.nickName = propertyMap.get("client_nickname", this.nickName);
        this.type = ClientType.byId(propertyMap.getInt("client_type", (this.type == null) ? 0 : this.type.getId()));
        this.talking = propertyMap.getBool("client_flag_talking", this.talking);
        this.inputMuted = propertyMap.getBool("client_input_muted", this.inputMuted);
        this.outputMuted = propertyMap.getBool("client_output_muted", this.outputMuted);
        this.inputHardware = propertyMap.getBool("client_input_hardware", this.inputHardware);
        this.outputHardware = propertyMap.getBool("client_output_hardware", this.outputHardware);
        this.talkPower = propertyMap.getInt("client_talk_power", this.talkPower);
        this.talker = propertyMap.getBool("client_is_talker", this.talker);
        this.prioritySpeaker = propertyMap.getBool("client_is_priority_speaker", this.prioritySpeaker);
        this.recording = propertyMap.getBool("client_is_recording", this.recording);
        this.channelCommander = propertyMap.getBool("client_is_channel_commander", this.channelCommander);
        this.muted = propertyMap.getBool("client_is_muted", this.muted);
        this.away = propertyMap.getBool("client_away", this.away);
        this.awayMessage = propertyMap.get("client_away_message", this.awayMessage);
        if (propertyMap.contains("client_servergroups")) {
            final String groups = propertyMap.get("client_servergroups");
            this.serverGroups.clear();
            String[] split;
            for (int length = (split = groups.split(",")).length, i = 0; i < length; ++i) {
                final String group = split[i];
                try {
                    final int groupId = Integer.parseInt(group);
                    final GroupImpl serverGroup = this.channel.getServerTab().getServerGroup(groupId);
                    if (serverGroup != null) {
                        this.serverGroups.add(serverGroup);
                    }
                }
                catch (final NumberFormatException ex) {}
            }
            Collections.sort(this.serverGroups);
        }
        if (propertyMap.contains("client_channel_group_id")) {
            final int groupId2 = propertyMap.getInt("client_channel_group_id");
            this.channelGroup = this.channel.getServerTab().getChannelGroup(groupId2);
        }
        this.iconId = propertyMap.getInt("client_icon_id", this.iconId);
    }
    
    @Override
    public int getId() {
        return this.id;
    }
    
    @Override
    public int getDatabaseId() {
        return this.databaseId;
    }
    
    @Override
    public String getUniqueId() {
        return this.uniqueId;
    }
    
    @Override
    public String getName() {
        return this.nickName;
    }
    
    @Override
    public ClientType getType() {
        return this.type;
    }
    
    @Override
    public String getDisplayName() {
        final StringBuilder stringBuilder = new StringBuilder();
        for (final Group group : this.serverGroups) {
            if (!group.isShowPrefix()) {
                continue;
            }
            stringBuilder.append("[").append(group.getName()).append("] ");
        }
        if (this.channelGroup != null && this.channelGroup.isShowPrefix()) {
            stringBuilder.append("[").append(this.channelGroup.getName()).append("] ");
        }
        stringBuilder.append(this.getName());
        return stringBuilder.toString();
    }
    
    @Override
    public ChannelImpl getChannel() {
        return this.channel;
    }
    
    public void setChannel(final ChannelImpl channel) {
        this.channel = channel;
    }
    
    @Override
    public BufferedImage getIcon() {
        return (this.channel.getServerInfo() == null || this.channel.getServerInfo().getUniqueId() == null) ? null : ImageManager.resolveIcon(this.channel.getServerInfo().getUniqueId(), this.iconId);
    }
    
    @Override
    public int getIconId() {
        return this.iconId;
    }
    
    @Override
    public BufferedImage getAvatar() {
        return (this.channel.getServerInfo() == null || this.channel.getServerInfo().getUniqueId() == null) ? null : ImageManager.resolveAvatar(this.channel.getServerInfo().getUniqueId(), this.uniqueId);
    }
    
    @Override
    public boolean isTalking() {
        return this.talking;
    }
    
    public void setTalking(final boolean talking) {
        this.talking = talking;
    }
    
    @Override
    public boolean isWhispering() {
        return this.whispering;
    }
    
    public void setWhispering(final boolean whispering) {
        this.whispering = whispering;
    }
    
    @Override
    public boolean isInputMuted() {
        return this.inputMuted;
    }
    
    @Override
    public boolean isOutputMuted() {
        return this.outputMuted;
    }
    
    @Override
    public boolean hasInputHardware() {
        return this.inputHardware;
    }
    
    @Override
    public boolean hasOutputHardware() {
        return this.outputHardware;
    }
    
    @Override
    public int getTalkPower() {
        return this.talkPower;
    }
    
    @Override
    public boolean isTalker() {
        return this.talker;
    }
    
    @Override
    public boolean isPrioritySpeaker() {
        return this.prioritySpeaker;
    }
    
    @Override
    public boolean isRecording() {
        return this.recording;
    }
    
    @Override
    public boolean isChannelCommander() {
        return this.channelCommander;
    }
    
    @Override
    public boolean isMuted() {
        return this.muted;
    }
    
    @Override
    public boolean isAway() {
        return this.away;
    }
    
    @Override
    public String getAwayMessage() {
        return this.awayMessage;
    }
    
    @Override
    public List<GroupImpl> getServerGroups() {
        return this.serverGroups;
    }
    
    @Override
    public GroupImpl getChannelGroup() {
        return this.channelGroup;
    }
    
    public void setChannelGroup(final GroupImpl channelGroup) {
        this.channelGroup = channelGroup;
    }
    
    @Override
    public void joinChannel(final Channel channel) {
        this.joinChannel0(channel, null);
    }
    
    @Override
    public void joinChannel(final Channel channel, final String password) {
        if (!Strings.isNullOrEmpty(password)) {
            this.networkManager.sendRequest(new HashPasswordRequest(password), new Callback<TeamSpeakCommandResponse>() {
                @Override
                public void onDone(final TeamSpeakCommandResponse response) {
                    final PropertyMap propertyMap = new PropertyMap(response.getParsedResponse());
                    final String hash = propertyMap.get("passwordhash");
                    ClientImpl.this.joinChannel0(channel, hash);
                }
            });
        }
        else {
            this.joinChannel(channel);
        }
    }
    
    private void joinChannel0(final Channel channel, final String hashedPassword) {
        this.networkManager.sendRequest(new ClientMoveRequest(channel.getId(), this.getId(), hashedPassword), new EmptyCallback<TeamSpeakCommandResponse>());
    }
    
    @Override
    public void joinChannel(final Channel channel, final Callback<Integer> errorCallback) {
        this.joinChannel0(channel, null, errorCallback);
    }
    
    @Override
    public void joinChannel(final Channel channel, final String password, final Callback<Integer> errorCallback) {
        if (!Strings.isNullOrEmpty(password)) {
            this.networkManager.sendRequest(new HashPasswordRequest(password), new Callback<TeamSpeakCommandResponse>() {
                @Override
                public void onDone(final TeamSpeakCommandResponse response) {
                    final PropertyMap propertyMap = new PropertyMap(response.getParsedResponse());
                    final String hash = propertyMap.get("passwordhash");
                    ClientImpl.this.joinChannel0(channel, hash, errorCallback);
                }
            });
        }
        else {
            this.joinChannel(channel, errorCallback);
        }
    }
    
    private void joinChannel0(final Channel channel, final String hashedPassword, final Callback<Integer> errorCallback) {
        this.networkManager.sendRequest(new ClientMoveRequest(channel.getId(), this.getId(), hashedPassword), new Callback<TeamSpeakCommandResponse>() {
            @Override
            public void onDone(final TeamSpeakCommandResponse response) {
            }
            
            @Override
            public void exceptionCaught(final TeamSpeakException exception) {
                if (exception.errorId != 0) {
                    errorCallback.onDone(exception.errorId);
                }
            }
        });
    }
    
    @Override
    public void addToServerGroup(final Group group) {
        this.networkManager.sendRequest(new ServerGroupAddClientRequest(group.getId(), this.databaseId), new EmptyCallback<TeamSpeakCommandResponse>());
    }
    
    @Override
    public void removeFromServerGroup(final Group group) {
        this.networkManager.sendRequest(new ServerGroupDelClientRequest(group.getId(), this.databaseId), new EmptyCallback<TeamSpeakCommandResponse>());
    }
    
    @Override
    public void setChannelGroup(final Channel channel, final Group group) {
        this.networkManager.sendRequest(new SetClientChannelGroupRequest(group.getId(), channel.getId(), this.databaseId), new EmptyCallback<TeamSpeakCommandResponse>());
    }
    
    @Override
    public void poke(final String message) {
        this.networkManager.sendRequest(new ClientPokeRequest(this.getId(), message), new EmptyCallback<TeamSpeakCommandResponse>());
    }
    
    @Override
    public void kickFromChannel(final String reason) {
        this.networkManager.sendRequest(new ClientKickRequest(4, reason, this.getId()), new EmptyCallback<TeamSpeakCommandResponse>());
    }
    
    @Override
    public void kickFromServer(final String reason) {
        this.networkManager.sendRequest(new ClientKickRequest(5, reason, this.getId()), new EmptyCallback<TeamSpeakCommandResponse>());
    }
    
    @Override
    public void banFromServer(final String reason, final int time) {
        this.networkManager.sendRequest(new BanClientRequest(this.getId(), reason, time), new EmptyCallback<TeamSpeakCommandResponse>());
    }
    
    @Override
    public void mute() {
        this.networkManager.sendRequest(new ClientMuteRequest(this.getId()), new EmptyCallback<TeamSpeakCommandResponse>());
    }
    
    @Override
    public void unMute() {
        this.networkManager.sendRequest(new ClientUnMuteRequest(this.getId()), new EmptyCallback<TeamSpeakCommandResponse>());
    }
    
    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || this.getClass() != o.getClass()) {
            return false;
        }
        final ClientImpl client = (ClientImpl)o;
        return this.id == client.id;
    }
    
    @Override
    public int hashCode() {
        return this.id;
    }
    
    @Override
    public int compareTo(final Client o) {
        return ComparisonChain.start().compare(o.getChannelGroup(), this.getChannelGroup()).compareTrueFirst(!this.getServerGroups().isEmpty(), !o.getServerGroups().isEmpty()).compare(this.getServerGroups().get(0), (Comparable<?>)o.getServerGroups().get(0)).compare(this.getName().toLowerCase(Locale.ROOT), o.getName().toLowerCase(Locale.ROOT)).result();
    }
    
    @Override
    public String toString() {
        return "Client{uniqueId='" + this.uniqueId + '\'' + ", nickName='" + this.nickName + '\'' + '}';
    }
}
