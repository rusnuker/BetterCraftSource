// 
// Decompiled by Procyon v0.6.0
// 

package net.labymod.ingamechat.tabs;

import net.minecraft.client.multiplayer.ServerData;
import net.labymod.utils.ModUtils;
import net.minecraft.client.Minecraft;
import java.util.Iterator;
import net.labymod.main.lang.LanguageManager;
import org.lwjgl.input.Keyboard;
import net.labymod.utils.ModColor;
import net.minecraft.client.gui.Gui;
import java.io.IOException;
import org.lwjgl.input.Mouse;
import net.labymod.core.LabyModCore;
import net.labymod.main.LabyMod;
import net.minecraft.client.gui.GuiTextField;
import net.labymod.ingamechat.tools.autotext.AutoTextKeyBinds;
import net.labymod.gui.elements.Scrollbar;
import net.labymod.ingamechat.GuiChatCustom;

public class GuiChatAutoText extends GuiChatCustom
{
    private Scrollbar scrollbar;
    private AutoTextKeyBinds.AutoText selectedAutoText;
    private GuiTextField textFieldMessage;
    private GuiTextField textFieldAutoTextKeyCode;
    private GuiTextField textFieldAutoTextServerAddress;
    private boolean markMessageRed;
    private boolean markKeybindRed;
    private boolean canScroll;
    
    public GuiChatAutoText(final String defaultText) {
        super(defaultText);
        this.scrollbar = new Scrollbar(15);
        this.markMessageRed = false;
        this.markKeybindRed = false;
    }
    
    @Override
    public void initGui() {
        super.initGui();
        this.scrollbar.setPosition(GuiChatAutoText.width - 6, GuiChatAutoText.height - 196, GuiChatAutoText.width - 5, GuiChatAutoText.height - 20);
        this.scrollbar.update(LabyMod.getInstance().getChatToolManager().getAutoTextKeyBinds().size());
        this.scrollbar.setSpeed(10);
        this.scrollbar.setEntryHeight(10.0);
        this.textFieldMessage = new GuiTextField(0, LabyModCore.getMinecraft().getFontRenderer(), 0, 0, 110, 10);
        this.textFieldAutoTextKeyCode = new GuiTextField(0, LabyModCore.getMinecraft().getFontRenderer(), 0, 0, 110, 10);
        this.textFieldAutoTextServerAddress = new GuiTextField(0, LabyModCore.getMinecraft().getFontRenderer(), 0, -100, 110, 10);
    }
    
    @Override
    public void handleMouseInput() throws IOException {
        super.handleMouseInput();
        if (this.canScroll) {
            this.scrollbar.mouseInput();
            final int i = Mouse.getEventDWheel();
            if (i != 0) {
                this.mc.ingameGUI.getChatGUI().resetScroll();
            }
        }
    }
    
