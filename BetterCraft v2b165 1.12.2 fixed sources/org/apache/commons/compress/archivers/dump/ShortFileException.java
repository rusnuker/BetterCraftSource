// 
// Decompiled by Procyon v0.6.0
// 

package org.apache.commons.compress.archivers.dump;

public class ShortFileException extends DumpArchiveException
{
    private static final long serialVersionUID = 1L;
    
    public ShortFileException() {
        super("unexpected EOF");
    }
}
