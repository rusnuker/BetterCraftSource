// 
// Decompiled by Procyon v0.6.0
// 

package net.minecraft.client.renderer.chunk;

import net.minecraft.client.renderer.ViewFrustum;
import net.minecraft.world.ChunkCache;
import net.minecraft.client.renderer.RegionRenderCacheBuilder;
import net.minecraft.block.BlockCactus;
import net.minecraft.block.BlockRedstoneWire;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.entity.EntityPlayerSP;
import javax.annotation.Nullable;
import optifine.RenderEnv;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import java.util.Iterator;
import net.minecraft.client.renderer.BlockRendererDispatcher;
import optifine.ChunkCacheOF;
import java.util.HashSet;
import java.util.Collection;
import shadersmod.client.SVertexBuilder;
import net.minecraft.world.IBlockAccess;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import optifine.ReflectorForge;
import optifine.BlockPosM;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import me.amkgre.bettercraft.client.mods.chunkanimator.ChunkAnimator;
import net.minecraft.util.math.Vec3i;
import net.minecraft.util.EnumFacing;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.renderer.OpenGlHelper;
import optifine.Reflector;
import optifine.Config;
import net.minecraft.client.renderer.GLAllocation;
import com.google.common.collect.Sets;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.client.renderer.vertex.VertexBuffer;
import java.nio.FloatBuffer;
import net.minecraft.tileentity.TileEntity;
import java.util.Set;
import java.util.concurrent.locks.ReentrantLock;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.world.World;

public class RenderChunk
{
    private World world;
    private final RenderGlobal renderGlobal;
    public static int renderChunksUpdated;
    public CompiledChunk compiledChunk;
    private final ReentrantLock lockCompileTask;
    private final ReentrantLock lockCompiledChunk;
    private ChunkCompileTaskGenerator compileTask;
    private final Set<TileEntity> setTileEntities;
    private final int index;
    private final FloatBuffer modelviewMatrix;
    private final VertexBuffer[] vertexBuffers;
    public AxisAlignedBB boundingBox;
    private int frameIndex;
    private boolean needsUpdate;
    private final BlockPos.MutableBlockPos position;
    private final BlockPos.MutableBlockPos[] mapEnumFacing;
    private boolean needsUpdateCustom;
    private static BlockRenderLayer[] ENUM_WORLD_BLOCK_LAYERS;
    private BlockRenderLayer[] blockLayersSingle;
    private boolean isMipmaps;
    private boolean fixBlockLayer;
    private boolean playerUpdate;
    private RenderChunk[] renderChunksOfset16;
    private Chunk chunk;
    
    static {
        RenderChunk.ENUM_WORLD_BLOCK_LAYERS = BlockRenderLayer.values();
    }
    
    public RenderChunk(final World p_i47120_1_, final RenderGlobal p_i47120_2_, final int p_i47120_3_) {
        this.compiledChunk = CompiledChunk.DUMMY;
        this.lockCompileTask = new ReentrantLock();
        this.lockCompiledChunk = new ReentrantLock();
        this.setTileEntities = (Set<TileEntity>)Sets.newHashSet();
        this.modelviewMatrix = GLAllocation.createDirectFloatBuffer(16);
        this.vertexBuffers = new VertexBuffer[BlockRenderLayer.values().length];
        this.frameIndex = -1;
        this.needsUpdate = true;
        this.position = new BlockPos.MutableBlockPos(-1, -1, -1);
        this.mapEnumFacing = new BlockPos.MutableBlockPos[6];
        this.blockLayersSingle = new BlockRenderLayer[1];
        this.isMipmaps = Config.isMipmaps();
        this.fixBlockLayer = !Reflector.BetterFoliageClient.exists();
        this.playerUpdate = false;
        this.renderChunksOfset16 = new RenderChunk[6];
        for (int i = 0; i < this.mapEnumFacing.length; ++i) {
            this.mapEnumFacing[i] = new BlockPos.MutableBlockPos();
        }
        this.world = p_i47120_1_;
        this.renderGlobal = p_i47120_2_;
        this.index = p_i47120_3_;
        if (OpenGlHelper.useVbo()) {
            for (int j = 0; j < BlockRenderLayer.values().length; ++j) {
                this.vertexBuffers[j] = new VertexBuffer(DefaultVertexFormats.BLOCK);
            }
        }
    }
    
