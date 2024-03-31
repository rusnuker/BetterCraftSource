// 
// Decompiled by Procyon v0.6.0
// 

package org.objectweb.asm;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class ClassReader
{
    public static final int SKIP_CODE = 1;
    public static final int SKIP_DEBUG = 2;
    public static final int SKIP_FRAMES = 4;
    public static final int EXPAND_FRAMES = 8;
    static final int EXPAND_ASM_INSNS = 256;
    private static final int INPUT_STREAM_DATA_CHUNK_SIZE = 4096;
    public final byte[] b;
    public final int header;
    private final int[] cpInfoOffsets;
    private final String[] constantUtf8Values;
    private final ConstantDynamic[] constantDynamicValues;
    private final int[] bootstrapMethodOffsets;
    private final int maxStringLength;
    
    public ClassReader(final byte[] classFile) {
        this(classFile, 0, classFile.length);
    }
    
    public ClassReader(final byte[] classFileBuffer, final int classFileOffset, final int classFileLength) {
        this(classFileBuffer, classFileOffset, true);
    }
    
    ClassReader(final byte[] classFileBuffer, final int classFileOffset, final boolean checkClassVersion) {
        this.b = classFileBuffer;
        if (checkClassVersion && this.readShort(classFileOffset + 6) > 56) {
            throw new IllegalArgumentException("Unsupported class file major version " + this.readShort(classFileOffset + 6));
        }
        final int constantPoolCount = this.readUnsignedShort(classFileOffset + 8);
        this.cpInfoOffsets = new int[constantPoolCount];
        this.constantUtf8Values = new String[constantPoolCount];
        int currentCpInfoIndex = 1;
        int currentCpInfoOffset = classFileOffset + 10;
        int currentMaxStringLength = 0;
        boolean hasConstantDynamic = false;
        boolean hasConstantInvokeDynamic = false;
        while (currentCpInfoIndex < constantPoolCount) {
            this.cpInfoOffsets[currentCpInfoIndex++] = currentCpInfoOffset + 1;
            int cpInfoSize = 0;
            switch (classFileBuffer[currentCpInfoOffset]) {
                case 1: {
                    cpInfoSize = 3 + this.readUnsignedShort(currentCpInfoOffset + 1);
                    if (cpInfoSize > currentMaxStringLength) {
                        currentMaxStringLength = cpInfoSize;
                        break;
                    }
                    break;
                }
                default: {
                    throw new IllegalArgumentException();
                }
                case 3:
                case 4:
                case 9:
                case 10:
                case 11:
                case 12: {
                    cpInfoSize = 5;
                    break;
                }
                case 5:
                case 6: {
                    cpInfoSize = 9;
                    ++currentCpInfoIndex;
                    break;
                }
                case 7:
                case 8:
                case 16:
                case 19:
                case 20: {
                    cpInfoSize = 3;
                    break;
                }
                case 15: {
                    cpInfoSize = 4;
                    break;
                }
                case 17: {
                    cpInfoSize = 5;
                    hasConstantDynamic = true;
                    break;
                }
                case 18: {
                    cpInfoSize = 5;
                    hasConstantInvokeDynamic = true;
                    break;
                }
            }
            currentCpInfoOffset += cpInfoSize;
        }
        this.maxStringLength = currentMaxStringLength;
        this.header = currentCpInfoOffset;
        this.constantDynamicValues = (ConstantDynamic[])(hasConstantDynamic ? new ConstantDynamic[constantPoolCount] : null);
        this.bootstrapMethodOffsets = (int[])((hasConstantDynamic | hasConstantInvokeDynamic) ? this.readBootstrapMethodsAttribute(currentMaxStringLength) : null);
    }
    
    public ClassReader(final InputStream inputStream) throws IOException {
        this(readStream(inputStream, false));
    }
    
    public ClassReader(final String className) throws IOException {
        this(readStream(ClassLoader.getSystemResourceAsStream(String.valueOf(className.replace('.', '/')) + ".class"), true));
    }
    
    private static byte[] readStream(final InputStream inputStream, final boolean close) throws IOException {
        if (inputStream == null) {
            throw new IOException("Class not found");
        }
        try {
            final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            final byte[] data = new byte[4096];
            int bytesRead;
            while ((bytesRead = inputStream.read(data, 0, data.length)) != -1) {
                outputStream.write(data, 0, bytesRead);
            }
            outputStream.flush();
            final byte[] var5 = outputStream.toByteArray();
            return var5;
        }
        finally {
            if (close) {
                inputStream.close();
            }
        }
    }
    
    public int getAccess() {
        return this.readUnsignedShort(this.header);
    }
    
    public String getClassName() {
        return this.readClass(this.header + 2, new char[this.maxStringLength]);
    }
    
    public String getSuperName() {
        return this.readClass(this.header + 4, new char[this.maxStringLength]);
    }
    
    public String[] getInterfaces() {
        int currentOffset = this.header + 6;
        final int interfacesCount = this.readUnsignedShort(currentOffset);
        final String[] interfaces = new String[interfacesCount];
        if (interfacesCount > 0) {
            final char[] charBuffer = new char[this.maxStringLength];
            for (int i = 0; i < interfacesCount; ++i) {
                currentOffset += 2;
                interfaces[i] = this.readClass(currentOffset, charBuffer);
            }
        }
        return interfaces;
    }
    
    public void accept(final ClassVisitor classVisitor, final int parsingOptions) {
        this.accept(classVisitor, new Attribute[0], parsingOptions);
    }
    
    public void accept(final ClassVisitor classVisitor, final Attribute[] attributePrototypes, final int parsingOptions) {
        final Context context = new Context();
        context.attributePrototypes = attributePrototypes;
        context.parsingOptions = parsingOptions;
        context.charBuffer = new char[this.maxStringLength];
        final char[] charBuffer = context.charBuffer;
        int currentOffset = this.header;
        int accessFlags = this.readUnsignedShort(currentOffset);
        final String thisClass = this.readClass(currentOffset + 2, charBuffer);
        final String superClass = this.readClass(currentOffset + 4, charBuffer);
        final String[] interfaces = new String[this.readUnsignedShort(currentOffset + 6)];
        currentOffset += 8;
        for (int innerClassesOffset = 0; innerClassesOffset < interfaces.length; ++innerClassesOffset) {
            interfaces[innerClassesOffset] = this.readClass(currentOffset, charBuffer);
            currentOffset += 2;
        }
        int innerClassesOffset = 0;
        int enclosingMethodOffset = 0;
        String signature = null;
        String sourceFile = null;
        String sourceDebugExtension = null;
        int runtimeVisibleAnnotationsOffset = 0;
        int runtimeInvisibleAnnotationsOffset = 0;
        int runtimeVisibleTypeAnnotationsOffset = 0;
        int runtimeInvisibleTypeAnnotationsOffset = 0;
        int moduleOffset = 0;
        int modulePackagesOffset = 0;
        String moduleMainClass = null;
        String nestHostClass = null;
        int nestMembersOffset = 0;
        Attribute attributes = null;
        int currentAttributeOffset = this.getFirstAttributeOffset();
        for (int fieldsCount = this.readUnsignedShort(currentAttributeOffset - 2); fieldsCount > 0; --fieldsCount) {
            final String methodsCount = this.readUTF8(currentAttributeOffset, charBuffer);
            final int annotationDescriptor = this.readInt(currentAttributeOffset + 2);
            currentAttributeOffset += 6;
            if ("SourceFile".equals(methodsCount)) {
                sourceFile = this.readUTF8(currentAttributeOffset, charBuffer);
            }
            else if ("InnerClasses".equals(methodsCount)) {
                innerClassesOffset = currentAttributeOffset;
            }
            else if ("EnclosingMethod".equals(methodsCount)) {
                enclosingMethodOffset = currentAttributeOffset;
            }
            else if ("NestHost".equals(methodsCount)) {
                nestHostClass = this.readClass(currentAttributeOffset, charBuffer);
            }
            else if ("NestMembers".equals(methodsCount)) {
                nestMembersOffset = currentAttributeOffset;
            }
            else if ("Signature".equals(methodsCount)) {
                signature = this.readUTF8(currentAttributeOffset, charBuffer);
            }
            else if ("RuntimeVisibleAnnotations".equals(methodsCount)) {
                runtimeVisibleAnnotationsOffset = currentAttributeOffset;
            }
            else if ("RuntimeVisibleTypeAnnotations".equals(methodsCount)) {
                runtimeVisibleTypeAnnotationsOffset = currentAttributeOffset;
            }
            else if ("Deprecated".equals(methodsCount)) {
                accessFlags |= 0x20000;
            }
            else if ("Synthetic".equals(methodsCount)) {
                accessFlags |= 0x1000;
            }
            else if ("SourceDebugExtension".equals(methodsCount)) {
                sourceDebugExtension = this.readUtf(currentAttributeOffset, annotationDescriptor, new char[annotationDescriptor]);
            }
            else if ("RuntimeInvisibleAnnotations".equals(methodsCount)) {
                runtimeInvisibleAnnotationsOffset = currentAttributeOffset;
            }
            else if ("RuntimeInvisibleTypeAnnotations".equals(methodsCount)) {
                runtimeInvisibleTypeAnnotationsOffset = currentAttributeOffset;
            }
            else if ("Module".equals(methodsCount)) {
                moduleOffset = currentAttributeOffset;
            }
            else if ("ModuleMainClass".equals(methodsCount)) {
                moduleMainClass = this.readClass(currentAttributeOffset, charBuffer);
            }
            else if ("ModulePackages".equals(methodsCount)) {
                modulePackagesOffset = currentAttributeOffset;
            }
            else if (!"BootstrapMethods".equals(methodsCount)) {
                final Attribute type = this.readAttribute(attributePrototypes, methodsCount, currentAttributeOffset, annotationDescriptor, charBuffer, -1, null);
                type.nextAttribute = attributes;
                attributes = type;
            }
            currentAttributeOffset += annotationDescriptor;
        }
        classVisitor.visit(this.readInt(this.cpInfoOffsets[1] - 7), accessFlags, thisClass, signature, superClass, interfaces);
        if ((parsingOptions & 0x2) == 0x0 && (sourceFile != null || sourceDebugExtension != null)) {
            classVisitor.visitSource(sourceFile, sourceDebugExtension);
        }
        if (moduleOffset != 0) {
            this.readModuleAttributes(classVisitor, context, moduleOffset, modulePackagesOffset, moduleMainClass);
        }
        if (nestHostClass != null) {
            classVisitor.visitNestHost(nestHostClass);
        }
        if (enclosingMethodOffset != 0) {
            final String var31 = this.readClass(enclosingMethodOffset, charBuffer);
            final int var32 = this.readUnsignedShort(enclosingMethodOffset + 2);
            final String var33 = (var32 == 0) ? null : this.readUTF8(this.cpInfoOffsets[var32], charBuffer);
            final String var34 = (var32 == 0) ? null : this.readUTF8(this.cpInfoOffsets[var32] + 2, charBuffer);
            classVisitor.visitOuterClass(var31, var33, var34);
        }
        if (runtimeVisibleAnnotationsOffset != 0) {
            int fieldsCount = this.readUnsignedShort(runtimeVisibleAnnotationsOffset);
            int var32 = runtimeVisibleAnnotationsOffset + 2;
            while (fieldsCount-- > 0) {
                final String var33 = this.readUTF8(var32, charBuffer);
                var32 += 2;
                var32 = this.readElementValues(classVisitor.visitAnnotation(var33, true), var32, true, charBuffer);
            }
        }
        if (runtimeInvisibleAnnotationsOffset != 0) {
            int fieldsCount = this.readUnsignedShort(runtimeInvisibleAnnotationsOffset);
            int var32 = runtimeInvisibleAnnotationsOffset + 2;
            while (fieldsCount-- > 0) {
                final String var33 = this.readUTF8(var32, charBuffer);
                var32 += 2;
                var32 = this.readElementValues(classVisitor.visitAnnotation(var33, false), var32, true, charBuffer);
            }
        }
        if (runtimeVisibleTypeAnnotationsOffset != 0) {
            int fieldsCount = this.readUnsignedShort(runtimeVisibleTypeAnnotationsOffset);
            int var32 = runtimeVisibleTypeAnnotationsOffset + 2;
            while (fieldsCount-- > 0) {
                var32 = this.readTypeAnnotationTarget(context, var32);
                final String var33 = this.readUTF8(var32, charBuffer);
                var32 += 2;
                var32 = this.readElementValues(classVisitor.visitTypeAnnotation(context.currentTypeAnnotationTarget, context.currentTypeAnnotationTargetPath, var33, true), var32, true, charBuffer);
            }
        }
        if (runtimeInvisibleTypeAnnotationsOffset != 0) {
            int fieldsCount = this.readUnsignedShort(runtimeInvisibleTypeAnnotationsOffset);
            int var32 = runtimeInvisibleTypeAnnotationsOffset + 2;
            while (fieldsCount-- > 0) {
                var32 = this.readTypeAnnotationTarget(context, var32);
                final String var33 = this.readUTF8(var32, charBuffer);
                var32 += 2;
                var32 = this.readElementValues(classVisitor.visitTypeAnnotation(context.currentTypeAnnotationTarget, context.currentTypeAnnotationTargetPath, var33, false), var32, true, charBuffer);
            }
        }
        while (attributes != null) {
            final Attribute var35 = attributes.nextAttribute;
            attributes.nextAttribute = null;
            classVisitor.visitAttribute(attributes);
            attributes = var35;
        }
        if (nestMembersOffset != 0) {
            int fieldsCount = this.readUnsignedShort(nestMembersOffset);
            int var32 = nestMembersOffset + 2;
            while (fieldsCount-- > 0) {
                classVisitor.visitNestMember(this.readClass(var32, charBuffer));
                var32 += 2;
            }
        }
        if (innerClassesOffset != 0) {
            int fieldsCount = this.readUnsignedShort(innerClassesOffset);
            int var32 = innerClassesOffset + 2;
            while (fieldsCount-- > 0) {
                classVisitor.visitInnerClass(this.readClass(var32, charBuffer), this.readClass(var32 + 2, charBuffer), this.readUTF8(var32 + 4, charBuffer), this.readUnsignedShort(var32 + 6));
                var32 += 8;
            }
        }
        int fieldsCount = this.readUnsignedShort(currentOffset);
        currentOffset += 2;
        while (fieldsCount-- > 0) {
            currentOffset = this.readField(classVisitor, context, currentOffset);
        }
        int var32 = this.readUnsignedShort(currentOffset);
        currentOffset += 2;
        while (var32-- > 0) {
            currentOffset = this.readMethod(classVisitor, context, currentOffset);
        }
        classVisitor.visitEnd();
    }
    
    private void readModuleAttributes(final ClassVisitor classVisitor, final Context context, final int moduleOffset, final int modulePackagesOffset, final String moduleMainClass) {
        final char[] buffer = context.charBuffer;
        final String moduleName = this.readModule(moduleOffset, buffer);
        final int moduleFlags = this.readUnsignedShort(moduleOffset + 2);
        final String moduleVersion = this.readUTF8(moduleOffset + 4, buffer);
        int currentOffset = moduleOffset + 6;
        final ModuleVisitor moduleVisitor = classVisitor.visitModule(moduleName, moduleFlags, moduleVersion);
        if (moduleVisitor != null) {
            if (moduleMainClass != null) {
                moduleVisitor.visitMainClass(moduleMainClass);
            }
            if (modulePackagesOffset != 0) {
                int requiresCount = this.readUnsignedShort(modulePackagesOffset);
                int exportsCount = modulePackagesOffset + 2;
                while (requiresCount-- > 0) {
                    moduleVisitor.visitPackage(this.readPackage(exportsCount, buffer));
                    exportsCount += 2;
                }
            }
            int requiresCount = this.readUnsignedShort(currentOffset);
            currentOffset += 2;
            while (requiresCount-- > 0) {
                final String var21 = this.readModule(currentOffset, buffer);
                final int opensCount = this.readUnsignedShort(currentOffset + 2);
                final String usesCount = this.readUTF8(currentOffset + 4, buffer);
                currentOffset += 6;
                moduleVisitor.visitRequire(var21, opensCount, usesCount);
            }
            int exportsCount = this.readUnsignedShort(currentOffset);
            currentOffset += 2;
            while (exportsCount-- > 0) {
                final String var22 = this.readPackage(currentOffset, buffer);
                final int var23 = this.readUnsignedShort(currentOffset + 2);
                final int providesCount = this.readUnsignedShort(currentOffset + 4);
                currentOffset += 6;
                String[] provides = null;
                if (providesCount != 0) {
                    provides = new String[providesCount];
                    for (int providesWithCount = 0; providesWithCount < providesCount; ++providesWithCount) {
                        provides[providesWithCount] = this.readModule(currentOffset, buffer);
                        currentOffset += 2;
                    }
                }
                moduleVisitor.visitExport(var22, var23, provides);
            }
            int opensCount = this.readUnsignedShort(currentOffset);
            currentOffset += 2;
            while (opensCount-- > 0) {
                final String usesCount = this.readPackage(currentOffset, buffer);
                final int providesCount = this.readUnsignedShort(currentOffset + 2);
                final int var24 = this.readUnsignedShort(currentOffset + 4);
                currentOffset += 6;
                String[] var25 = null;
                if (var24 != 0) {
                    var25 = new String[var24];
                    for (int providesWith = 0; providesWith < var24; ++providesWith) {
                        var25[providesWith] = this.readModule(currentOffset, buffer);
                        currentOffset += 2;
                    }
                }
                moduleVisitor.visitOpen(usesCount, providesCount, var25);
            }
            int var23 = this.readUnsignedShort(currentOffset);
            currentOffset += 2;
            while (var23-- > 0) {
                moduleVisitor.visitUse(this.readClass(currentOffset, buffer));
                currentOffset += 2;
            }
            int providesCount = this.readUnsignedShort(currentOffset);
            currentOffset += 2;
            while (providesCount-- > 0) {
                final String var26 = this.readClass(currentOffset, buffer);
                final int providesWithCount = this.readUnsignedShort(currentOffset + 2);
                currentOffset += 4;
                final String[] var27 = new String[providesWithCount];
                for (int i = 0; i < providesWithCount; ++i) {
                    var27[i] = this.readClass(currentOffset, buffer);
                    currentOffset += 2;
                }
                moduleVisitor.visitProvide(var26, var27);
            }
            moduleVisitor.visitEnd();
        }
    }
    
    private int readField(final ClassVisitor classVisitor, final Context context, final int fieldInfoOffset) {
        final char[] charBuffer = context.charBuffer;
        int accessFlags = this.readUnsignedShort(fieldInfoOffset);
        final String name = this.readUTF8(fieldInfoOffset + 2, charBuffer);
        final String descriptor = this.readUTF8(fieldInfoOffset + 4, charBuffer);
        int currentOffset = fieldInfoOffset + 6;
        Object constantValue = null;
        String signature = null;
        int runtimeVisibleAnnotationsOffset = 0;
        int runtimeInvisibleAnnotationsOffset = 0;
        int runtimeVisibleTypeAnnotationsOffset = 0;
        int runtimeInvisibleTypeAnnotationsOffset = 0;
        Attribute attributes = null;
        int attributesCount = this.readUnsignedShort(currentOffset);
        currentOffset += 2;
        while (attributesCount-- > 0) {
            final String fieldVisitor = this.readUTF8(currentOffset, charBuffer);
            final int nextAttribute = this.readInt(currentOffset + 2);
            currentOffset += 6;
            if ("ConstantValue".equals(fieldVisitor)) {
                final int currentAnnotationOffset = this.readUnsignedShort(currentOffset);
                constantValue = ((currentAnnotationOffset == 0) ? null : this.readConst(currentAnnotationOffset, charBuffer));
            }
            else if ("Signature".equals(fieldVisitor)) {
                signature = this.readUTF8(currentOffset, charBuffer);
            }
            else if ("Deprecated".equals(fieldVisitor)) {
                accessFlags |= 0x20000;
            }
            else if ("Synthetic".equals(fieldVisitor)) {
                accessFlags |= 0x1000;
            }
            else if ("RuntimeVisibleAnnotations".equals(fieldVisitor)) {
                runtimeVisibleAnnotationsOffset = currentOffset;
            }
            else if ("RuntimeVisibleTypeAnnotations".equals(fieldVisitor)) {
                runtimeVisibleTypeAnnotationsOffset = currentOffset;
            }
            else if ("RuntimeInvisibleAnnotations".equals(fieldVisitor)) {
                runtimeInvisibleAnnotationsOffset = currentOffset;
            }
            else if ("RuntimeInvisibleTypeAnnotations".equals(fieldVisitor)) {
                runtimeInvisibleTypeAnnotationsOffset = currentOffset;
            }
            else {
                final Attribute var22 = this.readAttribute(context.attributePrototypes, fieldVisitor, currentOffset, nextAttribute, charBuffer, -1, null);
                var22.nextAttribute = attributes;
                attributes = var22;
            }
            currentOffset += nextAttribute;
        }
        final FieldVisitor var23 = classVisitor.visitField(accessFlags, name, descriptor, signature, constantValue);
        if (var23 == null) {
            return currentOffset;
        }
        if (runtimeVisibleAnnotationsOffset != 0) {
            int nextAttribute = this.readUnsignedShort(runtimeVisibleAnnotationsOffset);
            int currentAnnotationOffset = runtimeVisibleAnnotationsOffset + 2;
            while (nextAttribute-- > 0) {
                final String annotationDescriptor = this.readUTF8(currentAnnotationOffset, charBuffer);
                currentAnnotationOffset += 2;
                currentAnnotationOffset = this.readElementValues(var23.visitAnnotation(annotationDescriptor, true), currentAnnotationOffset, true, charBuffer);
            }
        }
        if (runtimeInvisibleAnnotationsOffset != 0) {
            int nextAttribute = this.readUnsignedShort(runtimeInvisibleAnnotationsOffset);
            int currentAnnotationOffset = runtimeInvisibleAnnotationsOffset + 2;
            while (nextAttribute-- > 0) {
                final String annotationDescriptor = this.readUTF8(currentAnnotationOffset, charBuffer);
                currentAnnotationOffset += 2;
                currentAnnotationOffset = this.readElementValues(var23.visitAnnotation(annotationDescriptor, false), currentAnnotationOffset, true, charBuffer);
            }
        }
        if (runtimeVisibleTypeAnnotationsOffset != 0) {
            int nextAttribute = this.readUnsignedShort(runtimeVisibleTypeAnnotationsOffset);
            int currentAnnotationOffset = runtimeVisibleTypeAnnotationsOffset + 2;
            while (nextAttribute-- > 0) {
                currentAnnotationOffset = this.readTypeAnnotationTarget(context, currentAnnotationOffset);
                final String annotationDescriptor = this.readUTF8(currentAnnotationOffset, charBuffer);
                currentAnnotationOffset += 2;
                currentAnnotationOffset = this.readElementValues(var23.visitTypeAnnotation(context.currentTypeAnnotationTarget, context.currentTypeAnnotationTargetPath, annotationDescriptor, true), currentAnnotationOffset, true, charBuffer);
            }
        }
        if (runtimeInvisibleTypeAnnotationsOffset != 0) {
            int nextAttribute = this.readUnsignedShort(runtimeInvisibleTypeAnnotationsOffset);
            int currentAnnotationOffset = runtimeInvisibleTypeAnnotationsOffset + 2;
            while (nextAttribute-- > 0) {
                currentAnnotationOffset = this.readTypeAnnotationTarget(context, currentAnnotationOffset);
                final String annotationDescriptor = this.readUTF8(currentAnnotationOffset, charBuffer);
                currentAnnotationOffset += 2;
                currentAnnotationOffset = this.readElementValues(var23.visitTypeAnnotation(context.currentTypeAnnotationTarget, context.currentTypeAnnotationTargetPath, annotationDescriptor, false), currentAnnotationOffset, true, charBuffer);
            }
        }
        while (attributes != null) {
            final Attribute var24 = attributes.nextAttribute;
            attributes.nextAttribute = null;
            var23.visitAttribute(attributes);
            attributes = var24;
        }
        var23.visitEnd();
        return currentOffset;
    }
    
    private int readMethod(final ClassVisitor classVisitor, final Context context, final int methodInfoOffset) {
        final char[] charBuffer = context.charBuffer;
        context.currentMethodAccessFlags = this.readUnsignedShort(methodInfoOffset);
        context.currentMethodName = this.readUTF8(methodInfoOffset + 2, charBuffer);
        context.currentMethodDescriptor = this.readUTF8(methodInfoOffset + 4, charBuffer);
        int currentOffset = methodInfoOffset + 6;
        int codeOffset = 0;
        int exceptionsOffset = 0;
        String[] exceptions = null;
        boolean synthetic = false;
        int signatureIndex = 0;
        int runtimeVisibleAnnotationsOffset = 0;
        int runtimeInvisibleAnnotationsOffset = 0;
        int runtimeVisibleParameterAnnotationsOffset = 0;
        int runtimeInvisibleParameterAnnotationsOffset = 0;
        int runtimeVisibleTypeAnnotationsOffset = 0;
        int runtimeInvisibleTypeAnnotationsOffset = 0;
        int annotationDefaultOffset = 0;
        int methodParametersOffset = 0;
        Attribute attributes = null;
        int attributesCount = this.readUnsignedShort(currentOffset);
        currentOffset += 2;
        while (attributesCount-- > 0) {
            final String methodVisitor = this.readUTF8(currentOffset, charBuffer);
            final int nextAttribute = this.readInt(currentOffset + 2);
            currentOffset += 6;
            if ("Code".equals(methodVisitor)) {
                if ((context.parsingOptions & 0x1) == 0x0) {
                    codeOffset = currentOffset;
                }
            }
            else if ("Exceptions".equals(methodVisitor)) {
                exceptionsOffset = currentOffset;
                exceptions = new String[this.readUnsignedShort(currentOffset)];
                int var27 = currentOffset + 2;
                for (int annotationDescriptor = 0; annotationDescriptor < exceptions.length; ++annotationDescriptor) {
                    exceptions[annotationDescriptor] = this.readClass(var27, charBuffer);
                    var27 += 2;
                }
            }
            else if ("Signature".equals(methodVisitor)) {
                signatureIndex = this.readUnsignedShort(currentOffset);
            }
            else if ("Deprecated".equals(methodVisitor)) {
                context.currentMethodAccessFlags |= 0x20000;
            }
            else if ("RuntimeVisibleAnnotations".equals(methodVisitor)) {
                runtimeVisibleAnnotationsOffset = currentOffset;
            }
            else if ("RuntimeVisibleTypeAnnotations".equals(methodVisitor)) {
                runtimeVisibleTypeAnnotationsOffset = currentOffset;
            }
            else if ("AnnotationDefault".equals(methodVisitor)) {
                annotationDefaultOffset = currentOffset;
            }
            else if ("Synthetic".equals(methodVisitor)) {
                synthetic = true;
                context.currentMethodAccessFlags |= 0x1000;
            }
            else if ("RuntimeInvisibleAnnotations".equals(methodVisitor)) {
                runtimeInvisibleAnnotationsOffset = currentOffset;
            }
            else if ("RuntimeInvisibleTypeAnnotations".equals(methodVisitor)) {
                runtimeInvisibleTypeAnnotationsOffset = currentOffset;
            }
            else if ("RuntimeVisibleParameterAnnotations".equals(methodVisitor)) {
                runtimeVisibleParameterAnnotationsOffset = currentOffset;
            }
            else if ("RuntimeInvisibleParameterAnnotations".equals(methodVisitor)) {
                runtimeInvisibleParameterAnnotationsOffset = currentOffset;
            }
            else if ("MethodParameters".equals(methodVisitor)) {
                methodParametersOffset = currentOffset;
            }
            else {
                final Attribute currentAnnotationOffset = this.readAttribute(context.attributePrototypes, methodVisitor, currentOffset, nextAttribute, charBuffer, -1, null);
                currentAnnotationOffset.nextAttribute = attributes;
                attributes = currentAnnotationOffset;
            }
            currentOffset += nextAttribute;
        }
        final MethodVisitor var28 = classVisitor.visitMethod(context.currentMethodAccessFlags, context.currentMethodName, context.currentMethodDescriptor, (signatureIndex == 0) ? null : this.readUtf(signatureIndex, charBuffer), exceptions);
        if (var28 == null) {
            return currentOffset;
        }
        if (var28 instanceof MethodWriter) {
            final MethodWriter var29 = (MethodWriter)var28;
            if (var29.canCopyMethodAttributes(this, methodInfoOffset, currentOffset - methodInfoOffset, synthetic, (context.currentMethodAccessFlags & 0x20000) != 0x0, this.readUnsignedShort(methodInfoOffset + 4), signatureIndex, exceptionsOffset)) {
                return currentOffset;
            }
        }
        if (methodParametersOffset != 0) {
            int nextAttribute = this.readByte(methodParametersOffset);
            int var27 = methodParametersOffset + 1;
            while (nextAttribute-- > 0) {
                var28.visitParameter(this.readUTF8(var27, charBuffer), this.readUnsignedShort(var27 + 2));
                var27 += 4;
            }
        }
        if (annotationDefaultOffset != 0) {
            final AnnotationVisitor var30 = var28.visitAnnotationDefault();
            this.readElementValue(var30, annotationDefaultOffset, null, charBuffer);
            if (var30 != null) {
                var30.visitEnd();
            }
        }
        if (runtimeVisibleAnnotationsOffset != 0) {
            int nextAttribute = this.readUnsignedShort(runtimeVisibleAnnotationsOffset);
            int var27 = runtimeVisibleAnnotationsOffset + 2;
            while (nextAttribute-- > 0) {
                final String var31 = this.readUTF8(var27, charBuffer);
                var27 += 2;
                var27 = this.readElementValues(var28.visitAnnotation(var31, true), var27, true, charBuffer);
            }
        }
        if (runtimeInvisibleAnnotationsOffset != 0) {
            int nextAttribute = this.readUnsignedShort(runtimeInvisibleAnnotationsOffset);
            int var27 = runtimeInvisibleAnnotationsOffset + 2;
            while (nextAttribute-- > 0) {
                final String var31 = this.readUTF8(var27, charBuffer);
                var27 += 2;
                var27 = this.readElementValues(var28.visitAnnotation(var31, false), var27, true, charBuffer);
            }
        }
        if (runtimeVisibleTypeAnnotationsOffset != 0) {
            int nextAttribute = this.readUnsignedShort(runtimeVisibleTypeAnnotationsOffset);
            int var27 = runtimeVisibleTypeAnnotationsOffset + 2;
            while (nextAttribute-- > 0) {
                var27 = this.readTypeAnnotationTarget(context, var27);
                final String var31 = this.readUTF8(var27, charBuffer);
                var27 += 2;
                var27 = this.readElementValues(var28.visitTypeAnnotation(context.currentTypeAnnotationTarget, context.currentTypeAnnotationTargetPath, var31, true), var27, true, charBuffer);
            }
        }
        if (runtimeInvisibleTypeAnnotationsOffset != 0) {
            int nextAttribute = this.readUnsignedShort(runtimeInvisibleTypeAnnotationsOffset);
            int var27 = runtimeInvisibleTypeAnnotationsOffset + 2;
            while (nextAttribute-- > 0) {
                var27 = this.readTypeAnnotationTarget(context, var27);
                final String var31 = this.readUTF8(var27, charBuffer);
                var27 += 2;
                var27 = this.readElementValues(var28.visitTypeAnnotation(context.currentTypeAnnotationTarget, context.currentTypeAnnotationTargetPath, var31, false), var27, true, charBuffer);
            }
        }
        if (runtimeVisibleParameterAnnotationsOffset != 0) {
            this.readParameterAnnotations(var28, context, runtimeVisibleParameterAnnotationsOffset, true);
        }
        if (runtimeInvisibleParameterAnnotationsOffset != 0) {
            this.readParameterAnnotations(var28, context, runtimeInvisibleParameterAnnotationsOffset, false);
        }
        while (attributes != null) {
            final Attribute var32 = attributes.nextAttribute;
            attributes.nextAttribute = null;
            var28.visitAttribute(attributes);
            attributes = var32;
        }
        if (codeOffset != 0) {
            var28.visitCode();
            this.readCode(var28, context, codeOffset);
        }
        var28.visitEnd();
        return currentOffset;
    }
    
    private void readCode(final MethodVisitor methodVisitor, final Context context, final int codeOffset) {
        final byte[] classFileBuffer = this.b;
        final char[] charBuffer = context.charBuffer;
        final int maxStack = this.readUnsignedShort(codeOffset);
        final int maxLocals = this.readUnsignedShort(codeOffset + 2);
        final int codeLength = this.readInt(codeOffset + 4);
        final int bytecodeStartOffset;
        int currentOffset = bytecodeStartOffset = codeOffset + 8;
        final int bytecodeEndOffset = currentOffset + codeLength;
        final Label[] currentMethodLabels = new Label[codeLength + 1];
        context.currentMethodLabels = currentMethodLabels;
        final Label[] labels = currentMethodLabels;
        while (currentOffset < bytecodeEndOffset) {
            final int exceptionTableLength = currentOffset - bytecodeStartOffset;
            final int stackMapFrameOffset = classFileBuffer[currentOffset] & 0xFF;
            switch (stackMapFrameOffset) {
                case 0:
                case 1:
                case 2:
                case 3:
                case 4:
                case 5:
                case 6:
                case 7:
                case 8:
                case 9:
                case 10:
                case 11:
                case 12:
                case 13:
                case 14:
                case 15:
                case 26:
                case 27:
                case 28:
                case 29:
                case 30:
                case 31:
                case 32:
                case 33:
                case 34:
                case 35:
                case 36:
                case 37:
                case 38:
                case 39:
                case 40:
                case 41:
                case 42:
                case 43:
                case 44:
                case 45:
                case 46:
                case 47:
                case 48:
                case 49:
                case 50:
                case 51:
                case 52:
                case 53:
                case 59:
                case 60:
                case 61:
                case 62:
                case 63:
                case 64:
                case 65:
                case 66:
                case 67:
                case 68:
                case 69:
                case 70:
                case 71:
                case 72:
                case 73:
                case 74:
                case 75:
                case 76:
                case 77:
                case 78:
                case 79:
                case 80:
                case 81:
                case 82:
                case 83:
                case 84:
                case 85:
                case 86:
                case 87:
                case 88:
                case 89:
                case 90:
                case 91:
                case 92:
                case 93:
                case 94:
                case 95:
                case 96:
                case 97:
                case 98:
                case 99:
                case 100:
                case 101:
                case 102:
                case 103:
                case 104:
                case 105:
                case 106:
                case 107:
                case 108:
                case 109:
                case 110:
                case 111:
                case 112:
                case 113:
                case 114:
                case 115:
                case 116:
                case 117:
                case 118:
                case 119:
                case 120:
                case 121:
                case 122:
                case 123:
                case 124:
                case 125:
                case 126:
                case 127:
                case 128:
                case 129:
                case 130:
                case 131:
                case 133:
                case 134:
                case 135:
                case 136:
                case 137:
                case 138:
                case 139:
                case 140:
                case 141:
                case 142:
                case 143:
                case 144:
                case 145:
                case 146:
                case 147:
                case 148:
                case 149:
                case 150:
                case 151:
                case 152:
                case 172:
                case 173:
                case 174:
                case 175:
                case 176:
                case 177:
                case 190:
                case 191:
                case 194:
                case 195: {
                    ++currentOffset;
                    continue;
                }
                case 16:
                case 18:
                case 21:
                case 22:
                case 23:
                case 24:
                case 25:
                case 54:
                case 55:
                case 56:
                case 57:
                case 58:
                case 169:
                case 188: {
                    currentOffset += 2;
                    continue;
                }
                case 17:
                case 19:
                case 20:
                case 132:
                case 178:
                case 179:
                case 180:
                case 181:
                case 182:
                case 183:
                case 184:
                case 187:
                case 189:
                case 192:
                case 193: {
                    currentOffset += 3;
                    continue;
                }
                case 153:
                case 154:
                case 155:
                case 156:
                case 157:
                case 158:
                case 159:
                case 160:
                case 161:
                case 162:
                case 163:
                case 164:
                case 165:
                case 166:
                case 167:
                case 168:
                case 198:
                case 199: {
                    this.createLabel(exceptionTableLength + this.readShort(currentOffset + 1), labels);
                    currentOffset += 3;
                    continue;
                }
                case 170: {
                    currentOffset += 4 - (exceptionTableLength & 0x3);
                    this.createLabel(exceptionTableLength + this.readInt(currentOffset), labels);
                    int stackMapTableEndOffset = this.readInt(currentOffset + 8) - this.readInt(currentOffset + 4) + 1;
                    currentOffset += 12;
                    while (stackMapTableEndOffset-- > 0) {
                        this.createLabel(exceptionTableLength + this.readInt(currentOffset), labels);
                        currentOffset += 4;
                    }
                    continue;
                }
                case 171: {
                    currentOffset += 4 - (exceptionTableLength & 0x3);
                    this.createLabel(exceptionTableLength + this.readInt(currentOffset), labels);
                    int compressedFrames = this.readInt(currentOffset + 4);
                    currentOffset += 8;
                    while (compressedFrames-- > 0) {
                        this.createLabel(exceptionTableLength + this.readInt(currentOffset + 4), labels);
                        currentOffset += 8;
                    }
                    continue;
                }
                case 185:
                case 186: {
                    currentOffset += 5;
                    continue;
                }
                case 196: {
                    switch (classFileBuffer[currentOffset + 1] & 0xFF) {
                        case 21:
                        case 22:
                        case 23:
                        case 24:
                        case 25:
                        case 54:
                        case 55:
                        case 56:
                        case 57:
                        case 58:
                        case 169: {
                            currentOffset += 4;
                            continue;
                        }
                        case 132: {
                            currentOffset += 6;
                            continue;
                        }
                        default: {
                            throw new IllegalArgumentException();
                        }
                    }
                    break;
                }
                case 197: {
                    currentOffset += 4;
                    continue;
                }
                case 200:
                case 201:
                case 220: {
                    this.createLabel(exceptionTableLength + this.readInt(currentOffset + 1), labels);
                    currentOffset += 5;
                    continue;
                }
                case 202:
                case 203:
                case 204:
                case 205:
                case 206:
                case 207:
                case 208:
                case 209:
                case 210:
                case 211:
                case 212:
                case 213:
                case 214:
                case 215:
                case 216:
                case 217:
                case 218:
                case 219: {
                    this.createLabel(exceptionTableLength + this.readUnsignedShort(currentOffset + 1), labels);
                    currentOffset += 3;
                    continue;
                }
                default: {
                    throw new IllegalArgumentException();
                }
            }
        }
        int exceptionTableLength = this.readUnsignedShort(currentOffset);
        currentOffset += 2;
        while (exceptionTableLength-- > 0) {
            final Label var41 = this.createLabel(this.readUnsignedShort(currentOffset), labels);
            final Label var42 = this.createLabel(this.readUnsignedShort(currentOffset + 2), labels);
            final Label var43 = this.createLabel(this.readUnsignedShort(currentOffset + 4), labels);
            final String localVariableTableOffset = this.readUTF8(this.cpInfoOffsets[this.readUnsignedShort(currentOffset + 6)], charBuffer);
            currentOffset += 8;
            methodVisitor.visitTryCatchBlock(var41, var42, var43, localVariableTableOffset);
        }
        int stackMapFrameOffset = 0;
        int stackMapTableEndOffset = 0;
        boolean var44 = true;
        int var45 = 0;
        int localVariableTypeTableOffset = 0;
        int[] visibleTypeAnnotationOffsets = null;
        int[] invisibleTypeAnnotationOffsets = null;
        Attribute attributes = null;
        int attributesCount = this.readUnsignedShort(currentOffset);
        currentOffset += 2;
        while (attributesCount-- > 0) {
            final String expandFrames = this.readUTF8(currentOffset, charBuffer);
            final int currentVisibleTypeAnnotationIndex = this.readInt(currentOffset + 2);
            currentOffset += 6;
            if ("LocalVariableTable".equals(expandFrames)) {
                if ((context.parsingOptions & 0x2) == 0x0) {
                    var45 = currentOffset;
                    int currentInvisibleTypeAnnotationIndex = this.readUnsignedShort(currentOffset);
                    int var46 = currentOffset + 2;
                    while (currentInvisibleTypeAnnotationIndex-- > 0) {
                        final int currentInvisibleTypeAnnotationBytecodeOffset = this.readUnsignedShort(var46);
                        this.createDebugLabel(currentInvisibleTypeAnnotationBytecodeOffset, labels);
                        final int insertFrame = this.readUnsignedShort(var46 + 2);
                        this.createDebugLabel(currentInvisibleTypeAnnotationBytecodeOffset + insertFrame, labels);
                        var46 += 10;
                    }
                }
            }
            else if ("LocalVariableTypeTable".equals(expandFrames)) {
                localVariableTypeTableOffset = currentOffset;
            }
            else if ("LineNumberTable".equals(expandFrames)) {
                if ((context.parsingOptions & 0x2) == 0x0) {
                    int currentInvisibleTypeAnnotationIndex = this.readUnsignedShort(currentOffset);
                    int var46 = currentOffset + 2;
                    while (currentInvisibleTypeAnnotationIndex-- > 0) {
                        final int currentInvisibleTypeAnnotationBytecodeOffset = this.readUnsignedShort(var46);
                        final int insertFrame = this.readUnsignedShort(var46 + 2);
                        var46 += 4;
                        this.createDebugLabel(currentInvisibleTypeAnnotationBytecodeOffset, labels);
                        labels[currentInvisibleTypeAnnotationBytecodeOffset].addLineNumber(insertFrame);
                    }
                }
            }
            else if ("RuntimeVisibleTypeAnnotations".equals(expandFrames)) {
                visibleTypeAnnotationOffsets = this.readTypeAnnotations(methodVisitor, context, currentOffset, true);
            }
            else if ("RuntimeInvisibleTypeAnnotations".equals(expandFrames)) {
                invisibleTypeAnnotationOffsets = this.readTypeAnnotations(methodVisitor, context, currentOffset, false);
            }
            else if ("StackMapTable".equals(expandFrames)) {
                if ((context.parsingOptions & 0x4) == 0x0) {
                    stackMapFrameOffset = currentOffset + 2;
                    stackMapTableEndOffset = currentOffset + currentVisibleTypeAnnotationIndex;
                }
            }
            else if ("StackMap".equals(expandFrames)) {
                if ((context.parsingOptions & 0x4) == 0x0) {
                    stackMapFrameOffset = currentOffset + 2;
                    stackMapTableEndOffset = currentOffset + currentVisibleTypeAnnotationIndex;
                    var44 = false;
                }
            }
            else {
                final Attribute currentVisibleTypeAnnotationBytecodeOffset = this.readAttribute(context.attributePrototypes, expandFrames, currentOffset, currentVisibleTypeAnnotationIndex, charBuffer, codeOffset, labels);
                currentVisibleTypeAnnotationBytecodeOffset.nextAttribute = attributes;
                attributes = currentVisibleTypeAnnotationBytecodeOffset;
            }
            currentOffset += currentVisibleTypeAnnotationIndex;
        }
        final boolean var47 = (context.parsingOptions & 0x8) != 0x0;
        if (stackMapFrameOffset != 0) {
            context.currentFrameOffset = -1;
            context.currentFrameType = 0;
            context.currentFrameLocalCount = 0;
            context.currentFrameLocalCountDelta = 0;
            context.currentFrameLocalTypes = new Object[maxLocals];
            context.currentFrameStackCount = 0;
            context.currentFrameStackTypes = new Object[maxStack];
            if (var47) {
                this.computeImplicitFrame(context);
            }
            for (int currentVisibleTypeAnnotationIndex = stackMapFrameOffset; currentVisibleTypeAnnotationIndex < stackMapTableEndOffset - 2; ++currentVisibleTypeAnnotationIndex) {
                if (classFileBuffer[currentVisibleTypeAnnotationIndex] == 8) {
                    final int var46 = this.readUnsignedShort(currentVisibleTypeAnnotationIndex + 1);
                    if (var46 >= 0 && var46 < codeLength && (classFileBuffer[bytecodeStartOffset + var46] & 0xFF) == 0xBB) {
                        this.createLabel(var46, labels);
                    }
                }
            }
        }
        if (var47 && (context.parsingOptions & 0x100) != 0x0) {
            methodVisitor.visitFrame(-1, maxLocals, null, 0, null);
        }
        int currentVisibleTypeAnnotationIndex = 0;
        int var46 = this.getTypeAnnotationBytecodeOffset(visibleTypeAnnotationOffsets, 0);
        int currentInvisibleTypeAnnotationIndex = 0;
        int currentInvisibleTypeAnnotationBytecodeOffset = this.getTypeAnnotationBytecodeOffset(invisibleTypeAnnotationOffsets, 0);
        boolean var48 = false;
        final int wideJumpOpcodeDelta = ((context.parsingOptions & 0x100) == 0x0) ? 33 : 0;
        currentOffset = bytecodeStartOffset;
        while (currentOffset < bytecodeEndOffset) {
            final int nextAttribute = currentOffset - bytecodeStartOffset;
            final Label localVariableTableLength = labels[nextAttribute];
            if (localVariableTableLength != null) {
                localVariableTableLength.accept(methodVisitor, (context.parsingOptions & 0x2) == 0x0);
            }
            while (stackMapFrameOffset != 0 && (context.currentFrameOffset == nextAttribute || context.currentFrameOffset == -1)) {
                if (context.currentFrameOffset != -1) {
                    if (var44 && !var47) {
                        methodVisitor.visitFrame(context.currentFrameType, context.currentFrameLocalCountDelta, context.currentFrameLocalTypes, context.currentFrameStackCount, context.currentFrameStackTypes);
                    }
                    else {
                        methodVisitor.visitFrame(-1, context.currentFrameLocalCount, context.currentFrameLocalTypes, context.currentFrameStackCount, context.currentFrameStackTypes);
                    }
                    var48 = false;
                }
                if (stackMapFrameOffset < stackMapTableEndOffset) {
                    stackMapFrameOffset = this.readStackMapFrame(stackMapFrameOffset, var44, var47, context);
                }
                else {
                    stackMapFrameOffset = 0;
                }
            }
            if (var48) {
                if ((context.parsingOptions & 0x8) != 0x0) {
                    methodVisitor.visitFrame(256, 0, null, 0, null);
                }
                var48 = false;
            }
            int startPc = classFileBuffer[currentOffset] & 0xFF;
            switch (startPc) {
                case 0:
                case 1:
                case 2:
                case 3:
                case 4:
                case 5:
                case 6:
                case 7:
                case 8:
                case 9:
                case 10:
                case 11:
                case 12:
                case 13:
                case 14:
                case 15:
                case 46:
                case 47:
                case 48:
                case 49:
                case 50:
                case 51:
                case 52:
                case 53:
                case 79:
                case 80:
                case 81:
                case 82:
                case 83:
                case 84:
                case 85:
                case 86:
                case 87:
                case 88:
                case 89:
                case 90:
                case 91:
                case 92:
                case 93:
                case 94:
                case 95:
                case 96:
                case 97:
                case 98:
                case 99:
                case 100:
                case 101:
                case 102:
                case 103:
                case 104:
                case 105:
                case 106:
                case 107:
                case 108:
                case 109:
                case 110:
                case 111:
                case 112:
                case 113:
                case 114:
                case 115:
                case 116:
                case 117:
                case 118:
                case 119:
                case 120:
                case 121:
                case 122:
                case 123:
                case 124:
                case 125:
                case 126:
                case 127:
                case 128:
                case 129:
                case 130:
                case 131:
                case 133:
                case 134:
                case 135:
                case 136:
                case 137:
                case 138:
                case 139:
                case 140:
                case 141:
                case 142:
                case 143:
                case 144:
                case 145:
                case 146:
                case 147:
                case 148:
                case 149:
                case 150:
                case 151:
                case 152:
                case 172:
                case 173:
                case 174:
                case 175:
                case 176:
                case 177:
                case 190:
                case 191:
                case 194:
                case 195: {
                    methodVisitor.visitInsn(startPc);
                    ++currentOffset;
                    break;
                }
                case 16:
                case 188: {
                    methodVisitor.visitIntInsn(startPc, classFileBuffer[currentOffset + 1]);
                    currentOffset += 2;
                    break;
                }
                case 17: {
                    methodVisitor.visitIntInsn(startPc, this.readShort(currentOffset + 1));
                    currentOffset += 3;
                    break;
                }
                case 18: {
                    methodVisitor.visitLdcInsn(this.readConst(classFileBuffer[currentOffset + 1] & 0xFF, charBuffer));
                    currentOffset += 2;
                    break;
                }
                case 19:
                case 20: {
                    methodVisitor.visitLdcInsn(this.readConst(this.readUnsignedShort(currentOffset + 1), charBuffer));
                    currentOffset += 3;
                    break;
                }
                case 21:
                case 22:
                case 23:
                case 24:
                case 25:
                case 54:
                case 55:
                case 56:
                case 57:
                case 58:
                case 169: {
                    methodVisitor.visitVarInsn(startPc, classFileBuffer[currentOffset + 1] & 0xFF);
                    currentOffset += 2;
                    break;
                }
                case 26:
                case 27:
                case 28:
                case 29:
                case 30:
                case 31:
                case 32:
                case 33:
                case 34:
                case 35:
                case 36:
                case 37:
                case 38:
                case 39:
                case 40:
                case 41:
                case 42:
                case 43:
                case 44:
                case 45: {
                    startPc -= 26;
                    methodVisitor.visitVarInsn(21 + (startPc >> 2), startPc & 0x3);
                    ++currentOffset;
                    break;
                }
                case 59:
                case 60:
                case 61:
                case 62:
                case 63:
                case 64:
                case 65:
                case 66:
                case 67:
                case 68:
                case 69:
                case 70:
                case 71:
                case 72:
                case 73:
                case 74:
                case 75:
                case 76:
                case 77:
                case 78: {
                    startPc -= 59;
                    methodVisitor.visitVarInsn(54 + (startPc >> 2), startPc & 0x3);
                    ++currentOffset;
                    break;
                }
                case 132: {
                    methodVisitor.visitIincInsn(classFileBuffer[currentOffset + 1] & 0xFF, classFileBuffer[currentOffset + 2]);
                    currentOffset += 3;
                    break;
                }
                case 153:
                case 154:
                case 155:
                case 156:
                case 157:
                case 158:
                case 159:
                case 160:
                case 161:
                case 162:
                case 163:
                case 164:
                case 165:
                case 166:
                case 167:
                case 168:
                case 198:
                case 199: {
                    methodVisitor.visitJumpInsn(startPc, labels[nextAttribute + this.readShort(currentOffset + 1)]);
                    currentOffset += 3;
                    break;
                }
                case 170: {
                    currentOffset += 4 - (nextAttribute & 0x3);
                    final Label typeAnnotationOffset = labels[nextAttribute + this.readInt(currentOffset)];
                    final int var49 = this.readInt(currentOffset + 4);
                    final int var50 = this.readInt(currentOffset + 8);
                    currentOffset += 12;
                    final Label[] var51 = new Label[var50 - var49 + 1];
                    for (int signature = 0; signature < var51.length; ++signature) {
                        var51[signature] = labels[nextAttribute + this.readInt(currentOffset)];
                        currentOffset += 4;
                    }
                    methodVisitor.visitTableSwitchInsn(var49, var50, typeAnnotationOffset, var51);
                    break;
                }
                case 171: {
                    currentOffset += 4 - (nextAttribute & 0x3);
                    final Label typeAnnotationOffset = labels[nextAttribute + this.readInt(currentOffset)];
                    final int var49 = this.readInt(currentOffset + 4);
                    currentOffset += 8;
                    final int[] var52 = new int[var49];
                    final Label[] var51 = new Label[var49];
                    for (int signature = 0; signature < var49; ++signature) {
                        var52[signature] = this.readInt(currentOffset);
                        var51[signature] = labels[nextAttribute + this.readInt(currentOffset + 4)];
                        currentOffset += 8;
                    }
                    methodVisitor.visitLookupSwitchInsn(typeAnnotationOffset, var52, var51);
                    break;
                }
                case 178:
                case 179:
                case 180:
                case 181:
                case 182:
                case 183:
                case 184:
                case 185: {
                    final int var53 = this.cpInfoOffsets[this.readUnsignedShort(currentOffset + 1)];
                    final int var49 = this.cpInfoOffsets[this.readUnsignedShort(var53 + 2)];
                    final String annotationDescriptor = this.readClass(var53, charBuffer);
                    final String index = this.readUTF8(var49, charBuffer);
                    final String var54 = this.readUTF8(var49 + 2, charBuffer);
                    if (startPc < 182) {
                        methodVisitor.visitFieldInsn(startPc, annotationDescriptor, index, var54);
                    }
                    else {
                        final boolean var55 = classFileBuffer[var53 - 1] == 11;
                        methodVisitor.visitMethodInsn(startPc, annotationDescriptor, index, var54, var55);
                    }
                    if (startPc == 185) {
                        currentOffset += 5;
                        break;
                    }
                    currentOffset += 3;
                    break;
                }
                case 186: {
                    final int var53 = this.cpInfoOffsets[this.readUnsignedShort(currentOffset + 1)];
                    final int var49 = this.cpInfoOffsets[this.readUnsignedShort(var53 + 2)];
                    final String annotationDescriptor = this.readUTF8(var49, charBuffer);
                    final String index = this.readUTF8(var49 + 2, charBuffer);
                    int signature = this.bootstrapMethodOffsets[this.readUnsignedShort(var53)];
                    final Handle i = (Handle)this.readConst(this.readUnsignedShort(signature), charBuffer);
                    final Object[] bootstrapMethodArguments = new Object[this.readUnsignedShort(signature + 2)];
                    signature += 4;
                    for (int i2 = 0; i2 < bootstrapMethodArguments.length; ++i2) {
                        bootstrapMethodArguments[i2] = this.readConst(this.readUnsignedShort(signature), charBuffer);
                        signature += 2;
                    }
                    methodVisitor.visitInvokeDynamicInsn(annotationDescriptor, index, i, bootstrapMethodArguments);
                    currentOffset += 5;
                    break;
                }
                case 187:
                case 189:
                case 192:
                case 193: {
                    methodVisitor.visitTypeInsn(startPc, this.readClass(currentOffset + 1, charBuffer));
                    currentOffset += 3;
                    break;
                }
                case 196: {
                    startPc = (classFileBuffer[currentOffset + 1] & 0xFF);
                    if (startPc == 132) {
                        methodVisitor.visitIincInsn(this.readUnsignedShort(currentOffset + 2), this.readShort(currentOffset + 4));
                        currentOffset += 6;
                        break;
                    }
                    methodVisitor.visitVarInsn(startPc, this.readUnsignedShort(currentOffset + 2));
                    currentOffset += 4;
                    break;
                }
                case 197: {
                    methodVisitor.visitMultiANewArrayInsn(this.readClass(currentOffset + 1, charBuffer), classFileBuffer[currentOffset + 3] & 0xFF);
                    currentOffset += 4;
                    break;
                }
                case 200:
                case 201: {
                    methodVisitor.visitJumpInsn(startPc - wideJumpOpcodeDelta, labels[nextAttribute + this.readInt(currentOffset + 1)]);
                    currentOffset += 5;
                    break;
                }
                case 202:
                case 203:
                case 204:
                case 205:
                case 206:
                case 207:
                case 208:
                case 209:
                case 210:
                case 211:
                case 212:
                case 213:
                case 214:
                case 215:
                case 216:
                case 217:
                case 218:
                case 219: {
                    startPc = ((startPc < 218) ? (startPc - 49) : (startPc - 20));
                    final Label typeAnnotationOffset = labels[nextAttribute + this.readUnsignedShort(currentOffset + 1)];
                    if (startPc != 167 && startPc != 168) {
                        startPc = ((startPc < 167) ? ((startPc + 1 ^ 0x1) - 1) : (startPc ^ 0x1));
                        final Label targetType = this.createLabel(nextAttribute + 3, labels);
                        methodVisitor.visitJumpInsn(startPc, targetType);
                        methodVisitor.visitJumpInsn(200, typeAnnotationOffset);
                        var48 = true;
                    }
                    else {
                        methodVisitor.visitJumpInsn(startPc + 33, typeAnnotationOffset);
                    }
                    currentOffset += 3;
                    break;
                }
                case 220: {
                    methodVisitor.visitJumpInsn(200, labels[nextAttribute + this.readInt(currentOffset + 1)]);
                    var48 = true;
                    currentOffset += 5;
                    break;
                }
                default: {
                    throw new AssertionError();
                }
            }
            while (visibleTypeAnnotationOffsets != null && currentVisibleTypeAnnotationIndex < visibleTypeAnnotationOffsets.length) {
                if (var46 > nextAttribute) {
                    break;
                }
                if (var46 == nextAttribute) {
                    int var53 = this.readTypeAnnotationTarget(context, visibleTypeAnnotationOffsets[currentVisibleTypeAnnotationIndex]);
                    final String var56 = this.readUTF8(var53, charBuffer);
                    var53 += 2;
                    this.readElementValues(methodVisitor.visitInsnAnnotation(context.currentTypeAnnotationTarget, context.currentTypeAnnotationTargetPath, var56, true), var53, true, charBuffer);
                }
                ++currentVisibleTypeAnnotationIndex;
                var46 = this.getTypeAnnotationBytecodeOffset(visibleTypeAnnotationOffsets, currentVisibleTypeAnnotationIndex);
            }
            while (invisibleTypeAnnotationOffsets != null && currentInvisibleTypeAnnotationIndex < invisibleTypeAnnotationOffsets.length && currentInvisibleTypeAnnotationBytecodeOffset <= nextAttribute) {
                if (currentInvisibleTypeAnnotationBytecodeOffset == nextAttribute) {
                    int var53 = this.readTypeAnnotationTarget(context, invisibleTypeAnnotationOffsets[currentInvisibleTypeAnnotationIndex]);
                    final String var56 = this.readUTF8(var53, charBuffer);
                    var53 += 2;
                    this.readElementValues(methodVisitor.visitInsnAnnotation(context.currentTypeAnnotationTarget, context.currentTypeAnnotationTargetPath, var56, false), var53, true, charBuffer);
                }
                ++currentInvisibleTypeAnnotationIndex;
                currentInvisibleTypeAnnotationBytecodeOffset = this.getTypeAnnotationBytecodeOffset(invisibleTypeAnnotationOffsets, currentInvisibleTypeAnnotationIndex);
            }
        }
        if (labels[codeLength] != null) {
            methodVisitor.visitLabel(labels[codeLength]);
        }
        if (var45 != 0 && (context.parsingOptions & 0x2) == 0x0) {
            int[] var57 = null;
            if (localVariableTypeTableOffset != 0) {
                var57 = new int[this.readUnsignedShort(localVariableTypeTableOffset) * 3];
                currentOffset = localVariableTypeTableOffset + 2;
                for (int var58 = var57.length; var58 > 0; --var58, var57[var58] = currentOffset + 6, --var58, var57[var58] = this.readUnsignedShort(currentOffset + 8), --var58, var57[var58] = this.readUnsignedShort(currentOffset), currentOffset += 10) {}
            }
            int var58 = this.readUnsignedShort(var45);
            currentOffset = var45 + 2;
            while (var58-- > 0) {
                final int startPc = this.readUnsignedShort(currentOffset);
                final int var53 = this.readUnsignedShort(currentOffset + 2);
                final String var56 = this.readUTF8(currentOffset + 4, charBuffer);
                final String annotationDescriptor = this.readUTF8(currentOffset + 6, charBuffer);
                final int var59 = this.readUnsignedShort(currentOffset + 8);
                currentOffset += 10;
                String var54 = null;
                if (var57 != null) {
                    for (int var60 = 0; var60 < var57.length; var60 += 3) {
                        if (var57[var60] == startPc && var57[var60 + 1] == var59) {
                            var54 = this.readUTF8(var57[var60 + 2], charBuffer);
                            break;
                        }
                    }
                }
                methodVisitor.visitLocalVariable(var56, annotationDescriptor, var54, labels[startPc], labels[startPc + var53], var59);
            }
        }
        if (visibleTypeAnnotationOffsets != null) {
            final int[] var57 = visibleTypeAnnotationOffsets;
            for (int var58 = visibleTypeAnnotationOffsets.length, startPc = 0; startPc < var58; ++startPc) {
                final int var53 = var57[startPc];
                final int var49 = this.readByte(var53);
                if (var49 == 64 || var49 == 65) {
                    currentOffset = this.readTypeAnnotationTarget(context, var53);
                    final String annotationDescriptor = this.readUTF8(currentOffset, charBuffer);
                    currentOffset += 2;
                    this.readElementValues(methodVisitor.visitLocalVariableAnnotation(context.currentTypeAnnotationTarget, context.currentTypeAnnotationTargetPath, context.currentLocalVariableAnnotationRangeStarts, context.currentLocalVariableAnnotationRangeEnds, context.currentLocalVariableAnnotationRangeIndices, annotationDescriptor, true), currentOffset, true, charBuffer);
                }
            }
        }
        if (invisibleTypeAnnotationOffsets != null) {
            final int[] var57 = invisibleTypeAnnotationOffsets;
            for (int var58 = invisibleTypeAnnotationOffsets.length, startPc = 0; startPc < var58; ++startPc) {
                final int var53 = var57[startPc];
                final int var49 = this.readByte(var53);
                if (var49 == 64 || var49 == 65) {
                    currentOffset = this.readTypeAnnotationTarget(context, var53);
                    final String annotationDescriptor = this.readUTF8(currentOffset, charBuffer);
                    currentOffset += 2;
                    this.readElementValues(methodVisitor.visitLocalVariableAnnotation(context.currentTypeAnnotationTarget, context.currentTypeAnnotationTargetPath, context.currentLocalVariableAnnotationRangeStarts, context.currentLocalVariableAnnotationRangeEnds, context.currentLocalVariableAnnotationRangeIndices, annotationDescriptor, false), currentOffset, true, charBuffer);
                }
            }
        }
        while (attributes != null) {
            final Attribute var61 = attributes.nextAttribute;
            attributes.nextAttribute = null;
            methodVisitor.visitAttribute(attributes);
            attributes = var61;
        }
        methodVisitor.visitMaxs(maxStack, maxLocals);
    }
    
    protected Label readLabel(final int bytecodeOffset, final Label[] labels) {
        if (labels[bytecodeOffset] == null) {
            labels[bytecodeOffset] = new Label();
        }
        return labels[bytecodeOffset];
    }
    
    private Label createLabel(final int bytecodeOffset, final Label[] labels) {
        final Label label = this.readLabel(bytecodeOffset, labels);
        label.flags &= 0xFFFFFFFE;
        return label;
    }
    
    private void createDebugLabel(final int bytecodeOffset, final Label[] labels) {
        if (labels[bytecodeOffset] == null) {
            final Label var10000 = this.readLabel(bytecodeOffset, labels);
            var10000.flags |= 0x1;
        }
    }
    
    private int[] readTypeAnnotations(final MethodVisitor methodVisitor, final Context context, final int runtimeTypeAnnotationsOffset, final boolean visible) {
        final char[] charBuffer = context.charBuffer;
        final int[] typeAnnotationsOffsets = new int[this.readUnsignedShort(runtimeTypeAnnotationsOffset)];
        int currentOffset = runtimeTypeAnnotationsOffset + 2;
        for (int i = 0; i < typeAnnotationsOffsets.length; ++i) {
            typeAnnotationsOffsets[i] = currentOffset;
            final int targetType = this.readInt(currentOffset);
            switch (targetType >>> 24) {
                default: {
                    throw new IllegalArgumentException();
                }
                case 16:
                case 17:
                case 18:
                case 23:
                case 66:
                case 67:
                case 68:
                case 69:
                case 70: {
                    currentOffset += 3;
                    break;
                }
                case 64:
                case 65: {
                    int pathLength = this.readUnsignedShort(currentOffset + 1);
                    currentOffset += 3;
                    while (pathLength-- > 0) {
                        final int path = this.readUnsignedShort(currentOffset);
                        final int annotationDescriptor = this.readUnsignedShort(currentOffset + 2);
                        currentOffset += 6;
                        this.createLabel(path, context.currentMethodLabels);
                        this.createLabel(path + annotationDescriptor, context.currentMethodLabels);
                    }
                    break;
                }
                case 71:
                case 72:
                case 73:
                case 74:
                case 75: {
                    currentOffset += 4;
                    break;
                }
            }
            int pathLength = this.readByte(currentOffset);
            if (targetType >>> 24 == 66) {
                final TypePath var13 = (pathLength == 0) ? null : new TypePath(this.b, currentOffset);
                currentOffset += 1 + 2 * pathLength;
                final String var14 = this.readUTF8(currentOffset, charBuffer);
                currentOffset += 2;
                currentOffset = this.readElementValues(methodVisitor.visitTryCatchAnnotation(targetType & 0xFFFFFF00, var13, var14, visible), currentOffset, true, charBuffer);
            }
            else {
                currentOffset += 3 + 2 * pathLength;
                currentOffset = this.readElementValues(null, currentOffset, true, charBuffer);
            }
        }
        return typeAnnotationsOffsets;
    }
    
    private int getTypeAnnotationBytecodeOffset(final int[] typeAnnotationOffsets, final int typeAnnotationIndex) {
        return (typeAnnotationOffsets != null && typeAnnotationIndex < typeAnnotationOffsets.length && this.readByte(typeAnnotationOffsets[typeAnnotationIndex]) >= 67) ? this.readUnsignedShort(typeAnnotationOffsets[typeAnnotationIndex] + 1) : -1;
    }
    
    private int readTypeAnnotationTarget(final Context context, final int typeAnnotationOffset) {
        int targetType = this.readInt(typeAnnotationOffset);
        int currentOffset = 0;
        switch (targetType >>> 24) {
            case 0:
            case 1:
            case 22: {
                targetType &= 0xFFFF0000;
                currentOffset = typeAnnotationOffset + 2;
                break;
            }
            default: {
                throw new IllegalArgumentException();
            }
            case 16:
            case 17:
            case 18:
            case 23:
            case 66: {
                targetType &= 0xFFFFFF00;
                currentOffset = typeAnnotationOffset + 3;
                break;
            }
            case 19:
            case 20:
            case 21: {
                targetType &= 0xFF000000;
                currentOffset = typeAnnotationOffset + 1;
                break;
            }
            case 64:
            case 65: {
                targetType &= 0xFF000000;
                final int pathLength = this.readUnsignedShort(typeAnnotationOffset + 1);
                currentOffset = typeAnnotationOffset + 3;
                context.currentLocalVariableAnnotationRangeStarts = new Label[pathLength];
                context.currentLocalVariableAnnotationRangeEnds = new Label[pathLength];
                context.currentLocalVariableAnnotationRangeIndices = new int[pathLength];
                for (int i = 0; i < pathLength; ++i) {
                    final int startPc = this.readUnsignedShort(currentOffset);
                    final int length = this.readUnsignedShort(currentOffset + 2);
                    final int index = this.readUnsignedShort(currentOffset + 4);
                    currentOffset += 6;
                    context.currentLocalVariableAnnotationRangeStarts[i] = this.createLabel(startPc, context.currentMethodLabels);
                    context.currentLocalVariableAnnotationRangeEnds[i] = this.createLabel(startPc + length, context.currentMethodLabels);
                    context.currentLocalVariableAnnotationRangeIndices[i] = index;
                }
                break;
            }
            case 67:
            case 68:
            case 69:
            case 70: {
                targetType &= 0xFF000000;
                currentOffset = typeAnnotationOffset + 3;
                break;
            }
            case 71:
            case 72:
            case 73:
            case 74:
            case 75: {
                targetType &= 0xFF0000FF;
                currentOffset = typeAnnotationOffset + 4;
                break;
            }
        }
        context.currentTypeAnnotationTarget = targetType;
        final int pathLength = this.readByte(currentOffset);
        context.currentTypeAnnotationTargetPath = ((pathLength == 0) ? null : new TypePath(this.b, currentOffset));
        return currentOffset + 1 + 2 * pathLength;
    }
    
    private void readParameterAnnotations(final MethodVisitor methodVisitor, final Context context, final int runtimeParameterAnnotationsOffset, final boolean visible) {
        int currentOffset = runtimeParameterAnnotationsOffset + 1;
        final int numParameters = this.b[runtimeParameterAnnotationsOffset] & 0xFF;
        methodVisitor.visitAnnotableParameterCount(numParameters, visible);
        final char[] charBuffer = context.charBuffer;
        for (int i = 0; i < numParameters; ++i) {
            int numAnnotations = this.readUnsignedShort(currentOffset);
            currentOffset += 2;
            while (numAnnotations-- > 0) {
                final String annotationDescriptor = this.readUTF8(currentOffset, charBuffer);
                currentOffset += 2;
                currentOffset = this.readElementValues(methodVisitor.visitParameterAnnotation(i, annotationDescriptor, visible), currentOffset, true, charBuffer);
            }
        }
    }
    
    private int readElementValues(final AnnotationVisitor annotationVisitor, final int annotationOffset, final boolean named, final char[] charBuffer) {
        int numElementValuePairs = this.readUnsignedShort(annotationOffset);
        int currentOffset = annotationOffset + 2;
        if (named) {
            while (numElementValuePairs-- > 0) {
                final String elementName = this.readUTF8(currentOffset, charBuffer);
                currentOffset = this.readElementValue(annotationVisitor, currentOffset + 2, elementName, charBuffer);
            }
        }
        else {
            while (numElementValuePairs-- > 0) {
                currentOffset = this.readElementValue(annotationVisitor, currentOffset, null, charBuffer);
            }
        }
        if (annotationVisitor != null) {
            annotationVisitor.visitEnd();
        }
        return currentOffset;
    }
    
    private int readElementValue(final AnnotationVisitor annotationVisitor, final int elementValueOffset, final String elementName, final char[] charBuffer) {
        if (annotationVisitor != null) {
            int currentOffset = elementValueOffset + 1;
            switch (this.b[elementValueOffset] & 0xFF) {
                case 64: {
                    currentOffset = this.readElementValues(annotationVisitor.visitAnnotation(elementName, this.readUTF8(currentOffset, charBuffer)), currentOffset + 2, true, charBuffer);
                    break;
                }
                default: {
                    throw new IllegalArgumentException();
                }
                case 66: {
                    annotationVisitor.visit(elementName, (byte)this.readInt(this.cpInfoOffsets[this.readUnsignedShort(currentOffset)]));
                    currentOffset += 2;
                    break;
                }
                case 67: {
                    annotationVisitor.visit(elementName, (char)this.readInt(this.cpInfoOffsets[this.readUnsignedShort(currentOffset)]));
                    currentOffset += 2;
                    break;
                }
                case 68:
                case 70:
                case 73:
                case 74: {
                    annotationVisitor.visit(elementName, this.readConst(this.readUnsignedShort(currentOffset), charBuffer));
                    currentOffset += 2;
                    break;
                }
                case 83: {
                    annotationVisitor.visit(elementName, (short)this.readInt(this.cpInfoOffsets[this.readUnsignedShort(currentOffset)]));
                    currentOffset += 2;
                    break;
                }
                case 90: {
                    annotationVisitor.visit(elementName, (this.readInt(this.cpInfoOffsets[this.readUnsignedShort(currentOffset)]) == 0) ? Boolean.FALSE : Boolean.TRUE);
                    currentOffset += 2;
                    break;
                }
                case 91: {
                    final int numValues = this.readUnsignedShort(currentOffset);
                    currentOffset += 2;
                    if (numValues == 0) {
                        return this.readElementValues(annotationVisitor.visitArray(elementName), currentOffset - 2, false, charBuffer);
                    }
                    switch (this.b[currentOffset] & 0xFF) {
                        case 66: {
                            final byte[] byteValues = new byte[numValues];
                            for (int var16 = 0; var16 < numValues; ++var16) {
                                byteValues[var16] = (byte)this.readInt(this.cpInfoOffsets[this.readUnsignedShort(currentOffset + 1)]);
                                currentOffset += 3;
                            }
                            annotationVisitor.visit(elementName, byteValues);
                            return currentOffset;
                        }
                        case 67: {
                            final char[] var17 = new char[numValues];
                            for (int var18 = 0; var18 < numValues; ++var18) {
                                var17[var18] = (char)this.readInt(this.cpInfoOffsets[this.readUnsignedShort(currentOffset + 1)]);
                                currentOffset += 3;
                            }
                            annotationVisitor.visit(elementName, var17);
                            return currentOffset;
                        }
                        case 68: {
                            final double[] var19 = new double[numValues];
                            for (int i = 0; i < numValues; ++i) {
                                var19[i] = Double.longBitsToDouble(this.readLong(this.cpInfoOffsets[this.readUnsignedShort(currentOffset + 1)]));
                                currentOffset += 3;
                            }
                            annotationVisitor.visit(elementName, var19);
                            return currentOffset;
                        }
                        default: {
                            currentOffset = this.readElementValues(annotationVisitor.visitArray(elementName), currentOffset - 2, false, charBuffer);
                            return currentOffset;
                        }
                        case 70: {
                            final float[] var20 = new float[numValues];
                            for (int doubleValues = 0; doubleValues < numValues; ++doubleValues) {
                                var20[doubleValues] = Float.intBitsToFloat(this.readInt(this.cpInfoOffsets[this.readUnsignedShort(currentOffset + 1)]));
                                currentOffset += 3;
                            }
                            annotationVisitor.visit(elementName, var20);
                            return currentOffset;
                        }
                        case 73: {
                            final int[] intValues = new int[numValues];
                            for (int var21 = 0; var21 < numValues; ++var21) {
                                intValues[var21] = this.readInt(this.cpInfoOffsets[this.readUnsignedShort(currentOffset + 1)]);
                                currentOffset += 3;
                            }
                            annotationVisitor.visit(elementName, intValues);
                            return currentOffset;
                        }
                        case 74: {
                            final long[] longValues = new long[numValues];
                            for (int floatValues = 0; floatValues < numValues; ++floatValues) {
                                longValues[floatValues] = this.readLong(this.cpInfoOffsets[this.readUnsignedShort(currentOffset + 1)]);
                                currentOffset += 3;
                            }
                            annotationVisitor.visit(elementName, longValues);
                            return currentOffset;
                        }
                        case 83: {
                            final short[] var22 = new short[numValues];
                            for (int charValues = 0; charValues < numValues; ++charValues) {
                                var22[charValues] = (short)this.readInt(this.cpInfoOffsets[this.readUnsignedShort(currentOffset + 1)]);
                                currentOffset += 3;
                            }
                            annotationVisitor.visit(elementName, var22);
                            return currentOffset;
                        }
                        case 90: {
                            final boolean[] booleanValues = new boolean[numValues];
                            for (int shortValues = 0; shortValues < numValues; ++shortValues) {
                                booleanValues[shortValues] = (this.readInt(this.cpInfoOffsets[this.readUnsignedShort(currentOffset + 1)]) != 0);
                                currentOffset += 3;
                            }
                            annotationVisitor.visit(elementName, booleanValues);
                            return currentOffset;
                        }
                    }
                    break;
                }
                case 99: {
                    annotationVisitor.visit(elementName, Type.getType(this.readUTF8(currentOffset, charBuffer)));
                    currentOffset += 2;
                    break;
                }
                case 101: {
                    annotationVisitor.visitEnum(elementName, this.readUTF8(currentOffset, charBuffer), this.readUTF8(currentOffset + 2, charBuffer));
                    currentOffset += 4;
                    break;
                }
                case 115: {
                    annotationVisitor.visit(elementName, this.readUTF8(currentOffset, charBuffer));
                    currentOffset += 2;
                    break;
                }
            }
            return currentOffset;
        }
        switch (this.b[elementValueOffset] & 0xFF) {
            case 64: {
                return this.readElementValues(null, elementValueOffset + 3, true, charBuffer);
            }
            case 91: {
                return this.readElementValues(null, elementValueOffset + 1, false, charBuffer);
            }
            case 101: {
                return elementValueOffset + 5;
            }
            default: {
                return elementValueOffset + 3;
            }
        }
    }
    
    private void computeImplicitFrame(final Context context) {
        final String methodDescriptor = context.currentMethodDescriptor;
        final Object[] locals = context.currentFrameLocalTypes;
        int numLocal = 0;
        if ((context.currentMethodAccessFlags & 0x8) == 0x0) {
            if ("<init>".equals(context.currentMethodName)) {
                locals[numLocal++] = Opcodes.UNINITIALIZED_THIS;
            }
            else {
                locals[numLocal++] = this.readClass(this.header + 2, context.charBuffer);
            }
        }
        int currentMethodDescritorOffset = 1;
        while (true) {
            final int currentArgumentDescriptorStartOffset = currentMethodDescritorOffset;
            switch (methodDescriptor.charAt(currentMethodDescritorOffset++)) {
                case 'B':
                case 'C':
                case 'I':
                case 'S':
                case 'Z': {
                    locals[numLocal++] = Opcodes.INTEGER;
                    continue;
                }
                case 'D': {
                    locals[numLocal++] = Opcodes.DOUBLE;
                    continue;
                }
                default: {
                    context.currentFrameLocalCount = numLocal;
                    return;
                }
                case 'F': {
                    locals[numLocal++] = Opcodes.FLOAT;
                    continue;
                }
                case 'J': {
                    locals[numLocal++] = Opcodes.LONG;
                    continue;
                }
                case 'L': {
                    while (methodDescriptor.charAt(currentMethodDescritorOffset) != ';') {
                        ++currentMethodDescritorOffset;
                    }
                    locals[numLocal++] = methodDescriptor.substring(currentArgumentDescriptorStartOffset + 1, currentMethodDescritorOffset++);
                    continue;
                }
                case '[': {
                    while (methodDescriptor.charAt(currentMethodDescritorOffset) == '[') {
                        ++currentMethodDescritorOffset;
                    }
                    if (methodDescriptor.charAt(currentMethodDescritorOffset) == 'L') {
                        ++currentMethodDescritorOffset;
                        while (methodDescriptor.charAt(currentMethodDescritorOffset) != ';') {
                            ++currentMethodDescritorOffset;
                        }
                    }
                    final int var10001 = numLocal++;
                    ++currentMethodDescritorOffset;
                    locals[var10001] = methodDescriptor.substring(currentArgumentDescriptorStartOffset, currentMethodDescritorOffset);
                    continue;
                }
            }
        }
    }
    
    private int readStackMapFrame(final int stackMapFrameOffset, final boolean compressed, final boolean expand, final Context context) {
        int currentOffset = stackMapFrameOffset;
        final char[] charBuffer = context.charBuffer;
        final Label[] labels = context.currentMethodLabels;
        int frameType;
        if (compressed) {
            currentOffset = stackMapFrameOffset + 1;
            frameType = (this.b[stackMapFrameOffset] & 0xFF);
        }
        else {
            frameType = 255;
            context.currentFrameOffset = -1;
        }
        context.currentFrameLocalCountDelta = 0;
        int offsetDelta;
        if (frameType < 64) {
            offsetDelta = frameType;
            context.currentFrameType = 3;
            context.currentFrameStackCount = 0;
        }
        else if (frameType < 128) {
            offsetDelta = frameType - 64;
            currentOffset = this.readVerificationTypeInfo(currentOffset, context.currentFrameStackTypes, 0, charBuffer, labels);
            context.currentFrameType = 4;
            context.currentFrameStackCount = 1;
        }
        else {
            if (frameType < 247) {
                throw new IllegalArgumentException();
            }
            offsetDelta = this.readUnsignedShort(currentOffset);
            currentOffset += 2;
            if (frameType == 247) {
                currentOffset = this.readVerificationTypeInfo(currentOffset, context.currentFrameStackTypes, 0, charBuffer, labels);
                context.currentFrameType = 4;
                context.currentFrameStackCount = 1;
            }
            else if (frameType >= 248 && frameType < 251) {
                context.currentFrameType = 2;
                context.currentFrameLocalCountDelta = 251 - frameType;
                context.currentFrameLocalCount -= context.currentFrameLocalCountDelta;
                context.currentFrameStackCount = 0;
            }
            else if (frameType == 251) {
                context.currentFrameType = 3;
                context.currentFrameStackCount = 0;
            }
            else if (frameType < 255) {
                int numberOfLocals = expand ? context.currentFrameLocalCount : 0;
                for (int numberOfStackItems = frameType - 251; numberOfStackItems > 0; --numberOfStackItems) {
                    currentOffset = this.readVerificationTypeInfo(currentOffset, context.currentFrameLocalTypes, numberOfLocals++, charBuffer, labels);
                }
                context.currentFrameType = 1;
                context.currentFrameLocalCountDelta = frameType - 251;
                context.currentFrameLocalCount += context.currentFrameLocalCountDelta;
                context.currentFrameStackCount = 0;
            }
            else {
                final int numberOfLocals = this.readUnsignedShort(currentOffset);
                currentOffset += 2;
                context.currentFrameType = 0;
                context.currentFrameLocalCountDelta = numberOfLocals;
                context.currentFrameLocalCount = numberOfLocals;
                for (int numberOfStackItems = 0; numberOfStackItems < numberOfLocals; ++numberOfStackItems) {
                    currentOffset = this.readVerificationTypeInfo(currentOffset, context.currentFrameLocalTypes, numberOfStackItems, charBuffer, labels);
                }
                int numberOfStackItems = this.readUnsignedShort(currentOffset);
                currentOffset += 2;
                context.currentFrameStackCount = numberOfStackItems;
                for (int stack = 0; stack < numberOfStackItems; ++stack) {
                    currentOffset = this.readVerificationTypeInfo(currentOffset, context.currentFrameStackTypes, stack, charBuffer, labels);
                }
            }
        }
        this.createLabel(context.currentFrameOffset += offsetDelta + 1, labels);
        return currentOffset;
    }
    
    private int readVerificationTypeInfo(final int verificationTypeInfoOffset, final Object[] frame, final int index, final char[] charBuffer, final Label[] labels) {
        int currentOffset = verificationTypeInfoOffset + 1;
        final int tag = this.b[verificationTypeInfoOffset] & 0xFF;
        switch (tag) {
            case 0: {
                frame[index] = Opcodes.TOP;
                break;
            }
            case 1: {
                frame[index] = Opcodes.INTEGER;
                break;
            }
            case 2: {
                frame[index] = Opcodes.FLOAT;
                break;
            }
            case 3: {
                frame[index] = Opcodes.DOUBLE;
                break;
            }
            case 4: {
                frame[index] = Opcodes.LONG;
                break;
            }
            case 5: {
                frame[index] = Opcodes.NULL;
                break;
            }
            case 6: {
                frame[index] = Opcodes.UNINITIALIZED_THIS;
                break;
            }
            case 7: {
                frame[index] = this.readClass(currentOffset, charBuffer);
                currentOffset += 2;
                break;
            }
            case 8: {
                frame[index] = this.createLabel(this.readUnsignedShort(currentOffset), labels);
                currentOffset += 2;
                break;
            }
            default: {
                throw new IllegalArgumentException();
            }
        }
        return currentOffset;
    }
    
    final int getFirstAttributeOffset() {
        int currentOffset = this.header + 8 + this.readUnsignedShort(this.header + 6) * 2;
        int fieldsCount = this.readUnsignedShort(currentOffset);
        currentOffset += 2;
        while (fieldsCount-- > 0) {
            int methodsCount = this.readUnsignedShort(currentOffset + 6);
            currentOffset += 8;
            while (methodsCount-- > 0) {
                currentOffset += 6 + this.readInt(currentOffset + 2);
            }
        }
        int methodsCount = this.readUnsignedShort(currentOffset);
        currentOffset += 2;
        while (methodsCount-- > 0) {
            int attributesCount = this.readUnsignedShort(currentOffset + 6);
            currentOffset += 8;
            while (attributesCount-- > 0) {
                currentOffset += 6 + this.readInt(currentOffset + 2);
            }
        }
        return currentOffset + 2;
    }
    
    private int[] readBootstrapMethodsAttribute(final int maxStringLength) {
        final char[] charBuffer = new char[maxStringLength];
        int currentAttributeOffset = this.getFirstAttributeOffset();
        final Object currentBootstrapMethodOffsets = null;
        for (int i = this.readUnsignedShort(currentAttributeOffset - 2); i > 0; --i) {
            final String attributeName = this.readUTF8(currentAttributeOffset, charBuffer);
            final int attributeLength = this.readInt(currentAttributeOffset + 2);
            currentAttributeOffset += 6;
            if ("BootstrapMethods".equals(attributeName)) {
                final int[] var10 = new int[this.readUnsignedShort(currentAttributeOffset)];
                int currentBootstrapMethodOffset = currentAttributeOffset + 2;
                for (int j = 0; j < var10.length; ++j) {
                    var10[j] = currentBootstrapMethodOffset;
                    currentBootstrapMethodOffset += 4 + this.readUnsignedShort(currentBootstrapMethodOffset + 2) * 2;
                }
                return var10;
            }
            currentAttributeOffset += attributeLength;
        }
        return null;
    }
    
    private Attribute readAttribute(final Attribute[] attributePrototypes, final String type, final int offset, final int length, final char[] charBuffer, final int codeAttributeOffset, final Label[] labels) {
        final Attribute[] var8 = attributePrototypes;
        for (int var9 = attributePrototypes.length, var10 = 0; var10 < var9; ++var10) {
            final Attribute attributePrototype = var8[var10];
            if (attributePrototype.type.equals(type)) {
                return attributePrototype.read(this, offset, length, charBuffer, codeAttributeOffset, labels);
            }
        }
        return new Attribute(type).read(this, offset, length, null, -1, null);
    }
    
    public int getItemCount() {
        return this.cpInfoOffsets.length;
    }
    
    public int getItem(final int constantPoolEntryIndex) {
        return this.cpInfoOffsets[constantPoolEntryIndex];
    }
    
    public int getMaxStringLength() {
        return this.maxStringLength;
    }
    
    public int readByte(final int offset) {
        return this.b[offset] & 0xFF;
    }
    
    public int readUnsignedShort(final int offset) {
        final byte[] classFileBuffer = this.b;
        return (classFileBuffer[offset] & 0xFF) << 8 | (classFileBuffer[offset + 1] & 0xFF);
    }
    
    public short readShort(final int offset) {
        final byte[] classFileBuffer = this.b;
        return (short)((classFileBuffer[offset] & 0xFF) << 8 | (classFileBuffer[offset + 1] & 0xFF));
    }
    
    public int readInt(final int offset) {
        final byte[] classFileBuffer = this.b;
        return (classFileBuffer[offset] & 0xFF) << 24 | (classFileBuffer[offset + 1] & 0xFF) << 16 | (classFileBuffer[offset + 2] & 0xFF) << 8 | (classFileBuffer[offset + 3] & 0xFF);
    }
    
    public long readLong(final int offset) {
        final long l1 = this.readInt(offset);
        final long l2 = (long)this.readInt(offset + 4) & 0xFFFFFFFFL;
        return l1 << 32 | l2;
    }
    
    public String readUTF8(final int offset, final char[] charBuffer) {
        final int constantPoolEntryIndex = this.readUnsignedShort(offset);
        return (offset != 0 && constantPoolEntryIndex != 0) ? this.readUtf(constantPoolEntryIndex, charBuffer) : null;
    }
    
    final String readUtf(final int constantPoolEntryIndex, final char[] charBuffer) {
        final String value = this.constantUtf8Values[constantPoolEntryIndex];
        if (value != null) {
            return value;
        }
        final int cpInfoOffset = this.cpInfoOffsets[constantPoolEntryIndex];
        return this.constantUtf8Values[constantPoolEntryIndex] = this.readUtf(cpInfoOffset + 2, this.readUnsignedShort(cpInfoOffset), charBuffer);
    }
    
    private String readUtf(final int utfOffset, final int utfLength, final char[] charBuffer) {
        int currentOffset = utfOffset;
        final int endOffset = utfOffset + utfLength;
        int strLength = 0;
        final byte[] classFileBuffer = this.b;
        while (currentOffset < endOffset) {
            final byte currentByte = classFileBuffer[currentOffset++];
            if ((currentByte & 0x80) == 0x0) {
                charBuffer[strLength++] = (char)(currentByte & 0x7F);
            }
            else if ((currentByte & 0xE0) == 0xC0) {
                charBuffer[strLength++] = (char)(((currentByte & 0x1F) << 6) + (classFileBuffer[currentOffset++] & 0x3F));
            }
            else {
                charBuffer[strLength++] = (char)(((currentByte & 0xF) << 12) + ((classFileBuffer[currentOffset++] & 0x3F) << 6) + (classFileBuffer[currentOffset++] & 0x3F));
            }
        }
        return new String(charBuffer, 0, strLength);
    }
    
    private String readStringish(final int offset, final char[] charBuffer) {
        return this.readUTF8(this.cpInfoOffsets[this.readUnsignedShort(offset)], charBuffer);
    }
    
    public String readClass(final int offset, final char[] charBuffer) {
        return this.readStringish(offset, charBuffer);
    }
    
    public String readModule(final int offset, final char[] charBuffer) {
        return this.readStringish(offset, charBuffer);
    }
    
    public String readPackage(final int offset, final char[] charBuffer) {
        return this.readStringish(offset, charBuffer);
    }
    
    private ConstantDynamic readConstantDynamic(final int constantPoolEntryIndex, final char[] charBuffer) {
        final ConstantDynamic constantDynamic = this.constantDynamicValues[constantPoolEntryIndex];
        if (constantDynamic != null) {
            return constantDynamic;
        }
        final int cpInfoOffset = this.cpInfoOffsets[constantPoolEntryIndex];
        final int nameAndTypeCpInfoOffset = this.cpInfoOffsets[this.readUnsignedShort(cpInfoOffset + 2)];
        final String name = this.readUTF8(nameAndTypeCpInfoOffset, charBuffer);
        final String descriptor = this.readUTF8(nameAndTypeCpInfoOffset + 2, charBuffer);
        int bootstrapMethodOffset = this.bootstrapMethodOffsets[this.readUnsignedShort(cpInfoOffset)];
        final Handle handle = (Handle)this.readConst(this.readUnsignedShort(bootstrapMethodOffset), charBuffer);
        final Object[] bootstrapMethodArguments = new Object[this.readUnsignedShort(bootstrapMethodOffset + 2)];
        bootstrapMethodOffset += 4;
        for (int i = 0; i < bootstrapMethodArguments.length; ++i) {
            bootstrapMethodArguments[i] = this.readConst(this.readUnsignedShort(bootstrapMethodOffset), charBuffer);
            bootstrapMethodOffset += 2;
        }
        return this.constantDynamicValues[constantPoolEntryIndex] = new ConstantDynamic(name, descriptor, handle, bootstrapMethodArguments);
    }
    
    public Object readConst(final int constantPoolEntryIndex, final char[] charBuffer) {
        final int cpInfoOffset = this.cpInfoOffsets[constantPoolEntryIndex];
        switch (this.b[cpInfoOffset - 1]) {
            case 3: {
                return this.readInt(cpInfoOffset);
            }
            case 4: {
                return Float.intBitsToFloat(this.readInt(cpInfoOffset));
            }
            case 5: {
                return this.readLong(cpInfoOffset);
            }
            case 6: {
                return Double.longBitsToDouble(this.readLong(cpInfoOffset));
            }
            case 7: {
                return Type.getObjectType(this.readUTF8(cpInfoOffset, charBuffer));
            }
            case 8: {
                return this.readUTF8(cpInfoOffset, charBuffer);
            }
            default: {
                throw new IllegalArgumentException();
            }
            case 15: {
                final int referenceKind = this.readByte(cpInfoOffset);
                final int referenceCpInfoOffset = this.cpInfoOffsets[this.readUnsignedShort(cpInfoOffset + 1)];
                final int nameAndTypeCpInfoOffset = this.cpInfoOffsets[this.readUnsignedShort(referenceCpInfoOffset + 2)];
                final String owner = this.readClass(referenceCpInfoOffset, charBuffer);
                final String name = this.readUTF8(nameAndTypeCpInfoOffset, charBuffer);
                final String descriptor = this.readUTF8(nameAndTypeCpInfoOffset + 2, charBuffer);
                final boolean isInterface = this.b[referenceCpInfoOffset - 1] == 11;
                return new Handle(referenceKind, owner, name, descriptor, isInterface);
            }
            case 16: {
                return Type.getMethodType(this.readUTF8(cpInfoOffset, charBuffer));
            }
            case 17: {
                return this.readConstantDynamic(constantPoolEntryIndex, charBuffer);
            }
        }
    }
}
