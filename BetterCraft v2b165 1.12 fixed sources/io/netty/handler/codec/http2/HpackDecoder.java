// 
// Decompiled by Procyon v0.6.0
// 

package io.netty.handler.codec.http2;

import io.netty.handler.codec.Headers;
import io.netty.util.internal.ThrowableUtil;
import io.netty.util.AsciiString;
import io.netty.buffer.ByteBuf;
import io.netty.util.internal.ObjectUtil;

final class HpackDecoder
{
    private static final Http2Exception DECODE_ULE_128_DECOMPRESSION_EXCEPTION;
    private static final Http2Exception DECODE_ULE_128_TO_LONG_DECOMPRESSION_EXCEPTION;
    private static final Http2Exception DECODE_ULE_128_TO_INT_DECOMPRESSION_EXCEPTION;
    private static final Http2Exception DECODE_ILLEGAL_INDEX_VALUE;
    private static final Http2Exception INDEX_HEADER_ILLEGAL_INDEX_VALUE;
    private static final Http2Exception READ_NAME_ILLEGAL_INDEX_VALUE;
    private static final Http2Exception INVALID_MAX_DYNAMIC_TABLE_SIZE;
    private static final Http2Exception MAX_DYNAMIC_TABLE_SIZE_CHANGE_REQUIRED;
    private static final byte READ_HEADER_REPRESENTATION = 0;
    private static final byte READ_MAX_DYNAMIC_TABLE_SIZE = 1;
    private static final byte READ_INDEXED_HEADER = 2;
    private static final byte READ_INDEXED_HEADER_NAME = 3;
    private static final byte READ_LITERAL_HEADER_NAME_LENGTH_PREFIX = 4;
    private static final byte READ_LITERAL_HEADER_NAME_LENGTH = 5;
    private static final byte READ_LITERAL_HEADER_NAME = 6;
    private static final byte READ_LITERAL_HEADER_VALUE_LENGTH_PREFIX = 7;
    private static final byte READ_LITERAL_HEADER_VALUE_LENGTH = 8;
    private static final byte READ_LITERAL_HEADER_VALUE = 9;
    private final HpackDynamicTable hpackDynamicTable;
    private final HpackHuffmanDecoder hpackHuffmanDecoder;
    private long maxHeaderListSizeGoAway;
    private long maxHeaderListSize;
    private long maxDynamicTableSize;
    private long encoderMaxDynamicTableSize;
    private boolean maxDynamicTableSizeChangeRequired;
    
    HpackDecoder(final long maxHeaderListSize, final int initialHuffmanDecodeCapacity) {
        this(maxHeaderListSize, initialHuffmanDecodeCapacity, 4096);
    }
    
    HpackDecoder(final long maxHeaderListSize, final int initialHuffmanDecodeCapacity, final int maxHeaderTableSize) {
        this.maxHeaderListSize = ObjectUtil.checkPositive(maxHeaderListSize, "maxHeaderListSize");
        this.maxHeaderListSizeGoAway = Http2CodecUtil.calculateMaxHeaderListSizeGoAway(maxHeaderListSize);
        final long n = maxHeaderTableSize;
        this.encoderMaxDynamicTableSize = n;
        this.maxDynamicTableSize = n;
        this.maxDynamicTableSizeChangeRequired = false;
        this.hpackDynamicTable = new HpackDynamicTable(maxHeaderTableSize);
        this.hpackHuffmanDecoder = new HpackHuffmanDecoder(initialHuffmanDecodeCapacity);
    }
    
