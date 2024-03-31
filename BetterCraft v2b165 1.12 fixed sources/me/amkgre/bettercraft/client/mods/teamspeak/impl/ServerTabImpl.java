// 
// Decompiled by Procyon v0.6.0
// 

package me.amkgre.bettercraft.client.mods.teamspeak.impl;

import me.amkgre.bettercraft.client.mods.teamspeak.api.ServerInfo;
import me.amkgre.bettercraft.client.mods.teamspeak.api.OwnClient;
import me.amkgre.bettercraft.client.mods.teamspeak.api.Group;
import me.amkgre.bettercraft.client.mods.teamspeak.api.Chat;
import me.amkgre.bettercraft.client.mods.teamspeak.api.PrivateChat;
import me.amkgre.bettercraft.client.mods.teamspeak.api.Client;
import java.util.Collections;
import me.amkgre.bettercraft.client.mods.teamspeak.request.ChannelDeleteRequest;
import me.amkgre.bettercraft.client.mods.teamspeak.request.ChannelEditRequest;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.Charsets;
import me.amkgre.bettercraft.client.mods.teamspeak.request.Request;
import me.amkgre.bettercraft.client.mods.teamspeak.util.EmptyCallback;
import me.amkgre.bettercraft.client.mods.teamspeak.request.ChannelCreateRequest;
import me.amkgre.bettercraft.client.mods.teamspeak.util.PropertyMap;
import me.amkgre.bettercraft.client.mods.teamspeak.response.TeamSpeakCommandResponse;
import me.amkgre.bettercraft.client.mods.teamspeak.util.Callback;
import me.amkgre.bettercraft.client.mods.teamspeak.request.HashPasswordRequest;
import com.google.common.base.Strings;
import org.apache.commons.lang3.Validate;
import me.amkgre.bettercraft.client.mods.teamspeak.api.ChannelCodec;
import me.amkgre.bettercraft.client.mods.teamspeak.api.Channel;
import me.amkgre.bettercraft.client.mods.teamspeak.api.ChannelLifespan;
import java.util.Collection;
import com.google.common.collect.ImmutableList;
import java.util.Iterator;
import me.amkgre.bettercraft.client.mods.teamspeak.tslogs.LogFileParseManager;
import me.amkgre.bettercraft.client.mods.teamspeak.util.EmptyRunnable;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.Map;
import java.util.List;
import me.amkgre.bettercraft.client.mods.teamspeak.net.TeamSpeakNetworkManager;
import me.amkgre.bettercraft.client.mods.teamspeak.api.ServerTab;

public class ServerTabImpl implements ServerTab
{
    final TeamSpeakNetworkManager networkManager;
    private final int id;
    private boolean loaded;
    private boolean selected;
    private ServerInfoImpl serverInfo;
    private final Object LOCK;
    private final List<ChannelImpl> channels;
    private final Map<Integer, ChannelImpl> channelLookup;
    private final Map<Integer, ClientImpl> clientLookup;
    private int selfId;
    private OwnClientImpl self;
    private GroupImpl defaultServerGroup;
    private final List<GroupImpl> serverGroups;
    private final Map<Integer, GroupImpl> serverGroupLookup;
    private GroupImpl defaultChannelGroup;
    private final List<GroupImpl> channelGroups;
    private final Map<Integer, GroupImpl> channelGroupLookup;
    private final ServerChatImpl serverChat;
    private final ChannelChatImpl channelChat;
    private final PokeChatImpl pokeChat;
    private final List<PrivateChatImpl> privateChats;
    private final Map<Integer, PrivateChatImpl> privateChatLookup;
    
