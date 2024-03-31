// 
// Decompiled by Procyon v0.6.0
// 

package org.spongepowered.asm.util;

import java.util.HashMap;
import org.objectweb.asm.tree.analysis.Frame;
import org.objectweb.asm.tree.analysis.BasicValue;
import org.objectweb.asm.tree.analysis.AnalyzerException;
import org.objectweb.asm.tree.analysis.Interpreter;
import org.objectweb.asm.tree.analysis.Analyzer;
import org.spongepowered.asm.util.asm.MixinVerifier;
import org.spongepowered.asm.util.asm.ASM;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.util.throwables.LVTGeneratorError;
import org.spongepowered.asm.mixin.transformer.ClassInfo;
import org.objectweb.asm.tree.FrameNode;
import org.objectweb.asm.tree.LineNumberNode;
import org.objectweb.asm.tree.LabelNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.VarInsnNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.LocalVariableNode;
import java.util.List;
import java.util.Map;

public final class Locals
{
    private static final String[] FRAME_TYPES;
    private static final Map<String, List<LocalVariableNode>> calculatedLocalVariables;
    
    private Locals() {
    }
    
    public static void loadLocals(final Type[] locals, final InsnList insns, int pos, int limit) {
        while (pos < locals.length && limit > 0) {
            if (locals[pos] != null) {
                insns.add(new VarInsnNode(locals[pos].getOpcode(21), pos));
                --limit;
            }
            ++pos;
        }
    }
    
    public static LocalVariableNode[] getLocalsAt(final ClassNode classNode, final MethodNode method, final AbstractInsnNode node) {
        return getLocalsAt(classNode, method, node, Settings.DEFAULT);
    }
    
