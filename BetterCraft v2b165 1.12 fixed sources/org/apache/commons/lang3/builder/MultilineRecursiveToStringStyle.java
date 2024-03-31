// 
// Decompiled by Procyon v0.6.0
// 

package org.apache.commons.lang3.builder;

import org.apache.commons.lang3.ClassUtils;
import org.apache.commons.lang3.SystemUtils;

public class MultilineRecursiveToStringStyle extends RecursiveToStringStyle
{
    private static final long serialVersionUID = 1L;
    private int indent;
    private int spaces;
    
    public MultilineRecursiveToStringStyle() {
        this.indent = 2;
        this.spaces = 2;
        this.resetIndent();
    }
    
    private void resetIndent() {
        this.setArrayStart("{" + SystemUtils.LINE_SEPARATOR + (Object)this.spacer(this.spaces));
        this.setArraySeparator("," + SystemUtils.LINE_SEPARATOR + (Object)this.spacer(this.spaces));
        this.setArrayEnd(SystemUtils.LINE_SEPARATOR + (Object)this.spacer(this.spaces - this.indent) + "}");
        this.setContentStart("[" + SystemUtils.LINE_SEPARATOR + (Object)this.spacer(this.spaces));
        this.setFieldSeparator("," + SystemUtils.LINE_SEPARATOR + (Object)this.spacer(this.spaces));
        this.setContentEnd(SystemUtils.LINE_SEPARATOR + (Object)this.spacer(this.spaces - this.indent) + "]");
    }
    
    private StringBuilder spacer(final int spaces) {
        final StringBuilder sb = new StringBuilder();
        for (int i = 0; i < spaces; ++i) {
            sb.append(" ");
        }
        return sb;
    }
    
    @Override
    public void appendDetail(final StringBuffer buffer, final String fieldName, final Object value) {
        if (!ClassUtils.isPrimitiveWrapper(value.getClass()) && !String.class.equals(value.getClass()) && this.accept(value.getClass())) {
            this.spaces += this.indent;
            this.resetIndent();
            buffer.append(ReflectionToStringBuilder.toString(value, this));
            this.spaces -= this.indent;
            this.resetIndent();
        }
        else {
            super.appendDetail(buffer, fieldName, value);
        }
    }
    
    @Override
    protected void appendDetail(final StringBuffer buffer, final String fieldName, final Object[] array) {
        this.spaces += this.indent;
        this.resetIndent();
        super.appendDetail(buffer, fieldName, array);
        this.spaces -= this.indent;
        this.resetIndent();
    }
    
    @Override
    protected void reflectionAppendArrayDetail(final StringBuffer buffer, final String fieldName, final Object array) {
        this.spaces += this.indent;
        this.resetIndent();
        super.appendDetail(buffer, fieldName, array);
        this.spaces -= this.indent;
        this.resetIndent();
    }
    
    @Override
    protected void appendDetail(final StringBuffer buffer, final String fieldName, final long[] array) {
        this.spaces += this.indent;
        this.resetIndent();
        super.appendDetail(buffer, fieldName, array);
        this.spaces -= this.indent;
        this.resetIndent();
    }
    
    @Override
    protected void appendDetail(final StringBuffer buffer, final String fieldName, final int[] array) {
        this.spaces += this.indent;
        this.resetIndent();
        super.appendDetail(buffer, fieldName, array);
        this.spaces -= this.indent;
        this.resetIndent();
    }
    
    @Override
    protected void appendDetail(final StringBuffer buffer, final String fieldName, final short[] array) {
        this.spaces += this.indent;
        this.resetIndent();
        super.appendDetail(buffer, fieldName, array);
        this.spaces -= this.indent;
        this.resetIndent();
    }
    
    @Override
    protected void appendDetail(final StringBuffer buffer, final String fieldName, final byte[] array) {
        this.spaces += this.indent;
        this.resetIndent();
        super.appendDetail(buffer, fieldName, array);
        this.spaces -= this.indent;
        this.resetIndent();
    }
    
    @Override
    protected void appendDetail(final StringBuffer buffer, final String fieldName, final char[] array) {
        this.spaces += this.indent;
        this.resetIndent();
        super.appendDetail(buffer, fieldName, array);
        this.spaces -= this.indent;
        this.resetIndent();
    }
    
    @Override
    protected void appendDetail(final StringBuffer buffer, final String fieldName, final double[] array) {
        this.spaces += this.indent;
        this.resetIndent();
        super.appendDetail(buffer, fieldName, array);
        this.spaces -= this.indent;
        this.resetIndent();
    }
    
    @Override
    protected void appendDetail(final StringBuffer buffer, final String fieldName, final float[] array) {
        this.spaces += this.indent;
        this.resetIndent();
        super.appendDetail(buffer, fieldName, array);
        this.spaces -= this.indent;
        this.resetIndent();
    }
    
    @Override
    protected void appendDetail(final StringBuffer buffer, final String fieldName, final boolean[] array) {
        this.spaces += this.indent;
        this.resetIndent();
        super.appendDetail(buffer, fieldName, array);
        this.spaces -= this.indent;
        this.resetIndent();
    }
}
