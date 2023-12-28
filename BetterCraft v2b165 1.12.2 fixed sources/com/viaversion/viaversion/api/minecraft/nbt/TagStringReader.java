// 
// Decompiled by Procyon v0.6.0
// 

package com.viaversion.viaversion.api.minecraft.nbt;

import com.viaversion.viaversion.libs.opennbt.tag.builtin.DoubleTag;
import com.viaversion.viaversion.libs.opennbt.tag.builtin.FloatTag;
import com.viaversion.viaversion.libs.opennbt.tag.builtin.LongTag;
import com.viaversion.viaversion.libs.opennbt.tag.builtin.ShortTag;
import com.viaversion.viaversion.libs.opennbt.tag.builtin.ByteTag;
import com.viaversion.viaversion.libs.opennbt.tag.builtin.StringTag;
import com.viaversion.viaversion.libs.opennbt.tag.builtin.NumberTag;
import com.viaversion.viaversion.libs.opennbt.tag.builtin.IntTag;
import java.util.stream.IntStream;
import java.util.List;
import java.util.ArrayList;
import com.viaversion.viaversion.libs.opennbt.tag.builtin.LongArrayTag;
import com.viaversion.viaversion.libs.opennbt.tag.builtin.IntArrayTag;
import com.viaversion.viaversion.libs.opennbt.tag.builtin.ByteArrayTag;
import com.viaversion.viaversion.libs.opennbt.tag.builtin.ListTag;
import com.viaversion.viaversion.libs.opennbt.tag.builtin.Tag;
import com.viaversion.viaversion.libs.opennbt.tag.builtin.CompoundTag;

final class TagStringReader
{
    private final CharBuffer buffer;
    
    public TagStringReader(final CharBuffer buffer) {
        this.buffer = buffer;
    }
    
    public CompoundTag compound() throws StringTagParseException {
        this.buffer.expect('{');
        final CompoundTag compoundTag = new CompoundTag();
        if (this.buffer.peek() == '}') {
            this.buffer.take();
            return compoundTag;
        }
        while (this.buffer.hasMore()) {
            final String key = this.key();
            final Tag tag = this.tag();
            compoundTag.put(key, tag);
            if (this.separatorOrCompleteWith('}')) {
                return compoundTag;
            }
        }
        throw this.buffer.makeError("Unterminated compound tag!");
    }
    
    public ListTag list() throws StringTagParseException {
        final ListTag listTag = new ListTag();
        this.buffer.expect('[');
        final boolean prefixedIndex = this.buffer.peek() == '0' && this.buffer.peek(1) == ':';
        while (this.buffer.hasMore()) {
            if (this.buffer.peek() == ']') {
                this.buffer.advance();
                return listTag;
            }
            if (prefixedIndex) {
                this.buffer.takeUntil(':');
            }
            final Tag next = this.tag();
            listTag.add(next);
            if (this.separatorOrCompleteWith(']')) {
                return listTag;
            }
        }
        throw this.buffer.makeError("Reached end of file without end of list tag!");
    }
    
    public Tag array(final char elementType) throws StringTagParseException {
        this.buffer.expect('[').expect(elementType).expect(';');
        if (elementType == 'B') {
            return new ByteArrayTag(this.byteArray());
        }
        if (elementType == 'I') {
            return new IntArrayTag(this.intArray());
        }
        if (elementType == 'L') {
            return new LongArrayTag(this.longArray());
        }
        throw this.buffer.makeError("Type " + elementType + " is not a valid element type in an array!");
    }
    
    private byte[] byteArray() throws StringTagParseException {
        final List<Byte> bytes = new ArrayList<Byte>();
        while (this.buffer.hasMore()) {
            final CharSequence value = this.buffer.skipWhitespace().takeUntil('B');
            try {
                bytes.add(Byte.valueOf(value.toString()));
            }
            catch (final NumberFormatException ex) {
                throw this.buffer.makeError("All elements of a byte array must be bytes!");
            }
            if (this.separatorOrCompleteWith(']')) {
                final byte[] result = new byte[bytes.size()];
                for (int i = 0; i < bytes.size(); ++i) {
                    result[i] = bytes.get(i);
                }
                return result;
            }
        }
        throw this.buffer.makeError("Reached end of document without array close");
    }
    
    private int[] intArray() throws StringTagParseException {
        final IntStream.Builder builder = IntStream.builder();
        while (this.buffer.hasMore()) {
            final Tag value = this.tag();
            if (!(value instanceof IntTag)) {
                throw this.buffer.makeError("All elements of an int array must be ints!");
            }
            builder.add(((NumberTag)value).asInt());
            if (this.separatorOrCompleteWith(']')) {
                return builder.build().toArray();
            }
        }
        throw this.buffer.makeError("Reached end of document without array close");
    }
    
