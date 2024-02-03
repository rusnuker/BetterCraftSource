// 
// Decompiled by Procyon v0.6.0
// 

package com.viaversion.viaversion.libs.kyori.adventure.text.format;

import java.util.EnumMap;
import com.viaversion.viaversion.libs.kyori.adventure.util.Buildable;
import com.viaversion.viaversion.libs.kyori.examination.Examinable;
import com.viaversion.viaversion.libs.kyori.adventure.internal.Internals;
import com.viaversion.viaversion.libs.kyori.examination.ExaminableProperty;
import java.util.stream.Stream;
import java.util.Set;
import com.viaversion.viaversion.libs.kyori.adventure.text.event.HoverEventSource;
import java.util.Objects;
import java.util.Map;
import com.viaversion.viaversion.libs.kyori.adventure.text.event.HoverEvent;
import com.viaversion.viaversion.libs.kyori.adventure.text.event.ClickEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import com.viaversion.viaversion.libs.kyori.adventure.key.Key;

final class StyleImpl implements Style
{
    static final StyleImpl EMPTY;
    @Nullable
    final Key font;
    @Nullable
    final TextColor color;
    @NotNull
    final DecorationMap decorations;
    @Nullable
    final ClickEvent clickEvent;
    @Nullable
    final HoverEvent<?> hoverEvent;
    @Nullable
    final String insertion;
    
    StyleImpl(@Nullable final Key font, @Nullable final TextColor color, @NotNull final Map<TextDecoration, TextDecoration.State> decorations, @Nullable final ClickEvent clickEvent, @Nullable final HoverEvent<?> hoverEvent, @Nullable final String insertion) {
        this.font = font;
        this.color = color;
        this.decorations = DecorationMap.fromMap(decorations);
        this.clickEvent = clickEvent;
        this.hoverEvent = hoverEvent;
        this.insertion = insertion;
    }
    
    @Nullable
    @Override
    public Key font() {
        return this.font;
    }
    
    @NotNull
    @Override
    public Style font(@Nullable final Key font) {
        if (Objects.equals(this.font, font)) {
            return this;
        }
        return new StyleImpl(font, this.color, this.decorations, this.clickEvent, this.hoverEvent, this.insertion);
    }
    
    @Nullable
    @Override
    public TextColor color() {
        return this.color;
    }
    
    @NotNull
    @Override
    public Style color(@Nullable final TextColor color) {
        if (Objects.equals(this.color, color)) {
            return this;
        }
        return new StyleImpl(this.font, color, this.decorations, this.clickEvent, this.hoverEvent, this.insertion);
    }
    
    @NotNull
    @Override
    public Style colorIfAbsent(@Nullable final TextColor color) {
        if (this.color == null) {
            return this.color(color);
        }
        return this;
    }
    
    @Override
    public TextDecoration.State decoration(@NotNull final TextDecoration decoration) {
        final TextDecoration.State state = this.decorations.get(decoration);
        if (state != null) {
            return state;
        }
        throw new IllegalArgumentException(String.format("unknown decoration '%s'", decoration));
    }
    
    @NotNull
    @Override
    public Style decoration(@NotNull final TextDecoration decoration, final TextDecoration.State state) {
        Objects.requireNonNull(state, "state");
        if (this.decoration(decoration) == state) {
            return this;
        }
        return new StyleImpl(this.font, this.color, this.decorations.with(decoration, state), this.clickEvent, this.hoverEvent, this.insertion);
    }
    
    @NotNull
    @Override
    public Style decorationIfAbsent(@NotNull final TextDecoration decoration, final TextDecoration.State state) {
        Objects.requireNonNull(state, "state");
        final TextDecoration.State oldState = this.decorations.get(decoration);
        if (oldState == TextDecoration.State.NOT_SET) {
            return new StyleImpl(this.font, this.color, this.decorations.with(decoration, state), this.clickEvent, this.hoverEvent, this.insertion);
        }
        if (oldState != null) {
            return this;
        }
        throw new IllegalArgumentException(String.format("unknown decoration '%s'", decoration));
    }
    
    @NotNull
    @Override
    public Map<TextDecoration, TextDecoration.State> decorations() {
        return this.decorations;
    }
    
    @NotNull
    @Override
    public Style decorations(@NotNull final Map<TextDecoration, TextDecoration.State> decorations) {
        return new StyleImpl(this.font, this.color, DecorationMap.merge(decorations, this.decorations), this.clickEvent, this.hoverEvent, this.insertion);
    }
    
    @Nullable
    @Override
    public ClickEvent clickEvent() {
        return this.clickEvent;
    }
    
    @NotNull
    @Override
    public Style clickEvent(@Nullable final ClickEvent event) {
        return new StyleImpl(this.font, this.color, this.decorations, event, this.hoverEvent, this.insertion);
    }
    
    @Nullable
    @Override
    public HoverEvent<?> hoverEvent() {
        return this.hoverEvent;
    }
    
