// 
// Decompiled by Procyon v0.6.0
// 

package com.viaversion.viabackwards.protocol.protocol1_12_2to1_13.packets;

import com.viaversion.viaversion.api.protocol.AbstractProtocol;
import com.viaversion.viaversion.api.protocol.Protocol;
import com.viaversion.viaversion.api.protocol.remapper.PacketHandler;
import com.viaversion.viaversion.api.protocol.packet.PacketWrapper;
import com.viaversion.viaversion.api.protocol.remapper.PacketHandlers;
import com.viaversion.viaversion.protocols.protocol1_12_1to1_12.ClientboundPackets1_12_1;
import com.viaversion.viabackwards.protocol.protocol1_12_2to1_13.data.NamedSoundMapping;
import com.viaversion.viaversion.api.type.Type;
import com.viaversion.viaversion.protocols.protocol1_13to1_12_2.ClientboundPackets1_13;
import com.viaversion.viabackwards.protocol.protocol1_12_2to1_13.Protocol1_12_2To1_13;
import com.viaversion.viaversion.api.rewriter.RewriterBase;

public class SoundPackets1_13 extends RewriterBase<Protocol1_12_2To1_13>
{
    private static final String[] SOUND_SOURCES;
    
    public SoundPackets1_13(final Protocol1_12_2To1_13 protocol) {
        super(protocol);
    }
    
    @Override
    protected void registerPackets() {
        ((AbstractProtocol<ClientboundPackets1_13, CM, SM, SU>)this.protocol).registerClientbound(ClientboundPackets1_13.NAMED_SOUND, wrapper -> {
            final String sound = wrapper.read(Type.STRING);
            String mappedSound = NamedSoundMapping.getOldId(sound);
            if (mappedSound != null || (mappedSound = ((Protocol1_12_2To1_13)this.protocol).getMappingData().getMappedNamedSound(sound)) != null) {
                wrapper.write(Type.STRING, mappedSound);
            }
            else {
                wrapper.write(Type.STRING, sound);
            }
            return;
        });
        this.protocol.registerClientbound(ClientboundPackets1_13.STOP_SOUND, ClientboundPackets1_12_1.PLUGIN_MESSAGE, wrapper -> {
            wrapper.write(Type.STRING, "MC|StopSound");
            final byte flags = wrapper.read((Type<Byte>)Type.BYTE);
            String source;
            if ((flags & 0x1) != 0x0) {
                source = SoundPackets1_13.SOUND_SOURCES[wrapper.read((Type<Integer>)Type.VAR_INT)];
            }
            else {
                source = "";
            }
            String sound2;
            if ((flags & 0x2) != 0x0) {
                final String newSound = wrapper.read(Type.STRING);
                sound2 = ((Protocol1_12_2To1_13)this.protocol).getMappingData().getMappedNamedSound(newSound);
                if (sound2 == null) {
                    sound2 = "";
                }
            }
            else {
                sound2 = "";
            }
            wrapper.write(Type.STRING, source);
            wrapper.write(Type.STRING, sound2);
            return;
        });
        ((AbstractProtocol<ClientboundPackets1_13, CM, SM, SU>)this.protocol).registerClientbound(ClientboundPackets1_13.SOUND, new PacketHandlers() {
            public void register() {
                this.map((Type<Object>)Type.VAR_INT);
                this.handler(wrapper -> {
                    final int newSound = wrapper.get((Type<Integer>)Type.VAR_INT, 0);
                    final int oldSound = ((Protocol1_12_2To1_13)SoundPackets1_13.this.protocol).getMappingData().getSoundMappings().getNewId(newSound);
                    if (oldSound == -1) {
                        wrapper.cancel();
                    }
                    else {
                        wrapper.set(Type.VAR_INT, 0, oldSound);
                    }
                });
            }
        });
    }
    
    static {
        SOUND_SOURCES = new String[] { "master", "music", "record", "weather", "block", "hostile", "neutral", "player", "ambient", "voice" };
    }
}
