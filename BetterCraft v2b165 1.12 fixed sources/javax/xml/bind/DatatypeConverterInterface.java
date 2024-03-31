// 
// Decompiled by Procyon v0.6.0
// 

package javax.xml.bind;

import java.util.Calendar;
import javax.xml.namespace.QName;
import javax.xml.namespace.NamespaceContext;
import java.math.BigDecimal;
import java.math.BigInteger;

public interface DatatypeConverterInterface
{
    String parseString(final String p0);
    
    BigInteger parseInteger(final String p0);
    
    int parseInt(final String p0);
    
    long parseLong(final String p0);
    
    short parseShort(final String p0);
    
    BigDecimal parseDecimal(final String p0);
    
    float parseFloat(final String p0);
    
    double parseDouble(final String p0);
    
    boolean parseBoolean(final String p0);
    
    byte parseByte(final String p0);
    
    QName parseQName(final String p0, final NamespaceContext p1);
    
    Calendar parseDateTime(final String p0);
    
    byte[] parseBase64Binary(final String p0);
    
    byte[] parseHexBinary(final String p0);
    
    long parseUnsignedInt(final String p0);
    
    int parseUnsignedShort(final String p0);
    
    Calendar parseTime(final String p0);
    
    Calendar parseDate(final String p0);
    
    String parseAnySimpleType(final String p0);
    
    String printString(final String p0);
    
    String printInteger(final BigInteger p0);
    
    String printInt(final int p0);
    
    String printLong(final long p0);
    
    String printShort(final short p0);
    
    String printDecimal(final BigDecimal p0);
    
    String printFloat(final float p0);
    
    String printDouble(final double p0);
    
    String printBoolean(final boolean p0);
    
    String printByte(final byte p0);
    
    String printQName(final QName p0, final NamespaceContext p1);
    
    String printDateTime(final Calendar p0);
    
    String printBase64Binary(final byte[] p0);
    
    String printHexBinary(final byte[] p0);
    
    String printUnsignedInt(final long p0);
    
    String printUnsignedShort(final int p0);
    
    String printTime(final Calendar p0);
    
    String printDate(final Calendar p0);
    
    String printAnySimpleType(final String p0);
}
