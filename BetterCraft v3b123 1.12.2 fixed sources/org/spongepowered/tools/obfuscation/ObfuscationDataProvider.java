// 
// Decompiled by Procyon v0.6.0
// 

package org.spongepowered.tools.obfuscation;

import org.spongepowered.asm.mixin.injection.struct.MemberInfo;
import org.spongepowered.asm.obfuscation.mapping.common.MappingMethod;
import org.spongepowered.asm.obfuscation.mapping.common.MappingField;
import org.spongepowered.asm.obfuscation.mapping.IMapping;
import java.util.Iterator;
import org.spongepowered.tools.obfuscation.mirror.TypeHandle;
import java.util.Set;
import java.util.HashSet;
import org.spongepowered.asm.mixin.injection.selectors.ITargetSelectorRemappable;
import java.util.List;
import org.spongepowered.tools.obfuscation.interfaces.IMixinAnnotationProcessor;
import org.spongepowered.tools.obfuscation.interfaces.IObfuscationDataProvider;

public class ObfuscationDataProvider implements IObfuscationDataProvider
{
    private final IMixinAnnotationProcessor ap;
    private final List<ObfuscationEnvironment> environments;
    
    public ObfuscationDataProvider(final IMixinAnnotationProcessor ap, final List<ObfuscationEnvironment> environments) {
        this.ap = ap;
        this.environments = environments;
    }
    
    @Override
    public <T> ObfuscationData<T> getObfEntryRecursive(final ITargetSelectorRemappable targetMember) {
        final ObfuscationData<String> obfTargetNames = this.getObfClass(targetMember.getOwner());
        ObfuscationData<T> obfData = (ObfuscationData<T>)this.getObfEntry(targetMember);
        try {
            if (obfData.isEmpty()) {
                obfData = this.getObfEntryRecursive(targetMember, new HashSet<String>());
            }
            if (!obfData.isEmpty()) {
                return applyParents(obfTargetNames, obfData);
            }
        }
        catch (final Exception ex) {
            ex.printStackTrace();
            return (ObfuscationData<T>)this.getObfEntry(targetMember);
        }
        return obfData;
    }
    
    private <T> ObfuscationData<T> getObfEntryRecursive(final ITargetSelectorRemappable targetMember, final Set<String> visited) {
        final TypeHandle targetType = this.ap.getTypeProvider().getTypeHandle(targetMember.getOwner());
        if (targetType == null || !visited.add(targetType.toString())) {
            return new ObfuscationData<T>();
        }
        final TypeHandle superClass = targetType.getSuperclass();
        for (final TypeHandle iface : targetType.getInterfaces()) {
            ObfuscationData<T> obfData = this.getObfEntryUsing(targetMember, iface);
            if (!obfData.isEmpty()) {
                return obfData;
            }
            obfData = (ObfuscationData<T>)this.getObfEntryRecursive(targetMember.move(iface.getName()), visited);
            if (!obfData.isEmpty()) {
                return obfData;
            }
        }
        if (superClass == null) {
            return new ObfuscationData<T>();
        }
        ObfuscationData<T> obfData = this.getObfEntryUsing(targetMember, superClass);
        if (!obfData.isEmpty()) {
            return obfData;
        }
        return (ObfuscationData<T>)this.getObfEntryRecursive(targetMember.move(superClass.getName()), visited);
    }
    
    private <T> ObfuscationData<T> getObfEntryUsing(final ITargetSelectorRemappable targetMember, final TypeHandle targetClass) {
        return (targetClass == null) ? new ObfuscationData<T>() : this.getObfEntry(targetMember.move(targetClass.getName()));
    }
    
    @Override
    public <T> ObfuscationData<T> getObfEntry(final ITargetSelectorRemappable targetMember) {
        if (targetMember.isField()) {
            return (ObfuscationData<T>)this.getObfField(targetMember);
        }
        return (ObfuscationData<T>)this.getObfMethod(targetMember.asMethodMapping());
    }
    
    @Override
    public <T> ObfuscationData<T> getObfEntry(final IMapping<T> mapping) {
        if (mapping != null) {
            if (mapping.getType() == IMapping.Type.FIELD) {
                return (ObfuscationData<T>)this.getObfField((MappingField)mapping);
            }
            if (mapping.getType() == IMapping.Type.METHOD) {
                return (ObfuscationData<T>)this.getObfMethod((MappingMethod)mapping);
            }
        }
        return new ObfuscationData<T>();
    }
    
