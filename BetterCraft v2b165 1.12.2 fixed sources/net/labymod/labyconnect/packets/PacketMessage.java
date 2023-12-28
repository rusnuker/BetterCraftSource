// 
// Decompiled by Procyon v0.6.0
// 

package net.labymod.labyconnect.packets;

import net.labymod.labyconnect.handling.PacketHandler;
import net.labymod.labyconnect.user.ChatUser;

public class PacketMessage extends Packet
{
    private ChatUser sender;
    private ChatUser to;
    private String message;
    private long sentTime;
    private long fileSize;
    private double audioTime;
    
    public PacketMessage(final ChatUser sender, final ChatUser to, final String message, final long fileSize, final double time, final long sentTime) {
        this.sender = sender;
        this.to = to;
        this.message = message;
        this.fileSize = fileSize;
        this.audioTime = time;
        this.sentTime = sentTime;
    }
    
    public PacketMessage() {
    }
    
    @Override
    public void read(final PacketBuf buf) {
        this.sender = buf.readChatUser();
        this.to = buf.readChatUser();
        this.message = buf.readString();
        this.fileSize = buf.readLong();
        this.audioTime = buf.readDouble();
        this.sentTime = buf.readLong();
    }
    
    @Override
    public void write(final PacketBuf buf) {
        buf.writeChatUser(this.sender);
        buf.writeChatUser(this.to);
        buf.writeString(this.message);
        buf.writeLong(this.fileSize);
        buf.writeDouble(this.audioTime);
        buf.writeLong(this.sentTime);
    }
    
    @Override
    public void handle(final PacketHandler packetHandler) {
        packetHandler.handle(this);
    }
    
    public double getAudioTime() {
        return this.audioTime;
    }
    
    public long getFileSize() {
        return this.fileSize;
    }
    
    public String getMessage() {
        return this.message;
    }
    
    public ChatUser getSender() {
        return this.sender;
    }
    
    public ChatUser getTo() {
        return this.to;
    }
    
    public long getSentTime() {
        return this.sentTime;
    }
}
