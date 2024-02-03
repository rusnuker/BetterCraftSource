// 
// Decompiled by Procyon v0.6.0
// 

package org.spongepowered.asm.mixin.transformer;

import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.util.Bytecode;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.gen.Invoker;
import org.spongepowered.asm.mixin.gen.Accessor;
import java.lang.annotation.Annotation;
import org.spongepowered.asm.util.Annotations;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.util.Locals;
import org.objectweb.asm.tree.FrameNode;
import org.spongepowered.asm.logging.Level;
import org.spongepowered.asm.service.MixinService;
import org.objectweb.asm.tree.FieldInsnNode;
import org.objectweb.asm.tree.MethodInsnNode;
import com.google.common.collect.ImmutableList;
import java.util.ArrayList;
import com.google.common.base.Strings;
import org.objectweb.asm.Type;
import org.spongepowered.asm.mixin.extensibility.IMixinInfo;
import java.util.List;
import java.util.Iterator;
import java.util.LinkedHashSet;
import org.spongepowered.asm.util.asm.ClassNodeAdapter;
import org.objectweb.asm.tree.InnerClassNode;
import org.spongepowered.asm.mixin.MixinEnvironment;
import org.objectweb.asm.tree.FieldNode;
import org.objectweb.asm.tree.MethodNode;
import java.util.Collection;
import java.util.HashSet;
import org.objectweb.asm.tree.ClassNode;
import java.util.Collections;
import com.google.common.collect.ImmutableSet;
import java.util.HashMap;
import org.spongepowered.asm.util.ClassSignature;
import java.util.Set;
import java.util.Map;
import org.spongepowered.asm.util.perf.Profiler;
import org.spongepowered.asm.logging.ILogger;

public final class ClassInfo
{
    public static final int INCLUDE_PRIVATE = 2;
    public static final int INCLUDE_STATIC = 8;
    public static final int INCLUDE_ALL = 10;
    public static final int INCLUDE_INITIALISERS = 262144;
    private static final ILogger logger;
    private static final Profiler profiler;
    private static final String JAVA_LANG_OBJECT = "java/lang/Object";
    private static final Map<String, ClassInfo> cache;
    private static final ClassInfo OBJECT;
    private final String name;
    private final String superName;
    private final String outerName;
    private final boolean isInner;
    private final boolean isProbablyStatic;
    private final Set<String> interfaces;
    private final Set<Method> initialisers;
    private final Set<Method> methods;
    private final Set<Field> fields;
    private final Set<MixinInfo> mixins;
    private final Map<ClassInfo, ClassInfo> correspondingTypes;
    private final MixinInfo mixin;
    private final MethodMapper methodMapper;
    private final boolean isMixin;
    private final boolean isInterface;
    private final int access;
    private ClassInfo superClass;
    private ClassInfo outerClass;
    private ClassSignature signature;
    private Set<MixinInfo> appliedMixins;
    private String nestHost;
    private Set<String> nestMembers;
    
    private ClassInfo() {
        this.correspondingTypes = new HashMap<ClassInfo, ClassInfo>();
        this.name = "java/lang/Object";
        this.superName = null;
        this.outerName = null;
        this.isInner = false;
        this.isProbablyStatic = true;
        this.initialisers = ImmutableSet.of(new Method("<init>", "()V"));
        this.methods = ImmutableSet.of(new Method("getClass", "()Ljava/lang/Class;"), new Method("hashCode", "()I"), new Method("equals", "(Ljava/lang/Object;)Z"), new Method("clone", "()Ljava/lang/Object;"), new Method("toString", "()Ljava/lang/String;"), new Method("notify", "()V"), new Method("notifyAll", "()V"), new Method("wait", "(J)V"), new Method("wait", "(JI)V"), new Method("wait", "()V"), new Method("finalize", "()V"));
        this.fields = Collections.emptySet();
        this.isInterface = false;
        this.interfaces = Collections.emptySet();
        this.access = 1;
        this.isMixin = false;
        this.mixin = null;
        this.mixins = Collections.emptySet();
        this.methodMapper = null;
    }
    
