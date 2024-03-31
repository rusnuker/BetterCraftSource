// 
// Decompiled by Procyon v0.6.0
// 

package com.viaversion.viaversion.libs.gson;

import java.math.BigInteger;
import java.math.BigDecimal;
import com.viaversion.viaversion.libs.gson.internal.LazilyParsedNumber;
import java.util.Objects;

public final class JsonPrimitive extends JsonElement
{
    private final Object value;
    
    public JsonPrimitive(final Boolean bool) {
        this.value = Objects.requireNonNull(bool);
    }
    
    public JsonPrimitive(final Number number) {
        this.value = Objects.requireNonNull(number);
    }
    
    public JsonPrimitive(final String string) {
        this.value = Objects.requireNonNull(string);
    }
    
    public JsonPrimitive(final Character c) {
        this.value = Objects.requireNonNull(c).toString();
    }
    
    @Override
    public JsonPrimitive deepCopy() {
        return this;
    }
    
    public boolean isBoolean() {
        return this.value instanceof Boolean;
    }
    
    @Override
    public boolean getAsBoolean() {
        if (this.isBoolean()) {
            return (boolean)this.value;
        }
        return Boolean.parseBoolean(this.getAsString());
    }
    
    public boolean isNumber() {
        return this.value instanceof Number;
    }
    
    @Override
    public Number getAsNumber() {
        if (this.value instanceof Number) {
            return (Number)this.value;
        }
        if (this.value instanceof String) {
            return new LazilyParsedNumber((String)this.value);
        }
        throw new UnsupportedOperationException("Primitive is neither a number nor a string");
    }
    
    public boolean isString() {
        return this.value instanceof String;
    }
    
    @Override
    public String getAsString() {
        if (this.value instanceof String) {
            return (String)this.value;
        }
        if (this.isNumber()) {
            return this.getAsNumber().toString();
        }
        if (this.isBoolean()) {
            return ((Boolean)this.value).toString();
        }
        throw new AssertionError((Object)("Unexpected value type: " + this.value.getClass()));
    }
    
    @Override
    public double getAsDouble() {
        return this.isNumber() ? this.getAsNumber().doubleValue() : Double.parseDouble(this.getAsString());
    }
    
    @Override
    public BigDecimal getAsBigDecimal() {
        return (BigDecimal)((this.value instanceof BigDecimal) ? this.value : new BigDecimal(this.getAsString()));
    }
    
    @Override
    public BigInteger getAsBigInteger() {
        return (BigInteger)((this.value instanceof BigInteger) ? this.value : new BigInteger(this.getAsString()));
    }
    
    @Override
    public float getAsFloat() {
        return this.isNumber() ? this.getAsNumber().floatValue() : Float.parseFloat(this.getAsString());
    }
    
    @Override
    public long getAsLong() {
        return this.isNumber() ? this.getAsNumber().longValue() : Long.parseLong(this.getAsString());
    }
    
    @Override
    public short getAsShort() {
        return this.isNumber() ? this.getAsNumber().shortValue() : Short.parseShort(this.getAsString());
    }
    
    @Override
    public int getAsInt() {
        return this.isNumber() ? this.getAsNumber().intValue() : Integer.parseInt(this.getAsString());
    }
    
    @Override
    public byte getAsByte() {
        return this.isNumber() ? this.getAsNumber().byteValue() : Byte.parseByte(this.getAsString());
    }
    
    @Deprecated
    @Override
    public char getAsCharacter() {
        final String s = this.getAsString();
        if (s.isEmpty()) {
            throw new UnsupportedOperationException("String value is empty");
        }
        return s.charAt(0);
    }
    
    @Override
    public int hashCode() {
        if (this.value == null) {
            return 31;
        }
        if (isIntegral(this)) {
            final long value = this.getAsNumber().longValue();
            return (int)(value ^ value >>> 32);
        }
        if (this.value instanceof Number) {
            final long value = Double.doubleToLongBits(this.getAsNumber().doubleValue());
            return (int)(value ^ value >>> 32);
        }
        return this.value.hashCode();
    }
    
    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || this.getClass() != obj.getClass()) {
            return false;
        }
        final JsonPrimitive other = (JsonPrimitive)obj;
        if (this.value == null) {
            return other.value == null;
        }
        if (isIntegral(this) && isIntegral(other)) {
            return this.getAsNumber().longValue() == other.getAsNumber().longValue();
        }
        if (this.value instanceof Number && other.value instanceof Number) {
            final double a = this.getAsNumber().doubleValue();
            final double b = other.getAsNumber().doubleValue();
            return a == b || (Double.isNaN(a) && Double.isNaN(b));
        }
        return this.value.equals(other.value);
    }
    
    private static boolean isIntegral(final JsonPrimitive primitive) {
        if (primitive.value instanceof Number) {
            final Number number = (Number)primitive.value;
            return number instanceof BigInteger || number instanceof Long || number instanceof Integer || number instanceof Short || number instanceof Byte;
        }
        return false;
    }
}
