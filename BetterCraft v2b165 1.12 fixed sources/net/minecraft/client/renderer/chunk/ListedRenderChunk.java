// 
// Decompiled by Procyon v0.6.0
// 

package net.minecraft.client.renderer.chunk;

import net.minecraft.client.renderer.GLAllocation;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.world.World;

public class ListedRenderChunk extends RenderChunk
{
    private final int baseDisplayList;
    
    public ListedRenderChunk(final World p_i47121_1_, final RenderGlobal p_i47121_2_, final int p_i47121_3_) {
        super(p_i47121_1_, p_i47121_2_, p_i47121_3_);
        this.baseDisplayList = GLAllocation.generateDisplayLists(BlockRenderLayer.values().length);
    }
    
    public int getDisplayList(final BlockRenderLayer layer, final CompiledChunk p_178600_2_) {
        return p_178600_2_.isLayerEmpty(layer) ? -1 : (this.baseDisplayList + layer.ordinal());
    }
    
    @Override
    public void deleteGlResources() {
        super.deleteGlResources();
        GLAllocation.deleteDisplayLists(this.baseDisplayList, BlockRenderLayer.values().length);
    }
}
