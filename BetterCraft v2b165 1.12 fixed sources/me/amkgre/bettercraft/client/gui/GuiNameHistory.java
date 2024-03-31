// 
// Decompiled by Procyon v0.6.0
// 

package me.amkgre.bettercraft.client.gui;

import net.minecraft.client.gui.Gui;
import java.io.IOException;
import net.minecraft.client.gui.GuiChat;
import me.amkgre.bettercraft.client.utils.NameUtils;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.gui.GuiScreen;

public class GuiNameHistory extends GuiScreen
{
    public GuiTextField name;
    public String[] loadedNames;
    
    @Override
    public void initGui() {
        this.buttonList.clear();
        this.buttonList.add(new GuiButton(1, GuiNameHistory.width / 2 - 1 - 100, GuiNameHistory.height / 4 + 72 + 12, 100, 20, "Start"));
        this.buttonList.add(new GuiButton(4, GuiNameHistory.width / 2 + 1, GuiNameHistory.height / 4 + 72 + 12, 100, 20, "Clear"));
        this.buttonList.add(new GuiButton(3, GuiNameHistory.width / 2 - 100, GuiNameHistory.height / 4 + 96 + 12, "Back"));
        (this.name = new GuiTextField(2, this.fontRendererObj, GuiNameHistory.width / 2 - 100, GuiNameHistory.height / 2 - 70, 200, 20)).setMaxStringLength(16);
    }
    
    @Override
    protected void actionPerformed(final GuiButton button) throws IOException {
        switch (button.id) {
            case 1: {
                if (this.name.getText().isEmpty()) {
                    break;
                }
                this.loadedNames = NameUtils.getNameHistory(NameUtils.getUUID(this.name.getText().trim()));
                break;
            }
            case 3: {
                if (this.mc.world != null) {
                    this.mc.displayGuiScreen(new GuiChat());
                    break;
                }
                this.mc.displayGuiScreen(null);
                break;
            }
            case 4: {
                this.loadedNames = null;
                break;
            }
        }
    }
    
    @Override
    public void updateScreen() {
        this.name.updateCursorCounter();
    }
    
    @Override
    protected void keyTyped(final char typedChar, final int keyCode) throws IOException {
        this.name.textboxKeyTyped(typedChar, keyCode);
        super.keyTyped(typedChar, keyCode);
    }
    
    @Override
    protected void mouseClicked(final int mouseX, final int mouseY, final int mouseButton) throws IOException {
        this.name.mouseClicked(mouseX, mouseY, mouseButton);
        super.mouseClicked(mouseX, mouseY, mouseButton);
    }
    
    @Override
    public void drawScreen(final int mouseX, final int mouseY, final float partialTicks) {
        this.drawDefaultBackground();
        int y = 1;
        if (this.loadedNames != null) {
            String[] loadedNames;
            for (int length = (loadedNames = this.loadedNames).length, i = 0; i < length; ++i) {
                final String s2 = loadedNames[i];
                Gui.drawCenteredString(this.fontRendererObj, "§7" + s2, GuiNameHistory.width / 2, y + 10, -1);
                y += 10;
            }
        }
        this.name.drawTextBox();
        super.drawScreen(mouseX, mouseY, partialTicks);
    }
}
