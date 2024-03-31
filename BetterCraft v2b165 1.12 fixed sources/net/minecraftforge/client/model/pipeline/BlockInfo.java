// 
// Decompiled by Procyon v0.6.0
// 

package net.minecraftforge.client.model.pipeline;

import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.BlockPos;
import net.minecraft.block.state.IBlockState;
import net.minecraft.world.IBlockAccess;
import net.minecraft.client.renderer.color.BlockColors;
import net.minecraft.util.EnumFacing;

public class BlockInfo
{
    private static final EnumFacing[] SIDES;
    private final BlockColors colors;
    private IBlockAccess world;
    private IBlockState state;
    private BlockPos blockPos;
    private final boolean[][][] t;
    private final int[][][] s;
    private final int[][][] b;
    private final float[][][][] skyLight;
    private final float[][][][] blockLight;
    private final float[][][] ao;
    private final int[] packed;
    private boolean full;
    private float shx;
    private float shy;
    private float shz;
    private int cachedTint;
    private int cachedMultiplier;
    
    static {
        SIDES = EnumFacing.values();
    }
    
    public BlockInfo(final BlockColors colors) {
        this.t = new boolean[3][3][3];
        this.s = new int[3][3][3];
        this.b = new int[3][3][3];
        this.skyLight = new float[3][2][2][2];
        this.blockLight = new float[3][2][2][2];
        this.ao = new float[3][3][3];
        this.packed = new int[7];
        this.shx = 0.0f;
        this.shy = 0.0f;
        this.shz = 0.0f;
        this.cachedTint = -1;
        this.cachedMultiplier = -1;
        this.colors = colors;
    }
    
    public int getColorMultiplier(final int tint) {
        if (this.cachedTint == tint) {
            return this.cachedMultiplier;
        }
        this.cachedTint = tint;
        return this.cachedMultiplier = this.colors.colorMultiplier(this.state, this.world, this.blockPos, tint);
    }
    
    public void updateShift() {
        final Vec3d offset = this.state.func_191059_e(this.world, this.blockPos);
        this.shx = (float)offset.xCoord;
        this.shy = (float)offset.yCoord;
        this.shz = (float)offset.zCoord;
    }
    
    public void setWorld(final IBlockAccess world) {
        this.world = world;
        this.cachedTint = -1;
        this.cachedMultiplier = -1;
    }
    
    public void setState(final IBlockState state) {
        this.state = state;
        this.cachedTint = -1;
        this.cachedMultiplier = -1;
    }
    
    public void setBlockPos(final BlockPos blockPos) {
        this.blockPos = blockPos;
        this.cachedTint = -1;
        this.cachedMultiplier = -1;
        final float shx = 0.0f;
        this.shz = shx;
        this.shy = shx;
        this.shx = shx;
    }
    
    public void reset() {
        this.world = null;
        this.state = null;
        this.blockPos = null;
        this.cachedTint = -1;
        this.cachedMultiplier = -1;
        final float shx = 0.0f;
        this.shz = shx;
        this.shy = shx;
        this.shx = shx;
    }
    
    private float combine(int c, int s1, int s2, int s3, final boolean t0, final boolean t1, final boolean t2, final boolean t3) {
        if (c == 0 && !t0) {
            c = Math.max(0, Math.max(s1, s2) - 1);
        }
        if (s1 == 0 && !t1) {
            s1 = Math.max(0, c - 1);
        }
        if (s2 == 0 && !t2) {
            s2 = Math.max(0, c - 1);
        }
        if (s3 == 0 && !t3) {
            s3 = Math.max(0, Math.max(s1, s2) - 1);
        }
        return (c + s1 + s2 + s3) * 32.0f / 262140.0f;
    }
    
