// 
// Decompiled by Procyon v0.6.0
// 

package io.netty.handler.codec;

import io.netty.util.HashingStrategy;

public final class DefaultHeadersImpl<K, V> extends DefaultHeaders<K, V, DefaultHeadersImpl<K, V>>
{
    public DefaultHeadersImpl(final HashingStrategy<K> nameHashingStrategy, final ValueConverter<V> valueConverter, final NameValidator<K> nameValidator) {
        super(nameHashingStrategy, valueConverter, nameValidator);
    }
}
