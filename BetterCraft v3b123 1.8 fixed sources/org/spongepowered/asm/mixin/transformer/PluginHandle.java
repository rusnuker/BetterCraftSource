// 
// Decompiled by Procyon v0.6.0
// 

package org.spongepowered.asm.mixin.transformer;

import org.spongepowered.asm.service.MixinService;
import java.lang.reflect.InvocationTargetException;
import org.spongepowered.asm.mixin.throwables.CompanionPluginError;
import org.spongepowered.asm.mixin.extensibility.IMixinInfo;
import org.objectweb.asm.tree.ClassNode;
import java.util.List;
import com.google.common.base.Strings;
import org.spongepowered.asm.service.IMixinService;
import java.lang.reflect.Method;
import org.spongepowered.asm.mixin.extensibility.IMixinConfigPlugin;
import org.spongepowered.asm.logging.ILogger;

class PluginHandle
{
    private static final ILogger logger;
    private final MixinConfig parent;
    private final IMixinConfigPlugin plugin;
    private CompatibilityMode mode;
    private Method mdPreApply;
    private Method mdPostApply;
    
    PluginHandle(final MixinConfig parent, final IMixinService service, final String pluginClassName) {
        this.mode = CompatibilityMode.NORMAL;
        IMixinConfigPlugin plugin = null;
        if (!Strings.isNullOrEmpty(pluginClassName)) {
            try {
                final Class<?> pluginClass = service.getClassProvider().findClass(pluginClassName, true);
                plugin = (IMixinConfigPlugin)pluginClass.getDeclaredConstructor((Class<?>[])new Class[0]).newInstance(new Object[0]);
            }
            catch (final Throwable th) {
                PluginHandle.logger.error("Error loading companion plugin class [{}] for mixin config [{}]. The plugin may be out of date: {}:{}", pluginClassName, parent, th.getClass().getSimpleName(), th.getMessage(), th);
                plugin = null;
            }
        }
        this.parent = parent;
        this.plugin = plugin;
    }
    
    IMixinConfigPlugin get() {
        return this.plugin;
    }
    
    boolean isAvailable() {
        return this.plugin != null;
    }
    
    void onLoad(final String mixinPackage) {
        if (this.plugin != null) {
            this.plugin.onLoad(mixinPackage);
        }
    }
    
    String getRefMapperConfig() {
        return (this.plugin != null) ? this.plugin.getRefMapperConfig() : null;
    }
    
    List<String> getMixins() {
        return (this.plugin != null) ? this.plugin.getMixins() : null;
    }
    
    boolean shouldApplyMixin(final String targetName, final String className) {
        return this.plugin == null || this.plugin.shouldApplyMixin(targetName, className);
    }
    
    public void preApply(final String targetClassName, final ClassNode targetClass, final String mixinClassName, final MixinInfo mixinInfo) throws Exception {
        if (this.plugin == null) {
            return;
        }
        if (this.mode == CompatibilityMode.FAILED) {
            throw new IllegalStateException("Companion plugin failure for [" + this.parent + "] plugin [" + this.plugin.getClass() + "]");
        }
        if (this.mode == CompatibilityMode.COMPATIBLE) {
            try {
                this.applyLegacy(this.mdPreApply, targetClassName, targetClass, mixinClassName, mixinInfo);
            }
            catch (final Exception ex) {
                this.mode = CompatibilityMode.FAILED;
                throw ex;
            }
            return;
        }
        try {
            this.plugin.preApply(targetClassName, targetClass, mixinClassName, mixinInfo);
        }
        catch (final AbstractMethodError ex2) {
            this.mode = CompatibilityMode.COMPATIBLE;
            this.initReflection();
            this.preApply(targetClassName, targetClass, mixinClassName, mixinInfo);
        }
    }
    
    public void postApply(final String targetClassName, final ClassNode targetClass, final String mixinClassName, final MixinInfo mixinInfo) throws Exception {
        if (this.plugin == null) {
            return;
        }
        if (this.mode == CompatibilityMode.FAILED) {
            throw new IllegalStateException("Companion plugin failure for [" + this.parent + "] plugin [" + this.plugin.getClass() + "]");
        }
        if (this.mode == CompatibilityMode.COMPATIBLE) {
            try {
                this.applyLegacy(this.mdPostApply, targetClassName, targetClass, mixinClassName, mixinInfo);
            }
            catch (final Exception ex) {
                this.mode = CompatibilityMode.FAILED;
                throw ex;
            }
            return;
        }
        try {
            this.plugin.postApply(targetClassName, targetClass, mixinClassName, mixinInfo);
        }
        catch (final AbstractMethodError ex2) {
            this.mode = CompatibilityMode.COMPATIBLE;
            this.initReflection();
            this.postApply(targetClassName, targetClass, mixinClassName, mixinInfo);
        }
    }
    
    private void initReflection() {
        if (this.mdPreApply != null) {
            return;
        }
        try {
            final Class<?> pluginClass = this.plugin.getClass();
            this.mdPreApply = pluginClass.getMethod("preApply", String.class, org.spongepowered.asm.lib.tree.ClassNode.class, String.class, IMixinInfo.class);
            this.mdPostApply = pluginClass.getMethod("postApply", String.class, org.spongepowered.asm.lib.tree.ClassNode.class, String.class, IMixinInfo.class);
        }
        catch (final Throwable th) {
            PluginHandle.logger.catching(th);
        }
    }
    
    private void applyLegacy(final Method method, final String targetClassName, final ClassNode targetClass, final String mixinClassName, final IMixinInfo mixinInfo) {
        try {
            method.invoke(this.plugin, targetClassName, new org.spongepowered.asm.lib.tree.ClassNode(targetClass), mixinClassName, mixinInfo);
        }
        catch (final LinkageError err) {
            throw new CompanionPluginError(this.apiError("Accessing [" + err.getMessage() + "]"), err);
        }
        catch (final IllegalAccessException ex) {
            throw new CompanionPluginError(this.apiError("Fallback failed [" + ex.getMessage() + "]"), ex);
        }
        catch (final IllegalArgumentException ex2) {
            throw new CompanionPluginError(this.apiError("Fallback failed [" + ex2.getMessage() + "]"), ex2);
        }
        catch (final InvocationTargetException ex3) {
            final Throwable th = (ex3.getCause() != null) ? ex3.getCause() : ex3;
            throw new CompanionPluginError(this.apiError("Fallback failed [" + th.getMessage() + "]"), th);
        }
    }
    
    private String apiError(final String message) {
        return String.format("Companion plugin attempted to use a deprected API in [%s] plugin [%s]: %s", this.parent, this.plugin.getClass().getName(), message);
    }
    
    static {
        logger = MixinService.getService().getLogger("mixin");
    }
    
    enum CompatibilityMode
    {
        NORMAL, 
        COMPATIBLE, 
        FAILED;
    }
}
