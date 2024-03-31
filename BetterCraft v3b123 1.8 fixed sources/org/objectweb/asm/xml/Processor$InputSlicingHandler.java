// 
// Decompiled by Procyon v0.6.0
// 

package org.objectweb.asm.xml;

import org.xml.sax.SAXException;
import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.helpers.DefaultHandler;

final class Processor$InputSlicingHandler extends DefaultHandler
{
    private String subdocumentRoot;
    private final ContentHandler rootHandler;
    private Processor$ContentHandlerFactory subdocumentHandlerFactory;
    private boolean subdocument;
    private ContentHandler subdocumentHandler;
    
    Processor$InputSlicingHandler(final String subdocumentRoot, final ContentHandler rootHandler, final Processor$ContentHandlerFactory subdocumentHandlerFactory) {
        this.subdocument = false;
        this.subdocumentRoot = subdocumentRoot;
        this.rootHandler = rootHandler;
        this.subdocumentHandlerFactory = subdocumentHandlerFactory;
    }
    
    public final void startElement(final String s, final String s2, final String s3, final Attributes attributes) throws SAXException {
        if (this.subdocument) {
            this.subdocumentHandler.startElement(s, s2, s3, attributes);
        }
        else if (s2.equals(this.subdocumentRoot)) {
            (this.subdocumentHandler = this.subdocumentHandlerFactory.createContentHandler()).startDocument();
            this.subdocumentHandler.startElement(s, s2, s3, attributes);
            this.subdocument = true;
        }
        else if (this.rootHandler != null) {
            this.rootHandler.startElement(s, s2, s3, attributes);
        }
    }
    
    public final void endElement(final String s, final String s2, final String s3) throws SAXException {
        if (this.subdocument) {
            this.subdocumentHandler.endElement(s, s2, s3);
            if (s2.equals(this.subdocumentRoot)) {
                this.subdocumentHandler.endDocument();
                this.subdocument = false;
            }
        }
        else if (this.rootHandler != null) {
            this.rootHandler.endElement(s, s2, s3);
        }
    }
    
    public final void startDocument() throws SAXException {
        if (this.rootHandler != null) {
            this.rootHandler.startDocument();
        }
    }
    
    public final void endDocument() throws SAXException {
        if (this.rootHandler != null) {
            this.rootHandler.endDocument();
        }
    }
    
    public final void characters(final char[] array, final int n, final int n2) throws SAXException {
        if (this.subdocument) {
            this.subdocumentHandler.characters(array, n, n2);
        }
        else if (this.rootHandler != null) {
            this.rootHandler.characters(array, n, n2);
        }
    }
}