    private ClassInfo(final ClassNode classNode) {
        this.correspondingTypes = new HashMap<ClassInfo, ClassInfo>();
        final Profiler.Section timer = ClassInfo.profiler.begin(1, "class.meta");
        try {
            this.name = classNode.name;
            this.superName = ((classNode.superName != null) ? classNode.superName : "java/lang/Object");
            this.initialisers = new HashSet<Method>();
            this.methods = new HashSet<Method>();
            this.fields = new HashSet<Field>();
            this.isInterface = ((classNode.access & 0x200) != 0x0);
            this.interfaces = new HashSet<String>();
            this.isMixin = (classNode instanceof MixinInfo.MixinClassNode);
            this.mixin = (this.isMixin ? ((MixinInfo.MixinClassNode)classNode).getMixin() : null);
            this.mixins = (this.isMixin ? Collections.emptySet() : new HashSet<MixinInfo>());
            this.interfaces.addAll(classNode.interfaces);
            for (final MethodNode method : classNode.methods) {
                this.addMethod(method, this.isMixin);
            }
            boolean isProbablyStatic = true;
            String outerName = classNode.outerClass;
            for (final FieldNode field : classNode.fields) {
                if ((field.access & 0x1000) != 0x0 && field.name.startsWith("this$")) {
                    isProbablyStatic = false;
                    if (outerName == null) {
                        outerName = field.desc;
                        if (outerName != null && outerName.startsWith("L") && outerName.endsWith(";")) {
                            outerName = outerName.substring(1, outerName.length() - 1);
                        }
                    }
                }
                this.fields.add(new Field(field, this.isMixin));
            }
            this.isProbablyStatic = isProbablyStatic;
            this.methodMapper = new MethodMapper(MixinEnvironment.getCurrentEnvironment(), this);
            this.signature = ClassSignature.ofLazy(classNode);
            int access = classNode.access;
            boolean isInner = outerName != null;
            for (final InnerClassNode innerClass : classNode.innerClasses) {
                if (this.name.equals(innerClass.name)) {
                    access = innerClass.access;
                    isInner = true;
                    outerName = innerClass.outerName;
                }
            }
            this.access = access;
            this.isInner = isInner;
            this.outerName = outerName;
            if (MixinEnvironment.getCompatibilityLevel().supports(8)) {
                this.nestHost = ClassNodeAdapter.getNestHostClass(classNode);
                final List<String> nestMembers = ClassNodeAdapter.getNestMembers(classNode);
                if (nestMembers != null) {
                    (this.nestMembers = new LinkedHashSet<String>()).addAll(nestMembers);
                }
            }
        }
        finally {
            timer.end();
        }
    }
    
    void addInterface(final String iface) {
        this.interfaces.add(iface);
        this.getSignature().addInterface(iface);
    }
    
    void addMethod(final MethodNode method) {
        this.addMethod(method, true);
    }
    
    private void addMethod(final MethodNode method, final boolean injected) {
        if (method.name.startsWith("<")) {
            this.initialisers.add(new Method(method, injected));
        }
        else {
            this.methods.add(new Method(method, injected));
        }
    }
    
    void addMixin(final MixinInfo mixin) {
        if (this.isMixin) {
            throw new IllegalArgumentException("Cannot add target " + this.name + " for " + mixin.getClassName() + " because the target is a mixin");
        }
        this.mixins.add(mixin);
    }
    
    void addAppliedMixin(final MixinInfo mixin) {
        if (this.appliedMixins == null) {
            this.appliedMixins = new HashSet<MixinInfo>();
        }
        this.appliedMixins.add(mixin);
    }
    
    Set<MixinInfo> getMixins() {
        return this.isMixin ? Collections.emptySet() : Collections.unmodifiableSet((Set<? extends MixinInfo>)this.mixins);
    }
    
    public Set<IMixinInfo> getAppliedMixins() {
        return (this.appliedMixins != null) ? Collections.unmodifiableSet((Set<? extends IMixinInfo>)this.appliedMixins) : Collections.emptySet();
    }
    
    public boolean isMixin() {
        return this.isMixin;
    }
    
    public boolean isLoadable() {
        return this.mixin != null && this.mixin.isLoadable();
    }
    
    public boolean isPublic() {
        return (this.access & 0x1) != 0x0;
    }
    
    public boolean isReallyPublic() {
        final boolean isPublic = this.isPublic();
        if (!this.isInner || !isPublic) {
            return isPublic;
        }
        ClassInfo outer = this;
        while (outer != null && outer.outerName != null) {
            outer = forName(outer.outerName);
            if (outer != null && !outer.isPublic()) {
                return false;
            }
        }
        return true;
    }
    
    public boolean isProtected() {
        return (this.access & 0x4) != 0x0;
    }
    
    public boolean isPrivate() {
        return (this.access & 0x2) != 0x0;
    }
    
    public boolean isAbstract() {
        return (this.access & 0x400) != 0x0;
    }
    
    public boolean isSynthetic() {
        return (this.access & 0x1000) != 0x0;
    }
    
    public boolean isProbablyStatic() {
        return this.isProbablyStatic;
    }
    
    public boolean isInner() {
        return this.isInner;
    }
    
    public boolean isInterface() {
        return this.isInterface;
    }
    
    public Set<String> getInterfaces() {
        return Collections.unmodifiableSet((Set<? extends String>)this.interfaces);
    }
    
    @Override
    public String toString() {
        return this.name;
    }
    
    MethodMapper getMethodMapper() {
        return this.methodMapper;
    }
    
    public int getAccess() {
        return this.access;
    }
    
    public String getName() {
        return this.name;
    }
    
    public String getClassName() {
        return this.name.replace('/', '.');
    }
    
    public String getSimpleName() {
        final int pos = this.name.lastIndexOf(47);
        return (pos < 0) ? this.name : this.name.substring(pos + 1);
    }
    
    public Type getType() {
        return Type.getObjectType(this.name);
    }
    
    public String getSuperName() {
        return this.superName;
    }
    
    public ClassInfo getSuperClass() {
        if (this.superClass == null && this.superName != null) {
            this.superClass = forName(this.superName);
        }
        return this.superClass;
    }
    
    public String getOuterName() {
        return this.outerName;
    }
    
