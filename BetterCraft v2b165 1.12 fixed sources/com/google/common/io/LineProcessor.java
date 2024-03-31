// 
// Decompiled by Procyon v0.6.0
// 

package com.google.common.io;

import com.google.errorprone.annotations.CanIgnoreReturnValue;
import java.io.IOException;
import com.google.common.annotations.GwtIncompatible;
import com.google.common.annotations.Beta;

@Beta
@GwtIncompatible
public interface LineProcessor<T>
{
    @CanIgnoreReturnValue
    boolean processLine(final String p0) throws IOException;
    
    T getResult();
}
