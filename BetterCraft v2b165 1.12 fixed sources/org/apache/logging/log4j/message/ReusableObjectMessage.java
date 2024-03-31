// 
// Decompiled by Procyon v0.6.0
// 

package org.apache.logging.log4j.message;

import org.apache.logging.log4j.util.StringBuilderFormattable;
import org.apache.logging.log4j.util.PerformanceSensitive;

@PerformanceSensitive({ "allocation" })
public class ReusableObjectMessage implements ReusableMessage
{
    private static final long serialVersionUID = 6922476812535519960L;
    private transient Object obj;
    private transient String objectString;
    
    public void set(final Object object) {
        this.obj = object;
    }
    
    @Override
    public String getFormattedMessage() {
        return String.valueOf(this.obj);
    }
    
    @Override
    public void formatTo(final StringBuilder buffer) {
        if (this.obj == null || this.obj instanceof String) {
            buffer.append((String)this.obj);
        }
        else if (this.obj instanceof StringBuilderFormattable) {
            ((StringBuilderFormattable)this.obj).formatTo(buffer);
        }
        else if (this.obj instanceof CharSequence) {
            buffer.append((CharSequence)this.obj);
        }
        else if (this.obj instanceof Integer) {
            buffer.append((int)this.obj);
        }
        else if (this.obj instanceof Long) {
            buffer.append((long)this.obj);
        }
        else if (this.obj instanceof Double) {
            buffer.append((double)this.obj);
        }
        else if (this.obj instanceof Boolean) {
            buffer.append((boolean)this.obj);
        }
        else if (this.obj instanceof Character) {
            buffer.append((char)this.obj);
        }
        else if (this.obj instanceof Short) {
            buffer.append((short)this.obj);
        }
        else if (this.obj instanceof Float) {
            buffer.append((float)this.obj);
        }
        else {
            buffer.append(this.obj);
        }
    }
    
    @Override
    public String getFormat() {
        return this.getFormattedMessage();
    }
    
    public Object getParameter() {
        return this.obj;
    }
    
    @Override
    public Object[] getParameters() {
        return new Object[] { this.obj };
    }
    
    @Override
    public String toString() {
        return this.getFormattedMessage();
    }
    
    @Override
    public Throwable getThrowable() {
        return (this.obj instanceof Throwable) ? ((Throwable)this.obj) : null;
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
        return new ObjectMessage(this.obj);
    }
}
