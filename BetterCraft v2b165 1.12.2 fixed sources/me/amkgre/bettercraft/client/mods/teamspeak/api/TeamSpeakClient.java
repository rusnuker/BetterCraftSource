// 
// Decompiled by Procyon v0.6.0
// 

package me.amkgre.bettercraft.client.mods.teamspeak.api;

import java.util.List;
import me.amkgre.bettercraft.client.mods.teamspeak.listener.DisconnectListener;
import me.amkgre.bettercraft.client.mods.teamspeak.listener.ConnectListener;

public interface TeamSpeakClient
{
    void connect();
    
    void connect(final String p0);
    
    void disconnect();
    
    boolean isConnected();
    
    void addConnectListener(final ConnectListener p0);
    
    void removeConnectListener(final ConnectListener p0);
    
    void addDisconnectListener(final DisconnectListener p0);
    
    void removeDisconnectListener(final DisconnectListener p0);
    
    void setAutoReconnect(final boolean p0);
    
    boolean isAutoReconnect();
    
    List<? extends ServerTab> getServerTabs();
    
    ServerTab getServerTab(final int p0);
    
    ServerTab getSelectedTab();
}
