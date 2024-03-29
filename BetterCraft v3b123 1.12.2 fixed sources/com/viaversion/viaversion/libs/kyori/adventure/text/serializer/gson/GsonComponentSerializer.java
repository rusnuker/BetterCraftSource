// 
// Decompiled by Procyon v0.6.0
// 

package com.viaversion.viaversion.libs.kyori.adventure.text.serializer.gson;

import java.util.function.Consumer;
import org.jetbrains.annotations.ApiStatus;
import com.viaversion.viaversion.libs.kyori.adventure.util.PlatformAPI;
import org.jetbrains.annotations.Nullable;
import com.viaversion.viaversion.libs.kyori.adventure.builder.AbstractBuilder;
import com.viaversion.viaversion.libs.gson.JsonElement;
import com.viaversion.viaversion.libs.gson.GsonBuilder;
import java.util.function.UnaryOperator;
import com.viaversion.viaversion.libs.gson.Gson;
import org.jetbrains.annotations.NotNull;
import com.viaversion.viaversion.libs.kyori.adventure.util.Buildable;
import com.viaversion.viaversion.libs.kyori.adventure.text.Component;
import com.viaversion.viaversion.libs.kyori.adventure.text.serializer.ComponentSerializer;

public interface GsonComponentSerializer extends ComponentSerializer<Component, Component, String>, Buildable<GsonComponentSerializer, Builder>
{
    @NotNull
    default GsonComponentSerializer gson() {
        return GsonComponentSerializerImpl.Instances.INSTANCE;
    }
    
    @NotNull
    default GsonComponentSerializer colorDownsamplingGson() {
        return GsonComponentSerializerImpl.Instances.LEGACY_INSTANCE;
    }
    
    default Builder builder() {
        return new GsonComponentSerializerImpl.BuilderImpl();
    }
    
    @NotNull
    Gson serializer();
    
    @NotNull
    UnaryOperator<GsonBuilder> populator();
    
    @NotNull
    Component deserializeFromTree(@NotNull final JsonElement input);
    
    @NotNull
    JsonElement serializeToTree(@NotNull final Component component);
    
    public interface Builder extends AbstractBuilder<GsonComponentSerializer>, Buildable.Builder<GsonComponentSerializer>
    {
        @NotNull
        Builder downsampleColors();
        
        @NotNull
        Builder legacyHoverEventSerializer(@Nullable final LegacyHoverEventSerializer serializer);
        
        @NotNull
        Builder emitLegacyHoverEvent();
        
        @NotNull
        GsonComponentSerializer build();
    }
    
    @PlatformAPI
    @ApiStatus.Internal
    public interface Provider
    {
        @PlatformAPI
        @ApiStatus.Internal
        @NotNull
        GsonComponentSerializer gson();
        
        @PlatformAPI
        @ApiStatus.Internal
        @NotNull
        GsonComponentSerializer gsonLegacy();
        
        @PlatformAPI
        @ApiStatus.Internal
        @NotNull
        Consumer<Builder> builder();
    }
}
