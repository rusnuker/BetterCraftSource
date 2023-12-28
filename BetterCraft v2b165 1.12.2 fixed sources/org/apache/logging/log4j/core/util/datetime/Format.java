// 
// Decompiled by Procyon v0.6.0
// 

package org.apache.logging.log4j.core.util.datetime;

import java.text.ParseException;
import java.text.ParsePosition;
import java.text.FieldPosition;

public abstract class Format
{
    public final String format(final Object obj) {
        return this.format(obj, new StringBuilder(), new FieldPosition(0)).toString();
    }
    
    public abstract StringBuilder format(final Object p0, final StringBuilder p1, final FieldPosition p2);
    
    public abstract Object parseObject(final String p0, final ParsePosition p1);
    
    public Object parseObject(final String source) throws ParseException {
        final ParsePosition pos = new ParsePosition(0);
        final Object result = this.parseObject(source, pos);
        if (pos.getIndex() == 0) {
            throw new ParseException("Format.parseObject(String) failed", pos.getErrorIndex());
        }
        return result;
    }
}
