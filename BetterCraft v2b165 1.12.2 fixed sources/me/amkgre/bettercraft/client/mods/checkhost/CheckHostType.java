// 
// Decompiled by Procyon v0.6.0
// 

package me.amkgre.bettercraft.client.mods.checkhost;

public enum CheckHostType
{
    PING("PING", 0, "ping"), 
    TCP("TCP", 1, "tcp"), 
    UDP("UDP", 2, "udp"), 
    HTTP("HTTP", 3, "http"), 
    DNS("DNS", 4, "dns");
    
    private final String value;
    
    private CheckHostType(final String s, final int n, final String value) {
        this.value = value;
    }
    
    public String getValue() {
        return this.value;
    }
}
