// 
// Decompiled by Procyon v0.6.0
// 

package net.minecraftforge.client.model.pipeline;

import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.Vec3i;
import net.minecraft.client.renderer.vertex.VertexFormatElement;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.util.math.BlockPos;
import net.minecraft.client.renderer.BufferBuilder;

public class VertexBufferConsumer implements IVertexConsumer
{
    private static final float[] dummyColor;
    private BufferBuilder renderer;
    private int[] quadData;
    private int v;
    private BlockPos offset;
    
    static {
        dummyColor = new float[] { 1.0f, 1.0f, 1.0f, 1.0f };
    }
    
    public VertexBufferConsumer() {
        this.v = 0;
        this.offset = BlockPos.ORIGIN;
    }
    
    public VertexBufferConsumer(final BufferBuilder buffer) {
        this.v = 0;
        this.offset = BlockPos.ORIGIN;
        this.setBuffer(buffer);
    }
    
    @Override
    public VertexFormat getVertexFormat() {
        return this.renderer.getVertexFormat();
    }
    
    @Override
    public void put(final int e, float... data) {
        final VertexFormat format = this.getVertexFormat();
        if (this.renderer.isColorDisabled() && format.getElement(e).getUsage() == VertexFormatElement.EnumUsage.COLOR) {
            data = VertexBufferConsumer.dummyColor;
        }
        LightUtil.pack(data, this.quadData, format, this.v, e);
        if (e == format.getElementCount() - 1) {
            ++this.v;
            if (this.v == 4) {
                this.renderer.addVertexData(this.quadData);
                this.renderer.putPosition(this.offset.getX(), this.offset.getY(), this.offset.getZ());
                this.v = 0;
            }
        }
    }
    
    private void checkVertexFormat() {
        if (this.quadData == null || this.renderer.getVertexFormat().getNextOffset() != this.quadData.length) {
            this.quadData = new int[this.renderer.getVertexFormat().getNextOffset()];
        }
    }
    
    public void setBuffer(final BufferBuilder buffer) {
        this.renderer = buffer;
        this.checkVertexFormat();
    }
    
    public void setOffset(final BlockPos offset) {
        this.offset = new BlockPos(offset);
    }
    
    @Override
    public void setQuadTint(final int tint) {
    }
    
    @Override
    public void setQuadOrientation(final EnumFacing orientation) {
    }
    
    @Override
    public void setQuadColored() {
    }
}
