// 
// Decompiled by Procyon v0.6.0
// 

package org.objectweb.asm.xml;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.ContentHandler;

public class SAXAdapter
{
    private final ContentHandler h;
    
    protected SAXAdapter(final ContentHandler h) {
        this.h = h;
    }
    
    protected ContentHandler getContentHandler() {
        return this.h;
    }
    
    protected void addDocumentStart() {
        try {
            this.h.startDocument();
        }
        catch (final SAXException ex) {
            throw new RuntimeException(ex.getMessage(), ex.getException());
        }
    }
    
    protected void addDocumentEnd() {
        try {
            this.h.endDocument();
        }
        catch (final SAXException ex) {
            throw new RuntimeException(ex.getMessage(), ex.getException());
        }
    }
    
    protected final void addStart(final String s, final Attributes attributes) {
        try {
            this.h.startElement("", s, s, attributes);
        }
        catch (final SAXException ex) {
            throw new RuntimeException(ex.getMessage(), ex.getException());
        }
    }
    
    protected final void addEnd(final String s) {
        try {
            this.h.endElement("", s, s);
        }
        catch (final SAXException ex) {
            throw new RuntimeException(ex.getMessage(), ex.getException());
        }
    }
    
    protected final void addElement(final String s, final Attributes attributes) {
        this.addStart(s, attributes);
        this.addEnd(s);
    }
}
