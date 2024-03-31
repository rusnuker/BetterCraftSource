// 
// Decompiled by Procyon v0.6.0
// 

package me.amkgre.bettercraft.client.mods.teamspeak.impl;

import me.amkgre.bettercraft.client.mods.teamspeak.api.Client;
import me.amkgre.bettercraft.client.mods.teamspeak.api.ServerTab;
import me.amkgre.bettercraft.client.mods.teamspeak.request.ChannelMoveRequest;
import me.amkgre.bettercraft.client.mods.teamspeak.util.Callback;
import me.amkgre.bettercraft.client.mods.teamspeak.request.Request;
import me.amkgre.bettercraft.client.mods.teamspeak.response.TeamSpeakCommandResponse;
import me.amkgre.bettercraft.client.mods.teamspeak.util.EmptyCallback;
import me.amkgre.bettercraft.client.mods.teamspeak.request.ChannelEditRequest;
import com.google.common.base.Objects;
import java.util.Collections;
import java.util.Iterator;
import java.util.Collection;
import com.google.common.collect.ImmutableList;
import me.amkgre.bettercraft.client.mods.teamspeak.util.ImageManager;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.ArrayList;
import me.amkgre.bettercraft.client.mods.teamspeak.util.PropertyMap;
import me.amkgre.bettercraft.client.mods.teamspeak.api.ChannelCodec;
import java.util.Map;
import java.util.List;
import me.amkgre.bettercraft.client.mods.teamspeak.api.ChannelType;
import me.amkgre.bettercraft.client.mods.teamspeak.api.Channel;

public class ChannelImpl implements Channel
{
    private final ServerTabImpl serverTab;
    private final int id;
    private int order;
    private String name;
    private String formattedName;
    private ChannelType type;
    private int parentId;
    private ChannelImpl parent;
    private int iconId;
    private final Object LOCK;
    private final List<ChannelImpl> children;
    private final List<ClientImpl> clients;
    private final Map<Integer, ClientImpl> clientLookup;
    private String topic;
    private String description;
    private boolean subscribed;
    private boolean defaultChannel;
    private boolean requiresPassword;
    private boolean permanent;
    private boolean semiPermanent;
    private ChannelCodec codec;
    private int codecQuality;
    private int neededTalkPower;
    private int maxClients;
    private boolean maxClientsUnlimited;
    private int maxFamilyClients;
    private boolean maxFamilyClientsUnlimited;
    
    public ChannelImpl(final ServerTabImpl serverTab, final PropertyMap propertyMap) {
        this.LOCK = new Object();
        this.children = new ArrayList<ChannelImpl>();
        this.clients = new ArrayList<ClientImpl>();
        this.clientLookup = new HashMap<Integer, ClientImpl>();
        this.serverTab = serverTab;
        this.id = propertyMap.getInt("cid");
    }
    
    public void updateProperties(final PropertyMap propertyMap) {
        this.order = propertyMap.getInt("channel_order", this.order);
        if (propertyMap.contains("channel_name")) {
            this.name = propertyMap.get("channel_name", this.name);
            this.type = ChannelType.byName(this.name);
            this.formattedName = this.type.formatName(this.name);
        }
        this.topic = propertyMap.get("channel_topic", this.topic);
        this.description = propertyMap.get("channel_description", this.description);
        this.subscribed = propertyMap.getBool("channel_flag_are_subscribed", this.subscribed);
        this.defaultChannel = propertyMap.getBool("channel_flag_default", this.defaultChannel);
        this.requiresPassword = propertyMap.getBool("channel_flag_password", this.requiresPassword);
        this.permanent = propertyMap.getBool("channel_flag_permanent", this.permanent);
        this.semiPermanent = propertyMap.getBool("channel_flag_semi_permanent", this.semiPermanent);
        this.codec = ChannelCodec.byId(propertyMap.getInt("channel_codec", (this.codec == null) ? 0 : this.codec.getId()));
        this.codecQuality = propertyMap.getInt("channel_codec_quality", this.codecQuality);
        this.neededTalkPower = propertyMap.getInt("channel_needed_talk_power", this.neededTalkPower);
        this.maxClients = propertyMap.getInt("channel_maxclients", this.maxClients);
        this.maxClientsUnlimited = propertyMap.getBool("channel_flag_maxclients_unlimited", this.maxClientsUnlimited);
        this.maxFamilyClients = propertyMap.getInt("channel_maxfamilyclients", this.maxFamilyClients);
        this.maxFamilyClientsUnlimited = propertyMap.getBool("channel_flag_maxfamilyclients_unlimited", this.maxFamilyClientsUnlimited);
        if (propertyMap.contains("pid")) {
            this.parentId = propertyMap.getInt("pid", this.parentId);
        }
        else if (propertyMap.contains("cpid")) {
            this.parentId = propertyMap.getInt("cpid", this.parentId);
        }
        this.iconId = propertyMap.getInt("channel_icon_id", this.iconId);
    }
    
