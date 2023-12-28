// 
// Decompiled by Procyon v0.6.0
// 

package com.TominoCZ.FBP.handler;

import java.util.Iterator;
import net.minecraft.client.particle.IParticleFactory;
import com.TominoCZ.FBP.particle.FBPParticleManager;
import net.minecraft.client.particle.ParticleDigging;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.block.BlockTorch;
import net.minecraft.block.Block;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.BlockSlab;
import net.minecraft.util.EnumHand;
import com.TominoCZ.FBP.node.FBPBlockNode;
import net.minecraft.item.ItemBlock;
import net.minecraft.client.particle.Particle;
import com.TominoCZ.FBP.particle.FBPParticleBlock;
import net.minecraft.util.EnumFacing;
import net.minecraft.block.BlockFalling;
import net.minecraft.util.math.Vec3i;
import net.minecraft.util.math.MathHelper;
import com.TominoCZ.FBP.model.FBPModelHelper;
import net.minecraft.block.BlockDoublePlant;
import net.minecraft.world.IBlockAccess;
import net.minecraft.init.Blocks;
import com.TominoCZ.FBP.FBP;
import net.minecraft.block.state.IBlockState;
import net.minecraft.world.World;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import com.TominoCZ.FBP.node.FBPBlockPosNode;
import io.netty.util.internal.ConcurrentSet;
import net.minecraft.world.IWorldEventListener;
import net.minecraft.client.Minecraft;

public class FBPEventHandler
{
    Minecraft mc;
    IWorldEventListener listener;
    ConcurrentSet<FBPBlockPosNode> list;
    
    public FBPEventHandler() {
        this.mc = Minecraft.getMinecraft();
        this.list = new ConcurrentSet<FBPBlockPosNode>();
        this.listener = new IWorldEventListener() {
            @Override
            public void markBlockRangeForRenderUpdate(final int x1, final int y1, final int z1, final int x2, final int y2, final int z2) {
            }
            
            @Override
            public void broadcastSound(final int soundID, final BlockPos pos, final int data) {
            }
            
            @Override
            public void onEntityAdded(final Entity entityIn) {
            }
            
            @Override
            public void spawnParticle(final int particleID, final boolean ignoreRange, final double xCoord, final double yCoord, final double zCoord, final double xSpeed, final double ySpeed, final double zSpeed, final int... parameters) {
            }
            
            @Override
            public void sendBlockBreakProgress(final int breakerId, final BlockPos pos, final int progress) {
            }
            
            @Override
            public void playSoundToAllNearExcept(final EntityPlayer player, final SoundEvent soundIn, final SoundCategory category, final double x, final double y, final double z, final float volume, final float pitch) {
            }
            
            @Override
            public void playRecord(final SoundEvent soundIn, final BlockPos pos) {
            }
            
            @Override
            public void playEvent(final EntityPlayer player, final int type, final BlockPos blockPosIn, final int data) {
            }
            
            @Override
            public void onEntityRemoved(final Entity entityIn) {
            }
            
            @Override
            public void notifyLightSet(final BlockPos pos) {
            }
            
            @Override
            public void notifyBlockUpdate(final World worldIn, final BlockPos pos, final IBlockState oldState, final IBlockState newState, final int flags) {
                if (FBP.enabled && FBP.fancyPlaceAnim && (flags == 11 || flags == 3) && !oldState.equals(newState)) {
                    final FBPBlockPosNode node = FBPEventHandler.this.getNodeWithPos(pos);
                    if (node != null && !node.checked) {
                        if (newState.getBlock() == FBP.FBPBlock || newState.getBlock() == Blocks.AIR || oldState.getBlock() == newState.getBlock()) {
                            FBPEventHandler.this.removePosEntry(pos);
                            return;
                        }
                        final IBlockState state = newState.getActualState(worldIn, pos);
                        if (state.getBlock() instanceof BlockDoublePlant || !FBPModelHelper.isModelValid(state)) {
                            FBPEventHandler.this.removePosEntry(pos);
                            return;
                        }
                        final long seed = MathHelper.getPositionRandom(pos);
                        boolean isNotFalling = true;
                        if (state.getBlock() instanceof BlockFalling) {
                            final BlockFalling bf = (BlockFalling)state.getBlock();
                            if (BlockFalling.canFallThrough(worldIn.getBlockState(pos.offset(EnumFacing.DOWN)))) {
                                isNotFalling = false;
                            }
                        }
                        if (!FBP.INSTANCE.isBlacklisted(state.getBlock(), false) && isNotFalling) {
                            node.checked = true;
                            final FBPParticleBlock p = new FBPParticleBlock(worldIn, pos.getX() + 0.5f, pos.getY() + 0.5f, pos.getZ() + 0.5f, state, seed);
                            FBPEventHandler.this.mc.effectRenderer.addEffect(p);
                            FBP.FBPBlock.copyState(worldIn, pos, state, p);
                        }
                    }
                }
            }
            
            @Override
            public void func_190570_a(final int p_190570_1_, final boolean p_190570_2_, final boolean p_190570_3_, final double p_190570_4_, final double p_190570_6_, final double p_190570_8_, final double p_190570_10_, final double p_190570_12_, final double p_190570_14_, final int... p_190570_16_) {
            }
        };
    }
    
