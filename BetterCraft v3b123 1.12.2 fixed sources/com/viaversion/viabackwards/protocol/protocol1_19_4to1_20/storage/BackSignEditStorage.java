// 
// Decompiled by Procyon v0.6.0
// 

package com.viaversion.viabackwards.protocol.protocol1_19_4to1_20.storage;

import com.viaversion.viaversion.api.minecraft.Position;
import com.viaversion.viaversion.api.connection.StorableObject;

public final class BackSignEditStorage implements StorableObject
{
    private final Position position;
    
    public BackSignEditStorage(final Position position) {
        this.position = position;
    }
    
    public Position position() {
        return this.position;
    }
}