    private long[] longArray() throws StringTagParseException {
        final List<Long> longs = new ArrayList<Long>();
        while (this.buffer.hasMore()) {
            final CharSequence value = this.buffer.skipWhitespace().takeUntil('L');
            try {
                longs.add(Long.valueOf(value.toString()));
            }
            catch (final NumberFormatException ex) {
                throw this.buffer.makeError("All elements of a long array must be longs!");
            }
            if (this.separatorOrCompleteWith(']')) {
                final long[] result = new long[longs.size()];
                for (int i = 0; i < longs.size(); ++i) {
                    result[i] = longs.get(i);
                }
                return result;
            }
        }
        throw this.buffer.makeError("Reached end of document without array close");
    }
    
    public String key() throws StringTagParseException {
        this.buffer.skipWhitespace();
        final char starChar = this.buffer.peek();
        try {
            if (starChar == '\'' || starChar == '\"') {
                return unescape(this.buffer.takeUntil(this.buffer.take()).toString());
            }
            final StringBuilder builder = new StringBuilder();
            while (this.buffer.peek() != ':') {
                builder.append(this.buffer.take());
            }
            return builder.toString();
        }
        finally {
            this.buffer.expect(':');
        }
    }
    
    public Tag tag() throws StringTagParseException {
        final char startToken = this.buffer.skipWhitespace().peek();
        switch (startToken) {
            case '{': {
                return this.compound();
            }
            case '[': {
                if (this.buffer.peek(2) == ';') {
                    return this.array(this.buffer.peek(1));
                }
                return this.list();
            }
            case '\"':
            case '\'': {
                this.buffer.advance();
                return new StringTag(unescape(this.buffer.takeUntil(startToken).toString()));
            }
            default: {
                return this.scalar();
            }
        }
    }
    
    private Tag scalar() {
        final StringBuilder builder = new StringBuilder();
        boolean possiblyNumeric = true;
        while (this.buffer.hasMore()) {
            final char current = this.buffer.peek();
            if (possiblyNumeric && !Tokens.numeric(current) && builder.length() != 0) {
                Tag result = null;
                try {
                    switch (Character.toUpperCase(current)) {
                        case 'B': {
                            result = new ByteTag(Byte.parseByte(builder.toString()));
                            break;
                        }
                        case 'S': {
                            result = new ShortTag(Short.parseShort(builder.toString()));
                            break;
                        }
                        case 'L': {
                            result = new LongTag(Long.parseLong(builder.toString()));
                            break;
                        }
                        case 'F': {
                            result = new FloatTag(Float.parseFloat(builder.toString()));
                            break;
                        }
                        case 'D': {
                            result = new DoubleTag(Double.parseDouble(builder.toString()));
                            break;
                        }
                    }
                }
                catch (final NumberFormatException ex) {
                    possiblyNumeric = false;
                }
                if (result != null) {
                    this.buffer.take();
                    return result;
                }
            }
            if (current == '\\') {
                this.buffer.advance();
                builder.append(this.buffer.take());
            }
            else {
                if (!Tokens.id(current)) {
                    break;
                }
                builder.append(this.buffer.take());
            }
        }
        final String built = builder.toString();
        if (possiblyNumeric) {
            try {
                return new IntTag(Integer.parseInt(built));
            }
            catch (final NumberFormatException ex2) {}
        }
        return new StringTag(built);
    }
    
    private boolean separatorOrCompleteWith(final char endCharacter) throws StringTagParseException {
        if (this.buffer.skipWhitespace().peek() == endCharacter) {
            this.buffer.take();
            return true;
        }
        this.buffer.expect(',');
        if (this.buffer.skipWhitespace().peek() == endCharacter) {
            this.buffer.take();
            return true;
        }
        return false;
    }
    
    private static String unescape(final String withEscapes) {
        int escapeIdx = withEscapes.indexOf(92);
        if (escapeIdx == -1) {
            return withEscapes;
        }
        int lastEscape = 0;
        final StringBuilder output = new StringBuilder(withEscapes.length());
        do {
            output.append(withEscapes, lastEscape, escapeIdx);
            lastEscape = escapeIdx + 1;
        } while ((escapeIdx = withEscapes.indexOf(92, lastEscape + 1)) != -1);
        output.append(withEscapes.substring(lastEscape));
        return output.toString();
    }
}
