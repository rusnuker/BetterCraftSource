// 
// Decompiled by Procyon v0.6.0
// 

package org.spongepowered.asm.mixin.transformer;

import org.spongepowered.asm.service.MixinService;
import org.spongepowered.asm.mixin.injection.throwables.InjectionValidationException;
import org.spongepowered.asm.mixin.injection.throwables.InvalidInjectionException;
import org.spongepowered.asm.mixin.refmap.IReferenceMapper;
import org.spongepowered.asm.util.asm.ClassNodeAdapter;
import org.spongepowered.asm.logging.Level;
import java.util.Set;
import org.spongepowered.asm.mixin.transformer.ext.Extensions;
import org.spongepowered.asm.obfuscation.RemapperChain;
import java.util.Deque;
import java.util.Collection;
import java.util.LinkedList;
import org.objectweb.asm.tree.AnnotationNode;
import org.spongepowered.asm.mixin.injection.struct.Target;
import org.spongepowered.asm.mixin.throwables.ClassMetadataNotFoundException;
import org.spongepowered.asm.mixin.transformer.throwables.MixinTransformerError;
import org.objectweb.asm.ConstantDynamic;
import org.spongepowered.asm.util.asm.ASM;
import org.objectweb.asm.Handle;
import org.objectweb.asm.tree.VarInsnNode;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.LocalVariableNode;
import org.spongepowered.asm.mixin.extensibility.IMixinInfo;
import org.spongepowered.asm.mixin.SoftOverride;
import java.util.Iterator;
import org.spongepowered.asm.mixin.transformer.throwables.InvalidMixinException;
import org.spongepowered.asm.mixin.extensibility.IActivityContext;
import org.objectweb.asm.tree.InvokeDynamicInsnNode;
import org.objectweb.asm.tree.LdcInsnNode;
import org.objectweb.asm.tree.TypeInsnNode;
import org.objectweb.asm.tree.FieldInsnNode;
import org.spongepowered.asm.mixin.struct.MemberRef;
import org.objectweb.asm.tree.MethodInsnNode;
import org.spongepowered.asm.util.Bytecode;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.spongepowered.asm.util.ClassSignature;
import org.spongepowered.asm.util.Annotations;
import org.spongepowered.asm.mixin.transformer.meta.MixinMerged;
import java.lang.annotation.Annotation;
import org.spongepowered.asm.mixin.transformer.ext.IClassGenerator;
import org.spongepowered.asm.mixin.MixinEnvironment;
import java.util.LinkedHashMap;
import java.util.ArrayList;
import org.spongepowered.asm.mixin.struct.SourceMap;
import org.spongepowered.asm.mixin.gen.AccessorInfo;
import org.spongepowered.asm.mixin.injection.struct.InjectionInfo;
import org.spongepowered.asm.mixin.injection.struct.InjectorGroupInfo;
import org.objectweb.asm.tree.FieldNode;
import java.util.Map;
import org.objectweb.asm.tree.MethodNode;
import java.util.List;
import com.google.common.collect.BiMap;
import org.objectweb.asm.tree.ClassNode;
import org.spongepowered.asm.logging.ILogger;
import org.spongepowered.asm.mixin.refmap.IMixinContext;

public class MixinTargetContext extends ClassContext implements IMixinContext
{
    private static final ILogger logger;
    protected final ActivityStack activities;
    private final MixinInfo mixin;
    private final ClassNode classNode;
    private final TargetClassContext targetClass;
    private final String sessionId;
    private final ClassInfo targetClassInfo;
    private final BiMap<String, String> innerClasses;
    private final List<MethodNode> shadowMethods;
    private final Map<FieldNode, ClassInfo.Field> shadowFields;
    private final List<MethodNode> mergedMethods;
    private final InjectorGroupInfo.Map injectorGroups;
    private final List<InjectionInfo> injectors;
    private final List<AccessorInfo> accessors;
    private final boolean inheritsFromMixin;
    private final boolean detachedSuper;
    private final SourceMap.File stratum;
    private int minRequiredClassVersion;
    
