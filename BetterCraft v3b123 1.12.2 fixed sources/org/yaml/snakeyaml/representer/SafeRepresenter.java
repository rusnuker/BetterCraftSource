// 
// Decompiled by Procyon v0.6.0
// 

package org.yaml.snakeyaml.representer;

import java.util.LinkedHashMap;
import java.util.ArrayList;
import java.util.Arrays;
import java.math.BigInteger;
import org.yaml.snakeyaml.external.biz.base64Coder.Base64Coder;
import org.yaml.snakeyaml.error.YAMLException;
import java.nio.charset.StandardCharsets;
import org.yaml.snakeyaml.reader.StreamReader;
import org.yaml.snakeyaml.nodes.Node;
import java.util.HashMap;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.Set;
import java.util.List;
import java.util.UUID;
import java.util.regex.Pattern;
import org.yaml.snakeyaml.DumperOptions;
import java.util.TimeZone;
import org.yaml.snakeyaml.nodes.Tag;
import java.util.Map;

class SafeRepresenter extends BaseRepresenter
{
    protected Map<Class<?>, Tag> classTags;
    protected TimeZone timeZone;
    protected DumperOptions.NonPrintableStyle nonPrintableStyle;
    private static final Pattern MULTILINE_PATTERN;
    
    public SafeRepresenter(final DumperOptions options) {
        this.timeZone = null;
        if (options == null) {
            throw new NullPointerException("DumperOptions must be provided.");
        }
        this.nullRepresenter = new RepresentNull();
        this.representers.put(String.class, new RepresentString());
        this.representers.put(Boolean.class, new RepresentBoolean());
        this.representers.put(Character.class, new RepresentString());
        this.representers.put(UUID.class, new RepresentUuid());
        this.representers.put(byte[].class, new RepresentByteArray());
        final Represent primitiveArray = new RepresentPrimitiveArray();
        this.representers.put(short[].class, primitiveArray);
        this.representers.put(int[].class, primitiveArray);
        this.representers.put(long[].class, primitiveArray);
        this.representers.put(float[].class, primitiveArray);
        this.representers.put(double[].class, primitiveArray);
        this.representers.put(char[].class, primitiveArray);
        this.representers.put(boolean[].class, primitiveArray);
        this.multiRepresenters.put(Number.class, new RepresentNumber());
        this.multiRepresenters.put(List.class, new RepresentList());
        this.multiRepresenters.put(Map.class, new RepresentMap());
        this.multiRepresenters.put(Set.class, new RepresentSet());
        this.multiRepresenters.put(Iterator.class, new RepresentIterator());
        this.multiRepresenters.put(new Object[0].getClass(), new RepresentArray());
        this.multiRepresenters.put(Date.class, new RepresentDate());
        this.multiRepresenters.put(Enum.class, new RepresentEnum());
        this.multiRepresenters.put(Calendar.class, new RepresentDate());
        this.classTags = new HashMap<Class<?>, Tag>();
        this.nonPrintableStyle = options.getNonPrintableStyle();
    }
    
    protected Tag getTag(final Class<?> clazz, final Tag defaultTag) {
        if (this.classTags.containsKey(clazz)) {
            return this.classTags.get(clazz);
        }
        return defaultTag;
    }
    
    public Tag addClassTag(final Class<?> clazz, final Tag tag) {
        if (tag == null) {
            throw new NullPointerException("Tag must be provided.");
        }
        return this.classTags.put(clazz, tag);
    }
    
    public TimeZone getTimeZone() {
        return this.timeZone;
    }
    
    public void setTimeZone(final TimeZone timeZone) {
        this.timeZone = timeZone;
    }
    
    static {
        MULTILINE_PATTERN = Pattern.compile("\n|\u0085|\u2028|\u2029");
    }
    
    protected class RepresentNull implements Represent
    {
        @Override
        public Node representData(final Object data) {
            return SafeRepresenter.this.representScalar(Tag.NULL, "null");
        }
    }
    
