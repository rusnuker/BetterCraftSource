// 
// Decompiled by Procyon v0.6.0
// 

package org.spongepowered.tools.obfuscation.mapping.fg3;

import org.spongepowered.asm.obfuscation.mapping.common.MappingMethod;
import org.spongepowered.asm.obfuscation.mapping.common.MappingField;
import org.spongepowered.tools.obfuscation.mapping.IMappingConsumer;
import java.util.Iterator;
import java.io.IOException;
import java.io.PrintWriter;
import org.spongepowered.tools.obfuscation.ObfuscationType;
import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import org.spongepowered.tools.obfuscation.mapping.mcp.MappingWriterSrg;

public class MappingWriterTSrg extends MappingWriterSrg
{
    private final MappingProviderTSrg provider;
    private final boolean mergeExisting;
    
    public MappingWriterTSrg(final Messager messager, final Filer filer, final MappingProviderTSrg provider, final boolean mergeExisting) {
        super(messager, filer);
        this.provider = provider;
        this.mergeExisting = mergeExisting;
    }
    
    @Override
    protected PrintWriter openFileWriter(final String output, final ObfuscationType type) throws IOException {
        return this.openFileWriter(output, type + " composite mappings");
    }
    
    @Override
    protected void writeHeader(final PrintWriter writer) {
        if (this.mergeExisting) {
            for (final String line : this.provider.getInputMappings()) {
                writer.println(line);
            }
        }
    }
    
    @Override
    protected String formatFieldMapping(final IMappingConsumer.MappingSet.Pair<MappingField> field) {
        return String.format("%s %s %s", field.from.getOwner(), field.from.getSimpleName(), field.to.getSimpleName());
    }
    
    @Override
    protected String formatMethodMapping(final IMappingConsumer.MappingSet.Pair<MappingMethod> method) {
        return String.format("%s %s %s %s", method.from.getOwner(), method.from.getSimpleName(), method.from.getDesc(), method.to.getSimpleName());
    }
}
