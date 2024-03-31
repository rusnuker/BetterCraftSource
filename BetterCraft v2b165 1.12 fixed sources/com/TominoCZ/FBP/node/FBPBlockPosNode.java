// 
// Decompiled by Procyon v0.6.0
// 

package com.TominoCZ.FBP.node;

import net.minecraft.util.math.BlockPos;
import io.netty.util.internal.ConcurrentSet;

public class FBPBlockPosNode
{
    ConcurrentSet<BlockPos> possible;
    public boolean checked;
    
    public FBPBlockPosNode() {
        this.possible = new ConcurrentSet<BlockPos>();
        this.checked = false;
    }
    
    public void add(final BlockPos pos) {
        this.possible.add(pos);
    }
    
    public boolean hasPos(final BlockPos p1) {
        return this.possible.contains(p1);
    }
}
