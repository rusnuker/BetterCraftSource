// 
// Decompiled by Procyon v0.6.0
// 

package net.minecraft.network.play.server;

import net.minecraft.network.INetHandler;
import java.io.IOException;
import net.minecraft.network.PacketBuffer;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.CombatTracker;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.network.play.INetHandlerPlayClient;
import net.minecraft.network.Packet;

public class SPacketCombatEvent implements Packet<INetHandlerPlayClient>
{
    public Event eventType;
    public int playerId;
    public int entityId;
    public int duration;
    public ITextComponent deathMessage;
    
    public SPacketCombatEvent() {
    }
    
    public SPacketCombatEvent(final CombatTracker tracker, final Event eventIn) {
        this(tracker, eventIn, true);
    }
    
    public SPacketCombatEvent(final CombatTracker tracker, final Event eventIn, final boolean p_i46932_3_) {
        this.eventType = eventIn;
        final EntityLivingBase entitylivingbase = tracker.getBestAttacker();
        switch (eventIn) {
            case END_COMBAT: {
                this.duration = tracker.getCombatDuration();
                this.entityId = ((entitylivingbase == null) ? -1 : entitylivingbase.getEntityId());
                break;
            }
            case ENTITY_DIED: {
                this.playerId = tracker.getFighter().getEntityId();
                this.entityId = ((entitylivingbase == null) ? -1 : entitylivingbase.getEntityId());
                if (p_i46932_3_) {
                    this.deathMessage = tracker.getDeathMessage();
                    break;
                }
                this.deathMessage = new TextComponentString("");
                break;
            }
        }
    }
    
    @Override
    public void readPacketData(final PacketBuffer buf) throws IOException {
        this.eventType = buf.readEnumValue(Event.class);
        if (this.eventType == Event.END_COMBAT) {
            this.duration = buf.readVarIntFromBuffer();
            this.entityId = buf.readInt();
        }
        else if (this.eventType == Event.ENTITY_DIED) {
            this.playerId = buf.readVarIntFromBuffer();
            this.entityId = buf.readInt();
            this.deathMessage = buf.readTextComponent();
        }
    }
    
    @Override
    public void writePacketData(final PacketBuffer buf) throws IOException {
        buf.writeEnumValue(this.eventType);
        if (this.eventType == Event.END_COMBAT) {
            buf.writeVarIntToBuffer(this.duration);
            buf.writeInt(this.entityId);
        }
        else if (this.eventType == Event.ENTITY_DIED) {
            buf.writeVarIntToBuffer(this.playerId);
            buf.writeInt(this.entityId);
            buf.writeTextComponent(this.deathMessage);
        }
    }
    
    @Override
    public void processPacket(final INetHandlerPlayClient handler) {
        handler.handleCombatEvent(this);
    }
    
    public enum Event
    {
        ENTER_COMBAT("ENTER_COMBAT", 0), 
        END_COMBAT("END_COMBAT", 1), 
        ENTITY_DIED("ENTITY_DIED", 2);
        
        private Event(final String s, final int n) {
        }
    }
}
