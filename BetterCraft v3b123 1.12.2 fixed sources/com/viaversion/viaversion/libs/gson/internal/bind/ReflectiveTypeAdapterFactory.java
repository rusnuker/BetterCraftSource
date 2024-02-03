// 
// Decompiled by Procyon v0.6.0
// 

package com.viaversion.viaversion.libs.gson.internal.bind;

import java.util.Arrays;
import java.util.HashMap;
import java.lang.reflect.Constructor;
import com.viaversion.viaversion.libs.gson.JsonSyntaxException;
import com.viaversion.viaversion.libs.gson.stream.JsonToken;
import java.util.Iterator;
import com.viaversion.viaversion.libs.gson.internal.$Gson$Types;
import java.util.LinkedHashMap;
import java.util.Map;
import com.viaversion.viaversion.libs.gson.JsonParseException;
import com.viaversion.viaversion.libs.gson.stream.JsonReader;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import com.viaversion.viaversion.libs.gson.stream.JsonWriter;
import com.viaversion.viaversion.libs.gson.annotations.JsonAdapter;
import java.lang.reflect.Type;
import com.viaversion.viaversion.libs.gson.internal.Primitives;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Member;
import java.lang.reflect.AccessibleObject;
import com.viaversion.viaversion.libs.gson.internal.ObjectConstructor;
import com.viaversion.viaversion.libs.gson.internal.reflect.ReflectionHelper;
import com.viaversion.viaversion.libs.gson.JsonIOException;
import com.viaversion.viaversion.libs.gson.internal.ReflectionAccessFilterHelper;
import com.viaversion.viaversion.libs.gson.TypeAdapter;
import com.viaversion.viaversion.libs.gson.reflect.TypeToken;
import com.viaversion.viaversion.libs.gson.Gson;
import java.util.Collection;
import java.util.ArrayList;
import java.util.Collections;
import com.viaversion.viaversion.libs.gson.annotations.SerializedName;
import java.lang.reflect.Field;
import com.viaversion.viaversion.libs.gson.ReflectionAccessFilter;
import java.util.List;
import com.viaversion.viaversion.libs.gson.internal.Excluder;
import com.viaversion.viaversion.libs.gson.FieldNamingStrategy;
import com.viaversion.viaversion.libs.gson.internal.ConstructorConstructor;
import com.viaversion.viaversion.libs.gson.TypeAdapterFactory;

public final class ReflectiveTypeAdapterFactory implements TypeAdapterFactory
{
    private final ConstructorConstructor constructorConstructor;
    private final FieldNamingStrategy fieldNamingPolicy;
    private final Excluder excluder;
    private final JsonAdapterAnnotationTypeAdapterFactory jsonAdapterFactory;
    private final List<ReflectionAccessFilter> reflectionFilters;
    
    public ReflectiveTypeAdapterFactory(final ConstructorConstructor constructorConstructor, final FieldNamingStrategy fieldNamingPolicy, final Excluder excluder, final JsonAdapterAnnotationTypeAdapterFactory jsonAdapterFactory, final List<ReflectionAccessFilter> reflectionFilters) {
        this.constructorConstructor = constructorConstructor;
        this.fieldNamingPolicy = fieldNamingPolicy;
        this.excluder = excluder;
        this.jsonAdapterFactory = jsonAdapterFactory;
        this.reflectionFilters = reflectionFilters;
    }
    
    private boolean includeField(final Field f, final boolean serialize) {
        return !this.excluder.excludeClass(f.getType(), serialize) && !this.excluder.excludeField(f, serialize);
    }
    
    private List<String> getFieldNames(final Field f) {
        final SerializedName annotation = f.getAnnotation(SerializedName.class);
        if (annotation == null) {
            final String name = this.fieldNamingPolicy.translateName(f);
            return Collections.singletonList(name);
        }
        final String serializedName = annotation.value();
        final String[] alternates = annotation.alternate();
        if (alternates.length == 0) {
            return Collections.singletonList(serializedName);
        }
        final List<String> fieldNames = new ArrayList<String>(alternates.length + 1);
        fieldNames.add(serializedName);
        Collections.addAll(fieldNames, alternates);
        return fieldNames;
    }
    
