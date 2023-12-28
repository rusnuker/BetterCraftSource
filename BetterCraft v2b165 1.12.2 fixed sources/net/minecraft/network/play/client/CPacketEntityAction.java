// 
// Decompiled by Procyon v0.6.0
// 

package net.minecraft.network.play.client;

import net.minecraft.network.INetHandler;
import java.io.IOException;
import net.minecraft.network.PacketBuffer;
import net.minecraft.entity.Entity;
import net.minecraft.network.play.INetHandlerPlayServer;
import net.minecraft.network.Packet;

public class CPacketEntityAction implements Packet<INetHandlerPlayServer>
{
    public int entityID;
    private Action action;
    private int auxData;
    
    public CPacketEntityAction() {
    }
    
    public CPacketEntityAction(final Entity entityIn, final Action actionIn) {
        this(entityIn, actionIn, 0);
    }
    
    public CPacketEntityAction(final Entity entityIn, final Action actionIn, final int auxDataIn) {
        this.entityID = entityIn.getEntityId();
        this.action = actionIn;
        this.auxData = auxDataIn;
    }
    
    @Override
    public void readPacketData(final PacketBuffer buf) throws IOException {
        this.entityID = buf.readVarIntFromBuffer();
        this.action = buf.readEnumValue(Action.class);
        this.auxData = buf.readVarIntFromBuffer();
    }
    
    @Override
    public void writePacketData(final PacketBuffer buf) throws IOException {
        buf.writeVarIntToBuffer(this.entityID);
        buf.writeEnumValue(this.action);
        buf.writeVarIntToBuffer(this.auxData);
    }
    
    @Override
    public void processPacket(final INetHandlerPlayServer handler) {
        handler.processEntityAction(this);
    }
    
    public Action getAction() {
        return this.action;
    }
    
    public int getAuxData() {
        return this.auxData;
    }
    
    public enum Action
    {
        START_SNEAKING("START_SNEAKING", 0), 
        STOP_SNEAKING("STOP_SNEAKING", 1), 
        STOP_SLEEPING("STOP_SLEEPING", 2), 
        START_SPRINTING("START_SPRINTING", 3), 
        STOP_SPRINTING("STOP_SPRINTING", 4), 
        START_RIDING_JUMP("START_RIDING_JUMP", 5), 
        STOP_RIDING_JUMP("STOP_RIDING_JUMP", 6), 
        OPEN_INVENTORY("OPEN_INVENTORY", 7), 
        START_FALL_FLYING("START_FALL_FLYING", 8);
        
        private Action(final String s, final int n) {
        }
    }
}
