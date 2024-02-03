// 
// Decompiled by Procyon v0.6.0
// 

package org.objectweb.asm;

final class MethodWriter extends MethodVisitor
{
    static final int COMPUTE_NOTHING = 0;
    static final int COMPUTE_MAX_STACK_AND_LOCAL = 1;
    static final int COMPUTE_MAX_STACK_AND_LOCAL_FROM_FRAMES = 2;
    static final int COMPUTE_INSERTED_FRAMES = 3;
    static final int COMPUTE_ALL_FRAMES = 4;
    private static final int NA = 0;
    private static final int[] STACK_SIZE_DELTA;
    private final SymbolTable symbolTable;
    private final int accessFlags;
    private final int nameIndex;
    private final String name;
    private final int descriptorIndex;
    private final String descriptor;
    private final ByteVector code;
    private final int numberOfExceptions;
    private final int[] exceptionIndexTable;
    private final int signatureIndex;
    private final int compute;
    private int maxStack;
    private int maxLocals;
    private Handler firstHandler;
    private Handler lastHandler;
    private int lineNumberTableLength;
    private ByteVector lineNumberTable;
    private int localVariableTableLength;
    private ByteVector localVariableTable;
    private int localVariableTypeTableLength;
    private ByteVector localVariableTypeTable;
    private int stackMapTableNumberOfEntries;
    private ByteVector stackMapTableEntries;
    private AnnotationWriter lastCodeRuntimeVisibleTypeAnnotation;
    private AnnotationWriter lastCodeRuntimeInvisibleTypeAnnotation;
    private Attribute firstCodeAttribute;
    private AnnotationWriter lastRuntimeVisibleAnnotation;
    private AnnotationWriter lastRuntimeInvisibleAnnotation;
    private int visibleAnnotableParameterCount;
    private AnnotationWriter[] lastRuntimeVisibleParameterAnnotations;
    private int invisibleAnnotableParameterCount;
    private AnnotationWriter[] lastRuntimeInvisibleParameterAnnotations;
    private AnnotationWriter lastRuntimeVisibleTypeAnnotation;
    private AnnotationWriter lastRuntimeInvisibleTypeAnnotation;
    private ByteVector defaultValue;
    private int parametersCount;
    private ByteVector parameters;
    private Attribute firstAttribute;
    private Label firstBasicBlock;
    private Label lastBasicBlock;
    private Label currentBasicBlock;
    private int relativeStackSize;
    private int maxRelativeStackSize;
    private int currentLocals;
    private int previousFrameOffset;
    private int[] previousFrame;
    private int[] currentFrame;
    private boolean hasSubroutines;
    private boolean hasAsmInstructions;
    private int lastBytecodeOffset;
    private int sourceOffset;
    private int sourceLength;
    
    static {
        STACK_SIZE_DELTA = new int[] { 0, 1, 1, 1, 1, 1, 1, 1, 1, 2, 2, 1, 1, 1, 2, 2, 1, 1, 1, 0, 0, 1, 2, 1, 2, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, -1, 0, -1, 0, -1, -1, -1, -1, -1, -2, -1, -2, -1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, -3, -4, -3, -4, -3, -3, -3, -3, -1, -2, 1, 1, 1, 2, 2, 2, 0, -1, -2, -1, -2, -1, -2, -1, -2, -1, -2, -1, -2, -1, -2, -1, -2, -1, -2, -1, -2, 0, 0, 0, 0, -1, -1, -1, -1, -1, -1, -1, -2, -1, -2, -1, -2, 0, 1, 0, 1, -1, -1, 0, 0, 1, 1, -1, 0, -1, 0, 0, 0, -3, -1, -1, -3, -3, -1, -1, -1, -1, -1, -1, -2, -2, -2, -2, -2, -2, -2, -2, 0, 1, 0, -1, -1, -1, -2, -1, -2, -1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, -1, -1, 0, 0, -1, -1, 0, 0 };
    }
    
    MethodWriter(final SymbolTable symbolTable, final int access, final String name, final String descriptor, final String signature, final String[] exceptions, final int compute) {
        super(458752);
        this.code = new ByteVector();
        this.symbolTable = symbolTable;
        this.accessFlags = ("<init>".equals(name) ? (access | 0x40000) : access);
        this.nameIndex = symbolTable.addConstantUtf8(name);
        this.name = name;
        this.descriptorIndex = symbolTable.addConstantUtf8(descriptor);
        this.descriptor = descriptor;
        this.signatureIndex = ((signature == null) ? 0 : symbolTable.addConstantUtf8(signature));
        if (exceptions != null && exceptions.length > 0) {
            this.numberOfExceptions = exceptions.length;
            this.exceptionIndexTable = new int[this.numberOfExceptions];
            for (int argumentsSize = 0; argumentsSize < this.numberOfExceptions; ++argumentsSize) {
                this.exceptionIndexTable[argumentsSize] = symbolTable.addConstantClass(exceptions[argumentsSize]).index;
            }
        }
        else {
            this.numberOfExceptions = 0;
            this.exceptionIndexTable = null;
        }
        this.compute = compute;
        if (compute != 0) {
            int argumentsSize = Type.getArgumentsAndReturnSizes(descriptor) >> 2;
            if ((access & 0x8) != 0x0) {
                --argumentsSize;
            }
            this.maxLocals = argumentsSize;
            this.currentLocals = argumentsSize;
            this.visitLabel(this.firstBasicBlock = new Label());
        }
    }
    
    boolean hasFrames() {
        return this.stackMapTableNumberOfEntries > 0;
    }
    
    boolean hasAsmInstructions() {
        return this.hasAsmInstructions;
    }
    
    @Override
    public void visitParameter(final String name, final int access) {
        if (this.parameters == null) {
            this.parameters = new ByteVector();
        }
        ++this.parametersCount;
        this.parameters.putShort((name == null) ? 0 : this.symbolTable.addConstantUtf8(name)).putShort(access);
    }
    
    @Override
    public AnnotationVisitor visitAnnotationDefault() {
        this.defaultValue = new ByteVector();
        return new AnnotationWriter(this.symbolTable, false, this.defaultValue, null);
    }
    
    @Override
    public AnnotationVisitor visitAnnotation(final String descriptor, final boolean visible) {
        final ByteVector annotation = new ByteVector();
        annotation.putShort(this.symbolTable.addConstantUtf8(descriptor)).putShort(0);
        return visible ? (this.lastRuntimeVisibleAnnotation = new AnnotationWriter(this.symbolTable, annotation, this.lastRuntimeVisibleAnnotation)) : (this.lastRuntimeInvisibleAnnotation = new AnnotationWriter(this.symbolTable, annotation, this.lastRuntimeInvisibleAnnotation));
    }
    
    @Override
    public AnnotationVisitor visitTypeAnnotation(final int typeRef, final TypePath typePath, final String descriptor, final boolean visible) {
        final ByteVector typeAnnotation = new ByteVector();
        TypeReference.putTarget(typeRef, typeAnnotation);
        TypePath.put(typePath, typeAnnotation);
        typeAnnotation.putShort(this.symbolTable.addConstantUtf8(descriptor)).putShort(0);
        return visible ? (this.lastRuntimeVisibleTypeAnnotation = new AnnotationWriter(this.symbolTable, typeAnnotation, this.lastRuntimeVisibleTypeAnnotation)) : (this.lastRuntimeInvisibleTypeAnnotation = new AnnotationWriter(this.symbolTable, typeAnnotation, this.lastRuntimeInvisibleTypeAnnotation));
    }
    
    @Override
    public void visitAnnotableParameterCount(final int parameterCount, final boolean visible) {
        if (visible) {
            this.visibleAnnotableParameterCount = parameterCount;
        }
        else {
            this.invisibleAnnotableParameterCount = parameterCount;
        }
    }
    
