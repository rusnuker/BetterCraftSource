// 
// Decompiled by Procyon v0.6.0
// 

package net.labymod.gui;

import java.io.IOException;
import net.labymod.settings.LabyModSettingsGui;
import net.minecraft.client.gui.GuiMainMenu;
import net.labymod.core.LabyModCore;
import net.labymod.main.LabyMod;
import net.minecraft.client.gui.GuiButton;
import net.labymod.main.lang.LanguageManager;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.client.gui.GuiScreen;
import net.labymod.main.Source;
import net.minecraft.client.gui.GuiOptions;

public class ModGuiOptions extends GuiOptions
{
    private static final boolean MC18;
    
    static {
        MC18 = Source.ABOUT_MC_VERSION.startsWith("1.8");
    }
    
    public ModGuiOptions(final GuiScreen p_i1046_1_, final GameSettings p_i1046_2_) {
        super(p_i1046_1_, p_i1046_2_);
    }
    
    @Override
    public void initGui() {
        super.initGui();
        this.buttonList.add(new GuiButton(205, ModGuiOptions.width / 2 + 5, ModGuiOptions.height / 6 + 24 - 6, 150, 20, LanguageManager.translate("settings_title")));
        if (!ModGuiOptions.MC18 && LabyMod.getSettings().betterShaderSelection) {
            this.buttonList.add(new GuiButton(8675309, ModGuiOptions.width / 2 - 155, ModGuiOptions.height / 6 + 48 - 6 - 24, 150, 20, "Super Secret Settings..."));
        }
    }
    
    @Override
    protected void actionPerformed(final GuiButton button) throws IOException {
        if (button.id == 200) {
            if (LabyModCore.getMinecraft().getPlayer() != null) {
                this.mc.displayGuiScreen(new ModGuiIngameMenu());
            }
            else {
                this.mc.displayGuiScreen(new GuiMainMenu());
            }
            return;
        }
        if (button.id == 8675309 && LabyMod.getSettings().betterShaderSelection) {
            this.mc.displayGuiScreen(new GuiShaderSelection(this));
            return;
        }
        if (button.id == 205) {
            this.mc.displayGuiScreen(new LabyModSettingsGui(this));
            return;
        }
        super.actionPerformed(button);
    }
}
