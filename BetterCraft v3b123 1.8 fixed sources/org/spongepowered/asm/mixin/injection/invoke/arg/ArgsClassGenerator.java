// 
// Decompiled by Procyon v0.6.0
// 

package org.spongepowered.asm.mixin.injection.invoke.arg;

import org.spongepowered.asm.util.SignaturePrinter;
import org.spongepowered.asm.mixin.transformer.SyntheticClassInfo;
import org.spongepowered.asm.service.MixinService;
import org.objectweb.asm.Label;
import org.objectweb.asm.Type;
import org.spongepowered.asm.util.asm.MethodVisitorEx;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.util.CheckClassAdapter;
import org.spongepowered.asm.mixin.MixinEnvironment;
import org.objectweb.asm.tree.ClassNode;
import org.spongepowered.asm.util.Bytecode;
import org.spongepowered.asm.mixin.extensibility.IMixinInfo;
import java.util.HashMap;
import java.util.Map;
import org.spongepowered.asm.service.ISyntheticClassInfo;
import org.spongepowered.asm.util.IConsumer;
import org.spongepowered.asm.logging.ILogger;
import org.spongepowered.asm.mixin.transformer.ext.IClassGenerator;

public final class ArgsClassGenerator implements IClassGenerator
{
    public static final String ARGS_NAME;
    public static final String ARGS_REF;
    public static final String GETTER_PREFIX = "$";
    private static final String CLASS_NAME_BASE = "org.spongepowered.asm.synthetic.args.Args$";
    private static final String OBJECT = "java/lang/Object";
    private static final String OBJECT_ARRAY = "[Ljava/lang/Object;";
    private static final String VALUES_FIELD = "values";
    private static final String CTOR_DESC = "([Ljava/lang/Object;)V";
    private static final String SET = "set";
    private static final String SET_DESC = "(ILjava/lang/Object;)V";
    private static final String SETALL = "setAll";
    private static final String SETALL_DESC = "([Ljava/lang/Object;)V";
    private static final String NPE = "java/lang/NullPointerException";
    private static final String NPE_CTOR_DESC = "(Ljava/lang/String;)V";
    private static final String AIOOBE = "org/spongepowered/asm/mixin/injection/invoke/arg/ArgumentIndexOutOfBoundsException";
    private static final String AIOOBE_CTOR_DESC = "(I)V";
    private static final String ACE = "org/spongepowered/asm/mixin/injection/invoke/arg/ArgumentCountException";
    private static final String ACE_CTOR_DESC = "(IILjava/lang/String;)V";
    private static final ILogger logger;
    private final IConsumer<ISyntheticClassInfo> registry;
    private int nextIndex;
    private final Map<String, ArgsClassInfo> descToClass;
    private final Map<String, ArgsClassInfo> nameToClass;
    
    public ArgsClassGenerator(final IConsumer<ISyntheticClassInfo> registry) {
        this.nextIndex = 1;
        this.descToClass = new HashMap<String, ArgsClassInfo>();
        this.nameToClass = new HashMap<String, ArgsClassInfo>();
        this.registry = registry;
    }
    
    @Override
    public String getName() {
        return "args";
    }
    
    public ISyntheticClassInfo getArgsClass(final String desc, final IMixinInfo mixin) {
        final String voidDesc = Bytecode.changeDescriptorReturnType(desc, "V");
        ArgsClassInfo info = this.descToClass.get(voidDesc);
        if (info == null) {
            final String name = String.format("%s%d", "org.spongepowered.asm.synthetic.args.Args$", this.nextIndex++);
            ArgsClassGenerator.logger.debug("ArgsClassGenerator assigning {} for descriptor {}", name, voidDesc);
            info = new ArgsClassInfo(mixin, name, voidDesc);
            this.descToClass.put(voidDesc, info);
            this.nameToClass.put(name, info);
            this.registry.accept(info);
        }
        return info;
    }
    
    @Override
    public boolean generate(final String name, final ClassNode classNode) {
        final ArgsClassInfo info = this.nameToClass.get(name);
        if (info == null) {
            return false;
        }
        if (info.loaded > 0) {
            ArgsClassGenerator.logger.debug("ArgsClassGenerator is re-generating {}, already did this {} times!", name, info.loaded);
        }
        ClassVisitor visitor = classNode;
        if (MixinEnvironment.getCurrentEnvironment().getOption(MixinEnvironment.Option.DEBUG_VERIFY)) {
            visitor = new CheckClassAdapter(classNode);
        }
        visitor.visit(50, 4129, info.getName(), null, ArgsClassGenerator.ARGS_REF, null);
        visitor.visitSource(name.substring(name.lastIndexOf(46) + 1) + ".java", null);
        this.generateCtor(info, visitor);
        this.generateToString(info, visitor);
        this.generateFactory(info, visitor);
        this.generateSetters(info, visitor);
        this.generateGetters(info, visitor);
        visitor.visitEnd();
        final ArgsClassInfo argsClassInfo = info;
        ++argsClassInfo.loaded;
        return true;
    }
    
