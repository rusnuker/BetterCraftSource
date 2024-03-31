// 
// Decompiled by Procyon v0.6.0
// 

package com.viaversion.viaversion.libs.kyori.adventure.text;

import com.viaversion.viaversion.libs.kyori.adventure.internal.properties.AdventureProperties;
import com.viaversion.viaversion.libs.kyori.adventure.util.Buildable;
import com.viaversion.viaversion.libs.kyori.examination.Examinable;
import com.viaversion.viaversion.libs.kyori.adventure.internal.Internals;
import org.jetbrains.annotations.Nullable;
import com.viaversion.viaversion.libs.kyori.adventure.util.Nag;
import java.util.Collections;
import java.util.Objects;
import java.util.function.Predicate;
import com.viaversion.viaversion.libs.kyori.adventure.text.format.Style;
import org.jetbrains.annotations.NotNull;
import java.util.List;
import org.jetbrains.annotations.VisibleForTesting;

final class TextComponentImpl extends AbstractComponent implements TextComponent
{
    private static final boolean WARN_WHEN_LEGACY_FORMATTING_DETECTED;
    @VisibleForTesting
    static final char SECTION_CHAR = '§';
    static final TextComponent EMPTY;
    static final TextComponent NEWLINE;
    static final TextComponent SPACE;
    private final String content;
    
    static TextComponent create(@NotNull final List<? extends ComponentLike> children, @NotNull final Style style, @NotNull final String content) {
        final List<Component> filteredChildren = ComponentLike.asComponents(children, TextComponentImpl.IS_NOT_EMPTY);
        if (filteredChildren.isEmpty() && style.isEmpty() && content.isEmpty()) {
            return Component.empty();
        }
        return new TextComponentImpl(filteredChildren, Objects.requireNonNull(style, "style"), Objects.requireNonNull(content, "content"));
    }
    
    @NotNull
    private static TextComponent createDirect(@NotNull final String content) {
        return new TextComponentImpl(Collections.emptyList(), Style.empty(), content);
    }
    
    TextComponentImpl(@NotNull final List<Component> children, @NotNull final Style style, @NotNull final String content) {
        super(children, style);
        this.content = content;
        if (TextComponentImpl.WARN_WHEN_LEGACY_FORMATTING_DETECTED) {
            final LegacyFormattingDetected nag = this.warnWhenLegacyFormattingDetected();
            if (nag != null) {
                Nag.print(nag);
            }
        }
    }
    
    @VisibleForTesting
    @Nullable
    final LegacyFormattingDetected warnWhenLegacyFormattingDetected() {
        if (this.content.indexOf(167) != -1) {
            return new LegacyFormattingDetected(this);
        }
        return null;
    }
    
    @NotNull
    @Override
    public String content() {
        return this.content;
    }
    
    @NotNull
    @Override
    public TextComponent content(@NotNull final String content) {
        if (Objects.equals(this.content, content)) {
            return this;
        }
        return create(this.children, this.style, content);
    }
    
    @NotNull
    @Override
    public TextComponent children(@NotNull final List<? extends ComponentLike> children) {
        return create(children, this.style, this.content);
    }
    
    @NotNull
    @Override
    public TextComponent style(@NotNull final Style style) {
        return create(this.children, style, this.content);
    }
    
    @Override
    public boolean equals(@Nullable final Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof TextComponentImpl)) {
            return false;
        }
        if (!super.equals(other)) {
            return false;
        }
        final TextComponentImpl that = (TextComponentImpl)other;
        return Objects.equals(this.content, that.content);
    }
    
    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + this.content.hashCode();
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
    
    static {
        WARN_WHEN_LEGACY_FORMATTING_DETECTED = Boolean.TRUE.equals(AdventureProperties.TEXT_WARN_WHEN_LEGACY_FORMATTING_DETECTED.value());
        EMPTY = createDirect("");
        NEWLINE = createDirect("\n");
        SPACE = createDirect(" ");
    }
    
    static final class BuilderImpl extends AbstractComponentBuilder<TextComponent, TextComponent.Builder> implements TextComponent.Builder
    {
        private String content;
        
        BuilderImpl() {
            this.content = "";
        }
        
        BuilderImpl(@NotNull final TextComponent component) {
            super(component);
            this.content = "";
            this.content = component.content();
        }
        
        @NotNull
        @Override
        public TextComponent.Builder content(@NotNull final String content) {
            this.content = Objects.requireNonNull(content, "content");
            return this;
        }
        
        @NotNull
        @Override
        public String content() {
            return this.content;
        }
        
        @NotNull
        @Override
        public TextComponent build() {
            if (this.isEmpty()) {
                return Component.empty();
            }
            return TextComponentImpl.create(this.children, this.buildStyle(), this.content);
        }
        
        private boolean isEmpty() {
            return this.content.isEmpty() && this.children.isEmpty() && !this.hasStyle();
        }
    }
}
