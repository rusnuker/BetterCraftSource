// 
// Decompiled by Procyon v0.6.0
// 

package com.viaversion.viaversion.protocols.protocol1_13to1_12_2.blockconnections;

import java.util.Arrays;
import com.google.common.base.Preconditions;
import com.viaversion.viaversion.api.minecraft.BlockFace;
import com.viaversion.viaversion.libs.fastutil.ints.Int2ObjectArrayMap;
import com.viaversion.viaversion.libs.fastutil.ints.Int2ObjectMap;
import java.util.List;

public final class BlockData
{
    private static final List<String> CONNECTION_TYPES;
    private static final int MAGIC_STAIRS_ID;
    private final Int2ObjectMap<boolean[]> connectData;
    
    public BlockData() {
        this.connectData = new Int2ObjectArrayMap<boolean[]>();
    }
    
    public void put(final int blockConnectionTypeId, final boolean[] booleans) {
        this.connectData.put(blockConnectionTypeId, booleans);
    }
    
    public boolean connectsTo(final int blockConnectionTypeId, final BlockFace face, final boolean pre1_12AbstractFence) {
        if (pre1_12AbstractFence && this.connectData.containsKey(BlockData.MAGIC_STAIRS_ID)) {
            return false;
        }
        final boolean[] booleans = this.connectData.get(blockConnectionTypeId);
        return booleans != null && booleans[face.ordinal()];
    }
    
    public static int connectionTypeId(final String blockConnection) {
        final int connectionTypeId = BlockData.CONNECTION_TYPES.indexOf(blockConnection);
        Preconditions.checkArgument(connectionTypeId != -1, (Object)("Unknown connection type: " + blockConnection));
        return connectionTypeId;
    }
    
    static {
        CONNECTION_TYPES = Arrays.asList("fence", "netherFence", "pane", "cobbleWall", "redstone", "allFalseIfStairPre1_12");
        MAGIC_STAIRS_ID = connectionTypeId("allFalseIfStairPre1_12");
    }
}