    MixinTargetContext(final MixinInfo mixin, final ClassNode classNode, final TargetClassContext context) {
        this.activities = new ActivityStack(null);
        this.shadowMethods = new ArrayList<MethodNode>();
        this.shadowFields = new LinkedHashMap<FieldNode, ClassInfo.Field>();
        this.mergedMethods = new ArrayList<MethodNode>();
        this.injectorGroups = new InjectorGroupInfo.Map();
        this.injectors = new ArrayList<InjectionInfo>();
        this.accessors = new ArrayList<AccessorInfo>();
        this.minRequiredClassVersion = MixinEnvironment.CompatibilityLevel.JAVA_6.getClassVersion();
        this.mixin = mixin;
        this.classNode = classNode;
        this.targetClass = context;
        this.targetClassInfo = context.getClassInfo();
        this.stratum = context.getSourceMap().addFile(this.classNode);
        this.inheritsFromMixin = (mixin.getClassInfo().hasMixinInHierarchy() || this.targetClassInfo.hasMixinTargetInHierarchy());
        this.detachedSuper = !this.classNode.superName.equals(this.getTarget().getClassNode().superName);
        this.sessionId = context.getSessionId();
        this.requireVersion(classNode.version);
        final InnerClassGenerator icg = context.getExtensions().getGenerator(InnerClassGenerator.class);
        this.innerClasses = icg.getInnerClasses(this.mixin, this.getTargetClassRef());
    }
    
    void addShadowMethod(final MethodNode method) {
        this.shadowMethods.add(method);
    }
    
    void addShadowField(final FieldNode fieldNode, final ClassInfo.Field fieldInfo) {
        this.shadowFields.put(fieldNode, fieldInfo);
    }
    
    void addAccessorMethod(final MethodNode method, final Class<? extends Annotation> type) {
        this.accessors.add(AccessorInfo.of(this, method, type));
    }
    
    void addMixinMethod(final MethodNode method) {
        Annotations.setVisible(method, MixinMerged.class, "mixin", this.getClassName());
        this.getTarget().addMixinMethod(method);
    }
    
    void methodMerged(final MethodNode method) {
        this.mergedMethods.add(method);
        this.targetClassInfo.addMethod(method);
        this.getTarget().methodMerged(method);
        Annotations.setVisible(method, MixinMerged.class, "mixin", this.getClassName(), "priority", this.getPriority(), "sessionId", this.sessionId);
    }
    
    @Override
    public String toString() {
        return this.mixin.toString();
    }
    
    public MixinEnvironment getEnvironment() {
        return this.mixin.getParent().getEnvironment();
    }
    
    @Override
    public boolean getOption(final MixinEnvironment.Option option) {
        return this.getEnvironment().getOption(option);
    }
    
    public ClassNode getClassNode() {
        return this.classNode;
    }
    
    @Override
    public String getClassName() {
        return this.mixin.getClassName();
    }
    
    @Override
    public String getClassRef() {
        return this.mixin.getClassRef();
    }
    
    public TargetClassContext getTarget() {
        return this.targetClass;
    }
    
    @Override
    public String getTargetClassRef() {
        return this.getTarget().getClassRef();
    }
    
    public ClassNode getTargetClassNode() {
        return this.getTarget().getClassNode();
    }
    
    public ClassInfo getTargetClassInfo() {
        return this.targetClassInfo;
    }
    
    public ClassInfo getClassInfo() {
        return this.mixin.getClassInfo();
    }
    
    public ClassSignature getSignature() {
        return this.getClassInfo().getSignature();
    }
    
    public SourceMap.File getStratum() {
        return this.stratum;
    }
    
    public int getMinRequiredClassVersion() {
        return this.minRequiredClassVersion;
    }
    
    public int getDefaultRequiredInjections() {
        return this.mixin.getParent().getDefaultRequiredInjections();
    }
    
    public String getDefaultInjectorGroup() {
        return this.mixin.getParent().getDefaultInjectorGroup();
    }
    
    public int getMaxShiftByValue() {
        return this.mixin.getParent().getMaxShiftByValue();
    }
    
    public InjectorGroupInfo.Map getInjectorGroups() {
        return this.injectorGroups;
    }
    
    public boolean requireOverwriteAnnotations() {
        return this.mixin.getParent().requireOverwriteAnnotations();
    }
    
