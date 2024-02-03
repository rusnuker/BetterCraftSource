// 
// Decompiled by Procyon v0.6.0
// 

package org.objectweb.asm.xml;

import org.xml.sax.ContentHandler;
import java.io.Writer;

final class Processor$SAXWriterFactory implements Processor$ContentHandlerFactory
{
    private final Writer w;
    private final boolean optimizeEmptyElements;
    
    Processor$SAXWriterFactory(final Writer w, final boolean optimizeEmptyElements) {
        this.w = w;
        this.optimizeEmptyElements = optimizeEmptyElements;
    }
    
    public final ContentHandler createContentHandler() {
        return new Processor$SAXWriter(this.w, this.optimizeEmptyElements);
    }
}