    public boolean setFrameIndex(final int frameIndexIn) {
        if (this.frameIndex == frameIndexIn) {
            return false;
        }
        this.frameIndex = frameIndexIn;
        return true;
    }
    
    public VertexBuffer getVertexBufferByLayer(final int layer) {
        return this.vertexBuffers[layer];
    }
    
    public void setPosition(final int p_189562_1_, final int p_189562_2_, final int p_189562_3_) {
        if (p_189562_1_ != this.position.getX() || p_189562_2_ != this.position.getY() || p_189562_3_ != this.position.getZ()) {
            this.stopCompileTask();
            this.position.setPos(p_189562_1_, p_189562_2_, p_189562_3_);
            this.boundingBox = new AxisAlignedBB(p_189562_1_, p_189562_2_, p_189562_3_, p_189562_1_ + 16, p_189562_2_ + 16, p_189562_3_ + 16);
            EnumFacing[] values;
            for (int length = (values = EnumFacing.VALUES).length, i = 0; i < length; ++i) {
                final EnumFacing enumfacing = values[i];
                this.mapEnumFacing[enumfacing.ordinal()].setPos(this.position).move(enumfacing, 16);
                this.renderChunksOfset16[enumfacing.ordinal()] = null;
            }
            ChunkAnimator.INSTANCE.animationHandler.setOrigin(this, new BlockPos(p_189562_1_, p_189562_2_, p_189562_3_));
            this.chunk = null;
            this.initModelviewMatrix();
        }
    }
    
    public void resortTransparency(final float x, final float y, final float z, final ChunkCompileTaskGenerator generator) {
        final CompiledChunk compiledchunk = generator.getCompiledChunk();
        if (compiledchunk.getState() != null && !compiledchunk.isLayerEmpty(BlockRenderLayer.TRANSLUCENT)) {
            final BufferBuilder bufferbuilder = generator.getRegionRenderCacheBuilder().getWorldRendererByLayer(BlockRenderLayer.TRANSLUCENT);
            this.preRenderBlocks(bufferbuilder, this.position);
            bufferbuilder.setVertexState(compiledchunk.getState());
            this.postRenderBlocks(BlockRenderLayer.TRANSLUCENT, x, y, z, bufferbuilder, compiledchunk);
        }
    }
    
