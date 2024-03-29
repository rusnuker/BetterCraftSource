// 
// Decompiled by Procyon v0.6.0
// 

package com.viaversion.viaversion.libs.kyori.adventure.nbt.api;

import org.jetbrains.annotations.ApiStatus;
import com.viaversion.viaversion.libs.kyori.adventure.util.Codec;
import org.jetbrains.annotations.NotNull;

public interface BinaryTagHolder
{
    @NotNull
    default <T, EX extends Exception> BinaryTagHolder encode(@NotNull final T nbt, @NotNull final Codec<? super T, String, ?, EX> codec) throws EX, Exception {
        return new BinaryTagHolderImpl(codec.encode(nbt));
    }
    
    @NotNull
    default BinaryTagHolder binaryTagHolder(@NotNull final String string) {
        return new BinaryTagHolderImpl(string);
    }
    
    @Deprecated
    @ApiStatus.ScheduledForRemoval(inVersion = "5.0.0")
    @NotNull
    default BinaryTagHolder of(@NotNull final String string) {
        return new BinaryTagHolderImpl(string);
    }
    
    @NotNull
    String string();
    
    @NotNull
     <T, DX extends Exception> T get(@NotNull final Codec<T, String, DX, ?> codec) throws DX, Exception;
}
