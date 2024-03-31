// 
// Decompiled by Procyon v0.6.0
// 

package com.viaversion.viaversion.libs.kyori.adventure.text;

import org.jetbrains.annotations.Contract;
import java.util.List;
import com.viaversion.viaversion.libs.kyori.adventure.text.format.Style;
import java.util.Arrays;
import org.jetbrains.annotations.NotNull;

public interface TextComponent extends BuildableComponent<TextComponent, Builder>, ScopedComponent<TextComponent>
{
    @NotNull
    default TextComponent ofChildren(@NotNull final ComponentLike... components) {
        if (components.length == 0) {
            return Component.empty();
        }
        return new TextComponentImpl(Arrays.asList(components), Style.empty(), "");
    }
    
    @NotNull
    String content();
    
    @Contract(pure = true)
    @NotNull
    TextComponent content(@NotNull final String content);
    
    public interface Builder extends ComponentBuilder<TextComponent, Builder>
    {
        @NotNull
        String content();
        
        @Contract("_ -> this")
        @NotNull
        Builder content(@NotNull final String content);
    }
}
