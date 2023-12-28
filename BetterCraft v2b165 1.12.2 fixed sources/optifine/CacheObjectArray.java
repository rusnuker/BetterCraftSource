// 
// Decompiled by Procyon v0.6.0
// 

package optifine;

import java.lang.reflect.Array;
import net.minecraft.block.state.IBlockState;
import java.util.ArrayDeque;

public class CacheObjectArray
{
    private static ArrayDeque<int[]> arrays;
    private static int maxCacheSize;
    
    static {
        CacheObjectArray.arrays = new ArrayDeque<int[]>();
        CacheObjectArray.maxCacheSize = 10;
    }
    
    private static synchronized int[] allocateArray(final int p_allocateArray_0_) {
        int[] aint = CacheObjectArray.arrays.pollLast();
        if (aint == null || aint.length < p_allocateArray_0_) {
            aint = new int[p_allocateArray_0_];
        }
        return aint;
    }
    
    public static synchronized void freeArray(final int[] p_freeArray_0_) {
        if (CacheObjectArray.arrays.size() < CacheObjectArray.maxCacheSize) {
            CacheObjectArray.arrays.add(p_freeArray_0_);
        }
    }
    
    public static void main(final String[] p_main_0_) throws Exception {
        final int i = 4096;
        final int j = 500000;
        testNew(i, j);
        testClone(i, j);
        testNewObj(i, j);
        testCloneObj(i, j);
        testNewObjDyn(IBlockState.class, i, j);
        final long k = testNew(i, j);
        final long l = testClone(i, j);
        final long i2 = testNewObj(i, j);
        final long j2 = testCloneObj(i, j);
        final long k2 = testNewObjDyn(IBlockState.class, i, j);
        Config.dbg("New: " + k);
        Config.dbg("Clone: " + l);
        Config.dbg("NewObj: " + i2);
        Config.dbg("CloneObj: " + j2);
        Config.dbg("NewObjDyn: " + k2);
    }
    
    private static long testClone(final int p_testClone_0_, final int p_testClone_1_) {
        final long i = System.currentTimeMillis();
        final int[] aint = new int[p_testClone_0_];
        for (int j = 0; j < p_testClone_1_; ++j) {
            final int[] array = aint.clone();
        }
        final long k = System.currentTimeMillis();
        return k - i;
    }
    
    private static long testNew(final int p_testNew_0_, final int p_testNew_1_) {
        final long i = System.currentTimeMillis();
        for (int j = 0; j < p_testNew_1_; ++j) {
            final int[] array = (int[])Array.newInstance(Integer.TYPE, p_testNew_0_);
        }
        final long k = System.currentTimeMillis();
        return k - i;
    }
    
    private static long testCloneObj(final int p_testCloneObj_0_, final int p_testCloneObj_1_) {
        final long i = System.currentTimeMillis();
        final IBlockState[] aiblockstate = new IBlockState[p_testCloneObj_0_];
        for (int j = 0; j < p_testCloneObj_1_; ++j) {
            final IBlockState[] array = aiblockstate.clone();
        }
        final long k = System.currentTimeMillis();
        return k - i;
    }
    
    private static long testNewObj(final int p_testNewObj_0_, final int p_testNewObj_1_) {
        final long i = System.currentTimeMillis();
        for (int j = 0; j < p_testNewObj_1_; ++j) {
            final IBlockState[] array = new IBlockState[p_testNewObj_0_];
        }
        final long k = System.currentTimeMillis();
        return k - i;
    }
    
    private static long testNewObjDyn(final Class p_testNewObjDyn_0_, final int p_testNewObjDyn_1_, final int p_testNewObjDyn_2_) {
        final long i = System.currentTimeMillis();
        for (int j = 0; j < p_testNewObjDyn_2_; ++j) {
            final Object[] array = (Object[])Array.newInstance(p_testNewObjDyn_0_, p_testNewObjDyn_1_);
        }
        final long k = System.currentTimeMillis();
        return k - i;
    }
}
