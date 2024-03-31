// 
// Decompiled by Procyon v0.6.0
// 

package org.objectweb.asm.xml;

import java.io.IOException;
import org.xml.sax.SAXException;
import org.xml.sax.Attributes;
import java.io.Writer;
import org.xml.sax.ext.LexicalHandler;
import org.xml.sax.helpers.DefaultHandler;

final class Processor$SAXWriter extends DefaultHandler implements LexicalHandler
{
    private static final char[] OFF;
    private Writer w;
    private final boolean optimizeEmptyElements;
    private boolean openElement;
    private int ident;
    
    Processor$SAXWriter(final Writer w, final boolean optimizeEmptyElements) {
        this.openElement = false;
        this.ident = 0;
        this.w = w;
        this.optimizeEmptyElements = optimizeEmptyElements;
    }
    
    public final void startElement(final String s, final String s2, final String s3, final Attributes attributes) throws SAXException {
        try {
            this.closeElement();
            this.writeIdent();
            this.w.write('<' + s3);
            if (attributes != null && attributes.getLength() > 0) {
                this.writeAttributes(attributes);
            }
            if (this.optimizeEmptyElements) {
                this.openElement = true;
            }
            else {
                this.w.write(">\n");
            }
            this.ident += 2;
        }
        catch (final IOException ex) {
            throw new SAXException(ex);
        }
    }
    
    public final void endElement(final String s, final String s2, final String s3) throws SAXException {
        this.ident -= 2;
        try {
            if (this.openElement) {
                this.w.write("/>\n");
                this.openElement = false;
            }
            else {
                this.writeIdent();
                this.w.write("</" + s3 + ">\n");
            }
        }
        catch (final IOException ex) {
            throw new SAXException(ex);
        }
    }
    
    public final void endDocument() throws SAXException {
        try {
            this.w.flush();
        }
        catch (final IOException ex) {
            throw new SAXException(ex);
        }
    }
    
    public final void comment(final char[] array, final int n, final int n2) throws SAXException {
        try {
            this.closeElement();
            this.writeIdent();
            this.w.write("<!-- ");
            this.w.write(array, n, n2);
            this.w.write(" -->\n");
        }
        catch (final IOException ex) {
            throw new SAXException(ex);
        }
    }
    
    public final void startDTD(final String s, final String s2, final String s3) throws SAXException {
    }
    
    public final void endDTD() throws SAXException {
    }
    
    public final void startEntity(final String s) throws SAXException {
    }
    
    public final void endEntity(final String s) throws SAXException {
    }
    
    public final void startCDATA() throws SAXException {
    }
    
    public final void endCDATA() throws SAXException {
    }
    
    private final void writeAttributes(final Attributes attributes) throws IOException {
        final StringBuffer sb = new StringBuffer();
        for (int length = attributes.getLength(), i = 0; i < length; ++i) {
            sb.append(' ').append(attributes.getLocalName(i)).append("=\"").append(esc(attributes.getValue(i))).append('\"');
        }
        this.w.write(sb.toString());
    }
    
    private static final String esc(final String s) {
        final StringBuffer sb = new StringBuffer(s.length());
        for (int i = 0; i < s.length(); ++i) {
            final char char1 = s.charAt(i);
            switch (char1) {
                case 38: {
                    sb.append("&amp;");
                    break;
                }
                case 60: {
                    sb.append("&lt;");
                    break;
                }
                case 62: {
                    sb.append("&gt;");
                    break;
                }
                case 34: {
                    sb.append("&quot;");
                    break;
                }
                default: {
                    if (char1 > '\u007f') {
                        sb.append("&#").append(Integer.toString(char1)).append(';');
                        break;
                    }
                    sb.append(char1);
                    break;
                }
            }
        }
        return sb.toString();
    }
    
    private final void writeIdent() throws IOException {
        int i = this.ident;
        while (i > 0) {
            if (i > Processor$SAXWriter.OFF.length) {
                this.w.write(Processor$SAXWriter.OFF);
                i -= Processor$SAXWriter.OFF.length;
            }
            else {
                this.w.write(Processor$SAXWriter.OFF, 0, i);
                i = 0;
            }
        }
    }
    
    private final void closeElement() throws IOException {
        if (this.openElement) {
            this.w.write(">\n");
        }
        this.openElement = false;
    }
    
    static {
        _clinit_();
        OFF = "                                                                                                        ".toCharArray();
    }
    
    static /* synthetic */ void _clinit_() {
    }
}