    void transformMethod(final MethodNode method) {
        this.activities.clear();
        try {
            final IActivityContext.IActivity activity = this.activities.begin("Validate");
            this.validateMethod(method);
            activity.next("Transform Descriptor");
            this.transformDescriptor(method);
            activity.next("Transform LVT");
            this.transformLVT(method);
            activity.next("Transform Line Numbers");
            this.stratum.applyOffset(method);
            activity.next("Transform Instructions");
            AbstractInsnNode lastInsn = null;
            final Iterator<AbstractInsnNode> iter = method.instructions.iterator();
            while (iter.hasNext()) {
                final AbstractInsnNode insn = iter.next();
                final IActivityContext.IActivity insnActivity = this.activities.begin(Bytecode.getOpcodeName(insn) + " ");
                if (insn instanceof MethodInsnNode) {
                    final MethodInsnNode methodNode = (MethodInsnNode)insn;
                    insnActivity.append("%s::%s%s", methodNode.owner, methodNode.name, methodNode.desc);
                    this.transformMethodRef(method, iter, new MemberRef.Method(methodNode));
                }
                else if (insn instanceof FieldInsnNode) {
                    final FieldInsnNode fieldNode = (FieldInsnNode)insn;
                    insnActivity.append("%s::%s:%s", fieldNode.owner, fieldNode.name, fieldNode.desc);
                    this.transformFieldRef(method, iter, new MemberRef.Field(fieldNode));
                    this.checkFinal(method, iter, fieldNode);
                }
                else if (insn instanceof TypeInsnNode) {
                    final TypeInsnNode typeNode = (TypeInsnNode)insn;
                    insnActivity.append(typeNode.desc);
                    this.transformTypeNode(method, iter, typeNode, lastInsn);
                }
                else if (insn instanceof LdcInsnNode) {
                    this.transformConstantNode(method, iter, (LdcInsnNode)insn);
                }
                else if (insn instanceof InvokeDynamicInsnNode) {
                    final InvokeDynamicInsnNode invokeNode = (InvokeDynamicInsnNode)insn;
                    insnActivity.append("%s %s", invokeNode.name, invokeNode.desc);
                    this.transformInvokeDynamicNode(method, iter, invokeNode);
                }
                lastInsn = insn;
                insnActivity.end();
            }
            activity.end();
        }
        catch (final InvalidMixinException ex) {
            ex.prepend(this.activities);
            throw ex;
        }
        catch (final Exception ex2) {
            throw new InvalidMixinException(this, "Unexpecteded " + ex2.getClass().getSimpleName() + " whilst transforming the mixin class:", ex2, this.activities);
        }
    }
    
    private void validateMethod(final MethodNode method) {
        if (Annotations.getInvisible(method, SoftOverride.class) != null) {
            if (Bytecode.getVisibility(method) == Bytecode.Visibility.PRIVATE) {
                throw new InvalidMixinException(this, "Mixin method " + method.name + method.desc + " is tagged with @SoftOverride but the method is PRIVATE");
            }
            final ClassInfo.Method superMethod = this.targetClassInfo.findMethodInHierarchy(method.name, method.desc, ClassInfo.SearchType.SUPER_CLASSES_ONLY, ClassInfo.Traversal.SUPER);
            if (superMethod == null || !superMethod.isInjected()) {
                throw new InvalidMixinException(this, "Mixin method " + method.name + method.desc + " is tagged with @SoftOverride but no valid method was found in superclasses of " + this.getTarget().getClassName());
            }
        }
        if (Bytecode.isVirtual(method)) {
            final ClassInfo.Method superMethod = this.targetClassInfo.findMethodInHierarchy(method, ClassInfo.SearchType.SUPER_CLASSES_ONLY, ClassInfo.Traversal.ALL, 0);
            if (superMethod != null && superMethod.isFinal()) {
                throw new InvalidMixinException(this.mixin, String.format("%s%s in %s overrides a final method from %s", method.name, method.desc, this.mixin, superMethod.getOwner().getClassName()));
            }
        }
    }
    
    private void transformLVT(final MethodNode method) {
        if (method.localVariables == null) {
            return;
        }
        final IActivityContext.IActivity localVarActivity = this.activities.begin("?");
        for (final LocalVariableNode local : method.localVariables) {
            if (local != null) {
                if (local.desc == null) {
                    continue;
                }
                localVarActivity.next("var=%s", local.name);
                local.desc = this.transformSingleDescriptor(Type.getType(local.desc));
            }
        }
        localVarActivity.end();
    }
    
    private void transformMethodRef(final MethodNode method, final Iterator<AbstractInsnNode> iter, final MemberRef methodRef) {
        this.transformDescriptor(methodRef);
        if (methodRef.getOwner().equals(this.getClassRef())) {
            methodRef.setOwner(this.getTarget().getClassRef());
            final ClassInfo.Method md = this.getClassInfo().findMethod(methodRef.getName(), methodRef.getDesc(), 10);
            if (md != null && md.isRenamed() && md.getOriginalName().equals(methodRef.getName()) && (md.isSynthetic() || md.isConformed())) {
                methodRef.setName(md.getName());
            }
            this.upgradeMethodRef(method, methodRef, md);
        }
        else if (this.innerClasses.containsKey(methodRef.getOwner())) {
            methodRef.setOwner(this.innerClasses.get(methodRef.getOwner()));
            methodRef.setDesc(this.transformMethodDescriptor(methodRef.getDesc()));
        }
        else if (this.detachedSuper || this.inheritsFromMixin) {
            if (methodRef.getOpcode() == 183) {
                this.updateStaticBinding(method, methodRef);
            }
            else if (methodRef.getOpcode() == 182 && ClassInfo.forName(methodRef.getOwner()).isMixin()) {
                this.updateDynamicBinding(method, methodRef);
            }
        }
    }
    
