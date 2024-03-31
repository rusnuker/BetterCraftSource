// 
// Decompiled by Procyon v0.6.0
// 

package me.amkgre.bettercraft.client.mods.teamspeak.event;

import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Retention;
import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

@Target({ ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
public @interface EventHandler {
    Priority priority() default Priority.NORMAL;
    
    public enum Priority
    {
        LOW("LOW", 0), 
        NORMAL("NORMAL", 1), 
        HIGH("HIGH", 2);
        
        private Priority(final String s, final int n) {
        }
    }
}
