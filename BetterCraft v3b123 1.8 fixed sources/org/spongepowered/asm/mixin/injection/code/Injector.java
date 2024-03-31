// 
// Decompiled by Procyon v0.6.0
// 

package org.spongepowered.asm.mixin.injection.code;

import java.util.Collections;
import java.util.HashSet;
import org.spongepowered.asm.service.MixinService;
import org.spongepowered.asm.mixin.transformer.ClassInfo;
import org.objectweb.asm.tree.LdcInsnNode;
import org.objectweb.asm.tree.InsnNode;
import org.objectweb.asm.tree.TypeInsnNode;
import org.objectweb.asm.tree.AnnotationNode;
import java.lang.annotation.Annotation;
import org.spongepowered.asm.util.Annotations;
import org.spongepowered.asm.mixin.injection.Coerce;
import org.spongepowered.asm.util.SignaturePrinter;
import java.util.Arrays;
import com.google.common.collect.ObjectArrays;
import org.objectweb.asm.tree.LabelNode;
import org.objectweb.asm.tree.VarInsnNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.InsnList;
import org.spongepowered.asm.mixin.injection.throwables.InjectionError;
import java.util.Map;
import org.spongepowered.asm.mixin.refmap.IMixinContext;
import org.spongepowered.asm.mixin.injection.selectors.ISelectorContext;
import org.spongepowered.asm.mixin.injection.throwables.InvalidInjectionException;
import java.util.TreeMap;
import java.util.Collection;
import org.spongepowered.asm.mixin.MixinEnvironment;
import java.util.Set;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.spongepowered.asm.mixin.injection.struct.Target;
import java.util.Iterator;
import java.util.ArrayList;
import org.spongepowered.asm.mixin.injection.struct.InjectionNodes;
import org.spongepowered.asm.mixin.injection.InjectionPoint;
import java.util.List;
import org.spongepowered.asm.util.Bytecode;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.ClassNode;
import org.spongepowered.asm.mixin.injection.struct.InjectionInfo;
import org.spongepowered.asm.logging.ILogger;

public abstract class Injector
{
    protected static final ILogger logger;
    protected InjectionInfo info;
    protected final String annotationType;
    protected final ClassNode classNode;
    protected final MethodNode methodNode;
    protected final Type[] methodArgs;
    protected final Type returnType;
    protected final boolean isStatic;
    
    public Injector(final InjectionInfo info, final String annotationType) {
        this.info = info;
        this.annotationType = annotationType;
        this.classNode = info.getClassNode();
        this.methodNode = info.getMethod();
        this.methodArgs = Type.getArgumentTypes(this.methodNode.desc);
        this.returnType = Type.getReturnType(this.methodNode.desc);
        this.isStatic = Bytecode.isStatic(this.methodNode);
    }
    
    @Override
    public String toString() {
        return String.format("%s::%s", this.classNode.name, this.info.getMethodName());
    }
    
    public final List<InjectionNodes.InjectionNode> find(final InjectorTarget injectorTarget, final List<InjectionPoint> injectionPoints) {
        this.sanityCheck(injectorTarget.getTarget(), injectionPoints);
        final List<InjectionNodes.InjectionNode> myNodes = new ArrayList<InjectionNodes.InjectionNode>();
        for (final TargetNode node : this.findTargetNodes(injectorTarget, injectionPoints)) {
            this.addTargetNode(injectorTarget.getTarget(), myNodes, node.insn, node.nominators);
        }
        return myNodes;
    }
    
    protected void addTargetNode(final Target target, final List<InjectionNodes.InjectionNode> myNodes, final AbstractInsnNode node, final Set<InjectionPoint> nominators) {
        myNodes.add(target.addInjectionNode(node));
    }
    
    public final void preInject(final Target target, final List<InjectionNodes.InjectionNode> nodes) {
        for (final InjectionNodes.InjectionNode node : nodes) {
            this.preInject(target, node);
        }
    }
    
    public final void inject(final Target target, final List<InjectionNodes.InjectionNode> nodes) {
        for (final InjectionNodes.InjectionNode node : nodes) {
            if (node.isRemoved()) {
                if (!this.info.getMixin().getOption(MixinEnvironment.Option.DEBUG_VERBOSE)) {
                    continue;
                }
                Injector.logger.warn("Target node for {} was removed by a previous injector in {}", this.info, target);
            }
            else {
                this.inject(target, node);
            }
        }
        for (final InjectionNodes.InjectionNode node : nodes) {
            this.postInject(target, node);
        }
    }
    
