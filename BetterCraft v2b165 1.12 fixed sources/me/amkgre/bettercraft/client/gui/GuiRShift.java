// 
// Decompiled by Procyon v0.6.0
// 

package me.amkgre.bettercraft.client.gui;

import net.minecraft.client.renderer.GlStateManager;
import java.io.IOException;
import me.amkgre.bettercraft.client.mods.cosmetics.GuiCosmetics;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;

public class GuiRShift extends GuiScreen
{
    private Minecraft mc;
    private GuiScreen before;
    
    public GuiRShift(final GuiScreen before) {
        this.mc = Minecraft.getMinecraft();
        this.before = before;
    }
    
    @Override
    public void initGui() {
        this.buttonList.add(new GuiButton(0, GuiRShift.width / 2 - 100, GuiRShift.height / 3 + 20, 98, 20, "UI"));
        this.buttonList.add(new GuiButton(9, GuiRShift.width / 2 + 2, GuiRShift.height / 3 + 20, 98, 20, "Cosmetics"));
        this.buttonList.add(new GuiButton(10, GuiRShift.width / 2 - 100, GuiRShift.height / 3 + 60, 200, 20, "Back"));
    }
    
    @Override
    protected void actionPerformed(final GuiButton button) throws IOException {
        final int id = button.id;
        if (id == 0) {
            this.mc.displayGuiScreen(new GuiClientUI(this));
        }
        if (id == 9) {
            this.mc.displayGuiScreen(new GuiCosmetics(this));
        }
        if (id == 10) {
            this.mc.displayGuiScreen(this.before);
        }
    }
    
    @Override
    public boolean doesGuiPauseGame() {
        return true;
    }
    
    @Override
    public void drawScreen(final int mouseX, final int mouseY, final float partialTicks) {
        this.drawDefaultBackground();
        GlStateManager.scale(4.0f, 4.0f, 1.0f);
        GlStateManager.scale(0.5, 0.5, 1.0);
        GlStateManager.scale(0.5, 0.5, 1.0);
        super.drawScreen(mouseX, mouseY, partialTicks);
    }
}