    private void generateCtor(final ArgsClassInfo info, final ClassVisitor writer) {
        final MethodVisitor ctor = writer.visitMethod(2, "<init>", "([Ljava/lang/Object;)V", null, null);
        ctor.visitCode();
        ctor.visitVarInsn(25, 0);
        ctor.visitVarInsn(25, 1);
        ctor.visitMethodInsn(183, ArgsClassGenerator.ARGS_REF, "<init>", "([Ljava/lang/Object;)V", false);
        ctor.visitInsn(177);
        ctor.visitMaxs(2, 2);
        ctor.visitEnd();
    }
    
    private void generateToString(final ArgsClassInfo info, final ClassVisitor writer) {
        final MethodVisitor toString = writer.visitMethod(1, "toString", "()Ljava/lang/String;", null, null);
        toString.visitCode();
        toString.visitLdcInsn("Args" + info.getSignature());
        toString.visitInsn(176);
        toString.visitMaxs(1, 1);
        toString.visitEnd();
    }
    
    private void generateFactory(final ArgsClassInfo info, final ClassVisitor writer) {
        final String ref = info.getName();
        final String factoryDesc = Bytecode.changeDescriptorReturnType(info.desc, "L" + ref + ";");
        final MethodVisitorEx of = new MethodVisitorEx(writer.visitMethod(9, "of", factoryDesc, null, null));
        of.visitCode();
        of.visitTypeInsn(187, ref);
        of.visitInsn(89);
        of.visitConstant((byte)info.args.length);
        of.visitTypeInsn(189, "java/lang/Object");
        byte index = 0;
        byte argIndex = 0;
        while (index < info.args.length) {
            final Type arg = info.args[index];
            of.visitInsn(89);
            of.visitConstant(index);
            of.visitVarInsn(arg.getOpcode(21), argIndex);
            box(of, arg);
            of.visitInsn(83);
            argIndex += (byte)arg.getSize();
            ++index;
        }
        of.visitMethodInsn(183, ref, "<init>", "([Ljava/lang/Object;)V", false);
        of.visitInsn(176);
        of.visitMaxs(6, Bytecode.getArgsSize(info.args));
        of.visitEnd();
    }
    
    private void generateGetters(final ArgsClassInfo info, final ClassVisitor writer) {
        byte argIndex = 0;
        for (final Type arg : info.args) {
            final String name = "$" + argIndex;
            final String sig = "()" + arg.getDescriptor();
            final MethodVisitorEx get = new MethodVisitorEx(writer.visitMethod(1, name, sig, null, null));
            get.visitCode();
            get.visitVarInsn(25, 0);
            get.visitFieldInsn(180, info.getName(), "values", "[Ljava/lang/Object;");
            get.visitConstant(argIndex);
            get.visitInsn(50);
            unbox(get, arg);
            get.visitInsn(arg.getOpcode(172));
            get.visitMaxs(2, 1);
            get.visitEnd();
            ++argIndex;
        }
    }
    
    private void generateSetters(final ArgsClassInfo info, final ClassVisitor writer) {
        this.generateIndexedSetter(info, writer);
        this.generateMultiSetter(info, writer);
    }
    
    private void generateIndexedSetter(final ArgsClassInfo info, final ClassVisitor writer) {
        final MethodVisitorEx set = new MethodVisitorEx(writer.visitMethod(1, "set", "(ILjava/lang/Object;)V", null, null));
        set.visitCode();
        final Label store = new Label();
        final Label checkNull = new Label();
        final Label[] labels = new Label[info.args.length];
        for (int label = 0; label < labels.length; ++label) {
            labels[label] = new Label();
        }
        set.visitVarInsn(25, 0);
        set.visitFieldInsn(180, info.getName(), "values", "[Ljava/lang/Object;");
        for (byte index = 0; index < info.args.length; ++index) {
            set.visitVarInsn(21, 1);
            set.visitConstant(index);
            set.visitJumpInsn(159, labels[index]);
        }
        throwAIOOBE(set, 1);
        for (int index2 = 0; index2 < info.args.length; ++index2) {
            final String boxingType = Bytecode.getBoxingType(info.args[index2]);
            set.visitLabel(labels[index2]);
            set.visitVarInsn(21, 1);
            set.visitVarInsn(25, 2);
            set.visitTypeInsn(192, (boxingType != null) ? boxingType : info.args[index2].getInternalName());
            set.visitJumpInsn(167, (boxingType != null) ? checkNull : store);
        }
        set.visitLabel(checkNull);
        set.visitInsn(89);
        set.visitJumpInsn(199, store);
        throwNPE(set, "Argument with primitive type cannot be set to NULL");
        set.visitLabel(store);
        set.visitInsn(83);
        set.visitInsn(177);
        set.visitMaxs(6, 3);
        set.visitEnd();
    }
    
