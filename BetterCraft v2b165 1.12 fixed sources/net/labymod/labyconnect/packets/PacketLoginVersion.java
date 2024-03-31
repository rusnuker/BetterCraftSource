// 
// Decompiled by Procyon v0.6.0
// 

package net.labymod.labyconnect.packets;

import net.labymod.labyconnect.handling.PacketHandler;

public class PacketLoginVersion extends Packet
{
    private int versionId;
    private String versionName;
    private String updateLink;
    
    public PacketLoginVersion(final int internalVersion, final String externalVersion) {
        this.versionId = internalVersion;
        this.versionName = externalVersion;
    }
    
    public PacketLoginVersion() {
    }
    
    @Override
    public void read(final PacketBuf buf) {
        this.versionId = buf.readInt();
        this.versionName = buf.readString();
        this.updateLink = buf.readString();
    }
    
    @Override
    public void write(final PacketBuf buf) {
        buf.writeInt(this.versionId);
        buf.writeString(this.versionName);
        buf.writeString("");
    }
    
    @Override
    public void handle(final PacketHandler packetHandler) {
        packetHandler.handle(this);
    }
    
    public String getVersionName() {
        return this.versionName;
    }
    
    public int getVersionID() {
        return this.versionId;
    }
    
    public String getUpdateLink() {
        return this.updateLink;
    }
}
