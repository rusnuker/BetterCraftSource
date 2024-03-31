// 
// Decompiled by Procyon v0.6.0
// 

package me.nzxtercode.bettercraft.client.gui;

import java.io.IOException;
import me.nzxtercode.bettercraft.client.misc.cosmetics.GuiCosmetic;
import me.nzxtercode.bettercraft.client.gui.section.GuiUISettings;
import me.nzxtercode.bettercraft.client.gui.section.GuiMisc;
import me.nzxtercode.bettercraft.client.mods.GuiMods;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;

public class GuiRShift extends GuiScreen
{
    private GuiScreen parent;
    
    public GuiRShift(final GuiScreen parent) {
        this.parent = parent;
    }
    
    @Override
    public void initGui() {
        this.buttonList.add(new GuiButton(0, GuiRShift.width / 2 - 100, GuiRShift.height / 3 + 60, 200, 20, "Back"));
        this.buttonList.add(new GuiButton(1, GuiRShift.width / 2 - 100, GuiRShift.height / 3 - 5, 98, 20, "Mods"));
        this.buttonList.add(new GuiButton(2, GuiRShift.width / 2 + 2, GuiRShift.height / 3 - 5, 98, 20, "Misc"));
        this.buttonList.add(new GuiButton(3, GuiRShift.width / 2 - 100, GuiRShift.height / 3 + 20, 98, 20, "UI"));
        this.buttonList.add(new GuiButton(4, GuiRShift.width / 2 + 2, GuiRShift.height / 3 + 20, 98, 20, "Cosmetics"));
    }
    
    @Override
    protected void actionPerformed(final GuiButton button) throws IOException {
        switch (button.id) {
            case 0: {
                this.mc.displayGuiScreen(this.parent);
                break;
            }
            case 1: {
                this.mc.displayGuiScreen(new GuiMods(this));
                break;
            }
            case 2: {
                this.mc.displayGuiScreen(new GuiMisc(this));
                break;
            }
            case 3: {
                this.mc.displayGuiScreen(new GuiUISettings(this));
                break;
            }
            case 4: {
                this.mc.displayGuiScreen(new GuiCosmetic(this));
                break;
            }
        }
    }
    
    @Override
    public void drawScreen(final int mouseX, final int mouseY, final float partialTicks) {
        this.drawDefaultBackground();
        super.drawScreen(mouseX, mouseY, partialTicks);
    }
}
