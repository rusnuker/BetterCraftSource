// 
// Decompiled by Procyon v0.6.0
// 

package com.viaversion.viaversion.libs.kyori.adventure.text;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public interface KeybindComponent extends BuildableComponent<KeybindComponent, Builder>, ScopedComponent<KeybindComponent>
{
    @NotNull
    String keybind();
    
    @Contract(pure = true)
    @NotNull
    KeybindComponent keybind(@NotNull final String keybind);
    
    public interface Builder extends ComponentBuilder<KeybindComponent, Builder>
    {
        @Contract("_ -> this")
        @NotNull
        Builder keybind(@NotNull final String keybind);
    }
}
