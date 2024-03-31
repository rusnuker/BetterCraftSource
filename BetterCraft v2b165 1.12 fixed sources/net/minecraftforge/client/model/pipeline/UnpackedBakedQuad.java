// 
// Decompiled by Procyon v0.6.0
// 

package net.minecraftforge.client.model.pipeline;

import net.minecraft.client.renderer.vertex.VertexFormatElement;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.EnumFacing;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.client.renderer.block.model.BakedQuad;

public class UnpackedBakedQuad extends BakedQuad
{
    protected final float[][][] unpackedData;
    protected final VertexFormat format;
    protected boolean packed;
    
    public UnpackedBakedQuad(final float[][][] unpackedData, final int tint, final EnumFacing orientation, final TextureAtlasSprite texture, final boolean applyDiffuseLighting, final VertexFormat format) {
        super(new int[format.getNextOffset()], tint, orientation, texture, applyDiffuseLighting, format);
        this.packed = false;
        this.unpackedData = unpackedData;
        this.format = format;
    }
    
    @Override
    public int[] getVertexData() {
        if (!this.packed) {
            this.packed = true;
            for (int v = 0; v < 4; ++v) {
                for (int e = 0; e < this.format.getElementCount(); ++e) {
                    LightUtil.pack(this.unpackedData[v][e], this.vertexData, this.format, v, e);
                }
            }
        }
        return this.vertexData;
    }
    
    @Override
    public void pipe(final IVertexConsumer consumer) {
        final int[] eMap = LightUtil.mapFormats(consumer.getVertexFormat(), this.format);
        if (this.hasTintIndex()) {
            consumer.setQuadTint(this.getTintIndex());
        }
        consumer.setQuadOrientation(this.getFace());
        for (int v = 0; v < 4; ++v) {
            for (int e = 0; e < consumer.getVertexFormat().getElementCount(); ++e) {
                if (eMap[e] != this.format.getElementCount()) {
                    consumer.put(e, this.unpackedData[v][eMap[e]]);
                }
                else {
                    consumer.put(e, new float[0]);
                }
            }
        }
    }
    
    public static class Builder implements IVertexConsumer
    {
        private final VertexFormat format;
        private final float[][][] unpackedData;
        private int tint;
        private EnumFacing orientation;
        private TextureAtlasSprite texture;
        private boolean applyDiffuseLighting;
        private int vertices;
        private int elements;
        private boolean full;
        private boolean contractUVs;
        private final float eps = 0.00390625f;
        
        public Builder(final VertexFormat format) {
            this.tint = -1;
            this.applyDiffuseLighting = true;
            this.vertices = 0;
            this.elements = 0;
            this.full = false;
            this.contractUVs = false;
            this.format = format;
            this.unpackedData = new float[4][format.getElementCount()][4];
        }
        
        @Override
        public VertexFormat getVertexFormat() {
            return this.format;
        }
        
        public void setContractUVs(final boolean value) {
            this.contractUVs = value;
        }
        
        @Override
        public void setQuadTint(final int tint) {
            this.tint = tint;
        }
        
        @Override
        public void setQuadOrientation(final EnumFacing orientation) {
            this.orientation = orientation;
        }
        
        @Override
        public void put(final int element, final float... data) {
            for (int i = 0; i < 4; ++i) {
                if (i < data.length) {
                    this.unpackedData[this.vertices][element][i] = data[i];
                }
                else {
                    this.unpackedData[this.vertices][element][i] = 0.0f;
                }
            }
            ++this.elements;
            if (this.elements == this.format.getElementCount()) {
                ++this.vertices;
                this.elements = 0;
            }
            if (this.vertices == 4) {
                this.full = true;
            }
        }
        
        public UnpackedBakedQuad build() {
            if (!this.full) {
                throw new IllegalStateException("not enough data");
            }
            if (this.texture == null) {
                throw new IllegalStateException("texture not set");
            }
            if (this.contractUVs) {
                final float tX = this.texture.getIconWidth() / (this.texture.getMaxU() - this.texture.getMinU());
                final float tY = this.texture.getIconHeight() / (this.texture.getMaxV() - this.texture.getMinV());
                final float tS = (tX > tY) ? tX : tY;
                final float ep = 1.0f / (tS * 256.0f);
                int uve;
                for (uve = 0; uve < this.format.getElementCount(); ++uve) {
                    final VertexFormatElement e = this.format.getElement(uve);
                    if (e.getUsage() == VertexFormatElement.EnumUsage.UV && e.getIndex() == 0) {
                        break;
                    }
                }
                if (uve == this.format.getElementCount()) {
                    throw new IllegalStateException("Can't contract UVs: format doesn't contain UVs");
                }
                final float[] uvc = new float[4];
                for (int v = 0; v < 4; ++v) {
                    for (int i = 0; i < 4; ++i) {
                        final float[] array = uvc;
                        final int n = i;
                        array[n] += this.unpackedData[v][uve][i] / 4.0f;
                    }
                }
                for (int v = 0; v < 4; ++v) {
                    for (int i = 0; i < 4; ++i) {
                        final float uo = this.unpackedData[v][uve][i];
                        float un = uo * 0.99609375f + uvc[i] * 0.00390625f;
                        float aud;
                        final float ud = aud = uo - un;
                        if (aud < 0.0f) {
                            aud = -aud;
                        }
                        if (aud < ep) {
                            float udc = uo - uvc[i];
                            if (udc < 0.0f) {
                                udc = -udc;
                            }
                            if (udc < 2.0f * ep) {
                                un = (uo + uvc[i]) / 2.0f;
                            }
                            else {
                                un = uo + ((ud < 0.0f) ? ep : (-ep));
                            }
                        }
                        this.unpackedData[v][uve][i] = un;
                    }
                }
            }
            return new UnpackedBakedQuad(this.unpackedData, this.tint, this.orientation, this.texture, this.applyDiffuseLighting, this.format);
        }
        
        @Override
        public void setQuadColored() {
        }
    }
}