    public void decode(final int streamId, final ByteBuf in, final Http2Headers headers) throws Http2Exception {
        int index = 0;
        long headersLength = 0L;
        int nameLength = 0;
        int valueLength = 0;
        byte state = 0;
        boolean huffmanEncoded = false;
        CharSequence name = null;
        HpackUtil.IndexType indexType = HpackUtil.IndexType.NONE;
        while (in.isReadable()) {
            switch (state) {
                case 0: {
                    final byte b = in.readByte();
                    if (this.maxDynamicTableSizeChangeRequired && (b & 0xE0) != 0x20) {
                        throw HpackDecoder.MAX_DYNAMIC_TABLE_SIZE_CHANGE_REQUIRED;
                    }
                    if (b < 0) {
                        index = (b & 0x7F);
                        switch (index) {
                            case 0: {
                                throw HpackDecoder.DECODE_ILLEGAL_INDEX_VALUE;
                            }
                            case 127: {
                                state = 2;
                                continue;
                            }
                            default: {
                                headersLength = this.indexHeader(streamId, index, headers, headersLength);
                                continue;
                            }
                        }
                    }
                    else if ((b & 0x40) == 0x40) {
                        indexType = HpackUtil.IndexType.INCREMENTAL;
                        index = (b & 0x3F);
                        switch (index) {
                            case 0: {
                                state = 4;
                                continue;
                            }
                            case 63: {
                                state = 3;
                                continue;
                            }
                            default: {
                                name = this.readName(index);
                                state = 7;
                                continue;
                            }
                        }
                    }
                    else if ((b & 0x20) == 0x20) {
                        index = (b & 0x1F);
                        if (index == 31) {
                            state = 1;
                            continue;
                        }
                        this.setDynamicTableSize(index);
                        state = 0;
                        continue;
                    }
                    else {
                        indexType = (((b & 0x10) == 0x10) ? HpackUtil.IndexType.NEVER : HpackUtil.IndexType.NONE);
                        index = (b & 0xF);
                        switch (index) {
                            case 0: {
                                state = 4;
                                continue;
                            }
                            case 15: {
                                state = 3;
                                continue;
                            }
                            default: {
                                name = this.readName(index);
                                state = 7;
                                continue;
                            }
                        }
                    }
                    break;
                }
                case 1: {
                    this.setDynamicTableSize(decodeULE128(in, (long)index));
                    state = 0;
                    continue;
                }
                case 2: {
                    headersLength = this.indexHeader(streamId, decodeULE128(in, index), headers, headersLength);
                    state = 0;
                    continue;
                }
                case 3: {
                    name = this.readName(decodeULE128(in, index));
                    state = 7;
                    continue;
                }
                case 4: {
                    final byte b = in.readByte();
                    huffmanEncoded = ((b & 0x80) == 0x80);
                    index = (b & 0x7F);
                    if (index == 127) {
                        state = 5;
                        continue;
                    }
                    if (index > this.maxHeaderListSizeGoAway - headersLength) {
                        Http2CodecUtil.headerListSizeExceeded(this.maxHeaderListSizeGoAway);
                    }
                    nameLength = index;
                    state = 6;
                    continue;
                }
                case 5: {
                    nameLength = decodeULE128(in, index);
                    if (nameLength > this.maxHeaderListSizeGoAway - headersLength) {
                        Http2CodecUtil.headerListSizeExceeded(this.maxHeaderListSizeGoAway);
                    }
                    state = 6;
                    continue;
                }
                case 6: {
                    if (in.readableBytes() < nameLength) {
                        throw notEnoughDataException(in);
                    }
                    name = this.readStringLiteral(in, nameLength, huffmanEncoded);
                    state = 7;
                    continue;
                }
                case 7: {
                    final byte b = in.readByte();
                    huffmanEncoded = ((b & 0x80) == 0x80);
                    index = (b & 0x7F);
                    switch (index) {
                        case 127: {
                            state = 8;
                            continue;
                        }
                        case 0: {
                            headersLength = this.insertHeader(streamId, headers, name, AsciiString.EMPTY_STRING, indexType, headersLength);
                            state = 0;
                            continue;
                        }
                        default: {
                            if (index + (long)nameLength > this.maxHeaderListSizeGoAway - headersLength) {
                                Http2CodecUtil.headerListSizeExceeded(this.maxHeaderListSizeGoAway);
                            }
                            valueLength = index;
                            state = 9;
                            continue;
                        }
                    }
                    break;
                }
                case 8: {
                    valueLength = decodeULE128(in, index);
                    if (valueLength + (long)nameLength > this.maxHeaderListSizeGoAway - headersLength) {
                        Http2CodecUtil.headerListSizeExceeded(this.maxHeaderListSizeGoAway);
                    }
                    state = 9;
                    continue;
                }
                case 9: {
                    if (in.readableBytes() < valueLength) {
                        throw notEnoughDataException(in);
                    }
                    final CharSequence value = this.readStringLiteral(in, valueLength, huffmanEncoded);
                    headersLength = this.insertHeader(streamId, headers, name, value, indexType, headersLength);
                    state = 0;
                    continue;
                }
                default: {
                    throw new Error("should not reach here state: " + state);
                }
            }
        }
        if (headersLength > this.maxHeaderListSize) {
            Http2CodecUtil.headerListSizeExceeded(streamId, this.maxHeaderListSize, true);
        }
    }
    