    public static LocalVariableNode[] getLocalsAt(final ClassNode classNode, final MethodNode method, AbstractInsnNode node, final Settings settings) {
        AbstractInsnNode nextNode;
        for (int i = 0; i < 3 && (node instanceof LabelNode || node instanceof LineNumberNode); node = nextNode, ++i) {
            nextNode = nextNode(method.instructions, node);
            if (nextNode instanceof FrameNode) {
                break;
            }
        }
        final ClassInfo classInfo = ClassInfo.forName(classNode.name);
        if (classInfo == null) {
            throw new LVTGeneratorError("Could not load class metadata for " + classNode.name + " generating LVT for " + method.name);
        }
        final ClassInfo.Method methodInfo = classInfo.findMethod(method, method.access | 0x40000);
        if (methodInfo == null) {
            throw new LVTGeneratorError("Could not locate method metadata for " + method.name + " generating LVT in " + classNode.name);
        }
        final List<ClassInfo.FrameData> frames = methodInfo.getFrames();
        final LocalVariableNode[] frame = new LocalVariableNode[method.maxLocals];
        int local = 0;
        int index = 0;
        if ((method.access & 0x8) == 0x0) {
            frame[local++] = new LocalVariableNode("this", Type.getObjectType(classNode.name).toString(), null, null, null, 0);
        }
        for (final Type argType : Type.getArgumentTypes(method.desc)) {
            frame[local] = new LocalVariableNode("arg" + index++, argType.toString(), null, null, null, local);
            local += argType.getSize();
        }
        final int initialFrameSize = local;
        int frameSize = local;
        int frameIndex = -1;
        int lastFrameSize = local;
        int knownFrameSize = local;
        VarInsnNode storeInsn = null;
        for (final AbstractInsnNode insn : method.instructions) {
            for (int l = 0; l < frame.length; ++l) {
                if (frame[l] instanceof ZombieLocalVariableNode) {
                    final ZombieLocalVariableNode zombieLocalVariableNode;
                    final ZombieLocalVariableNode zombie = zombieLocalVariableNode = (ZombieLocalVariableNode)frame[l];
                    ++zombieLocalVariableNode.lifetime;
                    if (insn instanceof FrameNode) {
                        final ZombieLocalVariableNode zombieLocalVariableNode2 = zombie;
                        ++zombieLocalVariableNode2.frames;
                    }
                }
            }
            if (storeInsn != null) {
                final LocalVariableNode storedLocal = getLocalVariableAt(classNode, method, insn, storeInsn.var);
                frame[storeInsn.var] = storedLocal;
                knownFrameSize = Math.max(knownFrameSize, storeInsn.var + 1);
                if (storedLocal != null && storeInsn.var < method.maxLocals - 1 && storedLocal.desc != null && Type.getType(storedLocal.desc).getSize() == 2) {
                    frame[storeInsn.var + 1] = null;
                    knownFrameSize = Math.max(knownFrameSize, storeInsn.var + 2);
                    if (settings.hasFlags(Settings.RESURRECT_EXPOSED_ON_STORE)) {
                        resurrect(frame, knownFrameSize, settings);
                    }
                }
                storeInsn = null;
            }
            if (insn instanceof FrameNode) {
                ++frameIndex;
                final FrameNode frameNode = (FrameNode)insn;
                if (frameNode.type != 3) {
                    if (frameNode.type != 4) {
                        final int frameNodeSize = computeFrameSize(frameNode, initialFrameSize);
                        final ClassInfo.FrameData frameData = (frameIndex < frames.size()) ? frames.get(frameIndex) : null;
                        if (frameData != null) {
                            if (frameData.type == 0) {
                                lastFrameSize = (knownFrameSize = (frameSize = Math.max(initialFrameSize, Math.min(frameNodeSize, frameData.size))));
                            }
                            else {
                                frameSize = getAdjustedFrameSize(frameSize, frameData, initialFrameSize);
                            }
                        }
                        else {
                            frameSize = getAdjustedFrameSize(frameSize, frameNode, initialFrameSize);
                        }
                        if (frameSize < initialFrameSize) {
                            throw new IllegalStateException(String.format("Locals entered an invalid state evaluating %s::%s%s at instruction %d (%s). Initial frame size is %d, calculated a frame size of %d with %s", classNode.name, method.name, method.desc, method.instructions.indexOf(insn), Bytecode.describeNode(insn, false), initialFrameSize, frameSize, frameData));
                        }
                        if ((frameData == null && (frameNode.type == 2 || frameNode.type == -1)) || (frameData != null && frameData.type == 2)) {
                            for (int framePos = frameSize; framePos < frame.length; ++framePos) {
                                frame[framePos] = ZombieLocalVariableNode.of(frame[framePos], 'C');
                            }
                            lastFrameSize = (knownFrameSize = frameSize);
                        }
                        else {
                            int framePos = (frameNode.type == 1) ? lastFrameSize : 0;
                            lastFrameSize = frameSize;
                            for (int localPos = 0; framePos < frame.length; ++framePos, ++localPos) {
                                final Object localType = (localPos < frameNode.local.size()) ? frameNode.local.get(localPos) : null;
                                if (localType instanceof String) {
                                    frame[framePos] = getLocalVariableAt(classNode, method, insn, framePos);
                                }
                                else if (localType instanceof Integer) {
                                    final boolean isMarkerType = localType == Opcodes.UNINITIALIZED_THIS || localType == Opcodes.NULL;
                                    final boolean is32bitValue = localType == Opcodes.INTEGER || localType == Opcodes.FLOAT;
                                    final boolean is64bitValue = localType == Opcodes.DOUBLE || localType == Opcodes.LONG;
                                    if (localType == Opcodes.TOP) {
                                        if (frame[framePos] instanceof ZombieLocalVariableNode && settings.hasFlags(Settings.RESURRECT_FOR_BOGUS_TOP)) {
                                            final ZombieLocalVariableNode zombie2 = (ZombieLocalVariableNode)frame[framePos];
                                            if (zombie2.type == 'X') {
                                                frame[framePos] = zombie2.ancestor;
                                            }
                                        }
                                    }
                                    else if (isMarkerType) {
                                        frame[framePos] = null;
                                    }
                                    else {
                                        if (!is32bitValue && !is64bitValue) {
                                            throw new LVTGeneratorError("Unrecognised locals opcode " + localType + " in locals array at position " + localPos + " in " + classNode.name + "." + method.name + method.desc);
                                        }
                                        frame[framePos] = getLocalVariableAt(classNode, method, insn, framePos);
                                        if (is64bitValue) {
                                            ++framePos;
                                            frame[framePos] = null;
                                        }
                                    }
                                }
                                else if (localType == null) {
                                    if (framePos >= initialFrameSize && framePos >= frameSize && frameSize > 0) {
                                        if (framePos < knownFrameSize) {
                                            frame[framePos] = getLocalVariableAt(classNode, method, insn, framePos);
                                        }
                                        else {
                                            frame[framePos] = ZombieLocalVariableNode.of(frame[framePos], 'X');
                                        }
                                    }
                                }
                                else if (!(localType instanceof LabelNode)) {
                                    throw new LVTGeneratorError("Invalid value " + localType + " in locals array at position " + localPos + " in " + classNode.name + "." + method.name + method.desc);
                                }
                            }
                        }
                    }
                }
            }
            else if (insn instanceof VarInsnNode) {
                final VarInsnNode varInsn = (VarInsnNode)insn;
                final boolean isLoad = insn.getOpcode() >= 21 && insn.getOpcode() <= 53;
                if (isLoad) {
                    frame[varInsn.var] = getLocalVariableAt(classNode, method, insn, varInsn.var);
                    final int varSize = (frame[varInsn.var].desc != null) ? Type.getType(frame[varInsn.var].desc).getSize() : 1;
                    knownFrameSize = Math.max(knownFrameSize, varInsn.var + varSize);
                    if (settings.hasFlags(Settings.RESURRECT_EXPOSED_ON_LOAD)) {
                        resurrect(frame, knownFrameSize, settings);
                    }
                }
                else {
                    storeInsn = varInsn;
                }
            }
            if (insn == node) {
                break;
            }
        }
        for (int j = 0; j < frame.length; ++j) {
            if (frame[j] instanceof ZombieLocalVariableNode) {
                final ZombieLocalVariableNode zombie3 = (ZombieLocalVariableNode)frame[j];
                frame[j] = ((zombie3.lifetime > 1) ? null : zombie3.ancestor);
            }
            if ((frame[j] != null && frame[j].desc == null) || frame[j] instanceof SyntheticLocalVariableNode) {
                frame[j] = null;
            }
        }
        return frame;
    }
    
