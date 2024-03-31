// 
// Decompiled by Procyon v0.6.0
// 

package io.netty.handler.codec.xml;

public class XmlNamespace
{
    private final String prefix;
    private final String uri;
    
    public XmlNamespace(final String prefix, final String uri) {
        this.prefix = prefix;
        this.uri = uri;
    }
    
    public String prefix() {
        return this.prefix;
    }
    
    public String uri() {
        return this.uri;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || this.getClass() != o.getClass()) {
            return false;
        }
        final XmlNamespace that = (XmlNamespace)o;
        Label_0062: {
            if (this.prefix != null) {
                if (this.prefix.equals(that.prefix)) {
                    break Label_0062;
                }
            }
            else if (that.prefix == null) {
                break Label_0062;
            }
            return false;
        }
        if (this.uri != null) {
            if (this.uri.equals(that.uri)) {
                return true;
            }
        }
        else if (that.uri == null) {
            return true;
        }
        return false;
    }
    
    @Override
    public int hashCode() {
        int result = (this.prefix != null) ? this.prefix.hashCode() : 0;
        result = 31 * result + ((this.uri != null) ? this.uri.hashCode() : 0);
        return result;
    }
    
    @Override
    public String toString() {
        return "XmlNamespace{prefix='" + this.prefix + '\'' + ", uri='" + this.uri + '\'' + '}';
    }
}
