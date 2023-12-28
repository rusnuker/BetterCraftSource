// 
// Decompiled by Procyon v0.6.0
// 

package net.minecraftforge.client.model.pipeline;

import net.minecraft.util.math.BlockPos;
import net.minecraft.block.state.IBlockState;
import net.minecraft.world.IBlockAccess;
import net.minecraft.util.EnumFacing;
import net.minecraft.client.renderer.EntityRenderer;
import javax.vecmath.Tuple3f;
import javax.vecmath.Vector3f;
import java.util.Objects;
import net.minecraft.client.renderer.color.BlockColors;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.client.renderer.vertex.VertexFormatElement;

public class VertexLighterFlat extends QuadGatheringTransformer
{
    protected static final VertexFormatElement NORMAL_4F;
    protected final BlockInfo blockInfo;
    private int tint;
    private boolean diffuse;
    protected int posIndex;
    protected int normalIndex;
    protected int colorIndex;
    protected int lightmapIndex;
    protected VertexFormat baseFormat;
    private static final VertexFormat BLOCK_WITH_NORMAL;
    
    static {
        NORMAL_4F = new VertexFormatElement(0, VertexFormatElement.EnumType.FLOAT, VertexFormatElement.EnumUsage.NORMAL, 4);
        BLOCK_WITH_NORMAL = withNormalUncached(DefaultVertexFormats.BLOCK);
    }
    
    public VertexLighterFlat(final BlockColors colors) {
        this.tint = -1;
        this.diffuse = true;
        this.posIndex = -1;
        this.normalIndex = -1;
        this.colorIndex = -1;
        this.lightmapIndex = -1;
        this.blockInfo = new BlockInfo(colors);
    }
    
    @Override
    public void setParent(final IVertexConsumer parent) {
        super.setParent(parent);
        this.setVertexFormat(parent.getVertexFormat());
    }
    
    private void updateIndices() {
        for (int i = 0; i < this.getVertexFormat().getElementCount(); ++i) {
            switch (this.getVertexFormat().getElement(i).getUsage()) {
                case POSITION: {
                    this.posIndex = i;
                    break;
                }
                case NORMAL: {
                    this.normalIndex = i;
                    break;
                }
                case COLOR: {
                    this.colorIndex = i;
                    break;
                }
                case UV: {
                    if (this.getVertexFormat().getElement(i).getIndex() == 1) {
                        this.lightmapIndex = i;
                        break;
                    }
                    break;
                }
            }
        }
        if (this.posIndex == -1) {
            throw new IllegalArgumentException("vertex lighter needs format with position");
        }
        if (this.lightmapIndex == -1) {
            throw new IllegalArgumentException("vertex lighter needs format with lightmap");
        }
        if (this.colorIndex == -1) {
            throw new IllegalArgumentException("vertex lighter needs format with color");
        }
    }
    
    @Override
    public void setVertexFormat(final VertexFormat format) {
        if (Objects.equals(format, this.baseFormat)) {
            return;
        }
        this.baseFormat = format;
        super.setVertexFormat(withNormal(format));
        this.updateIndices();
    }
    
    static VertexFormat withNormal(final VertexFormat format) {
        if (format == DefaultVertexFormats.BLOCK) {
            return VertexLighterFlat.BLOCK_WITH_NORMAL;
        }
        return withNormalUncached(format);
    }
    
    private static VertexFormat withNormalUncached(final VertexFormat format) {
        if (format == null || format.hasNormal()) {
            return format;
        }
        return new VertexFormat(format).addElement(VertexLighterFlat.NORMAL_4F);
    }
    
