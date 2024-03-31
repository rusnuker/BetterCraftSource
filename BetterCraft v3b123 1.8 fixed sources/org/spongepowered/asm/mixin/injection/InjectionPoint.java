// 
// Decompiled by Procyon v0.6.0
// 

package org.spongepowered.asm.mixin.injection;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Retention;
import java.util.ListIterator;
import java.util.LinkedHashSet;
import java.lang.reflect.Array;
import java.util.ArrayList;
import com.google.common.base.Joiner;
import org.spongepowered.asm.mixin.injection.points.BeforeConstant;
import org.spongepowered.asm.mixin.injection.points.BeforeFinalReturn;
import org.spongepowered.asm.mixin.injection.modify.AfterStoreLocal;
import org.spongepowered.asm.mixin.injection.modify.BeforeLoadLocal;
import org.spongepowered.asm.mixin.injection.points.AfterInvoke;
import org.spongepowered.asm.mixin.injection.points.MethodHead;
import org.spongepowered.asm.mixin.injection.points.JumpInsnPoint;
import org.spongepowered.asm.mixin.injection.points.BeforeStringInvoke;
import org.spongepowered.asm.mixin.injection.points.BeforeReturn;
import org.spongepowered.asm.mixin.injection.points.BeforeNew;
import org.spongepowered.asm.mixin.injection.points.BeforeInvoke;
import org.spongepowered.asm.mixin.injection.points.BeforeFieldAccess;
import java.util.HashMap;
import com.google.common.base.Strings;
import org.spongepowered.asm.mixin.transformer.MixinTargetContext;
import org.spongepowered.asm.mixin.MixinEnvironment;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import org.spongepowered.asm.mixin.injection.throwables.InvalidInjectionException;
import org.spongepowered.asm.service.MixinService;
import java.util.Locale;
import org.spongepowered.asm.util.Annotations;
import java.util.Arrays;
import java.util.Iterator;
import org.spongepowered.asm.mixin.injection.struct.InjectionPointAnnotationContext;
import com.google.common.collect.ImmutableList;
import org.spongepowered.asm.mixin.struct.AnnotatedMethodInfo;
import java.util.List;
import org.objectweb.asm.tree.AnnotationNode;
import org.objectweb.asm.tree.MethodNode;
import org.spongepowered.asm.mixin.refmap.IMixinContext;
import org.objectweb.asm.tree.AbstractInsnNode;
import java.util.Collection;
import org.objectweb.asm.tree.InsnList;
import org.spongepowered.asm.mixin.injection.struct.InjectionPointData;
import org.spongepowered.asm.util.IMessageSink;
import java.util.Map;

public abstract class InjectionPoint
{
    public static final int DEFAULT_ALLOWED_SHIFT_BY = 0;
    public static final int MAX_ALLOWED_SHIFT_BY = 5;
    private static Map<String, Class<? extends InjectionPoint>> types;
    private final String slice;
    private final Selector selector;
    private final String id;
    private final IMessageSink messageSink;
    
    protected InjectionPoint() {
        this("", Selector.DEFAULT, null);
    }
    
    protected InjectionPoint(final InjectionPointData data) {
        this(data.getSlice(), data.getSelector(), data.getId(), data.getMessageSink());
    }
    
    public InjectionPoint(final String slice, final Selector selector, final String id) {
        this(slice, selector, id, null);
    }
    
    public InjectionPoint(final String slice, final Selector selector, final String id, final IMessageSink messageSink) {
        this.slice = slice;
        this.selector = selector;
        this.id = id;
        this.messageSink = messageSink;
    }
    
    public String getSlice() {
        return this.slice;
    }
    
    public Selector getSelector() {
        return this.selector;
    }
    
    public String getId() {
        return this.id;
    }
    
    protected void addMessage(final String format, final Object... args) {
        if (this.messageSink != null) {
            this.messageSink.addMessage(format, args);
        }
    }
    
