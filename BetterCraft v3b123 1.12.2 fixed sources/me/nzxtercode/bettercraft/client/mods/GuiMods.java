// 
// Decompiled by Procyon v0.6.0
// 

package me.nzxtercode.bettercraft.client.mods;

import java.io.IOException;
import org.apache.commons.lang3.StringUtils;
import me.nzxtercode.bettercraft.client.hud.IRender;
import me.nzxtercode.bettercraft.client.hud.HUDManager;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;

public class GuiMods extends GuiScreen
{
    public GuiScreen before;
    
    public GuiMods(final GuiScreen screen) {
        this.before = screen;
    }
    
    @Override
    public void initGui() {
        this.buttonList.clear();
        this.buttonList.add(new GuiButton(1337, GuiMods.width - 93, GuiMods.height - 23, 90, 20, "Back"));
        int index = 0;
        int x = 0;
        for (int y = 0; index < HUDManager.getInstance().getRegisteredRenderers().size(); ++index, y += 25) {
            final IRender renderer = HUDManager.getInstance().getRegisteredRenderers().get(index);
            if (renderer instanceof ModRender) {
                this.buttonList.add(new GuiButton(index, 3 + x, 3 + y, 98, 20, String.format("%s%s%s", '§', renderer.isEnabled() ? 'a' : 'c', StringUtils.join((Object[])StringUtils.splitByCharacterTypeCamelCase(renderer.getClass().getSimpleName().replace("Mod", "").replaceAll("\\d+", "")), " "))));
                if (y + 56 >= GuiMods.height) {
                    y = -25;
                    x += 101;
                }
            }
        }
    }
    
    @Override
    protected void actionPerformed(final GuiButton button) throws IOException {
        if (button.id == 1337) {
            this.mc.displayGuiScreen(this.before);
        }
        for (int index = 0; index < HUDManager.getInstance().getRegisteredRenderers().size(); ++index) {
            final IRender renderer = HUDManager.getInstance().getRegisteredRenderers().get(index);
            if (button.id == index) {
                ((ModRender)renderer).setEnabledState(!renderer.isEnabled());
                button.displayString = String.format("%s%s%s", '§', renderer.isEnabled() ? 'a' : 'c', StringUtils.join((Object[])StringUtils.splitByCharacterTypeCamelCase(renderer.getClass().getSimpleName().replace("Mod", "").replaceAll("\\d+", "")), " "));
            }
        }
    }
    
    @Override
    public void drawScreen(final int mouseX, final int mouseY, final float partialTicks) {
        this.drawDefaultBackground();
        super.drawScreen(mouseX, mouseY, partialTicks);
    }
}
