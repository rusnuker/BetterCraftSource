// 
// Decompiled by Procyon v0.6.0
// 

package com.viaversion.viaversion.libs.gson;

import com.viaversion.viaversion.libs.gson.internal.bind.SerializationDelegatingTypeAdapter;
import com.viaversion.viaversion.libs.gson.internal.bind.JsonTreeReader;
import java.io.EOFException;
import com.viaversion.viaversion.libs.gson.stream.MalformedJsonException;
import java.io.StringReader;
import com.viaversion.viaversion.libs.gson.internal.Primitives;
import java.io.Reader;
import java.io.Writer;
import com.viaversion.viaversion.libs.gson.internal.Streams;
import java.io.StringWriter;
import com.viaversion.viaversion.libs.gson.internal.bind.JsonTreeWriter;
import java.util.Iterator;
import java.util.HashMap;
import java.util.Objects;
import com.viaversion.viaversion.libs.gson.stream.JsonWriter;
import java.io.IOException;
import com.viaversion.viaversion.libs.gson.stream.JsonToken;
import com.viaversion.viaversion.libs.gson.stream.JsonReader;
import com.viaversion.viaversion.libs.gson.internal.bind.ReflectiveTypeAdapterFactory;
import com.viaversion.viaversion.libs.gson.internal.bind.MapTypeAdapterFactory;
import com.viaversion.viaversion.libs.gson.internal.bind.CollectionTypeAdapterFactory;
import com.viaversion.viaversion.libs.gson.internal.bind.ArrayTypeAdapter;
import com.viaversion.viaversion.libs.gson.internal.sql.SqlTypesSupport;
import com.viaversion.viaversion.libs.gson.internal.bind.DateTypeAdapter;
import com.viaversion.viaversion.libs.gson.internal.LazilyParsedNumber;
import java.math.BigInteger;
import java.math.BigDecimal;
import java.util.concurrent.atomic.AtomicLongArray;
import java.util.concurrent.atomic.AtomicLong;
import com.viaversion.viaversion.libs.gson.internal.bind.NumberTypeAdapter;
import java.util.Collection;
import com.viaversion.viaversion.libs.gson.internal.bind.ObjectTypeAdapter;
import com.viaversion.viaversion.libs.gson.internal.bind.TypeAdapters;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;
import java.util.Collections;
import java.lang.reflect.Type;
import com.viaversion.viaversion.libs.gson.internal.Excluder;
import java.util.List;
import com.viaversion.viaversion.libs.gson.internal.bind.JsonAdapterAnnotationTypeAdapterFactory;
import com.viaversion.viaversion.libs.gson.internal.ConstructorConstructor;
import java.util.concurrent.ConcurrentMap;
import com.viaversion.viaversion.libs.gson.reflect.TypeToken;
import java.util.Map;

public final class Gson
{
    static final boolean DEFAULT_JSON_NON_EXECUTABLE = false;
    static final boolean DEFAULT_LENIENT = false;
    static final boolean DEFAULT_PRETTY_PRINT = false;
    static final boolean DEFAULT_ESCAPE_HTML = true;
    static final boolean DEFAULT_SERIALIZE_NULLS = false;
    static final boolean DEFAULT_COMPLEX_MAP_KEYS = false;
    static final boolean DEFAULT_SPECIALIZE_FLOAT_VALUES = false;
    static final boolean DEFAULT_USE_JDK_UNSAFE = true;
    static final String DEFAULT_DATE_PATTERN;
    static final FieldNamingStrategy DEFAULT_FIELD_NAMING_STRATEGY;
    static final ToNumberStrategy DEFAULT_OBJECT_TO_NUMBER_STRATEGY;
    static final ToNumberStrategy DEFAULT_NUMBER_TO_NUMBER_STRATEGY;
    private static final String JSON_NON_EXECUTABLE_PREFIX = ")]}'\n";
    private final ThreadLocal<Map<TypeToken<?>, TypeAdapter<?>>> threadLocalAdapterResults;
    private final ConcurrentMap<TypeToken<?>, TypeAdapter<?>> typeTokenCache;
    private final ConstructorConstructor constructorConstructor;
    private final JsonAdapterAnnotationTypeAdapterFactory jsonAdapterFactory;
    final List<TypeAdapterFactory> factories;
    final Excluder excluder;
    final FieldNamingStrategy fieldNamingStrategy;
    final Map<Type, InstanceCreator<?>> instanceCreators;
    final boolean serializeNulls;
    final boolean complexMapKeySerialization;
    final boolean generateNonExecutableJson;
    final boolean htmlSafe;
    final boolean prettyPrinting;
    final boolean lenient;
    final boolean serializeSpecialFloatingPointValues;
    final boolean useJdkUnsafe;
    final String datePattern;
    final int dateStyle;
    final int timeStyle;
    final LongSerializationPolicy longSerializationPolicy;
    final List<TypeAdapterFactory> builderFactories;
    final List<TypeAdapterFactory> builderHierarchyFactories;
    final ToNumberStrategy objectToNumberStrategy;
    final ToNumberStrategy numberToNumberStrategy;
    final List<ReflectionAccessFilter> reflectionFilters;
    
