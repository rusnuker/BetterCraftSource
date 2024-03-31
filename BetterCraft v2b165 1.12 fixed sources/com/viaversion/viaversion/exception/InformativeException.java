// 
// Decompiled by Procyon v0.6.0
// 

package com.viaversion.viaversion.exception;

import java.util.Iterator;
import java.util.HashMap;
import java.util.Map;

public class InformativeException extends Exception
{
    private final Map<String, Object> info;
    private int sources;
    
    public InformativeException(final Throwable cause) {
        super(cause);
        this.info = new HashMap<String, Object>();
    }
    
    public InformativeException set(final String key, final Object value) {
        this.info.put(key, value);
        return this;
    }
    
    public InformativeException addSource(final Class<?> sourceClazz) {
        return this.set("Source " + this.sources++, this.getSource(sourceClazz));
    }
    
    private String getSource(final Class<?> sourceClazz) {
        if (sourceClazz.isAnonymousClass()) {
            return sourceClazz.getName() + " (Anonymous)";
        }
        return sourceClazz.getName();
    }
    
    @Override
    public String getMessage() {
        final StringBuilder builder = new StringBuilder();
        builder.append("Please post this error to https://github.com/ViaVersion/ViaVersion/issues and follow the issue template\n{");
        int i = 0;
        for (final Map.Entry<String, Object> entry : this.info.entrySet()) {
            builder.append((i == 0) ? "" : ", ").append(entry.getKey()).append(": ").append(entry.getValue().toString());
            ++i;
        }
        builder.append("}\nActual Error: ");
        return builder.toString();
    }
    
    @Override
    public synchronized Throwable fillInStackTrace() {
        return this;
    }
}
