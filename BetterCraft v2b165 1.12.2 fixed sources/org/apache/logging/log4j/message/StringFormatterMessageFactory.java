// 
// Decompiled by Procyon v0.6.0
// 

package org.apache.logging.log4j.message;

public final class StringFormatterMessageFactory extends AbstractMessageFactory
{
    public static final StringFormatterMessageFactory INSTANCE;
    private static final long serialVersionUID = -1626332412176965642L;
    
    @Override
    public Message newMessage(final String message, final Object... params) {
        return new StringFormattedMessage(message, params);
    }
    
    @Override
    public Message newMessage(final String message, final Object p0) {
        return new StringFormattedMessage(message, new Object[] { p0 });
    }
    
    @Override
    public Message newMessage(final String message, final Object p0, final Object p1) {
        return new StringFormattedMessage(message, new Object[] { p0, p1 });
    }
    
    @Override
    public Message newMessage(final String message, final Object p0, final Object p1, final Object p2) {
        return new StringFormattedMessage(message, new Object[] { p0, p1, p2 });
    }
    
    @Override
    public Message newMessage(final String message, final Object p0, final Object p1, final Object p2, final Object p3) {
        return new StringFormattedMessage(message, new Object[] { p0, p1, p2, p3 });
    }
    
    @Override
    public Message newMessage(final String message, final Object p0, final Object p1, final Object p2, final Object p3, final Object p4) {
        return new StringFormattedMessage(message, new Object[] { p0, p1, p2, p3, p4 });
    }
    
    @Override
    public Message newMessage(final String message, final Object p0, final Object p1, final Object p2, final Object p3, final Object p4, final Object p5) {
        return new StringFormattedMessage(message, new Object[] { p0, p1, p2, p3, p4, p5 });
    }
    
    @Override
    public Message newMessage(final String message, final Object p0, final Object p1, final Object p2, final Object p3, final Object p4, final Object p5, final Object p6) {
        return new StringFormattedMessage(message, new Object[] { p0, p1, p2, p3, p4, p5, p6 });
    }
    
    @Override
    public Message newMessage(final String message, final Object p0, final Object p1, final Object p2, final Object p3, final Object p4, final Object p5, final Object p6, final Object p7) {
        return new StringFormattedMessage(message, new Object[] { p0, p1, p2, p3, p4, p5, p6, p7 });
    }
    
    @Override
    public Message newMessage(final String message, final Object p0, final Object p1, final Object p2, final Object p3, final Object p4, final Object p5, final Object p6, final Object p7, final Object p8) {
        return new StringFormattedMessage(message, new Object[] { p0, p1, p2, p3, p4, p5, p6, p7, p8 });
    }
    
    @Override
    public Message newMessage(final String message, final Object p0, final Object p1, final Object p2, final Object p3, final Object p4, final Object p5, final Object p6, final Object p7, final Object p8, final Object p9) {
        return new StringFormattedMessage(message, new Object[] { p0, p1, p2, p3, p4, p5, p6, p7, p8, p9 });
    }
    
    static {
        INSTANCE = new StringFormatterMessageFactory();
    }
}