    public ServerTabImpl(final TeamSpeakNetworkManager networkManager, final int id) {
        this.LOCK = new Object();
        this.channels = new ArrayList<ChannelImpl>();
        this.channelLookup = new HashMap<Integer, ChannelImpl>();
        this.clientLookup = new HashMap<Integer, ClientImpl>();
        this.serverGroups = new ArrayList<GroupImpl>();
        this.serverGroupLookup = new HashMap<Integer, GroupImpl>();
        this.channelGroups = new ArrayList<GroupImpl>();
        this.channelGroupLookup = new HashMap<Integer, GroupImpl>();
        this.privateChats = new ArrayList<PrivateChatImpl>();
        this.privateChatLookup = new HashMap<Integer, PrivateChatImpl>();
        this.networkManager = networkManager;
        this.id = id;
        this.serverChat = new ServerChatImpl(networkManager);
        this.channelChat = new ChannelChatImpl(networkManager);
        this.pokeChat = new PokeChatImpl(networkManager);
    }
    
    @Override
    public int getId() {
        return this.id;
    }
    
    @Override
    public boolean isSelected() {
        return this.selected;
    }
    
    public void setSelected(final boolean selected) {
        this.selected = selected;
    }
    
    @Override
    public void setSelected() {
        if (!this.selected) {
            this.networkManager.trySelectTab(this.id, new EmptyRunnable());
        }
    }
    
    public boolean isLoaded() {
        return this.loaded;
    }
    
    public void setLoaded(final boolean loaded) {
        this.loaded = loaded;
    }
    
    @Override
    public ServerInfoImpl getServerInfo() {
        return this.serverInfo;
    }
    
    public void setServerInfo(final ServerInfoImpl serverInfo) {
        final LogFileParseManager parseManager;
        if (this.serverInfo != null && (parseManager = this.serverInfo.getParseManager()) != null) {
            parseManager.stop();
        }
        this.serverInfo = serverInfo;
    }
    
    public void setChannels(final Map<Integer, ChannelImpl> channelMap) {
        final Object object = this.LOCK;
        synchronized (object) {
            this.channels.clear();
            for (final ChannelImpl channel : channelMap.values()) {
                if (channel == null) {
                    throw new NullPointerException();
                }
                if (channel.getParentId() == 0) {
                    this.channels.add(channel);
                }
                else {
                    final ChannelImpl parent = channelMap.get(channel.getParentId());
                    channel.setParent(parent);
                    parent.addChild(channel);
                }
            }
            this.channelLookup.clear();
            this.channelLookup.putAll(channelMap);
            ChannelImpl.sort(this.channels);
            monitorexit(object);
        }
    }
    
    public void addChannel(final ChannelImpl channel) {
        if (channel == null) {
            throw new NullPointerException();
        }
        final Object object = this.LOCK;
        synchronized (object) {
            if (channel.getParentId() == 0) {
                for (final ChannelImpl ch : this.channels) {
                    if (ch.getOrder() != channel.getOrder()) {
                        continue;
                    }
                    ch.setOrder(channel.getId());
                    break;
                }
                this.channels.add(channel);
            }
            else {
                final ChannelImpl parent = this.channelLookup.get(channel.getParentId());
                channel.setParent(parent);
                parent.addChild(channel);
            }
            this.channelLookup.put(channel.getId(), channel);
            ChannelImpl.sort(this.channels);
            monitorexit(object);
        }
    }
    
    public void removeChannel(final ChannelImpl channel) {
        final Object object = this.LOCK;
        synchronized (object) {
            if (channel.getParentId() == 0) {
                this.channels.remove(channel);
                for (final ChannelImpl other : this.channels) {
                    if (other.getOrder() != channel.getId()) {
                        continue;
                    }
                    other.setOrder(channel.getOrder());
                }
            }
            else {
                this.channelLookup.get(channel.getParentId()).removeChild(channel);
                for (final ChannelImpl child : channel.getChildren()) {
                    this.removeChannel(child);
                }
            }
            this.channelLookup.remove(channel.getId());
            monitorexit(object);
        }
    }
    
