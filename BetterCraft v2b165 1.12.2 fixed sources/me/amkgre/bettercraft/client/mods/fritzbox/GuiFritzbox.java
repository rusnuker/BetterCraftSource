// 
// Decompiled by Procyon v0.6.0
// 

package me.amkgre.bettercraft.client.mods.fritzbox;

import net.minecraft.client.gui.Gui;
import org.lwjgl.opengl.GL11;
import java.io.IOException;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;

public class GuiFritzbox extends GuiScreen
{
    private Minecraft mc;
    private GuiScreen before;
    public static String renderText;
    
    static {
        GuiFritzbox.renderText = "";
    }
    
    public GuiFritzbox(final GuiScreen before) {
        this.mc = Minecraft.getMinecraft();
        this.before = before;
    }
    
    @Override
    public void initGui() {
        GuiFritzbox.renderText = "";
        this.buttonList.add(new GuiButton(0, GuiFritzbox.width / 2 - 100, GuiFritzbox.height / 4 + 140, "Back"));
        this.buttonList.add(new GuiButton(1, GuiFritzbox.width / 2 - 100, GuiFritzbox.height / 4 + 115, "§5Reconnect"));
    }
    
    @Override
    protected void actionPerformed(final GuiButton button) throws IOException {
        final int id = button.id;
        if (id == 0) {
            this.mc.displayGuiScreen(this.before);
        }
        if (id == 1) {
            FritzboxReconnector.reconnectFritzBox();
            GuiFritzbox.renderText = "§aSuccessful";
        }
    }
    
    @Override
    public boolean doesGuiPauseGame() {
        return true;
    }
    
    @Override
    public void drawScreen(final int mouseX, final int mouseY, final float partialTicks) {
        this.drawDefaultBackground();
        GL11.glPushMatrix();
        GL11.glColor4d(1.0, 1.0, 1.0, 1.0);
        GL11.glScaled(4.0, 4.0, 4.0);
        Gui.drawCenteredString(this.mc.fontRendererObj, GuiFritzbox.renderText, GuiFritzbox.width / 8, GuiFritzbox.height / 4 - this.mc.fontRendererObj.FONT_HEIGHT, 0);
        GL11.glPopMatrix();
        super.drawScreen(mouseX, mouseY, partialTicks);
    }
}