    @Override
    public <T> TypeAdapter<T> create(final Gson gson, final TypeToken<T> type) {
        final Class<? super T> raw = type.getRawType();
        if (!Object.class.isAssignableFrom(raw)) {
            return null;
        }
        final ReflectionAccessFilter.FilterResult filterResult = ReflectionAccessFilterHelper.getFilterResult(this.reflectionFilters, raw);
        if (filterResult == ReflectionAccessFilter.FilterResult.BLOCK_ALL) {
            throw new JsonIOException("ReflectionAccessFilter does not permit using reflection for " + raw + ". Register a TypeAdapter for this type or adjust the access filter.");
        }
        final boolean blockInaccessible = filterResult == ReflectionAccessFilter.FilterResult.BLOCK_INACCESSIBLE;
        if (ReflectionHelper.isRecord(raw)) {
            final TypeAdapter<T> adapter = (TypeAdapter<T>)new RecordAdapter((Class<Object>)raw, this.getBoundFields(gson, type, raw, blockInaccessible, true), blockInaccessible);
            return adapter;
        }
        final ObjectConstructor<T> constructor = this.constructorConstructor.get(type);
        return (TypeAdapter<T>)new FieldReflectionAdapter((ObjectConstructor<Object>)constructor, this.getBoundFields(gson, type, raw, blockInaccessible, false));
    }
    
    private static <M extends AccessibleObject & Member> void checkAccessible(final Object object, final M member) {
        if (!ReflectionAccessFilterHelper.canAccess(member, Modifier.isStatic(member.getModifiers()) ? null : object)) {
            final String memberDescription = ReflectionHelper.getAccessibleObjectDescription(member, true);
            throw new JsonIOException(memberDescription + " is not accessible and ReflectionAccessFilter does not permit making it accessible. Register a TypeAdapter for the declaring type, adjust the access filter or increase the visibility of the element and its declaring type.");
        }
    }
    
    private BoundField createBoundField(final Gson context, final Field field, final Method accessor, final String name, final TypeToken<?> fieldType, final boolean serialize, final boolean deserialize, final boolean blockInaccessible) {
        final boolean isPrimitive = Primitives.isPrimitive(fieldType.getRawType());
        final int modifiers = field.getModifiers();
        final boolean isStaticFinalField = Modifier.isStatic(modifiers) && Modifier.isFinal(modifiers);
        final JsonAdapter annotation = field.getAnnotation(JsonAdapter.class);
        TypeAdapter<?> mapped = null;
        if (annotation != null) {
            mapped = this.jsonAdapterFactory.getTypeAdapter(this.constructorConstructor, context, fieldType, annotation);
        }
        final boolean jsonAdapterPresent = mapped != null;
        if (mapped == null) {
            mapped = context.getAdapter(fieldType);
        }
        final TypeAdapter<Object> typeAdapter = (TypeAdapter<Object>)mapped;
        return new BoundField(name, field, serialize, deserialize) {
            @Override
            void write(final JsonWriter writer, final Object source) throws IOException, IllegalAccessException {
                if (!this.serialized) {
                    return;
                }
                if (blockInaccessible) {
                    if (accessor == null) {
                        checkAccessible(source, this.field);
                    }
                    else {
                        checkAccessible(source, accessor);
                    }
                }
                Object fieldValue = null;
                Label_0123: {
                    if (accessor != null) {
                        try {
                            fieldValue = accessor.invoke(source, new Object[0]);
                            break Label_0123;
                        }
                        catch (final InvocationTargetException e) {
                            final String accessorDescription = ReflectionHelper.getAccessibleObjectDescription(accessor, false);
                            throw new JsonIOException("Accessor " + accessorDescription + " threw exception", e.getCause());
                        }
                    }
                    fieldValue = this.field.get(source);
                }
                if (fieldValue == source) {
                    return;
                }
                writer.name(this.name);
                final TypeAdapter<Object> t = jsonAdapterPresent ? typeAdapter : new TypeAdapterRuntimeTypeWrapper<Object>(context, typeAdapter, fieldType.getType());
                t.write(writer, fieldValue);
            }
            
            @Override
            void readIntoArray(final JsonReader reader, final int index, final Object[] target) throws IOException, JsonParseException {
                final Object fieldValue = typeAdapter.read(reader);
                if (fieldValue == null && isPrimitive) {
                    throw new JsonParseException("null is not allowed as value for record component '" + this.fieldName + "' of primitive type; at path " + reader.getPath());
                }
                target[index] = fieldValue;
            }
            
            @Override
            void readIntoField(final JsonReader reader, final Object target) throws IOException, IllegalAccessException {
                final Object fieldValue = typeAdapter.read(reader);
                if (fieldValue != null || !isPrimitive) {
                    if (blockInaccessible) {
                        checkAccessible(target, this.field);
                    }
                    else if (isStaticFinalField) {
                        final String fieldDescription = ReflectionHelper.getAccessibleObjectDescription(this.field, false);
                        throw new JsonIOException("Cannot set value of 'static final' " + fieldDescription);
                    }
                    this.field.set(target, fieldValue);
                }
            }
        };
    }
    
