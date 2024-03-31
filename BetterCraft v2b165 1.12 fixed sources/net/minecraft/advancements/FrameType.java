// 
// Decompiled by Procyon v0.6.0
// 

package net.minecraft.advancements;

import net.minecraft.util.text.TextFormatting;

public enum FrameType
{
    TASK("TASK", 0, "task", 0, TextFormatting.GREEN), 
    CHALLENGE("CHALLENGE", 1, "challenge", 26, TextFormatting.DARK_PURPLE), 
    GOAL("GOAL", 2, "goal", 52, TextFormatting.GREEN);
    
    private final String field_192313_d;
    private final int field_192314_e;
    private final TextFormatting field_193230_f;
    
    private FrameType(final String s, final int n, final String p_i47585_3_, final int p_i47585_4_, final TextFormatting p_i47585_5_) {
        this.field_192313_d = p_i47585_3_;
        this.field_192314_e = p_i47585_4_;
        this.field_193230_f = p_i47585_5_;
    }
    
    public String func_192307_a() {
        return this.field_192313_d;
    }
    
    public int func_192309_b() {
        return this.field_192314_e;
    }
    
    public static FrameType func_192308_a(final String p_192308_0_) {
        FrameType[] values;
        for (int length = (values = values()).length, i = 0; i < length; ++i) {
            final FrameType frametype = values[i];
            if (frametype.field_192313_d.equals(p_192308_0_)) {
                return frametype;
            }
        }
        throw new IllegalArgumentException("Unknown frame type '" + p_192308_0_ + "'");
    }
    
    public TextFormatting func_193229_c() {
        return this.field_193230_f;
    }
}