    public ClassInfo getOuterClass() {
        if (this.outerClass == null && this.outerName != null) {
            this.outerClass = forName(this.outerName);
        }
        return this.outerClass;
    }
    
    public ClassSignature getSignature() {
        return this.signature.wake();
    }
    
    public String getNestHost() {
        return this.nestHost;
    }
    
    public Set<String> getNestMembers() {
        return (this.nestMembers != null) ? Collections.unmodifiableSet((Set<? extends String>)this.nestMembers) : Collections.emptySet();
    }
    
    public ClassInfo resolveNestHost() {
        if (!Strings.isNullOrEmpty(this.nestHost)) {
            return forName(this.nestHost);
        }
        return this;
    }
    
    List<ClassInfo> getTargets() {
        if (this.mixin != null) {
            final List<ClassInfo> targets = new ArrayList<ClassInfo>();
            targets.add(this);
            targets.addAll(this.mixin.getTargets());
            return targets;
        }
        return ImmutableList.of(this);
    }
    
    public Set<Method> getMethods() {
        return Collections.unmodifiableSet((Set<? extends Method>)this.methods);
    }
    
    public Set<Method> getInterfaceMethods(final boolean includeMixins) {
        final Set<Method> methods = new HashSet<Method>();
        ClassInfo supClass = this.addMethodsRecursive(methods, includeMixins);
        if (!this.isInterface) {
            while (supClass != null && supClass != ClassInfo.OBJECT) {
                supClass = supClass.addMethodsRecursive(methods, includeMixins);
            }
        }
        final Iterator<Method> it = methods.iterator();
        while (it.hasNext()) {
            if (!it.next().isAbstract()) {
                it.remove();
            }
        }
        return Collections.unmodifiableSet((Set<? extends Method>)methods);
    }
    
    private ClassInfo addMethodsRecursive(final Set<Method> methods, final boolean includeMixins) {
        if (this.isInterface) {
            for (final Method method : this.methods) {
                if (!method.isAbstract()) {
                    methods.remove(method);
                }
                methods.add(method);
            }
        }
        else if (!this.isMixin && includeMixins) {
            for (final MixinInfo mixin : this.mixins) {
                mixin.getClassInfo().addMethodsRecursive(methods, includeMixins);
            }
        }
        for (final String iface : this.interfaces) {
            forName(iface).addMethodsRecursive(methods, includeMixins);
        }
        return this.getSuperClass();
    }
    
    public boolean hasSuperClass(final Class<?> superClass) {
        return this.hasSuperClass(superClass, Traversal.NONE, superClass.isInterface());
    }
    
    public boolean hasSuperClass(final Class<?> superClass, final Traversal traversal) {
        return this.hasSuperClass(superClass, traversal, superClass.isInterface());
    }
    
    public boolean hasSuperClass(final Class<?> superClass, final Traversal traversal, final boolean includeInterfaces) {
        final String internalName = Type.getInternalName(superClass);
        return "java/lang/Object".equals(internalName) || this.findSuperClass(internalName, traversal) != null;
    }
    
    public boolean hasSuperClass(final String superClass) {
        return this.hasSuperClass(superClass, Traversal.NONE, false);
    }
    
    public boolean hasSuperClass(final String superClass, final Traversal traversal) {
        return this.hasSuperClass(superClass, traversal, false);
    }
    
    public boolean hasSuperClass(final String superClass, final Traversal traversal, final boolean includeInterfaces) {
        return "java/lang/Object".equals(superClass) || this.findSuperClass(superClass, traversal) != null;
    }
    
    public boolean hasSuperClass(final ClassInfo superClass) {
        return this.hasSuperClass(superClass, Traversal.NONE, false);
    }
    
    public boolean hasSuperClass(final ClassInfo superClass, final Traversal traversal) {
        return this.hasSuperClass(superClass, traversal, false);
    }
    
    public boolean hasSuperClass(final ClassInfo superClass, final Traversal traversal, final boolean includeInterfaces) {
        return ClassInfo.OBJECT == superClass || this.findSuperClass(superClass.name, traversal, includeInterfaces) != null;
    }
    
    public ClassInfo findSuperClass(final String superClass) {
        return this.findSuperClass(superClass, Traversal.NONE);
    }
    
    public ClassInfo findSuperClass(final String superClass, final Traversal traversal) {
        return this.findSuperClass(superClass, traversal, false, new HashSet<String>());
    }
    
    public ClassInfo findSuperClass(final String superClass, final Traversal traversal, final boolean includeInterfaces) {
        if (ClassInfo.OBJECT.name.equals(superClass)) {
            return null;
        }
        return this.findSuperClass(superClass, traversal, includeInterfaces, new HashSet<String>());
    }
    
