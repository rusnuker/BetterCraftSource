// 
// Decompiled by Procyon v0.6.0
// 

package me.amkgre.bettercraft.client.mods.clientchat;

import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.renderer.Tessellator;
import me.amkgre.bettercraft.client.utils.RenderUtils;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiSlot;
import net.minecraft.client.gui.FontRenderer;
import java.io.IOException;
import org.lwjgl.input.Keyboard;
import java.util.HashMap;
import net.minecraft.client.gui.GuiButton;
import java.util.List;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.gui.GuiScreen;

public class GuiClientChat extends GuiScreen
{
    private GuiTextField textField;
    private List<String> ircList;
    private GuiList ircGuiList;
    private GuiButton guiButton;
    private static HashMap<String, Integer> helmetTexture;
    private int selectedIrcMessage;
    private GuiScreen before;
    int i;
    
    static {
        GuiClientChat.helmetTexture = new HashMap<String, Integer>();
    }
    
    public GuiClientChat(final GuiScreen before) {
        this.selectedIrcMessage = -1;
        this.before = before;
        this.ircList = InterClienChatConnection.msgs;
    }
    
    public GuiClientChat() {
        this.selectedIrcMessage = -1;
        this.ircList = InterClienChatConnection.msgs;
    }
    
    @Override
    public void updateScreen() {
        this.textField.updateCursorCounter();
    }
    
    @Override
    public void initGui() {
        final int width = GuiClientChat.width / 2;
        (this.textField = new GuiTextField(0, this.mc.fontRendererObj, width / 2, GuiClientChat.height - 40, width, 20)).setMaxStringLength(Integer.MAX_VALUE);
        this.buttonList.add(new GuiButton(1, GuiClientChat.width - 80, GuiClientChat.height - 26, 75, 20, "Back"));
        this.buttonList.add(this.guiButton = new GuiButton(2, GuiClientChat.width - 80, GuiClientChat.height - 50, 75, 20, InterClienChatConnection.hasLostConnection ? "§aConnect" : "§cDisconnect"));
        (this.ircGuiList = new GuiList(this.mc)).registerScrollButtons(4, 5);
    }
    
    @Override
    protected void keyTyped(final char typedChar, final int keyCode) throws IOException {
        Keyboard.enableRepeatEvents(true);
        this.textField.textboxKeyTyped(typedChar, keyCode);
        switch (keyCode) {
            case 28: {
                if (!this.textField.getText().isEmpty()) {
                    if (this.textField.getText().startsWith("msg")) {
                        final String[] args = substringargs(this.textField.getText().substring(1).split(" "));
                        InterClienChatConnection.sendMsgMessage(args[0], this.textField.getText().replace(args[0], "").replace("msg", ""));
                    }
                    else {
                        InterClienChatConnection.sendIRCMessage(this.textField.getText());
                    }
                    this.textField.setText("");
                    break;
                }
                break;
            }
        }
        super.keyTyped(typedChar, keyCode);
    }
    
    @Override
    protected void actionPerformed(final GuiButton button) throws IOException {
        if (button.id == 2) {
            if (InterClienChatConnection.hasLostConnection) {
                InterClienChatConnection.start();
            }
            else {
                InterClienChatConnection.stop();
            }
        }
        if (button.id == 1) {
            this.mc.displayGuiScreen(this.before);
        }
    }
    
    @Override
    protected void mouseClicked(final int mouseX, final int mouseY, final int mouseButton) throws IOException {
        this.textField.mouseClicked(mouseX, mouseY, mouseButton);
        super.mouseClicked(mouseX, mouseY, mouseButton);
    }
    
    @Override
    public void drawScreen(final int mouseX, final int mouseY, final float partialTicks) {
        this.drawDefaultBackground();
        InterClienChatConnection.seenMsgs = InterClienChatConnection.msgs.size();
        if (this.i != InterClienChatConnection.msgs.size()) {
            ++this.i;
            this.ircGuiList.scrollBy(InterClienChatConnection.msgs.size());
        }
        this.guiButton.displayString = (InterClienChatConnection.hasLostConnection ? "§aConnect" : "§cDisconnect");
        this.textField.drawTextBox();
        this.ircGuiList.drawScreen(mouseX, mouseY, partialTicks);
        super.drawScreen(mouseX, mouseY, partialTicks);
    }
    
    @Override
    public void handleMouseInput() throws IOException {
        this.ircGuiList.handleMouseInput();
        super.handleMouseInput();
    }
    
    public static String[] substringargs(final String[] arr) {
        final int newLen = arr.length - 1;
        final String[] out = new String[newLen];
        for (int i = 0; i < newLen; ++i) {
            out[i] = arr[i + 1];
        }
        return out;
    }
    
    static /* synthetic */ void access$1(final GuiClientChat guiClientChat, final int selectedIrcMessage) {
        guiClientChat.selectedIrcMessage = selectedIrcMessage;
    }
    
    class GuiList extends GuiSlot
    {
        public GuiList(final Minecraft mcIn) {
            super(mcIn, GuiClientChat.width, GuiClientChat.height, 32, GuiClientChat.height - 64, 36);
            this.renderClicked = false;
        }
        
        @Override
        protected int getSize() {
            return GuiClientChat.this.ircList.size();
        }
        
        @Override
        protected void elementClicked(final int slotIndex, final boolean isDoubleClick, final int mouseX, final int mouseY) {
            GuiClientChat.access$1(GuiClientChat.this, slotIndex);
        }
        
        @Override
        protected boolean isSelected(final int slotIndex) {
            return slotIndex == GuiClientChat.this.selectedIrcMessage;
        }
        
        @Override
        protected int getContentHeight() {
            return this.getSize() * 36;
        }
        
        @Override
        public void scrollBy(final int amount) {
            super.scrollBy(amount);
        }
        
        @Override
        protected void drawBackground() {
        }
        
        @Override
        protected void func_192637_a(final int entryID, int x, final int y, final int p_192637_4_, final int p_192637_5_, final int p_192637_6_, final float p_192637_7_) {
            final String name = GuiClientChat.this.ircList.get(entryID);
            final String n = name.split(" ")[0];
            final String clientName = n.equals("§fME") ? Minecraft.session.getUsername() : n;
            x /= 4;
            Gui.drawString(GuiClientChat.this.fontRendererObj, name, x, y + 12, 8421504);
            try {
                if (GuiClientChat.helmetTexture.containsKey(clientName)) {
                    GlStateManager.bindTexture(GuiClientChat.helmetTexture.get(clientName));
                }
                else {
                    RenderUtils.downloadSkin(clientName);
                    if (RenderUtils.dynamicTexture != null) {
                        GuiClientChat.helmetTexture.put(clientName, RenderUtils.dynamicTexture.getGlTextureId());
                    }
                }
            }
            catch (final Exception ex) {}
            final int img_w = 32;
            final int img_h = 32;
            final Tessellator tessellator = Tessellator.getInstance();
            final BufferBuilder worldrenderer = tessellator.getBuffer();
            worldrenderer.begin(7, DefaultVertexFormats.POSITION_TEX);
            worldrenderer.pos(x - 40, y, 0.0).tex(0.0, 0.0).endVertex();
            worldrenderer.pos(x - 40, y + img_w, 0.0).tex(0.0, 1.0).endVertex();
            worldrenderer.pos(x + img_h - 40, y + img_w, 0.0).tex(1.0, 1.0).endVertex();
            worldrenderer.pos(x + img_h - 40, y, 0.0).tex(1.0, 0.0).endVertex();
            tessellator.draw();
        }
    }
}
