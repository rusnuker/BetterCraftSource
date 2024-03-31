// 
// Decompiled by Procyon v0.6.0
// 

package net.minecraft.client.util;

import java.util.Set;
import it.unimi.dsi.fastutil.ints.IntSet;
import java.util.Collections;
import com.google.common.collect.Sets;
import it.unimi.dsi.fastutil.ints.IntOpenHashSet;
import it.unimi.dsi.fastutil.Swapper;
import it.unimi.dsi.fastutil.Arrays;
import it.unimi.dsi.fastutil.ints.IntComparator;
import it.unimi.dsi.fastutil.ints.IntArrayList;
import com.google.common.collect.Lists;
import org.apache.logging.log4j.LogManager;
import it.unimi.dsi.fastutil.ints.IntList;
import java.util.List;
import org.apache.logging.log4j.Logger;

public class SuffixArray<T>
{
    private static final boolean field_194062_b;
    private static final boolean field_194063_c;
    private static final Logger field_194064_d;
    protected final List<T> field_194061_a;
    private final IntList field_194065_e;
    private final IntList field_194066_f;
    private IntList field_194067_g;
    private IntList field_194068_h;
    private int field_194069_i;
    
    static {
        field_194062_b = Boolean.parseBoolean(System.getProperty("SuffixArray.printComparisons", "false"));
        field_194063_c = Boolean.parseBoolean(System.getProperty("SuffixArray.printArray", "false"));
        field_194064_d = LogManager.getLogger();
    }
    
    public SuffixArray() {
        this.field_194061_a = (List<T>)Lists.newArrayList();
        this.field_194065_e = new IntArrayList();
        this.field_194066_f = new IntArrayList();
        this.field_194067_g = new IntArrayList();
        this.field_194068_h = new IntArrayList();
    }
    
    public void func_194057_a(final T p_194057_1_, final String p_194057_2_) {
        this.field_194069_i = Math.max(this.field_194069_i, p_194057_2_.length());
        final int i = this.field_194061_a.size();
        this.field_194061_a.add(p_194057_1_);
        this.field_194066_f.add(this.field_194065_e.size());
        for (int j = 0; j < p_194057_2_.length(); ++j) {
            this.field_194067_g.add(i);
            this.field_194068_h.add(j);
            this.field_194065_e.add(p_194057_2_.charAt(j));
        }
        this.field_194067_g.add(i);
        this.field_194068_h.add(p_194057_2_.length());
        this.field_194065_e.add(-1);
    }
    
    public void func_194058_a() {
        final int i = this.field_194065_e.size();
        final int[] aint = new int[i];
        final int[] aint2 = new int[i];
        final int[] aint3 = new int[i];
        final int[] aint4 = new int[i];
        final IntComparator intcomparator = new IntComparator() {
            @Override
            public int compare(final int p_compare_1_, final int p_compare_2_) {
                return (aint2[p_compare_1_] == aint2[p_compare_2_]) ? Integer.compare(aint3[p_compare_1_], aint3[p_compare_2_]) : Integer.compare(aint2[p_compare_1_], aint2[p_compare_2_]);
            }
            
            @Override
            public int compare(final Integer p_compare_1_, final Integer p_compare_2_) {
                return this.compare((int)p_compare_1_, (int)p_compare_2_);
            }
        };
        final Swapper swapper = (p_194054_3_, p_194054_4_) -> {
            if (p_194054_3_ != p_194054_4_) {
                final int i3 = array[p_194054_3_];
                array[p_194054_3_] = array[p_194054_4_];
                array[p_194054_4_] = i3;
                final int i4 = array2[p_194054_3_];
                array2[p_194054_3_] = array2[p_194054_4_];
                array2[p_194054_4_] = i4;
                final int i5 = array3[p_194054_3_];
                array3[p_194054_3_] = array3[p_194054_4_];
                array3[p_194054_4_] = i5;
            }
            return;
        };
        for (int j = 0; j < i; ++j) {
            aint[j] = this.field_194065_e.getInt(j);
        }
        for (int k1 = 1, l = Math.min(i, this.field_194069_i); k1 * 2 < l; k1 *= 2) {
            for (int m = 0; m < i; aint4[m] = m++) {
                aint2[m] = aint[m];
                aint3[m] = ((m + k1 < i) ? aint[m + k1] : -2);
            }
            Arrays.quickSort(0, i, intcomparator, swapper);
            for (int l2 = 0; l2 < i; ++l2) {
                if (l2 > 0 && aint2[l2] == aint2[l2 - 1] && aint3[l2] == aint3[l2 - 1]) {
                    aint[aint4[l2]] = aint[aint4[l2 - 1]];
                }
                else {
                    aint[aint4[l2]] = l2;
                }
            }
        }
        final IntList intlist1 = this.field_194067_g;
        final IntList intlist2 = this.field_194068_h;
        this.field_194067_g = new IntArrayList(intlist1.size());
        this.field_194068_h = new IntArrayList(intlist2.size());
        for (final int j2 : aint4) {
            this.field_194067_g.add(intlist1.getInt(j2));
            this.field_194068_h.add(intlist2.getInt(j2));
        }
        if (SuffixArray.field_194063_c) {
            this.func_194060_b();
        }
    }
    
