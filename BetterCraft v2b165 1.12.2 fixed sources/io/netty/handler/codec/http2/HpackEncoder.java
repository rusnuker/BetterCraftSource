// 
// Decompiled by Procyon v0.6.0
// 

package io.netty.handler.codec.http2;

import java.util.Arrays;
import io.netty.util.CharsetUtil;
import java.util.Iterator;
import java.util.Map;
import io.netty.buffer.ByteBuf;
import io.netty.util.internal.MathUtil;
import io.netty.util.AsciiString;

final class HpackEncoder
{
    private final HeaderEntry[] headerFields;
    private final HeaderEntry head;
    private final HpackHuffmanEncoder hpackHuffmanEncoder;
    private final byte hashMask;
    private final boolean ignoreMaxHeaderListSize;
    private long size;
    private long maxHeaderTableSize;
    private long maxHeaderListSize;
    
    HpackEncoder() {
        this(false);
    }
    
    public HpackEncoder(final boolean ignoreMaxHeaderListSize) {
        this(ignoreMaxHeaderListSize, 16);
    }
    
    public HpackEncoder(final boolean ignoreMaxHeaderListSize, final int arraySizeHint) {
        this.head = new HeaderEntry(-1, AsciiString.EMPTY_STRING, AsciiString.EMPTY_STRING, Integer.MAX_VALUE, null);
        this.hpackHuffmanEncoder = new HpackHuffmanEncoder();
        this.ignoreMaxHeaderListSize = ignoreMaxHeaderListSize;
        this.maxHeaderTableSize = 4096L;
        this.maxHeaderListSize = 8192L;
        this.headerFields = new HeaderEntry[MathUtil.findNextPositivePowerOfTwo(Math.max(2, Math.min(arraySizeHint, 128)))];
        this.hashMask = (byte)(this.headerFields.length - 1);
        final HeaderEntry head = this.head;
        final HeaderEntry head2 = this.head;
        final HeaderEntry head3 = this.head;
        head2.after = head3;
        head.before = head3;
    }
    
    public void encodeHeaders(final int streamId, final ByteBuf out, final Http2Headers headers, final Http2HeadersEncoder.SensitivityDetector sensitivityDetector) throws Http2Exception {
        if (this.ignoreMaxHeaderListSize) {
            this.encodeHeadersIgnoreMaxHeaderListSize(out, headers, sensitivityDetector);
        }
        else {
            this.encodeHeadersEnforceMaxHeaderListSize(streamId, out, headers, sensitivityDetector);
        }
    }
    
    private void encodeHeadersEnforceMaxHeaderListSize(final int streamId, final ByteBuf out, final Http2Headers headers, final Http2HeadersEncoder.SensitivityDetector sensitivityDetector) throws Http2Exception {
        long headerSize = 0L;
        for (final Map.Entry<CharSequence, CharSequence> header : headers) {
            final CharSequence name = header.getKey();
            final CharSequence value = header.getValue();
            headerSize += HpackHeaderField.sizeOf(name, value);
            if (headerSize > this.maxHeaderListSize) {
                Http2CodecUtil.headerListSizeExceeded(streamId, this.maxHeaderListSize, false);
            }
        }
        this.encodeHeadersIgnoreMaxHeaderListSize(out, headers, sensitivityDetector);
    }
    
    private void encodeHeadersIgnoreMaxHeaderListSize(final ByteBuf out, final Http2Headers headers, final Http2HeadersEncoder.SensitivityDetector sensitivityDetector) throws Http2Exception {
        for (final Map.Entry<CharSequence, CharSequence> header : headers) {
            final CharSequence name = header.getKey();
            final CharSequence value = header.getValue();
            this.encodeHeader(out, name, value, sensitivityDetector.isSensitive(name, value), HpackHeaderField.sizeOf(name, value));
        }
    }
    