    @Override
    public AnnotationVisitor visitParameterAnnotation(final int parameter, final String annotationDescriptor, final boolean visible) {
        final ByteVector annotation = new ByteVector();
        annotation.putShort(this.symbolTable.addConstantUtf8(annotationDescriptor)).putShort(0);
        if (visible) {
            if (this.lastRuntimeVisibleParameterAnnotations == null) {
                this.lastRuntimeVisibleParameterAnnotations = new AnnotationWriter[Type.getArgumentTypes(this.descriptor).length];
            }
            return this.lastRuntimeVisibleParameterAnnotations[parameter] = new AnnotationWriter(this.symbolTable, annotation, this.lastRuntimeVisibleParameterAnnotations[parameter]);
        }
        if (this.lastRuntimeInvisibleParameterAnnotations == null) {
            this.lastRuntimeInvisibleParameterAnnotations = new AnnotationWriter[Type.getArgumentTypes(this.descriptor).length];
        }
        return this.lastRuntimeInvisibleParameterAnnotations[parameter] = new AnnotationWriter(this.symbolTable, annotation, this.lastRuntimeInvisibleParameterAnnotations[parameter]);
    }
    
    @Override
    public void visitAttribute(final Attribute attribute) {
        if (attribute.isCodeAttribute()) {
            attribute.nextAttribute = this.firstCodeAttribute;
            this.firstCodeAttribute = attribute;
        }
        else {
            attribute.nextAttribute = this.firstAttribute;
            this.firstAttribute = attribute;
        }
    }
    
    @Override
    public void visitCode() {
    }
    
    @Override
    public void visitFrame(final int type, final int numLocal, final Object[] local, final int numStack, final Object[] stack) {
        if (this.compute != 4) {
            if (this.compute == 3) {
                if (this.currentBasicBlock.frame == null) {
                    (this.currentBasicBlock.frame = new CurrentFrame(this.currentBasicBlock)).setInputFrameFromDescriptor(this.symbolTable, this.accessFlags, this.descriptor, numLocal);
                    this.currentBasicBlock.frame.accept(this);
                }
                else {
                    if (type == -1) {
                        this.currentBasicBlock.frame.setInputFrameFromApiFormat(this.symbolTable, numLocal, local, numStack, stack);
                    }
                    this.currentBasicBlock.frame.accept(this);
                }
            }
            else if (type == -1) {
                if (this.previousFrame == null) {
                    final int i = Type.getArgumentsAndReturnSizes(this.descriptor) >> 2;
                    final Frame var8 = new Frame(new Label());
                    var8.setInputFrameFromDescriptor(this.symbolTable, this.accessFlags, this.descriptor, i);
                    var8.accept(this);
                }
                this.currentLocals = numLocal;
                int i = this.visitFrameStart(this.code.length, numLocal, numStack);
                for (int i2 = 0; i2 < numLocal; ++i2) {
                    this.currentFrame[i++] = Frame.getAbstractTypeFromApiFormat(this.symbolTable, local[i2]);
                }
                for (int i2 = 0; i2 < numStack; ++i2) {
                    this.currentFrame[i++] = Frame.getAbstractTypeFromApiFormat(this.symbolTable, stack[i2]);
                }
                this.visitFrameEnd();
            }
            else {
                int i;
                if (this.stackMapTableEntries == null) {
                    this.stackMapTableEntries = new ByteVector();
                    i = this.code.length;
                }
                else {
                    i = this.code.length - this.previousFrameOffset - 1;
                    if (i < 0) {
                        if (type == 3) {
                            return;
                        }
                        throw new IllegalStateException();
                    }
                }
                switch (type) {
                    case 0: {
                        this.currentLocals = numLocal;
                        this.stackMapTableEntries.putByte(255).putShort(i).putShort(numLocal);
                        for (int i2 = 0; i2 < numLocal; ++i2) {
                            this.putFrameType(local[i2]);
                        }
                        this.stackMapTableEntries.putShort(numStack);
                        for (int i2 = 0; i2 < numStack; ++i2) {
                            this.putFrameType(stack[i2]);
                        }
                        break;
                    }
                    case 1: {
                        this.currentLocals += numLocal;
                        this.stackMapTableEntries.putByte(251 + numLocal).putShort(i);
                        for (int i2 = 0; i2 < numLocal; ++i2) {
                            this.putFrameType(local[i2]);
                        }
                        break;
                    }
                    case 2: {
                        this.currentLocals -= numLocal;
                        this.stackMapTableEntries.putByte(251 - numLocal).putShort(i);
                        break;
                    }
                    case 3: {
                        if (i < 64) {
                            this.stackMapTableEntries.putByte(i);
                            break;
                        }
                        this.stackMapTableEntries.putByte(251).putShort(i);
                        break;
                    }
                    case 4: {
                        if (i < 64) {
                            this.stackMapTableEntries.putByte(64 + i);
                        }
                        else {
                            this.stackMapTableEntries.putByte(247).putShort(i);
                        }
                        this.putFrameType(stack[0]);
                        break;
                    }
                    default: {
                        throw new IllegalArgumentException();
                    }
                }
                this.previousFrameOffset = this.code.length;
                ++this.stackMapTableNumberOfEntries;
            }
            if (this.compute == 2) {
                this.relativeStackSize = numStack;
                for (int i = 0; i < numStack; ++i) {
                    if (stack[i] == Opcodes.LONG || stack[i] == Opcodes.DOUBLE) {
                        ++this.relativeStackSize;
                    }
                }
                if (this.relativeStackSize > this.maxRelativeStackSize) {
                    this.maxRelativeStackSize = this.relativeStackSize;
                }
            }
            this.maxStack = Math.max(this.maxStack, numStack);
            this.maxLocals = Math.max(this.maxLocals, this.currentLocals);
        }
    }
    
    @Override
    public void visitInsn(final int opcode) {
        this.lastBytecodeOffset = this.code.length;
        this.code.putByte(opcode);
        if (this.currentBasicBlock != null) {
            if (this.compute != 4 && this.compute != 3) {
                final int size = this.relativeStackSize + MethodWriter.STACK_SIZE_DELTA[opcode];
                if (size > this.maxRelativeStackSize) {
                    this.maxRelativeStackSize = size;
                }
                this.relativeStackSize = size;
            }
            else {
                this.currentBasicBlock.frame.execute(opcode, 0, null, null);
            }
            if ((opcode >= 172 && opcode <= 177) || opcode == 191) {
                this.endCurrentBasicBlockWithNoSuccessor();
            }
        }
    }
    
    @Override
    public void visitIntInsn(final int opcode, final int operand) {
        this.lastBytecodeOffset = this.code.length;
        if (opcode == 17) {
            this.code.put12(opcode, operand);
        }
        else {
            this.code.put11(opcode, operand);
        }
        if (this.currentBasicBlock != null) {
            if (this.compute != 4 && this.compute != 3) {
                if (opcode != 188) {
                    final int size = this.relativeStackSize + 1;
                    if (size > this.maxRelativeStackSize) {
                        this.maxRelativeStackSize = size;
                    }
                    this.relativeStackSize = size;
                }
            }
            else {
                this.currentBasicBlock.frame.execute(opcode, operand, null, null);
            }
        }
    }
    
    @Override
    public void visitVarInsn(final int opcode, final int var) {
        this.lastBytecodeOffset = this.code.length;
        if (var < 4 && opcode != 169) {
            int currentMaxLocals;
            if (opcode < 54) {
                currentMaxLocals = 26 + (opcode - 21 << 2) + var;
            }
            else {
                currentMaxLocals = 59 + (opcode - 54 << 2) + var;
            }
            this.code.putByte(currentMaxLocals);
        }
        else if (var >= 256) {
            this.code.putByte(196).put12(opcode, var);
        }
        else {
            this.code.put11(opcode, var);
        }
        if (this.currentBasicBlock != null) {
            if (this.compute != 4 && this.compute != 3) {
                if (opcode == 169) {
                    this.currentBasicBlock.flags |= 0x40;
                    this.currentBasicBlock.outputStackSize = (short)this.relativeStackSize;
                    this.endCurrentBasicBlockWithNoSuccessor();
                }
                else {
                    final int currentMaxLocals = this.relativeStackSize + MethodWriter.STACK_SIZE_DELTA[opcode];
                    if (currentMaxLocals > this.maxRelativeStackSize) {
                        this.maxRelativeStackSize = currentMaxLocals;
                    }
                    this.relativeStackSize = currentMaxLocals;
                }
            }
            else {
                this.currentBasicBlock.frame.execute(opcode, var, null, null);
            }
        }
        if (this.compute != 0) {
            int currentMaxLocals;
            if (opcode != 22 && opcode != 24 && opcode != 55 && opcode != 57) {
                currentMaxLocals = var + 1;
            }
            else {
                currentMaxLocals = var + 2;
            }
            if (currentMaxLocals > this.maxLocals) {
                this.maxLocals = currentMaxLocals;
            }
        }
        if (opcode >= 54 && this.compute == 4 && this.firstHandler != null) {
            this.visitLabel(new Label());
        }
    }
    