    public void onInteractionEvent(final int mouseId) {
        try {
            if (mouseId != 1) {
                return;
            }
            final RayTraceResult e = this.mc.objectMouseOver;
            if (e.hitVec == null || this.mc.player.getHeldItemMainhand() == null || !this.mc.world.isRemote || !(this.mc.player.getHeldItemMainhand().getItem() instanceof ItemBlock)) {
                return;
            }
            final BlockPos pos = e.getBlockPos();
            final BlockPos pos_o = e.getBlockPos().offset(e.sideHit);
            Block inHand = null;
            IBlockState atPos = this.mc.world.getBlockState(pos);
            IBlockState offset = this.mc.world.getBlockState(pos_o);
            boolean bool = false;
            final float f = (float)(e.hitVec.xCoord - pos.getX());
            final float f2 = (float)(e.hitVec.yCoord - pos.getY());
            final float f3 = (float)(e.hitVec.zCoord - pos.getZ());
            if (atPos.getBlock() == FBP.FBPBlock) {
                final FBPBlockNode n = FBP.FBPBlock.blockNodes.get(pos);
                if (n != null && n.state.getBlock() != null) {
                    final boolean activated = n.originalBlock.onBlockActivated(this.mc.world, pos, n.state, this.mc.player, EnumHand.MAIN_HAND, e.sideHit, f, f2, f3);
                    if (activated) {
                        return;
                    }
                    atPos = n.state;
                }
                if (atPos.getBlock() instanceof BlockSlab) {
                    final BlockSlab.EnumBlockHalf half = atPos.getValue(BlockSlab.HALF);
                    if (e.sideHit == EnumFacing.UP) {
                        if (half == BlockSlab.EnumBlockHalf.BOTTOM) {
                            bool = true;
                        }
                    }
                    else if (e.sideHit == EnumFacing.DOWN && half == BlockSlab.EnumBlockHalf.TOP) {
                        bool = true;
                    }
                }
            }
            if (offset.getBlock() == FBP.FBPBlock) {
                final FBPBlockNode n = FBP.FBPBlock.blockNodes.get(pos_o);
                if (n != null && n.state.getBlock() != null) {
                    offset = n.state;
                }
            }
            if (this.mc.player.getHeldItemMainhand() != null && this.mc.player.getHeldItemMainhand().getItem() != null) {
                inHand = Block.getBlockFromItem(this.mc.player.getHeldItemMainhand().getItem());
            }
            boolean addedOffset = false;
            final FBPBlockPosNode node = new FBPBlockPosNode();
            try {
                if (!bool && inHand != null && offset.getMaterial().isReplaceable() && !atPos.getBlock().isReplaceable(this.mc.world, pos) && inHand.canPlaceBlockAt(this.mc.world, pos_o)) {
                    node.add(pos_o);
                    addedOffset = true;
                }
                else {
                    node.add(pos);
                }
                boolean okToAdd = inHand != null && inHand != Blocks.AIR && inHand.canPlaceBlockAt(this.mc.world, addedOffset ? pos_o : pos);
                if (inHand != null && inHand instanceof BlockTorch) {
                    final BlockTorch bt = (BlockTorch)inHand;
                    if (!bt.canPlaceBlockAt(this.mc.world, pos_o)) {
                        okToAdd = false;
                    }
                    if (atPos.getBlock() == Blocks.TORCH) {
                        EnumFacing[] values;
                        for (int length = (values = EnumFacing.VALUES).length, i = 0; i < length; ++i) {
                            final EnumFacing fc = values[i];
                            final BlockPos p = pos_o.offset(fc);
                            final Block bl = this.mc.world.getBlockState(p).getBlock();
                            if (bl != Blocks.TORCH && bl != FBP.FBPBlock && bl.isFullCube(bl.getDefaultState())) {
                                okToAdd = true;
                                break;
                            }
                            okToAdd = false;
                        }
                    }
                }
                final FBPBlockPosNode last = this.getNodeWithPos(pos);
                final FBPBlockPosNode last_o = this.getNodeWithPos(pos_o);
                if (okToAdd) {
                    final boolean replaceable = (addedOffset ? offset : atPos).getBlock().isReplaceable(this.mc.world, addedOffset ? pos_o : pos);
                    if (last != null && !addedOffset && last.checked) {
                        return;
                    }
                    if (last_o != null && addedOffset && (last_o.checked || replaceable)) {
                        return;
                    }
                    final Chunk c = this.mc.world.getChunkFromBlockCoords(addedOffset ? pos_o : pos);
                    c.resetRelightChecks();
                    c.setLightPopulated(true);
                    this.list.add(node);
                }
            }
            catch (final Throwable t) {
                this.list.clear();
            }
        }
        catch (final Exception ex) {}
    }
    
    public void onWorldLoadEvent() {
        this.mc.world.addEventListener(this.listener);
        this.list.clear();
    }
    
    public void onEntityJoinWorldEvent(final Entity entity) {
        if (entity == this.mc.player) {
            FBP.fancyEffectRenderer = new FBPParticleManager(this.mc.world, this.mc.getTextureManager(), new ParticleDigging.Factory());
            if ((FBP.originalEffectRenderer == null || (FBP.originalEffectRenderer != this.mc.effectRenderer && FBP.originalEffectRenderer != FBP.fancyEffectRenderer)) && this.mc.effectRenderer != null) {
                FBP.originalEffectRenderer = this.mc.effectRenderer;
            }
            if (FBP.enabled) {
                this.mc.effectRenderer = FBP.fancyEffectRenderer;
            }
        }
    }
    
    FBPBlockPosNode getNodeWithPos(final BlockPos pos) {
        for (final FBPBlockPosNode n : this.list) {
            if (n.hasPos(pos)) {
                return n;
            }
        }
        return null;
    }
    
    public void removePosEntry(final BlockPos pos) {
        for (int i = 0; i < this.list.size(); ++i) {
            final FBPBlockPosNode n = this.getNodeWithPos(pos);
            if (n != null) {
                this.list.remove(n);
            }
        }
    }
}
