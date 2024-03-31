// 
// Decompiled by Procyon v0.6.0
// 

package org.spongepowered.tools.obfuscation.mirror;

import javax.lang.model.element.TypeElement;
import java.lang.annotation.Annotation;
import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import java.util.ListIterator;
import javax.lang.model.type.TypeMirror;
import org.objectweb.asm.Type;
import java.util.Iterator;
import java.util.ArrayList;
import com.google.common.collect.ImmutableList;
import java.util.Collections;
import java.util.List;
import javax.lang.model.element.AnnotationValue;
import javax.lang.model.element.VariableElement;
import javax.lang.model.element.AnnotationMirror;
import org.spongepowered.asm.util.asm.IAnnotationHandle;

public final class AnnotationHandle implements IAnnotationHandle
{
    public static final AnnotationHandle MISSING;
    private final AnnotationMirror annotation;
    
    private AnnotationHandle(final AnnotationMirror annotation) {
        this.annotation = annotation;
    }
    
    public AnnotationMirror asMirror() {
        return this.annotation;
    }
    
    @Override
    public boolean exists() {
        return this.annotation != null;
    }
    
    @Override
    public String getDesc() {
        if (this.annotation == null) {
            return "java/lang/Annotation";
        }
        return TypeUtils.getInternalName(this.annotation.getAnnotationType());
    }
    
    @Override
    public String toString() {
        if (this.annotation == null) {
            return "@{UnknownAnnotation}";
        }
        return "@" + (Object)this.annotation.getAnnotationType().asElement().getSimpleName();
    }
    
    @Override
    public <T> T getValue(final String key, final T defaultValue) {
        if (this.annotation == null) {
            return defaultValue;
        }
        final AnnotationValue value = this.getAnnotationValue(key);
        if (!(defaultValue instanceof Enum) || value == null) {
            return (T)((value != null) ? value.getValue() : defaultValue);
        }
        final VariableElement varValue = (VariableElement)value.getValue();
        if (varValue == null) {
            return defaultValue;
        }
        return Enum.valueOf(defaultValue.getClass(), varValue.getSimpleName().toString());
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
    public IAnnotationHandle getAnnotation(final String key) {
        final Object value = this.getValue(key);
        if (value instanceof AnnotationMirror) {
            return of((AnnotationMirror)value);
        }
        if (value instanceof AnnotationValue) {
            final Object mirror = ((AnnotationValue)value).getValue();
            if (mirror instanceof AnnotationMirror) {
                return of((AnnotationMirror)mirror);
            }
        }
        return null;
    }
    
    @Override
    public <T> List<T> getList() {
        return this.getList("value");
    }
    
    @Override
    public <T> List<T> getList(final String key) {
        final List<AnnotationValue> list = this.getValue(key, Collections.emptyList());
        return unwrapAnnotationValueList(list);
    }
    
    @Override
    public List<IAnnotationHandle> getAnnotationList(final String key) {
        final Object val = this.getValue(key, (Object)null);
        if (val == null) {
            return Collections.emptyList();
        }
        if (val instanceof AnnotationMirror) {
            return (List<IAnnotationHandle>)ImmutableList.of(of((AnnotationMirror)val));
        }
        final List<AnnotationValue> list = (List<AnnotationValue>)val;
        final List<AnnotationHandle> annotations = new ArrayList<AnnotationHandle>(list.size());
        for (final AnnotationValue value : list) {
            annotations.add(new AnnotationHandle((AnnotationMirror)value.getValue()));
        }
        return Collections.unmodifiableList((List<? extends IAnnotationHandle>)annotations);
    }
    
    @Override
    public Type getTypeValue(final String key) {
        final TypeMirror typeMirror = this.getValue(key);
        return (typeMirror == null) ? Type.VOID_TYPE : Type.getType(TypeUtils.getInternalName(typeMirror));
    }
    
    @Override
    public List<Type> getTypeList(final String key) {
        final List<Type> list = this.getList(key);
        final ListIterator<Type> iter = list.listIterator();
        while (iter.hasNext()) {
            final Object next = iter.next();
            if (next instanceof TypeMirror) {
                iter.set(Type.getType(TypeUtils.getInternalName((TypeMirror)next)));
            }
        }
        return list;
    }
    
    protected AnnotationValue getAnnotationValue(final String key) {
        for (final ExecutableElement elem : this.annotation.getElementValues().keySet()) {
            if (elem.getSimpleName().contentEquals(key)) {
                return (AnnotationValue)this.annotation.getElementValues().get(elem);
            }
        }
        return null;
    }
    
    protected static <T> List<T> unwrapAnnotationValueList(final List<AnnotationValue> list) {
        if (list == null) {
            return Collections.emptyList();
        }
        final List<T> unfolded = new ArrayList<T>(list.size());
        for (final AnnotationValue value : list) {
            unfolded.add((T)value.getValue());
        }
        return unfolded;
    }
    
    protected static AnnotationMirror getAnnotation(final Element elem, final Class<? extends Annotation> annotationClass) {
        if (elem == null) {
            return null;
        }
        final List<? extends AnnotationMirror> annotations = elem.getAnnotationMirrors();
        if (annotations == null) {
            return null;
        }
        for (final AnnotationMirror annotation : annotations) {
            final Element element = annotation.getAnnotationType().asElement();
            if (!(element instanceof TypeElement)) {
                continue;
            }
            final TypeElement annotationElement = (TypeElement)element;
            if (annotationElement.getQualifiedName().contentEquals(annotationClass.getName())) {
                return annotation;
            }
        }
        return null;
    }
    
    public static AnnotationMirror asMirror(final IAnnotationHandle handle) {
        return (handle instanceof AnnotationHandle) ? ((AnnotationHandle)handle).asMirror() : null;
    }
    
    public static AnnotationHandle of(final AnnotationMirror annotation) {
        return new AnnotationHandle(annotation);
    }
    
    public static AnnotationHandle of(final Element elem, final Class<? extends Annotation> annotationClass) {
        return new AnnotationHandle(getAnnotation(elem, annotationClass));
    }
    
    static {
        MISSING = new AnnotationHandle(null);
    }
}
