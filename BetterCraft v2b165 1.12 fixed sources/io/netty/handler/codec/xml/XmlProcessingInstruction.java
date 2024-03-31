// 
// Decompiled by Procyon v0.6.0
// 

package io.netty.handler.codec.xml;

public class XmlProcessingInstruction
{
    private final String data;
    private final String target;
    
    public XmlProcessingInstruction(final String data, final String target) {
        this.data = data;
        this.target = target;
    }
    
    public String data() {
        return this.data;
    }
    
    public String target() {
        return this.target;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || this.getClass() != o.getClass()) {
            return false;
        }
        final XmlProcessingInstruction that = (XmlProcessingInstruction)o;
        Label_0062: {
            if (this.data != null) {
                if (this.data.equals(that.data)) {
                    break Label_0062;
                }
            }
            else if (that.data == null) {
                break Label_0062;
            }
            return false;
        }
        if (this.target != null) {
            if (this.target.equals(that.target)) {
                return true;
            }
        }
        else if (that.target == null) {
            return true;
        }
        return false;
    }
    
    @Override
    public int hashCode() {
        int result = (this.data != null) ? this.data.hashCode() : 0;
        result = 31 * result + ((this.target != null) ? this.target.hashCode() : 0);
        return result;
    }
    
    @Override
    public String toString() {
        return "XmlProcessingInstruction{data='" + this.data + '\'' + ", target='" + this.target + '\'' + '}';
    }
}
