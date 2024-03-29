// 
// Decompiled by Procyon v0.6.0
// 

package com.viaversion.viaversion.libs.kyori.adventure.audience;

import com.viaversion.viaversion.libs.kyori.adventure.inventory.Book;
import com.viaversion.viaversion.libs.kyori.adventure.sound.SoundStop;
import com.viaversion.viaversion.libs.kyori.adventure.sound.Sound;
import com.viaversion.viaversion.libs.kyori.adventure.bossbar.BossBar;
import com.viaversion.viaversion.libs.kyori.adventure.title.TitlePart;
import com.viaversion.viaversion.libs.kyori.adventure.title.Title;
import java.util.Objects;
import com.viaversion.viaversion.libs.kyori.adventure.chat.SignedMessage;
import com.viaversion.viaversion.libs.kyori.adventure.chat.ChatType;
import com.viaversion.viaversion.libs.kyori.adventure.identity.Identified;
import com.viaversion.viaversion.libs.kyori.adventure.identity.Identity;
import org.jetbrains.annotations.ApiStatus;
import com.viaversion.viaversion.libs.kyori.adventure.text.Component;
import com.viaversion.viaversion.libs.kyori.adventure.text.ComponentLike;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Collector;
import java.util.Arrays;
import org.jetbrains.annotations.NotNull;
import com.viaversion.viaversion.libs.kyori.adventure.pointer.Pointered;

public interface Audience extends Pointered
{
    @NotNull
    default Audience empty() {
        return EmptyAudience.INSTANCE;
    }
    
    @NotNull
    default Audience audience(@NotNull final Audience... audiences) {
        final int length = audiences.length;
        if (length == 0) {
            return empty();
        }
        if (length == 1) {
            return audiences[0];
        }
        return audience(Arrays.asList(audiences));
    }
    
    @NotNull
    default ForwardingAudience audience(@NotNull final Iterable<? extends Audience> audiences) {
        return () -> audiences;
    }
    
    @NotNull
    default Collector<? super Audience, ?, ForwardingAudience> toAudience() {
        return Audiences.COLLECTOR;
    }
    
    @NotNull
    default Audience filterAudience(@NotNull final Predicate<? super Audience> filter) {
        return filter.test(this) ? this : empty();
    }
    
    default void forEachAudience(@NotNull final Consumer<? super Audience> action) {
        action.accept(this);
    }
    
    @ForwardingAudienceOverrideNotRequired
    default void sendMessage(@NotNull final ComponentLike message) {
        this.sendMessage(message.asComponent());
    }
    
    default void sendMessage(@NotNull final Component message) {
        this.sendMessage(message, MessageType.SYSTEM);
    }
    
    @Deprecated
    @ForwardingAudienceOverrideNotRequired
    @ApiStatus.ScheduledForRemoval(inVersion = "5.0.0")
    default void sendMessage(@NotNull final ComponentLike message, @NotNull final MessageType type) {
        this.sendMessage(message.asComponent(), type);
    }
    
    @Deprecated
    @ForwardingAudienceOverrideNotRequired
    @ApiStatus.ScheduledForRemoval(inVersion = "5.0.0")
    default void sendMessage(@NotNull final Component message, @NotNull final MessageType type) {
        this.sendMessage(Identity.nil(), message, type);
    }
    
    @Deprecated
    @ForwardingAudienceOverrideNotRequired
    default void sendMessage(@NotNull final Identified source, @NotNull final ComponentLike message) {
        this.sendMessage(source, message.asComponent());
    }
    
    @Deprecated
    @ForwardingAudienceOverrideNotRequired
    default void sendMessage(@NotNull final Identity source, @NotNull final ComponentLike message) {
        this.sendMessage(source, message.asComponent());
    }
    
    @Deprecated
    @ForwardingAudienceOverrideNotRequired
    default void sendMessage(@NotNull final Identified source, @NotNull final Component message) {
        this.sendMessage(source, message, MessageType.CHAT);
    }
    
    @Deprecated
    @ForwardingAudienceOverrideNotRequired
    default void sendMessage(@NotNull final Identity source, @NotNull final Component message) {
        this.sendMessage(source, message, MessageType.CHAT);
    }
    
    @Deprecated
    @ForwardingAudienceOverrideNotRequired
    @ApiStatus.ScheduledForRemoval(inVersion = "5.0.0")
    default void sendMessage(@NotNull final Identified source, @NotNull final ComponentLike message, @NotNull final MessageType type) {
        this.sendMessage(source, message.asComponent(), type);
    }
    