    public Gson() {
        this(Excluder.DEFAULT, Gson.DEFAULT_FIELD_NAMING_STRATEGY, Collections.emptyMap(), false, false, false, true, false, false, false, true, LongSerializationPolicy.DEFAULT, Gson.DEFAULT_DATE_PATTERN, 2, 2, Collections.emptyList(), Collections.emptyList(), Collections.emptyList(), Gson.DEFAULT_OBJECT_TO_NUMBER_STRATEGY, Gson.DEFAULT_NUMBER_TO_NUMBER_STRATEGY, Collections.emptyList());
    }
    
    Gson(final Excluder excluder, final FieldNamingStrategy fieldNamingStrategy, final Map<Type, InstanceCreator<?>> instanceCreators, final boolean serializeNulls, final boolean complexMapKeySerialization, final boolean generateNonExecutableGson, final boolean htmlSafe, final boolean prettyPrinting, final boolean lenient, final boolean serializeSpecialFloatingPointValues, final boolean useJdkUnsafe, final LongSerializationPolicy longSerializationPolicy, final String datePattern, final int dateStyle, final int timeStyle, final List<TypeAdapterFactory> builderFactories, final List<TypeAdapterFactory> builderHierarchyFactories, final List<TypeAdapterFactory> factoriesToBeAdded, final ToNumberStrategy objectToNumberStrategy, final ToNumberStrategy numberToNumberStrategy, final List<ReflectionAccessFilter> reflectionFilters) {
        this.threadLocalAdapterResults = new ThreadLocal<Map<TypeToken<?>, TypeAdapter<?>>>();
        this.typeTokenCache = new ConcurrentHashMap<TypeToken<?>, TypeAdapter<?>>();
        this.excluder = excluder;
        this.fieldNamingStrategy = fieldNamingStrategy;
        this.instanceCreators = instanceCreators;
        this.constructorConstructor = new ConstructorConstructor(instanceCreators, useJdkUnsafe, reflectionFilters);
        this.serializeNulls = serializeNulls;
        this.complexMapKeySerialization = complexMapKeySerialization;
        this.generateNonExecutableJson = generateNonExecutableGson;
        this.htmlSafe = htmlSafe;
        this.prettyPrinting = prettyPrinting;
        this.lenient = lenient;
        this.serializeSpecialFloatingPointValues = serializeSpecialFloatingPointValues;
        this.useJdkUnsafe = useJdkUnsafe;
        this.longSerializationPolicy = longSerializationPolicy;
        this.datePattern = datePattern;
        this.dateStyle = dateStyle;
        this.timeStyle = timeStyle;
        this.builderFactories = builderFactories;
        this.builderHierarchyFactories = builderHierarchyFactories;
        this.objectToNumberStrategy = objectToNumberStrategy;
        this.numberToNumberStrategy = numberToNumberStrategy;
        this.reflectionFilters = reflectionFilters;
        final List<TypeAdapterFactory> factories = new ArrayList<TypeAdapterFactory>();
        factories.add(TypeAdapters.JSON_ELEMENT_FACTORY);
        factories.add(ObjectTypeAdapter.getFactory(objectToNumberStrategy));
        factories.add(excluder);
        factories.addAll(factoriesToBeAdded);
        factories.add(TypeAdapters.STRING_FACTORY);
        factories.add(TypeAdapters.INTEGER_FACTORY);
        factories.add(TypeAdapters.BOOLEAN_FACTORY);
        factories.add(TypeAdapters.BYTE_FACTORY);
        factories.add(TypeAdapters.SHORT_FACTORY);
        final TypeAdapter<Number> longAdapter = longAdapter(longSerializationPolicy);
        factories.add(TypeAdapters.newFactory(Long.TYPE, Long.class, longAdapter));
        factories.add(TypeAdapters.newFactory(Double.TYPE, Double.class, this.doubleAdapter(serializeSpecialFloatingPointValues)));
        factories.add(TypeAdapters.newFactory(Float.TYPE, Float.class, this.floatAdapter(serializeSpecialFloatingPointValues)));
        factories.add(NumberTypeAdapter.getFactory(numberToNumberStrategy));
        factories.add(TypeAdapters.ATOMIC_INTEGER_FACTORY);
        factories.add(TypeAdapters.ATOMIC_BOOLEAN_FACTORY);
        factories.add(TypeAdapters.newFactory(AtomicLong.class, atomicLongAdapter(longAdapter)));
        factories.add(TypeAdapters.newFactory(AtomicLongArray.class, atomicLongArrayAdapter(longAdapter)));
        factories.add(TypeAdapters.ATOMIC_INTEGER_ARRAY_FACTORY);
        factories.add(TypeAdapters.CHARACTER_FACTORY);
        factories.add(TypeAdapters.STRING_BUILDER_FACTORY);
        factories.add(TypeAdapters.STRING_BUFFER_FACTORY);
        factories.add(TypeAdapters.newFactory(BigDecimal.class, TypeAdapters.BIG_DECIMAL));
        factories.add(TypeAdapters.newFactory(BigInteger.class, TypeAdapters.BIG_INTEGER));
        factories.add(TypeAdapters.newFactory(LazilyParsedNumber.class, TypeAdapters.LAZILY_PARSED_NUMBER));
        factories.add(TypeAdapters.URL_FACTORY);
        factories.add(TypeAdapters.URI_FACTORY);
        factories.add(TypeAdapters.UUID_FACTORY);
        factories.add(TypeAdapters.CURRENCY_FACTORY);
        factories.add(TypeAdapters.LOCALE_FACTORY);
        factories.add(TypeAdapters.INET_ADDRESS_FACTORY);
        factories.add(TypeAdapters.BIT_SET_FACTORY);
        factories.add(DateTypeAdapter.FACTORY);
        factories.add(TypeAdapters.CALENDAR_FACTORY);
        if (SqlTypesSupport.SUPPORTS_SQL_TYPES) {
            factories.add(SqlTypesSupport.TIME_FACTORY);
            factories.add(SqlTypesSupport.DATE_FACTORY);
            factories.add(SqlTypesSupport.TIMESTAMP_FACTORY);
        }
        factories.add(ArrayTypeAdapter.FACTORY);
        factories.add(TypeAdapters.CLASS_FACTORY);
        factories.add(new CollectionTypeAdapterFactory(this.constructorConstructor));
        factories.add(new MapTypeAdapterFactory(this.constructorConstructor, complexMapKeySerialization));
        factories.add(this.jsonAdapterFactory = new JsonAdapterAnnotationTypeAdapterFactory(this.constructorConstructor));
        factories.add(TypeAdapters.ENUM_FACTORY);
        factories.add(new ReflectiveTypeAdapterFactory(this.constructorConstructor, fieldNamingStrategy, excluder, this.jsonAdapterFactory, reflectionFilters));
        this.factories = Collections.unmodifiableList((List<? extends TypeAdapterFactory>)factories);
    }
    