    private void transformFieldRef(final MethodNode method, final Iterator<AbstractInsnNode> iter, final MemberRef fieldRef) {
        if ("super$".equals(fieldRef.getName())) {
            if (!(fieldRef instanceof MemberRef.Field)) {
                throw new InvalidMixinException(this.mixin, "Cannot call imaginary super from method handle.");
            }
            this.processImaginarySuper(method, ((MemberRef.Field)fieldRef).insn);
            iter.remove();
        }
        this.transformDescriptor(fieldRef);
        if (fieldRef.getOwner().equals(this.getClassRef())) {
            fieldRef.setOwner(this.getTarget().getClassRef());
            final ClassInfo.Field field = this.getClassInfo().findField(fieldRef.getName(), fieldRef.getDesc(), 10);
            if (field != null && field.isRenamed() && field.getOriginalName().equals(fieldRef.getName()) && field.isStatic()) {
                fieldRef.setName(field.getName());
            }
        }
        else {
            final ClassInfo fieldOwner = ClassInfo.forName(fieldRef.getOwner());
            if (fieldOwner.isMixin()) {
                final ClassInfo actualOwner = this.targetClassInfo.findCorrespondingType(fieldOwner);
                fieldRef.setOwner((actualOwner != null) ? actualOwner.getName() : this.getTarget().getClassRef());
            }
        }
    }
    
    private void checkFinal(final MethodNode method, final Iterator<AbstractInsnNode> iter, final FieldInsnNode fieldNode) {
        if (!fieldNode.owner.equals(this.getTarget().getClassRef())) {
            return;
        }
        final int opcode = fieldNode.getOpcode();
        if (opcode == 180 || opcode == 178) {
            return;
        }
        for (final Map.Entry<FieldNode, ClassInfo.Field> shadow : this.shadowFields.entrySet()) {
            final FieldNode shadowFieldNode = shadow.getKey();
            if (shadowFieldNode.desc.equals(fieldNode.desc)) {
                if (!shadowFieldNode.name.equals(fieldNode.name)) {
                    continue;
                }
                final ClassInfo.Field shadowField = shadow.getValue();
                if (shadowField.isDecoratedFinal()) {
                    if (shadowField.isDecoratedMutable()) {
                        if (this.mixin.getParent().getEnvironment().getOption(MixinEnvironment.Option.DEBUG_VERBOSE)) {
                            MixinTargetContext.logger.warn("Write access to @Mutable @Final field {} in {}::{}", shadowField, this.mixin, method.name);
                        }
                    }
                    else if ("<init>".equals(method.name) || "<clinit>".equals(method.name)) {
                        MixinTargetContext.logger.warn("@Final field {} in {} should be final", shadowField, this.mixin);
                    }
                    else {
                        MixinTargetContext.logger.error("Write access detected to @Final field {} in {}::{}", shadowField, this.mixin, method.name);
                        if (this.mixin.getParent().getEnvironment().getOption(MixinEnvironment.Option.DEBUG_VERIFY)) {
                            throw new InvalidMixinException(this.mixin, "Write access detected to @Final field " + shadowField + " in " + this.mixin + "::" + method.name);
                        }
                    }
                }
            }
        }
    }
    
    private void transformTypeNode(final MethodNode method, final Iterator<AbstractInsnNode> iter, final TypeInsnNode typeInsn, final AbstractInsnNode lastNode) {
        if (typeInsn.getOpcode() == 192 && typeInsn.desc.equals(this.getTarget().getClassRef()) && lastNode.getOpcode() == 25 && ((VarInsnNode)lastNode).var == 0 && !Bytecode.isStatic(method)) {
            iter.remove();
            return;
        }
        if (typeInsn.desc.equals(this.getClassRef())) {
            typeInsn.desc = this.getTarget().getClassRef();
        }
        else {
            final String newName = this.innerClasses.get(typeInsn.desc);
            if (newName != null) {
                typeInsn.desc = newName;
            }
        }
        this.transformDescriptor(typeInsn);
    }
    
    private void transformConstantNode(final MethodNode method, final Iterator<AbstractInsnNode> iter, final LdcInsnNode ldcInsn) {
        ldcInsn.cst = this.transformConstant(method, iter, ldcInsn.cst);
    }
    
    private void transformInvokeDynamicNode(final MethodNode method, final Iterator<AbstractInsnNode> iter, final InvokeDynamicInsnNode dynInsn) {
        this.requireVersion(51);
        dynInsn.desc = this.transformMethodDescriptor(dynInsn.desc);
        dynInsn.bsm = this.transformHandle(method, iter, dynInsn.bsm);
        for (int i = 0; i < dynInsn.bsmArgs.length; ++i) {
            dynInsn.bsmArgs[i] = this.transformConstant(method, iter, dynInsn.bsmArgs[i]);
        }
    }
    
