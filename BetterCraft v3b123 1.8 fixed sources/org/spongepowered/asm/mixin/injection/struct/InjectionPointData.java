// 
// Decompiled by Procyon v0.6.0
// 

package org.spongepowered.asm.mixin.injection.struct;

import com.google.common.base.Joiner;
import org.spongepowered.asm.util.asm.IAnnotationHandle;
import org.spongepowered.asm.mixin.injection.selectors.dynamic.DynamicSelectorDesc;
import org.spongepowered.asm.util.Annotations;
import org.spongepowered.asm.mixin.injection.selectors.InvalidSelectorException;
import org.spongepowered.asm.mixin.injection.throwables.InvalidInjectionPointException;
import org.spongepowered.asm.mixin.injection.selectors.ISelectorContext;
import org.spongepowered.asm.mixin.injection.selectors.TargetSelector;
import org.spongepowered.asm.mixin.injection.selectors.ITargetSelector;
import org.spongepowered.asm.mixin.injection.modify.LocalVariableDiscriminator;
import org.objectweb.asm.tree.AnnotationNode;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.MethodNode;
import org.spongepowered.asm.mixin.refmap.IMixinContext;
import org.spongepowered.asm.util.IMessageSink;
import java.util.Iterator;
import java.util.regex.Matcher;
import com.google.common.base.Strings;
import java.util.HashMap;
import java.util.List;
import org.spongepowered.asm.mixin.injection.InjectionPoint;
import org.spongepowered.asm.mixin.injection.IInjectionPointContext;
import java.util.Map;
import java.util.regex.Pattern;

public class InjectionPointData
{
    private static final Pattern AT_PATTERN;
    private final Map<String, String> args;
    private final IInjectionPointContext context;
    private final String at;
    private final String type;
    private final InjectionPoint.Selector selector;
    private final String target;
    private final String slice;
    private final int ordinal;
    private final int opcode;
    private final String id;
    
    public InjectionPointData(final IInjectionPointContext context, final String at, final List<String> args, final String target, final String slice, final int ordinal, final int opcode, final String id) {
        this.args = new HashMap<String, String>();
        this.context = context;
        this.at = at;
        this.target = target;
        this.slice = Strings.nullToEmpty(slice);
        this.ordinal = Math.max(-1, ordinal);
        this.opcode = opcode;
        this.id = id;
        this.parseArgs(args);
        this.args.put("target", target);
        this.args.put("ordinal", String.valueOf(ordinal));
        this.args.put("opcode", String.valueOf(opcode));
        final Matcher matcher = InjectionPointData.AT_PATTERN.matcher(at);
        this.type = parseType(matcher, at);
        this.selector = parseSelector(matcher);
    }
    
    private void parseArgs(final List<String> args) {
        if (args == null) {
            return;
        }
        for (final String arg : args) {
            if (arg != null) {
                final int eqPos = arg.indexOf(61);
                if (eqPos > -1) {
                    this.args.put(arg.substring(0, eqPos), arg.substring(eqPos + 1));
                }
                else {
                    this.args.put(arg, "");
                }
            }
        }
    }
    
    public IMessageSink getMessageSink() {
        return this.context;
    }
    
    public String getAt() {
        return this.at;
    }
    
    public String getType() {
        return this.type;
    }
    
    public InjectionPoint.Selector getSelector() {
        return this.selector;
    }
    
    public IInjectionPointContext getContext() {
        return this.context;
    }
    
    public IMixinContext getMixin() {
        return this.context.getMixin();
    }
    
    public MethodNode getMethod() {
        return this.context.getMethod();
    }
    
    public Type getMethodReturnType() {
        return Type.getReturnType(this.getMethod().desc);
    }
    
    public AnnotationNode getParent() {
        return this.context.getAnnotationNode();
    }
    
    public String getSlice() {
        return this.slice;
    }
    
