// 
// Decompiled by Procyon v0.6.0
// 

package net.minecraft.client.gui.recipebook;

import net.minecraft.client.resources.I18n;
import java.util.Collection;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.MathHelper;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.Minecraft;
import java.util.Iterator;
import java.util.List;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.stats.RecipeBook;
import net.minecraft.util.ResourceLocation;
import net.minecraft.client.gui.GuiButton;

public class GuiButtonRecipe extends GuiButton
{
    private static final ResourceLocation field_191780_o;
    private RecipeBook field_193930_p;
    private RecipeList field_191774_p;
    private float field_193931_r;
    private float field_191778_t;
    private int field_193932_t;
    
    static {
        field_191780_o = new ResourceLocation("textures/gui/recipe_book.png");
    }
    
    public GuiButtonRecipe() {
        super(0, 0, 0, 25, 25, "");
    }
    
    public void func_193928_a(final RecipeList p_193928_1_, final RecipeBookPage p_193928_2_, final RecipeBook p_193928_3_) {
        this.field_191774_p = p_193928_1_;
        this.field_193930_p = p_193928_3_;
        final List<IRecipe> list = p_193928_1_.func_194208_a(p_193928_3_.func_192815_c());
        for (final IRecipe irecipe : list) {
            if (p_193928_3_.func_194076_e(irecipe)) {
                p_193928_2_.func_194195_a(list);
                this.field_191778_t = 15.0f;
                break;
            }
        }
    }
    
    public RecipeList func_191771_c() {
        return this.field_191774_p;
    }
    
    public void func_191770_c(final int p_191770_1_, final int p_191770_2_) {
        this.xPosition = p_191770_1_;
        this.yPosition = p_191770_2_;
    }
    
    @Override
    public void drawButton(final Minecraft p_191745_1_, final int p_191745_2_, final int p_191745_3_, final float p_191745_4_) {
        if (this.visible) {
            if (!GuiScreen.isCtrlKeyDown()) {
                this.field_193931_r += p_191745_4_;
            }
            this.hovered = (p_191745_2_ >= this.xPosition && p_191745_3_ >= this.yPosition && p_191745_2_ < this.xPosition + this.width && p_191745_3_ < this.yPosition + this.height);
            RenderHelper.enableGUIStandardItemLighting();
            p_191745_1_.getTextureManager().bindTexture(GuiButtonRecipe.field_191780_o);
            GlStateManager.disableLighting();
            int i = 29;
            if (!this.field_191774_p.func_192708_c()) {
                i += 25;
            }
            int j = 206;
            if (this.field_191774_p.func_194208_a(this.field_193930_p.func_192815_c()).size() > 1) {
                j += 25;
            }
            final boolean flag = this.field_191778_t > 0.0f;
            if (flag) {
                final float f = 1.0f + 0.1f * (float)Math.sin(this.field_191778_t / 15.0f * 3.1415927f);
                GlStateManager.pushMatrix();
                GlStateManager.translate((float)(this.xPosition + 8), (float)(this.yPosition + 12), 0.0f);
                GlStateManager.scale(f, f, 1.0f);
                GlStateManager.translate((float)(-(this.xPosition + 8)), (float)(-(this.yPosition + 12)), 0.0f);
                this.field_191778_t -= p_191745_4_;
            }
            this.drawTexturedModalRect(this.xPosition, this.yPosition, i, j, this.width, this.height);
            final List<IRecipe> list = this.func_193927_f();
            this.field_193932_t = MathHelper.floor(this.field_193931_r / 30.0f) % list.size();
            final ItemStack itemstack = list.get(this.field_193932_t).getRecipeOutput();
            int k = 4;
            if (this.field_191774_p.func_194211_e() && this.func_193927_f().size() > 1) {
                p_191745_1_.getRenderItem().renderItemAndEffectIntoGUI(itemstack, this.xPosition + k + 1, this.yPosition + k + 1);
                --k;
            }
            p_191745_1_.getRenderItem().renderItemAndEffectIntoGUI(itemstack, this.xPosition + k, this.yPosition + k);
            if (flag) {
                GlStateManager.popMatrix();
            }
            GlStateManager.enableLighting();
            RenderHelper.disableStandardItemLighting();
        }
    }
    
    private List<IRecipe> func_193927_f() {
        final List<IRecipe> list = this.field_191774_p.func_194207_b(true);
        if (!this.field_193930_p.func_192815_c()) {
            list.addAll(this.field_191774_p.func_194207_b(false));
        }
        return list;
    }
    
    public boolean func_193929_d() {
        return this.func_193927_f().size() == 1;
    }
    
    public IRecipe func_193760_e() {
        final List<IRecipe> list = this.func_193927_f();
        return list.get(this.field_193932_t);
    }
    
    public List<String> func_191772_a(final GuiScreen p_191772_1_) {
        final ItemStack itemstack = this.func_193927_f().get(this.field_193932_t).getRecipeOutput();
        final List<String> list = p_191772_1_.func_191927_a(itemstack);
        if (this.field_191774_p.func_194208_a(this.field_193930_p.func_192815_c()).size() > 1) {
            list.add(I18n.format("gui.recipebook.moreRecipes", new Object[0]));
        }
        return list;
    }
    
    @Override
    public int getButtonWidth() {
        return 25;
    }
}
