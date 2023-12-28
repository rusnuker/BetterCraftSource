// 
// Decompiled by Procyon v0.6.0
// 

package net.labymod.user.util;

import java.util.Map;

public interface UserResolvedCallback
{
    void resolvedCosmetics(final Map<Integer, CosmeticData> p0);
    
    void resolvedEnumRank(final EnumUserRank p0);
    
    void resolvedRankVisibility(final boolean p0);
    
    void complete();
}
