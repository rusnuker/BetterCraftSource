// 
// Decompiled by Procyon v0.6.0
// 

package net.labymod.user;

import net.labymod.labyconnect.packets.Packet;
import net.labymod.labyconnect.packets.PacketUserBadge;
import java.util.ArrayList;
import java.util.LinkedList;
import net.labymod.main.LabyMod;
import java.util.UUID;
import java.util.List;

public class FamiliarManager
{
    private static final long REQUEST_COOLDOWN = 5000L;
    private List<UUID> queuedUsers;
    private List<UUID> familiarUsers;
    private long lastTimeQueueHandled;
    private LabyMod labyMod;
    
    public FamiliarManager(final LabyMod labyMod) {
        this.queuedUsers = new LinkedList<UUID>();
        this.familiarUsers = new ArrayList<UUID>();
        this.lastTimeQueueHandled = 0L;
        this.labyMod = labyMod;
    }
    
    public void tick() {
        if (this.lastTimeQueueHandled + 5000L <= System.currentTimeMillis()) {
            this.requestQueuedUsers();
        }
    }
    
    public boolean queueUser(final UUID uuid) {
        return !this.familiarUsers.contains(uuid) && !this.queuedUsers.contains(uuid) && this.queuedUsers.add(uuid);
    }
    
    public void requestQueuedUsers() {
        if (this.queuedUsers.size() != 0) {
            this.lastTimeQueueHandled = System.currentTimeMillis();
            final List<UUID> list = this.queuedUsers;
            this.queuedUsers = new LinkedList<UUID>();
            final UUID[] auuid = new UUID[list.size()];
            list.toArray(auuid);
            this.labyMod.getLabyConnect().getClientConnection().sendPacket(new PacketUserBadge(auuid));
        }
    }
    
    public void newFamiliarUser(final UUID uuid) {
        if (!this.familiarUsers.contains(uuid)) {
            this.familiarUsers.add(uuid);
        }
    }
    
    public boolean lostFamiliarUser(final UUID uuid) {
        return this.familiarUsers.remove(uuid) && this.queuedUsers.remove(uuid);
    }
    
    public boolean isFamiliar(final UUID uuid) {
        return this.familiarUsers.contains(uuid);
    }
    
    public void refresh() {
    }
    
    public void clear() {
        this.familiarUsers.clear();
        this.queuedUsers.clear();
    }
}
