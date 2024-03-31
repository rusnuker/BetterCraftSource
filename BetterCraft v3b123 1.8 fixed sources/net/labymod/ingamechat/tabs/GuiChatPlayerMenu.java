// 
// Decompiled by Procyon v0.6.0
// 

package net.labymod.ingamechat.tabs;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import net.labymod.utils.DrawUtils;
import net.labymod.utils.manager.TooltipHelper;
import net.labymod.utils.ModColor;
import net.labymod.main.lang.LanguageManager;
import net.minecraft.client.gui.Gui;
import net.labymod.main.LabyMod;
import net.labymod.core.LabyModCore;
import net.labymod.gui.elements.ModTextField;
import net.labymod.ingamechat.tools.playermenu.PlayerMenu;
import net.labymod.gui.elements.Scrollbar;
import net.labymod.ingamechat.GuiChatCustom;

public class GuiChatPlayerMenu extends GuiChatCustom
{
    private final int windowWidth = 148;
    private final int windowHeight = 194;
    private Scrollbar scrollbar;
    private PlayerMenu.PlayerMenuEntry openPlayerMenuEntry;
    private PlayerMenu.PlayerMenuEntry hoverPlayerMenuEntry;
    private boolean hoverSendInstantlyCheckbox;
    private boolean hoverAddButton;
    private boolean hoverCancelButton;
    private boolean hoverSaveButton;
    private boolean hoverDeleteButton;
    private ModTextField fieldDisplayName;
    private ModTextField fieldCommand;
    private boolean canHighlightRed;
    
    public GuiChatPlayerMenu(final String defaultText) {
        super(defaultText);
        this.scrollbar = new Scrollbar(10);
        this.openPlayerMenuEntry = null;
        this.hoverPlayerMenuEntry = null;
        this.hoverSendInstantlyCheckbox = false;
        this.hoverAddButton = false;
        this.hoverCancelButton = false;
        this.hoverSaveButton = false;
        this.hoverDeleteButton = false;
        this.canHighlightRed = false;
    }
    
    @Override
    public void initGui() {
        super.initGui();
        this.fieldDisplayName = new ModTextField(0, LabyModCore.getMinecraft().getFontRenderer(), 0, 0, 0, 10);
        this.fieldCommand = new ModTextField(0, LabyModCore.getMinecraft().getFontRenderer(), 0, 0, 0, 10);
        this.fieldDisplayName.setEnableBackgroundDrawing(false);
        this.fieldCommand.setEnableBackgroundDrawing(false);
        this.fieldDisplayName.setMaxStringLength(120);
        this.fieldCommand.setMaxStringLength(120);
    }
    
