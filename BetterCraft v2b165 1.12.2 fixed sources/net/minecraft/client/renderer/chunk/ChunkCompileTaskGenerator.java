// 
// Decompiled by Procyon v0.6.0
// 

package net.minecraft.client.renderer.chunk;

import com.google.common.primitives.Doubles;
import java.util.Iterator;
import com.google.common.collect.Lists;
import net.minecraft.client.renderer.RegionRenderCacheBuilder;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;

public class ChunkCompileTaskGenerator implements Comparable<ChunkCompileTaskGenerator>
{
    private final RenderChunk renderChunk;
    private final ReentrantLock lock;
    private final List<Runnable> listFinishRunnables;
    private final Type type;
    private final double distanceSq;
    private RegionRenderCacheBuilder regionRenderCacheBuilder;
    private CompiledChunk compiledChunk;
    private Status status;
    private boolean finished;
    
    public ChunkCompileTaskGenerator(final RenderChunk p_i46560_1_, final Type p_i46560_2_, final double p_i46560_3_) {
        this.lock = new ReentrantLock();
        this.listFinishRunnables = (List<Runnable>)Lists.newArrayList();
        this.status = Status.PENDING;
        this.renderChunk = p_i46560_1_;
        this.type = p_i46560_2_;
        this.distanceSq = p_i46560_3_;
    }
    
    public Status getStatus() {
        return this.status;
    }
    
    public RenderChunk getRenderChunk() {
        return this.renderChunk;
    }
    
    public CompiledChunk getCompiledChunk() {
        return this.compiledChunk;
    }
    
    public void setCompiledChunk(final CompiledChunk compiledChunkIn) {
        this.compiledChunk = compiledChunkIn;
    }
    
    public RegionRenderCacheBuilder getRegionRenderCacheBuilder() {
        return this.regionRenderCacheBuilder;
    }
    
    public void setRegionRenderCacheBuilder(final RegionRenderCacheBuilder regionRenderCacheBuilderIn) {
        this.regionRenderCacheBuilder = regionRenderCacheBuilderIn;
    }
    
    public void setStatus(final Status statusIn) {
        this.lock.lock();
        try {
            this.status = statusIn;
        }
        finally {
            this.lock.unlock();
        }
        this.lock.unlock();
    }
    
    public void finish() {
        this.lock.lock();
        try {
            if (this.type == Type.REBUILD_CHUNK && this.status != Status.DONE) {
                this.renderChunk.setNeedsUpdate(false);
            }
            this.finished = true;
            this.status = Status.DONE;
            for (final Runnable runnable : this.listFinishRunnables) {
                runnable.run();
            }
        }
        finally {
            this.lock.unlock();
        }
        this.lock.unlock();
    }
    
    public void addFinishRunnable(final Runnable runnable) {
        this.lock.lock();
        try {
            this.listFinishRunnables.add(runnable);
            if (this.finished) {
                runnable.run();
            }
        }
        finally {
            this.lock.unlock();
        }
        this.lock.unlock();
    }
    
    public ReentrantLock getLock() {
        return this.lock;
    }
    
    public Type getType() {
        return this.type;
    }
    
    public boolean isFinished() {
        return this.finished;
    }
    
    @Override
    public int compareTo(final ChunkCompileTaskGenerator p_compareTo_1_) {
        return Doubles.compare(this.distanceSq, p_compareTo_1_.distanceSq);
    }
    
    public double getDistanceSq() {
        return this.distanceSq;
    }
    
    public enum Status
    {
        PENDING("PENDING", 0), 
        COMPILING("COMPILING", 1), 
        UPLOADING("UPLOADING", 2), 
        DONE("DONE", 3);
        
        private Status(final String s, final int n) {
        }
    }
    
    public enum Type
    {
        REBUILD_CHUNK("REBUILD_CHUNK", 0), 
        RESORT_TRANSPARENCY("RESORT_TRANSPARENCY", 1);
        
        private Type(final String s, final int n) {
        }
    }
}
