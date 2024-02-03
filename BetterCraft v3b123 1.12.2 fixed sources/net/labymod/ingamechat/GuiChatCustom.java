// 
// Decompiled by Procyon v0.6.0
// 

package net.labymod.ingamechat;

import net.minecraft.item.ItemStack;
import net.labymod.api.events.MessageSendEvent;
import net.labymod.ingamechat.tools.shortcuts.Shortcuts;
import java.util.Iterator;
import net.minecraft.client.gui.GuiLabel;
import net.labymod.utils.UUIDFetcher;
import net.labymod.ingamechat.namehistory.NameHistory;
import net.labymod.utils.ModColor;
import net.labymod.utils.ModUtils;
import me.nzxtercode.bettercraft.client.utils.ColorUtils;
import net.labymod.ingamechat.namehistory.NameHistoryUtil;
import org.lwjgl.input.Mouse;
import net.labymod.core.LabyModCore;
import net.labymod.ingamechat.renderer.EnumMouseAction;
import net.labymod.settings.LabyModModuleEditorGui;
import net.labymod.ingamechat.tabs.GuiChatPlayerMenu;
import net.labymod.ingamechat.tabs.GuiChatShortcuts;
import net.labymod.ingamechat.tabs.GuiChatAutoText;
import net.labymod.ingamechat.tabs.GuiChatSymbols;
import net.labymod.main.lang.LanguageManager;
import net.labymod.utils.manager.TooltipHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import org.lwjgl.opengl.GL11;
import net.minecraft.client.gui.Gui;
import java.io.IOException;
import net.minecraft.client.gui.GuiScreen;
import net.labymod.ingamechat.tabs.GuiChatNameHistory;
import net.minecraft.client.gui.GuiButton;
import java.util.List;
import net.labymod.utils.Material;
import net.labymod.settings.elements.ControlElement;
import net.labymod.main.ModTextures;
import java.util.ArrayList;
import net.labymod.api.permissions.Permissions;
import net.labymod.main.LabyMod;
import net.minecraft.client.gui.GuiChat;

public class GuiChatCustom extends GuiChat
{
    public static int activeTab;
    private final IngameChatManager ingameChatManager;
    private ChatButton[] chatButtons;
    private String defaultText;
    
    static {
        GuiChatCustom.activeTab = -1;
    }
    
    public GuiChatCustom(final String defaultText) {
        super(defaultText);
        this.ingameChatManager = LabyMod.getInstance().getIngameChatManager();
        this.defaultText = defaultText;
    }
    
    public GuiChatCustom() {
        this.ingameChatManager = LabyMod.getInstance().getIngameChatManager();
    }
    
    @Override
    public void initGui() {
        super.initGui();
        if (this.mc.currentScreen != null && this.mc.currentScreen.getClass() == GuiChatCustom.class) {
            GuiChatCustom.activeTab = -1;
        }
        final boolean chatFeaturesAllowed = LabyMod.getInstance().getServerManager().isAllowed(Permissions.Permission.CHAT);
        final List<ChatButton> chatButtonList = new ArrayList<ChatButton>();
        if (LabyMod.getSettings().chatSymbols) {
            chatButtonList.add(new ChatButton(0, "symbols", new ControlElement.IconData(ModTextures.CHAT_TAB_SYMBOLS), chatFeaturesAllowed));
        }
        if (LabyMod.getSettings().nameHistory) {
            chatButtonList.add(new ChatButton(5, "namehistory", new ControlElement.IconData(Material.BOOK_AND_QUILL), true));
        }
        chatButtonList.toArray(this.chatButtons = new ChatButton[chatButtonList.size()]);
        this.inputField.setText((this.defaultText == null) ? "" : this.defaultText);
    }
    
    @Override
    protected void actionPerformed(final GuiButton button) throws IOException {
        super.actionPerformed(button);
        if (button.id == 4) {
            this.mc.displayGuiScreen(new GuiChatNameHistory(this.inputField.getText()));
        }
    }
    
    public void drawButtons(final int mouseX, final int mouseY, final float partialTicks) {
        int slot = 0;
        ChatButton[] chatButtons;
        for (int length = (chatButtons = this.chatButtons).length, i = 0; i < length; ++i) {
            final ChatButton chatButton = chatButtons[i];
            final boolean enabled = chatButton.isEnabled();
            final int x = GuiChatCustom.width - 2 - 11 - slot * 14;
            final int y = GuiChatCustom.height - 14;
            if (slot == GuiChatCustom.activeTab) {
                Gui.drawRect(x, y - 2, x + 13, y, Integer.MIN_VALUE);
            }
            GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
            final boolean hoverSymbols = mouseX >= x && mouseX < x + 13 && mouseY > y && mouseY < y + 12;
            if (chatButton.getIconData().hasMaterialIcon()) {
                GlStateManager.pushMatrix();
                final double scale = hoverSymbols ? 0.7 : 0.6;
                GlStateManager.scale(scale, scale, 1.0);
                LabyMod.getInstance().getDrawUtils().renderItemIntoGUI(chatButton.getItem(), (x + 5.5 - scale * 6.0) / scale, (y + 5 - scale * 6.0) / scale);
                GlStateManager.popMatrix();
            }
            else if (chatButton.getIconData().hastextureIcon()) {
                Minecraft.getMinecraft().getTextureManager().bindTexture(chatButton.getIconData().gettextureIcon());
                LabyMod.getInstance().getDrawUtils().drawTexture(x + 2 - (hoverSymbols ? 1 : 0), y + 2 - (hoverSymbols ? 1 : 0), 255.0, 255.0, hoverSymbols ? 11.0 : 9.0, hoverSymbols ? 11.0 : 9.0);
            }
            if (hoverSymbols) {
                TooltipHelper.getHelper().pointTooltip(mouseX, mouseY, 0L, enabled ? chatButton.getDisplayName() : LanguageManager.translate("ingame_chat_feature_not_allowed", chatButton.getDisplayName()));
            }
            ++slot;
        }
    }
    
