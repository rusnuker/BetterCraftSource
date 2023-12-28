// 
// Decompiled by Procyon v0.6.0
// 

package net.minecraft.world.gen;

import net.minecraft.world.biome.Biome;
import net.minecraft.util.math.BlockPos;
import net.minecraft.entity.EnumCreatureType;
import java.util.Iterator;
import java.util.List;
import com.google.common.collect.Lists;
import net.minecraft.world.MinecraftException;
import java.io.IOException;
import net.minecraft.world.World;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.util.ReportedException;
import net.minecraft.crash.CrashReport;
import javax.annotation.Nullable;
import it.unimi.dsi.fastutil.objects.ObjectIterator;
import net.minecraft.util.math.ChunkPos;
import java.util.Collection;
import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap;
import com.google.common.collect.Sets;
import org.apache.logging.log4j.LogManager;
import net.minecraft.world.WorldServer;
import net.minecraft.world.chunk.Chunk;
import it.unimi.dsi.fastutil.longs.Long2ObjectMap;
import net.minecraft.world.chunk.storage.IChunkLoader;
import java.util.Set;
import org.apache.logging.log4j.Logger;
import net.minecraft.world.chunk.IChunkProvider;

public class ChunkProviderServer implements IChunkProvider
{
    private static final Logger LOGGER;
    private final Set<Long> droppedChunksSet;
    private final IChunkGenerator chunkGenerator;
    private final IChunkLoader chunkLoader;
    private final Long2ObjectMap<Chunk> id2ChunkMap;
    private final WorldServer worldObj;
    
    static {
        LOGGER = LogManager.getLogger();
    }
    
    public ChunkProviderServer(final WorldServer worldObjIn, final IChunkLoader chunkLoaderIn, final IChunkGenerator chunkGeneratorIn) {
        this.droppedChunksSet = (Set<Long>)Sets.newHashSet();
        this.id2ChunkMap = new Long2ObjectOpenHashMap<Chunk>(8192);
        this.worldObj = worldObjIn;
        this.chunkLoader = chunkLoaderIn;
        this.chunkGenerator = chunkGeneratorIn;
    }
    
    public Collection<Chunk> getLoadedChunks() {
        return this.id2ChunkMap.values();
    }
    
    public void unload(final Chunk chunkIn) {
        if (this.worldObj.provider.canDropChunk(chunkIn.xPosition, chunkIn.zPosition)) {
            this.droppedChunksSet.add(ChunkPos.asLong(chunkIn.xPosition, chunkIn.zPosition));
            chunkIn.unloaded = true;
        }
    }
    
    public void unloadAllChunks() {
        for (final Chunk chunk : this.id2ChunkMap.values()) {
            this.unload(chunk);
        }
    }
    
    @Nullable
    @Override
    public Chunk getLoadedChunk(final int x, final int z) {
        final long i = ChunkPos.asLong(x, z);
        final Chunk chunk = this.id2ChunkMap.get(i);
        if (chunk != null) {
            chunk.unloaded = false;
        }
        return chunk;
    }
    
    @Nullable
    public Chunk loadChunk(final int x, final int z) {
        Chunk chunk = this.getLoadedChunk(x, z);
        if (chunk == null) {
            chunk = this.loadChunkFromFile(x, z);
            if (chunk != null) {
                this.id2ChunkMap.put(ChunkPos.asLong(x, z), chunk);
                chunk.onChunkLoad();
                chunk.populateChunk(this, this.chunkGenerator);
            }
        }
        return chunk;
    }
    
    @Override
    public Chunk provideChunk(final int x, final int z) {
        Chunk chunk = this.loadChunk(x, z);
        if (chunk == null) {
            final long i = ChunkPos.asLong(x, z);
            try {
                chunk = this.chunkGenerator.provideChunk(x, z);
            }
            catch (final Throwable throwable) {
                final CrashReport crashreport = CrashReport.makeCrashReport(throwable, "Exception generating new chunk");
                final CrashReportCategory crashreportcategory = crashreport.makeCategory("Chunk to be generated");
                crashreportcategory.addCrashSection("Location", String.format("%d,%d", x, z));
                crashreportcategory.addCrashSection("Position hash", i);
                crashreportcategory.addCrashSection("Generator", this.chunkGenerator);
                throw new ReportedException(crashreport);
            }
            this.id2ChunkMap.put(i, chunk);
            chunk.onChunkLoad();
            chunk.populateChunk(this, this.chunkGenerator);
        }
        return chunk;
    }
    
