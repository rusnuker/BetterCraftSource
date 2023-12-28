// 
// Decompiled by Procyon v0.6.0
// 

package com.TominoCZ.FBP.util;

import net.minecraft.util.math.BlockPos;
import net.minecraft.util.EnumFacing;
import org.lwjgl.util.vector.Vector3f;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.GlStateManager;
import com.TominoCZ.FBP.FBP;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.Tessellator;
import com.TominoCZ.FBP.vector.FBPVector3d;
import net.minecraft.util.math.Vec2f;
import net.minecraft.client.renderer.BufferBuilder;

public class FBPRenderUtil
{
    public static void renderCubeShaded_S(final BufferBuilder buf, final Vec2f[] par, final float f5, final float f6, final float f7, final double scale, final FBPVector3d rotVec, final int j, final int k, final float r, final float g, final float b, final float a, final boolean cartoon) {
        Tessellator.getInstance().draw();
        Minecraft.getMinecraft().getRenderManager().renderEngine.bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
        buf.begin(7, FBP.POSITION_TEX_COLOR_LMAP_NORMAL);
        Tessellator.getInstance().draw();
        Minecraft.getMinecraft().getRenderManager().renderEngine.bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
        buf.begin(7, FBP.POSITION_TEX_COLOR_LMAP_NORMAL);
        GlStateManager.enableCull();
        RenderHelper.enableStandardItemLighting();
        buf.setTranslation(f5, f6, f7);
        putCube_S(buf, par, scale, rotVec, j, k, r, g, b, a, FBP.cartoonMode);
        buf.setTranslation(0.0, 0.0, 0.0);
        Tessellator.getInstance().draw();
        Minecraft.getMinecraft().getRenderManager().renderEngine.bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
        buf.begin(7, DefaultVertexFormats.PARTICLE_POSITION_TEX_COLOR_LMAP);
        RenderHelper.disableStandardItemLighting();
    }
    
    public static void renderCubeShaded_WH(final BufferBuilder buf, final Vec2f[] par, final float f5, final float f6, final float f7, final double width, final double height, final FBPVector3d rotVec, final int j, final int k, final float r, final float g, final float b, final float a, final boolean cartoon) {
        Tessellator.getInstance().draw();
        Minecraft.getMinecraft().getRenderManager().renderEngine.bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
        buf.begin(7, FBP.POSITION_TEX_COLOR_LMAP_NORMAL);
        Tessellator.getInstance().draw();
        Minecraft.getMinecraft().getRenderManager().renderEngine.bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
        buf.begin(7, FBP.POSITION_TEX_COLOR_LMAP_NORMAL);
        GlStateManager.enableCull();
        RenderHelper.enableStandardItemLighting();
        buf.setTranslation(f5, f6, f7);
        putCube_WH(buf, par, width, height, rotVec, j, k, r, g, b, a, FBP.cartoonMode);
        buf.setTranslation(0.0, 0.0, 0.0);
        Tessellator.getInstance().draw();
        Minecraft.getMinecraft().getRenderManager().renderEngine.bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
        buf.begin(7, DefaultVertexFormats.PARTICLE_POSITION_TEX_COLOR_LMAP);
        RenderHelper.disableStandardItemLighting();
    }
    