    public void onButtonClick(final int mouseX, final int mouseY, final int mouseButton) {
        for (int slot = 0; slot < this.chatButtons.length; ++slot) {
            final ChatButton chatButton = this.chatButtons[slot];
            final boolean hoverSymbols = mouseX > GuiChatCustom.width - 2 - 13 - slot * 14 && mouseX < GuiChatCustom.width - 2 - slot * 14 && mouseY > GuiChatCustom.height - 12 && mouseY < GuiChatCustom.height;
            if (hoverSymbols && chatButton.isEnabled()) {
                switch (chatButton.getId()) {
                    case 0: {
                        GuiChatCustom.activeTab = slot;
                        this.mc.displayGuiScreen((this.mc.currentScreen instanceof GuiChatSymbols) ? new GuiChatCustom(this.inputField.getText()) : new GuiChatSymbols(this.inputField.getText()));
                        break;
                    }
                    case 1: {
                        GuiChatCustom.activeTab = slot;
                        this.mc.displayGuiScreen((this.mc.currentScreen instanceof GuiChatAutoText) ? new GuiChatCustom(this.inputField.getText()) : new GuiChatAutoText(this.inputField.getText()));
                        break;
                    }
                    case 2: {
                        GuiChatCustom.activeTab = slot;
                        this.mc.displayGuiScreen((this.mc.currentScreen instanceof GuiChatShortcuts) ? new GuiChatCustom(this.inputField.getText()) : new GuiChatShortcuts(this.inputField.getText()));
                        break;
                    }
                    case 3: {
                        GuiChatCustom.activeTab = slot;
                        this.mc.displayGuiScreen((this.mc.currentScreen instanceof GuiChatPlayerMenu) ? new GuiChatCustom(this.inputField.getText()) : new GuiChatPlayerMenu(this.inputField.getText()));
                        break;
                    }
                    case 4: {
                        GuiChatCustom.activeTab = slot;
                        break;
                    }
                    case 5: {
                        GuiChatCustom.activeTab = slot;
                        this.mc.displayGuiScreen((this.mc.currentScreen instanceof GuiChatNameHistory) ? new GuiChatCustom(this.inputField.getText()) : new GuiChatNameHistory(this.inputField.getText()));
                        break;
                    }
                    case 6: {
                        GuiChatCustom.activeTab = slot;
                        this.mc.displayGuiScreen(new LabyModModuleEditorGui(new GuiChatCustom(this.inputField.getText())));
                        break;
                    }
                }
            }
        }
    }
    
    public void mouseClicked(final int mouseX, final int mouseY, final int mouseButton) throws IOException {
        this.ingameChatManager.handleMouse(mouseX, mouseY, mouseButton, EnumMouseAction.CLICKED);
        super.mouseClicked(mouseX, mouseY, mouseButton);
        this.onButtonClick(mouseX, mouseY, mouseButton);
        if (mouseButton == 1) {
            final String value = LabyModCore.getMinecraft().getClickEventValue(Mouse.getX(), Mouse.getY());
            if (value != null && value.startsWith("/msg ")) {
                final String name = value.replace("/msg ", "").replace(" ", "");
                if (!NameHistoryUtil.isInCache(name)) {
                    NameHistoryUtil.getNameHistory(name);
                }
            }
        }
    }
    
    @Override
    protected void mouseReleased(final int mouseX, final int mouseY, final int state) {
        this.ingameChatManager.handleMouse(mouseX, mouseY, state, EnumMouseAction.RELEASED);
        super.mouseReleased(mouseX, mouseY, state);
    }
    
    @Override
    protected void mouseClickMove(final int mouseX, final int mouseY, final int clickedMouseButton, final long timeSinceLastClick) {
        this.ingameChatManager.handleMouse(mouseX, mouseY, clickedMouseButton, EnumMouseAction.DRAGGING);
        super.mouseClickMove(mouseX, mouseY, clickedMouseButton, timeSinceLastClick);
    }
    
    @Override
    public void handleMouseInput() throws IOException {
        super.handleMouseInput();
    }
    
