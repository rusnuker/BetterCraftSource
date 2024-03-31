// 
// Decompiled by Procyon v0.6.0
// 

package net.minecraft.network.play.client;

import net.minecraft.network.INetHandler;
import javax.annotation.Nullable;
import net.minecraft.world.World;
import java.io.IOException;
import net.minecraft.network.PacketBuffer;
import net.minecraft.entity.Entity;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.Vec3d;
import net.minecraft.network.play.INetHandlerPlayServer;
import net.minecraft.network.Packet;

public class CPacketUseEntity implements Packet<INetHandlerPlayServer>
{
    public int entityId;
    private Action action;
    private Vec3d hitVec;
    private EnumHand hand;
    
    public CPacketUseEntity() {
    }
    
    public CPacketUseEntity(final Entity entityIn) {
        this.entityId = entityIn.getEntityId();
        this.action = Action.ATTACK;
    }
    
    public CPacketUseEntity(final Entity entityIn, final EnumHand handIn) {
        this.entityId = entityIn.getEntityId();
        this.action = Action.INTERACT;
        this.hand = handIn;
    }
    
    public CPacketUseEntity(final Entity entityIn, final EnumHand handIn, final Vec3d hitVecIn) {
        this.entityId = entityIn.getEntityId();
        this.action = Action.INTERACT_AT;
        this.hand = handIn;
        this.hitVec = hitVecIn;
    }
    
    @Override
    public void readPacketData(final PacketBuffer buf) throws IOException {
        this.entityId = buf.readVarIntFromBuffer();
        this.action = buf.readEnumValue(Action.class);
        if (this.action == Action.INTERACT_AT) {
            this.hitVec = new Vec3d(buf.readFloat(), buf.readFloat(), buf.readFloat());
        }
        if (this.action == Action.INTERACT || this.action == Action.INTERACT_AT) {
            this.hand = buf.readEnumValue(EnumHand.class);
        }
    }
    
    @Override
    public void writePacketData(final PacketBuffer buf) throws IOException {
        buf.writeVarIntToBuffer(this.entityId);
        buf.writeEnumValue(this.action);
        if (this.action == Action.INTERACT_AT) {
            buf.writeFloat((float)this.hitVec.xCoord);
            buf.writeFloat((float)this.hitVec.yCoord);
            buf.writeFloat((float)this.hitVec.zCoord);
        }
        if (this.action == Action.INTERACT || this.action == Action.INTERACT_AT) {
            buf.writeEnumValue(this.hand);
        }
    }
    
    @Override
    public void processPacket(final INetHandlerPlayServer handler) {
        handler.processUseEntity(this);
    }
    
    @Nullable
    public Entity getEntityFromWorld(final World worldIn) {
        return worldIn.getEntityByID(this.entityId);
    }
    
    public Action getAction() {
        return this.action;
    }
    
    public EnumHand getHand() {
        return this.hand;
    }
    
    public Vec3d getHitVec() {
        return this.hitVec;
    }
    
    public enum Action
    {
        INTERACT("INTERACT", 0), 
        ATTACK("ATTACK", 1), 
        INTERACT_AT("INTERACT_AT", 2);
        
        private Action(final String s, final int n) {
        }
    }
}