    public void rebuildChunk(final float x, final float y, final float z, final ChunkCompileTaskGenerator generator) {
        final CompiledChunk compiledchunk = new CompiledChunk();
        final int i = 1;
        final BlockPos blockpos = this.position;
        final BlockPos blockpos2 = blockpos.add(15, 15, 15);
        generator.getLock().lock();
        try {
            if (generator.getStatus() != ChunkCompileTaskGenerator.Status.COMPILING) {
                return;
            }
            generator.setCompiledChunk(compiledchunk);
        }
        finally {
            generator.getLock().unlock();
        }
        generator.getLock().unlock();
        final VisGraph lvt_9_1_ = new VisGraph();
        final HashSet lvt_10_1_ = Sets.newHashSet();
        if (this.world != null) {
            final ChunkCacheOF chunkcacheof = this.makeChunkCacheOF();
            if (!chunkcacheof.isEmpty()) {
                ++RenderChunk.renderChunksUpdated;
                chunkcacheof.renderStart();
                final boolean[] aboolean = new boolean[RenderChunk.ENUM_WORLD_BLOCK_LAYERS.length];
                final BlockRendererDispatcher blockrendererdispatcher = Minecraft.getMinecraft().getBlockRendererDispatcher();
                final boolean flag = Reflector.ForgeBlock_canRenderInLayer.exists();
                final boolean flag2 = Reflector.ForgeHooksClient_setRenderLayer.exists();
                for (final Object blockposm0 : BlockPosM.getAllInBoxMutable(blockpos, blockpos2)) {
                    final BlockPosM blockposm2 = (BlockPosM)blockposm0;
                    final IBlockState iblockstate = chunkcacheof.getBlockState(blockposm2);
                    final Block block = iblockstate.getBlock();
                    if (iblockstate.isOpaqueCube()) {
                        lvt_9_1_.setOpaqueCube(blockposm2);
                    }
                    if (ReflectorForge.blockHasTileEntity(iblockstate)) {
                        final TileEntity tileentity = chunkcacheof.getTileEntity(blockposm2, Chunk.EnumCreateEntityType.CHECK);
                        if (tileentity != null) {
                            final TileEntitySpecialRenderer<TileEntity> tileentityspecialrenderer = TileEntityRendererDispatcher.instance.getSpecialRenderer(tileentity);
                            if (tileentityspecialrenderer != null) {
                                if (tileentityspecialrenderer.isGlobalRenderer(tileentity)) {
                                    lvt_10_1_.add(tileentity);
                                }
                                else {
                                    compiledchunk.addTileEntity(tileentity);
                                }
                            }
                        }
                    }
                    BlockRenderLayer[] ablockrenderlayer;
                    if (flag) {
                        ablockrenderlayer = RenderChunk.ENUM_WORLD_BLOCK_LAYERS;
                    }
                    else {
                        ablockrenderlayer = this.blockLayersSingle;
                        ablockrenderlayer[0] = block.getBlockLayer();
                    }
                    for (int j = 0; j < ablockrenderlayer.length; ++j) {
                        BlockRenderLayer blockrenderlayer = ablockrenderlayer[j];
                        if (flag) {
                            final boolean flag3 = Reflector.callBoolean(block, Reflector.ForgeBlock_canRenderInLayer, iblockstate, blockrenderlayer);
                            if (!flag3) {
                                continue;
                            }
                        }
                        if (flag2) {
                            Reflector.callVoid(Reflector.ForgeHooksClient_setRenderLayer, blockrenderlayer);
                        }
                        if (this.fixBlockLayer) {
                            blockrenderlayer = this.fixBlockLayer(block, blockrenderlayer);
                        }
                        final int k = blockrenderlayer.ordinal();
                        if (block.getDefaultState().getRenderType() != EnumBlockRenderType.INVISIBLE) {
                            final BufferBuilder bufferbuilder = generator.getRegionRenderCacheBuilder().getWorldRendererByLayerId(k);
                            bufferbuilder.setBlockLayer(blockrenderlayer);
                            final RenderEnv renderenv = bufferbuilder.getRenderEnv(chunkcacheof, iblockstate, blockposm2);
                            renderenv.setRegionRenderCacheBuilder(generator.getRegionRenderCacheBuilder());
                            if (!compiledchunk.isLayerStarted(blockrenderlayer)) {
                                compiledchunk.setLayerStarted(blockrenderlayer);
                                this.preRenderBlocks(bufferbuilder, blockpos);
                            }
                            final boolean[] array = aboolean;
                            final int n = k;
                            array[n] |= blockrendererdispatcher.renderBlock(iblockstate, blockposm2, chunkcacheof, bufferbuilder);
                            if (renderenv.isOverlaysRendered()) {
                                this.postRenderOverlays(generator.getRegionRenderCacheBuilder(), compiledchunk, aboolean);
                                renderenv.setOverlaysRendered(false);
                            }
                        }
                    }
                    if (flag2) {
                        Reflector.callVoid(Reflector.ForgeHooksClient_setRenderLayer, (Object[])null);
                    }
                }
                BlockRenderLayer[] enum_WORLD_BLOCK_LAYERS;
                for (int length = (enum_WORLD_BLOCK_LAYERS = RenderChunk.ENUM_WORLD_BLOCK_LAYERS).length, l = 0; l < length; ++l) {
                    final BlockRenderLayer blockrenderlayer2 = enum_WORLD_BLOCK_LAYERS[l];
                    if (aboolean[blockrenderlayer2.ordinal()]) {
                        compiledchunk.setLayerUsed(blockrenderlayer2);
                    }
                    if (compiledchunk.isLayerStarted(blockrenderlayer2)) {
                        if (Config.isShaders()) {
                            SVertexBuilder.calcNormalChunkLayer(generator.getRegionRenderCacheBuilder().getWorldRendererByLayer(blockrenderlayer2));
                        }
                        this.postRenderBlocks(blockrenderlayer2, x, y, z, generator.getRegionRenderCacheBuilder().getWorldRendererByLayer(blockrenderlayer2), compiledchunk);
                    }
                }
                chunkcacheof.renderFinish();
            }
            compiledchunk.setVisibility(lvt_9_1_.computeVisibility());
            this.lockCompileTask.lock();
            try {
                final Set<TileEntity> set = (Set<TileEntity>)Sets.newHashSet((Iterable<?>)lvt_10_1_);
                final Set<TileEntity> set2 = (Set<TileEntity>)Sets.newHashSet((Iterable<?>)this.setTileEntities);
                set.removeAll(this.setTileEntities);
                set2.removeAll(lvt_10_1_);
                this.setTileEntities.clear();
                this.setTileEntities.addAll(lvt_10_1_);
                this.renderGlobal.updateTileEntities(set2, set);
            }
            finally {
                this.lockCompileTask.unlock();
            }
            this.lockCompileTask.unlock();
        }
    }
    
