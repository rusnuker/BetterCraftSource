// 
// Decompiled by Procyon v0.6.0
// 

package me.nzxtercode.bettercraft.client.misc.irc;

import me.nzxtercode.bettercraft.client.utils.ColorUtils;
import net.minecraft.client.renderer.GlStateManager;
import java.util.Map;
import net.minecraft.client.Minecraft;
import org.jibble.pircbot.User;
import java.io.IOException;
import java.util.Arrays;
import org.lwjgl.input.Mouse;
import net.minecraft.client.gui.Gui;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.client.gui.GuiButton;
import org.lwjgl.input.Keyboard;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.gui.GuiSlot;
import net.minecraft.client.gui.GuiScreen;

public class GuiIRC extends GuiScreen
{
    private GuiScreen parent;
    private int sidebarX;
    private int sidebarY;
    private GuiSlot chatSlot;
    private GuiTextField messageField;
    
    public GuiIRC(final GuiScreen parent) {
        this.parent = parent;
    }
    
    @Override
    public void initGui() {
        Keyboard.enableRepeatEvents(true);
        this.buttonList.clear();
        this.buttonList.add(new GuiButton(0, GuiIRC.width - 88, GuiIRC.height - 26, 80, 20, "Back"));
        (this.messageField = new GuiTextField(0, this.fontRendererObj, 5, GuiIRC.height - 25, GuiIRC.width - 100, 20)).setMaxStringLength(100);
        this.chatSlot = new ChatSlot(this.mc, GuiIRC.width, GuiIRC.height, 10, GuiIRC.height - 32, 20);
        IRC.getInstance().setUnreadMessages(0);
        super.initGui();
    }
    
    @Override
    public void onGuiClosed() {
        IRC.getInstance().setUnreadMessages(0);
        super.onGuiClosed();
    }
    
    @Override
    public void drawScreen(final int mouseX, final int mouseY, final float partialTicks) {
        this.drawDefaultBackground();
        this.chatSlot.drawScreen(mouseX, mouseY, partialTicks);
        Gui.drawCenteredString(this.fontRendererObj, "Users: " + EnumChatFormatting.RED.toString() + String.valueOf(IRC.getInstance().getUsers(IRC.getInstance().currentChannel).length), GuiIRC.width / 2, 1, 16777215);
        this.messageField.drawTextBox();
        final int fadeSpeed = 10;
        final int sidebarWidth = 100;
        if (mouseX >= GuiIRC.width - sidebarWidth && mouseX <= GuiIRC.width) {
            if (this.sidebarX + fadeSpeed >= sidebarWidth) {
                this.sidebarX = sidebarWidth;
            }
            else {
                this.sidebarX += fadeSpeed;
            }
        }
        else if (this.sidebarX - fadeSpeed <= 0) {
            this.sidebarX = 0;
        }
        else {
            this.sidebarX -= fadeSpeed;
        }
        this.sidebarY += Mouse.getDWheel() / 30;
        Gui.drawRect(GuiIRC.width - this.sidebarX, 0, GuiIRC.width, GuiIRC.height, Integer.MIN_VALUE);
        Arrays.asList(IRC.getInstance().getUsers(IRC.getInstance().currentChannel)).forEach(user -> Gui.drawCenteredString(this.fontRendererObj, String.format("%s%s", user.isOp() ? EnumChatFormatting.RED : EnumChatFormatting.RESET, user.getNick()), GuiIRC.width - this.sidebarX + n / 2, (Arrays.asList(IRC.getInstance().getUsers(IRC.getInstance().currentChannel)).indexOf(user) + 1) * this.fontRendererObj.FONT_HEIGHT + this.sidebarY, -1));
        super.drawScreen(mouseX, mouseY, partialTicks);
    }
    
