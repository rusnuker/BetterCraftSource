// 
// Decompiled by Procyon v0.6.0
// 

package net.minecraft.advancements;

import com.google.gson.JsonParseException;
import net.minecraft.util.JsonUtils;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonElement;
import com.google.gson.JsonSerializationContext;
import java.lang.reflect.Type;
import com.google.gson.JsonSerializer;
import com.google.gson.JsonDeserializer;
import java.util.Date;
import java.util.List;
import com.google.common.collect.Lists;
import javax.annotation.Nullable;
import net.minecraft.network.PacketBuffer;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Set;
import com.google.common.collect.Maps;
import java.util.Map;

public class AdvancementProgress implements Comparable<AdvancementProgress>
{
    private final Map<String, CriterionProgress> field_192110_a;
    private String[][] field_192111_b;
    
    public AdvancementProgress() {
        this.field_192110_a = (Map<String, CriterionProgress>)Maps.newHashMap();
        this.field_192111_b = new String[0][];
    }
    
    public void func_192099_a(final Map<String, Criterion> p_192099_1_, final String[][] p_192099_2_) {
        final Set<String> set = p_192099_1_.keySet();
        final Iterator<Map.Entry<String, CriterionProgress>> iterator = this.field_192110_a.entrySet().iterator();
        while (iterator.hasNext()) {
            final Map.Entry<String, CriterionProgress> entry = iterator.next();
            if (!set.contains(entry.getKey())) {
                iterator.remove();
            }
        }
        for (final String s : set) {
            if (!this.field_192110_a.containsKey(s)) {
                this.field_192110_a.put(s, new CriterionProgress(this));
            }
        }
        this.field_192111_b = p_192099_2_;
    }
    
    public boolean func_192105_a() {
        if (this.field_192111_b.length == 0) {
            return false;
        }
        String[][] field_192111_b;
        for (int length = (field_192111_b = this.field_192111_b).length, i = 0; i < length; ++i) {
            final String[] astring = field_192111_b[i];
            boolean flag = false;
            String[] array;
            for (int length2 = (array = astring).length, j = 0; j < length2; ++j) {
                final String s = array[j];
                final CriterionProgress criterionprogress = this.func_192106_c(s);
                if (criterionprogress != null && criterionprogress.func_192151_a()) {
                    flag = true;
                    break;
                }
            }
            if (!flag) {
                return false;
            }
        }
        return true;
    }
    
    public boolean func_192108_b() {
        for (final CriterionProgress criterionprogress : this.field_192110_a.values()) {
            if (criterionprogress.func_192151_a()) {
                return true;
            }
        }
        return false;
    }
    
    public boolean func_192109_a(final String p_192109_1_) {
        final CriterionProgress criterionprogress = this.field_192110_a.get(p_192109_1_);
        if (criterionprogress != null && !criterionprogress.func_192151_a()) {
            criterionprogress.func_192153_b();
            return true;
        }
        return false;
    }
    
    public boolean func_192101_b(final String p_192101_1_) {
        final CriterionProgress criterionprogress = this.field_192110_a.get(p_192101_1_);
        if (criterionprogress != null && criterionprogress.func_192151_a()) {
            criterionprogress.func_192154_c();
            return true;
        }
        return false;
    }
    
    @Override
    public String toString() {
        return "AdvancementProgress{criteria=" + this.field_192110_a + ", requirements=" + Arrays.deepToString(this.field_192111_b) + '}';
    }
    
    public void func_192104_a(final PacketBuffer p_192104_1_) {
        p_192104_1_.writeVarIntToBuffer(this.field_192110_a.size());
        for (final Map.Entry<String, CriterionProgress> entry : this.field_192110_a.entrySet()) {
            p_192104_1_.writeString(entry.getKey());
            entry.getValue().func_192150_a(p_192104_1_);
        }
    }
    
    public static AdvancementProgress func_192100_b(final PacketBuffer p_192100_0_) {
        final AdvancementProgress advancementprogress = new AdvancementProgress();
        for (int i = p_192100_0_.readVarIntFromBuffer(), j = 0; j < i; ++j) {
            advancementprogress.field_192110_a.put(p_192100_0_.readStringFromBuffer(32767), CriterionProgress.func_192149_a(p_192100_0_, advancementprogress));
        }
        return advancementprogress;
    }
    
    @Nullable
    public CriterionProgress func_192106_c(final String p_192106_1_) {
        return this.field_192110_a.get(p_192106_1_);
    }
    
