// 
// Decompiled by Procyon v0.6.0
// 

package com.viaversion.viaversion.libs.kyori.adventure.text;

import com.viaversion.viaversion.libs.kyori.examination.ExaminableProperty;
import java.util.stream.Stream;
import java.util.Objects;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public interface KeybindComponent extends BuildableComponent<KeybindComponent, Builder>, ScopedComponent<KeybindComponent>
{
    @NotNull
    String keybind();
    
    @Contract(pure = true)
    @NotNull
    KeybindComponent keybind(@NotNull final String keybind);
    
    @Contract(pure = true)
    @NotNull
    default KeybindComponent keybind(@NotNull final KeybindLike keybind) {
        return this.keybind(Objects.requireNonNull(keybind, "keybind").asKeybind());
    }
    
    @NotNull
    default Stream<? extends ExaminableProperty> examinableProperties() {
        return Stream.concat((Stream<? extends ExaminableProperty>)Stream.of(ExaminableProperty.of("keybind", this.keybind())), super.examinableProperties());
    }
    
    public interface Builder extends ComponentBuilder<KeybindComponent, Builder>
    {
        @Contract("_ -> this")
        @NotNull
        Builder keybind(@NotNull final String keybind);
        
        @Contract(pure = true)
        @NotNull
        default Builder keybind(@NotNull final KeybindLike keybind) {
            return this.keybind(Objects.requireNonNull(keybind, "keybind").asKeybind());
        }
    }
    
    public interface KeybindLike
    {
        @NotNull
        String asKeybind();
    }
}