    public boolean checkPriority(final int targetPriority, final int mixinPriority) {
        return targetPriority < mixinPriority;
    }
    
    public RestrictTargetLevel getTargetRestriction(final IInjectionPointContext context) {
        return RestrictTargetLevel.METHODS_ONLY;
    }
    
    public abstract boolean find(final String p0, final InsnList p1, final Collection<AbstractInsnNode> p2);
    
    @Override
    public String toString() {
        return String.format("@At(\"%s\")", this.getAtCode());
    }
    
    protected static AbstractInsnNode nextNode(final InsnList insns, final AbstractInsnNode insn) {
        final int index = insns.indexOf(insn) + 1;
        if (index > 0 && index < insns.size()) {
            return insns.get(index);
        }
        return insn;
    }
    
    public static InjectionPoint and(final InjectionPoint... operands) {
        return new Intersection(operands);
    }
    
    public static InjectionPoint or(final InjectionPoint... operands) {
        return new Union(operands);
    }
    
    public static InjectionPoint after(final InjectionPoint point) {
        return new Shift(point, 1);
    }
    
    public static InjectionPoint before(final InjectionPoint point) {
        return new Shift(point, -1);
    }
    
    public static InjectionPoint shift(final InjectionPoint point, final int count) {
        return new Shift(point, count);
    }
    
    public static List<InjectionPoint> parse(final IMixinContext context, final MethodNode method, final AnnotationNode parent, final List<AnnotationNode> ats) {
        return parse(new AnnotatedMethodInfo(context, method, parent), ats);
    }
    
    public static List<InjectionPoint> parse(final IInjectionPointContext context, final List<AnnotationNode> ats) {
        final ImmutableList.Builder<InjectionPoint> injectionPoints = ImmutableList.builder();
        for (final AnnotationNode at : ats) {
            final InjectionPoint injectionPoint = parse(new InjectionPointAnnotationContext(context, at, "at"), at);
            if (injectionPoint != null) {
                injectionPoints.add(injectionPoint);
            }
        }
        return injectionPoints.build();
    }
    
    public static InjectionPoint parse(final IInjectionPointContext context, final At at) {
        return parse(context, at.value(), at.shift(), at.by(), Arrays.asList(at.args()), at.target(), at.slice(), at.ordinal(), at.opcode(), at.id());
    }
    
    public static InjectionPoint parse(final IMixinContext context, final MethodNode method, final AnnotationNode parent, final At at) {
        return parse(new AnnotatedMethodInfo(context, method, parent), at.value(), at.shift(), at.by(), Arrays.asList(at.args()), at.target(), at.slice(), at.ordinal(), at.opcode(), at.id());
    }
    
    public static InjectionPoint parse(final IMixinContext context, final MethodNode method, final AnnotationNode parent, final AnnotationNode at) {
        return parse(new InjectionPointAnnotationContext(new AnnotatedMethodInfo(context, method, parent), at, "at"), at);
    }
    
    public static InjectionPoint parse(final IInjectionPointContext context, final AnnotationNode at) {
        final String value = Annotations.getValue(at, "value");
        List<String> args = Annotations.getValue(at, "args");
        final String target = Annotations.getValue(at, "target", "");
        final String slice = Annotations.getValue(at, "slice", "");
        final At.Shift shift = Annotations.getValue(at, "shift", At.Shift.class, At.Shift.NONE);
        final int by = Annotations.getValue(at, "by", 0);
        final int ordinal = Annotations.getValue(at, "ordinal", -1);
        final int opcode = Annotations.getValue(at, "opcode", 0);
        final String id = Annotations.getValue(at, "id");
        if (args == null) {
            args = (List<String>)ImmutableList.of();
        }
        return parse(context, value, shift, by, args, target, slice, ordinal, opcode, id);
    }
    
    public static InjectionPoint parse(final IMixinContext context, final MethodNode method, final AnnotationNode parent, final String at, final At.Shift shift, final int by, final List<String> args, final String target, final String slice, final int ordinal, final int opcode, final String id) {
        return parse(new AnnotatedMethodInfo(context, method, parent), at, shift, by, args, target, slice, ordinal, opcode, id);
    }
    