    private static void resurrect(final LocalVariableNode[] frame, final int knownFrameSize, final Settings settings) {
        for (int l = 0; l < knownFrameSize && l < frame.length; ++l) {
            if (frame[l] instanceof ZombieLocalVariableNode) {
                final ZombieLocalVariableNode zombie = (ZombieLocalVariableNode)frame[l];
                if (zombie.checkResurrect(settings)) {
                    frame[l] = zombie.ancestor;
                }
            }
        }
    }
    
    public static LocalVariableNode getLocalVariableAt(final ClassNode classNode, final MethodNode method, final AbstractInsnNode node, final int var) {
        return getLocalVariableAt(classNode, method, method.instructions.indexOf(node), var);
    }
    
    private static LocalVariableNode getLocalVariableAt(final ClassNode classNode, final MethodNode method, final int pos, final int var) {
        LocalVariableNode localVariableNode = null;
        LocalVariableNode fallbackNode = null;
        for (final LocalVariableNode local : getLocalVariableTable(classNode, method)) {
            if (local.index != var) {
                continue;
            }
            if (isOpcodeInRange(method.instructions, local, pos)) {
                localVariableNode = local;
            }
            else {
                if (localVariableNode != null) {
                    continue;
                }
                fallbackNode = local;
            }
        }
        if (localVariableNode == null && !method.localVariables.isEmpty()) {
            for (final LocalVariableNode local : getGeneratedLocalVariableTable(classNode, method)) {
                if (local.index == var && isOpcodeInRange(method.instructions, local, pos)) {
                    localVariableNode = local;
                }
            }
        }
        return (localVariableNode != null) ? localVariableNode : fallbackNode;
    }
    
    private static boolean isOpcodeInRange(final InsnList insns, final LocalVariableNode local, final int pos) {
        return insns.indexOf(local.start) <= pos && insns.indexOf(local.end) > pos;
    }
    
    public static List<LocalVariableNode> getLocalVariableTable(final ClassNode classNode, final MethodNode method) {
        if (method.localVariables.isEmpty()) {
            return getGeneratedLocalVariableTable(classNode, method);
        }
        return Collections.unmodifiableList((List<? extends LocalVariableNode>)method.localVariables);
    }
    
    public static List<LocalVariableNode> getGeneratedLocalVariableTable(final ClassNode classNode, final MethodNode method) {
        final String methodId = String.format("%s.%s%s", classNode.name, method.name, method.desc);
        List<LocalVariableNode> localVars = Locals.calculatedLocalVariables.get(methodId);
        if (localVars != null) {
            return localVars;
        }
        localVars = generateLocalVariableTable(classNode, method);
        Locals.calculatedLocalVariables.put(methodId, localVars);
        return Collections.unmodifiableList((List<? extends LocalVariableNode>)localVars);
    }
    