    public void addChild(final ChannelImpl parent, final ChannelImpl child) {
        final Object object = this.LOCK;
        synchronized (object) {
            parent.addChild(child);
            this.channelLookup.put(child.getId(), child);
            ChannelImpl.sort(this.channels);
            monitorexit(object);
        }
    }
    
    public void removeChild(final ChannelImpl parent, final ChannelImpl child) {
        final Object object = this.LOCK;
        synchronized (object) {
            parent.removeChild(child);
            this.channelLookup.remove(child.getId());
            ChannelImpl.sort(this.channels);
            monitorexit(object);
        }
    }
    
    @Override
    public List<ChannelImpl> getChannels() {
        final Object object = this.LOCK;
        synchronized (object) {
            final ImmutableList<Object> copy = ImmutableList.copyOf((Collection<?>)this.channels);
            monitorexit(object);
            return (List<ChannelImpl>)copy;
        }
    }
    
    @Override
    public void createChannel(final String name, final String password, final String topic, final String description, final ChannelLifespan lifespan, final boolean defaultChannel, final Channel parentChannel, final Channel orderChannel, final boolean bottomPosition, final int neededTalkPower, final ChannelCodec codec, final int codecQuality, final int maxClients) {
        Validate.notEmpty(name, "Channel name cannot be empty!", new Object[0]);
        if (!Strings.isNullOrEmpty(password)) {
            this.networkManager.sendRequest(new HashPasswordRequest(password), new Callback<TeamSpeakCommandResponse>() {
                @Override
                public void onDone(final TeamSpeakCommandResponse response) {
                    final PropertyMap propertyMap = new PropertyMap(response.getParsedResponse());
                    final String hash = propertyMap.get("passwordhash");
                    ServerTabImpl.this.networkManager.sendRequest(new ChannelCreateRequest(name, hash, topic, description, lifespan, defaultChannel, parentChannel, orderChannel, bottomPosition, neededTalkPower, codec, codecQuality, maxClients), new EmptyCallback<TeamSpeakCommandResponse>());
                }
            });
        }
        else {
            this.networkManager.sendRequest(new ChannelCreateRequest(name, null, topic, description, lifespan, defaultChannel, parentChannel, orderChannel, bottomPosition, neededTalkPower, codec, codecQuality, maxClients), new EmptyCallback<TeamSpeakCommandResponse>());
        }
    }
    
    @Override
    public void updateChannelProperties(final Channel channel, final String name, final String password, final String topic, final String description, final ChannelLifespan lifespan, final boolean defaultChannel, final Channel parentChannel, final Channel orderChannel, final boolean bottomPosition, final int neededTalkPower, final ChannelCodec codec, final int codecQuality, final int maxClients) {
        Validate.notNull(channel, "Channel cannot be null!", new Object[0]);
        Validate.notEmpty(name, "Channel name cannot be empty!", new Object[0]);
        if (!Strings.isNullOrEmpty(password)) {
            this.networkManager.sendRequest(new HashPasswordRequest(password), new Callback<TeamSpeakCommandResponse>() {
                @Override
                public void onDone(final TeamSpeakCommandResponse response) {
                    final PropertyMap propertyMap = new PropertyMap(response.getParsedResponse());
                    final String hash = propertyMap.get("passwordhash");
                    final String base64hash = Base64.encodeBase64String(hash.getBytes(Charsets.UTF_8));
                    final ChannelEditRequest command = new ChannelEditRequest(channel, name, base64hash, topic, description, lifespan, defaultChannel, parentChannel, orderChannel, bottomPosition, neededTalkPower, codec, codecQuality, maxClients);
                    if (command.getParams().size() > 1) {
                        ServerTabImpl.this.networkManager.sendRequest(command, new EmptyCallback<TeamSpeakCommandResponse>());
                    }
                }
            });
        }
        else {
            final ChannelEditRequest command = new ChannelEditRequest(channel, name, null, topic, description, lifespan, defaultChannel, parentChannel, orderChannel, bottomPosition, neededTalkPower, codec, codecQuality, maxClients);
            if (command.getParams().size() > 1) {
                this.networkManager.sendRequest(command, new EmptyCallback<TeamSpeakCommandResponse>());
            }
        }
    }
    
