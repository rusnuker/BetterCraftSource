// 
// Decompiled by Procyon v0.6.0
// 

package org.yaml.snakeyaml.error;

import org.yaml.snakeyaml.scanner.Constant;
import java.io.Serializable;

public final class Mark implements Serializable
{
    private final String name;
    private final int index;
    private final int line;
    private final int column;
    private final int[] buffer;
    private final int pointer;
    
    private static int[] toCodePoints(final char[] str) {
        final int[] codePoints = new int[Character.codePointCount(str, 0, str.length)];
        int cp;
        for (int i = 0, c = 0; i < str.length; i += Character.charCount(cp), ++c) {
            cp = Character.codePointAt(str, i);
            codePoints[c] = cp;
        }
        return codePoints;
    }
    
    public Mark(final String name, final int index, final int line, final int column, final char[] str, final int pointer) {
        this(name, index, line, column, toCodePoints(str), pointer);
    }
    
    public Mark(final String name, final int index, final int line, final int column, final int[] buffer, final int pointer) {
        this.name = name;
        this.index = index;
        this.line = line;
        this.column = column;
        this.buffer = buffer;
        this.pointer = pointer;
    }
    
    private boolean isLineBreak(final int c) {
        return Constant.NULL_OR_LINEBR.has(c);
    }
    
    public String get_snippet(final int indent, final int max_length) {
        final float half = max_length / 2.0f - 1.0f;
        int start = this.pointer;
        String head = "";
        while (start > 0 && !this.isLineBreak(this.buffer[start - 1])) {
            --start;
            if (this.pointer - start > half) {
                head = " ... ";
                start += 5;
                break;
            }
        }
        String tail = "";
        int end = this.pointer;
        while (end < this.buffer.length && !this.isLineBreak(this.buffer[end])) {
            if (++end - this.pointer > half) {
                tail = " ... ";
                end -= 5;
                break;
            }
        }
        final StringBuilder result = new StringBuilder();
        for (int i = 0; i < indent; ++i) {
            result.append(" ");
        }
        result.append(head);
        for (int i = start; i < end; ++i) {
            result.appendCodePoint(this.buffer[i]);
        }
        result.append(tail);
        result.append("\n");
        for (int i = 0; i < indent + this.pointer - start + head.length(); ++i) {
            result.append(" ");
        }
        result.append("^");
        return result.toString();
    }
    
    public String get_snippet() {
        return this.get_snippet(4, 75);
    }
    
    @Override
    public String toString() {
        final String snippet = this.get_snippet();
        final String builder = " in " + this.name + ", line " + (this.line + 1) + ", column " + (this.column + 1) + ":\n" + snippet;
        return builder;
    }
    
    public String getName() {
        return this.name;
    }
    
    public int getLine() {
        return this.line;
    }
    
    public int getColumn() {
        return this.column;
    }
    
    public int getIndex() {
        return this.index;
    }
    
    public int[] getBuffer() {
        return this.buffer;
    }
    
    public int getPointer() {
        return this.pointer;
    }
}
