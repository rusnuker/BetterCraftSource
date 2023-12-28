// 
// Decompiled by Procyon v0.6.0
// 

package com.TominoCZ.FBP.node;

import com.TominoCZ.FBP.particle.FBPParticleBlock;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;

public class FBPBlockNode
{
    public IBlockState state;
    public Block originalBlock;
    public int meta;
    public FBPParticleBlock particle;
    
    public FBPBlockNode(final IBlockState s, final FBPParticleBlock p) {
        this.particle = p;
        this.state = s;
        this.originalBlock = s.getBlock();
        this.meta = this.originalBlock.getMetaFromState(s);
    }
}
