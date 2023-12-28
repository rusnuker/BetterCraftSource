// 
// Decompiled by Procyon v0.6.0
// 

package net.minecraft.world.chunk;

import javax.annotation.Nullable;

public interface IChunkProvider
{
    @Nullable
    Chunk getLoadedChunk(final int p0, final int p1);
    
    Chunk provideChunk(final int p0, final int p1);
    
    boolean unloadQueuedChunks();
    
    String makeString();
    
    boolean func_191062_e(final int p0, final int p1);
}
