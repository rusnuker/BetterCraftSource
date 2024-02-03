// 
// Decompiled by Procyon v0.6.0
// 

package net.labymod.labyconnect.log;

import net.labymod.labyconnect.LabyConnect;
import net.labymod.labyconnect.gui.GuiFriendsLayout;
import net.minecraft.client.Minecraft;
import org.lwjgl.opengl.Display;
import net.labymod.labyconnect.user.UserStatus;
import net.labymod.labyconnect.packets.Packet;
import net.labymod.labyconnect.packets.PacketMessage;
import net.labymod.core.LabyModCore;
import net.labymod.main.LabyMod;
import java.util.Collections;
import java.util.ArrayList;
import net.labymod.main.Source;
import java.util.List;
import net.labymod.labyconnect.user.ChatUser;
import net.minecraft.util.ResourceLocation;

public class SingleChat
{
    private static final ResourceLocation POP_SOUND;
    private final int id;
    private ChatUser chatPartner;
    private List<MessageChatComponent> messages;
    
    static {
        POP_SOUND = new ResourceLocation(Source.ABOUT_MC_VERSION.startsWith("1.8") ? "random.pop" : "entity.chicken.egg");
    }
    
    public SingleChat(final int id, final ChatUser friend, final List<MessageChatComponent> messages) {
        this.messages = Collections.synchronizedList(new ArrayList<MessageChatComponent>());
        this.id = id;
        this.chatPartner = friend;
        this.messages = messages;
    }
    
    public void addMessage(final MessageChatComponent message) {
        this.messages.add(message);
        this.chatPartner.setLastInteraction(System.currentTimeMillis());
        final LabyConnect chatClient = LabyMod.getInstance().getLabyConnect();
        final UserStatus userStatus = chatClient.getClientProfile().getUserStatus();
        final boolean isClientSender = message.getSender().equalsIgnoreCase(LabyMod.getInstance().getPlayerName());
        final boolean playSounds = LabyMod.getSettings().alertPlaySounds;
        if (isClientSender) {
            if (playSounds) {
                LabyModCore.getMinecraft().playSound(SingleChat.POP_SOUND, 1.5f);
            }
            if (this.chatPartner.isParty()) {
                LabyMod.getInstance().getLabyPlay().getPartySystem().sendChatMessage(message.getMessage());
            }
            else {
                final ChatUser clientUser = chatClient.getClientProfile().buildClientUser();
                final PacketMessage packet = new PacketMessage(clientUser, this.chatPartner, message.getMessage(), 0L, 0.0, System.currentTimeMillis());
                chatClient.getClientConnection().sendPacket(packet);
            }
        }
        else if (playSounds && userStatus != UserStatus.BUSY) {
            LabyModCore.getMinecraft().playSound(SingleChat.POP_SOUND, 2.5f);
            if (!Display.isActive() || Minecraft.getMinecraft().currentScreen == null || !(Minecraft.getMinecraft().currentScreen instanceof GuiFriendsLayout) || GuiFriendsLayout.selectedUser == null || !GuiFriendsLayout.selectedUser.equals(this.chatPartner)) {
                this.chatPartner.increaseUnreadMessages();
            }
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
