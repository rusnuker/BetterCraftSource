// 
// Decompiled by Procyon v0.6.0
// 

package me.amkgre.bettercraft.client.mods.checkhost.results;

import java.util.Map;

public class CheckHostDnsResult
{
    private final int ttl;
    private final Map<String, String[]> result;
    
    public CheckHostDnsResult(final int ttl, final Map<String, String[]> result) {
        this.ttl = ttl;
        this.result = result;
    }
    
    public Map<String, String[]> getResult() {
        return this.result;
    }
    
    public int getTTL() {
        return this.ttl;
    }
    
    public boolean isSuccessful() {
        return this.ttl >= 0;
    }
}
