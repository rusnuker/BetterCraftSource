// 
// Decompiled by Procyon v0.6.0
// 

package org.spongepowered.asm.mixin.injection.selectors;

import java.lang.reflect.InvocationTargetException;
import org.spongepowered.asm.mixin.throwables.MixinException;
import java.lang.reflect.Modifier;
import java.lang.reflect.Method;
import java.lang.annotation.Annotation;
import org.spongepowered.asm.mixin.injection.selectors.dynamic.DynamicSelectorDesc;
import java.util.LinkedHashMap;
import org.spongepowered.asm.mixin.injection.selectors.throwables.SelectorConstraintException;
import java.util.List;
import java.util.ArrayList;
import java.util.regex.Matcher;
import org.spongepowered.asm.mixin.injection.struct.MemberInfo;
import org.objectweb.asm.Type;
import org.spongepowered.asm.util.Annotations;
import org.objectweb.asm.tree.AnnotationNode;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Set;
import org.spongepowered.asm.util.asm.IAnnotationHandle;
import javax.tools.Diagnostic;
import org.spongepowered.asm.util.logging.MessageRouter;
import org.spongepowered.asm.mixin.throwables.MixinError;
import java.util.Locale;
import com.google.common.base.Strings;
import java.util.Map;
import java.util.regex.Pattern;

public final class TargetSelector
{
    private static final String DYNAMIC_SELECTOR_ID = "[a-z]+(:[a-z]+)?";
    private static final Pattern PATTERN_DYNAMIC;
    private static Map<String, DynamicSelectorEntry> dynamicSelectors;
    
    private TargetSelector() {
    }
    
    public static void register(final Class<? extends ITargetSelectorDynamic> type, String namespace) {
        final ITargetSelectorDynamic.SelectorId selectorId = type.getAnnotation(ITargetSelectorDynamic.SelectorId.class);
        if (selectorId == null) {
            throw new IllegalArgumentException("Dynamic target selector class " + type + " is not annotated with @SelectorId");
        }
        final String annotationNamespace = selectorId.namespace();
        if (!Strings.isNullOrEmpty(annotationNamespace)) {
            namespace = annotationNamespace;
        }
        if (Strings.isNullOrEmpty(namespace)) {
            throw new IllegalArgumentException("Dynamic target selector class " + type + " has no namespace. Please specify namespace in SelectorId annotation or declaring configuration");
        }
        DynamicSelectorEntry entry;
        try {
            entry = new DynamicSelectorEntry(namespace.toLowerCase(Locale.ROOT), selectorId.value().toLowerCase(Locale.ROOT), type);
        }
        catch (final NoSuchMethodException ex) {
            throw new MixinError("Dynamic target selector class " + type.getName() + " does not contain a valid parse method");
        }
        final String code = entry.getCode();
        if (!Pattern.matches("[a-z]+(:[a-z]+)?", code)) {
            throw new IllegalArgumentException("Dynamic target selector class " + type + " has an invalid id. Only alpha characters can be used in selector ids and namespaces");
        }
        final DynamicSelectorEntry existing = TargetSelector.dynamicSelectors.get(code);
        if (existing != null) {
            MessageRouter.getMessager().printMessage(Diagnostic.Kind.WARNING, String.format("Overriding target selector for @%s with %s (previously %s)", code, type.getName(), existing.type.getName()));
        }
        else {
            MessageRouter.getMessager().printMessage(Diagnostic.Kind.OTHER, String.format("Registering new target selector for @%s with %s", code, type.getName()));
        }
        TargetSelector.dynamicSelectors.put(code, entry);
    }
    
    private static void registerBuiltIn(final Class<? extends ITargetSelectorDynamic> type) {
        final ITargetSelectorDynamic.SelectorId selectorId = type.getAnnotation(ITargetSelectorDynamic.SelectorId.class);
        DynamicSelectorEntry entry;
        try {
            entry = new DynamicSelectorEntry(null, selectorId.value().toLowerCase(Locale.ROOT), type);
        }
        catch (final NoSuchMethodException ex) {
            throw new MixinError("Dynamic target selector class " + type.getName() + " does not contain a valid parse method");
        }
        TargetSelector.dynamicSelectors.put(entry.id, entry);
        TargetSelector.dynamicSelectors.put("mixin:" + entry.id, entry);
    }
    
    public static ITargetSelector parseAndValidate(final IAnnotationHandle annotation, final ISelectorContext context) throws InvalidSelectorException {
        return parse(annotation, context).validate();
    }
    
    public static ITargetSelector parseAndValidate(final String string, final ISelectorContext context) throws InvalidSelectorException {
        return parse(string, context).validate();
    }
    
    public static Set<ITargetSelector> parseAndValidate(final Iterable<?> selectors, final ISelectorContext context) throws InvalidSelectorException {
        final Set<ITargetSelector> parsed = parse(selectors, context, new LinkedHashSet<ITargetSelector>());
        for (final ITargetSelector selector : parsed) {
            selector.validate();
        }
        return parsed;
    }
    