    @Override
    public void visitTypeInsn(final int opcode, final String type) {
        this.lastBytecodeOffset = this.code.length;
        final Symbol typeSymbol = this.symbolTable.addConstantClass(type);
        this.code.put12(opcode, typeSymbol.index);
        if (this.currentBasicBlock != null) {
            if (this.compute != 4 && this.compute != 3) {
                if (opcode == 187) {
                    final int size = this.relativeStackSize + 1;
                    if (size > this.maxRelativeStackSize) {
                        this.maxRelativeStackSize = size;
                    }
                    this.relativeStackSize = size;
                }
            }
            else {
                this.currentBasicBlock.frame.execute(opcode, this.lastBytecodeOffset, typeSymbol, this.symbolTable);
            }
        }
    }
    
    @Override
    public void visitFieldInsn(final int opcode, final String owner, final String name, final String descriptor) {
        this.lastBytecodeOffset = this.code.length;
        final Symbol fieldrefSymbol = this.symbolTable.addConstantFieldref(owner, name, descriptor);
        this.code.put12(opcode, fieldrefSymbol.index);
        if (this.currentBasicBlock != null) {
            if (this.compute != 4 && this.compute != 3) {
                final char firstDescChar = descriptor.charAt(0);
                int size = 0;
                switch (opcode) {
                    case 178: {
                        size = this.relativeStackSize + ((firstDescChar != 'D' && firstDescChar != 'J') ? 1 : 2);
                        break;
                    }
                    case 179: {
                        size = this.relativeStackSize + ((firstDescChar != 'D' && firstDescChar != 'J') ? -1 : -2);
                        break;
                    }
                    case 180: {
                        size = this.relativeStackSize + ((firstDescChar == 'D' || firstDescChar == 'J') ? 1 : 0);
                        break;
                    }
                    default: {
                        size = this.relativeStackSize + ((firstDescChar != 'D' && firstDescChar != 'J') ? -2 : -3);
                        break;
                    }
                }
                if (size > this.maxRelativeStackSize) {
                    this.maxRelativeStackSize = size;
                }
                this.relativeStackSize = size;
            }
            else {
                this.currentBasicBlock.frame.execute(opcode, 0, fieldrefSymbol, this.symbolTable);
            }
        }
    }
    
    @Override
    public void visitMethodInsn(final int opcode, final String owner, final String name, final String descriptor, final boolean isInterface) {
        this.lastBytecodeOffset = this.code.length;
        final Symbol methodrefSymbol = this.symbolTable.addConstantMethodref(owner, name, descriptor, isInterface);
        if (opcode == 185) {
            this.code.put12(185, methodrefSymbol.index).put11(methodrefSymbol.getArgumentsAndReturnSizes() >> 2, 0);
        }
        else {
            this.code.put12(opcode, methodrefSymbol.index);
        }
        if (this.currentBasicBlock != null) {
            if (this.compute != 4 && this.compute != 3) {
                final int argumentsAndReturnSize = methodrefSymbol.getArgumentsAndReturnSizes();
                final int stackSizeDelta = (argumentsAndReturnSize & 0x3) - (argumentsAndReturnSize >> 2);
                int size;
                if (opcode == 184) {
                    size = this.relativeStackSize + stackSizeDelta + 1;
                }
                else {
                    size = this.relativeStackSize + stackSizeDelta;
                }
                if (size > this.maxRelativeStackSize) {
                    this.maxRelativeStackSize = size;
                }
                this.relativeStackSize = size;
            }
            else {
                this.currentBasicBlock.frame.execute(opcode, 0, methodrefSymbol, this.symbolTable);
            }
        }
    }
    
    @Override
    public void visitInvokeDynamicInsn(final String name, final String descriptor, final Handle bootstrapMethodHandle, final Object... bootstrapMethodArguments) {
        this.lastBytecodeOffset = this.code.length;
        final Symbol invokeDynamicSymbol = this.symbolTable.addConstantInvokeDynamic(name, descriptor, bootstrapMethodHandle, bootstrapMethodArguments);
        this.code.put12(186, invokeDynamicSymbol.index);
        this.code.putShort(0);
        if (this.currentBasicBlock != null) {
            if (this.compute != 4 && this.compute != 3) {
                final int argumentsAndReturnSize = invokeDynamicSymbol.getArgumentsAndReturnSizes();
                final int stackSizeDelta = (argumentsAndReturnSize & 0x3) - (argumentsAndReturnSize >> 2) + 1;
                final int size = this.relativeStackSize + stackSizeDelta;
                if (size > this.maxRelativeStackSize) {
                    this.maxRelativeStackSize = size;
                }
                this.relativeStackSize = size;
            }
            else {
                this.currentBasicBlock.frame.execute(186, 0, invokeDynamicSymbol, this.symbolTable);
            }
        }
    }
    
    @Override
    public void visitJumpInsn(final int opcode, final Label label) {
        this.lastBytecodeOffset = this.code.length;
        final int baseOpcode = (opcode >= 200) ? (opcode - 33) : opcode;
        boolean nextInsnIsJumpTarget = false;
        if ((label.flags & 0x4) != 0x0 && label.bytecodeOffset - this.code.length < -32768) {
            if (baseOpcode == 167) {
                this.code.putByte(200);
            }
            else if (baseOpcode == 168) {
                this.code.putByte(201);
            }
            else {
                this.code.putByte((baseOpcode >= 198) ? (baseOpcode ^ 0x1) : ((baseOpcode + 1 ^ 0x1) - 1));
                this.code.putShort(8);
                this.code.putByte(220);
                this.hasAsmInstructions = true;
                nextInsnIsJumpTarget = true;
            }
            label.put(this.code, this.code.length - 1, true);
        }
        else if (baseOpcode != opcode) {
            this.code.putByte(opcode);
            label.put(this.code, this.code.length - 1, true);
        }
        else {
            this.code.putByte(baseOpcode);
            label.put(this.code, this.code.length - 1, false);
        }
        if (this.currentBasicBlock != null) {
            Label nextBasicBlock = null;
            if (this.compute == 4) {
                this.currentBasicBlock.frame.execute(baseOpcode, 0, null, null);
                final Label var10000 = label.getCanonicalInstance();
                var10000.flags |= 0x2;
                this.addSuccessorToCurrentBasicBlock(0, label);
                if (baseOpcode != 167) {
                    nextBasicBlock = new Label();
                }
            }
            else if (this.compute == 3) {
                this.currentBasicBlock.frame.execute(baseOpcode, 0, null, null);
            }
            else if (this.compute == 2) {
                this.relativeStackSize += MethodWriter.STACK_SIZE_DELTA[baseOpcode];
            }
            else if (baseOpcode == 168) {
                if ((label.flags & 0x20) == 0x0) {
                    label.flags |= 0x20;
                    this.hasSubroutines = true;
                }
                this.currentBasicBlock.flags |= 0x10;
                this.addSuccessorToCurrentBasicBlock(this.relativeStackSize + 1, label);
                nextBasicBlock = new Label();
            }
            else {
                this.addSuccessorToCurrentBasicBlock(this.relativeStackSize += MethodWriter.STACK_SIZE_DELTA[baseOpcode], label);
            }
            if (nextBasicBlock != null) {
                if (nextInsnIsJumpTarget) {
                    nextBasicBlock.flags |= 0x2;
                }
                this.visitLabel(nextBasicBlock);
            }
            if (baseOpcode == 167) {
                this.endCurrentBasicBlockWithNoSuccessor();
            }
        }
    }
    
