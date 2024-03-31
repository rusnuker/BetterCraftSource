// 
// Decompiled by Procyon v0.6.0
// 

package com.viaversion.viabackwards.protocol.protocol1_19_1to1_19_3.storage;

import java.util.UUID;
import com.viaversion.viaversion.api.connection.StorableObject;

public final class ChatSessionStorage implements StorableObject
{
    private final UUID uuid;
    
    public ChatSessionStorage() {
        this.uuid = UUID.randomUUID();
    }
    
    public UUID uuid() {
        return this.uuid;
    }
}
