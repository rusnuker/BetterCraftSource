// 
// Decompiled by Procyon v0.6.0
// 

package com.viaversion.viaversion.libs.kyori.adventure.chat;

import com.viaversion.viaversion.libs.kyori.examination.ExaminableProperty;
import java.util.stream.Stream;
import com.viaversion.viaversion.libs.kyori.adventure.identity.Identity;
import com.viaversion.viaversion.libs.kyori.adventure.text.Component;
import java.time.Instant;
import org.jetbrains.annotations.Nullable;
import com.viaversion.viaversion.libs.kyori.adventure.text.ComponentLike;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.ApiStatus;
import com.viaversion.viaversion.libs.kyori.examination.Examinable;
import com.viaversion.viaversion.libs.kyori.adventure.identity.Identified;

@ApiStatus.NonExtendable
public interface SignedMessage extends Identified, Examinable
{
    @Contract(value = "_ -> new", pure = true)
    @NotNull
    default Signature signature(final byte[] signature) {
        return new SignedMessageImpl.SignatureImpl(signature);
    }
    
    @Contract(value = "_, _ -> new", pure = true)
    @NotNull
    default SignedMessage system(@NotNull final String message, @Nullable final ComponentLike unsignedContent) {
        return new SignedMessageImpl(message, ComponentLike.unbox(unsignedContent));
    }
    
    @Contract(pure = true)
    @NotNull
    Instant timestamp();
    
    @Contract(pure = true)
    long salt();
    
    @Contract(pure = true)
    @Nullable
    Signature signature();
    
    @Contract(pure = true)
    @Nullable
    Component unsignedContent();
    
    @Contract(pure = true)
    @NotNull
    String message();
    
    @Contract(pure = true)
    default boolean isSystem() {
        return this.identity() == Identity.nil();
    }
    
    @Contract(pure = true)
    default boolean canDelete() {
        return this.signature() != null;
    }
    
    @NotNull
    default Stream<? extends ExaminableProperty> examinableProperties() {
        return Stream.of((ExaminableProperty[])new ExaminableProperty[] { ExaminableProperty.of("timestamp", this.timestamp()), ExaminableProperty.of("salt", this.salt()), ExaminableProperty.of("signature", this.signature()), ExaminableProperty.of("unsignedContent", this.unsignedContent()), ExaminableProperty.of("message", this.message()) });
    }
    
    @ApiStatus.NonExtendable
    public interface Signature extends Examinable
    {
        @Contract(pure = true)
        byte[] bytes();
        
        @NotNull
        default Stream<? extends ExaminableProperty> examinableProperties() {
            return Stream.of(ExaminableProperty.of("bytes", this.bytes()));
        }
    }
}
