// 
// Decompiled by Procyon v0.6.0
// 

package org.objectweb.asm.xml;

import org.xml.sax.ContentHandler;

final class Processor$SubdocumentHandlerFactory implements Processor$ContentHandlerFactory
{
    private final ContentHandler subdocumentHandler;
    
    Processor$SubdocumentHandlerFactory(final ContentHandler subdocumentHandler) {
        this.subdocumentHandler = subdocumentHandler;
    }
    
    public final ContentHandler createContentHandler() {
        return this.subdocumentHandler;
    }
}
