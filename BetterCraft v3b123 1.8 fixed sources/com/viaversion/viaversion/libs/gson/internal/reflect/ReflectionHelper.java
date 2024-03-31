// 
// Decompiled by Procyon v0.6.0
// 

package com.viaversion.viaversion.libs.gson.internal.reflect;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.lang.reflect.Field;
import com.viaversion.viaversion.libs.gson.JsonIOException;
import java.lang.reflect.AccessibleObject;

public class ReflectionHelper
{
    private static final RecordHelper RECORD_HELPER;
    
    private ReflectionHelper() {
    }
    
    public static void makeAccessible(final AccessibleObject object) throws JsonIOException {
        try {
            object.setAccessible(true);
        }
        catch (final Exception exception) {
            final String description = getAccessibleObjectDescription(object, false);
            throw new JsonIOException("Failed making " + description + " accessible; either increase its visibility or write a custom TypeAdapter for its declaring type.", exception);
        }
    }
    
    public static String getAccessibleObjectDescription(final AccessibleObject object, final boolean uppercaseFirstLetter) {
        String description;
        if (object instanceof Field) {
            description = "field '" + fieldToString((Field)object) + "'";
        }
        else if (object instanceof Method) {
            final Method method = (Method)object;
            final StringBuilder methodSignatureBuilder = new StringBuilder(method.getName());
            appendExecutableParameters(method, methodSignatureBuilder);
            final String methodSignature = methodSignatureBuilder.toString();
            description = "method '" + method.getDeclaringClass().getName() + "#" + methodSignature + "'";
        }
        else if (object instanceof Constructor) {
            description = "constructor '" + constructorToString((Constructor<?>)object) + "'";
        }
        else {
            description = "<unknown AccessibleObject> " + object.toString();
        }
        if (uppercaseFirstLetter && Character.isLowerCase(description.charAt(0))) {
            description = Character.toUpperCase(description.charAt(0)) + description.substring(1);
        }
        return description;
    }
    
    public static String fieldToString(final Field field) {
        return field.getDeclaringClass().getName() + "#" + field.getName();
    }
    
    public static String constructorToString(final Constructor<?> constructor) {
        final StringBuilder stringBuilder = new StringBuilder(constructor.getDeclaringClass().getName());
        appendExecutableParameters(constructor, stringBuilder);
        return stringBuilder.toString();
    }
    
    private static void appendExecutableParameters(final AccessibleObject executable, final StringBuilder stringBuilder) {
        stringBuilder.append('(');
        final Class<?>[] parameters = (executable instanceof Method) ? ((Method)executable).getParameterTypes() : ((Constructor)executable).getParameterTypes();
        for (int i = 0; i < parameters.length; ++i) {
            if (i > 0) {
                stringBuilder.append(", ");
            }
            stringBuilder.append(parameters[i].getSimpleName());
        }
        stringBuilder.append(')');
    }
    
    public static String tryMakeAccessible(final Constructor<?> constructor) {
        try {
            constructor.setAccessible(true);
            return null;
        }
        catch (final Exception exception) {
            return "Failed making constructor '" + constructorToString(constructor) + "' accessible; either increase its visibility or write a custom InstanceCreator or TypeAdapter for its declaring type: " + exception.getMessage();
        }
    }
    
    public static boolean isRecord(final Class<?> raw) {
        return ReflectionHelper.RECORD_HELPER.isRecord(raw);
    }
    
    public static String[] getRecordComponentNames(final Class<?> raw) {
        return ReflectionHelper.RECORD_HELPER.getRecordComponentNames(raw);
    }
    
    public static Method getAccessor(final Class<?> raw, final Field field) {
        return ReflectionHelper.RECORD_HELPER.getAccessor(raw, field);
    }
    
    public static <T> Constructor<T> getCanonicalRecordConstructor(final Class<T> raw) {
        return ReflectionHelper.RECORD_HELPER.getCanonicalRecordConstructor(raw);
    }
    
    public static RuntimeException createExceptionForUnexpectedIllegalAccess(final IllegalAccessException exception) {
        throw new RuntimeException("Unexpected IllegalAccessException occurred (Gson 2.10.1). Certain ReflectionAccessFilter features require Java >= 9 to work correctly. If you are not using ReflectionAccessFilter, report this to the Gson maintainers.", exception);
    }
    
