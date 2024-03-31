// 
// Decompiled by Procyon v0.6.0
// 

package org.spongepowered.tools.obfuscation.mirror;

import java.util.HashMap;
import javax.annotation.processing.Filer;
import java.io.InputStream;
import java.io.IOException;
import java.io.FileNotFoundException;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassReader;
import javax.tools.JavaFileManager;
import javax.tools.StandardLocation;
import org.spongepowered.tools.obfuscation.interfaces.IMixinAnnotationProcessor;
import org.objectweb.asm.tree.FieldNode;
import org.spongepowered.asm.mixin.injection.selectors.ITargetSelectorByName;
import org.objectweb.asm.tree.MethodNode;
import java.util.Iterator;
import com.google.common.collect.ImmutableList;
import javax.lang.model.type.TypeMirror;
import java.util.List;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import org.objectweb.asm.tree.AnnotationNode;
import javax.lang.model.element.AnnotationMirror;
import org.spongepowered.asm.util.Annotations;
import org.spongepowered.asm.util.asm.IAnnotationHandle;
import java.lang.annotation.Annotation;
import javax.lang.model.element.PackageElement;
import org.spongepowered.tools.obfuscation.interfaces.ITypeHandleProvider;
import org.objectweb.asm.tree.ClassNode;
import java.util.Map;

public class TypeHandleASM extends TypeHandle
{
    private static final Map<String, TypeHandleASM> cache;
    private final ClassNode classNode;
    private final ITypeHandleProvider typeProvider;
    
    protected TypeHandleASM(final PackageElement pkg, final String name, final ClassNode classNode, final ITypeHandleProvider typeProvider) {
        super(pkg, name);
        this.classNode = classNode;
        this.typeProvider = typeProvider;
    }
    
    @Override
    public IAnnotationHandle getAnnotation(final Class<? extends Annotation> annotationClass) {
        final AnnotationNode visibleAnnotation = Annotations.getVisible(this.classNode, annotationClass);
        if (visibleAnnotation != null) {
            return Annotations.handleOf(visibleAnnotation);
        }
        final AnnotationNode invisibleAnnotation = Annotations.getInvisible(this.classNode, annotationClass);
        if (invisibleAnnotation != null) {
            return Annotations.handleOf(invisibleAnnotation);
        }
        return AnnotationHandle.of(null);
    }
    
    public <T extends Element> List<T> getEnclosedElements(final ElementKind... kind) {
        return super.getEnclosedElements(kind);
    }
    
    @Override
    public boolean hasTypeMirror() {
        return false;
    }
    
    @Override
    public TypeMirror getTypeMirror() {
        return null;
    }
    
    @Override
    public TypeHandle getSuperclass() {
        final TypeHandle superClass = this.typeProvider.getTypeHandle(this.classNode.superName);
        return superClass;
    }
    
    @Override
    public List<TypeHandle> getInterfaces() {
        final ImmutableList.Builder<TypeHandle> list = ImmutableList.builder();
        for (final String ifaceName : this.classNode.interfaces) {
            final TypeHandle iface = this.typeProvider.getTypeHandle(ifaceName);
            if (iface != null) {
                list.add(iface);
            }
        }
        return list.build();
    }
    
    @Override
    public List<MethodHandle> getMethods() {
        final ImmutableList.Builder<MethodHandle> methods = ImmutableList.builder();
        for (final MethodNode method : this.classNode.methods) {
            if (!method.name.startsWith("<") && (method.access & 0x1000) == 0x0) {
                methods.add(new MethodHandleASM(this, method));
            }
        }
        return methods.build();
    }
    
    @Override
    public boolean isPublic() {
        return (this.classNode.access & 0x1) != 0x0;
    }
    
    @Override
    public boolean isImaginary() {
        return false;
    }
    
    @Override
    public String findDescriptor(final ITargetSelectorByName selector) {
        String desc = selector.getDesc();
        if (desc == null) {
            for (final MethodNode method : this.classNode.methods) {
                if (method.name.equals(selector.getName())) {
                    desc = method.desc;
                    break;
                }
            }
        }
        return desc;
    }
    
    @Override
    public FieldHandle findField(final String name, final String type, final boolean matchCase) {
        for (final FieldNode field : this.classNode.fields) {
            if (compareElement(field.name, TypeUtils.getJavaSignature(field.desc), name, type, matchCase)) {
                return new FieldHandleASM(this, field);
            }
        }
        return null;
    }
    
    @Override
    public MethodHandle findMethod(final String name, final String signature, final boolean matchCase) {
        for (final MethodNode method : this.classNode.methods) {
            if (compareElement(method.name, TypeUtils.getJavaSignature(method.desc), name, signature, matchCase)) {
                return new MethodHandleASM(this, method);
            }
        }
        return null;
    }
    
    protected static boolean compareElement(final String elementName, final String elementType, final String name, final String type, final boolean matchCase) {
        try {
            final boolean compared = matchCase ? name.equals(elementName) : name.equalsIgnoreCase(elementName);
            return compared && (type.length() == 0 || type.equals(elementType));
        }
        catch (final NullPointerException ex) {
            return false;
        }
    }
    
    public static TypeHandle of(final PackageElement pkg, final String name, final IMixinAnnotationProcessor ap) {
        final String fqName = (Object)pkg.getQualifiedName() + "." + name;
        if (TypeHandleASM.cache.containsKey(fqName)) {
            return TypeHandleASM.cache.get(fqName);
        }
        InputStream is = null;
        try {
            final Filer filer = ap.getProcessingEnvironment().getFiler();
            is = filer.getResource(StandardLocation.CLASS_PATH, pkg.getQualifiedName(), name + ".class").openInputStream();
            final ClassNode classNode = new ClassNode();
            new ClassReader(is).accept(classNode, 0);
            final TypeHandleASM typeHandle = new TypeHandleASM(pkg, fqName, classNode, ap.getTypeProvider());
            TypeHandleASM.cache.put(fqName, typeHandle);
            return typeHandle;
        }
        catch (final FileNotFoundException fnfe) {
            TypeHandleASM.cache.put(fqName, null);
        }
        catch (final Exception ex2) {}
        finally {
            if (is != null) {
                try {
                    is.close();
                }
                catch (final IOException ex) {
                    ex.printStackTrace();
                }
            }
        }
        return null;
    }
    
    static {
        cache = new HashMap<String, TypeHandleASM>();
    }
}