    @Override
    public void drawScreen(final int mouseX, final int mouseY, final float partialTicks) {
        super.drawScreen(mouseX, mouseY, partialTicks);
        final DrawUtils draw = LabyMod.getInstance().getDrawUtils();
        final int margin = 2;
        final int chatHeight = 16;
        final int width = draw.getWidth();
        this.getClass();
        final int x = width - 148 - 2;
        final int height = draw.getHeight();
        this.getClass();
        final int y = height - 194 - 16;
        final int n = x;
        final int n2 = y;
        final int n3 = x;
        this.getClass();
        final int n4 = n3 + 148;
        final int n5 = y;
        this.getClass();
        Gui.drawRect(n, n2, n4, n5 + 194, Integer.MIN_VALUE);
        draw.drawString(LanguageManager.translate("ingame_chat_tab_playermenu"), x, y - 10);
        this.hoverPlayerMenuEntry = null;
        this.hoverDeleteButton = false;
        final List<PlayerMenu.PlayerMenuEntry> playerMenuEntries = LabyMod.getInstance().getChatToolManager().getPlayerMenu();
        this.scrollbar.setPosition(draw.getWidth() - 2 - 1, y + 2, draw.getWidth() - 2, draw.getHeight() - 16 - 2);
        this.scrollbar.update(playerMenuEntries.size());
        final int listPadding = 2;
        final int listX = x + 2;
        double listY = y + 2 + this.scrollbar.getScrollY();
        for (final PlayerMenu.PlayerMenuEntry playerMenuComponent : playerMenuEntries) {
            if (listY >= y && listY < draw.getHeight() - 16 - 2) {
                final DrawUtils drawUtils = draw;
                final double left = listX - 2;
                final double top = listY;
                final int n6 = listX - 2;
                this.getClass();
                final boolean hover = drawUtils.drawRect(mouseX, mouseY, left, top, n6 + 148, listY + 10.0, 0, ModColor.toRGB(100, 200, 200, 100));
                final String displayName = playerMenuComponent.getDisplayName();
                draw.drawString(displayName, listX, listY + 1.0);
                boolean b = false;
                Label_0411: {
                    if (hover) {
                        final int n7 = listX - 2;
                        this.getClass();
                        if (mouseX > n7 + 148 - 10) {
                            b = true;
                            break Label_0411;
                        }
                    }
                    b = false;
                }
                final boolean hoverDeleteButton = b;
                if (hover) {
                    final DrawUtils drawUtils2 = draw;
                    final String string = String.valueOf(ModColor.cl(hoverDeleteButton ? 'c' : '4')) + "\u2718";
                    final int n8 = listX - 2;
                    this.getClass();
                    drawUtils2.drawRightString(string, n8 + 148, listY + 1.0);
                }
                if (hover) {
                    this.hoverPlayerMenuEntry = playerMenuComponent;
                    this.hoverDeleteButton = hoverDeleteButton;
                }
            }
            listY += 10.0;
        }
        this.scrollbar.draw();
        if (this.openPlayerMenuEntry == null) {
            final int buttonSize = 14;
            final int buttonMargin = 2;
            this.hoverAddButton = draw.drawRect(mouseX, mouseY, String.valueOf(this.hoverAddButton ? ModColor.cl('a') : "") + "+", x - 14 - 2, y, x - 2, y + 14, Integer.MIN_VALUE, Integer.MAX_VALUE);
            this.hoverSaveButton = false;
            this.hoverCancelButton = false;
        }
        else {
            final int editorWidth = 130;
            final int editorMargin = 2;
            final int editorPadding = 2;
            int editorYIndex = y + 2;
            final int n9 = x - 130 - 2;
            final int n10 = y;
            final int n11 = x - 2;
            final int n12 = y;
            this.getClass();
            Gui.drawRect(n9, n10, n11, n12 + 194, Integer.MIN_VALUE);
            draw.drawString(String.valueOf(LanguageManager.translate("chat_playermenu_displayname")) + ":", x - 130 + 2 - 2, editorYIndex);
            editorYIndex += 10;
            this.fieldDisplayName.width = 126;
            this.fieldDisplayName.xPosition = x - 130 + 2 - 2;
            this.fieldDisplayName.yPosition = editorYIndex + 1;
            final int fieldplayerMenuColor = (this.canHighlightRed && this.fieldDisplayName.getText().isEmpty()) ? ModColor.toRGB(200, 100, 100, 200) : Integer.MAX_VALUE;
            Gui.drawRect(x - 130 + 2 - 2, editorYIndex, x - 2 - 2, editorYIndex + 10, fieldplayerMenuColor);
            this.fieldDisplayName.drawTextBox();
            editorYIndex += 15;
            draw.drawString(String.valueOf(LanguageManager.translate("chat_playermenu_command")) + ":", x - 130 + 2 - 2, editorYIndex);
            editorYIndex += 10;
            this.fieldCommand.width = 126;
            this.fieldCommand.xPosition = x - 130 + 2 - 2;
            this.fieldCommand.yPosition = editorYIndex + 1;
            final int fieldReplacementColor = (this.canHighlightRed && this.fieldCommand.getText().isEmpty()) ? ModColor.toRGB(200, 100, 100, 200) : Integer.MAX_VALUE;
            Gui.drawRect(x - 130 + 2 - 2, editorYIndex, x - 2 - 2, editorYIndex + 10, fieldReplacementColor);
            this.fieldCommand.drawTextBox();
            editorYIndex += 13;
            if (mouseX > this.fieldCommand.xPosition && mouseX < this.fieldCommand.xPosition + this.fieldCommand.width && mouseY > this.fieldCommand.yPosition && mouseY < this.fieldCommand.yPosition + this.fieldCommand.height) {
                TooltipHelper.getHelper().pointTooltip(mouseX, mouseY, LanguageManager.translate("chat_playermenu_tooltip"));
            }
            this.hoverSendInstantlyCheckbox = this.drawElementCheckBox("sendinstantly", this.openPlayerMenuEntry.isSendInstantly(), x - 130 + 2 - 2, editorYIndex + 1, mouseX, mouseY);
            editorYIndex += 15;
            final int buttonHeight = 12;
            final DrawUtils drawUtils3 = draw;
            final String translate = LanguageManager.translate("button_cancel");
            final double left2 = x - 130 + 2 - 2;
            final int n13 = y;
            this.getClass();
            final double top2 = n13 + 194 - 2 - 12;
            final double right = x - 130 + 2 - 2 + 65 - 2;
            final int n14 = y;
            this.getClass();
            this.hoverCancelButton = drawUtils3.drawRect(mouseX, mouseY, translate, left2, top2, right, n14 + 194 - 2, Integer.MAX_VALUE, ModColor.toRGB(200, 100, 100, 200));
            final DrawUtils drawUtils4 = draw;
            final String translate2 = LanguageManager.translate("button_save");
            final double left3 = x - 130 + 2 - 2 + 65 + 2;
            final int n15 = y;
            this.getClass();
            final double top3 = n15 + 194 - 2 - 12;
            final double right2 = x - 2 - 2;
            final int n16 = y;
            this.getClass();
            this.hoverSaveButton = drawUtils4.drawRect(mouseX, mouseY, translate2, left3, top3, right2, n16 + 194 - 2, Integer.MAX_VALUE, ModColor.toRGB(100, 200, 100, 200));
            this.hoverAddButton = false;
        }
    }
    
