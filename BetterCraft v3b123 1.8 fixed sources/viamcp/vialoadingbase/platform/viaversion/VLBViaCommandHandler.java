// 
// Decompiled by Procyon v0.6.0
// 

package viamcp.vialoadingbase.platform.viaversion;

import com.viaversion.viaversion.api.command.ViaSubCommand;
import viamcp.vialoadingbase.command.impl.LeakDetectSubCommand;
import com.viaversion.viaversion.commands.ViaCommandHandler;

public class VLBViaCommandHandler extends ViaCommandHandler
{
    public VLBViaCommandHandler() {
        this.registerVLBDefaults();
    }
    
    public void registerVLBDefaults() {
        this.registerSubCommand(new LeakDetectSubCommand());
    }
}
