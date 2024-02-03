// 
// Decompiled by Procyon v0.6.0
// 

package org.apache.commons.compress.archivers;

import java.util.Date;

public interface ArchiveEntry
{
    public static final long SIZE_UNKNOWN = -1L;
    
    String getName();
    
    long getSize();
    
    boolean isDirectory();
    
    Date getLastModifiedDate();
}
