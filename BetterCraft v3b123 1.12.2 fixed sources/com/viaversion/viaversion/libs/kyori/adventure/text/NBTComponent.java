// 
// Decompiled by Procyon v0.6.0
// 

package com.viaversion.viaversion.libs.kyori.adventure.text;

import com.viaversion.viaversion.libs.kyori.examination.ExaminableProperty;
import java.util.stream.Stream;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public interface NBTComponent<C extends NBTComponent<C, B>, B extends NBTComponentBuilder<C, B>> extends BuildableComponent<C, B>
{
    @NotNull
    String nbtPath();
    
    @Contract(pure = true)
    @NotNull
    C nbtPath(@NotNull final String nbtPath);
    
    boolean interpret();
    
    @Contract(pure = true)
    @NotNull
    C interpret(final boolean interpret);
    
    @Nullable
    Component separator();
    
    @NotNull
    C separator(@Nullable final ComponentLike separator);
    
    @NotNull
    default Stream<? extends ExaminableProperty> examinableProperties() {
        return Stream.concat((Stream<? extends ExaminableProperty>)Stream.of((T[])new ExaminableProperty[] { ExaminableProperty.of("nbtPath", this.nbtPath()), ExaminableProperty.of("interpret", this.interpret()), ExaminableProperty.of("separator", this.separator()) }), super.examinableProperties());
    }
}
