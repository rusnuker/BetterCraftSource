// 
// Decompiled by Procyon v0.6.0
// 

package net.labymod.labyconnect.packets;

import net.labymod.labyconnect.user.ServerInfo;
import net.labymod.labyconnect.handling.PacketHandler;

public class PacketPlayServerStatus extends Packet
{
    private String serverIp;
    private int port;
    private String gamemode;
    
    public PacketPlayServerStatus(final String serverIp, final int port) {
        this.serverIp = "";
        this.port = 0;
        this.gamemode = null;
        this.serverIp = serverIp;
        this.port = port;
        this.gamemode = null;
    }
    
    public PacketPlayServerStatus() {
        this.serverIp = "";
        this.port = 0;
        this.gamemode = null;
    }
    
    public PacketPlayServerStatus(final String serverIp, final int port, final String gamemode) {
        this.serverIp = "";
        this.port = 0;
        this.gamemode = null;
        this.serverIp = serverIp;
        this.port = port;
        this.gamemode = gamemode;
    }
    
    @Override
    public void read(final PacketBuf buf) {
        this.serverIp = buf.readString();
        this.port = buf.readInt();
        if (buf.readBoolean()) {
            this.gamemode = buf.readString();
        }
    }
    
    @Override
    public void write(final PacketBuf buf) {
        buf.writeString(this.serverIp);
        buf.writeInt(this.port);
        if (this.gamemode != null && !this.gamemode.isEmpty()) {
            buf.writeBoolean(true);
            buf.writeString(this.gamemode);
        }
        else {
            buf.writeBoolean(false);
        }
    }
    
    @Override
    public void handle(final PacketHandler packetHandler) {
        packetHandler.handle(this);
    }
    
    public String getServerIp() {
        return this.serverIp;
    }
    
    public int getPort() {
        return this.port;
    }
    
    public String getGamemode() {
        return this.gamemode;
    }
    
    public ServerInfo build() {
        return (this.gamemode == null) ? new ServerInfo(this.serverIp, this.port) : new ServerInfo(this.serverIp, this.port, this.gamemode);
    }
    
    public boolean equals(final PacketPlayServerStatus packet) {
        return this.serverIp.equals(packet.getServerIp()) && this.port == packet.getPort() && this.gamemode.equals(packet.getGamemode());
    }
}
