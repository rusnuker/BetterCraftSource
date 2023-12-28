// 
// Decompiled by Procyon v0.6.0
// 

package io.netty.handler.codec.http2;

import java.util.NoSuchElementException;
import java.util.Map;
import java.util.Iterator;
import io.netty.handler.codec.Headers;
import java.util.LinkedHashSet;
import java.util.Collections;
import java.util.Set;
import io.netty.handler.codec.CharSequenceValueConverter;
import java.util.ArrayList;
import java.util.List;
import io.netty.util.internal.EmptyArrays;
import io.netty.util.AsciiString;

public final class ReadOnlyHttp2Headers implements Http2Headers
{
    private static final byte PSEUDO_HEADER_TOKEN = 58;
    private final AsciiString[] pseudoHeaders;
    private final AsciiString[] otherHeaders;
    
    public static ReadOnlyHttp2Headers trailers(final boolean validateHeaders, final AsciiString... otherHeaders) {
        return new ReadOnlyHttp2Headers(validateHeaders, EmptyArrays.EMPTY_ASCII_STRINGS, otherHeaders);
    }
    
    public static ReadOnlyHttp2Headers clientHeaders(final boolean validateHeaders, final AsciiString method, final AsciiString path, final AsciiString scheme, final AsciiString authority, final AsciiString... otherHeaders) {
        return new ReadOnlyHttp2Headers(validateHeaders, new AsciiString[] { PseudoHeaderName.METHOD.value(), method, PseudoHeaderName.PATH.value(), path, PseudoHeaderName.SCHEME.value(), scheme, PseudoHeaderName.AUTHORITY.value(), authority }, otherHeaders);
    }
    
    public static ReadOnlyHttp2Headers serverHeaders(final boolean validateHeaders, final AsciiString status, final AsciiString... otherHeaders) {
        return new ReadOnlyHttp2Headers(validateHeaders, new AsciiString[] { PseudoHeaderName.STATUS.value(), status }, otherHeaders);
    }
    
    private ReadOnlyHttp2Headers(final boolean validateHeaders, final AsciiString[] pseudoHeaders, final AsciiString... otherHeaders) {
        assert (pseudoHeaders.length & 0x1) == 0x0;
        if ((otherHeaders.length & 0x1) != 0x0) {
            throw newInvalidArraySizeException();
        }
        if (validateHeaders) {
            validateHeaders(pseudoHeaders, otherHeaders);
        }
        this.pseudoHeaders = pseudoHeaders;
        this.otherHeaders = otherHeaders;
    }
    
    private static IllegalArgumentException newInvalidArraySizeException() {
        return new IllegalArgumentException("pseudoHeaders and otherHeaders must be arrays of [name, value] pairs");
    }
    
    private static void validateHeaders(final AsciiString[] pseudoHeaders, final AsciiString... otherHeaders) {
        for (int i = 1; i < pseudoHeaders.length; i += 2) {
            if (pseudoHeaders[i] == null) {
                throw new IllegalArgumentException("pseudoHeaders value at index " + i + " is null");
            }
        }
        boolean seenNonPseudoHeader = false;
        for (int otherHeadersEnd = otherHeaders.length - 1, j = 0; j < otherHeadersEnd; j += 2) {
            final AsciiString name = otherHeaders[j];
            DefaultHttp2Headers.HTTP2_NAME_VALIDATOR.validateName(name);
            if (!seenNonPseudoHeader && !name.isEmpty() && name.byteAt(0) != 58) {
                seenNonPseudoHeader = true;
            }
            else if (seenNonPseudoHeader && !name.isEmpty() && name.byteAt(0) == 58) {
                throw new IllegalArgumentException("otherHeaders name at index " + j + " is a pseudo header that appears after non-pseudo headers.");
            }
            if (otherHeaders[j + 1] == null) {
                throw new IllegalArgumentException("otherHeaders value at index " + (j + 1) + " is null");
            }
        }
    }
    
