// 
// Decompiled by Procyon v0.6.0
// 

package org.spongepowered.tools.obfuscation.ext;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public final class SpecialPackages
{
    private static final Set<String> suppressWarningsForPackages;
    
    private SpecialPackages() {
    }
    
    public static final void addExcludedPackage(final String packageName) {
        String internalName = packageName.replace('.', '/');
        if (!internalName.endsWith("/")) {
            internalName += "/";
        }
        SpecialPackages.suppressWarningsForPackages.add(internalName);
    }
    
    public static boolean isExcludedPackage(final String internalName) {
        for (final String prefix : SpecialPackages.suppressWarningsForPackages) {
            if (internalName.startsWith(prefix)) {
                return true;
            }
        }
        return false;
    }
    
    static {
        suppressWarningsForPackages = new HashSet<String>();
        addExcludedPackage("java.");
        addExcludedPackage("javax.");
        addExcludedPackage("sun.");
        addExcludedPackage("com.sun.");
    }
}
