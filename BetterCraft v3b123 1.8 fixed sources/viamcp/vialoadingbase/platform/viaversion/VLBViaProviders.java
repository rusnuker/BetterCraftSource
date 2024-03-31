// 
// Decompiled by Procyon v0.6.0
// 

package viamcp.vialoadingbase.platform.viaversion;

import com.viaversion.viaversion.api.platform.providers.ViaProviders;
import viamcp.vialoadingbase.ViaLoadingBase;
import viamcp.vialoadingbase.platform.providers.VLBMovementTransmitterProvider;
import com.viaversion.viaversion.protocols.protocol1_9to1_8.providers.MovementTransmitterProvider;
import viamcp.vialoadingbase.provider.VLBBaseVersionProvider;
import com.viaversion.viaversion.api.protocol.version.VersionProvider;
import com.viaversion.viaversion.api.Via;
import com.viaversion.viaversion.api.platform.ViaPlatformLoader;

public class VLBViaProviders implements ViaPlatformLoader
{
    @Override
    public void load() {
        final ViaProviders providers = Via.getManager().getProviders();
        providers.use((Class<VLBBaseVersionProvider>)VersionProvider.class, new VLBBaseVersionProvider());
        providers.use((Class<VLBMovementTransmitterProvider>)MovementTransmitterProvider.class, new VLBMovementTransmitterProvider());
        if (ViaLoadingBase.getInstance().getProviders() != null) {
            ViaLoadingBase.getInstance().getProviders().accept(providers);
        }
    }
    
    @Override
    public void unload() {
    }
}
