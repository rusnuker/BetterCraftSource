// 
// Decompiled by Procyon v0.6.0
// 

package org.spongepowered.asm.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class JavaVersion
{
    public static final double JAVA_6 = 1.6;
    public static final double JAVA_7 = 1.7;
    public static final double JAVA_8 = 1.8;
    public static final double JAVA_9 = 9.0;
    public static final double JAVA_10 = 10.0;
    public static final double JAVA_11 = 11.0;
    public static final double JAVA_12 = 12.0;
    public static final double JAVA_13 = 13.0;
    public static final double JAVA_14 = 14.0;
    public static final double JAVA_15 = 15.0;
    public static final double JAVA_16 = 16.0;
    public static final double JAVA_17 = 17.0;
    public static final double JAVA_18 = 18.0;
    private static double current;
    
    private JavaVersion() {
    }
    
    public static double current() {
        if (JavaVersion.current == 0.0) {
            JavaVersion.current = resolveCurrentVersion();
        }
        return JavaVersion.current;
    }
    
    private static double resolveCurrentVersion() {
        final String version = System.getProperty("java.version");
        final Matcher decimalMatcher = Pattern.compile("[0-9]+\\.[0-9]+").matcher(version);
        if (decimalMatcher.find()) {
            return Double.parseDouble(decimalMatcher.group());
        }
        final Matcher numberMatcher = Pattern.compile("[0-9]+").matcher(version);
        if (numberMatcher.find()) {
            return Double.parseDouble(numberMatcher.group());
        }
        return 1.6;
    }
    
    static {
        JavaVersion.current = 0.0;
    }
}