    @Override
    public void deleteChannel(final Channel channel, final boolean force) {
        this.networkManager.sendRequest(new ChannelDeleteRequest(channel.getId(), force), new EmptyCallback<TeamSpeakCommandResponse>());
    }
    
    public ChannelImpl getChannel(final int channelId) {
        final Object object = this.LOCK;
        synchronized (object) {
            final ChannelImpl channelImpl = this.channelLookup.get(channelId);
            monitorexit(object);
            return channelImpl;
        }
    }
    
    @Override
    public OwnClientImpl getSelf() {
        return this.self;
    }
    
    public void setClients(final List<ClientImpl> clients) {
        final Object object = this.LOCK;
        synchronized (object) {
            this.clientLookup.clear();
            for (final ClientImpl client : clients) {
                client.getChannel().addClient(client);
                this.clientLookup.put(client.getId(), client);
            }
            monitorexit(object);
        }
    }
    
    public void addClient(final ClientImpl client) {
        final Object object = this.LOCK;
        synchronized (object) {
            client.getChannel().addClient(client);
            this.clientLookup.put(client.getId(), client);
            monitorexit(object);
        }
    }
    
    public void removeClient(final ClientImpl client) {
        final Object object = this.LOCK;
        synchronized (object) {
            client.getChannel().removeClient(client);
            this.clientLookup.remove(client.getId());
            monitorexit(object);
        }
    }
    
    public ClientImpl getClient(final int id) {
        final Object object = this.LOCK;
        synchronized (object) {
            final ClientImpl clientImpl = this.clientLookup.get(id);
            monitorexit(object);
            return clientImpl;
        }
    }
    
    public void setSelf(final OwnClientImpl client) {
        this.self = client;
    }
    
    public void setSelfId(final int selfId) {
        this.selfId = selfId;
    }
    
    public int getSelfId() {
        return this.selfId;
    }
    
    @Override
    public List<GroupImpl> getServerGroups() {
        final Object object = this.LOCK;
        synchronized (object) {
            final ImmutableList<Object> copy = ImmutableList.copyOf((Collection<?>)this.serverGroups);
            monitorexit(object);
            return (List<GroupImpl>)copy;
        }
    }
    
    public void addServerGroup(final GroupImpl serverGroup) {
        final Object object = this.LOCK;
        synchronized (object) {
            if (this.serverGroups.contains(serverGroup)) {
                this.serverGroups.remove(serverGroup);
            }
            if (this.serverGroupLookup.containsKey(serverGroup.getId())) {
                this.serverGroupLookup.remove(serverGroup.getId());
            }
            this.serverGroups.add(serverGroup);
            this.serverGroupLookup.put(serverGroup.getId(), serverGroup);
            Collections.sort(this.serverGroups);
            monitorexit(object);
        }
    }
    
    @Override
    public GroupImpl getServerGroup(final int id) {
        final Object object = this.LOCK;
        synchronized (object) {
            final GroupImpl groupImpl = this.serverGroupLookup.get(id);
            monitorexit(object);
            return groupImpl;
        }
    }
    
    @Override
    public GroupImpl getDefaultServerGroup() {
        return this.defaultServerGroup;
    }
    
    public void setDefaultServerGroup(final GroupImpl defaultServerGroup) {
        this.defaultServerGroup = defaultServerGroup;
    }
    
    public void clearServerGroups() {
        final Object object = this.LOCK;
        synchronized (object) {
            this.serverGroups.clear();
            this.serverGroupLookup.clear();
            monitorexit(object);
        }
    }
    
