// 
// Decompiled by Procyon v0.6.0
// 

package org.objectweb.asm.xml;

import java.io.IOException;
import org.xml.sax.SAXException;
import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.helpers.DefaultHandler;

final class Processor$OutputSlicingHandler extends DefaultHandler
{
    private final String subdocumentRoot;
    private Processor$ContentHandlerFactory subdocumentHandlerFactory;
    private final Processor$EntryElement entryElement;
    private boolean isXml;
    private boolean subdocument;
    private ContentHandler subdocumentHandler;
    
    Processor$OutputSlicingHandler(final Processor$ContentHandlerFactory subdocumentHandlerFactory, final Processor$EntryElement entryElement, final boolean isXml) {
        this.subdocument = false;
        this.subdocumentRoot = "class";
        this.subdocumentHandlerFactory = subdocumentHandlerFactory;
        this.entryElement = entryElement;
        this.isXml = isXml;
    }
    
    public final void startElement(final String s, final String s2, final String s3, final Attributes attributes) throws SAXException {
        if (this.subdocument) {
            this.subdocumentHandler.startElement(s, s2, s3, attributes);
        }
        else if (s2.equals(this.subdocumentRoot)) {
            final String value = attributes.getValue("name");
            if (value == null || value.length() == 0) {
                throw new SAXException("Class element without name attribute.");
            }
            try {
                this.entryElement.openEntry(this.isXml ? (value + ".class.xml") : (value + ".class"));
            }
            catch (final IOException ex) {
                throw new SAXException(ex.toString(), ex);
            }
            (this.subdocumentHandler = this.subdocumentHandlerFactory.createContentHandler()).startDocument();
            this.subdocumentHandler.startElement(s, s2, s3, attributes);
            this.subdocument = true;
        }
    }
    
    public final void endElement(final String s, final String s2, final String s3) throws SAXException {
        if (this.subdocument) {
            this.subdocumentHandler.endElement(s, s2, s3);
            if (s2.equals(this.subdocumentRoot)) {
                this.subdocumentHandler.endDocument();
                this.subdocument = false;
                try {
                    this.entryElement.closeEntry();
                }
                catch (final IOException ex) {
                    throw new SAXException(ex.toString(), ex);
                }
            }
        }
    }
    
    public final void startDocument() throws SAXException {
    }
    
    public final void endDocument() throws SAXException {
    }
    
    public final void characters(final char[] array, final int n, final int n2) throws SAXException {
        if (this.subdocument) {
            this.subdocumentHandler.characters(array, n, n2);
        }
    }
}
