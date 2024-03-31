// 
// Decompiled by Procyon v0.6.0
// 

package org.objectweb.asm.xml;

import javax.xml.transform.sax.TransformerHandler;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.Result;
import javax.xml.transform.sax.SAXResult;
import org.xml.sax.ContentHandler;
import javax.xml.transform.Templates;
import javax.xml.transform.sax.SAXTransformerFactory;

final class Processor$TransformerHandlerFactory implements Processor$ContentHandlerFactory
{
    private SAXTransformerFactory saxtf;
    private final Templates templates;
    private ContentHandler outputHandler;
    
    Processor$TransformerHandlerFactory(final SAXTransformerFactory saxtf, final Templates templates, final ContentHandler outputHandler) {
        this.saxtf = saxtf;
        this.templates = templates;
        this.outputHandler = outputHandler;
    }
    
    public final ContentHandler createContentHandler() {
        try {
            final TransformerHandler transformerHandler = this.saxtf.newTransformerHandler(this.templates);
            transformerHandler.setResult(new SAXResult(this.outputHandler));
            return transformerHandler;
        }
        catch (final TransformerConfigurationException ex) {
            throw new RuntimeException(ex.toString());
        }
    }
}
