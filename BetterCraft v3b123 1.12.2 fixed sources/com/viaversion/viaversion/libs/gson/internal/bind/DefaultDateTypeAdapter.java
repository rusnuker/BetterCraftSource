// 
// Decompiled by Procyon v0.6.0
// 

package com.viaversion.viaversion.libs.gson.internal.bind;

import com.viaversion.viaversion.libs.gson.TypeAdapterFactory;
import java.util.Iterator;
import com.viaversion.viaversion.libs.gson.JsonSyntaxException;
import com.viaversion.viaversion.libs.gson.internal.bind.util.ISO8601Utils;
import java.text.ParsePosition;
import java.text.ParseException;
import com.viaversion.viaversion.libs.gson.stream.JsonToken;
import com.viaversion.viaversion.libs.gson.stream.JsonReader;
import java.io.IOException;
import com.viaversion.viaversion.libs.gson.stream.JsonWriter;
import com.viaversion.viaversion.libs.gson.internal.PreJava9DateFormatProvider;
import com.viaversion.viaversion.libs.gson.internal.JavaVersion;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.Objects;
import java.util.ArrayList;
import java.text.DateFormat;
import java.util.List;
import com.viaversion.viaversion.libs.gson.TypeAdapter;
import java.util.Date;

public final class DefaultDateTypeAdapter<T extends Date> extends TypeAdapter<T>
{
    private static final String SIMPLE_NAME = "DefaultDateTypeAdapter";
    private final DateType<T> dateType;
    private final List<DateFormat> dateFormats;
    
    private DefaultDateTypeAdapter(final DateType<T> dateType, final String datePattern) {
        this.dateFormats = new ArrayList<DateFormat>();
        this.dateType = Objects.requireNonNull(dateType);
        this.dateFormats.add(new SimpleDateFormat(datePattern, Locale.US));
        if (!Locale.getDefault().equals(Locale.US)) {
            this.dateFormats.add(new SimpleDateFormat(datePattern));
        }
    }
    
    private DefaultDateTypeAdapter(final DateType<T> dateType, final int style) {
        this.dateFormats = new ArrayList<DateFormat>();
        this.dateType = Objects.requireNonNull(dateType);
        this.dateFormats.add(DateFormat.getDateInstance(style, Locale.US));
        if (!Locale.getDefault().equals(Locale.US)) {
            this.dateFormats.add(DateFormat.getDateInstance(style));
        }
        if (JavaVersion.isJava9OrLater()) {
            this.dateFormats.add(PreJava9DateFormatProvider.getUSDateFormat(style));
        }
    }
    
    private DefaultDateTypeAdapter(final DateType<T> dateType, final int dateStyle, final int timeStyle) {
        this.dateFormats = new ArrayList<DateFormat>();
        this.dateType = Objects.requireNonNull(dateType);
        this.dateFormats.add(DateFormat.getDateTimeInstance(dateStyle, timeStyle, Locale.US));
        if (!Locale.getDefault().equals(Locale.US)) {
            this.dateFormats.add(DateFormat.getDateTimeInstance(dateStyle, timeStyle));
        }
        if (JavaVersion.isJava9OrLater()) {
            this.dateFormats.add(PreJava9DateFormatProvider.getUSDateTimeFormat(dateStyle, timeStyle));
        }
    }
    
    @Override
    public void write(final JsonWriter out, final Date value) throws IOException {
        if (value == null) {
            out.nullValue();
            return;
        }
        final DateFormat dateFormat = this.dateFormats.get(0);
        final String dateFormatAsString;
        synchronized (this.dateFormats) {
            dateFormatAsString = dateFormat.format(value);
        }
        out.value(dateFormatAsString);
    }
    
    @Override
    public T read(final JsonReader in) throws IOException {
        if (in.peek() == JsonToken.NULL) {
            in.nextNull();
            return null;
        }
        final Date date = this.deserializeToDate(in);
        return this.dateType.deserialize(date);
    }
    
    private Date deserializeToDate(final JsonReader in) throws IOException {
        final String s = in.nextString();
        synchronized (this.dateFormats) {
            for (final DateFormat dateFormat : this.dateFormats) {
                try {
                    return dateFormat.parse(s);
                }
                catch (final ParseException ex) {
                    continue;
                }
                break;
            }
        }
        try {
            return ISO8601Utils.parse(s, new ParsePosition(0));
        }
        catch (final ParseException e) {
            throw new JsonSyntaxException("Failed parsing '" + s + "' as Date; at path " + in.getPreviousPath(), e);
        }
    }
    
    @Override
    public String toString() {
        final DateFormat defaultFormat = this.dateFormats.get(0);
        if (defaultFormat instanceof SimpleDateFormat) {
            return "DefaultDateTypeAdapter(" + ((SimpleDateFormat)defaultFormat).toPattern() + ')';
        }
        return "DefaultDateTypeAdapter(" + defaultFormat.getClass().getSimpleName() + ')';
    }
    
    public abstract static class DateType<T extends Date>
    {
        public static final DateType<Date> DATE;
        private final Class<T> dateClass;
        
        protected DateType(final Class<T> dateClass) {
            this.dateClass = dateClass;
        }
        
        protected abstract T deserialize(final Date p0);
        
        private TypeAdapterFactory createFactory(final DefaultDateTypeAdapter<T> adapter) {
            return TypeAdapters.newFactory(this.dateClass, adapter);
        }
        
        public final TypeAdapterFactory createAdapterFactory(final String datePattern) {
            return this.createFactory(new DefaultDateTypeAdapter<T>(this, datePattern, null));
        }
        
        public final TypeAdapterFactory createAdapterFactory(final int style) {
            return this.createFactory(new DefaultDateTypeAdapter<T>(this, style, null));
        }
        
        public final TypeAdapterFactory createAdapterFactory(final int dateStyle, final int timeStyle) {
            return this.createFactory(new DefaultDateTypeAdapter<T>(this, dateStyle, timeStyle, null));
        }
        
        public final TypeAdapterFactory createDefaultsAdapterFactory() {
            return this.createFactory(new DefaultDateTypeAdapter<T>(this, 2, 2, null));
        }
        
        static {
            DATE = new DateType<Date>() {
                @Override
                protected Date deserialize(final Date date) {
                    return date;
                }
            };
        }
    }
}
