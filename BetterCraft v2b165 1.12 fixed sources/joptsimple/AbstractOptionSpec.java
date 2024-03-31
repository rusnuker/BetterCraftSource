// 
// Decompiled by Procyon v0.6.0
// 

package joptsimple;

import java.util.Iterator;
import java.util.Collection;
import joptsimple.internal.ReflectionException;
import joptsimple.internal.Reflection;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public abstract class AbstractOptionSpec<V> implements OptionSpec<V>, OptionDescriptor
{
    private final List<String> options;
    private final String description;
    private boolean forHelp;
    
    AbstractOptionSpec(final String option) {
        this(Collections.singletonList(option), "");
    }
    
    AbstractOptionSpec(final List<String> options, final String description) {
        this.options = new ArrayList<String>();
        this.arrangeOptions(options);
        this.description = description;
    }
    
    @Override
    public final List<String> options() {
        return Collections.unmodifiableList((List<? extends String>)this.options);
    }
    
    @Override
    public final List<V> values(final OptionSet detectedOptions) {
        return detectedOptions.valuesOf((OptionSpec<V>)this);
    }
    
    @Override
    public final V value(final OptionSet detectedOptions) {
        return detectedOptions.valueOf((OptionSpec<V>)this);
    }
    
    @Override
    public String description() {
        return this.description;
    }
    
    public final AbstractOptionSpec<V> forHelp() {
        this.forHelp = true;
        return this;
    }
    
    @Override
    public final boolean isForHelp() {
        return this.forHelp;
    }
    
    @Override
    public boolean representsNonOptions() {
        return false;
    }
    
    protected abstract V convert(final String p0);
    
    protected V convertWith(final ValueConverter<V> converter, final String argument) {
        try {
            return Reflection.convertWith(converter, argument);
        }
        catch (final ReflectionException | ValueConversionException ex) {
            throw new OptionArgumentConversionException(this, argument, ex);
        }
    }
    
    protected String argumentTypeIndicatorFrom(final ValueConverter<V> converter) {
        if (converter == null) {
            return null;
        }
        final String pattern = converter.valuePattern();
        return (pattern == null) ? converter.valueType().getName() : pattern;
    }
    
    abstract void handleOption(final OptionParser p0, final ArgumentList p1, final OptionSet p2, final String p3);
    
    private void arrangeOptions(final List<String> unarranged) {
        if (unarranged.size() == 1) {
            this.options.addAll(unarranged);
            return;
        }
        final List<String> shortOptions = new ArrayList<String>();
        final List<String> longOptions = new ArrayList<String>();
        for (final String each : unarranged) {
            if (each.length() == 1) {
                shortOptions.add(each);
            }
            else {
                longOptions.add(each);
            }
        }
        Collections.sort(shortOptions);
        Collections.sort(longOptions);
        this.options.addAll(shortOptions);
        this.options.addAll(longOptions);
    }
    
    @Override
    public boolean equals(final Object that) {
        if (!(that instanceof AbstractOptionSpec)) {
            return false;
        }
        final AbstractOptionSpec<?> other = (AbstractOptionSpec<?>)that;
        return this.options.equals(other.options);
    }
    
    @Override
    public int hashCode() {
        return this.options.hashCode();
    }
    
    @Override
    public String toString() {
        return this.options.toString();
    }
}
