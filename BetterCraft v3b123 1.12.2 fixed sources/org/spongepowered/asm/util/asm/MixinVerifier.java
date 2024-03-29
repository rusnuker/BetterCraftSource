// 
// Decompiled by Procyon v0.6.0
// 

package org.spongepowered.asm.util.asm;

import org.spongepowered.asm.mixin.transformer.ClassInfo;
import java.util.List;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.analysis.SimpleVerifier;

public class MixinVerifier extends SimpleVerifier
{
    private Type currentClass;
    private Type currentSuperClass;
    private List<Type> currentClassInterfaces;
    private boolean isInterface;
    
    public MixinVerifier(final int api, final Type currentClass, final Type currentSuperClass, final List<Type> currentClassInterfaces, final boolean isInterface) {
        super(api, currentClass, currentSuperClass, currentClassInterfaces, isInterface);
        this.currentClass = currentClass;
        this.currentSuperClass = currentSuperClass;
        this.currentClassInterfaces = currentClassInterfaces;
        this.isInterface = isInterface;
    }
    
    @Override
    protected boolean isInterface(final Type type) {
        if (this.currentClass != null && type.equals(this.currentClass)) {
            return this.isInterface;
        }
        return ClassInfo.forType(type, ClassInfo.TypeLookup.ELEMENT_TYPE).isInterface();
    }
    
    @Override
    protected Type getSuperClass(final Type type) {
        if (this.currentClass != null && type.equals(this.currentClass)) {
            return this.currentSuperClass;
        }
        final ClassInfo c = ClassInfo.forType(type, ClassInfo.TypeLookup.ELEMENT_TYPE).getSuperClass();
        return (c == null) ? null : Type.getType("L" + c.getName() + ";");
    }
    
    @Override
    protected boolean isAssignableFrom(final Type type, final Type other) {
        if (type.equals(other)) {
            return true;
        }
        if (this.currentClass != null && type.equals(this.currentClass)) {
            if (this.getSuperClass(other) == null) {
                return false;
            }
            if (this.isInterface) {
                return other.getSort() == 10 || other.getSort() == 9;
            }
            return this.isAssignableFrom(type, this.getSuperClass(other));
        }
        else if (this.currentClass != null && other.equals(this.currentClass)) {
            if (this.isAssignableFrom(type, this.currentSuperClass)) {
                return true;
            }
            if (this.currentClassInterfaces != null) {
                for (int i = 0; i < this.currentClassInterfaces.size(); ++i) {
                    final Type v = this.currentClassInterfaces.get(i);
                    if (this.isAssignableFrom(type, v)) {
                        return true;
                    }
                }
            }
            return false;
        }
        else {
            ClassInfo typeInfo = ClassInfo.forType(type, ClassInfo.TypeLookup.ELEMENT_TYPE);
            if (typeInfo == null) {
                return false;
            }
            if (typeInfo.isInterface()) {
                typeInfo = ClassInfo.forName("java/lang/Object");
            }
            return ClassInfo.forType(other, ClassInfo.TypeLookup.ELEMENT_TYPE).hasSuperClass(typeInfo);
        }
    }
}
