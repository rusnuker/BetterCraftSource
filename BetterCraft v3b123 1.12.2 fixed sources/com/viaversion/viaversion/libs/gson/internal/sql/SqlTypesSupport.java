// 
// Decompiled by Procyon v0.6.0
// 

package com.viaversion.viaversion.libs.gson.internal.sql;

import java.sql.Timestamp;
import com.viaversion.viaversion.libs.gson.TypeAdapterFactory;
import java.util.Date;
import com.viaversion.viaversion.libs.gson.internal.bind.DefaultDateTypeAdapter;

public final class SqlTypesSupport
{
    public static final boolean SUPPORTS_SQL_TYPES;
    public static final DefaultDateTypeAdapter.DateType<? extends Date> DATE_DATE_TYPE;
    public static final DefaultDateTypeAdapter.DateType<? extends Date> TIMESTAMP_DATE_TYPE;
    public static final TypeAdapterFactory DATE_FACTORY;
    public static final TypeAdapterFactory TIME_FACTORY;
    public static final TypeAdapterFactory TIMESTAMP_FACTORY;
    
    private SqlTypesSupport() {
    }
    
    static {
        boolean sqlTypesSupport;
        try {
            Class.forName("java.sql.Date");
            sqlTypesSupport = true;
        }
        catch (final ClassNotFoundException classNotFoundException) {
            sqlTypesSupport = false;
        }
        SUPPORTS_SQL_TYPES = sqlTypesSupport;
        if (SqlTypesSupport.SUPPORTS_SQL_TYPES) {
            DATE_DATE_TYPE = new DefaultDateTypeAdapter.DateType<java.sql.Date>() {
                @Override
                protected java.sql.Date deserialize(final Date date) {
                    return new java.sql.Date(date.getTime());
                }
            };
            TIMESTAMP_DATE_TYPE = new DefaultDateTypeAdapter.DateType<Timestamp>() {
                @Override
                protected Timestamp deserialize(final Date date) {
                    return new Timestamp(date.getTime());
                }
            };
            DATE_FACTORY = SqlDateTypeAdapter.FACTORY;
            TIME_FACTORY = SqlTimeTypeAdapter.FACTORY;
            TIMESTAMP_FACTORY = SqlTimestampTypeAdapter.FACTORY;
        }
        else {
            DATE_DATE_TYPE = null;
            TIMESTAMP_DATE_TYPE = null;
            DATE_FACTORY = null;
            TIME_FACTORY = null;
            TIMESTAMP_FACTORY = null;
        }
    }
}
