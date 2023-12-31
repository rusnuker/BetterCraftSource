// 
// Decompiled by Procyon v0.6.0
// 

package net.optifine.entity.model.anim;

import java.io.IOException;
import java.util.List;
import java.io.Reader;
import java.util.ArrayList;
import java.io.PushbackReader;
import java.io.StringReader;

public class TokenParser
{
    public static Token[] parse(final String str) throws IOException, ParseException {
        final Reader reader = new StringReader(str);
        final PushbackReader pushbackreader = new PushbackReader(reader);
        final List<Token> list = new ArrayList<Token>();
        while (true) {
            final int i = pushbackreader.read();
            if (i < 0) {
                final Token[] atoken = list.toArray(new Token[list.size()]);
                return atoken;
            }
            final char c0 = (char)i;
            if (Character.isWhitespace(c0)) {
                continue;
            }
            final EnumTokenType enumtokentype = EnumTokenType.getTypeByFirstChar(c0);
            if (enumtokentype == null) {
                throw new ParseException("Invalid character: '" + c0 + "', in: " + str);
            }
            final Token token = readToken(c0, enumtokentype, pushbackreader);
            list.add(token);
        }
    }
    
    private static Token readToken(final char chFirst, final EnumTokenType type, final PushbackReader pr) throws IOException {
        final StringBuffer stringbuffer = new StringBuffer();
        stringbuffer.append(chFirst);
        while (type.getMaxLen() <= 0 || stringbuffer.length() < type.getMaxLen()) {
            final int i = pr.read();
            if (i < 0) {
                break;
            }
            final char c0 = (char)i;
            if (!type.hasChar(c0)) {
                pr.unread(c0);
                break;
            }
            stringbuffer.append(c0);
        }
        return new Token(type, stringbuffer.toString());
    }
}
