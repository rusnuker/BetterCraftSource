// 
// Decompiled by Procyon v0.6.0
// 

package net.labymod.labyconnect.packets;

import net.labymod.labyconnect.handling.PacketHandler;
import java.util.UUID;

public class PacketLoginData extends Packet
{
    private UUID id;
    private String name;
    private String motd;
    
    public PacketLoginData() {
    }
    
    public PacketLoginData(final UUID id, final String name, final String motd) {
        this.id = id;
        this.name = name;
        this.motd = motd;
    }
    
    @Override
    public void read(final PacketBuf buf) {
        this.id = UUID.fromString(buf.readString());
        this.name = buf.readString();
        this.motd = buf.readString();
    }
    
    @Override
    public void write(final PacketBuf buf) {
        if (this.id == null) {
            buf.writeString(UUID.randomUUID().toString());
        }
        else {
            buf.writeString(this.id.toString());
        }
        buf.writeString(this.name);
        buf.writeString(this.motd);
    }
    
    public int getId() {
        return 0;
    }
    
    public UUID getUUID() {
        return this.id;
    }
    
    public String getName() {
        return this.name;
    }
    
    @Override
    public void handle(final PacketHandler packetHandler) {
        packetHandler.handle(this);
    }
    
    public String getMotd() {
        return this.motd;
    }
}
