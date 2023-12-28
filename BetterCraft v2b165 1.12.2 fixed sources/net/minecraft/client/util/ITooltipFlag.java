// 
// Decompiled by Procyon v0.6.0
// 

package net.minecraft.client.util;

public interface ITooltipFlag
{
    boolean func_194127_a();
    
    public enum TooltipFlags implements ITooltipFlag
    {
        NORMAL("NORMAL", 0, false), 
        ADVANCED("ADVANCED", 1, true);
        
        final boolean field_194131_c;
        
        private TooltipFlags(final String s, final int n, final boolean p_i47611_3_) {
            this.field_194131_c = p_i47611_3_;
        }
        
        @Override
        public boolean func_194127_a() {
            return this.field_194131_c;
        }
    }
}
