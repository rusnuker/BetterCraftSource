// 
// Decompiled by Procyon v0.6.0
// 

package net.labymod.gui;

import net.minecraft.client.gui.Gui;
import net.labymod.utils.ModColor;
import net.minecraft.client.Minecraft;
import java.io.IOException;
import net.labymod.utils.manager.TagManager;
import net.labymod.main.lang.LanguageManager;
import net.labymod.main.LabyMod;
import org.lwjgl.input.Keyboard;
import net.minecraft.client.gui.GuiButton;
import net.labymod.gui.elements.ModTextField;
import net.minecraft.client.gui.GuiScreen;

public class GuiTagsAdd extends GuiScreen
{
    private GuiScreen lastScreen;
    private String editName;
    private String storedName;
    private String storedTag;
    private String prevName;
    private ModTextField fieldMinecraftName;
    private ModTextField fieldTagName;
    private GuiButton buttonDone;
    private String renderHeadName;
    
    public GuiTagsAdd(final GuiScreen lastScreen, final String editName) {
        this.storedName = "";
        this.storedTag = "";
        this.lastScreen = lastScreen;
        this.editName = editName;
        this.prevName = editName;
    }
    
    @Override
    public void initGui() {
        super.initGui();
        Keyboard.enableRepeatEvents(true);
        (this.fieldMinecraftName = new ModTextField(-1, LabyMod.getInstance().getDrawUtils().fontRenderer, GuiTagsAdd.width / 2 - 100, GuiTagsAdd.height / 2 - 50, 200, 20)).setMaxStringLength(32);
        (this.fieldTagName = new ModTextField(-1, LabyMod.getInstance().getDrawUtils().fontRenderer, GuiTagsAdd.width / 2 - 100, GuiTagsAdd.height / 2 - 5, 200, 20)).setColorBarEnabled(true);
        this.fieldTagName.setMaxStringLength(32);
        this.buttonList.add(this.buttonDone = new GuiButton(0, GuiTagsAdd.width / 2 + 3, GuiTagsAdd.height / 2 + 35, 98, 20, LanguageManager.translate("button_add")));
        this.buttonList.add(new GuiButton(1, GuiTagsAdd.width / 2 - 101, GuiTagsAdd.height / 2 + 35, 98, 20, LanguageManager.translate("button_cancel")));
        this.buttonDone.enabled = false;
        if (this.editName != null) {
            this.storedName = this.editName;
            this.renderHeadName = this.editName;
            final String foundTag = TagManager.getConfigManager().getSettings().getTags().get(this.editName);
            if (foundTag != null) {
                this.storedTag = foundTag;
                this.buttonDone.enabled = true;
            }
            this.editName = null;
        }
        this.fieldMinecraftName.setText(this.storedName);
        this.fieldTagName.setText(this.storedTag);
    }
    
    @Override
    public void onGuiClosed() {
        Keyboard.enableRepeatEvents(false);
    }
    
    @Override
    protected void keyTyped(final char typedChar, final int keyCode) throws IOException {
        if (keyCode == 15) {
            if (this.fieldMinecraftName.isFocused()) {
                this.fieldTagName.setFocused(true);
                this.fieldMinecraftName.setFocused(false);
                this.renderHeadName = this.storedName;
            }
            else if (this.fieldTagName.isFocused()) {
                this.fieldMinecraftName.setFocused(true);
                this.fieldTagName.setFocused(false);
            }
        }
        if (this.fieldMinecraftName.textboxKeyTyped(typedChar, keyCode)) {
            this.storedName = this.fieldMinecraftName.getText();
        }
        if (this.fieldTagName.textboxKeyTyped(typedChar, keyCode)) {
            this.storedTag = this.fieldTagName.getText();
        }
        this.buttonDone.enabled = (!this.storedName.isEmpty() && !this.storedTag.isEmpty());
        super.keyTyped(typedChar, keyCode);
    }
    
    public void mouseClicked(final int mouseX, final int mouseY, final int mouseButton) throws IOException {
        final boolean flag = this.fieldMinecraftName.isFocused();
        this.fieldMinecraftName.mouseClicked(mouseX, mouseY, mouseButton);
        if (this.fieldTagName.mouseClicked(mouseX, mouseY, mouseButton)) {
            this.storedTag = this.fieldTagName.getText();
            if (flag && !this.fieldMinecraftName.isFocused()) {
                this.renderHeadName = this.storedName;
            }
        }
        super.mouseClicked(mouseX, mouseY, mouseButton);
    }
    
    @Override
    protected void actionPerformed(final GuiButton button) throws IOException {
        super.actionPerformed(button);
        switch (button.id) {
            case 0: {
                if (this.prevName != null) {
                    TagManager.getConfigManager().getSettings().getTags().remove(this.prevName);
                }
                TagManager.getConfigManager().getSettings().getTags().put(this.storedName, this.storedTag);
                Minecraft.getMinecraft().displayGuiScreen(this.lastScreen);
                break;
            }
            case 1: {
                Minecraft.getMinecraft().displayGuiScreen(this.lastScreen);
                break;
            }
        }
    }
    
    @Override
    public void updateScreen() {
        super.updateScreen();
        this.fieldMinecraftName.updateCursorCounter();
        this.fieldTagName.updateCursorCounter();
    }
    
    @Override
    public void drawScreen(final int mouseX, final int mouseY, final float partialTicks) {
        this.drawDefaultBackground();
        this.fieldMinecraftName.drawTextBox();
        this.fieldTagName.drawTextBox();
        this.fieldTagName.drawColorBar(mouseX, mouseY);
        if (this.renderHeadName != null && !this.renderHeadName.contains(" ") && this.renderHeadName.length() <= 16) {
            LabyMod.getInstance().getDrawUtils().drawPlayerHead(this.renderHeadName, this.fieldMinecraftName.xPosition - this.fieldMinecraftName.height - 4, this.fieldMinecraftName.yPosition, this.fieldMinecraftName.height);
        }
        LabyMod.getInstance().getDrawUtils().drawString(String.valueOf(LanguageManager.translate("minecraft_name")) + ":", GuiTagsAdd.width / 2 - 100, GuiTagsAdd.height / 2 - 65);
        LabyMod.getInstance().getDrawUtils().drawString(String.valueOf(LanguageManager.translate("custom_tag_for_player")) + ":", GuiTagsAdd.width / 2 - 100, GuiTagsAdd.height / 2 - 20);
        final String displayString = this.storedTag.replaceAll("&", ModColor.getCharAsString());
        final int strl = LabyMod.getInstance().getDrawUtils().getStringWidth(ModColor.removeColor(displayString));
        if (strl > 1) {
            Gui.drawRect(GuiTagsAdd.width / 2 - strl / 2 - 2, GuiTagsAdd.height / 2 - 92, GuiTagsAdd.width / 2 + strl / 2 + 2, GuiTagsAdd.height / 2 - 80, Integer.MIN_VALUE);
            LabyMod.getInstance().getDrawUtils().drawCenteredString(displayString, GuiTagsAdd.width / 2, GuiTagsAdd.height / 2 - 90);
        }
        super.drawScreen(mouseX, mouseY, partialTicks);
    }
}