    public GsonBuilder newBuilder() {
        return new GsonBuilder(this);
    }
    
    @Deprecated
    public Excluder excluder() {
        return this.excluder;
    }
    
    public FieldNamingStrategy fieldNamingStrategy() {
        return this.fieldNamingStrategy;
    }
    
    public boolean serializeNulls() {
        return this.serializeNulls;
    }
    
    public boolean htmlSafe() {
        return this.htmlSafe;
    }
    
    private TypeAdapter<Number> doubleAdapter(final boolean serializeSpecialFloatingPointValues) {
        if (serializeSpecialFloatingPointValues) {
            return TypeAdapters.DOUBLE;
        }
        return new TypeAdapter<Number>() {
            @Override
            public Double read(final JsonReader in) throws IOException {
                if (in.peek() == JsonToken.NULL) {
                    in.nextNull();
                    return null;
                }
                return in.nextDouble();
            }
            
            @Override
            public void write(final JsonWriter out, final Number value) throws IOException {
                if (value == null) {
                    out.nullValue();
                    return;
                }
                final double doubleValue = value.doubleValue();
                Gson.checkValidFloatingPoint(doubleValue);
                out.value(doubleValue);
            }
        };
    }
    
    private TypeAdapter<Number> floatAdapter(final boolean serializeSpecialFloatingPointValues) {
        if (serializeSpecialFloatingPointValues) {
            return TypeAdapters.FLOAT;
        }
        return new TypeAdapter<Number>() {
            @Override
            public Float read(final JsonReader in) throws IOException {
                if (in.peek() == JsonToken.NULL) {
                    in.nextNull();
                    return null;
                }
                return (float)in.nextDouble();
            }
            
            @Override
            public void write(final JsonWriter out, final Number value) throws IOException {
                if (value == null) {
                    out.nullValue();
                    return;
                }
                final float floatValue = value.floatValue();
                Gson.checkValidFloatingPoint(floatValue);
                final Number floatNumber = (value instanceof Float) ? value : Float.valueOf(floatValue);
                out.value(floatNumber);
            }
        };
    }
    
