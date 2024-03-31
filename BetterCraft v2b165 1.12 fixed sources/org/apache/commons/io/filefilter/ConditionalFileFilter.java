// 
// Decompiled by Procyon v0.6.0
// 

package org.apache.commons.io.filefilter;

import java.util.List;

public interface ConditionalFileFilter
{
    void addFileFilter(final IOFileFilter p0);
    
    List<IOFileFilter> getFileFilters();
    
    boolean removeFileFilter(final IOFileFilter p0);
    
    void setFileFilters(final List<IOFileFilter> p0);
}