    public static InjectionPoint parse(final IInjectionPointContext context, final String at, final At.Shift shift, final int by, final List<String> args, final String target, final String slice, final int ordinal, final int opcode, final String id) {
        final InjectionPointData data = new InjectionPointData(context, at, args, target, slice, ordinal, opcode, id);
        final Class<? extends InjectionPoint> ipClass = findClass(context.getMixin(), data);
        final InjectionPoint point = create(context.getMixin(), data, ipClass);
        return shift(context, point, shift, by);
    }
    
    private static Class<? extends InjectionPoint> findClass(final IMixinContext context, final InjectionPointData data) {
        final String type = data.getType();
        Class<? extends InjectionPoint> ipClass = InjectionPoint.types.get(type.toUpperCase(Locale.ROOT));
        if (ipClass == null) {
            if (type.matches("^([A-Za-z_][A-Za-z0-9_]*[\\.\\$])+[A-Za-z_][A-Za-z0-9_]*$")) {
                try {
                    ipClass = (Class<? extends InjectionPoint>)MixinService.getService().getClassProvider().findClass(type);
                    InjectionPoint.types.put(type, ipClass);
                    return ipClass;
                }
                catch (final Exception ex) {
                    throw new InvalidInjectionException(context, data + " could not be loaded or is not a valid InjectionPoint", ex);
                }
            }
            throw new InvalidInjectionException(context, data + " is not a valid injection point specifier");
        }
        return ipClass;
    }
    
    private static InjectionPoint create(final IMixinContext context, final InjectionPointData data, final Class<? extends InjectionPoint> ipClass) {
        Constructor<? extends InjectionPoint> ipCtor = null;
        try {
            ipCtor = ipClass.getDeclaredConstructor(InjectionPointData.class);
            ipCtor.setAccessible(true);
        }
        catch (final NoSuchMethodException ex) {
            throw new InvalidInjectionException(context, ipClass.getName() + " must contain a constructor which accepts an InjectionPointData", ex);
        }
        InjectionPoint point = null;
        try {
            point = (InjectionPoint)ipCtor.newInstance(data);
        }
        catch (final InvocationTargetException ex2) {
            throw new InvalidInjectionException(context, "Error whilst instancing injection point " + ipClass.getName() + " for " + data.getAt(), ex2.getCause());
        }
        catch (final Exception ex3) {
            throw new InvalidInjectionException(context, "Error whilst instancing injection point " + ipClass.getName() + " for " + data.getAt(), ex3);
        }
        return point;
    }
    
    private static InjectionPoint shift(final IInjectionPointContext context, final InjectionPoint point, final At.Shift shift, final int by) {
        if (point != null) {
            if (shift == At.Shift.BEFORE) {
                return before(point);
            }
            if (shift == At.Shift.AFTER) {
                return after(point);
            }
            if (shift == At.Shift.BY) {
                validateByValue(context.getMixin(), context.getMethod(), context.getAnnotationNode(), point, by);
                return shift(point, by);
            }
        }
        return point;
    }
    
    private static void validateByValue(final IMixinContext context, final MethodNode method, final AnnotationNode parent, final InjectionPoint point, final int by) {
        final MixinEnvironment env = context.getMixin().getConfig().getEnvironment();
        final ShiftByViolationBehaviour err = env.getOption(MixinEnvironment.Option.SHIFT_BY_VIOLATION_BEHAVIOUR, ShiftByViolationBehaviour.WARN);
        if (err == ShiftByViolationBehaviour.IGNORE) {
            return;
        }
        String limitBreached = "the maximum allowed value: ";
        String advice = "Increase the value of maxShiftBy to suppress this warning.";
        int allowed = 0;
        if (context instanceof MixinTargetContext) {
            allowed = ((MixinTargetContext)context).getMaxShiftByValue();
        }
        if (by <= allowed) {
            return;
        }
        if (by > 5) {
            limitBreached = "MAX_ALLOWED_SHIFT_BY=";
            advice = "You must use an alternate query or a custom injection point.";
            allowed = 5;
        }
        final String message = String.format("@%s(%s) Shift.BY=%d on %s::%s exceeds %s%d. %s", Annotations.getSimpleName(parent), point, by, context, method.name, limitBreached, allowed, advice);
        if (err == ShiftByViolationBehaviour.WARN && allowed < 5) {
            MixinService.getService().getLogger("mixin").warn(message, new Object[0]);
            return;
        }
        throw new InvalidInjectionException(context, message);
    }
    
