// 
// Decompiled by Procyon v0.6.0
// 

package net.labymod.gui;

import java.util.Iterator;
import java.util.List;
import java.io.IOException;
import net.labymod.core.LabyModCore;
import net.minecraft.client.gui.GuiButton;
import net.labymod.main.lang.LanguageManager;
import net.labymod.main.LabyMod;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.I18n;
import net.labymod.support.util.Debug;
import net.labymod.utils.ModColor;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.gui.GuiScreen;

public class GuiWebPanel extends GuiScreen
{
    private GuiScreen multiplayerScreen;
    private String pin;
    private GuiTextField field;
    
    public static void open(String message, final GuiScreen multiplayerScreen) {
        message = ModColor.removeColor(message);
        Debug.log(Debug.EnumDebugMode.MINECRAFT, "Disconnected: " + message);
        if (message.equals(I18n.format("disconnect.loginFailedInfo", I18n.format("disconnect.loginFailedInfo.invalidSession", new Object[0])))) {
            Minecraft.getMinecraft().displayGuiScreen(new GuiRefreshSession(multiplayerScreen));
            return;
        }
        if (!message.contains("Created PIN for ") && !message.contains("You need LabyMod to register")) {
            return;
        }
        if (!message.contains(":")) {
            return;
        }
        Minecraft.getMinecraft().displayGuiScreen(new GuiWebPanel(message.split("\n")[1], multiplayerScreen));
    }
    
    public GuiWebPanel(final String pin, final GuiScreen multiplayerScreen) {
        this.pin = pin;
        this.multiplayerScreen = multiplayerScreen;
        LabyMod.getInstance().openWebpage(String.format("http://www.labymod.net/key/?id=%s&pin=%s", LabyMod.getInstance().getPlayerUUID().toString(), this.pin), false);
    }
    
    @Override
    public void initGui() {
        this.buttonList.clear();
        this.buttonList.add(new GuiButton(6, GuiWebPanel.width / 2 - 120, GuiWebPanel.height / 2 + 10, 100, 20, LanguageManager.translate("button_not_working")));
        this.buttonList.add(new GuiButton(5, GuiWebPanel.width / 2 - 10, GuiWebPanel.height / 2 + 10, 130, 20, LanguageManager.translate("button_okay")));
        (this.field = new GuiTextField(0, LabyModCore.getMinecraft().getFontRenderer(), GuiWebPanel.width / 2 - 100, GuiWebPanel.height / 2 + 35, 200, 20)).setVisible(false);
        this.field.setMaxStringLength(640);
        this.field.setText(String.format("http://www.labymod.net/key/?id=%s&pin=%s", LabyMod.getInstance().getPlayerUUID().toString(), this.pin));
        super.initGui();
    }
    
    @Override
    protected void actionPerformed(final GuiButton button) throws IOException {
        if (button.id == 5) {
            Minecraft.getMinecraft().displayGuiScreen(this.multiplayerScreen);
        }
        if (button.id == 6) {
            this.field.setVisible(true);
            this.field.setFocused(true);
            this.field.setCursorPositionZero();
            this.field.setSelectionPos(this.field.getMaxStringLength() - 1);
            button.enabled = false;
        }
        super.actionPerformed(button);
    }
    
    @Override
    protected void keyTyped(final char typedChar, final int keyCode) throws IOException {
        this.field.textboxKeyTyped(typedChar, keyCode);
        super.keyTyped(typedChar, keyCode);
    }
    
    public void mouseClicked(final int mouseX, final int mouseY, final int mouseButton) throws IOException {
        this.field.mouseClicked(mouseX, mouseY, mouseButton);
        super.mouseClicked(mouseX, mouseY, mouseButton);
    }
    
    @Override
    public void drawScreen(final int mouseX, final int mouseY, final float partialTicks) {
        this.drawBackground(0);
        final List<String> list = LabyMod.getInstance().getDrawUtils().listFormattedStringToWidth(LanguageManager.translate("tab_opened"), GuiWebPanel.width / 3, 4);
        int y = 0;
        for (final String s : list) {
            LabyMod.getInstance().getDrawUtils().drawCenteredString(String.valueOf(ModColor.cl("a")) + s, GuiWebPanel.width / 2, GuiWebPanel.height / 2 - 40 + y);
            y += 10;
        }
        this.field.drawTextBox();
        if (this.field.visible) {
            LabyMod.getInstance().getDrawUtils().drawCenteredString(String.valueOf(ModColor.cl("c")) + LanguageManager.translate("open_link_in_browser"), GuiWebPanel.width / 2, GuiWebPanel.height / 2 + 60);
        }
        super.drawScreen(mouseX, mouseY, partialTicks);
    }
}
