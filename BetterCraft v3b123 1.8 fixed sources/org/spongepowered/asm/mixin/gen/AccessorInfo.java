// 
// Decompiled by Procyon v0.6.0
// 

package org.spongepowered.asm.mixin.gen;

import com.google.common.base.Joiner;
import java.util.Collection;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.Locale;
import java.util.regex.Pattern;
import com.google.common.collect.ImmutableSet;
import java.util.Collections;
import java.util.Set;
import java.util.List;
import org.spongepowered.asm.mixin.injection.selectors.ElementNode;
import org.spongepowered.asm.service.MixinService;
import org.spongepowered.asm.mixin.MixinEnvironment;
import org.spongepowered.asm.mixin.injection.selectors.ISelectorContext;
import org.spongepowered.asm.mixin.injection.selectors.TargetSelector;
import com.google.common.base.Strings;
import org.spongepowered.asm.mixin.injection.struct.MemberInfo;
import org.spongepowered.asm.mixin.refmap.IMixinContext;
import org.spongepowered.asm.mixin.gen.throwables.InvalidAccessorException;
import org.spongepowered.asm.util.Bytecode;
import org.spongepowered.asm.util.Annotations;
import org.spongepowered.asm.mixin.transformer.MixinTargetContext;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.FieldNode;
import org.spongepowered.asm.mixin.injection.selectors.ITargetSelector;
import org.objectweb.asm.Type;
import java.lang.annotation.Annotation;
import org.spongepowered.asm.mixin.struct.SpecialMethodInfo;

public class AccessorInfo extends SpecialMethodInfo
{
    protected final Class<? extends Annotation> annotationClass;
    protected final Type[] argTypes;
    protected final Type returnType;
    protected final boolean isStatic;
    protected final String specifiedName;
    protected final AccessorType type;
    private final Type targetFieldType;
    protected final ITargetSelector target;
    protected FieldNode targetField;
    protected MethodNode targetMethod;
    protected AccessorGenerator generator;
    
    public AccessorInfo(final MixinTargetContext mixin, final MethodNode method) {
        this(mixin, method, Accessor.class);
    }
    
    protected AccessorInfo(final MixinTargetContext mixin, final MethodNode method, final Class<? extends Annotation> annotationClass) {
        super(mixin, method, Annotations.getVisible(method, annotationClass));
        this.annotationClass = annotationClass;
        this.argTypes = Type.getArgumentTypes(method.desc);
        this.returnType = Type.getReturnType(method.desc);
        this.isStatic = Bytecode.isStatic(method);
        this.specifiedName = Annotations.getValue(this.annotation);
        this.type = this.initType();
        this.targetFieldType = this.initTargetFieldType();
        this.target = this.initTarget();
        this.annotation.visit("target", this.target.toString());
    }
    
    protected AccessorType initType() {
        if (this.returnType.equals(Type.VOID_TYPE)) {
            return AccessorType.FIELD_SETTER;
        }
        return AccessorType.FIELD_GETTER;
    }
    
    protected Type initTargetFieldType() {
        switch (this.type) {
            case FIELD_GETTER: {
                if (this.argTypes.length > 0) {
                    throw new InvalidAccessorException(this.mixin, this + " must take exactly 0 arguments, found " + this.argTypes.length);
                }
                return this.returnType;
            }
            case FIELD_SETTER: {
                if (this.argTypes.length != 1) {
                    throw new InvalidAccessorException(this.mixin, this + " must take exactly 1 argument, found " + this.argTypes.length);
                }
                return this.argTypes[0];
            }
            default: {
                throw new InvalidAccessorException(this.mixin, "Computed unsupported accessor type " + this.type + " for " + this);
            }
        }
    }
    
    protected ITargetSelector initTarget() {
        return new MemberInfo(this.getTargetName(this.specifiedName), null, this.targetFieldType.getDescriptor());
    }
    
    protected String getTargetName(final String name) {
        if (!Strings.isNullOrEmpty(name)) {
            return TargetSelector.parseName(name, this);
        }
        final String inflectedTarget = this.inflectTarget();
        if (inflectedTarget == null) {
            throw new InvalidAccessorException(this.mixin, String.format("Failed to inflect target name for %s, supported prefixes: %s", this, this.type.getExpectedPrefixes()));
        }
        return inflectedTarget;
    }
    
    protected String inflectTarget() {
        return inflectTarget(this.method.name, this.type, this.toString(), this, this.mixin.getEnvironment().getOption(MixinEnvironment.Option.DEBUG_VERBOSE));
    }
    
    public static String inflectTarget(final String name, final AccessorType type, final String description, final ISelectorContext context, final boolean verbose) {
        return inflectTarget(AccessorName.of(name), type, description, context, verbose);
    }
    
    public static String inflectTarget(final AccessorName name, final AccessorType type, final String description, final ISelectorContext context, final boolean verbose) {
        if (name != null) {
            if (!type.isExpectedPrefix(name.prefix) && verbose) {
                MixinService.getService().getLogger("mixin").warn("Unexpected prefix for {}, found [{}] expecting {}", description, name.prefix, type.getExpectedPrefixes());
            }
            return TargetSelector.parseName(name.name, context);
        }
        return null;
    }
    
    public final ITargetSelector getTarget() {
        return this.target;
    }
    
    public final Type getTargetFieldType() {
        return this.targetFieldType;
    }
    
    public final FieldNode getTargetField() {
        return this.targetField;
    }
    