    private ClassInfo findSuperClass(final String superClass, final Traversal traversal, final boolean includeInterfaces, final Set<String> traversed) {
        final ClassInfo superClassInfo = this.getSuperClass();
        if (superClassInfo != null) {
            for (final ClassInfo superTarget : superClassInfo.getTargets()) {
                if (superClass.equals(superTarget.getName())) {
                    return superClassInfo;
                }
                final ClassInfo found = superTarget.findSuperClass(superClass, traversal.next(), includeInterfaces, traversed);
                if (found != null) {
                    return found;
                }
            }
        }
        if (includeInterfaces) {
            final ClassInfo iface = this.findInterface(superClass);
            if (iface != null) {
                return iface;
            }
        }
        if (traversal.canTraverse()) {
            for (final MixinInfo mixin : this.mixins) {
                final String mixinClassName = mixin.getClassName();
                if (traversed.contains(mixinClassName)) {
                    continue;
                }
                traversed.add(mixinClassName);
                final ClassInfo mixinClass = mixin.getClassInfo();
                if (superClass.equals(mixinClass.getName())) {
                    return mixinClass;
                }
                final ClassInfo targetSuper = mixinClass.findSuperClass(superClass, Traversal.ALL, includeInterfaces, traversed);
                if (targetSuper != null) {
                    return targetSuper;
                }
            }
        }
        return null;
    }
    
    private ClassInfo findInterface(final String superClass) {
        for (final String ifaceName : this.getInterfaces()) {
            final ClassInfo iface = forName(ifaceName);
            if (superClass.equals(ifaceName)) {
                return iface;
            }
            final ClassInfo superIface = iface.findInterface(superClass);
            if (superIface != null) {
                return superIface;
            }
        }
        return null;
    }
    
    ClassInfo findCorrespondingType(final ClassInfo mixin) {
        if (mixin == null || !mixin.isMixin || this.isMixin) {
            return null;
        }
        ClassInfo correspondingType = this.correspondingTypes.get(mixin);
        if (correspondingType == null) {
            correspondingType = this.findSuperTypeForMixin(mixin);
            this.correspondingTypes.put(mixin, correspondingType);
        }
        return correspondingType;
    }
    
    private ClassInfo findSuperTypeForMixin(final ClassInfo mixin) {
        for (ClassInfo superClass = this; superClass != null && superClass != ClassInfo.OBJECT; superClass = superClass.getSuperClass()) {
            for (final MixinInfo minion : superClass.mixins) {
                if (minion.getClassInfo().equals(mixin)) {
                    return superClass;
                }
            }
        }
        return null;
    }
    
    public boolean hasMixinInHierarchy() {
        if (!this.isMixin) {
            return false;
        }
        for (ClassInfo supClass = this.getSuperClass(); supClass != null && supClass != ClassInfo.OBJECT; supClass = supClass.getSuperClass()) {
            if (supClass.isMixin) {
                return true;
            }
        }
        return false;
    }
    
    public boolean hasMixinTargetInHierarchy() {
        if (this.isMixin) {
            return false;
        }
        for (ClassInfo supClass = this.getSuperClass(); supClass != null && supClass != ClassInfo.OBJECT; supClass = supClass.getSuperClass()) {
            if (supClass.mixins.size() > 0) {
                return true;
            }
        }
        return false;
    }
    
    public Method findMethodInHierarchy(final MethodNode method, final SearchType searchType) {
        return this.findMethodInHierarchy(method.name, method.desc, searchType, Traversal.NONE);
    }
    
    public Method findMethodInHierarchy(final MethodNode method, final SearchType searchType, final Traversal traversal) {
        return this.findMethodInHierarchy(method.name, method.desc, searchType, traversal, 0);
    }
    
    public Method findMethodInHierarchy(final MethodNode method, final SearchType searchType, final int flags) {
        return this.findMethodInHierarchy(method.name, method.desc, searchType, Traversal.NONE, flags);
    }
    
    public Method findMethodInHierarchy(final MethodNode method, final SearchType searchType, final Traversal traversal, final int flags) {
        return this.findMethodInHierarchy(method.name, method.desc, searchType, traversal, flags);
    }
    
    public Method findMethodInHierarchy(final MethodInsnNode method, final SearchType searchType) {
        return this.findMethodInHierarchy(method.name, method.desc, searchType, Traversal.NONE);
    }
    
    public Method findMethodInHierarchy(final MethodInsnNode method, final SearchType searchType, final int flags) {
        return this.findMethodInHierarchy(method.name, method.desc, searchType, Traversal.NONE, flags);
    }
    
    public Method findMethodInHierarchy(final String name, final String desc, final SearchType searchType) {
        return this.findMethodInHierarchy(name, desc, searchType, Traversal.NONE);
    }
    
    public Method findMethodInHierarchy(final String name, final String desc, final SearchType searchType, final Traversal traversal) {
        return this.findMethodInHierarchy(name, desc, searchType, traversal, 0);
    }
    
    public Method findMethodInHierarchy(final String name, final String desc, final SearchType searchType, final Traversal traversal, final int flags) {
        return this.findInHierarchy(name, desc, searchType, traversal, flags, Member.Type.METHOD);
    }
    
    public Field findFieldInHierarchy(final FieldNode field, final SearchType searchType) {
        return this.findFieldInHierarchy(field.name, field.desc, searchType, Traversal.NONE);
    }
    
    public Field findFieldInHierarchy(final FieldNode field, final SearchType searchType, final int flags) {
        return this.findFieldInHierarchy(field.name, field.desc, searchType, Traversal.NONE, flags);
    }
    
