// 
// Decompiled by Procyon v0.6.0
// 

package javax.xml.bind;

import java.util.Calendar;
import javax.xml.namespace.QName;
import javax.xml.namespace.NamespaceContext;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.security.Permission;

public final class DatatypeConverter
{
    private static volatile DatatypeConverterInterface theConverter;
    private static final JAXBPermission SET_DATATYPE_CONVERTER_PERMISSION;
    
    static {
        DatatypeConverter.theConverter = null;
        SET_DATATYPE_CONVERTER_PERMISSION = new JAXBPermission("setDatatypeConverter");
    }
    
    private DatatypeConverter() {
    }
    
    public static void setDatatypeConverter(final DatatypeConverterInterface converter) {
        if (converter == null) {
            throw new IllegalArgumentException(Messages.format("DatatypeConverter.ConverterMustNotBeNull"));
        }
        if (DatatypeConverter.theConverter == null) {
            final SecurityManager sm = System.getSecurityManager();
            if (sm != null) {
                sm.checkPermission(DatatypeConverter.SET_DATATYPE_CONVERTER_PERMISSION);
            }
            DatatypeConverter.theConverter = converter;
        }
    }
    
    private static synchronized void initConverter() {
        DatatypeConverter.theConverter = new DatatypeConverterImpl();
    }
    
    public static String parseString(final String lexicalXSDString) {
        if (DatatypeConverter.theConverter == null) {
            initConverter();
        }
        return DatatypeConverter.theConverter.parseString(lexicalXSDString);
    }
    
    public static BigInteger parseInteger(final String lexicalXSDInteger) {
        if (DatatypeConverter.theConverter == null) {
            initConverter();
        }
        return DatatypeConverter.theConverter.parseInteger(lexicalXSDInteger);
    }
    
    public static int parseInt(final String lexicalXSDInt) {
        if (DatatypeConverter.theConverter == null) {
            initConverter();
        }
        return DatatypeConverter.theConverter.parseInt(lexicalXSDInt);
    }
    
    public static long parseLong(final String lexicalXSDLong) {
        if (DatatypeConverter.theConverter == null) {
            initConverter();
        }
        return DatatypeConverter.theConverter.parseLong(lexicalXSDLong);
    }
    
    public static short parseShort(final String lexicalXSDShort) {
        if (DatatypeConverter.theConverter == null) {
            initConverter();
        }
        return DatatypeConverter.theConverter.parseShort(lexicalXSDShort);
    }
    
    public static BigDecimal parseDecimal(final String lexicalXSDDecimal) {
        if (DatatypeConverter.theConverter == null) {
            initConverter();
        }
        return DatatypeConverter.theConverter.parseDecimal(lexicalXSDDecimal);
    }
    
    public static float parseFloat(final String lexicalXSDFloat) {
        if (DatatypeConverter.theConverter == null) {
            initConverter();
        }
        return DatatypeConverter.theConverter.parseFloat(lexicalXSDFloat);
    }
    
    public static double parseDouble(final String lexicalXSDDouble) {
        if (DatatypeConverter.theConverter == null) {
            initConverter();
        }
        return DatatypeConverter.theConverter.parseDouble(lexicalXSDDouble);
    }
    
    public static boolean parseBoolean(final String lexicalXSDBoolean) {
        if (DatatypeConverter.theConverter == null) {
            initConverter();
        }
        return DatatypeConverter.theConverter.parseBoolean(lexicalXSDBoolean);
    }
    
    public static byte parseByte(final String lexicalXSDByte) {
        if (DatatypeConverter.theConverter == null) {
            initConverter();
        }
        return DatatypeConverter.theConverter.parseByte(lexicalXSDByte);
    }
    
    public static QName parseQName(final String lexicalXSDQName, final NamespaceContext nsc) {
        if (DatatypeConverter.theConverter == null) {
            initConverter();
        }
        return DatatypeConverter.theConverter.parseQName(lexicalXSDQName, nsc);
    }
    
    public static Calendar parseDateTime(final String lexicalXSDDateTime) {
        if (DatatypeConverter.theConverter == null) {
            initConverter();
        }
        return DatatypeConverter.theConverter.parseDateTime(lexicalXSDDateTime);
    }
    
    public static byte[] parseBase64Binary(final String lexicalXSDBase64Binary) {
        if (DatatypeConverter.theConverter == null) {
            initConverter();
        }
        return DatatypeConverter.theConverter.parseBase64Binary(lexicalXSDBase64Binary);
    }
    
    public static byte[] parseHexBinary(final String lexicalXSDHexBinary) {
        if (DatatypeConverter.theConverter == null) {
            initConverter();
        }
        return DatatypeConverter.theConverter.parseHexBinary(lexicalXSDHexBinary);
    }
    
    public static long parseUnsignedInt(final String lexicalXSDUnsignedInt) {
        if (DatatypeConverter.theConverter == null) {
            initConverter();
        }
        return DatatypeConverter.theConverter.parseUnsignedInt(lexicalXSDUnsignedInt);
    }
    