    private Collection<TargetNode> findTargetNodes(final InjectorTarget injectorTarget, final List<InjectionPoint> injectionPoints) {
        final IMixinContext mixin = this.info.getMixin();
        final MethodNode method = injectorTarget.getMethod();
        final Map<Integer, TargetNode> targetNodes = new TreeMap<Integer, TargetNode>();
        final Collection<AbstractInsnNode> nodes = new ArrayList<AbstractInsnNode>(32);
        for (final InjectionPoint injectionPoint : injectionPoints) {
            nodes.clear();
            if (injectorTarget.isMerged() && !mixin.getClassName().equals(injectorTarget.getMergedBy()) && !injectionPoint.checkPriority(injectorTarget.getMergedPriority(), mixin.getPriority())) {
                throw new InvalidInjectionException(this.info, String.format("%s on %s with priority %d cannot inject into %s merged by %s with priority %d", injectionPoint, this, mixin.getPriority(), injectorTarget, injectorTarget.getMergedBy(), injectorTarget.getMergedPriority()));
            }
            if (!this.findTargetNodes(method, injectionPoint, injectorTarget, nodes)) {
                continue;
            }
            for (final AbstractInsnNode insn : nodes) {
                final Integer key = method.instructions.indexOf(insn);
                TargetNode targetNode = targetNodes.get(key);
                if (targetNode == null) {
                    targetNode = new TargetNode(insn);
                    targetNodes.put(key, targetNode);
                }
                targetNode.nominators.add(injectionPoint);
            }
        }
        return targetNodes.values();
    }
    
    protected boolean findTargetNodes(final MethodNode into, final InjectionPoint injectionPoint, final InjectorTarget injectorTarget, final Collection<AbstractInsnNode> nodes) {
        return injectionPoint.find(into.desc, injectorTarget.getSlice(injectionPoint), nodes);
    }
    
    protected void sanityCheck(final Target target, final List<InjectionPoint> injectionPoints) {
        if (target.classNode != this.classNode) {
            throw new InvalidInjectionException(this.info, "Target class does not match injector class in " + this);
        }
    }
    
    protected final void checkTargetModifiers(final Target target, final boolean exactMatch) {
        if (exactMatch && target.isStatic != this.isStatic) {
            throw new InvalidInjectionException(this.info, String.format("'static' modifier of handler method does not match target in %s", this));
        }
        if (!exactMatch && !this.isStatic && target.isStatic) {
            throw new InvalidInjectionException(this.info, String.format("non-static callback method %s targets a static method which is not supported", this));
        }
    }
    
    protected void checkTargetForNode(final Target target, final InjectionNodes.InjectionNode node, final InjectionPoint.RestrictTargetLevel targetLevel) {
        if (target.isCtor) {
            if (targetLevel == InjectionPoint.RestrictTargetLevel.METHODS_ONLY) {
                throw new InvalidInjectionException(this.info, String.format("Found %s targetting a constructor in injector %s", this.annotationType, this));
            }
            final Bytecode.DelegateInitialiser superCall = target.findDelegateInitNode();
            if (!superCall.isPresent) {
                throw new InjectionError(String.format("Delegate constructor lookup failed for %s target on %s", this.annotationType, this.info));
            }
            final int superCallIndex = target.indexOf(superCall.insn);
            final int targetIndex = target.indexOf(node.getCurrentTarget());
            if (targetIndex <= superCallIndex) {
                if (targetLevel == InjectionPoint.RestrictTargetLevel.CONSTRUCTORS_AFTER_DELEGATE) {
                    throw new InvalidInjectionException(this.info, String.format("Found %s targetting a constructor before %s() in injector %s", this.annotationType, superCall, this));
                }
                if (!this.isStatic) {
                    throw new InvalidInjectionException(this.info, String.format("%s handler before %s() invocation must be static in injector %s", this.annotationType, superCall, this));
                }
                return;
            }
        }
        this.checkTargetModifiers(target, true);
    }
    
    protected void preInject(final Target target, final InjectionNodes.InjectionNode node) {
    }
    
    protected abstract void inject(final Target p0, final InjectionNodes.InjectionNode p1);
    
    protected void postInject(final Target target, final InjectionNodes.InjectionNode node) {
    }
    
    protected AbstractInsnNode invokeHandler(final InsnList insns) {
        return this.invokeHandler(insns, this.methodNode);
    }
    
    protected AbstractInsnNode invokeHandler(final InsnList insns, final MethodNode handler) {
        final boolean isPrivate = (handler.access & 0x2) != 0x0;
        final int invokeOpcode = this.isStatic ? 184 : (isPrivate ? 183 : 182);
        final MethodInsnNode insn = new MethodInsnNode(invokeOpcode, this.classNode.name, handler.name, handler.desc, false);
        insns.add(insn);
        this.info.addCallbackInvocation(handler);
        return insn;
    }
    
