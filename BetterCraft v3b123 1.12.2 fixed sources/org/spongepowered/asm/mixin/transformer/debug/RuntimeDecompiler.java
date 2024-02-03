// 
// Decompiled by Procyon v0.6.0
// 

package org.spongepowered.asm.mixin.transformer.debug;

import java.util.jar.Manifest;
import com.google.common.base.Charsets;
import java.lang.reflect.Method;
import org.jetbrains.java.decompiler.util.InterpreterUtil;
import org.jetbrains.java.decompiler.main.extern.IBytecodeProvider;
import java.net.URL;
import org.spongepowered.asm.util.Files;
import org.jetbrains.java.decompiler.main.Fernflower;
import java.io.IOException;
import com.google.common.io.MoreFiles;
import com.google.common.io.RecursiveDeleteOption;
import org.spongepowered.asm.service.MixinService;
import com.google.common.collect.ImmutableMap;
import org.spongepowered.asm.logging.ILogger;
import java.io.File;
import java.util.Map;
import org.spongepowered.asm.logging.Level;
import org.jetbrains.java.decompiler.main.extern.IResultSaver;
import org.spongepowered.asm.mixin.transformer.ext.IDecompiler;
import org.jetbrains.java.decompiler.main.extern.IFernflowerLogger;

public class RuntimeDecompiler extends IFernflowerLogger implements IDecompiler, IResultSaver
{
    private static final Level[] SEVERITY_LEVELS;
    private final Map<String, Object> options;
    private final File outputPath;
    protected final ILogger logger;
    
    public RuntimeDecompiler(final File outputPath) {
        this.options = (Map<String, Object>)ImmutableMap.builder().put("din", "0").put("rbr", "0").put("dgs", "1").put("asc", "1").put("den", "1").put("hdc", "1").put("ind", "    ").build();
        this.logger = MixinService.getService().getLogger("fernflower");
        this.outputPath = outputPath;
        if (this.outputPath.exists()) {
            try {
                MoreFiles.deleteRecursively(this.outputPath.toPath(), new RecursiveDeleteOption[] { RecursiveDeleteOption.ALLOW_INSECURE });
            }
            catch (final IOException ex) {
                this.logger.debug("Error cleaning output directory: {}", ex.getMessage());
            }
        }
    }
    
    public String toString() {
        try {
            final URL codeSource = Fernflower.class.getProtectionDomain().getCodeSource().getLocation();
            final File file = Files.toFile(codeSource);
            return file.getName();
        }
        catch (final Exception ex) {
            return "unknown source (classpath)";
        }
    }
    
    public void decompile(final File file) {
        try {
            final Fernflower fernflower = new Fernflower((IBytecodeProvider)new IBytecodeProvider() {
                private byte[] byteCode;
                
                public byte[] getBytecode(final String externalPath, final String internalPath) throws IOException {
                    if (this.byteCode == null) {
                        this.byteCode = InterpreterUtil.getBytes(new File(externalPath));
                    }
                    return this.byteCode;
                }
            }, (IResultSaver)this, (Map)this.options, (IFernflowerLogger)this);
            try {
                final Method mdAddSource = fernflower.getClass().getDeclaredMethod("addSource", File.class);
                mdAddSource.invoke(fernflower, file);
            }
            catch (final ReflectiveOperationException ex) {
                fernflower.getStructContext().addSpace(file, true);
            }
            fernflower.decompileContext();
        }
        catch (final Throwable ex2) {
            this.logger.warn("Decompilation error while processing {}", file.getName());
        }
    }
    
    public void saveFolder(final String path) {
    }
    
    public void saveClassFile(final String path, final String qualifiedName, final String entryName, final String content, final int[] mapping) {
        final File file = new File(this.outputPath, qualifiedName + ".java");
        file.getParentFile().mkdirs();
        try {
            this.logger.info("Writing {}", file.getAbsolutePath());
            com.google.common.io.Files.write(content, file, Charsets.UTF_8);
        }
        catch (final IOException ex) {
            this.writeMessage("Cannot write source file " + file, ex);
        }
    }
    
    public void startReadingClass(final String className) {
        this.logger.info("Decompiling {}", className);
    }
    
    public void writeMessage(final String message, final IFernflowerLogger.Severity severity) {
        this.logger.log(RuntimeDecompiler.SEVERITY_LEVELS[severity.ordinal()], message, new Object[0]);
    }
    
    public void writeMessage(final String message, final Throwable t) {
        this.logger.warn("{} {}: {}", message, t.getClass().getSimpleName(), t.getMessage());
    }
    
    public void writeMessage(final String message, final IFernflowerLogger.Severity severity, final Throwable t) {
        this.logger.log(RuntimeDecompiler.SEVERITY_LEVELS[severity.ordinal()], message, (severity == IFernflowerLogger.Severity.ERROR) ? t : null);
    }
    
    public void copyFile(final String source, final String path, final String entryName) {
    }
    
    public void createArchive(final String path, final String archiveName, final Manifest manifest) {
    }
    
    public void saveDirEntry(final String path, final String archiveName, final String entryName) {
    }
    
    public void copyEntry(final String source, final String path, final String archiveName, final String entry) {
    }
    
    public void saveClassEntry(final String path, final String archiveName, final String qualifiedName, final String entryName, final String content) {
    }
    
    public void closeArchive(final String path, final String archiveName) {
    }
    
    static {
        SEVERITY_LEVELS = new Level[] { Level.TRACE, Level.INFO, Level.WARN, Level.ERROR };
    }
}
