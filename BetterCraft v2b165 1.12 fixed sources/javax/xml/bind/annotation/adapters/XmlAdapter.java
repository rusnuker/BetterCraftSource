// 
// Decompiled by Procyon v0.6.0
// 

package javax.xml.bind.annotation.adapters;

public abstract class XmlAdapter<ValueType, BoundType>
{
    protected XmlAdapter() {
    }
    
    public abstract BoundType unmarshal(final ValueType p0) throws Exception;
    
    public abstract ValueType marshal(final BoundType p0) throws Exception;
}