    public void setMaxHeaderTableSize(final long maxHeaderTableSize) throws Http2Exception {
        if (maxHeaderTableSize < 0L || maxHeaderTableSize > 4294967295L) {
            throw Http2Exception.connectionError(Http2Error.PROTOCOL_ERROR, "Header Table Size must be >= %d and <= %d but was %d", 0L, 4294967295L, maxHeaderTableSize);
        }
        this.maxDynamicTableSize = maxHeaderTableSize;
        if (this.maxDynamicTableSize < this.encoderMaxDynamicTableSize) {
            this.maxDynamicTableSizeChangeRequired = true;
            this.hpackDynamicTable.setCapacity(this.maxDynamicTableSize);
        }
    }
    
    public void setMaxHeaderListSize(final long maxHeaderListSize, final long maxHeaderListSizeGoAway) throws Http2Exception {
        if (maxHeaderListSizeGoAway < maxHeaderListSize || maxHeaderListSizeGoAway < 0L) {
            throw Http2Exception.connectionError(Http2Error.INTERNAL_ERROR, "Header List Size GO_AWAY %d must be positive and >= %d", maxHeaderListSizeGoAway, maxHeaderListSize);
        }
        if (maxHeaderListSize < 0L || maxHeaderListSize > 4294967295L) {
            throw Http2Exception.connectionError(Http2Error.PROTOCOL_ERROR, "Header List Size must be >= %d and <= %d but was %d", 0L, 4294967295L, maxHeaderListSize);
        }
        this.maxHeaderListSize = maxHeaderListSize;
        this.maxHeaderListSizeGoAway = maxHeaderListSizeGoAway;
    }
    
    public long getMaxHeaderListSize() {
        return this.maxHeaderListSize;
    }
    
    public long getMaxHeaderListSizeGoAway() {
        return this.maxHeaderListSizeGoAway;
    }
    
    public long getMaxHeaderTableSize() {
        return this.hpackDynamicTable.capacity();
    }
    
    int length() {
        return this.hpackDynamicTable.length();
    }
    
    long size() {
        return this.hpackDynamicTable.size();
    }
    
    HpackHeaderField getHeaderField(final int index) {
        return this.hpackDynamicTable.getEntry(index + 1);
    }
    
    private void setDynamicTableSize(final long dynamicTableSize) throws Http2Exception {
        if (dynamicTableSize > this.maxDynamicTableSize) {
            throw HpackDecoder.INVALID_MAX_DYNAMIC_TABLE_SIZE;
        }
        this.encoderMaxDynamicTableSize = dynamicTableSize;
        this.maxDynamicTableSizeChangeRequired = false;
        this.hpackDynamicTable.setCapacity(dynamicTableSize);
    }
    
    private CharSequence readName(final int index) throws Http2Exception {
        if (index <= HpackStaticTable.length) {
            final HpackHeaderField hpackHeaderField = HpackStaticTable.getEntry(index);
            return hpackHeaderField.name;
        }
        if (index - HpackStaticTable.length <= this.hpackDynamicTable.length()) {
            final HpackHeaderField hpackHeaderField = this.hpackDynamicTable.getEntry(index - HpackStaticTable.length);
            return hpackHeaderField.name;
        }
        throw HpackDecoder.READ_NAME_ILLEGAL_INDEX_VALUE;
    }
    
    private long indexHeader(final int streamId, final int index, final Http2Headers headers, final long headersLength) throws Http2Exception {
        if (index <= HpackStaticTable.length) {
            final HpackHeaderField hpackHeaderField = HpackStaticTable.getEntry(index);
            return this.addHeader(streamId, headers, hpackHeaderField.name, hpackHeaderField.value, headersLength);
        }
        if (index - HpackStaticTable.length <= this.hpackDynamicTable.length()) {
            final HpackHeaderField hpackHeaderField = this.hpackDynamicTable.getEntry(index - HpackStaticTable.length);
            return this.addHeader(streamId, headers, hpackHeaderField.name, hpackHeaderField.value, headersLength);
        }
        throw HpackDecoder.INDEX_HEADER_ILLEGAL_INDEX_VALUE;
    }
    
    private long insertHeader(final int streamId, final Http2Headers headers, final CharSequence name, final CharSequence value, final HpackUtil.IndexType indexType, long headerSize) throws Http2Exception {
        headerSize = this.addHeader(streamId, headers, name, value, headerSize);
        switch (indexType) {
            case NONE:
            case NEVER: {
                break;
            }
            case INCREMENTAL: {
                this.hpackDynamicTable.add(new HpackHeaderField(name, value));
                break;
            }
            default: {
                throw new Error("should not reach here");
            }
        }
        return headerSize;
    }
    
