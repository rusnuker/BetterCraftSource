// 
// Decompiled by Procyon v0.6.0
// 

package io.netty.handler.codec.http2;

import java.util.Arrays;
import io.netty.handler.codec.ValueConverter;
import io.netty.handler.codec.UnsupportedValueConverter;
import io.netty.util.AsciiString;
import java.util.List;

final class HpackStaticTable
{
    private static final List<HpackHeaderField> STATIC_TABLE;
    private static final CharSequenceMap<Integer> STATIC_INDEX_BY_NAME;
    static final int length;
    
    private static HpackHeaderField newEmptyHeaderField(final CharSequence name) {
        return newHeaderField(name, AsciiString.EMPTY_STRING);
    }
    
    private static HpackHeaderField newHeaderField(final CharSequence name, final CharSequence value) {
        return new HpackHeaderField(AsciiString.of(name), AsciiString.of(value));
    }
    
    static HpackHeaderField getEntry(final int index) {
        return HpackStaticTable.STATIC_TABLE.get(index - 1);
    }
    
    static int getIndex(final CharSequence name) {
        final Integer index = HpackStaticTable.STATIC_INDEX_BY_NAME.get(name);
        if (index == null) {
            return -1;
        }
        return index;
    }
    
    static int getIndex(final CharSequence name, final CharSequence value) {
        int index = getIndex(name);
        if (index == -1) {
            return -1;
        }
        while (index <= HpackStaticTable.length) {
            final HpackHeaderField entry = getEntry(index);
            if (HpackUtil.equalsConstantTime(name, entry.name) == 0) {
                break;
            }
            if (HpackUtil.equalsConstantTime(value, entry.value) != 0) {
                return index;
            }
            ++index;
        }
        return -1;
    }
    
    private static CharSequenceMap<Integer> createMap() {
        final int length = HpackStaticTable.STATIC_TABLE.size();
        final CharSequenceMap<Integer> ret = new CharSequenceMap<Integer>(true, (ValueConverter<Integer>)UnsupportedValueConverter.instance(), length);
        for (int index = length; index > 0; --index) {
            final HpackHeaderField entry = getEntry(index);
            final CharSequence name = entry.name;
            ret.set(name, Integer.valueOf(index));
        }
        return ret;
    }
    
    private HpackStaticTable() {
    }
    
    static {
        STATIC_TABLE = Arrays.asList(newEmptyHeaderField(":authority"), newHeaderField(":method", "GET"), newHeaderField(":method", "POST"), newHeaderField(":path", "/"), newHeaderField(":path", "/index.html"), newHeaderField(":scheme", "http"), newHeaderField(":scheme", "https"), newHeaderField(":status", "200"), newHeaderField(":status", "204"), newHeaderField(":status", "206"), newHeaderField(":status", "304"), newHeaderField(":status", "400"), newHeaderField(":status", "404"), newHeaderField(":status", "500"), newEmptyHeaderField("accept-charset"), newHeaderField("accept-encoding", "gzip, deflate"), newEmptyHeaderField("accept-language"), newEmptyHeaderField("accept-ranges"), newEmptyHeaderField("accept"), newEmptyHeaderField("access-control-allow-origin"), newEmptyHeaderField("age"), newEmptyHeaderField("allow"), newEmptyHeaderField("authorization"), newEmptyHeaderField("cache-control"), newEmptyHeaderField("content-disposition"), newEmptyHeaderField("content-encoding"), newEmptyHeaderField("content-language"), newEmptyHeaderField("content-length"), newEmptyHeaderField("content-location"), newEmptyHeaderField("content-range"), newEmptyHeaderField("content-type"), newEmptyHeaderField("cookie"), newEmptyHeaderField("date"), newEmptyHeaderField("etag"), newEmptyHeaderField("expect"), newEmptyHeaderField("expires"), newEmptyHeaderField("from"), newEmptyHeaderField("host"), newEmptyHeaderField("if-match"), newEmptyHeaderField("if-modified-since"), newEmptyHeaderField("if-none-match"), newEmptyHeaderField("if-range"), newEmptyHeaderField("if-unmodified-since"), newEmptyHeaderField("last-modified"), newEmptyHeaderField("link"), newEmptyHeaderField("location"), newEmptyHeaderField("max-forwards"), newEmptyHeaderField("proxy-authenticate"), newEmptyHeaderField("proxy-authorization"), newEmptyHeaderField("range"), newEmptyHeaderField("referer"), newEmptyHeaderField("refresh"), newEmptyHeaderField("retry-after"), newEmptyHeaderField("server"), newEmptyHeaderField("set-cookie"), newEmptyHeaderField("strict-transport-security"), newEmptyHeaderField("transfer-encoding"), newEmptyHeaderField("user-agent"), newEmptyHeaderField("vary"), newEmptyHeaderField("via"), newEmptyHeaderField("www-authenticate"));
        STATIC_INDEX_BY_NAME = createMap();
        length = HpackStaticTable.STATIC_TABLE.size();
    }
}
