// 
// Decompiled by Procyon v0.6.0
// 

package org.spongepowered.asm.util;

import org.spongepowered.asm.mixin.Debug;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Intrinsic;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.service.MixinService;
import java.util.ListIterator;
import java.util.Collections;
import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.google.common.base.Function;
import java.util.Iterator;
import org.objectweb.asm.tree.ClassNode;
import java.util.ArrayList;
import java.util.List;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.FieldNode;
import org.objectweb.asm.Type;
import java.lang.annotation.Annotation;
import org.objectweb.asm.tree.AnnotationNode;
import org.spongepowered.asm.util.asm.IAnnotationHandle;
import java.util.regex.Pattern;

public final class Annotations
{
    private static final Class<?>[] MERGEABLE_MIXIN_ANNOTATIONS;
    private static Pattern mergeableAnnotationPattern;
    
    private Annotations() {
    }
    
    public static IAnnotationHandle handleOf(final Object annotation) {
        if (annotation instanceof IAnnotationHandle) {
            return (IAnnotationHandle)annotation;
        }
        if (annotation instanceof AnnotationNode) {
            return new Handle((AnnotationNode)annotation);
        }
        if (annotation == null) {
            return null;
        }
        throw new IllegalArgumentException("Unsupported annotation type: " + annotation.getClass().getName());
    }
    
    public static String getDesc(final Class<? extends Annotation> annotationType) {
        return Type.getType(annotationType).getInternalName();
    }
    
    public static String getSimpleName(final Class<? extends Annotation> annotationType) {
        return annotationType.getSimpleName();
    }
    
    public static String getSimpleName(final AnnotationNode annotation) {
        return Bytecode.getSimpleName(annotation.desc);
    }
    
    public static void setVisible(final FieldNode field, final Class<? extends Annotation> annotationClass, final Object... value) {
        final AnnotationNode node = createNode(Type.getDescriptor(annotationClass), value);
        field.visibleAnnotations = add(field.visibleAnnotations, node);
    }
    
    public static void setInvisible(final FieldNode field, final Class<? extends Annotation> annotationClass, final Object... value) {
        final AnnotationNode node = createNode(Type.getDescriptor(annotationClass), value);
        field.invisibleAnnotations = add(field.invisibleAnnotations, node);
    }
    
    public static void setVisible(final MethodNode method, final Class<? extends Annotation> annotationClass, final Object... value) {
        final AnnotationNode node = createNode(Type.getDescriptor(annotationClass), value);
        method.visibleAnnotations = add(method.visibleAnnotations, node);
    }
    
    public static void setInvisible(final MethodNode method, final Class<? extends Annotation> annotationClass, final Object... value) {
        final AnnotationNode node = createNode(Type.getDescriptor(annotationClass), value);
        method.invisibleAnnotations = add(method.invisibleAnnotations, node);
    }
    
    private static AnnotationNode createNode(final String annotationType, final Object... value) {
        final AnnotationNode node = new AnnotationNode(annotationType);
        for (int pos = 0; pos < value.length - 1; pos += 2) {
            if (!(value[pos] instanceof String)) {
                throw new IllegalArgumentException("Annotation keys must be strings, found " + value[pos].getClass().getSimpleName() + " with " + value[pos].toString() + " at index " + pos + " creating " + annotationType);
            }
            node.visit((String)value[pos], value[pos + 1]);
        }
        return node;
    }
    
    private static List<AnnotationNode> add(List<AnnotationNode> annotations, final AnnotationNode node) {
        if (annotations == null) {
            annotations = new ArrayList<AnnotationNode>(1);
        }
        else {
            annotations.remove(get(annotations, node.desc));
        }
        annotations.add(node);
        return annotations;
    }
    
    public static AnnotationNode getVisible(final FieldNode field, final Class<? extends Annotation> annotationClass) {
        return get(field.visibleAnnotations, Type.getDescriptor(annotationClass));
    }
    
    public static AnnotationNode getInvisible(final FieldNode field, final Class<? extends Annotation> annotationClass) {
        return get(field.invisibleAnnotations, Type.getDescriptor(annotationClass));
    }
    
