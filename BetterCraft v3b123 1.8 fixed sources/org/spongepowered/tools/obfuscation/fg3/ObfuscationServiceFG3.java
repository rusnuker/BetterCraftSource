// 
// Decompiled by Procyon v0.6.0
// 

package org.spongepowered.tools.obfuscation.fg3;

import org.spongepowered.tools.obfuscation.ObfuscationEnvironment;
import com.google.common.collect.ImmutableList;
import org.spongepowered.tools.obfuscation.service.ObfuscationTypeDescriptor;
import java.util.Collection;
import org.spongepowered.tools.obfuscation.interfaces.IMixinAnnotationProcessor;
import com.google.common.collect.ImmutableSet;
import java.util.Set;
import org.spongepowered.tools.obfuscation.service.IObfuscationService;

public class ObfuscationServiceFG3 implements IObfuscationService
{
    public static final String SEARGE = "searge";
    public static final String REOBF_TSRG_FILE = "reobfTsrgFile";
    public static final String REOBF_EXTRA_TSRG_FILES = "reobfTsrgFiles";
    public static final String OUT_TSRG_SRG_FILE = "outTsrgFile";
    public static final String TSRG_OUTPUT_BEHAVIOUR = "mergeBehaviour";
    
    @Override
    public Set<String> getSupportedOptions() {
        return ImmutableSet.of("reobfTsrgFile", "reobfTsrgFiles", "outTsrgFile", "mergeBehaviour");
    }
    
    @Override
    public Collection<ObfuscationTypeDescriptor> getObfuscationTypes(final IMixinAnnotationProcessor ap) {
        final ImmutableList.Builder<ObfuscationTypeDescriptor> list = ImmutableList.builder();
        if (ap.getOptions("mappingTypes").contains("tsrg")) {
            list.add(new ObfuscationTypeDescriptor("searge", "reobfTsrgFile", "reobfTsrgFiles", "outTsrgFile", ObfuscationEnvironmentFG3.class));
        }
        return list.build();
    }
}
