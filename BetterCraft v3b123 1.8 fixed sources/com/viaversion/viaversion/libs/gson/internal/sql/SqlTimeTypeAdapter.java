// 
// Decompiled by Procyon v0.6.0
// 

package com.viaversion.viaversion.libs.gson.internal.sql;

import com.viaversion.viaversion.libs.gson.reflect.TypeToken;
import com.viaversion.viaversion.libs.gson.Gson;
import com.viaversion.viaversion.libs.gson.stream.JsonWriter;
import java.io.IOException;
import java.util.Date;
import java.text.ParseException;
import com.viaversion.viaversion.libs.gson.JsonSyntaxException;
import com.viaversion.viaversion.libs.gson.stream.JsonToken;
import com.viaversion.viaversion.libs.gson.stream.JsonReader;
import java.text.SimpleDateFormat;
import java.text.DateFormat;
import com.viaversion.viaversion.libs.gson.TypeAdapterFactory;
import java.sql.Time;
import com.viaversion.viaversion.libs.gson.TypeAdapter;

final class SqlTimeTypeAdapter extends TypeAdapter<Time>
{
    static final TypeAdapterFactory FACTORY;
    private final DateFormat format;
    
    private SqlTimeTypeAdapter() {
        this.format = new SimpleDateFormat("hh:mm:ss a");
    }
    
    @Override
    public Time read(final JsonReader in) throws IOException {
        if (in.peek() == JsonToken.NULL) {
            in.nextNull();
            return null;
        }
        final String s = in.nextString();
        try {
            synchronized (this) {
                final Date date = this.format.parse(s);
                return new Time(date.getTime());
            }
        }
        catch (final ParseException e) {
            throw new JsonSyntaxException("Failed parsing '" + s + "' as SQL Time; at path " + in.getPreviousPath(), e);
        }
    }
    
    @Override
    public void write(final JsonWriter out, final Time value) throws IOException {
        if (value == null) {
            out.nullValue();
            return;
        }
        final String timeString;
        synchronized (this) {
            timeString = this.format.format(value);
        }
        out.value(timeString);
    }
    
    static {
        FACTORY = new TypeAdapterFactory() {
            @Override
            public <T> TypeAdapter<T> create(final Gson gson, final TypeToken<T> typeToken) {
                return (TypeAdapter<T>)((typeToken.getRawType() == Time.class) ? new SqlTimeTypeAdapter(null) : null);
            }
        };
    }
}
