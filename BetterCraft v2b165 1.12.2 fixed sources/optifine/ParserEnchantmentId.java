// 
// Decompiled by Procyon v0.6.0
// 

package optifine;

import net.minecraft.enchantment.Enchantment;

public class ParserEnchantmentId implements IParserInt
{
    @Override
    public int parse(final String p_parse_1_, final int p_parse_2_) {
        final Enchantment enchantment = Enchantment.getEnchantmentByLocation(p_parse_1_);
        return (enchantment == null) ? p_parse_2_ : Enchantment.getEnchantmentID(enchantment);
    }
}
