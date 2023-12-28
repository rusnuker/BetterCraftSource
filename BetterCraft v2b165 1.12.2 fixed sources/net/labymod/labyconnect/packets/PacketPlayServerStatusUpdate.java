// 
// Decompiled by Procyon v0.6.0
// 

package net.labymod.labyconnect.packets;

import com.google.common.base.Objects;
import net.labymod.labyconnect.handling.PacketHandler;

public class PacketPlayServerStatusUpdate extends Packet
{
    private String serverIp;
    private int port;
    private String gamemode;
    private boolean viaServerlist;
    
    public PacketPlayServerStatusUpdate(final String serverIp, final int port) {
        this.serverIp = "";
        this.port = 25565;
        this.gamemode = null;
        this.serverIp = serverIp;
        this.port = port;
        this.gamemode = null;
    }
    
    public PacketPlayServerStatusUpdate() {
        this.serverIp = "";
        this.port = 25565;
        this.gamemode = null;
    }
    
    public PacketPlayServerStatusUpdate(final String serverIp, final int port, final String gamemode, final boolean viaServerlist) {
        this.serverIp = "";
        this.port = 25565;
        this.gamemode = null;
        this.serverIp = serverIp;
        this.port = port;
        this.gamemode = gamemode;
        this.viaServerlist = viaServerlist;
    }
    
    @Override
    public void read(final PacketBuf buf) {
        this.serverIp = buf.readString();
        this.port = buf.readInt();
        this.viaServerlist = buf.readBoolean();
        if (buf.readBoolean()) {
            this.gamemode = buf.readString();
        }
    }
    
    @Override
    public void write(final PacketBuf buf) {
        buf.writeString(this.serverIp);
        buf.writeInt(this.port);
        buf.writeBoolean(this.viaServerlist);
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
    
    public boolean equals(final PacketPlayServerStatusUpdate packet) {
        return this.serverIp.equals(packet.serverIp) && this.port == packet.port && Objects.equal(this.gamemode, packet.gamemode);
    }
}
