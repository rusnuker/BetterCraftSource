// 
// Decompiled by Procyon v0.6.0
// 

package javax.xml.bind.util;

import org.xml.sax.SAXParseException;
import org.xml.sax.SAXException;
import org.xml.sax.InputSource;
import org.xml.sax.ContentHandler;
import org.xml.sax.SAXNotRecognizedException;
import org.xml.sax.helpers.XMLFilterImpl;
import org.xml.sax.ErrorHandler;
import org.xml.sax.XMLFilter;
import org.xml.sax.DTDHandler;
import org.xml.sax.EntityResolver;
import org.xml.sax.ext.LexicalHandler;
import javax.xml.bind.JAXBException;
import javax.xml.bind.JAXBContext;
import org.xml.sax.XMLReader;
import javax.xml.bind.Marshaller;
import javax.xml.transform.sax.SAXSource;

public class JAXBSource extends SAXSource
{
    private final Marshaller marshaller;
    private final Object contentObject;
    private final XMLReader pseudoParser;
    
    public JAXBSource(final JAXBContext context, final Object contentObject) throws JAXBException {
        this((context == null) ? assertionFailed(Messages.format("JAXBSource.NullContext")) : context.createMarshaller(), (contentObject == null) ? assertionFailed(Messages.format("JAXBSource.NullContent")) : contentObject);
    }
    
    public JAXBSource(final Marshaller marshaller, final Object contentObject) throws JAXBException {
        this.pseudoParser = new XMLReader() {
            private LexicalHandler lexicalHandler;
            private EntityResolver entityResolver;
            private DTDHandler dtdHandler;
            private XMLFilter repeater = new XMLFilterImpl();
            private ErrorHandler errorHandler;
            
            @Override
            public boolean getFeature(final String name) throws SAXNotRecognizedException {
                if (name.equals("http://xml.org/sax/features/namespaces")) {
                    return true;
                }
                if (name.equals("http://xml.org/sax/features/namespace-prefixes")) {
                    return false;
                }
                throw new SAXNotRecognizedException(name);
            }
            
            @Override
            public void setFeature(final String name, final boolean value) throws SAXNotRecognizedException {
                if (name.equals("http://xml.org/sax/features/namespaces") && value) {
                    return;
                }
                if (name.equals("http://xml.org/sax/features/namespace-prefixes") && !value) {
                    return;
                }
                throw new SAXNotRecognizedException(name);
            }
            
            @Override
            public Object getProperty(final String name) throws SAXNotRecognizedException {
                if ("http://xml.org/sax/properties/lexical-handler".equals(name)) {
                    return this.lexicalHandler;
                }
                throw new SAXNotRecognizedException(name);
            }
            
            @Override
            public void setProperty(final String name, final Object value) throws SAXNotRecognizedException {
                if ("http://xml.org/sax/properties/lexical-handler".equals(name)) {
                    this.lexicalHandler = (LexicalHandler)value;
                    return;
                }
                throw new SAXNotRecognizedException(name);
            }
            
            @Override
            public void setEntityResolver(final EntityResolver resolver) {
                this.entityResolver = resolver;
            }
            
            @Override
            public EntityResolver getEntityResolver() {
                return this.entityResolver;
            }
            
            @Override
            public void setDTDHandler(final DTDHandler handler) {
                this.dtdHandler = handler;
            }
            
            @Override
            public DTDHandler getDTDHandler() {
                return this.dtdHandler;
            }
            
            @Override
            public void setContentHandler(final ContentHandler handler) {
                this.repeater.setContentHandler(handler);
            }
            
            @Override
            public ContentHandler getContentHandler() {
                return this.repeater.getContentHandler();
            }
            
            @Override
            public void setErrorHandler(final ErrorHandler handler) {
                this.errorHandler = handler;
            }
            
            @Override
            public ErrorHandler getErrorHandler() {
                return this.errorHandler;
            }
            
            @Override
            public void parse(final InputSource input) throws SAXException {
                this.parse();
            }
            
            @Override
            public void parse(final String systemId) throws SAXException {
                this.parse();
            }
            
            public void parse() throws SAXException {
                try {
                    JAXBSource.this.marshaller.marshal(JAXBSource.this.contentObject, (ContentHandler)this.repeater);
                }
                catch (final JAXBException e) {
                    final SAXParseException se = new SAXParseException(e.getMessage(), null, null, -1, -1, e);
                    if (this.errorHandler != null) {
                        this.errorHandler.fatalError(se);
                    }
                    throw se;
                }
            }
        };
        if (marshaller == null) {
            throw new JAXBException(Messages.format("JAXBSource.NullMarshaller"));
        }
        if (contentObject == null) {
            throw new JAXBException(Messages.format("JAXBSource.NullContent"));
        }
        this.marshaller = marshaller;
        this.contentObject = contentObject;
        super.setXMLReader(this.pseudoParser);
        super.setInputSource(new InputSource());
    }
    
    private static Marshaller assertionFailed(final String message) throws JAXBException {
        throw new JAXBException(message);
    }
}
