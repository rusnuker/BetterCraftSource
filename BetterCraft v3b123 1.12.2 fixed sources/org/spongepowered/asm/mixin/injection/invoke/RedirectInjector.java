// 
// Decompiled by Procyon v0.6.0
// 

package org.spongepowered.asm.mixin.injection.invoke;

import com.google.common.collect.ObjectArrays;
import org.objectweb.asm.tree.JumpInsnNode;
import org.objectweb.asm.tree.LabelNode;
import org.objectweb.asm.tree.LdcInsnNode;
import org.spongepowered.asm.util.SignaturePrinter;
import org.objectweb.asm.tree.InsnNode;
import org.objectweb.asm.tree.VarInsnNode;
import org.objectweb.asm.Type;
import com.google.common.primitives.Ints;
import org.spongepowered.asm.util.Bytecode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.FieldInsnNode;
import java.util.Iterator;
import org.objectweb.asm.tree.TypeInsnNode;
import org.spongepowered.asm.mixin.injection.points.BeforeFieldAccess;
import org.spongepowered.asm.mixin.injection.code.Injector;
import org.spongepowered.asm.mixin.injection.selectors.ISelectorContext;
import org.spongepowered.asm.mixin.injection.throwables.InvalidInjectionException;
import org.objectweb.asm.tree.MethodInsnNode;
import org.spongepowered.asm.mixin.injection.InjectionPoint;
import java.util.Set;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.spongepowered.asm.mixin.injection.struct.InjectionNodes;
import java.util.List;
import org.spongepowered.asm.mixin.injection.struct.Target;
import java.lang.annotation.Annotation;
import org.spongepowered.asm.util.Annotations;
import org.spongepowered.asm.mixin.Final;
import java.util.HashMap;
import org.spongepowered.asm.mixin.injection.struct.InjectionInfo;
import org.spongepowered.asm.mixin.injection.points.BeforeNew;
import java.util.Map;

public class RedirectInjector extends InvokeInjector
{
    private static final String GET_CLASS_METHOD = "getClass";
    private static final String IS_ASSIGNABLE_FROM_METHOD = "isAssignableFrom";
    private static final String NPE = "java/lang/NullPointerException";
    private static final String KEY_NOMINATORS = "nominators";
    private static final String KEY_FUZZ = "fuzz";
    private static final String KEY_OPCODE = "opcode";
    protected Meta meta;
    private Map<BeforeNew, ConstructorRedirectData> ctorRedirectors;
    
    public RedirectInjector(final InjectionInfo info) {
        this(info, "@Redirect");
    }
    
    protected RedirectInjector(final InjectionInfo info, final String annotationType) {
        super(info, annotationType);
        this.ctorRedirectors = new HashMap<BeforeNew, ConstructorRedirectData>();
        final int priority = info.getMixin().getPriority();
        final boolean isFinal = Annotations.getVisible(this.methodNode, Final.class) != null;
        this.meta = new Meta(priority, isFinal, this.info.toString(), this.methodNode.desc);
    }
    
    @Override
    protected void checkTarget(final Target target) {
    }
    
