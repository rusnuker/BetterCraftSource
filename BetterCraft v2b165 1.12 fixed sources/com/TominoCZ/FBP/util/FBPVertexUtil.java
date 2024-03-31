// 
// Decompiled by Procyon v0.6.0
// 

package com.TominoCZ.FBP.util;

import java.util.Iterator;
import java.util.List;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.util.EnumFacing;
import org.lwjgl.util.vector.Vector3f;

public final class FBPVertexUtil
{
    public static float[] calculateUV(final Vector3f from, final Vector3f to, final EnumFacing facing1) {
        EnumFacing facing2 = facing1;
        if (facing2 == null) {
            if (from.y == to.y) {
                facing2 = EnumFacing.UP;
            }
            else if (from.x == to.x) {
                facing2 = EnumFacing.EAST;
            }
            else {
                if (from.z != to.z) {
                    return null;
                }
                facing2 = EnumFacing.SOUTH;
            }
        }
        switch (facing2) {
            case DOWN: {
                return new float[] { from.x, 16.0f - to.z, to.x, 16.0f - from.z };
            }
            case UP: {
                return new float[] { from.x, from.z, to.x, to.z };
            }
            case NORTH: {
                return new float[] { 16.0f - to.x, 16.0f - to.y, 16.0f - from.x, 16.0f - from.y };
            }
            case SOUTH: {
                return new float[] { from.x, 16.0f - to.y, to.x, 16.0f - from.y };
            }
            case WEST: {
                return new float[] { from.z, 16.0f - to.y, to.z, 16.0f - from.y };
            }
            case EAST: {
                return new float[] { 16.0f - to.z, 16.0f - to.y, 16.0f - from.z, 16.0f - from.y };
            }
            default: {
                return null;
            }
        }
    }
    
    public static BakedQuad clone(final BakedQuad quad) {
        return new BakedQuad(quad.getVertexData(), quad.getTintIndex(), quad.getFace(), quad.getSprite(), quad.shouldApplyDiffuseLighting(), quad.getFormat());
    }
    
    public static int multiplyColor(final int src, final int dst) {
        int out = 0;
        for (int i = 0; i < 32; i += 8) {
            out |= ((src >> i & 0xFF) * (dst >> i & 0xFF) / 255 & 0xFF) << i;
        }
        return out;
    }
    
    public static BakedQuad recolorQuad(final BakedQuad quad, final int color) {
        final int c = DefaultVertexFormats.BLOCK.getColorOffset() / 4;
        final int v = DefaultVertexFormats.BLOCK.getNextOffset() / 4;
        final int[] vertexData = quad.getVertexData();
        for (int i = 0; i < 4; ++i) {
            vertexData[v * i + c] = multiplyColor(vertexData[v * i + c], color);
        }
        return quad;
    }
    
    public static void addRecoloredQuads(final List<BakedQuad> src, final int color, final List<BakedQuad> target, final EnumFacing facing) {
        for (final BakedQuad quad : src) {
            final BakedQuad quad2 = clone(quad);
            final int c = DefaultVertexFormats.BLOCK.getColorOffset() / 4;
            final int v = DefaultVertexFormats.BLOCK.getNextOffset() / 4;
            final int[] vertexData = quad2.getVertexData();
            for (int i = 0; i < 4; ++i) {
                vertexData[v * i + c] = multiplyColor(vertexData[v * i + c], color);
            }
            target.add(quad2);
        }
    }
}
