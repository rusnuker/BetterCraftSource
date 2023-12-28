// 
// Decompiled by Procyon v0.6.0
// 

package joptsimple;

import java.util.Collections;
import joptsimple.internal.Reflection;
import java.util.List;
import java.util.Arrays;

public class NonOptionArgumentSpec<V> extends AbstractOptionSpec<V>
{
    static final String NAME = "[arguments]";
    private ValueConverter<V> converter;
    private String argumentDescription;
    
    NonOptionArgumentSpec() {
        this("");
    }
    
    NonOptionArgumentSpec(final String description) {
        super(Arrays.asList("[arguments]"), description);
        this.argumentDescription = "";
    }
    
    public <T> NonOptionArgumentSpec<T> ofType(final Class<T> argumentType) {
        this.converter = Reflection.findConverter((Class<V>)argumentType);
        return (NonOptionArgumentSpec<T>)this;
    }
    
    public final <T> NonOptionArgumentSpec<T> withValuesConvertedBy(final ValueConverter<T> aConverter) {
        if (aConverter == null) {
            throw new NullPointerException("illegal null converter");
        }
        this.converter = (ValueConverter<V>)aConverter;
        return (NonOptionArgumentSpec<T>)this;
    }
    
    public NonOptionArgumentSpec<V> describedAs(final String description) {
        this.argumentDescription = description;
        return this;
    }
    
    @Override
    protected final V convert(final String argument) {
        return this.convertWith(this.converter, argument);
    }
    
    @Override
    void handleOption(final OptionParser parser, final ArgumentList arguments, final OptionSet detectedOptions, final String detectedArgument) {
        detectedOptions.addWithArgument(this, detectedArgument);
    }
    
    @Override
    public List<?> defaultValues() {
        return Collections.emptyList();
    }
    
    @Override
    public boolean isRequired() {
        return false;
    }
    
    @Override
    public boolean acceptsArguments() {
        return false;
    }
    
    @Override
    public boolean requiresArgument() {
        return false;
    }
    
    @Override
    public String argumentDescription() {
        return this.argumentDescription;
    }
    
    @Override
    public String argumentTypeIndicator() {
        return this.argumentTypeIndicatorFrom(this.converter);
    }
    
    @Override
    public boolean representsNonOptions() {
        return true;
    }
}
