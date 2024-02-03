// 
// Decompiled by Procyon v0.6.0
// 

package org.spongepowered.asm.mixin.struct;

import org.spongepowered.asm.mixin.transformer.throwables.MixinTransformerError;
import org.spongepowered.asm.util.Handles;
import org.objectweb.asm.Handle;
import org.objectweb.asm.tree.FieldInsnNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.spongepowered.asm.util.Bytecode;

public abstract class MemberRef
{
    public abstract boolean isField();
    
    public abstract int getOpcode();
    
    public abstract void setOpcode(final int p0);
    
    public abstract String getOwner();
    
    public abstract void setOwner(final String p0);
    
    public abstract String getName();
    
    public abstract void setName(final String p0);
    
    public abstract String getDesc();
    
    public abstract void setDesc(final String p0);
    
    @Override
    public String toString() {
        return String.format("%s for %s.%s%s%s", Bytecode.getOpcodeName(this.getOpcode()), this.getOwner(), this.getName(), this.isField() ? ":" : "", this.getDesc());
    }
    
    @Override
    public boolean equals(final Object obj) {
        if (!(obj instanceof MemberRef)) {
            return false;
        }
        final MemberRef other = (MemberRef)obj;
        return this.getOpcode() == other.getOpcode() && this.getOwner().equals(other.getOwner()) && this.getName().equals(other.getName()) && this.getDesc().equals(other.getDesc());
    }
    
    @Override
    public int hashCode() {
        return this.toString().hashCode();
    }
    
    public static final class Method extends MemberRef
    {
        private static final int OPCODES = 191;
        public final MethodInsnNode insn;
        
        public Method(final MethodInsnNode insn) {
            this.insn = insn;
        }
        
        @Override
        public boolean isField() {
            return false;
        }
        
        @Override
        public int getOpcode() {
            return this.insn.getOpcode();
        }
        
        @Override
        public void setOpcode(final int opcode) {
            if ((opcode & 0xBF) == 0x0) {
                throw new IllegalArgumentException("Invalid opcode for method instruction: 0x" + Integer.toHexString(opcode));
            }
            this.insn.setOpcode(opcode);
        }
        
        @Override
        public String getOwner() {
            return this.insn.owner;
        }
        
        @Override
        public void setOwner(final String owner) {
            this.insn.owner = owner;
        }
        
        @Override
        public String getName() {
            return this.insn.name;
        }
        
        @Override
        public void setName(final String name) {
            this.insn.name = name;
        }
        
        @Override
        public String getDesc() {
            return this.insn.desc;
        }
        
        @Override
        public void setDesc(final String desc) {
            this.insn.desc = desc;
        }
    }
    
    public static final class Field extends MemberRef
    {
        private static final int OPCODES = 183;
        public final FieldInsnNode insn;
        
        public Field(final FieldInsnNode insn) {
            this.insn = insn;
        }
        
        @Override
        public boolean isField() {
            return true;
        }
        
        @Override
        public int getOpcode() {
            return this.insn.getOpcode();
        }
        
        @Override
        public void setOpcode(final int opcode) {
            if ((opcode & 0xB7) == 0x0) {
                throw new IllegalArgumentException("Invalid opcode for field instruction: 0x" + Integer.toHexString(opcode));
            }
            this.insn.setOpcode(opcode);
        }
        
        @Override
        public String getOwner() {
            return this.insn.owner;
        }
        
        @Override
        public void setOwner(final String owner) {
            this.insn.owner = owner;
        }
        
        @Override
        public String getName() {
            return this.insn.name;
        }
        
        @Override
        public void setName(final String name) {
            this.insn.name = name;
        }
        
        @Override
        public String getDesc() {
            return this.insn.desc;
        }
        
        @Override
        public void setDesc(final String desc) {
            this.insn.desc = desc;
        }
    }
    
    public static final class Handle extends MemberRef
    {
        private org.objectweb.asm.Handle handle;
        
        public Handle(final org.objectweb.asm.Handle handle) {
            this.handle = handle;
        }
        
        public org.objectweb.asm.Handle getMethodHandle() {
            return this.handle;
        }
        
        @Override
        public boolean isField() {
            return Handles.isField(this.handle);
        }
        
        @Override
        public int getOpcode() {
            final int opcode = Handles.opcodeFromTag(this.handle.getTag());
            if (opcode == 0) {
                throw new MixinTransformerError("Invalid tag " + this.handle.getTag() + " for method handle " + this.handle + ".");
            }
            return opcode;
        }
        
        @Override
        public void setOpcode(final int opcode) {
            final int tag = Handles.tagFromOpcode(opcode);
            if (tag == 0) {
                throw new MixinTransformerError("Invalid opcode " + Bytecode.getOpcodeName(opcode) + " for method handle " + this.handle + ".");
            }
            this.setHandle(tag, this.handle.getOwner(), this.handle.getName(), this.handle.getDesc(), this.handle.isInterface());
        }
        
        @Override
        public String getOwner() {
            return this.handle.getOwner();
        }
        
        @Override
        public void setOwner(final String owner) {
            this.setHandle(this.handle.getTag(), owner, this.handle.getName(), this.handle.getDesc(), this.handle.isInterface());
        }
        
        @Override
        public String getName() {
            return this.handle.getName();
        }
        
        @Override
        public void setName(final String name) {
            this.setHandle(this.handle.getTag(), this.handle.getOwner(), name, this.handle.getDesc(), this.handle.isInterface());
        }
        
        @Override
        public String getDesc() {
            return this.handle.getDesc();
        }
        
        @Override
        public void setDesc(final String desc) {
            this.setHandle(this.handle.getTag(), this.handle.getOwner(), this.handle.getName(), desc, this.handle.isInterface());
        }
        
        public void setHandle(final int tag, final String owner, final String name, final String desc, final boolean isInterface) {
            this.handle = new org.objectweb.asm.Handle(tag, owner, name, desc, isInterface);
        }
    }
}