    public void updateLightMatrix() {
        for (int x = 0; x <= 2; ++x) {
            for (int y = 0; y <= 2; ++y) {
                for (int z = 0; z <= 2; ++z) {
                    final BlockPos pos = this.blockPos.add(x - 1, y - 1, z - 1);
                    final IBlockState state = this.world.getBlockState(pos);
                    this.t[x][y][z] = (state.getLightOpacity() < 15);
                    final int brightness = state.getPackedLightmapCoords(this.world, pos);
                    this.s[x][y][z] = (brightness >> 20 & 0xF);
                    this.b[x][y][z] = (brightness >> 4 & 0xF);
                    this.ao[x][y][z] = state.getAmbientOcclusionLightValue();
                }
            }
        }
        EnumFacing[] sides;
        for (int length = (sides = BlockInfo.SIDES).length, i = 0; i < length; ++i) {
            final EnumFacing side = sides[i];
            if (!this.state.func_191057_i()) {
                final int x2 = side.getFrontOffsetX() + 1;
                final int y2 = side.getFrontOffsetY() + 1;
                final int z2 = side.getFrontOffsetZ() + 1;
                this.s[x2][y2][z2] = Math.max(this.s[1][1][1] - 1, this.s[x2][y2][z2]);
                this.b[x2][y2][z2] = Math.max(this.b[1][1][1] - 1, this.b[x2][y2][z2]);
            }
        }
        for (int x = 0; x < 2; ++x) {
            for (int y = 0; y < 2; ++y) {
                for (int z = 0; z < 2; ++z) {
                    final int x3 = x * 2;
                    final int y3 = y * 2;
                    final int z3 = z * 2;
                    final int sxyz = this.s[x3][y3][z3];
                    final int bxyz = this.b[x3][y3][z3];
                    final boolean txyz = this.t[x3][y3][z3];
                    final int sxz = this.s[x3][1][z3];
                    final int sxy = this.s[x3][y3][1];
                    final int syz = this.s[1][y3][z3];
                    final int bxz = this.b[x3][1][z3];
                    final int bxy = this.b[x3][y3][1];
                    final int byz = this.b[1][y3][z3];
                    final boolean txz = this.t[x3][1][z3];
                    final boolean txy = this.t[x3][y3][1];
                    final boolean tyz = this.t[1][y3][z3];
                    final int sx = this.s[x3][1][1];
                    final int sy = this.s[1][y3][1];
                    final int sz = this.s[1][1][z3];
                    final int bx = this.b[x3][1][1];
                    final int by = this.b[1][y3][1];
                    final int bz = this.b[1][1][z3];
                    final boolean tx = this.t[x3][1][1];
                    final boolean ty = this.t[1][y3][1];
                    final boolean tz = this.t[1][1][z3];
                    this.skyLight[0][x][y][z] = this.combine(sx, sxz, sxy, (txz || txy) ? sxyz : sx, tx, txz, txy, (txz || txy) ? txyz : tx);
                    this.blockLight[0][x][y][z] = this.combine(bx, bxz, bxy, (txz || txy) ? bxyz : bx, tx, txz, txy, (txz || txy) ? txyz : tx);
                    this.skyLight[1][x][y][z] = this.combine(sy, sxy, syz, (txy || tyz) ? sxyz : sy, ty, txy, tyz, (txy || tyz) ? txyz : ty);
                    this.blockLight[1][x][y][z] = this.combine(by, bxy, byz, (txy || tyz) ? bxyz : by, ty, txy, tyz, (txy || tyz) ? txyz : ty);
                    this.skyLight[2][x][y][z] = this.combine(sz, syz, sxz, (tyz || txz) ? sxyz : sz, tz, tyz, txz, (tyz || txz) ? txyz : tz);
                    this.blockLight[2][x][y][z] = this.combine(bz, byz, bxz, (tyz || txz) ? bxyz : bz, tz, tyz, txz, (tyz || txz) ? txyz : tz);
                }
            }
        }
    }
    
    public void updateFlatLighting() {
        this.full = this.state.isFullCube();
        this.packed[0] = this.state.getPackedLightmapCoords(this.world, this.blockPos);
        EnumFacing[] sides;
        for (int length = (sides = BlockInfo.SIDES).length, j = 0; j < length; ++j) {
            final EnumFacing side = sides[j];
            final int i = side.ordinal() + 1;
            this.packed[i] = this.state.getPackedLightmapCoords(this.world, this.blockPos.offset(side));
        }
    }
    
    public IBlockAccess getWorld() {
        return this.world;
    }
    
    public IBlockState getState() {
        return this.state;
    }
    
    public BlockPos getBlockPos() {
        return this.blockPos;
    }
    
    public boolean[][][] getTranslucent() {
        return this.t;
    }
    
    public float[][][][] getSkyLight() {
        return this.skyLight;
    }
    
    public float[][][][] getBlockLight() {
        return this.blockLight;
    }
    
    public float[][][] getAo() {
        return this.ao;
    }
    
    public int[] getPackedLight() {
        return this.packed;
    }
    
    public boolean isFullCube() {
        return this.full;
    }
    
    public float getShx() {
        return this.shx;
    }
    
    public float getShy() {
        return this.shy;
    }
    
    public float getShz() {
        return this.shz;
    }
    
    public int getCachedTint() {
        return this.cachedTint;
    }
    
    public int getCachedMultiplier() {
        return this.cachedMultiplier;
    }
}