    @Override
    public void mouseClicked(final int mouseX, final int mouseY, final int mouseButton) throws IOException {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        if (this.hoverAddButton && mouseButton == 0) {
            this.openPlayerMenuEntry = new PlayerMenu.PlayerMenuEntry("", "", true);
            this.canHighlightRed = false;
            this.fieldDisplayName.setText("");
            this.fieldCommand.setText("");
        }
        if (this.hoverPlayerMenuEntry != null && mouseButton == 0) {
            if (this.hoverDeleteButton) {
                final List<PlayerMenu.PlayerMenuEntry> entries = LabyMod.getInstance().getChatToolManager().getPlayerMenu();
                entries.remove(this.hoverPlayerMenuEntry);
                if (this.hoverPlayerMenuEntry == this.openPlayerMenuEntry) {
                    this.openPlayerMenuEntry = null;
                }
                LabyMod.getInstance().getChatToolManager().saveTools();
            }
            else {
                this.openPlayerMenuEntry = this.hoverPlayerMenuEntry;
                this.canHighlightRed = false;
                this.fieldDisplayName.setText(this.openPlayerMenuEntry.getDisplayName());
                this.fieldCommand.setText(this.openPlayerMenuEntry.getCommand());
            }
        }
        if (this.hoverCancelButton && mouseButton == 0) {
            this.openPlayerMenuEntry = null;
        }
        if (this.hoverSaveButton && mouseButton == 0) {
            final String playerMenu = this.fieldDisplayName.getText();
            final String replacement = this.fieldCommand.getText();
            if (playerMenu.isEmpty() || replacement.isEmpty()) {
                this.canHighlightRed = true;
                return;
            }
            this.canHighlightRed = false;
            this.openPlayerMenuEntry.setDisplayName(this.fieldDisplayName.getText());
            this.openPlayerMenuEntry.setCommand(this.fieldCommand.getText());
            final List<PlayerMenu.PlayerMenuEntry> entries2 = LabyMod.getInstance().getChatToolManager().getPlayerMenu();
            if (!entries2.contains(this.openPlayerMenuEntry)) {
                entries2.add(this.openPlayerMenuEntry);
            }
            this.openPlayerMenuEntry = null;
            LabyMod.getInstance().getChatToolManager().saveTools();
        }
        if (this.openPlayerMenuEntry != null) {
            this.fieldDisplayName.mouseClicked(mouseX, mouseY, mouseButton);
            this.fieldCommand.mouseClicked(mouseX, mouseY, mouseButton);
            if (this.hoverSendInstantlyCheckbox) {
                this.openPlayerMenuEntry.setSendInstantly(!this.openPlayerMenuEntry.isSendInstantly());
            }
        }
    }
    