    @Override
    protected void keyTyped(final char typedChar, final int keyCode) throws IOException {
        if (keyCode == 28 && this.messageField.isFocused() && !this.messageField.getText().isEmpty()) {
            IRC.getInstance().sendClientMessage(this.messageField.getText());
            this.messageField.setText("");
        }
        this.messageField.textboxKeyTyped(typedChar, keyCode);
        super.keyTyped(typedChar, keyCode);
    }
    
    @Override
    protected void mouseClicked(final int mouseX, final int mouseY, final int mouseButton) throws IOException {
        this.messageField.mouseClicked(mouseX, mouseY, mouseButton);
        super.mouseClicked(mouseX, mouseY, mouseButton);
    }
    
    @Override
    protected void actionPerformed(final GuiButton button) throws IOException {
        this.chatSlot.actionPerformed(button);
        switch (button.id) {
            case 0: {
                this.mc.displayGuiScreen(this.parent);
                break;
            }
        }
    }
    
    @Override
    public void handleMouseInput() throws IOException {
        this.chatSlot.handleMouseInput();
        super.handleMouseInput();
    }
    
    public class ChatSlot extends GuiSlot
    {
        public ChatSlot(final Minecraft mcIn, final int width, final int height, final int topIn, final int bottomIn, final int slotHeightIn) {
            super(mcIn, width, height, topIn, bottomIn, slotHeightIn);
            IRC.getInstance().getMessageListeners().add(messageEntry -> this.amountScrolled += this.slotHeight);
        }
        
        @Override
        protected int getSize() {
            return IRC.getInstance().getMessageList().size();
        }
        
        @Override
        public int getListWidth() {
            return this.width - 40;
        }
        
        @Override
        protected int getScrollBarX() {
            return this.width - 6;
        }
        
        @Override
        protected void elementClicked(final int slotIndex, final boolean isDoubleClick, final int mouseX, final int mouseY) {
        }
        
        @Override
        protected boolean isSelected(final int slotIndex) {
            return false;
        }
        
        @Override
        protected void drawBackground() {
        }
        
        @Override
        protected void drawSlot(final int entryID, final int x, final int y, final int p_180791_4_, final int mouseXIn, final int mouseYIn) {
            final String sender = (String)IRC.getInstance().getMessageList().get(entryID).getKey();
            final String message = (String)IRC.getInstance().getMessageList().get(entryID).getValue();
            if (sender.equals(IRC.getInstance().getName())) {
                GlStateManager.pushMatrix();
                final float scale = 0.7f;
                GlStateManager.scale(scale, scale, scale);
                this.mc.fontRendererObj.drawString(sender, (int)((x + this.getListWidth() - (this.width - this.getScrollBarX()) - (((int)(this.mc.fontRendererObj.getStringWidth(sender) * scale) > this.mc.fontRendererObj.getStringWidth(message)) ? (this.mc.fontRendererObj.getStringWidth(sender) * scale) : ((float)this.mc.fontRendererObj.getStringWidth(message)))) / scale), (int)((y + 2) / scale), ColorUtils.rainbowEffect());
                GlStateManager.popMatrix();
                this.mc.fontRendererObj.drawString(String.valueOf(EnumChatFormatting.GRAY.toString()) + message, (int)(x + this.getListWidth() - (this.width - this.getScrollBarX()) - (((int)(this.mc.fontRendererObj.getStringWidth(sender) * scale) > this.mc.fontRendererObj.getStringWidth(message)) ? (this.mc.fontRendererObj.getStringWidth(sender) * scale) : ((float)this.mc.fontRendererObj.getStringWidth(message)))), y + 8, -1);
            }
            else {
                GlStateManager.pushMatrix();
                final float scale = 0.7f;
                GlStateManager.scale(scale, scale, scale);
                this.mc.fontRendererObj.drawString(sender, (int)(x / scale), (int)((y + 2) / scale), ColorUtils.rainbowEffect());
                GlStateManager.popMatrix();
                this.mc.fontRendererObj.drawString(String.valueOf(EnumChatFormatting.GRAY.toString()) + message, x, y + 8, -1);
            }
        }
    }
}