    @Override
    protected void keyTyped(final char typedChar, final int keyCode) throws IOException {
        super.keyTyped(typedChar, keyCode);
    }
    
    @Override
    public void onGuiClosed() {
        super.onGuiClosed();
    }
    
    @Override
    public void drawScreen(final int mouseX, final int mouseY, final float partialTicks) {
        this.drawModifiedSuperScreen(mouseX, mouseY, partialTicks);
        Gui.drawRect(2, GuiChatCustom.height - 14, GuiChatCustom.width - 2, GuiChatCustom.height - 2, Integer.MIN_VALUE);
        this.ingameChatManager.handleMouse(mouseX, mouseY, -1, EnumMouseAction.RENDER);
        this.drawButtons(mouseX, mouseY, partialTicks);
        this.inputField.drawTextBox();
        this.drawString(this.fontRendererObj, String.valueOf(this.inputField.getText().length()) + " / " + this.inputField.getMaxStringLength(), 5, GuiChatCustom.height - 25, ColorUtils.rainbowEffect());
        this.inputField.setMaxStringLength(this.inputField.getText().startsWith("/") ? Integer.MAX_VALUE : 100);
        GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
        final String value = LabyModCore.getMinecraft().getClickEventValue(Mouse.getX(), Mouse.getY());
        if (LabyMod.getSettings().hoverNameHistory && value != null && value.startsWith("/msg ")) {
            final String name = value.replace("/msg ", "").replace(" ", "");
            if (NameHistoryUtil.isInCache(name)) {
                final NameHistory history = NameHistoryUtil.getNameHistory(name);
                final ArrayList<String> lines = new ArrayList<String>();
                boolean currentName = true;
                UUIDFetcher[] changes;
                for (int length = (changes = history.getChanges()).length, i = 0; i < length; ++i) {
                    final UUIDFetcher change = changes[i];
                    if (change.changedToAt != 0L) {
                        final String date = ModUtils.getTimeDiff(change.changedToAt);
                        String c = "7";
                        if (currentName) {
                            c = "6";
                        }
                        currentName = false;
                        lines.add(String.valueOf(ModColor.cl(c)) + change.name + ModColor.cl("8") + " - " + ModColor.cl("8") + date);
                    }
                    else {
                        lines.add(String.valueOf(ModColor.cl("a")) + change.name);
                    }
                }
                this.drawHoveringText(lines, mouseX, mouseY);
            }
            else {
                final ArrayList<String> lines2 = new ArrayList<String>();
                lines2.add(LanguageManager.translate("ingame_chat_rightclick_for_namechanges"));
                this.drawHoveringText(lines2, mouseX, mouseY);
            }
            GlStateManager.disableLighting();
        }
    }
    
    private void drawModifiedSuperScreen(final int mouseX, final int mouseY, final float partialTicks) {
        this.inputField.drawTextBox();
        this.handleComponentHover(this.mc.ingameGUI.getChatGUI().getChatComponent(Mouse.getX(), Mouse.getY()), mouseX, mouseY);
        for (final GuiButton guiButton : this.buttonList) {
            LabyModCore.getMinecraft().drawButton(guiButton, mouseX, mouseY);
        }
        for (final GuiLabel guiLabel : this.labelList) {
            guiLabel.drawLabel(this.mc, mouseX, mouseY);
        }
    }
    
    @Override
    public void sendChatMessage(String msg, final boolean addToChat) {
        boolean cancelled = false;
        for (final Shortcuts.Shortcut shortcut : LabyMod.getInstance().getChatToolManager().getShortcuts()) {
            msg = msg.replace(shortcut.getShortcut(), String.format(shortcut.getReplacement(), LabyMod.getInstance().getPlayerName()));
        }
        for (final MessageSendEvent messageSend : LabyMod.getInstance().getEventManager().getMessageSend()) {
            if (messageSend.onSend(msg) && !cancelled) {
                cancelled = true;
            }
        }
        if (cancelled) {
            if (addToChat) {
                this.mc.ingameGUI.getChatGUI().addToSentMessages(msg);
            }
            return;
        }
        super.sendChatMessage(msg, addToChat);
    }
    
    private class ChatButton
    {
        private final int id;
        private final String displayName;
        private final boolean enabled;
        private final ItemStack item;
        private final ControlElement.IconData iconData;
        
        public ChatButton(final int id, final String languageKey, final ControlElement.IconData iconData, final boolean enabled) {
            this.id = id;
            this.displayName = LanguageManager.translate("ingame_chat_tab_" + languageKey);
            this.item = (iconData.hasMaterialIcon() ? iconData.getMaterialIcon().createItemStack() : null);
            this.iconData = iconData;
            this.enabled = enabled;
        }
        
        public int getId() {
            return this.id;
        }
        
        public String getDisplayName() {
            return this.displayName;
        }
        
        public boolean isEnabled() {
            return this.enabled;
        }
        
        public ItemStack getItem() {
            return this.item;
        }
        
        public ControlElement.IconData getIconData() {
            return this.iconData;
        }
    }
}
