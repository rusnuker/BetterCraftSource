// 
// Decompiled by Procyon v0.6.0
// 

package org.spongepowered.tools.obfuscation.mapping.fg3;

import org.spongepowered.asm.obfuscation.mapping.mcp.MappingFieldSrg;
import java.io.IOException;
import java.util.Iterator;
import com.google.common.collect.BiMap;
import org.spongepowered.tools.obfuscation.mapping.IMappingProvider;
import org.spongepowered.asm.obfuscation.mapping.common.MappingMethod;
import org.spongepowered.asm.obfuscation.mapping.common.MappingField;
import com.google.common.base.Strings;
import java.util.Collection;
import com.google.common.io.Files;
import java.nio.charset.Charset;
import java.io.File;
import java.util.ArrayList;
import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import java.util.List;
import org.spongepowered.tools.obfuscation.mapping.common.MappingProvider;

public class MappingProviderTSrg extends MappingProvider
{
    private List<String> inputMappings;
    
    public MappingProviderTSrg(final Messager messager, final Filer filer) {
        super(messager, filer);
        this.inputMappings = new ArrayList<String>();
    }
    
    @Override
    public void read(final File input) throws IOException {
        final BiMap<String, String> packageMap = this.packageMap;
        final BiMap<String, String> classMap = this.classMap;
        final BiMap<MappingField, MappingField> fieldMap = this.fieldMap;
        final BiMap<MappingMethod, MappingMethod> methodMap = this.methodMap;
        String fromClass = null;
        String toClass = null;
        this.inputMappings.addAll(Files.readLines(input, Charset.defaultCharset()));
        for (final String line : this.inputMappings) {
            if (!Strings.isNullOrEmpty(line) && !line.startsWith("#") && !line.startsWith("tsrg2")) {
                if (line.startsWith("\t\t")) {
                    continue;
                }
                final String[] parts = line.split(" ");
                if (line.startsWith("\t")) {
                    if (fromClass == null) {
                        throw new IllegalStateException("Error parsing TSRG file, found member declaration with no class: " + line);
                    }
                    parts[0] = parts[0].substring(1);
                    if (parts.length == 2) {
                        fieldMap.forcePut(new MappingField(fromClass, parts[0]), new MappingField(toClass, parts[1]));
                    }
                    else {
                        if (parts.length != 3) {
                            throw new IllegalStateException("Error parsing TSRG file, too many arguments: " + line);
                        }
                        methodMap.forcePut(new MappingMethod(fromClass, parts[0], parts[1]), new MappingMethodLazy(toClass, parts[2], parts[1], this));
                    }
                }
                else {
                    if (parts.length <= 1) {
                        throw new IllegalStateException("Error parsing TSRG, unrecognised directive: " + line);
                    }
                    final String from = parts[0];
                    if (parts.length == 2) {
                        final String to = parts[1];
                        if (from.endsWith("/")) {
                            packageMap.forcePut(from.substring(0, from.length() - 1), to.substring(0, to.length() - 1));
                        }
                        else {
                            classMap.forcePut(from, to);
                            fromClass = from;
                            toClass = to;
                        }
                    }
                    else {
                        if (parts.length <= 2) {
                            continue;
                        }
                        final String to = classMap.get(from);
                        if (to == null) {
                            throw new IllegalStateException("Error parsing TSRG file, found inline member before class mapping: " + line);
                        }
                        if (parts.length == 3) {
                            fieldMap.forcePut(new MappingField(from, parts[1]), new MappingField(to, parts[2]));
                        }
                        else {
                            if (parts.length != 4) {
                                throw new IllegalStateException("Error parsing TSRG file, too many arguments: " + line);
                            }
                            methodMap.forcePut(new MappingMethod(from, parts[1], parts[2]), new MappingMethodLazy(to, parts[3], parts[2], this));
                        }
                    }
                }
            }
        }
    }
    
    @Override
    public MappingField getFieldMapping(MappingField field) {
        if (field.getDesc() != null) {
            field = new MappingFieldSrg(field);
        }
        return this.fieldMap.get(field);
    }
    
    List<String> getInputMappings() {
        return this.inputMappings;
    }
}