    public static List<LocalVariableNode> generateLocalVariableTable(final ClassNode classNode, final MethodNode method) {
        List<Type> interfaces = null;
        if (classNode.interfaces != null) {
            interfaces = new ArrayList<Type>();
            for (final String iface : classNode.interfaces) {
                interfaces.add(Type.getObjectType(iface));
            }
        }
        Type objectType = null;
        if (classNode.superName != null) {
            objectType = Type.getObjectType(classNode.superName);
        }
        final Analyzer<BasicValue> analyzer = (Analyzer<BasicValue>)new Analyzer(new MixinVerifier(ASM.API_VERSION, Type.getObjectType(classNode.name), objectType, interfaces, false));
        try {
            analyzer.analyze(classNode.name, method);
        }
        catch (final AnalyzerException ex) {
            ex.printStackTrace();
        }
        final Frame<BasicValue>[] frames = (Frame<BasicValue>[])analyzer.getFrames();
        final int methodSize = method.instructions.size();
        final List<LocalVariableNode> localVariables = new ArrayList<LocalVariableNode>();
        final LocalVariableNode[] localNodes = new LocalVariableNode[method.maxLocals];
        final BasicValue[] locals = new BasicValue[method.maxLocals];
        final LabelNode[] labels = new LabelNode[methodSize];
        final String[] lastKnownType = new String[method.maxLocals];
        for (int i = 0; i < methodSize; ++i) {
            final Frame<BasicValue> f = frames[i];
            if (f != null) {
                LabelNode label = null;
                for (int j = 0; j < f.getLocals(); ++j) {
                    final BasicValue local = (BasicValue)f.getLocal(j);
                    if (local != null || locals[j] != null) {
                        if (local == null || !local.equals(locals[j])) {
                            if (label == null) {
                                final AbstractInsnNode existingLabel = method.instructions.get(i);
                                if (existingLabel instanceof LabelNode) {
                                    label = (LabelNode)existingLabel;
                                }
                                else {
                                    label = (labels[i] = new LabelNode());
                                }
                            }
                            if (local == null && locals[j] != null) {
                                localVariables.add(localNodes[j]);
                                localNodes[j].end = label;
                                localNodes[j] = null;
                            }
                            else if (local != null) {
                                if (locals[j] != null) {
                                    localVariables.add(localNodes[j]);
                                    localNodes[j].end = label;
                                    localNodes[j] = null;
                                }
                                String desc = lastKnownType[j];
                                final Type localType = local.getType();
                                if (localType != null) {
                                    desc = ((localType.getSort() >= 9 && "null".equals(localType.getInternalName())) ? "Ljava/lang/Object;" : localType.getDescriptor());
                                }
                                localNodes[j] = new LocalVariableNode("var" + j, desc, null, label, null, j);
                                if (desc != null) {
                                    lastKnownType[j] = desc;
                                }
                            }
                            locals[j] = local;
                        }
                    }
                }
            }
        }
        LabelNode label2 = null;
        for (int k = 0; k < localNodes.length; ++k) {
            if (localNodes[k] != null) {
                if (label2 == null) {
                    label2 = new LabelNode();
                    method.instructions.add(label2);
                }
                localNodes[k].end = label2;
                localVariables.add(localNodes[k]);
            }
        }
        for (int n = methodSize - 1; n >= 0; --n) {
            if (labels[n] != null) {
                method.instructions.insert(method.instructions.get(n), labels[n]);
            }
        }
        return localVariables;
    }
    
    private static AbstractInsnNode nextNode(final InsnList insns, final AbstractInsnNode insn) {
        final int index = insns.indexOf(insn) + 1;
        if (index > 0 && index < insns.size()) {
            return insns.get(index);
        }
        return insn;
    }
    
    private static int getAdjustedFrameSize(final int currentSize, final FrameNode frameNode, final int initialFrameSize) {
        return getAdjustedFrameSize(currentSize, frameNode.type, computeFrameSize(frameNode, initialFrameSize), initialFrameSize);
    }
    
    private static int getAdjustedFrameSize(final int currentSize, final ClassInfo.FrameData frameData, final int initialFrameSize) {
        return getAdjustedFrameSize(currentSize, frameData.type, frameData.size, initialFrameSize);
    }
    
    private static int getAdjustedFrameSize(final int currentSize, final int type, final int size, final int initialFrameSize) {
        switch (type) {
            case -1:
            case 0: {
                return Math.max(initialFrameSize, size);
            }
            case 1: {
                return currentSize + size;
            }
            case 2: {
                return Math.max(initialFrameSize, currentSize - size);
            }
            case 3:
            case 4: {
                return currentSize;
            }
            default: {
                return currentSize;
            }
        }
    }
    
    public static int computeFrameSize(final FrameNode frameNode, final int initialFrameSize) {
        if (frameNode.local == null) {
            return initialFrameSize;
        }
        int size = 0;
        for (final Object local : frameNode.local) {
            if (local instanceof Integer) {
                size += ((local == Opcodes.DOUBLE || local == Opcodes.LONG) ? 2 : 1);
            }
            else {
                ++size;
            }
        }
        return Math.max(initialFrameSize, size);
    }
    
