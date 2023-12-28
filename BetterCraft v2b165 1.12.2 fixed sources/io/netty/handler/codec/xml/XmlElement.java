// 
// Decompiled by Procyon v0.6.0
// 

package io.netty.handler.codec.xml;

import java.util.LinkedList;
import java.util.List;

public abstract class XmlElement
{
    private final String name;
    private final String namespace;
    private final String prefix;
    private final List<XmlNamespace> namespaces;
    
    protected XmlElement(final String name, final String namespace, final String prefix) {
        this.namespaces = new LinkedList<XmlNamespace>();
        this.name = name;
        this.namespace = namespace;
        this.prefix = prefix;
    }
    
    public String name() {
        return this.name;
    }
    
    public String namespace() {
        return this.namespace;
    }
    
    public String prefix() {
        return this.prefix;
    }
    
    public List<XmlNamespace> namespaces() {
        return this.namespaces;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || this.getClass() != o.getClass()) {
            return false;
        }
        final XmlElement that = (XmlElement)o;
        if (!this.name.equals(that.name)) {
            return false;
        }
        Label_0078: {
            if (this.namespace != null) {
                if (this.namespace.equals(that.namespace)) {
                    break Label_0078;
                }
            }
            else if (that.namespace == null) {
                break Label_0078;
            }
            return false;
        }
        Label_0113: {
            if (this.namespaces != null) {
                if (this.namespaces.equals(that.namespaces)) {
                    break Label_0113;
                }
            }
            else if (that.namespaces == null) {
                break Label_0113;
            }
            return false;
        }
        if (this.prefix != null) {
            if (this.prefix.equals(that.prefix)) {
                return true;
            }
        }
        else if (that.prefix == null) {
            return true;
        }
        return false;
    }
    
    @Override
    public int hashCode() {
        int result = this.name.hashCode();
        result = 31 * result + ((this.namespace != null) ? this.namespace.hashCode() : 0);
        result = 31 * result + ((this.prefix != null) ? this.prefix.hashCode() : 0);
        result = 31 * result + ((this.namespaces != null) ? this.namespaces.hashCode() : 0);
        return result;
    }
    
    @Override
    public String toString() {
        return ", name='" + this.name + '\'' + ", namespace='" + this.namespace + '\'' + ", prefix='" + this.prefix + '\'' + ", namespaces=" + this.namespaces;
    }
}
