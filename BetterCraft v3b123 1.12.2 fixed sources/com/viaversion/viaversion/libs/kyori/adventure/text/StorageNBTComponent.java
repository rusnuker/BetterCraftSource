// 
// Decompiled by Procyon v0.6.0
// 

package com.viaversion.viaversion.libs.kyori.adventure.text;

import com.viaversion.viaversion.libs.kyori.examination.ExaminableProperty;
import java.util.stream.Stream;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import com.viaversion.viaversion.libs.kyori.adventure.key.Key;

public interface StorageNBTComponent extends NBTComponent<StorageNBTComponent, Builder>, ScopedComponent<StorageNBTComponent>
{
    @NotNull
    Key storage();
    
    @Contract(pure = true)
    @NotNull
    StorageNBTComponent storage(@NotNull final Key storage);
    
    @NotNull
    default Stream<? extends ExaminableProperty> examinableProperties() {
        return Stream.concat((Stream<? extends ExaminableProperty>)Stream.of(ExaminableProperty.of("storage", this.storage())), super.examinableProperties());
    }
    
    public interface Builder extends NBTComponentBuilder<StorageNBTComponent, Builder>
    {
        @Contract("_ -> this")
        @NotNull
        Builder storage(@NotNull final Key storage);
    }
}
