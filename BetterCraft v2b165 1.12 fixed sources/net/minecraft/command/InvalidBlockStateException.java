// 
// Decompiled by Procyon v0.6.0
// 

package net.minecraft.command;

public class InvalidBlockStateException extends CommandException
{
    public InvalidBlockStateException() {
        this("commands.generic.blockstate.invalid", new Object[0]);
    }
    
    public InvalidBlockStateException(final String p_i47331_1_, final Object... p_i47331_2_) {
        super(p_i47331_1_, p_i47331_2_);
    }
    
    @Override
    public synchronized Throwable fillInStackTrace() {
        return this;
    }
}
