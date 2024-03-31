// 
// Decompiled by Procyon v0.6.0
// 

package wdl.api;

import java.util.List;

public interface IEntityAdder extends IWDLMod
{
    List<String> getModEntities();
    
    int getDefaultEntityTrackDistance(final String p0);
    
    String getEntityCategory(final String p0);
}
