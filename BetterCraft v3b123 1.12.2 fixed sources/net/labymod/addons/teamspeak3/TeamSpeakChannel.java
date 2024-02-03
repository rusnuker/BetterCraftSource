// 
// Decompiled by Procyon v0.6.0
// 

package net.labymod.addons.teamspeak3;

import java.util.Iterator;
import java.util.Collection;
import java.util.ArrayList;
import java.util.List;

public class TeamSpeakChannel
{
    private static final List<TeamSpeakChannel> channels;
    private final int channelId;
    private String channel_name;
    private int pid;
    private int channel_order;
    private int total_clients;
    private int channel_codec;
    private int channel_codec_quality;
    private int talk_power;
    private int max_clients;
    private int max_family_clients;
    private boolean flag_are_subscribed;
    private String topic;
    private boolean flagDefault;
    private boolean password;
    private boolean permanent;
    private boolean semiPermanent;
    private int iconID;
    
    static {
        channels = new ArrayList<TeamSpeakChannel>();
    }
    
    public TeamSpeakChannel(final int channel_id) {
        this.channelId = channel_id;
        TeamSpeakChannel.channels.add(this);
    }
    
    protected void updatePID(final int pid) {
        this.pid = pid;
    }
    
    protected void updateChannelOrder(final int channel_order) {
        this.channel_order = channel_order;
    }
    
    protected void updateTotalClients(final int total_clients) {
        this.total_clients = total_clients;
    }
    
    public String getChannelName() {
        return this.channel_name;
    }
    
    public int getChannelOrder() {
        return this.channel_order;
    }
    
    public int getChannelId() {
        return this.channelId;
    }
    
    public int getPid() {
        return this.pid;
    }
    
    public int getTotalClients() {
        return this.total_clients;
    }
    
    public static void reset() {
        TeamSpeakChannel.channels.clear();
    }
    
    protected static List<TeamSpeakChannel> getChannels() {
        final List<TeamSpeakChannel> list = new ArrayList<TeamSpeakChannel>();
        list.addAll(TeamSpeakChannel.channels);
        return list;
    }
    
    public int getChannelCodec() {
        return this.channel_codec;
    }
    
    public String getChannelCodecName() {
        return new StringBuilder(String.valueOf(this.channel_codec)).toString().replace("0", "Speex Narrowband").replace("1", "Speex Wideband").replace("2", "Speex Ultra-Wideband").replace("3", "CELT Mono").replace("4", "Opus Voice").replace("5", "Opus Music");
    }
    
    public int getChannelCodecQuality() {
        return this.channel_codec_quality;
    }
    
    public int getMaxClients() {
        return this.max_clients;
    }
    
    public int getTalkPower() {
        return this.talk_power;
    }
    
    public void updateChannelName(final String name) {
        this.channel_name = name;
    }
    
    public int getMaxFamilyClients() {
        return this.max_family_clients;
    }
    
    public boolean getSubscription() {
        return this.flag_are_subscribed;
    }
    
    public void updateChannelCodec(final int channel_codec) {
        this.channel_codec = channel_codec;
    }
    
    public void updateChannelCodecQuality(final int channel_codec_quality) {
        this.channel_codec_quality = channel_codec_quality;
    }
    
    public void updateFlagAreSubscribed(final boolean flag_are_subscribed) {
        this.flag_are_subscribed = flag_are_subscribed;
    }
    
    public void updateMaxClients(final int max_clients) {
        this.max_clients = max_clients;
    }
    
    public void updateTalkPower(final int talk_power) {
        this.talk_power = talk_power;
    }
    
    public void updateMaxFamilyClients(final int max_family_clients) {
        this.max_family_clients = max_family_clients;
    }
    
    public int getIconID() {
        return this.iconID;
    }
    
    public String getTopic() {
        return this.topic;
    }
    
    public boolean getFlagDefault() {
        return this.flagDefault;
    }
    
    public boolean getIsPassword() {
        return this.password;
    }
    
    public boolean getIsPermanent() {
        return this.permanent;
    }
    
    public boolean getIsSemiPermanent() {
        return this.semiPermanent;
    }
    
    public void updateIsPassword(final boolean password) {
        this.password = password;
    }
    
    public void updatePermanent(final boolean permanent) {
        this.permanent = permanent;
    }
    
    public void updateSemiPermanent(final boolean semiPermanent) {
        this.semiPermanent = semiPermanent;
    }
    
    public void updateTopic(final String topic) {
        this.topic = topic;
    }
    
    public void updateIconID(final int iconID) {
        this.iconID = iconID;
    }
    
    public void updateFlagDefault(final boolean flagDefault) {
        this.flagDefault = flagDefault;
    }
    
    public static boolean contains(final int id) {
        for (final TeamSpeakChannel teamspeakchannel : TeamSpeakChannel.channels) {
            if (teamspeakchannel.getChannelId() == id) {
                return true;
            }
        }
        return false;
    }
    
    public static void deleteChannel(final TeamSpeakChannel c) {
        TeamSpeakChannel.channels.remove(c);
    }
    
    public static int amount() {
        return TeamSpeakChannel.channels.size();
    }
}
