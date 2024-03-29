// 
// Decompiled by Procyon v0.6.0
// 

package com.viaversion.viaversion.libs.gson.internal;

import java.util.Objects;
import java.io.Writer;
import com.viaversion.viaversion.libs.gson.stream.JsonWriter;
import com.viaversion.viaversion.libs.gson.JsonParseException;
import java.io.IOException;
import com.viaversion.viaversion.libs.gson.JsonIOException;
import com.viaversion.viaversion.libs.gson.stream.MalformedJsonException;
import java.io.EOFException;
import com.viaversion.viaversion.libs.gson.JsonSyntaxException;
import com.viaversion.viaversion.libs.gson.JsonNull;
import com.viaversion.viaversion.libs.gson.internal.bind.TypeAdapters;
import com.viaversion.viaversion.libs.gson.JsonElement;
import com.viaversion.viaversion.libs.gson.stream.JsonReader;

public final class Streams
{
    private Streams() {
        throw new UnsupportedOperationException();
    }
    
    public static JsonElement parse(final JsonReader reader) throws JsonParseException {
        boolean isEmpty = true;
        try {
            reader.peek();
            isEmpty = false;
            return TypeAdapters.JSON_ELEMENT.read(reader);
        }
        catch (final EOFException e) {
            if (isEmpty) {
                return JsonNull.INSTANCE;
            }
            throw new JsonSyntaxException(e);
        }
        catch (final MalformedJsonException e2) {
            throw new JsonSyntaxException(e2);
        }
        catch (final IOException e3) {
            throw new JsonIOException(e3);
        }
        catch (final NumberFormatException e4) {
            throw new JsonSyntaxException(e4);
        }
    }
    
    public static void write(final JsonElement element, final JsonWriter writer) throws IOException {
        TypeAdapters.JSON_ELEMENT.write(writer, element);
    }
    
    public static Writer writerForAppendable(final Appendable appendable) {
        return (appendable instanceof Writer) ? ((Writer)appendable) : new AppendableWriter(appendable);
    }
    
    private static final class AppendableWriter extends Writer
    {
        private final Appendable appendable;
        private final CurrentWrite currentWrite;
        
        AppendableWriter(final Appendable appendable) {
            this.currentWrite = new CurrentWrite();
            this.appendable = appendable;
        }
        
        @Override
        public void write(final char[] chars, final int offset, final int length) throws IOException {
            this.currentWrite.setChars(chars);
            this.appendable.append(this.currentWrite, offset, offset + length);
        }
        
        @Override
        public void flush() {
        }
        
        @Override
        public void close() {
        }
        
        @Override
        public void write(final int i) throws IOException {
            this.appendable.append((char)i);
        }
        
        @Override
        public void write(final String str, final int off, final int len) throws IOException {
            Objects.requireNonNull(str);
            this.appendable.append(str, off, off + len);
        }
        
        @Override
        public Writer append(final CharSequence csq) throws IOException {
            this.appendable.append(csq);
            return this;
        }
        
        @Override
        public Writer append(final CharSequence csq, final int start, final int end) throws IOException {
            this.appendable.append(csq, start, end);
            return this;
        }
        
        private static class CurrentWrite implements CharSequence
        {
            private char[] chars;
            private String cachedString;
            
            void setChars(final char[] chars) {
                this.chars = chars;
                this.cachedString = null;
            }
            
            @Override
            public int length() {
                return this.chars.length;
            }
            
            @Override
            public char charAt(final int i) {
                return this.chars[i];
            }
            
            @Override
            public CharSequence subSequence(final int start, final int end) {
                return new String(this.chars, start, end - start);
            }
            
            @Override
            public String toString() {
                if (this.cachedString == null) {
                    this.cachedString = new String(this.chars);
                }
                return this.cachedString;
            }
        }
    }
}
