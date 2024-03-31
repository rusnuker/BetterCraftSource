// 
// Decompiled by Procyon v0.6.0
// 

package net.labymod.main.listeners;

import net.lenni0451.eventapi.events.EventTarget;
import net.labymod.settings.PreviewRenderer;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.Minecraft;
import net.labymod.main.LabyMod;
import net.lenni0451.eventapi.manager.ASMEventManager;

public class RenderTickListener
{
    public RenderTickListener() {
        ASMEventManager.register(this);
    }
    
    @EventTarget
    public void handleEvent(final RenderTickListener event) {
        LabyMod.getInstance().getDrawUtils().setScaledResolution(new ScaledResolution(Minecraft.getMinecraft()));
        if (Minecraft.getMinecraft().currentScreen != null) {
            PreviewRenderer.getInstance().createFrame();
        }
    }
    
    public void drawMenuOverlay(final int mouseX, final int mouseY, final float partialTicks) {
        if (!LabyMod.getInstance().isInGame()) {
            LabyMod.getInstance().getGuiCustomAchievement().updateAchievementWindow();
        }
    }
}
