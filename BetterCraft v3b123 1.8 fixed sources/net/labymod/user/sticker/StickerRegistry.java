// 
// Decompiled by Procyon v0.6.0
// 

package net.labymod.user.sticker;

import java.nio.ByteOrder;
import java.nio.ByteBuffer;
import net.labymod.user.sticker.data.StickerPack;
import net.labymod.user.sticker.data.Sticker;
import net.labymod.labyconnect.packets.PacketActionPlayResponse;
import com.google.common.util.concurrent.FutureCallback;
import net.labymod.utils.ModColor;
import net.labymod.support.util.Debug;
import net.labymod.main.LabyMod;
import net.labymod.core.LabyModCore;
import net.labymod.user.User;
import net.labymod.user.sticker.data.StickerData;
import net.labymod.user.gui.StickerSelectorGui;

public class StickerRegistry
{
    public static final long STICKER_DURATION = 4000L;
    private StickerSelectorGui stickerSelectorGui;
    protected StickerData stickerData;
    
    public StickerRegistry() {
        this.stickerSelectorGui = new StickerSelectorGui();
    }
    
    public void init() {
        new StickerLoader(this).start();
    }
    
    public void playSticker(final User user, final short stickerId) {
        if (LabyModCore.getMinecraft().getPlayer() == null || !LabyMod.getSettings().stickers) {
            Debug.log(Debug.EnumDebugMode.STICKER, "Playing sticker canceled because sticker are disabled or player is null.");
            return;
        }
        if (!LabyMod.getInstance().getLabyConnect().isOnline()) {
            LabyMod.getInstance().displayMessageInChat(String.valueOf(ModColor.cl('c')) + LabyMod.getMessage("sticker_error_not_connected", new Object[0]));
            Debug.log(Debug.EnumDebugMode.STICKER, "Playing sticker canceled because player is not connected to labymod chat!");
            return;
        }
        final byte[] stickerIdBytes = this.shortToBytes(stickerId);
        Debug.log(Debug.EnumDebugMode.STICKER, "Request labymod chat to play sticker id " + stickerId);
        LabyMod.getInstance().getUserManager().requestAction((short)3, stickerIdBytes, new FutureCallback<PacketActionPlayResponse>() {
            @Override
            public void onSuccess(final PacketActionPlayResponse responsePacket) {
                if (responsePacket == null || !responsePacket.isAllowed()) {
                    Debug.log(Debug.EnumDebugMode.LABYMOD_CHAT, (responsePacket == null) ? "Response timed out" : ("Response packet: " + responsePacket.getReason()));
                    if (stickerId != -1) {
                        String message = null;
                        if (responsePacket == null) {
                            message = LabyMod.getMessage("sticker_no_response", new Object[0]);
                        }
                        else if (responsePacket.getReason().startsWith("illegal sticker use")) {
                            message = LabyMod.getMessage("sticker_error_illegal_sticker", new Object[0]);
                        }
                        else {
                            message = responsePacket.getReason();
                        }
                        LabyMod.getInstance().displayMessageInChat(String.valueOf(ModColor.cl('c')) + message);
                    }
                    return;
                }
                if (LabyModCore.getMinecraft().getPlayer() != null) {
                    LabyMod.getInstance().getUserManager().broadcastBitUpdate(stickerId != -1);
                }
                StickerRegistry.this.handleSticker(user, stickerId);
            }
            
            @Override
            public void onFailure(final Throwable throwable) {
                throwable.printStackTrace();
                LabyMod.getInstance().displayMessageInChat(String.valueOf(ModColor.cl('c')) + "Error: " + throwable.getMessage());
            }
        });
    }
    
    public Sticker getSticker(final short id) {
        StickerPack[] packs;
        for (int length = (packs = this.stickerData.getPacks()).length, i = 0; i < length; ++i) {
            final StickerPack stickerPack = packs[i];
            Sticker[] stickers;
            for (int length2 = (stickers = stickerPack.getStickers()).length, j = 0; j < length2; ++j) {
                final Sticker sticker = stickers[j];
                if (sticker.getId() == id) {
                    return sticker;
                }
            }
        }
        return null;
    }
    
    public void handleSticker(final User user, final short id) {
        user.setPlayingSticker(id);
        if (id != -1) {
            user.setStickerStartedPlaying(System.currentTimeMillis());
        }
    }
    
    public void stopSticker(final User user) {
        this.handleSticker(user, (short)(-1));
    }
    
    public short bytesToShort(final byte[] bytes) {
        return ByteBuffer.wrap(bytes).order(ByteOrder.LITTLE_ENDIAN).getShort();
    }
    
    public byte[] shortToBytes(final short value) {
        final byte[] bytes = new byte[2];
        ByteBuffer.wrap(bytes).order(ByteOrder.LITTLE_ENDIAN).putShort(value);
        return bytes;
    }
    
    public StickerSelectorGui getStickerSelectorGui() {
        return this.stickerSelectorGui;
    }
    
    public StickerData getStickerData() {
        return this.stickerData;
    }
}
