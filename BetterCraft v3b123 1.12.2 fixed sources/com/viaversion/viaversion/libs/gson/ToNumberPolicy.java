// 
// Decompiled by Procyon v0.6.0
// 

package com.viaversion.viaversion.libs.gson;

import java.math.BigDecimal;
import com.viaversion.viaversion.libs.gson.stream.MalformedJsonException;
import com.viaversion.viaversion.libs.gson.internal.LazilyParsedNumber;
import java.io.IOException;
import com.viaversion.viaversion.libs.gson.stream.JsonReader;

public enum ToNumberPolicy implements ToNumberStrategy
{
    DOUBLE {
        @Override
        public Double readNumber(final JsonReader in) throws IOException {
            return in.nextDouble();
        }
    }, 
    LAZILY_PARSED_NUMBER {
        @Override
        public Number readNumber(final JsonReader in) throws IOException {
            return new LazilyParsedNumber(in.nextString());
        }
    }, 
    LONG_OR_DOUBLE {
        @Override
        public Number readNumber(final JsonReader in) throws IOException, JsonParseException {
            final String value = in.nextString();
            try {
                return Long.parseLong(value);
            }
            catch (final NumberFormatException longE) {
                try {
                    final Double d = Double.valueOf(value);
                    if ((d.isInfinite() || d.isNaN()) && !in.isLenient()) {
                        throw new MalformedJsonException("JSON forbids NaN and infinities: " + d + "; at path " + in.getPreviousPath());
                    }
                    return d;
                }
                catch (final NumberFormatException doubleE) {
                    throw new JsonParseException("Cannot parse " + value + "; at path " + in.getPreviousPath(), doubleE);
                }
            }
        }
    }, 
    BIG_DECIMAL {
        @Override
        public BigDecimal readNumber(final JsonReader in) throws IOException {
            final String value = in.nextString();
            try {
                return new BigDecimal(value);
            }
            catch (final NumberFormatException e) {
                throw new JsonParseException("Cannot parse " + value + "; at path " + in.getPreviousPath(), e);
            }
        }
    };
}
