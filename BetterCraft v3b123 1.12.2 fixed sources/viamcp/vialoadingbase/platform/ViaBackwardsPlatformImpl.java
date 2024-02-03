// 
// Decompiled by Procyon v0.6.0
// 

package viamcp.vialoadingbase.platform;

import viamcp.vialoadingbase.ViaLoadingBase;
import java.util.logging.Logger;
import java.io.File;
import com.viaversion.viabackwards.api.ViaBackwardsPlatform;

public class ViaBackwardsPlatformImpl implements ViaBackwardsPlatform
{
    private final File directory;
    
    public ViaBackwardsPlatformImpl(final File directory) {
        this.init(this.directory = directory);
    }
    
    @Override
    public Logger getLogger() {
        return ViaLoadingBase.LOGGER;
    }
    
    @Override
    public boolean isOutdated() {
        return false;
    }
    
    @Override
    public void disable() {
    }
    
    @Override
    public File getDataFolder() {
        return this.directory;
    }
}