    protected String getAtCode() {
        final AtCode code = this.getClass().getAnnotation(AtCode.class);
        return (code == null) ? this.getClass().getName() : code.value().toUpperCase();
    }
    
    @Deprecated
    public static void register(final Class<? extends InjectionPoint> type) {
        register(type, null);
    }
    
    public static void register(final Class<? extends InjectionPoint> type, String namespace) {
        final AtCode code = type.getAnnotation(AtCode.class);
        if (code == null) {
            throw new IllegalArgumentException("Injection point class " + type + " is not annotated with @AtCode");
        }
        final String annotationNamespace = code.namespace();
        if (!Strings.isNullOrEmpty(annotationNamespace)) {
            namespace = annotationNamespace;
        }
        final Class<? extends InjectionPoint> existing = InjectionPoint.types.get(code.value());
        if (existing != null && !existing.equals(type)) {
            MixinService.getService().getLogger("mixin").debug("Overriding InjectionPoint {} with {} (previously {})", code.value(), type.getName(), existing.getName());
        }
        else if (Strings.isNullOrEmpty(namespace)) {
            MixinService.getService().getLogger("mixin").warn("Registration of InjectionPoint {} with {} without specifying namespace is deprecated.", code.value(), type.getName());
        }
        String id = code.value().toUpperCase(Locale.ROOT);
        if (!Strings.isNullOrEmpty(namespace)) {
            id = namespace.toUpperCase(Locale.ROOT) + ":" + id;
        }
        InjectionPoint.types.put(id, type);
    }
    
    private static void registerBuiltIn(final Class<? extends InjectionPoint> type) {
        final String code = type.getAnnotation(AtCode.class).value().toUpperCase(Locale.ROOT);
        InjectionPoint.types.put(code, type);
        InjectionPoint.types.put("MIXIN:" + code, type);
    }
    
    static {
        InjectionPoint.types = new HashMap<String, Class<? extends InjectionPoint>>();
        registerBuiltIn(BeforeFieldAccess.class);
        registerBuiltIn(BeforeInvoke.class);
        registerBuiltIn(BeforeNew.class);
        registerBuiltIn(BeforeReturn.class);
        registerBuiltIn(BeforeStringInvoke.class);
        registerBuiltIn(JumpInsnPoint.class);
        registerBuiltIn(MethodHead.class);
        registerBuiltIn(AfterInvoke.class);
        registerBuiltIn(BeforeLoadLocal.class);
        registerBuiltIn(AfterStoreLocal.class);
        registerBuiltIn(BeforeFinalReturn.class);
        registerBuiltIn(BeforeConstant.class);
    }
    
    public enum Selector
    {
        FIRST, 
        LAST, 
        ONE;
        
        public static final Selector DEFAULT;
        
        static {
            DEFAULT = Selector.FIRST;
        }
    }
    
    public enum RestrictTargetLevel
    {
        METHODS_ONLY, 
        CONSTRUCTORS_AFTER_DELEGATE, 
        ALLOW_ALL;
    }
    
    enum ShiftByViolationBehaviour
    {
        IGNORE, 
        WARN, 
        ERROR;
    }
    
    abstract static class CompositeInjectionPoint extends InjectionPoint
    {
        protected final InjectionPoint[] components;
        