    @Nullable
    private Chunk loadChunkFromFile(final int x, final int z) {
        try {
            final Chunk chunk = this.chunkLoader.loadChunk(this.worldObj, x, z);
            if (chunk != null) {
                chunk.setLastSaveTime(this.worldObj.getTotalWorldTime());
                this.chunkGenerator.recreateStructures(chunk, x, z);
            }
            return chunk;
        }
        catch (final Exception exception) {
            ChunkProviderServer.LOGGER.error("Couldn't load chunk", exception);
            return null;
        }
    }
    
    private void saveChunkExtraData(final Chunk chunkIn) {
        try {
            this.chunkLoader.saveExtraChunkData(this.worldObj, chunkIn);
        }
        catch (final Exception exception) {
            ChunkProviderServer.LOGGER.error("Couldn't save entities", exception);
        }
    }
    
    private void saveChunkData(final Chunk chunkIn) {
        try {
            chunkIn.setLastSaveTime(this.worldObj.getTotalWorldTime());
            this.chunkLoader.saveChunk(this.worldObj, chunkIn);
        }
        catch (final IOException ioexception) {
            ChunkProviderServer.LOGGER.error("Couldn't save chunk", ioexception);
        }
        catch (final MinecraftException minecraftexception) {
            ChunkProviderServer.LOGGER.error("Couldn't save chunk; already in use by another instance of Minecraft?", minecraftexception);
        }
    }
    
    public boolean saveChunks(final boolean p_186027_1_) {
        int i = 0;
        final List<Chunk> list = (List<Chunk>)Lists.newArrayList((Iterable<?>)this.id2ChunkMap.values());
        for (int j = 0; j < list.size(); ++j) {
            final Chunk chunk = list.get(j);
            if (p_186027_1_) {
                this.saveChunkExtraData(chunk);
            }
            if (chunk.needsSaving(p_186027_1_)) {
                this.saveChunkData(chunk);
                chunk.setModified(false);
                if (++i == 24 && !p_186027_1_) {
                    return false;
                }
            }
        }
        return true;
    }
    
    public void saveExtraData() {
        this.chunkLoader.saveExtraData();
    }
    
    @Override
    public boolean unloadQueuedChunks() {
        if (!this.worldObj.disableLevelSaving) {
            if (!this.droppedChunksSet.isEmpty()) {
                final Iterator<Long> iterator = this.droppedChunksSet.iterator();
                int i = 0;
                while (i < 100 && iterator.hasNext()) {
                    final Long olong = iterator.next();
                    final Chunk chunk = this.id2ChunkMap.get(olong);
                    if (chunk != null && chunk.unloaded) {
                        chunk.onChunkUnload();
                        this.saveChunkData(chunk);
                        this.saveChunkExtraData(chunk);
                        this.id2ChunkMap.remove(olong);
                        ++i;
                    }
                    iterator.remove();
                }
            }
            this.chunkLoader.chunkTick();
        }
        return false;
    }
    
    public boolean canSave() {
        return !this.worldObj.disableLevelSaving;
    }
    
    @Override
    public String makeString() {
        return "ServerChunkCache: " + this.id2ChunkMap.size() + " Drop: " + this.droppedChunksSet.size();
    }
    
    public List<Biome.SpawnListEntry> getPossibleCreatures(final EnumCreatureType creatureType, final BlockPos pos) {
        return this.chunkGenerator.getPossibleCreatures(creatureType, pos);
    }
    
    @Nullable
    public BlockPos getStrongholdGen(final World worldIn, final String structureName, final BlockPos position, final boolean p_180513_4_) {
        return this.chunkGenerator.getStrongholdGen(worldIn, structureName, position, p_180513_4_);
    }
    
    public boolean func_193413_a(final World p_193413_1_, final String p_193413_2_, final BlockPos p_193413_3_) {
        return this.chunkGenerator.func_193414_a(p_193413_1_, p_193413_2_, p_193413_3_);
    }
    
    public int getLoadedChunkCount() {
        return this.id2ChunkMap.size();
    }
    
    public boolean chunkExists(final int x, final int z) {
        return this.id2ChunkMap.containsKey(ChunkPos.asLong(x, z));
    }
    
    @Override
    public boolean func_191062_e(final int p_191062_1_, final int p_191062_2_) {
        return this.id2ChunkMap.containsKey(ChunkPos.asLong(p_191062_1_, p_191062_2_)) || this.chunkLoader.func_191063_a(p_191062_1_, p_191062_2_);
    }
}
