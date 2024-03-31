// 
// Decompiled by Procyon v0.6.0
// 

package org.spongepowered.tools.obfuscation.mirror;

import javax.lang.model.element.Modifier;
import org.spongepowered.asm.util.Bytecode;
import javax.lang.model.element.Name;
import java.util.List;
import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeVariable;
import javax.lang.model.type.ArrayType;
import org.spongepowered.asm.util.SignaturePrinter;
import java.util.Iterator;
import javax.lang.model.element.TypeParameterElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.element.PackageElement;
import javax.lang.model.type.TypeMirror;

public abstract class TypeUtils
{
    private static final int MAX_GENERIC_RECURSION_DEPTH = 5;
    private static final String OBJECT_SIG = "java.lang.Object";
    
    private TypeUtils() {
    }
    
    public static PackageElement getPackage(final TypeMirror type) {
        if (!(type instanceof DeclaredType)) {
            return null;
        }
        return getPackage((TypeElement)((DeclaredType)type).asElement());
    }
    
    public static PackageElement getPackage(final TypeElement type) {
        Element parent;
        for (parent = type.getEnclosingElement(); parent != null && !(parent instanceof PackageElement); parent = parent.getEnclosingElement()) {}
        return (PackageElement)parent;
    }
    
    public static String getElementType(final Element element) {
        if (element instanceof TypeElement) {
            return "TypeElement";
        }
        if (element instanceof ExecutableElement) {
            return "ExecutableElement";
        }
        if (element instanceof VariableElement) {
            return "VariableElement";
        }
        if (element instanceof PackageElement) {
            return "PackageElement";
        }
        if (element instanceof TypeParameterElement) {
            return "TypeParameterElement";
        }
        return element.getClass().getSimpleName();
    }
    
    public static String stripGenerics(final String type) {
        final StringBuilder sb = new StringBuilder();
        int pos = 0;
        int depth = 0;
        while (pos < type.length()) {
            final char c = type.charAt(pos);
            if (c == '<') {
                ++depth;
            }
            if (depth == 0) {
                sb.append(c);
            }
            else if (c == '>') {
                --depth;
            }
            ++pos;
        }
        return sb.toString();
    }
    
    public static String getName(final VariableElement field) {
        return (field != null) ? field.getSimpleName().toString() : null;
    }
    
    public static String getName(final ExecutableElement method) {
        return (method != null) ? method.getSimpleName().toString() : null;
    }
    
    public static String getJavaSignature(final Element element) {
        if (element == null) {
            return "";
        }
        if (element instanceof ExecutableElement) {
            final ExecutableElement method = (ExecutableElement)element;
            final StringBuilder desc = new StringBuilder().append("(");
            boolean extra = false;
            for (final VariableElement arg : method.getParameters()) {
                if (extra) {
                    desc.append(',');
                }
                desc.append(getTypeName(arg.asType()));
                extra = true;
            }
            desc.append(')').append(getTypeName(method.getReturnType()));
            return desc.toString();
        }
        return getTypeName(element.asType());
    }
    
    public static String getJavaSignature(final String descriptor) {
        return new SignaturePrinter("", descriptor).setFullyQualified(true).toDescriptor();
    }
    
    public static String getSimpleName(final TypeMirror type) {
        final String name = getTypeName(type);
        final int pos = name.lastIndexOf(46);
        return (pos > 0) ? name.substring(pos + 1) : name;
    }
    
    public static String getTypeName(final TypeMirror type) {
        switch (type.getKind()) {
            case ARRAY: {
                return getTypeName(((ArrayType)type).getComponentType()) + "[]";
            }
            case DECLARED: {
                return getTypeName((DeclaredType)type);
            }
            case TYPEVAR: {
                return getTypeName(getUpperBound(type));
            }
            case ERROR: {
                return "java.lang.Object";
            }
            default: {
                return type.toString();
            }
        }
    }
    
    public static String getTypeName(final DeclaredType type) {
        if (type == null) {
            return "java.lang.Object";
        }
        return getInternalName((TypeElement)type.asElement()).replace('/', '.');
    }
    
    public static String getDescriptor(final Element element) {
        if (element instanceof ExecutableElement) {
            return getDescriptor((ExecutableElement)element);
        }
        if (element instanceof VariableElement) {
            return getInternalName((VariableElement)element);
        }
        return getInternalName(element.asType());
    }
    
    public static String getDescriptor(final ExecutableElement method) {
        if (method == null) {
            return null;
        }
        final StringBuilder signature = new StringBuilder();
        for (final VariableElement var : method.getParameters()) {
            signature.append(getInternalName(var));
        }
        final String returnType = getInternalName(method.getReturnType());
        return String.format("(%s)%s", signature, returnType);
    }
    