    public static Set<ITargetSelector> parse(final Iterable<?> selectors, final ISelectorContext context) {
        return parse(selectors, context, new LinkedHashSet<ITargetSelector>());
    }
    
    public static Set<ITargetSelector> parse(final Iterable<?> selectors, final ISelectorContext context, Set<ITargetSelector> parsed) {
        if (parsed == null) {
            parsed = new LinkedHashSet<ITargetSelector>();
        }
        if (selectors != null) {
            for (final Object selector : selectors) {
                if (selector instanceof IAnnotationHandle) {
                    parsed.add(parse((IAnnotationHandle)selector, context));
                }
                else if (selector instanceof AnnotationNode) {
                    parsed.add(parse(Annotations.handleOf(selector), context));
                }
                else if (selector instanceof String) {
                    parsed.add(parse((String)selector, context));
                }
                else if (selector instanceof Class) {
                    final String desc = Type.getType((Class<?>)selector).getDescriptor();
                    parsed.add(parse(desc, context));
                }
                else {
                    if (selector == null) {
                        continue;
                    }
                    parsed.add(parse(selector.toString(), context));
                }
            }
        }
        return parsed;
    }
    
    public static ITargetSelector parse(final IAnnotationHandle annotation, final ISelectorContext context) {
        for (final DynamicSelectorEntry entry : TargetSelector.dynamicSelectors.values()) {
            if (entry.annotation != null && Annotations.getDesc(entry.annotation).equals(annotation.getDesc())) {
                try {
                    return entry.parse(annotation, context);
                }
                catch (final ReflectiveOperationException ex) {
                    return new InvalidSelector(ex.getCause());
                }
                catch (final Exception ex2) {
                    return new InvalidSelector(ex2);
                }
            }
        }
        return new InvalidSelector(new InvalidSelectorException("Dynamic selector for annotation " + annotation + " is not registered."));
    }
    
    public static ITargetSelector parse(String string, final ISelectorContext context) {
        string = string.trim();
        if (string.endsWith("/")) {
            final MemberMatcher regexMatcher = MemberMatcher.parse(string, context);
            if (regexMatcher != null) {
                return regexMatcher;
            }
        }
        if (!string.startsWith("@")) {
            return MemberInfo.parse(string, context);
        }
        final Matcher dynamic = TargetSelector.PATTERN_DYNAMIC.matcher(string);
        if (!dynamic.matches()) {
            return new InvalidSelector(new InvalidSelectorException("Dynamic selector was in an unrecognised format. Parsing selector: " + string));
        }
        final String selectorId = dynamic.group(1).toLowerCase(Locale.ROOT);
        if (!TargetSelector.dynamicSelectors.containsKey(selectorId)) {
            return new InvalidSelector(new InvalidSelectorException("Dynamic selector with id '@" + dynamic.group(1) + "' is not registered. Parsing selector: " + string));
        }
        try {
            return TargetSelector.dynamicSelectors.get(selectorId).parse(Strings.nullToEmpty(dynamic.group(4)).trim(), context);
        }
        catch (final ReflectiveOperationException ex) {
            return new InvalidSelector(ex.getCause(), string);
        }
        catch (final Exception ex2) {
            return new InvalidSelector(ex2);
        }
    }
    
    public static String parseName(final String name, final ISelectorContext context) {
        final ITargetSelector selector = parse(name, context);
        if (!(selector instanceof ITargetSelectorByName)) {
            return name;
        }
        final String mappedName = ((ITargetSelectorByName)selector).getName();
        return (mappedName != null) ? mappedName : name;
    }
    
    public static <TNode> Result<TNode> run(final ITargetSelector selector, final Iterable<ElementNode<TNode>> nodes) {
        final List<ElementNode<TNode>> candidates = new ArrayList<ElementNode<TNode>>();
        final ElementNode<TNode> exactMatch = runSelector(selector, nodes, candidates);
        return new Result<TNode>(exactMatch, candidates);
    }
    
    public static <TNode> Result<TNode> run(final Iterable<ITargetSelector> selector, final Iterable<ElementNode<TNode>> nodes) {
        ElementNode<TNode> exactMatch = null;
        final List<ElementNode<TNode>> candidates = new ArrayList<ElementNode<TNode>>();
        for (final ITargetSelector target : selector) {
            final ElementNode<TNode> selectorExactMatch = runSelector(target, nodes, candidates);
            if (exactMatch == null) {
                exactMatch = selectorExactMatch;
            }
        }
        return new Result<TNode>(exactMatch, candidates);
    }
    