    public static int parseUnsignedShort(final String lexicalXSDUnsignedShort) {
        if (DatatypeConverter.theConverter == null) {
            initConverter();
        }
        return DatatypeConverter.theConverter.parseUnsignedShort(lexicalXSDUnsignedShort);
    }
    
    public static Calendar parseTime(final String lexicalXSDTime) {
        if (DatatypeConverter.theConverter == null) {
            initConverter();
        }
        return DatatypeConverter.theConverter.parseTime(lexicalXSDTime);
    }
    
    public static Calendar parseDate(final String lexicalXSDDate) {
        if (DatatypeConverter.theConverter == null) {
            initConverter();
        }
        return DatatypeConverter.theConverter.parseDate(lexicalXSDDate);
    }
    
    public static String parseAnySimpleType(final String lexicalXSDAnySimpleType) {
        if (DatatypeConverter.theConverter == null) {
            initConverter();
        }
        return DatatypeConverter.theConverter.parseAnySimpleType(lexicalXSDAnySimpleType);
    }
    
    public static String printString(final String val) {
        if (DatatypeConverter.theConverter == null) {
            initConverter();
        }
        return DatatypeConverter.theConverter.printString(val);
    }
    
    public static String printInteger(final BigInteger val) {
        if (DatatypeConverter.theConverter == null) {
            initConverter();
        }
        return DatatypeConverter.theConverter.printInteger(val);
    }
    
    public static String printInt(final int val) {
        if (DatatypeConverter.theConverter == null) {
            initConverter();
        }
        return DatatypeConverter.theConverter.printInt(val);
    }
    
    public static String printLong(final long val) {
        if (DatatypeConverter.theConverter == null) {
            initConverter();
        }
        return DatatypeConverter.theConverter.printLong(val);
    }
    
    public static String printShort(final short val) {
        if (DatatypeConverter.theConverter == null) {
            initConverter();
        }
        return DatatypeConverter.theConverter.printShort(val);
    }
    
    public static String printDecimal(final BigDecimal val) {
        if (DatatypeConverter.theConverter == null) {
            initConverter();
        }
        return DatatypeConverter.theConverter.printDecimal(val);
    }
    
    public static String printFloat(final float val) {
        if (DatatypeConverter.theConverter == null) {
            initConverter();
        }
        return DatatypeConverter.theConverter.printFloat(val);
    }
    
    public static String printDouble(final double val) {
        if (DatatypeConverter.theConverter == null) {
            initConverter();
        }
        return DatatypeConverter.theConverter.printDouble(val);
    }
    
    public static String printBoolean(final boolean val) {
        if (DatatypeConverter.theConverter == null) {
            initConverter();
        }
        return DatatypeConverter.theConverter.printBoolean(val);
    }
    
    public static String printByte(final byte val) {
        if (DatatypeConverter.theConverter == null) {
            initConverter();
        }
        return DatatypeConverter.theConverter.printByte(val);
    }
    
    public static String printQName(final QName val, final NamespaceContext nsc) {
        if (DatatypeConverter.theConverter == null) {
            initConverter();
        }
        return DatatypeConverter.theConverter.printQName(val, nsc);
    }
    
    public static String printDateTime(final Calendar val) {
        if (DatatypeConverter.theConverter == null) {
            initConverter();
        }
        return DatatypeConverter.theConverter.printDateTime(val);
    }
    
    public static String printBase64Binary(final byte[] val) {
        if (DatatypeConverter.theConverter == null) {
            initConverter();
        }
        return DatatypeConverter.theConverter.printBase64Binary(val);
    }
    
    public static String printHexBinary(final byte[] val) {
        if (DatatypeConverter.theConverter == null) {
            initConverter();
        }
        return DatatypeConverter.theConverter.printHexBinary(val);
    }
    
    public static String printUnsignedInt(final long val) {
        if (DatatypeConverter.theConverter == null) {
            initConverter();
        }
        return DatatypeConverter.theConverter.printUnsignedInt(val);
    }
    
    public static String printUnsignedShort(final int val) {
        if (DatatypeConverter.theConverter == null) {
            initConverter();
        }
        return DatatypeConverter.theConverter.printUnsignedShort(val);
    }
    
    public static String printTime(final Calendar val) {
        if (DatatypeConverter.theConverter == null) {
            initConverter();
        }
        return DatatypeConverter.theConverter.printTime(val);
    }
    
    public static String printDate(final Calendar val) {
        if (DatatypeConverter.theConverter == null) {
            initConverter();
        }
        return DatatypeConverter.theConverter.printDate(val);
    }
    
    public static String printAnySimpleType(final String val) {
        if (DatatypeConverter.theConverter == null) {
            initConverter();
        }
        return DatatypeConverter.theConverter.printAnySimpleType(val);
    }
}
