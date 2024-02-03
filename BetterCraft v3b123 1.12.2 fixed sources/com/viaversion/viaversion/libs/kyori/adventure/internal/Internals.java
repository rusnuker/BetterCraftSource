// 
// Decompiled by Procyon v0.6.0
// 

package com.viaversion.viaversion.libs.kyori.adventure.internal;

import com.viaversion.viaversion.libs.kyori.examination.Examiner;
import com.viaversion.viaversion.libs.kyori.examination.string.StringExaminer;
import org.jetbrains.annotations.NotNull;
import com.viaversion.viaversion.libs.kyori.examination.Examinable;
import org.jetbrains.annotations.ApiStatus;

@ApiStatus.Internal
public final class Internals
{
    private Internals() {
    }
    
    @NotNull
    public static String toString(@NotNull final Examinable examinable) {
        return examinable.examine((Examiner<String>)StringExaminer.simpleEscaping());
    }
}