    protected void finishCompileTask() {
        this.lockCompileTask.lock();
        try {
            if (this.compileTask != null && this.compileTask.getStatus() != ChunkCompileTaskGenerator.Status.DONE) {
                this.compileTask.finish();
                this.compileTask = null;
            }
        }
        finally {
            this.lockCompileTask.unlock();
        }
        this.lockCompileTask.unlock();
    }
    
    public ReentrantLock getLockCompileTask() {
        return this.lockCompileTask;
    }
    
    public ChunkCompileTaskGenerator makeCompileTaskChunk() {
        this.lockCompileTask.lock();
        ChunkCompileTaskGenerator chunkcompiletaskgenerator;
        try {
            this.finishCompileTask();
            this.compileTask = new ChunkCompileTaskGenerator(this, ChunkCompileTaskGenerator.Type.REBUILD_CHUNK, this.getDistanceSq());
            this.resetChunkCache();
            chunkcompiletaskgenerator = this.compileTask;
        }
        finally {
            this.lockCompileTask.unlock();
        }
        this.lockCompileTask.unlock();
        return chunkcompiletaskgenerator;
    }
    
    private void resetChunkCache() {
        final int i = 1;
    }
    
    @Nullable
    public ChunkCompileTaskGenerator makeCompileTaskTransparency() {
        this.lockCompileTask.lock();
        ChunkCompileTaskGenerator chunkcompiletaskgenerator4;
        try {
            if (this.compileTask != null && this.compileTask.getStatus() == ChunkCompileTaskGenerator.Status.PENDING) {
                final ChunkCompileTaskGenerator chunkcompiletaskgenerator2 = null;
                return chunkcompiletaskgenerator2;
            }
            if (this.compileTask != null && this.compileTask.getStatus() != ChunkCompileTaskGenerator.Status.DONE) {
                this.compileTask.finish();
                this.compileTask = null;
            }
            (this.compileTask = new ChunkCompileTaskGenerator(this, ChunkCompileTaskGenerator.Type.RESORT_TRANSPARENCY, this.getDistanceSq())).setCompiledChunk(this.compiledChunk);
            final ChunkCompileTaskGenerator chunkcompiletaskgenerator3 = chunkcompiletaskgenerator4 = this.compileTask;
        }
        finally {
            this.lockCompileTask.unlock();
        }
        this.lockCompileTask.unlock();
        return chunkcompiletaskgenerator4;
    }
    
    protected double getDistanceSq() {
        final EntityPlayerSP entityplayersp = Minecraft.getMinecraft().player;
        final double d0 = this.boundingBox.minX + 8.0 - entityplayersp.posX;
        final double d2 = this.boundingBox.minY + 8.0 - entityplayersp.posY;
        final double d3 = this.boundingBox.minZ + 8.0 - entityplayersp.posZ;
        return d0 * d0 + d2 * d2 + d3 * d3;
    }
    