    @Override
    public void visitLabel(final Label label) {
        this.hasAsmInstructions |= label.resolve(this.code.data, this.code.length);
        if ((label.flags & 0x1) == 0x0) {
            if (this.compute == 4) {
                if (this.currentBasicBlock != null) {
                    if (label.bytecodeOffset == this.currentBasicBlock.bytecodeOffset) {
                        this.currentBasicBlock.flags |= (short)(label.flags & 0x2);
                        label.frame = this.currentBasicBlock.frame;
                        return;
                    }
                    this.addSuccessorToCurrentBasicBlock(0, label);
                }
                if (this.lastBasicBlock != null) {
                    if (label.bytecodeOffset == this.lastBasicBlock.bytecodeOffset) {
                        this.lastBasicBlock.flags |= (short)(label.flags & 0x2);
                        label.frame = this.lastBasicBlock.frame;
                        this.currentBasicBlock = this.lastBasicBlock;
                        return;
                    }
                    this.lastBasicBlock.nextBasicBlock = label;
                }
                this.lastBasicBlock = label;
                this.currentBasicBlock = label;
                label.frame = new Frame(label);
            }
            else if (this.compute == 3) {
                if (this.currentBasicBlock == null) {
                    this.currentBasicBlock = label;
                }
                else {
                    this.currentBasicBlock.frame.owner = label;
                }
            }
            else if (this.compute == 1) {
                if (this.currentBasicBlock != null) {
                    this.currentBasicBlock.outputStackMax = (short)this.maxRelativeStackSize;
                    this.addSuccessorToCurrentBasicBlock(this.relativeStackSize, label);
                }
                this.currentBasicBlock = label;
                this.relativeStackSize = 0;
                this.maxRelativeStackSize = 0;
                if (this.lastBasicBlock != null) {
                    this.lastBasicBlock.nextBasicBlock = label;
                }
                this.lastBasicBlock = label;
            }
            else if (this.compute == 2 && this.currentBasicBlock == null) {
                this.currentBasicBlock = label;
            }
        }
    }
    
    @Override
    public void visitLdcInsn(final Object value) {
        this.lastBytecodeOffset = this.code.length;
        final Symbol constantSymbol = this.symbolTable.addConstant(value);
        final int constantIndex = constantSymbol.index;
        final char firstDescriptorChar;
        final boolean isLongOrDouble = constantSymbol.tag == 5 || constantSymbol.tag == 6 || (constantSymbol.tag == 17 && ((firstDescriptorChar = constantSymbol.value.charAt(0)) == 'J' || firstDescriptorChar == 'D'));
        if (isLongOrDouble) {
            this.code.put12(20, constantIndex);
        }
        else if (constantIndex >= 256) {
            this.code.put12(19, constantIndex);
        }
        else {
            this.code.put11(18, constantIndex);
        }
        if (this.currentBasicBlock != null) {
            if (this.compute != 4 && this.compute != 3) {
                final int size = this.relativeStackSize + (isLongOrDouble ? 2 : 1);
                if (size > this.maxRelativeStackSize) {
                    this.maxRelativeStackSize = size;
                }
                this.relativeStackSize = size;
            }
            else {
                this.currentBasicBlock.frame.execute(18, 0, constantSymbol, this.symbolTable);
            }
        }
    }
    
    @Override
    public void visitIincInsn(final int var, final int increment) {
        this.lastBytecodeOffset = this.code.length;
        if (var <= 255 && increment <= 127 && increment >= -128) {
            this.code.putByte(132).put11(var, increment);
        }
        else {
            this.code.putByte(196).put12(132, var).putShort(increment);
        }
        if (this.currentBasicBlock != null && (this.compute == 4 || this.compute == 3)) {
            this.currentBasicBlock.frame.execute(132, var, null, null);
        }
        if (this.compute != 0) {
            final int currentMaxLocals = var + 1;
            if (currentMaxLocals > this.maxLocals) {
                this.maxLocals = currentMaxLocals;
            }
        }
    }
    
    @Override
    public void visitTableSwitchInsn(final int min, final int max, final Label dflt, final Label... labels) {
        this.lastBytecodeOffset = this.code.length;
        this.code.putByte(170).putByteArray(null, 0, (4 - this.code.length % 4) % 4);
        dflt.put(this.code, this.lastBytecodeOffset, true);
        this.code.putInt(min).putInt(max);
        final Label[] var5 = labels;
        for (int var6 = labels.length, var7 = 0; var7 < var6; ++var7) {
            final Label label = var5[var7];
            label.put(this.code, this.lastBytecodeOffset, true);
        }
        this.visitSwitchInsn(dflt, labels);
    }
    
    @Override
    public void visitLookupSwitchInsn(final Label dflt, final int[] keys, final Label[] labels) {
        this.lastBytecodeOffset = this.code.length;
        this.code.putByte(171).putByteArray(null, 0, (4 - this.code.length % 4) % 4);
        dflt.put(this.code, this.lastBytecodeOffset, true);
        this.code.putInt(labels.length);
        for (int i = 0; i < labels.length; ++i) {
            this.code.putInt(keys[i]);
            labels[i].put(this.code, this.lastBytecodeOffset, true);
        }
        this.visitSwitchInsn(dflt, labels);
    }
    
    private void visitSwitchInsn(final Label dflt, final Label[] labels) {
        if (this.currentBasicBlock != null) {
            if (this.compute == 4) {
                this.currentBasicBlock.frame.execute(171, 0, null, null);
                this.addSuccessorToCurrentBasicBlock(0, dflt);
                Label var10000 = dflt.getCanonicalInstance();
                var10000.flags |= 0x2;
                final Label[] var10001 = labels;
                for (int var10002 = labels.length, var10003 = 0; var10003 < var10002; ++var10003) {
                    final Label label = var10001[var10003];
                    this.addSuccessorToCurrentBasicBlock(0, label);
                    var10000 = label.getCanonicalInstance();
                    var10000.flags |= 0x2;
                }
            }
            else if (this.compute == 1) {
                this.addSuccessorToCurrentBasicBlock(--this.relativeStackSize, dflt);
                final Label[] var10001 = labels;
                for (int var10002 = labels.length, var10003 = 0; var10003 < var10002; ++var10003) {
                    final Label label = var10001[var10003];
                    this.addSuccessorToCurrentBasicBlock(this.relativeStackSize, label);
                }
            }
            this.endCurrentBasicBlockWithNoSuccessor();
        }
    }
    
    @Override
    public void visitMultiANewArrayInsn(final String descriptor, final int numDimensions) {
        this.lastBytecodeOffset = this.code.length;
        final Symbol descSymbol = this.symbolTable.addConstantClass(descriptor);
        this.code.put12(197, descSymbol.index).putByte(numDimensions);
        if (this.currentBasicBlock != null) {
            if (this.compute != 4 && this.compute != 3) {
                this.relativeStackSize += 1 - numDimensions;
            }
            else {
                this.currentBasicBlock.frame.execute(197, numDimensions, descSymbol, this.symbolTable);
            }
        }
    }
    
    @Override
    public AnnotationVisitor visitInsnAnnotation(final int typeRef, final TypePath typePath, final String descriptor, final boolean visible) {
        final ByteVector typeAnnotation = new ByteVector();
        TypeReference.putTarget((typeRef & 0xFF0000FF) | this.lastBytecodeOffset << 8, typeAnnotation);
        TypePath.put(typePath, typeAnnotation);
        typeAnnotation.putShort(this.symbolTable.addConstantUtf8(descriptor)).putShort(0);
        return visible ? (this.lastCodeRuntimeVisibleTypeAnnotation = new AnnotationWriter(this.symbolTable, typeAnnotation, this.lastCodeRuntimeVisibleTypeAnnotation)) : (this.lastCodeRuntimeInvisibleTypeAnnotation = new AnnotationWriter(this.symbolTable, typeAnnotation, this.lastCodeRuntimeInvisibleTypeAnnotation));
    }
    