    public static String getInternalName(final VariableElement field) {
        if (field == null) {
            return null;
        }
        return getInternalName(field.asType());
    }
    
    public static String getInternalName(final TypeMirror type) {
        switch (type.getKind()) {
            case ARRAY: {
                return "[" + getInternalName(((ArrayType)type).getComponentType());
            }
            case DECLARED: {
                return "L" + getInternalName((DeclaredType)type) + ";";
            }
            case TYPEVAR: {
                return "L" + getInternalName(getUpperBound(type)) + ";";
            }
            case BOOLEAN: {
                return "Z";
            }
            case BYTE: {
                return "B";
            }
            case CHAR: {
                return "C";
            }
            case DOUBLE: {
                return "D";
            }
            case FLOAT: {
                return "F";
            }
            case INT: {
                return "I";
            }
            case LONG: {
                return "J";
            }
            case SHORT: {
                return "S";
            }
            case VOID: {
                return "V";
            }
            case ERROR: {
                return "Ljava/lang/Object;";
            }
            default: {
                throw new IllegalArgumentException("Unable to parse type symbol " + type + " with " + type.getKind() + " to equivalent bytecode type");
            }
        }
    }
    
    public static String getInternalName(final DeclaredType type) {
        if (type == null) {
            return "java/lang/Object";
        }
        return getInternalName((TypeElement)type.asElement());
    }
    
    public static String getInternalName(final TypeElement element) {
        if (element == null) {
            return null;
        }
        final StringBuilder reference = new StringBuilder();
        reference.append(element.getSimpleName());
        for (Element parent = element.getEnclosingElement(); parent != null; parent = parent.getEnclosingElement()) {
            if (parent instanceof TypeElement) {
                reference.insert(0, "$").insert(0, parent.getSimpleName());
            }
            else if (parent instanceof PackageElement) {
                reference.insert(0, "/").insert(0, ((PackageElement)parent).getQualifiedName().toString().replace('.', '/'));
            }
        }
        return reference.toString();
    }
    
    private static DeclaredType getUpperBound(final TypeMirror type) {
        try {
            return getUpperBound0(type, 5);
        }
        catch (final IllegalStateException ex) {
            throw new IllegalArgumentException("Type symbol \"" + type + "\" is too complex", ex);
        }
        catch (final IllegalArgumentException ex2) {
            throw new IllegalArgumentException("Unable to compute upper bound of type symbol " + type, ex2);
        }
    }
    
    private static DeclaredType getUpperBound0(final TypeMirror type, int depth) {
        if (depth == 0) {
            throw new IllegalStateException("Generic symbol \"" + type + "\" is too complex, exceeded " + 5 + " iterations attempting to determine upper bound");
        }
        if (type instanceof DeclaredType) {
            return (DeclaredType)type;
        }
        if (type instanceof TypeVariable) {
            try {
                final TypeMirror upper = ((TypeVariable)type).getUpperBound();
                return getUpperBound0(upper, --depth);
            }
            catch (final IllegalStateException ex) {
                throw ex;
            }
            catch (final IllegalArgumentException ex2) {
                throw ex2;
            }
            catch (final Exception ex3) {
                throw new IllegalArgumentException("Unable to compute upper bound of type symbol " + type);
            }
        }
        return null;
    }
    
    private static String describeGenericBound(final TypeMirror type) {
        if (type instanceof TypeVariable) {
            final StringBuilder description = new StringBuilder("<");
            final TypeVariable typeVar = (TypeVariable)type;
            description.append(typeVar.toString());
            final TypeMirror lowerBound = typeVar.getLowerBound();
            if (lowerBound.getKind() != TypeKind.NULL) {
                description.append(" super ").append(lowerBound);
            }
            final TypeMirror upperBound = typeVar.getUpperBound();
            if (upperBound.getKind() != TypeKind.NULL) {
                description.append(" extends ").append(upperBound);
            }
            return description.append(">").toString();
        }
        return type.toString();
    }
    
    public static boolean isAssignable(final ProcessingEnvironment processingEnv, final TypeMirror targetType, final TypeMirror superClass) {
        final boolean assignable = processingEnv.getTypeUtils().isAssignable(targetType, superClass);
        if (!assignable && targetType instanceof DeclaredType && superClass instanceof DeclaredType) {
            final TypeMirror rawTargetType = toRawType(processingEnv, (DeclaredType)targetType);
            final TypeMirror rawSuperType = toRawType(processingEnv, (DeclaredType)superClass);
            return processingEnv.getTypeUtils().isAssignable(rawTargetType, rawSuperType);
        }
        return assignable;
    }
    
