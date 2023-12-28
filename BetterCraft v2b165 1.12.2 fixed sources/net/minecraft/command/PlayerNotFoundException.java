// 
// Decompiled by Procyon v0.6.0
// 

package net.minecraft.command;

public class PlayerNotFoundException extends CommandException
{
    public PlayerNotFoundException(final String p_i47330_1_) {
        super(p_i47330_1_, new Object[0]);
    }
    
    public PlayerNotFoundException(final String message, final Object... replacements) {
        super(message, replacements);
    }
    
    @Override
    public synchronized Throwable fillInStackTrace() {
        return this;
    }
}