    @Override
    public ServerTabImpl getServerTab() {
        return this.serverTab;
    }
    
    public ServerInfoImpl getServerInfo() {
        return this.serverTab.getServerInfo();
    }
    
    @Override
    public int getId() {
        return this.id;
    }
    
    public int getOrder() {
        return this.order;
    }
    
    public void setOrder(final int order) {
        this.order = order;
    }
    
    @Override
    public String getName() {
        return this.name;
    }
    
    @Override
    public String getFormattedName() {
        return this.formattedName;
    }
    
    @Override
    public ChannelType getType() {
        return this.type;
    }
    
    public int getParentId() {
        return this.parentId;
    }
    
    @Override
    public int getIconId() {
        return this.iconId;
    }
    
    @Override
    public BufferedImage getIcon() {
        return (this.getServerInfo() == null || this.getServerInfo().getUniqueId() == null) ? null : ImageManager.resolveIcon(this.getServerInfo().getUniqueId(), this.getIconId());
    }
    
    @Override
    public ChannelImpl getParent() {
        return this.parent;
    }
    
    public void setParent(final ChannelImpl parent) {
        this.parent = parent;
    }
    
    @Override
    public Channel getAbove() {
        return this.serverTab.getChannel(this.order);
    }
    
    @Override
    public List<ChannelImpl> getChildren() {
        final Object object = this.LOCK;
        synchronized (object) {
            final ImmutableList<Object> copy = ImmutableList.copyOf((Collection<?>)this.children);
            monitorexit(object);
            return (List<ChannelImpl>)copy;
        }
    }
    
    void addChild(final ChannelImpl channel) {
        final Object object = this.LOCK;
        synchronized (object) {
            for (final ChannelImpl ch : this.children) {
                if (ch.getOrder() != channel.getOrder()) {
                    continue;
                }
                ch.setOrder(channel.getId());
                break;
            }
            this.children.add(channel);
            monitorexit(object);
        }
    }
    
    void removeChild(final ChannelImpl channel) {
        final Object object = this.LOCK;
        synchronized (object) {
            this.children.remove(channel);
            for (final ChannelImpl other : this.children) {
                if (other.getOrder() != channel.getId()) {
                    continue;
                }
                other.setOrder(channel.getOrder());
            }
            monitorexit(object);
        }
    }
    
    @Override
    public List<ClientImpl> getClients() {
        final Object object = this.LOCK;
        synchronized (object) {
            final ImmutableList<Object> copy = ImmutableList.copyOf((Collection<?>)this.clients);
            monitorexit(object);
            return (List<ClientImpl>)copy;
        }
    }
    
    public void addClient(final ClientImpl client) {
        final Object object = this.LOCK;
        synchronized (object) {
            this.clients.add(client);
            this.clientLookup.put(client.getId(), client);
            Collections.sort(this.clients);
            monitorexit(object);
        }
    }
    
