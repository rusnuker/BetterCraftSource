// 
// Decompiled by Procyon v0.6.0
// 

package javax.activation;

import java.io.File;

public abstract class FileTypeMap
{
    private static FileTypeMap defaultFileTypeMap;
    
    static {
        FileTypeMap.defaultFileTypeMap = null;
    }
    
    public static void setDefaultFileTypeMap(final FileTypeMap fileMap) {
        final SecurityManager sm = System.getSecurityManager();
        if (sm != null) {
            sm.checkSetFactory();
        }
        FileTypeMap.defaultFileTypeMap = fileMap;
    }
    
    public static synchronized FileTypeMap getDefaultFileTypeMap() {
        if (FileTypeMap.defaultFileTypeMap == null) {
            FileTypeMap.defaultFileTypeMap = new MimetypesFileTypeMap();
        }
        return FileTypeMap.defaultFileTypeMap;
    }
    
    public abstract String getContentType(final File p0);
    
    public abstract String getContentType(final String p0);
}
