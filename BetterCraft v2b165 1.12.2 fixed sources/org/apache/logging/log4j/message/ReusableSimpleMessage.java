// 
// Decompiled by Procyon v0.6.0
// 

package org.apache.logging.log4j.message;

import org.apache.logging.log4j.util.PerformanceSensitive;

@PerformanceSensitive({ "allocation" })
public class ReusableSimpleMessage implements ReusableMessage, CharSequence
{
    private static final long serialVersionUID = -9199974506498249809L;
    private static Object[] EMPTY_PARAMS;
    private CharSequence charSequence;
    
    public void set(final String message) {
        this.charSequence = message;
    }
    
    public void set(final CharSequence charSequence) {
        this.charSequence = charSequence;
    }
    
    @Override
    public String getFormattedMessage() {
        return String.valueOf(this.charSequence);
    }
    
    @Override
    public String getFormat() {
        return this.getFormattedMessage();
    }
    
    @Override
    public Object[] getParameters() {
        return ReusableSimpleMessage.EMPTY_PARAMS;
    }
    
    @Override
    public Throwable getThrowable() {
        return null;
    }
    
    @Override
    public void formatTo(final StringBuilder buffer) {
        buffer.append(this.charSequence);
    }
    
    @Override
    public Object[] swapParameters(final Object[] emptyReplacement) {
        return emptyReplacement;
    }
    
    @Override
    public short getParameterCount() {
        return 0;
    }
    
    @Override
    public Message memento() {
        return new SimpleMessage(this.charSequence);
    }
    
    @Override
    public int length() {
        return (this.charSequence == null) ? 0 : this.charSequence.length();
    }
    
    @Override
    public char charAt(final int index) {
        return this.charSequence.charAt(index);
    }
    
    @Override
    public CharSequence subSequence(final int start, final int end) {
        return this.charSequence.subSequence(start, end);
    }
    
    static {
        ReusableSimpleMessage.EMPTY_PARAMS = new Object[0];
    }
}