    @Override
    public void visitTryCatchBlock(final Label start, final Label end, final Label handler, final String type) {
        final Handler newHandler = new Handler(start, end, handler, (type != null) ? this.symbolTable.addConstantClass(type).index : 0, type);
        if (this.firstHandler == null) {
            this.firstHandler = newHandler;
        }
        else {
            this.lastHandler.nextHandler = newHandler;
        }
        this.lastHandler = newHandler;
    }
    
    @Override
    public AnnotationVisitor visitTryCatchAnnotation(final int typeRef, final TypePath typePath, final String descriptor, final boolean visible) {
        final ByteVector typeAnnotation = new ByteVector();
        TypeReference.putTarget(typeRef, typeAnnotation);
        TypePath.put(typePath, typeAnnotation);
        typeAnnotation.putShort(this.symbolTable.addConstantUtf8(descriptor)).putShort(0);
        return visible ? (this.lastCodeRuntimeVisibleTypeAnnotation = new AnnotationWriter(this.symbolTable, typeAnnotation, this.lastCodeRuntimeVisibleTypeAnnotation)) : (this.lastCodeRuntimeInvisibleTypeAnnotation = new AnnotationWriter(this.symbolTable, typeAnnotation, this.lastCodeRuntimeInvisibleTypeAnnotation));
    }
    
    @Override
    public void visitLocalVariable(final String name, final String descriptor, final String signature, final Label start, final Label end, final int index) {
        if (signature != null) {
            if (this.localVariableTypeTable == null) {
                this.localVariableTypeTable = new ByteVector();
            }
            ++this.localVariableTypeTableLength;
            this.localVariableTypeTable.putShort(start.bytecodeOffset).putShort(end.bytecodeOffset - start.bytecodeOffset).putShort(this.symbolTable.addConstantUtf8(name)).putShort(this.symbolTable.addConstantUtf8(signature)).putShort(index);
        }
        if (this.localVariableTable == null) {
            this.localVariableTable = new ByteVector();
        }
        ++this.localVariableTableLength;
        this.localVariableTable.putShort(start.bytecodeOffset).putShort(end.bytecodeOffset - start.bytecodeOffset).putShort(this.symbolTable.addConstantUtf8(name)).putShort(this.symbolTable.addConstantUtf8(descriptor)).putShort(index);
        if (this.compute != 0) {
            final char firstDescChar = descriptor.charAt(0);
            final int currentMaxLocals = index + ((firstDescChar != 'J' && firstDescChar != 'D') ? 1 : 2);
            if (currentMaxLocals > this.maxLocals) {
                this.maxLocals = currentMaxLocals;
            }
        }
    }
    
    @Override
    public AnnotationVisitor visitLocalVariableAnnotation(final int typeRef, final TypePath typePath, final Label[] start, final Label[] end, final int[] index, final String descriptor, final boolean visible) {
        final ByteVector typeAnnotation = new ByteVector();
        typeAnnotation.putByte(typeRef >>> 24).putShort(start.length);
        for (int i = 0; i < start.length; ++i) {
            typeAnnotation.putShort(start[i].bytecodeOffset).putShort(end[i].bytecodeOffset - start[i].bytecodeOffset).putShort(index[i]);
        }
        TypePath.put(typePath, typeAnnotation);
        typeAnnotation.putShort(this.symbolTable.addConstantUtf8(descriptor)).putShort(0);
        return visible ? (this.lastCodeRuntimeVisibleTypeAnnotation = new AnnotationWriter(this.symbolTable, typeAnnotation, this.lastCodeRuntimeVisibleTypeAnnotation)) : (this.lastCodeRuntimeInvisibleTypeAnnotation = new AnnotationWriter(this.symbolTable, typeAnnotation, this.lastCodeRuntimeInvisibleTypeAnnotation));
    }
    
    @Override
    public void visitLineNumber(final int line, final Label start) {
        if (this.lineNumberTable == null) {
            this.lineNumberTable = new ByteVector();
        }
        ++this.lineNumberTableLength;
        this.lineNumberTable.putShort(start.bytecodeOffset);
        this.lineNumberTable.putShort(line);
    }
    
    @Override
    public void visitMaxs(final int maxStack, final int maxLocals) {
        if (this.compute == 4) {
            this.computeAllFrames();
        }
        else if (this.compute == 1) {
            this.computeMaxStackAndLocal();
        }
        else if (this.compute == 2) {
            this.maxStack = this.maxRelativeStackSize;
        }
        else {
            this.maxStack = maxStack;
            this.maxLocals = maxLocals;
        }
    }
    
    private void computeAllFrames() {
        for (Handler handler = this.firstHandler; handler != null; handler = handler.nextHandler) {
            final String firstFrame = (handler.catchTypeDescriptor == null) ? "java/lang/Throwable" : handler.catchTypeDescriptor;
            final int listOfBlocksToProcess = Frame.getAbstractTypeFromInternalName(this.symbolTable, firstFrame);
            final Label maxStackSize = handler.handlerPc.getCanonicalInstance();
            maxStackSize.flags |= 0x2;
            for (Label basicBlock = handler.startPc.getCanonicalInstance(), nextBasicBlock = handler.endPc.getCanonicalInstance(); basicBlock != nextBasicBlock; basicBlock = basicBlock.nextBasicBlock) {
                basicBlock.outgoingEdges = new Edge(listOfBlocksToProcess, maxStackSize, basicBlock.outgoingEdges);
            }
        }
        final Frame var10 = this.firstBasicBlock.frame;
        var10.setInputFrameFromDescriptor(this.symbolTable, this.accessFlags, this.descriptor, this.maxLocals);
        var10.accept(this);
        Label var11 = this.firstBasicBlock;
        var11.nextListElement = Label.EMPTY_LIST;
        int var12 = 0;
        while (var11 != Label.EMPTY_LIST) {
            final Label basicBlock = var11;
            var11 = var11.nextListElement;
            basicBlock.nextListElement = null;
            basicBlock.flags |= 0x8;
            final int var13 = basicBlock.frame.getInputStackSize() + basicBlock.outputStackMax;
            if (var13 > var12) {
                var12 = var13;
            }
            for (Edge startOffset = basicBlock.outgoingEdges; startOffset != null; startOffset = startOffset.nextEdge) {
                final Label endOffset = startOffset.successor.getCanonicalInstance();
                final boolean frameIndex = basicBlock.frame.merge(this.symbolTable, endOffset.frame, startOffset.info);
                if (frameIndex && endOffset.nextListElement == null) {
                    endOffset.nextListElement = var11;
                    var11 = endOffset;
                }
            }
        }
        for (Label basicBlock = this.firstBasicBlock; basicBlock != null; basicBlock = basicBlock.nextBasicBlock) {
            if ((basicBlock.flags & 0xA) == 0xA) {
                basicBlock.frame.accept(this);
            }
            if ((basicBlock.flags & 0x8) == 0x0) {
                final Label nextBasicBlock = basicBlock.nextBasicBlock;
                final int var14 = basicBlock.bytecodeOffset;
                final int var15 = ((nextBasicBlock == null) ? this.code.length : nextBasicBlock.bytecodeOffset) - 1;
                if (var15 >= var14) {
                    for (int var16 = var14; var16 < var15; ++var16) {
                        this.code.data[var16] = 0;
                    }
                    this.code.data[var15] = -65;
                    int var16 = this.visitFrameStart(var14, 0, 1);
                    this.currentFrame[var16] = Frame.getAbstractTypeFromInternalName(this.symbolTable, "java/lang/Throwable");
                    this.visitFrameEnd();
                    this.firstHandler = Handler.removeRange(this.firstHandler, basicBlock, nextBasicBlock);
                    var12 = Math.max(var12, 1);
                }
            }
        }
        this.maxStack = var12;
    }
    
