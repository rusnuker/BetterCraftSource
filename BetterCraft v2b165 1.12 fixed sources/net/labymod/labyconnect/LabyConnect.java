// 
// Decompiled by Procyon v0.6.0
// 

package net.labymod.labyconnect;

import java.util.Collections;
import java.util.Comparator;
import net.labymod.utils.ServerData;
import net.labymod.labyconnect.packets.Packet;
import java.util.Iterator;
import java.util.UUID;
import net.labymod.labyconnect.packets.EnumConnectionState;
import java.util.ArrayList;
import net.labymod.main.LabyMod;
import net.labymod.labyconnect.user.EnumAlertDisplayType;
import net.labymod.labyconnect.packets.PacketPlayServerStatus;
import net.labymod.labyconnect.user.ClientProfile;
import net.labymod.labyconnect.user.ChatRequest;
import net.labymod.labyconnect.user.ChatUser;
import java.util.List;
import net.labymod.labyconnect.log.ChatlogManager;

public class LabyConnect extends Thread
{
    private static final long RECONNECT_INTERVAL = 60000L;
    private ClientConnection clientConnection;
    private ChatlogManager chatlogManager;
    private List<ChatUser> friends;
    private List<ChatRequest> requests;
    private List<ChatUser> sortFriends;
    private ClientProfile clientProfile;
    private PacketPlayServerStatus lastPacketPlayServerStatus;
    private EnumAlertDisplayType alertDisplayType;
    public LabyMod labyMod;
    
    public LabyConnect(final LabyMod labyMod) {
        this.friends = new ArrayList<ChatUser>();
        this.requests = new ArrayList<ChatRequest>();
        this.sortFriends = new ArrayList<ChatUser>();
        this.labyMod = labyMod;
        this.chatlogManager = new ChatlogManager(labyMod);
        try {
            this.clientConnection = new ClientConnection(labyMod, this);
            this.clientProfile = new ClientProfile(this, this.clientConnection);
        }
        catch (final Throwable throwable) {
            throwable.printStackTrace();
        }
        this.start();
        Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
            @Override
            public void run() {
                if (LabyConnect.this.clientConnection != null) {
                    LabyConnect.this.clientConnection.disconnect(false);
                }
            }
        }));
    }
    
    @Override
    public void run() {
        while (this.clientConnection != null) {
            try {
                this.clientConnection.getCurrentConnectionState();
            }
            catch (final Exception exception) {
                exception.printStackTrace();
            }
            try {
                Thread.sleep(60000L);
            }
            catch (final InterruptedException interruptedexception) {
                interruptedexception.printStackTrace();
            }
        }
    }
    
    public boolean isOnline() {
        return this.clientConnection != null && this.clientConnection.getCurrentConnectionState() == EnumConnectionState.PLAY;
    }
    
    public ChatUser getChatUser(final ChatUser chatUser) {
        return this.getChatUserByUUID(chatUser.getGameProfile().getId());
    }
    
    public ChatUser getChatUserByUUID(final UUID uuid) {
        for (final ChatUser chatuser : this.friends) {
            if (!chatuser.getGameProfile().getId().equals(uuid)) {
                continue;
            }
            return chatuser;
        }
        return null;
    }
    
    public void updatePlayingOnServerState(final String gamemode) {
        final ServerData serverdata = this.labyMod.getCurrentServerData();
        final PacketPlayServerStatus packetplayserverstatus = (serverdata != null && this.labyMod.isInGame()) ? new PacketPlayServerStatus(serverdata.getIp(), serverdata.getPort(), (gamemode == null) ? "" : gamemode) : new PacketPlayServerStatus("", 25565, "");
        if (this.lastPacketPlayServerStatus == null || !this.lastPacketPlayServerStatus.equals(packetplayserverstatus)) {
            this.lastPacketPlayServerStatus = packetplayserverstatus;
            this.getClientConnection().sendPacket(packetplayserverstatus);
        }
    }
    
    public void sortFriendList(final int sortMode) {
        final List<ChatUser> list = new ArrayList<ChatUser>();
        for (final ChatUser chatuser : this.friends) {
            switch (sortMode) {
                case 1: {
                    if (chatuser.isOnline()) {
                        break;
                    }
                    break;
                }
                case 2: {
                    if (this.getChatlogManager().getChat(chatuser).getMessages().isEmpty()) {
                        continue;
                    }
                    break;
                }
            }
            list.add(chatuser);
        }
        Collections.sort(list, new Comparator<ChatUser>() {
            @Override
            public int compare(final ChatUser a, final ChatUser b) {
                if (a.isParty()) {
                    return Integer.MIN_VALUE;
                }
                switch (sortMode) {
                    case 0: {
                        final long k = a.isOnline() ? (a.getLastOnline() / 1000L + 1L) : (a.getLastOnline() / 2000L);
                        final long l = b.isOnline() ? (b.getLastOnline() / 1000L + 1L) : (b.getLastOnline() / 2000L);
                        return (int)(l - k);
                    }
                    case 1: {
                        return 0;
                    }
                    case 2: {
                        final long i = a.isOnline() ? (a.getLastInteraction() / 1000L) : (a.getLastInteraction() / 2000L);
                        final long j = b.isOnline() ? (b.getLastInteraction() / 1000L) : (b.getLastInteraction() / 2000L);
                        return (int)(j - i);
                    }
                    default: {
                        return 0;
                    }
                }
            }
        });
        this.sortFriends = list;
    }
    
    public ClientConnection getClientConnection() {
        return this.clientConnection;
    }
    
    public ChatlogManager getChatlogManager() {
        return this.chatlogManager;
    }
    
    public List<ChatUser> getFriends() {
        return this.friends;
    }
    
    public List<ChatRequest> getRequests() {
        return this.requests;
    }
    
    public List<ChatUser> getSortFriends() {
        return this.sortFriends;
    }
    
    public ClientProfile getClientProfile() {
        return this.clientProfile;
    }
    
    public PacketPlayServerStatus getLastPacketPlayServerStatus() {
        return this.lastPacketPlayServerStatus;
    }
    
    public EnumAlertDisplayType getAlertDisplayType() {
        return this.alertDisplayType;
    }
}
