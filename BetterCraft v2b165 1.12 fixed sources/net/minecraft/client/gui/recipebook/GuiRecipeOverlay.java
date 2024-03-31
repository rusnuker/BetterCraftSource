// 
// Decompiled by Procyon v0.6.0
// 

package net.minecraft.client.gui.recipebook;

import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.item.crafting.ShapedRecipes;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import java.util.Iterator;
import net.minecraft.util.math.MathHelper;
import java.util.Collections;
import net.minecraft.stats.RecipeBook;
import com.google.common.collect.Lists;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.client.Minecraft;
import java.util.List;
import net.minecraft.util.ResourceLocation;
import net.minecraft.client.gui.Gui;

public class GuiRecipeOverlay extends Gui
{
    private static final ResourceLocation field_191847_a;
    private final List<Button> field_193972_f;
    private boolean field_191850_h;
    private int field_191851_i;
    private int field_191852_j;
    private Minecraft field_191853_k;
    private RecipeList field_191848_f;
    private IRecipe field_193973_l;
    private float field_193974_m;
    
    static {
        field_191847_a = new ResourceLocation("textures/gui/recipe_book.png");
    }
    
    public GuiRecipeOverlay() {
        this.field_193972_f = (List<Button>)Lists.newArrayList();
    }
    
    public void func_191845_a(final Minecraft p_191845_1_, final RecipeList p_191845_2_, final int p_191845_3_, final int p_191845_4_, final int p_191845_5_, final int p_191845_6_, final float p_191845_7_, final RecipeBook p_191845_8_) {
        this.field_191853_k = p_191845_1_;
        this.field_191848_f = p_191845_2_;
        final boolean flag = p_191845_8_.func_192815_c();
        final List<IRecipe> list = p_191845_2_.func_194207_b(true);
        final List<IRecipe> list2 = flag ? Collections.emptyList() : p_191845_2_.func_194207_b(false);
        final int i = list.size();
        final int j = i + list2.size();
        final int k = (j <= 16) ? 4 : 5;
        final int l = (int)Math.ceil(j / (float)k);
        this.field_191851_i = p_191845_3_;
        this.field_191852_j = p_191845_4_;
        final int i2 = 25;
        final float f = (float)(this.field_191851_i + Math.min(j, k) * 25);
        final float f2 = (float)(p_191845_5_ + 50);
        if (f > f2) {
            this.field_191851_i -= (int)(p_191845_7_ * (int)((f - f2) / p_191845_7_));
        }
        final float f3 = (float)(this.field_191852_j + l * 25);
        final float f4 = (float)(p_191845_6_ + 50);
        if (f3 > f4) {
            this.field_191852_j -= (int)(p_191845_7_ * MathHelper.ceil((f3 - f4) / p_191845_7_));
        }
        final float f5 = (float)this.field_191852_j;
        final float f6 = (float)(p_191845_6_ - 100);
        if (f5 < f6) {
            this.field_191852_j -= (int)(p_191845_7_ * MathHelper.ceil((f5 - f6) / p_191845_7_));
        }
        this.field_191850_h = true;
        this.field_193972_f.clear();
        for (int j2 = 0; j2 < j; ++j2) {
            final boolean flag2 = j2 < i;
            this.field_193972_f.add(new Button(this.field_191851_i + 4 + 25 * (j2 % k), this.field_191852_j + 5 + 25 * (j2 / k), flag2 ? list.get(j2) : list2.get(j2 - i), flag2));
        }
        this.field_193973_l = null;
    }
    
    public RecipeList func_193971_a() {
        return this.field_191848_f;
    }
    
    public IRecipe func_193967_b() {
        return this.field_193973_l;
    }
    
    public boolean func_193968_a(final int p_193968_1_, final int p_193968_2_, final int p_193968_3_) {
        if (p_193968_3_ != 0) {
            return false;
        }
        for (final Button guirecipeoverlay$button : this.field_193972_f) {
            if (guirecipeoverlay$button.mousePressed(this.field_191853_k, p_193968_1_, p_193968_2_)) {
                this.field_193973_l = guirecipeoverlay$button.field_193924_p;
                return true;
            }
        }
        return false;
    }
    