    @Override
    public ObfuscationData<MappingMethod> getObfMethodRecursive(final ITargetSelectorRemappable targetMember) {
        return this.getObfEntryRecursive(targetMember);
    }
    
    @Override
    public ObfuscationData<MappingMethod> getObfMethod(final ITargetSelectorRemappable method) {
        return this.getRemappedMethod(method, method.isConstructor());
    }
    
    @Override
    public ObfuscationData<MappingMethod> getRemappedMethod(final ITargetSelectorRemappable method) {
        return this.getRemappedMethod(method, true);
    }
    
    private ObfuscationData<MappingMethod> getRemappedMethod(final ITargetSelectorRemappable method, final boolean remapDescriptor) {
        final ObfuscationData<MappingMethod> data = new ObfuscationData<MappingMethod>();
        for (final ObfuscationEnvironment env : this.environments) {
            final MappingMethod obfMethod = env.getObfMethod(method);
            if (obfMethod != null) {
                data.put(env.getType(), obfMethod);
            }
        }
        if (!data.isEmpty() || !remapDescriptor) {
            return data;
        }
        return this.remapDescriptor(data, method);
    }
    
    @Override
    public ObfuscationData<MappingMethod> getObfMethod(final MappingMethod method) {
        return this.getRemappedMethod(method, method.isConstructor());
    }
    
    @Override
    public ObfuscationData<MappingMethod> getRemappedMethod(final MappingMethod method) {
        return this.getRemappedMethod(method, true);
    }
    
    private ObfuscationData<MappingMethod> getRemappedMethod(final MappingMethod method, final boolean remapDescriptor) {
        final ObfuscationData<MappingMethod> data = new ObfuscationData<MappingMethod>();
        for (final ObfuscationEnvironment env : this.environments) {
            final MappingMethod obfMethod = env.getObfMethod(method);
            if (obfMethod != null) {
                data.put(env.getType(), obfMethod);
            }
        }
        if (!data.isEmpty() || !remapDescriptor) {
            return data;
        }
        return this.remapDescriptor(data, new MemberInfo(method));
    }
    
    public ObfuscationData<MappingMethod> remapDescriptor(final ObfuscationData<MappingMethod> data, final ITargetSelectorRemappable method) {
        for (final ObfuscationEnvironment env : this.environments) {
            final ITargetSelectorRemappable obfMethod = env.remapDescriptor(method);
            if (obfMethod != null) {
                data.put(env.getType(), obfMethod.asMethodMapping());
            }
        }
        return data;
    }
    
    @Override
    public ObfuscationData<MappingField> getObfFieldRecursive(final ITargetSelectorRemappable targetMember) {
        return this.getObfEntryRecursive(targetMember);
    }
    
    @Override
    public ObfuscationData<MappingField> getObfField(final ITargetSelectorRemappable field) {
        return this.getObfField(field.asFieldMapping());
    }
    
    @Override
    public ObfuscationData<MappingField> getObfField(final MappingField field) {
        final ObfuscationData<MappingField> data = new ObfuscationData<MappingField>();
        for (final ObfuscationEnvironment env : this.environments) {
            MappingField obfField = env.getObfField(field);
            if (obfField != null) {
                if (obfField.getDesc() == null && field.getDesc() != null) {
                    obfField = obfField.transform(env.remapDescriptor(field.getDesc()));
                }
                data.put(env.getType(), obfField);
            }
        }
        return data;
    }
    
    @Override
    public ObfuscationData<String> getObfClass(final TypeHandle type) {
        return this.getObfClass(type.getName());
    }
    
    @Override
    public ObfuscationData<String> getObfClass(final String className) {
        final ObfuscationData<String> data = new ObfuscationData<String>(className);
        for (final ObfuscationEnvironment env : this.environments) {
            final String obfClass = env.getObfClass(className);
            if (obfClass != null) {
                data.put(env.getType(), obfClass);
            }
        }
        return data;
    }
    
    private static <T> ObfuscationData<T> applyParents(final ObfuscationData<String> parents, final ObfuscationData<T> members) {
        for (final ObfuscationType type : members) {
            final String obfClass = parents.get(type);
            final T obfMember = members.get(type);
            members.put(type, (T)MemberInfo.fromMapping((IMapping<?>)obfMember).move(obfClass).asMapping());
        }
        return members;
    }
}
