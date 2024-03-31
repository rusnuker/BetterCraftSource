// 
// Decompiled by Procyon v0.6.0
// 

package net.minecraft.client.tutorial;

import net.minecraft.item.ItemStack;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.util.MouseHelper;
import net.minecraft.util.MovementInput;

public interface ITutorialStep
{
    default void func_193248_b() {
    }
    
    default void func_193245_a() {
    }
    
    default void func_193247_a(final MovementInput p_193247_1_) {
    }
    
    default void func_193249_a(final MouseHelper p_193249_1_) {
    }
    
    default void func_193246_a(final WorldClient p_193246_1_, final RayTraceResult p_193246_2_) {
    }
    
    default void func_193250_a(final WorldClient p_193250_1_, final BlockPos p_193250_2_, final IBlockState p_193250_3_, final float p_193250_4_) {
    }
    
    default void func_193251_c() {
    }
    
    default void func_193252_a(final ItemStack p_193252_1_) {
    }
}
