// 
// Decompiled by Procyon v0.6.0
// 

package io.netty.handler.codec.http2;

import io.netty.util.AsciiString;
import io.netty.handler.codec.ValueConverter;
import io.netty.handler.codec.UnsupportedValueConverter;
import io.netty.handler.codec.DefaultHeaders;

public final class CharSequenceMap<V> extends DefaultHeaders<CharSequence, V, CharSequenceMap<V>>
{
    public CharSequenceMap() {
        this(true);
    }
    
    public CharSequenceMap(final boolean caseSensitive) {
        this(caseSensitive, UnsupportedValueConverter.instance());
    }
    
    public CharSequenceMap(final boolean caseSensitive, final ValueConverter<V> valueConverter) {
        super(caseSensitive ? AsciiString.CASE_SENSITIVE_HASHER : AsciiString.CASE_INSENSITIVE_HASHER, valueConverter);
    }
    
    public CharSequenceMap(final boolean caseSensitive, final ValueConverter<V> valueConverter, final int arraySizeHint) {
        super(caseSensitive ? AsciiString.CASE_SENSITIVE_HASHER : AsciiString.CASE_INSENSITIVE_HASHER, valueConverter, NameValidator.NOT_NULL, arraySizeHint);
    }
}
