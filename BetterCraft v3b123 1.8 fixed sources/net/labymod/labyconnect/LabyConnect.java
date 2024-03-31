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
import net.labymod.support.util.Debug;
import net.minecraft.client.Minecraft;
import net.labymod.labyconnect.packets.EnumConnectionState;
import com.google.gson.JsonObject;
import com.google.gson.JsonElement;
import net.labymod.main.LabyMod;
import java.util.ArrayList;
import net.labymod.labyconnect.user.EnumAlertDisplayType;
import net.labymod.labyconnect.packets.PacketPlayServerStatusUpdate;
import net.labymod.labyconnect.user.ClientProfile;
import net.labymod.labyconnect.user.ChatRequest;
import net.labymod.labyconnect.user.ChatUser;
import java.util.List;
import net.labymod.labyconnect.log.ChatlogManager;
import net.labymod.api.events.ServerMessageEvent;

public class LabyConnect extends Thread implements ServerMessageEvent
{
    private static final long RECONNECT_INTERVAL = 60000L;
    private ClientConnection clientConnection;
    private ChatlogManager chatlogManager;
    private List<ChatUser> friends;
    private List<ChatRequest> requests;
    private List<ChatUser> sortFriends;
    private ClientProfile clientProfile;
    private PacketPlayServerStatusUpdate lastPacketPlayServerStatus;
    private EnumAlertDisplayType alertDisplayType;
    private boolean forcedLogout;
    private boolean viaServerList;
    
    public LabyConnect() {
        this.chatlogManager = new ChatlogManager();
        this.friends = new ArrayList<ChatUser>();
        this.requests = new ArrayList<ChatRequest>();
        this.sortFriends = new ArrayList<ChatUser>();
        this.forcedLogout = false;
        this.viaServerList = false;
        try {
            this.clientConnection = new ClientConnection(this);
            this.clientProfile = new ClientProfile(this, this.clientConnection);
        }
        catch (final Throwable error) {
            error.printStackTrace();
        }
        this.updateAlertDisplayType();
        this.start();
        Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
            @Override
            public void run() {
                if (LabyConnect.this.clientConnection != null) {
                    LabyConnect.this.clientConnection.disconnect(false);
                }
            }
        }));
        LabyMod.getInstance().getEventManager().register(this);
    }
    
    @Override
    public void onServerMessage(final String messageKey, final JsonElement serverMessage) {
        if (messageKey.equals("server_gamemode")) {
            try {
                final JsonObject jsonObject = serverMessage.getAsJsonObject();
                if (jsonObject.has("show_gamemode")) {
                    final boolean showGamemode = jsonObject.get("show_gamemode").getAsBoolean();
                    if (showGamemode) {
                        if (jsonObject.has("gamemode_name")) {
                            this.updatePlayingOnServerState(jsonObject.get("gamemode_name").getAsString());
                        }
                    }
                    else {
                        this.updatePlayingOnServerState(null);
                    }
                }
            }
            catch (final Exception error) {
                error.printStackTrace();
            }
        }
    }
    
    @Override
    public void run() {
        do {
            try {
                if (this.clientConnection.getCurrentConnectionState() == EnumConnectionState.OFFLINE && Minecraft.getMinecraft().getSession().getToken() != null && !this.forcedLogout) {
                    this.clientConnection.connect();
                }
            }
            catch (final Exception error) {
                error.printStackTrace();
            }
            try {
                Thread.sleep(60000L);
            }
            catch (final InterruptedException e) {
                e.printStackTrace();
            }
        } while (this.clientConnection != null && !Debug.isActive());
    }
    
    public void updateAlertDisplayType() {
        try {
            this.alertDisplayType = EnumAlertDisplayType.valueOf(LabyMod.getSettings().alertDisplayType);
        }
        catch (final Exception error) {
            this.alertDisplayType = EnumAlertDisplayType.ACHIEVEMENT;
            error.printStackTrace();
        }
    }
    
    public boolean isOnline() {
        return this.clientConnection != null && this.clientConnection.getCurrentConnectionState() == EnumConnectionState.PLAY;
    }
    
    public ChatUser getChatUser(final ChatUser chatUser) {
        return this.getChatUserByUUID(chatUser.getGameProfile().getId());
    }
    
    public ChatUser getChatUserByUUID(final UUID uuid) {
        for (final ChatUser chatUser : this.friends) {
            if (chatUser.getGameProfile().getId().equals(uuid)) {
                return chatUser;
            }
        }
        return null;
    }
    
    public void updatePlayingOnServerState(final String gamemode) {
        final ServerData serverData = LabyMod.getInstance().getCurrentServerData();
        final boolean viaServerlist = this.viaServerList && serverData != null;
        PacketPlayServerStatusUpdate packet;
        if (serverData == null || !LabyMod.getInstance().isInGame() || Minecraft.getMinecraft().isSingleplayer()) {
            packet = new PacketPlayServerStatusUpdate();
        }
        else {
            packet = new PacketPlayServerStatusUpdate(serverData.getIp(), serverData.getPort(), (gamemode == null) ? "" : gamemode, viaServerlist);
        }
        if (this.lastPacketPlayServerStatus != null && this.lastPacketPlayServerStatus.equals(packet)) {
            return;
        }
        this.lastPacketPlayServerStatus = packet;
        if (LabyMod.getSettings().showConnectedIp) {
            this.getClientConnection().sendPacket(packet);
        }
    }
    
    public void sortFriendList(final int sortMode) {
        final List<ChatUser> sortedList = new ArrayList<ChatUser>();
        for (final ChatUser chatUser : this.friends) {
            switch (sortMode) {
                case 1: {
                    if (!chatUser.isOnline()) {
                        continue;
                    }
                    break;
                }
                case 2: {
                    if (this.getChatlogManager().getChat(chatUser).getMessages().isEmpty()) {
                        continue;
                    }
                    break;
                }
            }
            sortedList.add(chatUser);
        }
        Collections.sort(sortedList, new Comparator<ChatUser>() {
            @Override
            public int compare(final ChatUser a, final ChatUser b) {
                if (a.isParty()) {
                    return Integer.MIN_VALUE;
                }
                switch (sortMode) {
                    case 0: {
                        final long la = a.isOnline() ? (a.getLastOnline() / 1000L + 1L) : (a.getLastOnline() / 2000L);
                        final long lb = b.isOnline() ? (b.getLastOnline() / 1000L + 1L) : (b.getLastOnline() / 2000L);
                        return (int)(lb - la);
                    }
                    case 1: {
                        return 0;
                    }
                    case 2: {
                        final long la = a.isOnline() ? (a.getLastInteraction() / 1000L) : (a.getLastInteraction() / 2000L);
                        final long lb = b.isOnline() ? (b.getLastInteraction() / 1000L) : (b.getLastInteraction() / 2000L);
                        return (int)(lb - la);
                    }
                    default: {
                        return 0;
                    }
                }
            }
        });
        this.sortFriends = sortedList;
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
    
    public PacketPlayServerStatusUpdate getLastPacketPlayServerStatus() {
        return this.lastPacketPlayServerStatus;
    }
    
    public EnumAlertDisplayType getAlertDisplayType() {
        return this.alertDisplayType;
    }
    
    public boolean isForcedLogout() {
        return this.forcedLogout;
    }
    
    public boolean isViaServerList() {
        return this.viaServerList;
    }
    
    public void setForcedLogout(final boolean forcedLogout) {
        this.forcedLogout = forcedLogout;
    }
    
    public void setViaServerList(final boolean viaServerList) {
        this.viaServerList = viaServerList;
    }
}
