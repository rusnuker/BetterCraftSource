// 
// Decompiled by Procyon v0.6.0
// 

package com.TominoCZ.FBP.particle;

import net.minecraft.util.math.AxisAlignedBB;
import javax.vecmath.Vector2d;
import net.minecraft.block.BlockAir;
import net.minecraft.world.IBlockAccess;
import org.lwjgl.opengl.GL11;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketPlayerDigging;
import net.minecraft.entity.Entity;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.client.renderer.Vector3d;
import com.TominoCZ.FBP.util.FBPRenderUtil;
import net.minecraft.init.Blocks;
import com.TominoCZ.FBP.FBP;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraft.tileentity.TileEntity;
import com.TominoCZ.FBP.vector.FBPVector3d;
import net.minecraft.util.EnumFacing;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.BlockModelRenderer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.block.Block;
import net.minecraft.util.math.BlockPos;
import net.minecraft.client.particle.Particle;

public class FBPParticleBlock extends Particle
{
    public BlockPos pos;
    Block block;
    IBlockState blockState;
    BlockModelRenderer mr;
    IBakedModel modelPrefab;
    Minecraft mc;
    EnumFacing facing;
    FBPVector3d prevRot;
    FBPVector3d rot;
    long textureSeed;
    float startingHeight;
    float startingAngle;
    float step;
    float height;
    float prevHeight;
    float smoothHeight;
    boolean lookingUp;
    boolean spawned;
    long tick;
    boolean blockSet;
    TileEntity tileEntity;
    
    public FBPParticleBlock(final World worldIn, final double posXIn, final double posYIn, final double posZIn, final IBlockState state, final long rand) {
        super(worldIn, posXIn, posYIn, posZIn);
        this.step = 0.00275f;
        this.spawned = false;
        this.tick = -1L;
        this.blockSet = false;
        this.pos = new BlockPos(posXIn, posYIn, posZIn);
        this.mc = Minecraft.getMinecraft();
        this.facing = this.mc.player.getHorizontalFacing();
        this.lookingUp = (MathHelper.wrapDegrees(this.mc.player.rotationPitch) <= 0.0f);
        final float prevHeight = (float)FBP.random.nextDouble(0.065, 0.115);
        this.startingHeight = prevHeight;
        this.height = prevHeight;
        this.prevHeight = prevHeight;
        this.startingAngle = (float)FBP.random.nextDouble(0.03125, 0.0635);
        this.prevRot = new FBPVector3d();
        this.rot = new FBPVector3d();
        switch (this.facing) {
            case EAST: {
                this.rot.z = -this.startingAngle;
                this.rot.x = -this.startingAngle;
                break;
            }
            case NORTH: {
                this.rot.x = -this.startingAngle;
                this.rot.z = this.startingAngle;
                break;
            }
            case SOUTH: {
                this.rot.x = this.startingAngle;
                this.rot.z = -this.startingAngle;
                break;
            }
            case WEST: {
                this.rot.z = this.startingAngle;
                this.rot.x = this.startingAngle;
                break;
            }
        }
        this.textureSeed = rand;
        this.blockState = state;
        this.block = state.getBlock();
        this.mr = this.mc.getBlockRendererDispatcher().getBlockModelRenderer();
        this.canCollide = false;
        this.modelPrefab = this.mc.getBlockRendererDispatcher().getBlockModelShapes().getModelForState(state);
        if (this.modelPrefab == null) {
            this.canCollide = true;
            this.isExpired = true;
        }
        this.tileEntity = worldIn.getTileEntity(this.pos);
    }
    
    @Override
    public void onUpdate() {
        if (++this.particleAge >= 10) {
            this.killParticle();
        }
        if (!this.canCollide) {
            final IBlockState s = this.mc.world.getBlockState(this.pos);
            if (s.getBlock() != FBP.FBPBlock || s.getBlock() == this.block) {
                if (this.blockSet && s.getBlock() == Blocks.AIR) {
                    this.killParticle();
                    FBP.FBPBlock.onBlockDestroyedByPlayer(this.mc.world, this.pos, s);
                    this.mc.world.setBlockState(this.pos, Blocks.AIR.getDefaultState(), 2);
                    return;
                }
                FBP.FBPBlock.copyState(this.mc.world, this.pos, this.blockState, this);
                this.mc.world.setBlockState(this.pos, FBP.FBPBlock.getDefaultState(), 2);
                final Chunk c = this.mc.world.getChunkFromBlockCoords(this.pos);
                c.resetRelightChecks();
                c.setLightPopulated(true);
                FBPRenderUtil.markBlockForRender(this.pos);
                this.blockSet = true;
            }
            this.spawned = true;
        }
        if (this.isExpired || this.mc.isGamePaused()) {
            return;
        }
        this.prevHeight = this.height;
        this.prevRot.copyFrom(this.rot);
        switch (this.facing) {
            case EAST: {
                final FBPVector3d rot = this.rot;
                rot.z += this.step;
                final FBPVector3d rot2 = this.rot;
                rot2.x += this.step;
                break;
            }
            case NORTH: {
                final FBPVector3d rot3 = this.rot;
                rot3.x += this.step;
                final FBPVector3d rot4 = this.rot;
                rot4.z -= this.step;
                break;
            }
            case SOUTH: {
                final FBPVector3d rot5 = this.rot;
                rot5.x -= this.step;
                final FBPVector3d rot6 = this.rot;
                rot6.z += this.step;
                break;
            }
            case WEST: {
                final FBPVector3d rot7 = this.rot;
                rot7.z -= this.step;
                final FBPVector3d rot8 = this.rot;
                rot8.x -= this.step;
                break;
            }
        }
        this.height -= this.step * 5.0f;
        this.step *= 1.5678982f;
    }
    