    private void encodeHeader(final ByteBuf out, final CharSequence name, final CharSequence value, final boolean sensitive, final long headerSize) {
        if (sensitive) {
            final int nameIndex = this.getNameIndex(name);
            this.encodeLiteral(out, name, value, HpackUtil.IndexType.NEVER, nameIndex);
            return;
        }
        if (this.maxHeaderTableSize == 0L) {
            final int staticTableIndex = HpackStaticTable.getIndex(name, value);
            if (staticTableIndex == -1) {
                final int nameIndex2 = HpackStaticTable.getIndex(name);
                this.encodeLiteral(out, name, value, HpackUtil.IndexType.NONE, nameIndex2);
            }
            else {
                encodeInteger(out, 128, 7, staticTableIndex);
            }
            return;
        }
        if (headerSize > this.maxHeaderTableSize) {
            final int nameIndex = this.getNameIndex(name);
            this.encodeLiteral(out, name, value, HpackUtil.IndexType.NONE, nameIndex);
            return;
        }
        final HeaderEntry headerField = this.getEntry(name, value);
        if (headerField != null) {
            final int index = this.getIndex(headerField.index) + HpackStaticTable.length;
            encodeInteger(out, 128, 7, index);
        }
        else {
            final int staticTableIndex2 = HpackStaticTable.getIndex(name, value);
            if (staticTableIndex2 != -1) {
                encodeInteger(out, 128, 7, staticTableIndex2);
            }
            else {
                this.ensureCapacity(headerSize);
                this.encodeLiteral(out, name, value, HpackUtil.IndexType.INCREMENTAL, this.getNameIndex(name));
                this.add(name, value, headerSize);
            }
        }
    }
    
    public void setMaxHeaderTableSize(final ByteBuf out, final long maxHeaderTableSize) throws Http2Exception {
        if (maxHeaderTableSize < 0L || maxHeaderTableSize > 4294967295L) {
            throw Http2Exception.connectionError(Http2Error.PROTOCOL_ERROR, "Header Table Size must be >= %d and <= %d but was %d", 0L, 4294967295L, maxHeaderTableSize);
        }
        if (this.maxHeaderTableSize == maxHeaderTableSize) {
            return;
        }
        this.maxHeaderTableSize = maxHeaderTableSize;
        this.ensureCapacity(0L);
        encodeInteger(out, 32, 5, maxHeaderTableSize);
    }
    
    public long getMaxHeaderTableSize() {
        return this.maxHeaderTableSize;
    }
    
    public void setMaxHeaderListSize(final long maxHeaderListSize) throws Http2Exception {
        if (maxHeaderListSize < 0L || maxHeaderListSize > 4294967295L) {
            throw Http2Exception.connectionError(Http2Error.PROTOCOL_ERROR, "Header List Size must be >= %d and <= %d but was %d", 0L, 4294967295L, maxHeaderListSize);
        }
        this.maxHeaderListSize = maxHeaderListSize;
    }
    
    public long getMaxHeaderListSize() {
        return this.maxHeaderListSize;
    }
    
    private static void encodeInteger(final ByteBuf out, final int mask, final int n, final int i) {
        encodeInteger(out, mask, n, (long)i);
    }
    
    private static void encodeInteger(final ByteBuf out, final int mask, final int n, final long i) {
        assert n >= 0 && n <= 8 : "N: " + n;
        final int nbits = 255 >>> 8 - n;
        if (i < nbits) {
            out.writeByte((int)((long)mask | i));
        }
        else {
            out.writeByte(mask | nbits);
            long length;
            for (length = i - nbits; (length & 0xFFFFFFFFFFFFFF80L) != 0x0L; length >>>= 7) {
                out.writeByte((int)((length & 0x7FL) | 0x80L));
            }
            out.writeByte((int)length);
        }
    }
    
    private void encodeStringLiteral(final ByteBuf out, final CharSequence string) {
        final int huffmanLength = this.hpackHuffmanEncoder.getEncodedLength(string);
        if (huffmanLength < string.length()) {
            encodeInteger(out, 128, 7, huffmanLength);
            this.hpackHuffmanEncoder.encode(out, string);
        }
        else {
            encodeInteger(out, 0, 7, string.length());
            if (string instanceof AsciiString) {
                final AsciiString asciiString = (AsciiString)string;
                out.writeBytes(asciiString.array(), asciiString.arrayOffset(), asciiString.length());
            }
            else {
                out.writeCharSequence(string, CharsetUtil.ISO_8859_1);
            }
        }
    }
    
    private void encodeLiteral(final ByteBuf out, final CharSequence name, final CharSequence value, final HpackUtil.IndexType indexType, final int nameIndex) {
        final boolean nameIndexValid = nameIndex != -1;
        switch (indexType) {
            case INCREMENTAL: {
                encodeInteger(out, 64, 6, nameIndexValid ? nameIndex : 0);
                break;
            }
            case NONE: {
                encodeInteger(out, 0, 4, nameIndexValid ? nameIndex : 0);
                break;
            }
            case NEVER: {
                encodeInteger(out, 16, 4, nameIndexValid ? nameIndex : 0);
                break;
            }
            default: {
                throw new Error("should not reach here");
            }
        }
        if (!nameIndexValid) {
            this.encodeStringLiteral(out, name);
        }
        this.encodeStringLiteral(out, value);
    }
    
