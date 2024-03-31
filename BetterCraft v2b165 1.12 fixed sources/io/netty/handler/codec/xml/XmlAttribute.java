// 
// Decompiled by Procyon v0.6.0
// 

package io.netty.handler.codec.xml;

public class XmlAttribute
{
    private final String type;
    private final String name;
    private final String prefix;
    private final String namespace;
    private final String value;
    
    public XmlAttribute(final String type, final String name, final String prefix, final String namespace, final String value) {
        this.type = type;
        this.name = name;
        this.prefix = prefix;
        this.namespace = namespace;
        this.value = value;
    }
    
    public String type() {
        return this.type;
    }
    
    public String name() {
        return this.name;
    }
    
    public String prefix() {
        return this.prefix;
    }
    
    public String namespace() {
        return this.namespace;
    }
    
    public String value() {
        return this.value;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || this.getClass() != o.getClass()) {
            return false;
        }
        final XmlAttribute that = (XmlAttribute)o;
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
        Label_0111: {
            if (this.prefix != null) {
                if (this.prefix.equals(that.prefix)) {
                    break Label_0111;
                }
            }
            else if (that.prefix == null) {
                break Label_0111;
            }
            return false;
        }
        Label_0144: {
            if (this.type != null) {
                if (this.type.equals(that.type)) {
                    break Label_0144;
                }
            }
            else if (that.type == null) {
                break Label_0144;
            }
            return false;
        }
        if (this.value != null) {
            if (this.value.equals(that.value)) {
                return true;
            }
        }
        else if (that.value == null) {
            return true;
        }
        return false;
    }
    
    @Override
    public int hashCode() {
        int result = (this.type != null) ? this.type.hashCode() : 0;
        result = 31 * result + this.name.hashCode();
        result = 31 * result + ((this.prefix != null) ? this.prefix.hashCode() : 0);
        result = 31 * result + ((this.namespace != null) ? this.namespace.hashCode() : 0);
        result = 31 * result + ((this.value != null) ? this.value.hashCode() : 0);
        return result;
    }
    
    @Override
    public String toString() {
        return "XmlAttribute{type='" + this.type + '\'' + ", name='" + this.name + '\'' + ", prefix='" + this.prefix + '\'' + ", namespace='" + this.namespace + '\'' + ", value='" + this.value + '\'' + '}';
    }
}
