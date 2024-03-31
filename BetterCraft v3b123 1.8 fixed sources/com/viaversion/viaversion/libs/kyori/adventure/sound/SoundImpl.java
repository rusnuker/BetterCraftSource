// 
// Decompiled by Procyon v0.6.0
// 

package com.viaversion.viaversion.libs.kyori.adventure.sound;

import java.util.Objects;
import java.util.function.Supplier;
import com.viaversion.viaversion.libs.kyori.adventure.key.Key;
import com.viaversion.viaversion.libs.kyori.examination.Examinable;
import com.viaversion.viaversion.libs.kyori.adventure.internal.Internals;
import com.viaversion.viaversion.libs.kyori.examination.ExaminableProperty;
import java.util.stream.Stream;
import com.viaversion.viaversion.libs.kyori.adventure.util.ShadyPines;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.NotNull;
import java.util.OptionalLong;

abstract class SoundImpl implements Sound
{
    static final Emitter EMITTER_SELF;
    private final Source source;
    private final float volume;
    private final float pitch;
    private final OptionalLong seed;
    private SoundStop stop;
    
    SoundImpl(@NotNull final Source source, final float volume, final float pitch, final OptionalLong seed) {
        this.source = source;
        this.volume = volume;
        this.pitch = pitch;
        this.seed = seed;
    }
    
    @NotNull
    @Override
    public Source source() {
        return this.source;
    }
    
    @Override
    public float volume() {
        return this.volume;
    }
    
    @Override
    public float pitch() {
        return this.pitch;
    }
    
    @Override
    public OptionalLong seed() {
        return this.seed;
    }
    
    @NotNull
    @Override
    public SoundStop asStop() {
        if (this.stop == null) {
            this.stop = SoundStop.namedOnSource(this.name(), this.source());
        }
        return this.stop;
    }
    
    @Override
    public boolean equals(@Nullable final Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof SoundImpl)) {
            return false;
        }
        final SoundImpl that = (SoundImpl)other;
        return this.name().equals(that.name()) && this.source == that.source && ShadyPines.equals(this.volume, that.volume) && ShadyPines.equals(this.pitch, that.pitch) && this.seed.equals(that.seed);
    }
    
    @Override
    public int hashCode() {
        int result = this.name().hashCode();
        result = 31 * result + this.source.hashCode();
        result = 31 * result + Float.hashCode(this.volume);
        result = 31 * result + Float.hashCode(this.pitch);
        result = 31 * result + this.seed.hashCode();
        return result;
    }
    
    @NotNull
    @Override
    public Stream<? extends ExaminableProperty> examinableProperties() {
        return Stream.of((ExaminableProperty[])new ExaminableProperty[] { ExaminableProperty.of("name", this.name()), ExaminableProperty.of("source", this.source), ExaminableProperty.of("volume", this.volume), ExaminableProperty.of("pitch", this.pitch), ExaminableProperty.of("seed", this.seed) });
    }
    
    @Override
    public String toString() {
        return Internals.toString(this);
    }
    
    static {
        EMITTER_SELF = new Emitter() {
            @Override
            public String toString() {
                return "SelfSoundEmitter";
            }
        };
    }
    
    static final class BuilderImpl implements Builder
    {
        private static final float DEFAULT_VOLUME = 1.0f;
        private static final float DEFAULT_PITCH = 1.0f;
        private Key eagerType;
        private Supplier<? extends Type> lazyType;
        private Source source;
        private float volume;
        private float pitch;
        private OptionalLong seed;
        
        BuilderImpl() {
            this.source = Source.MASTER;
            this.volume = 1.0f;
            this.pitch = 1.0f;
            this.seed = OptionalLong.empty();
        }
        
        BuilderImpl(@NotNull final Sound existing) {
            this.source = Source.MASTER;
            this.volume = 1.0f;
            this.pitch = 1.0f;
            this.seed = OptionalLong.empty();
            if (existing instanceof Eager) {
                this.type(((Eager)existing).name);
            }
            else {
                if (!(existing instanceof Lazy)) {
                    throw new IllegalArgumentException("Unknown sound type " + existing + ", must be Eager or Lazy");
                }
                this.type(((Lazy)existing).supplier);
            }
            this.source(existing.source()).volume(existing.volume()).pitch(existing.pitch()).seed(existing.seed());
        }
        
        @NotNull
        @Override
        public Builder type(@NotNull final Key type) {
            this.eagerType = Objects.requireNonNull(type, "type");
            this.lazyType = null;
            return this;
        }
        
        @NotNull
        @Override
        public Builder type(@NotNull final Type type) {
            this.eagerType = Objects.requireNonNull(Objects.requireNonNull(type, "type").key(), "type.key()");
            this.lazyType = null;
            return this;
        }
        
        @NotNull
        @Override
        public Builder type(@NotNull final Supplier<? extends Type> typeSupplier) {
            this.lazyType = Objects.requireNonNull(typeSupplier, "typeSupplier");
            this.eagerType = null;
            return this;
        }
        
        @NotNull
        @Override
        public Builder source(@NotNull final Source source) {
            this.source = Objects.requireNonNull(source, "source");
            return this;
        }
        
        @NotNull
        @Override
        public Builder source(final Source.Provider source) {
            return this.source(source.soundSource());
        }
        
        @NotNull
        @Override
        public Builder volume(final float volume) {
            this.volume = volume;
            return this;
        }
        
        @NotNull
        @Override
        public Builder pitch(final float pitch) {
            this.pitch = pitch;
            return this;
        }
        
        @NotNull
        @Override
        public Builder seed(final long seed) {
            this.seed = OptionalLong.of(seed);
            return this;
        }
        
        @NotNull
        @Override
        public Builder seed(@NotNull final OptionalLong seed) {
            this.seed = Objects.requireNonNull(seed, "seed");
            return this;
        }
        
        @NotNull
        @Override
        public Sound build() {
            if (this.eagerType != null) {
                return new Eager(this.eagerType, this.source, this.volume, this.pitch, this.seed);
            }
            if (this.lazyType != null) {
                return new Lazy(this.lazyType, this.source, this.volume, this.pitch, this.seed);
            }
            throw new IllegalStateException("A sound type must be provided to build a sound");
        }
    }
    
    static final class Eager extends SoundImpl
    {
        final Key name;
        
        Eager(@NotNull final Key name, @NotNull final Source source, final float volume, final float pitch, final OptionalLong seed) {
            super(source, volume, pitch, seed);
            this.name = name;
        }
        
        @NotNull
        @Override
        public Key name() {
            return this.name;
        }
    }
    
    static final class Lazy extends SoundImpl
    {
        final Supplier<? extends Type> supplier;
        
        Lazy(@NotNull final Supplier<? extends Type> supplier, @NotNull final Source source, final float volume, final float pitch, final OptionalLong seed) {
            super(source, volume, pitch, seed);
            this.supplier = supplier;
        }
        
        @NotNull
        @Override
        public Key name() {
            return ((Type)this.supplier.get()).key();
        }
    }
}
