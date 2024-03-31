// 
// Decompiled by Procyon v0.6.0
// 

package viamcp.vialoadingbase.command;

import java.util.UUID;
import com.viaversion.viaversion.api.Via;
import com.viaversion.viaversion.api.connection.UserConnection;
import com.viaversion.viaversion.api.command.ViaCommandSender;

public class UserCommandSender implements ViaCommandSender
{
    private final UserConnection user;
    
    public UserCommandSender(final UserConnection user) {
        this.user = user;
    }
    
    @Override
    public boolean hasPermission(final String s) {
        return false;
    }
    
    @Override
    public void sendMessage(final String s) {
        Via.getPlatform().sendMessage(this.getUUID(), s);
    }
    
    @Override
    public UUID getUUID() {
        return this.user.getProtocolInfo().getUuid();
    }
    
    @Override
    public String getName() {
        return this.user.getProtocolInfo().getUsername();
    }
}
