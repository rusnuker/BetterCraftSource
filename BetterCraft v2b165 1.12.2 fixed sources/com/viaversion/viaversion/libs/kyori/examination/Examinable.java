// 
// Decompiled by Procyon v0.6.0
// 

package com.viaversion.viaversion.libs.kyori.examination;

import java.util.stream.Stream;

public interface Examinable
{
    default String examinableName() {
        return this.getClass().getSimpleName();
    }
    
    default Stream<? extends ExaminableProperty> examinableProperties() {
        return Stream.empty();
    }
    
    default <R> R examine(final Examiner<R> examiner) {
        return examiner.examine(this);
    }
}
