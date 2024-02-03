// 
// Decompiled by Procyon v0.6.0
// 

package viamcp.vialoadingbase.platform;

import viamcp.vialoadingbase.ViaLoadingBase;
import java.util.logging.Logger;
import de.gerrygames.viarewind.api.ViaRewindConfig;
import de.gerrygames.viarewind.api.ViaRewindConfigImpl;
import java.io.File;
import de.gerrygames.viarewind.api.ViaRewindPlatform;

public class ViaRewindPlatformImpl implements ViaRewindPlatform
{
    public ViaRewindPlatformImpl(final File directory) {
        final ViaRewindConfigImpl config = new ViaRewindConfigImpl(new File(directory, "viarewind.yml"));
        config.reloadConfig();
        this.init(config);
    }
    
    @Override
    public Logger getLogger() {
        return ViaLoadingBase.LOGGER;
    }
}