    private void func_194060_b() {
        for (int i2 = 0; i2 < this.field_194067_g.size(); ++i2) {
            SuffixArray.field_194064_d.debug("{} {}", (Object)i2, this.func_194059_a(i2));
        }
        SuffixArray.field_194064_d.debug("");
    }
    
    private String func_194059_a(final int p_194059_1_) {
        final int i2 = this.field_194068_h.getInt(p_194059_1_);
        final int j2 = this.field_194066_f.getInt(this.field_194067_g.getInt(p_194059_1_));
        final StringBuilder stringbuilder = new StringBuilder();
        for (int k2 = 0; j2 + k2 < this.field_194065_e.size(); ++k2) {
            if (k2 == i2) {
                stringbuilder.append('^');
            }
            final int l2 = this.field_194065_e.get(j2 + k2);
            if (l2 == -1) {
                break;
            }
            stringbuilder.append((char)l2);
        }
        return stringbuilder.toString();
    }
    
    private int func_194056_a(final String p_194056_1_, final int p_194056_2_) {
        final int i2 = this.field_194066_f.getInt(this.field_194067_g.getInt(p_194056_2_));
        final int j2 = this.field_194068_h.getInt(p_194056_2_);
        for (int k2 = 0; k2 < p_194056_1_.length(); ++k2) {
            final int l2 = this.field_194065_e.getInt(i2 + j2 + k2);
            if (l2 == -1) {
                return 1;
            }
            final char c0 = p_194056_1_.charAt(k2);
            final char c2 = (char)l2;
            if (c0 < c2) {
                return -1;
            }
            if (c0 > c2) {
                return 1;
            }
        }
        return 0;
    }
    
    public List<T> func_194055_a(final String p_194055_1_) {
        final int i2 = this.field_194067_g.size();
        int j2 = 0;
        int k2 = i2;
        while (j2 < k2) {
            final int l2 = j2 + (k2 - j2) / 2;
            final int i3 = this.func_194056_a(p_194055_1_, l2);
            if (SuffixArray.field_194062_b) {
                SuffixArray.field_194064_d.debug("comparing lower \"{}\" with {} \"{}\": {}", p_194055_1_, l2, this.func_194059_a(l2), i3);
            }
            if (i3 > 0) {
                j2 = l2 + 1;
            }
            else {
                k2 = l2;
            }
        }
        if (j2 >= 0 && j2 < i2) {
            final int i4 = j2;
            k2 = i2;
            while (j2 < k2) {
                final int j3 = j2 + (k2 - j2) / 2;
                final int j4 = this.func_194056_a(p_194055_1_, j3);
                if (SuffixArray.field_194062_b) {
                    SuffixArray.field_194064_d.debug("comparing upper \"{}\" with {} \"{}\": {}", p_194055_1_, j3, this.func_194059_a(j3), j4);
                }
                if (j4 >= 0) {
                    j2 = j3 + 1;
                }
                else {
                    k2 = j3;
                }
            }
            final int k3 = j2;
            final IntSet intset = new IntOpenHashSet();
            for (int k4 = i4; k4 < k3; ++k4) {
                intset.add(this.field_194067_g.getInt(k4));
            }
            final int[] aint4 = intset.toIntArray();
            java.util.Arrays.sort(aint4);
            final Set<T> set = (Set<T>)Sets.newLinkedHashSet();
            int[] array;
            for (int length = (array = aint4).length, n = 0; n < length; ++n) {
                final int l3 = array[n];
                set.add(this.field_194061_a.get(l3));
            }
            return (List<T>)Lists.newArrayList((Iterable<?>)set);
        }
        return Collections.emptyList();
    }
}