    protected class RepresentString implements Represent
    {
        @Override
        public Node representData(final Object data) {
            Tag tag = Tag.STR;
            DumperOptions.ScalarStyle style = null;
            String value = data.toString();
            if (SafeRepresenter.this.nonPrintableStyle == DumperOptions.NonPrintableStyle.BINARY && !StreamReader.isPrintable(value)) {
                tag = Tag.BINARY;
                final byte[] bytes = value.getBytes(StandardCharsets.UTF_8);
                final String checkValue = new String(bytes, StandardCharsets.UTF_8);
                if (!checkValue.equals(value)) {
                    throw new YAMLException("invalid string value has occurred");
                }
                final char[] binary = Base64Coder.encode(bytes);
                value = String.valueOf(binary);
                style = DumperOptions.ScalarStyle.LITERAL;
            }
            if (SafeRepresenter.this.defaultScalarStyle == DumperOptions.ScalarStyle.PLAIN && SafeRepresenter.MULTILINE_PATTERN.matcher(value).find()) {
                style = DumperOptions.ScalarStyle.LITERAL;
            }
            return SafeRepresenter.this.representScalar(tag, value, style);
        }
    }
    
    protected class RepresentBoolean implements Represent
    {
        @Override
        public Node representData(final Object data) {
            String value;
            if (Boolean.TRUE.equals(data)) {
                value = "true";
            }
            else {
                value = "false";
            }
            return SafeRepresenter.this.representScalar(Tag.BOOL, value);
        }
    }
    
    protected class RepresentNumber implements Represent
    {
        @Override
        public Node representData(final Object data) {
            Tag tag;
            String value;
            if (data instanceof Byte || data instanceof Short || data instanceof Integer || data instanceof Long || data instanceof BigInteger) {
                tag = Tag.INT;
                value = data.toString();
            }
            else {
                final Number number = (Number)data;
                tag = Tag.FLOAT;
                if (number.equals(Double.NaN)) {
                    value = ".NaN";
                }
                else if (number.equals(Double.POSITIVE_INFINITY)) {
                    value = ".inf";
                }
                else if (number.equals(Double.NEGATIVE_INFINITY)) {
                    value = "-.inf";
                }
                else {
                    value = number.toString();
                }
            }
            return SafeRepresenter.this.representScalar(SafeRepresenter.this.getTag(data.getClass(), tag), value);
        }
    }
    
    protected class RepresentList implements Represent
    {
        @Override
        public Node representData(final Object data) {
            return SafeRepresenter.this.representSequence(SafeRepresenter.this.getTag(data.getClass(), Tag.SEQ), (Iterable<?>)data, DumperOptions.FlowStyle.AUTO);
        }
    }
    
    protected class RepresentIterator implements Represent
    {
        @Override
        public Node representData(final Object data) {
            final Iterator<Object> iter = (Iterator<Object>)data;
            return SafeRepresenter.this.representSequence(SafeRepresenter.this.getTag(data.getClass(), Tag.SEQ), new IteratorWrapper(iter), DumperOptions.FlowStyle.AUTO);
        }
    }
    
    private static class IteratorWrapper implements Iterable<Object>
    {
        private final Iterator<Object> iter;
        
        public IteratorWrapper(final Iterator<Object> iter) {
            this.iter = iter;
        }
        
        @Override
        public Iterator<Object> iterator() {
            return this.iter;
        }
    }
    
    protected class RepresentArray implements Represent
    {
        @Override
        public Node representData(final Object data) {
            final Object[] array = (Object[])data;
            final List<Object> list = Arrays.asList(array);
            return SafeRepresenter.this.representSequence(Tag.SEQ, list, DumperOptions.FlowStyle.AUTO);
        }
    }
    
