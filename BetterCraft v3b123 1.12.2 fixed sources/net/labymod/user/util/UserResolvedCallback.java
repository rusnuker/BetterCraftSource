// 
// Decompiled by Procyon v0.6.0
// 

package net.labymod.user.util;

import net.labymod.user.group.LabyGroup;
import net.labymod.user.cosmetic.util.CosmeticData;
import java.util.Map;

public interface UserResolvedCallback
{
    void resolvedCosmetics(final Map<Integer, CosmeticData> p0);
    
    void resolvedGroup(final LabyGroup p0);
    
    void complete();
    
    void resolvedDailyEmoteFlat(final boolean p0);
}
