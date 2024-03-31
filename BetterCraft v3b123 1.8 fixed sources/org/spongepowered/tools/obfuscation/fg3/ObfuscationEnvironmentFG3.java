// 
// Decompiled by Procyon v0.6.0
// 

package org.spongepowered.tools.obfuscation.fg3;

import org.spongepowered.tools.obfuscation.mapping.fg3.MappingWriterTSrg;
import org.spongepowered.tools.obfuscation.mapping.IMappingWriter;
import org.spongepowered.tools.obfuscation.mapping.IMappingProvider;
import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import org.spongepowered.tools.obfuscation.ObfuscationType;
import org.spongepowered.tools.obfuscation.mapping.fg3.MappingProviderTSrg;
import org.spongepowered.tools.obfuscation.ObfuscationEnvironment;

public class ObfuscationEnvironmentFG3 extends ObfuscationEnvironment
{
    private MappingProviderTSrg provider;
    
    protected ObfuscationEnvironmentFG3(final ObfuscationType type) {
        super(type);
    }
    
    @Override
    protected IMappingProvider getMappingProvider(final Messager messager, final Filer filer) {
        return this.provider = new MappingProviderTSrg(messager, filer);
    }
    
    @Override
    protected IMappingWriter getMappingWriter(final Messager messager, final Filer filer) {
        final String outputBehaviour = this.ap.getOption("mergeBehaviour");
        return new MappingWriterTSrg(messager, filer, this.provider, outputBehaviour != null && outputBehaviour.equalsIgnoreCase("merge"));
    }
}