    @Override
    protected void addTargetNode(final Target target, final List<InjectionNodes.InjectionNode> myNodes, final AbstractInsnNode insn, final Set<InjectionPoint> nominators) {
        final InjectionNodes.InjectionNode node = target.getInjectionNode(insn);
        ConstructorRedirectData ctorData = null;
        int fuzz = 8;
        int opcode = 0;
        if (insn instanceof MethodInsnNode && "<init>".equals(((MethodInsnNode)insn).name)) {
            throw new InvalidInjectionException(this.info, String.format("Illegal %s of constructor specified on %s", this.annotationType, this));
        }
        if (node != null) {
            final Meta other = node.getDecoration("redirector");
            if (other != null && other.getOwner() != this) {
                if (other.priority >= this.meta.priority) {
                    Injector.logger.warn("{} conflict. Skipping {} with priority {}, already redirected by {} with priority {}", this.annotationType, this.info, this.meta.priority, other.name, other.priority);
                    return;
                }
                if (other.isFinal) {
                    throw new InvalidInjectionException(this.info, String.format("%s conflict: %s failed because target was already remapped by %s", this.annotationType, this, other.name));
                }
            }
        }
        for (final InjectionPoint ip : nominators) {
            if (ip instanceof BeforeNew) {
                ctorData = this.getCtorRedirect((BeforeNew)ip);
                ctorData.wildcard = !((BeforeNew)ip).hasDescriptor();
            }
            else {
                if (!(ip instanceof BeforeFieldAccess)) {
                    continue;
                }
                final BeforeFieldAccess bfa = (BeforeFieldAccess)ip;
                fuzz = bfa.getFuzzFactor();
                opcode = bfa.getArrayOpcode();
            }
        }
        final InjectionNodes.InjectionNode targetNode = target.addInjectionNode(insn);
        targetNode.decorate("redirector", this.meta);
        targetNode.decorate("nominators", nominators);
        if (insn instanceof TypeInsnNode && insn.getOpcode() == 187) {
            targetNode.decorate("ctor", ctorData);
        }
        else {
            targetNode.decorate("fuzz", fuzz);
            targetNode.decorate("opcode", opcode);
        }
        myNodes.add(targetNode);
    }
    
    private ConstructorRedirectData getCtorRedirect(final BeforeNew ip) {
        ConstructorRedirectData ctorRedirect = this.ctorRedirectors.get(ip);
        if (ctorRedirect == null) {
            ctorRedirect = new ConstructorRedirectData();
            this.ctorRedirectors.put(ip, ctorRedirect);
        }
        return ctorRedirect;
    }
    
    @Override
    protected void inject(final Target target, final InjectionNodes.InjectionNode node) {
        if (!this.preInject(node)) {
            return;
        }
        if (node.isReplaced()) {
            throw new UnsupportedOperationException("Redirector target failure for " + this.info);
        }
        if (node.getCurrentTarget() instanceof MethodInsnNode) {
            this.checkTargetForNode(target, node, InjectionPoint.RestrictTargetLevel.ALLOW_ALL);
            this.injectAtInvoke(target, node);
            return;
        }
        if (node.getCurrentTarget() instanceof FieldInsnNode) {
            this.checkTargetForNode(target, node, InjectionPoint.RestrictTargetLevel.ALLOW_ALL);
            this.injectAtFieldAccess(target, node);
            return;
        }
        if (node.getCurrentTarget() instanceof TypeInsnNode) {
            final int opcode = node.getCurrentTarget().getOpcode();
            if (opcode == 187) {
                if (!this.isStatic && target.isStatic) {
                    throw new InvalidInjectionException(this.info, String.format("non-static callback method %s has a static target which is not supported", this));
                }
                this.injectAtConstructor(target, node);
                return;
            }
            else if (opcode == 193) {
                this.checkTargetModifiers(target, false);
                this.injectAtInstanceOf(target, node);
                return;
            }
        }
        throw new InvalidInjectionException(this.info, String.format("%s annotation on is targetting an invalid insn in %s in %s", this.annotationType, target, this));
    }
    
    protected boolean preInject(final InjectionNodes.InjectionNode node) {
        final Meta other = node.getDecoration("redirector");
        if (other.getOwner() != this) {
            Injector.logger.warn("{} conflict. Skipping {} with priority {}, already redirected by {} with priority {}", this.annotationType, this.info, this.meta.priority, other.name, other.priority);
            return false;
        }
        return true;
    }
    
    @Override
    protected void postInject(final Target target, final InjectionNodes.InjectionNode node) {
        super.postInject(target, node);
        if (node.getOriginalTarget() instanceof TypeInsnNode && node.getOriginalTarget().getOpcode() == 187) {
            final ConstructorRedirectData meta = node.getDecoration("ctor");
            if (meta.wildcard && meta.injected == 0) {
                throw new InvalidInjectionException(this.info, String.format("%s ctor invocation was not found in %s", this.annotationType, target), meta.lastException);
            }
        }
    }
    
