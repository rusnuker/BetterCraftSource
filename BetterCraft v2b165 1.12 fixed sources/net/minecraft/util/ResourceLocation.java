// 
// Decompiled by Procyon v0.6.0
// 

package net.minecraft.util;

import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonParseException;
import com.google.gson.JsonDeserializationContext;
import java.lang.reflect.Type;
import com.google.gson.JsonElement;
import com.google.gson.JsonSerializer;
import com.google.gson.JsonDeserializer;
import org.apache.commons.lang3.Validate;
import java.util.Locale;
import org.apache.commons.lang3.StringUtils;

public class ResourceLocation implements Comparable<ResourceLocation>
{
    protected final String resourceDomain;
    protected final String resourcePath;
    
    protected ResourceLocation(final int unused, final String... resourceName) {
        this.resourceDomain = (StringUtils.isEmpty(resourceName[0]) ? "minecraft" : resourceName[0].toLowerCase(Locale.ROOT));
        Validate.notNull(this.resourcePath = resourceName[1].toLowerCase(Locale.ROOT));
    }
    
    public ResourceLocation(final String resourceName) {
        this(0, splitObjectName(resourceName));
    }
    
    public ResourceLocation(final String resourceDomainIn, final String resourcePathIn) {
        this(0, new String[] { resourceDomainIn, resourcePathIn });
    }
    
    protected static String[] splitObjectName(final String toSplit) {
        final String[] astring = { "minecraft", toSplit };
        final int i = toSplit.indexOf(58);
        if (i >= 0) {
            astring[1] = toSplit.substring(i + 1, toSplit.length());
            if (i > 1) {
                astring[0] = toSplit.substring(0, i);
            }
        }
        return astring;
    }
    
    public String getResourcePath() {
        return this.resourcePath;
    }
    
    public String getResourceDomain() {
        return this.resourceDomain;
    }
    
    @Override
    public String toString() {
        return String.valueOf(this.resourceDomain) + ':' + this.resourcePath;
    }
    
    @Override
    public boolean equals(final Object p_equals_1_) {
        if (this == p_equals_1_) {
            return true;
        }
        if (!(p_equals_1_ instanceof ResourceLocation)) {
            return false;
        }
        final ResourceLocation resourcelocation = (ResourceLocation)p_equals_1_;
        return this.resourceDomain.equals(resourcelocation.resourceDomain) && this.resourcePath.equals(resourcelocation.resourcePath);
    }
    
    @Override
    public int hashCode() {
        return 31 * this.resourceDomain.hashCode() + this.resourcePath.hashCode();
    }
    
    @Override
    public int compareTo(final ResourceLocation p_compareTo_1_) {
        int i = this.resourceDomain.compareTo(p_compareTo_1_.resourceDomain);
        if (i == 0) {
            i = this.resourcePath.compareTo(p_compareTo_1_.resourcePath);
        }
        return i;
    }
    
    public static class Serializer implements JsonDeserializer<ResourceLocation>, JsonSerializer<ResourceLocation>
    {
        @Override
        public ResourceLocation deserialize(final JsonElement p_deserialize_1_, final Type p_deserialize_2_, final JsonDeserializationContext p_deserialize_3_) throws JsonParseException {
            return new ResourceLocation(JsonUtils.getString(p_deserialize_1_, "location"));
        }
        
        @Override
        public JsonElement serialize(final ResourceLocation p_serialize_1_, final Type p_serialize_2_, final JsonSerializationContext p_serialize_3_) {
            return new JsonPrimitive(p_serialize_1_.toString());
        }
    }
}
