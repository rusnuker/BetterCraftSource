// 
// Decompiled by Procyon v0.6.0
// 

package io.netty.handler.codec.http;

import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import io.netty.handler.codec.Headers;
import java.util.List;
import io.netty.handler.codec.ValueConverter;
import io.netty.util.HashingStrategy;
import io.netty.util.internal.StringUtil;
import io.netty.handler.codec.DefaultHeaders;
import io.netty.util.AsciiString;

public class CombinedHttpHeaders extends DefaultHttpHeaders
{
    public CombinedHttpHeaders(final boolean validate) {
        super(new CombinedHttpHeadersImpl(AsciiString.CASE_INSENSITIVE_HASHER, DefaultHttpHeaders.valueConverter(validate), DefaultHttpHeaders.nameValidator(validate)));
    }
    
    private static final class CombinedHttpHeadersImpl extends DefaultHeaders<CharSequence, CharSequence, CombinedHttpHeadersImpl>
    {
        private static final int VALUE_LENGTH_ESTIMATE = 10;
        private CsvValueEscaper<Object> objectEscaper;
        private CsvValueEscaper<CharSequence> charSequenceEscaper;
        
        private CsvValueEscaper<Object> objectEscaper() {
            if (this.objectEscaper == null) {
                this.objectEscaper = new CsvValueEscaper<Object>() {
                    @Override
                    public CharSequence escape(final Object value) {
                        return StringUtil.escapeCsv(DefaultHeaders.this.valueConverter().convertObject(value));
                    }
                };
            }
            return this.objectEscaper;
        }
        
        private CsvValueEscaper<CharSequence> charSequenceEscaper() {
            if (this.charSequenceEscaper == null) {
                this.charSequenceEscaper = new CsvValueEscaper<CharSequence>() {
                    @Override
                    public CharSequence escape(final CharSequence value) {
                        return StringUtil.escapeCsv(value);
                    }
                };
            }
            return this.charSequenceEscaper;
        }
        
        public CombinedHttpHeadersImpl(final HashingStrategy<CharSequence> nameHashingStrategy, final ValueConverter<CharSequence> valueConverter, final NameValidator<CharSequence> nameValidator) {
            super(nameHashingStrategy, valueConverter, nameValidator);
        }
        
        @Override
        public List<CharSequence> getAll(final CharSequence name) {
            final List<CharSequence> values = super.getAll(name);
            if (values.isEmpty()) {
                return values;
            }
            if (values.size() != 1) {
                throw new IllegalStateException("CombinedHttpHeaders should only have one value");
            }
            return StringUtil.unescapeCsvFields(values.get(0));
        }
        
        @Override
        public CombinedHttpHeadersImpl add(final Headers<? extends CharSequence, ? extends CharSequence, ?> headers) {
            if (headers == this) {
                throw new IllegalArgumentException("can't add to itself.");
            }
            if (headers instanceof CombinedHttpHeadersImpl) {
                if (this.isEmpty()) {
                    this.addImpl(headers);
                }
                else {
                    for (final Map.Entry<? extends CharSequence, ? extends CharSequence> header : headers) {
                        this.addEscapedValue((CharSequence)header.getKey(), (CharSequence)header.getValue());
                    }
                }
            }
            else {
                for (final Map.Entry<? extends CharSequence, ? extends CharSequence> header : headers) {
                    this.add((CharSequence)header.getKey(), (CharSequence)header.getValue());
                }
            }
            return this;
        }
        
        @Override
        public CombinedHttpHeadersImpl set(final Headers<? extends CharSequence, ? extends CharSequence, ?> headers) {
            if (headers == this) {
                return this;
            }
            this.clear();
            return this.add(headers);
        }
        
        @Override
        public CombinedHttpHeadersImpl setAll(final Headers<? extends CharSequence, ? extends CharSequence, ?> headers) {
            if (headers == this) {
                return this;
            }
            for (final CharSequence key : headers.names()) {
                ((DefaultHeaders<CharSequence, V, T>)this).remove(key);
            }
            return this.add(headers);
        }
        
