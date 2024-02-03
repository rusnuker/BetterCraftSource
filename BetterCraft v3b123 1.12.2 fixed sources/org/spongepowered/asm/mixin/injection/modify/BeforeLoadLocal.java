// 
// Decompiled by Procyon v0.6.0
// 

package org.spongepowered.asm.mixin.injection.modify;

import java.util.ListIterator;
import org.objectweb.asm.tree.VarInsnNode;
import org.spongepowered.asm.mixin.injection.struct.Target;
import org.objectweb.asm.tree.AbstractInsnNode;
import java.util.Collection;
import org.objectweb.asm.tree.InsnList;
import org.spongepowered.asm.mixin.injection.struct.InjectionInfo;
import org.spongepowered.asm.mixin.injection.struct.InjectionPointData;
import org.objectweb.asm.Type;
import org.spongepowered.asm.mixin.injection.InjectionPoint;

@AtCode("LOAD")
public class BeforeLoadLocal extends ModifyVariableInjector.LocalVariableInjectionPoint
{
    protected final Type returnType;
    protected final LocalVariableDiscriminator discriminator;
    protected final int opcode;
    protected final int ordinal;
    private boolean opcodeAfter;
    
    protected BeforeLoadLocal(final InjectionPointData data) {
        this(data, 21, false);
    }
    
    protected BeforeLoadLocal(final InjectionPointData data, final int opcode, final boolean opcodeAfter) {
        super(data);
        this.returnType = data.getMethodReturnType();
        this.discriminator = data.getLocalVariableDiscriminator();
        this.opcode = data.getOpcode(this.returnType.getOpcode(opcode));
        this.ordinal = data.getOrdinal();
        this.opcodeAfter = opcodeAfter;
    }
    
    @Override
    boolean find(final InjectionInfo info, final InsnList insns, final Collection<AbstractInsnNode> nodes, final Target target) {
        final SearchState state = new SearchState();
        for (final AbstractInsnNode insn : insns) {
            if (state.isPendingCheck()) {
                state.check(info, target, nodes, insn);
            }
            else {
                if (!(insn instanceof VarInsnNode) || insn.getOpcode() != this.opcode || (this.ordinal != -1 && state.success())) {
                    continue;
                }
                state.register((VarInsnNode)insn);
                if (this.opcodeAfter) {
                    state.setPendingCheck();
                }
                else {
                    state.check(info, target, nodes, insn);
                }
            }
        }
        return state.success();
    }
    
    @Override
    protected void addMessage(final String format, final Object... args) {
        super.addMessage(format, args);
    }
    
    @Override
    public String toString() {
        return String.format("@At(\"%s\" %s)", this.getAtCode(), this.discriminator.toString());
    }
    
    public String toString(final LocalVariableDiscriminator.Context context) {
        return String.format("@At(\"%s\" %s)", this.getAtCode(), this.discriminator.toString(context));
    }
    
    class SearchState
    {
        private static final int INVALID_IMPLICIT = -2;
        private final boolean print;
        private int currentOrdinal;
        private boolean pendingCheck;
        private boolean found;
        private VarInsnNode varNode;
        
        SearchState() {
            this.currentOrdinal = 0;
            this.pendingCheck = false;
            this.found = false;
            this.print = BeforeLoadLocal.this.discriminator.printLVT();
        }
        
        boolean success() {
            return this.found;
        }
        
        boolean isPendingCheck() {
            return this.pendingCheck;
        }
        
        void setPendingCheck() {
            this.pendingCheck = true;
        }
        
        void register(final VarInsnNode node) {
            this.varNode = node;
        }
        
        void check(final InjectionInfo info, final Target target, final Collection<AbstractInsnNode> nodes, final AbstractInsnNode insn) {
            final LocalVariableDiscriminator.Context context = new LocalVariableDiscriminator.Context(info, BeforeLoadLocal.this.returnType, BeforeLoadLocal.this.discriminator.isArgsOnly(), target, insn);
            int local = -2;
            try {
                local = BeforeLoadLocal.this.discriminator.findLocal(context);
            }
            catch (final InvalidImplicitDiscriminatorException ex) {
                BeforeLoadLocal.this.addMessage("%s has invalid IMPLICIT discriminator for opcode %d in %s: %s", BeforeLoadLocal.this.toString(context), target.indexOf(insn), target, ex.getMessage());
            }
            this.pendingCheck = false;
            if (local != this.varNode.var && (local > -2 || !this.print)) {
                this.varNode = null;
                return;
            }
            if (BeforeLoadLocal.this.ordinal == -1 || BeforeLoadLocal.this.ordinal == this.currentOrdinal) {
                nodes.add(insn);
                this.found = true;
            }
            ++this.currentOrdinal;
            this.varNode = null;
        }
    }
}
