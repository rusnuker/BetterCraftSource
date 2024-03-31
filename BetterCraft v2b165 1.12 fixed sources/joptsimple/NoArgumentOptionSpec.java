// 
// Decompiled by Procyon v0.6.0
// 

package joptsimple;

import java.util.List;
import java.util.Collections;

class NoArgumentOptionSpec extends AbstractOptionSpec<Void>
{
    NoArgumentOptionSpec(final String option) {
        this(Collections.singletonList(option), "");
    }
    
    NoArgumentOptionSpec(final List<String> options, final String description) {
        super(options, description);
    }
    
    @Override
    void handleOption(final OptionParser parser, final ArgumentList arguments, final OptionSet detectedOptions, final String detectedArgument) {
        detectedOptions.add(this);
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
    public boolean isRequired() {
        return false;
    }
    
    @Override
    public String argumentDescription() {
        return "";
    }
    
    @Override
    public String argumentTypeIndicator() {
        return "";
    }
    
    @Override
    protected Void convert(final String argument) {
        return null;
    }
    
    @Override
    public List<Void> defaultValues() {
        return Collections.emptyList();
    }
}