    public Field findFieldInHierarchy(final FieldInsnNode field, final SearchType searchType) {
        return this.findFieldInHierarchy(field.name, field.desc, searchType, Traversal.NONE);
    }
    
    public Field findFieldInHierarchy(final FieldInsnNode field, final SearchType searchType, final int flags) {
        return this.findFieldInHierarchy(field.name, field.desc, searchType, Traversal.NONE, flags);
    }
    
    public Field findFieldInHierarchy(final String name, final String desc, final SearchType searchType) {
        return this.findFieldInHierarchy(name, desc, searchType, Traversal.NONE);
    }
    
    public Field findFieldInHierarchy(final String name, final String desc, final SearchType searchType, final Traversal traversal) {
        return this.findFieldInHierarchy(name, desc, searchType, traversal, 0);
    }
    
    public Field findFieldInHierarchy(final String name, final String desc, final SearchType searchType, final Traversal traversal, final int flags) {
        return this.findInHierarchy(name, desc, searchType, traversal, flags, Member.Type.FIELD);
    }
    
    private <M extends Member> M findInHierarchy(final String name, final String desc, final SearchType searchType, final Traversal traversal, final int flags, final Member.Type type) {
        if (searchType == SearchType.ALL_CLASSES) {
            final M member = this.findMember(name, desc, flags, type);
            if (member != null) {
                return member;
            }
            if (traversal.canTraverse()) {
                for (final MixinInfo mixin : this.mixins) {
                    final M mixinMember = mixin.getClassInfo().findMember(name, desc, flags, type);
                    if (mixinMember != null) {
                        return this.cloneMember(mixinMember);
                    }
                }
            }
        }
        final ClassInfo superClassInfo = this.getSuperClass();
        if (superClassInfo != null) {
            for (final ClassInfo superTarget : superClassInfo.getTargets()) {
                final M member2 = (M)superTarget.findInHierarchy(name, desc, SearchType.ALL_CLASSES, traversal.next(), flags & 0xFFFFFFFD, type);
                if (member2 != null) {
                    return member2;
                }
            }
        }
        if (type == Member.Type.METHOD && (this.isInterface || MixinEnvironment.getCompatibilityLevel().supports(1))) {
            for (final String implemented : this.interfaces) {
                final ClassInfo iface = forName(implemented);
                if (iface == null) {
                    ClassInfo.logger.debug("Failed to resolve declared interface {} on {}", implemented, this.name);
                }
                else {
                    final M member3 = (M)iface.findInHierarchy(name, desc, SearchType.ALL_CLASSES, traversal.next(), flags & 0xFFFFFFFD, type);
                    if (member3 != null) {
                        return (M)(this.isInterface ? member3 : new InterfaceMethod(member3));
                    }
                    continue;
                }
            }
        }
        return null;
    }
    
    private <M extends Member> M cloneMember(final M member) {
        if (member instanceof Method) {
            return (M)new Method(member);
        }
        return (M)new Field(member);
    }
    
    public Method findMethod(final MethodNode method) {
        return this.findMethod(method.name, method.desc, method.access);
    }
    
    public Method findMethod(final MethodNode method, final int flags) {
        return this.findMethod(method.name, method.desc, flags);
    }
    
    public Method findMethod(final MethodInsnNode method) {
        return this.findMethod(method.name, method.desc, 0);
    }
    
    public Method findMethod(final MethodInsnNode method, final int flags) {
        return this.findMethod(method.name, method.desc, flags);
    }
    
    public Method findMethod(final String name, final String desc, final int flags) {
        return this.findMember(name, desc, flags, Member.Type.METHOD);
    }
    
    public Field findField(final FieldNode field) {
        return this.findField(field.name, field.desc, field.access);
    }
    
    public Field findField(final FieldInsnNode field, final int flags) {
        return this.findField(field.name, field.desc, flags);
    }
    
    public Field findField(final String name, final String desc, final int flags) {
        return this.findMember(name, desc, flags, Member.Type.FIELD);
    }
    
    private <M extends Member> M findMember(final String name, final String desc, final int flags, final Member.Type memberType) {
        final Set<M> members = (Set<M>)((memberType == Member.Type.METHOD) ? this.methods : this.fields);
        for (final M member : members) {
            if (member.equals(name, desc) && member.matchesFlags(flags)) {
                return member;
            }
        }
        if (memberType == Member.Type.METHOD && (flags & 0x40000) != 0x0) {
            for (final Method ctor : this.initialisers) {
                if (ctor.equals(name, desc) && ctor.matchesFlags(flags)) {
                    return (M)ctor;
                }
            }
        }
        return null;
    }
    
    @Override
    public boolean equals(final Object other) {
        return other instanceof ClassInfo && ((ClassInfo)other).name.equals(this.name);
    }
    
    @Override
    public int hashCode() {
        return this.name.hashCode();
    }
    
    static ClassInfo fromClassNode(final ClassNode classNode) {
        ClassInfo info = ClassInfo.cache.get(classNode.name);
        if (info == null) {
            info = new ClassInfo(classNode);
            ClassInfo.cache.put(classNode.name, info);
        }
        return info;
    }
    
