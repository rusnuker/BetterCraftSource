// 
// Decompiled by Procyon v0.6.0
// 

package io.netty.handler.codec.xml;

public class XmlDocumentStart
{
    private final String encoding;
    private final String version;
    private final boolean standalone;
    private final String encodingScheme;
    
    public XmlDocumentStart(final String encoding, final String version, final boolean standalone, final String encodingScheme) {
        this.encoding = encoding;
        this.version = version;
        this.standalone = standalone;
        this.encodingScheme = encodingScheme;
    }
    
    public String encoding() {
        return this.encoding;
    }
    
    public String version() {
        return this.version;
    }
    
    public boolean standalone() {
        return this.standalone;
    }
    
    public String encodingScheme() {
        return this.encodingScheme;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || this.getClass() != o.getClass()) {
            return false;
        }
        final XmlDocumentStart that = (XmlDocumentStart)o;
        if (this.standalone != that.standalone) {
            return false;
        }
        Label_0075: {
            if (this.encoding != null) {
                if (this.encoding.equals(that.encoding)) {
                    break Label_0075;
                }
            }
            else if (that.encoding == null) {
                break Label_0075;
            }
            return false;
        }
        Label_0108: {
            if (this.encodingScheme != null) {
                if (this.encodingScheme.equals(that.encodingScheme)) {
                    break Label_0108;
                }
            }
            else if (that.encodingScheme == null) {
                break Label_0108;
            }
            return false;
        }
        if (this.version != null) {
            if (this.version.equals(that.version)) {
                return true;
            }
        }
        else if (that.version == null) {
            return true;
        }
        return false;
    }
    
    @Override
    public int hashCode() {
        int result = (this.encoding != null) ? this.encoding.hashCode() : 0;
        result = 31 * result + ((this.version != null) ? this.version.hashCode() : 0);
        result = 31 * result + (this.standalone ? 1 : 0);
        result = 31 * result + ((this.encodingScheme != null) ? this.encodingScheme.hashCode() : 0);
        return result;
    }
    
    @Override
    public String toString() {
        return "XmlDocumentStart{encoding='" + this.encoding + '\'' + ", version='" + this.version + '\'' + ", standalone=" + this.standalone + ", encodingScheme='" + this.encodingScheme + '\'' + '}';
    }
}
