// 
// Decompiled by Procyon v0.6.0
// 

package net.jpountz.xxhash;

final class StreamingXXHash32JNI extends StreamingXXHash32
{
    private long state;
    
    StreamingXXHash32JNI(final int seed) {
        super(seed);
        this.state = XXHashJNI.XXH32_init(seed);
    }
    
    private void checkState() {
        if (this.state == 0L) {
            throw new AssertionError((Object)"Already finalized");
        }
    }
    
    @Override
    public synchronized void reset() {
        this.checkState();
        XXHashJNI.XXH32_free(this.state);
        this.state = XXHashJNI.XXH32_init(this.seed);
    }
    
    @Override
    public synchronized int getValue() {
        this.checkState();
        return XXHashJNI.XXH32_digest(this.state);
    }
    
    @Override
    public synchronized void update(final byte[] bytes, final int off, final int len) {
        this.checkState();
        XXHashJNI.XXH32_update(this.state, bytes, off, len);
    }
    
    @Override
    public synchronized void close() {
        if (this.state != 0L) {
            super.close();
            XXHashJNI.XXH32_free(this.state);
            this.state = 0L;
        }
    }
    
    @Override
    protected synchronized void finalize() throws Throwable {
        super.finalize();
        if (this.state != 0L) {
            XXHashJNI.XXH32_free(this.state);
            this.state = 0L;
        }
    }
    
    static class Factory implements StreamingXXHash32.Factory
    {
        public static final StreamingXXHash32.Factory INSTANCE;
        
        @Override
        public StreamingXXHash32 newStreamingHash(final int seed) {
            return new StreamingXXHash32JNI(seed);
        }
        
        static {
            INSTANCE = new Factory();
        }
    }
}