    public static AnnotationNode getVisible(final MethodNode method, final Class<? extends Annotation> annotationClass) {
        return get(method.visibleAnnotations, Type.getDescriptor(annotationClass));
    }
    
    public static AnnotationNode getInvisible(final MethodNode method, final Class<? extends Annotation> annotationClass) {
        return get(method.invisibleAnnotations, Type.getDescriptor(annotationClass));
    }
    
    public static AnnotationNode getSingleVisible(final MethodNode method, final Class<? extends Annotation>... annotationClasses) {
        return getSingle(method.visibleAnnotations, annotationClasses);
    }
    
    public static AnnotationNode getSingleInvisible(final MethodNode method, final Class<? extends Annotation>... annotationClasses) {
        return getSingle(method.invisibleAnnotations, annotationClasses);
    }
    
    public static AnnotationNode getVisible(final ClassNode classNode, final Class<? extends Annotation> annotationClass) {
        return get(classNode.visibleAnnotations, Type.getDescriptor(annotationClass));
    }
    
    public static AnnotationNode getInvisible(final ClassNode classNode, final Class<? extends Annotation> annotationClass) {
        return get(classNode.invisibleAnnotations, Type.getDescriptor(annotationClass));
    }
    
    public static AnnotationNode getVisibleParameter(final MethodNode method, final Class<? extends Annotation> annotationClass, final int paramIndex) {
        if (paramIndex < 0) {
            return getVisible(method, annotationClass);
        }
        return getParameter(method.visibleParameterAnnotations, Type.getDescriptor(annotationClass), paramIndex);
    }
    
    public static AnnotationNode getInvisibleParameter(final MethodNode method, final Class<? extends Annotation> annotationClass, final int paramIndex) {
        if (paramIndex < 0) {
            return getInvisible(method, annotationClass);
        }
        return getParameter(method.invisibleParameterAnnotations, Type.getDescriptor(annotationClass), paramIndex);
    }
    
    public static AnnotationNode getParameter(final List<AnnotationNode>[] parameterAnnotations, final String annotationType, final int paramIndex) {
        if (parameterAnnotations == null || paramIndex < 0 || paramIndex >= parameterAnnotations.length) {
            return null;
        }
        return get(parameterAnnotations[paramIndex], annotationType);
    }
    
    public static AnnotationNode get(final List<AnnotationNode> annotations, final String annotationType) {
        if (annotations == null) {
            return null;
        }
        for (final AnnotationNode annotation : annotations) {
            if (annotationType.equals(annotation.desc)) {
                return annotation;
            }
        }
        return null;
    }
    
    private static AnnotationNode getSingle(final List<AnnotationNode> annotations, final Class<? extends Annotation>[] annotationClasses) {
        final List<AnnotationNode> nodes = new ArrayList<AnnotationNode>();
        for (final Class<? extends Annotation> annotationClass : annotationClasses) {
            final AnnotationNode annotation = get(annotations, Type.getDescriptor(annotationClass));
            if (annotation != null) {
                nodes.add(annotation);
            }
        }
        final int foundNodes = nodes.size();
        if (foundNodes > 1) {
            throw new IllegalArgumentException("Conflicting annotations found: " + Lists.transform(nodes, (Function<? super AnnotationNode, ?>)new Function<AnnotationNode, String>() {
                @Override
                public String apply(final AnnotationNode input) {
                    return input.desc;
                }
            }));
        }
        return (foundNodes == 0) ? null : nodes.get(0);
    }
    
    public static <T> T getValue(final AnnotationNode annotation) {
        return getValue(annotation, "value");
    }
    
    public static <T> T getValue(final AnnotationNode annotation, final String key, final T defaultValue) {
        final T returnValue = getValue(annotation, key);
        return (returnValue != null) ? returnValue : defaultValue;
    }
    
