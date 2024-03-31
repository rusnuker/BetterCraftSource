// 
// Decompiled by Procyon v0.6.0
// 

package org.spongepowered.tools.obfuscation.mapping.fg3;

import java.util.regex.Matcher;
import org.spongepowered.tools.obfuscation.mapping.IMappingProvider;
import java.util.regex.Pattern;
import org.spongepowered.asm.obfuscation.mapping.common.MappingMethod;

public class MappingMethodLazy extends MappingMethod
{
    private static final Pattern PATTERN_CLASSNAME;
    private final String originalDesc;
    private final IMappingProvider mappingProvider;
    private String newDesc;
    
    public MappingMethodLazy(final String owner, final String simpleName, final String originalDesc, final IMappingProvider mappingProvider) {
        super(owner, simpleName, "{" + originalDesc + "}");
        this.originalDesc = originalDesc;
        this.mappingProvider = mappingProvider;
    }
    
    @Override
    public String getDesc() {
        if (this.newDesc == null) {
            this.newDesc = this.generateDescriptor();
        }
        return this.newDesc;
    }
    
    @Override
    public String toString() {
        final String desc = this.getDesc();
        return String.format("%s%s%s", this.getName(), (desc != null) ? " " : "", (desc != null) ? desc : "");
    }
    
    private String generateDescriptor() {
        final StringBuffer desc = new StringBuffer();
        final Matcher matcher = MappingMethodLazy.PATTERN_CLASSNAME.matcher(this.originalDesc);
        while (matcher.find()) {
            final String remapped = this.mappingProvider.getClassMapping(matcher.group(1));
            if (remapped != null) {
                matcher.appendReplacement(desc, Matcher.quoteReplacement("L" + remapped + ";"));
            }
            else {
                matcher.appendReplacement(desc, Matcher.quoteReplacement("L" + matcher.group(1) + ";"));
            }
        }
        matcher.appendTail(desc);
        return desc.toString();
    }
    
    static {
        PATTERN_CLASSNAME = Pattern.compile("L([^;]+);");
    }
}
