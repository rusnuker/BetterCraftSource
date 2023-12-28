// 
// Decompiled by Procyon v0.6.0
// 

package net.labymod.labyconnect.packets;

import com.google.common.base.Charsets;
import net.labymod.labyconnect.user.ServerInfo;
import net.labymod.labyconnect.user.UserStatus;
import java.util.UUID;
import net.labymod.labyconnect.user.ChatRequest;
import com.mojang.authlib.GameProfile;
import net.labymod.labyconnect.user.ChatUser;
import io.netty.buffer.ByteBuf;

public abstract class PacketBuf extends ByteBuf
{
    protected ByteBuf buf;
    
    public PacketBuf(final ByteBuf buf) {
        this.buf = buf;
    }
    
    public ChatUser readChatUser() {
        final String s = this.readString();
        final UUID uuid = this.readUUID();
        final String s2 = this.readString();
        final UserStatus userstatus = this.readUserStatus();
        final boolean flag = this.readBoolean();
        final String s3 = this.readString();
        final int i = this.readInt();
        final long j = this.readLong();
        final long k = this.readLong();
        final ServerInfo serverinfo = this.readServerInfo();
        return flag ? new ChatRequest(new GameProfile(uuid, s)) : new ChatUser(new GameProfile(uuid, s), userstatus, s2, serverinfo, 0, System.currentTimeMillis(), s3, j, k, i, false);
    }
    
    public void writeChatUser(final ChatUser player) {
        this.writeString(player.getGameProfile().getName());
        this.writeUUID(player.getGameProfile().getId());
        this.writeString(player.getStatusMessage());
        this.writeUserStatus(player.getStatus());
        this.writeBoolean(player.isFriendRequest());
        this.writeString(player.getTimeZone());
        this.writeInt(player.getContactAmount());
        this.writeLong(player.getLastOnline());
        this.writeLong(player.getFirstJoined());
        this.writeServerInfo(player.getCurrentServerInfo());
    }
    
    public ServerInfo readServerInfo() {
        final String s = this.readString();
        final int i = this.readInt();
        return this.readBoolean() ? new ServerInfo(s, i, this.readString()) : new ServerInfo(s, i);
    }
    
    public PacketBuf writeServerInfo(ServerInfo info) {
        if (info == null) {
            info = new ServerInfo("", 0);
        }
        this.writeString((info.getServerIp() == null) ? "" : info.getServerIp());
        this.writeInt(info.getServerPort());
        if (info.getSpecifiedServerName() != null) {
            this.writeBoolean(true);
            this.writeString(info.getSpecifiedServerName());
        }
        else {
            this.writeBoolean(false);
        }
        return this;
    }
    
    public PacketBuf writeUserStatus(final UserStatus status) {
        this.writeByte(status.getId());
        return this;
    }
    
    public UserStatus readUserStatus() {
        return UserStatus.getById(this.readByte());
    }
    
    public void writeByteArray(final byte[] data) {
        this.writeInt(data.length);
        this.writeBytes(data);
    }
    
    public byte[] readByteArray() {
        final byte[] abyte = new byte[this.readInt()];
        for (int i = 0; i < abyte.length; ++i) {
            abyte[i] = this.readByte();
        }
        return abyte;
    }
    
    public void writeEnum(final Enum<?> enume) {
        this.writeInt(enume.ordinal());
    }
    
    public void writeUUID(final UUID uuid) {
        this.writeString(uuid.toString());
    }
    
    public UUID readUUID() {
        return UUID.fromString(this.readString());
    }
    
    public void writeString(final String string) {
        this.writeInt(string.getBytes(Charsets.UTF_8).length);
        this.writeBytes(string.getBytes(Charsets.UTF_8));
    }
    
    public String readString() {
        final byte[] abyte = new byte[this.readInt()];
        for (int i = 0; i < abyte.length; ++i) {
            abyte[i] = this.readByte();
        }
        return new String(abyte, Charsets.UTF_8);
    }
    
    public static int getVarIntSize(final int input) {
        for (int i = 1; i < 5; ++i) {
            if ((input & -1 << i * 7) == 0x0) {
                return i;
            }
        }
        return 5;
    }
    
    public int readVarIntFromBuffer() {
        int i = 0;
        int j = 0;
        byte b0;
        do {
            b0 = this.readByte();
            i |= (b0 & 0x7F) << j++ * 7;
            if (j <= 5) {
                continue;
            }
            throw new RuntimeException("VarInt too big");
        } while ((b0 & 0x80) == 0x80);
        return i;
    }
    
    public void writeVarIntToBuffer(int input) {
        while ((input & 0xFFFFFF80) != 0x0) {
            this.writeByte((input & 0x7F) | 0x80);
            input >>>= 7;
        }
        this.writeByte(input);
    }
}
