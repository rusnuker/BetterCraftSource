// 
// Decompiled by Procyon v0.6.0
// 

package net.labymod.labyconnect.packets;

import net.labymod.labyconnect.handling.PacketHandler;
import java.util.TimeZone;
import net.labymod.labyconnect.user.UserStatus;

public class PacketPlayChangeOptions extends Packet
{
    private PacketLoginOptions.Options options;
    
    public PacketPlayChangeOptions(final PacketLoginOptions.Options options) {
        this.options = options;
    }
    
    public PacketPlayChangeOptions(final boolean showServer, final UserStatus status, final TimeZone timeZone) {
        this.options = new PacketLoginOptions.Options(showServer, status, timeZone);
    }
    
    public PacketPlayChangeOptions() {
    }
    
    @Override
    public void read(final PacketBuf buf) {
        this.options = new PacketLoginOptions.Options(buf.readBoolean(), buf.readUserStatus(), TimeZone.getTimeZone(buf.readString()));
    }
    
    @Override
    public void write(final PacketBuf buf) {
        buf.writeBoolean(this.getOptions().isShowServer());
        buf.writeUserStatus(this.getOptions().getOnlineStatus());
        buf.writeString(this.getOptions().getTimeZone().getID());
    }
    
    @Override
    public void handle(final PacketHandler packetHandler) {
        packetHandler.handle(this);
    }
    
    public PacketLoginOptions.Options getOptions() {
        return this.options;
    }
}
