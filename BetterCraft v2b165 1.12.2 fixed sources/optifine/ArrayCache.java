// 
// Decompiled by Procyon v0.6.0
// 

package optifine;

import java.lang.reflect.Array;
import java.util.ArrayDeque;

public class ArrayCache
{
    private Class elementClass;
    private int maxCacheSize;
    private ArrayDeque cache;
    
    public ArrayCache(final Class p_i10_1_, final int p_i10_2_) {
        this.elementClass = null;
        this.maxCacheSize = 0;
        this.cache = new ArrayDeque();
        this.elementClass = p_i10_1_;
        this.maxCacheSize = p_i10_2_;
    }
    
    public synchronized Object allocate(final int p_allocate_1_) {
        Object object = this.cache.pollLast();
        if (object == null || Array.getLength(object) < p_allocate_1_) {
            object = Array.newInstance(this.elementClass, p_allocate_1_);
        }
        return object;
    }
    
    public synchronized void free(final Object p_free_1_) {
        if (p_free_1_ != null) {
            final Class oclass = p_free_1_.getClass();
            if (oclass.getComponentType() != this.elementClass) {
                throw new IllegalArgumentException("Wrong component type");
            }
            if (this.cache.size() < this.maxCacheSize) {
                this.cache.add(p_free_1_);
            }
        }
    }
}