    public void func_191842_a(final int p_191842_1_, final int p_191842_2_, final float p_191842_3_) {
        if (this.field_191850_h) {
            this.field_193974_m += p_191842_3_;
            RenderHelper.enableGUIStandardItemLighting();
            GlStateManager.enableBlend();
            GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
            this.field_191853_k.getTextureManager().bindTexture(GuiRecipeOverlay.field_191847_a);
            GlStateManager.pushMatrix();
            GlStateManager.translate(0.0f, 0.0f, 170.0f);
            final int i = (this.field_193972_f.size() <= 16) ? 4 : 5;
            final int j = Math.min(this.field_193972_f.size(), i);
            final int k = MathHelper.ceil(this.field_193972_f.size() / (float)i);
            final int l = 24;
            final int i2 = 4;
            final int j2 = 82;
            final int k2 = 208;
            this.func_191846_c(j, k, 24, 4, 82, 208);
            GlStateManager.disableBlend();
            RenderHelper.disableStandardItemLighting();
            for (final Button guirecipeoverlay$button : this.field_193972_f) {
                guirecipeoverlay$button.drawButton(this.field_191853_k, p_191842_1_, p_191842_2_, p_191842_3_);
            }
            GlStateManager.popMatrix();
        }
    }
    
    private void func_191846_c(final int p_191846_1_, final int p_191846_2_, final int p_191846_3_, final int p_191846_4_, final int p_191846_5_, final int p_191846_6_) {
        this.drawTexturedModalRect(this.field_191851_i, this.field_191852_j, p_191846_5_, p_191846_6_, p_191846_4_, p_191846_4_);
        this.drawTexturedModalRect(this.field_191851_i + p_191846_4_ * 2 + p_191846_1_ * p_191846_3_, this.field_191852_j, p_191846_5_ + p_191846_3_ + p_191846_4_, p_191846_6_, p_191846_4_, p_191846_4_);
        this.drawTexturedModalRect(this.field_191851_i, this.field_191852_j + p_191846_4_ * 2 + p_191846_2_ * p_191846_3_, p_191846_5_, p_191846_6_ + p_191846_3_ + p_191846_4_, p_191846_4_, p_191846_4_);
        this.drawTexturedModalRect(this.field_191851_i + p_191846_4_ * 2 + p_191846_1_ * p_191846_3_, this.field_191852_j + p_191846_4_ * 2 + p_191846_2_ * p_191846_3_, p_191846_5_ + p_191846_3_ + p_191846_4_, p_191846_6_ + p_191846_3_ + p_191846_4_, p_191846_4_, p_191846_4_);
        for (int i = 0; i < p_191846_1_; ++i) {
            this.drawTexturedModalRect(this.field_191851_i + p_191846_4_ + i * p_191846_3_, this.field_191852_j, p_191846_5_ + p_191846_4_, p_191846_6_, p_191846_3_, p_191846_4_);
            this.drawTexturedModalRect(this.field_191851_i + p_191846_4_ + (i + 1) * p_191846_3_, this.field_191852_j, p_191846_5_ + p_191846_4_, p_191846_6_, p_191846_4_, p_191846_4_);
            for (int j = 0; j < p_191846_2_; ++j) {
                if (i == 0) {
                    this.drawTexturedModalRect(this.field_191851_i, this.field_191852_j + p_191846_4_ + j * p_191846_3_, p_191846_5_, p_191846_6_ + p_191846_4_, p_191846_4_, p_191846_3_);
                    this.drawTexturedModalRect(this.field_191851_i, this.field_191852_j + p_191846_4_ + (j + 1) * p_191846_3_, p_191846_5_, p_191846_6_ + p_191846_4_, p_191846_4_, p_191846_4_);
                }
                this.drawTexturedModalRect(this.field_191851_i + p_191846_4_ + i * p_191846_3_, this.field_191852_j + p_191846_4_ + j * p_191846_3_, p_191846_5_ + p_191846_4_, p_191846_6_ + p_191846_4_, p_191846_3_, p_191846_3_);
                this.drawTexturedModalRect(this.field_191851_i + p_191846_4_ + (i + 1) * p_191846_3_, this.field_191852_j + p_191846_4_ + j * p_191846_3_, p_191846_5_ + p_191846_4_, p_191846_6_ + p_191846_4_, p_191846_4_, p_191846_3_);
                this.drawTexturedModalRect(this.field_191851_i + p_191846_4_ + i * p_191846_3_, this.field_191852_j + p_191846_4_ + (j + 1) * p_191846_3_, p_191846_5_ + p_191846_4_, p_191846_6_ + p_191846_4_, p_191846_3_, p_191846_4_);
                this.drawTexturedModalRect(this.field_191851_i + p_191846_4_ + (i + 1) * p_191846_3_ - 1, this.field_191852_j + p_191846_4_ + (j + 1) * p_191846_3_ - 1, p_191846_5_ + p_191846_4_, p_191846_6_ + p_191846_4_, p_191846_4_ + 1, p_191846_4_ + 1);
                if (i == p_191846_1_ - 1) {
                    this.drawTexturedModalRect(this.field_191851_i + p_191846_4_ * 2 + p_191846_1_ * p_191846_3_, this.field_191852_j + p_191846_4_ + j * p_191846_3_, p_191846_5_ + p_191846_3_ + p_191846_4_, p_191846_6_ + p_191846_4_, p_191846_4_, p_191846_3_);
                    this.drawTexturedModalRect(this.field_191851_i + p_191846_4_ * 2 + p_191846_1_ * p_191846_3_, this.field_191852_j + p_191846_4_ + (j + 1) * p_191846_3_, p_191846_5_ + p_191846_3_ + p_191846_4_, p_191846_6_ + p_191846_4_, p_191846_4_, p_191846_4_);
                }
            }
            this.drawTexturedModalRect(this.field_191851_i + p_191846_4_ + i * p_191846_3_, this.field_191852_j + p_191846_4_ * 2 + p_191846_2_ * p_191846_3_, p_191846_5_ + p_191846_4_, p_191846_6_ + p_191846_3_ + p_191846_4_, p_191846_3_, p_191846_4_);
            this.drawTexturedModalRect(this.field_191851_i + p_191846_4_ + (i + 1) * p_191846_3_, this.field_191852_j + p_191846_4_ * 2 + p_191846_2_ * p_191846_3_, p_191846_5_ + p_191846_4_, p_191846_6_ + p_191846_3_ + p_191846_4_, p_191846_4_, p_191846_4_);
        }
    }
    
