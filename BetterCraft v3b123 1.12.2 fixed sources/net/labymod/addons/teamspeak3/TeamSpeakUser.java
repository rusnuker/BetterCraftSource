// 
// Decompiled by Procyon v0.6.0
// 

package net.labymod.addons.teamspeak3;

import java.util.Iterator;
import java.util.ArrayList;
import java.util.List;

public class TeamSpeakUser
{
    private static List<TeamSpeakUser> users;
    private String nickName;
    private int channelId;
    private final int clientId;
    private boolean talkStatus;
    private int databaseId;
    private String uid;
    private boolean client_input_muted;
    private boolean client_output_muted;
    private boolean client_input_hardware;
    private boolean client_output_hardware;
    private boolean typing;
    private boolean away;
    private int talkPower;
    private String awayMessage;
    private boolean talker;
    private boolean prioritySpeaker;
    private boolean recording;
    private boolean channelCommander;
    private boolean muted;
    private ArrayList<Integer> serverGroups;
    private int channelGroupId;
    private int iconId;
    private String country;
    
    static {
        TeamSpeakUser.users = new ArrayList<TeamSpeakUser>();
    }
    
    public TeamSpeakUser(final int clientId) {
        this.clientId = clientId;
        TeamSpeakUser.users.add(this);
    }
    
    public int getChannelId() {
        return this.channelId;
    }
    
    protected void updateChannelId(final int channelId) {
        this.channelId = channelId;
    }
    
    public int getClientId() {
        return this.clientId;
    }
    
    public String getNickName() {
        return this.nickName.replace("§", "&");
    }
    
    protected void updateNickname(final String nickname) {
        this.nickName = nickname;
    }
    
    public boolean isTalking() {
        return this.talkStatus;
    }
    
    public void updateTalkPower(final int talkPower) {
        this.talkPower = talkPower;
    }
    
    public void updateTalkStatus(final boolean talkStatus) {
        this.talkStatus = talkStatus;
    }
    
    protected static List<TeamSpeakUser> getUsers() {
        return TeamSpeakUser.users;
    }
    
    public int getDatabaseId() {
        return this.databaseId;
    }
    
    public String getUid() {
        return this.uid;
    }
    
    public boolean isTyping() {
        return this.typing;
    }
    
    public boolean hasClientInputHardware() {
        return this.client_input_hardware;
    }
    
    public boolean hasClientOutputHardware() {
        return this.client_output_hardware;
    }
    
    public boolean hasClientInputMuted() {
        return this.client_input_muted;
    }
    
    public boolean hasClientOutputMuted() {
        return this.client_output_muted;
    }
    
    public void updateClientInput(final boolean muted) {
        this.client_input_muted = muted;
    }
    
    public void updateClientOutput(final boolean muted) {
        this.client_output_muted = muted;
    }
    
    public void updateTyping(final boolean typing) {
        this.typing = typing;
    }
    
    public void updateAway(final boolean away, final String awayMessage) {
        this.away = away;
        this.awayMessage = awayMessage;
    }
    
    public void updateDatabaseId(final int databaseId) {
        this.databaseId = databaseId;
    }
    
    public void updateAway(final boolean away) {
        this.away = away;
        this.awayMessage = "";
    }
    
    public String getAwayMessage() {
        return this.awayMessage;
    }
    
    public boolean isAway() {
        return this.away;
    }
    
    public void updateClientInputHardware(final boolean muted) {
        this.client_input_hardware = muted;
    }
    
    public void updateClientOutputHardware(final boolean muted) {
        this.client_output_hardware = muted;
    }
    
    public static void reset() {
        TeamSpeakUser.users.clear();
    }
    
    public int getTalkPower() {
        return this.talkPower;
    }
    
    public int getChannelGroupId() {
        return this.channelGroupId;
    }
    
    public String getCountry() {
        return this.country;
    }
    
    public ArrayList<Integer> getServerGroups() {
        return this.serverGroups;
    }
    
    public int getIconId() {
        return this.iconId;
    }
    
    public void updateTalker(final boolean talker) {
        this.talker = talker;
    }
    
    public void updatePrioritySpeaker(final boolean prioritySpeaker) {
        this.prioritySpeaker = prioritySpeaker;
    }
    
    public void updateRecording(final boolean recording) {
        this.recording = recording;
    }
    
    public void updateChannelCommander(final boolean channelCommander) {
        this.channelCommander = channelCommander;
    }
    
    public void updateMuted(final boolean muted) {
        this.muted = muted;
    }
    
    public void updateServerGroups(final ArrayList<Integer> serverGroups) {
        this.serverGroups = serverGroups;
    }
    
    public void updateChannelGroupId(final int channelGroupId) {
        this.channelGroupId = channelGroupId;
    }
    
    public void updateIconId(final int iconId) {
        this.iconId = iconId;
    }
    
    public void updateCountry(final String country) {
        this.country = country;
    }
    
    public void updateUid(final String uid) {
        this.uid = uid;
    }
    
    public boolean isChannelCommander() {
        return this.channelCommander;
    }
    
    public static boolean contains(final int id) {
        for (final TeamSpeakUser teamspeakuser : TeamSpeakUser.users) {
            if (teamspeakuser.getClientId() == id) {
                return true;
            }
        }
        return false;
    }
    
    public static void unregisterUser(final TeamSpeakUser user) {
        TeamSpeakUser.users.remove(user);
    }
    
    public static int amount() {
        return TeamSpeakUser.users.size();
    }
}
