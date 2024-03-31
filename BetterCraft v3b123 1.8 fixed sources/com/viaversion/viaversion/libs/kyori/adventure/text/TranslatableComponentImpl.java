// 
// Decompiled by Procyon v0.6.0
// 

package com.viaversion.viaversion.libs.kyori.adventure.text;

import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.function.Function;
import java.util.stream.Stream;
import java.util.Collections;
import com.viaversion.viaversion.libs.kyori.adventure.util.Buildable;
import com.viaversion.viaversion.libs.kyori.examination.Examinable;
import com.viaversion.viaversion.libs.kyori.adventure.internal.Internals;
import org.jetbrains.annotations.Nullable;
import java.util.function.Predicate;
import java.util.Arrays;
import java.util.Objects;
import com.viaversion.viaversion.libs.kyori.adventure.text.format.Style;
import org.jetbrains.annotations.NotNull;
import java.util.List;

final class TranslatableComponentImpl extends AbstractComponent implements TranslatableComponent
{
    private final String key;
    private final List<Component> args;
    
    static TranslatableComponent create(@NotNull final List<Component> children, @NotNull final Style style, @NotNull final String key, @NotNull final ComponentLike[] args) {
        Objects.requireNonNull(args, "args");
        return create(children, style, key, Arrays.asList(args));
    }
    
    static TranslatableComponent create(@NotNull final List<? extends ComponentLike> children, @NotNull final Style style, @NotNull final String key, @NotNull final List<? extends ComponentLike> args) {
        return new TranslatableComponentImpl(ComponentLike.asComponents(children, TranslatableComponentImpl.IS_NOT_EMPTY), Objects.requireNonNull(style, "style"), Objects.requireNonNull(key, "key"), ComponentLike.asComponents(args));
    }
    
    TranslatableComponentImpl(@NotNull final List<Component> children, @NotNull final Style style, @NotNull final String key, @NotNull final List<Component> args) {
        super(children, style);
        this.key = key;
        this.args = args;
    }
    
    @NotNull
    @Override
    public String key() {
        return this.key;
    }
    
    @NotNull
    @Override
    public TranslatableComponent key(@NotNull final String key) {
        if (Objects.equals(this.key, key)) {
            return this;
        }
        return create(this.children, this.style, key, this.args);
    }
    
    @NotNull
    @Override
    public List<Component> args() {
        return this.args;
    }
    
    @NotNull
    @Override
    public TranslatableComponent args(@NotNull final ComponentLike... args) {
        return create(this.children, this.style, this.key, args);
    }
    
    @NotNull
    @Override
    public TranslatableComponent args(@NotNull final List<? extends ComponentLike> args) {
        return create(this.children, this.style, this.key, args);
    }
    
    @NotNull
    @Override
    public TranslatableComponent children(@NotNull final List<? extends ComponentLike> children) {
        return create(children, this.style, this.key, this.args);
    }
    
    @NotNull
    @Override
    public TranslatableComponent style(@NotNull final Style style) {
        return create(this.children, style, this.key, this.args);
    }
    
    @Override
    public boolean equals(@Nullable final Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof TranslatableComponent)) {
            return false;
        }
        if (!super.equals(other)) {
            return false;
        }
        final TranslatableComponent that = (TranslatableComponent)other;
        return Objects.equals(this.key, that.key()) && Objects.equals(this.args, that.args());
    }
    
    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + this.key.hashCode();
        result = 31 * result + this.args.hashCode();
        return result;
    }
    
    @Override
    public String toString() {
        return Internals.toString(this);
    }
    
    @NotNull
    @Override
    public Builder toBuilder() {
        return new BuilderImpl(this);
    }
    
    static final class BuilderImpl extends AbstractComponentBuilder<TranslatableComponent, TranslatableComponent.Builder> implements TranslatableComponent.Builder
    {
        @Nullable
        private String key;
        private List<? extends Component> args;
        
        BuilderImpl() {
            this.args = Collections.emptyList();
        }
        
        BuilderImpl(@NotNull final TranslatableComponent component) {
            super(component);
            this.args = Collections.emptyList();
            this.key = component.key();
            this.args = component.args();
        }
        
        @NotNull
        @Override
        public TranslatableComponent.Builder key(@NotNull final String key) {
            this.key = key;
            return this;
        }
        
        @NotNull
        @Override
        public TranslatableComponent.Builder args(@NotNull final ComponentBuilder<?, ?> arg) {
            return this.args((List<? extends ComponentLike>)Collections.singletonList(Objects.requireNonNull(arg, "arg").build()));
        }
        
        @NotNull
        @Override
        public TranslatableComponent.Builder args(@NotNull final ComponentBuilder<?, ?>... args) {
            Objects.requireNonNull(args, "args");
            if (args.length == 0) {
                return this.args(Collections.emptyList());
            }
            return this.args((List<? extends ComponentLike>)Stream.of(args).map((Function<? super ComponentBuilder<?, ?>, ?>)ComponentBuilder::build).collect((Collector<? super Object, ?, List<? super Object>>)Collectors.toList()));
        }
        
        @NotNull
        @Override
        public TranslatableComponent.Builder args(@NotNull final Component arg) {
            return this.args((List<? extends ComponentLike>)Collections.singletonList((Object)Objects.requireNonNull((T)arg, "arg")));
        }
        
        @NotNull
        @Override
        public TranslatableComponent.Builder args(@NotNull final ComponentLike... args) {
            Objects.requireNonNull(args, "args");
            if (args.length == 0) {
                return this.args(Collections.emptyList());
            }
            return this.args(Arrays.asList(args));
        }
        
        @NotNull
        @Override
        public TranslatableComponent.Builder args(@NotNull final List<? extends ComponentLike> args) {
            this.args = ComponentLike.asComponents(Objects.requireNonNull(args, "args"));
            return this;
        }
        
        @NotNull
        @Override
        public TranslatableComponent build() {
            if (this.key == null) {
                throw new IllegalStateException("key must be set");
            }
            return TranslatableComponentImpl.create(this.children, this.buildStyle(), this.key, this.args);
        }
    }
}
