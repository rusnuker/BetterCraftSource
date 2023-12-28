// 
// Decompiled by Procyon v0.6.0
// 

package com.ibm.icu.impl;

import java.util.MissingResourceException;
import com.ibm.icu.util.UResourceBundle;
import com.ibm.icu.util.VersionInfo;

public final class ICUDataVersion
{
    private static final String U_ICU_VERSION_BUNDLE = "icuver";
    private static final String U_ICU_DATA_KEY = "DataVersion";
    
    public static VersionInfo getDataVersion() {
        UResourceBundle icudatares = null;
        try {
            icudatares = UResourceBundle.getBundleInstance("com/ibm/icu/impl/data/icudt51b", "icuver", ICUResourceBundle.ICU_DATA_CLASS_LOADER);
            icudatares = icudatares.get("DataVersion");
        }
        catch (final MissingResourceException ex) {
            return null;
        }
        return VersionInfo.getInstance(icudatares.getString());
    }
}