    private static RuntimeException createExceptionForRecordReflectionException(final ReflectiveOperationException exception) {
        throw new RuntimeException("Unexpected ReflectiveOperationException occurred (Gson 2.10.1). To support Java records, reflection is utilized to read out information about records. All these invocations happens after it is established that records exist in the JVM. This exception is unexpected behavior.", exception);
    }
    
    static {
        RecordHelper instance;
        try {
            instance = new RecordSupportedHelper();
        }
        catch (final NoSuchMethodException e) {
            instance = new RecordNotSupportedHelper();
        }
        RECORD_HELPER = instance;
    }
    
    private abstract static class RecordHelper
    {
        abstract boolean isRecord(final Class<?> p0);
        
        abstract String[] getRecordComponentNames(final Class<?> p0);
        
        abstract <T> Constructor<T> getCanonicalRecordConstructor(final Class<T> p0);
        
        public abstract Method getAccessor(final Class<?> p0, final Field p1);
    }
    
    private static class RecordSupportedHelper extends RecordHelper
    {
        private final Method isRecord;
        private final Method getRecordComponents;
        private final Method getName;
        private final Method getType;
        
        private RecordSupportedHelper() throws NoSuchMethodException {
            this.isRecord = Class.class.getMethod("isRecord", (Class<?>[])new Class[0]);
            this.getRecordComponents = Class.class.getMethod("getRecordComponents", (Class<?>[])new Class[0]);
            final Class<?> classRecordComponent = this.getRecordComponents.getReturnType().getComponentType();
            this.getName = classRecordComponent.getMethod("getName", (Class<?>[])new Class[0]);
            this.getType = classRecordComponent.getMethod("getType", (Class<?>[])new Class[0]);
        }
        
        @Override
        boolean isRecord(final Class<?> raw) {
            try {
                return (boolean)this.isRecord.invoke(raw, new Object[0]);
            }
            catch (final ReflectiveOperationException e) {
                throw createExceptionForRecordReflectionException(e);
            }
        }
        
        @Override
        String[] getRecordComponentNames(final Class<?> raw) {
            try {
                final Object[] recordComponents = (Object[])this.getRecordComponents.invoke(raw, new Object[0]);
                final String[] componentNames = new String[recordComponents.length];
                for (int i = 0; i < recordComponents.length; ++i) {
                    componentNames[i] = (String)this.getName.invoke(recordComponents[i], new Object[0]);
                }
                return componentNames;
            }
            catch (final ReflectiveOperationException e) {
                throw createExceptionForRecordReflectionException(e);
            }
        }
        
        public <T> Constructor<T> getCanonicalRecordConstructor(final Class<T> raw) {
            try {
                final Object[] recordComponents = (Object[])this.getRecordComponents.invoke(raw, new Object[0]);
                final Class<?>[] recordComponentTypes = new Class[recordComponents.length];
                for (int i = 0; i < recordComponents.length; ++i) {
                    recordComponentTypes[i] = (Class)this.getType.invoke(recordComponents[i], new Object[0]);
                }
                return raw.getDeclaredConstructor(recordComponentTypes);
            }
            catch (final ReflectiveOperationException e) {
                throw createExceptionForRecordReflectionException(e);
            }
        }
        
        @Override
        public Method getAccessor(final Class<?> raw, final Field field) {
            try {
                return raw.getMethod(field.getName(), (Class<?>[])new Class[0]);
            }
            catch (final ReflectiveOperationException e) {
                throw createExceptionForRecordReflectionException(e);
            }
        }
    }
    
    private static class RecordNotSupportedHelper extends RecordHelper
    {
        @Override
        boolean isRecord(final Class<?> clazz) {
            return false;
        }
        
        @Override
        String[] getRecordComponentNames(final Class<?> clazz) {
            throw new UnsupportedOperationException("Records are not supported on this JVM, this method should not be called");
        }
        
        @Override
         <T> Constructor<T> getCanonicalRecordConstructor(final Class<T> raw) {
            throw new UnsupportedOperationException("Records are not supported on this JVM, this method should not be called");
        }
        
        @Override
        public Method getAccessor(final Class<?> raw, final Field field) {
            throw new UnsupportedOperationException("Records are not supported on this JVM, this method should not be called");
        }
    }
}