    public static ClassInfo forName(String className) {
        className = className.replace('.', '/');
        ClassInfo info = ClassInfo.cache.get(className);
        if (info == null) {
            try {
                final ClassNode classNode = MixinService.getService().getBytecodeProvider().getClassNode(className);
                info = new ClassInfo(classNode);
            }
            catch (final Exception ex) {
                ClassInfo.logger.catching(Level.TRACE, ex);
                ClassInfo.logger.warn("Error loading class: {} ({}: {})", className, ex.getClass().getName(), ex.getMessage());
            }
            ClassInfo.cache.put(className, info);
            ClassInfo.logger.trace("Added class metadata for {} to metadata cache", className);
        }
        return info;
    }
    
    public static ClassInfo forDescriptor(final String descriptor, final TypeLookup lookup) {
        Type type;
        try {
            type = Type.getObjectType(descriptor);
        }
        catch (final IllegalArgumentException ex) {
            ClassInfo.logger.warn("Error resolving type from descriptor: {}", descriptor);
            return null;
        }
        return forType(type, lookup);
    }
    
    public static ClassInfo forType(final Type type, final TypeLookup lookup) {
        if (type.getSort() == 9) {
            if (lookup == TypeLookup.ELEMENT_TYPE) {
                return forType(type.getElementType(), TypeLookup.ELEMENT_TYPE);
            }
            return ClassInfo.OBJECT;
        }
        else {
            if (type.getSort() < 9) {
                return null;
            }
            return forName(type.getClassName().replace('.', '/'));
        }
    }
    
    public static ClassInfo fromCache(final String className) {
        return ClassInfo.cache.get(className.replace('.', '/'));
    }
    
    public static ClassInfo fromCache(final Type type, final TypeLookup lookup) {
        if (type.getSort() == 9) {
            if (lookup == TypeLookup.ELEMENT_TYPE) {
                return fromCache(type.getElementType(), TypeLookup.ELEMENT_TYPE);
            }
            return ClassInfo.OBJECT;
        }
        else {
            if (type.getSort() < 9) {
                return null;
            }
            return fromCache(type.getClassName());
        }
    }
    
    public static ClassInfo getCommonSuperClass(final String type1, final String type2) {
        if (type1 == null || type2 == null) {
            return ClassInfo.OBJECT;
        }
        return getCommonSuperClass(forName(type1), forName(type2));
    }
    
    public static ClassInfo getCommonSuperClass(final Type type1, final Type type2) {
        if (type1 == null || type2 == null || type1.getSort() != 10 || type2.getSort() != 10) {
            return ClassInfo.OBJECT;
        }
        return getCommonSuperClass(forType(type1, TypeLookup.DECLARED_TYPE), forType(type2, TypeLookup.DECLARED_TYPE));
    }
    
    private static ClassInfo getCommonSuperClass(final ClassInfo type1, final ClassInfo type2) {
        if (type1 == null || type2 == null) {
            return ClassInfo.OBJECT;
        }
        return getCommonSuperClass(type1, type2, false);
    }
    
    public static ClassInfo getCommonSuperClassOrInterface(final String type1, final String type2) {
        if (type1 == null || type2 == null) {
            return ClassInfo.OBJECT;
        }
        return getCommonSuperClassOrInterface(forName(type1), forName(type2));
    }
    
    public static ClassInfo getCommonSuperClassOrInterface(final Type type1, final Type type2) {
        if (type1 == null || type2 == null || type1.getSort() != 10 || type2.getSort() != 10) {
            return ClassInfo.OBJECT;
        }
        return getCommonSuperClassOrInterface(forType(type1, TypeLookup.DECLARED_TYPE), forType(type2, TypeLookup.DECLARED_TYPE));
    }
    
    public static ClassInfo getCommonSuperClassOrInterface(final ClassInfo type1, final ClassInfo type2) {
        return getCommonSuperClass(type1, type2, true);
    }
    
    private static ClassInfo getCommonSuperClass(ClassInfo type1, final ClassInfo type2, final boolean includeInterfaces) {
        if (type1.hasSuperClass(type2, Traversal.NONE, includeInterfaces)) {
            return type2;
        }
        if (type2.hasSuperClass(type1, Traversal.NONE, includeInterfaces)) {
            return type1;
        }
        if (type1.isInterface() || type2.isInterface()) {
            return ClassInfo.OBJECT;
        }
        do {
            type1 = type1.getSuperClass();
            if (type1 == null) {
                return ClassInfo.OBJECT;
            }
        } while (!type2.hasSuperClass(type1, Traversal.NONE, includeInterfaces));
        return type1;
    }
    
    static {
        logger = MixinService.getService().getLogger("mixin");
        profiler = Profiler.getProfiler("meta");
        cache = new HashMap<String, ClassInfo>();
        OBJECT = new ClassInfo();
        ClassInfo.cache.put("java/lang/Object", ClassInfo.OBJECT);
    }
    
    public enum SearchType
    {
        ALL_CLASSES, 
        SUPER_CLASSES_ONLY;
    }
    
    public enum TypeLookup
    {
        DECLARED_TYPE, 
        ELEMENT_TYPE;
    }
    
