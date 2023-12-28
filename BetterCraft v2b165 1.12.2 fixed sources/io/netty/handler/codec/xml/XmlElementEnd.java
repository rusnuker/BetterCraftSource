// 
// Decompiled by Procyon v0.6.0
// 

package io.netty.handler.codec.xml;

public class XmlElementEnd extends XmlElement
{
    public XmlElementEnd(final String name, final String namespace, final String prefix) {
        super(name, namespace, prefix);
    }
    
    @Override
    public String toString() {
        return "XmlElementStart{" + super.toString() + "} ";
    }
}
