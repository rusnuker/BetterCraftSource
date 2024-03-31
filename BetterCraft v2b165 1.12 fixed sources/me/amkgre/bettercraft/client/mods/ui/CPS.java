// 
// Decompiled by Procyon v0.6.0
// 

package me.amkgre.bettercraft.client.mods.ui;

import org.lwjgl.input.Mouse;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;

public class CPS extends Gui
{
    private static Minecraft mc;
    private static List<Long> clicks;
    private static boolean wasPressed;
    private static long lastPressed;
    
    static {
        CPS.mc = Minecraft.getMinecraft();
        CPS.clicks = new ArrayList<Long>();
    }
    
    public static int getCPS() {
        final long time = System.currentTimeMillis();
        CPS.clicks.removeIf(aLong -> aLong + 1000L < n);
        return CPS.clicks.size();
    }
    
    public static void render() {
        final boolean pressed = Mouse.isButtonDown(0);
        if (pressed != CPS.wasPressed) {
            CPS.lastPressed = System.currentTimeMillis();
            if (CPS.wasPressed = pressed) {
                CPS.clicks.add(CPS.lastPressed);
            }
        }
    }
}
