// 
// Decompiled by Procyon v0.6.0
// 

package com.viaversion.viaversion.libs.gson;

import java.io.IOException;
import com.viaversion.viaversion.libs.gson.stream.JsonReader;

public interface ToNumberStrategy
{
    Number readNumber(final JsonReader p0) throws IOException;
}
