// 
// Decompiled by Procyon v0.6.0
// 

package org.apache.commons.io.input;

public interface TailerListener
{
    void init(final Tailer p0);
    
    void fileNotFound();
    
    void fileRotated();
    
    void handle(final String p0);
    
    void handle(final Exception p0);
}
