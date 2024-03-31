// 
// Decompiled by Procyon v0.6.0
// 

package wdl.api;

import net.minecraft.entity.Entity;
import com.google.common.collect.Multimap;

public interface ISpecialEntityHandler extends IWDLMod
{
    Multimap<String, String> getSpecialEntities();
    
    String getSpecialEntityName(final Entity p0);
    
    String getSpecialEntityCategory(final String p0);
    
    int getSpecialEntityTrackDistance(final String p0);
}
