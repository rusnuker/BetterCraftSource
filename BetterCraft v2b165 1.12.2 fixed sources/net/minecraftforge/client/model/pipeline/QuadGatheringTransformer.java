// 
// Decompiled by Procyon v0.6.0
// 

package net.minecraftforge.client.model.pipeline;

import net.minecraft.client.renderer.vertex.VertexFormat;

public abstract class QuadGatheringTransformer implements IVertexConsumer
{
    protected IVertexConsumer parent;
    protected VertexFormat format;
    protected int vertices;
    protected byte[] dataLength;
    protected float[][][] quadData;
    
    public QuadGatheringTransformer() {
        this.vertices = 0;
        this.dataLength = null;
        this.quadData = null;
    }
    
    public void setParent(final IVertexConsumer parent) {
        this.parent = parent;
    }
    
    public void setVertexFormat(final VertexFormat format) {
        this.format = format;
        this.dataLength = new byte[format.getElementCount()];
        this.quadData = new float[format.getElementCount()][4][4];
    }
    
    @Override
    public VertexFormat getVertexFormat() {
        return this.format;
    }
    
    @Override
    public void put(final int element, final float... data) {
        System.arraycopy(data, 0, this.quadData[element][this.vertices], 0, data.length);
        if (this.vertices == 0) {
            this.dataLength[element] = (byte)data.length;
        }
        if (element == this.getVertexFormat().getElementCount() - 1) {
            ++this.vertices;
        }
        if (this.vertices == 4) {
            this.vertices = 0;
            this.processQuad();
        }
    }
    
    protected abstract void processQuad();
}