    static void checkValidFloatingPoint(final double value) {
        if (Double.isNaN(value) || Double.isInfinite(value)) {
            throw new IllegalArgumentException(value + " is not a valid double value as per JSON specification. To override this behavior, use GsonBuilder.serializeSpecialFloatingPointValues() method.");
        }
    }
    
    private static TypeAdapter<Number> longAdapter(final LongSerializationPolicy longSerializationPolicy) {
        if (longSerializationPolicy == LongSerializationPolicy.DEFAULT) {
            return TypeAdapters.LONG;
        }
        return new TypeAdapter<Number>() {
            @Override
            public Number read(final JsonReader in) throws IOException {
                if (in.peek() == JsonToken.NULL) {
                    in.nextNull();
                    return null;
                }
                return in.nextLong();
            }
            
            @Override
            public void write(final JsonWriter out, final Number value) throws IOException {
                if (value == null) {
                    out.nullValue();
                    return;
                }
                out.value(value.toString());
            }
        };
    }
    
    private static TypeAdapter<AtomicLong> atomicLongAdapter(final TypeAdapter<Number> longAdapter) {
        return new TypeAdapter<AtomicLong>() {
            @Override
            public void write(final JsonWriter out, final AtomicLong value) throws IOException {
                longAdapter.write(out, value.get());
            }
            
            @Override
            public AtomicLong read(final JsonReader in) throws IOException {
                final Number value = longAdapter.read(in);
                return new AtomicLong(value.longValue());
            }
        }.nullSafe();
    }
    
    private static TypeAdapter<AtomicLongArray> atomicLongArrayAdapter(final TypeAdapter<Number> longAdapter) {
        return new TypeAdapter<AtomicLongArray>() {
            @Override
            public void write(final JsonWriter out, final AtomicLongArray value) throws IOException {
                out.beginArray();
                for (int i = 0, length = value.length(); i < length; ++i) {
                    longAdapter.write(out, value.get(i));
                }
                out.endArray();
            }
            
            @Override
            public AtomicLongArray read(final JsonReader in) throws IOException {
                final List<Long> list = new ArrayList<Long>();
                in.beginArray();
                while (in.hasNext()) {
                    final long value = longAdapter.read(in).longValue();
                    list.add(value);
                }
                in.endArray();
                final int length = list.size();
                final AtomicLongArray array = new AtomicLongArray(length);
                for (int i = 0; i < length; ++i) {
                    array.set(i, list.get(i));
                }
                return array;
            }
        }.nullSafe();
    }
    
