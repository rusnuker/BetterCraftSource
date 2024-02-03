// 
// Decompiled by Procyon v0.6.0
// 

package org.spongepowered.tools.obfuscation.interfaces;

import javax.lang.model.element.AnnotationValue;
import javax.lang.model.element.AnnotationMirror;
import org.spongepowered.tools.obfuscation.SuppressedBy;
import javax.lang.model.element.Element;
import javax.tools.Diagnostic;

public interface IMessagerSuppressible extends IMessagerEx
{
    void printMessage(final Diagnostic.Kind p0, final CharSequence p1, final Element p2, final SuppressedBy p3);
    
    void printMessage(final MessageType p0, final CharSequence p1, final Element p2, final SuppressedBy p3);
    
    void printMessage(final Diagnostic.Kind p0, final CharSequence p1, final Element p2, final AnnotationMirror p3, final SuppressedBy p4);
    
    void printMessage(final MessageType p0, final CharSequence p1, final Element p2, final AnnotationMirror p3, final SuppressedBy p4);
    
    void printMessage(final Diagnostic.Kind p0, final CharSequence p1, final Element p2, final AnnotationMirror p3, final AnnotationValue p4, final SuppressedBy p5);
    
    void printMessage(final MessageType p0, final CharSequence p1, final Element p2, final AnnotationMirror p3, final AnnotationValue p4, final SuppressedBy p5);
}
