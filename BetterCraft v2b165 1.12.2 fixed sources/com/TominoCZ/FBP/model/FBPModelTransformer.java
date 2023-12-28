// 
// Decompiled by Procyon v0.6.0
// 

package com.TominoCZ.FBP.model;

import net.minecraft.client.renderer.vertex.VertexFormatElement;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraftforge.client.model.pipeline.LightUtil;
import net.minecraftforge.client.model.pipeline.IVertexConsumer;
import net.minecraftforge.client.model.pipeline.UnpackedBakedQuad;
import java.util.Iterator;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.util.EnumFacing;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.IBakedModel;

public final class FBPModelTransformer
{
    public static FBPSimpleBakedModel transform(final IBakedModel model, final IBlockState state, final long rand, final IVertexTransformer transformer) {
        try {
            final FBPSimpleBakedModel out = new FBPSimpleBakedModel(model);
            for (int i = 0; i <= 6; ++i) {
                final EnumFacing side = (i == 6) ? null : EnumFacing.getFront(i);
                for (final BakedQuad quad : model.getQuads(state, side, rand)) {
                    out.addQuad(side, transform(quad, transformer));
                }
            }
            return out;
        }
        catch (final Throwable t) {
            return null;
        }
    }
    
    private static BakedQuad transform(final BakedQuad quad, final IVertexTransformer transformer) {
        final VertexFormat format = quad.getFormat();
        final UnpackedBakedQuad.Builder builder = new UnpackedBakedQuad.Builder(format);
        LightUtil.putBakedQuad(new VertexTransformerWrapper(builder, quad, transformer), quad);
        return builder.build();
    }
    
    private static final class VertexTransformerWrapper implements IVertexConsumer
    {
        private final IVertexConsumer parent;
        private final BakedQuad parentQuad;
        private final VertexFormat format;
        private final IVertexTransformer transformer;
        
        public VertexTransformerWrapper(final IVertexConsumer parent, final BakedQuad parentQuad, final IVertexTransformer transformer) {
            this.parent = parent;
            this.parentQuad = parentQuad;
            this.format = parent.getVertexFormat();
            this.transformer = transformer;
        }
        
        @Override
        public VertexFormat getVertexFormat() {
            return this.format;
        }
        
        @Override
        public void setQuadTint(final int tint) {
            this.parent.setQuadTint(tint);
        }
        
        @Override
        public void setQuadOrientation(final EnumFacing orientation) {
            this.parent.setQuadOrientation(orientation);
        }
        
        @Override
        public void put(final int elementId, final float... data) {
            final VertexFormatElement element = this.format.getElement(elementId);
            this.parent.put(elementId, this.transformer.transform(this.parentQuad, element, data));
        }
        
        @Override
        public void setQuadColored() {
            this.parent.setQuadColored();
        }
    }
    
    public interface IVertexTransformer
    {
        float[] transform(final BakedQuad p0, final VertexFormatElement p1, final float... p2);
    }
}
