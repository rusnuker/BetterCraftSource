// 
// Decompiled by Procyon v0.6.0
// 

package me.amkgre.bettercraft.client.mods.thealtening.api.utils.exceptions;

public class TheAlteningException extends RuntimeException
{
    public TheAlteningException(final String error, final String errorMessage) {
        super(String.format("[%s]: %s", error, errorMessage));
    }
}