    public void removeClient(final ClientImpl client) {
        final Object object = this.LOCK;
        synchronized (object) {
            this.clients.remove(client);
            this.clientLookup.remove(client.getId());
            Collections.sort(this.clients);
            monitorexit(object);
        }
    }
    
    @Override
    public ClientImpl getClient(final int id) {
        final Object object = this.LOCK;
        synchronized (object) {
            final ClientImpl clientImpl = this.clientLookup.get(id);
            monitorexit(object);
            return clientImpl;
        }
    }
    
    @Override
    public String getTopic() {
        return this.topic;
    }
    
    @Override
    public String getDescription() {
        return this.description;
    }
    
    @Override
    public boolean hasSubscribed() {
        return this.subscribed;
    }
    
    public void setSubscribed(final boolean subscribed) {
        this.subscribed = subscribed;
    }
    
    @Override
    public boolean isDefault() {
        return this.defaultChannel;
    }
    
    @Override
    public boolean requiresPassword() {
        return this.requiresPassword;
    }
    
    @Override
    public boolean isPermanent() {
        return this.permanent;
    }
    
    @Override
    public boolean isSemiPermanent() {
        return this.semiPermanent;
    }
    
    @Override
    public ChannelCodec getCodec() {
        return this.codec;
    }
    
    @Override
    public int getCodecQuality() {
        return this.codecQuality;
    }
    
    @Override
    public int getNeededTalkPower() {
        return this.neededTalkPower;
    }
    
    @Override
    public int getMaxClients() {
        return this.maxClientsUnlimited ? -1 : this.maxClients;
    }
    
    @Override
    public int getMaxFamilyClients() {
        return this.maxFamilyClientsUnlimited ? -1 : this.maxFamilyClients;
    }
    
    @Override
    public void moveBelow(final Channel channel) {
        if (Objects.equal(this.parent, channel.getParent())) {
            this.serverTab.networkManager.sendRequest(new ChannelEditRequest(this.getId(), channel.getId()), new EmptyCallback<TeamSpeakCommandResponse>());
        }
        else {
            this.serverTab.networkManager.sendRequest(new ChannelMoveRequest(this.getId(), (channel.getParent() == null) ? 0 : channel.getParent().getId(), channel.getId()), new EmptyCallback<TeamSpeakCommandResponse>());
        }
    }
    
    @Override
    public void moveInside(final Channel channel) {
        this.moveInside(channel, null);
    }
    
    @Override
    public void moveInside(final Channel channel, final Channel above) {
        this.serverTab.networkManager.sendRequest(new ChannelMoveRequest(this.getId(), channel.getId(), (above == null) ? 0 : above.getId()), new EmptyCallback<TeamSpeakCommandResponse>());
    }
    
    @Override
    public String toString() {
        return "ChannelImpl{id=" + this.id + ", name='" + this.name + '\'' + '}';
    }
    
    static void sort(final List<ChannelImpl> channels) {
        sort0(channels);
        for (final ChannelImpl channel : channels) {
            sort(channel.children);
        }
    }
    
    private static void sort0(final List<ChannelImpl> channels) {
        final HashMap<Integer, ChannelImpl> map = new HashMap<Integer, ChannelImpl>();
        for (final ChannelImpl channel : channels) {
            if (map.containsKey(channel.getOrder())) {
                throw new IllegalArgumentException("duplicate order ids: " + channel.getOrder());
            }
            map.put(channel.getOrder(), channel);
        }
        final ArrayList<ChannelImpl> sorted = new ArrayList<ChannelImpl>();
        for (int i = 0; i < channels.size(); ++i) {
            final ChannelImpl channelImpl;
            final ChannelImpl e = channelImpl = ((i == 0) ? map.get(0) : map.get(sorted.get(i - 1).getId()));
            if (e == null) {
                throw new NullPointerException("map " + map + " does not contain element with id " + ((i == 0) ? 0 : sorted.get(i - 1).getId()) + "!");
            }
            sorted.add(e);
        }
        channels.clear();
        channels.addAll(sorted);
    }
}
