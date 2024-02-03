// 
// Decompiled by Procyon v0.6.0
// 

package com.viaversion.viaversion.libs.kyori.adventure.chat;

import org.jetbrains.annotations.Nullable;
import com.viaversion.viaversion.libs.kyori.adventure.text.Component;
import com.viaversion.viaversion.libs.kyori.examination.Examinable;
import com.viaversion.viaversion.libs.kyori.adventure.internal.Internals;
import org.jetbrains.annotations.NotNull;
import com.viaversion.viaversion.libs.kyori.adventure.key.Key;

final class ChatTypeImpl implements ChatType
{
    private final Key key;
    
    ChatTypeImpl(@NotNull final Key key) {
        this.key = key;
    }
    
    @NotNull
    @Override
    public Key key() {
        return this.key;
    }
    
    @Override
    public String toString() {
        return Internals.toString(this);
    }
    
    static final class BoundImpl implements Bound
    {
        private final ChatType chatType;
        private final Component name;
        @Nullable
        private final Component target;
        
        BoundImpl(final ChatType chatType, final Component name, @Nullable final Component target) {
            this.chatType = chatType;
            this.name = name;
            this.target = target;
        }
        
        @NotNull
        @Override
        public ChatType type() {
            return this.chatType;
        }
        
        @NotNull
        @Override
        public Component name() {
            return this.name;
        }
        
        @Nullable
        @Override
        public Component target() {
            return this.target;
        }
        
        @Override
        public String toString() {
            return Internals.toString(this);
        }
    }
}
