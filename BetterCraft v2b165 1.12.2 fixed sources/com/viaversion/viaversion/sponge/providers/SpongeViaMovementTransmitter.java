// 
// Decompiled by Procyon v0.6.0
// 

package com.viaversion.viaversion.sponge.providers;

import java.lang.reflect.Field;
import com.viaversion.viaversion.protocols.protocol1_9to1_8.providers.MovementTransmitterProvider;

public class SpongeViaMovementTransmitter extends MovementTransmitterProvider
{
    private Object idlePacket;
    private Object idlePacket2;
    
    public SpongeViaMovementTransmitter() {
        Class<?> idlePacketClass;
        try {
            idlePacketClass = Class.forName("net.minecraft.network.play.client.C03PacketPlayer");
        }
        catch (final ClassNotFoundException e) {
            return;
        }
        try {
            this.idlePacket = idlePacketClass.newInstance();
            this.idlePacket2 = idlePacketClass.newInstance();
            final Field flying = idlePacketClass.getDeclaredField("field_149474_g");
            flying.setAccessible(true);
            flying.set(this.idlePacket2, true);
        }
        catch (final NoSuchFieldException | InstantiationException | IllegalArgumentException | IllegalAccessException e2) {
            throw new RuntimeException("Couldn't make player idle packet, help!", e2);
        }
    }
    
    @Override
    public Object getFlyingPacket() {
        if (this.idlePacket == null) {
            throw new NullPointerException("Could not locate flying packet");
        }
        return this.idlePacket2;
    }
    
    @Override
    public Object getGroundPacket() {
        if (this.idlePacket == null) {
            throw new NullPointerException("Could not locate flying packet");
        }
        return this.idlePacket;
    }
}