    public LocalVariableDiscriminator getLocalVariableDiscriminator() {
        return LocalVariableDiscriminator.parse(this.getParent());
    }
    
    public String get(final String key, final String defaultValue) {
        final String value = this.args.get(key);
        return (value != null) ? value : defaultValue;
    }
    
    public int get(final String key, final int defaultValue) {
        return parseInt(this.get(key, String.valueOf(defaultValue)), defaultValue);
    }
    
    public boolean get(final String key, final boolean defaultValue) {
        return parseBoolean(this.get(key, String.valueOf(defaultValue)), defaultValue);
    }
    
    public ITargetSelector get(final String key) {
        try {
            return TargetSelector.parseAndValidate(this.get(key, ""), this.context);
        }
        catch (final InvalidSelectorException ex) {
            throw new InvalidInjectionPointException(this.getMixin(), ex, "Failed parsing @At(\"%s\").%s \"%s\" on %s", new Object[] { this.at, key, this.target, this.getDescription() });
        }
    }
    
    public ITargetSelector getTarget() {
        try {
            if (Strings.isNullOrEmpty(this.target)) {
                final IAnnotationHandle selectorAnnotation = this.context.getSelectorAnnotation();
                final AnnotationNode desc = Annotations.getValue(((Annotations.Handle)selectorAnnotation).getNode(), "desc");
                if (desc != null) {
                    final String id = Annotations.getValue(desc, "id", "at");
                    if ("at".equalsIgnoreCase(id)) {
                        return DynamicSelectorDesc.of(Annotations.handleOf(desc), this.context);
                    }
                }
            }
            return TargetSelector.parseAndValidate(this.target, this.context);
        }
        catch (final InvalidSelectorException ex) {
            throw new InvalidInjectionPointException(this.getMixin(), ex, "Failed validating @At(\"%s\").target \"%s\" on %s", new Object[] { this.at, this.target, this.getDescription() });
        }
    }
    
    public String getDescription() {
        return InjectionInfo.describeInjector(this.context.getMixin(), this.context.getAnnotationNode(), this.context.getMethod());
    }
    
    public int getOrdinal() {
        return this.ordinal;
    }
    
    public int getOpcode() {
        return this.opcode;
    }
    
    public int getOpcode(final int defaultOpcode) {
        return (this.opcode > 0) ? this.opcode : defaultOpcode;
    }
    
    public int getOpcode(final int defaultOpcode, final int... validOpcodes) {
        for (final int validOpcode : validOpcodes) {
            if (this.opcode == validOpcode) {
                return this.opcode;
            }
        }
        return defaultOpcode;
    }
    
    public String getId() {
        return this.id;
    }
    
    @Override
    public String toString() {
        return this.type;
    }
    
    private static Pattern createPattern() {
        return Pattern.compile(String.format("^(.+?)(:(%s))?$", Joiner.on('|').join(InjectionPoint.Selector.values())));
    }
    
    public static String parseType(final String at) {
        final Matcher matcher = InjectionPointData.AT_PATTERN.matcher(at);
        return parseType(matcher, at);
    }
    
    private static String parseType(final Matcher matcher, final String at) {
        return matcher.matches() ? matcher.group(1) : at;
    }
    
    private static InjectionPoint.Selector parseSelector(final Matcher matcher) {
        return (matcher.matches() && matcher.group(3) != null) ? InjectionPoint.Selector.valueOf(matcher.group(3)) : InjectionPoint.Selector.DEFAULT;
    }
    
    private static int parseInt(final String string, final int defaultValue) {
        try {
            return Integer.parseInt(string);
        }
        catch (final Exception ex) {
            return defaultValue;
        }
    }
    
    private static boolean parseBoolean(final String string, final boolean defaultValue) {
        try {
            return Boolean.parseBoolean(string);
        }
        catch (final Exception ex) {
            return defaultValue;
        }
    }
    
    static {
        AT_PATTERN = createPattern();
    }
}