    @Override
    public void drawScreen(final int mouseX, final int mouseY, final float partialTicks) {
        super.drawScreen(mouseX, mouseY, partialTicks);
        this.scrollbar.calc();
        Gui.drawRect(GuiChatAutoText.width - 150, GuiChatAutoText.height - 200, GuiChatAutoText.width - 2, GuiChatAutoText.height - 16, Integer.MIN_VALUE);
        this.canScroll = (mouseX > GuiChatAutoText.width - 150 && mouseX < GuiChatAutoText.width - 2 && mouseY > GuiChatAutoText.height - 150 && mouseY < GuiChatAutoText.height - 16);
        int row = 0;
        for (final AutoTextKeyBinds.AutoText component : LabyMod.getInstance().getChatToolManager().getAutoTextKeyBinds()) {
            final double posY = GuiChatAutoText.height - 195 + row * 10 + this.scrollbar.getScrollY();
            ++row;
            if (posY >= GuiChatAutoText.height - 200) {
                if (posY > GuiChatAutoText.height - 25) {
                    continue;
                }
                final boolean hover = this.selectedAutoText == null && mouseX > GuiChatAutoText.width - 150 + 1 && mouseX < GuiChatAutoText.width - 2 - 1 && mouseY > posY - 1.0 && mouseY < posY + 9.0;
                if (hover || this.selectedAutoText == component) {
                    Gui.drawRect(GuiChatAutoText.width - 150 + 1, (int)posY - 1, GuiChatAutoText.width - 2 - 1, (int)posY + 9, hover ? ModColor.toRGB(100, 200, 200, 100) : Integer.MAX_VALUE);
                }
                this.drawString(LabyModCore.getMinecraft().getFontRenderer(), LabyMod.getInstance().getDrawUtils().trimStringToWidth("[" + ModColor.cl("a") + (component.isKeyShift() ? "SHIFT+" : "") + (component.isKeyAlt() ? "ALT+" : "") + (component.isKeyCtrl() ? "CTRL+" : "") + Keyboard.getKeyName(component.getKeyCode()) + ModColor.cl("r") + "] " + component.getMessage(), 130), GuiChatAutoText.width - 145, (int)posY, Integer.MAX_VALUE);
                if (this.selectedAutoText != null) {
                    continue;
                }
                final boolean hoverX = mouseX > GuiChatAutoText.width - 12 - 1 && mouseX < GuiChatAutoText.width - 12 + 7 && mouseY > posY && mouseY < posY + 8.0;
                this.drawString(LabyModCore.getMinecraft().getFontRenderer(), String.valueOf(ModColor.cl(hoverX ? "c" : "4")) + "\u2718", GuiChatAutoText.width - 12, (int)posY, Integer.MAX_VALUE);
            }
        }
        if (!this.scrollbar.isHidden()) {
            Gui.drawRect(GuiChatAutoText.width - 6, GuiChatAutoText.height - 145, GuiChatAutoText.width - 5, GuiChatAutoText.height - 20, Integer.MIN_VALUE);
            Gui.drawRect(GuiChatAutoText.width - 7, (int)this.scrollbar.getTop(), GuiChatAutoText.width - 4, (int)(this.scrollbar.getTop() + this.scrollbar.getBarLength()), Integer.MAX_VALUE);
        }
        if (this.selectedAutoText == null) {
            final boolean hover2 = mouseX > GuiChatAutoText.width - 165 && mouseX < GuiChatAutoText.width - 152 && mouseY > GuiChatAutoText.height - 200 && mouseY < GuiChatAutoText.height - 200 + 13;
            Gui.drawRect(GuiChatAutoText.width - 165, GuiChatAutoText.height - 200, GuiChatAutoText.width - 152, GuiChatAutoText.height - 200 + 13, hover2 ? Integer.MAX_VALUE : Integer.MIN_VALUE);
            Gui.drawCenteredString(LabyModCore.getMinecraft().getFontRenderer(), "+", GuiChatAutoText.width - 158, GuiChatAutoText.height - 197, hover2 ? ModColor.toRGB(50, 220, 120, 210) : Integer.MAX_VALUE);
        }
        else {
            Gui.drawRect(GuiChatAutoText.width - 270, GuiChatAutoText.height - 200, GuiChatAutoText.width - 152, GuiChatAutoText.height - 16, Integer.MIN_VALUE);
            final int relX = GuiChatAutoText.width - 270;
            final int relY = GuiChatAutoText.height - 200;
            this.drawElementTextField("message", this.textFieldMessage, relX, relY, mouseX, mouseY);
            this.drawElementTextField("key", this.textFieldAutoTextKeyCode, relX, relY + 23, mouseX, mouseY);
            this.drawElementCheckBox("with_shift", this.selectedAutoText.isKeyShift(), relX, relY + 46, mouseX, mouseY);
            this.drawElementCheckBox("with_alt", this.selectedAutoText.isKeyAlt(), relX, relY + 46 + 11, mouseX, mouseY);
            this.drawElementCheckBox("with_ctrl", this.selectedAutoText.isKeyCtrl(), relX, relY + 46 + 22, mouseX, mouseY);
            this.drawElementCheckBox("send_instantly", !this.selectedAutoText.isSendNotInstantly(), relX, relY + 46 + 33, mouseX, mouseY);
            this.drawElementCheckBox("server_bound", this.selectedAutoText.isServerBound(), relX, relY + 46 + 44, mouseX, mouseY);
            if (this.selectedAutoText.isServerBound()) {
                this.drawElementTextField("server_address", this.textFieldAutoTextServerAddress, relX, relY + 46 + 55, mouseX, mouseY);
            }
            final boolean hoverCancel = mouseX > GuiChatAutoText.width - 268 && mouseX < GuiChatAutoText.width - 213 && mouseY > GuiChatAutoText.height - 30 && mouseY < GuiChatAutoText.height - 18;
            final boolean hoverSave = mouseX > GuiChatAutoText.width - 210 && mouseX < GuiChatAutoText.width - 154 && mouseY > GuiChatAutoText.height - 30 && mouseY < GuiChatAutoText.height - 18;
            Gui.drawRect(GuiChatAutoText.width - 268, GuiChatAutoText.height - 30, GuiChatAutoText.width - 213, GuiChatAutoText.height - 18, hoverCancel ? ModColor.toRGB(200, 100, 100, 200) : Integer.MAX_VALUE);
            Gui.drawRect(GuiChatAutoText.width - 210, GuiChatAutoText.height - 30, GuiChatAutoText.width - 154, GuiChatAutoText.height - 18, hoverSave ? ModColor.toRGB(100, 200, 100, 200) : Integer.MAX_VALUE);
            Gui.drawCenteredString(LabyModCore.getMinecraft().getFontRenderer(), LanguageManager.translate("button_cancel"), GuiChatAutoText.width - 248 + 22 - 14, GuiChatAutoText.height - 30 + 2, Integer.MAX_VALUE);
            Gui.drawCenteredString(LabyModCore.getMinecraft().getFontRenderer(), LanguageManager.translate("button_save"), GuiChatAutoText.width - 200 + 23 - 4, GuiChatAutoText.height - 30 + 2, Integer.MAX_VALUE);
            this.textFieldMessage.drawTextBox();
            this.textFieldAutoTextKeyCode.drawTextBox();
            if (this.selectedAutoText != null && this.selectedAutoText.isServerBound()) {
                this.textFieldAutoTextServerAddress.drawTextBox();
            }
        }
        this.drawString(LabyModCore.getMinecraft().getFontRenderer(), LanguageManager.translate("ingame_chat_tab_autotext"), GuiChatAutoText.width - 150, GuiChatAutoText.height - 210, -1);
    }
    