    private long addHeader(final int streamId, final Http2Headers headers, final CharSequence name, final CharSequence value, long headersLength) throws Http2Exception {
        headersLength += name.length() + value.length();
        if (headersLength > this.maxHeaderListSizeGoAway) {
            Http2CodecUtil.headerListSizeExceeded(this.maxHeaderListSizeGoAway);
        }
        ((Headers<CharSequence, CharSequence, Headers>)headers).add(name, value);
        return headersLength;
    }
    
    private CharSequence readStringLiteral(final ByteBuf in, final int length, final boolean huffmanEncoded) throws Http2Exception {
        if (huffmanEncoded) {
            return this.hpackHuffmanDecoder.decode(in, length);
        }
        final byte[] buf = new byte[length];
        in.readBytes(buf);
        return new AsciiString(buf, false);
    }
    
    private static IllegalArgumentException notEnoughDataException(final ByteBuf in) {
        return new IllegalArgumentException("decode only works with an entire header block! " + in);
    }
    
    static int decodeULE128(final ByteBuf in, final int result) throws Http2Exception {
        final int readerIndex = in.readerIndex();
        final long v = decodeULE128(in, (long)result);
        if (v > 2147483647L) {
            in.readerIndex(readerIndex);
            throw HpackDecoder.DECODE_ULE_128_TO_INT_DECOMPRESSION_EXCEPTION;
        }
        return (int)v;
    }
    
    static long decodeULE128(final ByteBuf in, long result) throws Http2Exception {
        assert result <= 127L && result >= 0L;
        final boolean resultStartedAtZero = result == 0L;
        for (int writerIndex = in.writerIndex(), readerIndex = in.readerIndex(), shift = 0; readerIndex < writerIndex; ++readerIndex, shift += 7) {
            final byte b = in.getByte(readerIndex);
            if (shift == 56 && ((b & 0x80) != 0x0 || (b == 127 && !resultStartedAtZero))) {
                throw HpackDecoder.DECODE_ULE_128_TO_LONG_DECOMPRESSION_EXCEPTION;
            }
            if ((b & 0x80) == 0x0) {
                in.readerIndex(readerIndex + 1);
                return result + (((long)b & 0x7FL) << shift);
            }
            result += ((long)b & 0x7FL) << shift;
        }
        throw HpackDecoder.DECODE_ULE_128_DECOMPRESSION_EXCEPTION;
    }
    
    static {
        DECODE_ULE_128_DECOMPRESSION_EXCEPTION = ThrowableUtil.unknownStackTrace(Http2Exception.connectionError(Http2Error.COMPRESSION_ERROR, "HPACK - decompression failure", new Object[0]), HpackDecoder.class, "decodeULE128(..)");
        DECODE_ULE_128_TO_LONG_DECOMPRESSION_EXCEPTION = ThrowableUtil.unknownStackTrace(Http2Exception.connectionError(Http2Error.COMPRESSION_ERROR, "HPACK - long overflow", new Object[0]), HpackDecoder.class, "decodeULE128(..)");
        DECODE_ULE_128_TO_INT_DECOMPRESSION_EXCEPTION = ThrowableUtil.unknownStackTrace(Http2Exception.connectionError(Http2Error.COMPRESSION_ERROR, "HPACK - int overflow", new Object[0]), HpackDecoder.class, "decodeULE128ToInt(..)");
        DECODE_ILLEGAL_INDEX_VALUE = ThrowableUtil.unknownStackTrace(Http2Exception.connectionError(Http2Error.COMPRESSION_ERROR, "HPACK - illegal index value", new Object[0]), HpackDecoder.class, "decode(..)");
        INDEX_HEADER_ILLEGAL_INDEX_VALUE = ThrowableUtil.unknownStackTrace(Http2Exception.connectionError(Http2Error.COMPRESSION_ERROR, "HPACK - illegal index value", new Object[0]), HpackDecoder.class, "indexHeader(..)");
        READ_NAME_ILLEGAL_INDEX_VALUE = ThrowableUtil.unknownStackTrace(Http2Exception.connectionError(Http2Error.COMPRESSION_ERROR, "HPACK - illegal index value", new Object[0]), HpackDecoder.class, "readName(..)");
        INVALID_MAX_DYNAMIC_TABLE_SIZE = ThrowableUtil.unknownStackTrace(Http2Exception.connectionError(Http2Error.COMPRESSION_ERROR, "HPACK - invalid max dynamic table size", new Object[0]), HpackDecoder.class, "setDynamicTableSize(..)");
        MAX_DYNAMIC_TABLE_SIZE_CHANGE_REQUIRED = ThrowableUtil.unknownStackTrace(Http2Exception.connectionError(Http2Error.COMPRESSION_ERROR, "HPACK - max dynamic table size change required", new Object[0]), HpackDecoder.class, "decode(..)");
    }
}
