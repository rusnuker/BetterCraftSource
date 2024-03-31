// 
// Decompiled by Procyon v0.6.0
// 

package com.viaversion.viaversion.rewriter;

import com.viaversion.viaversion.api.protocol.packet.PacketWrapper;
import com.viaversion.viaversion.api.protocol.remapper.PacketHandler;
import com.viaversion.viaversion.api.type.Type;
import com.viaversion.viaversion.api.protocol.remapper.PacketHandlers;
import com.viaversion.viaversion.api.protocol.Protocol;
import com.viaversion.viaversion.api.protocol.packet.ClientboundPacketType;

public class SoundRewriter<C extends ClientboundPacketType>
{
    protected final Protocol<C, ?, ?, ?> protocol;
    protected final IdRewriteFunction idRewriter;
    
    public SoundRewriter(final Protocol<C, ?, ?, ?> protocol) {
        this.protocol = protocol;
        this.idRewriter = (id -> protocol.getMappingData().getSoundMappings().getNewId(id));
    }
    
    public SoundRewriter(final Protocol<C, ?, ?, ?> protocol, final IdRewriteFunction idRewriter) {
        this.protocol = protocol;
        this.idRewriter = idRewriter;
    }
    
    public void registerSound(final C packetType) {
        this.protocol.registerClientbound(packetType, new PacketHandlers() {
            public void register() {
                this.map((Type<Object>)Type.VAR_INT);
                this.handler(SoundRewriter.this.getSoundHandler());
            }
        });
    }
    
    public void register1_19_3Sound(final C packetType) {
        this.protocol.registerClientbound(packetType, wrapper -> {
            final int soundId = wrapper.read((Type<Integer>)Type.VAR_INT);
            if (soundId == 0) {
                wrapper.write(Type.VAR_INT, 0);
            }
            else {
                final int mappedId = this.idRewriter.rewrite(soundId - 1);
                if (mappedId == -1) {
                    wrapper.cancel();
                }
                else {
                    wrapper.write(Type.VAR_INT, mappedId + 1);
                }
            }
        });
    }
    
    public PacketHandler getSoundHandler() {
        return wrapper -> {
            final int soundId = wrapper.get((Type<Integer>)Type.VAR_INT, 0);
            final int mappedId = this.idRewriter.rewrite(soundId);
            if (mappedId == -1) {
                wrapper.cancel();
            }
            else if (soundId != mappedId) {
                wrapper.set(Type.VAR_INT, 0, mappedId);
            }
        };
    }
}