        @Override
        public CombinedHttpHeadersImpl add(final CharSequence name, final CharSequence value) {
            return this.addEscapedValue(name, StringUtil.escapeCsv(value));
        }
        
        @Override
        public CombinedHttpHeadersImpl add(final CharSequence name, final CharSequence... values) {
            return this.addEscapedValue(name, commaSeparate(this.charSequenceEscaper(), values));
        }
        
        @Override
        public CombinedHttpHeadersImpl add(final CharSequence name, final Iterable<? extends CharSequence> values) {
            return this.addEscapedValue(name, commaSeparate(this.charSequenceEscaper(), values));
        }
        
        @Override
        public CombinedHttpHeadersImpl addObject(final CharSequence name, final Iterable<?> values) {
            return this.addEscapedValue(name, commaSeparate(this.objectEscaper(), values));
        }
        
        @Override
        public CombinedHttpHeadersImpl addObject(final CharSequence name, final Object... values) {
            return this.addEscapedValue(name, commaSeparate(this.objectEscaper(), values));
        }
        
        @Override
        public CombinedHttpHeadersImpl set(final CharSequence name, final CharSequence... values) {
            super.set(name, commaSeparate(this.charSequenceEscaper(), values));
            return this;
        }
        
        @Override
        public CombinedHttpHeadersImpl set(final CharSequence name, final Iterable<? extends CharSequence> values) {
            super.set(name, commaSeparate(this.charSequenceEscaper(), values));
            return this;
        }
        
        @Override
        public CombinedHttpHeadersImpl setObject(final CharSequence name, final Object value) {
            super.set(name, commaSeparate(this.objectEscaper(), value));
            return this;
        }
        
        @Override
        public CombinedHttpHeadersImpl setObject(final CharSequence name, final Object... values) {
            super.set(name, commaSeparate(this.objectEscaper(), values));
            return this;
        }
        
        @Override
        public CombinedHttpHeadersImpl setObject(final CharSequence name, final Iterable<?> values) {
            super.set(name, commaSeparate(this.objectEscaper(), values));
            return this;
        }
        
        private CombinedHttpHeadersImpl addEscapedValue(final CharSequence name, final CharSequence escapedValue) {
            final CharSequence currentValue = super.get(name);
            if (currentValue == null) {
                super.add(name, escapedValue);
            }
            else {
                super.set(name, commaSeparateEscapedValues(currentValue, escapedValue));
            }
            return this;
        }
        
        private static <T> CharSequence commaSeparate(final CsvValueEscaper<T> escaper, final T... values) {
            final StringBuilder sb = new StringBuilder(values.length * 10);
            if (values.length > 0) {
                final int end = values.length - 1;
                for (int i = 0; i < end; ++i) {
                    sb.append(escaper.escape(values[i])).append(',');
                }
                sb.append(escaper.escape(values[end]));
            }
            return sb;
        }
        
        private static <T> CharSequence commaSeparate(final CsvValueEscaper<T> escaper, final Iterable<? extends T> values) {
            final StringBuilder sb = (values instanceof Collection) ? new StringBuilder(((Collection)values).size() * 10) : new StringBuilder();
            final Iterator<? extends T> iterator = values.iterator();
            if (iterator.hasNext()) {
                T next = (T)iterator.next();
                while (iterator.hasNext()) {
                    sb.append(escaper.escape(next)).append(',');
                    next = (T)iterator.next();
                }
                sb.append(escaper.escape(next));
            }
            return sb;
        }
        
        private static CharSequence commaSeparateEscapedValues(final CharSequence currentValue, final CharSequence value) {
            return new StringBuilder(currentValue.length() + 1 + value.length()).append(currentValue).append(',').append(value);
        }
        
        private interface CsvValueEscaper<T>
        {
            CharSequence escape(final T p0);
        }
    }
}
