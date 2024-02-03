// 
// Decompiled by Procyon v0.6.0
// 

package org.spongepowered.asm.service.modlauncher;

import java.util.function.Consumer;
import org.spongepowered.asm.service.IMixinAuditTrail;

public class ModLauncherAuditTrail implements IMixinAuditTrail
{
    private static final String APPLY_MIXIN_ACTIVITY = "APP";
    private static final String POST_PROCESS_ACTIVITY = "DEC";
    private static final String GENERATE_ACTIVITY = "GEN";
    private String currentClass;
    private Consumer<String[]> consumer;
    
    public void setConsumer(final String className, final Consumer<String[]> consumer) {
        this.currentClass = className;
        this.consumer = consumer;
    }
    
    @Override
    public void onApply(final String className, final String mixinName) {
        this.writeActivity(className, "APP", mixinName);
    }
    
    @Override
    public void onPostProcess(final String className) {
        this.writeActivity(className, "DEC");
    }
    
    @Override
    public void onGenerate(final String className, final String generatorName) {
        this.writeActivity(className, "GEN");
    }
    
    private void writeActivity(final String className, final String... activity) {
        if (this.consumer != null && className.equals(this.currentClass)) {
            this.consumer.accept(activity);
        }
    }
}