    protected class RepresentPrimitiveArray implements Represent
    {
        @Override
        public Node representData(final Object data) {
            final Class<?> type = data.getClass().getComponentType();
            if (Byte.TYPE == type) {
                return SafeRepresenter.this.representSequence(Tag.SEQ, this.asByteList(data), DumperOptions.FlowStyle.AUTO);
            }
            if (Short.TYPE == type) {
                return SafeRepresenter.this.representSequence(Tag.SEQ, this.asShortList(data), DumperOptions.FlowStyle.AUTO);
            }
            if (Integer.TYPE == type) {
                return SafeRepresenter.this.representSequence(Tag.SEQ, this.asIntList(data), DumperOptions.FlowStyle.AUTO);
            }
            if (Long.TYPE == type) {
                return SafeRepresenter.this.representSequence(Tag.SEQ, this.asLongList(data), DumperOptions.FlowStyle.AUTO);
            }
            if (Float.TYPE == type) {
                return SafeRepresenter.this.representSequence(Tag.SEQ, this.asFloatList(data), DumperOptions.FlowStyle.AUTO);
            }
            if (Double.TYPE == type) {
                return SafeRepresenter.this.representSequence(Tag.SEQ, this.asDoubleList(data), DumperOptions.FlowStyle.AUTO);
            }
            if (Character.TYPE == type) {
                return SafeRepresenter.this.representSequence(Tag.SEQ, this.asCharList(data), DumperOptions.FlowStyle.AUTO);
            }
            if (Boolean.TYPE == type) {
                return SafeRepresenter.this.representSequence(Tag.SEQ, this.asBooleanList(data), DumperOptions.FlowStyle.AUTO);
            }
            throw new YAMLException("Unexpected primitive '" + type.getCanonicalName() + "'");
        }
        
        private List<Byte> asByteList(final Object in) {
            final byte[] array = (byte[])in;
            final List<Byte> list = new ArrayList<Byte>(array.length);
            for (int i = 0; i < array.length; ++i) {
                list.add(array[i]);
            }
            return list;
        }
        
        private List<Short> asShortList(final Object in) {
            final short[] array = (short[])in;
            final List<Short> list = new ArrayList<Short>(array.length);
            for (int i = 0; i < array.length; ++i) {
                list.add(array[i]);
            }
            return list;
        }
        
        private List<Integer> asIntList(final Object in) {
            final int[] array = (int[])in;
            final List<Integer> list = new ArrayList<Integer>(array.length);
            for (int i = 0; i < array.length; ++i) {
                list.add(array[i]);
            }
            return list;
        }
        
        private List<Long> asLongList(final Object in) {
            final long[] array = (long[])in;
            final List<Long> list = new ArrayList<Long>(array.length);
            for (int i = 0; i < array.length; ++i) {
                list.add(array[i]);
            }
            return list;
        }
        
        private List<Float> asFloatList(final Object in) {
            final float[] array = (float[])in;
            final List<Float> list = new ArrayList<Float>(array.length);
            for (int i = 0; i < array.length; ++i) {
                list.add(array[i]);
            }
            return list;
        }
        
        private List<Double> asDoubleList(final Object in) {
            final double[] array = (double[])in;
            final List<Double> list = new ArrayList<Double>(array.length);
            for (int i = 0; i < array.length; ++i) {
                list.add(array[i]);
            }
            return list;
        }
        
        private List<Character> asCharList(final Object in) {
            final char[] array = (char[])in;
            final List<Character> list = new ArrayList<Character>(array.length);
            for (int i = 0; i < array.length; ++i) {
                list.add(array[i]);
            }
            return list;
        }
        
        private List<Boolean> asBooleanList(final Object in) {
            final boolean[] array = (boolean[])in;
            final List<Boolean> list = new ArrayList<Boolean>(array.length);
            for (int i = 0; i < array.length; ++i) {
                list.add(array[i]);
            }
            return list;
        }
    }
    
    protected class RepresentMap implements Represent
    {
        @Override
        public Node representData(final Object data) {
            return SafeRepresenter.this.representMapping(SafeRepresenter.this.getTag(data.getClass(), Tag.MAP), (Map<?, ?>)data, DumperOptions.FlowStyle.AUTO);
        }
    }
    