    private int getNameIndex(final CharSequence name) {
        int index = HpackStaticTable.getIndex(name);
        if (index == -1) {
            index = this.getIndex(name);
            if (index >= 0) {
                index += HpackStaticTable.length;
            }
        }
        return index;
    }
    
    private void ensureCapacity(final long headerSize) {
        while (this.maxHeaderTableSize - this.size < headerSize) {
            final int index = this.length();
            if (index == 0) {
                break;
            }
            this.remove();
        }
    }
    
    int length() {
        return (this.size == 0L) ? 0 : (this.head.after.index - this.head.before.index + 1);
    }
    
    long size() {
        return this.size;
    }
    
    HpackHeaderField getHeaderField(int index) {
        HeaderEntry entry = this.head;
        while (index-- >= 0) {
            entry = entry.before;
        }
        return entry;
    }
    
    private HeaderEntry getEntry(final CharSequence name, final CharSequence value) {
        if (this.length() == 0 || name == null || value == null) {
            return null;
        }
        final int h = AsciiString.hashCode(name);
        final int i = this.index(h);
        for (HeaderEntry e = this.headerFields[i]; e != null; e = e.next) {
            if (e.hash == h && (HpackUtil.equalsConstantTime(name, e.name) & HpackUtil.equalsConstantTime(value, e.value)) != 0x0) {
                return e;
            }
        }
        return null;
    }
    
    private int getIndex(final CharSequence name) {
        if (this.length() == 0 || name == null) {
            return -1;
        }
        final int h = AsciiString.hashCode(name);
        final int i = this.index(h);
        for (HeaderEntry e = this.headerFields[i]; e != null; e = e.next) {
            if (e.hash == h && HpackUtil.equalsConstantTime(name, e.name) != 0) {
                return this.getIndex(e.index);
            }
        }
        return -1;
    }
    
    private int getIndex(final int index) {
        return (index == -1) ? -1 : (index - this.head.before.index + 1);
    }
    
    private void add(final CharSequence name, final CharSequence value, final long headerSize) {
        if (headerSize > this.maxHeaderTableSize) {
            this.clear();
            return;
        }
        while (this.maxHeaderTableSize - this.size < headerSize) {
            this.remove();
        }
        final int h = AsciiString.hashCode(name);
        final int i = this.index(h);
        final HeaderEntry old = this.headerFields[i];
        final HeaderEntry e = new HeaderEntry(h, name, value, this.head.before.index - 1, old);
        (this.headerFields[i] = e).addBefore(this.head);
        this.size += headerSize;
    }
    
    private HpackHeaderField remove() {
        if (this.size == 0L) {
            return null;
        }
        final HeaderEntry eldest = this.head.after;
        final int h = eldest.hash;
        final int i = this.index(h);
        HeaderEntry e;
        HeaderEntry next;
        for (HeaderEntry prev = e = this.headerFields[i]; e != null; e = next) {
            next = e.next;
            if (e == eldest) {
                if (prev == eldest) {
                    this.headerFields[i] = next;
                }
                else {
                    prev.next = next;
                }
                eldest.remove();
                this.size -= eldest.size();
                return eldest;
            }
            prev = e;
        }
        return null;
    }
    
    private void clear() {
        Arrays.fill(this.headerFields, null);
        final HeaderEntry head = this.head;
        final HeaderEntry head2 = this.head;
        final HeaderEntry head3 = this.head;
        head2.after = head3;
        head.before = head3;
        this.size = 0L;
    }
    
    private int index(final int h) {
        return h & this.hashMask;
    }
    
    private static final class HeaderEntry extends HpackHeaderField
    {
        HeaderEntry before;
        HeaderEntry after;
        HeaderEntry next;
        int hash;
        int index;
        
        HeaderEntry(final int hash, final CharSequence name, final CharSequence value, final int index, final HeaderEntry next) {
            super(name, value);
            this.index = index;
            this.hash = hash;
            this.next = next;
        }
        
        private void remove() {
            this.before.after = this.after;
            this.after.before = this.before;
            this.before = null;
            this.after = null;
            this.next = null;
        }
        
        private void addBefore(final HeaderEntry existingEntry) {
            this.after = existingEntry;
            this.before = existingEntry.before;
            this.before.after = this;
            this.after.before = this;
        }
    }
}