    public static <T> T getValue(final AnnotationNode annotation, final String key, final Class<?> annotationClass) {
        Preconditions.checkNotNull(annotationClass, (Object)"annotationClass cannot be null");
        T value = getValue(annotation, key);
        if (value == null) {
            try {
                value = (T)annotationClass.getDeclaredMethod(key, (Class<?>[])new Class[0]).getDefaultValue();
            }
            catch (final NoSuchMethodException ex) {}
        }
        return value;
    }
    
    public static <T> T getValue(final AnnotationNode annotation, final String key) {
        boolean getNextValue = false;
        if (annotation == null || annotation.values == null) {
            return null;
        }
        for (final Object value : annotation.values) {
            if (getNextValue) {
                return (T)value;
            }
            if (!value.equals(key)) {
                continue;
            }
            getNextValue = true;
        }
        return null;
    }
    
    public static <T extends Enum<T>> T getValue(final AnnotationNode annotation, final String key, final Class<T> enumClass, final T defaultValue) {
        final String[] value = getValue(annotation, key);
        if (value == null) {
            return defaultValue;
        }
        return toEnumValue(enumClass, value);
    }
    
    public static <T> List<T> getValue(final AnnotationNode annotation, final String key, final boolean notNull) {
        final Object value = getValue(annotation, key);
        if (value instanceof List) {
            return (List)value;
        }
        if (value != null) {
            final List<T> list = new ArrayList<T>();
            list.add((T)value);
            return list;
        }
        return Collections.emptyList();
    }
    
    public static <T extends Enum<T>> List<T> getValue(final AnnotationNode annotation, final String key, final boolean notNull, final Class<T> enumClass) {
        final Object value = getValue(annotation, key);
        if (value instanceof List) {
            final ListIterator<Object> iter = ((List)value).listIterator();
            while (iter.hasNext()) {
                iter.set(toEnumValue(enumClass, iter.next()));
            }
            return (List)value;
        }
        if (value instanceof String[]) {
            final List<T> list = new ArrayList<T>();
            list.add(toEnumValue(enumClass, (String[])value));
            return list;
        }
        return Collections.emptyList();
    }
    
    public static void setValue(final AnnotationNode annotation, final String key, final Object value) {
        if (annotation == null) {
            return;
        }
        int existingIndex = 0;
        if (annotation.values != null) {
            for (int pos = 0; pos < annotation.values.size() - 1; pos += 2) {
                final String keyName = annotation.values.get(pos).toString();
                if (key.equals(keyName)) {
                    existingIndex = pos + 1;
                    break;
                }
            }
        }
        else {
            annotation.values = new ArrayList<Object>();
        }
        if (existingIndex > 0) {
            annotation.values.set(existingIndex, packValue(value));
            return;
        }
        annotation.values.add(key);
        annotation.values.add(packValue(value));
    }
    
    private static Object packValue(final Object value) {
        final Class<?> type = value.getClass();
        if (type.isEnum()) {
            return new String[] { Type.getDescriptor(type), value.toString() };
        }
        return value;
    }
    
    public static void merge(final ClassNode from, final ClassNode to) {
        to.visibleAnnotations = merge(from.visibleAnnotations, to.visibleAnnotations, "class", from.name);
        to.invisibleAnnotations = merge(from.invisibleAnnotations, to.invisibleAnnotations, "class", from.name);
    }
    
    public static void merge(final MethodNode from, final MethodNode to) {
        to.visibleAnnotations = merge(from.visibleAnnotations, to.visibleAnnotations, "method", from.name);
        to.invisibleAnnotations = merge(from.invisibleAnnotations, to.invisibleAnnotations, "method", from.name);
    }
    
    public static void merge(final FieldNode from, final FieldNode to) {
        to.visibleAnnotations = merge(from.visibleAnnotations, to.visibleAnnotations, "field", from.name);
        to.invisibleAnnotations = merge(from.invisibleAnnotations, to.invisibleAnnotations, "field", from.name);
    }
    
