// 
// Decompiled by Procyon v0.6.0
// 

package io.netty.util;

import java.nio.charset.CharsetDecoder;
import java.util.Map;
import io.netty.util.internal.InternalThreadLocalMap;
import io.netty.util.internal.ObjectUtil;
import java.nio.charset.CodingErrorAction;
import java.nio.charset.CharsetEncoder;
import java.nio.charset.Charset;

public final class CharsetUtil
{
    public static final Charset UTF_16;
    public static final Charset UTF_16BE;
    public static final Charset UTF_16LE;
    public static final Charset UTF_8;
    public static final Charset ISO_8859_1;
    public static final Charset US_ASCII;
    private static final Charset[] CHARSETS;
    
    public static Charset[] values() {
        return CharsetUtil.CHARSETS;
    }
    
    @Deprecated
    public static CharsetEncoder getEncoder(final Charset charset) {
        return encoder(charset);
    }
    
    public static CharsetEncoder encoder(final Charset charset, final CodingErrorAction malformedInputAction, final CodingErrorAction unmappableCharacterAction) {
        ObjectUtil.checkNotNull(charset, "charset");
        final CharsetEncoder e = charset.newEncoder();
        e.onMalformedInput(malformedInputAction).onUnmappableCharacter(unmappableCharacterAction);
        return e;
    }
    
    public static CharsetEncoder encoder(final Charset charset, final CodingErrorAction codingErrorAction) {
        return encoder(charset, codingErrorAction, codingErrorAction);
    }
    
    public static CharsetEncoder encoder(final Charset charset) {
        ObjectUtil.checkNotNull(charset, "charset");
        final Map<Charset, CharsetEncoder> map = InternalThreadLocalMap.get().charsetEncoderCache();
        CharsetEncoder e = map.get(charset);
        if (e != null) {
            e.reset().onMalformedInput(CodingErrorAction.REPLACE).onUnmappableCharacter(CodingErrorAction.REPLACE);
            return e;
        }
        e = encoder(charset, CodingErrorAction.REPLACE, CodingErrorAction.REPLACE);
        map.put(charset, e);
        return e;
    }
    
    @Deprecated
    public static CharsetDecoder getDecoder(final Charset charset) {
        return decoder(charset);
    }
    
    public static CharsetDecoder decoder(final Charset charset, final CodingErrorAction malformedInputAction, final CodingErrorAction unmappableCharacterAction) {
        ObjectUtil.checkNotNull(charset, "charset");
        final CharsetDecoder d = charset.newDecoder();
        d.onMalformedInput(malformedInputAction).onUnmappableCharacter(unmappableCharacterAction);
        return d;
    }
    
    public static CharsetDecoder decoder(final Charset charset, final CodingErrorAction codingErrorAction) {
        return decoder(charset, codingErrorAction, codingErrorAction);
    }
    
    public static CharsetDecoder decoder(final Charset charset) {
        ObjectUtil.checkNotNull(charset, "charset");
        final Map<Charset, CharsetDecoder> map = InternalThreadLocalMap.get().charsetDecoderCache();
        CharsetDecoder d = map.get(charset);
        if (d != null) {
            d.reset().onMalformedInput(CodingErrorAction.REPLACE).onUnmappableCharacter(CodingErrorAction.REPLACE);
            return d;
        }
        d = decoder(charset, CodingErrorAction.REPLACE, CodingErrorAction.REPLACE);
        map.put(charset, d);
        return d;
    }
    
    private CharsetUtil() {
    }
    
    static {
        UTF_16 = Charset.forName("UTF-16");
        UTF_16BE = Charset.forName("UTF-16BE");
        UTF_16LE = Charset.forName("UTF-16LE");
        UTF_8 = Charset.forName("UTF-8");
        ISO_8859_1 = Charset.forName("ISO-8859-1");
        US_ASCII = Charset.forName("US-ASCII");
        CHARSETS = new Charset[] { CharsetUtil.UTF_16, CharsetUtil.UTF_16BE, CharsetUtil.UTF_16LE, CharsetUtil.UTF_8, CharsetUtil.ISO_8859_1, CharsetUtil.US_ASCII };
    }
}
