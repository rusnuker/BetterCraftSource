// 
// Decompiled by Procyon v0.6.0
// 

package net.minecraft.command;

public class WrongUsageException extends SyntaxErrorException
{
    public WrongUsageException(final String message, final Object... replacements) {
        super(message, replacements);
    }
    
    @Override
    public synchronized Throwable fillInStackTrace() {
        return this;
    }
}
