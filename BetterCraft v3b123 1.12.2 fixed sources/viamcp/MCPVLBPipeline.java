// 
// Decompiled by Procyon v0.6.0
// 

package viamcp;

import com.viaversion.viaversion.api.connection.UserConnection;
import viamcp.vialoadingbase.netty.VLBPipeline;

public class MCPVLBPipeline extends VLBPipeline
{
    public MCPVLBPipeline(final UserConnection user) {
        super(user);
    }
    
    @Override
    public String getDecoderHandlerName() {
        return "decoder";
    }
    
    @Override
    public String getEncoderHandlerName() {
        return "encoder";
    }
    
    @Override
    public String getDecompressionHandlerName() {
        return "decompress";
    }
    
    @Override
    public String getCompressionHandlerName() {
        return "compress";
    }
}
