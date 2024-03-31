// 
// Decompiled by Procyon v0.6.0
// 

package com.TominoCZ.FBP.model;

import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.VertexFormatElement;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.Minecraft;
import net.minecraft.block.state.IBlockState;

public class FBPModelHelper
{
    static int vertexes;
    static boolean isAllCorruptedTexture;
    
    static {
        FBPModelHelper.vertexes = 0;
        FBPModelHelper.isAllCorruptedTexture = true;
    }
    
    public static boolean isModelValid(final IBlockState state) {
        final IBakedModel model = Minecraft.getMinecraft().getBlockRendererDispatcher().getBlockModelShapes().getModelForState(state);
        final TextureAtlasSprite s = model.getParticleTexture();
        if (s == null || s.getIconName().equals("missingno")) {
            return false;
        }
        FBPModelHelper.vertexes = 0;
        try {
            FBPModelTransformer.transform(model, state, 0L, new FBPModelTransformer.IVertexTransformer() {
                @Override
                public float[] transform(final BakedQuad quad, final VertexFormatElement element, final float... data) {
                    if (element.getUsage() == VertexFormatElement.EnumUsage.POSITION) {
                        ++FBPModelHelper.vertexes;
                    }
                    final TextureAtlasSprite s = quad.getSprite();
                    if (s != null && !s.getIconName().equals("missingno")) {
                        FBPModelHelper.isAllCorruptedTexture = false;
                    }
                    return data;
                }
            });
        }
        catch (final Throwable t) {}
        return FBPModelHelper.vertexes >= 3 && !FBPModelHelper.isAllCorruptedTexture;
    }
}