    private AsciiString get0(final CharSequence name) {
        final int nameHash = AsciiString.hashCode(name);
        for (int pseudoHeadersEnd = this.pseudoHeaders.length - 1, i = 0; i < pseudoHeadersEnd; i += 2) {
            final AsciiString roName = this.pseudoHeaders[i];
            if (roName.hashCode() == nameHash && roName.contentEqualsIgnoreCase(name)) {
                return this.pseudoHeaders[i + 1];
            }
        }
        for (int otherHeadersEnd = this.otherHeaders.length - 1, j = 0; j < otherHeadersEnd; j += 2) {
            final AsciiString roName2 = this.otherHeaders[j];
            if (roName2.hashCode() == nameHash && roName2.contentEqualsIgnoreCase(name)) {
                return this.otherHeaders[j + 1];
            }
        }
        return null;
    }
    
    @Override
    public CharSequence get(final CharSequence name) {
        return this.get0(name);
    }
    
    @Override
    public CharSequence get(final CharSequence name, final CharSequence defaultValue) {
        final CharSequence value = this.get(name);
        return (value != null) ? value : defaultValue;
    }
    
    @Override
    public CharSequence getAndRemove(final CharSequence name) {
        throw new UnsupportedOperationException("read only");
    }
    
    @Override
    public CharSequence getAndRemove(final CharSequence name, final CharSequence defaultValue) {
        throw new UnsupportedOperationException("read only");
    }
    
    @Override
    public List<CharSequence> getAll(final CharSequence name) {
        final int nameHash = AsciiString.hashCode(name);
        final List<CharSequence> values = new ArrayList<CharSequence>();
        for (int pseudoHeadersEnd = this.pseudoHeaders.length - 1, i = 0; i < pseudoHeadersEnd; i += 2) {
            final AsciiString roName = this.pseudoHeaders[i];
            if (roName.hashCode() == nameHash && roName.contentEqualsIgnoreCase(name)) {
                values.add(this.pseudoHeaders[i + 1]);
            }
        }
        for (int otherHeadersEnd = this.otherHeaders.length - 1, j = 0; j < otherHeadersEnd; j += 2) {
            final AsciiString roName2 = this.otherHeaders[j];
            if (roName2.hashCode() == nameHash && roName2.contentEqualsIgnoreCase(name)) {
                values.add(this.otherHeaders[j + 1]);
            }
        }
        return values;
    }
    
    @Override
    public List<CharSequence> getAllAndRemove(final CharSequence name) {
        throw new UnsupportedOperationException("read only");
    }
    
    @Override
    public Boolean getBoolean(final CharSequence name) {
        final AsciiString value = this.get0(name);
        return (value != null) ? Boolean.valueOf(CharSequenceValueConverter.INSTANCE.convertToBoolean((CharSequence)value)) : null;
    }
    
    @Override
    public boolean getBoolean(final CharSequence name, final boolean defaultValue) {
        final Boolean value = this.getBoolean(name);
        return (value != null) ? value : defaultValue;
    }
    
    @Override
    public Byte getByte(final CharSequence name) {
        final AsciiString value = this.get0(name);
        return (value != null) ? Byte.valueOf(CharSequenceValueConverter.INSTANCE.convertToByte((CharSequence)value)) : null;
    }
    
    @Override
    public byte getByte(final CharSequence name, final byte defaultValue) {
        final Byte value = this.getByte(name);
        return (value != null) ? value : defaultValue;
    }
    
    @Override
    public Character getChar(final CharSequence name) {
        final AsciiString value = this.get0(name);
        return (value != null) ? Character.valueOf(CharSequenceValueConverter.INSTANCE.convertToChar((CharSequence)value)) : null;
    }
    
    @Override
    public char getChar(final CharSequence name, final char defaultValue) {
        final Character value = this.getChar(name);
        return (value != null) ? value : defaultValue;
    }
    
    @Override
    public Short getShort(final CharSequence name) {
        final AsciiString value = this.get0(name);
        return (value != null) ? Short.valueOf(CharSequenceValueConverter.INSTANCE.convertToShort((CharSequence)value)) : null;
    }
    
    @Override
    public short getShort(final CharSequence name, final short defaultValue) {
        final Short value = this.getShort(name);
        return (value != null) ? value : defaultValue;
    }
    
    @Override
    public Integer getInt(final CharSequence name) {
        final AsciiString value = this.get0(name);
        return (value != null) ? Integer.valueOf(CharSequenceValueConverter.INSTANCE.convertToInt((CharSequence)value)) : null;
    }
    
