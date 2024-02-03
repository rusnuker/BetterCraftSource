// 
// Decompiled by Procyon v0.6.0
// 

package net.labymod.core_implementation.mc18.listener;

import java.util.Iterator;
import net.minecraft.network.play.server.S38PacketPlayerListItem;
import net.labymod.user.FamiliarManager;
import net.labymod.main.LabyMod;
import net.labymod.utils.Consumer;

public class ServerIncomingPacketListener implements Consumer<Object>
{
    private final LabyMod labymod;
    private final FamiliarManager familiarManager;
    
    public ServerIncomingPacketListener(final LabyMod labymod) {
        this.labymod = labymod;
        this.familiarManager = labymod.getUserManager().getFamiliarManager();
    }
    
    public void register() {
        this.labymod.getLabyModAPI().getEventManager().registerOnIncomingPacket(this);
    }
    
    @Override
    public void accept(final Object packetObject) {
        if (packetObject instanceof S38PacketPlayerListItem) {
            final S38PacketPlayerListItem packet = (S38PacketPlayerListItem)packetObject;
            if (LabyMod.getSettings().revealFamiliarUsers) {
                if (packet.getAction() == S38PacketPlayerListItem.Action.ADD_PLAYER) {
                    for (final S38PacketPlayerListItem.AddPlayerData data : packet.getEntries()) {
                        if (data != null && data.getProfile() != null) {
                            this.familiarManager.queueUser(data.getProfile().getId());
                        }
                    }
                }
                if (packet.getAction() == S38PacketPlayerListItem.Action.REMOVE_PLAYER) {
                    for (final S38PacketPlayerListItem.AddPlayerData data : packet.getEntries()) {
                        if (data != null && data.getProfile() != null) {
                            this.familiarManager.lostFamiliarUser(data.getProfile().getId());
                        }
                    }
                }
            }
        }
    }
}
