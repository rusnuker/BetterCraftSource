// 
// Decompiled by Procyon v0.6.0
// 

package net.minecraft.client.gui;

import java.io.IOException;
import net.minecraft.client.resources.I18n;

public class GuiErrorScreen extends GuiScreen
{
    private final String title;
    private final String message;
    
    public GuiErrorScreen(final String titleIn, final String messageIn) {
        this.title = titleIn;
        this.message = messageIn;
    }
    
    @Override
    public void initGui() {
        super.initGui();
        this.buttonList.add(new GuiButton(0, GuiErrorScreen.width / 2 - 100, 140, I18n.format("gui.cancel", new Object[0])));
    }
    
    @Override
    public void drawScreen(final int mouseX, final int mouseY, final float partialTicks) {
        this.drawDefaultBackground();
        Gui.drawCenteredString(this.fontRendererObj, this.title, GuiErrorScreen.width / 2, 90, 16777215);
        Gui.drawCenteredString(this.fontRendererObj, this.message, GuiErrorScreen.width / 2, 110, 16777215);
        super.drawScreen(mouseX, mouseY, partialTicks);
    }
    
    @Override
    protected void keyTyped(final char typedChar, final int keyCode) throws IOException {
    }
    
    @Override
    protected void actionPerformed(final GuiButton button) throws IOException {
        this.mc.displayGuiScreen(null);
    }
}