    protected AbstractInsnNode invokeHandlerWithArgs(final Type[] args, final InsnList insns, final int[] argMap) {
        return this.invokeHandlerWithArgs(args, insns, argMap, 0, args.length);
    }
    
    protected AbstractInsnNode invokeHandlerWithArgs(final Type[] args, final InsnList insns, final int[] argMap, final int startArg, final int endArg) {
        if (!this.isStatic) {
            insns.add(new VarInsnNode(25, 0));
        }
        this.pushArgs(args, insns, argMap, startArg, endArg);
        return this.invokeHandler(insns);
    }
    
    protected int[] storeArgs(final Target target, final Type[] args, final InsnList insns, final int start) {
        return this.storeArgs(target, args, insns, start, null, null);
    }
    
    protected int[] storeArgs(final Target target, final Type[] args, final InsnList insns, final int start, final LabelNode from, final LabelNode to) {
        final int[] argMap = target.generateArgMap(args, start);
        this.storeArgs(target, args, insns, argMap, start, args.length, from, to);
        return argMap;
    }
    
    protected void storeArgs(final Target target, final Type[] args, final InsnList insns, final int[] argMap, final int start, final int end) {
        this.storeArgs(target, args, insns, argMap, start, end, null, null);
    }
    
    protected void storeArgs(final Target target, final Type[] args, final InsnList insns, final int[] argMap, final int start, final int end, final LabelNode from, final LabelNode to) {
        for (int arg = end - 1; arg >= start; --arg) {
            insns.add(new VarInsnNode(args[arg].getOpcode(54), argMap[arg]));
            target.addLocalVariable(argMap[arg], String.format("injectorAllocatedLocal%d", argMap[arg]), args[arg].getDescriptor(), from, to);
        }
    }
    
    protected void pushArgs(final Type[] args, final InsnList insns, final int[] argMap, final int start, final int end) {
        this.pushArgs(args, insns, argMap, start, end, null);
    }
    
    protected void pushArgs(final Type[] args, final InsnList insns, final int[] argMap, final int start, final int end, final Target.Extension extension) {
        for (int arg = start; arg < end && arg < args.length; ++arg) {
            insns.add(new VarInsnNode(args[arg].getOpcode(21), argMap[arg]));
            if (extension != null) {
                extension.add(args[arg].getSize());
            }
        }
    }
    
    protected final void validateParams(final InjectorData injector, final Type returnType, final Type... args) {
        final String description = String.format("%s %s method %s from %s", this.annotationType, injector, this, this.info.getMixin());
        int argIndex = 0;
        try {
            injector.coerceReturnType = this.checkCoerce(-1, returnType, description, injector.allowCoerceArgs);
            for (final Type arg : args) {
                if (arg != null) {
                    this.checkCoerce(argIndex, arg, description, injector.allowCoerceArgs);
                    ++argIndex;
                }
            }
            if (argIndex == this.methodArgs.length) {
                return;
            }
            for (int targetArg = 0; targetArg < injector.target.arguments.length && argIndex < this.methodArgs.length; ++targetArg, ++argIndex) {
                this.checkCoerce(argIndex, injector.target.arguments[targetArg], description, true);
                ++injector.captureTargetArgs;
            }
        }
        catch (final InvalidInjectionException ex) {
            final String expected = (this.methodArgs.length > args.length) ? Bytecode.generateDescriptor(returnType, (Type[])ObjectArrays.concat(args, injector.target.arguments, Type.class)) : Bytecode.generateDescriptor(returnType, args);
            throw new InvalidInjectionException(this.info, String.format("%s. Handler signature: %s Expected signature: %s", ex.getMessage(), this.methodNode.desc, expected));
        }
        if (argIndex < this.methodArgs.length) {
            final Type[] extraArgs = Arrays.copyOfRange(this.methodArgs, argIndex, this.methodArgs.length);
            throw new InvalidInjectionException(this.info, String.format("%s has an invalid signature. Found %d unexpected additional method arguments: %s", description, this.methodArgs.length - argIndex, new SignaturePrinter(extraArgs).getFormattedArgs()));
        }
    }
    
