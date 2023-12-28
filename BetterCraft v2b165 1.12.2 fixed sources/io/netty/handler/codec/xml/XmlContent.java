// 
// Decompiled by Procyon v0.6.0
// 

package io.netty.handler.codec.xml;

public abstract class XmlContent
{
    private final String data;
    
    protected XmlContent(final String data) {
        this.data = data;
    }
    
    public String data() {
        return this.data;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || this.getClass() != o.getClass()) {
            return false;
        }
        final XmlContent that = (XmlContent)o;
        if (this.data != null) {
            if (this.data.equals(that.data)) {
                return true;
            }
        }
        else if (that.data == null) {
            return true;
        }
        return false;
    }
    
    @Override
    public int hashCode() {
        return (this.data != null) ? this.data.hashCode() : 0;
    }
    
    @Override
    public String toString() {
        return "XmlContent{data='" + this.data + '\'' + '}';
    }
}
