// 
// Decompiled by Procyon v0.6.0
// 

package com.ibm.icu.impl.data;

import java.util.ListResourceBundle;

public class HolidayBundle extends ListResourceBundle
{
    private static final Object[][] fContents;
    
    public synchronized Object[][] getContents() {
        return HolidayBundle.fContents;
    }
    
    static {
        fContents = new Object[][] { { "", "" } };
    }
}