    @Override
    public int getInt(final CharSequence name, final int defaultValue) {
        final Integer value = this.getInt(name);
        return (value != null) ? value : defaultValue;
    }
    
    @Override
    public Long getLong(final CharSequence name) {
        final AsciiString value = this.get0(name);
        return (value != null) ? Long.valueOf(CharSequenceValueConverter.INSTANCE.convertToLong((CharSequence)value)) : null;
    }
    
    @Override
    public long getLong(final CharSequence name, final long defaultValue) {
        final Long value = this.getLong(name);
        return (value != null) ? value : defaultValue;
    }
    
    @Override
    public Float getFloat(final CharSequence name) {
        final AsciiString value = this.get0(name);
        return (value != null) ? Float.valueOf(CharSequenceValueConverter.INSTANCE.convertToFloat((CharSequence)value)) : null;
    }
    
    @Override
    public float getFloat(final CharSequence name, final float defaultValue) {
        final Float value = this.getFloat(name);
        return (value != null) ? value : defaultValue;
    }
    
    @Override
    public Double getDouble(final CharSequence name) {
        final AsciiString value = this.get0(name);
        return (value != null) ? Double.valueOf(CharSequenceValueConverter.INSTANCE.convertToDouble((CharSequence)value)) : null;
    }
    
    @Override
    public double getDouble(final CharSequence name, final double defaultValue) {
        final Double value = this.getDouble(name);
        return (value != null) ? value : defaultValue;
    }
    
    @Override
    public Long getTimeMillis(final CharSequence name) {
        final AsciiString value = this.get0(name);
        return (value != null) ? Long.valueOf(CharSequenceValueConverter.INSTANCE.convertToTimeMillis((CharSequence)value)) : null;
    }
    
    @Override
    public long getTimeMillis(final CharSequence name, final long defaultValue) {
        final Long value = this.getTimeMillis(name);
        return (value != null) ? value : defaultValue;
    }
    
    @Override
    public Boolean getBooleanAndRemove(final CharSequence name) {
        throw new UnsupportedOperationException("read only");
    }
    
    @Override
    public boolean getBooleanAndRemove(final CharSequence name, final boolean defaultValue) {
        throw new UnsupportedOperationException("read only");
    }
    
    @Override
    public Byte getByteAndRemove(final CharSequence name) {
        throw new UnsupportedOperationException("read only");
    }
    
    @Override
    public byte getByteAndRemove(final CharSequence name, final byte defaultValue) {
        throw new UnsupportedOperationException("read only");
    }
    
    @Override
    public Character getCharAndRemove(final CharSequence name) {
        throw new UnsupportedOperationException("read only");
    }
    
    @Override
    public char getCharAndRemove(final CharSequence name, final char defaultValue) {
        throw new UnsupportedOperationException("read only");
    }
    
    @Override
    public Short getShortAndRemove(final CharSequence name) {
        throw new UnsupportedOperationException("read only");
    }
    
    @Override
    public short getShortAndRemove(final CharSequence name, final short defaultValue) {
        throw new UnsupportedOperationException("read only");
    }
    
    @Override
    public Integer getIntAndRemove(final CharSequence name) {
        throw new UnsupportedOperationException("read only");
    }
    
    @Override
    public int getIntAndRemove(final CharSequence name, final int defaultValue) {
        throw new UnsupportedOperationException("read only");
    }
    
    @Override
    public Long getLongAndRemove(final CharSequence name) {
        throw new UnsupportedOperationException("read only");
    }
    
    @Override
    public long getLongAndRemove(final CharSequence name, final long defaultValue) {
        throw new UnsupportedOperationException("read only");
    }
    
    @Override
    public Float getFloatAndRemove(final CharSequence name) {
        throw new UnsupportedOperationException("read only");
    }
    
    @Override
    public float getFloatAndRemove(final CharSequence name, final float defaultValue) {
        throw new UnsupportedOperationException("read only");
    }
    
    @Override
    public Double getDoubleAndRemove(final CharSequence name) {
        throw new UnsupportedOperationException("read only");
    }
    
