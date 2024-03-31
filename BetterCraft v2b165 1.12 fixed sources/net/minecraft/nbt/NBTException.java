// 
// Decompiled by Procyon v0.6.0
// 

package net.minecraft.nbt;

public class NBTException extends Exception
{
    public NBTException(final String p_i47523_1_, final String p_i47523_2_, final int p_i47523_3_) {
        super(String.valueOf(p_i47523_1_) + " at: " + func_193592_a(p_i47523_2_, p_i47523_3_));
    }
    
    private static String func_193592_a(final String p_193592_0_, final int p_193592_1_) {
        final StringBuilder stringbuilder = new StringBuilder();
        final int i = Math.min(p_193592_0_.length(), p_193592_1_);
        if (i > 35) {
            stringbuilder.append("...");
        }
        stringbuilder.append(p_193592_0_.substring(Math.max(0, i - 35), i));
        stringbuilder.append("<--[HERE]");
        return stringbuilder.toString();
    }
}
