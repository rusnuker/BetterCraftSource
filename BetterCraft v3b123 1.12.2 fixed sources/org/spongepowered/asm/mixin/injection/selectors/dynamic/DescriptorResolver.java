// 
// Decompiled by Procyon v0.6.0
// 

package org.spongepowered.asm.mixin.injection.selectors.dynamic;

import org.spongepowered.asm.util.PrettyPrinter;
import java.util.LinkedHashSet;
import org.spongepowered.asm.util.Quantifier;
import org.objectweb.asm.Type;
import com.google.common.base.Joiner;
import java.util.Set;
import org.spongepowered.asm.util.asm.IAnnotatedElement;
import org.spongepowered.asm.mixin.transformer.MixinTargetContext;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodNode;
import java.util.Iterator;
import java.util.List;
import org.spongepowered.asm.mixin.injection.Desc;
import java.lang.annotation.Annotation;
import org.spongepowered.asm.mixin.injection.Descriptors;
import org.spongepowered.asm.util.Annotations;
import com.google.common.base.Strings;
import java.util.Collections;
import org.spongepowered.asm.mixin.injection.selectors.ISelectorContext;
import org.spongepowered.asm.util.asm.IAnnotationHandle;

public final class DescriptorResolver
{
    public static String PRINT_ID;
    
    private DescriptorResolver() {
    }
    
    public static IResolvedDescriptor resolve(final IAnnotationHandle desc, final ISelectorContext context) {
        return new Descriptor(Collections.emptySet(), desc, context);
    }
    
    public static IResolvedDescriptor resolve(String id, final ISelectorContext context) {
        IResolverObserver observer = new ResolverObserverBasic();
        if (!Strings.isNullOrEmpty(id)) {
            if (DescriptorResolver.PRINT_ID.equals(id)) {
                observer = new ResolverObserverDebug(context);
                id = "";
            }
            else {
                observer.visit(id, "", "");
            }
        }
        final IAnnotationHandle desc = resolve(id, context, observer, context.getSelectorCoordinate(true));
        observer.postResolve();
        return new Descriptor(observer.getSearched(), desc, context);
    }
    
    private static IAnnotationHandle resolve(final String id, final ISelectorContext context, final IResolverObserver observer, final String coordinate) {
        final IAnnotationHandle annotation = Annotations.handleOf(context.getSelectorAnnotation());
        observer.visit(coordinate, annotation, annotation.toString() + ".desc");
        IAnnotationHandle resolved = resolve(id, context, observer, coordinate, annotation.getAnnotationList("desc"));
        if (resolved != null) {
            return resolved;
        }
        resolved = resolve(id, context, observer, coordinate, context.getMethod(), "method");
        if (resolved != null) {
            return resolved;
        }
        final ISelectorContext root = getRoot(context);
        final String rootCoordinate = root.getSelectorCoordinate(false);
        final String mixinCoordinate = ((root != context || !coordinate.contains(".")) && !rootCoordinate.equals(coordinate)) ? (rootCoordinate + "." + coordinate) : coordinate;
        resolved = resolve(id, context, observer, mixinCoordinate, context.getMixin(), "mixin");
        if (resolved != null) {
            return resolved;
        }
        final ISelectorContext parent = context.getParent();
        if (parent != null) {
            final String parentCoordinate = parent.getSelectorCoordinate(false) + "." + coordinate;
            return resolve(id, parent, observer, parentCoordinate);
        }
        return null;
    }
    
    private static IAnnotationHandle resolve(final String id, final ISelectorContext context, final IResolverObserver observer, final String coordinate, final Object element, final String detail) {
        observer.visit(coordinate, element, detail);
        final IAnnotationHandle descriptors = getVisibleAnnotation(element, Descriptors.class);
        if (descriptors != null) {
            final IAnnotationHandle resolved = resolve(id, context, observer, coordinate, descriptors.getAnnotationList("value"));
            if (resolved != null) {
                return resolved;
            }
        }
        final IAnnotationHandle descriptor = getVisibleAnnotation(element, Desc.class);
        if (descriptor != null) {
            final IAnnotationHandle resolved2 = resolve(id, context, observer, coordinate, descriptor);
            if (resolved2 != null) {
                return resolved2;
            }
        }
        return null;
    }
    
    private static IAnnotationHandle resolve(final String id, final ISelectorContext context, final IResolverObserver observer, final String coordinate, final List<IAnnotationHandle> availableDescriptors) {
        if (availableDescriptors != null) {
            for (final IAnnotationHandle desc : availableDescriptors) {
                final IAnnotationHandle resolved = resolve(id, context, observer, coordinate, desc);
                if (resolved != null) {
                    return resolved;
                }
            }
        }
        return null;
    }
    
    private static IAnnotationHandle resolve(final String id, final ISelectorContext context, final IResolverObserver observer, final String coordinate, final IAnnotationHandle desc) {
        if (desc != null) {
            final String descriptorId = desc.getValue("id", coordinate);
            final boolean implicit = Strings.isNullOrEmpty(id);
            if ((implicit && descriptorId.equalsIgnoreCase(coordinate)) || (!implicit && descriptorId.equalsIgnoreCase(id))) {
                return desc;
            }
        }
        return null;
    }
    
