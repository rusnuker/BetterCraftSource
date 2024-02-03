// 
// Decompiled by Procyon v0.6.0
// 

package org.spongepowered.asm.mixin.transformer.ext.extensions;

import org.spongepowered.asm.service.MixinService;
import org.objectweb.asm.ClassVisitor;
import org.spongepowered.asm.transformers.MixinClassWriter;
import com.google.common.io.Files;
import org.spongepowered.asm.util.perf.Profiler;
import org.objectweb.asm.tree.ClassNode;
import org.spongepowered.asm.mixin.transformer.ext.ITargetClassContext;
import java.util.regex.Pattern;
import java.lang.reflect.Constructor;
import java.io.IOException;
import com.google.common.io.MoreFiles;
import com.google.common.io.RecursiveDeleteOption;
import org.spongepowered.asm.util.Constants;
import org.spongepowered.asm.mixin.MixinEnvironment;
import org.spongepowered.asm.mixin.transformer.ext.IDecompiler;
import java.io.File;
import org.spongepowered.asm.logging.ILogger;
import org.spongepowered.asm.mixin.transformer.ext.IExtension;

public class ExtensionClassExporter implements IExtension
{
    private static final String DECOMPILER_CLASS = "org.spongepowered.asm.mixin.transformer.debug.RuntimeDecompiler";
    private static final String EXPORT_CLASS_DIR = "class";
    private static final String EXPORT_JAVA_DIR = "java";
    private static final ILogger logger;
    private final File classExportDir;
    private final IDecompiler decompiler;
    
    public ExtensionClassExporter(final MixinEnvironment env) {
        this.classExportDir = new File(Constants.DEBUG_OUTPUT_DIR, "class");
        this.decompiler = this.initDecompiler(env, new File(Constants.DEBUG_OUTPUT_DIR, "java"));
        try {
            MoreFiles.deleteRecursively(this.classExportDir.toPath(), new RecursiveDeleteOption[] { RecursiveDeleteOption.ALLOW_INSECURE });
        }
        catch (final IOException ex) {
            ExtensionClassExporter.logger.debug("Error cleaning class output directory: {}", ex.getMessage());
        }
    }
    
    public boolean isDecompilerActive() {
        return this.decompiler != null;
    }
    
    private IDecompiler initDecompiler(final MixinEnvironment env, final File outputPath) {
        if (!env.getOption(MixinEnvironment.Option.DEBUG_EXPORT_DECOMPILE)) {
            return null;
        }
        try {
            final boolean as = env.getOption(MixinEnvironment.Option.DEBUG_EXPORT_DECOMPILE_THREADED);
            ExtensionClassExporter.logger.info("Attempting to load Fernflower decompiler{}", as ? " (Threaded mode)" : "");
            final String className = "org.spongepowered.asm.mixin.transformer.debug.RuntimeDecompiler" + (as ? "Async" : "");
            final Class<? extends IDecompiler> clazz = (Class<? extends IDecompiler>)Class.forName(className);
            final Constructor<? extends IDecompiler> ctor = clazz.getDeclaredConstructor(File.class);
            final IDecompiler decompiler = (IDecompiler)ctor.newInstance(outputPath);
            ExtensionClassExporter.logger.info("Fernflower decompiler was successfully initialised from {}, exported classes will be decompiled{}", decompiler, as ? " in a separate thread" : "");
            return decompiler;
        }
        catch (final Throwable th) {
            ExtensionClassExporter.logger.info("Fernflower could not be loaded, exported classes will not be decompiled. {}: {}", th.getClass().getSimpleName(), th.getMessage());
            return null;
        }
    }
    
    private String prepareFilter(String filter) {
        filter = "^\\Q" + filter.replace("**", "\u0081").replace("*", "\u0082").replace("?", "\u0083") + "\\E$";
        return filter.replace("\u0081", "\\E.*\\Q").replace("\u0082", "\\E[^\\.]+\\Q").replace("\u0083", "\\E.\\Q").replace("\\Q\\E", "");
    }
    
    private boolean applyFilter(final String filter, final String subject) {
        return Pattern.compile(this.prepareFilter(filter), 2).matcher(subject).matches();
    }
    
    @Override
    public boolean checkActive(final MixinEnvironment environment) {
        return true;
    }
    
    @Override
    public void preApply(final ITargetClassContext context) {
    }
    
    @Override
    public void postApply(final ITargetClassContext context) {
    }
    
    @Override
    public void export(final MixinEnvironment env, final String name, final boolean force, final ClassNode classNode) {
        if (force || env.getOption(MixinEnvironment.Option.DEBUG_EXPORT)) {
            final String filter = env.getOptionValue(MixinEnvironment.Option.DEBUG_EXPORT_FILTER);
            if (force || filter == null || this.applyFilter(filter, name)) {
                final Profiler.Section exportTimer = Profiler.getProfiler("export").begin("debug.export");
                final File outputFile = this.dumpClass(name.replace('.', '/'), classNode);
                if (this.decompiler != null) {
                    this.decompiler.decompile(outputFile);
                }
                exportTimer.end();
            }
        }
    }
    
    public File dumpClass(final String fileName, final ClassNode classNode) {
        final File outputFile = new File(this.classExportDir, fileName + ".class");
        outputFile.getParentFile().mkdirs();
        try {
            final byte[] bytecode = getClassBytes(classNode, true);
            if (bytecode != null) {
                Files.write(bytecode, outputFile);
            }
        }
        catch (final IOException ex) {}
        return outputFile;
    }
    
    private static byte[] getClassBytes(final ClassNode classNode, final boolean computeFrames) {
        byte[] bytes = null;
        try {
            final MixinClassWriter cw = new MixinClassWriter(computeFrames ? 2 : 0);
            classNode.accept(cw);
            bytes = cw.toByteArray();
        }
        catch (final NegativeArraySizeException ex) {
            if (computeFrames) {
                ExtensionClassExporter.logger.warn("Exporting class {} with COMPUTE_FRAMES failed! Trying a raw export.", classNode.name);
                return getClassBytes(classNode, false);
            }
            ex.printStackTrace();
        }
        catch (final Exception ex2) {
            ex2.printStackTrace();
        }
        return bytes;
    }
    
    static {
        logger = MixinService.getService().getLogger("mixin");
    }
}
