// 
// Decompiled by Procyon v0.6.0
// 

package net.labymod.user.emote;

import java.nio.ByteOrder;
import java.nio.ByteBuffer;
import net.labymod.user.emote.keys.provider.EmoteProvider;
import net.labymod.user.emote.keys.provider.StoredEmote;
import net.labymod.labyconnect.packets.PacketActionPlayResponse;
import com.google.common.util.concurrent.FutureCallback;
import net.labymod.utils.ModColor;
import net.labymod.support.util.Debug;
import net.labymod.user.UserManager;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.lenni0451.eventapi.events.EventTarget;
import java.util.Iterator;
import me.nzxtercode.bettercraft.client.events.ClientTickEvent;
import com.google.gson.JsonObject;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import net.labymod.labyconnect.packets.Packet;
import net.labymod.labyconnect.packets.PacketActionRequest;
import net.labymod.core.LabyModCore;
import net.labymod.api.EventManager;
import net.labymod.main.LabyMod;
import java.util.concurrent.ConcurrentHashMap;
import java.util.HashMap;
import java.util.UUID;
import net.labymod.user.emote.keys.provider.KeyFrameStorage;
import java.util.Map;
import net.labymod.user.gui.EmoteSelectorGui;
import net.labymod.api.events.ServerMessageEvent;
import net.labymod.utils.Consumer;

public class EmoteRegistry implements Consumer<Object>, ServerMessageEvent
{
    private final EmoteSelectorGui emoteSelectorGui;
    protected Map<Short, KeyFrameStorage> emoteSources;
    protected Map<UUID, EmoteRenderer> playingEmotes;
    private boolean cleanPlayingMap;
    
    public EmoteRegistry() {
        this.emoteSources = new HashMap<Short, KeyFrameStorage>();
        this.playingEmotes = new ConcurrentHashMap<UUID, EmoteRenderer>();
        this.emoteSelectorGui = new EmoteSelectorGui();
        this.cleanPlayingMap = false;
    }
    
    public void init() {
        final EventManager eventManager = LabyMod.getInstance().getEventManager();
        eventManager.registerOnIncomingPacket(this);
        eventManager.register(this);
        new EmoteLoader(this).start();
    }
    
    @Override
    public void accept(final Object packet) {
        if (!LabyMod.getSettings().emotes) {
            return;
        }
        final UUID uuid = LabyModCore.getMinecraft().isEmotePacket(packet);
        if (uuid != null) {
            LabyMod.getInstance().getLabyConnect().getClientConnection().sendPacket(new PacketActionRequest(uuid));
        }
    }
    
    @Override
    public void onServerMessage(final String messageKey, final JsonElement serverMessage) {
        if (!messageKey.equals("emote_api")) {
            return;
        }
        final JsonArray array = serverMessage.getAsJsonArray();
        for (int i = 0; i < array.size(); ++i) {
            final JsonObject object = array.get(i).getAsJsonObject();
            if (object.has("uuid") && object.has("emote_id")) {
                final UUID uuid = UUID.fromString(object.get("uuid").getAsString());
                final short emoteId = object.get("emote_id").getAsShort();
                if (uuid.getLeastSignificantBits() == 0L) {
                    this.handleEmote(uuid, emoteId);
                }
            }
        }
    }
    
    @EventTarget
    public void handleEvent(final ClientTickEvent event) {
        if (!this.cleanPlayingMap) {
            return;
        }
        this.cleanPlayingMap = false;
        for (final EmoteRenderer renderer : this.playingEmotes.values()) {
            if (renderer.isAborted()) {
                this.abortEmote(renderer.getUuid(), false);
            }
        }
    }
    
    public EmoteRenderer getEmoteRendererFor(final AbstractClientPlayer abstractClientPlayer) {
        final UserManager userManager = LabyMod.getInstance().getUserManager();
        final UUID uuid = abstractClientPlayer.getUniqueID();
        if ((userManager.isWhitelisted(uuid) || uuid.getLeastSignificantBits() == 0L) && !abstractClientPlayer.isInvisible()) {
            return LabyMod.getInstance().getEmoteRegistry().getPlayingEmotes().get(uuid);
        }
        return null;
    }
    
    public void playEmote(final short emoteId) {
        if (LabyModCore.getMinecraft().getPlayer() == null || !LabyMod.getSettings().emotes) {
            Debug.log(Debug.EnumDebugMode.EMOTE, "Playing emote canceled because emotes are disabled or player is null.");
            return;
        }
        if (!LabyMod.getInstance().getLabyConnect().isOnline()) {
            LabyMod.getInstance().displayMessageInChat(String.valueOf(ModColor.cl('c')) + LabyMod.getMessage("emote_error_not_connected", new Object[0]));
            Debug.log(Debug.EnumDebugMode.EMOTE, "Playing emote canceled because player is not connected to labymod chat!");
            return;
        }
        final UUID uuid = LabyModCore.getMinecraft().getPlayer().getUniqueID();
        final byte[] emoteIdBytes = this.shortToBytes(emoteId);
        Debug.log(Debug.EnumDebugMode.EMOTE, "Request labymod chat to play emote id " + emoteId);
        LabyMod.getInstance().getUserManager().requestAction((short)1, emoteIdBytes, new FutureCallback<PacketActionPlayResponse>() {
            @Override
            public void onSuccess(final PacketActionPlayResponse responsePacket) {
                if (responsePacket == null || !responsePacket.isAllowed()) {
                    Debug.log(Debug.EnumDebugMode.LABYMOD_CHAT, (responsePacket == null) ? "Response timed out" : ("Response packet: " + responsePacket.getReason()));
                    if (emoteId != -1) {
                        String message = null;
                        if (responsePacket == null) {
                            message = LabyMod.getMessage("emote_error_no_response", new Object[0]);
                        }
                        else if (responsePacket.getReason().startsWith("illegal emote use")) {
                            message = LabyMod.getMessage("emote_error_illegal_emote", new Object[0]);
                        }
                        else {
                            message = responsePacket.getReason();
                        }
                        LabyMod.getInstance().displayMessageInChat(String.valueOf(ModColor.cl('c')) + message);
                    }
                    return;
                }
                if (LabyModCore.getMinecraft().getPlayer() != null) {
                    LabyMod.getInstance().getUserManager().broadcastBitUpdate(emoteId != -1);
                }
                EmoteRegistry.this.handleEmote(uuid, emoteIdBytes);
            }
            
            @Override
            public void onFailure(final Throwable throwable) {
                throwable.printStackTrace();
                LabyMod.getInstance().displayMessageInChat(String.valueOf(ModColor.cl('c')) + "Error: " + throwable.getMessage());
            }
        });
    }
    
