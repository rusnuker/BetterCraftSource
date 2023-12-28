// 
// Decompiled by Procyon v0.6.0
// 

package me.amkgre.bettercraft.client.mods.proxy;

import java.io.IOException;
import org.lwjgl.input.Keyboard;
import net.minecraft.client.gui.Gui;
import org.lwjgl.opengl.GL11;
import net.minecraft.client.gui.ScaledResolution;
import java.net.Proxy;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.gui.GuiScreen;

public final class GuiProxy extends GuiScreen
{
    static GuiTextField ip;
    static GuiScreen before;
    private static boolean isRunning;
    private GuiButton button;
    private String status;
    public static String renderText;
    
    static {
        GuiProxy.renderText = "";
    }
    
    public GuiProxy(final GuiScreen before) {
        GuiProxy.before = before;
    }
    
    @Override
    protected void actionPerformed(final GuiButton button) {
        switch (button.id) {
            case 1: {
                this.mc.displayGuiScreen(GuiProxy.before);
                break;
            }
            case 0: {
                if (GuiProxy.isRunning) {
                    GuiProxy.isRunning = false;
                    button.displayString = "§aConnect";
                    ProxyManager.setProxy(null);
                    break;
                }
                final String[] split = GuiProxy.ip.getText().split(":");
                if (split.length == 2) {
                    ProxyManager.setProxy(ProxyManager.getProxyFromString(GuiProxy.ip.getText()));
                    this.status = "§5Proxy used " + ProxyManager.getProxy().address().toString();
                    GuiProxy.renderText = "§aSuccessful";
                    GuiProxy.isRunning = true;
                    button.displayString = "§cDisconnect";
                    break;
                }
                this.status = "§cPlease use: <host>:<port>";
                break;
            }
        }
    }
    
    @Override
    public void drawScreen(final int x, final int y, final float z) {
        final ScaledResolution scaledRes = new ScaledResolution(this.mc);
        this.drawDefaultBackground();
        GL11.glPushMatrix();
        GL11.glColor4d(1.0, 1.0, 1.0, 1.0);
        GL11.glScaled(4.0, 4.0, 4.0);
        Gui.drawCenteredString(this.mc.fontRendererObj, GuiProxy.renderText, GuiProxy.width / 8, GuiProxy.height / 4 - this.mc.fontRendererObj.FONT_HEIGHT, 0);
        GL11.glPopMatrix();
        Gui.drawCenteredString(this.mc.fontRendererObj, this.status, GuiProxy.width / 2, 20, -1);
        GuiProxy.ip.drawTextBox();
        Gui.drawCenteredString(this.mc.fontRendererObj, "§7Proxy IP:Port", GuiProxy.width / 2, 50, -1);
        super.drawScreen(x, y, z);
    }
    
    @Override
    public void initGui() {
        GuiProxy.renderText = "";
        this.buttonList.add(this.button = new GuiButton(0, GuiProxy.width / 2 - 100, GuiProxy.height / 3 + 40, 200, 20, GuiProxy.isRunning ? "§cDisconnect" : "§aConnect"));
        this.buttonList.add(new GuiButton(1, GuiProxy.width / 2 - 100, GuiProxy.height / 3 + 66, 200, 20, "Back"));
        (GuiProxy.ip = new GuiTextField(GuiProxy.height, this.mc.fontRendererObj, GuiProxy.width / 2 - 100, 60, 200, 20)).setMaxStringLength(100);
        GuiProxy.ip.setText("127.0.0.1:8080");
        this.status = "§5Waiting...";
        GuiProxy.ip.setFocused(true);
        Keyboard.enableRepeatEvents(true);
    }
    
    @Override
    protected void keyTyped(final char character, final int key) {
        try {
            super.keyTyped(character, key);
        }
        catch (final IOException e) {
            e.printStackTrace();
        }
        if (character == '\r') {
            this.actionPerformed(this.buttonList.get(0));
        }
        GuiProxy.ip.textboxKeyTyped(character, key);
    }
    
    @Override
    protected void mouseClicked(final int x, final int y, final int button) {
        try {
            super.mouseClicked(x, y, button);
        }
        catch (final IOException e) {
            e.printStackTrace();
        }
        GuiProxy.ip.mouseClicked(x, y, button);
    }
    
    @Override
    public void onGuiClosed() {
        Keyboard.enableRepeatEvents(false);
    }
    
    @Override
    public void updateScreen() {
        GuiProxy.ip.updateCursorCounter();
    }
}