    private boolean drawElementCheckBox(final String text, final boolean check, final int x, int y, final int mouseX, final int mouseY) {
        final String displayText = String.valueOf(LanguageManager.translate(new StringBuilder("chat_playermenu_").append(text).toString())) + ":";
        LabyMod.getInstance().getDrawUtils().drawString(displayText, x + 2, y + 2, 0.7);
        y += 10;
        final boolean hover = mouseX > x + 3 && mouseX < x + 12 && mouseY > y + 1 && mouseY < y + 10;
        Gui.drawRect(x + 3, y + 1, x + 12, y + 10, hover ? 2147483547 : Integer.MAX_VALUE);
        if (!check) {
            return hover;
        }
        Gui.drawCenteredString(LabyModCore.getMinecraft().getFontRenderer(), String.valueOf(ModColor.cl("a")) + "\u2714", x + 8, y + 1, Integer.MAX_VALUE);
        return hover;
    }
    
    @Override
    protected void keyTyped(final char typedChar, final int keyCode) throws IOException {
        if (this.openPlayerMenuEntry != null) {
            this.fieldDisplayName.textboxKeyTyped(typedChar, keyCode);
            this.fieldCommand.textboxKeyTyped(typedChar, keyCode);
            if (keyCode == 15) {
                if (this.fieldDisplayName.isFocused()) {
                    this.fieldDisplayName.setFocused(false);
                    this.fieldCommand.setFocused(true);
                }
                else {
                    this.fieldDisplayName.setFocused(true);
                    this.fieldCommand.setFocused(false);
                }
            }
            if (keyCode == 1) {
                super.keyTyped(typedChar, keyCode);
            }
        }
        else {
            super.keyTyped(typedChar, keyCode);
        }
    }
    
    @Override
    protected void mouseReleased(final int mouseX, final int mouseY, final int state) {
        super.mouseReleased(mouseX, mouseY, state);
        this.scrollbar.mouseAction(mouseX, mouseY, Scrollbar.EnumMouseAction.RELEASED);
    }
    
    @Override
    protected void mouseClickMove(final int mouseX, final int mouseY, final int clickedMouseButton, final long timeSinceLastClick) {
        super.mouseClickMove(mouseX, mouseY, clickedMouseButton, timeSinceLastClick);
        this.scrollbar.mouseAction(mouseX, mouseY, Scrollbar.EnumMouseAction.DRAGGING);
    }
    
    @Override
    public void handleMouseInput() throws IOException {
        super.handleMouseInput();
        this.scrollbar.mouseInput();
    }
    
    @Override
    public void updateScreen() {
        if (this.openPlayerMenuEntry != null) {
            this.fieldDisplayName.updateCursorCounter();
            this.fieldCommand.updateCursorCounter();
        }
    }
}
