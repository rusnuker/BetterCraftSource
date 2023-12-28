// 
// Decompiled by Procyon v0.6.0
// 

package me.amkgre.bettercraft.client.mods.teamspeak.event;

public enum TeamSpeakConnectStatus
{
    DISCONNECTED("DISCONNECTED", 0, "disconnected"), 
    CONNECTING("CONNECTING", 1, "connecting"), 
    CONNECTED("CONNECTED", 2, "connected"), 
    CONNECTION_ESTABLISHING("CONNECTION_ESTABLISHING", 3, "connection_establishing"), 
    CONNECTION_ESTABLISHED("CONNECTION_ESTABLISHED", 4, "connection_established");
    
    private final String status;
    
    private TeamSpeakConnectStatus(final String s, final int n, final String status) {
        this.status = status;
    }
    
    public static TeamSpeakConnectStatus byName(final String status) {
        TeamSpeakConnectStatus[] values;
        for (int length = (values = values()).length, i = 0; i < length; ++i) {
            final TeamSpeakConnectStatus teamSpeakConnectStatus = values[i];
            if (teamSpeakConnectStatus.status.equals(status)) {
                return teamSpeakConnectStatus;
            }
        }
        return null;
    }
}
