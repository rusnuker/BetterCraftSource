// 
// Decompiled by Procyon v0.6.0
// 

package com.TominoCZ.FBP.util;

import java.lang.reflect.Field;

public class FBPReflectionHelper
{
    public static Field findField(final Class<?> clazz, final String... fields) {
        Field[] declaredFields;
        for (int length = (declaredFields = clazz.getDeclaredFields()).length, i = 0; i < length; ++i) {
            final Field fld = declaredFields[i];
            for (final String name : fields) {
                if (name.equals(fld.getName())) {
                    fld.setAccessible(true);
                    return fld;
                }
            }
        }
        return null;
    }
}