    @Override
    public double getDoubleAndRemove(final CharSequence name, final double defaultValue) {
        throw new UnsupportedOperationException("read only");
    }
    
    @Override
    public Long getTimeMillisAndRemove(final CharSequence name) {
        throw new UnsupportedOperationException("read only");
    }
    
    @Override
    public long getTimeMillisAndRemove(final CharSequence name, final long defaultValue) {
        throw new UnsupportedOperationException("read only");
    }
    
    @Override
    public boolean contains(final CharSequence name) {
        return this.get(name) != null;
    }
    
    @Override
    public boolean contains(final CharSequence name, final CharSequence value) {
        final int nameHash = AsciiString.hashCode(name);
        final int valueHash = AsciiString.hashCode(value);
        for (int pseudoHeadersEnd = this.pseudoHeaders.length - 1, i = 0; i < pseudoHeadersEnd; i += 2) {
            final AsciiString roName = this.pseudoHeaders[i];
            final AsciiString roValue = this.pseudoHeaders[i + 1];
            if (roName.hashCode() == nameHash && roValue.hashCode() == valueHash && roName.contentEqualsIgnoreCase(name) && roValue.contentEqualsIgnoreCase(value)) {
                return true;
            }
        }
        for (int otherHeadersEnd = this.otherHeaders.length - 1, j = 0; j < otherHeadersEnd; j += 2) {
            final AsciiString roName2 = this.otherHeaders[j];
            final AsciiString roValue2 = this.otherHeaders[j + 1];
            if (roName2.hashCode() == nameHash && roValue2.hashCode() == valueHash && roName2.contentEqualsIgnoreCase(name) && roValue2.contentEqualsIgnoreCase(value)) {
                return true;
            }
        }
        return false;
    }
    
    @Override
    public boolean containsObject(final CharSequence name, final Object value) {
        if (value instanceof CharSequence) {
            return this.contains(name, (CharSequence)value);
        }
        return this.contains(name, (CharSequence)value.toString());
    }
    
    @Override
    public boolean containsBoolean(final CharSequence name, final boolean value) {
        return this.contains(name, (CharSequence)String.valueOf(value));
    }
    
    @Override
    public boolean containsByte(final CharSequence name, final byte value) {
        return this.contains(name, (CharSequence)String.valueOf(value));
    }
    
    @Override
    public boolean containsChar(final CharSequence name, final char value) {
        return this.contains(name, (CharSequence)String.valueOf(value));
    }
    
    @Override
    public boolean containsShort(final CharSequence name, final short value) {
        return this.contains(name, (CharSequence)String.valueOf(value));
    }
    
    @Override
    public boolean containsInt(final CharSequence name, final int value) {
        return this.contains(name, (CharSequence)String.valueOf(value));
    }
    
    @Override
    public boolean containsLong(final CharSequence name, final long value) {
        return this.contains(name, (CharSequence)String.valueOf(value));
    }
    
    @Override
    public boolean containsFloat(final CharSequence name, final float value) {
        return false;
    }
    
    @Override
    public boolean containsDouble(final CharSequence name, final double value) {
        return this.contains(name, (CharSequence)String.valueOf(value));
    }
    
    @Override
    public boolean containsTimeMillis(final CharSequence name, final long value) {
        return this.contains(name, (CharSequence)String.valueOf(value));
    }
    
    @Override
    public int size() {
        return this.pseudoHeaders.length + this.otherHeaders.length >>> 1;
    }
    
    @Override
    public boolean isEmpty() {
        return this.pseudoHeaders.length == 0 && this.otherHeaders.length == 0;
    }
    
    @Override
    public Set<CharSequence> names() {
        if (this.isEmpty()) {
            return Collections.emptySet();
        }
        final Set<CharSequence> names = new LinkedHashSet<CharSequence>(this.size());
        for (int pseudoHeadersEnd = this.pseudoHeaders.length - 1, i = 0; i < pseudoHeadersEnd; i += 2) {
            names.add(this.pseudoHeaders[i]);
        }
        for (int otherHeadersEnd = this.otherHeaders.length - 1, j = 0; j < otherHeadersEnd; j += 2) {
            names.add(this.otherHeaders[j]);
        }
        return names;
    }
    
