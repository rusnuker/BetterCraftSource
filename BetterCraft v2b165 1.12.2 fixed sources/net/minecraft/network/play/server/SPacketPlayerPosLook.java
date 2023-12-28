// 
// Decompiled by Procyon v0.6.0
// 

package net.minecraft.network.play.server;

import java.util.Iterator;
import java.util.EnumSet;
import net.minecraft.network.INetHandler;
import java.io.IOException;
import net.minecraft.network.PacketBuffer;
import java.util.Set;
import net.minecraft.network.play.INetHandlerPlayClient;
import net.minecraft.network.Packet;

public class SPacketPlayerPosLook implements Packet<INetHandlerPlayClient>
{
    private double x;
    private double y;
    private double z;
    private float yaw;
    private float pitch;
    private Set<EnumFlags> flags;
    private int teleportId;
    
    public SPacketPlayerPosLook() {
    }
    
    public SPacketPlayerPosLook(final double xIn, final double yIn, final double zIn, final float yawIn, final float pitchIn, final Set<EnumFlags> flagsIn, final int teleportIdIn) {
        this.x = xIn;
        this.y = yIn;
        this.z = zIn;
        this.yaw = yawIn;
        this.pitch = pitchIn;
        this.flags = flagsIn;
        this.teleportId = teleportIdIn;
    }
    
    @Override
    public void readPacketData(final PacketBuffer buf) throws IOException {
        this.x = buf.readDouble();
        this.y = buf.readDouble();
        this.z = buf.readDouble();
        this.yaw = buf.readFloat();
        this.pitch = buf.readFloat();
        this.flags = EnumFlags.unpack(buf.readUnsignedByte());
        this.teleportId = buf.readVarIntFromBuffer();
    }
    
    @Override
    public void writePacketData(final PacketBuffer buf) throws IOException {
        buf.writeDouble(this.x);
        buf.writeDouble(this.y);
        buf.writeDouble(this.z);
        buf.writeFloat(this.yaw);
        buf.writeFloat(this.pitch);
        buf.writeByte(EnumFlags.pack(this.flags));
        buf.writeVarIntToBuffer(this.teleportId);
    }
    
    @Override
    public void processPacket(final INetHandlerPlayClient handler) {
        handler.handlePlayerPosLook(this);
    }
    
    public double getX() {
        return this.x;
    }
    
    public double getY() {
        return this.y;
    }
    
    public double getZ() {
        return this.z;
    }
    
    public float getYaw() {
        return this.yaw;
    }
    
    public float getPitch() {
        return this.pitch;
    }
    
    public int getTeleportId() {
        return this.teleportId;
    }
    
    public Set<EnumFlags> getFlags() {
        return this.flags;
    }
    
    public enum EnumFlags
    {
        X("X", 0, 0), 
        Y("Y", 1, 1), 
        Z("Z", 2, 2), 
        Y_ROT("Y_ROT", 3, 3), 
        X_ROT("X_ROT", 4, 4);
        
        private final int bit;
        
        private EnumFlags(final String s, final int n, final int p_i46690_3_) {
            this.bit = p_i46690_3_;
        }
        
        private int getMask() {
            return 1 << this.bit;
        }
        
        private boolean isSet(final int p_187043_1_) {
            return (p_187043_1_ & this.getMask()) == this.getMask();
        }
        
        public static Set<EnumFlags> unpack(final int flags) {
            final Set<EnumFlags> set = EnumSet.noneOf(EnumFlags.class);
            EnumFlags[] values;
            for (int length = (values = values()).length, i = 0; i < length; ++i) {
                final EnumFlags spacketplayerposlook$enumflags = values[i];
                if (spacketplayerposlook$enumflags.isSet(flags)) {
                    set.add(spacketplayerposlook$enumflags);
                }
            }
            return set;
        }
        
        public static int pack(final Set<EnumFlags> flags) {
            int i = 0;
            for (final EnumFlags spacketplayerposlook$enumflags : flags) {
                i |= spacketplayerposlook$enumflags.getMask();
            }
            return i;
        }
    }
}
