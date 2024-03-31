// 
// Decompiled by Procyon v0.6.0
// 

package net.labymod.api;

import net.lenni0451.eventapi.manager.ASMEventManager;
import net.labymod.labyconnect.packets.PacketAddonDevelopment;
import java.util.UUID;
import net.labymod.utils.ModTextureUtils;
import net.labymod.utils.texture.DynamicTextureManager;
import net.labymod.user.UserManager;
import net.labymod.utils.ServerData;
import net.labymod.utils.DrawUtils;
import net.labymod.labyconnect.ClientConnection;
import net.labymod.labyconnect.LabyConnect;
import net.labymod.labyconnect.packets.Packet;
import net.labymod.labyconnect.packets.PacketAddonMessage;
import net.labymod.servermanager.Server;
import net.labymod.support.util.Debug;
import net.minecraft.network.PacketBuffer;
import io.netty.buffer.Unpooled;
import net.minecraft.client.Minecraft;
import net.labymod.core.LabyModCore;
import com.google.gson.JsonElement;
import net.labymod.main.LabyMod;

public class LabyModAPI
{
    public static final String LABYMOD_API_CHANNEL = "LMC";
    private final LabyMod labyMod;
    private boolean crosshairHidden;
    
    public LabyModAPI(final LabyMod labyMod) {
        this.labyMod = labyMod;
    }
    
    public void sendJsonMessageToServer(final String messageKey, final JsonElement message) {
        if (LabyModCore.getMinecraft().getPlayer() == null || Minecraft.getMinecraft().isSingleplayer()) {
            return;
        }
        final PacketBuffer packetBuffer = new PacketBuffer(Unpooled.buffer());
        packetBuffer.writeString(messageKey);
        packetBuffer.writeString(message.toString());
        Debug.log(Debug.EnumDebugMode.PLUGINMESSAGE, "[OUT] [LMC] " + message.toString());
        LabyModCore.getMinecraft().sendPluginMessage("LMC", packetBuffer);
    }
    
    public void registerServerSupport(final LabyModAddon labyModAddon, final Server server) {
        this.labyMod.getServerManager().registerServerSupport(labyModAddon, server);
    }
    
    public void sendPluginMessage(final String channelName, final PacketBuffer packetBuffer) {
        LabyModCore.getMinecraft().sendPluginMessage(channelName, packetBuffer);
    }
    
    public void sendAddonMessage(final String key, final byte[] data) {
        final LabyConnect chatClient = LabyMod.getInstance().getLabyConnect();
        final ClientConnection clientConnection = chatClient.getClientConnection();
        if (clientConnection != null && chatClient.isOnline()) {
            clientConnection.sendPacket(new PacketAddonMessage(key, data));
        }
    }
    
    public void sendAddonMessage(final String key, final String json) {
        final LabyConnect chatClient = LabyMod.getInstance().getLabyConnect();
        final ClientConnection clientConnection = chatClient.getClientConnection();
        if (clientConnection != null && chatClient.isOnline()) {
            clientConnection.sendPacket(new PacketAddonMessage(key, json));
        }
    }
    
    public DrawUtils getDrawUtils() {
        return this.labyMod.getDrawUtils();
    }
    
    public ServerData getCurrentServer() {
        return this.labyMod.getCurrentServerData();
    }
    
    public boolean isCurrentlyPlayingOn(final String address) {
        return this.labyMod.getCurrentServerData() != null && this.labyMod.getCurrentServerData().getIp().toLowerCase().contains(address.toLowerCase());
    }
    
    public boolean isIngame() {
        return this.labyMod.isInGame();
    }
    
    public boolean hasGameFocus() {
        return LabyModCore.getMinecraft().hasInGameFocus();
    }
    
    public boolean isCurrentScreenNull() {
        return LabyModCore.getMinecraft().isCurrentScreenNull();
    }
    
    public boolean isMinecraftChatOpen() {
        return LabyModCore.getMinecraft().isMinecraftChatOpen();
    }
    
    public UserManager getUserManager() {
        return this.labyMod.getUserManager();
    }
    
    public DynamicTextureManager getDynamicTextureManager() {
        return this.labyMod.getDynamicTextureManager();
    }
    
    @Deprecated
    public ModTextureUtils getModTextureUtils() {
        return ModTextureUtils.INSTANCE;
    }
    
    public LabyConnect getLabyModChatClient() {
        return this.labyMod.getLabyConnect();
    }
    
    public UUID getPlayerUUID() {
        return this.labyMod.getPlayerUUID();
    }
    
    public String getPlayerUsername() {
        return this.labyMod.getPlayerName();
    }
    
    public void displayMessageInChat(final String message) {
        this.labyMod.displayMessageInChat(message);
    }
    
    public void connectToServer(final String address) {
        this.labyMod.connectToServer(address);
    }
    
    public void updateCurrentGamemode(final String gamemodeName) {
        this.labyMod.getLabyConnect().updatePlayingOnServerState(gamemodeName);
    }
    
    public void sendAddonDevelopmentPacket(final PacketAddonDevelopment packetAddonDevelopment) {
        this.labyMod.getLabyConnect().getClientConnection().sendPacket(packetAddonDevelopment);
    }
    
    public void registerForgeListener(final Object target) {
        ASMEventManager.register(target);
    }
    
    public EventManager getEventManager() {
        return this.labyMod.getEventManager();
    }
    
    public boolean isCrosshairHidden() {
        return this.crosshairHidden;
    }
    
    public void setCrosshairHidden(final boolean crosshairHidden) {
        this.crosshairHidden = crosshairHidden;
    }
}
