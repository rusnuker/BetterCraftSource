// 
// Decompiled by Procyon v0.6.0
// 

package joptsimple;

import java.util.List;

class RequiredArgumentOptionSpec<V> extends ArgumentAcceptingOptionSpec<V>
{
    RequiredArgumentOptionSpec(final String option) {
        super(option, true);
    }
    
    RequiredArgumentOptionSpec(final List<String> options, final String description) {
        super(options, true, description);
    }
    
    @Override
    protected void detectOptionArgument(final OptionParser parser, final ArgumentList arguments, final OptionSet detectedOptions) {
        if (!arguments.hasMore()) {
            throw new OptionMissingRequiredArgumentException(this);
        }
        this.addArguments(detectedOptions, arguments.next());
    }
}
