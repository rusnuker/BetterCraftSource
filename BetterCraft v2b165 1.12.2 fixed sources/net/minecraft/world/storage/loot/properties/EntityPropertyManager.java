// 
// Decompiled by Procyon v0.6.0
// 

package net.minecraft.world.storage.loot.properties;

import com.google.common.collect.Maps;
import net.minecraft.util.ResourceLocation;
import java.util.Map;

public class EntityPropertyManager
{
    private static final Map<ResourceLocation, EntityProperty.Serializer<?>> NAME_TO_SERIALIZER_MAP;
    private static final Map<Class<? extends EntityProperty>, EntityProperty.Serializer<?>> CLASS_TO_SERIALIZER_MAP;
    
    static {
        NAME_TO_SERIALIZER_MAP = Maps.newHashMap();
        CLASS_TO_SERIALIZER_MAP = Maps.newHashMap();
        registerProperty((EntityProperty.Serializer<? extends EntityProperty>)new EntityOnFire.Serializer());
    }
    
    public static <T extends EntityProperty> void registerProperty(final EntityProperty.Serializer<? extends T> p_186644_0_) {
        final ResourceLocation resourcelocation = p_186644_0_.getName();
        final Class<T> oclass = (Class<T>)p_186644_0_.getPropertyClass();
        if (EntityPropertyManager.NAME_TO_SERIALIZER_MAP.containsKey(resourcelocation)) {
            throw new IllegalArgumentException("Can't re-register entity property name " + resourcelocation);
        }
        if (EntityPropertyManager.CLASS_TO_SERIALIZER_MAP.containsKey(oclass)) {
            throw new IllegalArgumentException("Can't re-register entity property class " + oclass.getName());
        }
        EntityPropertyManager.NAME_TO_SERIALIZER_MAP.put(resourcelocation, p_186644_0_);
        EntityPropertyManager.CLASS_TO_SERIALIZER_MAP.put(oclass, p_186644_0_);
    }
    
    public static EntityProperty.Serializer<?> getSerializerForName(final ResourceLocation p_186646_0_) {
        final EntityProperty.Serializer<?> serializer = EntityPropertyManager.NAME_TO_SERIALIZER_MAP.get(p_186646_0_);
        if (serializer == null) {
            throw new IllegalArgumentException("Unknown loot entity property '" + p_186646_0_ + "'");
        }
        return serializer;
    }
    
    public static <T extends EntityProperty> EntityProperty.Serializer<T> getSerializerFor(final T property) {
        final EntityProperty.Serializer<?> serializer = EntityPropertyManager.CLASS_TO_SERIALIZER_MAP.get(property.getClass());
        if (serializer == null) {
            throw new IllegalArgumentException("Unknown loot entity property " + property);
        }
        return (EntityProperty.Serializer<T>)serializer;
    }
}