    @Override
    public void renderParticle(final BufferBuilder buff, final Entity entityIn, final float partialTicks, final float rotationX, final float rotationZ, final float rotationYZ, final float rotationXY, final float rotationXZ) {
        if (this.isExpired) {
            return;
        }
        if (this.canCollide) {
            final Block b = this.mc.world.getBlockState(this.pos).getBlock();
            if (this.block != b && b != Blocks.AIR && this.mc.world.getBlockState(this.pos).getBlock() != this.blockState.getBlock()) {
                this.mc.world.setBlockState(this.pos, this.blockState, 2);
                if (this.tileEntity != null) {
                    this.mc.world.setTileEntity(this.pos, this.tileEntity);
                }
                this.mc.world.sendPacketToServer(new CPacketPlayerDigging(CPacketPlayerDigging.Action.ABORT_DESTROY_BLOCK, this.pos, this.facing));
                FBPRenderUtil.markBlockForRender(this.pos);
                FBP.INSTANCE.eventHandler.removePosEntry(this.pos);
            }
            if (this.tick >= 1L) {
                this.killParticle();
                return;
            }
            ++this.tick;
        }
        if (!this.spawned) {
            return;
        }
        final float f = 0.0f;
        final float f2 = 0.0f;
        final float f3 = 0.0f;
        final float f4 = 0.0f;
        final float f5 = (float)(this.prevPosX + (this.posX - this.prevPosX) * partialTicks - FBPParticleBlock.interpPosX) - 0.5f;
        final float f6 = (float)(this.prevPosY + (this.posY - this.prevPosY) * partialTicks - FBPParticleBlock.interpPosY) - 0.5f;
        final float f7 = (float)(this.prevPosZ + (this.posZ - this.prevPosZ) * partialTicks - FBPParticleBlock.interpPosZ) - 0.5f;
        this.smoothHeight = (float)(this.prevHeight + (this.height - this.prevHeight) * (double)partialTicks);
        final FBPVector3d smoothRot = this.rot.partialVec(this.prevRot, partialTicks);
        if (this.smoothHeight <= 0.0f) {
            this.smoothHeight = 0.0f;
        }
        final FBPVector3d t = new FBPVector3d(0.0, this.smoothHeight, 0.0);
        final FBPVector3d tRot = new FBPVector3d(0.0, this.smoothHeight, 0.0);
        switch (this.facing) {
            case EAST: {
                if (smoothRot.z > 0.0) {
                    this.canCollide = true;
                    smoothRot.z = 0.0;
                    smoothRot.x = 0.0;
                }
                t.x = -this.smoothHeight;
                t.z = this.smoothHeight;
                tRot.x = 1.0;
                break;
            }
            case NORTH: {
                if (smoothRot.z < 0.0) {
                    this.canCollide = true;
                    smoothRot.x = 0.0;
                    smoothRot.z = 0.0;
                }
                t.x = this.smoothHeight;
                t.z = this.smoothHeight;
                break;
            }
            case SOUTH: {
                if (smoothRot.x < 0.0) {
                    this.canCollide = true;
                    smoothRot.x = 0.0;
                    smoothRot.z = 0.0;
                }
                t.x = -this.smoothHeight;
                t.z = -this.smoothHeight;
                tRot.x = 1.0;
                tRot.z = 1.0;
                break;
            }
            case WEST: {
                if (smoothRot.z < 0.0) {
                    this.canCollide = true;
                    smoothRot.z = 0.0;
                    smoothRot.x = 0.0;
                }
                t.x = this.smoothHeight;
                t.z = -this.smoothHeight;
                tRot.z = 1.0;
                break;
            }
        }
        if (FBP.spawnPlaceParticles && this.canCollide && this.tick == 0L && (!FBP.frozen || FBP.spawnWhileFrozen) && (FBP.spawnRedstoneBlockParticles || this.block != Blocks.REDSTONE_BLOCK) && this.mc.gameSettings.particleSetting < 2) {
            this.spawnParticles();
        }
        buff.setTranslation(-this.pos.getX(), -this.pos.getY(), -this.pos.getZ());
        Tessellator.getInstance().draw();
        this.mc.getRenderManager().renderEngine.bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
        buff.begin(7, DefaultVertexFormats.BLOCK);
        GlStateManager.pushMatrix();
        GlStateManager.enableCull();
        GlStateManager.enableColorMaterial();
        GL11.glColorMaterial(1028, 5634);
        GlStateManager.translate(f5, f6, f7);
        GlStateManager.translate(tRot.x, tRot.y, tRot.z);
        GlStateManager.rotate((float)Math.toDegrees(smoothRot.x), 1.0f, 0.0f, 0.0f);
        GlStateManager.rotate((float)Math.toDegrees(smoothRot.z), 0.0f, 0.0f, 1.0f);
        GlStateManager.translate(-tRot.x, -tRot.y, -tRot.z);
        GlStateManager.translate(t.x, t.y, t.z);
        if (FBP.animSmoothLighting) {
            this.mr.renderModelSmooth(this.mc.world, this.modelPrefab, this.blockState, this.pos, buff, false, this.textureSeed);
        }
        else {
            this.mr.renderModelFlat(this.mc.world, this.modelPrefab, this.blockState, this.pos, buff, false, this.textureSeed);
        }
        buff.setTranslation(0.0, 0.0, 0.0);
        Tessellator.getInstance().draw();
        GlStateManager.popMatrix();
        this.mc.getTextureManager().bindTexture(FBP.LOCATION_PARTICLE_TEXTURE);
        buff.begin(7, DefaultVertexFormats.PARTICLE_POSITION_TEX_COLOR_LMAP);
    }
    
