// 
// Decompiled by Procyon v0.6.0
// 

package org.objectweb.asm.tree;

import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.TypePath;
import java.util.ArrayList;
import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.ModuleVisitor;
import com.google.common.collect.Lists;
import java.util.Arrays;
import java.util.Collections;
import java.util.concurrent.CopyOnWriteArrayList;
import org.objectweb.asm.Attribute;
import java.util.List;
import org.objectweb.asm.ClassVisitor;

public class ClassNode extends ClassVisitor
{
    public int version;
    public int access;
    public String name;
    public String signature;
    public String superName;
    public List<String> interfaces;
    public String sourceFile;
    public String sourceDebug;
    public ModuleNode module;
    public String outerClass;
    public String outerMethod;
    public String outerMethodDesc;
    public List<AnnotationNode> visibleAnnotations;
    public List<AnnotationNode> invisibleAnnotations;
    public List<TypeAnnotationNode> visibleTypeAnnotations;
    public List<TypeAnnotationNode> invisibleTypeAnnotations;
    public List<Attribute> attrs;
    public List<InnerClassNode> innerClasses;
    public String nestHostClass;
    public List<String> nestMembers;
    public List<FieldNode> fields;
    public List<MethodNode> methods;
    
    public ClassNode() {
        this(458752);
        if (this.getClass() != ClassNode.class) {
            throw new IllegalStateException();
        }
    }
    
    public ClassNode(final int api) {
        super(api);
        this.interfaces = Collections.synchronizedList(new CopyOnWriteArrayList<String>());
        this.innerClasses = Collections.synchronizedList(new CopyOnWriteArrayList<InnerClassNode>());
        this.fields = Collections.synchronizedList(new CopyOnWriteArrayList<FieldNode>());
        this.methods = Collections.synchronizedList(new CopyOnWriteArrayList<MethodNode>());
    }
    
    @Override
    public void visit(final int version, final int access, final String name, final String signature, final String superName, final String[] interfaces) {
        this.version = version;
        this.access = access;
        this.name = name;
        this.signature = signature;
        this.superName = superName;
        this.interfaces = Collections.synchronizedList((List<String>)Lists.newCopyOnWriteArrayList((Iterable<?>)Arrays.asList(interfaces)));
    }
    
    @Override
    public void visitSource(final String file, final String debug) {
        this.sourceFile = file;
        this.sourceDebug = debug;
    }
    
    @Override
    public ModuleVisitor visitModule(final String name, final int access, final String version) {
        return this.module = new ModuleNode(name, access, version);
    }
    
    @Override
    public void visitNestHost(final String nestHost) {
        this.nestHostClass = nestHost;
    }
    
    @Override
    public void visitOuterClass(final String owner, final String name, final String descriptor) {
        this.outerClass = owner;
        this.outerMethod = name;
        this.outerMethodDesc = descriptor;
    }
    
    @Override
    public AnnotationVisitor visitAnnotation(final String descriptor, final boolean visible) {
        final AnnotationNode annotation = new AnnotationNode(descriptor);
        if (visible) {
            if (this.visibleAnnotations == null) {
                this.visibleAnnotations = Collections.synchronizedList(new ArrayList<AnnotationNode>(1));
            }
            this.visibleAnnotations.add(annotation);
        }
        else {
            if (this.invisibleAnnotations == null) {
                this.invisibleAnnotations = Collections.synchronizedList(new ArrayList<AnnotationNode>(1));
            }
            this.invisibleAnnotations.add(annotation);
        }
        return annotation;
    }
    
    @Override
    public AnnotationVisitor visitTypeAnnotation(final int typeRef, final TypePath typePath, final String descriptor, final boolean visible) {
        final TypeAnnotationNode typeAnnotation = new TypeAnnotationNode(typeRef, typePath, descriptor);
        if (visible) {
            if (this.visibleTypeAnnotations == null) {
                this.visibleTypeAnnotations = Collections.synchronizedList(new ArrayList<TypeAnnotationNode>(1));
            }
            this.visibleTypeAnnotations.add(typeAnnotation);
        }
        else {
            if (this.invisibleTypeAnnotations == null) {
                this.invisibleTypeAnnotations = Collections.synchronizedList(new ArrayList<TypeAnnotationNode>(1));
            }
            this.invisibleTypeAnnotations.add(typeAnnotation);
        }
        return typeAnnotation;
    }
    
    @Override
    public void visitAttribute(final Attribute attribute) {
        if (this.attrs == null) {
            this.attrs = Collections.synchronizedList(new ArrayList<Attribute>(1));
        }
        this.attrs.add(attribute);
    }
    
    @Override
    public void visitNestMember(final String nestMember) {
        if (this.nestMembers == null) {
            this.nestMembers = Collections.synchronizedList(new ArrayList<String>());
        }
        this.nestMembers.add(nestMember);
    }
    
    @Override
    public void visitInnerClass(final String name, final String outerName, final String innerName, final int access) {
        final InnerClassNode innerClass = new InnerClassNode(name, outerName, innerName, access);
        this.innerClasses.add(innerClass);
    }
    
    @Override
    public FieldVisitor visitField(final int access, final String name, final String descriptor, final String signature, final Object value) {
        final FieldNode field = new FieldNode(access, name, descriptor, signature, value);
        this.fields.add(field);
        return field;
    }
    