    @Override
    protected void processQuad() {
        final float[][] position = this.quadData[this.posIndex];
        float[][] normal = null;
        final float[][] lightmap = this.quadData[this.lightmapIndex];
        final float[][] color = this.quadData[this.colorIndex];
        if (this.dataLength[this.normalIndex] >= 3 && (this.quadData[this.normalIndex][0][0] != -1.0f || this.quadData[this.normalIndex][0][1] != -1.0f || this.quadData[this.normalIndex][0][2] != -1.0f)) {
            normal = this.quadData[this.normalIndex];
        }
        else {
            normal = new float[4][4];
            final Vector3f v1 = new Vector3f(position[3]);
            final Vector3f t = new Vector3f(position[1]);
            final Vector3f v2 = new Vector3f(position[2]);
            v1.sub(t);
            t.set(position[0]);
            v2.sub(t);
            v1.cross(v2, v1);
            v1.normalize();
            for (int v3 = 0; v3 < 4; ++v3) {
                normal[v3][0] = v1.x;
                normal[v3][1] = v1.y;
                normal[v3][2] = v1.z;
                normal[v3][3] = 0.0f;
            }
        }
        int multiplier = -1;
        if (this.tint != -1) {
            multiplier = this.blockInfo.getColorMultiplier(this.tint);
        }
        final VertexFormat format = this.parent.getVertexFormat();
        final int count = format.getElementCount();
        for (int v3 = 0; v3 < 4; ++v3) {
            final float[] array = position[v3];
            final int n = 0;
            array[n] += this.blockInfo.getShx();
            final float[] array2 = position[v3];
            final int n2 = 1;
            array2[n2] += this.blockInfo.getShy();
            final float[] array3 = position[v3];
            final int n3 = 2;
            array3[n3] += this.blockInfo.getShz();
            float x = position[v3][0] - 0.5f;
            float y = position[v3][1] - 0.5f;
            float z = position[v3][2] - 0.5f;
            x += normal[v3][0] * 0.5f;
            y += normal[v3][1] * 0.5f;
            z += normal[v3][2] * 0.5f;
            final float blockLight = lightmap[v3][0];
            final float skyLight = lightmap[v3][1];
            this.updateLightmap(normal[v3], lightmap[v3], x, y, z);
            if (this.dataLength[this.lightmapIndex] > 1) {
                if (blockLight > lightmap[v3][0]) {
                    lightmap[v3][0] = blockLight;
                }
                if (skyLight > lightmap[v3][1]) {
                    lightmap[v3][1] = skyLight;
                }
            }
            this.updateColor(normal[v3], color[v3], x, y, z, (float)this.tint, multiplier);
            if (this.diffuse) {
                final float d = LightUtil.diffuseLight(normal[v3][0], normal[v3][1], normal[v3][2]);
                for (int i = 0; i < 3; ++i) {
                    final float[] array4 = color[v3];
                    final int n4 = i;
                    array4[n4] *= d;
                }
            }
            if (EntityRenderer.anaglyphEnable) {
                this.applyAnaglyph(color[v3]);
            }
            for (int e = 0; e < count; ++e) {
                final VertexFormatElement element = format.getElement(e);
                switch (element.getUsage()) {
                    case POSITION: {
                        this.parent.put(e, position[v3]);
                        continue;
                    }
                    case NORMAL: {
                        this.parent.put(e, normal[v3]);
                        continue;
                    }
                    case COLOR: {
                        this.parent.put(e, color[v3]);
                        continue;
                    }
                    case UV: {
                        if (element.getIndex() == 1) {
                            this.parent.put(e, lightmap[v3]);
                            continue;
                        }
                        break;
                    }
                }
                this.parent.put(e, this.quadData[e][v3]);
            }
        }
        this.tint = -1;
    }
    
    protected void applyAnaglyph(final float[] color) {
        final float r = color[0];
        color[0] = (r * 30.0f + color[1] * 59.0f + color[2] * 11.0f) / 100.0f;
        color[1] = (r * 3.0f + color[1] * 7.0f) / 10.0f;
        color[2] = (r * 3.0f + color[2] * 7.0f) / 10.0f;
    }
    
    protected void updateLightmap(final float[] normal, final float[] lightmap, final float x, final float y, final float z) {
        final float e1 = 0.99f;
        final float e2 = 0.95f;
        final boolean full = this.blockInfo.isFullCube();
        EnumFacing side = null;
        if ((full || y < -0.99f) && normal[1] < -0.95f) {
            side = EnumFacing.DOWN;
        }
        else if ((full || y > 0.99f) && normal[1] > 0.95f) {
            side = EnumFacing.UP;
        }
        else if ((full || z < -0.99f) && normal[2] < -0.95f) {
            side = EnumFacing.NORTH;
        }
        else if ((full || z > 0.99f) && normal[2] > 0.95f) {
            side = EnumFacing.SOUTH;
        }
        else if ((full || x < -0.99f) && normal[0] < -0.95f) {
            side = EnumFacing.WEST;
        }
        else if ((full || x > 0.99f) && normal[0] > 0.95f) {
            side = EnumFacing.EAST;
        }
        final int i = (side == null) ? 0 : (side.ordinal() + 1);
        final int brightness = this.blockInfo.getPackedLight()[i];
        lightmap[0] = (brightness >> 4 & 0xF) * 32.0f / 65535.0f;
        lightmap[1] = (brightness >> 20 & 0xF) * 32.0f / 65535.0f;
    }
    
    protected void updateColor(final float[] normal, final float[] color, final float x, final float y, final float z, final float tint, final int multiplier) {
        if (tint != -1.0f) {
            final int n = 0;
            color[n] *= (multiplier >> 16 & 0xFF) / 255.0f;
            final int n2 = 1;
            color[n2] *= (multiplier >> 8 & 0xFF) / 255.0f;
            final int n3 = 2;
            color[n3] *= (multiplier & 0xFF) / 255.0f;
        }
    }
    
    @Override
    public void setQuadTint(final int tint) {
        this.tint = tint;
    }
    
    @Override
    public void setQuadOrientation(final EnumFacing orientation) {
    }
    
    public void setQuadCulled() {
    }
    
    public void setWorld(final IBlockAccess world) {
        this.blockInfo.setWorld(world);
    }
    
    public void setState(final IBlockState state) {
        this.blockInfo.setState(state);
    }
    
    public void setBlockPos(final BlockPos blockPos) {
        this.blockInfo.setBlockPos(blockPos);
    }
    
    public void resetBlockInfo() {
        this.blockInfo.reset();
    }
    
    public void updateBlockInfo() {
        this.blockInfo.updateShift();
        this.blockInfo.updateFlatLighting();
    }
    
    @Override
    public void setQuadColored() {
    }
}
