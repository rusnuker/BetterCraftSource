// 
// Decompiled by Procyon v0.6.0
// 

package net.labymod.labyconnect.user;

public class ServerInfo
{
    private String serverIp;
    private int serverPort;
    private String specifiedServerName;
    
    public ServerInfo(final String serverIp, final int serverPort, final String specifiedServerName) {
        this.serverIp = serverIp;
        this.serverPort = serverPort;
        this.specifiedServerName = specifiedServerName;
    }
    
    public ServerInfo(final String serverIp, final int serverPort) {
        this.serverIp = serverIp;
        this.serverPort = serverPort;
        this.specifiedServerName = null;
    }
    
    public boolean isServerAvailable() {
        return this.serverIp != null && !this.serverIp.replaceAll(" ", "").isEmpty();
    }
    
    public String getDisplayAddress() {
        String s = this.serverIp;
        if (s.endsWith(".")) {
            s = s.substring(0, s.length() - 1);
        }
        if (this.serverPort != 25565) {
            s = String.valueOf(String.valueOf(s)) + ":" + this.serverPort;
        }
        return s;
    }
    
    public String getServerIp() {
        return this.serverIp;
    }
    
    public int getServerPort() {
        return this.serverPort;
    }
    
    public String getSpecifiedServerName() {
        return this.specifiedServerName;
    }
}
