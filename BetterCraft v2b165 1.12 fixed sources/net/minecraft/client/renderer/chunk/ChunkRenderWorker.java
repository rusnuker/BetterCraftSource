// 
// Decompiled by Procyon v0.6.0
// 

package net.minecraft.client.renderer.chunk;

import java.util.ArrayList;
import net.minecraft.world.World;
import java.util.concurrent.CancellationException;
import java.util.List;
import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.Futures;
import net.minecraft.util.BlockRenderLayer;
import com.google.common.collect.Lists;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.Vec3i;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.client.Minecraft;
import net.minecraft.crash.CrashReport;
import javax.annotation.Nullable;
import org.apache.logging.log4j.LogManager;
import net.minecraft.client.renderer.RegionRenderCacheBuilder;
import org.apache.logging.log4j.Logger;

public class ChunkRenderWorker implements Runnable
{
    private static final Logger LOGGER;
    private final ChunkRenderDispatcher chunkRenderDispatcher;
    private final RegionRenderCacheBuilder regionRenderCacheBuilder;
    private boolean shouldRun;
    
    static {
        LOGGER = LogManager.getLogger();
    }
    
    public ChunkRenderWorker(final ChunkRenderDispatcher p_i46201_1_) {
        this(p_i46201_1_, null);
    }
    
    public ChunkRenderWorker(final ChunkRenderDispatcher chunkRenderDispatcherIn, @Nullable final RegionRenderCacheBuilder regionRenderCacheBuilderIn) {
        this.shouldRun = true;
        this.chunkRenderDispatcher = chunkRenderDispatcherIn;
        this.regionRenderCacheBuilder = regionRenderCacheBuilderIn;
    }
    
    @Override
    public void run() {
        while (this.shouldRun) {
            try {
                this.processTask(this.chunkRenderDispatcher.getNextChunkUpdate());
            }
            catch (final InterruptedException var3) {
                ChunkRenderWorker.LOGGER.debug("Stopping chunk worker due to interrupt");
            }
            catch (final Throwable throwable) {
                final CrashReport crashreport = CrashReport.makeCrashReport(throwable, "Batching chunks");
                Minecraft.getMinecraft().crashed(Minecraft.getMinecraft().addGraphicsAndWorldToCrashReport(crashreport));
            }
        }
    }
    