    public void func_192999_a(final boolean p_192999_1_) {
        this.field_191850_h = p_192999_1_;
    }
    
    public boolean func_191839_a() {
        return this.field_191850_h;
    }
    
    class Button extends GuiButton
    {
        private final IRecipe field_193924_p;
        private final boolean field_193925_q;
        
        public Button(final int p_i47594_2_, final int p_i47594_3_, final IRecipe p_i47594_4_, final boolean p_i47594_5_) {
            super(0, p_i47594_2_, p_i47594_3_, "");
            this.width = 24;
            this.height = 24;
            this.field_193924_p = p_i47594_4_;
            this.field_193925_q = p_i47594_5_;
        }
        
        @Override
        public void drawButton(final Minecraft p_191745_1_, final int p_191745_2_, final int p_191745_3_, final float p_191745_4_) {
            RenderHelper.enableGUIStandardItemLighting();
            GlStateManager.enableAlpha();
            p_191745_1_.getTextureManager().bindTexture(GuiRecipeOverlay.field_191847_a);
            this.hovered = (p_191745_2_ >= this.xPosition && p_191745_3_ >= this.yPosition && p_191745_2_ < this.xPosition + this.width && p_191745_3_ < this.yPosition + this.height);
            int i = 152;
            if (!this.field_193925_q) {
                i += 26;
            }
            int j = 78;
            if (this.hovered) {
                j += 26;
            }
            this.drawTexturedModalRect(this.xPosition, this.yPosition, i, j, this.width, this.height);
            int k = 3;
            int l = 3;
            if (this.field_193924_p instanceof ShapedRecipes) {
                final ShapedRecipes shapedrecipes = (ShapedRecipes)this.field_193924_p;
                k = shapedrecipes.func_192403_f();
                l = shapedrecipes.func_192404_g();
            }
            final Iterator<Ingredient> iterator = this.field_193924_p.func_192400_c().iterator();
            for (int i2 = 0; i2 < l; ++i2) {
                final int j2 = 3 + i2 * 7;
                for (int k2 = 0; k2 < k; ++k2) {
                    if (iterator.hasNext()) {
                        final ItemStack[] aitemstack = iterator.next().func_193365_a();
                        if (aitemstack.length != 0) {
                            final int l2 = 3 + k2 * 7;
                            GlStateManager.pushMatrix();
                            final float f = 0.42f;
                            final int i3 = (int)((this.xPosition + l2) / 0.42f - 3.0f);
                            final int j3 = (int)((this.yPosition + j2) / 0.42f - 3.0f);
                            GlStateManager.scale(0.42f, 0.42f, 1.0f);
                            GlStateManager.enableLighting();
                            p_191745_1_.getRenderItem().renderItemAndEffectIntoGUI(aitemstack[MathHelper.floor(GuiRecipeOverlay.this.field_193974_m / 30.0f) % aitemstack.length], i3, j3);
                            GlStateManager.disableLighting();
                            GlStateManager.popMatrix();
                        }
                    }
                }
            }
            GlStateManager.disableAlpha();
            RenderHelper.disableStandardItemLighting();
        }
    }
}