    private void computeMaxStackAndLocal() {
        for (Handler handler = this.firstHandler; handler != null; handler = handler.nextHandler) {
            final Label listOfBlocksToProcess = handler.handlerPc;
            for (Label maxStackSize = handler.startPc, basicBlock = handler.endPc; maxStackSize != basicBlock; maxStackSize = maxStackSize.nextBasicBlock) {
                if ((maxStackSize.flags & 0x10) == 0x0) {
                    maxStackSize.outgoingEdges = new Edge(Integer.MAX_VALUE, listOfBlocksToProcess, maxStackSize.outgoingEdges);
                }
                else {
                    maxStackSize.outgoingEdges.nextEdge.nextEdge = new Edge(Integer.MAX_VALUE, listOfBlocksToProcess, maxStackSize.outgoingEdges.nextEdge.nextEdge);
                }
            }
        }
        if (this.hasSubroutines) {
            short var9 = 1;
            this.firstBasicBlock.markSubroutine(var9);
            for (short var10 = 1; var10 <= var9; ++var10) {
                for (Label basicBlock = this.firstBasicBlock; basicBlock != null; basicBlock = basicBlock.nextBasicBlock) {
                    if ((basicBlock.flags & 0x10) != 0x0 && basicBlock.subroutineId == var10) {
                        final Label inputStackTop = basicBlock.outgoingEdges.nextEdge.successor;
                        if (inputStackTop.subroutineId == 0) {
                            ++var9;
                            inputStackTop.markSubroutine(var9);
                        }
                    }
                }
            }
            for (Label maxStackSize = this.firstBasicBlock; maxStackSize != null; maxStackSize = maxStackSize.nextBasicBlock) {
                if ((maxStackSize.flags & 0x10) != 0x0) {
                    final Label basicBlock = maxStackSize.outgoingEdges.nextEdge.successor;
                    basicBlock.addSubroutineRetSuccessors(maxStackSize);
                }
            }
        }
        Label listOfBlocksToProcess = this.firstBasicBlock;
        listOfBlocksToProcess.nextListElement = Label.EMPTY_LIST;
        int var11 = this.maxStack;
        while (listOfBlocksToProcess != Label.EMPTY_LIST) {
            final Label basicBlock = listOfBlocksToProcess;
            listOfBlocksToProcess = listOfBlocksToProcess.nextListElement;
            final short var12 = basicBlock.inputStackSize;
            final int maxBlockStackSize = var12 + basicBlock.outputStackMax;
            if (maxBlockStackSize > var11) {
                var11 = maxBlockStackSize;
            }
            Edge outgoingEdge = basicBlock.outgoingEdges;
            if ((basicBlock.flags & 0x10) != 0x0) {
                outgoingEdge = outgoingEdge.nextEdge;
            }
            while (outgoingEdge != null) {
                final Label successorBlock = outgoingEdge.successor;
                if (successorBlock.nextListElement == null) {
                    successorBlock.inputStackSize = (short)((outgoingEdge.info == Integer.MAX_VALUE) ? 1 : (var12 + outgoingEdge.info));
                    successorBlock.nextListElement = listOfBlocksToProcess;
                    listOfBlocksToProcess = successorBlock;
                }
                outgoingEdge = outgoingEdge.nextEdge;
            }
        }
        this.maxStack = var11;
    }
    
    @Override
    public void visitEnd() {
    }
    
    private void addSuccessorToCurrentBasicBlock(final int info, final Label successor) {
        this.currentBasicBlock.outgoingEdges = new Edge(info, successor, this.currentBasicBlock.outgoingEdges);
    }
    
    private void endCurrentBasicBlockWithNoSuccessor() {
        if (this.compute == 4) {
            final Label nextBasicBlock = new Label();
            nextBasicBlock.frame = new Frame(nextBasicBlock);
            nextBasicBlock.resolve(this.code.data, this.code.length);
            this.lastBasicBlock.nextBasicBlock = nextBasicBlock;
            this.lastBasicBlock = nextBasicBlock;
            this.currentBasicBlock = null;
        }
        else if (this.compute == 1) {
            this.currentBasicBlock.outputStackMax = (short)this.maxRelativeStackSize;
            this.currentBasicBlock = null;
        }
    }
    
    int visitFrameStart(final int offset, final int numLocal, final int numStack) {
        final int frameLength = 3 + numLocal + numStack;
        if (this.currentFrame == null || this.currentFrame.length < frameLength) {
            this.currentFrame = new int[frameLength];
        }
        this.currentFrame[0] = offset;
        this.currentFrame[1] = numLocal;
        this.currentFrame[2] = numStack;
        return 3;
    }
    
    void visitAbstractType(final int frameIndex, final int abstractType) {
        this.currentFrame[frameIndex] = abstractType;
    }
    
    void visitFrameEnd() {
        if (this.previousFrame != null) {
            if (this.stackMapTableEntries == null) {
                this.stackMapTableEntries = new ByteVector();
            }
            this.putFrame();
            ++this.stackMapTableNumberOfEntries;
        }
        this.previousFrame = this.currentFrame;
        this.currentFrame = null;
    }
    
    private void putFrame() {
        final int numLocal = this.currentFrame[1];
        final int numStack = this.currentFrame[2];
        if (this.symbolTable.getMajorVersion() < 50) {
            this.stackMapTableEntries.putShort(this.currentFrame[0]).putShort(numLocal);
            this.putAbstractTypes(3, 3 + numLocal);
            this.stackMapTableEntries.putShort(numStack);
            this.putAbstractTypes(3 + numLocal, 3 + numLocal + numStack);
        }
        else {
            final int offsetDelta = (this.stackMapTableNumberOfEntries == 0) ? this.currentFrame[0] : (this.currentFrame[0] - this.previousFrame[0] - 1);
            final int previousNumlocal = this.previousFrame[1];
            final int numLocalDelta = numLocal - previousNumlocal;
            int type = 255;
            if (numStack == 0) {
                switch (numLocalDelta) {
                    case -3:
                    case -2:
                    case -1: {
                        type = 248;
                        break;
                    }
                    case 0: {
                        type = ((offsetDelta < 64) ? 0 : 251);
                        break;
                    }
                    case 1:
                    case 2:
                    case 3: {
                        type = 252;
                        break;
                    }
                }
            }
            else if (numLocalDelta == 0 && numStack == 1) {
                type = ((offsetDelta < 63) ? 64 : 247);
            }
            if (type != 255) {
                int frameIndex = 3;
                for (int i = 0; i < previousNumlocal && i < numLocal; ++i) {
                    if (this.currentFrame[frameIndex] != this.previousFrame[frameIndex]) {
                        type = 255;
                        break;
                    }
                    ++frameIndex;
                }
            }
            switch (type) {
                case 0: {
                    this.stackMapTableEntries.putByte(offsetDelta);
                    break;
                }
                case 64: {
                    this.stackMapTableEntries.putByte(64 + offsetDelta);
                    this.putAbstractTypes(3 + numLocal, 4 + numLocal);
                    break;
                }
                case 247: {
                    this.stackMapTableEntries.putByte(247).putShort(offsetDelta);
                    this.putAbstractTypes(3 + numLocal, 4 + numLocal);
                    break;
                }
                case 248: {
                    this.stackMapTableEntries.putByte(251 + numLocalDelta).putShort(offsetDelta);
                    break;
                }
                case 251: {
                    this.stackMapTableEntries.putByte(251).putShort(offsetDelta);
                    break;
                }
                case 252: {
                    this.stackMapTableEntries.putByte(251 + numLocalDelta).putShort(offsetDelta);
                    this.putAbstractTypes(3 + previousNumlocal, 3 + numLocal);
                    break;
                }
                default: {
                    this.stackMapTableEntries.putByte(255).putShort(offsetDelta).putShort(numLocal);
                    this.putAbstractTypes(3, 3 + numLocal);
                    this.stackMapTableEntries.putShort(numStack);
                    this.putAbstractTypes(3 + numLocal, 3 + numLocal + numStack);
                    break;
                }
            }
        }
    }
    
    private void putAbstractTypes(final int start, final int end) {
        for (int i = start; i < end; ++i) {
            Frame.putAbstractType(this.symbolTable, this.currentFrame[i], this.stackMapTableEntries);
        }
    }
    
