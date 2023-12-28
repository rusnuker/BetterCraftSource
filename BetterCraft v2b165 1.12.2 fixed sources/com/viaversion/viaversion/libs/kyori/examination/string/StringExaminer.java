// 
// Decompiled by Procyon v0.6.0
// 

package com.viaversion.viaversion.libs.kyori.examination.string;

import java.util.stream.Collectors;
import java.util.function.LongFunction;
import java.util.stream.LongStream;
import java.util.function.IntFunction;
import java.util.stream.IntStream;
import java.util.function.DoubleFunction;
import java.util.stream.DoubleStream;
import java.util.Map;
import java.util.Collection;
import java.util.stream.Stream;
import java.util.stream.Collector;
import java.util.function.Function;
import com.viaversion.viaversion.libs.kyori.examination.AbstractExaminer;

public class StringExaminer extends AbstractExaminer<String>
{
    private static final Function<String, String> DEFAULT_ESCAPER;
    private static final Collector<CharSequence, ?, String> COMMA_CURLY;
    private static final Collector<CharSequence, ?, String> COMMA_SQUARE;
    private final Function<String, String> escaper;
    
    public static StringExaminer simpleEscaping() {
        return Instances.SIMPLE_ESCAPING;
    }
    
    public StringExaminer(final Function<String, String> escaper) {
        this.escaper = escaper;
    }
    
    @Override
    protected <E> String array(final E[] array, final Stream<String> elements) {
        return elements.collect(StringExaminer.COMMA_SQUARE);
    }
    
    @Override
    protected <E> String collection(final Collection<E> collection, final Stream<String> elements) {
        return elements.collect(StringExaminer.COMMA_SQUARE);
    }
    
    @Override
    protected String examinable(final String name, final Stream<Map.Entry<String, String>> properties) {
        return name + properties.map(property -> property.getKey() + '=' + property.getValue()).collect((Collector<? super Object, ?, String>)StringExaminer.COMMA_CURLY);
    }
    
    @Override
    protected <K, V> String map(final Map<K, V> map, final Stream<Map.Entry<String, String>> entries) {
        return entries.map(entry -> entry.getKey() + '=' + entry.getValue()).collect((Collector<? super Object, ?, String>)StringExaminer.COMMA_CURLY);
    }
    
    @Override
    protected String nil() {
        return "null";
    }
    
    @Override
    protected String scalar(final Object value) {
        return String.valueOf(value);
    }
    
    @Override
    public String examine(final boolean value) {
        return String.valueOf(value);
    }
    
    @Override
    public String examine(final boolean[] values) {
        if (values == null) {
            return this.nil();
        }
        return array(values.length, index -> this.examine(values[index]));
    }
    
    @Override
    public String examine(final byte value) {
        return String.valueOf(value);
    }
    
    @Override
    public String examine(final byte[] values) {
        if (values == null) {
            return this.nil();
        }
        return array(values.length, index -> this.examine(values[index]));
    }
    
    @Override
    public String examine(final char value) {
        return '\'' + this.escaper.apply(String.valueOf(value)) + '\'';
    }
    
    @Override
    public String examine(final char[] values) {
        if (values == null) {
            return this.nil();
        }
        return array(values.length, index -> this.examine(values[index]));
    }
    
    @Override
    public String examine(final double value) {
        return withSuffix(String.valueOf(value), 'd');
    }
    
    @Override
    public String examine(final double[] values) {
        if (values == null) {
            return this.nil();
        }
        return array(values.length, index -> this.examine(values[index]));
    }
    
    @Override
    public String examine(final float value) {
        return withSuffix(String.valueOf(value), 'f');
    }
    
    @Override
    public String examine(final float[] values) {
        if (values == null) {
            return this.nil();
        }
        return array(values.length, index -> this.examine(values[index]));
    }
    
    @Override
    public String examine(final int value) {
        return String.valueOf(value);
    }
    
    @Override
    public String examine(final int[] values) {
        if (values == null) {
            return this.nil();
        }
        return array(values.length, index -> this.examine(values[index]));
    }
    
    @Override
    public String examine(final long value) {
        return String.valueOf(value);
    }
    
    @Override
    public String examine(final long[] values) {
        if (values == null) {
            return this.nil();
        }
        return array(values.length, index -> this.examine(values[index]));
    }
    
    @Override
    public String examine(final short value) {
        return String.valueOf(value);
    }
    
    @Override
    public String examine(final short[] values) {
        if (values == null) {
            return this.nil();
        }
        return array(values.length, index -> this.examine(values[index]));
    }
    
    @Override
    protected <T> String stream(final Stream<T> stream) {
        return stream.map((Function<? super T, ?>)this::examine).collect((Collector<? super Object, ?, String>)StringExaminer.COMMA_SQUARE);
    }
    
    @Override
    protected String stream(final DoubleStream stream) {
        return stream.mapToObj((DoubleFunction<?>)this::examine).collect((Collector<? super Object, ?, String>)StringExaminer.COMMA_SQUARE);
    }
    
    @Override
    protected String stream(final IntStream stream) {
        return stream.mapToObj((IntFunction<?>)this::examine).collect((Collector<? super Object, ?, String>)StringExaminer.COMMA_SQUARE);
    }
    
    @Override
    protected String stream(final LongStream stream) {
        return stream.mapToObj((LongFunction<?>)this::examine).collect((Collector<? super Object, ?, String>)StringExaminer.COMMA_SQUARE);
    }
    
    @Override
    public String examine(final String value) {
        if (value == null) {
            return this.nil();
        }
        return '\"' + this.escaper.apply(value) + '\"';
    }
    
    private static String withSuffix(final String string, final char suffix) {
        return string + suffix;
    }
    
    private static String array(final int length, final IntFunction<String> value) {
        final StringBuilder sb = new StringBuilder();
        sb.append('[');
        for (int i = 0; i < length; ++i) {
            sb.append(value.apply(i));
            if (i + 1 < length) {
                sb.append(", ");
            }
        }
        sb.append(']');
        return sb.toString();
    }
    
    static {
        DEFAULT_ESCAPER = (string -> string.replace("\"", "\\\"").replace("\\", "\\\\").replace("\b", "\\b").replace("\f", "\\f").replace("\n", "\\n").replace("\r", "\\r").replace("\t", "\\t"));
        COMMA_CURLY = Collectors.joining(", ", "{", "}");
        COMMA_SQUARE = Collectors.joining(", ", "[", "]");
    }
    
    private static final class Instances
    {
        static final StringExaminer SIMPLE_ESCAPING;
        
        static {
            SIMPLE_ESCAPING = new StringExaminer(StringExaminer.DEFAULT_ESCAPER);
        }
    }
}
