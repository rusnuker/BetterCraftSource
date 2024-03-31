// 
// Decompiled by Procyon v0.6.0
// 

package com.viaversion.viaversion.protocols.protocol1_14to1_13_2.storage;

import java.util.concurrent.ConcurrentHashMap;
import com.viaversion.viaversion.api.minecraft.entities.EntityType;
import com.viaversion.viaversion.api.minecraft.entities.Entity1_14Types;
import com.viaversion.viaversion.api.connection.UserConnection;
import java.util.Map;
import com.viaversion.viaversion.data.entity.EntityTrackerBase;

public class EntityTracker1_14 extends EntityTrackerBase
{
    private final Map<Integer, Byte> insentientData;
    private final Map<Integer, Byte> sleepingAndRiptideData;
    private final Map<Integer, Byte> playerEntityFlags;
    private int latestTradeWindowId;
    private boolean forceSendCenterChunk;
    private int chunkCenterX;
    private int chunkCenterZ;
    
    public EntityTracker1_14(final UserConnection user) {
        super(user, Entity1_14Types.PLAYER);
        this.insentientData = new ConcurrentHashMap<Integer, Byte>();
        this.sleepingAndRiptideData = new ConcurrentHashMap<Integer, Byte>();
        this.playerEntityFlags = new ConcurrentHashMap<Integer, Byte>();
        this.forceSendCenterChunk = true;
    }
    
    @Override
    public void removeEntity(final int entityId) {
        super.removeEntity(entityId);
        this.insentientData.remove(entityId);
        this.sleepingAndRiptideData.remove(entityId);
        this.playerEntityFlags.remove(entityId);
    }
    
    public byte getInsentientData(final int entity) {
        final Byte val = this.insentientData.get(entity);
        return (byte)((val == null) ? 0 : ((byte)val));
    }
    
    public void setInsentientData(final int entity, final byte value) {
        this.insentientData.put(entity, value);
    }
    
    private static byte zeroIfNull(final Byte val) {
        if (val == null) {
            return 0;
        }
        return val;
    }
    
    public boolean isSleeping(final int player) {
        return (zeroIfNull(this.sleepingAndRiptideData.get(player)) & 0x1) != 0x0;
    }
    
    public void setSleeping(final int player, final boolean value) {
        final byte newValue = (byte)((zeroIfNull(this.sleepingAndRiptideData.get(player)) & 0xFFFFFFFE) | (value ? 1 : 0));
        if (newValue == 0) {
            this.sleepingAndRiptideData.remove(player);
        }
        else {
            this.sleepingAndRiptideData.put(player, newValue);
        }
    }
    
    public boolean isRiptide(final int player) {
        return (zeroIfNull(this.sleepingAndRiptideData.get(player)) & 0x2) != 0x0;
    }
    
    public void setRiptide(final int player, final boolean value) {
        final byte newValue = (byte)((zeroIfNull(this.sleepingAndRiptideData.get(player)) & 0xFFFFFFFD) | (value ? 2 : 0));
        if (newValue == 0) {
            this.sleepingAndRiptideData.remove(player);
        }
        else {
            this.sleepingAndRiptideData.put(player, newValue);
        }
    }
    
    public byte getEntityFlags(final int player) {
        return zeroIfNull(this.playerEntityFlags.get(player));
    }
    
    public void setEntityFlags(final int player, final byte data) {
        this.playerEntityFlags.put(player, data);
    }
    
    public int getLatestTradeWindowId() {
        return this.latestTradeWindowId;
    }
    
    public void setLatestTradeWindowId(final int latestTradeWindowId) {
        this.latestTradeWindowId = latestTradeWindowId;
    }
    
    public boolean isForceSendCenterChunk() {
        return this.forceSendCenterChunk;
    }
    
    public void setForceSendCenterChunk(final boolean forceSendCenterChunk) {
        this.forceSendCenterChunk = forceSendCenterChunk;
    }
    
    public int getChunkCenterX() {
        return this.chunkCenterX;
    }
    
    public void setChunkCenterX(final int chunkCenterX) {
        this.chunkCenterX = chunkCenterX;
    }
    
    public int getChunkCenterZ() {
        return this.chunkCenterZ;
    }
    
    public void setChunkCenterZ(final int chunkCenterZ) {
        this.chunkCenterZ = chunkCenterZ;
    }
}
