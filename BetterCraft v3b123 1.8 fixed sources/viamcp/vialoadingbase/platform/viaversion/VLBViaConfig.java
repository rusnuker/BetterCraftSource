// 
// Decompiled by Procyon v0.6.0
// 

package viamcp.vialoadingbase.platform.viaversion;

import java.util.Map;
import java.net.URL;
import java.io.File;
import java.util.Arrays;
import java.util.List;
import com.viaversion.viaversion.configuration.AbstractViaConfig;

public class VLBViaConfig extends AbstractViaConfig
{
    private static final List<String> UNSUPPORTED;
    
    static {
        UNSUPPORTED = Arrays.asList("anti-xray-patch", "bungee-ping-interval", "bungee-ping-save", "bungee-servers", "quick-move-action-fix", "nms-player-ticking", "velocity-ping-interval", "velocity-ping-save", "velocity-servers", "blockconnection-method", "change-1_9-hitbox", "change-1_14-hitbox", "show-shield-when-sword-in-hand", "left-handed-handling");
    }
    
    public VLBViaConfig(final File configFile) {
        super(configFile);
        this.reloadConfig();
    }
    
    @Override
    public URL getDefaultConfigURL() {
        return this.getClass().getClassLoader().getResource("assets/viaversion/config.yml");
    }
    
    @Override
    protected void handleConfig(final Map<String, Object> config) {
    }
    
    @Override
    public List<String> getUnsupportedOptions() {
        return VLBViaConfig.UNSUPPORTED;
    }
}
