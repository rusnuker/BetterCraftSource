// 
// Decompiled by Procyon v0.6.0
// 

package com.viaversion.viaversion.libs.kyori.adventure.text;

import com.viaversion.viaversion.libs.kyori.examination.ExaminableProperty;
import java.util.stream.Stream;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public interface EntityNBTComponent extends NBTComponent<EntityNBTComponent, Builder>, ScopedComponent<EntityNBTComponent>
{
    @NotNull
    String selector();
    
    @Contract(pure = true)
    @NotNull
    EntityNBTComponent selector(@NotNull final String selector);
    
    @NotNull
    default Stream<? extends ExaminableProperty> examinableProperties() {
        return Stream.concat((Stream<? extends ExaminableProperty>)Stream.of(ExaminableProperty.of("selector", this.selector())), super.examinableProperties());
    }
    
    public interface Builder extends NBTComponentBuilder<EntityNBTComponent, Builder>
    {
        @Contract("_ -> this")
        @NotNull
        Builder selector(@NotNull final String selector);
    }
}