    private static IAnnotationHandle getVisibleAnnotation(final Object element, final Class<? extends Annotation> annotationClass) {
        if (element instanceof MethodNode) {
            return Annotations.handleOf(Annotations.getVisible((MethodNode)element, annotationClass));
        }
        if (element instanceof ClassNode) {
            return Annotations.handleOf(Annotations.getVisible((ClassNode)element, annotationClass));
        }
        if (element instanceof MixinTargetContext) {
            return Annotations.handleOf(Annotations.getVisible(((MixinTargetContext)element).getClassNode(), annotationClass));
        }
        if (element instanceof IAnnotatedElement) {
            final IAnnotationHandle annotation = ((IAnnotatedElement)element).getAnnotation(annotationClass);
            return (annotation != null && annotation.exists()) ? annotation : null;
        }
        if (element == null) {
            return null;
        }
        throw new IllegalStateException("Cannot read visible annotations from element with unknown type: " + element.getClass().getName());
    }
    
    private static ISelectorContext getRoot(ISelectorContext context) {
        for (ISelectorContext parent = context.getParent(); parent != null; parent = context.getParent()) {
            context = parent;
        }
        return context;
    }
    
    static {
        DescriptorResolver.PRINT_ID = "?";
    }
    
    static final class Descriptor implements IResolvedDescriptor
    {
        private final Set<String> searched;
        private final IAnnotationHandle desc;
        private final ISelectorContext context;
        
        Descriptor(final Set<String> searched, final IAnnotationHandle desc, final ISelectorContext context) {
            this.searched = searched;
            this.desc = desc;
            this.context = context;
        }
        
        @Override
        public boolean isResolved() {
            return this.desc != null;
        }
        
        @Override
        public String getResolutionInfo() {
            if (this.searched == null) {
                return "";
            }
            return String.format("Searched coordinates [ \"%s\" ]", Joiner.on("\", \"").join(this.searched));
        }
        
        @Override
        public IAnnotationHandle getAnnotation() {
            return this.desc;
        }
        
        @Override
        public String getId() {
            return (this.desc != null) ? this.desc.getValue("id", "") : "";
        }
        
        @Override
        public Type getOwner() {
            if (this.desc == null) {
                return Type.VOID_TYPE;
            }
            final Type ownerClass = this.desc.getTypeValue("owner");
            if (ownerClass != Type.VOID_TYPE) {
                return ownerClass;
            }
            return (this.context != null) ? Type.getObjectType(this.context.getMixin().getTargetClassRef()) : ownerClass;
        }
        
        @Override
        public String getName() {
            if (this.desc == null) {
                return "";
            }
            final String value = this.desc.getValue("value", "");
            if (!value.isEmpty()) {
                return value;
            }
            return this.desc.getValue("name", "");
        }
        
        @Override
        public Type[] getArgs() {
            if (this.desc == null) {
                return new Type[0];
            }
            final List<Type> args = this.desc.getTypeList("args");
            return args.toArray(new Type[args.size()]);
        }
        
        @Override
        public Type getReturnType() {
            if (this.desc == null) {
                return Type.VOID_TYPE;
            }
            return this.desc.getTypeValue("ret");
        }
        
        @Override
        public Quantifier getMatches() {
            if (this.desc == null) {
                return Quantifier.DEFAULT;
            }
            final int min = Math.max(0, (this.desc != null) ? ((int)this.desc.getValue("min", 0)) : 0);
            final Integer max = (this.desc != null) ? this.desc.getValue("max", (Integer)null) : null;
            return new Quantifier(min, (max != null) ? ((max > 0) ? max : Integer.MAX_VALUE) : -1);
        }
        
        @Override
        public List<IAnnotationHandle> getNext() {
            return (this.desc != null) ? this.desc.getAnnotationList("next") : Collections.emptyList();
        }
    }
    
    static class ResolverObserverBasic implements IResolverObserver
    {
        private final Set<String> searched;
        
        ResolverObserverBasic() {
            this.searched = new LinkedHashSet<String>();
        }
        
        @Override
        public void visit(final String coordinate, final Object element, final String detail) {
            this.searched.add(coordinate);
        }
        
        @Override
        public Set<String> getSearched() {
            return this.searched;
        }
        
        @Override
        public void postResolve() {
        }
    }
    
    static class ResolverObserverDebug extends ResolverObserverBasic
    {
        private final PrettyPrinter printer;
        
        ResolverObserverDebug(final ISelectorContext context) {
            this.printer = new PrettyPrinter();
            this.printer.add("Searching for implicit descriptor").add(context).hr().table();
            this.printer.tr("Context Coordinate:", context.getSelectorCoordinate(true) + " (" + context.getSelectorCoordinate(false) + ")");
            this.printer.tr("Selector Annotation:", context.getSelectorAnnotation());
            this.printer.tr("Root Annotation:", context.getAnnotation());
            this.printer.tr("Method:", context.getMethod()).hr();
            this.printer.table("Search Coordinate", "Search Element", "Detail").th().hr();
        }
        
        @Override
        public void visit(final String coordinate, final Object element, final String detail) {
            super.visit(coordinate, element, detail);
            this.printer.tr(coordinate, element, detail);
        }
        
        @Override
        public void postResolve() {
            this.printer.print();
        }
    }
    
    interface IResolverObserver
    {
        void visit(final String p0, final Object p1, final String p2);
        
        Set<String> getSearched();
        
        void postResolve();
    }
}