    public <T> TypeAdapter<T> getAdapter(final TypeToken<T> type) {
        Objects.requireNonNull(type, "type must not be null");
        final TypeAdapter<?> cached = this.typeTokenCache.get(type);
        if (cached != null) {
            final TypeAdapter<T> adapter = (TypeAdapter<T>)cached;
            return adapter;
        }
        Map<TypeToken<?>, TypeAdapter<?>> threadCalls = this.threadLocalAdapterResults.get();
        boolean isInitialAdapterRequest = false;
        if (threadCalls == null) {
            threadCalls = new HashMap<TypeToken<?>, TypeAdapter<?>>();
            this.threadLocalAdapterResults.set(threadCalls);
            isInitialAdapterRequest = true;
        }
        else {
            final TypeAdapter<T> ongoingCall = (TypeAdapter<T>)threadCalls.get(type);
            if (ongoingCall != null) {
                return ongoingCall;
            }
        }
        TypeAdapter<T> candidate = null;
        try {
            final FutureTypeAdapter<T> call = new FutureTypeAdapter<T>();
            threadCalls.put(type, call);
            for (final TypeAdapterFactory factory : this.factories) {
                candidate = factory.create(this, type);
                if (candidate != null) {
                    call.setDelegate(candidate);
                    threadCalls.put(type, candidate);
                    break;
                }
            }
        }
        finally {
            if (isInitialAdapterRequest) {
                this.threadLocalAdapterResults.remove();
            }
        }
        if (candidate == null) {
            throw new IllegalArgumentException("GSON (2.10.1) cannot handle " + type);
        }
        if (isInitialAdapterRequest) {
            this.typeTokenCache.putAll((Map<?, ?>)threadCalls);
        }
        return candidate;
    }
    
    public <T> TypeAdapter<T> getDelegateAdapter(TypeAdapterFactory skipPast, final TypeToken<T> type) {
        if (!this.factories.contains(skipPast)) {
            skipPast = this.jsonAdapterFactory;
        }
        boolean skipPastFound = false;
        for (final TypeAdapterFactory factory : this.factories) {
            if (!skipPastFound) {
                if (factory != skipPast) {
                    continue;
                }
                skipPastFound = true;
            }
            else {
                final TypeAdapter<T> candidate = factory.create(this, type);
                if (candidate != null) {
                    return candidate;
                }
                continue;
            }
        }
        throw new IllegalArgumentException("GSON cannot serialize " + type);
    }
    
    public <T> TypeAdapter<T> getAdapter(final Class<T> type) {
        return this.getAdapter((TypeToken<T>)TypeToken.get((Class<T>)type));
    }
    
    public JsonElement toJsonTree(final Object src) {
        if (src == null) {
            return JsonNull.INSTANCE;
        }
        return this.toJsonTree(src, src.getClass());
    }
    
    public JsonElement toJsonTree(final Object src, final Type typeOfSrc) {
        final JsonTreeWriter writer = new JsonTreeWriter();
        this.toJson(src, typeOfSrc, writer);
        return writer.get();
    }
    
    public String toJson(final Object src) {
        if (src == null) {
            return this.toJson(JsonNull.INSTANCE);
        }
        return this.toJson(src, src.getClass());
    }
    
    public String toJson(final Object src, final Type typeOfSrc) {
        final StringWriter writer = new StringWriter();
        this.toJson(src, typeOfSrc, writer);
        return writer.toString();
    }
    
    public void toJson(final Object src, final Appendable writer) throws JsonIOException {
        if (src != null) {
            this.toJson(src, src.getClass(), writer);
        }
        else {
            this.toJson(JsonNull.INSTANCE, writer);
        }
    }
    
    public void toJson(final Object src, final Type typeOfSrc, final Appendable writer) throws JsonIOException {
        try {
            final JsonWriter jsonWriter = this.newJsonWriter(Streams.writerForAppendable(writer));
            this.toJson(src, typeOfSrc, jsonWriter);
        }
        catch (final IOException e) {
            throw new JsonIOException(e);
        }
    }
    
