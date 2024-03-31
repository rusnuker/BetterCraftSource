// 
// Decompiled by Procyon v0.6.0
// 

package net.labymod.utils;

public class ServerData
{
    private String ip;
    private int port;
    private int index;
    
    public ServerData(final String ip, final int port) {
        this.port = 0;
        this.index = 0;
        this.ip = ip;
        this.port = port;
    }
    
    public String getIp() {
        return this.ip;
    }
    
    public int getPort() {
        return this.port;
    }
    
    public int getIndex() {
        return this.index;
    }
    
    public void setIp(final String ip) {
        this.ip = ip;
    }
    
    public void setPort(final int port) {
        this.port = port;
    }
    
    public void setIndex(final int index) {
        this.index = index;
    }
}
