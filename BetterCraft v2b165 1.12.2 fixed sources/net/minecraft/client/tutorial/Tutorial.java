// 
// Decompiled by Procyon v0.6.0
// 

package net.minecraft.client.tutorial;

import net.minecraft.util.text.TextComponentKeybind;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.GameType;
import net.minecraft.item.ItemStack;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.util.MouseHelper;
import net.minecraft.util.MovementInput;
import javax.annotation.Nullable;
import net.minecraft.client.Minecraft;

public class Tutorial
{
    private final Minecraft field_193304_a;
    @Nullable
    private ITutorialStep field_193305_b;
    
    public Tutorial(final Minecraft p_i47578_1_) {
        this.field_193304_a = p_i47578_1_;
    }
    
    public void func_193293_a(final MovementInput p_193293_1_) {
        if (this.field_193305_b != null) {
            this.field_193305_b.func_193247_a(p_193293_1_);
        }
    }
    
    public void func_193299_a(final MouseHelper p_193299_1_) {
        if (this.field_193305_b != null) {
            this.field_193305_b.func_193249_a(p_193299_1_);
        }
    }
    
    public void func_193297_a(@Nullable final WorldClient p_193297_1_, @Nullable final RayTraceResult p_193297_2_) {
        if (this.field_193305_b != null && p_193297_2_ != null && p_193297_1_ != null) {
            this.field_193305_b.func_193246_a(p_193297_1_, p_193297_2_);
        }
    }
    
    public void func_193294_a(final WorldClient p_193294_1_, final BlockPos p_193294_2_, final IBlockState p_193294_3_, final float p_193294_4_) {
        if (this.field_193305_b != null) {
            this.field_193305_b.func_193250_a(p_193294_1_, p_193294_2_, p_193294_3_, p_193294_4_);
        }
    }
    
    public void func_193296_a() {
        if (this.field_193305_b != null) {
            this.field_193305_b.func_193251_c();
        }
    }
    
    public void func_193301_a(final ItemStack p_193301_1_) {
        if (this.field_193305_b != null) {
            this.field_193305_b.func_193252_a(p_193301_1_);
        }
    }
    
    public void func_193300_b() {
        if (this.field_193305_b != null) {
            this.field_193305_b.func_193248_b();
            this.field_193305_b = null;
        }
    }
    
    public void func_193302_c() {
        if (this.field_193305_b != null) {
            this.func_193300_b();
        }
        this.field_193305_b = this.field_193304_a.gameSettings.field_193631_S.func_193309_a(this);
    }
    
    public void func_193303_d() {
        if (this.field_193305_b != null) {
            if (this.field_193304_a.world != null) {
                this.field_193305_b.func_193245_a();
            }
            else {
                this.func_193300_b();
            }
        }
        else if (this.field_193304_a.world != null) {
            this.func_193302_c();
        }
    }
    
    public void func_193292_a(final TutorialSteps p_193292_1_) {
        this.field_193304_a.gameSettings.field_193631_S = p_193292_1_;
        this.field_193304_a.gameSettings.saveOptions();
        if (this.field_193305_b != null) {
            this.field_193305_b.func_193248_b();
            this.field_193305_b = p_193292_1_.func_193309_a(this);
        }
    }
    
    public Minecraft func_193295_e() {
        return this.field_193304_a;
    }
    
    public GameType func_194072_f() {
        return (this.field_193304_a.playerController == null) ? GameType.NOT_SET : this.field_193304_a.playerController.getCurrentGameType();
    }
    
    public static ITextComponent func_193291_a(final String p_193291_0_) {
        final TextComponentKeybind textcomponentkeybind = new TextComponentKeybind("key." + p_193291_0_);
        textcomponentkeybind.getStyle().setBold(true);
        return textcomponentkeybind;
    }
}
