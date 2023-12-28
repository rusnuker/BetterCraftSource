// 
// Decompiled by Procyon v0.6.0
// 

package me.amkgre.bettercraft.client.mods.teamspeak.api;

import java.util.List;
import java.awt.image.BufferedImage;

public interface Channel
{
    ServerTab getServerTab();
    
    int getId();
    
    String getName();
    
    String getFormattedName();
    
    ChannelType getType();
    
    BufferedImage getIcon();
    
    int getIconId();
    
    Channel getParent();
    
    Channel getAbove();
    
    List<? extends Channel> getChildren();
    
    List<? extends Client> getClients();
    
    Client getClient(final int p0);
    
    String getTopic();
    
    String getDescription();
    
    boolean hasSubscribed();
    
    boolean isDefault();
    
    boolean requiresPassword();
    
    boolean isPermanent();
    
    boolean isSemiPermanent();
    
    ChannelCodec getCodec();
    
    int getCodecQuality();
    
    int getNeededTalkPower();
    
    int getMaxClients();
    
    int getMaxFamilyClients();
    
    void moveBelow(final Channel p0);
    
    void moveInside(final Channel p0);
    
    void moveInside(final Channel p0, final Channel p1);
}
