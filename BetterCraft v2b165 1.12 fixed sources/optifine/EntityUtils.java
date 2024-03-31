// 
// Decompiled by Procyon v0.6.0
// 

package optifine;

import net.minecraft.util.ResourceLocation;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import java.util.HashMap;
import java.util.Map;

public class EntityUtils
{
    private static final Map<Class, Integer> mapIdByClass;
    private static final Map<String, Integer> mapIdByLocation;
    private static final Map<String, Integer> mapIdByName;
    
    static {
        mapIdByClass = new HashMap<Class, Integer>();
        mapIdByLocation = new HashMap<String, Integer>();
        mapIdByName = new HashMap<String, Integer>();
        for (int i = 0; i < 1000; ++i) {
            final Class oclass = EntityList.getClassFromID(i);
            if (oclass != null) {
                final ResourceLocation resourcelocation = EntityList.func_191306_a(oclass);
                if (resourcelocation != null) {
                    final String s = resourcelocation.toString();
                    final String s2 = EntityList.func_191302_a(resourcelocation);
                    if (s2 != null) {
                        if (EntityUtils.mapIdByClass.containsKey(oclass)) {
                            Config.warn("Duplicate entity class: " + oclass + ", id1: " + EntityUtils.mapIdByClass.get(oclass) + ", id2: " + i);
                        }
                        if (EntityUtils.mapIdByLocation.containsKey(s)) {
                            Config.warn("Duplicate entity location: " + s + ", id1: " + EntityUtils.mapIdByLocation.get(s) + ", id2: " + i);
                        }
                        if (EntityUtils.mapIdByName.containsKey(s)) {
                            Config.warn("Duplicate entity name: " + s2 + ", id1: " + EntityUtils.mapIdByName.get(s2) + ", id2: " + i);
                        }
                        EntityUtils.mapIdByClass.put(oclass, i);
                        EntityUtils.mapIdByLocation.put(s, i);
                        EntityUtils.mapIdByName.put(s2, i);
                    }
                }
            }
        }
    }
    
    public static int getEntityIdByClass(final Entity p_getEntityIdByClass_0_) {
        return (p_getEntityIdByClass_0_ == null) ? -1 : getEntityIdByClass(p_getEntityIdByClass_0_.getClass());
    }
    
    public static int getEntityIdByClass(final Class p_getEntityIdByClass_0_) {
        final Integer integer = EntityUtils.mapIdByClass.get(p_getEntityIdByClass_0_);
        return (integer == null) ? -1 : integer;
    }
    
    public static int getEntityIdByLocation(final String p_getEntityIdByLocation_0_) {
        final Integer integer = EntityUtils.mapIdByLocation.get(p_getEntityIdByLocation_0_);
        return (integer == null) ? -1 : integer;
    }
    
    public static int getEntityIdByName(final String p_getEntityIdByName_0_) {
        final Integer integer = EntityUtils.mapIdByName.get(p_getEntityIdByName_0_);
        return (integer == null) ? -1 : integer;
    }
}
