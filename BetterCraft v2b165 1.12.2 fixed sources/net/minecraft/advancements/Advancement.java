// 
// Decompiled by Procyon v0.6.0
// 

package net.minecraft.advancements;

import java.io.IOException;
import java.util.Iterator;
import org.apache.commons.lang3.ArrayUtils;
import com.google.gson.JsonArray;
import com.google.gson.JsonSyntaxException;
import net.minecraft.util.JsonUtils;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import net.minecraft.network.PacketBuffer;
import java.util.function.Function;
import java.util.Arrays;
import net.minecraft.util.text.event.HoverEvent;
import net.minecraft.util.text.TextComponentString;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Sets;
import javax.annotation.Nullable;
import net.minecraft.util.text.ITextComponent;
import java.util.Set;
import java.util.Map;
import net.minecraft.util.ResourceLocation;

public class Advancement
{
    private final Advancement field_192076_a;
    private final DisplayInfo field_192077_b;
    private final AdvancementRewards field_192078_c;
    private final ResourceLocation field_192079_d;
    private final Map<String, Criterion> field_192080_e;
    private final String[][] field_192081_f;
    private final Set<Advancement> field_192082_g;
    private final ITextComponent field_193125_h;
    
    public Advancement(final ResourceLocation p_i47472_1_, @Nullable final Advancement p_i47472_2_, @Nullable final DisplayInfo p_i47472_3_, final AdvancementRewards p_i47472_4_, final Map<String, Criterion> p_i47472_5_, final String[][] p_i47472_6_) {
        this.field_192082_g = (Set<Advancement>)Sets.newLinkedHashSet();
        this.field_192079_d = p_i47472_1_;
        this.field_192077_b = p_i47472_3_;
        this.field_192080_e = (Map<String, Criterion>)ImmutableMap.copyOf((Map<?, ?>)p_i47472_5_);
        this.field_192076_a = p_i47472_2_;
        this.field_192078_c = p_i47472_4_;
        this.field_192081_f = p_i47472_6_;
        if (p_i47472_2_ != null) {
            p_i47472_2_.func_192071_a(this);
        }
        if (p_i47472_3_ == null) {
            this.field_193125_h = new TextComponentString(p_i47472_1_.toString());
        }
        else {
            this.field_193125_h = new TextComponentString("[");
            this.field_193125_h.getStyle().setColor(p_i47472_3_.func_192291_d().func_193229_c());
            final ITextComponent itextcomponent = p_i47472_3_.func_192297_a().createCopy();
            final ITextComponent itextcomponent2 = new TextComponentString("");
            final ITextComponent itextcomponent3 = itextcomponent.createCopy();
            itextcomponent3.getStyle().setColor(p_i47472_3_.func_192291_d().func_193229_c());
            itextcomponent2.appendSibling(itextcomponent3);
            itextcomponent2.appendText("\n");
            itextcomponent2.appendSibling(p_i47472_3_.func_193222_b());
            itextcomponent.getStyle().setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, itextcomponent2));
            this.field_193125_h.appendSibling(itextcomponent);
            this.field_193125_h.appendText("]");
        }
    }
    
    public Builder func_192075_a() {
        return new Builder((this.field_192076_a == null) ? null : this.field_192076_a.func_192067_g(), this.field_192077_b, this.field_192078_c, this.field_192080_e, this.field_192081_f);
    }
    
    @Nullable
    public Advancement func_192070_b() {
        return this.field_192076_a;
    }
    
    @Nullable
    public DisplayInfo func_192068_c() {
        return this.field_192077_b;
    }
    
    public AdvancementRewards func_192072_d() {
        return this.field_192078_c;
    }
    
    @Override
    public String toString() {
        return "SimpleAdvancement{id=" + this.func_192067_g() + ", parent=" + ((this.field_192076_a == null) ? "null" : this.field_192076_a.func_192067_g()) + ", display=" + this.field_192077_b + ", rewards=" + this.field_192078_c + ", criteria=" + this.field_192080_e + ", requirements=" + Arrays.deepToString(this.field_192081_f) + '}';
    }
    
    public Iterable<Advancement> func_192069_e() {
        return this.field_192082_g;
    }
    
    public Map<String, Criterion> func_192073_f() {
        return this.field_192080_e;
    }
    
    public int func_193124_g() {
        return this.field_192081_f.length;
    }
    
    public void func_192071_a(final Advancement p_192071_1_) {
        this.field_192082_g.add(p_192071_1_);
    }
    
    public ResourceLocation func_192067_g() {
        return this.field_192079_d;
    }
    
    @Override
    public boolean equals(final Object p_equals_1_) {
        if (this == p_equals_1_) {
            return true;
        }
        if (!(p_equals_1_ instanceof Advancement)) {
            return false;
        }
        final Advancement advancement = (Advancement)p_equals_1_;
        return this.field_192079_d.equals(advancement.field_192079_d);
    }
    
    @Override
    public int hashCode() {
        return this.field_192079_d.hashCode();
    }
    
    public String[][] func_192074_h() {
        return this.field_192081_f;
    }
    
    public ITextComponent func_193123_j() {
        return this.field_193125_h;
    }
    
    public static class Builder
    {
        private final ResourceLocation field_192061_a;
        private Advancement field_192062_b;
        private final DisplayInfo field_192063_c;
        private final AdvancementRewards field_192064_d;
        private final Map<String, Criterion> field_192065_e;
        private final String[][] field_192066_f;
        
        Builder(@Nullable final ResourceLocation p_i47414_1_, @Nullable final DisplayInfo p_i47414_2_, final AdvancementRewards p_i47414_3_, final Map<String, Criterion> p_i47414_4_, final String[][] p_i47414_5_) {
            this.field_192061_a = p_i47414_1_;
            this.field_192063_c = p_i47414_2_;
            this.field_192064_d = p_i47414_3_;
            this.field_192065_e = p_i47414_4_;
            this.field_192066_f = p_i47414_5_;
        }
        
        public boolean func_192058_a(final Function<ResourceLocation, Advancement> p_192058_1_) {
            if (this.field_192061_a == null) {
                return true;
            }
            this.field_192062_b = p_192058_1_.apply(this.field_192061_a);
            return this.field_192062_b != null;
        }
        
        public Advancement func_192056_a(final ResourceLocation p_192056_1_) {
            return new Advancement(p_192056_1_, this.field_192062_b, this.field_192063_c, this.field_192064_d, this.field_192065_e, this.field_192066_f);
        }
        
        public void func_192057_a(final PacketBuffer p_192057_1_) {
            if (this.field_192061_a == null) {
                p_192057_1_.writeBoolean(false);
            }
            else {
                p_192057_1_.writeBoolean(true);
                p_192057_1_.func_192572_a(this.field_192061_a);
            }
            if (this.field_192063_c == null) {
                p_192057_1_.writeBoolean(false);
            }
            else {
                p_192057_1_.writeBoolean(true);
                this.field_192063_c.func_192290_a(p_192057_1_);
            }
            Criterion.func_192141_a(this.field_192065_e, p_192057_1_);
            p_192057_1_.writeVarIntToBuffer(this.field_192066_f.length);
            String[][] field_192066_f;
            for (int length = (field_192066_f = this.field_192066_f).length, i = 0; i < length; ++i) {
                final String[] astring = field_192066_f[i];
                p_192057_1_.writeVarIntToBuffer(astring.length);
                String[] array;
                for (int length2 = (array = astring).length, j = 0; j < length2; ++j) {
                    final String s = array[j];
                    p_192057_1_.writeString(s);
                }
            }
        }
        
        @Override
        public String toString() {
            return "Task Advancement{parentId=" + this.field_192061_a + ", display=" + this.field_192063_c + ", rewards=" + this.field_192064_d + ", criteria=" + this.field_192065_e + ", requirements=" + Arrays.deepToString(this.field_192066_f) + '}';
        }
        
        public static Builder func_192059_a(final JsonObject p_192059_0_, final JsonDeserializationContext p_192059_1_) {
            final ResourceLocation resourcelocation = p_192059_0_.has("parent") ? new ResourceLocation(JsonUtils.getString(p_192059_0_, "parent")) : null;
            final DisplayInfo displayinfo = p_192059_0_.has("display") ? DisplayInfo.func_192294_a(JsonUtils.getJsonObject(p_192059_0_, "display"), p_192059_1_) : null;
            final AdvancementRewards advancementrewards = JsonUtils.deserializeClass(p_192059_0_, "rewards", AdvancementRewards.field_192114_a, p_192059_1_, AdvancementRewards.class);
            final Map<String, Criterion> map = Criterion.func_192144_b(JsonUtils.getJsonObject(p_192059_0_, "criteria"), p_192059_1_);
            if (map.isEmpty()) {
                throw new JsonSyntaxException("Advancement criteria cannot be empty");
            }
            final JsonArray jsonarray = JsonUtils.getJsonArray(p_192059_0_, "requirements", new JsonArray());
            String[][] astring = new String[jsonarray.size()][];
            for (int i = 0; i < jsonarray.size(); ++i) {
                final JsonArray jsonarray2 = JsonUtils.getJsonArray(jsonarray.get(i), "requirements[" + i + "]");
                astring[i] = new String[jsonarray2.size()];
                for (int j = 0; j < jsonarray2.size(); ++j) {
                    astring[i][j] = JsonUtils.getString(jsonarray2.get(j), "requirements[" + i + "][" + j + "]");
                }
            }
            if (astring.length == 0) {
                astring = new String[map.size()][];
                int k = 0;
                for (final String s2 : map.keySet()) {
                    astring[k++] = new String[] { s2 };
                }
            }
            String[][] array;
            for (int length = (array = astring).length, l = 0; l < length; ++l) {
                final String[] astring2 = array[l];
                if (astring2.length == 0 && map.isEmpty()) {
                    throw new JsonSyntaxException("Requirement entry cannot be empty");
                }
                String[] array2;
                for (int length2 = (array2 = astring2).length, n = 0; n < length2; ++n) {
                    final String s3 = array2[n];
                    if (!map.containsKey(s3)) {
                        throw new JsonSyntaxException("Unknown required criterion '" + s3 + "'");
                    }
                }
            }
            for (final String s4 : map.keySet()) {
                boolean flag = false;
                String[][] array3;
                for (int length3 = (array3 = astring).length, n2 = 0; n2 < length3; ++n2) {
                    final String[] astring3 = array3[n2];
                    if (ArrayUtils.contains(astring3, s4)) {
                        flag = true;
                        break;
                    }
                }
                if (!flag) {
                    throw new JsonSyntaxException("Criterion '" + s4 + "' isn't a requirement for completion. This isn't supported behaviour, all criteria must be required.");
                }
            }
            return new Builder(resourcelocation, displayinfo, advancementrewards, map, astring);
        }
        
        public static Builder func_192060_b(final PacketBuffer p_192060_0_) throws IOException {
            final ResourceLocation resourcelocation = p_192060_0_.readBoolean() ? p_192060_0_.func_192575_l() : null;
            final DisplayInfo displayinfo = p_192060_0_.readBoolean() ? DisplayInfo.func_192295_b(p_192060_0_) : null;
            final Map<String, Criterion> map = Criterion.func_192142_c(p_192060_0_);
            final String[][] astring = new String[p_192060_0_.readVarIntFromBuffer()][];
            for (int i = 0; i < astring.length; ++i) {
                astring[i] = new String[p_192060_0_.readVarIntFromBuffer()];
                for (int j = 0; j < astring[i].length; ++j) {
                    astring[i][j] = p_192060_0_.readStringFromBuffer(32767);
                }
            }
            return new Builder(resourcelocation, displayinfo, AdvancementRewards.field_192114_a, map, astring);
        }
    }
}
