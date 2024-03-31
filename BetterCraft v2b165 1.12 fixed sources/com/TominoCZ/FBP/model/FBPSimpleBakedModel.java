// 
// Decompiled by Procyon v0.6.0
// 

package com.TominoCZ.FBP.model;

import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.block.model.ItemOverrideList;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumFacing;
import java.util.ArrayList;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.block.model.BakedQuad;
import java.util.List;
import net.minecraft.client.renderer.block.model.IBakedModel;

public class FBPSimpleBakedModel implements IBakedModel
{
    private final List<BakedQuad>[] quads;
    private final IBakedModel parent;
    private TextureAtlasSprite particle;
    
    public FBPSimpleBakedModel() {
        this(null);
    }
    
    public FBPSimpleBakedModel(final IBakedModel parent) {
        this.quads = new List[7];
        this.parent = parent;
        for (int i = 0; i < this.quads.length; ++i) {
            this.quads[i] = new ArrayList<BakedQuad>();
        }
    }
    
    public void setParticle(final TextureAtlasSprite particle) {
        this.particle = particle;
    }
    
    public void addQuad(final EnumFacing side, final BakedQuad quad) {
        this.quads[(side == null) ? 6 : side.ordinal()].add(quad);
    }
    
    @Override
    public List<BakedQuad> getQuads(final IBlockState state, final EnumFacing side, final long rand) {
        return this.quads[(side == null) ? 6 : side.ordinal()];
    }
    
    @Override
    public boolean isAmbientOcclusion() {
        return this.parent == null || this.parent.isAmbientOcclusion();
    }
    
    @Override
    public boolean isGui3d() {
        return this.parent == null || this.parent.isGui3d();
    }
    
    @Override
    public boolean isBuiltInRenderer() {
        return false;
    }
    
    @Override
    public TextureAtlasSprite getParticleTexture() {
        if (this.particle != null) {
            return this.particle;
        }
        return (this.parent != null) ? this.parent.getParticleTexture() : null;
    }
    
    @Override
    public ItemOverrideList getOverrides() {
        return ItemOverrideList.NONE;
    }
    
    @Override
    public ItemCameraTransforms getItemCameraTransforms() {
        return this.parent.getItemCameraTransforms();
    }
}
