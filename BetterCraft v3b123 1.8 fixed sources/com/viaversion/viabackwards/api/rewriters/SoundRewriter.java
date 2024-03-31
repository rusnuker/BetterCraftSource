// 
// Decompiled by Procyon v0.6.0
// 

package com.viaversion.viabackwards.api.rewriters;

import com.viaversion.viaversion.api.protocol.packet.PacketWrapper;
import com.viaversion.viaversion.api.protocol.remapper.PacketHandler;
import com.viaversion.viaversion.api.type.Type;
import com.viaversion.viaversion.api.protocol.remapper.PacketHandlers;
import com.viaversion.viaversion.api.protocol.Protocol;
import com.viaversion.viabackwards.api.BackwardsProtocol;
import com.viaversion.viaversion.api.protocol.packet.ClientboundPacketType;

public class SoundRewriter<C extends ClientboundPacketType> extends com.viaversion.viaversion.rewriter.SoundRewriter<C>
{
    private final BackwardsProtocol<C, ?, ?, ?> protocol;
    
    public SoundRewriter(final BackwardsProtocol<C, ?, ?, ?> protocol) {
        super(protocol);
        this.protocol = protocol;
    }
    
    public void registerNamedSound(final C packetType) {
        this.protocol.registerClientbound(packetType, new PacketHandlers() {
            public void register() {
                this.map(Type.STRING);
                this.handler(SoundRewriter.this.getNamedSoundHandler());
            }
        });
    }
    
    public void registerStopSound(final C packetType) {
        this.protocol.registerClientbound(packetType, new PacketHandlers() {
            public void register() {
                this.handler(SoundRewriter.this.getStopSoundHandler());
            }
        });
    }
    
    public PacketHandler getNamedSoundHandler() {
        return wrapper -> {
            final String soundId = wrapper.get(Type.STRING, 0);
            final String mappedId = this.protocol.getMappingData().getMappedNamedSound(soundId);
            if (mappedId != null) {
                if (!mappedId.isEmpty()) {
                    wrapper.set(Type.STRING, 0, mappedId);
                }
                else {
                    wrapper.cancel();
                }
            }
        };
    }
    
    public PacketHandler getStopSoundHandler() {
        return wrapper -> {
            final byte flags = wrapper.passthrough((Type<Byte>)Type.BYTE);
            if ((flags & 0x2) != 0x0) {
                if ((flags & 0x1) != 0x0) {
                    wrapper.passthrough((Type<Object>)Type.VAR_INT);
                }
                final String soundId = wrapper.read(Type.STRING);
                final String mappedId = this.protocol.getMappingData().getMappedNamedSound(soundId);
                if (mappedId == null) {
                    wrapper.write(Type.STRING, soundId);
                }
                else if (!mappedId.isEmpty()) {
                    wrapper.write(Type.STRING, mappedId);
                }
                else {
                    wrapper.cancel();
                }
            }
        };
    }
    
    @Override
    public void register1_19_3Sound(final C packetType) {
        this.protocol.registerClientbound(packetType, this.get1_19_3SoundHandler());
    }
    
    public PacketHandler get1_19_3SoundHandler() {
        return wrapper -> {
            final int soundId = wrapper.read((Type<Integer>)Type.VAR_INT);
            if (soundId != 0) {
                final int mappedId = this.idRewriter.rewrite(soundId - 1);
                if (mappedId == -1) {
                    wrapper.cancel();
                }
                else {
                    wrapper.write(Type.VAR_INT, mappedId + 1);
                }
            }
            else {
                wrapper.write(Type.VAR_INT, 0);
                String soundIdentifier = wrapper.read(Type.STRING);
                final String mappedIdentifier = this.protocol.getMappingData().getMappedNamedSound(soundIdentifier);
                if (mappedIdentifier != null) {
                    if (mappedIdentifier.isEmpty()) {
                        wrapper.cancel();
                        return;
                    }
                    else {
                        soundIdentifier = mappedIdentifier;
                    }
                }
                wrapper.write(Type.STRING, soundIdentifier);
            }
        };
    }
}
