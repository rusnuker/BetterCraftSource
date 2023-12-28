// 
// Decompiled by Procyon v0.6.0
// 

package io.netty.handler.codec;

public interface ValueConverter<T>
{
    T convertObject(final Object p0);
    
    T convertBoolean(final boolean p0);
    
    boolean convertToBoolean(final T p0);
    
    T convertByte(final byte p0);
    
    byte convertToByte(final T p0);
    
    T convertChar(final char p0);
    
    char convertToChar(final T p0);
    
    T convertShort(final short p0);
    
    short convertToShort(final T p0);
    
    T convertInt(final int p0);
    
    int convertToInt(final T p0);
    
    T convertLong(final long p0);
    
    long convertToLong(final T p0);
    
    T convertTimeMillis(final long p0);
    
    long convertToTimeMillis(final T p0);
    
    T convertFloat(final float p0);
    
    float convertToFloat(final T p0);
    
    T convertDouble(final double p0);
    
    double convertToDouble(final T p0);
}