    public final MethodNode getTargetMethod() {
        return this.targetMethod;
    }
    
    public final Type getReturnType() {
        return this.returnType;
    }
    
    public final Type[] getArgTypes() {
        return this.argTypes;
    }
    
    public boolean isStatic() {
        return this.isStatic;
    }
    
    @Override
    public String toString() {
        final String typeString = (this.type != null) ? this.type.toString() : "UNPARSED_ACCESSOR";
        return String.format("%s->@%s[%s]::%s%s", this.mixin, Annotations.getSimpleName(this.annotation), typeString, this.methodName, this.method.desc);
    }
    
    public void locate() {
        this.targetField = this.findTargetField();
    }
    
    public void validate() {
        (this.generator = this.type.getGenerator(this)).validate();
    }
    
    public MethodNode generate() {
        final MethodNode generatedAccessor = this.generator.generate();
        Annotations.merge(this.method, generatedAccessor);
        return generatedAccessor;
    }
    
    private FieldNode findTargetField() {
        return this.findTarget(ElementNode.fieldList(this.classNode));
    }
    
    protected <TNode> TNode findTarget(final List<ElementNode<TNode>> nodes) {
        final TargetSelector.Result<TNode> result = TargetSelector.run(this.target.configure(ITargetSelector.Configure.ORPHAN, new String[0]), nodes);
        try {
            return result.getSingleResult(true);
        }
        catch (final IllegalStateException ex) {
            throw new InvalidAccessorException(this, ex.getMessage() + " matching " + this.target + " in " + this.classNode.name + " for " + this);
        }
    }
    
    public static AccessorInfo of(final MixinTargetContext mixin, final MethodNode method, final Class<? extends Annotation> type) {
        if (type == Accessor.class) {
            return new AccessorInfo(mixin, method);
        }
        if (type == Invoker.class) {
            return new InvokerInfo(mixin, method);
        }
        throw new InvalidAccessorException(mixin, "Could not parse accessor for unknown type " + type.getName());
    }
    
    public enum AccessorType
    {
        FIELD_GETTER((Set)ImmutableSet.of("get", "is")) {
            @Override
            AccessorGenerator getGenerator(final AccessorInfo info) {
                return new AccessorGeneratorFieldGetter(info);
            }
        }, 
        FIELD_SETTER((Set)ImmutableSet.of("set")) {
            @Override
            AccessorGenerator getGenerator(final AccessorInfo info) {
                return new AccessorGeneratorFieldSetter(info);
            }
        }, 
        METHOD_PROXY((Set)ImmutableSet.of("call", "invoke")) {
            @Override
            AccessorGenerator getGenerator(final AccessorInfo info) {
                return new AccessorGeneratorMethodProxy(info);
            }
        }, 
        OBJECT_FACTORY((Set)ImmutableSet.of("new", "create")) {
            @Override
            AccessorGenerator getGenerator(final AccessorInfo info) {
                return new AccessorGeneratorObjectFactory(info);
            }
        };
        
        private final Set<String> expectedPrefixes;
        
        private AccessorType(final Set<String> expectedPrefixes) {
            this.expectedPrefixes = expectedPrefixes;
        }
        
        public boolean isExpectedPrefix(final String prefix) {
            return this.expectedPrefixes.contains(prefix);
        }
        
        public Set<String> getExpectedPrefixes() {
            return Collections.unmodifiableSet((Set<? extends String>)this.expectedPrefixes);
        }
        
        abstract AccessorGenerator getGenerator(final AccessorInfo p0);
    }
    
    public static final class AccessorName
    {
        private static final Pattern PATTERN;
        public final String methodName;
        public final String prefix;
        public final String name;
        
        private AccessorName(final String methodName, final String prefix, final String name) {
            this.methodName = methodName;
            this.prefix = prefix;
            this.name = name;
        }
        
        @Override
        public String toString() {
            return super.toString();
        }
        
        public static AccessorName of(final String methodName) {
            return of(methodName, true);
        }
        
        public static AccessorName of(final String methodName, final boolean toMemberCase) {
            final Matcher nameMatcher = AccessorName.PATTERN.matcher(methodName);
            if (nameMatcher.matches()) {
                final String prefix = nameMatcher.group(1);
                final String namePart = nameMatcher.group(2);
                final String firstChar = nameMatcher.group(3);
                final String remainder = nameMatcher.group(4);
                final boolean nameIsUpperCase = isUpperCase(Locale.ROOT, namePart);
                final String name = String.format("%s%s", toLowerCaseIf(Locale.ROOT, firstChar, toMemberCase && !nameIsUpperCase), remainder);
                return new AccessorName(methodName, prefix, name);
            }
            return null;
        }
        
        private static boolean isUpperCase(final Locale locale, final String string) {
            return string.toUpperCase(locale).equals(string);
        }
        
        private static String toLowerCaseIf(final Locale locale, final String string, final boolean condition) {
            return condition ? string.toLowerCase(locale) : string;
        }
        
        private static String getPrefixList() {
            final List<String> prefixes = new ArrayList<String>();
            for (final AccessorType type : AccessorType.values()) {
                prefixes.addAll(type.getExpectedPrefixes());
            }
            return Joiner.on('|').join(prefixes);
        }
        
        static {
            PATTERN = Pattern.compile("^(" + getPrefixList() + ")(([A-Z])(.*?))(_\\$md.*)?$");
        }
    }
}
