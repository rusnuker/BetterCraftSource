// 
// Decompiled by Procyon v0.6.0
// 

package viamcp.vialoadingbase.platform.viaversion;

import com.viaversion.viaversion.libs.gson.JsonObject;
import java.util.Iterator;
import com.viaversion.viaversion.api.protocol.version.ProtocolVersion;
import com.viaversion.viaversion.libs.fastutil.ints.IntLinkedOpenHashSet;
import com.viaversion.viaversion.libs.fastutil.ints.IntSortedSet;
import com.viaversion.viaversion.api.platform.ViaInjector;

public class VLBViaInjector implements ViaInjector
{
    @Override
    public void inject() {
    }
    
    @Override
    public void uninject() {
    }
    
    @Override
    public String getDecoderName() {
        return "via-decoder";
    }
    
    @Override
    public String getEncoderName() {
        return "via-encoder";
    }
    
    @Override
    public IntSortedSet getServerProtocolVersions() {
        final IntSortedSet versions = new IntLinkedOpenHashSet();
        for (final ProtocolVersion value : ProtocolVersion.getProtocols()) {
            if (value.getVersion() >= ProtocolVersion.v1_7_1.getVersion()) {
                versions.add(value.getVersion());
            }
        }
        return versions;
    }
    
    @Override
    public int getServerProtocolVersion() {
        return this.getServerProtocolVersions().firstInt();
    }
    
    @Override
    public JsonObject getDump() {
        return new JsonObject();
    }
}