    private void putFrameType(final Object type) {
        if (type instanceof Integer) {
            this.stackMapTableEntries.putByte((int)type);
        }
        else if (type instanceof String) {
            this.stackMapTableEntries.putByte(7).putShort(this.symbolTable.addConstantClass((String)type).index);
        }
        else {
            this.stackMapTableEntries.putByte(8).putShort(((Label)type).bytecodeOffset);
        }
    }
    
    boolean canCopyMethodAttributes(final ClassReader source, final int methodInfoOffset, final int methodInfoLength, final boolean hasSyntheticAttribute, final boolean hasDeprecatedAttribute, final int descriptorIndex, final int signatureIndex, final int exceptionsOffset) {
        if (source != this.symbolTable.getSource() || descriptorIndex != this.descriptorIndex || signatureIndex != this.signatureIndex || hasDeprecatedAttribute != ((this.accessFlags & 0x20000) != 0x0)) {
            return false;
        }
        final boolean needSyntheticAttribute = this.symbolTable.getMajorVersion() < 49 && (this.accessFlags & 0x1000) != 0x0;
        if (hasSyntheticAttribute != needSyntheticAttribute) {
            return false;
        }
        if (exceptionsOffset == 0) {
            if (this.numberOfExceptions != 0) {
                return false;
            }
        }
        else if (source.readUnsignedShort(exceptionsOffset) == this.numberOfExceptions) {
            int currentExceptionOffset = exceptionsOffset + 2;
            for (int i = 0; i < this.numberOfExceptions; ++i) {
                if (source.readUnsignedShort(currentExceptionOffset) != this.exceptionIndexTable[i]) {
                    return false;
                }
                currentExceptionOffset += 2;
            }
        }
        this.sourceOffset = methodInfoOffset + 6;
        this.sourceLength = methodInfoLength - 6;
        return true;
    }
    
    int computeMethodInfoSize() {
        if (this.sourceOffset != 0) {
            return 6 + this.sourceLength;
        }
        int size = 8;
        if (this.code.length > 0) {
            if (this.code.length > 65535) {
                throw new MethodTooLargeException(this.symbolTable.getClassName(), this.name, this.descriptor, this.code.length);
            }
            this.symbolTable.addConstantUtf8("Code");
            size += 16 + this.code.length + Handler.getExceptionTableSize(this.firstHandler);
            if (this.stackMapTableEntries != null) {
                final boolean useSyntheticAttribute = this.symbolTable.getMajorVersion() >= 50;
                this.symbolTable.addConstantUtf8(useSyntheticAttribute ? "StackMapTable" : "StackMap");
                size += 8 + this.stackMapTableEntries.length;
            }
            if (this.lineNumberTable != null) {
                this.symbolTable.addConstantUtf8("LineNumberTable");
                size += 8 + this.lineNumberTable.length;
            }
            if (this.localVariableTable != null) {
                this.symbolTable.addConstantUtf8("LocalVariableTable");
                size += 8 + this.localVariableTable.length;
            }
            if (this.localVariableTypeTable != null) {
                this.symbolTable.addConstantUtf8("LocalVariableTypeTable");
                size += 8 + this.localVariableTypeTable.length;
            }
            if (this.lastCodeRuntimeVisibleTypeAnnotation != null) {
                size += this.lastCodeRuntimeVisibleTypeAnnotation.computeAnnotationsSize("RuntimeVisibleTypeAnnotations");
            }
            if (this.lastCodeRuntimeInvisibleTypeAnnotation != null) {
                size += this.lastCodeRuntimeInvisibleTypeAnnotation.computeAnnotationsSize("RuntimeInvisibleTypeAnnotations");
            }
            if (this.firstCodeAttribute != null) {
                size += this.firstCodeAttribute.computeAttributesSize(this.symbolTable, this.code.data, this.code.length, this.maxStack, this.maxLocals);
            }
        }
        if (this.numberOfExceptions > 0) {
            this.symbolTable.addConstantUtf8("Exceptions");
            size += 8 + 2 * this.numberOfExceptions;
        }
        final boolean useSyntheticAttribute = this.symbolTable.getMajorVersion() < 49;
        if ((this.accessFlags & 0x1000) != 0x0 && useSyntheticAttribute) {
            this.symbolTable.addConstantUtf8("Synthetic");
            size += 6;
        }
        if (this.signatureIndex != 0) {
            this.symbolTable.addConstantUtf8("Signature");
            size += 8;
        }
        if ((this.accessFlags & 0x20000) != 0x0) {
            this.symbolTable.addConstantUtf8("Deprecated");
            size += 6;
        }
        if (this.lastRuntimeVisibleAnnotation != null) {
            size += this.lastRuntimeVisibleAnnotation.computeAnnotationsSize("RuntimeVisibleAnnotations");
        }
        if (this.lastRuntimeInvisibleAnnotation != null) {
            size += this.lastRuntimeInvisibleAnnotation.computeAnnotationsSize("RuntimeInvisibleAnnotations");
        }
        if (this.lastRuntimeVisibleParameterAnnotations != null) {
            size += AnnotationWriter.computeParameterAnnotationsSize("RuntimeVisibleParameterAnnotations", this.lastRuntimeVisibleParameterAnnotations, (this.visibleAnnotableParameterCount == 0) ? this.lastRuntimeVisibleParameterAnnotations.length : this.visibleAnnotableParameterCount);
        }
        if (this.lastRuntimeInvisibleParameterAnnotations != null) {
            size += AnnotationWriter.computeParameterAnnotationsSize("RuntimeInvisibleParameterAnnotations", this.lastRuntimeInvisibleParameterAnnotations, (this.invisibleAnnotableParameterCount == 0) ? this.lastRuntimeInvisibleParameterAnnotations.length : this.invisibleAnnotableParameterCount);
        }
        if (this.lastRuntimeVisibleTypeAnnotation != null) {
            size += this.lastRuntimeVisibleTypeAnnotation.computeAnnotationsSize("RuntimeVisibleTypeAnnotations");
        }
        if (this.lastRuntimeInvisibleTypeAnnotation != null) {
            size += this.lastRuntimeInvisibleTypeAnnotation.computeAnnotationsSize("RuntimeInvisibleTypeAnnotations");
        }
        if (this.defaultValue != null) {
            this.symbolTable.addConstantUtf8("AnnotationDefault");
            size += 6 + this.defaultValue.length;
        }
        if (this.parameters != null) {
            this.symbolTable.addConstantUtf8("MethodParameters");
            size += 7 + this.parameters.length;
        }
        if (this.firstAttribute != null) {
            size += this.firstAttribute.computeAttributesSize(this.symbolTable);
        }
        return size;
    }
    