    private Object transformConstant(final MethodNode method, final Iterator<AbstractInsnNode> iter, final Object constant) {
        if (constant instanceof Type) {
            final Type type = (Type)constant;
            final String desc = this.transformDescriptor(type);
            if (!type.toString().equals(desc)) {
                return Type.getType(desc);
            }
            return constant;
        }
        else {
            if (constant instanceof Handle) {
                return this.transformHandle(method, iter, (Handle)constant);
            }
            if (ASM.isAtLeastVersion(6) && constant instanceof ConstantDynamic) {
                return this.transformDynamicConstant(method, iter, (ConstantDynamic)constant);
            }
            return constant;
        }
    }
    
    private Handle transformHandle(final MethodNode method, final Iterator<AbstractInsnNode> iter, final Handle handle) {
        final MemberRef.Handle memberRef = new MemberRef.Handle(handle);
        if (memberRef.isField()) {
            this.transformFieldRef(method, iter, memberRef);
        }
        else {
            this.transformMethodRef(method, iter, memberRef);
        }
        return memberRef.getMethodHandle();
    }
    
    private ConstantDynamic transformDynamicConstant(final MethodNode method, final Iterator<AbstractInsnNode> iter, final ConstantDynamic constant) {
        this.requireVersion(55);
        if (!MixinEnvironment.getCompatibilityLevel().supports(16)) {
            throw new InvalidMixinException(this, String.format("%s%s in %s contains a dynamic constant, which is not supported by the current compatibility level", method.name, method.desc, this));
        }
        final String desc = this.transformSingleDescriptor(constant.getDescriptor(), false);
        final Handle bsm = this.transformHandle(method, iter, constant.getBootstrapMethod());
        final Object[] bsmArgs = new Object[constant.getBootstrapMethodArgumentCount()];
        for (int i = 0; i < bsmArgs.length; ++i) {
            bsmArgs[i] = this.transformConstant(method, iter, constant.getBootstrapMethodArgument(i));
        }
        return new ConstantDynamic(constant.getName(), desc, bsm, bsmArgs);
    }
    
    private void processImaginarySuper(final MethodNode method, final FieldInsnNode fieldInsn) {
        if (fieldInsn.getOpcode() != 180) {
            if ("<init>".equals(method.name)) {
                throw new InvalidMixinException(this, "Illegal imaginary super declaration: field " + fieldInsn.name + " must not specify an initialiser");
            }
            throw new InvalidMixinException(this, "Illegal imaginary super access: found " + Bytecode.getOpcodeName(fieldInsn.getOpcode()) + " opcode in " + method.name + method.desc);
        }
        else {
            if ((method.access & 0x2) != 0x0 || (method.access & 0x8) != 0x0) {
                throw new InvalidMixinException(this, "Illegal imaginary super access: method " + method.name + method.desc + " is private or static");
            }
            if (Annotations.getInvisible(method, SoftOverride.class) == null) {
                throw new InvalidMixinException(this, "Illegal imaginary super access: method " + method.name + method.desc + " is not decorated with @SoftOverride");
            }
            final Iterator<AbstractInsnNode> methodIter = method.instructions.iterator(method.instructions.indexOf(fieldInsn));
            while (methodIter.hasNext()) {
                final AbstractInsnNode insn = methodIter.next();
                if (insn instanceof MethodInsnNode) {
                    final MethodInsnNode methodNode = (MethodInsnNode)insn;
                    if (methodNode.owner.equals(this.getClassRef()) && methodNode.name.equals(method.name) && methodNode.desc.equals(method.desc)) {
                        methodNode.setOpcode(183);
                        this.updateStaticBinding(method, new MemberRef.Method(methodNode));
                        return;
                    }
                    continue;
                }
            }
            throw new InvalidMixinException(this, "Illegal imaginary super access: could not find INVOKE for " + method.name + method.desc);
        }
    }
    
    private void updateStaticBinding(final MethodNode method, final MemberRef methodRef) {
        this.updateBinding(method, methodRef, ClassInfo.Traversal.SUPER);
    }
    
    private void updateDynamicBinding(final MethodNode method, final MemberRef methodRef) {
        this.updateBinding(method, methodRef, ClassInfo.Traversal.ALL);
    }
    