    @Override
    public List<GroupImpl> getChannelGroups() {
        final Object object = this.LOCK;
        synchronized (object) {
            final ImmutableList<Object> copy = ImmutableList.copyOf((Collection<?>)this.channelGroups);
            monitorexit(object);
            return (List<GroupImpl>)copy;
        }
    }
    
    public void addChannelGroup(final GroupImpl channelGroup) {
        final Object object = this.LOCK;
        synchronized (object) {
            if (this.channelGroups.contains(channelGroup)) {
                this.channelGroups.remove(channelGroup);
            }
            if (this.channelGroupLookup.containsKey(channelGroup.getId())) {
                this.channelGroupLookup.remove(channelGroup.getId());
            }
            this.channelGroups.add(channelGroup);
            this.channelGroupLookup.put(channelGroup.getId(), channelGroup);
            Collections.sort(this.channelGroups);
            monitorexit(object);
        }
    }
    
    @Override
    public GroupImpl getChannelGroup(final int id) {
        final Object object = this.LOCK;
        synchronized (object) {
            final GroupImpl groupImpl = this.channelGroupLookup.get(id);
            monitorexit(object);
            return groupImpl;
        }
    }
    
    @Override
    public GroupImpl getDefaultChannelGroup() {
        return this.defaultChannelGroup;
    }
    
    public void setDefaultChannelGroup(final GroupImpl defaultChannelGroup) {
        this.defaultChannelGroup = defaultChannelGroup;
    }
    
    public void clearChannelGroups() {
        final Object object = this.LOCK;
        synchronized (object) {
            this.channelGroups.clear();
            this.channelGroupLookup.clear();
            monitorexit(object);
        }
    }
    
    @Override
    public ServerChatImpl getServerChat() {
        return this.serverChat;
    }
    
    @Override
    public ChannelChatImpl getChannelChat() {
        return this.channelChat;
    }
    
    @Override
    public PokeChatImpl getPokeChat() {
        return this.pokeChat;
    }
    
    @Override
    public void resetPokeChat() {
        this.pokeChat.reset();
    }
    
    @Override
    public List<PrivateChatImpl> getPrivateChats() {
        final Object object = this.LOCK;
        synchronized (object) {
            final ImmutableList<Object> copy = ImmutableList.copyOf((Collection<?>)this.privateChats);
            monitorexit(object);
            return (List<PrivateChatImpl>)copy;
        }
    }
    
    @Override
    public PrivateChatImpl getPrivateChat(final Client client) {
        final Object object = this.LOCK;
        PrivateChatImpl privateChat;
        synchronized (object) {
            privateChat = this.privateChatLookup.get(client.getId());
            monitorexit(object);
        }
        if (privateChat == null) {
            privateChat = new PrivateChatImpl(this.networkManager, (ClientImpl)client);
            this.addPrivateChat(privateChat);
        }
        return privateChat;
    }
    
    public void addPrivateChat(final PrivateChatImpl privateChat) {
        final Object object = this.LOCK;
        synchronized (object) {
            this.privateChats.add(privateChat);
            this.privateChatLookup.put(privateChat.getClient().getId(), privateChat);
            monitorexit(object);
        }
    }
    
    @Override
    public void removePrivateChat(final PrivateChat privateChat) {
        final Object object = this.LOCK;
        synchronized (object) {
            final PrivateChatImpl impl = (PrivateChatImpl)privateChat;
            this.privateChats.remove(impl);
            this.privateChatLookup.remove(privateChat.getClient().getId());
            monitorexit(object);
        }
    }
    
    @Override
    public String toString() {
        return "ServerTab{id=" + this.id + ", serverInfo=" + this.serverInfo + '}';
    }
    
    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || this.getClass() != o.getClass()) {
            return false;
        }
        final ServerTabImpl serverTab = (ServerTabImpl)o;
        return this.id == serverTab.id;
    }
    
    @Override
    public int hashCode() {
        return this.id;
    }
}
