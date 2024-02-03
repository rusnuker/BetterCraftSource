// 
// Decompiled by Procyon v0.6.0
// 

package net.labymod.addons.teamspeak3;

public interface ControlListener
{
    void onPokeRecieved(final TeamSpeakUser p0, final String p1);
    
    void onClientDisconnected(final TeamSpeakUser p0, final String p1);
    
    void onClientTimout(final TeamSpeakUser p0);
    
    void onClientConnect(final TeamSpeakUser p0);
    
    void onMessageRecieved(final TeamSpeakUser p0, final TeamSpeakUser p1, final String p2);
    
    void onClientStartTyping(final TeamSpeakUser p0);
    
    void onDisconnect();
    
    void onConnect();
    
    void onChannelMessageRecieved(final TeamSpeakUser p0, final String p1);
    
    void onServerMessageRecieved(final TeamSpeakUser p0, final String p1);
    
    void onError(final int p0, final String p1);
}