    public void toJson(final Object src, final Type typeOfSrc, final JsonWriter writer) throws JsonIOException {
        final TypeAdapter<Object> adapter = this.getAdapter(TypeToken.get(typeOfSrc));
        final boolean oldLenient = writer.isLenient();
        writer.setLenient(true);
        final boolean oldHtmlSafe = writer.isHtmlSafe();
        writer.setHtmlSafe(this.htmlSafe);
        final boolean oldSerializeNulls = writer.getSerializeNulls();
        writer.setSerializeNulls(this.serializeNulls);
        try {
            adapter.write(writer, src);
        }
        catch (final IOException e) {
            throw new JsonIOException(e);
        }
        catch (final AssertionError e2) {
            throw new AssertionError("AssertionError (GSON 2.10.1): " + e2.getMessage(), e2);
        }
        finally {
            writer.setLenient(oldLenient);
            writer.setHtmlSafe(oldHtmlSafe);
            writer.setSerializeNulls(oldSerializeNulls);
        }
    }
    
    public String toJson(final JsonElement jsonElement) {
        final StringWriter writer = new StringWriter();
        this.toJson(jsonElement, writer);
        return writer.toString();
    }
    
    public void toJson(final JsonElement jsonElement, final Appendable writer) throws JsonIOException {
        try {
            final JsonWriter jsonWriter = this.newJsonWriter(Streams.writerForAppendable(writer));
            this.toJson(jsonElement, jsonWriter);
        }
        catch (final IOException e) {
            throw new JsonIOException(e);
        }
    }
    
    public JsonWriter newJsonWriter(final Writer writer) throws IOException {
        if (this.generateNonExecutableJson) {
            writer.write(")]}'\n");
        }
        final JsonWriter jsonWriter = new JsonWriter(writer);
        if (this.prettyPrinting) {
            jsonWriter.setIndent("  ");
        }
        jsonWriter.setHtmlSafe(this.htmlSafe);
        jsonWriter.setLenient(this.lenient);
        jsonWriter.setSerializeNulls(this.serializeNulls);
        return jsonWriter;
    }
    
    public JsonReader newJsonReader(final Reader reader) {
        final JsonReader jsonReader = new JsonReader(reader);
        jsonReader.setLenient(this.lenient);
        return jsonReader;
    }
    
    public void toJson(final JsonElement jsonElement, final JsonWriter writer) throws JsonIOException {
        final boolean oldLenient = writer.isLenient();
        writer.setLenient(true);
        final boolean oldHtmlSafe = writer.isHtmlSafe();
        writer.setHtmlSafe(this.htmlSafe);
        final boolean oldSerializeNulls = writer.getSerializeNulls();
        writer.setSerializeNulls(this.serializeNulls);
        try {
            Streams.write(jsonElement, writer);
        }
        catch (final IOException e) {
            throw new JsonIOException(e);
        }
        catch (final AssertionError e2) {
            throw new AssertionError("AssertionError (GSON 2.10.1): " + e2.getMessage(), e2);
        }
        finally {
            writer.setLenient(oldLenient);
            writer.setHtmlSafe(oldHtmlSafe);
            writer.setSerializeNulls(oldSerializeNulls);
        }
    }
    
    public <T> T fromJson(final String json, final Class<T> classOfT) throws JsonSyntaxException {
        final T object = this.fromJson(json, (TypeToken<T>)TypeToken.get((Class<T>)classOfT));
        return Primitives.wrap(classOfT).cast(object);
    }
    
    public <T> T fromJson(final String json, final Type typeOfT) throws JsonSyntaxException {
        return this.fromJson(json, TypeToken.get(typeOfT));
    }
    
    public <T> T fromJson(final String json, final TypeToken<T> typeOfT) throws JsonSyntaxException {
        if (json == null) {
            return null;
        }
        final StringReader reader = new StringReader(json);
        return this.fromJson(reader, typeOfT);
    }
    
    public <T> T fromJson(final Reader json, final Class<T> classOfT) throws JsonSyntaxException, JsonIOException {
        final T object = this.fromJson(json, (TypeToken<T>)TypeToken.get((Class<T>)classOfT));
        return Primitives.wrap(classOfT).cast(object);
    }
    
    public <T> T fromJson(final Reader json, final Type typeOfT) throws JsonIOException, JsonSyntaxException {
        return this.fromJson(json, TypeToken.get(typeOfT));
    }
    
