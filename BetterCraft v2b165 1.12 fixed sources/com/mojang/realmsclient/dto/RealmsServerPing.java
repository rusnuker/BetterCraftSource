// 
// Decompiled by Procyon v0.6.0
// 

package com.mojang.realmsclient.dto;

public class RealmsServerPing
{
    public volatile String nrOfPlayers;
    public volatile String playerList;
    
    public RealmsServerPing() {
        this.nrOfPlayers = "0";
        this.playerList = "";
    }
}