    static void putCube_S(final BufferBuilder worldRendererIn, final Vec2f[] par, final double scale, final FBPVector3d rotVec, final int j, final int k, final float r, final float g, final float b, final float a, final boolean cartoon) {
        final float radsX = (float)Math.toRadians(rotVec.x);
        final float radsY = (float)Math.toRadians(rotVec.y);
        final float radsZ = (float)Math.toRadians(rotVec.z);
        for (int i = 0; i < FBP.CUBE.length; i += 4) {
            Vec3d v1 = FBP.CUBE[i];
            Vec3d v2 = FBP.CUBE[i + 1];
            Vec3d v3 = FBP.CUBE[i + 2];
            Vec3d v4 = FBP.CUBE[i + 3];
            v1 = rotatef_d(v1, radsX, radsY, radsZ);
            v2 = rotatef_d(v2, radsX, radsY, radsZ);
            v3 = rotatef_d(v3, radsX, radsY, radsZ);
            v4 = rotatef_d(v4, radsX, radsY, radsZ);
            final Vec3d normal = rotatef_d(FBP.CUBE_NORMALS[i / 4], radsX, radsY, radsZ);
            if (!cartoon) {
                addVt_S(worldRendererIn, scale, v1, par[0].x, par[0].y, j, k, r, g, b, a, normal);
                addVt_S(worldRendererIn, scale, v2, par[1].x, par[1].y, j, k, r, g, b, a, normal);
                addVt_S(worldRendererIn, scale, v3, par[2].x, par[2].y, j, k, r, g, b, a, normal);
                addVt_S(worldRendererIn, scale, v4, par[3].x, par[3].y, j, k, r, g, b, a, normal);
            }
            else {
                addVt_S(worldRendererIn, scale, v1, par[0].x, par[0].y, j, k, r, g, b, a, normal);
                addVt_S(worldRendererIn, scale, v2, par[0].x, par[0].y, j, k, r, g, b, a, normal);
                addVt_S(worldRendererIn, scale, v3, par[0].x, par[0].y, j, k, r, g, b, a, normal);
                addVt_S(worldRendererIn, scale, v4, par[0].x, par[0].y, j, k, r, g, b, a, normal);
            }
        }
    }
    
    static void putCube_WH(final BufferBuilder worldRendererIn, final Vec2f[] par, final double width, final double height, final FBPVector3d rotVec, final int j, final int k, final float r, final float g, final float b, final float a, final boolean cartoon) {
        final float radsX = (float)Math.toRadians(rotVec.x);
        final float radsY = (float)Math.toRadians(rotVec.y);
        final float radsZ = (float)Math.toRadians(rotVec.z);
        for (int i = 0; i < FBP.CUBE.length; i += 4) {
            Vec3d v1 = FBP.CUBE[i];
            Vec3d v2 = FBP.CUBE[i + 1];
            Vec3d v3 = FBP.CUBE[i + 2];
            Vec3d v4 = FBP.CUBE[i + 3];
            v1 = rotatef_d(v1, radsX, radsY, radsZ);
            v2 = rotatef_d(v2, radsX, radsY, radsZ);
            v3 = rotatef_d(v3, radsX, radsY, radsZ);
            v4 = rotatef_d(v4, radsX, radsY, radsZ);
            final Vec3d normal = rotatef_d(FBP.CUBE_NORMALS[i / 4], radsX, radsY, radsZ);
            if (!cartoon) {
                addVt_WH(worldRendererIn, width, height, v1, par[0].x, par[0].y, j, k, r, g, b, a, normal);
                addVt_WH(worldRendererIn, width, height, v2, par[1].x, par[1].y, j, k, r, g, b, a, normal);
                addVt_WH(worldRendererIn, width, height, v3, par[2].x, par[2].y, j, k, r, g, b, a, normal);
                addVt_WH(worldRendererIn, width, height, v4, par[3].x, par[3].y, j, k, r, g, b, a, normal);
            }
            else {
                addVt_WH(worldRendererIn, width, height, v1, par[0].x, par[0].y, j, k, r, g, b, a, normal);
                addVt_WH(worldRendererIn, width, height, v2, par[0].x, par[0].y, j, k, r, g, b, a, normal);
                addVt_WH(worldRendererIn, width, height, v3, par[0].x, par[0].y, j, k, r, g, b, a, normal);
                addVt_WH(worldRendererIn, width, height, v4, par[0].x, par[0].y, j, k, r, g, b, a, normal);
            }
        }
    }
    
    static void addVt_S(final BufferBuilder worldRendererIn, final double scale, final Vec3d pos, final double u, final double v, final int j, final int k, final float r, final float g, final float b, final float a, final Vec3d n) {
        worldRendererIn.pos(pos.xCoord * scale, pos.yCoord * scale, pos.zCoord * scale).tex(u, v).color(r, g, b, a).lightmap(j, k).normal((float)n.xCoord, (float)n.yCoord, (float)n.zCoord).endVertex();
    }
    
