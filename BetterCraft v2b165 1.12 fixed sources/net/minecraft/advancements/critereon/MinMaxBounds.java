// 
// Decompiled by Procyon v0.6.0
// 

package net.minecraft.advancements.critereon;

import com.google.gson.JsonObject;
import net.minecraft.util.JsonUtils;
import com.google.gson.JsonElement;
import javax.annotation.Nullable;

public class MinMaxBounds
{
    public static final MinMaxBounds field_192516_a;
    private final Float field_192517_b;
    private final Float field_192518_c;
    
    static {
        field_192516_a = new MinMaxBounds(null, null);
    }
    
    public MinMaxBounds(@Nullable final Float p_i47431_1_, @Nullable final Float p_i47431_2_) {
        this.field_192517_b = p_i47431_1_;
        this.field_192518_c = p_i47431_2_;
    }
    
    public boolean func_192514_a(final float p_192514_1_) {
        return (this.field_192517_b == null || this.field_192517_b <= p_192514_1_) && (this.field_192518_c == null || this.field_192518_c >= p_192514_1_);
    }
    
    public boolean func_192513_a(final double p_192513_1_) {
        return (this.field_192517_b == null || this.field_192517_b * this.field_192517_b <= p_192513_1_) && (this.field_192518_c == null || this.field_192518_c * this.field_192518_c >= p_192513_1_);
    }
    
    public static MinMaxBounds func_192515_a(@Nullable final JsonElement p_192515_0_) {
        if (p_192515_0_ == null || p_192515_0_.isJsonNull()) {
            return MinMaxBounds.field_192516_a;
        }
        if (JsonUtils.isNumber(p_192515_0_)) {
            final float f2 = JsonUtils.getFloat(p_192515_0_, "value");
            return new MinMaxBounds(f2, f2);
        }
        final JsonObject jsonobject = JsonUtils.getJsonObject(p_192515_0_, "value");
        final Float f3 = jsonobject.has("min") ? Float.valueOf(JsonUtils.getFloat(jsonobject, "min")) : null;
        final Float f4 = jsonobject.has("max") ? Float.valueOf(JsonUtils.getFloat(jsonobject, "max")) : null;
        return new MinMaxBounds(f3, f4);
    }
}