    private Map<String, BoundField> getBoundFields(final Gson context, TypeToken<?> type, Class<?> raw, boolean blockInaccessible, final boolean isRecord) {
        final Map<String, BoundField> result = new LinkedHashMap<String, BoundField>();
        if (raw.isInterface()) {
            return result;
        }
        final Class<?> originalRaw = raw;
        while (raw != Object.class) {
            final Field[] fields = raw.getDeclaredFields();
            if (raw != originalRaw && fields.length > 0) {
                final ReflectionAccessFilter.FilterResult filterResult = ReflectionAccessFilterHelper.getFilterResult(this.reflectionFilters, raw);
                if (filterResult == ReflectionAccessFilter.FilterResult.BLOCK_ALL) {
                    throw new JsonIOException("ReflectionAccessFilter does not permit using reflection for " + raw + " (supertype of " + originalRaw + "). Register a TypeAdapter for this type or adjust the access filter.");
                }
                blockInaccessible = (filterResult == ReflectionAccessFilter.FilterResult.BLOCK_INACCESSIBLE);
            }
            for (final Field field : fields) {
                boolean serialize = this.includeField(field, true);
                boolean deserialize = this.includeField(field, false);
                if (serialize || deserialize) {
                    Method accessor = null;
                    if (isRecord) {
                        if (Modifier.isStatic(field.getModifiers())) {
                            deserialize = false;
                        }
                        else {
                            accessor = ReflectionHelper.getAccessor(raw, field);
                            if (!blockInaccessible) {
                                ReflectionHelper.makeAccessible(accessor);
                            }
                            if (accessor.getAnnotation(SerializedName.class) != null && field.getAnnotation(SerializedName.class) == null) {
                                final String methodDescription = ReflectionHelper.getAccessibleObjectDescription(accessor, false);
                                throw new JsonIOException("@SerializedName on " + methodDescription + " is not supported");
                            }
                        }
                    }
                    if (!blockInaccessible && accessor == null) {
                        ReflectionHelper.makeAccessible(field);
                    }
                    final Type fieldType = $Gson$Types.resolve(type.getType(), raw, field.getGenericType());
                    final List<String> fieldNames = this.getFieldNames(field);
                    BoundField previous = null;
                    for (int i = 0, size = fieldNames.size(); i < size; ++i) {
                        final String name = fieldNames.get(i);
                        if (i != 0) {
                            serialize = false;
                        }
                        final BoundField boundField = this.createBoundField(context, field, accessor, name, TypeToken.get(fieldType), serialize, deserialize, blockInaccessible);
                        final BoundField replaced = result.put(name, boundField);
                        if (previous == null) {
                            previous = replaced;
                        }
                    }
                    if (previous != null) {
                        throw new IllegalArgumentException("Class " + originalRaw.getName() + " declares multiple JSON fields named '" + previous.name + "'; conflict is caused by fields " + ReflectionHelper.fieldToString(previous.field) + " and " + ReflectionHelper.fieldToString(field));
                    }
                }
            }
            type = TypeToken.get($Gson$Types.resolve(type.getType(), raw, raw.getGenericSuperclass()));
            raw = type.getRawType();
        }
        return result;
    }
    
    abstract static class BoundField
    {
        final String name;
        final Field field;
        final String fieldName;
        final boolean serialized;
        final boolean deserialized;
        
        protected BoundField(final String name, final Field field, final boolean serialized, final boolean deserialized) {
            this.name = name;
            this.field = field;
            this.fieldName = field.getName();
            this.serialized = serialized;
            this.deserialized = deserialized;
        }
        
        abstract void write(final JsonWriter p0, final Object p1) throws IOException, IllegalAccessException;
        
        abstract void readIntoArray(final JsonReader p0, final int p1, final Object[] p2) throws IOException, JsonParseException;
        
        abstract void readIntoField(final JsonReader p0, final Object p1) throws IOException, IllegalAccessException;
    }
    
    public abstract static class Adapter<T, A> extends TypeAdapter<T>
    {
        final Map<String, BoundField> boundFields;
        
        Adapter(final Map<String, BoundField> boundFields) {
            this.boundFields = boundFields;
        }
        
        @Override
        public void write(final JsonWriter out, final T value) throws IOException {
            if (value == null) {
                out.nullValue();
                return;
            }
            out.beginObject();
            try {
                for (final BoundField boundField : this.boundFields.values()) {
                    boundField.write(out, value);
                }
            }
            catch (final IllegalAccessException e) {
                throw ReflectionHelper.createExceptionForUnexpectedIllegalAccess(e);
            }
            out.endObject();
        }
        
        @Override
        public T read(final JsonReader in) throws IOException {
            if (in.peek() == JsonToken.NULL) {
                in.nextNull();
                return null;
            }
            final A accumulator = this.createAccumulator();
            try {
                in.beginObject();
                while (in.hasNext()) {
                    final String name = in.nextName();
                    final BoundField field = this.boundFields.get(name);
                    if (field == null || !field.deserialized) {
                        in.skipValue();
                    }
                    else {
                        this.readField(accumulator, in, field);
                    }
                }
            }
            catch (final IllegalStateException e) {
                throw new JsonSyntaxException(e);
            }
            catch (final IllegalAccessException e2) {
                throw ReflectionHelper.createExceptionForUnexpectedIllegalAccess(e2);
            }
            in.endObject();
            return this.finalize(accumulator);
        }
        
