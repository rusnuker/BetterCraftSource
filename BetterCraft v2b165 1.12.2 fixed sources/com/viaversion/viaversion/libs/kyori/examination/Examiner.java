// 
// Decompiled by Procyon v0.6.0
// 

package com.viaversion.viaversion.libs.kyori.examination;

import java.util.stream.Stream;

public interface Examiner<R>
{
    default R examine(final Examinable examinable) {
        return this.examine(examinable.examinableName(), examinable.examinableProperties());
    }
    
    R examine(final String name, final Stream<? extends ExaminableProperty> properties);
    
    R examine(final Object value);
    
    R examine(final boolean value);
    
    R examine(final boolean[] values);
    
    R examine(final byte value);
    
    R examine(final byte[] values);
    
    R examine(final char value);
    
    R examine(final char[] values);
    
    R examine(final double value);
    
    R examine(final double[] values);
    
    R examine(final float value);
    
    R examine(final float[] values);
    
    R examine(final int value);
    
    R examine(final int[] values);
    
    R examine(final long value);
    
    R examine(final long[] values);
    
    R examine(final short value);
    
    R examine(final short[] values);
    
    R examine(final String value);
}