    public <T> T fromJson(final Reader json, final TypeToken<T> typeOfT) throws JsonIOException, JsonSyntaxException {
        final JsonReader jsonReader = this.newJsonReader(json);
        final T object = this.fromJson(jsonReader, typeOfT);
        assertFullConsumption(object, jsonReader);
        return object;
    }
    
    private static void assertFullConsumption(final Object obj, final JsonReader reader) {
        try {
            if (obj != null && reader.peek() != JsonToken.END_DOCUMENT) {
                throw new JsonSyntaxException("JSON document was not fully consumed.");
            }
        }
        catch (final MalformedJsonException e) {
            throw new JsonSyntaxException(e);
        }
        catch (final IOException e2) {
            throw new JsonIOException(e2);
        }
    }
    
    public <T> T fromJson(final JsonReader reader, final Type typeOfT) throws JsonIOException, JsonSyntaxException {
        return this.fromJson(reader, TypeToken.get(typeOfT));
    }
    
    public <T> T fromJson(final JsonReader reader, final TypeToken<T> typeOfT) throws JsonIOException, JsonSyntaxException {
        boolean isEmpty = true;
        final boolean oldLenient = reader.isLenient();
        reader.setLenient(true);
        try {
            reader.peek();
            isEmpty = false;
            final TypeAdapter<T> typeAdapter = this.getAdapter(typeOfT);
            return typeAdapter.read(reader);
        }
        catch (final EOFException e) {
            if (isEmpty) {
                return null;
            }
            throw new JsonSyntaxException(e);
        }
        catch (final IllegalStateException e2) {
            throw new JsonSyntaxException(e2);
        }
        catch (final IOException e3) {
            throw new JsonSyntaxException(e3);
        }
        catch (final AssertionError e4) {
            throw new AssertionError("AssertionError (GSON 2.10.1): " + e4.getMessage(), e4);
        }
        finally {
            reader.setLenient(oldLenient);
        }
    }
    
    public <T> T fromJson(final JsonElement json, final Class<T> classOfT) throws JsonSyntaxException {
        final T object = this.fromJson(json, (TypeToken<T>)TypeToken.get((Class<T>)classOfT));
        return Primitives.wrap(classOfT).cast(object);
    }
    
    public <T> T fromJson(final JsonElement json, final Type typeOfT) throws JsonSyntaxException {
        return this.fromJson(json, TypeToken.get(typeOfT));
    }
    
    public <T> T fromJson(final JsonElement json, final TypeToken<T> typeOfT) throws JsonSyntaxException {
        if (json == null) {
            return null;
        }
        return this.fromJson(new JsonTreeReader(json), typeOfT);
    }
    
    @Override
    public String toString() {
        return "{serializeNulls:" + this.serializeNulls + ",factories:" + this.factories + ",instanceCreators:" + this.constructorConstructor + "}";
    }
    
    static {
        DEFAULT_DATE_PATTERN = null;
        DEFAULT_FIELD_NAMING_STRATEGY = FieldNamingPolicy.IDENTITY;
        DEFAULT_OBJECT_TO_NUMBER_STRATEGY = ToNumberPolicy.DOUBLE;
        DEFAULT_NUMBER_TO_NUMBER_STRATEGY = ToNumberPolicy.LAZILY_PARSED_NUMBER;
    }
    
    static class FutureTypeAdapter<T> extends SerializationDelegatingTypeAdapter<T>
    {
        private TypeAdapter<T> delegate;
        
        FutureTypeAdapter() {
            this.delegate = null;
        }
        
        public void setDelegate(final TypeAdapter<T> typeAdapter) {
            if (this.delegate != null) {
                throw new AssertionError((Object)"Delegate is already set");
            }
            this.delegate = typeAdapter;
        }
        
        private TypeAdapter<T> delegate() {
            final TypeAdapter<T> delegate = this.delegate;
            if (delegate == null) {
                throw new IllegalStateException("Adapter for type with cyclic dependency has been used before dependency has been resolved");
            }
            return delegate;
        }
        
        @Override
        public TypeAdapter<T> getSerializationDelegate() {
            return this.delegate();
        }
        
        @Override
        public T read(final JsonReader in) throws IOException {
            return this.delegate().read(in);
        }
        
        @Override
        public void write(final JsonWriter out, final T value) throws IOException {
            this.delegate().write(out, value);
        }
    }
}
