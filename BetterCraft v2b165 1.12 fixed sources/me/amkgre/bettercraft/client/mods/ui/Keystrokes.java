// 
// Decompiled by Procyon v0.6.0
// 

package me.amkgre.bettercraft.client.mods.ui;

import net.minecraft.client.gui.Gui;
import java.awt.Color;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.Minecraft;

public class Keystrokes
{
    private static Minecraft mc;
    
    static {
        Keystrokes.mc = Minecraft.getMinecraft();
    }
    
    public static void render() {
        final ScaledResolution sr = new ScaledResolution(Keystrokes.mc);
        if (Keystrokes.mc.gameSettings.keyBindRight.pressed) {
            Gui.drawRect(ScaledResolution.getScaledWidth() - 2, ScaledResolution.getScaledHeight() - 52, ScaledResolution.getScaledWidth() - 20, ScaledResolution.getScaledHeight() - 72, Color.WHITE.getRGB());
        }
        Gui.drawRect(ScaledResolution.getScaledWidth() - 2, ScaledResolution.getScaledHeight() - 52, ScaledResolution.getScaledWidth() - 20, ScaledResolution.getScaledHeight() - 72, Integer.MIN_VALUE);
        if (Keystrokes.mc.gameSettings.keyBindBack.pressed) {
            Gui.drawRect(ScaledResolution.getScaledWidth() - 21, ScaledResolution.getScaledHeight() - 52, ScaledResolution.getScaledWidth() - 40, ScaledResolution.getScaledHeight() - 72, Color.WHITE.getRGB());
        }
        Gui.drawRect(ScaledResolution.getScaledWidth() - 21, ScaledResolution.getScaledHeight() - 52, ScaledResolution.getScaledWidth() - 40, ScaledResolution.getScaledHeight() - 72, Integer.MIN_VALUE);
        if (Keystrokes.mc.gameSettings.keyBindForward.pressed) {
            Gui.drawRect(ScaledResolution.getScaledWidth() - 21, ScaledResolution.getScaledHeight() - 73, ScaledResolution.getScaledWidth() - 40, ScaledResolution.getScaledHeight() - 93, Color.WHITE.getRGB());
        }
        Gui.drawRect(ScaledResolution.getScaledWidth() - 21, ScaledResolution.getScaledHeight() - 73, ScaledResolution.getScaledWidth() - 40, ScaledResolution.getScaledHeight() - 93, Integer.MIN_VALUE);
        if (Keystrokes.mc.gameSettings.keyBindLeft.pressed) {
            Gui.drawRect(ScaledResolution.getScaledWidth() - 41, ScaledResolution.getScaledHeight() - 72, ScaledResolution.getScaledWidth() - 60, ScaledResolution.getScaledHeight() - 52, Color.WHITE.getRGB());
        }
        Gui.drawRect(ScaledResolution.getScaledWidth() - 41, ScaledResolution.getScaledHeight() - 72, ScaledResolution.getScaledWidth() - 60, ScaledResolution.getScaledHeight() - 52, Integer.MIN_VALUE);
        if (Keystrokes.mc.gameSettings.keyBindUseItem.pressed) {
            Gui.drawRect(ScaledResolution.getScaledWidth() - 2, ScaledResolution.getScaledHeight() - 50, ScaledResolution.getScaledWidth() - 30, ScaledResolution.getScaledHeight() - 30, Color.WHITE.getRGB());
        }
        Gui.drawRect(ScaledResolution.getScaledWidth() - 2, ScaledResolution.getScaledHeight() - 50, ScaledResolution.getScaledWidth() - 30, ScaledResolution.getScaledHeight() - 30, Integer.MIN_VALUE);
        if (Keystrokes.mc.gameSettings.keyBindAttack.pressed) {
            Gui.drawRect(ScaledResolution.getScaledWidth() - 60, ScaledResolution.getScaledHeight() - 50, ScaledResolution.getScaledWidth() - 31, ScaledResolution.getScaledHeight() - 30, Color.WHITE.getRGB());
        }
        Gui.drawRect(ScaledResolution.getScaledWidth() - 60, ScaledResolution.getScaledHeight() - 50, ScaledResolution.getScaledWidth() - 31, ScaledResolution.getScaledHeight() - 30, Integer.MIN_VALUE);
        Keystrokes.mc.fontRendererObj.drawString("W", ScaledResolution.getScaledWidth() - 34, ScaledResolution.getScaledHeight() - 86, -1);
        Keystrokes.mc.fontRendererObj.drawString("A", ScaledResolution.getScaledWidth() - 53, ScaledResolution.getScaledHeight() - 65, -1);
        Keystrokes.mc.fontRendererObj.drawString("S", ScaledResolution.getScaledWidth() - 33, ScaledResolution.getScaledHeight() - 65, -1);
        Keystrokes.mc.fontRendererObj.drawString("D", ScaledResolution.getScaledWidth() - 13, ScaledResolution.getScaledHeight() - 65, -1);
        Keystrokes.mc.fontRendererObj.drawString("LMB", ScaledResolution.getScaledWidth() - 54, ScaledResolution.getScaledHeight() - 44, -1);
        Keystrokes.mc.fontRendererObj.drawString("RMB", ScaledResolution.getScaledWidth() - 25, ScaledResolution.getScaledHeight() - 44, -1);
    }
}