    private void updateBinding(final MethodNode method, final MemberRef methodRef, final ClassInfo.Traversal traversal) {
        if ("<init>".equals(method.name) || methodRef.getOwner().equals(this.getTarget().getClassRef()) || this.getTarget().getClassRef().startsWith("<")) {
            return;
        }
        final ClassInfo.Method superMethod = this.targetClassInfo.findMethodInHierarchy(methodRef.getName(), methodRef.getDesc(), traversal.getSearchType(), traversal);
        if (superMethod != null) {
            if (superMethod.getOwner().isMixin()) {
                throw new InvalidMixinException(this, "Invalid " + methodRef + " in " + this + " resolved " + superMethod.getOwner() + " but is mixin.");
            }
            methodRef.setOwner(superMethod.getImplementor().getName());
        }
        else if (ClassInfo.forName(methodRef.getOwner()).isMixin()) {
            throw new MixinTransformerError("Error resolving " + methodRef + " in " + this);
        }
    }
    
    void transformDescriptor(final FieldNode field) {
        if (!this.inheritsFromMixin && this.innerClasses.size() == 0) {
            return;
        }
        field.desc = this.transformSingleDescriptor(field.desc, false);
    }
    
    void transformDescriptor(final MethodNode method) {
        if (!this.inheritsFromMixin && this.innerClasses.size() == 0) {
            return;
        }
        method.desc = this.transformMethodDescriptor(method.desc);
    }
    
    void transformDescriptor(final MemberRef member) {
        if (!this.inheritsFromMixin && this.innerClasses.size() == 0) {
            return;
        }
        if (member.isField()) {
            member.setDesc(this.transformSingleDescriptor(member.getDesc(), false));
        }
        else {
            member.setDesc(this.transformMethodDescriptor(member.getDesc()));
        }
    }
    
    void transformDescriptor(final TypeInsnNode typeInsn) {
        if (!this.inheritsFromMixin && this.innerClasses.size() == 0) {
            return;
        }
        typeInsn.desc = this.transformSingleDescriptor(typeInsn.desc, true);
    }
    
    private String transformDescriptor(final Type type) {
        if (type.getSort() == 11) {
            return this.transformMethodDescriptor(type.getDescriptor());
        }
        return this.transformSingleDescriptor(type);
    }
    
    private String transformSingleDescriptor(final Type type) {
        if (type.getSort() < 9) {
            return type.toString();
        }
        return this.transformSingleDescriptor(type.toString(), false);
    }
    
    private String transformSingleDescriptor(final String desc, boolean isObject) {
        final IActivityContext.IActivity descriptorActivity = this.activities.begin("desc=%s", desc);
        boolean isArray = false;
        String type = desc;
        while (type.startsWith("[") || type.startsWith("L")) {
            if (type.startsWith("[")) {
                type = type.substring(1);
                isArray = true;
            }
            else {
                type = type.substring(1, type.indexOf(";"));
                isObject = true;
            }
        }
        if (!isObject) {
            descriptorActivity.end();
            return desc;
        }
        if (isArray && type.length() == 1) {
            final Type parsedType = Type.getType(type);
            if (parsedType.getSort() <= 8) {
                descriptorActivity.end();
                return desc;
            }
        }
        final String innerClassName = this.innerClasses.get(type);
        if (innerClassName != null) {
            descriptorActivity.end();
            return desc.replace(type, innerClassName);
        }
        if (this.innerClasses.inverse().containsKey(type)) {
            descriptorActivity.end();
            return desc;
        }
        final ClassInfo typeInfo = ClassInfo.forName(type);
        if (typeInfo == null) {
            throw new ClassMetadataNotFoundException(type.replace('/', '.'));
        }
        if (!typeInfo.isMixin() || typeInfo.isLoadable()) {
            descriptorActivity.end();
            return desc;
        }
        final String realDesc = desc.replace(type, this.findRealType(typeInfo).toString());
        descriptorActivity.end();
        return realDesc;
    }
    
    private String transformMethodDescriptor(final String desc) {
        final StringBuilder newDesc = new StringBuilder();
        newDesc.append('(');
        for (final Type arg : Type.getArgumentTypes(desc)) {
            newDesc.append(this.transformSingleDescriptor(arg));
        }
        return newDesc.append(')').append(this.transformSingleDescriptor(Type.getReturnType(desc))).toString();
    }
    
    public Target getTargetMethod(final MethodNode method) {
        return this.getTarget().getTargetMethod(method);
    }
    
    MethodNode findMethod(final MethodNode method, final AnnotationNode annotation) {
        final Deque<String> aliases = new LinkedList<String>();
        aliases.add(method.name);
        if (annotation != null) {
            final List<String> aka = Annotations.getValue(annotation, "aliases");
            if (aka != null) {
                aliases.addAll((Collection<?>)aka);
            }
        }
        return this.getTarget().findMethod(aliases, method.desc);
    }
    