    @Override
    public MethodVisitor visitMethod(final int access, final String name, final String descriptor, final String signature, final String[] exceptions) {
        final MethodNode method = new MethodNode(access, name, descriptor, signature, exceptions);
        this.methods.add(method);
        return method;
    }
    
    @Override
    public void visitEnd() {
    }
    
    public void check(final int api) {
        if (api < 458752 && (this.nestHostClass != null || this.nestMembers != null)) {
            throw new UnsupportedClassVersionException();
        }
        if (api < 393216 && this.module != null) {
            throw new UnsupportedClassVersionException();
        }
        if (api < 327680) {
            if (this.visibleTypeAnnotations != null && !this.visibleTypeAnnotations.isEmpty()) {
                throw new UnsupportedClassVersionException();
            }
            if (this.invisibleTypeAnnotations != null && !this.invisibleTypeAnnotations.isEmpty()) {
                throw new UnsupportedClassVersionException();
            }
        }
        if (this.visibleAnnotations != null) {
            for (int i = this.visibleAnnotations.size() - 1; i >= 0; --i) {
                this.visibleAnnotations.get(i).check(api);
            }
        }
        if (this.invisibleAnnotations != null) {
            for (int i = this.invisibleAnnotations.size() - 1; i >= 0; --i) {
                this.invisibleAnnotations.get(i).check(api);
            }
        }
        if (this.visibleTypeAnnotations != null) {
            for (int i = this.visibleTypeAnnotations.size() - 1; i >= 0; --i) {
                this.visibleTypeAnnotations.get(i).check(api);
            }
        }
        if (this.invisibleTypeAnnotations != null) {
            for (int i = this.invisibleTypeAnnotations.size() - 1; i >= 0; --i) {
                this.invisibleTypeAnnotations.get(i).check(api);
            }
        }
        for (int i = this.fields.size() - 1; i >= 0; --i) {
            this.fields.get(i).check(api);
        }
        for (int i = this.methods.size() - 1; i >= 0; --i) {
            this.methods.get(i).check(api);
        }
    }
    
    public void accept(final ClassVisitor classVisitor) {
        final String[] interfacesArray = new String[this.interfaces.size()];
        this.interfaces.toArray(interfacesArray);
        classVisitor.visit(this.version, this.access, this.name, this.signature, this.superName, interfacesArray);
        if (this.sourceFile != null || this.sourceDebug != null) {
            classVisitor.visitSource(this.sourceFile, this.sourceDebug);
        }
        if (this.module != null) {
            this.module.accept(classVisitor);
        }
        if (this.nestHostClass != null) {
            classVisitor.visitNestHost(this.nestHostClass);
        }
        if (this.outerClass != null) {
            classVisitor.visitOuterClass(this.outerClass, this.outerMethod, this.outerMethodDesc);
        }
        if (this.visibleAnnotations != null) {
            for (int i = 0, n = this.visibleAnnotations.size(); i < n; ++i) {
                final AnnotationNode annotation = this.visibleAnnotations.get(i);
                annotation.accept(classVisitor.visitAnnotation(annotation.desc, true));
            }
        }
        if (this.invisibleAnnotations != null) {
            for (int i = 0, n = this.invisibleAnnotations.size(); i < n; ++i) {
                final AnnotationNode annotation = this.invisibleAnnotations.get(i);
                annotation.accept(classVisitor.visitAnnotation(annotation.desc, false));
            }
        }
        if (this.visibleTypeAnnotations != null) {
            for (int i = 0, n = this.visibleTypeAnnotations.size(); i < n; ++i) {
                final TypeAnnotationNode typeAnnotation = this.visibleTypeAnnotations.get(i);
                typeAnnotation.accept(classVisitor.visitTypeAnnotation(typeAnnotation.typeRef, typeAnnotation.typePath, typeAnnotation.desc, true));
            }
        }
        if (this.invisibleTypeAnnotations != null) {
            for (int i = 0, n = this.invisibleTypeAnnotations.size(); i < n; ++i) {
                final TypeAnnotationNode typeAnnotation = this.invisibleTypeAnnotations.get(i);
                typeAnnotation.accept(classVisitor.visitTypeAnnotation(typeAnnotation.typeRef, typeAnnotation.typePath, typeAnnotation.desc, false));
            }
        }
        if (this.attrs != null) {
            for (int i = 0, n = this.attrs.size(); i < n; ++i) {
                classVisitor.visitAttribute(this.attrs.get(i));
            }
        }
        if (this.nestMembers != null) {
            for (int i = 0, n = this.nestMembers.size(); i < n; ++i) {
                classVisitor.visitNestMember(this.nestMembers.get(i));
            }
        }
        for (int i = 0, n = this.innerClasses.size(); i < n; ++i) {
            this.innerClasses.get(i).accept(classVisitor);
        }
        for (int i = 0, n = this.fields.size(); i < n; ++i) {
            this.fields.get(i).accept(classVisitor);
        }
        for (int i = 0, n = this.methods.size(); i < n; ++i) {
            this.methods.get(i).accept(classVisitor);
        }
        classVisitor.visitEnd();
    }
}