    private static <TNode> ElementNode<TNode> runSelector(final ITargetSelector selector, final Iterable<ElementNode<TNode>> nodes, final List<ElementNode<TNode>> candidates) {
        int matchCount = 0;
        ElementNode<TNode> exactMatch = null;
        for (final ElementNode<TNode> element : nodes) {
            final MatchResult match = selector.match(element);
            if (match.isMatch()) {
                if (++matchCount > selector.getMaxMatchCount()) {
                    break;
                }
                if (!candidates.contains(element)) {
                    candidates.add(element);
                }
                if (exactMatch != null || !match.isExactMatch()) {
                    continue;
                }
                exactMatch = element;
            }
        }
        if (matchCount < selector.getMinMatchCount()) {
            throw new SelectorConstraintException(selector, String.format("%s did not match the required number of targets (required=%d, matched=%d)", selector, selector.getMinMatchCount(), matchCount));
        }
        return exactMatch;
    }
    
    static {
        PATTERN_DYNAMIC = Pattern.compile("(?i)^\\x40([a-z]+(:[a-z]+)?)(\\((.*)\\))?$");
        TargetSelector.dynamicSelectors = new LinkedHashMap<String, DynamicSelectorEntry>();
        registerBuiltIn(DynamicSelectorDesc.class);
    }
    
    public static class Result<TNode>
    {
        public final ElementNode<TNode> exactMatch;
        public final List<ElementNode<TNode>> candidates;
        
        Result(final ElementNode<TNode> exactMatch, final List<ElementNode<TNode>> candidates) {
            this.exactMatch = exactMatch;
            this.candidates = candidates;
        }
        
        public TNode getSingleResult(final boolean strict) {
            final int resultCount = this.candidates.size();
            if (this.exactMatch != null) {
                return this.exactMatch.get();
            }
            if (resultCount == 1 || !strict) {
                return this.candidates.get(0).get();
            }
            throw new IllegalStateException(((resultCount == 0) ? "No" : "Multiple") + " candidates were found");
        }
    }
    
    static class DynamicSelectorEntry
    {
        final String namespace;
        final String id;
        final Class<? extends ITargetSelectorDynamic> type;
        final Class<? extends Annotation> annotation;
        final Method mdParseString;
        final Method mdParseAnnotation;
        
        DynamicSelectorEntry(final String namespace, final String id, final Class<? extends ITargetSelectorDynamic> type) throws NoSuchMethodException {
            this.namespace = namespace;
            this.id = id;
            this.type = type;
            this.mdParseString = type.getDeclaredMethod("parse", String.class, ISelectorContext.class);
            if (!Modifier.isStatic(this.mdParseString.getModifiers())) {
                throw new MixinError("parse method for dynamic target selector [" + this.type.getName() + "] must be static");
            }
            if (!ITargetSelectorDynamic.class.isAssignableFrom(this.mdParseString.getReturnType())) {
                throw new MixinError("parse(String) method for dynamic target selector [" + this.type.getName() + "] must return an ITargetSelectorDynamic subtype");
            }
            Class<? extends Annotation> annotation = null;
            Method mdParseAnnotation = null;
            final ITargetSelectorDynamic.SelectorAnnotation selectorAnnotation = type.getAnnotation(ITargetSelectorDynamic.SelectorAnnotation.class);
            if (selectorAnnotation != null) {
                annotation = selectorAnnotation.value();
                mdParseAnnotation = type.getDeclaredMethod("parse", IAnnotationHandle.class, ISelectorContext.class);
                if (!Modifier.isStatic(mdParseAnnotation.getModifiers())) {
                    throw new MixinError("parse method for dynamic target selector [" + this.type.getName() + "] must be static");
                }
                if (!ITargetSelectorDynamic.class.isAssignableFrom(mdParseAnnotation.getReturnType())) {
                    throw new MixinError("parse(Annotation) method for dynamic target selector [" + this.type.getName() + "] must return an ITargetSelectorDynamic subtype");
                }
            }
            this.annotation = annotation;
            this.mdParseAnnotation = mdParseAnnotation;
        }
        
        String getCode() {
            return ((this.namespace != null) ? (this.namespace + ":") : "") + this.id;
        }
        
        ITargetSelectorDynamic parse(final String input, final ISelectorContext context) throws ReflectiveOperationException {
            return this.parse(input, context, this.mdParseString);
        }
        
        ITargetSelectorDynamic parse(final IAnnotationHandle input, final ISelectorContext context) throws ReflectiveOperationException {
            return this.parse(input, context, this.mdParseAnnotation);
        }
        
        ITargetSelectorDynamic parse(final Object input, final ISelectorContext context, final Method parseMethod) throws ReflectiveOperationException {
            try {
                return (ITargetSelectorDynamic)parseMethod.invoke(null, input, context);
            }
            catch (final InvocationTargetException itex) {
                final Throwable cause = itex.getCause();
                if (cause instanceof MixinException) {
                    throw (MixinException)cause;
                }
                final Throwable ex = (cause != null) ? cause : itex;
                throw new MixinError("Error parsing dynamic target selector [" + this.type.getName() + "] for " + context, ex);
            }
        }
    }
}