    private void generateMultiSetter(final ArgsClassInfo info, final ClassVisitor writer) {
        final MethodVisitorEx set = new MethodVisitorEx(writer.visitMethod(1, "setAll", "([Ljava/lang/Object;)V", null, null));
        set.visitCode();
        final Label lengthOk = new Label();
        final Label nullPrimitive = new Label();
        int maxStack = 6;
        set.visitVarInsn(25, 1);
        set.visitInsn(190);
        set.visitInsn(89);
        set.visitConstant((byte)info.args.length);
        set.visitJumpInsn(159, lengthOk);
        set.visitTypeInsn(187, "org/spongepowered/asm/mixin/injection/invoke/arg/ArgumentCountException");
        set.visitInsn(89);
        set.visitInsn(93);
        set.visitInsn(88);
        set.visitConstant((byte)info.args.length);
        set.visitLdcInsn(info.getSignature());
        set.visitMethodInsn(183, "org/spongepowered/asm/mixin/injection/invoke/arg/ArgumentCountException", "<init>", "(IILjava/lang/String;)V", false);
        set.visitInsn(191);
        set.visitLabel(lengthOk);
        set.visitInsn(87);
        set.visitVarInsn(25, 0);
        set.visitFieldInsn(180, info.getName(), "values", "[Ljava/lang/Object;");
        for (byte index = 0; index < info.args.length; ++index) {
            set.visitInsn(89);
            set.visitConstant(index);
            set.visitVarInsn(25, 1);
            set.visitConstant(index);
            set.visitInsn(50);
            final String boxingType = Bytecode.getBoxingType(info.args[index]);
            set.visitTypeInsn(192, (boxingType != null) ? boxingType : info.args[index].getInternalName());
            if (boxingType != null) {
                set.visitInsn(89);
                set.visitJumpInsn(198, nullPrimitive);
                maxStack = 7;
            }
            set.visitInsn(83);
        }
        set.visitInsn(177);
        set.visitLabel(nullPrimitive);
        throwNPE(set, "Argument with primitive type cannot be set to NULL");
        set.visitInsn(177);
        set.visitMaxs(maxStack, 2);
        set.visitEnd();
    }
    
    private static void throwNPE(final MethodVisitorEx method, final String message) {
        method.visitTypeInsn(187, "java/lang/NullPointerException");
        method.visitInsn(89);
        method.visitLdcInsn(message);
        method.visitMethodInsn(183, "java/lang/NullPointerException", "<init>", "(Ljava/lang/String;)V", false);
        method.visitInsn(191);
    }
    
    private static void throwAIOOBE(final MethodVisitorEx method, final int arg) {
        method.visitTypeInsn(187, "org/spongepowered/asm/mixin/injection/invoke/arg/ArgumentIndexOutOfBoundsException");
        method.visitInsn(89);
        method.visitVarInsn(21, arg);
        method.visitMethodInsn(183, "org/spongepowered/asm/mixin/injection/invoke/arg/ArgumentIndexOutOfBoundsException", "<init>", "(I)V", false);
        method.visitInsn(191);
    }
    
    private static void box(final MethodVisitor method, final Type var) {
        final String boxingType = Bytecode.getBoxingType(var);
        if (boxingType != null) {
            final String desc = String.format("(%s)L%s;", var.getDescriptor(), boxingType);
            method.visitMethodInsn(184, boxingType, "valueOf", desc, false);
        }
    }
    
    private static void unbox(final MethodVisitor method, final Type var) {
        final String boxingType = Bytecode.getBoxingType(var);
        if (boxingType != null) {
            final String unboxingMethod = Bytecode.getUnboxingMethod(var);
            final String desc = "()" + var.getDescriptor();
            method.visitTypeInsn(192, boxingType);
            method.visitMethodInsn(182, boxingType, unboxingMethod, desc, false);
        }
        else {
            method.visitTypeInsn(192, var.getInternalName());
        }
    }
    
    static {
        ARGS_NAME = Args.class.getName();
        ARGS_REF = ArgsClassGenerator.ARGS_NAME.replace('.', '/');
        logger = MixinService.getService().getLogger("mixin");
    }
    
    class ArgsClassInfo extends SyntheticClassInfo
    {
        final String desc;
        final Type[] args;
        int loaded;
        
        ArgsClassInfo(final IMixinInfo mixin, final String name, final String desc) {
            super(mixin, name);
            this.loaded = 0;
            this.desc = desc;
            this.args = Type.getArgumentTypes(desc);
        }
        
        @Override
        public boolean isLoaded() {
            return this.loaded > 0;
        }
        
        String getSignature() {
            return new SignaturePrinter("", null, this.args).setFullyQualified(true).getFormattedArgs();
        }
    }
}