    public static EquivalencyResult isEquivalentType(final ProcessingEnvironment processingEnv, TypeMirror t1, TypeMirror t2) {
        if (t1 == null || t2 == null) {
            return EquivalencyResult.notEquivalent("Invalid types supplied: %s, %s", t1, t2);
        }
        if (processingEnv.getTypeUtils().isSameType(t1, t2)) {
            return EquivalencyResult.EQUIVALENT;
        }
        if (t1 instanceof TypeVariable && t2 instanceof TypeVariable) {
            t1 = getUpperBound(t1);
            t2 = getUpperBound(t2);
            if (processingEnv.getTypeUtils().isSameType(t1, t2)) {
                return EquivalencyResult.EQUIVALENT;
            }
        }
        if (!(t1 instanceof DeclaredType) || !(t2 instanceof DeclaredType)) {
            return EquivalencyResult.notEquivalent("%s and %s do not match", t1, t2);
        }
        final DeclaredType dtT1 = (DeclaredType)t1;
        final DeclaredType dtT2 = (DeclaredType)t2;
        final TypeMirror rawT1 = toRawType(processingEnv, dtT1);
        final TypeMirror rawT2 = toRawType(processingEnv, dtT2);
        if (!processingEnv.getTypeUtils().isSameType(rawT1, rawT2)) {
            return EquivalencyResult.notEquivalent("Base types %s and %s are not compatible", rawT1, rawT2);
        }
        final List<? extends TypeMirror> argsT1 = dtT1.getTypeArguments();
        final List<? extends TypeMirror> argsT2 = dtT2.getTypeArguments();
        if (argsT1.size() == argsT2.size()) {
            for (int arg = 0; arg < argsT1.size(); ++arg) {
                final TypeMirror argT1 = (TypeMirror)argsT1.get(arg);
                final TypeMirror argT2 = (TypeMirror)argsT2.get(arg);
                if (isEquivalentType(processingEnv, argT1, argT2).type != Equivalency.EQUIVALENT) {
                    return EquivalencyResult.boundsMismatch("Generic bounds mismatch between %s and %s", describeGenericBound(argT1), describeGenericBound(argT2));
                }
            }
            return EquivalencyResult.EQUIVALENT;
        }
        if (argsT1.size() == 0) {
            return EquivalencyResult.equivalentButRaw(1);
        }
        if (argsT2.size() == 0) {
            return EquivalencyResult.equivalentButRaw(2);
        }
        return EquivalencyResult.notEquivalent("Mismatched generic argument counts %s<[%d]> and %s<[%d]>", rawT1, argsT1.size(), rawT2, argsT2.size());
    }
    
    private static TypeMirror toRawType(final ProcessingEnvironment processingEnv, final DeclaredType targetType) {
        if (targetType.getKind() == TypeKind.INTERSECTION) {
            return targetType;
        }
        final Name qualifiedName = ((TypeElement)targetType.asElement()).getQualifiedName();
        final TypeElement typeElement = processingEnv.getElementUtils().getTypeElement(qualifiedName);
        return (typeElement != null) ? typeElement.asType() : targetType;
    }
    
    public static Bytecode.Visibility getVisibility(final Element element) {
        if (element == null) {
            return null;
        }
        for (final Modifier modifier : element.getModifiers()) {
            switch (modifier) {
                case PUBLIC: {
                    return Bytecode.Visibility.PUBLIC;
                }
                case PROTECTED: {
                    return Bytecode.Visibility.PROTECTED;
                }
                case PRIVATE: {
                    return Bytecode.Visibility.PRIVATE;
                }
                default: {
                    continue;
                }
            }
        }
        return Bytecode.Visibility.PACKAGE;
    }
    
    public enum Equivalency
    {
        NOT_EQUIVALENT, 
        EQUIVALENT_BUT_RAW, 
        BOUNDS_MISMATCH, 
        EQUIVALENT;
    }
    
    public static class EquivalencyResult
    {
        static final EquivalencyResult EQUIVALENT;
        public final Equivalency type;
        public final String detail;
        public final int rawType;
        
        EquivalencyResult(final Equivalency type, final String detail, final int rawType) {
            this.type = type;
            this.detail = detail;
            this.rawType = rawType;
        }
        
        @Override
        public String toString() {
            return this.detail;
        }
        
        static EquivalencyResult notEquivalent(final String format, final Object... args) {
            return new EquivalencyResult(Equivalency.NOT_EQUIVALENT, String.format(format, args), 0);
        }
        
        static EquivalencyResult boundsMismatch(final String format, final Object... args) {
            return new EquivalencyResult(Equivalency.BOUNDS_MISMATCH, String.format(format, args), 0);
        }
        
        static EquivalencyResult equivalentButRaw(final int rawType) {
            return new EquivalencyResult(Equivalency.EQUIVALENT_BUT_RAW, String.format("Type %d is raw", rawType), rawType);
        }
        
        static {
            EQUIVALENT = new EquivalencyResult(Equivalency.EQUIVALENT, "", 0);
        }
    }
}
