// 
// Decompiled by Procyon v0.6.0
// 

package wdl;

import java.lang.reflect.Field;

public class ReflectionUtils
{
    public static Field stealField(final Class<?> typeOfClass, final Class<?> typeOfField) {
        final Field[] fields = typeOfClass.getDeclaredFields();
        Field[] array;
        for (int length = (array = fields).length, i = 0; i < length; ++i) {
            final Field f = array[i];
            if (f.getType().equals(typeOfField)) {
                try {
                    f.setAccessible(true);
                    return f;
                }
                catch (final Exception e) {
                    throw new RuntimeException("WorldDownloader: Couldn't steal Field of type \"" + typeOfField + "\" from class \"" + typeOfClass + "\" !", e);
                }
            }
        }
        throw new RuntimeException("WorldDownloader: Couldn't steal Field of type \"" + typeOfField + "\" from class \"" + typeOfClass + "\" !");
    }
    
    public static <T> T stealAndGetField(Object object, final Class<T> typeOfField) {
        Class<?> typeOfObject;
        if (object instanceof Class) {
            typeOfObject = (Class)object;
            object = null;
        }
        else {
            typeOfObject = object.getClass();
        }
        try {
            final Field f = stealField(typeOfObject, typeOfField);
            return typeOfField.cast(f.get(object));
        }
        catch (final Exception e) {
            throw new RuntimeException("WorldDownloader: Couldn't get Field of type \"" + typeOfField + "\" from object \"" + object + "\" !", e);
        }
    }
    
    public static void stealAndSetField(Object object, final Class<?> typeOfField, final Object value) {
        Class<?> typeOfObject;
        if (object instanceof Class) {
            typeOfObject = (Class)object;
            object = null;
        }
        else {
            typeOfObject = object.getClass();
        }
        try {
            final Field f = stealField(typeOfObject, typeOfField);
            f.set(object, value);
        }
        catch (final Exception e) {
            throw new RuntimeException("WorldDownloader: Couldn't set Field of type \"" + typeOfField + "\" from object \"" + object + "\" to " + value + "!", e);
        }
    }
    
    public static <T> T stealAndGetField(final Object object, final Class<?> typeOfObject, final Class<T> typeOfField) {
        try {
            final Field f = stealField(typeOfObject, typeOfField);
            return typeOfField.cast(f.get(object));
        }
        catch (final Exception e) {
            throw new RuntimeException("WorldDownloader: Couldn't get Field of type \"" + typeOfField + "\" from object \"" + object + "\" !", e);
        }
    }
    
    public static void stealAndSetField(final Object object, final Class<?> typeOfObject, final Class<?> typeOfField, final Object value) {
        try {
            final Field f = stealField(typeOfObject, typeOfField);
            f.set(object, value);
        }
        catch (final Exception e) {
            throw new RuntimeException("WorldDownloader: Couldn't set Field of type \"" + typeOfField + "\" from object \"" + object + "\" to " + value + "!", e);
        }
    }
}
