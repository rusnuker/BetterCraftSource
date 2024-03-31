// 
// Decompiled by Procyon v0.6.0
// 

package viamcp.vialoadingbase.provider;

import viamcp.vialoadingbase.ViaLoadingBase;
import com.viaversion.viaversion.api.connection.UserConnection;
import com.viaversion.viaversion.protocols.base.BaseVersionProvider;

public class VLBBaseVersionProvider extends BaseVersionProvider
{
    @Override
    public int getClosestServerProtocol(final UserConnection connection) throws Exception {
        if (connection.isClientSide()) {
            return ViaLoadingBase.getInstance().getTargetVersion().getVersion();
        }
        return super.getClosestServerProtocol(connection);
    }
}
