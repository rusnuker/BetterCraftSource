// 
// Decompiled by Procyon v0.6.0
// 

package net.labymod.labyconnect.gui.elements;

import net.labymod.labyconnect.log.SingleChat;
import net.labymod.labyconnect.log.MessageChatComponent;
import net.labymod.labyconnect.packets.Packet;
import net.labymod.labyconnect.packets.PacketPlayTyping;
import net.labymod.main.LabyMod;
import net.labymod.main.lang.LanguageManager;
import net.labymod.core.LabyModCore;
import java.util.List;
import net.minecraft.client.gui.GuiButton;
import net.labymod.gui.elements.ModTextField;
import net.labymod.labyconnect.gui.GuiFriendsLayout;
import net.labymod.gui.layout.WindowElement;

public class WinMessageField extends WindowElement<GuiFriendsLayout>
{
    private static String storedMessage;
    private static long cooldown;
    private ModTextField fieldMessage;
    private GuiButton buttonSendMessage;
    private long typingCooldown;
    
    static {
        WinMessageField.storedMessage = "";
        WinMessageField.cooldown = 0L;
    }
    
    public WinMessageField(final GuiFriendsLayout chatLayout) {
        super(chatLayout);
        this.typingCooldown = 0L;
    }
    
    @Override
    protected void init(final List<GuiButton> buttonlist, final int left, final int top, final int right, final int bottom) {
        final int paddingHeight = 3;
        final int paddingWidth = 0;
        final int dragBarWidth = 2;
        final int spaceBetween = 5;
        final int buttonWidth = 70;
        (this.fieldMessage = new ModTextField(0, LabyModCore.getMinecraft().getFontRenderer(), left + 0 + 2, top + 3, right - left - 70 - 0 - 5, bottom - top - 6)).setBlackBox(false);
        this.fieldMessage.setMaxStringLength(120);
        this.fieldMessage.setText(WinMessageField.storedMessage);
        buttonlist.add(this.buttonSendMessage = new GuiButton(1, right - 70, top + (bottom - top - 20) / 2, 70, 20, LanguageManager.translate("button_send")));
    }
    
    @Override
    public void draw(final int mouseX, final int mouseY) {
        super.draw(mouseX, mouseY);
        this.fieldMessage.drawTextBox();
        this.buttonSendMessage.enabled = (GuiFriendsLayout.selectedUser != null && !this.fieldMessage.getText().replaceAll(" ", "").isEmpty() && WinMessageField.cooldown < System.currentTimeMillis());
        this.fieldMessage.setEnabled(GuiFriendsLayout.selectedUser != null);
    }
    
    @Override
    public boolean mouseClicked(final int mouseX, final int mouseY, final int mouseButton) {
        this.fieldMessage.mouseClicked(mouseX, mouseY, mouseButton);
        return false;
    }
    
    @Override
    public void keyTyped(final char typedChar, final int keyCode) {
        if (this.fieldMessage.textboxKeyTyped(typedChar, keyCode)) {
            WinMessageField.storedMessage = this.fieldMessage.getText();
            if (GuiFriendsLayout.selectedUser != null && this.typingCooldown + 1000L < System.currentTimeMillis()) {
                this.typingCooldown = System.currentTimeMillis();
                LabyMod.getInstance().getLabyConnect().getClientConnection().sendPacket(new PacketPlayTyping(LabyMod.getInstance().getLabyConnect().getClientProfile().buildClientUser(), GuiFriendsLayout.selectedUser, true));
            }
        }
        if (keyCode == 28 && this.fieldMessage.isFocused()) {
            this.actionPerformed(this.buttonSendMessage);
        }
    }
    
    @Override
    public void actionPerformed(final GuiButton button) {
        if (button.enabled && button.id == 1 && GuiFriendsLayout.selectedUser != null && !this.fieldMessage.getText().replaceAll(" ", "").isEmpty()) {
            final SingleChat singleChat = LabyMod.getInstance().getLabyConnect().getChatlogManager().getChat(GuiFriendsLayout.selectedUser);
            final MessageChatComponent messageChatComponent = new MessageChatComponent(LabyMod.getInstance().getPlayerName(), System.currentTimeMillis(), this.fieldMessage.getText());
            singleChat.addMessage(messageChatComponent);
            this.fieldMessage.setText("");
            WinMessageField.storedMessage = "";
            WinMessageField.cooldown = System.currentTimeMillis() + 1000L;
        }
    }
    
    @Override
    public void mouseClickMove(final int mouseX, final int mouseY) {
    }
    
    @Override
    public void mouseReleased(final int mouseX, final int mouseY, final int mouseButton) {
    }
    
    @Override
    public void mouseInput() {
    }
    
    @Override
    public void updateScreen() {
        this.fieldMessage.updateCursorCounter();
    }
    
    public ModTextField getFieldMessage() {
        return this.fieldMessage;
    }
}