    @Override
    protected void injectAtInvoke(final Target target, final InjectionNodes.InjectionNode node) {
        final RedirectedInvokeData invoke = new RedirectedInvokeData(target, (MethodInsnNode)node.getCurrentTarget());
        this.validateParams(invoke, invoke.returnType, invoke.handlerArgs);
        final InsnList insns = new InsnList();
        final Target.Extension extraLocals = target.extendLocals().add(invoke.handlerArgs).add(1);
        final Target.Extension extraStack = target.extendStack().add(1);
        int[] argMap = this.storeArgs(target, invoke.handlerArgs, insns, 0);
        if (invoke.captureTargetArgs > 0) {
            final int argSize = Bytecode.getArgsSize(target.arguments, 0, invoke.captureTargetArgs);
            extraLocals.add(argSize);
            extraStack.add(argSize);
            argMap = Ints.concat(new int[][] { argMap, target.getArgIndices() });
        }
        final AbstractInsnNode champion = this.invokeHandlerWithArgs(this.methodArgs, insns, argMap);
        if (invoke.coerceReturnType && invoke.returnType.getSort() >= 9) {
            insns.add(new TypeInsnNode(192, invoke.returnType.getInternalName()));
        }
        target.replaceNode(invoke.node, champion, insns);
        extraLocals.apply();
        extraStack.apply();
    }
    
    private void injectAtFieldAccess(final Target target, final InjectionNodes.InjectionNode node) {
        final RedirectedFieldData field = new RedirectedFieldData(target, (FieldInsnNode)node.getCurrentTarget());
        final int handlerDimensions = (this.returnType.getSort() == 9) ? this.returnType.getDimensions() : 0;
        if (handlerDimensions > field.dimensions) {
            throw new InvalidInjectionException(this.info, "Dimensionality of handler method is greater than target array on " + this);
        }
        if (handlerDimensions == 0 && field.dimensions > 0) {
            final int fuzz = node.getDecoration("fuzz");
            final int opcode = node.getDecoration("opcode");
            this.injectAtArrayField(field, fuzz, opcode);
        }
        else {
            this.injectAtScalarField(field);
        }
    }
    
    private void injectAtArrayField(final RedirectedFieldData field, final int fuzz, int opcode) {
        final Type elementType = field.type.getElementType();
        if (field.opcode != 178 && field.opcode != 180) {
            throw new InvalidInjectionException(this.info, String.format("Unspported opcode %s for array access %s", Bytecode.getOpcodeName(field.opcode), this.info));
        }
        if (this.returnType.getSort() != 0) {
            if (opcode != 190) {
                opcode = elementType.getOpcode(46);
            }
            final AbstractInsnNode varNode = BeforeFieldAccess.findArrayNode(field.target.insns, field.node, opcode, fuzz);
            this.injectAtGetArray(field, varNode);
        }
        else {
            final AbstractInsnNode varNode = BeforeFieldAccess.findArrayNode(field.target.insns, field.node, elementType.getOpcode(79), fuzz);
            this.injectAtSetArray(field, varNode);
        }
    }
    
    private void injectAtGetArray(final RedirectedFieldData field, final AbstractInsnNode varNode) {
        field.description = "array getter";
        field.elementType = field.type.getElementType();
        if (varNode != null && varNode.getOpcode() == 190) {
            field.elementType = Type.INT_TYPE;
            field.extraDimensions = 0;
        }
        this.validateParams(field, field.elementType, field.getArrayArgs(new Type[0]));
        this.injectArrayRedirect(field, varNode, "array getter");
    }
    
    private void injectAtSetArray(final RedirectedFieldData field, final AbstractInsnNode varNode) {
        field.description = "array setter";
        Type elementType = field.type.getElementType();
        final int valueArgIndex = field.getTotalDimensions();
        if (this.checkCoerce(valueArgIndex, elementType, String.format("%s array setter method %s from %s", this.annotationType, this, this.info.getMixin()), true)) {
            elementType = this.methodArgs[valueArgIndex];
        }
        this.validateParams(field, Type.VOID_TYPE, field.getArrayArgs(elementType));
        this.injectArrayRedirect(field, varNode, "array setter");
    }
    
