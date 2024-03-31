// 
// Decompiled by Procyon v0.6.0
// 

package me.amkgre.bettercraft.client.mods.teamspeak.api;

import me.amkgre.bettercraft.client.mods.teamspeak.util.Callback;
import java.util.List;
import java.awt.image.BufferedImage;

public interface Client extends Comparable<Client>
{
    int getId();
    
    int getDatabaseId();
    
    String getUniqueId();
    
    String getName();
    
    String getDisplayName();
    
    ClientType getType();
    
    Channel getChannel();
    
    BufferedImage getIcon();
    
    int getIconId();
    
    BufferedImage getAvatar();
    
    boolean isTalking();
    
    boolean isWhispering();
    
    boolean isInputMuted();
    
    boolean isOutputMuted();
    
    boolean hasInputHardware();
    
    boolean hasOutputHardware();
    
    int getTalkPower();
    
    boolean isTalker();
    
    boolean isPrioritySpeaker();
    
    boolean isRecording();
    
    boolean isChannelCommander();
    
    boolean isMuted();
    
    boolean isAway();
    
    String getAwayMessage();
    
    List<? extends Group> getServerGroups();
    
    Group getChannelGroup();
    
    void joinChannel(final Channel p0);
    
    void joinChannel(final Channel p0, final String p1);
    
    void joinChannel(final Channel p0, final Callback<Integer> p1);
    
    void joinChannel(final Channel p0, final String p1, final Callback<Integer> p2);
    
    void addToServerGroup(final Group p0);
    
    void removeFromServerGroup(final Group p0);
    
    void setChannelGroup(final Channel p0, final Group p1);
    
    void poke(final String p0);
    
    void kickFromChannel(final String p0);
    
    void kickFromServer(final String p0);
    
    void banFromServer(final String p0, final int p1);
    
    void mute();
    
    void unMute();
}