    protected final boolean checkCoerce(final int index, final Type toType, final String description, final boolean allowCoercion) {
        if (index >= this.methodArgs.length) {
            throw new InvalidInjectionException(this.info, String.format("%s has an invalid signature. Not enough arguments: expected argument type %s at index %d", description, SignaturePrinter.getTypeName(toType), index));
        }
        final Type fromType = (index < 0) ? this.returnType : this.methodArgs[index];
        final AnnotationNode coerce = Annotations.getInvisibleParameter(this.methodNode, Coerce.class, index);
        final boolean isReturn = index < 0;
        final String argType = isReturn ? "return" : "argument";
        final Object argIndex = isReturn ? "" : (" at index " + index);
        if (fromType.equals(toType)) {
            if (coerce != null && this.info.getMixin().getOption(MixinEnvironment.Option.DEBUG_VERBOSE)) {
                Injector.logger.info("Possibly-redundant @Coerce on {} {} type{}, {} is identical to {}", description, argType, argIndex, SignaturePrinter.getTypeName(toType), SignaturePrinter.getTypeName(fromType));
            }
            return false;
        }
        if (coerce == null || !allowCoercion) {
            final String coerceWarning = (coerce != null) ? ". @Coerce not allowed here" : "";
            throw new InvalidInjectionException(this.info, String.format("%s has an invalid signature. Found unexpected %s type %s%s, expected %s%s", description, argType, SignaturePrinter.getTypeName(fromType), argIndex, SignaturePrinter.getTypeName(toType), coerceWarning));
        }
        final boolean canCoerce = canCoerce(fromType, toType);
        if (!canCoerce) {
            throw new InvalidInjectionException(this.info, String.format("%s has an invalid signature. Cannot @Coerce %s type %s%s to %s", description, argType, SignaturePrinter.getTypeName(toType), argIndex, SignaturePrinter.getTypeName(fromType)));
        }
        return true;
    }
    
    protected void throwException(final InsnList insns, final Target.Extension extraStack, final String exceptionType, final String message) {
        insns.add(new TypeInsnNode(187, exceptionType));
        insns.add(new InsnNode(89));
        insns.add(new LdcInsnNode(message));
        insns.add(new MethodInsnNode(183, exceptionType, "<init>", "(Ljava/lang/String;)V", false));
        insns.add(new InsnNode(191));
        extraStack.add(3);
    }
    
    public static boolean canCoerce(final Type from, final Type to) {
        final int fromSort = from.getSort();
        final int toSort = to.getSort();
        if (fromSort >= 9 && toSort >= 9 && fromSort == toSort) {
            return (fromSort != 9 || from.getDimensions() == to.getDimensions()) && canCoerce(ClassInfo.forType(from, ClassInfo.TypeLookup.ELEMENT_TYPE), ClassInfo.forType(to, ClassInfo.TypeLookup.ELEMENT_TYPE));
        }
        return canCoerce(from.getDescriptor(), to.getDescriptor());
    }
    
    public static boolean canCoerce(final String from, final String to) {
        return from.length() <= 1 && to.length() <= 1 && canCoerce(from.charAt(0), to.charAt(0));
    }
    
    public static boolean canCoerce(final char from, final char to) {
        return to == 'I' && "IBSCZ".indexOf(from) > -1;
    }
    
    private static boolean canCoerce(final ClassInfo from, final ClassInfo to) {
        return from != null && to != null && (to == from || to.hasSuperClass(from, ClassInfo.Traversal.ALL, true));
    }
    
    static {
        logger = MixinService.getService().getLogger("mixin");
    }
    
    public static final class TargetNode
    {
        final AbstractInsnNode insn;
        final Set<InjectionPoint> nominators;
        
        TargetNode(final AbstractInsnNode insn) {
            this.nominators = new HashSet<InjectionPoint>();
            this.insn = insn;
        }
        
        public AbstractInsnNode getNode() {
            return this.insn;
        }
        
        public Set<InjectionPoint> getNominators() {
            return Collections.unmodifiableSet((Set<? extends InjectionPoint>)this.nominators);
        }
        
        @Override
        public boolean equals(final Object obj) {
            return obj != null && obj.getClass() == TargetNode.class && ((TargetNode)obj).insn == this.insn;
        }
        
        @Override
        public int hashCode() {
            return this.insn.hashCode();
        }
    }
    
    public static class InjectorData
    {
        public final Target target;
        public String description;
        public boolean allowCoerceArgs;
        public int captureTargetArgs;
        public boolean coerceReturnType;
        
        public InjectorData(final Target target) {
            this(target, "handler");
        }
        
        public InjectorData(final Target target, final String description) {
            this(target, description, true);
        }
        
        public InjectorData(final Target target, final String description, final boolean allowCoerceArgs) {
            this.captureTargetArgs = 0;
            this.coerceReturnType = false;
            this.target = target;
            this.description = description;
            this.allowCoerceArgs = allowCoerceArgs;
        }
        
        @Override
        public String toString() {
            return this.description;
        }
    }
}