    public enum Traversal
    {
        NONE((Traversal)null, false, SearchType.SUPER_CLASSES_ONLY), 
        ALL((Traversal)null, true, SearchType.ALL_CLASSES), 
        IMMEDIATE(Traversal.NONE, true, SearchType.SUPER_CLASSES_ONLY), 
        SUPER(Traversal.ALL, false, SearchType.SUPER_CLASSES_ONLY);
        
        private final Traversal next;
        private final boolean traverse;
        private final SearchType searchType;
        
        private Traversal(final Traversal next, final boolean traverse, final SearchType searchType) {
            this.next = ((next != null) ? next : this);
            this.traverse = traverse;
            this.searchType = searchType;
        }
        
        public Traversal next() {
            return this.next;
        }
        
        public boolean canTraverse() {
            return this.traverse;
        }
        
        public SearchType getSearchType() {
            return this.searchType;
        }
    }
    
    public static class FrameData
    {
        private static final String[] FRAMETYPES;
        public final int index;
        public final int type;
        public final int locals;
        public final int size;
        
        FrameData(final int index, final int type, final int locals, final int size) {
            this.index = index;
            this.type = type;
            this.locals = locals;
            this.size = size;
        }
        
        FrameData(final int index, final FrameNode frameNode, final int initialFrameSize) {
            this.index = index;
            this.type = frameNode.type;
            this.locals = ((frameNode.local != null) ? frameNode.local.size() : 0);
            this.size = Locals.computeFrameSize(frameNode, initialFrameSize);
        }
        
        @Override
        public String toString() {
            return String.format("FrameData[index=%d, type=%s, locals=%d size=%d]", this.index, FrameData.FRAMETYPES[this.type + 1], this.locals, this.size);
        }
        
        static {
            FRAMETYPES = new String[] { "NEW", "FULL", "APPEND", "CHOP", "SAME", "SAME1" };
        }
    }
    
    abstract static class Member
    {
        private final Type type;
        private final String memberName;
        private final String memberDesc;
        private final boolean isInjected;
        private final int modifiers;
        private String currentName;
        private String currentDesc;
        private boolean decoratedFinal;
        private boolean decoratedMutable;
        private boolean unique;
        
        protected Member(final Member member) {
            this(member.type, member.memberName, member.memberDesc, member.modifiers, member.isInjected);
            this.currentName = member.currentName;
            this.currentDesc = member.currentDesc;
            this.unique = member.unique;
        }
        
        protected Member(final Type type, final String name, final String desc, final int access) {
            this(type, name, desc, access, false);
        }
        
        protected Member(final Type type, final String name, final String desc, final int access, final boolean injected) {
            this.type = type;
            this.memberName = name;
            this.memberDesc = desc;
            this.isInjected = injected;
            this.currentName = name;
            this.currentDesc = desc;
            this.modifiers = access;
        }
        
        public String getOriginalName() {
            return this.memberName;
        }
        
        public String getName() {
            return this.currentName;
        }
        
        public String getOriginalDesc() {
            return this.memberDesc;
        }
        
        public String getDesc() {
            return this.currentDesc;
        }
        
        public boolean isInjected() {
            return this.isInjected;
        }
        
        public boolean isRenamed() {
            return !this.currentName.equals(this.memberName);
        }
        
        public boolean isRemapped() {
            return !this.currentDesc.equals(this.memberDesc);
        }
        
        public boolean isPrivate() {
            return (this.modifiers & 0x2) != 0x0;
        }
        
        public boolean isStatic() {
            return (this.modifiers & 0x8) != 0x0;
        }
        
        public boolean isAbstract() {
            return (this.modifiers & 0x400) != 0x0;
        }
        
        public boolean isFinal() {
            return (this.modifiers & 0x10) != 0x0;
        }
        
        public boolean isSynthetic() {
            return (this.modifiers & 0x1000) != 0x0;
        }
        
        public boolean isUnique() {
            return this.unique;
        }
        
        public void setUnique(final boolean unique) {
            this.unique = unique;
        }
        
        public boolean isDecoratedFinal() {
            return this.decoratedFinal;
        }
        
        public boolean isDecoratedMutable() {
            return this.decoratedMutable;
        }
        
        protected void setDecoratedFinal(final boolean decoratedFinal, final boolean decoratedMutable) {
            this.decoratedFinal = decoratedFinal;
            this.decoratedMutable = decoratedMutable;
        }
        
        public boolean matchesFlags(final int flags) {
            return ((~this.modifiers | (flags & 0x2)) & 0x2) != 0x0 && ((~this.modifiers | (flags & 0x8)) & 0x8) != 0x0;
        }
        
        public abstract ClassInfo getOwner();
        
        public ClassInfo getImplementor() {
            return this.getOwner();
        }
        
        public int getAccess() {
            return this.modifiers;
        }
        
        public String renameTo(final String name) {
            return this.currentName = name;
        }
        
        public String remapTo(final String desc) {
            return this.currentDesc = desc;
        }
        
        public boolean equals(final String name, final String desc) {
            return (this.memberName.equals(name) || this.currentName.equals(name)) && (this.memberDesc.equals(desc) || this.currentDesc.equals(desc));
        }
        
