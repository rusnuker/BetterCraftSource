// 
// Decompiled by Procyon v0.6.0
// 

package net.labymod.labyconnect.handling;

import net.labymod.labyconnect.packets.PacketUserBadge;
import net.labymod.labyconnect.packets.PacketAddonMessage;
import net.labymod.labyconnect.packets.PacketUpdateCosmetics;
import net.labymod.labyconnect.packets.PacketMojangStatus;
import net.labymod.labyconnect.packets.PacketEncryptionResponse;
import net.labymod.labyconnect.packets.PacketEncryptionRequest;
import net.labymod.labyconnect.packets.PacketLoginVersion;
import net.labymod.labyconnect.packets.PacketLoginTime;
import net.labymod.labyconnect.packets.PacketPlayChangeOptions;
import net.labymod.labyconnect.packets.PacketPlayFriendPlayingOn;
import net.labymod.labyconnect.packets.PacketPlayFriendStatus;
import net.labymod.labyconnect.packets.PacketPlayServerStatusUpdate;
import net.labymod.labyconnect.packets.PacketPlayServerStatus;
import net.labymod.labyconnect.packets.PacketLoginOptions;
import net.labymod.labyconnect.packets.PacketPlayFriendRemove;
import net.labymod.labyconnect.packets.PacketPlayDenyFriendRequest;
import net.labymod.labyconnect.packets.PacketPlayRequestRemove;
import net.labymod.labyconnect.packets.PacketPlayRequestAddFriendResponse;
import net.labymod.labyconnect.packets.PacketPlayTyping;
import net.labymod.labyconnect.packets.PacketMessage;
import net.labymod.labyconnect.packets.PacketServerMessage;
import net.labymod.labyconnect.packets.PacketPong;
import net.labymod.labyconnect.packets.PacketPing;
import net.labymod.labyconnect.packets.PacketBanned;
import net.labymod.labyconnect.packets.PacketLoginRequest;
import net.labymod.labyconnect.packets.PacketLoginFriend;
import net.labymod.labyconnect.packets.PacketPlayRequestAddFriend;
import net.labymod.labyconnect.packets.PacketDisconnect;
import net.labymod.labyconnect.packets.PacketKick;
import net.labymod.labyconnect.packets.PacketChatVisibilityChange;
import net.labymod.labyconnect.packets.PacketLoginComplete;
import net.labymod.labyconnect.packets.PacketPlayPlayerOnline;
import net.labymod.labyconnect.packets.PacketHelloPong;
import net.labymod.labyconnect.packets.PacketHelloPing;
import net.labymod.labyconnect.packets.PacketLoginData;
import net.labymod.labyconnect.packets.Packet;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

public abstract class PacketHandler extends SimpleChannelInboundHandler<Object>
{
    @Override
    protected void channelRead0(final ChannelHandlerContext ctx, final Object packet) throws Exception {
        this.handlePacket((Packet)packet);
    }
    
    private void handlePacket(final Packet packet) {
        packet.handle(this);
    }
    
    public abstract void handle(final PacketLoginData p0);
    
    public abstract void handle(final PacketHelloPing p0);
    
    public abstract void handle(final PacketHelloPong p0);
    
    public abstract void handle(final PacketPlayPlayerOnline p0);
    
    public abstract void handle(final PacketLoginComplete p0);
    
    public abstract void handle(final PacketChatVisibilityChange p0);
    
    public abstract void handle(final PacketKick p0);
    
    public abstract void handle(final PacketDisconnect p0);
    
    public abstract void handle(final PacketPlayRequestAddFriend p0);
    
    public abstract void handle(final PacketLoginFriend p0);
    
    public abstract void handle(final PacketLoginRequest p0);
    
    public abstract void handle(final PacketBanned p0);
    
    public abstract void handle(final PacketPing p0);
    
    public abstract void handle(final PacketPong p0);
    
    public abstract void handle(final PacketServerMessage p0);
    
    public abstract void handle(final PacketMessage p0);
    
    public abstract void handle(final PacketPlayTyping p0);
    
    public abstract void handle(final PacketPlayRequestAddFriendResponse p0);
    
    public abstract void handle(final PacketPlayRequestRemove p0);
    
    public abstract void handle(final PacketPlayDenyFriendRequest p0);
    
    public abstract void handle(final PacketPlayFriendRemove p0);
    
    public abstract void handle(final PacketLoginOptions p0);
    
    public abstract void handle(final PacketPlayServerStatus p0);
    
    public abstract void handle(final PacketPlayServerStatusUpdate p0);
    
    public abstract void handle(final PacketPlayFriendStatus p0);
    
    public abstract void handle(final PacketPlayFriendPlayingOn p0);
    
    public abstract void handle(final PacketPlayChangeOptions p0);
    
    public abstract void handle(final PacketLoginTime p0);
    
    public abstract void handle(final PacketLoginVersion p0);
    
    public abstract void handle(final PacketEncryptionRequest p0);
    
    public abstract void handle(final PacketEncryptionResponse p0);
    
    public abstract void handle(final PacketMojangStatus p0);
    
    public abstract void handle(final PacketUpdateCosmetics p0);
    
    public abstract void handle(final PacketAddonMessage p0);
    
    public abstract void handle(final PacketUserBadge p0);
}
