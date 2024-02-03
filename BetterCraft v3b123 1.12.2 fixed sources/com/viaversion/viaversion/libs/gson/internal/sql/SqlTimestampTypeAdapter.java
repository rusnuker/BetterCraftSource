// 
// Decompiled by Procyon v0.6.0
// 

package com.viaversion.viaversion.libs.gson.internal.sql;

import com.viaversion.viaversion.libs.gson.reflect.TypeToken;
import com.viaversion.viaversion.libs.gson.Gson;
import com.viaversion.viaversion.libs.gson.stream.JsonWriter;
import java.io.IOException;
import com.viaversion.viaversion.libs.gson.stream.JsonReader;
import java.util.Date;
import com.viaversion.viaversion.libs.gson.TypeAdapterFactory;
import java.sql.Timestamp;
import com.viaversion.viaversion.libs.gson.TypeAdapter;

class SqlTimestampTypeAdapter extends TypeAdapter<Timestamp>
{
    static final TypeAdapterFactory FACTORY;
    private final TypeAdapter<Date> dateTypeAdapter;
    
    private SqlTimestampTypeAdapter(final TypeAdapter<Date> dateTypeAdapter) {
        this.dateTypeAdapter = dateTypeAdapter;
    }
    
    @Override
    public Timestamp read(final JsonReader in) throws IOException {
        final Date date = this.dateTypeAdapter.read(in);
        return (date != null) ? new Timestamp(date.getTime()) : null;
    }
    
    @Override
    public void write(final JsonWriter out, final Timestamp value) throws IOException {
        this.dateTypeAdapter.write(out, value);
    }
    
    static {
        FACTORY = new TypeAdapterFactory() {
            @Override
            public <T> TypeAdapter<T> create(final Gson gson, final TypeToken<T> typeToken) {
                if (typeToken.getRawType() == Timestamp.class) {
                    final TypeAdapter<Date> dateTypeAdapter = gson.getAdapter(Date.class);
                    return (TypeAdapter<T>)new SqlTimestampTypeAdapter(dateTypeAdapter, null);
                }
                return null;
            }
        };
    }
}