    @Override
    public void mouseClicked(final int mouseX, final int mouseY, final int mouseButton) throws IOException {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        this.scrollbar.mouseAction(mouseX, mouseY, Scrollbar.EnumMouseAction.CLICKED);
        if (this.selectedAutoText == null && mouseX > GuiChatAutoText.width - 165 && mouseX < GuiChatAutoText.width - 152 && mouseY > GuiChatAutoText.height - 200 && mouseY < GuiChatAutoText.height - 200 + 13) {
            this.loadAutoText(new AutoTextKeyBinds.AutoText("", false, false, false, -1, false, false, ""));
        }
        if (this.selectedAutoText == null) {
            int row = 0;
            AutoTextKeyBinds.AutoText todoDelete = null;
            for (final AutoTextKeyBinds.AutoText component : LabyMod.getInstance().getChatToolManager().getAutoTextKeyBinds()) {
                final double posY = GuiChatAutoText.height - 195 + row * 10 + this.scrollbar.getScrollY();
                ++row;
                if (posY >= GuiChatAutoText.height - 200) {
                    if (posY > GuiChatAutoText.height - 25) {
                        continue;
                    }
                    if (mouseX > GuiChatAutoText.width - 12 - 1 && mouseX < GuiChatAutoText.width - 12 + 7 && mouseY > posY && mouseY < posY + 8.0) {
                        todoDelete = component;
                    }
                    else {
                        if (mouseX <= GuiChatAutoText.width - 150 + 1 || mouseX >= GuiChatAutoText.width - 2 - 1 || mouseY <= posY - 1.0) {
                            continue;
                        }
                        if (mouseY >= posY + 9.0) {
                            continue;
                        }
                        this.loadAutoText(component);
                    }
                }
            }
            if (todoDelete != null) {
                LabyMod.getInstance().getChatToolManager().getAutoTextKeyBinds().remove(todoDelete);
                LabyMod.getInstance().getChatToolManager().saveTools();
            }
        }
        else {
            final int relX = GuiChatAutoText.width - 270;
            final int relY = GuiChatAutoText.height - 200;
            if (this.isHoverElementCheckbox("with_alt", this.selectedAutoText.isKeyAlt(), relX, relY + 46 + 11, mouseX, mouseY)) {
                this.selectedAutoText.setKeyAlt(!this.selectedAutoText.isKeyAlt());
            }
            if (this.isHoverElementCheckbox("with_shift", this.selectedAutoText.isKeyShift(), relX, relY + 46, mouseX, mouseY)) {
                this.selectedAutoText.setKeyShift(!this.selectedAutoText.isKeyShift());
            }
            if (this.isHoverElementCheckbox("with_ctrl", this.selectedAutoText.isKeyCtrl(), relX, relY + 46 + 22, mouseX, mouseY)) {
                this.selectedAutoText.setKeyCtrl(!this.selectedAutoText.isKeyCtrl());
            }
            if (this.isHoverElementCheckbox("send_instantly", this.selectedAutoText.isSendNotInstantly(), relX, relY + 46 + 33, mouseX, mouseY)) {
                this.selectedAutoText.setSendNotInstantly(!this.selectedAutoText.isSendNotInstantly());
            }
            if (this.isHoverElementCheckbox("server_bound", this.selectedAutoText.isServerBound(), relX, relY + 46 + 44, mouseX, mouseY)) {
                this.selectedAutoText.setServerBound(!this.selectedAutoText.isServerBound());
                if (this.selectedAutoText.isServerBound() && this.textFieldAutoTextServerAddress.getText().isEmpty()) {
                    String value = "";
                    final ServerData serverData = Minecraft.getMinecraft().getCurrentServerData();
                    if (serverData == null && Minecraft.getMinecraft().isSingleplayer()) {
                        value = "singleplayer";
                    }
                    else {
                        value = serverData.serverIP;
                    }
                    this.textFieldAutoTextServerAddress.setText(ModUtils.getProfileNameByIp(value));
                }
            }
            final boolean hoverCancel = mouseX > GuiChatAutoText.width - 268 && mouseX < GuiChatAutoText.width - 213 && mouseY > GuiChatAutoText.height - 30 && mouseY < GuiChatAutoText.height - 18;
            final boolean hoverSave = mouseX > GuiChatAutoText.width - 210 && mouseX < GuiChatAutoText.width - 154 && mouseY > GuiChatAutoText.height - 30 && mouseY < GuiChatAutoText.height - 18;
            if (hoverCancel) {
                this.selectedAutoText = null;
            }
            if (hoverSave) {
                this.selectedAutoText.setMessage(this.textFieldMessage.getText());
                this.selectedAutoText.setServerAddress(this.textFieldAutoTextServerAddress.getText());
                if (this.selectedAutoText.getMessage().replaceAll(" ", "").isEmpty() || this.selectedAutoText.getKeyCode() == -1) {
                    this.markMessageRed = this.textFieldMessage.getText().replaceAll(" ", "").isEmpty();
                    this.markKeybindRed = (this.selectedAutoText.getKeyCode() == -1);
                }
                if (!this.markMessageRed && !this.markKeybindRed) {
                    if (!LabyMod.getInstance().getChatToolManager().getAutoTextKeyBinds().contains(this.selectedAutoText)) {
                        LabyMod.getInstance().getChatToolManager().getAutoTextKeyBinds().add(this.selectedAutoText);
                    }
                    LabyMod.getInstance().getChatToolManager().saveTools();
                    this.selectedAutoText = null;
                    this.initGui();
                }
            }
        }
    }
    
