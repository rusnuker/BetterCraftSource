// 
// Decompiled by Procyon v0.6.0
// 

package com.viaversion.viaversion.libs.kyori.adventure.audience;

import java.util.Collections;
import com.viaversion.viaversion.libs.kyori.adventure.inventory.Book;
import com.viaversion.viaversion.libs.kyori.adventure.sound.SoundStop;
import com.viaversion.viaversion.libs.kyori.adventure.sound.Sound;
import com.viaversion.viaversion.libs.kyori.adventure.bossbar.BossBar;
import com.viaversion.viaversion.libs.kyori.adventure.title.Title;
import com.viaversion.viaversion.libs.kyori.adventure.identity.Identity;
import java.util.Iterator;
import com.viaversion.viaversion.libs.kyori.adventure.text.Component;
import com.viaversion.viaversion.libs.kyori.adventure.identity.Identified;
import java.util.function.Supplier;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.Nullable;
import java.util.Optional;
import com.viaversion.viaversion.libs.kyori.adventure.pointer.Pointer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.ApiStatus;

@FunctionalInterface
public interface ForwardingAudience extends Audience
{
    @ApiStatus.OverrideOnly
    @NotNull
    Iterable<? extends Audience> audiences();
    
    @NotNull
    default <T> Optional<T> get(@NotNull final Pointer<T> pointer) {
        return Optional.empty();
    }
    
    @Contract("_, null -> null; _, !null -> !null")
    @Nullable
    default <T> T getOrDefault(@NotNull final Pointer<T> pointer, @Nullable final T defaultValue) {
        return defaultValue;
    }
    
    default <T> T getOrDefaultFrom(@NotNull final Pointer<T> pointer, @NotNull final Supplier<? extends T> defaultValue) {
        return (T)defaultValue.get();
    }
    
    default void sendMessage(@NotNull final Identified source, @NotNull final Component message, @NotNull final MessageType type) {
        for (final Audience audience : this.audiences()) {
            audience.sendMessage(source, message, type);
        }
    }
    
    default void sendMessage(@NotNull final Identity source, @NotNull final Component message, @NotNull final MessageType type) {
        for (final Audience audience : this.audiences()) {
            audience.sendMessage(source, message, type);
        }
    }
    
    default void sendActionBar(@NotNull final Component message) {
        for (final Audience audience : this.audiences()) {
            audience.sendActionBar(message);
        }
    }
    
    default void sendPlayerListHeader(@NotNull final Component header) {
        for (final Audience audience : this.audiences()) {
            audience.sendPlayerListHeader(header);
        }
    }
    
    default void sendPlayerListFooter(@NotNull final Component footer) {
        for (final Audience audience : this.audiences()) {
            audience.sendPlayerListFooter(footer);
        }
    }
    
    default void sendPlayerListHeaderAndFooter(@NotNull final Component header, @NotNull final Component footer) {
        for (final Audience audience : this.audiences()) {
            audience.sendPlayerListHeaderAndFooter(header, footer);
        }
    }
    
    default void showTitle(@NotNull final Title title) {
        for (final Audience audience : this.audiences()) {
            audience.showTitle(title);
        }
    }
    
    default void clearTitle() {
        for (final Audience audience : this.audiences()) {
            audience.clearTitle();
        }
    }
    
    default void resetTitle() {
        for (final Audience audience : this.audiences()) {
            audience.resetTitle();
        }
    }
    
    default void showBossBar(@NotNull final BossBar bar) {
        for (final Audience audience : this.audiences()) {
            audience.showBossBar(bar);
        }
    }
    
    default void hideBossBar(@NotNull final BossBar bar) {
        for (final Audience audience : this.audiences()) {
            audience.hideBossBar(bar);
        }
    }
    
    default void playSound(@NotNull final Sound sound) {
        for (final Audience audience : this.audiences()) {
            audience.playSound(sound);
        }
    }
    
    default void playSound(@NotNull final Sound sound, final double x, final double y, final double z) {
        for (final Audience audience : this.audiences()) {
            audience.playSound(sound, x, y, z);
        }
    }
    
    default void playSound(@NotNull final Sound sound, final Sound.Emitter emitter) {
        for (final Audience audience : this.audiences()) {
            audience.playSound(sound, emitter);
        }
    }
    
    default void stopSound(@NotNull final SoundStop stop) {
        for (final Audience audience : this.audiences()) {
            audience.stopSound(stop);
        }
    }
    
    default void openBook(@NotNull final Book book) {
        for (final Audience audience : this.audiences()) {
            audience.openBook(book);
        }
    }
    
    public interface Single extends ForwardingAudience
    {
        @ApiStatus.OverrideOnly
        @NotNull
        Audience audience();
        
        @Deprecated
        @NotNull
        default Iterable<? extends Audience> audiences() {
            return Collections.singleton(this.audience());
        }
        
        @NotNull
        default <T> Optional<T> get(@NotNull final Pointer<T> pointer) {
            return this.audience().get(pointer);
        }
        
        @Contract("_, null -> null; _, !null -> !null")
        @Nullable
        default <T> T getOrDefault(@NotNull final Pointer<T> pointer, @Nullable final T defaultValue) {
            return this.audience().getOrDefault(pointer, defaultValue);
        }
        
        default <T> T getOrDefaultFrom(@NotNull final Pointer<T> pointer, @NotNull final Supplier<? extends T> defaultValue) {
            return this.audience().getOrDefaultFrom(pointer, defaultValue);
        }
        
        default void sendMessage(@NotNull final Identified source, @NotNull final Component message, @NotNull final MessageType type) {
            this.audience().sendMessage(source, message, type);
        }
        
        default void sendMessage(@NotNull final Identity source, @NotNull final Component message, @NotNull final MessageType type) {
            this.audience().sendMessage(source, message, type);
        }
        
        default void sendActionBar(@NotNull final Component message) {
            this.audience().sendActionBar(message);
        }
        
        default void sendPlayerListHeader(@NotNull final Component header) {
            this.audience().sendPlayerListHeader(header);
        }
        
        default void sendPlayerListFooter(@NotNull final Component footer) {
            this.audience().sendPlayerListFooter(footer);
        }
        
        default void sendPlayerListHeaderAndFooter(@NotNull final Component header, @NotNull final Component footer) {
            this.audience().sendPlayerListHeaderAndFooter(header, footer);
        }
        
        default void showTitle(@NotNull final Title title) {
            this.audience().showTitle(title);
        }
        
        default void clearTitle() {
            this.audience().clearTitle();
        }
        
        default void resetTitle() {
            this.audience().resetTitle();
        }
        
        default void showBossBar(@NotNull final BossBar bar) {
            this.audience().showBossBar(bar);
        }
        
        default void hideBossBar(@NotNull final BossBar bar) {
            this.audience().hideBossBar(bar);
        }
        
        default void playSound(@NotNull final Sound sound) {
            this.audience().playSound(sound);
        }
        
        default void playSound(@NotNull final Sound sound, final double x, final double y, final double z) {
            this.audience().playSound(sound, x, y, z);
        }
        
        default void playSound(@NotNull final Sound sound, final Sound.Emitter emitter) {
            this.audience().playSound(sound, emitter);
        }
        
        default void stopSound(@NotNull final SoundStop stop) {
            this.audience().stopSound(stop);
        }
        
        default void openBook(@NotNull final Book book) {
            this.audience().openBook(book);
        }
    }
}