    private void injectArrayRedirect(final RedirectedFieldData field, final AbstractInsnNode varNode, final String type) {
        if (varNode == null) {
            final String advice = "";
            throw new InvalidInjectionException(this.info, String.format("Array element %s on %s could not locate a matching %s instruction in %s. %s", this.annotationType, this, type, field.target, advice));
        }
        final Target.Extension extraStack = field.target.extendStack();
        if (!this.isStatic) {
            final VarInsnNode loadThis = new VarInsnNode(25, 0);
            field.target.insns.insert(field.node, loadThis);
            field.target.insns.insert(loadThis, new InsnNode(95));
            extraStack.add();
        }
        final InsnList insns = new InsnList();
        if (field.captureTargetArgs > 0) {
            this.pushArgs(field.target.arguments, insns, field.target.getArgIndices(), 0, field.captureTargetArgs, extraStack);
        }
        extraStack.apply();
        final AbstractInsnNode champion = this.invokeHandler(insns);
        if (field.coerceReturnType && field.type.getSort() >= 9) {
            insns.add(new TypeInsnNode(192, field.elementType.getInternalName()));
        }
        field.target.replaceNode(varNode, champion, insns);
    }
    
    private void injectAtScalarField(final RedirectedFieldData field) {
        AbstractInsnNode invoke = null;
        final InsnList insns = new InsnList();
        if (field.isGetter) {
            invoke = this.injectAtGetField(field, insns);
        }
        else {
            if (!field.isSetter) {
                throw new InvalidInjectionException(this.info, String.format("Unspported opcode %s for %s", Bytecode.getOpcodeName(field.opcode), this.info));
            }
            invoke = this.injectAtPutField(field, insns);
        }
        field.target.replaceNode(field.node, invoke, insns);
    }
    
    private AbstractInsnNode injectAtGetField(final RedirectedFieldData field, final InsnList insns) {
        this.validateParams(field, field.type, field.isStatic ? null : field.owner);
        final Target.Extension extraStack = field.target.extendStack();
        if (!this.isStatic) {
            extraStack.add();
            insns.add(new VarInsnNode(25, 0));
            if (!field.isStatic) {
                insns.add(new InsnNode(95));
            }
        }
        if (field.captureTargetArgs > 0) {
            this.pushArgs(field.target.arguments, insns, field.target.getArgIndices(), 0, field.captureTargetArgs, extraStack);
        }
        extraStack.apply();
        final AbstractInsnNode champion = this.invokeHandler(insns);
        if (field.coerceReturnType && field.type.getSort() >= 9) {
            insns.add(new TypeInsnNode(192, field.type.getInternalName()));
        }
        return champion;
    }
    
    private AbstractInsnNode injectAtPutField(final RedirectedFieldData field, final InsnList insns) {
        this.validateParams(field, Type.VOID_TYPE, field.isStatic ? null : field.owner, field.type);
        final Target.Extension extraStack = field.target.extendStack();
        if (!this.isStatic) {
            if (field.isStatic) {
                insns.add(new VarInsnNode(25, 0));
                insns.add(new InsnNode(95));
            }
            else {
                extraStack.add();
                final int marshallVar = field.target.allocateLocals(field.type.getSize());
                insns.add(new VarInsnNode(field.type.getOpcode(54), marshallVar));
                insns.add(new VarInsnNode(25, 0));
                insns.add(new InsnNode(95));
                insns.add(new VarInsnNode(field.type.getOpcode(21), marshallVar));
            }
        }
        if (field.captureTargetArgs > 0) {
            this.pushArgs(field.target.arguments, insns, field.target.getArgIndices(), 0, field.captureTargetArgs, extraStack);
        }
        extraStack.apply();
        return this.invokeHandler(insns);
    }
    