    @NotNull
    @Override
    public Style hoverEvent(@Nullable final HoverEventSource<?> source) {
        return new StyleImpl(this.font, this.color, this.decorations, this.clickEvent, HoverEventSource.unbox(source), this.insertion);
    }
    
    @Nullable
    @Override
    public String insertion() {
        return this.insertion;
    }
    
    @NotNull
    @Override
    public Style insertion(@Nullable final String insertion) {
        if (Objects.equals(this.insertion, insertion)) {
            return this;
        }
        return new StyleImpl(this.font, this.color, this.decorations, this.clickEvent, this.hoverEvent, insertion);
    }
    
    @NotNull
    @Override
    public Style merge(@NotNull final Style that, final Merge.Strategy strategy, @NotNull final Set<Merge> merges) {
        if (nothingToMerge(that, strategy, merges)) {
            return this;
        }
        if (this.isEmpty() && Merge.hasAll(merges)) {
            return that;
        }
        final Builder builder = this.toBuilder();
        builder.merge(that, strategy, merges);
        return builder.build();
    }
    
    @NotNull
    @Override
    public Style unmerge(@NotNull final Style that) {
        if (this.isEmpty()) {
            return this;
        }
        final Builder builder = new BuilderImpl(this);
        if (Objects.equals(this.font(), that.font())) {
            builder.font((Key)null);
        }
        if (Objects.equals(this.color(), that.color())) {
            builder.color((TextColor)null);
        }
        for (int i = 0, length = DecorationMap.DECORATIONS.length; i < length; ++i) {
            final TextDecoration decoration = DecorationMap.DECORATIONS[i];
            if (this.decoration(decoration) == that.decoration(decoration)) {
                builder.decoration(decoration, TextDecoration.State.NOT_SET);
            }
        }
        if (Objects.equals(this.clickEvent(), that.clickEvent())) {
            builder.clickEvent((ClickEvent)null);
        }
        if (Objects.equals(this.hoverEvent(), that.hoverEvent())) {
            builder.hoverEvent((HoverEventSource<?>)null);
        }
        if (Objects.equals(this.insertion(), that.insertion())) {
            builder.insertion((String)null);
        }
        return builder.build();
    }
    
    static boolean nothingToMerge(@NotNull final Style mergeFrom, final Merge.Strategy strategy, @NotNull final Set<Merge> merges) {
        return strategy == Merge.Strategy.NEVER || mergeFrom.isEmpty() || merges.isEmpty();
    }
    
    @Override
    public boolean isEmpty() {
        return this == StyleImpl.EMPTY;
    }
    
    @NotNull
    @Override
    public Builder toBuilder() {
        return new BuilderImpl(this);
    }
    
    @NotNull
    @Override
    public Stream<? extends ExaminableProperty> examinableProperties() {
        return Stream.concat(this.decorations.examinableProperties(), (Stream<? extends ExaminableProperty>)Stream.of((T[])new ExaminableProperty[] { ExaminableProperty.of("color", this.color), ExaminableProperty.of("clickEvent", this.clickEvent), ExaminableProperty.of("hoverEvent", this.hoverEvent), ExaminableProperty.of("insertion", this.insertion), ExaminableProperty.of("font", this.font) }));
    }
    
    @NotNull
    @Override
    public String toString() {
        return Internals.toString(this);
    }
    
