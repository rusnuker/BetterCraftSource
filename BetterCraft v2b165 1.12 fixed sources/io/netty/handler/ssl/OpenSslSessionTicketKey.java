// 
// Decompiled by Procyon v0.6.0
// 

package io.netty.handler.ssl;

import io.netty.internal.tcnative.SessionTicketKey;

public final class OpenSslSessionTicketKey
{
    public static final int NAME_SIZE = 16;
    public static final int HMAC_KEY_SIZE = 16;
    public static final int AES_KEY_SIZE = 16;
    public static final int TICKET_KEY_SIZE = 48;
    final SessionTicketKey key;
    
    public OpenSslSessionTicketKey(final byte[] name, final byte[] hmacKey, final byte[] aesKey) {
        this.key = new SessionTicketKey(name.clone(), hmacKey.clone(), aesKey.clone());
    }
    
    public byte[] name() {
        return this.key.getName().clone();
    }
    
    public byte[] hmacKey() {
        return this.key.getHmacKey().clone();
    }
    
    public byte[] aesKey() {
        return this.key.getAesKey().clone();
    }
}
