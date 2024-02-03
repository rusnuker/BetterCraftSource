// 
// Decompiled by Procyon v0.6.0
// 

package com.viaversion.viaversion.libs.kyori.adventure.chat;

import com.viaversion.viaversion.libs.kyori.adventure.identity.Identity;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.NotNull;
import com.viaversion.viaversion.libs.kyori.adventure.text.Component;
import java.time.Instant;
import java.security.SecureRandom;

final class SignedMessageImpl implements SignedMessage
{
    static final SecureRandom RANDOM;
    private final Instant instant;
    private final long salt;
    private final String message;
    private final Component unsignedContent;
    
    SignedMessageImpl(final String message, final Component unsignedContent) {
        this.instant = Instant.now();
        this.salt = SignedMessageImpl.RANDOM.nextLong();
        this.message = message;
        this.unsignedContent = unsignedContent;
    }
    
    @NotNull
    @Override
    public Instant timestamp() {
        return this.instant;
    }
    
    @Override
    public long salt() {
        return this.salt;
    }
    
    @Override
    public Signature signature() {
        return null;
    }
    
    @Nullable
    @Override
    public Component unsignedContent() {
        return this.unsignedContent;
    }
    
    @NotNull
    @Override
    public String message() {
        return this.message;
    }
    
    @NotNull
    @Override
    public Identity identity() {
        return Identity.nil();
    }
    
    static {
        RANDOM = new SecureRandom();
    }
    
    static final class SignatureImpl implements Signature
    {
        final byte[] signature;
        
        SignatureImpl(final byte[] signature) {
            this.signature = signature;
        }
        
        @Override
        public byte[] bytes() {
            return this.signature;
        }
    }
}