    @Override
    protected void keyTyped(final char typedChar, final int keyCode) throws IOException {
        if (this.selectedAutoText != null && this.textFieldMessage.isFocused()) {
            this.textFieldMessage.textboxKeyTyped(typedChar, keyCode);
            if (keyCode == 15) {
                this.textFieldMessage.setFocused(false);
                this.textFieldAutoTextKeyCode.setFocused(true);
            }
            return;
        }
        if (this.selectedAutoText != null && this.textFieldAutoTextServerAddress.isFocused()) {
            this.textFieldAutoTextServerAddress.textboxKeyTyped(typedChar, keyCode);
            if (keyCode == 15) {
                this.textFieldAutoTextServerAddress.setFocused(false);
                this.textFieldMessage.setFocused(true);
            }
            return;
        }
        if (this.selectedAutoText != null && keyCode != 56 && keyCode != 29 && keyCode != 42 && this.textFieldAutoTextKeyCode.isFocused()) {
            this.textFieldAutoTextKeyCode.setFocused(false);
            this.textFieldAutoTextKeyCode.setText(Keyboard.getKeyName(keyCode));
            this.selectedAutoText.setKeyCode(keyCode);
            this.selectedAutoText.setKeyAlt(Keyboard.isKeyDown(56));
            this.selectedAutoText.setKeyCtrl(Keyboard.isKeyDown(29));
            this.selectedAutoText.setKeyShift(Keyboard.isKeyDown(42));
            return;
        }
        super.keyTyped(typedChar, keyCode);
    }
    
