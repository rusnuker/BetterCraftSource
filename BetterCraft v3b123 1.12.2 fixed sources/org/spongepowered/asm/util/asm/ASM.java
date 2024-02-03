// 
// Decompiled by Procyon v0.6.0
// 

package org.spongepowered.asm.util.asm;

import java.util.jar.Attributes;
import org.spongepowered.asm.launch.platform.MainAttributes;
import java.lang.reflect.Field;
import org.spongepowered.asm.util.VersionNumber;
import org.objectweb.asm.Opcodes;

public final class ASM
{
    private static int majorVersion;
    private static int minorVersion;
    private static int implMinorVersion;
    private static int patchVersion;
    private static String maxVersion;
    private static int maxClassVersion;
    private static int maxClassMajorVersion;
    private static int maxClassMinorVersion;
    private static String maxJavaVersion;
    public static final int API_VERSION;
    
    private ASM() {
    }
    
    public static boolean isAtLeastVersion(final int majorVersion) {
        return ASM.majorVersion >= majorVersion;
    }
    
    public static boolean isAtLeastVersion(final int majorVersion, final int minorVersion) {
        return ASM.majorVersion >= majorVersion && (ASM.majorVersion > majorVersion || ASM.implMinorVersion >= minorVersion);
    }
    
    public static boolean isAtLeastVersion(final int majorVersion, final int minorVersion, final int patchVersion) {
        if (ASM.majorVersion == majorVersion) {
            return ASM.implMinorVersion >= minorVersion && (ASM.implMinorVersion > minorVersion || ASM.patchVersion >= patchVersion);
        }
        return ASM.majorVersion > majorVersion;
    }
    
    public static int getApiVersionMajor() {
        return ASM.majorVersion;
    }
    
    public static int getApiVersionMinor() {
        return ASM.minorVersion;
    }
    
    public static String getApiVersionString() {
        return String.format("%d.%d", ASM.majorVersion, ASM.minorVersion);
    }
    
    public static String getVersionString() {
        return String.format("ASM %d.%d%s (%s)", ASM.majorVersion, ASM.implMinorVersion, (ASM.patchVersion > 0) ? ("." + ASM.patchVersion) : "", ASM.maxVersion);
    }
    
    public static int getMaxSupportedClassVersion() {
        return ASM.maxClassVersion;
    }
    
    public static int getMaxSupportedClassVersionMajor() {
        return ASM.maxClassMajorVersion;
    }
    
    public static int getMaxSupportedClassVersionMinor() {
        return ASM.maxClassMinorVersion;
    }
    
    public static String getClassVersionString() {
        return String.format("Up to Java %s (class file version %d.%d)", ASM.maxJavaVersion, ASM.maxClassMajorVersion, ASM.maxClassMinorVersion);
    }
    
    private static int detectVersion() {
        int apiVersion = 262144;
        final VersionNumber packageVersion = getPackageVersion(Opcodes.class);
        for (final Field field : Opcodes.class.getDeclaredFields()) {
            if (field.getType() == Integer.TYPE) {
                try {
                    final String name = field.getName();
                    final int version = field.getInt(null);
                    if (name.startsWith("ASM")) {
                        final int minor = version >> 8 & 0xFF;
                        final int major = version >> 16 & 0xFF;
                        final boolean experimental = (version >> 24 & 0xFF) != 0x0;
                        if (major >= ASM.majorVersion) {
                            ASM.maxVersion = name;
                            if (!experimental) {
                                apiVersion = version;
                                ASM.majorVersion = major;
                                ASM.minorVersion = (ASM.implMinorVersion = minor);
                                if (packageVersion.getMajor() == major && minor == 0) {
                                    ASM.implMinorVersion = packageVersion.getMinor();
                                    ASM.patchVersion = packageVersion.getPatch();
                                }
                            }
                        }
                    }
                    else if (name.matches("V([0-9_]+)")) {
                        final int minor = version >> 16 & 0xFFFF;
                        final int major = version & 0xFFFF;
                        if (major > ASM.maxClassMajorVersion || (major == ASM.maxClassMajorVersion && minor > ASM.maxClassMinorVersion)) {
                            ASM.maxClassMajorVersion = major;
                            ASM.maxClassMinorVersion = minor;
                            ASM.maxClassVersion = version;
                            ASM.maxJavaVersion = name.replace('_', '.').substring(1);
                        }
                    }
                    else if ("ACC_PUBLIC".equals(name)) {
                        break;
                    }
                }
                catch (final ReflectiveOperationException ex) {
                    throw new Error(ex);
                }
            }
        }
        return apiVersion;
    }
    
    private static VersionNumber getPackageVersion(final Class<?> clazz) {
        final String implVersion = clazz.getPackage().getImplementationVersion();
        if (implVersion != null) {
            return VersionNumber.parse(implVersion);
        }
        try {
            final MainAttributes manifest = MainAttributes.of(clazz.getProtectionDomain().getCodeSource().getLocation().toURI());
            return VersionNumber.parse(manifest.get(Attributes.Name.IMPLEMENTATION_VERSION));
        }
        catch (final Exception ex) {
            return VersionNumber.NONE;
        }
    }
    
    static {
        ASM.majorVersion = 5;
        ASM.minorVersion = 0;
        ASM.implMinorVersion = 0;
        ASM.patchVersion = 0;
        ASM.maxVersion = "FALLBACK";
        ASM.maxClassVersion = 50;
        ASM.maxClassMajorVersion = 50;
        ASM.maxClassMinorVersion = 0;
        ASM.maxJavaVersion = "V1.6";
        API_VERSION = detectVersion();
    }
}
