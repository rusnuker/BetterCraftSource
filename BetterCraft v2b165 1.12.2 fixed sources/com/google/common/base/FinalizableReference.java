// 
// Decompiled by Procyon v0.6.0
// 

package com.google.common.base;

import com.google.common.annotations.GwtIncompatible;

@GwtIncompatible
public interface FinalizableReference
{
    void finalizeReferent();
}