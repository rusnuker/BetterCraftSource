// 
// Decompiled by Procyon v0.6.0
// 

package net.minecraftforge.client.model.pipeline;

import net.minecraft.util.EnumFacing;
import net.minecraft.client.renderer.vertex.VertexFormat;

public class VertexTransformer implements IVertexConsumer
{
    protected final IVertexConsumer parent;
    
    public VertexTransformer(final IVertexConsumer parent) {
        this.parent = parent;
    }
    
    @Override
    public VertexFormat getVertexFormat() {
        return this.parent.getVertexFormat();
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
    public void put(final int element, final float... data) {
        this.parent.put(element, data);
    }
    
    @Override
    public void setQuadColored() {
        this.parent.setQuadColored();
    }
}
