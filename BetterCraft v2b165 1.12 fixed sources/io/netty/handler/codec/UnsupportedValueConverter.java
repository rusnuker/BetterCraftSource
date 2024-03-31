// 
// Decompiled by Procyon v0.6.0
// 

package io.netty.handler.codec;

public final class UnsupportedValueConverter<V> implements ValueConverter<V>
{
    private static final UnsupportedValueConverter INSTANCE;
    
    private UnsupportedValueConverter() {
    }
    
    public static <V> UnsupportedValueConverter<V> instance() {
        return UnsupportedValueConverter.INSTANCE;
    }
    
    @Override
    public V convertObject(final Object value) {
        throw new UnsupportedOperationException();
    }
    
    @Override
    public V convertBoolean(final boolean value) {
        throw new UnsupportedOperationException();
    }
    
    @Override
    public boolean convertToBoolean(final V value) {
        throw new UnsupportedOperationException();
    }
    
    @Override
    public V convertByte(final byte value) {
        throw new UnsupportedOperationException();
    }
    
    @Override
    public byte convertToByte(final V value) {
        throw new UnsupportedOperationException();
    }
    
    @Override
    public V convertChar(final char value) {
        throw new UnsupportedOperationException();
    }
    
    @Override
    public char convertToChar(final V value) {
        throw new UnsupportedOperationException();
    }
    
    @Override
    public V convertShort(final short value) {
        throw new UnsupportedOperationException();
    }
    
    @Override
    public short convertToShort(final V value) {
        throw new UnsupportedOperationException();
    }
    
    @Override
    public V convertInt(final int value) {
        throw new UnsupportedOperationException();
    }
    
    @Override
    public int convertToInt(final V value) {
        throw new UnsupportedOperationException();
    }
    
    @Override
    public V convertLong(final long value) {
        throw new UnsupportedOperationException();
    }
    
    @Override
    public long convertToLong(final V value) {
        throw new UnsupportedOperationException();
    }
    
    @Override
    public V convertTimeMillis(final long value) {
        throw new UnsupportedOperationException();
    }
    
    @Override
    public long convertToTimeMillis(final V value) {
        throw new UnsupportedOperationException();
    }
    
    @Override
    public V convertFloat(final float value) {
        throw new UnsupportedOperationException();
    }
    
    @Override
    public float convertToFloat(final V value) {
        throw new UnsupportedOperationException();
    }
    
    @Override
    public V convertDouble(final double value) {
        throw new UnsupportedOperationException();
    }
    
    @Override
    public double convertToDouble(final V value) {
        throw new UnsupportedOperationException();
    }
    
    static {
        INSTANCE = new UnsupportedValueConverter();
    }
}