    private void spawnParticles() {
        if (this.mc.world.getBlockState(this.pos.offset(EnumFacing.DOWN)).getBlock() instanceof BlockAir) {
            return;
        }
        final AxisAlignedBB aabb = this.block.getSelectedBoundingBox(this.blockState, this.mc.world, this.pos);
        final Vector2d[] corners = { new Vector2d(aabb.minX, aabb.minZ), new Vector2d(aabb.maxX, aabb.maxZ), new Vector2d(aabb.minX, aabb.maxZ), new Vector2d(aabb.maxX, aabb.minZ) };
        final Vector2d middle = new Vector2d(this.pos.getX() + 0.5f, this.pos.getZ() + 0.5f);
        Vector2d[] array;
        for (int length = (array = corners).length, i = 0; i < length; ++i) {
            final Vector2d corner = array[i];
            double mX = middle.x - corner.x;
            double mZ = middle.y - corner.y;
            mX /= -0.5;
            mZ /= -0.5;
            this.mc.effectRenderer.addEffect(new FBPParticleDigging(this.mc.world, corner.x, this.pos.getY() + 0.1f, corner.y, mX, 0.0, mZ, 0.6f, 1.0f, 1.0f, 1.0f, this.block.getActualState(this.blockState, this.mc.world, this.pos), null, this.particleTexture).multipleParticleScaleBy(0.5f).multiplyVelocity(0.5f));
        }
        Vector2d[] array2;
        for (int length2 = (array2 = corners).length, j = 0; j < length2; ++j) {
            final Vector2d corner = array2[j];
            if (corner != null) {
                double mX = middle.x - corner.x;
                double mZ = middle.y - corner.y;
                mX /= -0.45;
                mZ /= -0.45;
                this.mc.effectRenderer.addEffect(new FBPParticleDigging(this.mc.world, corner.x, this.pos.getY() + 0.1f, corner.y, mX / 3.0, 0.0, mZ / 3.0, 0.6f, 1.0f, 1.0f, 1.0f, this.block.getActualState(this.blockState, this.mc.world, this.pos), null, this.particleTexture).multipleParticleScaleBy(0.75f).multiplyVelocity(0.75f));
            }
        }
    }
    
    public void killParticle() {
        this.isExpired = true;
        FBP.FBPBlock.blockNodes.remove(this.pos);
        FBP.INSTANCE.eventHandler.removePosEntry(this.pos);
    }
    
    @Override
    public void setExpired() {
        FBP.INSTANCE.eventHandler.removePosEntry(this.pos);
    }
}