    void putMethodInfo(final ByteVector output) {
        final boolean useSyntheticAttribute = this.symbolTable.getMajorVersion() < 49;
        final int mask = useSyntheticAttribute ? 4096 : 0;
        output.putShort(this.accessFlags & ~mask).putShort(this.nameIndex).putShort(this.descriptorIndex);
        if (this.sourceOffset != 0) {
            output.putByteArray(this.symbolTable.getSource().b, this.sourceOffset, this.sourceLength);
        }
        else {
            int attributeCount = 0;
            if (this.code.length > 0) {
                ++attributeCount;
            }
            if (this.numberOfExceptions > 0) {
                ++attributeCount;
            }
            if ((this.accessFlags & 0x1000) != 0x0 && useSyntheticAttribute) {
                ++attributeCount;
            }
            if (this.signatureIndex != 0) {
                ++attributeCount;
            }
            if ((this.accessFlags & 0x20000) != 0x0) {
                ++attributeCount;
            }
            if (this.lastRuntimeVisibleAnnotation != null) {
                ++attributeCount;
            }
            if (this.lastRuntimeInvisibleAnnotation != null) {
                ++attributeCount;
            }
            if (this.lastRuntimeVisibleParameterAnnotations != null) {
                ++attributeCount;
            }
            if (this.lastRuntimeInvisibleParameterAnnotations != null) {
                ++attributeCount;
            }
            if (this.lastRuntimeVisibleTypeAnnotation != null) {
                ++attributeCount;
            }
            if (this.lastRuntimeInvisibleTypeAnnotation != null) {
                ++attributeCount;
            }
            if (this.defaultValue != null) {
                ++attributeCount;
            }
            if (this.parameters != null) {
                ++attributeCount;
            }
            if (this.firstAttribute != null) {
                attributeCount += this.firstAttribute.getAttributeCount();
            }
            output.putShort(attributeCount);
            if (this.code.length > 0) {
                int size = 10 + this.code.length + Handler.getExceptionTableSize(this.firstHandler);
                int codeAttributeCount = 0;
                if (this.stackMapTableEntries != null) {
                    size += 8 + this.stackMapTableEntries.length;
                    ++codeAttributeCount;
                }
                if (this.lineNumberTable != null) {
                    size += 8 + this.lineNumberTable.length;
                    ++codeAttributeCount;
                }
                if (this.localVariableTable != null) {
                    size += 8 + this.localVariableTable.length;
                    ++codeAttributeCount;
                }
                if (this.localVariableTypeTable != null) {
                    size += 8 + this.localVariableTypeTable.length;
                    ++codeAttributeCount;
                }
                if (this.lastCodeRuntimeVisibleTypeAnnotation != null) {
                    size += this.lastCodeRuntimeVisibleTypeAnnotation.computeAnnotationsSize("RuntimeVisibleTypeAnnotations");
                    ++codeAttributeCount;
                }
                if (this.lastCodeRuntimeInvisibleTypeAnnotation != null) {
                    size += this.lastCodeRuntimeInvisibleTypeAnnotation.computeAnnotationsSize("RuntimeInvisibleTypeAnnotations");
                    ++codeAttributeCount;
                }
                if (this.firstCodeAttribute != null) {
                    size += this.firstCodeAttribute.computeAttributesSize(this.symbolTable, this.code.data, this.code.length, this.maxStack, this.maxLocals);
                    codeAttributeCount += this.firstCodeAttribute.getAttributeCount();
                }
                output.putShort(this.symbolTable.addConstantUtf8("Code")).putInt(size).putShort(this.maxStack).putShort(this.maxLocals).putInt(this.code.length).putByteArray(this.code.data, 0, this.code.length);
                Handler.putExceptionTable(this.firstHandler, output);
                output.putShort(codeAttributeCount);
                if (this.stackMapTableEntries != null) {
                    final boolean useStackMapTable = this.symbolTable.getMajorVersion() >= 50;
                    output.putShort(this.symbolTable.addConstantUtf8(useStackMapTable ? "StackMapTable" : "StackMap")).putInt(2 + this.stackMapTableEntries.length).putShort(this.stackMapTableNumberOfEntries).putByteArray(this.stackMapTableEntries.data, 0, this.stackMapTableEntries.length);
                }
                if (this.lineNumberTable != null) {
                    output.putShort(this.symbolTable.addConstantUtf8("LineNumberTable")).putInt(2 + this.lineNumberTable.length).putShort(this.lineNumberTableLength).putByteArray(this.lineNumberTable.data, 0, this.lineNumberTable.length);
                }
                if (this.localVariableTable != null) {
                    output.putShort(this.symbolTable.addConstantUtf8("LocalVariableTable")).putInt(2 + this.localVariableTable.length).putShort(this.localVariableTableLength).putByteArray(this.localVariableTable.data, 0, this.localVariableTable.length);
                }
                if (this.localVariableTypeTable != null) {
                    output.putShort(this.symbolTable.addConstantUtf8("LocalVariableTypeTable")).putInt(2 + this.localVariableTypeTable.length).putShort(this.localVariableTypeTableLength).putByteArray(this.localVariableTypeTable.data, 0, this.localVariableTypeTable.length);
                }
                if (this.lastCodeRuntimeVisibleTypeAnnotation != null) {
                    this.lastCodeRuntimeVisibleTypeAnnotation.putAnnotations(this.symbolTable.addConstantUtf8("RuntimeVisibleTypeAnnotations"), output);
                }
                if (this.lastCodeRuntimeInvisibleTypeAnnotation != null) {
                    this.lastCodeRuntimeInvisibleTypeAnnotation.putAnnotations(this.symbolTable.addConstantUtf8("RuntimeInvisibleTypeAnnotations"), output);
                }
                if (this.firstCodeAttribute != null) {
                    this.firstCodeAttribute.putAttributes(this.symbolTable, this.code.data, this.code.length, this.maxStack, this.maxLocals, output);
                }
            }
            if (this.numberOfExceptions > 0) {
                output.putShort(this.symbolTable.addConstantUtf8("Exceptions")).putInt(2 + 2 * this.numberOfExceptions).putShort(this.numberOfExceptions);
                for (final int exceptionIndex : this.exceptionIndexTable) {
                    output.putShort(exceptionIndex);
                }
            }
            if ((this.accessFlags & 0x1000) != 0x0 && useSyntheticAttribute) {
                output.putShort(this.symbolTable.addConstantUtf8("Synthetic")).putInt(0);
            }
            if (this.signatureIndex != 0) {
                output.putShort(this.symbolTable.addConstantUtf8("Signature")).putInt(2).putShort(this.signatureIndex);
            }
            if ((this.accessFlags & 0x20000) != 0x0) {
                output.putShort(this.symbolTable.addConstantUtf8("Deprecated")).putInt(0);
            }
            if (this.lastRuntimeVisibleAnnotation != null) {
                this.lastRuntimeVisibleAnnotation.putAnnotations(this.symbolTable.addConstantUtf8("RuntimeVisibleAnnotations"), output);
            }
            if (this.lastRuntimeInvisibleAnnotation != null) {
                this.lastRuntimeInvisibleAnnotation.putAnnotations(this.symbolTable.addConstantUtf8("RuntimeInvisibleAnnotations"), output);
            }
            if (this.lastRuntimeVisibleParameterAnnotations != null) {
                AnnotationWriter.putParameterAnnotations(this.symbolTable.addConstantUtf8("RuntimeVisibleParameterAnnotations"), this.lastRuntimeVisibleParameterAnnotations, (this.visibleAnnotableParameterCount == 0) ? this.lastRuntimeVisibleParameterAnnotations.length : this.visibleAnnotableParameterCount, output);
            }
            if (this.lastRuntimeInvisibleParameterAnnotations != null) {
                AnnotationWriter.putParameterAnnotations(this.symbolTable.addConstantUtf8("RuntimeInvisibleParameterAnnotations"), this.lastRuntimeInvisibleParameterAnnotations, (this.invisibleAnnotableParameterCount == 0) ? this.lastRuntimeInvisibleParameterAnnotations.length : this.invisibleAnnotableParameterCount, output);
            }
            if (this.lastRuntimeVisibleTypeAnnotation != null) {
                this.lastRuntimeVisibleTypeAnnotation.putAnnotations(this.symbolTable.addConstantUtf8("RuntimeVisibleTypeAnnotations"), output);
            }
            if (this.lastRuntimeInvisibleTypeAnnotation != null) {
                this.lastRuntimeInvisibleTypeAnnotation.putAnnotations(this.symbolTable.addConstantUtf8("RuntimeInvisibleTypeAnnotations"), output);
            }
            if (this.defaultValue != null) {
                output.putShort(this.symbolTable.addConstantUtf8("AnnotationDefault")).putInt(this.defaultValue.length).putByteArray(this.defaultValue.data, 0, this.defaultValue.length);
            }
            if (this.parameters != null) {
                output.putShort(this.symbolTable.addConstantUtf8("MethodParameters")).putInt(1 + this.parameters.length).putByte(this.parametersCount).putByteArray(this.parameters.data, 0, this.parameters.length);
            }
            if (this.firstAttribute != null) {
                this.firstAttribute.putAttributes(this.symbolTable, output);
            }
        }
    }
    
    final void collectAttributePrototypes(final Attribute.Set attributePrototypes) {
        attributePrototypes.addAttributes(this.firstAttribute);
        attributePrototypes.addAttributes(this.firstCodeAttribute);
    }
}