    protected void injectAtConstructor(final Target target, final InjectionNodes.InjectionNode node) {
        final ConstructorRedirectData meta = node.getDecoration("ctor");
        if (meta == null) {
            throw new InvalidInjectionException(this.info, String.format("%s ctor redirector has no metadata, the injector failed a preprocessing phase", this.annotationType));
        }
        final TypeInsnNode newNode = (TypeInsnNode)node.getCurrentTarget();
        final AbstractInsnNode dupNode = target.get(target.indexOf(newNode) + 1);
        final MethodInsnNode initNode = target.findInitNodeFor(newNode);
        if (initNode == null) {
            meta.throwOrCollect(new InvalidInjectionException(this.info, String.format("%s ctor invocation was not found in %s", this.annotationType, target)));
            return;
        }
        final boolean isAssigned = dupNode.getOpcode() == 89;
        final RedirectedInvokeData ctor = new RedirectedInvokeData(target, initNode);
        ctor.description = "factory";
        try {
            this.validateParams(ctor, Type.getObjectType(newNode.desc), ctor.targetArgs);
        }
        catch (final InvalidInjectionException ex) {
            meta.throwOrCollect(ex);
            return;
        }
        if (isAssigned) {
            target.removeNode(dupNode);
        }
        if (this.isStatic) {
            target.removeNode(newNode);
        }
        else {
            target.replaceNode(newNode, new VarInsnNode(25, 0));
        }
        final Target.Extension extraStack = target.extendStack();
        final InsnList insns = new InsnList();
        if (ctor.captureTargetArgs > 0) {
            this.pushArgs(target.arguments, insns, target.getArgIndices(), 0, ctor.captureTargetArgs, extraStack);
        }
        this.invokeHandler(insns);
        if (ctor.coerceReturnType) {
            insns.add(new TypeInsnNode(192, newNode.desc));
        }
        extraStack.apply();
        if (isAssigned) {
            this.doNullCheck(insns, extraStack, "constructor handler", newNode.desc.replace('/', '.'));
        }
        else {
            insns.add(new InsnNode(87));
        }
        extraStack.apply();
        target.replaceNode(initNode, insns);
        final ConstructorRedirectData constructorRedirectData = meta;
        ++constructorRedirectData.injected;
    }
    
    protected void injectAtInstanceOf(final Target target, final InjectionNodes.InjectionNode node) {
        this.injectAtInstanceOf(target, (TypeInsnNode)node.getCurrentTarget());
    }
    
    protected void injectAtInstanceOf(final Target target, final TypeInsnNode typeNode) {
        if (this.returnType.getSort() == 1) {
            this.redirectInstanceOf(target, typeNode, false);
            return;
        }
        if (this.returnType.equals(Type.getType("Ljava/lang/Class;"))) {
            this.redirectInstanceOf(target, typeNode, true);
            return;
        }
        throw new InvalidInjectionException(this.info, String.format("%s on %s has an invalid signature. Found unexpected return type %s. INSTANCEOF handler expects (Ljava/lang/Object;Ljava/lang/Class;)Z or (Ljava/lang/Object;Ljava/lang/Class;)Ljava/lang/Class;", this.annotationType, this, SignaturePrinter.getTypeName(this.returnType)));
    }
    
    private void redirectInstanceOf(final Target target, final TypeInsnNode typeNode, final boolean dynamic) {
        final Target.Extension extraStack = target.extendStack();
        final InsnList insns = new InsnList();
        final InjectorData handler = new InjectorData(target, "instanceof handler", false);
        this.validateParams(handler, this.returnType, Type.getType("Ljava/lang/Object;"), Type.getType("Ljava/lang/Class;"));
        if (dynamic) {
            insns.add(new InsnNode(89));
            extraStack.add();
        }
        if (!this.isStatic) {
            insns.add(new VarInsnNode(25, 0));
            insns.add(new InsnNode(95));
            extraStack.add();
        }
        insns.add(new LdcInsnNode(Type.getObjectType(typeNode.desc)));
        extraStack.add();
        if (handler.captureTargetArgs > 0) {
            this.pushArgs(target.arguments, insns, target.getArgIndices(), 0, handler.captureTargetArgs, extraStack);
        }
        final AbstractInsnNode champion = this.invokeHandler(insns);
        if (dynamic) {
            this.doNullCheck(insns, extraStack, "instanceof handler", "class type");
            this.checkIsAssignableFrom(insns, extraStack);
        }
        target.replaceNode(typeNode, champion, insns);
        extraStack.apply();
    }
    
