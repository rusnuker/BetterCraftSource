// 
// Decompiled by Procyon v0.6.0
// 

package net.minecraft.advancements;

import java.text.ParseException;
import com.google.gson.JsonSyntaxException;
import com.google.gson.JsonNull;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonElement;
import net.minecraft.network.PacketBuffer;
import java.util.Date;
import java.text.SimpleDateFormat;

public class CriterionProgress
{
    private static final SimpleDateFormat field_192155_a;
    private final AdvancementProgress field_192156_b;
    private Date field_192157_c;
    
    static {
        field_192155_a = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss Z");
    }
    
    public CriterionProgress(final AdvancementProgress p_i47469_1_) {
        this.field_192156_b = p_i47469_1_;
    }
    
    public boolean func_192151_a() {
        return this.field_192157_c != null;
    }
    
    public void func_192153_b() {
        this.field_192157_c = new Date();
    }
    
    public void func_192154_c() {
        this.field_192157_c = null;
    }
    
    public Date func_193140_d() {
        return this.field_192157_c;
    }
    
    @Override
    public String toString() {
        return "CriterionProgress{obtained=" + ((this.field_192157_c == null) ? "false" : this.field_192157_c) + '}';
    }
    
    public void func_192150_a(final PacketBuffer p_192150_1_) {
        p_192150_1_.writeBoolean(this.field_192157_c != null);
        if (this.field_192157_c != null) {
            p_192150_1_.func_192574_a(this.field_192157_c);
        }
    }
    
    public JsonElement func_192148_e() {
        return (this.field_192157_c != null) ? new JsonPrimitive(CriterionProgress.field_192155_a.format(this.field_192157_c)) : JsonNull.INSTANCE;
    }
    
    public static CriterionProgress func_192149_a(final PacketBuffer p_192149_0_, final AdvancementProgress p_192149_1_) {
        final CriterionProgress criterionprogress = new CriterionProgress(p_192149_1_);
        if (p_192149_0_.readBoolean()) {
            criterionprogress.field_192157_c = p_192149_0_.func_192573_m();
        }
        return criterionprogress;
    }
    
    public static CriterionProgress func_192152_a(final AdvancementProgress p_192152_0_, final String p_192152_1_) {
        final CriterionProgress criterionprogress = new CriterionProgress(p_192152_0_);
        try {
            criterionprogress.field_192157_c = CriterionProgress.field_192155_a.parse(p_192152_1_);
            return criterionprogress;
        }
        catch (final ParseException parseexception) {
            throw new JsonSyntaxException("Invalid datetime: " + p_192152_1_, parseexception);
        }
    }
}