    @Deprecated
    @ForwardingAudienceOverrideNotRequired
    @ApiStatus.ScheduledForRemoval(inVersion = "5.0.0")
    default void sendMessage(@NotNull final Identity source, @NotNull final ComponentLike message, @NotNull final MessageType type) {
        this.sendMessage(source, message.asComponent(), type);
    }
    
    @Deprecated
    @ApiStatus.ScheduledForRemoval(inVersion = "5.0.0")
    default void sendMessage(@NotNull final Identified source, @NotNull final Component message, @NotNull final MessageType type) {
        this.sendMessage(source.identity(), message, type);
    }
    
    @Deprecated
    @ApiStatus.ScheduledForRemoval(inVersion = "5.0.0")
    default void sendMessage(@NotNull final Identity source, @NotNull final Component message, @NotNull final MessageType type) {
    }
    
    default void sendMessage(@NotNull final Component message, final ChatType.Bound boundChatType) {
        this.sendMessage(message, MessageType.CHAT);
    }
    
    @ForwardingAudienceOverrideNotRequired
    default void sendMessage(@NotNull final ComponentLike message, final ChatType.Bound boundChatType) {
        this.sendMessage(message.asComponent(), boundChatType);
    }
    
    default void sendMessage(@NotNull final SignedMessage signedMessage, final ChatType.Bound boundChatType) {
        final Component content = (signedMessage.unsignedContent() != null) ? signedMessage.unsignedContent() : Component.text(signedMessage.message());
        if (signedMessage.isSystem()) {
            this.sendMessage(content);
        }
        else {
            this.sendMessage(signedMessage.identity(), content, MessageType.CHAT);
        }
    }
    
    @ForwardingAudienceOverrideNotRequired
    default void deleteMessage(@NotNull final SignedMessage signedMessage) {
        if (signedMessage.canDelete()) {
            this.deleteMessage(Objects.requireNonNull(signedMessage.signature()));
        }
    }
    
    default void deleteMessage(final SignedMessage.Signature signature) {
    }
    
    @ForwardingAudienceOverrideNotRequired
    default void sendActionBar(@NotNull final ComponentLike message) {
        this.sendActionBar(message.asComponent());
    }
    
    default void sendActionBar(@NotNull final Component message) {
    }
    
    @ForwardingAudienceOverrideNotRequired
    default void sendPlayerListHeader(@NotNull final ComponentLike header) {
        this.sendPlayerListHeader(header.asComponent());
    }
    
    default void sendPlayerListHeader(@NotNull final Component header) {
        this.sendPlayerListHeaderAndFooter(header, Component.empty());
    }
    
    @ForwardingAudienceOverrideNotRequired
    default void sendPlayerListFooter(@NotNull final ComponentLike footer) {
        this.sendPlayerListFooter(footer.asComponent());
    }
    
    default void sendPlayerListFooter(@NotNull final Component footer) {
        this.sendPlayerListHeaderAndFooter(Component.empty(), footer);
    }
    
    @ForwardingAudienceOverrideNotRequired
    default void sendPlayerListHeaderAndFooter(@NotNull final ComponentLike header, @NotNull final ComponentLike footer) {
        this.sendPlayerListHeaderAndFooter(header.asComponent(), footer.asComponent());
    }
    
    default void sendPlayerListHeaderAndFooter(@NotNull final Component header, @NotNull final Component footer) {
    }
    
    @ForwardingAudienceOverrideNotRequired
    default void showTitle(@NotNull final Title title) {
        final Title.Times times = title.times();
        if (times != null) {
            this.sendTitlePart(TitlePart.TIMES, times);
        }
        this.sendTitlePart(TitlePart.SUBTITLE, title.subtitle());
        this.sendTitlePart(TitlePart.TITLE, title.title());
    }
    
    default <T> void sendTitlePart(@NotNull final TitlePart<T> part, @NotNull final T value) {
    }
    
    default void clearTitle() {
    }
    
    default void resetTitle() {
    }
    
    default void showBossBar(@NotNull final BossBar bar) {
    }
    
    default void hideBossBar(@NotNull final BossBar bar) {
    }
    
    default void playSound(@NotNull final Sound sound) {
    }
    
    default void playSound(@NotNull final Sound sound, final double x, final double y, final double z) {
    }
    
    default void playSound(@NotNull final Sound sound, final Sound.Emitter emitter) {
    }
    
    @ForwardingAudienceOverrideNotRequired
    default void stopSound(@NotNull final Sound sound) {
        this.stopSound(Objects.requireNonNull(sound, "sound").asStop());
    }
    
    default void stopSound(@NotNull final SoundStop stop) {
    }
    
    @ForwardingAudienceOverrideNotRequired
    default void openBook(final Book.Builder book) {
        this.openBook(book.build());
    }
    
    default void openBook(@NotNull final Book book) {
    }
}