        @Override
        public boolean equals(final Object obj) {
            if (!(obj instanceof Member)) {
                return false;
            }
            final Member other = (Member)obj;
            return (other.memberName.equals(this.memberName) || other.currentName.equals(this.currentName)) && (other.memberDesc.equals(this.memberDesc) || other.currentDesc.equals(this.currentDesc));
        }
        
        @Override
        public int hashCode() {
            return this.toString().hashCode();
        }
        
        @Override
        public String toString() {
            return String.format(this.getDisplayFormat(), this.memberName, this.memberDesc);
        }
        
        protected String getDisplayFormat() {
            return "%s%s";
        }
        
        enum Type
        {
            METHOD, 
            FIELD;
        }
    }
    
    public class Method extends Member
    {
        private final List<FrameData> frames;
        private boolean isAccessor;
        private boolean conformed;
        
        public Method(final Member member) {
            super(member);
            this.frames = ((member instanceof Method) ? ((Method)member).frames : null);
        }
        
        public Method(final ClassInfo this$0, final MethodNode method) {
            this(this$0, method, false);
        }
        
        public Method(final MethodNode method, final boolean injected) {
            super(Type.METHOD, method.name, method.desc, method.access, injected);
            this.frames = this.gatherFrames(method);
            this.setUnique(Annotations.getVisible(method, Unique.class) != null);
            this.isAccessor = (Annotations.getSingleVisible(method, Accessor.class, Invoker.class) != null);
            final boolean decoratedFinal = Annotations.getVisible(method, Final.class) != null;
            final boolean decoratedMutable = Annotations.getVisible(method, Mutable.class) != null;
            this.setDecoratedFinal(decoratedFinal, decoratedMutable);
        }
        
        public Method(final String name, final String desc) {
            super(Type.METHOD, name, desc, 1, false);
            this.frames = null;
        }
        
        public Method(final String name, final String desc, final int access) {
            super(Type.METHOD, name, desc, access, false);
            this.frames = null;
        }
        
        public Method(final String name, final String desc, final int access, final boolean injected) {
            super(Type.METHOD, name, desc, access, injected);
            this.frames = null;
        }
        
        private List<FrameData> gatherFrames(final MethodNode method) {
            final List<FrameData> frames = new ArrayList<FrameData>();
            for (final AbstractInsnNode insn : method.instructions) {
                if (insn instanceof FrameNode) {
                    frames.add(new FrameData(method.instructions.indexOf(insn), (FrameNode)insn, Bytecode.getFirstNonArgLocalIndex(method)));
                }
            }
            return frames;
        }
        
        public List<FrameData> getFrames() {
            return this.frames;
        }
        
        @Override
        public ClassInfo getOwner() {
            return ClassInfo.this;
        }
        
        public boolean isAccessor() {
            return this.isAccessor;
        }
        
        public boolean isConformed() {
            return this.conformed;
        }
        
        @Override
        public String renameTo(final String name) {
            this.conformed = false;
            return super.renameTo(name);
        }
        
        public String conform(final String name) {
            final boolean nameChanged = !name.equals(this.getName());
            if (this.conformed && nameChanged) {
                throw new IllegalStateException("Method " + this + " was already conformed. Original= " + this.getOriginalName() + " Current=" + this.getName() + " New=" + name);
            }
            if (nameChanged) {
                this.renameTo(name);
                this.conformed = true;
            }
            return name;
        }
        
        @Override
        public boolean equals(final Object obj) {
            return obj instanceof Method && super.equals(obj);
        }
    }
    
    public class InterfaceMethod extends Method
    {
        private final ClassInfo owner;
        
        public InterfaceMethod(final Member member) {
            super(member);
            this.owner = member.getOwner();
        }
        
        @Override
        public ClassInfo getOwner() {
            return this.owner;
        }
        
        @Override
        public ClassInfo getImplementor() {
            return ClassInfo.this;
        }
    }
    
    public class Field extends Member
    {
        public Field(final Member member) {
            super(member);
        }
        
        public Field(final ClassInfo this$0, final FieldNode field) {
            this(this$0, field, false);
        }
        
        public Field(final FieldNode field, final boolean injected) {
            super(Type.FIELD, field.name, field.desc, field.access, injected);
            this.setUnique(Annotations.getVisible(field, Unique.class) != null);
            if (Annotations.getVisible(field, Shadow.class) != null) {
                final boolean decoratedFinal = Annotations.getVisible(field, Final.class) != null;
                final boolean decoratedMutable = Annotations.getVisible(field, Mutable.class) != null;
                this.setDecoratedFinal(decoratedFinal, decoratedMutable);
            }
        }
        
        public Field(final String name, final String desc, final int access) {
            super(Type.FIELD, name, desc, access, false);
        }
        
        public Field(final String name, final String desc, final int access, final boolean injected) {
            super(Type.FIELD, name, desc, access, injected);
        }
        
        @Override
        public ClassInfo getOwner() {
            return ClassInfo.this;
        }
        
        @Override
        public boolean equals(final Object obj) {
            return obj instanceof Field && super.equals(obj);
        }
        
        @Override
        protected String getDisplayFormat() {
            return "%s:%s";
        }
    }
}