    public void abortEmote(final UUID uuid, final boolean forced) {
        this.playingEmotes.remove(uuid);
        final UUID clientUUID = LabyMod.getInstance().getPlayerUUID();
        if (uuid.equals(clientUUID)) {
            if (forced) {
                this.playEmote((short)(-1));
            }
            else {
                LabyMod.getInstance().getUserManager().broadcastBitUpdate(false);
            }
        }
    }
    
    public EmoteRenderer handleEmote(final UUID player, final byte[] data) {
        if (data.length < 2) {
            return null;
        }
        final short emoteId = this.bytesToShort(data);
        return this.handleEmote(player, emoteId);
    }
    
    public EmoteRenderer handleEmote(final UUID player, final short emoteId) {
        if (!LabyMod.getSettings().emotes) {
            return null;
        }
        if (emoteId == -1) {
            final EmoteRenderer emoteRenderer = this.playingEmotes.get(player);
            if (emoteRenderer != null) {
                emoteRenderer.abort();
            }
            return null;
        }
        if (!player.equals(LabyMod.getInstance().getPlayerUUID()) && this.playingEmotes.containsKey(player)) {
            return null;
        }
        Debug.log(Debug.EnumDebugMode.EMOTE, String.valueOf(player.toString()) + " started playing emote " + emoteId);
        if (!this.emoteSources.containsKey(emoteId)) {
            if (player.equals(LabyMod.getInstance().getPlayerUUID())) {
                LabyMod.getInstance().displayMessageInChat(String.valueOf(ModColor.cl('c')) + "Invalid emote id: " + emoteId + ". Maybe restart your game?");
            }
            Debug.log(Debug.EnumDebugMode.EMOTE, "Invalid emote id: " + emoteId);
            return null;
        }
        final KeyFrameStorage keyFrameStorage = this.emoteSources.get(emoteId);
        EmoteRenderer prevRenderer = this.playingEmotes.get(player);
        if (keyFrameStorage != null) {
            if (prevRenderer != null && !prevRenderer.isVisible()) {
                prevRenderer = null;
            }
            try {
                final EmoteRenderer emoteRenderer2 = new EmoteRenderer(player, emoteId, keyFrameStorage.getTimeout(), false, new StoredEmote(keyFrameStorage), prevRenderer);
                this.playingEmotes.put(player, emoteRenderer2);
                return emoteRenderer2;
            }
            catch (final Exception error) {
                error.printStackTrace();
                LabyMod.getInstance().displayMessageInChat(String.valueOf(ModColor.cl('c')) + "Error while playing emotes: " + error.getMessage());
                return null;
            }
        }
        if (prevRenderer != null) {
            prevRenderer.abort();
        }
        return null;
    }
    
    public void stream(final UUID player, final EmoteProvider emoteProvider) {
        final EmoteRenderer prevRenderer = this.playingEmotes.get(player);
        if (prevRenderer != null) {
            prevRenderer.abort();
        }
        if (emoteProvider != null) {
            final EmoteRenderer emoteRenderer = new EmoteRenderer(player, (short)(-3), 0L, true, emoteProvider, prevRenderer);
            this.playingEmotes.put(player, emoteRenderer);
        }
        else {
            this.playingEmotes.remove(player);
        }
    }
    
    public short bytesToShort(final byte[] bytes) {
        return ByteBuffer.wrap(bytes).order(ByteOrder.LITTLE_ENDIAN).getShort();
    }
    
    public byte[] shortToBytes(final short value) {
        final byte[] bytes = new byte[2];
        ByteBuffer.wrap(bytes).order(ByteOrder.LITTLE_ENDIAN).putShort(value);
        return bytes;
    }
    
    public Map<Short, KeyFrameStorage> getEmoteSources() {
        return this.emoteSources;
    }
    
    public Map<UUID, EmoteRenderer> getPlayingEmotes() {
        return this.playingEmotes;
    }
    
    public EmoteSelectorGui getEmoteSelectorGui() {
        return this.emoteSelectorGui;
    }
    
    public boolean isCleanPlayingMap() {
        return this.cleanPlayingMap;
    }
    
    public void setCleanPlayingMap(final boolean cleanPlayingMap) {
        this.cleanPlayingMap = cleanPlayingMap;
    }
}