    @Override
    public Http2Headers add(final CharSequence name, final CharSequence value) {
        throw new UnsupportedOperationException("read only");
    }
    
    @Override
    public Http2Headers add(final CharSequence name, final Iterable<? extends CharSequence> values) {
        throw new UnsupportedOperationException("read only");
    }
    
    @Override
    public Http2Headers add(final CharSequence name, final CharSequence... values) {
        throw new UnsupportedOperationException("read only");
    }
    
    @Override
    public Http2Headers addObject(final CharSequence name, final Object value) {
        throw new UnsupportedOperationException("read only");
    }
    
    @Override
    public Http2Headers addObject(final CharSequence name, final Iterable<?> values) {
        throw new UnsupportedOperationException("read only");
    }
    
    @Override
    public Http2Headers addObject(final CharSequence name, final Object... values) {
        throw new UnsupportedOperationException("read only");
    }
    
    @Override
    public Http2Headers addBoolean(final CharSequence name, final boolean value) {
        throw new UnsupportedOperationException("read only");
    }
    
    @Override
    public Http2Headers addByte(final CharSequence name, final byte value) {
        throw new UnsupportedOperationException("read only");
    }
    
    @Override
    public Http2Headers addChar(final CharSequence name, final char value) {
        throw new UnsupportedOperationException("read only");
    }
    
    @Override
    public Http2Headers addShort(final CharSequence name, final short value) {
        throw new UnsupportedOperationException("read only");
    }
    
    @Override
    public Http2Headers addInt(final CharSequence name, final int value) {
        throw new UnsupportedOperationException("read only");
    }
    
    @Override
    public Http2Headers addLong(final CharSequence name, final long value) {
        throw new UnsupportedOperationException("read only");
    }
    
    @Override
    public Http2Headers addFloat(final CharSequence name, final float value) {
        throw new UnsupportedOperationException("read only");
    }
    
    @Override
    public Http2Headers addDouble(final CharSequence name, final double value) {
        throw new UnsupportedOperationException("read only");
    }
    
    @Override
    public Http2Headers addTimeMillis(final CharSequence name, final long value) {
        throw new UnsupportedOperationException("read only");
    }
    
    @Override
    public Http2Headers add(final Headers<? extends CharSequence, ? extends CharSequence, ?> headers) {
        throw new UnsupportedOperationException("read only");
    }
    
    @Override
    public Http2Headers set(final CharSequence name, final CharSequence value) {
        throw new UnsupportedOperationException("read only");
    }
    
    @Override
    public Http2Headers set(final CharSequence name, final Iterable<? extends CharSequence> values) {
        throw new UnsupportedOperationException("read only");
    }
    
    @Override
    public Http2Headers set(final CharSequence name, final CharSequence... values) {
        throw new UnsupportedOperationException("read only");
    }
    
    @Override
    public Http2Headers setObject(final CharSequence name, final Object value) {
        throw new UnsupportedOperationException("read only");
    }
    
    @Override
    public Http2Headers setObject(final CharSequence name, final Iterable<?> values) {
        throw new UnsupportedOperationException("read only");
    }
    
    @Override
    public Http2Headers setObject(final CharSequence name, final Object... values) {
        throw new UnsupportedOperationException("read only");
    }
    
    @Override
    public Http2Headers setBoolean(final CharSequence name, final boolean value) {
        throw new UnsupportedOperationException("read only");
    }
    
    @Override
    public Http2Headers setByte(final CharSequence name, final byte value) {
        throw new UnsupportedOperationException("read only");
    }
    
    @Override
    public Http2Headers setChar(final CharSequence name, final char value) {
        throw new UnsupportedOperationException("read only");
    }
    
    @Override
    public Http2Headers setShort(final CharSequence name, final short value) {
        throw new UnsupportedOperationException("read only");
    }
    
    @Override
    public Http2Headers setInt(final CharSequence name, final int value) {
        throw new UnsupportedOperationException("read only");
    }
    
    @Override
    public Http2Headers setLong(final CharSequence name, final long value) {
        throw new UnsupportedOperationException("read only");
    }
    
    @Override
    public Http2Headers setFloat(final CharSequence name, final float value) {
        throw new UnsupportedOperationException("read only");
    }
    
