// 
// Decompiled by Procyon v0.6.0
// 

package com.viaversion.viaversion.libs.kyori.adventure.chat;

import com.viaversion.viaversion.libs.kyori.adventure.key.Key;
import com.viaversion.viaversion.libs.kyori.examination.ExaminableProperty;
import java.util.stream.Stream;
import com.viaversion.viaversion.libs.kyori.adventure.text.Component;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Contract;
import com.viaversion.viaversion.libs.kyori.adventure.text.ComponentLike;
import java.util.Objects;
import org.jetbrains.annotations.NotNull;
import com.viaversion.viaversion.libs.kyori.adventure.key.Keyed;
import com.viaversion.viaversion.libs.kyori.examination.Examinable;

public interface ChatType extends Examinable, Keyed
{
    public static final ChatType CHAT = new ChatTypeImpl(Key.key("chat"));
    public static final ChatType SAY_COMMAND = new ChatTypeImpl(Key.key("say_command"));
    public static final ChatType MSG_COMMAND_INCOMING = new ChatTypeImpl(Key.key("msg_command_incoming"));
    public static final ChatType MSG_COMMAND_OUTGOING = new ChatTypeImpl(Key.key("msg_command_outgoing"));
    public static final ChatType TEAM_MSG_COMMAND_INCOMING = new ChatTypeImpl(Key.key("team_msg_command_incoming"));
    public static final ChatType TEAM_MSG_COMMAND_OUTGOING = new ChatTypeImpl(Key.key("team_msg_command_outgoing"));
    public static final ChatType EMOTE_COMMAND = new ChatTypeImpl(Key.key("emote_command"));
    
    @NotNull
    default ChatType chatType(@NotNull final Keyed key) {
        return (key instanceof ChatType) ? ((ChatType)key) : new ChatTypeImpl(Objects.requireNonNull(key, "key").key());
    }
    
    @Contract(value = "_ -> new", pure = true)
    default Bound bind(@NotNull final ComponentLike name) {
        return this.bind(name, null);
    }
    
    @Contract(value = "_, _ -> new", pure = true)
    default Bound bind(@NotNull final ComponentLike name, @Nullable final ComponentLike target) {
        return new ChatTypeImpl.BoundImpl(this, Objects.requireNonNull(name.asComponent(), "name"), ComponentLike.unbox(target));
    }
    
    @NotNull
    default Stream<? extends ExaminableProperty> examinableProperties() {
        return Stream.of(ExaminableProperty.of("key", this.key()));
    }
    
    public interface Bound extends Examinable
    {
        @Contract(pure = true)
        @NotNull
        ChatType type();
        
        @Contract(pure = true)
        @NotNull
        Component name();
        
        @Contract(pure = true)
        @Nullable
        Component target();
        
        @NotNull
        default Stream<? extends ExaminableProperty> examinableProperties() {
            return Stream.of((ExaminableProperty[])new ExaminableProperty[] { ExaminableProperty.of("type", this.type()), ExaminableProperty.of("name", this.name()), ExaminableProperty.of("target", this.target()) });
        }
    }
}