    MethodNode findRemappedMethod(final MethodNode method) {
        final RemapperChain remapperChain = this.getEnvironment().getRemappers();
        final String remappedName = remapperChain.mapMethodName(this.getTarget().getClassRef(), method.name, method.desc);
        if (remappedName.equals(method.name)) {
            return null;
        }
        final Deque<String> aliases = new LinkedList<String>();
        aliases.add(remappedName);
        return this.getTarget().findAliasedMethod(aliases, method.desc);
    }
    
    FieldNode findField(final FieldNode field, final AnnotationNode shadow) {
        final Deque<String> aliases = new LinkedList<String>();
        aliases.add(field.name);
        if (shadow != null) {
            final List<String> aka = Annotations.getValue(shadow, "aliases");
            if (aka != null) {
                aliases.addAll((Collection<?>)aka);
            }
        }
        return this.getTarget().findAliasedField(aliases, field.desc);
    }
    
    FieldNode findRemappedField(final FieldNode field) {
        final RemapperChain remapperChain = this.getEnvironment().getRemappers();
        final String remappedName = remapperChain.mapFieldName(this.getTarget().getClassRef(), field.name, field.desc);
        if (remappedName.equals(field.name)) {
            return null;
        }
        final Deque<String> aliases = new LinkedList<String>();
        aliases.add(remappedName);
        return this.getTarget().findAliasedField(aliases, field.desc);
    }
    
    protected void requireVersion(final int version) {
        final int majorVersion = version & 0xFFFF;
        final int minorVersion = version >> 16 & 0xFFFF;
        if (majorVersion <= (this.minRequiredClassVersion & 0xFFFF)) {
            return;
        }
        this.minRequiredClassVersion = version;
        if (majorVersion > ASM.getMaxSupportedClassVersionMajor()) {
            throw new InvalidMixinException(this, String.format("Unsupported mixin class version %d.%d. ASM supports %s", majorVersion, minorVersion, ASM.getClassVersionString()));
        }
        this.mixin.getParent().checkCompatibilityLevel(this.mixin, majorVersion, minorVersion);
    }
    
    @Override
    public Extensions getExtensions() {
        return this.targetClass.getExtensions();
    }
    
    @Override
    public IMixinInfo getMixin() {
        return this.mixin;
    }
    
    MixinInfo getInfo() {
        return this.mixin;
    }
    
    boolean isRequired() {
        return this.mixin.isRequired();
    }
    
    @Override
    public int getPriority() {
        return this.mixin.getPriority();
    }
    
    Set<String> getInterfaces() {
        return this.mixin.getInterfaces();
    }
    
    Collection<MethodNode> getShadowMethods() {
        return this.shadowMethods;
    }
    
    List<MethodNode> getMethods() {
        return this.classNode.methods;
    }
    
    Set<Map.Entry<FieldNode, ClassInfo.Field>> getShadowFields() {
        return this.shadowFields.entrySet();
    }
    
    List<FieldNode> getFields() {
        return this.classNode.fields;
    }
    
    Level getLoggingLevel() {
        return this.mixin.getLoggingLevel();
    }
    
    boolean shouldSetSourceFile() {
        return this.mixin.getParent().shouldSetSourceFile();
    }
    
    String getSourceFile() {
        return this.classNode.sourceFile;
    }
    
    String getNestHostClass() {
        return ClassNodeAdapter.getNestHostClass(this.classNode);
    }
    
    List<String> getNestMembers() {
        return ClassNodeAdapter.getNestMembers(this.classNode);
    }
    
    BiMap<String, String> getInnerClasses() {
        return this.innerClasses;
    }
    
    @Override
    public IReferenceMapper getReferenceMapper() {
        return this.mixin.getParent().getReferenceMapper();
    }
    
    void preApply(final String transformedName, final ClassNode targetClass) throws Exception {
        this.mixin.preApply(transformedName, targetClass);
    }
    
    void postApply(final String transformedName, final ClassNode targetClass) {
        this.activities.clear();
        try {
            final IActivityContext.IActivity activity = this.activities.begin("Validating Injector Groups");
            this.injectorGroups.validateAll();
            activity.next("Plugin Post-Application");
            this.mixin.postApply(transformedName, targetClass);
            activity.end();
        }
        catch (final InjectionValidationException ex) {
            final InjectorGroupInfo group = ex.getGroup();
            throw new InvalidInjectionException(group.getMembers().iterator().next().getMixin(), String.format("Critical injection failure: Callback group %s in %s failed injection check: %s", group, this.mixin, ex.getMessage()), ex);
        }
        catch (final InvalidMixinException ex2) {
            ex2.prepend(this.activities);
            throw ex2;
        }
        catch (final Exception ex3) {
            throw new InvalidMixinException(this, "Unexpecteded " + ex3.getClass().getSimpleName() + " whilst transforming the mixin class:", ex3, this.activities);
        }
    }
    