    private static List<AnnotationNode> merge(final List<AnnotationNode> from, List<AnnotationNode> to, final String type, final String name) {
        try {
            if (from == null) {
                return to;
            }
            if (to == null) {
                to = new ArrayList<AnnotationNode>();
            }
            for (final AnnotationNode annotation : from) {
                if (!isMergeableAnnotation(annotation)) {
                    continue;
                }
                final Iterator<AnnotationNode> iter = to.iterator();
                while (iter.hasNext()) {
                    if (iter.next().desc.equals(annotation.desc)) {
                        iter.remove();
                        break;
                    }
                }
                to.add(annotation);
            }
        }
        catch (final Exception ex) {
            MixinService.getService().getLogger("mixin").warn("Exception encountered whilst merging annotations for {} {}", type, name);
        }
        return to;
    }
    
    private static boolean isMergeableAnnotation(final AnnotationNode annotation) {
        return !annotation.desc.startsWith("L" + Constants.MIXIN_PACKAGE_REF) || Annotations.mergeableAnnotationPattern.matcher(annotation.desc).matches();
    }
    
    private static Pattern getMergeableAnnotationPattern() {
        final StringBuilder sb = new StringBuilder("^L(");
        for (int i = 0; i < Annotations.MERGEABLE_MIXIN_ANNOTATIONS.length; ++i) {
            if (i > 0) {
                sb.append('|');
            }
            sb.append(Annotations.MERGEABLE_MIXIN_ANNOTATIONS[i].getName().replace('.', '/'));
        }
        return Pattern.compile(sb.append(");$").toString());
    }
    
    private static <T extends Enum<T>> T toEnumValue(final Class<T> enumClass, final String[] value) {
        if (!enumClass.getName().equals(Type.getType(value[0]).getClassName())) {
            throw new IllegalArgumentException("The supplied enum class does not match the stored enum value");
        }
        return Enum.valueOf(enumClass, value[1]);
    }
    
    static {
        MERGEABLE_MIXIN_ANNOTATIONS = new Class[] { Overwrite.class, Intrinsic.class, Final.class, Debug.class };
        Annotations.mergeableAnnotationPattern = getMergeableAnnotationPattern();
    }
    
    public static class Handle implements IAnnotationHandle
    {
        private final AnnotationNode annotation;
        
        Handle(final AnnotationNode annotation) {
            Preconditions.checkNotNull(annotation, (Object)"annotation");
            this.annotation = annotation;
        }
        
        @Override
        public boolean exists() {
            return true;
        }
        
        public AnnotationNode getNode() {
            return this.annotation;
        }
        
        @Override
        public String getDesc() {
            return Type.getType(this.annotation.desc).getInternalName();
        }
        
        @Override
        public List<IAnnotationHandle> getAnnotationList(final String key) {
            final List<AnnotationNode> value = Annotations.getValue(this.annotation, key, false);
            final List<IAnnotationHandle> list = new ArrayList<IAnnotationHandle>();
            if (value != null) {
                for (final AnnotationNode node : value) {
                    list.add(new Handle(node));
                }
            }
            return list;
        }
        
        @Override
        public Type getTypeValue(final String key) {
            return this.getValue(key, Type.VOID_TYPE);
        }
        
        @Override
        public List<Type> getTypeList(final String key) {
            return this.getList(key);
        }
        
        @Override
        public IAnnotationHandle getAnnotation(final String key) {
            final AnnotationNode value = Annotations.getValue(this.annotation, key);
            return (value != null) ? new Handle(value) : null;
        }
        
        @Override
        public <T> T getValue(final String key, final T defaultValue) {
            return Annotations.getValue(this.annotation, key, defaultValue);
        }
        
        @Override
        public <T> T getValue() {
            return this.getValue("value", (T)null);
        }
        
        @Override
        public <T> T getValue(final String key) {
            return this.getValue(key, (T)null);
        }
        
        @Override
        public boolean getBoolean(final String key, final boolean defaultValue) {
            return this.getValue(key, defaultValue);
        }
        
        @Override
        public <T> List<T> getList() {
            return this.getList("value");
        }
        
        @Override
        public <T> List<T> getList(final String key) {
            return Annotations.getValue(this.annotation, key, false);
        }
        
        @Override
        public String toString() {
            return "@" + Annotations.getSimpleName(this.annotation);
        }
    }
}