    @Override
    public Http2Headers setDouble(final CharSequence name, final double value) {
        throw new UnsupportedOperationException("read only");
    }
    
    @Override
    public Http2Headers setTimeMillis(final CharSequence name, final long value) {
        throw new UnsupportedOperationException("read only");
    }
    
    @Override
    public Http2Headers set(final Headers<? extends CharSequence, ? extends CharSequence, ?> headers) {
        throw new UnsupportedOperationException("read only");
    }
    
    @Override
    public Http2Headers setAll(final Headers<? extends CharSequence, ? extends CharSequence, ?> headers) {
        throw new UnsupportedOperationException("read only");
    }
    
    @Override
    public boolean remove(final CharSequence name) {
        throw new UnsupportedOperationException("read only");
    }
    
    @Override
    public Http2Headers clear() {
        throw new UnsupportedOperationException("read only");
    }
    
    @Override
    public Iterator<Map.Entry<CharSequence, CharSequence>> iterator() {
        return new ReadOnlyIterator();
    }
    
    @Override
    public Http2Headers method(final CharSequence value) {
        throw new UnsupportedOperationException("read only");
    }
    
    @Override
    public Http2Headers scheme(final CharSequence value) {
        throw new UnsupportedOperationException("read only");
    }
    
    @Override
    public Http2Headers authority(final CharSequence value) {
        throw new UnsupportedOperationException("read only");
    }
    
    @Override
    public Http2Headers path(final CharSequence value) {
        throw new UnsupportedOperationException("read only");
    }
    
    @Override
    public Http2Headers status(final CharSequence value) {
        throw new UnsupportedOperationException("read only");
    }
    
    @Override
    public CharSequence method() {
        return this.get((CharSequence)PseudoHeaderName.METHOD.value());
    }
    
    @Override
    public CharSequence scheme() {
        return this.get((CharSequence)PseudoHeaderName.SCHEME.value());
    }
    
    @Override
    public CharSequence authority() {
        return this.get((CharSequence)PseudoHeaderName.AUTHORITY.value());
    }
    
    @Override
    public CharSequence path() {
        return this.get((CharSequence)PseudoHeaderName.PATH.value());
    }
    
    @Override
    public CharSequence status() {
        return this.get((CharSequence)PseudoHeaderName.STATUS.value());
    }
    
    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder(this.getClass().getSimpleName()).append('[');
        String separator = "";
        for (final Map.Entry<CharSequence, CharSequence> entry : this) {
            builder.append(separator);
            builder.append(entry.getKey()).append(": ").append(entry.getValue());
            separator = ", ";
        }
        return builder.append(']').toString();
    }
    
    private final class ReadOnlyIterator implements Map.Entry<CharSequence, CharSequence>, Iterator<Map.Entry<CharSequence, CharSequence>>
    {
        private int i;
        private AsciiString[] current;
        private AsciiString key;
        private AsciiString value;
        
        private ReadOnlyIterator() {
            this.current = ((ReadOnlyHttp2Headers.this.pseudoHeaders.length != 0) ? ReadOnlyHttp2Headers.this.pseudoHeaders : ReadOnlyHttp2Headers.this.otherHeaders);
        }
        
        @Override
        public boolean hasNext() {
            return this.i != this.current.length;
        }
        
        @Override
        public Map.Entry<CharSequence, CharSequence> next() {
            if (!this.hasNext()) {
                throw new NoSuchElementException();
            }
            this.key = this.current[this.i];
            this.value = this.current[this.i + 1];
            this.i += 2;
            if (this.i == this.current.length && this.current == ReadOnlyHttp2Headers.this.pseudoHeaders) {
                this.current = ReadOnlyHttp2Headers.this.otherHeaders;
                this.i = 0;
            }
            return this;
        }
        
        @Override
        public CharSequence getKey() {
            return this.key;
        }
        
        @Override
        public CharSequence getValue() {
            return this.value;
        }
        
        @Override
        public CharSequence setValue(final CharSequence value) {
            throw new UnsupportedOperationException("read only");
        }
        
        @Override
        public void remove() {
            throw new UnsupportedOperationException("read only");
        }
        
        @Override
        public String toString() {
            return this.key.toString() + '=' + this.value.toString();
        }
    }
}