    String getUniqueName(final MethodNode method, final boolean preservePrefix) {
        return this.targetClassInfo.getMethodMapper().getUniqueName(method, this.sessionId, preservePrefix);
    }
    
    String getUniqueName(final FieldNode field) {
        return this.targetClassInfo.getMethodMapper().getUniqueName(field, this.sessionId);
    }
    
    void prepareInjections() {
        this.activities.clear();
        try {
            this.injectors.clear();
            final IActivityContext.IActivity prepareActivity = this.activities.begin("?");
            for (final MethodNode method : this.mergedMethods) {
                prepareActivity.next("%s%s", method.name, method.desc);
                final IActivityContext.IActivity methodActivity = this.activities.begin("Parse");
                final InjectionInfo injectInfo = InjectionInfo.parse(this, method);
                if (injectInfo == null) {
                    continue;
                }
                methodActivity.next("Validate");
                if (injectInfo.isValid()) {
                    methodActivity.next("Prepare");
                    injectInfo.prepare();
                    this.injectors.add(injectInfo);
                }
                methodActivity.next("Undecorate");
                method.visibleAnnotations.remove(injectInfo.getAnnotationNode());
                methodActivity.end();
            }
            prepareActivity.end();
        }
        catch (final InvalidMixinException ex) {
            ex.prepend(this.activities);
            throw ex;
        }
        catch (final Exception ex2) {
            throw new InvalidMixinException(this, "Unexpecteded " + ex2.getClass().getSimpleName() + " whilst transforming the mixin class:", ex2, this.activities);
        }
    }
    
    void applyInjections() {
        this.activities.clear();
        try {
            final IActivityContext.IActivity applyActivity = this.activities.begin("PreInject");
            final IActivityContext.IActivity preInjectActivity = this.activities.begin("?");
            for (final InjectionInfo injectInfo : this.injectors) {
                preInjectActivity.next(injectInfo.toString());
                injectInfo.preInject();
            }
            applyActivity.next("Inject");
            final IActivityContext.IActivity injectActivity = this.activities.begin("?");
            for (final InjectionInfo injectInfo2 : this.injectors) {
                injectActivity.next(injectInfo2.toString());
                injectInfo2.inject();
            }
            applyActivity.next("PostInject");
            final IActivityContext.IActivity postInjectActivity = this.activities.begin("?");
            for (final InjectionInfo injectInfo3 : this.injectors) {
                postInjectActivity.next(injectInfo3.toString());
                injectInfo3.postInject();
            }
            applyActivity.end();
            this.injectors.clear();
        }
        catch (final InvalidMixinException ex) {
            ex.prepend(this.activities);
            throw ex;
        }
        catch (final Exception ex2) {
            throw new InvalidMixinException(this, "Unexpecteded " + ex2.getClass().getSimpleName() + " whilst transforming the mixin class:", ex2, this.activities);
        }
    }
    
    List<MethodNode> generateAccessors() {
        this.activities.clear();
        final List<MethodNode> methods = new ArrayList<MethodNode>();
        try {
            final IActivityContext.IActivity accessorActivity = this.activities.begin("Locate");
            final IActivityContext.IActivity locateActivity = this.activities.begin("?");
            for (final AccessorInfo accessor : this.accessors) {
                locateActivity.next(accessor.toString());
                accessor.locate();
            }
            accessorActivity.next("Validate");
            final IActivityContext.IActivity validateActivity = this.activities.begin("?");
            for (final AccessorInfo accessor2 : this.accessors) {
                validateActivity.next(accessor2.toString());
                accessor2.validate();
            }
            accessorActivity.next("Generate");
            final IActivityContext.IActivity generateActivity = this.activities.begin("?");
            for (final AccessorInfo accessor3 : this.accessors) {
                generateActivity.next(accessor3.toString());
                final MethodNode generated = accessor3.generate();
                this.getTarget().addMixinMethod(generated);
                methods.add(generated);
            }
            accessorActivity.end();
        }
        catch (final InvalidMixinException ex) {
            ex.prepend(this.activities);
            throw ex;
        }
        catch (final Exception ex2) {
            throw new InvalidMixinException(this, "Unexpecteded " + ex2.getClass().getSimpleName() + " whilst transforming the mixin class:", ex2, this.activities);
        }
        return methods;
    }
    
    private ClassInfo findRealType(final ClassInfo mixin) {
        if (mixin == this.getClassInfo()) {
            return this.targetClassInfo;
        }
        final ClassInfo type = this.targetClassInfo.findCorrespondingType(mixin);
        if (type == null) {
            throw new InvalidMixinException(this, "Resolution error: unable to find corresponding type for " + mixin + " in hierarchy of " + this.targetClassInfo);
        }
        return type;
    }
    
    static {
        logger = MixinService.getService().getLogger("mixin");
    }
}
