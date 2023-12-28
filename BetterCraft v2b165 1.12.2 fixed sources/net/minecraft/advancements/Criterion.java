// 
// Decompiled by Procyon v0.6.0
// 

package net.minecraft.advancements;

import javax.annotation.Nullable;
import java.util.Iterator;
import com.google.gson.JsonElement;
import com.google.common.collect.Maps;
import java.util.Map;
import com.google.gson.JsonSyntaxException;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.JsonUtils;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import net.minecraft.network.PacketBuffer;

public class Criterion
{
    private final ICriterionInstance field_192147_a;
    
    public Criterion(final ICriterionInstance p_i47470_1_) {
        this.field_192147_a = p_i47470_1_;
    }
    
    public Criterion() {
        this.field_192147_a = null;
    }
    
    public void func_192140_a(final PacketBuffer p_192140_1_) {
    }
    
    public static Criterion func_192145_a(final JsonObject p_192145_0_, final JsonDeserializationContext p_192145_1_) {
        final ResourceLocation resourcelocation = new ResourceLocation(JsonUtils.getString(p_192145_0_, "trigger"));
        final ICriterionTrigger<?> icriteriontrigger = CriteriaTriggers.func_192119_a(resourcelocation);
        if (icriteriontrigger == null) {
            throw new JsonSyntaxException("Invalid criterion trigger: " + resourcelocation);
        }
        final ICriterionInstance icriterioninstance = (ICriterionInstance)icriteriontrigger.func_192166_a(JsonUtils.getJsonObject(p_192145_0_, "conditions", new JsonObject()), p_192145_1_);
        return new Criterion(icriterioninstance);
    }
    
    public static Criterion func_192146_b(final PacketBuffer p_192146_0_) {
        return new Criterion();
    }
    
    public static Map<String, Criterion> func_192144_b(final JsonObject p_192144_0_, final JsonDeserializationContext p_192144_1_) {
        final Map<String, Criterion> map = (Map<String, Criterion>)Maps.newHashMap();
        for (final Map.Entry<String, JsonElement> entry : p_192144_0_.entrySet()) {
            map.put(entry.getKey(), func_192145_a(JsonUtils.getJsonObject(entry.getValue(), "criterion"), p_192144_1_));
        }
        return map;
    }
    
    public static Map<String, Criterion> func_192142_c(final PacketBuffer p_192142_0_) {
        final Map<String, Criterion> map = (Map<String, Criterion>)Maps.newHashMap();
        for (int i = p_192142_0_.readVarIntFromBuffer(), j = 0; j < i; ++j) {
            map.put(p_192142_0_.readStringFromBuffer(32767), func_192146_b(p_192142_0_));
        }
        return map;
    }
    
    public static void func_192141_a(final Map<String, Criterion> p_192141_0_, final PacketBuffer p_192141_1_) {
        p_192141_1_.writeVarIntToBuffer(p_192141_0_.size());
        for (final Map.Entry<String, Criterion> entry : p_192141_0_.entrySet()) {
            p_192141_1_.writeString(entry.getKey());
            entry.getValue().func_192140_a(p_192141_1_);
        }
    }
    
    @Nullable
    public ICriterionInstance func_192143_a() {
        return this.field_192147_a;
    }
}
