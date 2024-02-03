// 
// Decompiled by Procyon v0.6.0
// 

package org.spongepowered.asm.util;

import org.spongepowered.asm.mixin.Mixin;
import java.io.File;

public abstract class Constants
{
    public static final String CTOR = "<init>";
    public static final String CLINIT = "<clinit>";
    public static final String IMAGINARY_SUPER = "super$";
    public static final String DEBUG_OUTPUT_PATH = ".mixin.out";
    public static final String MIXIN_PACKAGE;
    public static final String MIXIN_PACKAGE_REF;
    public static final String STRING = "java/lang/String";
    public static final String OBJECT = "java/lang/Object";
    public static final String CLASS = "java/lang/Class";
    public static final String STRING_DESC = "Ljava/lang/String;";
    public static final String OBJECT_DESC = "Ljava/lang/Object;";
    public static final String CLASS_DESC = "Ljava/lang/Class;";
    public static final String SYNTHETIC_PACKAGE = "org.spongepowered.asm.synthetic";
    public static final char UNICODE_SNOWMAN = '\u2603';
    public static final File DEBUG_OUTPUT_DIR;
    public static final String SIDE_DEDICATEDSERVER = "DEDICATEDSERVER";
    public static final String SIDE_SERVER = "SERVER";
    public static final String SIDE_CLIENT = "CLIENT";
    public static final String SIDE_UNKNOWN = "UNKNOWN";
    
    private Constants() {
    }
    
    static {
        MIXIN_PACKAGE = Mixin.class.getPackage().getName();
        MIXIN_PACKAGE_REF = Constants.MIXIN_PACKAGE.replace('.', '/');
        DEBUG_OUTPUT_DIR = new File(".mixin.out");
    }
    
    public abstract static class ManifestAttributes
    {
        public static final String TWEAKER = "TweakClass";
        public static final String MAINCLASS = "Main-Class";
        public static final String MIXINCONFIGS = "MixinConfigs";
        public static final String TOKENPROVIDERS = "MixinTokenProviders";
        public static final String MIXINCONNECTOR = "MixinConnector";
        @Deprecated
        public static final String COMPATIBILITY = "MixinCompatibilityLevel";
        
        private ManifestAttributes() {
        }
    }
}