        abstract A createAccumulator();
        
        abstract void readField(final A p0, final JsonReader p1, final BoundField p2) throws IllegalAccessException, IOException;
        
        abstract T finalize(final A p0);
    }
    
    private static final class FieldReflectionAdapter<T> extends Adapter<T, T>
    {
        private final ObjectConstructor<T> constructor;
        
        FieldReflectionAdapter(final ObjectConstructor<T> constructor, final Map<String, BoundField> boundFields) {
            super(boundFields);
            this.constructor = constructor;
        }
        
        @Override
        T createAccumulator() {
            return this.constructor.construct();
        }
        
        @Override
        void readField(final T accumulator, final JsonReader in, final BoundField field) throws IllegalAccessException, IOException {
            field.readIntoField(in, accumulator);
        }
        
        @Override
        T finalize(final T accumulator) {
            return accumulator;
        }
    }
    
    private static final class RecordAdapter<T> extends Adapter<T, Object[]>
    {
        static final Map<Class<?>, Object> PRIMITIVE_DEFAULTS;
        private final Constructor<T> constructor;
        private final Object[] constructorArgsDefaults;
        private final Map<String, Integer> componentIndices;
        
        RecordAdapter(final Class<T> raw, final Map<String, BoundField> boundFields, final boolean blockInaccessible) {
            super(boundFields);
            this.componentIndices = new HashMap<String, Integer>();
            this.constructor = ReflectionHelper.getCanonicalRecordConstructor(raw);
            if (blockInaccessible) {
                checkAccessible(null, this.constructor);
            }
            else {
                ReflectionHelper.makeAccessible(this.constructor);
            }
            final String[] componentNames = ReflectionHelper.getRecordComponentNames(raw);
            for (int i = 0; i < componentNames.length; ++i) {
                this.componentIndices.put(componentNames[i], i);
            }
            final Class<?>[] parameterTypes = this.constructor.getParameterTypes();
            this.constructorArgsDefaults = new Object[parameterTypes.length];
            for (int j = 0; j < parameterTypes.length; ++j) {
                this.constructorArgsDefaults[j] = RecordAdapter.PRIMITIVE_DEFAULTS.get(parameterTypes[j]);
            }
        }
        
        private static Map<Class<?>, Object> primitiveDefaults() {
            final Map<Class<?>, Object> zeroes = new HashMap<Class<?>, Object>();
            zeroes.put(Byte.TYPE, 0);
            zeroes.put(Short.TYPE, 0);
            zeroes.put(Integer.TYPE, 0);
            zeroes.put(Long.TYPE, 0L);
            zeroes.put(Float.TYPE, 0.0f);
            zeroes.put(Double.TYPE, 0.0);
            zeroes.put(Character.TYPE, '\0');
            zeroes.put(Boolean.TYPE, false);
            return zeroes;
        }
        
        @Override
        Object[] createAccumulator() {
            return this.constructorArgsDefaults.clone();
        }
        
        @Override
        void readField(final Object[] accumulator, final JsonReader in, final BoundField field) throws IOException {
            final Integer componentIndex = this.componentIndices.get(field.fieldName);
            if (componentIndex == null) {
                throw new IllegalStateException("Could not find the index in the constructor '" + ReflectionHelper.constructorToString(this.constructor) + "' for field with name '" + field.fieldName + "', unable to determine which argument in the constructor the field corresponds to. This is unexpected behavior, as we expect the RecordComponents to have the same names as the fields in the Java class, and that the order of the RecordComponents is the same as the order of the canonical constructor parameters.");
            }
            field.readIntoArray(in, componentIndex, accumulator);
        }
        
        @Override
        T finalize(final Object[] accumulator) {
            try {
                return this.constructor.newInstance(accumulator);
            }
            catch (final IllegalAccessException e) {
                throw ReflectionHelper.createExceptionForUnexpectedIllegalAccess(e);
            }
            catch (final InstantiationException | IllegalArgumentException e2) {
                throw new RuntimeException("Failed to invoke constructor '" + ReflectionHelper.constructorToString(this.constructor) + "' with args " + Arrays.toString(accumulator), e2);
            }
            catch (final InvocationTargetException e3) {
                throw new RuntimeException("Failed to invoke constructor '" + ReflectionHelper.constructorToString(this.constructor) + "' with args " + Arrays.toString(accumulator), e3.getCause());
            }
        }
        
        static {
            PRIMITIVE_DEFAULTS = primitiveDefaults();
        }
    }
}
