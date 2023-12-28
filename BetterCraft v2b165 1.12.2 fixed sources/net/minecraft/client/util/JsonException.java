// 
// Decompiled by Procyon v0.6.0
// 

package net.minecraft.client.util;

import org.apache.commons.lang3.StringUtils;
import java.io.FileNotFoundException;
import com.google.common.collect.Lists;
import java.util.List;
import java.io.IOException;

public class JsonException extends IOException
{
    private final List<Entry> entries;
    private final String message;
    
    public JsonException(final String messageIn) {
        (this.entries = (List<Entry>)Lists.newArrayList()).add(new Entry(null));
        this.message = messageIn;
    }
    
    public JsonException(final String messageIn, final Throwable cause) {
        super(cause);
        (this.entries = (List<Entry>)Lists.newArrayList()).add(new Entry(null));
        this.message = messageIn;
    }
    
    public void prependJsonKey(final String p_151380_1_) {
        this.entries.get(0).addJsonKey(p_151380_1_);
    }
    
    public void setFilenameAndFlush(final String p_151381_1_) {
        Entry.access$2(this.entries.get(0), p_151381_1_);
        this.entries.add(0, new Entry(null));
    }
    
    @Override
    public String getMessage() {
        return "Invalid " + this.entries.get(this.entries.size() - 1) + ": " + this.message;
    }
    
    public static JsonException forException(final Exception p_151379_0_) {
        if (p_151379_0_ instanceof JsonException) {
            return (JsonException)p_151379_0_;
        }
        String s = p_151379_0_.getMessage();
        if (p_151379_0_ instanceof FileNotFoundException) {
            s = "File not found";
        }
        return new JsonException(s, p_151379_0_);
    }
    
    public static class Entry
    {
        private String filename;
        private final List<String> jsonKeys;
        
        private Entry() {
            this.jsonKeys = (List<String>)Lists.newArrayList();
        }
        
        private void addJsonKey(final String p_151373_1_) {
            this.jsonKeys.add(0, p_151373_1_);
        }
        
        public String getJsonKeys() {
            return StringUtils.join(this.jsonKeys, "->");
        }
        
        @Override
        public String toString() {
            if (this.filename != null) {
                return this.jsonKeys.isEmpty() ? this.filename : (String.valueOf(this.filename) + " " + this.getJsonKeys());
            }
            return this.jsonKeys.isEmpty() ? "(Unknown file)" : ("(Unknown file) " + this.getJsonKeys());
        }
        
        static /* synthetic */ void access$2(final Entry entry, final String filename) {
            entry.filename = filename;
        }
    }
}
