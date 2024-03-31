// 
// Decompiled by Procyon v0.6.0
// 

package net.minecraft.client.gui.toasts;

import net.minecraft.item.crafting.IRecipe;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.renderer.GlStateManager;
import com.google.common.collect.Lists;
import net.minecraft.item.ItemStack;
import java.util.List;

public class RecipeToast implements IToast
{
    private final List<ItemStack> field_193666_c;
    private long field_193667_d;
    private boolean field_193668_e;
    
    public RecipeToast(final ItemStack p_i47489_1_) {
        (this.field_193666_c = (List<ItemStack>)Lists.newArrayList()).add(p_i47489_1_);
    }
    
    @Override
    public Visibility func_193653_a(final GuiToast p_193653_1_, final long p_193653_2_) {
        if (this.field_193668_e) {
            this.field_193667_d = p_193653_2_;
            this.field_193668_e = false;
        }
        if (this.field_193666_c.isEmpty()) {
            return Visibility.HIDE;
        }
        p_193653_1_.func_192989_b().getTextureManager().bindTexture(RecipeToast.field_193654_a);
        GlStateManager.color(1.0f, 1.0f, 1.0f);
        p_193653_1_.drawTexturedModalRect(0, 0, 0, 32, 160, 32);
        p_193653_1_.func_192989_b().fontRendererObj.drawString(I18n.format("recipe.toast.title", new Object[0]), 30, 7, -11534256);
        p_193653_1_.func_192989_b().fontRendererObj.drawString(I18n.format("recipe.toast.description", new Object[0]), 30, 18, -16777216);
        RenderHelper.enableGUIStandardItemLighting();
        p_193653_1_.func_192989_b().getRenderItem().renderItemAndEffectIntoGUI(null, this.field_193666_c.get((int)(p_193653_2_ / (5000L / this.field_193666_c.size()) % this.field_193666_c.size())), 8, 8);
        return (p_193653_2_ - this.field_193667_d >= 5000L) ? Visibility.HIDE : Visibility.SHOW;
    }
    
    public void func_193664_a(final ItemStack p_193664_1_) {
        if (this.field_193666_c.add(p_193664_1_)) {
            this.field_193668_e = true;
        }
    }
    
    public static void func_193665_a(final GuiToast p_193665_0_, final IRecipe p_193665_1_) {
        final RecipeToast recipetoast = p_193665_0_.func_192990_a((Class<? extends RecipeToast>)RecipeToast.class, RecipeToast.field_193655_b);
        if (recipetoast == null) {
            p_193665_0_.func_192988_a(new RecipeToast(p_193665_1_.getRecipeOutput()));
        }
        else {
            recipetoast.func_193664_a(p_193665_1_.getRecipeOutput());
        }
    }
}
