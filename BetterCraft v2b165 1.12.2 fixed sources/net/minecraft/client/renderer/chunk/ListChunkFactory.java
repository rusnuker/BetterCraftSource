// 
// Decompiled by Procyon v0.6.0
// 

package net.minecraft.client.renderer.chunk;

import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.world.World;

public class ListChunkFactory implements IRenderChunkFactory
{
    @Override
    public RenderChunk create(final World worldIn, final RenderGlobal p_189565_2_, final int p_189565_3_) {
        return new ListedRenderChunk(worldIn, p_189565_2_, p_189565_3_);
    }
}
