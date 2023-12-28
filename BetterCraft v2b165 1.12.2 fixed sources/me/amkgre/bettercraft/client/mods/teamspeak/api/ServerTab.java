// 
// Decompiled by Procyon v0.6.0
// 

package me.amkgre.bettercraft.client.mods.teamspeak.api;

import java.util.List;

public interface ServerTab
{
    int getId();
    
    boolean isSelected();
    
    void setSelected();
    
    ServerInfo getServerInfo();
    
    List<? extends Channel> getChannels();
    
    void createChannel(final String p0, final String p1, final String p2, final String p3, final ChannelLifespan p4, final boolean p5, final Channel p6, final Channel p7, final boolean p8, final int p9, final ChannelCodec p10, final int p11, final int p12);
    
    void updateChannelProperties(final Channel p0, final String p1, final String p2, final String p3, final String p4, final ChannelLifespan p5, final boolean p6, final Channel p7, final Channel p8, final boolean p9, final int p10, final ChannelCodec p11, final int p12, final int p13);
    
    void deleteChannel(final Channel p0, final boolean p1);
    
    OwnClient getSelf();
    
    List<? extends Group> getServerGroups();
    
    Group getServerGroup(final int p0);
    
    Group getDefaultServerGroup();
    
    List<? extends Group> getChannelGroups();
    
    Group getChannelGroup(final int p0);
    
    Group getDefaultChannelGroup();
    
    Chat getServerChat();
    
    Chat getChannelChat();
    
    Chat getPokeChat();
    
    void resetPokeChat();
    
    List<? extends PrivateChat> getPrivateChats();
    
    PrivateChat getPrivateChat(final Client p0);
    
    void removePrivateChat(final PrivateChat p0);
}