    private void preRenderBlocks(final BufferBuilder worldRendererIn, final BlockPos pos) {
        worldRendererIn.begin(7, DefaultVertexFormats.BLOCK);
        worldRendererIn.setTranslation(-pos.getX(), -pos.getY(), -pos.getZ());
    }
    
    private void postRenderBlocks(final BlockRenderLayer layer, final float x, final float y, final float z, final BufferBuilder worldRendererIn, final CompiledChunk compiledChunkIn) {
        if (layer == BlockRenderLayer.TRANSLUCENT && !compiledChunkIn.isLayerEmpty(layer)) {
            worldRendererIn.sortVertexData(x, y, z);
            compiledChunkIn.setState(worldRendererIn.getVertexState());
        }
        worldRendererIn.finishDrawing();
    }
    
    private void initModelviewMatrix() {
        GlStateManager.pushMatrix();
        GlStateManager.loadIdentity();
        final float f = 1.000001f;
        GlStateManager.translate(-8.0f, -8.0f, -8.0f);
        GlStateManager.scale(1.000001f, 1.000001f, 1.000001f);
        GlStateManager.translate(8.0f, 8.0f, 8.0f);
        GlStateManager.getFloat(2982, this.modelviewMatrix);
        GlStateManager.popMatrix();
    }
    
    public void multModelviewMatrix() {
        GlStateManager.multMatrix(this.modelviewMatrix);
    }
    
    public CompiledChunk getCompiledChunk() {
        return this.compiledChunk;
    }
    
    public void setCompiledChunk(final CompiledChunk compiledChunkIn) {
        this.lockCompiledChunk.lock();
        try {
            this.compiledChunk = compiledChunkIn;
        }
        finally {
            this.lockCompiledChunk.unlock();
        }
        this.lockCompiledChunk.unlock();
    }
    
    public void stopCompileTask() {
        this.finishCompileTask();
        this.compiledChunk = CompiledChunk.DUMMY;
    }
    
    public void deleteGlResources() {
        this.stopCompileTask();
        this.world = null;
        for (int i = 0; i < BlockRenderLayer.values().length; ++i) {
            if (this.vertexBuffers[i] != null) {
                this.vertexBuffers[i].deleteGlBuffers();
            }
        }
    }
    
    public BlockPos getPosition() {
        return this.position;
    }
    
    public void setNeedsUpdate(boolean needsUpdateIn) {
        if (this.needsUpdate) {
            needsUpdateIn |= this.needsUpdateCustom;
        }
        this.needsUpdate = true;
        this.needsUpdateCustom = needsUpdateIn;
        if (this.isWorldPlayerUpdate()) {
            this.playerUpdate = true;
        }
    }
    
    public void clearNeedsUpdate() {
        this.needsUpdate = false;
        this.needsUpdateCustom = false;
        this.playerUpdate = false;
    }
    
    public boolean isNeedsUpdate() {
        return this.needsUpdate;
    }
    
    public boolean isNeedsUpdateCustom() {
        return this.needsUpdate && this.needsUpdateCustom;
    }
    
    public BlockPos getBlockPosOffset16(final EnumFacing p_181701_1_) {
        return this.mapEnumFacing[p_181701_1_.ordinal()];
    }
    
    public World getWorld() {
        return this.world;
    }
    
    private boolean isWorldPlayerUpdate() {
        if (this.world instanceof WorldClient) {
            final WorldClient worldclient = (WorldClient)this.world;
            return worldclient.isPlayerUpdate();
        }
        return false;
    }
    
    public boolean isPlayerUpdate() {
        return this.playerUpdate;
    }
    