    public float func_192103_c() {
        if (this.field_192110_a.isEmpty()) {
            return 0.0f;
        }
        final float f = (float)this.field_192111_b.length;
        final float f2 = (float)this.func_194032_h();
        return f2 / f;
    }
    
    @Nullable
    public String func_193126_d() {
        if (this.field_192110_a.isEmpty()) {
            return null;
        }
        final int i = this.field_192111_b.length;
        if (i <= 1) {
            return null;
        }
        final int j = this.func_194032_h();
        return String.valueOf(j) + "/" + i;
    }
    
    private int func_194032_h() {
        int i = 0;
        String[][] field_192111_b;
        for (int length = (field_192111_b = this.field_192111_b).length, j = 0; j < length; ++j) {
            final String[] astring = field_192111_b[j];
            boolean flag = false;
            String[] array;
            for (int length2 = (array = astring).length, k = 0; k < length2; ++k) {
                final String s = array[k];
                final CriterionProgress criterionprogress = this.func_192106_c(s);
                if (criterionprogress != null && criterionprogress.func_192151_a()) {
                    flag = true;
                    break;
                }
            }
            if (flag) {
                ++i;
            }
        }
        return i;
    }
    
    public Iterable<String> func_192107_d() {
        final List<String> list = (List<String>)Lists.newArrayList();
        for (final Map.Entry<String, CriterionProgress> entry : this.field_192110_a.entrySet()) {
            if (!entry.getValue().func_192151_a()) {
                list.add(entry.getKey());
            }
        }
        return list;
    }
    
    public Iterable<String> func_192102_e() {
        final List<String> list = (List<String>)Lists.newArrayList();
        for (final Map.Entry<String, CriterionProgress> entry : this.field_192110_a.entrySet()) {
            if (entry.getValue().func_192151_a()) {
                list.add(entry.getKey());
            }
        }
        return list;
    }
    
    @Nullable
    public Date func_193128_g() {
        Date date = null;
        for (final CriterionProgress criterionprogress : this.field_192110_a.values()) {
            if (criterionprogress.func_192151_a() && (date == null || criterionprogress.func_193140_d().before(date))) {
                date = criterionprogress.func_193140_d();
            }
        }
        return date;
    }
    
    @Override
    public int compareTo(final AdvancementProgress p_compareTo_1_) {
        final Date date = this.func_193128_g();
        final Date date2 = p_compareTo_1_.func_193128_g();
        if (date == null && date2 != null) {
            return 1;
        }
        if (date != null && date2 == null) {
            return -1;
        }
        return (date == null && date2 == null) ? 0 : date.compareTo(date2);
    }
    
    public static class Serializer implements JsonDeserializer<AdvancementProgress>, JsonSerializer<AdvancementProgress>
    {
        @Override
        public JsonElement serialize(final AdvancementProgress p_serialize_1_, final Type p_serialize_2_, final JsonSerializationContext p_serialize_3_) {
            final JsonObject jsonobject = new JsonObject();
            final JsonObject jsonobject2 = new JsonObject();
            for (final Map.Entry<String, CriterionProgress> entry : p_serialize_1_.field_192110_a.entrySet()) {
                final CriterionProgress criterionprogress = entry.getValue();
                if (criterionprogress.func_192151_a()) {
                    jsonobject2.add(entry.getKey(), criterionprogress.func_192148_e());
                }
            }
            if (!jsonobject2.entrySet().isEmpty()) {
                jsonobject.add("criteria", jsonobject2);
            }
            jsonobject.addProperty("done", p_serialize_1_.func_192105_a());
            return jsonobject;
        }
        
        @Override
        public AdvancementProgress deserialize(final JsonElement p_deserialize_1_, final Type p_deserialize_2_, final JsonDeserializationContext p_deserialize_3_) throws JsonParseException {
            final JsonObject jsonobject = JsonUtils.getJsonObject(p_deserialize_1_, "advancement");
            final JsonObject jsonobject2 = JsonUtils.getJsonObject(jsonobject, "criteria", new JsonObject());
            final AdvancementProgress advancementprogress = new AdvancementProgress();
            for (final Map.Entry<String, JsonElement> entry : jsonobject2.entrySet()) {
                final String s = entry.getKey();
                advancementprogress.field_192110_a.put(s, CriterionProgress.func_192152_a(advancementprogress, JsonUtils.getString(entry.getValue(), s)));
            }
            return advancementprogress;
        }
    }
}
