// 
// Decompiled by Procyon v0.6.0
// 

package com.mcf.davidee.nbtedit;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.lang.reflect.Field;
import net.minecraft.tileentity.TileEntity;

public class TileEntityHelper
{
    public static <T extends TileEntity> void copyData(final T from, final T into) throws Exception {
        final Class<?> clazz = from.getClass();
        final Set<Field> fields = asSet(clazz.getFields(), clazz.getDeclaredFields());
        final Field modifiers = Field.class.getDeclaredField("modifiers");
        modifiers.setAccessible(true);
        for (final Field field : fields) {
            field.setAccessible(true);
            modifiers.setInt(field, field.getModifiers() & 0xFFFFFFEF);
            field.set(into, field.get(from));
        }
    }
    
    public static Set<Field> asSet(final Field[] a, final Field[] b) {
        final HashSet<Field> s = new HashSet<Field>();
        Collections.addAll(s, a);
        Collections.addAll(s, b);
        return s;
    }
}