    protected void processTask(final ChunkCompileTaskGenerator generator) throws InterruptedException {
        generator.getLock().lock();
        try {
            if (generator.getStatus() != ChunkCompileTaskGenerator.Status.PENDING) {
                if (!generator.isFinished()) {
                    ChunkRenderWorker.LOGGER.warn("Chunk render task was {} when I expected it to be pending; ignoring task", generator.getStatus());
                }
                return;
            }
            final BlockPos blockpos = new BlockPos(Minecraft.getMinecraft().player);
            final BlockPos blockpos2 = generator.getRenderChunk().getPosition();
            final int i = 16;
            final int j = 8;
            final int k = 24;
            if (blockpos2.add(8, 8, 8).distanceSq(blockpos) > 576.0) {
                final World world = generator.getRenderChunk().getWorld();
                final BlockPos.MutableBlockPos blockpos$mutableblockpos = new BlockPos.MutableBlockPos(blockpos2);
                if (!this.isChunkExisting(blockpos$mutableblockpos.setPos(blockpos2).move(EnumFacing.WEST, 16), world) || !this.isChunkExisting(blockpos$mutableblockpos.setPos(blockpos2).move(EnumFacing.NORTH, 16), world) || !this.isChunkExisting(blockpos$mutableblockpos.setPos(blockpos2).move(EnumFacing.EAST, 16), world) || !this.isChunkExisting(blockpos$mutableblockpos.setPos(blockpos2).move(EnumFacing.SOUTH, 16), world)) {
                    return;
                }
            }
            generator.setStatus(ChunkCompileTaskGenerator.Status.COMPILING);
        }
        finally {
            generator.getLock().unlock();
        }
        generator.getLock().unlock();
        final Entity entity1 = Minecraft.getMinecraft().getRenderViewEntity();
        if (entity1 == null) {
            generator.finish();
        }
        else {
            generator.setRegionRenderCacheBuilder(this.getRegionRenderCacheBuilder());
            final float f = (float)entity1.posX;
            final float f2 = (float)entity1.posY + entity1.getEyeHeight();
            final float f3 = (float)entity1.posZ;
            final ChunkCompileTaskGenerator.Type chunkcompiletaskgenerator$type = generator.getType();
            if (chunkcompiletaskgenerator$type == ChunkCompileTaskGenerator.Type.REBUILD_CHUNK) {
                generator.getRenderChunk().rebuildChunk(f, f2, f3, generator);
            }
            else if (chunkcompiletaskgenerator$type == ChunkCompileTaskGenerator.Type.RESORT_TRANSPARENCY) {
                generator.getRenderChunk().resortTransparency(f, f2, f3, generator);
            }
            generator.getLock().lock();
            try {
                if (generator.getStatus() != ChunkCompileTaskGenerator.Status.COMPILING) {
                    if (!generator.isFinished()) {
                        ChunkRenderWorker.LOGGER.warn("Chunk render task was {} when I expected it to be compiling; aborting task", generator.getStatus());
                    }
                    this.freeRenderBuilder(generator);
                    return;
                }
                generator.setStatus(ChunkCompileTaskGenerator.Status.UPLOADING);
            }
            finally {
                generator.getLock().unlock();
            }
            generator.getLock().unlock();
            final CompiledChunk compiledchunk1 = generator.getCompiledChunk();
            final ArrayList arraylist1 = Lists.newArrayList();
            if (chunkcompiletaskgenerator$type == ChunkCompileTaskGenerator.Type.REBUILD_CHUNK) {
                BlockRenderLayer[] values;
                for (int length = (values = BlockRenderLayer.values()).length, l = 0; l < length; ++l) {
                    final BlockRenderLayer blockrenderlayer = values[l];
                    if (compiledchunk1.isLayerStarted(blockrenderlayer)) {
                        arraylist1.add(this.chunkRenderDispatcher.uploadChunk(blockrenderlayer, generator.getRegionRenderCacheBuilder().getWorldRendererByLayer(blockrenderlayer), generator.getRenderChunk(), compiledchunk1, generator.getDistanceSq()));
                    }
                }
            }
            else if (chunkcompiletaskgenerator$type == ChunkCompileTaskGenerator.Type.RESORT_TRANSPARENCY) {
                arraylist1.add(this.chunkRenderDispatcher.uploadChunk(BlockRenderLayer.TRANSLUCENT, generator.getRegionRenderCacheBuilder().getWorldRendererByLayer(BlockRenderLayer.TRANSLUCENT), generator.getRenderChunk(), compiledchunk1, generator.getDistanceSq()));
            }
            final ListenableFuture<List<Object>> listenablefuture = Futures.allAsList((Iterable<? extends ListenableFuture<?>>)arraylist1);
            generator.addFinishRunnable(new Runnable() {
                @Override
                public void run() {
                    listenablefuture.cancel(false);
                }
            });
            Futures.addCallback(listenablefuture, new FutureCallback<List<Object>>() {
                @Override
                public void onSuccess(@Nullable final List<Object> p_onSuccess_1_) {
                    ChunkRenderWorker.this.freeRenderBuilder(generator);
                    generator.getLock().lock();
                    try {
                        if (generator.getStatus() != ChunkCompileTaskGenerator.Status.UPLOADING) {
                            if (!generator.isFinished()) {
                                ChunkRenderWorker.LOGGER.warn("Chunk render task was {} when I expected it to be uploading; aborting task", generator.getStatus());
                            }
                            return;
                        }
                        generator.setStatus(ChunkCompileTaskGenerator.Status.DONE);
                    }
                    finally {
                        generator.getLock().unlock();
                    }
                    generator.getLock().unlock();
                    generator.getRenderChunk().setCompiledChunk(compiledchunk1);
                }
                
                @Override
                public void onFailure(final Throwable p_onFailure_1_) {
                    ChunkRenderWorker.this.freeRenderBuilder(generator);
                    if (!(p_onFailure_1_ instanceof CancellationException) && !(p_onFailure_1_ instanceof InterruptedException)) {
                        Minecraft.getMinecraft().crashed(CrashReport.makeCrashReport(p_onFailure_1_, "Rendering chunk"));
                    }
                }
            });
        }
    }
    
    private boolean isChunkExisting(final BlockPos p_188263_1_, final World p_188263_2_) {
        return p_188263_2_ != null && !p_188263_2_.getChunkFromChunkCoords(p_188263_1_.getX() >> 4, p_188263_1_.getZ() >> 4).isEmpty();
    }
    
    private RegionRenderCacheBuilder getRegionRenderCacheBuilder() throws InterruptedException {
        return (this.regionRenderCacheBuilder != null) ? this.regionRenderCacheBuilder : this.chunkRenderDispatcher.allocateRenderBuilder();
    }
    
    private void freeRenderBuilder(final ChunkCompileTaskGenerator taskGenerator) {
        if (this.regionRenderCacheBuilder == null) {
            this.chunkRenderDispatcher.freeRenderBuilder(taskGenerator.getRegionRenderCacheBuilder());
        }
    }
    
    public void notifyToStop() {
        this.shouldRun = false;
    }
}
