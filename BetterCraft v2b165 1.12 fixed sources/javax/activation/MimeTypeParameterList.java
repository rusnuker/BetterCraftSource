// 
// Decompiled by Procyon v0.6.0
// 

package javax.activation;

import java.util.Iterator;
import java.util.Collection;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

public class MimeTypeParameterList
{
    private final Map params;
    
    public MimeTypeParameterList() {
        this.params = new HashMap();
    }
    
    public MimeTypeParameterList(final String parameterList) throws MimeTypeParseException {
        this.params = new HashMap();
        this.parse(parameterList);
    }
    
    protected void parse(final String parameterList) throws MimeTypeParseException {
        if (parameterList == null) {
            throw new MimeTypeParseException("parameterList is null");
        }
        final RFC2045Parser parser = new RFC2045Parser(parameterList, null);
        while (parser.hasMoreParams()) {
            final String attribute = parser.expectAttribute();
            parser.expectEquals();
            final String value = parser.expectValue();
            this.params.put(attribute.toLowerCase(), value);
        }
    }
    
    public int size() {
        return this.params.size();
    }
    
    public boolean isEmpty() {
        return this.params.isEmpty();
    }
    
    public String get(final String name) {
        return this.params.get(name.toLowerCase());
    }
    
    public void set(final String name, final String value) {
        this.params.put(name.toLowerCase(), value);
    }
    
    public void remove(final String name) {
        this.params.remove(name.toLowerCase());
    }
    
    public Enumeration getNames() {
        return Collections.enumeration((Collection<Object>)this.params.keySet());
    }
    
    @Override
    public String toString() {
        final StringBuffer buf = new StringBuffer(this.params.size() << 4);
        for (final Map.Entry entry : this.params.entrySet()) {
            buf.append("; ").append(entry.getKey()).append('=');
            this.quote(buf, entry.getValue());
        }
        return buf.toString();
    }
    
    private void quote(final StringBuffer buf, final String value) {
        final int length = value.length();
        boolean quote = false;
        for (int i = 0; i < length; ++i) {
            if (MimeType.isSpecial(value.charAt(i))) {
                quote = true;
                break;
            }
        }
        if (quote) {
            buf.append('\"');
            for (int i = 0; i < length; ++i) {
                final char c = value.charAt(i);
                if (c == '\\' || c == '\"') {
                    buf.append('\\');
                }
                buf.append(c);
            }
            buf.append('\"');
        }
        else {
            buf.append(value);
        }
    }
    
    private static class RFC2045Parser
    {
        private final String text;
        private int index;
        
        private RFC2045Parser(final String text) {
            this.index = 0;
            this.text = text;
        }
        
        private boolean hasMoreParams() throws MimeTypeParseException {
            while (this.index != this.text.length()) {
                final char c = this.text.charAt(this.index++);
                if (!Character.isWhitespace(c)) {
                    if (c != ';') {
                        throw new MimeTypeParseException("Expected \";\" at " + (this.index - 1) + " in " + this.text);
                    }
                    return true;
                }
            }
            return false;
        }
        
        private String expectAttribute() throws MimeTypeParseException {
            while (this.index != this.text.length()) {
                final char c = this.text.charAt(this.index++);
                if (!Character.isWhitespace(c)) {
                    final int start = this.index - 1;
                    while (this.index != this.text.length() && !MimeType.isSpecial(this.text.charAt(this.index))) {
                        ++this.index;
                    }
                    return this.text.substring(start, this.index);
                }
            }
            throw new MimeTypeParseException("Expected attribute at " + (this.index - 1) + " in " + this.text);
        }
        
        private void expectEquals() throws MimeTypeParseException {
            while (this.index != this.text.length()) {
                final char c = this.text.charAt(this.index++);
                if (!Character.isWhitespace(c)) {
                    if (c != '=') {
                        throw new MimeTypeParseException("Expected \"=\" at " + (this.index - 1) + " in " + this.text);
                    }
                    return;
                }
            }
            throw new MimeTypeParseException("Expected \"=\" at " + (this.index - 1) + " in " + this.text);
        }
        
        private String expectValue() throws MimeTypeParseException {
            while (this.index != this.text.length()) {
                char c = this.text.charAt(this.index++);
                if (!Character.isWhitespace(c)) {
                    if (c == '\"') {
                        final StringBuffer buf = new StringBuffer();
                        while (this.index != this.text.length()) {
                            c = this.text.charAt(this.index++);
                            if (c == '\"') {
                                return buf.toString();
                            }
                            if (c == '\\') {
                                if (this.index == this.text.length()) {
                                    throw new MimeTypeParseException("Expected escaped char at " + (this.index - 1) + " in " + this.text);
                                }
                                c = this.text.charAt(this.index++);
                            }
                            buf.append(c);
                        }
                        throw new MimeTypeParseException("Expected closing quote at " + (this.index - 1) + " in " + this.text);
                    }
                    final int start = this.index - 1;
                    while (this.index != this.text.length() && !MimeType.isSpecial(this.text.charAt(this.index))) {
                        ++this.index;
                    }
                    return this.text.substring(start, this.index);
                }
            }
            throw new MimeTypeParseException("Expected value at " + (this.index - 1) + " in " + this.text);
        }
    }
}
