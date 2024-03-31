// 
// Decompiled by Procyon v0.6.0
// 

package io.netty.handler.codec.xml;

public class XmlEntityReference
{
    private final String name;
    private final String text;
    
    public XmlEntityReference(final String name, final String text) {
        this.name = name;
        this.text = text;
    }
    
    public String name() {
        return this.name;
    }
    
    public String text() {
        return this.text;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || this.getClass() != o.getClass()) {
            return false;
        }
        final XmlEntityReference that = (XmlEntityReference)o;
        Label_0062: {
            if (this.name != null) {
                if (this.name.equals(that.name)) {
                    break Label_0062;
                }
            }
            else if (that.name == null) {
                break Label_0062;
            }
            return false;
        }
        if (this.text != null) {
            if (this.text.equals(that.text)) {
                return true;
            }
        }
        else if (that.text == null) {
            return true;
        }
        return false;
    }
    
    @Override
    public int hashCode() {
        int result = (this.name != null) ? this.name.hashCode() : 0;
        result = 31 * result + ((this.text != null) ? this.text.hashCode() : 0);
        return result;
    }
    
    @Override
    public String toString() {
        return "XmlEntityReference{name='" + this.name + '\'' + ", text='" + this.text + '\'' + '}';
    }
}
