// 
// Decompiled by Procyon v0.6.0
// 

package com.viaversion.viaversion.libs.kyori.adventure.text;

import org.jetbrains.annotations.Contract;
import com.viaversion.viaversion.libs.kyori.adventure.builder.AbstractBuilder;
import com.viaversion.viaversion.libs.kyori.adventure.text.format.Style;
import java.util.function.Predicate;
import java.util.function.Function;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.ApiStatus;
import com.viaversion.viaversion.libs.kyori.examination.Examinable;
import com.viaversion.viaversion.libs.kyori.adventure.util.Buildable;

@ApiStatus.NonExtendable
public interface JoinConfiguration extends Buildable<JoinConfiguration, Builder>, Examinable
{
    @NotNull
    default Builder builder() {
        return new JoinConfigurationImpl.BuilderImpl();
    }
    
    @NotNull
    default JoinConfiguration noSeparators() {
        return JoinConfigurationImpl.NULL;
    }
    
    @NotNull
    default JoinConfiguration newlines() {
        return JoinConfigurationImpl.STANDARD_NEW_LINES;
    }
    
    @NotNull
    default JoinConfiguration commas(final boolean spaces) {
        return spaces ? JoinConfigurationImpl.STANDARD_COMMA_SPACE_SEPARATED : JoinConfigurationImpl.STANDARD_COMMA_SEPARATED;
    }
    
    @NotNull
    default JoinConfiguration arrayLike() {
        return JoinConfigurationImpl.STANDARD_ARRAY_LIKE;
    }
    
    @NotNull
    default JoinConfiguration separator(@Nullable final ComponentLike separator) {
        if (separator == null) {
            return JoinConfigurationImpl.NULL;
        }
        return builder().separator(separator).build();
    }
    
    @NotNull
    default JoinConfiguration separators(@Nullable final ComponentLike separator, @Nullable final ComponentLike lastSeparator) {
        if (separator == null && lastSeparator == null) {
            return JoinConfigurationImpl.NULL;
        }
        return builder().separator(separator).lastSeparator(lastSeparator).build();
    }
    
    @Nullable
    Component prefix();
    
    @Nullable
    Component suffix();
    
    @Nullable
    Component separator();
    
    @Nullable
    Component lastSeparator();
    
    @Nullable
    Component lastSeparatorIfSerial();
    
    @NotNull
    Function<ComponentLike, Component> convertor();
    
    @NotNull
    Predicate<ComponentLike> predicate();
    
    @NotNull
    Style parentStyle();
    
    public interface Builder extends AbstractBuilder<JoinConfiguration>, Buildable.Builder<JoinConfiguration>
    {
        @Contract("_ -> this")
        @NotNull
        Builder prefix(@Nullable final ComponentLike prefix);
        
        @Contract("_ -> this")
        @NotNull
        Builder suffix(@Nullable final ComponentLike suffix);
        
        @Contract("_ -> this")
        @NotNull
        Builder separator(@Nullable final ComponentLike separator);
        
        @Contract("_ -> this")
        @NotNull
        Builder lastSeparator(@Nullable final ComponentLike lastSeparator);
        
        @Contract("_ -> this")
        @NotNull
        Builder lastSeparatorIfSerial(@Nullable final ComponentLike lastSeparatorIfSerial);
        
        @Contract("_ -> this")
        @NotNull
        Builder convertor(@NotNull final Function<ComponentLike, Component> convertor);
        
        @Contract("_ -> this")
        @NotNull
        Builder predicate(@NotNull final Predicate<ComponentLike> predicate);
        
        @Contract("_ -> this")
        @NotNull
        Builder parentStyle(@NotNull final Style parentStyle);
    }
}
