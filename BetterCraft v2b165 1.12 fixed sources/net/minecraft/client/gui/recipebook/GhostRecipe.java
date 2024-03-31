// 
// Decompiled by Procyon v0.6.0
// 

package net.minecraft.client.gui.recipebook;

import net.minecraft.util.math.MathHelper;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.item.ItemStack;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.Minecraft;
import javax.annotation.Nullable;
import net.minecraft.item.crafting.Ingredient;
import com.google.common.collect.Lists;
import java.util.List;
import net.minecraft.item.crafting.IRecipe;

public class GhostRecipe
{
    private IRecipe field_192687_a;
    private final List<GhostIngredient> field_192688_b;
    private float field_194190_c;
    
    public GhostRecipe() {
        this.field_192688_b = (List<GhostIngredient>)Lists.newArrayList();
    }
    
    public void func_192682_a() {
        this.field_192687_a = null;
        this.field_192688_b.clear();
        this.field_194190_c = 0.0f;
    }
    
    public void func_194187_a(final Ingredient p_194187_1_, final int p_194187_2_, final int p_194187_3_) {
        this.field_192688_b.add(new GhostIngredient(p_194187_1_, p_194187_2_, p_194187_3_));
    }
    
    public GhostIngredient func_192681_a(final int p_192681_1_) {
        return this.field_192688_b.get(p_192681_1_);
    }
    
    public int func_192684_b() {
        return this.field_192688_b.size();
    }
    
    @Nullable
    public IRecipe func_192686_c() {
        return this.field_192687_a;
    }
    
    public void func_192685_a(final IRecipe p_192685_1_) {
        this.field_192687_a = p_192685_1_;
    }
    
    public void func_194188_a(final Minecraft p_194188_1_, final int p_194188_2_, final int p_194188_3_, final boolean p_194188_4_, final float p_194188_5_) {
        if (!GuiScreen.isCtrlKeyDown()) {
            this.field_194190_c += p_194188_5_;
        }
        RenderHelper.enableGUIStandardItemLighting();
        GlStateManager.disableLighting();
        for (int i = 0; i < this.field_192688_b.size(); ++i) {
            final GhostIngredient ghostrecipe$ghostingredient = this.field_192688_b.get(i);
            final int j = ghostrecipe$ghostingredient.func_193713_b() + p_194188_2_;
            final int k = ghostrecipe$ghostingredient.func_193712_c() + p_194188_3_;
            if (i == 0 && p_194188_4_) {
                Gui.drawRect(j - 4, k - 4, j + 20, k + 20, 822018048);
            }
            else {
                Gui.drawRect(j, k, j + 16, k + 16, 822018048);
            }
            GlStateManager.disableLighting();
            final ItemStack itemstack = ghostrecipe$ghostingredient.func_194184_c();
            final RenderItem renderitem = p_194188_1_.getRenderItem();
            renderitem.renderItemAndEffectIntoGUI(p_194188_1_.player, itemstack, j, k);
            GlStateManager.depthFunc(516);
            Gui.drawRect(j, k, j + 16, k + 16, 822083583);
            GlStateManager.depthFunc(515);
            if (i == 0) {
                renderitem.renderItemOverlays(p_194188_1_.fontRendererObj, itemstack, j, k);
            }
            GlStateManager.enableLighting();
        }
        RenderHelper.disableStandardItemLighting();
    }
    
    public class GhostIngredient
    {
        private final Ingredient field_194186_b;
        private final int field_192678_b;
        private final int field_192679_c;
        
        public GhostIngredient(final Ingredient p_i47604_2_, final int p_i47604_3_, final int p_i47604_4_) {
            this.field_194186_b = p_i47604_2_;
            this.field_192678_b = p_i47604_3_;
            this.field_192679_c = p_i47604_4_;
        }
        
        public int func_193713_b() {
            return this.field_192678_b;
        }
        
        public int func_193712_c() {
            return this.field_192679_c;
        }
        
        public ItemStack func_194184_c() {
            final ItemStack[] aitemstack = this.field_194186_b.func_193365_a();
            return aitemstack[MathHelper.floor(GhostRecipe.this.field_194190_c / 30.0f) % aitemstack.length];
        }
    }
}
