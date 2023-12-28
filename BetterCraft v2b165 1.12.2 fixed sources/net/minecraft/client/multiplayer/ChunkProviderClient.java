// 
// Decompiled by Procyon v0.6.0
// 

package net.minecraft.client.multiplayer;

import it.unimi.dsi.fastutil.objects.ObjectIterator;
import com.google.common.base.MoreObjects;
import javax.annotation.Nullable;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.chunk.EmptyChunk;
import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap;
import org.apache.logging.log4j.LogManager;
import net.minecraft.world.World;
import it.unimi.dsi.fastutil.longs.Long2ObjectMap;
import net.minecraft.world.chunk.Chunk;
import org.apache.logging.log4j.Logger;
import net.minecraft.world.chunk.IChunkProvider;

public class ChunkProviderClient implements IChunkProvider
{
    private static final Logger LOGGER;
    private final Chunk blankChunk;
    private final Long2ObjectMap<Chunk> chunkMapping;
    private final World worldObj;
    
    static {
        LOGGER = LogManager.getLogger();
    }
    
    public ChunkProviderClient(final World worldIn) {
        this.chunkMapping = new Long2ObjectOpenHashMap<Chunk>(8192) {
            @Override
            protected void rehash(final int p_rehash_1_) {
                if (p_rehash_1_ > this.key.length) {
                    super.rehash(p_rehash_1_);
                }
            }
        };
        this.blankChunk = new EmptyChunk(worldIn, 0, 0);
        this.worldObj = worldIn;
    }
    
    public void unloadChunk(final int x, final int z) {
        final Chunk chunk = this.provideChunk(x, z);
        if (!chunk.isEmpty()) {
            chunk.onChunkUnload();
        }
        this.chunkMapping.remove(ChunkPos.asLong(x, z));
    }
    
    @Nullable
    @Override
    public Chunk getLoadedChunk(final int x, final int z) {
        return this.chunkMapping.get(ChunkPos.asLong(x, z));
    }
    
    public Chunk loadChunk(final int chunkX, final int chunkZ) {
        final Chunk chunk = new Chunk(this.worldObj, chunkX, chunkZ);
        this.chunkMapping.put(ChunkPos.asLong(chunkX, chunkZ), chunk);
        chunk.setChunkLoaded(true);
        return chunk;
    }
    
    @Override
    public Chunk provideChunk(final int x, final int z) {
        return MoreObjects.firstNonNull(this.getLoadedChunk(x, z), this.blankChunk);
    }
    
    @Override
    public boolean unloadQueuedChunks() {
        final long i = System.currentTimeMillis();
        for (final Chunk chunk : this.chunkMapping.values()) {
            chunk.onTick(System.currentTimeMillis() - i > 5L);
        }
        if (System.currentTimeMillis() - i > 100L) {
            ChunkProviderClient.LOGGER.info("Warning: Clientside chunk ticking took {} ms", (Object)(System.currentTimeMillis() - i));
        }
        return false;
    }
    
    @Override
    public String makeString() {
        return "MultiplayerChunkCache: " + this.chunkMapping.size() + ", " + this.chunkMapping.size();
    }
    
    @Override
    public boolean func_191062_e(final int p_191062_1_, final int p_191062_2_) {
        return this.chunkMapping.containsKey(ChunkPos.asLong(p_191062_1_, p_191062_2_));
    }
}
