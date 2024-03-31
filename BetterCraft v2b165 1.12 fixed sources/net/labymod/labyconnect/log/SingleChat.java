// 
// Decompiled by Procyon v0.6.0
// 

package net.labymod.labyconnect.log;

import net.labymod.labyconnect.LabyConnect;
import net.labymod.labyconnect.user.UserStatus;
import net.labymod.labyconnect.packets.Packet;
import net.labymod.labyconnect.packets.PacketMessage;
import java.util.Collections;
import java.util.ArrayList;
import net.labymod.main.LabyMod;
import java.util.List;
import net.labymod.labyconnect.user.ChatUser;

public class SingleChat
{
    private final int id;
    private ChatUser chatPartner;
    private List<MessageChatComponent> messages;
    private LabyMod labyMod;
    
    public SingleChat(final LabyMod labyMod, final int id, final ChatUser friend, final List<MessageChatComponent> messages) {
        this.messages = Collections.synchronizedList(new ArrayList<MessageChatComponent>());
        this.id = id;
        this.chatPartner = friend;
        this.messages = messages;
        this.labyMod = labyMod;
    }
    
    public void addMessage(final MessageChatComponent message) {
        this.messages.add(message);
        this.chatPartner.setLastInteraction(System.currentTimeMillis());
        final LabyConnect labyconnect = this.labyMod.getLabyConnect();
        final UserStatus userstatus = labyconnect.getClientProfile().getUserStatus();
        final boolean flag = message.getSender().equalsIgnoreCase(this.labyMod.getPlayerName());
        final boolean flag2 = true;
        if (flag) {
            if (this.chatPartner.isParty()) {
                this.labyMod.getLabyPlay().getPartySystem().sendChatMessage(message.getMessage());
            }
            else {
                final ChatUser chatuser = labyconnect.getClientProfile().buildClientUser();
                final PacketMessage packetmessage = new PacketMessage(chatuser, this.chatPartner, message.getMessage(), 0L, 0.0, System.currentTimeMillis());
                labyconnect.getClientConnection().sendPacket(packetmessage);
            }
        }
        else {
            final UserStatus busy = UserStatus.BUSY;
        }
    }
    
    public SingleChat apply(final ChatUser chatUser) {
        this.chatPartner = chatUser;
        return this;
    }
    
    public int getId() {
        return this.id;
    }
    
    public ChatUser getChatPartner() {
        return this.chatPartner;
    }
    
    public List<MessageChatComponent> getMessages() {
        return this.messages;
    }
    
    public void setChatPartner(final ChatUser chatPartner) {
        this.chatPartner = chatPartner;
    }
    
    public void setMessages(final List<MessageChatComponent> messages) {
        this.messages = messages;
    }
}
