// 
// Decompiled by Procyon v0.6.0
// 

package viamcp.vialoadingbase.model;

import viamcp.vialoadingbase.ViaLoadingBase;
import com.viaversion.viaversion.api.protocol.version.ProtocolVersion;

public class ProtocolRange
{
    private final ComparableProtocolVersion lowerBound;
    private final ComparableProtocolVersion upperBound;
    
    public ProtocolRange(final ProtocolVersion lowerBound, final ProtocolVersion upperBound) {
        this(ViaLoadingBase.fromProtocolVersion(lowerBound), ViaLoadingBase.fromProtocolVersion(upperBound));
    }
    
    public ProtocolRange(final ComparableProtocolVersion lowerBound, final ComparableProtocolVersion upperBound) {
        if (lowerBound == null && upperBound == null) {
            throw new RuntimeException("Invalid protocol range");
        }
        this.lowerBound = lowerBound;
        this.upperBound = upperBound;
    }
    
    public static ProtocolRange andNewer(final ProtocolVersion version) {
        return new ProtocolRange(null, version);
    }
    
    public static ProtocolRange singleton(final ProtocolVersion version) {
        return new ProtocolRange(version, version);
    }
    
    public static ProtocolRange andOlder(final ProtocolVersion version) {
        return new ProtocolRange(version, null);
    }
    
    public boolean contains(final ComparableProtocolVersion protocolVersion) {
        return (this.lowerBound == null || protocolVersion.getIndex() >= this.lowerBound.getIndex()) && (this.upperBound == null || protocolVersion.getIndex() <= this.upperBound.getIndex());
    }
    
    @Override
    public String toString() {
        if (this.lowerBound == null) {
            return String.valueOf(this.upperBound.getName()) + "+";
        }
        if (this.upperBound == null) {
            return String.valueOf(this.lowerBound.getName()) + "-";
        }
        if (this.lowerBound == this.upperBound) {
            return this.lowerBound.getName();
        }
        return String.valueOf(this.lowerBound.getName()) + " - " + this.upperBound.getName();
    }
}