    protected class RepresentSet implements Represent
    {
        @Override
        public Node representData(final Object data) {
            final Map<Object, Object> value = new LinkedHashMap<Object, Object>();
            final Set<Object> set = (Set<Object>)data;
            for (final Object key : set) {
                value.put(key, null);
            }
            return SafeRepresenter.this.representMapping(SafeRepresenter.this.getTag(data.getClass(), Tag.SET), value, DumperOptions.FlowStyle.AUTO);
        }
    }
    
    protected class RepresentDate implements Represent
    {
        @Override
        public Node representData(final Object data) {
            Calendar calendar;
            if (data instanceof Calendar) {
                calendar = (Calendar)data;
            }
            else {
                calendar = Calendar.getInstance((SafeRepresenter.this.getTimeZone() == null) ? TimeZone.getTimeZone("UTC") : SafeRepresenter.this.timeZone);
                calendar.setTime((Date)data);
            }
            final int years = calendar.get(1);
            final int months = calendar.get(2) + 1;
            final int days = calendar.get(5);
            final int hour24 = calendar.get(11);
            final int minutes = calendar.get(12);
            final int seconds = calendar.get(13);
            final int millis = calendar.get(14);
            final StringBuilder buffer = new StringBuilder(String.valueOf(years));
            while (buffer.length() < 4) {
                buffer.insert(0, "0");
            }
            buffer.append("-");
            if (months < 10) {
                buffer.append("0");
            }
            buffer.append(months);
            buffer.append("-");
            if (days < 10) {
                buffer.append("0");
            }
            buffer.append(days);
            buffer.append("T");
            if (hour24 < 10) {
                buffer.append("0");
            }
            buffer.append(hour24);
            buffer.append(":");
            if (minutes < 10) {
                buffer.append("0");
            }
            buffer.append(minutes);
            buffer.append(":");
            if (seconds < 10) {
                buffer.append("0");
            }
            buffer.append(seconds);
            if (millis > 0) {
                if (millis < 10) {
                    buffer.append(".00");
                }
                else if (millis < 100) {
                    buffer.append(".0");
                }
                else {
                    buffer.append(".");
                }
                buffer.append(millis);
            }
            int gmtOffset = calendar.getTimeZone().getOffset(calendar.getTime().getTime());
            if (gmtOffset == 0) {
                buffer.append('Z');
            }
            else {
                if (gmtOffset < 0) {
                    buffer.append('-');
                    gmtOffset *= -1;
                }
                else {
                    buffer.append('+');
                }
                final int minutesOffset = gmtOffset / 60000;
                final int hoursOffset = minutesOffset / 60;
                final int partOfHour = minutesOffset % 60;
                if (hoursOffset < 10) {
                    buffer.append('0');
                }
                buffer.append(hoursOffset);
                buffer.append(':');
                if (partOfHour < 10) {
                    buffer.append('0');
                }
                buffer.append(partOfHour);
            }
            return SafeRepresenter.this.representScalar(SafeRepresenter.this.getTag(data.getClass(), Tag.TIMESTAMP), buffer.toString(), DumperOptions.ScalarStyle.PLAIN);
        }
    }
    
    protected class RepresentEnum implements Represent
    {
        @Override
        public Node representData(final Object data) {
            final Tag tag = new Tag(data.getClass());
            return SafeRepresenter.this.representScalar(SafeRepresenter.this.getTag(data.getClass(), tag), ((Enum)data).name());
        }
    }
    
    protected class RepresentByteArray implements Represent
    {
        @Override
        public Node representData(final Object data) {
            final char[] binary = Base64Coder.encode((byte[])data);
            return SafeRepresenter.this.representScalar(Tag.BINARY, String.valueOf(binary), DumperOptions.ScalarStyle.LITERAL);
        }
    }
    
    protected class RepresentUuid implements Represent
    {
        @Override
        public Node representData(final Object data) {
            return SafeRepresenter.this.representScalar(SafeRepresenter.this.getTag(data.getClass(), new Tag(UUID.class)), data.toString());
        }
    }
}
