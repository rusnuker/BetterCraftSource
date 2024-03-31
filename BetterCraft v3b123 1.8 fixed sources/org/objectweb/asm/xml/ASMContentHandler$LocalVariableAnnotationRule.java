// 
// Decompiled by Procyon v0.6.0
// 

package org.objectweb.asm.xml;

import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Label;
import org.objectweb.asm.TypePath;
import org.xml.sax.Attributes;

final class ASMContentHandler$LocalVariableAnnotationRule extends ASMContentHandler$Rule
{
    final /* synthetic */ ASMContentHandler this$0;
    
    ASMContentHandler$LocalVariableAnnotationRule(final ASMContentHandler this$0) {
        this.this$0 = this$0;
        super(this$0);
    }
    
    public void begin(final String s, final Attributes attributes) {
        final String value = attributes.getValue("desc");
        final boolean booleanValue = Boolean.valueOf(attributes.getValue("visible"));
        final int int1 = Integer.parseInt(attributes.getValue("typeRef"));
        final TypePath fromString = TypePath.fromString(attributes.getValue("typePath"));
        final String[] split = attributes.getValue("start").split(" ");
        final Label[] start = new Label[split.length];
        for (int i = 0; i < start.length; ++i) {
            start[i] = this.getLabel(split[i]);
        }
        final String[] split2 = attributes.getValue("end").split(" ");
        final Label[] end = new Label[split2.length];
        for (int j = 0; j < end.length; ++j) {
            end[j] = this.getLabel(split2[j]);
        }
        final String[] split3 = attributes.getValue("index").split(" ");
        final int[] index = new int[split3.length];
        for (int k = 0; k < index.length; ++k) {
            index[k] = Integer.parseInt(split3[k]);
        }
        this.this$0.push(((MethodVisitor)this.this$0.peek()).visitLocalVariableAnnotation(int1, fromString, start, end, index, value, booleanValue));
    }
    
    public void end(final String s) {
        final AnnotationVisitor annotationVisitor = (AnnotationVisitor)this.this$0.pop();
        if (annotationVisitor != null) {
            annotationVisitor.visitEnd();
        }
    }
}
