// 
// Decompiled by Procyon v0.6.0
// 

package com.viaversion.viaversion.libs.kyori.adventure.sound;

import com.viaversion.viaversion.libs.kyori.adventure.key.Keyed;
import com.viaversion.viaversion.libs.kyori.adventure.util.Index;
import java.util.OptionalLong;
import java.util.function.Supplier;
import java.util.Objects;
import com.viaversion.viaversion.libs.kyori.adventure.key.Key;
import com.viaversion.viaversion.libs.kyori.adventure.builder.AbstractBuilder;
import java.util.function.Consumer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.ApiStatus;
import com.viaversion.viaversion.libs.kyori.examination.Examinable;

@ApiStatus.NonExtendable
public interface Sound extends Examinable
{
    @NotNull
    default Builder sound() {
        return new SoundImpl.BuilderImpl();
    }
    
    @NotNull
    default Builder sound(@NotNull final Sound existing) {
        return new SoundImpl.BuilderImpl(existing);
    }
    
    @NotNull
    default Sound sound(@NotNull final Consumer<Builder> configurer) {
        return AbstractBuilder.configureAndBuild(sound(), configurer);
    }
    
    @NotNull
    default Sound sound(@NotNull final Key name, @NotNull final Source source, final float volume, final float pitch) {
        return sound().type(name).source(source).volume(volume).pitch(pitch).build();
    }
    
    @NotNull
    default Sound sound(@NotNull final Type type, @NotNull final Source source, final float volume, final float pitch) {
        Objects.requireNonNull(type, "type");
        return sound(type.key(), source, volume, pitch);
    }
    
    @NotNull
    default Sound sound(@NotNull final Supplier<? extends Type> type, @NotNull final Source source, final float volume, final float pitch) {
        return sound().type(type).source(source).volume(volume).pitch(pitch).build();
    }
    
    @NotNull
    default Sound sound(@NotNull final Key name, final Source.Provider source, final float volume, final float pitch) {
        return sound(name, source.soundSource(), volume, pitch);
    }
    
    @NotNull
    default Sound sound(@NotNull final Type type, final Source.Provider source, final float volume, final float pitch) {
        return sound(type, source.soundSource(), volume, pitch);
    }
    
    @NotNull
    default Sound sound(@NotNull final Supplier<? extends Type> type, final Source.Provider source, final float volume, final float pitch) {
        return sound(type, source.soundSource(), volume, pitch);
    }
    
    @NotNull
    Key name();
    
    @NotNull
    Source source();
    
    float volume();
    
    float pitch();
    
    @NotNull
    OptionalLong seed();
    
    @NotNull
    SoundStop asStop();
    
    public enum Source
    {
        MASTER("master"), 
        MUSIC("music"), 
        RECORD("record"), 
        WEATHER("weather"), 
        BLOCK("block"), 
        HOSTILE("hostile"), 
        NEUTRAL("neutral"), 
        PLAYER("player"), 
        AMBIENT("ambient"), 
        VOICE("voice");
        
        public static final Index<String, Source> NAMES;
        private final String name;
        
        private Source(final String name) {
            this.name = name;
        }
        
        static {
            NAMES = Index.create(Source.class, source -> source.name);
        }
        
        public interface Provider
        {
            @NotNull
            Source soundSource();
        }
    }
    
    public interface Emitter
    {
        @NotNull
        default Emitter self() {
            return SoundImpl.EMITTER_SELF;
        }
    }
    
    public interface Builder extends AbstractBuilder<Sound>
    {
        @NotNull
        Builder type(@NotNull final Key type);
        
        @NotNull
        Builder type(@NotNull final Type type);
        
        @NotNull
        Builder type(@NotNull final Supplier<? extends Type> typeSupplier);
        
        @NotNull
        Builder source(@NotNull final Source source);
        
        @NotNull
        Builder source(final Source.Provider source);
        
        @NotNull
        Builder volume(final float volume);
        
        @NotNull
        Builder pitch(final float pitch);
        
        @NotNull
        Builder seed(final long seed);
        
        @NotNull
        Builder seed(@NotNull final OptionalLong seed);
    }
    
    public interface Type extends Keyed
    {
        @NotNull
        Key key();
    }
}
