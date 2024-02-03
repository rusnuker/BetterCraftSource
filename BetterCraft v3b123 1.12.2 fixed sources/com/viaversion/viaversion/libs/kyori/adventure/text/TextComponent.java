// 
// Decompiled by Procyon v0.6.0
// 

package com.viaversion.viaversion.libs.kyori.adventure.text;

import com.viaversion.viaversion.libs.kyori.examination.ExaminableProperty;
import java.util.stream.Stream;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

public interface TextComponent extends BuildableComponent<TextComponent, Builder>, ScopedComponent<TextComponent>
{
    @Deprecated
    @ApiStatus.ScheduledForRemoval(inVersion = "5.0.0")
    @NotNull
    default TextComponent ofChildren(@NotNull final ComponentLike... components) {
        return Component.textOfChildren(components);
    }
    
    @NotNull
    String content();
    
    @Contract(pure = true)
    @NotNull
    TextComponent content(@NotNull final String content);
    
    @NotNull
    default Stream<? extends ExaminableProperty> examinableProperties() {
        return Stream.concat((Stream<? extends ExaminableProperty>)Stream.of(ExaminableProperty.of("content", this.content())), super.examinableProperties());
    }
    
    public interface Builder extends ComponentBuilder<TextComponent, Builder>
    {
        @NotNull
        String content();
        
        @Contract("_ -> this")
        @NotNull
        Builder content(@NotNull final String content);
    }
}