    static void addVt_WH(final BufferBuilder worldRendererIn, final double width, final double height, final Vec3d pos, final double u, final double v, final int j, final int k, final float r, final float g, final float b, final float a, final Vec3d n) {
        worldRendererIn.pos(pos.xCoord * width, pos.yCoord * height, pos.zCoord * width).tex(u, v).color(r, g, b, a).lightmap(j, k).normal((float)n.xCoord, (float)n.yCoord, (float)n.zCoord).endVertex();
    }
    
    public static Vec3d rotatef_d(Vec3d vec, final float AngleX, final float AngleY, final float AngleZ) {
        final FBPVector3d sin = new FBPVector3d(MathHelper.sin(AngleX), MathHelper.sin(AngleY), MathHelper.sin(AngleZ));
        final FBPVector3d cos = new FBPVector3d(MathHelper.cos(AngleX), MathHelper.cos(AngleY), MathHelper.cos(AngleZ));
        vec = new Vec3d(vec.xCoord, vec.yCoord * cos.x - vec.zCoord * sin.x, vec.yCoord * sin.x + vec.zCoord * cos.x);
        vec = new Vec3d(vec.xCoord * cos.z - vec.yCoord * sin.z, vec.xCoord * sin.z + vec.yCoord * cos.z, vec.zCoord);
        vec = new Vec3d(vec.xCoord * cos.y + vec.zCoord * sin.y, vec.yCoord, vec.xCoord * sin.y - vec.zCoord * cos.y);
        return vec;
    }
    
    public static Vector3f rotatef_f(final Vector3f pos, final float AngleX, final float AngleY, final float AngleZ, final EnumFacing facing) {
        final FBPVector3d sin = new FBPVector3d(MathHelper.sin(AngleX), MathHelper.sin(AngleY), MathHelper.sin(AngleZ));
        final FBPVector3d cos = new FBPVector3d(MathHelper.cos(AngleX), MathHelper.cos(AngleY), MathHelper.cos(AngleZ));
        final FBPVector3d pos2 = new FBPVector3d(pos.x, pos.y, pos.z);
        if (facing == EnumFacing.EAST) {
            final FBPVector3d fbpVector3d = pos2;
            --fbpVector3d.x;
        }
        else if (facing == EnumFacing.WEST) {
            final FBPVector3d fbpVector3d2 = pos2;
            ++fbpVector3d2.x;
        }
        else if (facing == EnumFacing.SOUTH) {
            final FBPVector3d fbpVector3d3 = pos2;
            --fbpVector3d3.z;
            final FBPVector3d fbpVector3d4 = pos2;
            --fbpVector3d4.x;
        }
        FBPVector3d pos3 = new FBPVector3d(pos2.x, pos2.y * cos.x - pos2.z * sin.x, pos2.y * sin.x + pos2.z * cos.x);
        pos3 = new FBPVector3d(pos3.x * cos.z - pos3.y * sin.z, pos3.x * sin.z + pos3.y * cos.z, pos3.z);
        pos3 = new FBPVector3d(pos3.x * cos.y + pos3.z * sin.y, pos3.y, pos3.x * sin.y - pos3.z * cos.y);
        if (facing == EnumFacing.EAST) {
            final FBPVector3d fbpVector3d5 = pos3;
            ++fbpVector3d5.x;
        }
        else if (facing == EnumFacing.WEST) {
            final FBPVector3d fbpVector3d6 = pos3;
            --fbpVector3d6.x;
        }
        else if (facing == EnumFacing.SOUTH) {
            final FBPVector3d fbpVector3d7 = pos3;
            ++fbpVector3d7.z;
            final FBPVector3d fbpVector3d8 = pos3;
            ++fbpVector3d8.x;
        }
        return new Vector3f((float)pos3.x, (float)pos3.y, (float)pos3.z);
    }
    
    public static void markBlockForRender(final BlockPos pos) {
        final BlockPos bp1 = pos.add(1, 1, 1);
        final BlockPos bp2 = pos.add(-1, -1, -1);
        Minecraft.getMinecraft().renderGlobal.markBlockRangeForRenderUpdate(bp1.getX(), bp1.getY(), bp1.getZ(), bp2.getX(), bp2.getY(), bp2.getZ());
    }
}
