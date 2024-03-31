// 
// Decompiled by Procyon v0.6.0
// 

package com.viaversion.viabackwards.api.rewriters;

import com.viaversion.viaversion.api.protocol.remapper.PacketHandler;
import com.viaversion.viaversion.api.protocol.packet.PacketWrapper;
import com.viaversion.viaversion.api.protocol.remapper.PacketHandlers;
import com.viaversion.viaversion.api.type.Type;
import com.viaversion.viaversion.api.protocol.packet.State;
import com.viaversion.viabackwards.ViaBackwards;
import com.viaversion.viaversion.api.protocol.Protocol;
import com.viaversion.viabackwards.api.BackwardsProtocol;
import java.util.Iterator;
import com.viaversion.viaversion.libs.gson.JsonObject;
import java.util.HashMap;
import com.viaversion.viaversion.libs.gson.JsonElement;
import com.viaversion.viabackwards.api.data.VBMappingDataLoader;
import java.util.Map;
import com.viaversion.viaversion.rewriter.ComponentRewriter;
import com.viaversion.viaversion.api.protocol.packet.ClientboundPacketType;

public class TranslatableRewriter<C extends ClientboundPacketType> extends ComponentRewriter<C>
{
    private static final Map<String, Map<String, String>> TRANSLATABLES;
    private final Map<String, String> newTranslatables;
    
    public static void loadTranslatables() {
        final JsonObject jsonObject = VBMappingDataLoader.loadFromDataDir("translation-mappings.json");
        for (final Map.Entry<String, JsonElement> entry : jsonObject.entrySet()) {
            final Map<String, String> versionMappings = new HashMap<String, String>();
            TranslatableRewriter.TRANSLATABLES.put(entry.getKey(), versionMappings);
            for (final Map.Entry<String, JsonElement> translationEntry : entry.getValue().getAsJsonObject().entrySet()) {
                versionMappings.put(translationEntry.getKey(), translationEntry.getValue().getAsString());
            }
        }
    }
    
    public TranslatableRewriter(final BackwardsProtocol<C, ?, ?, ?> protocol) {
        this(protocol, protocol.getClass().getSimpleName().split("To")[1].replace("_", "."));
    }
    
    public TranslatableRewriter(final BackwardsProtocol<C, ?, ?, ?> protocol, final String sectionIdentifier) {
        super(protocol);
        final Map<String, String> newTranslatables = TranslatableRewriter.TRANSLATABLES.get(sectionIdentifier);
        if (newTranslatables == null) {
            ViaBackwards.getPlatform().getLogger().warning("Error loading " + sectionIdentifier + " translatables!");
            this.newTranslatables = new HashMap<String, String>();
        }
        else {
            this.newTranslatables = newTranslatables;
        }
    }
    
    public void registerPing() {
        this.protocol.registerClientbound(State.LOGIN, 0, 0, wrapper -> this.processText(wrapper.passthrough(Type.COMPONENT)));
    }
    
    public void registerDisconnect(final C packetType) {
        this.protocol.registerClientbound(packetType, wrapper -> this.processText(wrapper.passthrough(Type.COMPONENT)));
    }
    
    public void registerLegacyOpenWindow(final C packetType) {
        this.protocol.registerClientbound(packetType, new PacketHandlers() {
            public void register() {
                this.map((Type<Object>)Type.UNSIGNED_BYTE);
                this.map(Type.STRING);
                this.handler(wrapper -> TranslatableRewriter.this.processText(wrapper.passthrough(Type.COMPONENT)));
            }
        });
    }
    
    public void registerOpenWindow(final C packetType) {
        this.protocol.registerClientbound(packetType, new PacketHandlers() {
            public void register() {
                this.map((Type<Object>)Type.VAR_INT);
                this.map((Type<Object>)Type.VAR_INT);
                this.handler(wrapper -> TranslatableRewriter.this.processText(wrapper.passthrough(Type.COMPONENT)));
            }
        });
    }
    
    public void registerTabList(final C packetType) {
        this.protocol.registerClientbound(packetType, wrapper -> {
            this.processText(wrapper.passthrough(Type.COMPONENT));
            this.processText(wrapper.passthrough(Type.COMPONENT));
        });
    }
    
    public void registerCombatKill(final C packetType) {
        this.protocol.registerClientbound(packetType, new PacketHandlers() {
            public void register() {
                this.map((Type<Object>)Type.VAR_INT);
                this.map((Type<Object>)Type.INT);
                this.handler(wrapper -> TranslatableRewriter.this.processText(wrapper.passthrough(Type.COMPONENT)));
            }
        });
    }
    
    public void registerCombatKill1_20(final C packetType) {
        this.protocol.registerClientbound(packetType, new PacketHandlers() {
            public void register() {
                this.map((Type<Object>)Type.VAR_INT);
                this.handler(wrapper -> TranslatableRewriter.this.processText(wrapper.passthrough(Type.COMPONENT)));
            }
        });
    }
    
    @Override
    protected void handleTranslate(final JsonObject root, final String translate) {
        final String newTranslate = this.mappedTranslationKey(translate);
        if (newTranslate != null) {
            root.addProperty("translate", newTranslate);
        }
    }
    
    public String mappedTranslationKey(final String translationKey) {
        return this.newTranslatables.get(translationKey);
    }
    
    static {
        TRANSLATABLES = new HashMap<String, Map<String, String>>();
    }
}
