// 
// Decompiled by Procyon v0.6.0
// 

package org.spongepowered.asm.util.asm;

import org.objectweb.asm.Type;
import java.util.List;

public interface IAnnotationHandle
{
    boolean exists();
    
    String getDesc();
    
    List<IAnnotationHandle> getAnnotationList(final String p0);
    
    Type getTypeValue(final String p0);
    
    List<Type> getTypeList(final String p0);
    
    IAnnotationHandle getAnnotation(final String p0);
    
     <T> T getValue(final String p0, final T p1);
    
     <T> T getValue();
    
     <T> T getValue(final String p0);
    
    boolean getBoolean(final String p0, final boolean p1);
    
     <T> List<T> getList();
    
     <T> List<T> getList(final String p0);
}
