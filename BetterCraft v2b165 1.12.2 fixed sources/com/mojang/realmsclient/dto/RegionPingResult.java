// 
// Decompiled by Procyon v0.6.0
// 

package com.mojang.realmsclient.dto;

public class RegionPingResult
{
    private final String regionName;
    private final int ping;
    
    public RegionPingResult(final String regionName, final int ping) {
        this.regionName = regionName;
        this.ping = ping;
    }
    
    public int ping() {
        return this.ping;
    }
    
    @Override
    public String toString() {
        return String.format("%s --> %.2f ms", this.regionName, this.ping);
    }
}
