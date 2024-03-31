// 
// Decompiled by Procyon v0.6.0
// 

package me.amkgre.bettercraft.client.mods.teamspeak.api;

import java.awt.image.BufferedImage;

public interface ServerInfo
{
    String getIp();
    
    int getPort();
    
    String getName();
    
    String getUniqueId();
    
    String getPlatform();
    
    String getVersion();
    
    long getTimeCreated();
    
    ServerImage getServerBanner();
    
    ServerImage getServerHostButton();
    
    BufferedImage getIcon();
}
