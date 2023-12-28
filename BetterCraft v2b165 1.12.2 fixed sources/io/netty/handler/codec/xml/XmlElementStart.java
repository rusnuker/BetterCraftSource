// 
// Decompiled by Procyon v0.6.0
// 

package io.netty.handler.codec.xml;

import java.util.LinkedList;
import java.util.List;

public class XmlElementStart extends XmlElement
{
    private final List<XmlAttribute> attributes;
    
    public XmlElementStart(final String name, final String namespace, final String prefix) {
        super(name, namespace, prefix);
        this.attributes = new LinkedList<XmlAttribute>();
    }
    
    public List<XmlAttribute> attributes() {
        return this.attributes;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || this.getClass() != o.getClass()) {
            return false;
        }
        if (!super.equals(o)) {
            return false;
        }
        final XmlElementStart that = (XmlElementStart)o;
        if (this.attributes != null) {
            if (this.attributes.equals(that.attributes)) {
                return true;
            }
        }
        else if (that.attributes == null) {
            return true;
        }
        return false;
    }
    
    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + ((this.attributes != null) ? this.attributes.hashCode() : 0);
        return result;
    }
    
    @Override
    public String toString() {
        return "XmlElementStart{attributes=" + this.attributes + super.toString() + "} ";
    }
}
