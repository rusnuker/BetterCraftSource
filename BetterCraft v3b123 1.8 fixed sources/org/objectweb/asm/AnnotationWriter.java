// 
// Decompiled by Procyon v0.6.0
// 

package org.objectweb.asm;

final class AnnotationWriter extends AnnotationVisitor
{
    private final SymbolTable symbolTable;
    private final boolean useNamedValues;
    private final ByteVector annotation;
    private final int numElementValuePairsOffset;
    private final AnnotationWriter previousAnnotation;
    private int numElementValuePairs;
    private AnnotationWriter nextAnnotation;
    
    AnnotationWriter(final SymbolTable symbolTable, final boolean useNamedValues, final ByteVector annotation, final AnnotationWriter previousAnnotation) {
        super(458752);
        this.symbolTable = symbolTable;
        this.useNamedValues = useNamedValues;
        this.annotation = annotation;
        this.numElementValuePairsOffset = ((annotation.length == 0) ? -1 : (annotation.length - 2));
        this.previousAnnotation = previousAnnotation;
        if (previousAnnotation != null) {
            previousAnnotation.nextAnnotation = this;
        }
    }
    
    AnnotationWriter(final SymbolTable symbolTable, final ByteVector annotation, final AnnotationWriter previousAnnotation) {
        this(symbolTable, true, annotation, previousAnnotation);
    }
    
    static int computeParameterAnnotationsSize(final String attributeName, final AnnotationWriter[] annotationWriters, final int annotableParameterCount) {
        int attributeSize = 7 + 2 * annotableParameterCount;
        for (final AnnotationWriter annotationWriter : annotationWriters) {
            attributeSize += ((annotationWriter == null) ? 0 : (annotationWriter.computeAnnotationsSize(attributeName) - 8));
        }
        return attributeSize;
    }
    
    static void putParameterAnnotations(final int attributeNameIndex, final AnnotationWriter[] annotationWriters, final int annotableParameterCount, final ByteVector output) {
        int attributeLength = 1 + 2 * annotableParameterCount;
        for (final AnnotationWriter annotationWriter : annotationWriters) {
            attributeLength += ((annotationWriter == null) ? 0 : (annotationWriter.computeAnnotationsSize(null) - 8));
        }
        output.putShort(attributeNameIndex);
        output.putInt(attributeLength);
        output.putByte(annotableParameterCount);
        for (AnnotationWriter annotationWriter : annotationWriters) {
            AnnotationWriter firstAnnotation = null;
            int numAnnotations = 0;
            while (annotationWriter != null) {
                annotationWriter.visitEnd();
                ++numAnnotations;
                firstAnnotation = annotationWriter;
                annotationWriter = annotationWriter.previousAnnotation;
            }
            output.putShort(numAnnotations);
            for (annotationWriter = firstAnnotation; annotationWriter != null; annotationWriter = annotationWriter.nextAnnotation) {
                output.putByteArray(annotationWriter.annotation.data, 0, annotationWriter.annotation.length);
            }
        }
    }
    