    @Override
    protected void mouseClickMove(final int mouseX, final int mouseY, final int clickedMouseButton, final long timeSinceLastClick) {
        this.scrollbar.mouseAction(mouseX, mouseY, Scrollbar.EnumMouseAction.DRAGGING);
        super.mouseClickMove(mouseX, mouseY, clickedMouseButton, timeSinceLastClick);
    }
    
    @Override
    protected void mouseReleased(final int mouseX, final int mouseY, final int mouseButton) {
        super.mouseReleased(mouseX, mouseY, mouseButton);
        this.scrollbar.mouseAction(mouseX, mouseY, Scrollbar.EnumMouseAction.RELEASED);
        this.textFieldMessage.mouseClicked(mouseX, mouseY, mouseButton);
        this.textFieldAutoTextKeyCode.mouseClicked(mouseX, mouseY, mouseButton);
        if (this.selectedAutoText != null && this.selectedAutoText.isServerBound()) {
            this.textFieldAutoTextServerAddress.mouseClicked(mouseX, mouseY, mouseButton);
        }
    }
    
    private void drawElementTextField(String prefix, final GuiTextField textField, final int x, final int y, final int mouseX, final int mouseY) {
        prefix = String.valueOf(LanguageManager.translate(new StringBuilder("autotext_").append(prefix).toString())) + ":";
        this.drawString(LabyModCore.getMinecraft().getFontRenderer(), prefix, x + 2, y + 2, Integer.MAX_VALUE);
        Gui.drawRect(x + 2, y + 12, x + 2 + 114, y + 12 + 10, ((this.markMessageRed && textField != null && textField.equals(this.textFieldMessage)) || (this.markKeybindRed && textField != null && textField.equals(this.textFieldAutoTextKeyCode))) ? ModColor.toRGB(200, 100, 100, 200) : Integer.MAX_VALUE);
        if (textField == null) {
            return;
        }
        LabyModCore.getMinecraft().setTextFieldXPosition(textField, x + 2);
        LabyModCore.getMinecraft().setTextFieldYPosition(textField, y + 13);
        textField.setEnableBackgroundDrawing(false);
        textField.setMaxStringLength(120);
    }
    
    private void drawElementCheckBox(String text, final boolean check, int x, final int y, final int mouseX, final int mouseY) {
        final boolean hover = this.isHoverElementCheckbox(text, check, x, y, mouseX, mouseY);
        text = LanguageManager.translate("autotext_" + text);
        this.drawString(LabyModCore.getMinecraft().getFontRenderer(), text, x + 2, y + 2, Integer.MAX_VALUE);
        x += LabyModCore.getMinecraft().getFontRenderer().getStringWidth(text) + 2;
        Gui.drawRect(x + 3, y + 1, x + 12, y + 10, hover ? 2147483547 : Integer.MAX_VALUE);
        if (!check) {
            return;
        }
        Gui.drawCenteredString(LabyModCore.getMinecraft().getFontRenderer(), String.valueOf(ModColor.cl("a")) + "\u2714", x + 8, y + 1, Integer.MAX_VALUE);
    }
    
    private boolean isHoverElementCheckbox(String text, final boolean check, int x, final int y, final int mouseX, final int mouseY) {
        text = LanguageManager.translate("autotext_" + text);
        x += LabyModCore.getMinecraft().getFontRenderer().getStringWidth(text) + 2;
        return mouseX > x + 3 && mouseX < x + 12 && mouseY > y + 1 && mouseY < y + 10;
    }
    
    private void loadAutoText(final AutoTextKeyBinds.AutoText autoText) {
        if (autoText == null) {
            return;
        }
        this.selectedAutoText = autoText;
        this.markMessageRed = false;
        this.markKeybindRed = false;
        this.textFieldMessage.setText(autoText.getMessage());
        this.textFieldAutoTextServerAddress.setText((autoText.getServerAddress() == null) ? "" : autoText.getServerAddress());
        this.textFieldAutoTextKeyCode.setText((autoText.getKeyCode() == -1) ? "" : Keyboard.getKeyName(autoText.getKeyCode()));
    }
}