        protected CompositeInjectionPoint(final InjectionPoint... components) {
            if (components == null || components.length < 2) {
                throw new IllegalArgumentException("Must supply two or more component injection points for composite point!");
            }
            this.components = components;
        }
        
        @Override
        public String toString() {
            return "CompositeInjectionPoint(" + this.getClass().getSimpleName() + ")[" + Joiner.on(',').join(this.components) + "]";
        }
    }
    
    static final class Intersection extends CompositeInjectionPoint
    {
        public Intersection(final InjectionPoint... points) {
            super(points);
        }
        
        @Override
        public boolean find(final String desc, final InsnList insns, final Collection<AbstractInsnNode> nodes) {
            boolean found = false;
            final ArrayList<AbstractInsnNode>[] allNodes = (ArrayList<AbstractInsnNode>[])Array.newInstance(ArrayList.class, this.components.length);
            for (int i = 0; i < this.components.length; ++i) {
                allNodes[i] = new ArrayList<AbstractInsnNode>();
                this.components[i].find(desc, insns, allNodes[i]);
            }
            final ArrayList<AbstractInsnNode> alpha = allNodes[0];
            for (int nodeIndex = 0; nodeIndex < alpha.size(); ++nodeIndex) {
                final AbstractInsnNode node = alpha.get(nodeIndex);
                final boolean in = true;
                for (int b = 1; b < allNodes.length && allNodes[b].contains(node); ++b) {}
                if (in) {
                    nodes.add(node);
                    found = true;
                }
            }
            return found;
        }
    }
    
    static final class Union extends CompositeInjectionPoint
    {
        public Union(final InjectionPoint... points) {
            super(points);
        }
        
        @Override
        public boolean find(final String desc, final InsnList insns, final Collection<AbstractInsnNode> nodes) {
            final LinkedHashSet<AbstractInsnNode> allNodes = new LinkedHashSet<AbstractInsnNode>();
            for (int i = 0; i < this.components.length; ++i) {
                this.components[i].find(desc, insns, allNodes);
            }
            nodes.addAll(allNodes);
            return allNodes.size() > 0;
        }
    }
    
    static final class Shift extends InjectionPoint
    {
        private final InjectionPoint input;
        private final int shift;
        
        public Shift(final InjectionPoint input, final int shift) {
            if (input == null) {
                throw new IllegalArgumentException("Must supply an input injection point for SHIFT");
            }
            this.input = input;
            this.shift = shift;
        }
        
        @Override
        public String toString() {
            return "InjectionPoint(" + this.getClass().getSimpleName() + ")[" + this.input + "]";
        }
        
        @Override
        public boolean find(final String desc, final InsnList insns, final Collection<AbstractInsnNode> nodes) {
            final List<AbstractInsnNode> list = (nodes instanceof List) ? ((List)nodes) : new ArrayList<AbstractInsnNode>(nodes);
            this.input.find(desc, insns, nodes);
            final ListIterator<AbstractInsnNode> iter = list.listIterator();
            while (iter.hasNext()) {
                final int sourceIndex = insns.indexOf(iter.next());
                final int newIndex = sourceIndex + this.shift;
                if (newIndex >= 0 && newIndex < insns.size()) {
                    iter.set(insns.get(newIndex));
                }
                else {
                    iter.remove();
                    final int absShift = Math.abs(this.shift);
                    final char operator = (absShift != this.shift) ? '-' : '+';
                    this.input.addMessage("@At.shift offset outside the target bounds: Index (index(%d) %s offset(%d) = %d) is outside the allowed range (0-%d)", sourceIndex, operator, absShift, newIndex, insns.size());
                }
            }
            if (nodes != list) {
                nodes.clear();
                nodes.addAll(list);
            }
            return nodes.size() > 0;
        }
    }
    
    @Retention(RetentionPolicy.RUNTIME)
    @Target({ ElementType.TYPE })
    public @interface AtCode {
        String namespace() default "";
        
        String value();
    }
}