    @Override
    public boolean equals(@Nullable final Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof StyleImpl)) {
            return false;
        }
        final StyleImpl that = (StyleImpl)other;
        return Objects.equals(this.color, that.color) && this.decorations.equals(that.decorations) && Objects.equals(this.clickEvent, that.clickEvent) && Objects.equals(this.hoverEvent, that.hoverEvent) && Objects.equals(this.insertion, that.insertion) && Objects.equals(this.font, that.font);
    }
    
    @Override
    public int hashCode() {
        int result = Objects.hashCode(this.color);
        result = 31 * result + this.decorations.hashCode();
        result = 31 * result + Objects.hashCode(this.clickEvent);
        result = 31 * result + Objects.hashCode(this.hoverEvent);
        result = 31 * result + Objects.hashCode(this.insertion);
        result = 31 * result + Objects.hashCode(this.font);
        return result;
    }
    
    static {
        EMPTY = new StyleImpl(null, null, DecorationMap.EMPTY, null, null, null);
    }
    
    static final class BuilderImpl implements Builder
    {
        @Nullable
        Key font;
        @Nullable
        TextColor color;
        final Map<TextDecoration, TextDecoration.State> decorations;
        @Nullable
        ClickEvent clickEvent;
        @Nullable
        HoverEvent<?> hoverEvent;
        @Nullable
        String insertion;
        
        BuilderImpl() {
            this.decorations = new EnumMap<TextDecoration, TextDecoration.State>(DecorationMap.EMPTY);
        }
        
        BuilderImpl(@NotNull final StyleImpl style) {
            this.color = style.color;
            this.decorations = new EnumMap<TextDecoration, TextDecoration.State>(style.decorations);
            this.clickEvent = style.clickEvent;
            this.hoverEvent = style.hoverEvent;
            this.insertion = style.insertion;
            this.font = style.font;
        }
        
        @NotNull
        @Override
        public Builder font(@Nullable final Key font) {
            this.font = font;
            return this;
        }
        
        @NotNull
        @Override
        public Builder color(@Nullable final TextColor color) {
            this.color = color;
            return this;
        }
        
        @NotNull
        @Override
        public Builder colorIfAbsent(@Nullable final TextColor color) {
            if (this.color == null) {
                this.color = color;
            }
            return this;
        }
        
        @NotNull
        @Override
        public Builder decoration(@NotNull final TextDecoration decoration, final TextDecoration.State state) {
            Objects.requireNonNull(state, "state");
            Objects.requireNonNull(decoration, "decoration");
            this.decorations.put(decoration, state);
            return this;
        }
        
        @NotNull
        @Override
        public Builder decorationIfAbsent(@NotNull final TextDecoration decoration, final TextDecoration.State state) {
            Objects.requireNonNull(state, "state");
            final TextDecoration.State oldState = this.decorations.get(decoration);
            if (oldState == TextDecoration.State.NOT_SET) {
                this.decorations.put(decoration, state);
            }
            if (oldState != null) {
                return this;
            }
            throw new IllegalArgumentException(String.format("unknown decoration '%s'", decoration));
        }
        
        @NotNull
        @Override
        public Builder clickEvent(@Nullable final ClickEvent event) {
            this.clickEvent = event;
            return this;
        }
        
        @NotNull
        @Override
        public Builder hoverEvent(@Nullable final HoverEventSource<?> source) {
            this.hoverEvent = HoverEventSource.unbox(source);
            return this;
        }
        
        @NotNull
        @Override
        public Builder insertion(@Nullable final String insertion) {
            this.insertion = insertion;
            return this;
        }
        
        @NotNull
        @Override
        public Builder merge(@NotNull final Style that, final Merge.Strategy strategy, @NotNull final Set<Merge> merges) {
            Objects.requireNonNull(that, "style");
            Objects.requireNonNull(strategy, "strategy");
            Objects.requireNonNull(merges, "merges");
            if (StyleImpl.nothingToMerge(that, strategy, merges)) {
                return this;
            }
            if (merges.contains(Merge.COLOR)) {
                final TextColor color = that.color();
                if (color != null && (strategy == Merge.Strategy.ALWAYS || (strategy == Merge.Strategy.IF_ABSENT_ON_TARGET && this.color == null))) {
                    this.color(color);
                }
            }
            if (merges.contains(Merge.DECORATIONS)) {
                for (int i = 0, length = DecorationMap.DECORATIONS.length; i < length; ++i) {
                    final TextDecoration decoration = DecorationMap.DECORATIONS[i];
                    final TextDecoration.State state = that.decoration(decoration);
                    if (state != TextDecoration.State.NOT_SET) {
                        if (strategy == Merge.Strategy.ALWAYS) {
                            this.decoration(decoration, state);
                        }
                        else if (strategy == Merge.Strategy.IF_ABSENT_ON_TARGET) {
                            this.decorationIfAbsent(decoration, state);
                        }
                    }
                }
            }
            if (merges.contains(Merge.EVENTS)) {
                final ClickEvent clickEvent = that.clickEvent();
                if (clickEvent != null && (strategy == Merge.Strategy.ALWAYS || (strategy == Merge.Strategy.IF_ABSENT_ON_TARGET && this.clickEvent == null))) {
                    this.clickEvent(clickEvent);
                }
                final HoverEvent<?> hoverEvent = that.hoverEvent();
                if (hoverEvent != null && (strategy == Merge.Strategy.ALWAYS || (strategy == Merge.Strategy.IF_ABSENT_ON_TARGET && this.hoverEvent == null))) {
                    this.hoverEvent((HoverEventSource<?>)hoverEvent);
                }
            }
            if (merges.contains(Merge.INSERTION)) {
                final String insertion = that.insertion();
                if (insertion != null && (strategy == Merge.Strategy.ALWAYS || (strategy == Merge.Strategy.IF_ABSENT_ON_TARGET && this.insertion == null))) {
                    this.insertion(insertion);
                }
            }
            if (merges.contains(Merge.FONT)) {
                final Key font = that.font();
                if (font != null && (strategy == Merge.Strategy.ALWAYS || (strategy == Merge.Strategy.IF_ABSENT_ON_TARGET && this.font == null))) {
                    this.font(font);
                }
            }
            return this;
        }
        
        @NotNull
        @Override
        public StyleImpl build() {
            if (this.isEmpty()) {
                return StyleImpl.EMPTY;
            }
            return new StyleImpl(this.font, this.color, this.decorations, this.clickEvent, this.hoverEvent, this.insertion);
        }
        
        private boolean isEmpty() {
            return this.color == null && this.decorations.values().stream().allMatch(state -> state == TextDecoration.State.NOT_SET) && this.clickEvent == null && this.hoverEvent == null && this.insertion == null && this.font == null;
        }
    }
}
