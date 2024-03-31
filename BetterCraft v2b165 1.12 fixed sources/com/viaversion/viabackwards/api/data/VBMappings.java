// 
// Decompiled by Procyon v0.6.0
// 

package com.viaversion.viabackwards.api.data;

import java.util.Arrays;
import com.viaversion.viaversion.libs.gson.JsonArray;
import com.viaversion.viaversion.libs.gson.JsonObject;
import com.viaversion.viaversion.api.data.IntArrayMappings;

public class VBMappings extends IntArrayMappings
{
    public VBMappings(final int size, final JsonObject oldMapping, final JsonObject newMapping, final JsonObject diffMapping, final boolean warnOnMissing) {
        super(create(size, oldMapping, newMapping, diffMapping, warnOnMissing));
    }
    
    public VBMappings(final JsonObject oldMapping, final JsonObject newMapping, final JsonObject diffMapping, final boolean warnOnMissing) {
        super(create(oldMapping.entrySet().size(), oldMapping, newMapping, diffMapping, warnOnMissing));
    }
    
    public VBMappings(final JsonObject oldMapping, final JsonObject newMapping, final boolean warnOnMissing) {
        this(oldMapping, newMapping, null, warnOnMissing);
    }
    
    public VBMappings(final JsonArray oldMapping, final JsonArray newMapping, final JsonObject diffMapping, final boolean warnOnMissing) {
        super(oldMapping.size(), oldMapping, newMapping, diffMapping, warnOnMissing);
    }
    
    private static int[] create(final int size, final JsonObject oldMapping, final JsonObject newMapping, final JsonObject diffMapping, final boolean warnOnMissing) {
        final int[] oldToNew = new int[size];
        Arrays.fill(oldToNew, -1);
        VBMappingDataLoader.mapIdentifiers(oldToNew, oldMapping, newMapping, diffMapping, warnOnMissing);
        return oldToNew;
    }
}
