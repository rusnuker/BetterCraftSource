// 
// Decompiled by Procyon v0.6.0
// 

package me.nzxtercode.bettercraft.client.mods.impl;

import net.minecraft.client.renderer.RenderHelper;
import me.nzxtercode.bettercraft.client.utils.ColorUtils;
import org.lwjgl.opengl.GL11;
import net.minecraft.item.Item;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import me.nzxtercode.bettercraft.client.hud.ScreenPosition;
import me.nzxtercode.bettercraft.client.mods.ModRender;

public class ModArmorStatus extends ModRender
{
    @Override
    public int getWidth() {
        return 64;
    }
    
    @Override
    public int getHeight() {
        return 84;
    }
    
    @Override
    public void render(final ScreenPosition pos) {
        this.renderItemStack(pos, 4, this.mc.thePlayer.inventory.getCurrentItem());
        this.font.drawString(new String(), 0, 0, -1);
        for (int i = 0; i < this.mc.thePlayer.inventory.armorInventory.length; ++i) {
            final ItemStack itemStack = this.mc.thePlayer.inventory.armorInventory[i];
            this.renderItemStack(pos, i, itemStack);
        }
    }
    
    @Override
    public void renderDummy(final ScreenPosition pos) {
        this.renderItemStack(pos, 4, new ItemStack(Items.diamond_sword));
        this.renderItemStack(pos, 3, new ItemStack(Items.diamond_helmet));
        this.renderItemStack(pos, 2, new ItemStack(Items.diamond_chestplate));
        this.renderItemStack(pos, 1, new ItemStack(Items.diamond_leggings));
        this.renderItemStack(pos, 0, new ItemStack(Items.diamond_boots));
    }
    
    private void renderItemStack(final ScreenPosition pos, final int i, final ItemStack is) {
        if (is == null) {
            return;
        }
        GL11.glPushMatrix();
        final int yAdd = -16 * i + 68;
        if (is.getItem().isDamageable()) {
            final double damage = (is.getMaxDamage() - is.getItemDamage()) / (double)is.getMaxDamage() * 100.0;
            this.font.drawString(String.format("%.2f%%", damage), pos.getAbsoluteX() + 22, pos.getAbsoluteY() + yAdd + 5, ColorUtils.rainbowEffect());
        }
        RenderHelper.enableGUIStandardItemLighting();
        this.mc.getRenderItem().renderItemAndEffectIntoGUI(is, pos.getAbsoluteX() + 4, pos.getAbsoluteY() + yAdd);
        GL11.glPopMatrix();
    }
}