    private BlockRenderLayer fixBlockLayer(final Block p_fixBlockLayer_1_, final BlockRenderLayer p_fixBlockLayer_2_) {
        if (this.isMipmaps) {
            if (p_fixBlockLayer_2_ == BlockRenderLayer.CUTOUT) {
                if (p_fixBlockLayer_1_ instanceof BlockRedstoneWire) {
                    return p_fixBlockLayer_2_;
                }
                if (p_fixBlockLayer_1_ instanceof BlockCactus) {
                    return p_fixBlockLayer_2_;
                }
                return BlockRenderLayer.CUTOUT_MIPPED;
            }
        }
        else if (p_fixBlockLayer_2_ == BlockRenderLayer.CUTOUT_MIPPED) {
            return BlockRenderLayer.CUTOUT;
        }
        return p_fixBlockLayer_2_;
    }
    
    private void postRenderOverlays(final RegionRenderCacheBuilder p_postRenderOverlays_1_, final CompiledChunk p_postRenderOverlays_2_, final boolean[] p_postRenderOverlays_3_) {
        this.postRenderOverlay(BlockRenderLayer.CUTOUT, p_postRenderOverlays_1_, p_postRenderOverlays_2_, p_postRenderOverlays_3_);
        this.postRenderOverlay(BlockRenderLayer.CUTOUT_MIPPED, p_postRenderOverlays_1_, p_postRenderOverlays_2_, p_postRenderOverlays_3_);
        this.postRenderOverlay(BlockRenderLayer.TRANSLUCENT, p_postRenderOverlays_1_, p_postRenderOverlays_2_, p_postRenderOverlays_3_);
    }
    
    private void postRenderOverlay(final BlockRenderLayer p_postRenderOverlay_1_, final RegionRenderCacheBuilder p_postRenderOverlay_2_, final CompiledChunk p_postRenderOverlay_3_, final boolean[] p_postRenderOverlay_4_) {
        final BufferBuilder bufferbuilder = p_postRenderOverlay_2_.getWorldRendererByLayer(p_postRenderOverlay_1_);
        if (bufferbuilder.isDrawing()) {
            p_postRenderOverlay_3_.setLayerStarted(p_postRenderOverlay_1_);
            p_postRenderOverlay_4_[p_postRenderOverlay_1_.ordinal()] = true;
        }
    }
    
    private ChunkCacheOF makeChunkCacheOF() {
        final BlockPos blockpos = this.position.add(-1, -1, -1);
        final ChunkCache chunkcache = this.createRegionRenderCache(this.world, blockpos, this.position.add(16, 16, 16), 1);
        if (Reflector.MinecraftForgeClient_onRebuildChunk.exists()) {
            Reflector.call(Reflector.MinecraftForgeClient_onRebuildChunk, this.world, this.position, chunkcache);
        }
        final ChunkCacheOF chunkcacheof = new ChunkCacheOF(chunkcache, blockpos, 1);
        return chunkcacheof;
    }
    
    public RenderChunk getRenderChunkOffset16(final ViewFrustum p_getRenderChunkOffset16_1_, final EnumFacing p_getRenderChunkOffset16_2_) {
        RenderChunk renderchunk = this.renderChunksOfset16[p_getRenderChunkOffset16_2_.ordinal()];
        if (renderchunk == null) {
            final BlockPos blockpos = this.getBlockPosOffset16(p_getRenderChunkOffset16_2_);
            renderchunk = p_getRenderChunkOffset16_1_.getRenderChunk(blockpos);
            this.renderChunksOfset16[p_getRenderChunkOffset16_2_.ordinal()] = renderchunk;
        }
        return renderchunk;
    }
    
    public Chunk getChunk(final World p_getChunk_1_) {
        if (this.chunk != null && this.chunk.isLoaded()) {
            return this.chunk;
        }
        return this.chunk = p_getChunk_1_.getChunkFromBlockCoords(this.getPosition());
    }
    
    protected ChunkCache createRegionRenderCache(final World p_createRegionRenderCache_1_, final BlockPos p_createRegionRenderCache_2_, final BlockPos p_createRegionRenderCache_3_, final int p_createRegionRenderCache_4_) {
        return new ChunkCache(p_createRegionRenderCache_1_, p_createRegionRenderCache_2_, p_createRegionRenderCache_3_, p_createRegionRenderCache_4_);
    }
}