    @Override
    public void visit(final String name, final Object value) {
        ++this.numElementValuePairs;
        if (this.useNamedValues) {
            this.annotation.putShort(this.symbolTable.addConstantUtf8(name));
        }
        if (value instanceof String) {
            this.annotation.put12(115, this.symbolTable.addConstantUtf8((String)value));
        }
        else if (value instanceof Byte) {
            this.annotation.put12(66, this.symbolTable.addConstantInteger((byte)value).index);
        }
        else if (value instanceof Boolean) {
            final int booleanValue = ((boolean)value) ? 1 : 0;
            this.annotation.put12(90, this.symbolTable.addConstantInteger(booleanValue).index);
        }
        else if (value instanceof Character) {
            this.annotation.put12(67, this.symbolTable.addConstantInteger((char)value).index);
        }
        else if (value instanceof Short) {
            this.annotation.put12(83, this.symbolTable.addConstantInteger((short)value).index);
        }
        else if (value instanceof Type) {
            this.annotation.put12(99, this.symbolTable.addConstantUtf8(((Type)value).getDescriptor()));
        }
        else if (value instanceof byte[]) {
            final byte[] byteArray = (byte[])value;
            this.annotation.put12(91, byteArray.length);
            byte[] array;
            for (int length = (array = byteArray).length, i = 0; i < length; ++i) {
                final byte byteValue = array[i];
                this.annotation.put12(66, this.symbolTable.addConstantInteger(byteValue).index);
            }
        }
        else if (value instanceof boolean[]) {
            final boolean[] booleanArray = (boolean[])value;
            this.annotation.put12(91, booleanArray.length);
            boolean[] array2;
            for (int length2 = (array2 = booleanArray).length, j = 0; j < length2; ++j) {
                final boolean booleanValue2 = array2[j];
                this.annotation.put12(90, this.symbolTable.addConstantInteger(booleanValue2 ? 1 : 0).index);
            }
        }
        else if (value instanceof short[]) {
            final short[] shortArray = (short[])value;
            this.annotation.put12(91, shortArray.length);
            short[] array3;
            for (int length3 = (array3 = shortArray).length, k = 0; k < length3; ++k) {
                final short shortValue = array3[k];
                this.annotation.put12(83, this.symbolTable.addConstantInteger(shortValue).index);
            }
        }
        else if (value instanceof char[]) {
            final char[] charArray = (char[])value;
            this.annotation.put12(91, charArray.length);
            char[] array4;
            for (int length4 = (array4 = charArray).length, l = 0; l < length4; ++l) {
                final char charValue = array4[l];
                this.annotation.put12(67, this.symbolTable.addConstantInteger(charValue).index);
            }
        }
        else if (value instanceof int[]) {
            final int[] intArray = (int[])value;
            this.annotation.put12(91, intArray.length);
            int[] array5;
            for (int length5 = (array5 = intArray).length, n = 0; n < length5; ++n) {
                final int intValue = array5[n];
                this.annotation.put12(73, this.symbolTable.addConstantInteger(intValue).index);
            }
        }
        else if (value instanceof long[]) {
            final long[] longArray = (long[])value;
            this.annotation.put12(91, longArray.length);
            long[] array6;
            for (int length6 = (array6 = longArray).length, n2 = 0; n2 < length6; ++n2) {
                final long longValue = array6[n2];
                this.annotation.put12(74, this.symbolTable.addConstantLong(longValue).index);
            }
        }
        else if (value instanceof float[]) {
            final float[] floatArray = (float[])value;
            this.annotation.put12(91, floatArray.length);
            float[] array7;
            for (int length7 = (array7 = floatArray).length, n3 = 0; n3 < length7; ++n3) {
                final float floatValue = array7[n3];
                this.annotation.put12(70, this.symbolTable.addConstantFloat(floatValue).index);
            }
        }
        else if (value instanceof double[]) {
            final double[] doubleArray = (double[])value;
            this.annotation.put12(91, doubleArray.length);
            double[] array8;
            for (int length8 = (array8 = doubleArray).length, n4 = 0; n4 < length8; ++n4) {
                final double doubleValue = array8[n4];
                this.annotation.put12(68, this.symbolTable.addConstantDouble(doubleValue).index);
            }
        }
        else {
            final Symbol symbol = this.symbolTable.addConstant(value);
            this.annotation.put12(".s.IFJDCS".charAt(symbol.tag), symbol.index);
        }
    }
    
    @Override
    public void visitEnum(final String name, final String descriptor, final String value) {
        ++this.numElementValuePairs;
        if (this.useNamedValues) {
            this.annotation.putShort(this.symbolTable.addConstantUtf8(name));
        }
        this.annotation.put12(101, this.symbolTable.addConstantUtf8(descriptor)).putShort(this.symbolTable.addConstantUtf8(value));
    }
    
    @Override
    public AnnotationVisitor visitAnnotation(final String name, final String descriptor) {
        ++this.numElementValuePairs;
        if (this.useNamedValues) {
            this.annotation.putShort(this.symbolTable.addConstantUtf8(name));
        }
        this.annotation.put12(64, this.symbolTable.addConstantUtf8(descriptor)).putShort(0);
        return new AnnotationWriter(this.symbolTable, this.annotation, null);
    }
    
    @Override
    public AnnotationVisitor visitArray(final String name) {
        ++this.numElementValuePairs;
        if (this.useNamedValues) {
            this.annotation.putShort(this.symbolTable.addConstantUtf8(name));
        }
        this.annotation.put12(91, 0);
        return new AnnotationWriter(this.symbolTable, false, this.annotation, null);
    }
    
    @Override
    public void visitEnd() {
        if (this.numElementValuePairsOffset != -1) {
            final byte[] data = this.annotation.data;
            data[this.numElementValuePairsOffset] = (byte)(this.numElementValuePairs >>> 8);
            data[this.numElementValuePairsOffset + 1] = (byte)this.numElementValuePairs;
        }
    }
    
    int computeAnnotationsSize(final String attributeName) {
        if (attributeName != null) {
            this.symbolTable.addConstantUtf8(attributeName);
        }
        int attributeSize = 8;
        for (AnnotationWriter annotationWriter = this; annotationWriter != null; annotationWriter = annotationWriter.previousAnnotation) {
            attributeSize += annotationWriter.annotation.length;
        }
        return attributeSize;
    }
    
    void putAnnotations(final int attributeNameIndex, final ByteVector output) {
        int attributeLength = 2;
        int numAnnotations = 0;
        AnnotationWriter annotationWriter = this;
        AnnotationWriter firstAnnotation = null;
        while (annotationWriter != null) {
            annotationWriter.visitEnd();
            attributeLength += annotationWriter.annotation.length;
            ++numAnnotations;
            firstAnnotation = annotationWriter;
            annotationWriter = annotationWriter.previousAnnotation;
        }
        output.putShort(attributeNameIndex);
        output.putInt(attributeLength);
        output.putShort(numAnnotations);
        for (annotationWriter = firstAnnotation; annotationWriter != null; annotationWriter = annotationWriter.nextAnnotation) {
            output.putByteArray(annotationWriter.annotation.data, 0, annotationWriter.annotation.length);
        }
    }
}