    public static String getFrameTypeName(final Object frameEntry) {
        if (frameEntry == null) {
            return "NULL";
        }
        if (frameEntry instanceof String) {
            return Bytecode.getSimpleName(frameEntry.toString());
        }
        if (!(frameEntry instanceof Integer)) {
            return "?";
        }
        final int type = (int)frameEntry;
        if (type >= Locals.FRAME_TYPES.length) {
            return "INVALID";
        }
        return Locals.FRAME_TYPES[type];
    }
    
    static {
        FRAME_TYPES = new String[] { "TOP", "INTEGER", "FLOAT", "DOUBLE", "LONG", "NULL", "UNINITIALIZED_THIS" };
        calculatedLocalVariables = new HashMap<String, List<LocalVariableNode>>();
    }
    
    public static class SyntheticLocalVariableNode extends LocalVariableNode
    {
        public SyntheticLocalVariableNode(final String name, final String descriptor, final String signature, final LabelNode start, final LabelNode end, final int index) {
            super(name, descriptor, signature, start, end, index);
        }
    }
    
    static class ZombieLocalVariableNode extends LocalVariableNode
    {
        static final char CHOP = 'C';
        static final char TRIM = 'X';
        final LocalVariableNode ancestor;
        final char type;
        int lifetime;
        int frames;
        
        ZombieLocalVariableNode(final LocalVariableNode ancestor, final char type) {
            super(ancestor.name, ancestor.desc, ancestor.signature, ancestor.start, ancestor.end, ancestor.index);
            this.ancestor = ancestor;
            this.type = type;
        }
        
        boolean checkResurrect(final Settings settings) {
            final int insnThreshold = (this.type == 'C') ? settings.choppedInsnThreshold : settings.trimmedInsnThreshold;
            if (insnThreshold > -1 && this.lifetime > insnThreshold) {
                return false;
            }
            final int frameThreshold = (this.type == 'C') ? settings.choppedFrameThreshold : settings.trimmedFrameThreshold;
            return frameThreshold == -1 || this.frames <= frameThreshold;
        }
        
        static ZombieLocalVariableNode of(final LocalVariableNode ancestor, final char type) {
            if (ancestor instanceof ZombieLocalVariableNode) {
                return (ZombieLocalVariableNode)ancestor;
            }
            return (ancestor != null) ? new ZombieLocalVariableNode(ancestor, type) : null;
        }
        
        @Override
        public String toString() {
            return String.format("Z(%s,%-2d)", this.type, this.lifetime);
        }
    }
    
    public static class Settings
    {
        public static int RESURRECT_FOR_BOGUS_TOP;
        public static int RESURRECT_EXPOSED_ON_LOAD;
        public static int RESURRECT_EXPOSED_ON_STORE;
        public static int DEFAULT_FLAGS;
        public static Settings DEFAULT;
        final int flags;
        final int flagsCustom;
        final int choppedInsnThreshold;
        final int choppedFrameThreshold;
        final int trimmedInsnThreshold;
        final int trimmedFrameThreshold;
        
        public Settings(final int flags, final int flagsCustom, final int insnThreshold, final int frameThreshold) {
            this(flags, flagsCustom, insnThreshold, frameThreshold, insnThreshold, frameThreshold);
        }
        
        public Settings(final int flags, final int flagsCustom, final int choppedInsnThreshold, final int choppedFrameThreshold, final int trimmedInsnThreshold, final int trimmedFrameThreshold) {
            this.flags = flags;
            this.flagsCustom = flagsCustom;
            this.choppedInsnThreshold = choppedInsnThreshold;
            this.choppedFrameThreshold = choppedFrameThreshold;
            this.trimmedInsnThreshold = trimmedInsnThreshold;
            this.trimmedFrameThreshold = trimmedFrameThreshold;
        }
        
        boolean hasFlags(final int flags) {
            return (this.flags & flags) == flags;
        }
        
        boolean hasCustomFlags(final int flagsCustom) {
            return (this.flagsCustom & flagsCustom) == flagsCustom;
        }
        
        static {
            Settings.RESURRECT_FOR_BOGUS_TOP = 1;
            Settings.RESURRECT_EXPOSED_ON_LOAD = 2;
            Settings.RESURRECT_EXPOSED_ON_STORE = 4;
            Settings.DEFAULT_FLAGS = (Settings.RESURRECT_FOR_BOGUS_TOP | Settings.RESURRECT_EXPOSED_ON_LOAD | Settings.RESURRECT_EXPOSED_ON_STORE);
            Settings.DEFAULT = new Settings(Settings.DEFAULT_FLAGS, 0, -1, 1, -1, -1);
        }
    }
}
