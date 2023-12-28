// 
// Decompiled by Procyon v0.6.0
// 

package com.viaversion.viaversion.libs.kyori.examination;

import java.util.AbstractMap;
import java.util.function.Function;
import java.util.Arrays;
import java.util.stream.LongStream;
import java.util.stream.IntStream;
import java.util.stream.DoubleStream;
import java.util.stream.Stream;
import java.util.stream.BaseStream;
import java.util.Map;
import java.util.Collection;

public abstract class AbstractExaminer<R> implements Examiner<R>
{
    @Override
    public R examine(final Object value) {
        if (value == null) {
            return this.nil();
        }
        if (value instanceof String) {
            return this.examine((String)value);
        }
        if (value instanceof Examinable) {
            return this.examine((Examinable)value);
        }
        if (value instanceof Collection) {
            return this.collection((Collection<Object>)value);
        }
        if (value instanceof Map) {
            return this.map((Map<Object, Object>)value);
        }
        if (value.getClass().isArray()) {
            final Class<?> type = value.getClass().getComponentType();
            if (type.isPrimitive()) {
                if (type == Boolean.TYPE) {
                    return this.examine((boolean[])value);
                }
                if (type == Byte.TYPE) {
                    return this.examine((byte[])value);
                }
                if (type == Character.TYPE) {
                    return this.examine((char[])value);
                }
                if (type == Double.TYPE) {
                    return this.examine((double[])value);
                }
                if (type == Float.TYPE) {
                    return this.examine((float[])value);
                }
                if (type == Integer.TYPE) {
                    return this.examine((int[])value);
                }
                if (type == Long.TYPE) {
                    return this.examine((long[])value);
                }
                if (type == Short.TYPE) {
                    return this.examine((short[])value);
                }
            }
            return this.array((Object[])value);
        }
        if (value instanceof Boolean) {
            return this.examine((boolean)value);
        }
        if (value instanceof Character) {
            return this.examine((char)value);
        }
        if (value instanceof Number) {
            if (value instanceof Byte) {
                return this.examine((byte)value);
            }
            if (value instanceof Double) {
                return this.examine((double)value);
            }
            if (value instanceof Float) {
                return this.examine((float)value);
            }
            if (value instanceof Integer) {
                return this.examine((int)value);
            }
            if (value instanceof Long) {
                return this.examine((long)value);
            }
            if (value instanceof Short) {
                return this.examine((short)value);
            }
        }
        else if (value instanceof BaseStream) {
            if (value instanceof Stream) {
                return this.stream((Stream<Object>)value);
            }
            if (value instanceof DoubleStream) {
                return this.stream((DoubleStream)value);
            }
            if (value instanceof IntStream) {
                return this.stream((IntStream)value);
            }
            if (value instanceof LongStream) {
                return this.stream((LongStream)value);
            }
        }
        return this.scalar(value);
    }
    
    private <E> R array(final E[] array) {
        return this.array(array, Arrays.stream(array).map((Function<? super E, ? extends R>)this::examine));
    }
    
    protected abstract <E> R array(final E[] array, final Stream<R> elements);
    
    private <E> R collection(final Collection<E> collection) {
        return this.collection(collection, collection.stream().map((Function<? super E, ? extends R>)this::examine));
    }
    
    protected abstract <E> R collection(final Collection<E> collection, final Stream<R> elements);
    
    @Override
    public R examine(final String name, final Stream<? extends ExaminableProperty> properties) {
        return this.examinable(name, properties.map(property -> new AbstractMap.SimpleImmutableEntry(property.name(), property.examine((Examiner<?>)this))));
    }
    
    protected abstract R examinable(final String name, final Stream<Map.Entry<String, R>> properties);
    
    private <K, V> R map(final Map<K, V> map) {
        return this.map(map, map.entrySet().stream().map(entry -> new AbstractMap.SimpleImmutableEntry(this.examine(entry.getKey()), this.examine(entry.getValue()))));
    }
    
    protected abstract <K, V> R map(final Map<K, V> map, final Stream<Map.Entry<R, R>> entries);
    
    protected abstract R nil();
    
    protected abstract R scalar(final Object value);
    
    protected abstract <T> R stream(final Stream<T> stream);
    
    protected abstract R stream(final DoubleStream stream);
    
    protected abstract R stream(final IntStream stream);
    
    protected abstract R stream(final LongStream stream);
}