    private void checkIsAssignableFrom(final InsnList insns, final Target.Extension extraStack) {
        final LabelNode objectIsNull = new LabelNode();
        final LabelNode checkComplete = new LabelNode();
        insns.add(new InsnNode(95));
        insns.add(new InsnNode(89));
        extraStack.add();
        insns.add(new JumpInsnNode(198, objectIsNull));
        insns.add(new MethodInsnNode(182, "java/lang/Object", "getClass", "()Ljava/lang/Class;", false));
        insns.add(new MethodInsnNode(182, "java/lang/Class", "isAssignableFrom", "(Ljava/lang/Class;)Z", false));
        insns.add(new JumpInsnNode(167, checkComplete));
        insns.add(objectIsNull);
        insns.add(new InsnNode(87));
        insns.add(new InsnNode(87));
        insns.add(new InsnNode(3));
        insns.add(checkComplete);
        extraStack.add();
    }
    
    private void doNullCheck(final InsnList insns, final Target.Extension extraStack, final String type, final String value) {
        final LabelNode nullCheckSucceeded = new LabelNode();
        insns.add(new InsnNode(89));
        insns.add(new JumpInsnNode(199, nullCheckSucceeded));
        this.throwException(insns, extraStack, "java/lang/NullPointerException", String.format("%s %s %s returned null for %s", this.annotationType, type, this, value));
        insns.add(nullCheckSucceeded);
        extraStack.add();
    }
    
    class Meta
    {
        public static final String KEY = "redirector";
        final int priority;
        final boolean isFinal;
        final String name;
        final String desc;
        
        public Meta(final int priority, final boolean isFinal, final String name, final String desc) {
            this.priority = priority;
            this.isFinal = isFinal;
            this.name = name;
            this.desc = desc;
        }
        
        RedirectInjector getOwner() {
            return RedirectInjector.this;
        }
    }
    
    static class ConstructorRedirectData
    {
        public static final String KEY = "ctor";
        boolean wildcard;
        int injected;
        InvalidInjectionException lastException;
        
        ConstructorRedirectData() {
            this.wildcard = false;
            this.injected = 0;
        }
        
        public void throwOrCollect(final InvalidInjectionException ex) {
            if (!this.wildcard) {
                throw ex;
            }
            this.lastException = ex;
        }
    }
    
    static class RedirectedInvokeData extends InjectorData
    {
        final MethodInsnNode node;
        final Type returnType;
        final Type[] targetArgs;
        final Type[] handlerArgs;
        
        RedirectedInvokeData(final Target target, final MethodInsnNode node) {
            super(target);
            this.node = node;
            this.returnType = Type.getReturnType(node.desc);
            this.targetArgs = Type.getArgumentTypes(node.desc);
            this.handlerArgs = ((node.getOpcode() == 184) ? this.targetArgs : ObjectArrays.concat(Type.getObjectType(node.owner), this.targetArgs));
        }
    }
    
    static class RedirectedFieldData extends InjectorData
    {
        final FieldInsnNode node;
        final int opcode;
        final Type owner;
        final Type type;
        final int dimensions;
        final boolean isStatic;
        final boolean isGetter;
        final boolean isSetter;
        Type elementType;
        int extraDimensions;
        
        RedirectedFieldData(final Target target, final FieldInsnNode node) {
            super(target);
            this.extraDimensions = 1;
            this.node = node;
            this.opcode = node.getOpcode();
            this.owner = Type.getObjectType(node.owner);
            this.type = Type.getType(node.desc);
            this.dimensions = ((this.type.getSort() == 9) ? this.type.getDimensions() : 0);
            this.isStatic = (this.opcode == 178 || this.opcode == 179);
            this.isGetter = (this.opcode == 178 || this.opcode == 180);
            this.isSetter = (this.opcode == 179 || this.opcode == 181);
            this.description = (this.isGetter ? "field getter" : (this.isSetter ? "field setter" : "handler"));
        }
        
        int getTotalDimensions() {
            return this.dimensions + this.extraDimensions;
        }
        
        Type[] getArrayArgs(final Type... extra) {
            final int dimensions = this.getTotalDimensions();
            final Type[] args = new Type[dimensions + extra.length];
            for (int i = 0; i < args.length; ++i) {
                args[i] = ((i == 0) ? this.type : ((i < dimensions) ? Type.INT_TYPE : extra[dimensions - i]));
            }
            return args;
        }
    }
}
