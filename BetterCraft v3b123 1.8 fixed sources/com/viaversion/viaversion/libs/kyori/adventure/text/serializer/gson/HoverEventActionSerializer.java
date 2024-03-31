// 
// Decompiled by Procyon v0.6.0
// 

package com.viaversion.viaversion.libs.kyori.adventure.text.serializer.gson;

import com.viaversion.viaversion.libs.kyori.adventure.text.event.HoverEvent;
import com.viaversion.viaversion.libs.gson.TypeAdapter;

final class HoverEventActionSerializer
{
    static final TypeAdapter<HoverEvent.Action<?>> INSTANCE;
    
    private HoverEventActionSerializer() {
    }
    
    static {
        INSTANCE = IndexedSerializer.lenient("hover action", HoverEvent.Action.NAMES);
    }
}
