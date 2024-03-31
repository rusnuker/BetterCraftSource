// 
// Decompiled by Procyon v0.6.0
// 

package net.minecraft.advancements.critereon;

import com.google.gson.JsonObject;
import net.minecraft.util.JsonUtils;
import javax.annotation.Nullable;
import com.google.gson.JsonElement;
import net.minecraft.util.math.MathHelper;

public class DistancePredicate
{
    public static final DistancePredicate field_193423_a;
    private final MinMaxBounds field_193424_b;
    private final MinMaxBounds field_193425_c;
    private final MinMaxBounds field_193426_d;
    private final MinMaxBounds field_193427_e;
    private final MinMaxBounds field_193428_f;
    
    static {
        field_193423_a = new DistancePredicate(MinMaxBounds.field_192516_a, MinMaxBounds.field_192516_a, MinMaxBounds.field_192516_a, MinMaxBounds.field_192516_a, MinMaxBounds.field_192516_a);
    }
    
    public DistancePredicate(final MinMaxBounds p_i47542_1_, final MinMaxBounds p_i47542_2_, final MinMaxBounds p_i47542_3_, final MinMaxBounds p_i47542_4_, final MinMaxBounds p_i47542_5_) {
        this.field_193424_b = p_i47542_1_;
        this.field_193425_c = p_i47542_2_;
        this.field_193426_d = p_i47542_3_;
        this.field_193427_e = p_i47542_4_;
        this.field_193428_f = p_i47542_5_;
    }
    
    public boolean func_193422_a(final double p_193422_1_, final double p_193422_3_, final double p_193422_5_, final double p_193422_7_, final double p_193422_9_, final double p_193422_11_) {
        final float f = (float)(p_193422_1_ - p_193422_7_);
        final float f2 = (float)(p_193422_3_ - p_193422_9_);
        final float f3 = (float)(p_193422_5_ - p_193422_11_);
        return this.field_193424_b.func_192514_a(MathHelper.abs(f)) && this.field_193425_c.func_192514_a(MathHelper.abs(f2)) && this.field_193426_d.func_192514_a(MathHelper.abs(f3)) && this.field_193427_e.func_192513_a(f * f + f3 * f3) && this.field_193428_f.func_192513_a(f * f + f2 * f2 + f3 * f3);
    }
    
    public static DistancePredicate func_193421_a(@Nullable final JsonElement p_193421_0_) {
        if (p_193421_0_ != null && !p_193421_0_.isJsonNull()) {
            final JsonObject jsonobject = JsonUtils.getJsonObject(p_193421_0_, "distance");
            final MinMaxBounds minmaxbounds = MinMaxBounds.func_192515_a(jsonobject.get("x"));
            final MinMaxBounds minmaxbounds2 = MinMaxBounds.func_192515_a(jsonobject.get("y"));
            final MinMaxBounds minmaxbounds3 = MinMaxBounds.func_192515_a(jsonobject.get("z"));
            final MinMaxBounds minmaxbounds4 = MinMaxBounds.func_192515_a(jsonobject.get("horizontal"));
            final MinMaxBounds minmaxbounds5 = MinMaxBounds.func_192515_a(jsonobject.get("absolute"));
            return new DistancePredicate(minmaxbounds, minmaxbounds2, minmaxbounds3, minmaxbounds4, minmaxbounds5);
        }
        return DistancePredicate.field_193423_a;
    }
}
