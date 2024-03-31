// 
// Decompiled by Procyon v0.6.0
// 

package shadersmod.client;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ShaderParser
{
    public static Pattern PATTERN_UNIFORM;
    public static Pattern PATTERN_COMMENT;
    public static Pattern PATTERN_CONST_INT;
    public static Pattern PATTERN_CONST_FLOAT;
    public static Pattern PATTERN_CONST_BOOL;
    public static Pattern PATTERN_COMPOSITE_FSH;
    public static Pattern PATTERN_FINAL_FSH;
    public static Pattern PATTERN_DRAW_BUFFERS;
    
    static {
        ShaderParser.PATTERN_UNIFORM = Pattern.compile("\\s*uniform\\s+\\w+\\s+(\\w+).*");
        ShaderParser.PATTERN_COMMENT = Pattern.compile("\\s*/\\*\\s+([A-Z]+):(\\S+)\\s+\\*/.*");
        ShaderParser.PATTERN_CONST_INT = Pattern.compile("\\s*const\\s+int\\s+(\\w+)\\s*=\\s*([-+.\\w]+)\\s*;.*");
        ShaderParser.PATTERN_CONST_FLOAT = Pattern.compile("\\s*const\\s+float\\s+(\\w+)\\s*=\\s*([-+.\\w]+)\\s*;.*");
        ShaderParser.PATTERN_CONST_BOOL = Pattern.compile("\\s*const\\s+bool\\s+(\\w+)\\s*=\\s*(\\w+)\\s*;.*");
        ShaderParser.PATTERN_COMPOSITE_FSH = Pattern.compile(".*composite[0-9]?\\.fsh");
        ShaderParser.PATTERN_FINAL_FSH = Pattern.compile(".*final\\.fsh");
        ShaderParser.PATTERN_DRAW_BUFFERS = Pattern.compile("[0-7N]*");
    }
    
    public static ShaderLine parseLine(final String line) {
        final Matcher matcher = ShaderParser.PATTERN_UNIFORM.matcher(line);
        if (matcher.matches()) {
            return new ShaderLine(1, matcher.group(1), "", line);
        }
        final Matcher matcher2 = ShaderParser.PATTERN_COMMENT.matcher(line);
        if (matcher2.matches()) {
            return new ShaderLine(2, matcher2.group(1), matcher2.group(2), line);
        }
        final Matcher matcher3 = ShaderParser.PATTERN_CONST_INT.matcher(line);
        if (matcher3.matches()) {
            return new ShaderLine(3, matcher3.group(1), matcher3.group(2), line);
        }
        final Matcher matcher4 = ShaderParser.PATTERN_CONST_FLOAT.matcher(line);
        if (matcher4.matches()) {
            return new ShaderLine(4, matcher4.group(1), matcher4.group(2), line);
        }
        final Matcher matcher5 = ShaderParser.PATTERN_CONST_BOOL.matcher(line);
        return matcher5.matches() ? new ShaderLine(5, matcher5.group(1), matcher5.group(2), line) : null;
    }
    
    public static int getIndex(final String uniform, final String prefix, final int minIndex, final int maxIndex) {
        if (uniform.length() != prefix.length() + 1) {
            return -1;
        }
        if (!uniform.startsWith(prefix)) {
            return -1;
        }
        final int i = uniform.charAt(prefix.length()) - '0';
        return (i >= minIndex && i <= maxIndex) ? i : -1;
    }
    
    public static int getShadowDepthIndex(final String uniform) {
        byte b0 = -1;
        switch (uniform.hashCode()) {
            case -903579360: {
                if (uniform.equals("shadow")) {
                    b0 = 0;
                    break;
                }
                break;
            }
            case 1235669239: {
                if (uniform.equals("watershadow")) {
                    b0 = 1;
                    break;
                }
                break;
            }
        }
        switch (b0) {
            case 0: {
                return 0;
            }
            case 1: {
                return 1;
            }
            default: {
                return getIndex(uniform, "shadowtex", 0, 1);
            }
        }
    }
    
    public static int getShadowColorIndex(final String uniform) {
        byte b0 = -1;
        switch (uniform.hashCode()) {
            case -1560188349: {
                if (uniform.equals("shadowcolor")) {
                    b0 = 0;
                    break;
                }
                break;
            }
        }
        switch (b0) {
            case 0: {
                return 0;
            }
            default: {
                return getIndex(uniform, "shadowcolor", 0, 1);
            }
        }
    }
    
    public static int getDepthIndex(final String uniform) {
        return getIndex(uniform, "depthtex", 0, 2);
    }
    
    public static int getColorIndex(final String uniform) {
        final int i = getIndex(uniform, "gaux", 1, 4);
        return (i > 0) ? (i + 3) : getIndex(uniform, "colortex", 4, 7);
    }
    
    public static boolean isComposite(final String filename) {
        return ShaderParser.PATTERN_COMPOSITE_FSH.matcher(filename).matches();
    }
    
    public static boolean isFinal(final String filename) {
        return ShaderParser.PATTERN_FINAL_FSH.matcher(filename).matches();
    }
    
    public static boolean isValidDrawBuffers(final String str) {
        return ShaderParser.PATTERN_DRAW_BUFFERS.matcher(str).matches();
    }
}
