// 
// Decompiled by Procyon v0.6.0
// 

package net.minecraft.item;

import net.minecraft.client.util.ITooltipFlag;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.world.World;
import net.minecraft.block.Block;

public class ItemAir extends Item
{
    private final Block field_190904_a;
    
    public ItemAir(final Block p_i47264_1_) {
        this.field_190904_a = p_i47264_1_;
    }
    
    @Override
    public String getUnlocalizedName(final ItemStack stack) {
        return this.field_190904_a.getUnlocalizedName();
    }
    
    @Override
    public String getUnlocalizedName() {
        return this.field_190904_a.getUnlocalizedName();
    }
    
    @Override
    public void addInformation(final ItemStack stack, @Nullable final World playerIn, final List<String> tooltip, final ITooltipFlag advanced) {
        super.addInformation(stack, playerIn, tooltip, advanced);
        this.field_190904_a.func_190948_a(stack, playerIn, tooltip, advanced);
    }
}
