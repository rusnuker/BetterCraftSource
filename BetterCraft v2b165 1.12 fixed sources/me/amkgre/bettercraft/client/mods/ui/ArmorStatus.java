// 
// Decompiled by Procyon v0.6.0
// 

package me.amkgre.bettercraft.client.mods.ui;

import java.util.ArrayList;
import me.amkgre.bettercraft.client.utils.ColorUtils;
import net.minecraft.client.gui.ScaledResolution;
import org.lwjgl.opengl.GL11;
import net.minecraft.item.ItemStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;

public class ArmorStatus extends Gui
{
    private static Minecraft mc;
    
    static {
        ArmorStatus.mc = Minecraft.getMinecraft();
    }
    
    public static void render() {
        renderItemStack(4, ArmorStatus.mc.player.inventory.getCurrentItem());
        for (int i = 0; i < Minecraft.getMinecraft().player.inventory.armorInventory.size(); ++i) {
            final ItemStack itemStack = Minecraft.getMinecraft().player.inventory.armorInventory.get(i);
            renderItemStack(i, itemStack);
        }
    }
    
    private static void renderItemStack(final int i, final ItemStack is) {
        if (is == null) {
            return;
        }
        GL11.glPushMatrix();
        final int yAdd = -16 * i + 48;
        final ScaledResolution sr = new ScaledResolution(ArmorStatus.mc);
        if (is.getItem().isDamageable()) {
            final double damage = (is.getMaxDamage() - is.getItemDamage()) / (double)is.getMaxDamage() * 100.0;
            Gui.drawString(ArmorStatus.mc.fontRendererObj, String.format("%.2f%%", damage), ScaledResolution.getScaledWidth() / 2 + 70 - 55 * i, ScaledResolution.getScaledHeight() - 86, ColorUtils.rainbowEffect(0L, 1.0f).getRGB());
        }
        ArmorStatus.mc.getRenderItem().renderItemAndEffectIntoGUI(is, ScaledResolution.getScaledWidth() / 2 + 55 - 55 * i, ScaledResolution.getScaledHeight() - 90);
        GL11.glPopMatrix();
    }
    
    public static ArrayList<ItemStack> getItemsInInventory() {
        final ArrayList<ItemStack> items = new ArrayList<ItemStack>();
        for (int i = 8; i < 45; ++i) {
            if (Minecraft.getMinecraft().player.inventoryContainer.getSlot(i) != null) {
                items.add(Minecraft.getMinecraft().player.inventoryContainer.getSlot(i).getStack());
            }
        }
        return items;
    }
    
    public static ArrayList<ItemStack> getItemsInArmor() {
        final ArrayList<ItemStack> items = new ArrayList<ItemStack>();
        for (int i = 0; i < 3; ++i) {
            if (Minecraft.getMinecraft().player.inventoryContainer.getSlot(i) != null) {
                items.add(Minecraft.getMinecraft().player.inventoryContainer.getSlot(i).getStack());
            }
        }
        return items;
    }
}
