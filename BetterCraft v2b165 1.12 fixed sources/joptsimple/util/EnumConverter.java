// 
// Decompiled by Procyon v0.6.0
// 

package joptsimple.util;

import java.text.MessageFormat;
import java.util.ResourceBundle;
import java.util.Iterator;
import java.util.EnumSet;
import joptsimple.ValueConversionException;
import joptsimple.ValueConverter;

public abstract class EnumConverter<E extends Enum<E>> implements ValueConverter<E>
{
    private final Class<E> clazz;
    private String delimiters;
    
    protected EnumConverter(final Class<E> clazz) {
        this.delimiters = "[,]";
        this.clazz = clazz;
    }
    
    @Override
    public E convert(final String value) {
        for (final E each : this.valueType().getEnumConstants()) {
            if (each.name().equalsIgnoreCase(value)) {
                return each;
            }
        }
        throw new ValueConversionException(this.message(value));
    }
    
    @Override
    public Class<E> valueType() {
        return this.clazz;
    }
    
    public void setDelimiters(final String delimiters) {
        this.delimiters = delimiters;
    }
    
    @Override
    public String valuePattern() {
        final EnumSet<E> values = EnumSet.allOf(this.valueType());
        final StringBuilder builder = new StringBuilder();
        builder.append(this.delimiters.charAt(0));
        final Iterator<E> i = values.iterator();
        while (i.hasNext()) {
            builder.append(i.next().toString());
            if (i.hasNext()) {
                builder.append(this.delimiters.charAt(1));
            }
        }
        builder.append(this.delimiters.charAt(2));
        return builder.toString();
    }
    
    private String message(final String value) {
        final ResourceBundle bundle = ResourceBundle.getBundle("joptsimple.ExceptionMessages");
        final Object[] arguments = { value, this.valuePattern() };
        final String template = bundle.getString(EnumConverter.class.getName() + ".message");
        return new MessageFormat(template).format(arguments);
    }
}
