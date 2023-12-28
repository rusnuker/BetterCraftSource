// 
// Decompiled by Procyon v0.6.0
// 

package io.netty.util;

import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.Promise;

public interface AsyncMapping<IN, OUT>
{
    Future<OUT> map(final IN p0, final Promise<OUT> p1);
}
